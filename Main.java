import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ExperimentTracker tracker = new ExperimentTracker();

    public static void main(String[] args) {
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

    private static void addExperiment() {
        int id = readInt("Experiment id (number): ");
        if (tracker.findById(id) != null) {
            System.out.println("An experiment with this id already exists.");
            return;
        }

        String name = readText("Experiment name: ");
        Model model = readModel();
        String date = readText("Date (e.g. 2026-11-17): ");

        Experiment experiment = new Experiment(id, name, model, date);

        System.out.println("Add parameters (type 'done' as the name to stop):");
        while (true) {
            String parameterName = readText("  Parameter name: ");
            if (parameterName.equalsIgnoreCase("done")) {
                break;
            }
            double parameterValue = readDouble("  Value: ");
            experiment.addParameter(new Parameter(parameterName, parameterValue));
        }

        System.out.println("Add evaluation results (type 'done' as the metric to stop):");
        while (true) {
            String metric = readText("  Metric name: ");
            if (metric.equalsIgnoreCase("done")) {
                break;
            }
            double value = readDouble("  Value: ");
            experiment.addResult(new EvaluationResult(metric, value));
        }

        tracker.addExperiment(experiment);
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

    private static void updateExperiment() {
        int id = readInt("Id of the experiment to update: ");
        if (tracker.findById(id) == null) {
            System.out.println("Experiment not found.");
            return;
        }

        String name = readText("New name: ");
        Model model = readModel();
        String date = readText("New date: ");
        tracker.updateExperiment(id, name, model, date);
        System.out.println("Experiment updated.");
    }

    private static void deleteExperiment() {
        int id = readInt("Id of the experiment to delete: ");
        if (tracker.removeExperiment(id)) {
            System.out.println("Experiment deleted.");
        } else {
            System.out.println("Experiment not found.");
        }
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
