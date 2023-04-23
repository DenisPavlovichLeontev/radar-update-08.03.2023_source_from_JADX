package mil.nga.geopackage.property;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeoPackageProperties {
    private static final Logger log = Logger.getLogger(GeoPackageProperties.class.getName());
    private static Properties mProperties;

    public static String getProperty(String str) {
        return getProperty(str, true);
    }

    public static synchronized String getProperty(String str, boolean z) {
        String property;
        synchronized (GeoPackageProperties.class) {
            if (mProperties == null) {
                mProperties = initializeConfigurationProperties();
            }
            property = mProperties.getProperty(str);
            if (property == null) {
                if (z) {
                    throw new RuntimeException("Property not found: " + str);
                }
            }
        }
        return property;
    }

    public static String getProperty(String str, String str2) {
        return getProperty(str, str2, true);
    }

    public static synchronized String getProperty(String str, String str2, boolean z) {
        String property;
        synchronized (GeoPackageProperties.class) {
            property = getProperty(str + PropertyConstants.PROPERTY_DIVIDER + str2, z);
        }
        return property;
    }

    public static int getIntegerProperty(String str) {
        return getIntegerProperty(str, true).intValue();
    }

    public static Integer getIntegerProperty(String str, boolean z) {
        String property = getProperty(str, z);
        if (property != null) {
            return Integer.valueOf(property);
        }
        return null;
    }

    public static int getIntegerProperty(String str, String str2) {
        return getIntegerProperty(str, str2, true).intValue();
    }

    public static Integer getIntegerProperty(String str, String str2, boolean z) {
        return getIntegerProperty(str + PropertyConstants.PROPERTY_DIVIDER + str2, z);
    }

    public static float getFloatProperty(String str) {
        return getFloatProperty(str, true).floatValue();
    }

    public static Float getFloatProperty(String str, boolean z) {
        String property = getProperty(str, z);
        if (property != null) {
            return Float.valueOf(property);
        }
        return null;
    }

    public static boolean getBooleanProperty(String str) {
        return getBooleanProperty(str, true).booleanValue();
    }

    public static Boolean getBooleanProperty(String str, boolean z) {
        String property = getProperty(str, z);
        if (property != null) {
            return Boolean.valueOf(property);
        }
        return null;
    }

    private static Properties initializeConfigurationProperties() {
        Properties properties = new Properties();
        InputStream resourceAsStream = GeoPackageProperties.class.getResourceAsStream("/geopackage.properties");
        if (resourceAsStream != null) {
            try {
                properties.load(resourceAsStream);
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    log.log(Level.WARNING, "Failed to close properties file: geopackage.properties", e);
                }
            } catch (Exception e2) {
                log.log(Level.SEVERE, "Failed to load properties file: geopackage.properties", e2);
                resourceAsStream.close();
            } catch (Throwable th) {
                try {
                    resourceAsStream.close();
                } catch (IOException e3) {
                    log.log(Level.WARNING, "Failed to close properties file: geopackage.properties", e3);
                }
                throw th;
            }
        } else {
            log.log(Level.SEVERE, "Failed to load properties, file not found: geopackage.properties");
        }
        return properties;
    }
}
