public class EvaluationResult {
    private String metricName;
    private double value;

    public EvaluationResult(String metricName, double value) {
        this.metricName = metricName;
        this.value = value;
    }

    public String getMetricName() {
        return metricName;
    }

    public double getValue() {
        return value;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return metricName + ": " + value;
    }
}
