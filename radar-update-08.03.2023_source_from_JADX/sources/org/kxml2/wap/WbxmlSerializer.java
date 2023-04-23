package org.kxml2.wap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Vector;
import kotlin.jvm.internal.ByteCompanionObject;
import org.xmlpull.p018v1.XmlSerializer;

public class WbxmlSerializer implements XmlSerializer {
    private int attrPage;
    Hashtable attrStartTable = new Hashtable();
    Hashtable attrValueTable = new Hashtable();
    Vector attributes = new Vector();
    ByteArrayOutputStream buf = new ByteArrayOutputStream();
    int depth;
    private String encoding;
    String name;
    String namespace;
    OutputStream out;
    String pending;
    Hashtable stringTable = new Hashtable();
    ByteArrayOutputStream stringTableBuf = new ByteArrayOutputStream();
    private int tagPage;
    Hashtable tagTable = new Hashtable();

    static void writeInt(OutputStream outputStream, int i) throws IOException {
        int i2;
        byte[] bArr = new byte[5];
        int i3 = 0;
        while (true) {
            i2 = i3 + 1;
            bArr[i3] = (byte) (i & 127);
            i >>= 7;
            if (i == 0) {
                break;
            }
            i3 = i2;
        }
        while (i2 > 1) {
            i2--;
            outputStream.write(bArr[i2] | ByteCompanionObject.MIN_VALUE);
        }
        outputStream.write(bArr[0]);
    }

    private void writeStr(String str) throws IOException {
        int length = str.length();
        int i = 0;
        int i2 = 0;
        while (i < length) {
            while (i < length && str.charAt(i) < 'A') {
                i++;
            }
            int i3 = i;
            while (i3 < length && str.charAt(i3) >= 'A') {
                i3++;
            }
            if (i3 - i > 10) {
                if (i > i2 && str.charAt(i - 1) == ' ' && this.stringTable.get(str.substring(i, i3)) == null) {
                    this.buf.write(Wbxml.STR_T);
                    writeStrT(str.substring(i2, i3), false);
                } else {
                    if (i > i2 && str.charAt(i - 1) == ' ') {
                        i--;
                    }
                    if (i > i2) {
                        this.buf.write(Wbxml.STR_T);
                        writeStrT(str.substring(i2, i), false);
                    }
                    this.buf.write(Wbxml.STR_T);
                    writeStrT(str.substring(i, i3), true);
                }
                i2 = i3;
            }
            i = i3;
        }
        if (i2 < length) {
            this.buf.write(Wbxml.STR_T);
            writeStrT(str.substring(i2, length), false);
        }
    }

    private final void writeStrT(String str, boolean z) throws IOException {
        Integer num = (Integer) this.stringTable.get(str);
        if (num != null) {
            writeInt(this.buf, num.intValue());
            return;
        }
        int size = this.stringTableBuf.size();
        if (str.charAt(0) < '0' || !z) {
            writeInt(this.buf, size);
        } else {
            str = new StringBuffer().append(' ').append(str).toString();
            writeInt(this.buf, size + 1);
        }
        this.stringTable.put(str, new Integer(size));
        if (str.charAt(0) == ' ') {
            this.stringTable.put(str.substring(1), new Integer(size + 1));
        }
        int lastIndexOf = str.lastIndexOf(32);
        if (lastIndexOf > 1) {
            int i = size + lastIndexOf;
            this.stringTable.put(str.substring(lastIndexOf), new Integer(i));
            this.stringTable.put(str.substring(lastIndexOf + 1), new Integer(i + 1));
        }
        writeStrI(this.stringTableBuf, str);
        this.stringTableBuf.flush();
    }

    public XmlSerializer attribute(String str, String str2, String str3) {
        this.attributes.addElement(str2);
        this.attributes.addElement(str3);
        return this;
    }

    public void cdsect(String str) throws IOException {
        text(str);
    }

    public void checkPending(boolean z) throws IOException {
        if (this.pending != null) {
            int size = this.attributes.size();
            int[] iArr = (int[]) this.tagTable.get(this.pending);
            if (iArr == null) {
                this.buf.write(size == 0 ? z ? 4 : 68 : z ? Wbxml.LITERAL_A : Wbxml.LITERAL_AC);
                writeStrT(this.pending, false);
            } else {
                int i = iArr[0];
                if (i != this.tagPage) {
                    this.tagPage = i;
                    this.buf.write(0);
                    this.buf.write(this.tagPage);
                }
                this.buf.write(size == 0 ? z ? iArr[1] : iArr[1] | 64 : z ? iArr[1] | 128 : iArr[1] | Wbxml.EXT_0);
            }
            int i2 = 0;
            while (i2 < size) {
                int[] iArr2 = (int[]) this.attrStartTable.get(this.attributes.elementAt(i2));
                if (iArr2 == null) {
                    this.buf.write(4);
                    writeStrT((String) this.attributes.elementAt(i2), false);
                } else {
                    int i3 = iArr2[0];
                    if (i3 != this.attrPage) {
                        this.attrPage = i3;
                        this.buf.write(0);
                        this.buf.write(this.attrPage);
                    }
                    this.buf.write(iArr2[1]);
                }
                int i4 = i2 + 1;
                int[] iArr3 = (int[]) this.attrValueTable.get(this.attributes.elementAt(i4));
                if (iArr3 == null) {
                    writeStr((String) this.attributes.elementAt(i4));
                } else {
                    int i5 = iArr3[0];
                    if (i5 != this.attrPage) {
                        this.attrPage = i5;
                        this.buf.write(0);
                        this.buf.write(this.attrPage);
                    }
                    this.buf.write(iArr3[1]);
                }
                i2 = i4 + 1;
            }
            if (size > 0) {
                this.buf.write(1);
            }
            this.pending = null;
            this.attributes.removeAllElements();
        }
    }

    public void comment(String str) {
    }

    public void docdecl(String str) {
        throw new RuntimeException("Cannot write docdecl for WBXML");
    }

    public void endDocument() throws IOException {
        writeInt(this.out, this.stringTableBuf.size());
        this.out.write(this.stringTableBuf.toByteArray());
        this.out.write(this.buf.toByteArray());
        this.out.flush();
    }

    public XmlSerializer endTag(String str, String str2) throws IOException {
        if (this.pending != null) {
            checkPending(true);
        } else {
            this.buf.write(1);
        }
        this.depth--;
        return this;
    }

    public void entityRef(String str) {
        throw new RuntimeException("EntityReference not supported for WBXML");
    }

    public void flush() {
    }

    public int getDepth() {
        return this.depth;
    }

    public boolean getFeature(String str) {
        return false;
    }

    public String getName() {
        throw new RuntimeException("NYI");
    }

    public String getNamespace() {
        throw new RuntimeException("NYI");
    }

    public String getPrefix(String str, boolean z) {
        throw new RuntimeException("NYI");
    }

    public Object getProperty(String str) {
        return null;
    }

    public void ignorableWhitespace(String str) {
    }

    public void processingInstruction(String str) {
        throw new RuntimeException("PI NYI");
    }

    public void setAttrStartTable(int i, String[] strArr) {
        for (int i2 = 0; i2 < strArr.length; i2++) {
            String str = strArr[i2];
            if (str != null) {
                this.attrStartTable.put(str, new int[]{i, i2 + 5});
            }
        }
    }

    public void setAttrValueTable(int i, String[] strArr) {
        for (int i2 = 0; i2 < strArr.length; i2++) {
            String str = strArr[i2];
            if (str != null) {
                this.attrValueTable.put(str, new int[]{i, i2 + 133});
            }
        }
    }

    public void setFeature(String str, boolean z) {
        throw new IllegalArgumentException(new StringBuffer().append("unknown feature ").append(str).toString());
    }

    public void setOutput(OutputStream outputStream, String str) throws IOException {
        if (str == null) {
            str = "UTF-8";
        }
        this.encoding = str;
        this.out = outputStream;
        this.buf = new ByteArrayOutputStream();
        this.stringTableBuf = new ByteArrayOutputStream();
    }

    public void setOutput(Writer writer) {
        throw new RuntimeException("Wbxml requires an OutputStream!");
    }

    public void setPrefix(String str, String str2) {
        throw new RuntimeException("NYI");
    }

    public void setProperty(String str, Object obj) {
        throw new IllegalArgumentException(new StringBuffer().append("unknown property ").append(str).toString());
    }

    public void setTagTable(int i, String[] strArr) {
        for (int i2 = 0; i2 < strArr.length; i2++) {
            String str = strArr[i2];
            if (str != null) {
                this.tagTable.put(str, new int[]{i, i2 + 5});
            }
        }
    }

    public void startDocument(String str, Boolean bool) throws IOException {
        OutputStream outputStream;
        int i;
        this.out.write(3);
        this.out.write(1);
        if (str != null) {
            this.encoding = str;
        }
        if (this.encoding.toUpperCase().equals("UTF-8")) {
            outputStream = this.out;
            i = 106;
        } else if (this.encoding.toUpperCase().equals("ISO-8859-1")) {
            outputStream = this.out;
            i = 4;
        } else {
            throw new UnsupportedEncodingException(str);
        }
        outputStream.write(i);
    }

    public XmlSerializer startTag(String str, String str2) throws IOException {
        if (str == null || "".equals(str)) {
            checkPending(false);
            this.pending = str2;
            this.depth++;
            return this;
        }
        throw new RuntimeException("NSP NYI");
    }

    public XmlSerializer text(String str) throws IOException {
        checkPending(false);
        writeStr(str);
        return this;
    }

    public XmlSerializer text(char[] cArr, int i, int i2) throws IOException {
        checkPending(false);
        writeStr(new String(cArr, i, i2));
        return this;
    }

    /* access modifiers changed from: package-private */
    public void writeStrI(OutputStream outputStream, String str) throws IOException {
        outputStream.write(str.getBytes(this.encoding));
        outputStream.write(0);
    }

    public void writeWapExtension(int i, Object obj) throws IOException {
        checkPending(false);
        this.buf.write(i);
        switch (i) {
            case 64:
            case 65:
            case 66:
                writeStrI(this.buf, (String) obj);
                return;
            default:
                switch (i) {
                    case 128:
                    case Wbxml.EXT_T_1:
                    case Wbxml.EXT_T_2:
                        writeStrT((String) obj, false);
                        return;
                    default:
                        switch (i) {
                            case Wbxml.EXT_0:
                            case Wbxml.EXT_1:
                            case Wbxml.EXT_2:
                                return;
                            case Wbxml.OPAQUE:
                                byte[] bArr = (byte[]) obj;
                                writeInt(this.buf, bArr.length);
                                this.buf.write(bArr);
                                return;
                            default:
                                throw new IllegalArgumentException();
                        }
                }
        }
    }
}
