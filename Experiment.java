import java.util.ArrayList;

public class Experiment {
    private int id;
    private String name;
    private Model model;
    private String date;
    private ArrayList<Parameter> parameters;
    private ArrayList<EvaluationResult> results;

    public Experiment(int id, String name, Model model, String date) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.date = date;
        this.parameters = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    public void addParameter(Parameter p) {
        parameters.add(p);
    }

    public void addResult(EvaluationResult r) {
        results.add(r);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Model getModel() {
        return model;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    public ArrayList<EvaluationResult> getResults() {
        return results;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Experiment #").append(id).append(": ").append(name)
          .append("\n  Model: ").append(model.getSummary())
          .append("\n  Date: ").append(date)
          .append("\n  Parameters:");
        for (Parameter p : parameters) {
            sb.append("\n    - ").append(p);
        }
        sb.append("\n  Results:");
        for (EvaluationResult r : results) {
            sb.append("\n    - ").append(r);
        }
        return sb.toString();
    }
}
