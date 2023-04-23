package org.osgeo.proj4j.units;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;

public class AngleFormat extends NumberFormat {
    public static final char CH_DEG_ABBREV = 'd';
    public static final char CH_DEG_SYMBOL = '°';
    public static final char CH_E = 'E';
    public static final char CH_MIN_ABBREV = 'm';
    public static final char CH_MIN_SYMBOL = '\'';
    public static final char CH_N = 'N';
    public static final char CH_S = 'S';
    public static final char CH_W = 'W';
    public static final String STR_SEC_ABBREV = "s";
    public static final String STR_SEC_SYMBOL = "\"";
    public static final String ddmmssLatPattern = "DdM'S\"N";
    public static final String ddmmssLongPattern = "DdM'S\"W";
    public static final String ddmmssPattern = "DdM";
    public static final String ddmmssPattern2 = "DdM'S\"";
    public static final String ddmmssPattern4 = "DdMmSs";
    public static final String decimalPattern = "D.F";
    private DecimalFormat format;
    private boolean isDegrees;
    private String pattern;

    public AngleFormat() {
        this(ddmmssPattern);
    }

    public AngleFormat(String str) {
        this(str, false);
    }

    public AngleFormat(String str, boolean z) {
        this.pattern = str;
        this.isDegrees = z;
        DecimalFormat decimalFormat = new DecimalFormat();
        this.format = decimalFormat;
        decimalFormat.setMaximumFractionDigits(0);
        this.format.setGroupingUsed(false);
    }

    public StringBuffer format(long j, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        return format((double) j, stringBuffer, fieldPosition);
    }

    public StringBuffer format(double d, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        boolean z;
        double d2 = d;
        StringBuffer stringBuffer2 = stringBuffer;
        int length = this.pattern.length();
        int i = (d2 > 0.0d ? 1 : (d2 == 0.0d ? 0 : -1));
        char c = CH_W;
        if (i < 0) {
            int i2 = length - 1;
            while (true) {
                if (i2 < 0) {
                    break;
                }
                char charAt = this.pattern.charAt(i2);
                if (charAt == 'W' || charAt == 'N') {
                    d2 = -d2;
                    z = true;
                } else {
                    i2--;
                }
            }
        }
        z = false;
        double degrees = this.isDegrees ? d2 : Math.toDegrees(d2);
        int round = (int) Math.round(3600.0d * degrees);
        if (round < 0) {
            round = -round;
        }
        int i3 = round % 3600;
        for (int i4 = 0; i4 < length; i4++) {
            char charAt2 = this.pattern.charAt(i4);
            if (charAt2 == 'D') {
                stringBuffer2.append((int) degrees);
            } else if (charAt2 == 'F') {
                stringBuffer2.append(i3);
            } else if (charAt2 != c) {
                if (charAt2 == 'M') {
                    int i5 = i3 / 60;
                    if (i5 < 10) {
                        stringBuffer2.append('0');
                    }
                    stringBuffer2.append(i5);
                } else if (charAt2 != 'N') {
                    if (charAt2 == 'R') {
                        stringBuffer2.append(d2);
                    } else if (charAt2 != 'S') {
                        stringBuffer2.append(charAt2);
                    } else {
                        int i6 = i3 % 60;
                        if (i6 < 10) {
                            stringBuffer2.append('0');
                        }
                        stringBuffer2.append(i6);
                    }
                } else if (z) {
                    stringBuffer2.append(CH_S);
                } else {
                    stringBuffer2.append(CH_N);
                }
                c = CH_W;
            } else if (z) {
                c = CH_W;
                stringBuffer2.append(CH_W);
            } else {
                c = CH_W;
                stringBuffer2.append(CH_E);
            }
        }
        return stringBuffer2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0034  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00e4  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x00f2  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00fb  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Number parse(java.lang.String r13, java.text.ParsePosition r14) {
        /*
            r12 = this;
            int r0 = r13.length()
            r1 = 0
            r2 = 1
            if (r0 <= 0) goto L_0x002a
            int r0 = r0 - r2
            char r3 = r13.charAt(r0)
            char r3 = java.lang.Character.toUpperCase(r3)
            r4 = 69
            if (r3 == r4) goto L_0x0024
            r4 = 78
            if (r3 == r4) goto L_0x0024
            r4 = 83
            if (r3 == r4) goto L_0x0022
            r4 = 87
            if (r3 == r4) goto L_0x0022
            goto L_0x002a
        L_0x0022:
            r3 = r2
            goto L_0x0025
        L_0x0024:
            r3 = r1
        L_0x0025:
            java.lang.String r13 = r13.substring(r1, r0)
            goto L_0x002b
        L_0x002a:
            r3 = r1
        L_0x002b:
            r0 = 100
            int r0 = r13.indexOf(r0)
            r4 = -1
            if (r0 != r4) goto L_0x003a
            r0 = 176(0xb0, float:2.47E-43)
            int r0 = r13.indexOf(r0)
        L_0x003a:
            if (r0 == r4) goto L_0x00e4
            java.lang.String r5 = r13.substring(r1, r0)
            int r0 = r0 + r2
            java.lang.String r0 = r13.substring(r0)
            java.lang.Double r5 = java.lang.Double.valueOf(r5)
            double r6 = r5.doubleValue()
            r5 = 109(0x6d, float:1.53E-43)
            int r5 = r0.indexOf(r5)
            if (r5 != r4) goto L_0x005b
            r5 = 39
            int r5 = r0.indexOf(r5)
        L_0x005b:
            r8 = 0
            if (r5 == r4) goto L_0x00c8
            if (r5 == 0) goto L_0x006e
            java.lang.String r4 = r0.substring(r1, r5)
            java.lang.Double r4 = java.lang.Double.valueOf(r4)
            double r10 = r4.doubleValue()
            goto L_0x006f
        L_0x006e:
            r10 = r8
        L_0x006f:
            java.lang.String r4 = "s"
            boolean r4 = r0.endsWith(r4)
            if (r4 != 0) goto L_0x007f
            java.lang.String r4 = "\""
            boolean r4 = r0.endsWith(r4)
            if (r4 == 0) goto L_0x0088
        L_0x007f:
            int r4 = r0.length()
            int r4 = r4 - r2
            java.lang.String r0 = r0.substring(r1, r4)
        L_0x0088:
            int r1 = r0.length()
            int r1 = r1 - r2
            if (r5 == r1) goto L_0x009d
            int r5 = r5 + r2
            java.lang.String r0 = r0.substring(r5)
            java.lang.Double r0 = java.lang.Double.valueOf(r0)
            double r0 = r0.doubleValue()
            goto L_0x009e
        L_0x009d:
            r0 = r8
        L_0x009e:
            int r2 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1))
            if (r2 < 0) goto L_0x00c0
            r4 = 4633500329122463744(0x404d800000000000, double:59.0)
            int r2 = (r10 > r4 ? 1 : (r10 == r4 ? 0 : -1))
            if (r2 > 0) goto L_0x00c0
            int r2 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1))
            if (r2 < 0) goto L_0x00b8
            r4 = 4633641066610819072(0x404e000000000000, double:60.0)
            int r2 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r2 >= 0) goto L_0x00b8
            r8 = r10
            r10 = r0
            goto L_0x00d6
        L_0x00b8:
            java.lang.NumberFormatException r13 = new java.lang.NumberFormatException
            java.lang.String r14 = "Seconds must be between 0 and 59"
            r13.<init>(r14)
            throw r13
        L_0x00c0:
            java.lang.NumberFormatException r13 = new java.lang.NumberFormatException
            java.lang.String r14 = "Minutes must be between 0 and 59"
            r13.<init>(r14)
            throw r13
        L_0x00c8:
            if (r5 == 0) goto L_0x00d5
            java.lang.Double r0 = java.lang.Double.valueOf(r0)
            double r0 = r0.doubleValue()
            r10 = r8
            r8 = r0
            goto L_0x00d6
        L_0x00d5:
            r10 = r8
        L_0x00d6:
            boolean r0 = r12.isDegrees
            if (r0 == 0) goto L_0x00df
            double r0 = org.osgeo.proj4j.util.ProjectionMath.dmsToDeg(r6, r8, r10)
            goto L_0x00f0
        L_0x00df:
            double r0 = org.osgeo.proj4j.util.ProjectionMath.dmsToRad(r6, r8, r10)
            goto L_0x00f0
        L_0x00e4:
            double r0 = java.lang.Double.parseDouble(r13)
            boolean r2 = r12.isDegrees
            if (r2 != 0) goto L_0x00f0
            double r0 = java.lang.Math.toRadians(r0)
        L_0x00f0:
            if (r14 == 0) goto L_0x00f9
            int r13 = r13.length()
            r14.setIndex(r13)
        L_0x00f9:
            if (r3 == 0) goto L_0x00fc
            double r0 = -r0
        L_0x00fc:
            java.lang.Double r13 = new java.lang.Double
            r13.<init>(r0)
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osgeo.proj4j.units.AngleFormat.parse(java.lang.String, java.text.ParsePosition):java.lang.Number");
    }
}
