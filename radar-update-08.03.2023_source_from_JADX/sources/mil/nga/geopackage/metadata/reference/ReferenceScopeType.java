package mil.nga.geopackage.metadata.reference;

import java.util.Locale;
import mil.nga.geopackage.extension.Extensions;

public enum ReferenceScopeType {
    GEOPACKAGE("geopackage"),
    TABLE("table"),
    COLUMN("column"),
    ROW("row"),
    ROW_COL("row/col");
    
    private final String value;

    private ReferenceScopeType(String str) {
        this.value = str;
    }

    public String getValue() {
        return this.value;
    }

    public static ReferenceScopeType fromValue(String str) {
        return valueOf(str.replace("/", Extensions.EXTENSION_NAME_DIVIDER).toUpperCase(Locale.US));
    }
}
