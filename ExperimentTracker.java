import java.util.ArrayList;

public class ExperimentTracker {
    private final InMemoryExperimentRepository repository = new InMemoryExperimentRepository();

    public boolean addExperiment(Experiment experiment) {
        return repository.add(experiment);
    }

    public ArrayList<Experiment> getAll() {
        return repository.getAll();
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

        for (Experiment e : repository.getAll()) {
            String name = e.getName().toLowerCase();
            String modelName = e.getModel().getName().toLowerCase();
            if (name.contains(target) || modelName.contains(target)) {
                found.add(e);
            }
        }
        return found;
    }

    public Experiment findById(int id) {
        return repository.findById(id);
    }

    public boolean updateExperiment(int id, String name, Model model, String date) {
        Experiment existing = repository.findById(id);
        if (existing == null) {
            return false;
        }

        existing.setName(name);
        existing.setModel(model);
        existing.setDate(date);
        return repository.update(existing);
    }

    public boolean removeExperiment(int id) {
        return repository.delete(id);
    }
}
