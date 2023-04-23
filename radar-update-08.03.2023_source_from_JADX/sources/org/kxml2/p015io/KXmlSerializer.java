package org.kxml2.p015io;

import com.j256.ormlite.stmt.query.SimpleComparison;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.json.HTTP;
import org.xmlpull.p018v1.XmlSerializer;

/* renamed from: org.kxml2.io.KXmlSerializer */
public class KXmlSerializer implements XmlSerializer {
    private int auto;
    private int depth;
    private String[] elementStack = new String[12];
    private String encoding;
    private boolean[] indent = new boolean[4];
    private int[] nspCounts = new int[4];
    private String[] nspStack = new String[8];
    private boolean pending;
    private boolean unicode;
    private Writer writer;

    private final void check(boolean z) throws IOException {
        if (this.pending) {
            int i = this.depth + 1;
            this.depth = i;
            this.pending = false;
            boolean[] zArr = this.indent;
            if (zArr.length <= i) {
                boolean[] zArr2 = new boolean[(i + 4)];
                System.arraycopy(zArr, 0, zArr2, 0, i);
                this.indent = zArr2;
            }
            boolean[] zArr3 = this.indent;
            int i2 = this.depth;
            zArr3[i2] = zArr3[i2 - 1];
            int i3 = this.nspCounts[i2 - 1];
            while (true) {
                int[] iArr = this.nspCounts;
                int i4 = this.depth;
                if (i3 < iArr[i4]) {
                    this.writer.write(32);
                    this.writer.write("xmlns");
                    int i5 = i3 * 2;
                    if (!"".equals(this.nspStack[i5])) {
                        this.writer.write(58);
                        this.writer.write(this.nspStack[i5]);
                    } else if ("".equals(getNamespace()) && !"".equals(this.nspStack[i5 + 1])) {
                        throw new IllegalStateException("Cannot set default namespace for elements in no namespace");
                    }
                    this.writer.write("=\"");
                    writeEscaped(this.nspStack[i5 + 1], 34);
                    this.writer.write(34);
                    i3++;
                } else {
                    if (iArr.length <= i4 + 1) {
                        int[] iArr2 = new int[(i4 + 8)];
                        System.arraycopy(iArr, 0, iArr2, 0, i4 + 1);
                        this.nspCounts = iArr2;
                    }
                    int[] iArr3 = this.nspCounts;
                    int i6 = this.depth;
                    iArr3[i6 + 1] = iArr3[i6];
                    this.writer.write(z ? " />" : SimpleComparison.GREATER_THAN_OPERATION);
                    return;
                }
            }
        }
    }

    private final String getPrefix(String str, boolean z, boolean z2) throws IOException {
        int i = this.nspCounts[this.depth + 1] * 2;
        while (true) {
            i -= 2;
            String str2 = null;
            String str3 = "";
            if (i >= 0) {
                if (this.nspStack[i + 1].equals(str) && (z || !this.nspStack[i].equals(str3))) {
                    String str4 = this.nspStack[i];
                    int i2 = i + 2;
                    while (true) {
                        if (i2 >= this.nspCounts[this.depth + 1] * 2) {
                            str2 = str4;
                            break;
                        } else if (this.nspStack[i2].equals(str4)) {
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (str2 != null) {
                        return str2;
                    }
                }
            } else if (!z2) {
                return null;
            } else {
                if (!str3.equals(str)) {
                    do {
                        StringBuffer append = new StringBuffer().append("n");
                        int i3 = this.auto;
                        this.auto = i3 + 1;
                        String stringBuffer = append.append(i3).toString();
                        int i4 = (this.nspCounts[this.depth + 1] * 2) - 2;
                        while (true) {
                            if (i4 < 0) {
                                str3 = stringBuffer;
                                continue;
                                break;
                            } else if (stringBuffer.equals(this.nspStack[i4])) {
                                str3 = null;
                                continue;
                                break;
                            } else {
                                i4 -= 2;
                            }
                        }
                    } while (str3 == null);
                } else {
                    boolean z3 = this.pending;
                    this.pending = false;
                    setPrefix(str3, str);
                    this.pending = z3;
                }
                boolean z32 = this.pending;
                this.pending = false;
                setPrefix(str3, str);
                this.pending = z32;
                return str3;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x002b, code lost:
        if (r1 != '\'') goto L_0x0050;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void writeEscaped(java.lang.String r6, int r7) throws java.io.IOException {
        /*
            r5 = this;
            r0 = 0
        L_0x0001:
            int r1 = r6.length()
            if (r0 >= r1) goto L_0x00a1
            char r1 = r6.charAt(r0)
            r2 = 9
            java.lang.String r3 = "&#"
            if (r1 == r2) goto L_0x007e
            r2 = 10
            if (r1 == r2) goto L_0x007e
            r2 = 13
            if (r1 == r2) goto L_0x007e
            r2 = 34
            if (r1 == r2) goto L_0x0041
            r4 = 60
            if (r1 == r4) goto L_0x003c
            r4 = 62
            if (r1 == r4) goto L_0x0037
            r4 = 38
            if (r1 == r4) goto L_0x002e
            r4 = 39
            if (r1 == r4) goto L_0x0041
            goto L_0x0050
        L_0x002e:
            java.io.Writer r1 = r5.writer
            java.lang.String r2 = "&amp;"
        L_0x0032:
            r1.write(r2)
            goto L_0x009d
        L_0x0037:
            java.io.Writer r1 = r5.writer
            java.lang.String r2 = "&gt;"
            goto L_0x0032
        L_0x003c:
            java.io.Writer r1 = r5.writer
            java.lang.String r2 = "&lt;"
            goto L_0x0032
        L_0x0041:
            if (r1 != r7) goto L_0x0050
            java.io.Writer r3 = r5.writer
            if (r1 != r2) goto L_0x004a
            java.lang.String r1 = "&quot;"
            goto L_0x004c
        L_0x004a:
            java.lang.String r1 = "&apos;"
        L_0x004c:
            r3.write(r1)
            goto L_0x009d
        L_0x0050:
            r2 = 32
            if (r1 < r2) goto L_0x0061
            r2 = 64
            if (r1 == r2) goto L_0x0061
            r2 = 127(0x7f, float:1.78E-43)
            if (r1 < r2) goto L_0x0081
            boolean r2 = r5.unicode
            if (r2 == 0) goto L_0x0061
            goto L_0x0081
        L_0x0061:
            java.io.Writer r2 = r5.writer
            java.lang.StringBuffer r4 = new java.lang.StringBuffer
            r4.<init>()
            java.lang.StringBuffer r3 = r4.append(r3)
            java.lang.StringBuffer r1 = r3.append(r1)
            java.lang.String r3 = ";"
            java.lang.StringBuffer r1 = r1.append(r3)
        L_0x0076:
            java.lang.String r1 = r1.toString()
            r2.write(r1)
            goto L_0x009d
        L_0x007e:
            r2 = -1
            if (r7 != r2) goto L_0x0087
        L_0x0081:
            java.io.Writer r2 = r5.writer
            r2.write(r1)
            goto L_0x009d
        L_0x0087:
            java.io.Writer r2 = r5.writer
            java.lang.StringBuffer r4 = new java.lang.StringBuffer
            r4.<init>()
            java.lang.StringBuffer r3 = r4.append(r3)
            java.lang.StringBuffer r1 = r3.append(r1)
            r3 = 59
            java.lang.StringBuffer r1 = r1.append(r3)
            goto L_0x0076
        L_0x009d:
            int r0 = r0 + 1
            goto L_0x0001
        L_0x00a1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.kxml2.p015io.KXmlSerializer.writeEscaped(java.lang.String, int):void");
    }

    public XmlSerializer attribute(String str, String str2, String str3) throws IOException {
        if (this.pending) {
            if (str == null) {
                str = "";
            }
            String prefix = "".equals(str) ? "" : getPrefix(str, false, true);
            this.writer.write(32);
            if (!"".equals(prefix)) {
                this.writer.write(prefix);
                this.writer.write(58);
            }
            this.writer.write(str2);
            this.writer.write(61);
            int i = 34;
            if (str3.indexOf(34) != -1) {
                i = 39;
            }
            this.writer.write(i);
            writeEscaped(str3, i);
            this.writer.write(i);
            return this;
        }
        throw new IllegalStateException("illegal position for attribute");
    }

    public void cdsect(String str) throws IOException {
        check(false);
        this.writer.write("<![CDATA[");
        this.writer.write(str);
        this.writer.write("]]>");
    }

    public void comment(String str) throws IOException {
        check(false);
        this.writer.write("<!--");
        this.writer.write(str);
        this.writer.write("-->");
    }

    public void docdecl(String str) throws IOException {
        this.writer.write("<!DOCTYPE");
        this.writer.write(str);
        this.writer.write(SimpleComparison.GREATER_THAN_OPERATION);
    }

    public void endDocument() throws IOException {
        while (true) {
            int i = this.depth;
            if (i > 0) {
                String[] strArr = this.elementStack;
                endTag(strArr[(i * 3) - 3], strArr[(i * 3) - 1]);
            } else {
                flush();
                return;
            }
        }
    }

    public XmlSerializer endTag(String str, String str2) throws IOException {
        if (!this.pending) {
            this.depth--;
        }
        if ((str != null || this.elementStack[this.depth * 3] == null) && ((str == null || str.equals(this.elementStack[this.depth * 3])) && this.elementStack[(this.depth * 3) + 2].equals(str2))) {
            if (this.pending) {
                check(true);
                this.depth--;
            } else {
                if (this.indent[this.depth + 1]) {
                    this.writer.write(HTTP.CRLF);
                    for (int i = 0; i < this.depth; i++) {
                        this.writer.write("  ");
                    }
                }
                this.writer.write("</");
                String str3 = this.elementStack[(this.depth * 3) + 1];
                if (!"".equals(str3)) {
                    this.writer.write(str3);
                    this.writer.write(58);
                }
                this.writer.write(str2);
                this.writer.write(62);
            }
            int[] iArr = this.nspCounts;
            int i2 = this.depth;
            iArr[i2 + 1] = iArr[i2];
            return this;
        }
        throw new IllegalArgumentException(new StringBuffer().append("</{").append(str).append("}").append(str2).append("> does not match start").toString());
    }

    public void entityRef(String str) throws IOException {
        check(false);
        this.writer.write(38);
        this.writer.write(str);
        this.writer.write(59);
    }

    public void flush() throws IOException {
        check(false);
        this.writer.flush();
    }

    public int getDepth() {
        return this.pending ? this.depth + 1 : this.depth;
    }

    public boolean getFeature(String str) {
        if ("http://xmlpull.org/v1/doc/features.html#indent-output".equals(str)) {
            return this.indent[this.depth];
        }
        return false;
    }

    public String getName() {
        if (getDepth() == 0) {
            return null;
        }
        return this.elementStack[(getDepth() * 3) - 1];
    }

    public String getNamespace() {
        if (getDepth() == 0) {
            return null;
        }
        return this.elementStack[(getDepth() * 3) - 3];
    }

    public String getPrefix(String str, boolean z) {
        try {
            return getPrefix(str, false, z);
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public Object getProperty(String str) {
        throw new RuntimeException("Unsupported property");
    }

    public void ignorableWhitespace(String str) throws IOException {
        text(str);
    }

    public void processingInstruction(String str) throws IOException {
        check(false);
        this.writer.write("<?");
        this.writer.write(str);
        this.writer.write("?>");
    }

    public void setFeature(String str, boolean z) {
        if ("http://xmlpull.org/v1/doc/features.html#indent-output".equals(str)) {
            this.indent[this.depth] = z;
            return;
        }
        throw new RuntimeException("Unsupported Feature");
    }

    public void setOutput(OutputStream outputStream, String str) throws IOException {
        OutputStreamWriter outputStreamWriter;
        if (outputStream != null) {
            if (str != null) {
                outputStreamWriter = new OutputStreamWriter(outputStream, str);
            }
            setOutput(outputStreamWriter);
            this.encoding = str;
            if (str != null && str.toLowerCase().startsWith("utf")) {
                this.unicode = true;
                return;
            }
            return;
        }
        throw new IllegalArgumentException();
    }

    public void setOutput(Writer writer2) {
        this.writer = writer2;
        int[] iArr = this.nspCounts;
        iArr[0] = 2;
        iArr[1] = 2;
        String[] strArr = this.nspStack;
        strArr[0] = "";
        strArr[1] = "";
        strArr[2] = "xml";
        strArr[3] = "http://www.w3.org/XML/1998/namespace";
        this.pending = false;
        this.auto = 0;
        this.depth = 0;
        this.unicode = false;
    }

    public void setPrefix(String str, String str2) throws IOException {
        check(false);
        if (str == null) {
            str = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        if (!str.equals(getPrefix(str2, true, false))) {
            int[] iArr = this.nspCounts;
            int i = this.depth + 1;
            int i2 = iArr[i];
            iArr[i] = i2 + 1;
            int i3 = i2 << 1;
            String[] strArr = this.nspStack;
            int i4 = i3 + 1;
            if (strArr.length < i4) {
                String[] strArr2 = new String[(strArr.length + 16)];
                System.arraycopy(strArr, 0, strArr2, 0, i3);
                this.nspStack = strArr2;
            }
            String[] strArr3 = this.nspStack;
            strArr3[i3] = str;
            strArr3[i4] = str2;
        }
    }

    public void setProperty(String str, Object obj) {
        throw new RuntimeException(new StringBuffer().append("Unsupported Property:").append(obj).toString());
    }

    public void startDocument(String str, Boolean bool) throws IOException {
        this.writer.write("<?xml version='1.0' ");
        if (str != null) {
            this.encoding = str;
            if (str.toLowerCase().startsWith("utf")) {
                this.unicode = true;
            }
        }
        if (this.encoding != null) {
            this.writer.write("encoding='");
            this.writer.write(this.encoding);
            this.writer.write("' ");
        }
        if (bool != null) {
            this.writer.write("standalone='");
            this.writer.write(bool.booleanValue() ? "yes" : SVGParser.XML_STYLESHEET_ATTR_ALTERNATE_NO);
            this.writer.write("' ");
        }
        this.writer.write("?>");
    }

    public XmlSerializer startTag(String str, String str2) throws IOException {
        check(false);
        if (this.indent[this.depth]) {
            this.writer.write(HTTP.CRLF);
            for (int i = 0; i < this.depth; i++) {
                this.writer.write("  ");
            }
        }
        int i2 = this.depth * 3;
        String[] strArr = this.elementStack;
        if (strArr.length < i2 + 3) {
            String[] strArr2 = new String[(strArr.length + 12)];
            System.arraycopy(strArr, 0, strArr2, 0, i2);
            this.elementStack = strArr2;
        }
        String prefix = str == null ? "" : getPrefix(str, true, true);
        if ("".equals(str)) {
            int i3 = this.nspCounts[this.depth];
            while (i3 < this.nspCounts[this.depth + 1]) {
                int i4 = i3 * 2;
                if (!"".equals(this.nspStack[i4]) || "".equals(this.nspStack[i4 + 1])) {
                    i3++;
                } else {
                    throw new IllegalStateException("Cannot set default namespace for elements in no namespace");
                }
            }
        }
        String[] strArr3 = this.elementStack;
        int i5 = i2 + 1;
        strArr3[i2] = str;
        strArr3[i5] = prefix;
        strArr3[i5 + 1] = str2;
        this.writer.write(60);
        if (!"".equals(prefix)) {
            this.writer.write(prefix);
            this.writer.write(58);
        }
        this.writer.write(str2);
        this.pending = true;
        return this;
    }

    public XmlSerializer text(String str) throws IOException {
        check(false);
        this.indent[this.depth] = false;
        writeEscaped(str, -1);
        return this;
    }

    public XmlSerializer text(char[] cArr, int i, int i2) throws IOException {
        text(new String(cArr, i, i2));
        return this;
    }
}
