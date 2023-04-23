package mil.nga.geopackage.extension;

import java.util.Locale;

public enum ExtensionScopeType {
    READ_WRITE("read-write"),
    WRITE_ONLY("write-only");
    
    private final String value;

    private ExtensionScopeType(String str) {
        this.value = str;
    }

    public String getValue() {
        return this.value;
    }

    public static ExtensionScopeType fromValue(String str) {
        return valueOf(str.replace("-", Extensions.EXTENSION_NAME_DIVIDER).toUpperCase(Locale.US));
    }
}
