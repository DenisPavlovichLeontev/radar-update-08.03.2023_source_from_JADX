package com.google.android.gms.common.util;

import java.util.HashMap;
import org.osgeo.proj4j.units.AngleFormat;

/* compiled from: com.google.android.gms:play-services-basement@@18.1.0 */
public class MapUtils {
    public static void writeStringMapToJson(StringBuilder sb, HashMap<String, String> hashMap) {
        sb.append("{");
        boolean z = true;
        for (String next : hashMap.keySet()) {
            if (!z) {
                sb.append(",");
            }
            String str = hashMap.get(next);
            sb.append(AngleFormat.STR_SEC_SYMBOL);
            sb.append(next);
            sb.append("\":");
            if (str == null) {
                sb.append("null");
            } else {
                sb.append(AngleFormat.STR_SEC_SYMBOL);
                sb.append(str);
                sb.append(AngleFormat.STR_SEC_SYMBOL);
            }
            z = false;
        }
        sb.append("}");
    }
}
