import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ExperimentTracker tracker = new ExperimentTracker();
    private static final Path dataFile = Path.of("experiments.csv").toAbsolutePath();
    private static final ExperimentCsv csv = new ExperimentCsv(dataFile);

    public static void main(String[] args) {
        System.out.println("Data file: " + dataFile);
        try {
            csv.load(tracker);
            runMenu();
        } catch (java.util.NoSuchElementException endOfInput) {
            System.out.println("\nTerminal input ended. Any unfinished entry was discarded.");
        } catch (IOException error) {
            System.err.println("Storage error: " + error.getMessage());
            System.err.println("MLTrack closed. Check the data file before trying again.");
        }
    }

    private static void runMenu() throws IOException {
        int choice;
        do {
            printMenu();
            choice = readInt("Choose an option: ");
            switch (choice) {
                case 1:
                    addExperiment();
                    break;
                case 2:
                    tracker.displayAll();
                    break;
                case 3:
                    searchExperiments();
                    break;
                case 4:
                    updateExperiment();
                    break;
                case 5:
                    deleteExperiment();
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Choose a number from 0 to 5.");
            }
        } while (choice != 0);
    }

    private static void printMenu() {
        System.out.println("\n=== ML Experiment Tracker ===");
        System.out.println("1. Add experiment");
        System.out.println("2. View all experiments");
        System.out.println("3. Search experiments");
        System.out.println("4. Update experiment");
        System.out.println("5. Delete experiment");
        System.out.println("0. Exit");
    }

    private static Model readModel() {
        System.out.println("Model type: 1. Decision Tree   2. Neural Network");
        int type;
        do {
            type = readInt("Choose 1 or 2: ");
        } while (type != 1 && type != 2);

        String modelName = readText("Model name: ");
        if (type == 1) {
            int depth = readInt("Max depth: ");
            return new DecisionTree(modelName, depth);
        } else {
            int layers = readInt("Number of layers: ");
            return new NeuralNetwork(modelName, layers);
        }
    }

    private static void addExperiment() throws IOException {
        int id = readInt("Experiment id (number): ");
        if (tracker.findById(id) != null) {
            System.out.println("An experiment with this id already exists.");
            return;
        }

        String name = readText("Experiment name: ");
        Model model = readModel();
        String date = readWithDefault("Date (YYYY-MM-DD)", LocalDate.now().toString());

        Experiment experiment = new Experiment(id, name, model, date);

        System.out.println("Add model settings such as learning_rate or batch_size.");
        while (true) {
            System.out.print("  Parameter name (Enter or 'done' to continue to results): ");
            String parameterName = scanner.nextLine().trim();
            if (parameterName.isEmpty() || parameterName.equalsIgnoreCase("done")) {
                break;
            }
            double parameterValue = readDouble("  Value: ");
            experiment.addParameter(new Parameter(parameterName, parameterValue));
        }

        System.out.println("Add measured results such as accuracy or precision.");
        while (true) {
            System.out.print("  Metric name (Enter or 'done' to save the experiment): ");
            String metric = scanner.nextLine().trim();
            if (metric.isEmpty() || metric.equalsIgnoreCase("done")) {
                break;
            }
            double value = readDouble("  Value: ");
            experiment.addResult(new EvaluationResult(metric, value));
        }

        tracker.addExperiment(experiment);
        csv.save(tracker);
        System.out.println("Experiment added.");
    }

    private static void searchExperiments() {
        String keyword = readText("Search by experiment or model name: ");
        ArrayList<Experiment> found = tracker.search(keyword);
        if (found.isEmpty()) {
            System.out.println("No experiments found.");
            return;
        }

        for (Experiment e : found) {
            System.out.println(e);
            System.out.println();
        }
    }

    private static void updateExperiment() throws IOException {
        int id = readInt("Id of the experiment to update: ");
        Experiment current = tracker.findById(id);
        if (current == null) {
            System.out.println("Experiment not found.");
            return;
        }

        System.out.println("What do you want to edit? 1. Name  2. Model  3. Date  4. Parameters  5. Results  0. Cancel");
        switch (readInt("Choose a field: ")) {
            case 1 -> current.setName(readWithDefault("Experiment name", current.getName()));
            case 2 -> current.setModel(readModel());
            case 3 -> current.setDate(readWithDefault("Date (YYYY-MM-DD)", current.getDate()));
            case 4 -> {
                int index = selectEntry(current.getParameters());
                if (index < 0) return;
                current.getParameters().get(index).setValue(readDouble("New parameter value: "));
            }
            case 5 -> {
                int index = selectEntry(current.getResults());
                if (index < 0) return;
                current.getResults().get(index).setValue(readDouble("New result value: "));
            }
            case 0 -> { return; }
            default -> {
                System.out.println("Unknown field. No changes saved.");
                return;
            }
        }
        csv.save(tracker);
        System.out.println("Experiment updated.");
    }

    private static int selectEntry(java.util.List<?> entries) {
        if (entries.isEmpty()) {
            System.out.println("There are no entries to edit.");
            return -1;
        }
        for (int i = 0; i < entries.size(); i++) {
            System.out.println((i + 1) + ". " + entries.get(i));
        }
        while (true) {
            int choice = readInt("Choose an entry (0 to cancel): ");
            if (choice >= 0 && choice <= entries.size()) return choice - 1;
            System.out.println("Choose one of the listed numbers.");
        }
    }

    private static void deleteExperiment() throws IOException {
        int id = readInt("Id of the experiment to delete: ");
        Experiment experiment = tracker.findById(id);
        if (experiment == null) {
            System.out.println("Experiment not found.");
            return;
        }
        String answer = readWithDefault("Delete experiment #" + id + " (" + experiment.getName()
                + ")? Type yes to confirm", "no");
        if (!answer.equalsIgnoreCase("yes")) {
            System.out.println("Deletion cancelled.");
            return;
        }
        tracker.removeExperiment(id);
        csv.save(tracker);
        System.out.println("Experiment deleted.");
    }

    private static String readWithDefault(String label, String defaultValue) {
        System.out.print(label + " [Enter for " + defaultValue + "]: ");
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? defaultValue : input;
    }

    private static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Try again.");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number (e.g. 0.92).");
            }
        }
    }
}
