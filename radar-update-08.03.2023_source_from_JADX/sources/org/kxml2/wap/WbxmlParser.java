package org.kxml2.wap;

import androidx.core.p003os.EnvironmentCompat;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Vector;
import kotlin.text.Typography;
import org.xmlpull.p018v1.XmlPullParser;
import org.xmlpull.p018v1.XmlPullParserException;

public class WbxmlParser implements XmlPullParser {
    static final String HEX_DIGITS = "0123456789abcdef";
    private static final String ILLEGAL_TYPE = "Wrong event type";
    private static final String UNEXPECTED_EOF = "Unexpected EOF";
    public static final int WAP_EXTENSION = 64;
    private int ATTR_START_TABLE = 1;
    private int ATTR_VALUE_TABLE = 2;
    private int TAG_TABLE = 0;
    private String[] attrStartTable;
    private String[] attrValueTable;
    private int attributeCount;
    private String[] attributes = new String[16];
    private Hashtable cacheStringTable = null;
    private boolean degenerated;
    private int depth;
    private String[] elementStack = new String[16];
    private String encoding;

    /* renamed from: in */
    private InputStream f379in;
    private boolean isWhitespace;
    private String name;
    private String namespace;
    private int nextId = -2;
    private int[] nspCounts = new int[4];
    private String[] nspStack = new String[8];
    private String prefix;
    private boolean processNsp;
    private int publicIdentifierId;
    private byte[] stringTable;
    private Vector tables = new Vector();
    private String[] tagTable;
    private String text;
    private int type;
    private int version;
    private int wapCode;
    private Object wapExtensionData;

    private final boolean adjustNsp() throws XmlPullParserException {
        int i;
        String str;
        int i2 = 0;
        boolean z = false;
        while (true) {
            i = this.attributeCount;
            if (i2 >= (i << 2)) {
                break;
            }
            String str2 = this.attributes[i2 + 2];
            int indexOf = str2.indexOf(58);
            if (indexOf != -1) {
                String substring = str2.substring(0, indexOf);
                str = str2.substring(indexOf + 1);
                str2 = substring;
            } else if (str2.equals("xmlns")) {
                str = null;
            } else {
                i2 += 4;
            }
            if (!str2.equals("xmlns")) {
                z = true;
            } else {
                int[] iArr = this.nspCounts;
                int i3 = this.depth;
                int i4 = iArr[i3];
                iArr[i3] = i4 + 1;
                int i5 = i4 << 1;
                String[] ensureCapacity = ensureCapacity(this.nspStack, i5 + 2);
                this.nspStack = ensureCapacity;
                ensureCapacity[i5] = str;
                String[] strArr = this.attributes;
                int i6 = i2 + 3;
                ensureCapacity[i5 + 1] = strArr[i6];
                if (str != null && strArr[i6].equals("")) {
                    exception("illegal empty namespace");
                }
                String[] strArr2 = this.attributes;
                int i7 = this.attributeCount - 1;
                this.attributeCount = i7;
                System.arraycopy(strArr2, i2 + 4, strArr2, i2, (i7 << 2) - i2);
                i2 -= 4;
            }
            i2 += 4;
        }
        if (z) {
            int i8 = (i << 2) - 4;
            while (i8 >= 0) {
                int i9 = i8 + 2;
                String str3 = this.attributes[i9];
                int indexOf2 = str3.indexOf(58);
                if (indexOf2 != 0) {
                    if (indexOf2 != -1) {
                        String substring2 = str3.substring(0, indexOf2);
                        String substring3 = str3.substring(indexOf2 + 1);
                        String namespace2 = getNamespace(substring2);
                        if (namespace2 != null) {
                            String[] strArr3 = this.attributes;
                            strArr3[i8] = namespace2;
                            strArr3[i8 + 1] = substring2;
                            strArr3[i9] = substring3;
                            for (int i10 = (this.attributeCount << 2) - 4; i10 > i8; i10 -= 4) {
                                if (substring3.equals(this.attributes[i10 + 2]) && namespace2.equals(this.attributes[i10])) {
                                    exception(new StringBuffer().append("Duplicate Attribute: {").append(namespace2).append("}").append(substring3).toString());
                                }
                            }
                        } else {
                            throw new RuntimeException(new StringBuffer().append("Undefined Prefix: ").append(substring2).append(" in ").append(this).toString());
                        }
                    }
                    i8 -= 4;
                } else {
                    throw new RuntimeException(new StringBuffer().append("illegal attribute name: ").append(str3).append(" at ").append(this).toString());
                }
            }
        }
        int indexOf3 = this.name.indexOf(58);
        if (indexOf3 == 0) {
            exception(new StringBuffer().append("illegal tag name: ").append(this.name).toString());
        } else if (indexOf3 != -1) {
            this.prefix = this.name.substring(0, indexOf3);
            this.name = this.name.substring(indexOf3 + 1);
        }
        String namespace3 = getNamespace(this.prefix);
        this.namespace = namespace3;
        if (namespace3 == null) {
            if (this.prefix != null) {
                exception(new StringBuffer().append("undefined prefix: ").append(this.prefix).toString());
            }
            this.namespace = "";
        }
        return z;
    }

    private final String[] ensureCapacity(String[] strArr, int i) {
        if (strArr.length >= i) {
            return strArr;
        }
        String[] strArr2 = new String[(i + 16)];
        System.arraycopy(strArr, 0, strArr2, 0, strArr.length);
        return strArr2;
    }

    private final void exception(String str) throws XmlPullParserException {
        throw new XmlPullParserException(str, this, (Throwable) null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0054, code lost:
        r4.type = 64;
        r4.wapCode = r0;
        r4.wapExtensionData = parseWapExtension(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void nextImpl() throws java.io.IOException, org.xmlpull.p018v1.XmlPullParserException {
        /*
            r4 = this;
            int r0 = r4.type
            r1 = 3
            r2 = 1
            if (r0 != r1) goto L_0x000b
            int r0 = r4.depth
            int r0 = r0 - r2
            r4.depth = r0
        L_0x000b:
            boolean r0 = r4.degenerated
            if (r0 == 0) goto L_0x0015
            r4.type = r1
            r0 = 0
            r4.degenerated = r0
            return
        L_0x0015:
            r0 = 0
            r4.text = r0
            r4.prefix = r0
            r4.name = r0
        L_0x001c:
            int r0 = r4.peekId()
            r3 = -2
            r4.nextId = r3
            if (r0 != 0) goto L_0x002d
            int r0 = r4.readByte()
            r4.selectPage(r0, r2)
            goto L_0x001c
        L_0x002d:
            r3 = -1
            if (r0 == r3) goto L_0x00b3
            r3 = 2
            if (r0 == r2) goto L_0x009b
            if (r0 == r3) goto L_0x006a
            r2 = 4
            if (r0 == r1) goto L_0x0061
            switch(r0) {
                case 64: goto L_0x0054;
                case 65: goto L_0x0054;
                case 66: goto L_0x0054;
                case 67: goto L_0x004c;
                default: goto L_0x003b;
            }
        L_0x003b:
            switch(r0) {
                case 128: goto L_0x0054;
                case 129: goto L_0x0054;
                case 130: goto L_0x0054;
                case 131: goto L_0x0045;
                default: goto L_0x003e;
            }
        L_0x003e:
            switch(r0) {
                case 192: goto L_0x0054;
                case 193: goto L_0x0054;
                case 194: goto L_0x0054;
                case 195: goto L_0x0054;
                default: goto L_0x0041;
            }
        L_0x0041:
            r4.parseElement(r0)
            goto L_0x00b5
        L_0x0045:
            r4.type = r2
            java.lang.String r0 = r4.readStrT()
            goto L_0x0067
        L_0x004c:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            java.lang.String r1 = "PI curr. not supp."
            r0.<init>(r1)
            throw r0
        L_0x0054:
            r1 = 64
            r4.type = r1
            r4.wapCode = r0
            java.lang.Object r0 = r4.parseWapExtension(r0)
            r4.wapExtensionData = r0
            goto L_0x00b5
        L_0x0061:
            r4.type = r2
            java.lang.String r0 = r4.readStrI()
        L_0x0067:
            r4.text = r0
            goto L_0x00b5
        L_0x006a:
            r0 = 6
            r4.type = r0
            int r0 = r4.readInt()
            char r0 = (char) r0
            java.lang.StringBuffer r1 = new java.lang.StringBuffer
            r1.<init>()
            java.lang.String r2 = ""
            java.lang.StringBuffer r1 = r1.append(r2)
            java.lang.StringBuffer r1 = r1.append(r0)
            java.lang.String r1 = r1.toString()
            r4.text = r1
            java.lang.StringBuffer r1 = new java.lang.StringBuffer
            r1.<init>()
            java.lang.String r2 = "#"
            java.lang.StringBuffer r1 = r1.append(r2)
            java.lang.StringBuffer r0 = r1.append(r0)
            java.lang.String r0 = r0.toString()
            goto L_0x00b0
        L_0x009b:
            int r0 = r4.depth
            int r0 = r0 - r2
            int r0 = r0 << r3
            r4.type = r1
            java.lang.String[] r1 = r4.elementStack
            r2 = r1[r0]
            r4.namespace = r2
            int r2 = r0 + 1
            r2 = r1[r2]
            r4.prefix = r2
            int r0 = r0 + r3
            r0 = r1[r0]
        L_0x00b0:
            r4.name = r0
            goto L_0x00b5
        L_0x00b3:
            r4.type = r2
        L_0x00b5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.kxml2.wap.WbxmlParser.nextImpl():void");
    }

    private int peekId() throws IOException {
        if (this.nextId == -2) {
            this.nextId = this.f379in.read();
        }
        return this.nextId;
    }

    private void selectPage(int i, boolean z) throws XmlPullParserException {
        if (this.tables.size() != 0 || i != 0) {
            int i2 = i * 3;
            if (i2 > this.tables.size()) {
                exception(new StringBuffer().append("Code Page ").append(i).append(" undefined!").toString());
            }
            Vector vector = this.tables;
            if (z) {
                this.tagTable = (String[]) vector.elementAt(i2 + this.TAG_TABLE);
                return;
            }
            this.attrStartTable = (String[]) vector.elementAt(this.ATTR_START_TABLE + i2);
            this.attrValueTable = (String[]) this.tables.elementAt(i2 + this.ATTR_VALUE_TABLE);
        }
    }

    private final void setTable(int i, int i2, String[] strArr) {
        if (this.stringTable == null) {
            while (true) {
                int i3 = i * 3;
                if (this.tables.size() < i3 + 3) {
                    this.tables.addElement((Object) null);
                } else {
                    this.tables.setElementAt(strArr, i3 + i2);
                    return;
                }
            }
        } else {
            throw new RuntimeException("setXxxTable must be called before setInput!");
        }
    }

    public void defineEntityReplacementText(String str, String str2) throws XmlPullParserException {
    }

    public int getAttributeCount() {
        return this.attributeCount;
    }

    public String getAttributeName(int i) {
        if (i < this.attributeCount) {
            return this.attributes[(i << 2) + 2];
        }
        throw new IndexOutOfBoundsException();
    }

    public String getAttributeNamespace(int i) {
        if (i < this.attributeCount) {
            return this.attributes[i << 2];
        }
        throw new IndexOutOfBoundsException();
    }

    public String getAttributePrefix(int i) {
        if (i < this.attributeCount) {
            return this.attributes[(i << 2) + 1];
        }
        throw new IndexOutOfBoundsException();
    }

    public String getAttributeType(int i) {
        return "CDATA";
    }

    public String getAttributeValue(int i) {
        if (i < this.attributeCount) {
            return this.attributes[(i << 2) + 3];
        }
        throw new IndexOutOfBoundsException();
    }

    public String getAttributeValue(String str, String str2) {
        for (int i = (this.attributeCount << 2) - 4; i >= 0; i -= 4) {
            if (this.attributes[i + 2].equals(str2) && (str == null || this.attributes[i].equals(str))) {
                return this.attributes[i + 3];
            }
        }
        return null;
    }

    public int getColumnNumber() {
        return -1;
    }

    public int getDepth() {
        return this.depth;
    }

    public int getEventType() throws XmlPullParserException {
        return this.type;
    }

    public boolean getFeature(String str) {
        if (XmlPullParser.FEATURE_PROCESS_NAMESPACES.equals(str)) {
            return this.processNsp;
        }
        return false;
    }

    public String getInputEncoding() {
        return this.encoding;
    }

    public int getLineNumber() {
        return -1;
    }

    public String getName() {
        return this.name;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String getNamespace(String str) {
        if ("xml".equals(str)) {
            return "http://www.w3.org/XML/1998/namespace";
        }
        if ("xmlns".equals(str)) {
            return "http://www.w3.org/2000/xmlns/";
        }
        for (int namespaceCount = (getNamespaceCount(this.depth) << 1) - 2; namespaceCount >= 0; namespaceCount -= 2) {
            String[] strArr = this.nspStack;
            if (str == null) {
                if (strArr[namespaceCount] == null) {
                    return strArr[namespaceCount + 1];
                }
            } else if (str.equals(strArr[namespaceCount])) {
                return this.nspStack[namespaceCount + 1];
            }
        }
        return null;
    }

    public int getNamespaceCount(int i) {
        if (i <= this.depth) {
            return this.nspCounts[i];
        }
        throw new IndexOutOfBoundsException();
    }

    public String getNamespacePrefix(int i) {
        return this.nspStack[i << 1];
    }

    public String getNamespaceUri(int i) {
        return this.nspStack[(i << 1) + 1];
    }

    public String getPositionDescription() {
        String text2;
        StringBuffer stringBuffer = new StringBuffer(this.type < XmlPullParser.TYPES.length ? XmlPullParser.TYPES[this.type] : EnvironmentCompat.MEDIA_UNKNOWN);
        stringBuffer.append(' ');
        int i = this.type;
        if (i == 2 || i == 3) {
            if (this.degenerated) {
                stringBuffer.append("(empty) ");
            }
            stringBuffer.append(Typography.less);
            if (this.type == 3) {
                stringBuffer.append('/');
            }
            if (this.prefix != null) {
                stringBuffer.append(new StringBuffer().append("{").append(this.namespace).append("}").append(this.prefix).append(":").toString());
            }
            stringBuffer.append(this.name);
            int i2 = this.attributeCount << 2;
            for (int i3 = 0; i3 < i2; i3 += 4) {
                stringBuffer.append(' ');
                int i4 = i3 + 1;
                if (this.attributes[i4] != null) {
                    stringBuffer.append(new StringBuffer().append("{").append(this.attributes[i3]).append("}").append(this.attributes[i4]).append(":").toString());
                }
                stringBuffer.append(new StringBuffer().append(this.attributes[i3 + 2]).append("='").append(this.attributes[i3 + 3]).append("'").toString());
            }
            stringBuffer.append(Typography.greater);
        } else if (i != 7) {
            if (i != 4) {
                text2 = getText();
            } else if (this.isWhitespace) {
                text2 = "(whitespace)";
            } else {
                text2 = getText();
                if (text2.length() > 16) {
                    text2 = new StringBuffer().append(text2.substring(0, 16)).append("...").toString();
                }
            }
            stringBuffer.append(text2);
        }
        return stringBuffer.toString();
    }

    public String getPrefix() {
        return this.prefix;
    }

    public Object getProperty(String str) {
        return null;
    }

    public String getText() {
        return this.text;
    }

    public char[] getTextCharacters(int[] iArr) {
        if (this.type >= 4) {
            iArr[0] = 0;
            iArr[1] = this.text.length();
            char[] cArr = new char[this.text.length()];
            String str = this.text;
            str.getChars(0, str.length(), cArr, 0);
            return cArr;
        }
        iArr[0] = -1;
        iArr[1] = -1;
        return null;
    }

    public int getWapCode() {
        return this.wapCode;
    }

    public Object getWapExtensionData() {
        return this.wapExtensionData;
    }

    public boolean isAttributeDefault(int i) {
        return false;
    }

    public boolean isEmptyElementTag() throws XmlPullParserException {
        if (this.type != 2) {
            exception(ILLEGAL_TYPE);
        }
        return this.degenerated;
    }

    public boolean isWhitespace() throws XmlPullParserException {
        int i = this.type;
        if (!(i == 4 || i == 7 || i == 5)) {
            exception(ILLEGAL_TYPE);
        }
        return this.isWhitespace;
    }

    public int next() throws XmlPullParserException, IOException {
        this.isWhitespace = true;
        int i = 9999;
        while (true) {
            String str = this.text;
            nextImpl();
            int i2 = this.type;
            if (i2 < i) {
                i = i2;
            }
            if (i <= 5) {
                if (i >= 4) {
                    if (str != null) {
                        if (this.text != null) {
                            str = new StringBuffer().append(str).append(this.text).toString();
                        }
                        this.text = str;
                    }
                    int peekId = peekId();
                    if (!(peekId == 2 || peekId == 3 || peekId == 4 || peekId == 68 || peekId == 196 || peekId == 131 || peekId == 132)) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        this.type = i;
        if (i > 4) {
            this.type = 4;
        }
        return this.type;
    }

    public int nextTag() throws XmlPullParserException, IOException {
        next();
        if (this.type == 4 && this.isWhitespace) {
            next();
        }
        int i = this.type;
        if (!(i == 3 || i == 2)) {
            exception("unexpected type");
        }
        return this.type;
    }

    public String nextText() throws XmlPullParserException, IOException {
        String str;
        if (this.type != 2) {
            exception("precondition: START_TAG");
        }
        next();
        if (this.type == 4) {
            str = getText();
            next();
        } else {
            str = "";
        }
        if (this.type != 3) {
            exception("END_TAG expected");
        }
        return str;
    }

    public int nextToken() throws XmlPullParserException, IOException {
        this.isWhitespace = true;
        nextImpl();
        return this.type;
    }

    /* access modifiers changed from: package-private */
    public void parseElement(int i) throws IOException, XmlPullParserException {
        this.type = 2;
        this.name = resolveId(this.tagTable, i & 63);
        this.attributeCount = 0;
        if ((i & 128) != 0) {
            readAttr();
        }
        this.degenerated = (i & 64) == 0;
        int i2 = this.depth;
        this.depth = i2 + 1;
        int i3 = i2 << 2;
        String[] ensureCapacity = ensureCapacity(this.elementStack, i3 + 4);
        this.elementStack = ensureCapacity;
        ensureCapacity[i3 + 3] = this.name;
        int i4 = this.depth;
        int[] iArr = this.nspCounts;
        if (i4 >= iArr.length) {
            int[] iArr2 = new int[(i4 + 4)];
            System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
            this.nspCounts = iArr2;
        }
        int[] iArr3 = this.nspCounts;
        int i5 = this.depth;
        iArr3[i5] = iArr3[i5 - 1];
        for (int i6 = this.attributeCount - 1; i6 > 0; i6--) {
            for (int i7 = 0; i7 < i6; i7++) {
                if (getAttributeName(i6).equals(getAttributeName(i7))) {
                    exception(new StringBuffer().append("Duplicate Attribute: ").append(getAttributeName(i6)).toString());
                }
            }
        }
        if (this.processNsp) {
            adjustNsp();
        } else {
            this.namespace = "";
        }
        String[] strArr = this.elementStack;
        strArr[i3] = this.namespace;
        strArr[i3 + 1] = this.prefix;
        strArr[i3 + 2] = this.name;
    }

    public Object parseWapExtension(int i) throws IOException, XmlPullParserException {
        switch (i) {
            case 64:
            case 65:
            case 66:
                return readStrI();
            default:
                switch (i) {
                    case 128:
                    case Wbxml.EXT_T_1:
                    case Wbxml.EXT_T_2:
                        return new Integer(readInt());
                    default:
                        byte[] bArr = null;
                        switch (i) {
                            case Wbxml.EXT_0:
                            case Wbxml.EXT_1:
                            case Wbxml.EXT_2:
                                break;
                            case Wbxml.OPAQUE:
                                int readInt = readInt();
                                bArr = new byte[readInt];
                                int i2 = readInt;
                                while (i2 > 0) {
                                    i2 -= this.f379in.read(bArr, readInt - i2, i2);
                                }
                                break;
                            default:
                                exception(new StringBuffer().append("illegal id: ").append(i).toString());
                                return null;
                        }
                        return bArr;
                }
        }
    }

    public void readAttr() throws IOException, XmlPullParserException {
        StringBuffer stringBuffer;
        int readByte;
        String str;
        int readByte2 = readByte();
        int i = 0;
        while (readByte2 != 1) {
            while (readByte2 == 0) {
                selectPage(readByte(), false);
                readByte2 = readByte();
            }
            String resolveId = resolveId(this.attrStartTable, readByte2);
            int indexOf = resolveId.indexOf(61);
            if (indexOf == -1) {
                stringBuffer = new StringBuffer();
            } else {
                StringBuffer stringBuffer2 = new StringBuffer(resolveId.substring(indexOf + 1));
                resolveId = resolveId.substring(0, indexOf);
                stringBuffer = stringBuffer2;
            }
            while (true) {
                readByte = readByte();
                if (readByte > 128 || readByte == 0 || readByte == 2 || readByte == 3 || readByte == 131 || ((readByte >= 64 && readByte <= 66) || (readByte >= 128 && readByte <= 130))) {
                    if (readByte == 0) {
                        selectPage(readByte(), false);
                    } else if (readByte != 2) {
                        if (readByte != 3) {
                            switch (readByte) {
                                case 64:
                                case 65:
                                case 66:
                                    str = resolveWapExtension(readByte, parseWapExtension(readByte));
                                    break;
                                default:
                                    switch (readByte) {
                                        case 128:
                                        case Wbxml.EXT_T_1:
                                        case Wbxml.EXT_T_2:
                                            break;
                                        case Wbxml.STR_T:
                                            str = readStrT();
                                            break;
                                        default:
                                            switch (readByte) {
                                                case Wbxml.EXT_0:
                                                case Wbxml.EXT_1:
                                                case Wbxml.EXT_2:
                                                case Wbxml.OPAQUE:
                                                    break;
                                                default:
                                                    str = resolveId(this.attrValueTable, readByte);
                                                    break;
                                            }
                                    }
                                    str = resolveWapExtension(readByte, parseWapExtension(readByte));
                                    break;
                            }
                        } else {
                            str = readStrI();
                        }
                        stringBuffer.append(str);
                    } else {
                        stringBuffer.append((char) readInt());
                    }
                }
            }
            String[] ensureCapacity = ensureCapacity(this.attributes, i + 4);
            this.attributes = ensureCapacity;
            int i2 = i + 1;
            ensureCapacity[i] = "";
            int i3 = i2 + 1;
            ensureCapacity[i2] = null;
            int i4 = i3 + 1;
            ensureCapacity[i3] = resolveId;
            i = i4 + 1;
            ensureCapacity[i4] = stringBuffer.toString();
            this.attributeCount++;
            readByte2 = readByte;
        }
    }

    /* access modifiers changed from: package-private */
    public int readByte() throws IOException {
        int read = this.f379in.read();
        if (read != -1) {
            return read;
        }
        throw new IOException(UNEXPECTED_EOF);
    }

    /* access modifiers changed from: package-private */
    public int readInt() throws IOException {
        int readByte;
        int i = 0;
        do {
            readByte = readByte();
            i = (i << 7) | (readByte & 127);
        } while ((readByte & 128) != 0);
        return i;
    }

    /* access modifiers changed from: package-private */
    public String readStrI() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        boolean z = true;
        while (true) {
            int read = this.f379in.read();
            if (read == 0) {
                this.isWhitespace = z;
                String str = new String(byteArrayOutputStream.toByteArray(), this.encoding);
                byteArrayOutputStream.close();
                return str;
            } else if (read != -1) {
                if (read > 32) {
                    z = false;
                }
                byteArrayOutputStream.write(read);
            } else {
                throw new IOException(UNEXPECTED_EOF);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public String readStrT() throws IOException {
        byte[] bArr;
        int readInt = readInt();
        if (this.cacheStringTable == null) {
            this.cacheStringTable = new Hashtable();
        }
        String str = (String) this.cacheStringTable.get(new Integer(readInt));
        if (str != null) {
            return str;
        }
        int i = readInt;
        while (true) {
            bArr = this.stringTable;
            if (i >= bArr.length || bArr[i] == 0) {
                String str2 = new String(bArr, readInt, i - readInt, this.encoding);
                this.cacheStringTable.put(new Integer(readInt), str2);
            } else {
                i++;
            }
        }
        String str22 = new String(bArr, readInt, i - readInt, this.encoding);
        this.cacheStringTable.put(new Integer(readInt), str22);
        return str22;
    }

    public void require(int i, String str, String str2) throws XmlPullParserException, IOException {
        if (i != this.type || ((str != null && !str.equals(getNamespace())) || (str2 != null && !str2.equals(getName())))) {
            exception(new StringBuffer().append("expected: ").append(i == 64 ? "WAP Ext." : new StringBuffer().append(XmlPullParser.TYPES[i]).append(" {").append(str).append("}").append(str2).toString()).toString());
        }
    }

    /* access modifiers changed from: package-private */
    public String resolveId(String[] strArr, int i) throws IOException {
        String str;
        int i2 = (i & 127) - 5;
        if (i2 == -1) {
            this.wapCode = -1;
            return readStrT();
        } else if (i2 < 0 || strArr == null || i2 >= strArr.length || (str = strArr[i2]) == null) {
            throw new IOException(new StringBuffer().append("id ").append(i).append(" undef.").toString());
        } else {
            this.wapCode = i2 + 5;
            return str;
        }
    }

    /* access modifiers changed from: protected */
    public String resolveWapExtension(int i, Object obj) {
        if (!(obj instanceof byte[])) {
            return new StringBuffer().append("$(").append(obj).append(")").toString();
        }
        StringBuffer stringBuffer = new StringBuffer();
        byte[] bArr = (byte[]) obj;
        for (int i2 = 0; i2 < bArr.length; i2++) {
            stringBuffer.append(HEX_DIGITS.charAt((bArr[i2] >> 4) & 15));
            stringBuffer.append(HEX_DIGITS.charAt(bArr[i2] & 15));
        }
        return stringBuffer.toString();
    }

    public void setAttrStartTable(int i, String[] strArr) {
        setTable(i, this.ATTR_START_TABLE, strArr);
    }

    public void setAttrValueTable(int i, String[] strArr) {
        setTable(i, this.ATTR_VALUE_TABLE, strArr);
    }

    public void setFeature(String str, boolean z) throws XmlPullParserException {
        if (XmlPullParser.FEATURE_PROCESS_NAMESPACES.equals(str)) {
            this.processNsp = z;
        } else {
            exception(new StringBuffer().append("unsupported feature: ").append(str).toString());
        }
    }

    public void setInput(InputStream inputStream, String str) throws XmlPullParserException {
        this.f379in = inputStream;
        try {
            this.version = readByte();
            int readInt = readInt();
            this.publicIdentifierId = readInt;
            if (readInt == 0) {
                readInt();
            }
            int readInt2 = readInt();
            if (str == null) {
                if (readInt2 == 4) {
                    str = "ISO-8859-1";
                } else if (readInt2 == 106) {
                    str = "UTF-8";
                } else {
                    throw new UnsupportedEncodingException(new StringBuffer().append("").append(readInt2).toString());
                }
            }
            this.encoding = str;
            int readInt3 = readInt();
            this.stringTable = new byte[readInt3];
            int i = 0;
            while (true) {
                if (i >= readInt3) {
                    break;
                }
                int read = inputStream.read(this.stringTable, i, readInt3 - i);
                if (read <= 0) {
                    break;
                }
                i += read;
            }
            selectPage(0, true);
            selectPage(0, false);
        } catch (IOException unused) {
            exception("Illegal input format");
        }
    }

    public void setInput(Reader reader) throws XmlPullParserException {
        exception("InputStream required");
    }

    public void setProperty(String str, Object obj) throws XmlPullParserException {
        throw new XmlPullParserException(new StringBuffer().append("unsupported property: ").append(str).toString());
    }

    public void setTagTable(int i, String[] strArr) {
        setTable(i, this.TAG_TABLE, strArr);
    }
}
