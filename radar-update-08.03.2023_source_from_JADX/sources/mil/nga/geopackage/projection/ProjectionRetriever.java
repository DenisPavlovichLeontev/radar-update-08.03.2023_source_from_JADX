package mil.nga.geopackage.projection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import mil.nga.geopackage.property.PropertyConstants;

public class ProjectionRetriever {
    public static final String PROJECTIONS_PROPERTY_FILE_PREFIX = "projections";
    public static final String PROJECTIONS_PROPERTY_FILE_SUFFIX = "properties";
    private static final Logger log = Logger.getLogger(ProjectionRetriever.class.getName());
    private static final Map<String, Properties> properties = new HashMap();

    public static String getProjection(long j) {
        return getProjection(ProjectionConstants.AUTHORITY_EPSG, j);
    }

    public static String getProjection(String str, long j) {
        return getProjection(str, String.valueOf(j));
    }

    public static String getProjection(String str, String str2) {
        return getOrCreateProjections(str).getProperty(str2);
    }

    public static Properties getOrCreateProjections(String str) {
        String lowerCase = str.toLowerCase();
        Map<String, Properties> map = properties;
        Properties properties2 = map.get(lowerCase);
        if (properties2 != null) {
            return properties2;
        }
        loadProperties(lowerCase);
        return map.get(lowerCase);
    }

    public static Properties getProjections(String str) {
        return properties.get(str.toLowerCase());
    }

    public static void clear() {
        properties.clear();
    }

    public static void clear(String str) {
        properties.remove(str.toLowerCase());
    }

    public static void clear(String str, long j) {
        clear(str, String.valueOf(j));
    }

    public static void clear(String str, String str2) {
        Properties projections = getProjections(str);
        if (projections != null) {
            projections.remove(str2);
        }
    }

    private static void loadProperties(String str) {
        String propertyFileName = propertyFileName(str);
        setProjections(str, ProjectionRetriever.class.getResourceAsStream("/" + propertyFileName));
    }

    public static String propertyFileName(String str) {
        return "projections." + str.toLowerCase() + PropertyConstants.PROPERTY_DIVIDER + PROJECTIONS_PROPERTY_FILE_SUFFIX;
    }

    public static void setProjections(String str, InputStream inputStream) {
        Logger logger;
        Level level;
        StringBuilder sb;
        Properties properties2 = new Properties();
        if (inputStream != null) {
            try {
                properties2.load(inputStream);
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e = e;
                    logger = log;
                    level = Level.WARNING;
                    sb = new StringBuilder();
                }
            } catch (Exception e2) {
                Logger logger2 = log;
                Level level2 = Level.WARNING;
                logger2.log(level2, "Failed to load authority: " + str, e2);
                try {
                    inputStream.close();
                } catch (IOException e3) {
                    e = e3;
                    logger = log;
                    level = Level.WARNING;
                    sb = new StringBuilder();
                }
            } catch (Throwable th) {
                try {
                    inputStream.close();
                } catch (IOException e4) {
                    Logger logger3 = log;
                    Level level3 = Level.WARNING;
                    logger3.log(level3, "Failed to close authority: " + str, e4);
                }
                throw th;
            }
        } else {
            Logger logger4 = log;
            Level level4 = Level.WARNING;
            logger4.log(level4, "Failed to load authority: " + str);
        }
        setProjections(str, properties2);
        sb.append("Failed to close authority: ");
        sb.append(str);
        logger.log(level, sb.toString(), e);
        setProjections(str, properties2);
    }

    public static void setProjections(String str, Properties properties2) {
        properties.put(str.toLowerCase(), properties2);
    }

    public static void setProjections(String str, File file) throws FileNotFoundException {
        setProjections(str, (InputStream) new FileInputStream(file));
    }

    public static void setProjection(String str, long j, String str2) {
        setProjection(str, String.valueOf(j), str2);
    }

    public static void setProjection(String str, String str2, String str3) {
        getOrCreateProjections(str).setProperty(str2, str3);
    }
}
