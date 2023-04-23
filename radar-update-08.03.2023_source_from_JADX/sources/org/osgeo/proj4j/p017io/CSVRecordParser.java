package org.osgeo.proj4j.p017io;

import java.util.ArrayList;
import kotlin.text.Typography;

/* renamed from: org.osgeo.proj4j.io.CSVRecordParser */
public class CSVRecordParser {
    private static final int CH_DATA = 3;
    private static final int CH_EOL = 5;
    private static final int CH_QUOTE = 1;
    private static final int CH_SEPARATOR = 4;
    private static final int CH_WHITESPACE = 2;
    private static final int STATE_AFTER = 5;
    private static final int STATE_BEFORE = 2;
    private static final int STATE_DATA = 1;
    private static final int STATE_QUOTED_DATA = 3;
    private static final int STATE_SEEN_QUOTE = 4;
    private static final String[] strArrayType = new String[0];
    private boolean isStrictMode = false;
    private int loc = 0;
    private char quote = Typography.quote;
    private char separator = ',';

    public String[] parse(String str) {
        this.loc = 0;
        ArrayList arrayList = new ArrayList();
        int length = str.length();
        while (this.loc < length) {
            arrayList.add(parseField(str));
        }
        return (String[]) arrayList.toArray(strArrayType);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003e, code lost:
        return r0.toString();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0069, code lost:
        return r0.toString();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String parseField(java.lang.String r9) {
        /*
            r8 = this;
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r0.<init>()
            r1 = 2
            r2 = r1
        L_0x0007:
            int r3 = r8.loc
            int r4 = r9.length()
            r5 = 5
            if (r3 >= r4) goto L_0x001b
            int r3 = r8.loc
            char r3 = r9.charAt(r3)
            int r3 = r8.categorize(r3)
            goto L_0x001c
        L_0x001b:
            r3 = r5
        L_0x001c:
            r4 = 4
            r6 = 3
            r7 = 1
            if (r2 == r7) goto L_0x00e2
            if (r2 == r1) goto L_0x00ae
            if (r2 == r6) goto L_0x0085
            if (r2 == r4) goto L_0x0055
            if (r2 == r5) goto L_0x002a
            goto L_0x0007
        L_0x002a:
            if (r3 == r7) goto L_0x004d
            if (r3 == r1) goto L_0x0047
            if (r3 == r6) goto L_0x003f
            if (r3 == r4) goto L_0x0035
            if (r3 == r5) goto L_0x0035
            goto L_0x0007
        L_0x0035:
            int r9 = r8.loc
            int r9 = r9 + r7
            r8.loc = r9
            java.lang.String r9 = r0.toString()
            return r9
        L_0x003f:
            java.lang.IllegalArgumentException r9 = new java.lang.IllegalArgumentException
            java.lang.String r0 = "Malformed field - unexpected data after quote"
            r9.<init>(r0)
            throw r9
        L_0x0047:
            int r3 = r8.loc
            int r3 = r3 + r7
            r8.loc = r3
            goto L_0x0007
        L_0x004d:
            java.lang.IllegalArgumentException r9 = new java.lang.IllegalArgumentException
            java.lang.String r0 = "Malformed field - unexpected quote"
            r9.<init>(r0)
            throw r9
        L_0x0055:
            if (r3 == r7) goto L_0x0079
            if (r3 == r1) goto L_0x0072
            if (r3 == r6) goto L_0x006a
            if (r3 == r4) goto L_0x0060
            if (r3 == r5) goto L_0x0060
            goto L_0x0007
        L_0x0060:
            int r9 = r8.loc
            int r9 = r9 + r7
            r8.loc = r9
            java.lang.String r9 = r0.toString()
            return r9
        L_0x006a:
            java.lang.IllegalArgumentException r9 = new java.lang.IllegalArgumentException
            java.lang.String r0 = "Malformed field - quote not at end of field"
            r9.<init>(r0)
            throw r9
        L_0x0072:
            int r2 = r8.loc
            int r2 = r2 + r7
            r8.loc = r2
            r2 = r5
            goto L_0x0007
        L_0x0079:
            int r2 = r8.loc
            int r2 = r2 + r7
            r8.loc = r2
            r2 = 34
            r0.append(r2)
        L_0x0083:
            r2 = r6
            goto L_0x0007
        L_0x0085:
            if (r3 == r7) goto L_0x00a6
            if (r3 == r1) goto L_0x0096
            if (r3 == r6) goto L_0x0096
            if (r3 == r4) goto L_0x0096
            if (r3 == r5) goto L_0x0091
            goto L_0x0007
        L_0x0091:
            java.lang.String r9 = r0.toString()
            return r9
        L_0x0096:
            int r3 = r8.loc
            char r3 = r9.charAt(r3)
            r0.append(r3)
            int r3 = r8.loc
            int r3 = r3 + r7
            r8.loc = r3
            goto L_0x0007
        L_0x00a6:
            int r2 = r8.loc
            int r2 = r2 + r7
            r8.loc = r2
            r2 = r4
            goto L_0x0007
        L_0x00ae:
            if (r3 == r7) goto L_0x00dc
            if (r3 == r1) goto L_0x00d5
            if (r3 == r6) goto L_0x00c4
            if (r3 == r4) goto L_0x00bc
            if (r3 == r5) goto L_0x00ba
            goto L_0x0007
        L_0x00ba:
            r9 = 0
            return r9
        L_0x00bc:
            int r9 = r8.loc
            int r9 = r9 + r7
            r8.loc = r9
            java.lang.String r9 = ""
            return r9
        L_0x00c4:
            int r2 = r8.loc
            char r2 = r9.charAt(r2)
            r0.append(r2)
            int r2 = r8.loc
            int r2 = r2 + r7
            r8.loc = r2
            r2 = r7
            goto L_0x0007
        L_0x00d5:
            int r3 = r8.loc
            int r3 = r3 + r7
            r8.loc = r3
            goto L_0x0007
        L_0x00dc:
            int r2 = r8.loc
            int r2 = r2 + r7
            r8.loc = r2
            goto L_0x0083
        L_0x00e2:
            if (r3 == r7) goto L_0x0108
            if (r3 == r1) goto L_0x00f8
            if (r3 == r6) goto L_0x00f8
            if (r3 == r4) goto L_0x00ee
            if (r3 == r5) goto L_0x00ee
            goto L_0x0007
        L_0x00ee:
            int r9 = r8.loc
            int r9 = r9 + r7
            r8.loc = r9
            java.lang.String r9 = r0.toString()
            return r9
        L_0x00f8:
            int r3 = r8.loc
            char r3 = r9.charAt(r3)
            r0.append(r3)
            int r3 = r8.loc
            int r3 = r3 + r7
            r8.loc = r3
            goto L_0x0007
        L_0x0108:
            boolean r3 = r8.isStrictMode
            if (r3 != 0) goto L_0x011c
            int r3 = r8.loc
            char r3 = r9.charAt(r3)
            r0.append(r3)
            int r3 = r8.loc
            int r3 = r3 + r7
            r8.loc = r3
            goto L_0x0007
        L_0x011c:
            java.lang.IllegalArgumentException r9 = new java.lang.IllegalArgumentException
            java.lang.String r0 = "Malformed field - quote not at beginning of field"
            r9.<init>(r0)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osgeo.proj4j.p017io.CSVRecordParser.parseField(java.lang.String):java.lang.String");
    }

    private int categorize(char c) {
        if (c == 10 || c == 13 || c == ' ' || c == 255) {
            return 2;
        }
        if (c == this.quote) {
            return 1;
        }
        if (c == this.separator) {
            return 4;
        }
        if ('!' <= c && c <= '~') {
            return 3;
        }
        if ((c < 0 || c > ' ') && !Character.isWhitespace(c)) {
            return 3;
        }
        return 2;
    }
}
