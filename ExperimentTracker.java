import java.util.ArrayList;
import java.util.HashMap;

public class ExperimentTracker {
    private HashMap<Integer, Experiment> experiments;

    public ExperimentTracker() {
        this.experiments = new HashMap<>();
    }

    public boolean addExperiment(Experiment experiment) {
        if (experiments.containsKey(experiment.getId())) {
            return false;
        }
        experiments.put(experiment.getId(), experiment);
        return true;
    }

    public ArrayList<Experiment> getAll() {
        return new ArrayList<>(experiments.values());
    }

    public void displayAll() {
        ArrayList<Experiment> all = getAll();
        if (all.isEmpty()) {
            System.out.println("No experiments available.");
            return;
        }

        for (Experiment e : all) {
            System.out.println(e);
            System.out.println();
        }
    }

    public ArrayList<Experiment> search(String keyword) {
        ArrayList<Experiment> found = new ArrayList<>();
        String target = keyword.trim().toLowerCase();

        if (target.isEmpty()) {
            return found;
        }

        for (Experiment e : experiments.values()) {
            String name = e.getName().toLowerCase();
            String modelName = e.getModel().getName().toLowerCase();
            if (name.contains(target) || modelName.contains(target)) {
                found.add(e);
            }
        }
        return found;
    }

    public Experiment findById(int id) {
        return experiments.get(id);
    }

    public boolean updateExperiment(int id, String name, Model model, String date) {
        Experiment existing = experiments.get(id);
        if (existing == null) {
            return false;
        }

        existing.setName(name);
        existing.setModel(model);
        existing.setDate(date);
        return true;
    }

    public boolean removeExperiment(int id) {
        return experiments.remove(id) != null;
    }
}
