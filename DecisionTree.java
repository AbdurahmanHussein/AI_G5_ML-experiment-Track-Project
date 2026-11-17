public class DecisionTree extends Model {
    private int maxDepth;

    public DecisionTree(String name, int maxDepth) {
        super(name);
        this.maxDepth = maxDepth;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override
    public String getSummary() {
        return name + " (Decision Tree, max depth = " + maxDepth + ")";
    }
}
