import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** Keeps experiment records by ID for the entire time of this repository. */
public class InMemoryExperimentRepository {
    private final Map<Integer, Experiment> experiments = new HashMap<>();

    public boolean add(Experiment experiment) {
        if (experiment == null || experiments.containsKey(experiment.getId())) {
            return false;
        }
        experiments.put(experiment.getId(), experiment);
        return true;
    }

    /** Returns null value when the ID is missing. */
    public Experiment findById(int id) {
        return experiments.get(id);
    }

    public ArrayList<Experiment> getAll() {
        return new ArrayList<>(experiments.values());
    }

    /** Replaces an existing record with the complete edited experiment. */
    public boolean update(Experiment experiment) {
        if (experiment == null || !experiments.containsKey(experiment.getId())) {
            return false;
        }
        experiments.put(experiment.getId(), experiment);
        return true;
    }

    public boolean delete(int id) {
        return experiments.remove(id) != null;
    }
}
