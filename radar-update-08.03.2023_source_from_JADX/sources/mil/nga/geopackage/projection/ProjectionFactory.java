package mil.nga.geopackage.projection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.srs.SpatialReferenceSystem;
import org.osgeo.proj4j.CRSFactory;

public class ProjectionFactory {
    private static Map<String, AuthorityProjections> authorities = new HashMap();
    private static final CRSFactory csFactory = new CRSFactory();
    private static final Logger logger = Logger.getLogger(ProjectionFactory.class.getName());

    public static Projection getProjection(long j) {
        return getProjection(ProjectionConstants.AUTHORITY_EPSG, String.valueOf(j));
    }

    public static Projection getProjection(String str, long j) {
        return getProjection(str, String.valueOf(j));
    }

    public static Projection getProjection(String str, String str2) {
        return getProjection(str, str2, (String[]) null, (String) null);
    }

    public static Projection getProjection(String str, long j, String str2) {
        return getProjection(str, String.valueOf(j), str2);
    }

    public static Projection getProjection(String str, String str2, String str3) {
        return getProjection(str, str2, (str3 == null || str3.isEmpty()) ? null : str3.split("\\s+"));
    }

    public static Projection getProjection(String str, long j, String[] strArr) {
        return getProjection(str, String.valueOf(j), strArr);
    }

    public static Projection getProjection(String str, String str2, String[] strArr) {
        return getProjection(str, str2, strArr, (String) null);
    }

    public static Projection getProjection(String str, long j, String[] strArr, String str2) {
        return getProjection(str, String.valueOf(j), strArr, str2);
    }

    public static Projection getProjection(String str, String str2, String[] strArr, String str3) {
        AuthorityProjections projections = getProjections(str);
        Projection projection = projections.getProjection(str2);
        if (projection != null || (projection = fromDefinition(projections, str2, str3)) != null || (projection = fromParams(projections, str2, strArr)) != null || (projection = fromProperties(projections, str2)) != null || (projection = fromName(projections, str2)) != null) {
            return projection;
        }
        throw new GeoPackageException("Failed to create projection for authority: " + str + ", code: " + str2 + ", definition: " + str3 + ", params: " + Arrays.toString(strArr));
    }

    public static AuthorityProjections getProjections(String str) {
        AuthorityProjections authorityProjections = authorities.get(str.toUpperCase());
        if (authorityProjections != null) {
            return authorityProjections;
        }
        AuthorityProjections authorityProjections2 = new AuthorityProjections(str);
        authorities.put(str.toUpperCase(), authorityProjections2);
        return authorityProjections2;
    }

    public static void clear() {
        authorities.clear();
    }

    public static void clear(String str) {
        getProjections(str).clear();
    }

    public static void clear(String str, long j) {
        getProjections(str).clear(j);
    }

    public static void clear(String str, String str2) {
        getProjections(str).clear(str2);
    }

    private static Projection fromDefinition(AuthorityProjections authorityProjections, String str, String str2) {
        if (str2 == null) {
            return null;
        }
        str2.isEmpty();
        return null;
    }

    private static Projection fromParams(AuthorityProjections authorityProjections, String str, String[] strArr) {
        Projection projection;
        Exception e;
        if (strArr == null || strArr.length <= 0) {
            return null;
        }
        try {
            projection = new Projection(authorityProjections.getAuthority(), str, csFactory.createFromParameters(coordinateName(authorityProjections.getAuthority(), str), strArr));
            try {
                authorityProjections.addProjection(projection);
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Exception e3) {
            projection = null;
            e = e3;
            logger.log(Level.WARNING, "Failed to create projection for authority: " + authorityProjections.getAuthority() + ", code: " + str + ", parameters: " + Arrays.toString(strArr), e);
            return projection;
        }
        return projection;
    }

    private static Projection fromProperties(AuthorityProjections authorityProjections, String str) {
        Projection projection;
        Exception e;
        String projection2 = ProjectionRetriever.getProjection(authorityProjections.getAuthority(), str);
        if (projection2 == null || projection2.isEmpty()) {
            return null;
        }
        try {
            projection = new Projection(authorityProjections.getAuthority(), str, csFactory.createFromParameters(coordinateName(authorityProjections.getAuthority(), str), projection2));
            try {
                authorityProjections.addProjection(projection);
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Exception e3) {
            projection = null;
            e = e3;
            logger.log(Level.WARNING, "Failed to create projection for authority: " + authorityProjections.getAuthority() + ", code: " + str + ", parameters: " + projection2, e);
            return projection;
        }
        return projection;
    }

    private static Projection fromName(AuthorityProjections authorityProjections, String str) {
        String coordinateName = coordinateName(authorityProjections.getAuthority(), str);
        Projection projection = null;
        try {
            Projection projection2 = new Projection(authorityProjections.getAuthority(), str, csFactory.createFromName(coordinateName));
            try {
                authorityProjections.addProjection(projection2);
                return projection2;
            } catch (Exception e) {
                e = e;
                projection = projection2;
            }
        } catch (Exception e2) {
            e = e2;
            Logger logger2 = logger;
            Level level = Level.WARNING;
            logger2.log(level, "Failed to create projection from name: " + coordinateName, e);
            return projection;
        }
    }

    private static String coordinateName(String str, String str2) {
        return str.toUpperCase() + ":" + str2;
    }

    public static Projection getProjection(SpatialReferenceSystem spatialReferenceSystem) {
        String organization = spatialReferenceSystem.getOrganization();
        long organizationCoordsysId = spatialReferenceSystem.getOrganizationCoordsysId();
        String definition_12_063 = spatialReferenceSystem.getDefinition_12_063();
        if (definition_12_063 == null) {
            definition_12_063 = spatialReferenceSystem.getDefinition();
        }
        return getProjection(organization, organizationCoordsysId, (String[]) null, definition_12_063);
    }
}
