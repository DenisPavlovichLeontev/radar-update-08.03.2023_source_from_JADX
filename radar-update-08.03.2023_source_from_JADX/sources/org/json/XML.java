package org.json;

import com.j256.ormlite.stmt.query.SimpleComparison;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import kotlin.text.Typography;
import org.mapsforge.core.model.Tag;
import org.osgeo.proj4j.units.AngleFormat;

public class XML {
    public static final Character AMP = Character.valueOf(Typography.amp);
    public static final Character APOS = Character.valueOf(AngleFormat.CH_MIN_SYMBOL);
    public static final Character BANG = '!';

    /* renamed from: EQ */
    public static final Character f368EQ = Character.valueOf(Tag.KEY_VALUE_SEPARATOR);

    /* renamed from: GT */
    public static final Character f369GT = Character.valueOf(Typography.greater);

    /* renamed from: LT */
    public static final Character f370LT = Character.valueOf(Typography.less);
    public static final String NULL_ATTR = "xsi:nil";
    public static final Character QUEST = '?';
    public static final Character QUOT = Character.valueOf(Typography.quote);
    public static final Character SLASH = '/';
    public static final String TYPE_ATTR = "xsi:type";

    private static Iterable<Integer> codePointIterator(final String str) {
        return new Iterable<Integer>() {
            public Iterator<Integer> iterator() {
                return new Iterator<Integer>() {
                    private int length;
                    private int nextIndex = 0;

                    {
                        this.length = str.length();
                    }

                    public boolean hasNext() {
                        return this.nextIndex < this.length;
                    }

                    public Integer next() {
                        int codePointAt = str.codePointAt(this.nextIndex);
                        this.nextIndex += Character.charCount(codePointAt);
                        return Integer.valueOf(codePointAt);
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public static String escape(String str) {
        StringBuilder sb = new StringBuilder(str.length());
        for (Integer intValue : codePointIterator(str)) {
            int intValue2 = intValue.intValue();
            if (intValue2 == 34) {
                sb.append("&quot;");
            } else if (intValue2 == 60) {
                sb.append("&lt;");
            } else if (intValue2 == 62) {
                sb.append("&gt;");
            } else if (intValue2 == 38) {
                sb.append("&amp;");
            } else if (intValue2 == 39) {
                sb.append("&apos;");
            } else if (mustEscape(intValue2)) {
                sb.append("&#x");
                sb.append(Integer.toHexString(intValue2));
                sb.append(';');
            } else {
                sb.appendCodePoint(intValue2);
            }
        }
        return sb.toString();
    }

    private static boolean mustEscape(int i) {
        return !(!Character.isISOControl(i) || i == 9 || i == 10 || i == 13) || ((i < 32 || i > 55295) && ((i < 57344 || i > 65533) && (i < 65536 || i > 1114111)));
    }

    public static String unescape(String str) {
        StringBuilder sb = new StringBuilder(str.length());
        int length = str.length();
        int i = 0;
        while (i < length) {
            char charAt = str.charAt(i);
            if (charAt == '&') {
                int indexOf = str.indexOf(59, i);
                if (indexOf > i) {
                    String substring = str.substring(i + 1, indexOf);
                    sb.append(XMLTokener.unescapeEntity(substring));
                    i += substring.length() + 1;
                } else {
                    sb.append(charAt);
                }
            } else {
                sb.append(charAt);
            }
            i++;
        }
        return sb.toString();
    }

    public static void noSpace(String str) throws JSONException {
        int length = str.length();
        if (length != 0) {
            int i = 0;
            while (i < length) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    i++;
                } else {
                    throw new JSONException("'" + str + "' contains a space character.");
                }
            }
            return;
        }
        throw new JSONException("Empty string.");
    }

    private static boolean parse(XMLTokener xMLTokener, JSONObject jSONObject, String str, XMLParserConfiguration xMLParserConfiguration) throws JSONException {
        Object nextToken = xMLTokener.nextToken();
        int i = 1;
        if (nextToken == BANG) {
            char next = xMLTokener.next();
            if (next == '-') {
                if (xMLTokener.next() == '-') {
                    xMLTokener.skipPast("-->");
                    return false;
                }
                xMLTokener.back();
            } else if (next == '[') {
                if (!"CDATA".equals(xMLTokener.nextToken()) || xMLTokener.next() != '[') {
                    throw xMLTokener.syntaxError("Expected 'CDATA['");
                }
                String nextCDATA = xMLTokener.nextCDATA();
                if (nextCDATA.length() > 0) {
                    jSONObject.accumulate(xMLParserConfiguration.getcDataTagName(), nextCDATA);
                }
                return false;
            }
            do {
                Object nextMeta = xMLTokener.nextMeta();
                if (nextMeta == null) {
                    throw xMLTokener.syntaxError("Missing '>' after '<!'.");
                } else if (nextMeta == f370LT) {
                    i++;
                    continue;
                } else if (nextMeta == f369GT) {
                    i--;
                    continue;
                } else {
                    continue;
                }
            } while (i > 0);
            return false;
        } else if (nextToken == QUEST) {
            xMLTokener.skipPast("?>");
            return false;
        } else if (nextToken == SLASH) {
            Object nextToken2 = xMLTokener.nextToken();
            if (str == null) {
                throw xMLTokener.syntaxError("Mismatched close tag " + nextToken2);
            } else if (!nextToken2.equals(str)) {
                throw xMLTokener.syntaxError("Mismatched " + str + " and " + nextToken2);
            } else if (xMLTokener.nextToken() == f369GT) {
                return true;
            } else {
                throw xMLTokener.syntaxError("Misshaped close tag");
            }
        } else if (!(nextToken instanceof Character)) {
            String str2 = (String) nextToken;
            JSONObject jSONObject2 = new JSONObject();
            boolean z = false;
            Object obj = null;
            XMLXsiTypeConverter xMLXsiTypeConverter = null;
            while (true) {
                if (obj == null) {
                    obj = xMLTokener.nextToken();
                }
                if (obj instanceof String) {
                    String str3 = (String) obj;
                    Object nextToken3 = xMLTokener.nextToken();
                    if (nextToken3 == f368EQ) {
                        Object nextToken4 = xMLTokener.nextToken();
                        if (nextToken4 instanceof String) {
                            if (xMLParserConfiguration.isConvertNilAttributeToNull() && NULL_ATTR.equals(str3) && Boolean.parseBoolean((String) nextToken4)) {
                                z = true;
                            } else if (xMLParserConfiguration.getXsiTypeMap() != null && !xMLParserConfiguration.getXsiTypeMap().isEmpty() && TYPE_ATTR.equals(str3)) {
                                xMLXsiTypeConverter = xMLParserConfiguration.getXsiTypeMap().get(nextToken4);
                            } else if (!z) {
                                String str4 = (String) nextToken4;
                                Object obj2 = str4;
                                if (!xMLParserConfiguration.isKeepStrings()) {
                                    obj2 = stringToValue(str4);
                                }
                                jSONObject2.accumulate(str3, obj2);
                            }
                            obj = null;
                        } else {
                            throw xMLTokener.syntaxError("Missing value");
                        }
                    } else {
                        jSONObject2.accumulate(str3, "");
                        obj = nextToken3;
                    }
                } else if (obj == SLASH) {
                    if (xMLTokener.nextToken() == f369GT) {
                        if (xMLParserConfiguration.getForceList().contains(str2)) {
                            if (z) {
                                jSONObject.append(str2, JSONObject.NULL);
                            } else if (jSONObject2.length() > 0) {
                                jSONObject.append(str2, jSONObject2);
                            } else {
                                jSONObject.put(str2, (Object) new JSONArray());
                            }
                        } else if (z) {
                            jSONObject.accumulate(str2, JSONObject.NULL);
                        } else if (jSONObject2.length() > 0) {
                            jSONObject.accumulate(str2, jSONObject2);
                        } else {
                            jSONObject.accumulate(str2, "");
                        }
                        return false;
                    }
                    throw xMLTokener.syntaxError("Misshaped tag");
                } else if (obj == f369GT) {
                    while (true) {
                        Object nextContent = xMLTokener.nextContent();
                        if (nextContent == null) {
                            if (str2 == null) {
                                return false;
                            }
                            throw xMLTokener.syntaxError("Unclosed tag " + str2);
                        } else if (nextContent instanceof String) {
                            String str5 = (String) nextContent;
                            if (str5.length() > 0) {
                                if (xMLXsiTypeConverter != null) {
                                    jSONObject2.accumulate(xMLParserConfiguration.getcDataTagName(), stringToValue(str5, xMLXsiTypeConverter));
                                } else {
                                    String str6 = xMLParserConfiguration.getcDataTagName();
                                    Object obj3 = str5;
                                    if (!xMLParserConfiguration.isKeepStrings()) {
                                        obj3 = stringToValue(str5);
                                    }
                                    jSONObject2.accumulate(str6, obj3);
                                }
                            }
                        } else if (nextContent == f370LT && parse(xMLTokener, jSONObject2, str2, xMLParserConfiguration)) {
                            if (xMLParserConfiguration.getForceList().contains(str2)) {
                                if (jSONObject2.length() == 0) {
                                    jSONObject.put(str2, (Object) new JSONArray());
                                } else if (jSONObject2.length() != 1 || jSONObject2.opt(xMLParserConfiguration.getcDataTagName()) == null) {
                                    jSONObject.append(str2, jSONObject2);
                                } else {
                                    jSONObject.append(str2, jSONObject2.opt(xMLParserConfiguration.getcDataTagName()));
                                }
                            } else if (jSONObject2.length() == 0) {
                                jSONObject.accumulate(str2, "");
                            } else if (jSONObject2.length() != 1 || jSONObject2.opt(xMLParserConfiguration.getcDataTagName()) == null) {
                                jSONObject.accumulate(str2, jSONObject2);
                            } else {
                                jSONObject.accumulate(str2, jSONObject2.opt(xMLParserConfiguration.getcDataTagName()));
                            }
                            return false;
                        }
                    }
                } else {
                    throw xMLTokener.syntaxError("Misshaped tag");
                }
            }
        } else {
            throw xMLTokener.syntaxError("Misshaped tag");
        }
    }

    public static Object stringToValue(String str, XMLXsiTypeConverter<?> xMLXsiTypeConverter) {
        if (xMLXsiTypeConverter != null) {
            return xMLXsiTypeConverter.convert(str);
        }
        return stringToValue(str);
    }

    public static Object stringToValue(String str) {
        if ("".equals(str)) {
            return str;
        }
        if ("true".equalsIgnoreCase(str)) {
            return Boolean.TRUE;
        }
        if ("false".equalsIgnoreCase(str)) {
            return Boolean.FALSE;
        }
        if ("null".equalsIgnoreCase(str)) {
            return JSONObject.NULL;
        }
        char charAt = str.charAt(0);
        if ((charAt < '0' || charAt > '9') && charAt != '-') {
            return str;
        }
        try {
            return stringToNumber(str);
        } catch (Exception unused) {
            return str;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r0 = java.lang.Double.valueOf(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003a, code lost:
        if (r0.isNaN() != false) goto L_0x0043;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0042, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x005a, code lost:
        throw new java.lang.NumberFormatException("val [" + r8 + "] is not a valid number.");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0072, code lost:
        throw new java.lang.NumberFormatException("val [" + r8 + "] is not a valid number.");
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0032 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.Number stringToNumber(java.lang.String r8) throws java.lang.NumberFormatException {
        /*
            r0 = 0
            char r0 = r8.charAt(r0)
            r1 = 57
            r2 = 45
            java.lang.String r3 = "] is not a valid number."
            java.lang.String r4 = "val ["
            r5 = 48
            if (r0 < r5) goto L_0x0013
            if (r0 <= r1) goto L_0x0015
        L_0x0013:
            if (r0 != r2) goto L_0x00f5
        L_0x0015:
            boolean r6 = isDecimalNotation(r8)
            if (r6 == 0) goto L_0x0073
            java.math.BigDecimal r1 = new java.math.BigDecimal     // Catch:{ NumberFormatException -> 0x0032 }
            r1.<init>(r8)     // Catch:{ NumberFormatException -> 0x0032 }
            if (r0 != r2) goto L_0x0031
            java.math.BigDecimal r0 = java.math.BigDecimal.ZERO     // Catch:{ NumberFormatException -> 0x0032 }
            int r0 = r0.compareTo(r1)     // Catch:{ NumberFormatException -> 0x0032 }
            if (r0 != 0) goto L_0x0031
            r0 = -9223372036854775808
            java.lang.Double r8 = java.lang.Double.valueOf(r0)     // Catch:{ NumberFormatException -> 0x0032 }
            return r8
        L_0x0031:
            return r1
        L_0x0032:
            java.lang.Double r0 = java.lang.Double.valueOf(r8)     // Catch:{ NumberFormatException -> 0x005b }
            boolean r1 = r0.isNaN()     // Catch:{ NumberFormatException -> 0x005b }
            if (r1 != 0) goto L_0x0043
            boolean r1 = r0.isInfinite()     // Catch:{ NumberFormatException -> 0x005b }
            if (r1 != 0) goto L_0x0043
            return r0
        L_0x0043:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException     // Catch:{ NumberFormatException -> 0x005b }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ NumberFormatException -> 0x005b }
            r1.<init>()     // Catch:{ NumberFormatException -> 0x005b }
            r1.append(r4)     // Catch:{ NumberFormatException -> 0x005b }
            r1.append(r8)     // Catch:{ NumberFormatException -> 0x005b }
            r1.append(r3)     // Catch:{ NumberFormatException -> 0x005b }
            java.lang.String r1 = r1.toString()     // Catch:{ NumberFormatException -> 0x005b }
            r0.<init>(r1)     // Catch:{ NumberFormatException -> 0x005b }
            throw r0     // Catch:{ NumberFormatException -> 0x005b }
        L_0x005b:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r4)
            r1.append(r8)
            r1.append(r3)
            java.lang.String r8 = r1.toString()
            r0.<init>(r8)
            throw r0
        L_0x0073:
            r6 = 1
            if (r0 != r5) goto L_0x009d
            int r7 = r8.length()
            if (r7 <= r6) goto L_0x009d
            char r0 = r8.charAt(r6)
            if (r0 < r5) goto L_0x00cd
            if (r0 <= r1) goto L_0x0085
            goto L_0x00cd
        L_0x0085:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r4)
            r1.append(r8)
            r1.append(r3)
            java.lang.String r8 = r1.toString()
            r0.<init>(r8)
            throw r0
        L_0x009d:
            if (r0 != r2) goto L_0x00cd
            int r0 = r8.length()
            r2 = 2
            if (r0 <= r2) goto L_0x00cd
            char r0 = r8.charAt(r6)
            char r2 = r8.charAt(r2)
            if (r0 != r5) goto L_0x00cd
            if (r2 < r5) goto L_0x00cd
            if (r2 <= r1) goto L_0x00b5
            goto L_0x00cd
        L_0x00b5:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r4)
            r1.append(r8)
            r1.append(r3)
            java.lang.String r8 = r1.toString()
            r0.<init>(r8)
            throw r0
        L_0x00cd:
            java.math.BigInteger r0 = new java.math.BigInteger
            r0.<init>(r8)
            int r8 = r0.bitLength()
            r1 = 31
            if (r8 > r1) goto L_0x00e3
            int r8 = r0.intValue()
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)
            return r8
        L_0x00e3:
            int r8 = r0.bitLength()
            r1 = 63
            if (r8 > r1) goto L_0x00f4
            long r0 = r0.longValue()
            java.lang.Long r8 = java.lang.Long.valueOf(r0)
            return r8
        L_0x00f4:
            return r0
        L_0x00f5:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r4)
            r1.append(r8)
            r1.append(r3)
            java.lang.String r8 = r1.toString()
            r0.<init>(r8)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.json.XML.stringToNumber(java.lang.String):java.lang.Number");
    }

    private static boolean isDecimalNotation(String str) {
        return str.indexOf(46) > -1 || str.indexOf(101) > -1 || str.indexOf(69) > -1 || "-0".equals(str);
    }

    public static JSONObject toJSONObject(String str) throws JSONException {
        return toJSONObject(str, XMLParserConfiguration.ORIGINAL);
    }

    public static JSONObject toJSONObject(Reader reader) throws JSONException {
        return toJSONObject(reader, XMLParserConfiguration.ORIGINAL);
    }

    public static JSONObject toJSONObject(Reader reader, boolean z) throws JSONException {
        if (z) {
            return toJSONObject(reader, XMLParserConfiguration.KEEP_STRINGS);
        }
        return toJSONObject(reader, XMLParserConfiguration.ORIGINAL);
    }

    public static JSONObject toJSONObject(Reader reader, XMLParserConfiguration xMLParserConfiguration) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        XMLTokener xMLTokener = new XMLTokener(reader);
        while (xMLTokener.more()) {
            xMLTokener.skipPast(SimpleComparison.LESS_THAN_OPERATION);
            if (xMLTokener.more()) {
                parse(xMLTokener, jSONObject, (String) null, xMLParserConfiguration);
            }
        }
        return jSONObject;
    }

    public static JSONObject toJSONObject(String str, boolean z) throws JSONException {
        return toJSONObject((Reader) new StringReader(str), z);
    }

    public static JSONObject toJSONObject(String str, XMLParserConfiguration xMLParserConfiguration) throws JSONException {
        return toJSONObject((Reader) new StringReader(str), xMLParserConfiguration);
    }

    public static String toString(Object obj) throws JSONException {
        return toString(obj, (String) null, XMLParserConfiguration.ORIGINAL);
    }

    public static String toString(Object obj, String str) {
        return toString(obj, str, XMLParserConfiguration.ORIGINAL);
    }

    public static String toString(Object obj, String str, XMLParserConfiguration xMLParserConfiguration) throws JSONException {
        String str2;
        JSONArray jSONArray;
        StringBuilder sb = new StringBuilder();
        if (obj instanceof JSONObject) {
            if (str != null) {
                sb.append(Typography.less);
                sb.append(str);
                sb.append(Typography.greater);
            }
            JSONObject jSONObject = (JSONObject) obj;
            for (String next : jSONObject.keySet()) {
                Object opt = jSONObject.opt(next);
                if (opt == null) {
                    opt = "";
                } else if (opt.getClass().isArray()) {
                    opt = new JSONArray(opt);
                }
                if (next.equals(xMLParserConfiguration.getcDataTagName())) {
                    if (opt instanceof JSONArray) {
                        JSONArray jSONArray2 = (JSONArray) opt;
                        int length = jSONArray2.length();
                        for (int i = 0; i < length; i++) {
                            if (i > 0) {
                                sb.append(10);
                            }
                            sb.append(escape(jSONArray2.opt(i).toString()));
                        }
                    } else {
                        sb.append(escape(opt.toString()));
                    }
                } else if (opt instanceof JSONArray) {
                    JSONArray jSONArray3 = (JSONArray) opt;
                    int length2 = jSONArray3.length();
                    for (int i2 = 0; i2 < length2; i2++) {
                        Object opt2 = jSONArray3.opt(i2);
                        if (opt2 instanceof JSONArray) {
                            sb.append(Typography.less);
                            sb.append(next);
                            sb.append(Typography.greater);
                            sb.append(toString(opt2, (String) null, xMLParserConfiguration));
                            sb.append("</");
                            sb.append(next);
                            sb.append(Typography.greater);
                        } else {
                            sb.append(toString(opt2, next, xMLParserConfiguration));
                        }
                    }
                } else if ("".equals(opt)) {
                    sb.append(Typography.less);
                    sb.append(next);
                    sb.append("/>");
                } else {
                    sb.append(toString(opt, next, xMLParserConfiguration));
                }
            }
            if (str != null) {
                sb.append("</");
                sb.append(str);
                sb.append(Typography.greater);
            }
            return sb.toString();
        } else if (obj == null || (!(obj instanceof JSONArray) && !obj.getClass().isArray())) {
            if (obj == null) {
                str2 = "null";
            } else {
                str2 = escape(obj.toString());
            }
            if (str == null) {
                return AngleFormat.STR_SEC_SYMBOL + str2 + AngleFormat.STR_SEC_SYMBOL;
            } else if (str2.length() == 0) {
                return SimpleComparison.LESS_THAN_OPERATION + str + "/>";
            } else {
                return SimpleComparison.LESS_THAN_OPERATION + str + SimpleComparison.GREATER_THAN_OPERATION + str2 + "</" + str + SimpleComparison.GREATER_THAN_OPERATION;
            }
        } else {
            if (obj.getClass().isArray()) {
                jSONArray = new JSONArray(obj);
            } else {
                jSONArray = (JSONArray) obj;
            }
            int length3 = jSONArray.length();
            for (int i3 = 0; i3 < length3; i3++) {
                sb.append(toString(jSONArray.opt(i3), str == null ? "array" : str, xMLParserConfiguration));
            }
            return sb.toString();
        }
    }
}
