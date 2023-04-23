package mil.nga.geopackage.p009db;

import java.util.Locale;

/* renamed from: mil.nga.geopackage.db.GeoPackageDataType */
public enum GeoPackageDataType {
    BOOLEAN(Boolean.class),
    TINYINT(Byte.class),
    SMALLINT(Short.class),
    MEDIUMINT(Integer.class),
    INT(Long.class),
    INTEGER(Long.class),
    FLOAT(Float.class),
    DOUBLE(Double.class),
    REAL(Double.class),
    TEXT(String.class),
    BLOB(byte[].class),
    DATE(String.class),
    DATETIME(String.class);
    
    private final Class<?> classType;

    private GeoPackageDataType(Class<?> cls) {
        this.classType = cls;
    }

    public Class<?> getClassType() {
        return this.classType;
    }

    public static GeoPackageDataType fromName(String str) {
        return valueOf(str.toUpperCase(Locale.US));
    }
}
