package mil.nga.geopackage.extension.link;

public class FeatureTileLinkKey {
    private String featureTableName;
    private String tileTableName;

    public FeatureTileLinkKey(String str, String str2) {
        this.featureTableName = str;
        this.tileTableName = str2;
    }

    public String getFeatureTableName() {
        return this.featureTableName;
    }

    public void setFeatureTableName(String str) {
        this.featureTableName = str;
    }

    public String getTileTableName() {
        return this.tileTableName;
    }

    public void setTileTableName(String str) {
        this.tileTableName = str;
    }

    public String toString() {
        return this.featureTableName + "-" + this.tileTableName;
    }

    public int hashCode() {
        int i;
        String str = this.featureTableName;
        int i2 = 0;
        if (str == null) {
            i = 0;
        } else {
            i = str.hashCode();
        }
        int i3 = (i + 31) * 31;
        String str2 = this.tileTableName;
        if (str2 != null) {
            i2 = str2.hashCode();
        }
        return i3 + i2;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        FeatureTileLinkKey featureTileLinkKey = (FeatureTileLinkKey) obj;
        String str = this.featureTableName;
        if (str == null) {
            if (featureTileLinkKey.featureTableName != null) {
                return false;
            }
        } else if (!str.equals(featureTileLinkKey.featureTableName)) {
            return false;
        }
        String str2 = this.tileTableName;
        if (str2 == null) {
            if (featureTileLinkKey.tileTableName != null) {
                return false;
            }
        } else if (!str2.equals(featureTileLinkKey.tileTableName)) {
            return false;
        }
        return true;
    }
}
