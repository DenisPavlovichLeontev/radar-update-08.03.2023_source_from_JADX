package com.google.android.gms.internal.location;

import java.text.SimpleDateFormat;
import java.util.Locale;
import mil.nga.geopackage.features.columns.GeometryColumns;
import org.osgeo.proj4j.units.AngleFormat;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;

/* compiled from: com.google.android.gms:play-services-location@@21.0.1 */
public final class zzdj {
    private static final SimpleDateFormat zza = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.ROOT);
    private static final SimpleDateFormat zzb = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.ROOT);
    private static final StringBuilder zzc = new StringBuilder(33);

    public static String zza(long j) {
        String sb;
        StringBuilder sb2 = zzc;
        synchronized (sb2) {
            sb2.setLength(0);
            zzb(j, sb2);
            sb = sb2.toString();
        }
        return sb;
    }

    public static void zzb(long j, StringBuilder sb) {
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i == 0) {
            sb.append("0s");
            return;
        }
        sb.ensureCapacity(sb.length() + 27);
        boolean z = false;
        if (i < 0) {
            sb.append("-");
            if (j != Long.MIN_VALUE) {
                j = -j;
            } else {
                j = Long.MAX_VALUE;
                z = true;
            }
        }
        if (j >= OpenStreetMapTileProviderConstants.ONE_DAY) {
            sb.append(j / OpenStreetMapTileProviderConstants.ONE_DAY);
            sb.append("d");
            j %= OpenStreetMapTileProviderConstants.ONE_DAY;
        }
        if (true == z) {
            j = 25975808;
        }
        if (j >= OpenStreetMapTileProviderConstants.ONE_HOUR) {
            sb.append(j / OpenStreetMapTileProviderConstants.ONE_HOUR);
            sb.append("h");
            j %= OpenStreetMapTileProviderConstants.ONE_HOUR;
        }
        if (j >= OpenStreetMapTileProviderConstants.ONE_MINUTE) {
            sb.append(j / OpenStreetMapTileProviderConstants.ONE_MINUTE);
            sb.append(GeometryColumns.COLUMN_M);
            j %= OpenStreetMapTileProviderConstants.ONE_MINUTE;
        }
        if (j >= 1000) {
            sb.append(j / 1000);
            sb.append(AngleFormat.STR_SEC_ABBREV);
            j %= 1000;
        }
        if (j > 0) {
            sb.append(j);
            sb.append("ms");
        }
    }
}
