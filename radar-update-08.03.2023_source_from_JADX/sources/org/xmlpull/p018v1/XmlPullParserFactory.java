package org.xmlpull.p018v1;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/* renamed from: org.xmlpull.v1.XmlPullParserFactory */
public class XmlPullParserFactory {
    public static final String PROPERTY_NAME = "org.xmlpull.v1.XmlPullParserFactory";
    private static final String RESOURCE_NAME = "/META-INF/services/org.xmlpull.v1.XmlPullParserFactory";
    static final Class referenceContextClass = new XmlPullParserFactory().getClass();
    protected String classNamesLocation;
    protected Hashtable features = new Hashtable();
    protected Vector parserClasses;
    protected Vector serializerClasses;

    protected XmlPullParserFactory() {
    }

    public void setFeature(String str, boolean z) throws XmlPullParserException {
        this.features.put(str, new Boolean(z));
    }

    public boolean getFeature(String str) {
        Boolean bool = (Boolean) this.features.get(str);
        if (bool != null) {
            return bool.booleanValue();
        }
        return false;
    }

    public void setNamespaceAware(boolean z) {
        this.features.put(XmlPullParser.FEATURE_PROCESS_NAMESPACES, new Boolean(z));
    }

    public boolean isNamespaceAware() {
        return getFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES);
    }

    public void setValidating(boolean z) {
        this.features.put(XmlPullParser.FEATURE_VALIDATION, new Boolean(z));
    }

    public boolean isValidating() {
        return getFeature(XmlPullParser.FEATURE_VALIDATION);
    }

    public XmlPullParser newPullParser() throws XmlPullParserException {
        Vector vector = this.parserClasses;
        if (vector == null) {
            throw new XmlPullParserException(new StringBuffer().append("Factory initialization was incomplete - has not tried ").append(this.classNamesLocation).toString());
        } else if (vector.size() != 0) {
            StringBuffer stringBuffer = new StringBuffer();
            int i = 0;
            while (i < this.parserClasses.size()) {
                Class cls = (Class) this.parserClasses.elementAt(i);
                try {
                    XmlPullParser xmlPullParser = (XmlPullParser) cls.newInstance();
                    Enumeration keys = this.features.keys();
                    while (keys.hasMoreElements()) {
                        String str = (String) keys.nextElement();
                        Boolean bool = (Boolean) this.features.get(str);
                        if (bool != null && bool.booleanValue()) {
                            xmlPullParser.setFeature(str, true);
                        }
                    }
                    return xmlPullParser;
                } catch (Exception e) {
                    stringBuffer.append(new StringBuffer().append(cls.getName()).append(": ").append(e.toString()).append("; ").toString());
                    i++;
                }
            }
            throw new XmlPullParserException(new StringBuffer().append("could not create parser: ").append(stringBuffer).toString());
        } else {
            throw new XmlPullParserException(new StringBuffer().append("No valid parser classes found in ").append(this.classNamesLocation).toString());
        }
    }

    public XmlSerializer newSerializer() throws XmlPullParserException {
        Vector vector = this.serializerClasses;
        if (vector == null) {
            throw new XmlPullParserException(new StringBuffer().append("Factory initialization incomplete - has not tried ").append(this.classNamesLocation).toString());
        } else if (vector.size() != 0) {
            StringBuffer stringBuffer = new StringBuffer();
            int i = 0;
            while (i < this.serializerClasses.size()) {
                Class cls = (Class) this.serializerClasses.elementAt(i);
                try {
                    return (XmlSerializer) cls.newInstance();
                } catch (Exception e) {
                    stringBuffer.append(new StringBuffer().append(cls.getName()).append(": ").append(e.toString()).append("; ").toString());
                    i++;
                }
            }
            throw new XmlPullParserException(new StringBuffer().append("could not create serializer: ").append(stringBuffer).toString());
        } else {
            throw new XmlPullParserException(new StringBuffer().append("No valid serializer classes found in ").append(this.classNamesLocation).toString());
        }
    }

    public static XmlPullParserFactory newInstance() throws XmlPullParserException {
        return newInstance((String) null, (Class) null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x00a0  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x00da A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static org.xmlpull.p018v1.XmlPullParserFactory newInstance(java.lang.String r12, java.lang.Class r13) throws org.xmlpull.p018v1.XmlPullParserException {
        /*
            if (r13 != 0) goto L_0x0004
            java.lang.Class r13 = referenceContextClass
        L_0x0004:
            java.lang.String r0 = "'"
            r1 = 0
            if (r12 == 0) goto L_0x0030
            int r2 = r12.length()
            if (r2 == 0) goto L_0x0030
            java.lang.String r2 = "DEFAULT"
            boolean r2 = r2.equals(r12)
            if (r2 == 0) goto L_0x0018
            goto L_0x0030
        L_0x0018:
            java.lang.StringBuffer r13 = new java.lang.StringBuffer
            r13.<init>()
            java.lang.String r2 = "parameter classNames to newInstance() that contained '"
            java.lang.StringBuffer r13 = r13.append(r2)
            java.lang.StringBuffer r13 = r13.append(r12)
            java.lang.StringBuffer r13 = r13.append(r0)
            java.lang.String r13 = r13.toString()
            goto L_0x0061
        L_0x0030:
            java.lang.String r12 = "/META-INF/services/org.xmlpull.v1.XmlPullParserFactory"
            java.io.InputStream r12 = r13.getResourceAsStream(r12)     // Catch:{ Exception -> 0x00ef }
            if (r12 == 0) goto L_0x00e7
            java.lang.StringBuffer r13 = new java.lang.StringBuffer     // Catch:{ Exception -> 0x00ef }
            r13.<init>()     // Catch:{ Exception -> 0x00ef }
        L_0x003d:
            int r2 = r12.read()     // Catch:{ Exception -> 0x00ef }
            if (r2 >= 0) goto L_0x00dd
            r12.close()     // Catch:{ Exception -> 0x00ef }
            java.lang.String r12 = r13.toString()     // Catch:{ Exception -> 0x00ef }
            java.lang.StringBuffer r13 = new java.lang.StringBuffer
            r13.<init>()
            java.lang.String r2 = "resource /META-INF/services/org.xmlpull.v1.XmlPullParserFactory that contained '"
            java.lang.StringBuffer r13 = r13.append(r2)
            java.lang.StringBuffer r13 = r13.append(r12)
            java.lang.StringBuffer r13 = r13.append(r0)
            java.lang.String r13 = r13.toString()
        L_0x0061:
            java.util.Vector r0 = new java.util.Vector
            r0.<init>()
            java.util.Vector r2 = new java.util.Vector
            r2.<init>()
            r3 = 0
            r5 = r1
            r4 = r3
        L_0x006e:
            int r6 = r12.length()
            if (r4 < r6) goto L_0x0082
            if (r5 != 0) goto L_0x007b
            org.xmlpull.v1.XmlPullParserFactory r5 = new org.xmlpull.v1.XmlPullParserFactory
            r5.<init>()
        L_0x007b:
            r5.parserClasses = r0
            r5.serializerClasses = r2
            r5.classNamesLocation = r13
            return r5
        L_0x0082:
            r6 = 44
            int r6 = r12.indexOf(r6, r4)
            r7 = -1
            if (r6 != r7) goto L_0x008f
            int r6 = r12.length()
        L_0x008f:
            java.lang.String r4 = r12.substring(r4, r6)
            java.lang.Class r7 = java.lang.Class.forName(r4)     // Catch:{ Exception -> 0x009c }
            java.lang.Object r8 = r7.newInstance()     // Catch:{ Exception -> 0x009d }
            goto L_0x009e
        L_0x009c:
            r7 = r1
        L_0x009d:
            r8 = r1
        L_0x009e:
            if (r7 == 0) goto L_0x00da
            boolean r9 = r8 instanceof org.xmlpull.p018v1.XmlPullParser
            r10 = 1
            if (r9 == 0) goto L_0x00aa
            r0.addElement(r7)
            r9 = r10
            goto L_0x00ab
        L_0x00aa:
            r9 = r3
        L_0x00ab:
            boolean r11 = r8 instanceof org.xmlpull.p018v1.XmlSerializer
            if (r11 == 0) goto L_0x00b3
            r2.addElement(r7)
            r9 = r10
        L_0x00b3:
            boolean r7 = r8 instanceof org.xmlpull.p018v1.XmlPullParserFactory
            if (r7 == 0) goto L_0x00bd
            if (r5 != 0) goto L_0x00be
            r5 = r8
            org.xmlpull.v1.XmlPullParserFactory r5 = (org.xmlpull.p018v1.XmlPullParserFactory) r5
            goto L_0x00be
        L_0x00bd:
            r10 = r9
        L_0x00be:
            if (r10 == 0) goto L_0x00c1
            goto L_0x00da
        L_0x00c1:
            org.xmlpull.v1.XmlPullParserException r12 = new org.xmlpull.v1.XmlPullParserException
            java.lang.StringBuffer r13 = new java.lang.StringBuffer
            r13.<init>()
            java.lang.String r0 = "incompatible class: "
            java.lang.StringBuffer r13 = r13.append(r0)
            java.lang.StringBuffer r13 = r13.append(r4)
            java.lang.String r13 = r13.toString()
            r12.<init>(r13)
            throw r12
        L_0x00da:
            int r4 = r6 + 1
            goto L_0x006e
        L_0x00dd:
            r3 = 32
            if (r2 <= r3) goto L_0x003d
            char r2 = (char) r2
            r13.append(r2)     // Catch:{ Exception -> 0x00ef }
            goto L_0x003d
        L_0x00e7:
            org.xmlpull.v1.XmlPullParserException r12 = new org.xmlpull.v1.XmlPullParserException     // Catch:{ Exception -> 0x00ef }
            java.lang.String r13 = "resource not found: /META-INF/services/org.xmlpull.v1.XmlPullParserFactory make sure that parser implementing XmlPull API is available"
            r12.<init>(r13)     // Catch:{ Exception -> 0x00ef }
            throw r12     // Catch:{ Exception -> 0x00ef }
        L_0x00ef:
            r12 = move-exception
            org.xmlpull.v1.XmlPullParserException r13 = new org.xmlpull.v1.XmlPullParserException
            r13.<init>(r1, r1, r12)
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xmlpull.p018v1.XmlPullParserFactory.newInstance(java.lang.String, java.lang.Class):org.xmlpull.v1.XmlPullParserFactory");
    }
}
