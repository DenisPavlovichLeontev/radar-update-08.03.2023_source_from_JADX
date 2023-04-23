package org.json;

import com.j256.ormlite.stmt.query.SimpleComparison;
import java.util.Locale;
import mil.nga.geopackage.schema.constraints.DataColumnConstraints;
import org.mapsforge.core.model.Tag;

public class Cookie {
    public static String escape(String str) {
        String trim = str.trim();
        int length = trim.length();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char charAt = trim.charAt(i);
            if (charAt < ' ' || charAt == '+' || charAt == '%' || charAt == '=' || charAt == ';') {
                sb.append('%');
                sb.append(Character.forDigit((char) ((charAt >>> 4) & 15), 16));
                sb.append(Character.forDigit((char) (charAt & 15), 16));
            } else {
                sb.append(charAt);
            }
        }
        return sb.toString();
    }

    public static JSONObject toJSONObject(String str) {
        Object obj;
        JSONObject jSONObject = new JSONObject();
        JSONTokener jSONTokener = new JSONTokener(str);
        String unescape = unescape(jSONTokener.nextTo((char) Tag.KEY_VALUE_SEPARATOR).trim());
        if (!"".equals(unescape)) {
            jSONObject.put("name", (Object) unescape);
            jSONTokener.next((char) Tag.KEY_VALUE_SEPARATOR);
            jSONObject.put(DataColumnConstraints.COLUMN_VALUE, (Object) unescape(jSONTokener.nextTo(';')).trim());
            jSONTokener.next();
            while (jSONTokener.more()) {
                String lowerCase = unescape(jSONTokener.nextTo("=;")).trim().toLowerCase(Locale.ROOT);
                if ("name".equalsIgnoreCase(lowerCase)) {
                    throw new JSONException("Illegal attribute name: 'name'");
                } else if (!DataColumnConstraints.COLUMN_VALUE.equalsIgnoreCase(lowerCase)) {
                    if (jSONTokener.next() != '=') {
                        obj = Boolean.TRUE;
                    } else {
                        obj = unescape(jSONTokener.nextTo(';')).trim();
                        jSONTokener.next();
                    }
                    if (!"".equals(lowerCase) && !"".equals(obj)) {
                        jSONObject.put(lowerCase, obj);
                    }
                } else {
                    throw new JSONException("Illegal attribute name: 'value'");
                }
            }
            return jSONObject;
        }
        throw new JSONException("Cookies must have a 'name'");
    }

    public static String toString(JSONObject jSONObject) throws JSONException {
        StringBuilder sb = new StringBuilder();
        String str = null;
        String str2 = null;
        for (String next : jSONObject.keySet()) {
            if ("name".equalsIgnoreCase(next)) {
                str = jSONObject.getString(next).trim();
            }
            if (DataColumnConstraints.COLUMN_VALUE.equalsIgnoreCase(next)) {
                str2 = jSONObject.getString(next).trim();
            }
            if (str != null && str2 != null) {
                break;
            }
        }
        if (str == null || "".equals(str.trim())) {
            throw new JSONException("Cookie does not have a name");
        }
        if (str2 == null) {
            str2 = "";
        }
        sb.append(escape(str));
        sb.append(SimpleComparison.EQUAL_TO_OPERATION);
        String str3 = str2;
        sb.append(escape(str2));
        for (String next2 : jSONObject.keySet()) {
            if (!"name".equalsIgnoreCase(next2) && !DataColumnConstraints.COLUMN_VALUE.equalsIgnoreCase(next2)) {
                Object opt = jSONObject.opt(next2);
                if (!(opt instanceof Boolean)) {
                    sb.append(';');
                    sb.append(escape(next2));
                    sb.append(Tag.KEY_VALUE_SEPARATOR);
                    sb.append(escape(opt.toString()));
                } else if (Boolean.TRUE.equals(opt)) {
                    sb.append(';');
                    sb.append(escape(next2));
                }
            }
        }
        return sb.toString();
    }

    public static String unescape(String str) {
        int i;
        int length = str.length();
        StringBuilder sb = new StringBuilder(length);
        int i2 = 0;
        while (i2 < length) {
            char charAt = str.charAt(i2);
            if (charAt == '+') {
                charAt = ' ';
            } else if (charAt == '%' && (i = i2 + 2) < length) {
                int dehexchar = JSONTokener.dehexchar(str.charAt(i2 + 1));
                int dehexchar2 = JSONTokener.dehexchar(str.charAt(i));
                if (dehexchar >= 0 && dehexchar2 >= 0) {
                    charAt = (char) ((dehexchar * 16) + dehexchar2);
                    i2 = i;
                }
            }
            sb.append(charAt);
            i2++;
        }
        return sb.toString();
    }
}
