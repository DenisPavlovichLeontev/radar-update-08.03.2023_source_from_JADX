package mil.nga.geopackage.user;

public class ColumnValue {
    private final Double tolerance;
    private final Object value;

    public ColumnValue(Object obj) {
        this(obj, (Double) null);
    }

    public ColumnValue(Object obj, Double d) {
        this.value = obj;
        this.tolerance = d;
    }

    public Object getValue() {
        return this.value;
    }

    public Double getTolerance() {
        return this.tolerance;
    }
}
