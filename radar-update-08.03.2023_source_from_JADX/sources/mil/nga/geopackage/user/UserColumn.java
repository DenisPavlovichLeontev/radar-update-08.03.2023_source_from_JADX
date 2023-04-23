package mil.nga.geopackage.user;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.p009db.GeoPackageDataType;

public abstract class UserColumn implements Comparable<UserColumn> {
    private final GeoPackageDataType dataType;
    private final Object defaultValue;
    private final int index;
    private final Long max;
    private final String name;
    private final boolean notNull;
    private final boolean primaryKey;

    protected UserColumn(int i, String str, GeoPackageDataType geoPackageDataType, Long l, boolean z, Object obj, boolean z2) {
        this.index = i;
        this.name = str;
        this.max = l;
        this.notNull = z;
        this.defaultValue = obj;
        this.primaryKey = z2;
        this.dataType = geoPackageDataType;
        validateMax();
    }

    public int getIndex() {
        return this.index;
    }

    public String getName() {
        return this.name;
    }

    public Long getMax() {
        return this.max;
    }

    public boolean isNotNull() {
        return this.notNull;
    }

    public Object getDefaultValue() {
        return this.defaultValue;
    }

    public boolean isPrimaryKey() {
        return this.primaryKey;
    }

    public GeoPackageDataType getDataType() {
        return this.dataType;
    }

    public String getTypeName() {
        GeoPackageDataType geoPackageDataType = this.dataType;
        if (geoPackageDataType != null) {
            return geoPackageDataType.name();
        }
        return null;
    }

    public int compareTo(UserColumn userColumn) {
        return this.index - userColumn.index;
    }

    private void validateMax() {
        if (this.max != null) {
            GeoPackageDataType geoPackageDataType = this.dataType;
            if (geoPackageDataType == null) {
                throw new GeoPackageException("Column max is only supported for data typed columns. column: " + this.name + ", max: " + this.max);
            } else if (geoPackageDataType != GeoPackageDataType.TEXT && this.dataType != GeoPackageDataType.BLOB) {
                throw new GeoPackageException("Column max is only supported for " + GeoPackageDataType.TEXT.name() + " and " + GeoPackageDataType.BLOB.name() + " columns. column: " + this.name + ", max: " + this.max + ", type: " + this.dataType.name());
            }
        }
    }
}
