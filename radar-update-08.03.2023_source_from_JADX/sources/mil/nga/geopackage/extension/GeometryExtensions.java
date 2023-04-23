package mil.nga.geopackage.extension;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.wkb.geom.GeometryType;

public class GeometryExtensions extends BaseExtension {
    public static final String GEOMETRY_TYPES_EXTENSION_DEFINITION = GeoPackageProperties.getProperty(PropertyConstants.EXTENSIONS, "geometry_types");
    public static final String USER_GEOMETRY_TYPES_EXTENSION_DEFINITION = GeoPackageProperties.getProperty(PropertyConstants.EXTENSIONS, "user_geometry_types");

    public GeometryExtensions(GeoPackageCore geoPackageCore) {
        super(geoPackageCore);
    }

    public Extensions getOrCreate(String str, String str2, GeometryType geometryType) {
        return getOrCreate(getExtensionName(geometryType), str, str2, GEOMETRY_TYPES_EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE);
    }

    public boolean has(String str, String str2, GeometryType geometryType) {
        return has(getExtensionName(geometryType), str, str2);
    }

    public static boolean isExtension(GeometryType geometryType) {
        return geometryType.getCode() > GeometryType.GEOMETRYCOLLECTION.getCode();
    }

    public static boolean isNonStandard(GeometryType geometryType) {
        return geometryType.getCode() > GeometryType.SURFACE.getCode();
    }

    public static boolean isGeoPackageExtension(GeometryType geometryType) {
        return geometryType.getCode() >= GeometryType.CIRCULARSTRING.getCode() && geometryType.getCode() <= GeometryType.SURFACE.getCode();
    }

    public static String getExtensionName(GeometryType geometryType) {
        if (!isExtension(geometryType)) {
            StringBuilder sb = new StringBuilder();
            Class<GeometryType> cls = GeometryType.class;
            sb.append("GeometryType");
            sb.append(" is not an extension: ");
            sb.append(geometryType.getName());
            throw new GeoPackageException(sb.toString());
        } else if (isGeoPackageExtension(geometryType)) {
            return "gpkg_geom_" + geometryType.getName();
        } else {
            StringBuilder sb2 = new StringBuilder();
            Class<GeometryType> cls2 = GeometryType.class;
            sb2.append("GeometryType");
            sb2.append(" is not a GeoPackage extension, User-Defined requires an author: ");
            sb2.append(geometryType.getName());
            throw new GeoPackageException(sb2.toString());
        }
    }

    public Extensions getOrCreate(String str, String str2, String str3, GeometryType geometryType) {
        return getOrCreate(getExtensionName(str3, geometryType), str, str2, isGeoPackageExtension(geometryType) ? GEOMETRY_TYPES_EXTENSION_DEFINITION : USER_GEOMETRY_TYPES_EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE);
    }

    public boolean has(String str, String str2, String str3, GeometryType geometryType) {
        return has(getExtensionName(str3, geometryType), str, str2);
    }

    public static String getExtensionName(String str, GeometryType geometryType) {
        if (isExtension(geometryType)) {
            StringBuilder sb = new StringBuilder();
            if (isGeoPackageExtension(geometryType)) {
                str = "gpkg";
            }
            sb.append(str);
            sb.append(Extensions.EXTENSION_NAME_DIVIDER);
            sb.append(GeoPackageConstants.GEOMETRY_EXTENSION_PREFIX);
            sb.append(Extensions.EXTENSION_NAME_DIVIDER);
            sb.append(geometryType.getName());
            return sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        Class<GeometryType> cls = GeometryType.class;
        sb2.append("GeometryType");
        sb2.append(" is not an extension: ");
        sb2.append(geometryType.getName());
        throw new GeoPackageException(sb2.toString());
    }
}
