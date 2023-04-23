package mil.nga.geopackage.metadata;

import androidx.core.app.NotificationCompat;
import java.util.Locale;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.SchemaExtension;
import org.osmdroid.tileprovider.modules.DatabaseFileArchive;

public enum MetadataScopeType {
    UNDEFINED("undefined", "NA", "Metadata information scope is undefined"),
    FIELD_SESSION("fieldSession", "012", "Information applies to the field session"),
    COLLECTION_SESSION("collectionSession", "004", "Information applies to the collection session"),
    SERIES("series", "006", "Information applies to the (dataset) series"),
    DATASET("dataset", "005", "Information applies to the (geographic feature) dataset"),
    FEATURE_TYPE("featureType", "010", "Information applies to a feature type (class)"),
    FEATURE("feature", "009", "Information applies to a feature (instance)"),
    ATTRIBUTE_TYPE("attributeType", "002", "Information applies to the attribute class"),
    ATTRIBUTE("attribute", "001", "Information applies to the characteristic of a feature (instance)"),
    TILE(DatabaseFileArchive.COLUMN_TILE, "016", "Information applies to a tile, a spatial subset of geographic data"),
    MODEL("model", "015", "Information applies to a copy or imitation of an existing or hypothetical object"),
    CATALOG("catalog", "NA", "Metadata applies to a feature catalog"),
    SCHEMA(SchemaExtension.NAME, "NA", "Metadata applies to an application schema"),
    TAXONOMY("taxonomy", "NA", "Metadata applies to a taxonomy or knowledge system"),
    SOFTWARE("software", "013", "Information applies to a computer program or routine"),
    SERVICE(NotificationCompat.CATEGORY_SERVICE, "014", "Information applies to a capability which a service provider entity makes available to a service user entity through a set of interfaces that define a behaviour, such as a use case"),
    COLLECTION_HARDWARE("collectionHardware", "003", "Information applies to the collection hardware class"),
    NON_GEOGRAPHIC_DATASET("nonGeographicDataset", "007", "Information applies to non-geographic data"),
    DIMENSION_GROUP("dimensionGroup", "008", "Information applies to a dimension group");
    
    private final String code;
    private final String definition;
    private final String name;

    private MetadataScopeType(String str, String str2, String str3) {
        this.name = str;
        this.code = str2;
        this.definition = str3;
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public String getDefinition() {
        return this.definition;
    }

    public static MetadataScopeType fromName(String str) {
        StringBuilder sb = new StringBuilder();
        for (String str2 : str.split("(?<!^)(?=[A-Z])")) {
            if (sb.length() > 0) {
                sb.append(Extensions.EXTENSION_NAME_DIVIDER);
            }
            sb.append(str2.toUpperCase(Locale.US));
        }
        return valueOf(sb.toString());
    }
}
