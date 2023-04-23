package org.osgeo.proj4j.units;

public class Angle {
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0034  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00e2  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00e8  */
    /* JADX WARNING: Removed duplicated region for block: B:58:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static double parse(java.lang.String r13) throws java.lang.NumberFormatException {
        /*
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
            if (r0 == r4) goto L_0x00e2
            java.lang.String r5 = r13.substring(r1, r0)
            int r0 = r0 + r2
            java.lang.String r13 = r13.substring(r0)
            java.lang.Double r0 = java.lang.Double.valueOf(r5)
            double r5 = r0.doubleValue()
            r0 = 109(0x6d, float:1.53E-43)
            int r0 = r13.indexOf(r0)
            if (r0 != r4) goto L_0x005b
            r0 = 39
            int r0 = r13.indexOf(r0)
        L_0x005b:
            r7 = 0
            if (r0 == r4) goto L_0x00c8
            if (r0 == 0) goto L_0x006e
            java.lang.String r4 = r13.substring(r1, r0)
            java.lang.Double r4 = java.lang.Double.valueOf(r4)
            double r9 = r4.doubleValue()
            goto L_0x006f
        L_0x006e:
            r9 = r7
        L_0x006f:
            java.lang.String r4 = "s"
            boolean r4 = r13.endsWith(r4)
            if (r4 != 0) goto L_0x007f
            java.lang.String r4 = "\""
            boolean r4 = r13.endsWith(r4)
            if (r4 == 0) goto L_0x0088
        L_0x007f:
            int r4 = r13.length()
            int r4 = r4 - r2
            java.lang.String r13 = r13.substring(r1, r4)
        L_0x0088:
            int r1 = r13.length()
            int r1 = r1 - r2
            if (r0 == r1) goto L_0x009d
            int r0 = r0 + r2
            java.lang.String r13 = r13.substring(r0)
            java.lang.Double r13 = java.lang.Double.valueOf(r13)
            double r0 = r13.doubleValue()
            goto L_0x009e
        L_0x009d:
            r0 = r7
        L_0x009e:
            int r13 = (r9 > r7 ? 1 : (r9 == r7 ? 0 : -1))
            if (r13 < 0) goto L_0x00c0
            r11 = 4633500329122463744(0x404d800000000000, double:59.0)
            int r13 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1))
            if (r13 > 0) goto L_0x00c0
            int r13 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1))
            if (r13 < 0) goto L_0x00b8
            r7 = 4633641066610819072(0x404e000000000000, double:60.0)
            int r13 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1))
            if (r13 >= 0) goto L_0x00b8
            r7 = r9
            r9 = r0
            goto L_0x00dd
        L_0x00b8:
            java.lang.NumberFormatException r13 = new java.lang.NumberFormatException
            java.lang.String r0 = "Seconds must be between 0 and 59"
            r13.<init>(r0)
            throw r13
        L_0x00c0:
            java.lang.NumberFormatException r13 = new java.lang.NumberFormatException
            java.lang.String r0 = "Minutes must be between 0 and 59"
            r13.<init>(r0)
            throw r13
        L_0x00c8:
            if (r0 == 0) goto L_0x00dc
            int r0 = r13.length()
            if (r0 != 0) goto L_0x00d1
            goto L_0x00dc
        L_0x00d1:
            java.lang.Double r13 = java.lang.Double.valueOf(r13)
            double r0 = r13.doubleValue()
            r9 = r7
            r7 = r0
            goto L_0x00dd
        L_0x00dc:
            r9 = r7
        L_0x00dd:
            double r0 = org.osgeo.proj4j.util.ProjectionMath.dmsToDeg(r5, r7, r9)
            goto L_0x00e6
        L_0x00e2:
            double r0 = java.lang.Double.parseDouble(r13)
        L_0x00e6:
            if (r3 == 0) goto L_0x00e9
            double r0 = -r0
        L_0x00e9:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osgeo.proj4j.units.Angle.parse(java.lang.String):double");
    }
}
