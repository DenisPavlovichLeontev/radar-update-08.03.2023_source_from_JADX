package mil.nga.geopackage.extension.coverage;

import java.util.logging.Level;
import java.util.logging.Logger;

public enum GriddedCoverageEncodingType {
    CENTER("grid-value-is-center"),
    AREA("grid-value-is-area"),
    CORNER("grid-value-is-corner");
    
    private static final Logger logger = null;
    private final String name;

    static {
        logger = Logger.getLogger(GriddedCoverageEncodingType.class.getName());
    }

    private GriddedCoverageEncodingType(String str) {
        this.name = str;
    }

    public String getName() {
        return this.name;
    }

    public static GriddedCoverageEncodingType fromName(String str) {
        GriddedCoverageEncodingType griddedCoverageEncodingType;
        if (str == null) {
            return CENTER;
        }
        GriddedCoverageEncodingType[] values = values();
        int length = values.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                griddedCoverageEncodingType = null;
                break;
            }
            griddedCoverageEncodingType = values[i];
            if (str.equalsIgnoreCase(griddedCoverageEncodingType.getName())) {
                break;
            }
            i++;
        }
        if (griddedCoverageEncodingType != null) {
            return griddedCoverageEncodingType;
        }
        Logger logger2 = logger;
        Level level = Level.WARNING;
        StringBuilder sb = new StringBuilder();
        sb.append("Unsupported ");
        sb.append("GriddedCoverageEncodingType");
        sb.append(": ");
        sb.append(str);
        sb.append(", Defaulting to : ");
        GriddedCoverageEncodingType griddedCoverageEncodingType2 = CENTER;
        sb.append(griddedCoverageEncodingType2.name);
        logger2.log(level, sb.toString());
        return griddedCoverageEncodingType2;
    }
}
