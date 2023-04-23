package org.osmdroid.util;

import android.location.Location;
import android.location.LocationManager;
import org.osmdroid.config.Configuration;

public class LocationUtils {
    private LocationUtils() {
    }

    public static Location getLastKnownLocation(LocationManager locationManager) {
        if (locationManager == null) {
            return null;
        }
        Location lastKnownLocation = getLastKnownLocation(locationManager, "gps");
        Location lastKnownLocation2 = getLastKnownLocation(locationManager, "network");
        if (lastKnownLocation == null) {
            return lastKnownLocation2;
        }
        return (lastKnownLocation2 != null && lastKnownLocation2.getTime() > lastKnownLocation.getTime() + Configuration.getInstance().getGpsWaitTime()) ? lastKnownLocation2 : lastKnownLocation;
    }

    private static Location getLastKnownLocation(LocationManager locationManager, String str) {
        try {
            if (!locationManager.isProviderEnabled(str)) {
                return null;
            }
            return locationManager.getLastKnownLocation(str);
        } catch (IllegalArgumentException unused) {
            return null;
        }
    }
}
