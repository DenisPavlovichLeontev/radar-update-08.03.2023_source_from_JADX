package org.osgeo.proj4j.parser;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import org.osgeo.proj4j.UnsupportedParameterException;

public class Proj4Keyword {

    /* renamed from: R */
    public static final String f419R = "R";
    public static final String R_A = "R_A";
    public static final String R_V = "R_V";
    public static final String R_a = "R_a";
    public static final String R_g = "R_g";
    public static final String R_h = "R_h";
    public static final String R_lat_a = "R_lat_a";
    public static final String R_lat_g = "R_lat_g";

    /* renamed from: a */
    public static final String f420a = "a";
    public static final String alpha = "alpha";
    public static final String azi = "azi";

    /* renamed from: b */
    public static final String f421b = "b";
    public static final String datum = "datum";
    public static final String ellps = "ellps";

    /* renamed from: es */
    public static final String f422es = "es";

    /* renamed from: f */
    public static final String f423f = "f";

    /* renamed from: k */
    public static final String f424k = "k";
    public static final String k_0 = "k_0";
    public static final String lat_0 = "lat_0";
    public static final String lat_1 = "lat_1";
    public static final String lat_2 = "lat_2";
    public static final String lat_ts = "lat_ts";
    public static final String lon_0 = "lon_0";
    public static final String lonc = "lonc";
    public static final String nadgrids = "nadgrids";
    public static final String no_defs = "no_defs";

    /* renamed from: pm */
    public static final String f425pm = "pm";
    public static final String proj = "proj";

    /* renamed from: rf */
    public static final String f426rf = "rf";
    public static final String south = "south";
    private static Set<String> supportedParams = null;
    public static final String title = "title";
    public static final String to_meter = "to_meter";
    public static final String towgs84 = "towgs84";
    public static final String units = "units";
    public static final String wktext = "wktext";
    public static final String x_0 = "x_0";
    public static final String y_0 = "y_0";
    public static final String zone = "zone";

    public static synchronized Set supportedParameters() {
        Set<String> set;
        synchronized (Proj4Keyword.class) {
            if (supportedParams == null) {
                TreeSet treeSet = new TreeSet();
                supportedParams = treeSet;
                treeSet.add(f420a);
                supportedParams.add(f426rf);
                supportedParams.add(f423f);
                supportedParams.add("alpha");
                supportedParams.add(f422es);
                supportedParams.add(f421b);
                supportedParams.add(datum);
                supportedParams.add(ellps);
                supportedParams.add(R_A);
                supportedParams.add(f424k);
                supportedParams.add(k_0);
                supportedParams.add(lat_ts);
                supportedParams.add(lat_0);
                supportedParams.add(lat_1);
                supportedParams.add(lat_2);
                supportedParams.add(lon_0);
                supportedParams.add(lonc);
                supportedParams.add(x_0);
                supportedParams.add(y_0);
                supportedParams.add(proj);
                supportedParams.add(south);
                supportedParams.add(towgs84);
                supportedParams.add(to_meter);
                supportedParams.add(units);
                supportedParams.add(zone);
                supportedParams.add("title");
                supportedParams.add(no_defs);
                supportedParams.add(wktext);
                supportedParams.add(nadgrids);
            }
            set = supportedParams;
        }
        return set;
    }

    public static boolean isSupported(String str) {
        return supportedParameters().contains(str);
    }

    public static void checkUnsupported(String str) {
        if (!isSupported(str)) {
            throw new UnsupportedParameterException(str + " parameter is not supported");
        }
    }

    public static void checkUnsupported(Collection collection) {
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            checkUnsupported((String) it.next());
        }
    }
}
