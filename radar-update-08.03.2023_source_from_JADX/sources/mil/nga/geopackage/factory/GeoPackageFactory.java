package mil.nga.geopackage.factory;

import android.content.Context;
import com.j256.ormlite.logger.LocalLog;
import mil.nga.geopackage.GeoPackageManager;

public class GeoPackageFactory {
    static {
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "INFO");
    }

    public static GeoPackageManager getManager(Context context) {
        Thread.currentThread().setContextClassLoader(GeoPackageManager.class.getClassLoader());
        return new GeoPackageManagerImpl(context);
    }
}
