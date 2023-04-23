package mil.nga.geopackage.core.contents;

public enum ContentsDataType {
    FEATURES("features"),
    TILES("tiles"),
    ATTRIBUTES("attributes"),
    GRIDDED_COVERAGE("2d-gridded-coverage");
    
    private final String name;

    private ContentsDataType(String str) {
        this.name = str;
    }

    public String getName() {
        return this.name;
    }

    public static ContentsDataType fromName(String str) {
        if (str != null) {
            for (ContentsDataType contentsDataType : values()) {
                if (str.equals(contentsDataType.getName())) {
                    return contentsDataType;
                }
            }
        }
        return null;
    }
}
