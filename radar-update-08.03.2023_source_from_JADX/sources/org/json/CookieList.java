package org.json;

import com.j256.ormlite.stmt.query.SimpleComparison;
import org.mapsforge.core.model.Tag;

public class CookieList {
    public static JSONObject toJSONObject(String str) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        JSONTokener jSONTokener = new JSONTokener(str);
        while (jSONTokener.more()) {
            String unescape = Cookie.unescape(jSONTokener.nextTo((char) Tag.KEY_VALUE_SEPARATOR));
            jSONTokener.next((char) Tag.KEY_VALUE_SEPARATOR);
            jSONObject.put(unescape, (Object) Cookie.unescape(jSONTokener.nextTo(';')));
            jSONTokener.next();
        }
        return jSONObject;
    }

    public static String toString(JSONObject jSONObject) throws JSONException {
        StringBuilder sb = new StringBuilder();
        boolean z = false;
        for (String next : jSONObject.keySet()) {
            Object opt = jSONObject.opt(next);
            if (!JSONObject.NULL.equals(opt)) {
                if (z) {
                    sb.append(';');
                }
                sb.append(Cookie.escape(next));
                sb.append(SimpleComparison.EQUAL_TO_OPERATION);
                sb.append(Cookie.escape(opt.toString()));
                z = true;
            }
        }
        return sb.toString();
    }
}
