package org.xmlpull.p018v1;

/* renamed from: org.xmlpull.v1.XmlPullParserException */
public class XmlPullParserException extends Exception {
    protected int column;
    protected Throwable detail;
    protected int row;

    public XmlPullParserException(String str) {
        super(str);
        this.row = -1;
        this.column = -1;
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public XmlPullParserException(java.lang.String r4, org.xmlpull.p018v1.XmlPullParser r5, java.lang.Throwable r6) {
        /*
            r3 = this;
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r0.<init>()
            java.lang.String r1 = ""
            if (r4 != 0) goto L_0x000b
            r4 = r1
            goto L_0x001e
        L_0x000b:
            java.lang.StringBuffer r2 = new java.lang.StringBuffer
            r2.<init>()
            java.lang.StringBuffer r4 = r2.append(r4)
            java.lang.String r2 = " "
            java.lang.StringBuffer r4 = r4.append(r2)
            java.lang.String r4 = r4.toString()
        L_0x001e:
            java.lang.StringBuffer r4 = r0.append(r4)
            if (r5 != 0) goto L_0x0026
            r0 = r1
            goto L_0x0043
        L_0x0026:
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r0.<init>()
            java.lang.String r2 = "(position:"
            java.lang.StringBuffer r0 = r0.append(r2)
            java.lang.String r2 = r5.getPositionDescription()
            java.lang.StringBuffer r0 = r0.append(r2)
            java.lang.String r2 = ") "
            java.lang.StringBuffer r0 = r0.append(r2)
            java.lang.String r0 = r0.toString()
        L_0x0043:
            java.lang.StringBuffer r4 = r4.append(r0)
            if (r6 != 0) goto L_0x004a
            goto L_0x005d
        L_0x004a:
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r0.<init>()
            java.lang.String r1 = "caused by: "
            java.lang.StringBuffer r0 = r0.append(r1)
            java.lang.StringBuffer r0 = r0.append(r6)
            java.lang.String r1 = r0.toString()
        L_0x005d:
            java.lang.StringBuffer r4 = r4.append(r1)
            java.lang.String r4 = r4.toString()
            r3.<init>(r4)
            r4 = -1
            r3.row = r4
            r3.column = r4
            if (r5 == 0) goto L_0x007b
            int r4 = r5.getLineNumber()
            r3.row = r4
            int r4 = r5.getColumnNumber()
            r3.column = r4
        L_0x007b:
            r3.detail = r6
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xmlpull.p018v1.XmlPullParserException.<init>(java.lang.String, org.xmlpull.v1.XmlPullParser, java.lang.Throwable):void");
    }

    public Throwable getDetail() {
        return this.detail;
    }

    public int getLineNumber() {
        return this.row;
    }

    public int getColumnNumber() {
        return this.column;
    }

    public void printStackTrace() {
        if (this.detail == null) {
            super.printStackTrace();
            return;
        }
        synchronized (System.err) {
            System.err.println(new StringBuffer().append(super.getMessage()).append("; nested exception is:").toString());
            this.detail.printStackTrace();
        }
    }
}
