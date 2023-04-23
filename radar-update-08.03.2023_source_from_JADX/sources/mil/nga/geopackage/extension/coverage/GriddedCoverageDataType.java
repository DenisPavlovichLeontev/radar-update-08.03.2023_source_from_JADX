package mil.nga.geopackage.extension.coverage;

import androidx.constraintlayout.core.motion.utils.TypedValues;

public enum GriddedCoverageDataType {
    INTEGER(TypedValues.Custom.S_INT),
    FLOAT(TypedValues.Custom.S_FLOAT);
    
    private final String name;

    private GriddedCoverageDataType(String str) {
        this.name = str;
    }

    public String getName() {
        return this.name;
    }

    public static GriddedCoverageDataType fromName(String str) {
        if (str != null) {
            for (GriddedCoverageDataType griddedCoverageDataType : values()) {
                if (str.equals(griddedCoverageDataType.getName())) {
                    return griddedCoverageDataType;
                }
            }
        }
        return null;
    }
}
