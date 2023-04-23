package mil.nga.geopackage.extension.index;

public class GeometryIndexKey {
    private long geomId;
    private String tableName;

    public GeometryIndexKey(String str, long j) {
        this.tableName = str;
        this.geomId = j;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String str) {
        this.tableName = str;
    }

    public long getGeomId() {
        return this.geomId;
    }

    public void setGeomId(long j) {
        this.geomId = j;
    }

    public String toString() {
        return this.tableName + ":" + this.geomId;
    }

    public int hashCode() {
        int i;
        long j = this.geomId;
        int i2 = (((int) (j ^ (j >>> 32))) + 31) * 31;
        String str = this.tableName;
        if (str == null) {
            i = 0;
        } else {
            i = str.hashCode();
        }
        return i2 + i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        GeometryIndexKey geometryIndexKey = (GeometryIndexKey) obj;
        if (this.geomId != geometryIndexKey.geomId) {
            return false;
        }
        String str = this.tableName;
        if (str == null) {
            if (geometryIndexKey.tableName != null) {
                return false;
            }
        } else if (!str.equals(geometryIndexKey.tableName)) {
            return false;
        }
        return true;
    }
}
