package mil.nga.geopackage.p009db;

import org.osgeo.proj4j.units.AngleFormat;

/* renamed from: mil.nga.geopackage.db.CoreSQLUtils */
public class CoreSQLUtils {
    public static String quoteWrap(String str) {
        if (str == null) {
            return null;
        }
        if (str.startsWith(AngleFormat.STR_SEC_SYMBOL) && str.endsWith(AngleFormat.STR_SEC_SYMBOL)) {
            return str;
        }
        return AngleFormat.STR_SEC_SYMBOL + str + AngleFormat.STR_SEC_SYMBOL;
    }

    public static String[] quoteWrap(String[] strArr) {
        if (strArr == null) {
            return null;
        }
        String[] strArr2 = new String[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            strArr2[i] = quoteWrap(strArr[i]);
        }
        return strArr2;
    }

    public static String[] buildColumnsAs(String[] strArr, String[] strArr2) {
        if (strArr2 == null) {
            return strArr;
        }
        String[] strArr3 = new String[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            String str = strArr[i];
            String str2 = strArr2[i];
            if (str2 != null) {
                str = str2 + " AS " + str;
            }
            strArr3[i] = str;
        }
        return strArr3;
    }
}
