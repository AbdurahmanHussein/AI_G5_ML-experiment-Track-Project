import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.function.BiConsumer;

/** One CSV row per experiment. The console accepts single-line text fields. */
final class ExperimentCsv {
    private static final String HEADER =
        "Experiment ID,Experiment name,Model type,Model name,Max depth,Layers,Date,Parameters,Results";
    private final Path file;

    ExperimentCsv(Path file) {
        this.file = file.toAbsolutePath();
    }

    void load(ExperimentTracker tracker) throws IOException {
        if (Files.notExists(file)) return;
        try (BufferedReader input = Files.newBufferedReader(file)) {
            if (!HEADER.equals(input.readLine())) throw new IOException("Unexpected CSV header");
            String line;
            int row = 1;
            while ((line = input.readLine()) != null) {
                row++;
                try {
                    Experiment experiment = decode(splitRow(line));
                    if (!tracker.addExperiment(experiment)) throw new IllegalArgumentException("Duplicate ID");
                } catch (IllegalArgumentException error) {
                    throw new IOException("Invalid CSV row " + row + ": " + error.getMessage(), error);
                }
            }
        }
    }

    void save(ExperimentTracker tracker) throws IOException {
        Path temp = Files.createTempFile(file.getParent(), "mltrack-", ".tmp");
        try {
            try (BufferedWriter out = Files.newBufferedWriter(temp)) {
                out.write(HEADER);
                out.newLine();
                for (Experiment experiment : tracker.getAll()) {
                    out.write(encode(experiment));
                    out.newLine();
                }
            }
            Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } finally {
            Files.deleteIfExists(temp);
        }
    }

    private static String encode(Experiment e) {
        Model model = e.getModel();
        String type, depth = "", layers = "";
        if (model instanceof DecisionTree tree) {
            type = "Decision Tree";
            depth = Integer.toString(tree.getMaxDepth());
        } else if (model instanceof NeuralNetwork network) {
            type = "Neural Network";
            layers = Integer.toString(network.getLayers());
        } else {
            throw new IllegalArgumentException("Unknown model type");
        }
        StringJoiner parameters = new StringJoiner(";");
        for (Parameter p : e.getParameters()) parameters.add(pair(p.getName(), p.getValue()));
        StringJoiner results = new StringJoiner(";");
        for (EvaluationResult r : e.getResults()) results.add(pair(r.getMetricName(), r.getValue()));
        StringJoiner row = new StringJoiner(",");
        for (String field : List.of(Integer.toString(e.getId()), e.getName(), type, model.getName(),
                depth, layers, e.getDate(), parameters.toString(), results.toString())) {
            row.add(field.contains(",") || field.contains("\"") ?
                    "\"" + field.replace("\"", "\"\"") + "\"" : field);
        }
        return row.toString();
    }

    private static Experiment decode(List<String> f) {
        if (f.size() != 9) throw new IllegalArgumentException("Expected nine fields");
        Model model = switch (f.get(2)) {
            case "Decision Tree" -> new DecisionTree(f.get(3), Integer.parseInt(f.get(4)));
            case "Neural Network" -> new NeuralNetwork(f.get(3), Integer.parseInt(f.get(5)));
            default -> throw new IllegalArgumentException("Unknown model type");
        };
        Experiment e = new Experiment(Integer.parseInt(f.get(0)), f.get(1), model, f.get(6));
        readPairs(f.get(7), (name, value) -> e.addParameter(new Parameter(name, value)));
        readPairs(f.get(8), (name, value) -> e.addResult(new EvaluationResult(name, value)));
        return e;
    }

    // Escape separators in names so metric names such as "top;k" can round-trip.
    private static String pair(String name, double value) {
        return URLEncoder.encode(name, StandardCharsets.UTF_8) + "=" + value;
    }

    private static void readPairs(String cell, BiConsumer<String, Double> accept) {
        if (cell.isEmpty()) return;
        for (String entry : cell.split(";", -1)) {
            int separator = entry.indexOf('=');
            if (separator < 1) throw new IllegalArgumentException("Expected name=value");
            String name = URLDecoder.decode(entry.substring(0, separator), StandardCharsets.UTF_8);
            accept.accept(name, Double.parseDouble(entry.substring(separator + 1)));
        }
    }

    private static List<String> splitRow(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted = false;
        boolean closed = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (quoted && c == '"' && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                field.append('"');
                i++;
            } else if (quoted && c == '"') {
                quoted = false;
                closed = true;
            } else if (!quoted && c == ',') {
                fields.add(field.toString());
                field.setLength(0);
                closed = false;
            } else if (!quoted && c == '"' && field.length() == 0 && !closed) {
                quoted = true;
            } else if (closed || (!quoted && c == '"')) {
                throw new IllegalArgumentException("Unexpected character in CSV field");
            } else {
                field.append(c);
            }
        }
        if (quoted) throw new IllegalArgumentException("Unclosed CSV quote");
        fields.add(field.toString());
        return fields;
    }
}
