package mil.nga.geopackage.schema.constraints;

import java.util.Locale;

public enum DataColumnConstraintType {
    RANGE,
    ENUM,
    GLOB;
    
    private final String value;

    public String getValue() {
        return this.value;
    }

    public static DataColumnConstraintType fromValue(String str) {
        return valueOf(str.toUpperCase(Locale.US));
    }
}
