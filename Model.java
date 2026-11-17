// Parent class: every kind of model has a name and can describe itself.
public abstract class Model {
    protected String name;

    public Model(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Each subclass writes its own version (polymorphism)
    public abstract String getSummary();
}
