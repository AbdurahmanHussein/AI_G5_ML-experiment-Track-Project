public class NeuralNetwork extends Model {
    private int layers;

    public NeuralNetwork(String name, int layers) {
        super(name);
        this.layers = layers;
    }

    public int getLayers() {
        return layers;
    }

    public void setLayers(int layers) {
        this.layers = layers;
    }

    @Override
    public String getSummary() {
        return name + " (Neural Network, layers = " + layers + ")";
    }
}
