package org.kxml2.p015io;

import androidx.core.p003os.EnvironmentCompat;
import com.j256.ormlite.stmt.query.SimpleComparison;
import java.io.IOException;
import java.io.Reader;
import java.util.Hashtable;
import kotlin.text.Typography;
import org.mapsforge.core.model.Tag;
import org.osgeo.proj4j.units.AngleFormat;
import org.xmlpull.p018v1.XmlPullParser;
import org.xmlpull.p018v1.XmlPullParserException;

/* renamed from: org.kxml2.io.KXmlParser */
public class KXmlParser implements XmlPullParser {
    private static final String ILLEGAL_TYPE = "Wrong event type";
    private static final int LEGACY = 999;
    private static final String UNEXPECTED_EOF = "Unexpected EOF";
    private static final int XML_DECL = 998;
    private int attributeCount;
    private String[] attributes;
    private int column;
    private boolean degenerated;
    private int depth;
    private String[] elementStack = new String[16];
    private String encoding;
    private Hashtable entityMap;
    private String error;
    private boolean isWhitespace;
    private int line;
    private Object location;
    private String name;
    private String namespace;
    private int[] nspCounts = new int[4];
    private String[] nspStack = new String[8];
    private int[] peek;
    private int peekCount;
    private String prefix;
    private boolean processNsp;
    private Reader reader;
    private boolean relaxed;
    private char[] srcBuf;
    private int srcCount;
    private int srcPos;
    private int stackMismatch;
    private Boolean standalone;
    private boolean token;
    private char[] txtBuf;
    private int txtPos;
    private int type;
    private boolean unresolved;
    private String version;
    private boolean wasCR;

    public KXmlParser() {
        int i = 128;
        this.txtBuf = new char[128];
        this.attributes = new String[16];
        this.stackMismatch = 0;
        this.peek = new int[2];
        this.srcBuf = new char[(Runtime.getRuntime().freeMemory() >= 1048576 ? 8192 : i)];
    }

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
                    error("illegal empty namespace");
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
                if (indexOf2 != 0 || this.relaxed) {
                    if (indexOf2 != -1) {
                        String substring2 = str3.substring(0, indexOf2);
                        String substring3 = str3.substring(indexOf2 + 1);
                        String namespace2 = getNamespace(substring2);
                        if (namespace2 != null || this.relaxed) {
                            String[] strArr3 = this.attributes;
                            strArr3[i8] = namespace2;
                            strArr3[i8 + 1] = substring2;
                            strArr3[i9] = substring3;
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
            error(new StringBuffer().append("illegal tag name: ").append(this.name).toString());
        }
        if (indexOf3 != -1) {
            this.prefix = this.name.substring(0, indexOf3);
            this.name = this.name.substring(indexOf3 + 1);
        }
        String namespace3 = getNamespace(this.prefix);
        this.namespace = namespace3;
        if (namespace3 == null) {
            if (this.prefix != null) {
                error(new StringBuffer().append("undefined prefix: ").append(this.prefix).toString());
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

    private final void error(String str) throws XmlPullParserException {
        if (!this.relaxed) {
            exception(str);
        } else if (this.error == null) {
            this.error = new StringBuffer().append("ERR: ").append(str).toString();
        }
    }

    private final void exception(String str) throws XmlPullParserException {
        if (str.length() >= 100) {
            str = new StringBuffer().append(str.substring(0, 100)).append("\n").toString();
        }
        throw new XmlPullParserException(str, this, (Throwable) null);
    }

    private final String get(int i) {
        return new String(this.txtBuf, i, this.txtPos - i);
    }

    private final boolean isProp(String str, boolean z, String str2) {
        if (!str.startsWith("http://xmlpull.org/v1/doc/")) {
            return false;
        }
        return str.substring(z ? 42 : 40).equals(str2);
    }

    private final void nextImpl() throws IOException, XmlPullParserException {
        int parseLegacy;
        if (this.reader == null) {
            exception("No Input specified");
        }
        if (this.type == 3) {
            this.depth--;
        }
        do {
            this.attributeCount = -1;
            if (this.degenerated) {
                this.degenerated = false;
                this.type = 3;
                return;
            } else if (this.error != null) {
                for (int i = 0; i < this.error.length(); i++) {
                    push(this.error.charAt(i));
                }
                this.error = null;
                this.type = 9;
                return;
            } else if (!this.relaxed || (this.stackMismatch <= 0 && (peek(0) != -1 || this.depth <= 0))) {
                this.prefix = null;
                this.name = null;
                this.namespace = null;
                int peekType = peekType();
                this.type = peekType;
                if (peekType == 1) {
                    return;
                }
                if (peekType == 2) {
                    parseStartTag(false);
                    return;
                } else if (peekType == 3) {
                    parseEndTag();
                    return;
                } else if (peekType == 4) {
                    pushText(60, !this.token);
                    if (this.depth == 0 && this.isWhitespace) {
                        this.type = 7;
                        return;
                    }
                    return;
                } else if (peekType != 6) {
                    parseLegacy = parseLegacy(this.token);
                    this.type = parseLegacy;
                } else {
                    pushEntity();
                    return;
                }
            } else {
                int i2 = (this.depth - 1) << 2;
                this.type = 3;
                String[] strArr = this.elementStack;
                this.namespace = strArr[i2];
                this.prefix = strArr[i2 + 1];
                this.name = strArr[i2 + 2];
                if (this.stackMismatch != 1) {
                    this.error = new StringBuffer().append("missing end tag /").append(this.name).append(" inserted").toString();
                }
                int i3 = this.stackMismatch;
                if (i3 > 0) {
                    this.stackMismatch = i3 - 1;
                    return;
                }
                return;
            }
        } while (parseLegacy == XML_DECL);
    }

    private final void parseDoctype(boolean z) throws IOException, XmlPullParserException {
        int i = 1;
        boolean z2 = false;
        while (true) {
            int read = read();
            if (read != -1) {
                if (read == 39) {
                    z2 = !z2;
                } else if (read != 60) {
                    if (read == 62 && !z2 && i - 1 == 0) {
                        return;
                    }
                } else if (!z2) {
                    i++;
                }
                if (z) {
                    push(read);
                }
            } else {
                error(UNEXPECTED_EOF);
                return;
            }
        }
    }

    private final void parseEndTag() throws IOException, XmlPullParserException {
        read();
        read();
        this.name = readName();
        skip();
        read(Typography.greater);
        int i = this.depth;
        int i2 = (i - 1) << 2;
        if (i == 0) {
            error("element stack empty");
            this.type = 9;
            return;
        }
        int i3 = i2 + 3;
        if (!this.name.equals(this.elementStack[i3])) {
            error(new StringBuffer().append("expected: /").append(this.elementStack[i3]).append(" read: ").append(this.name).toString());
            int i4 = i2;
            while (i4 >= 0 && !this.name.toLowerCase().equals(this.elementStack[i4 + 3].toLowerCase())) {
                this.stackMismatch++;
                i4 -= 4;
            }
            if (i4 < 0) {
                this.stackMismatch = 0;
                this.type = 9;
                return;
            }
        }
        String[] strArr = this.elementStack;
        this.namespace = strArr[i2];
        this.prefix = strArr[i2 + 1];
        this.name = strArr[i2 + 2];
    }

    private final int parseLegacy(boolean z) throws IOException, XmlPullParserException {
        String str;
        int i;
        int i2;
        String str2;
        Boolean bool;
        read();
        int read = read();
        if (read == 63) {
            if ((peek(0) == 120 || peek(0) == 88) && (peek(1) == 109 || peek(1) == 77)) {
                if (z) {
                    push(peek(0));
                    push(peek(1));
                }
                read();
                read();
                if ((peek(0) == 108 || peek(0) == 76) && peek(1) <= 32) {
                    if (this.line != 1 || this.column > 4) {
                        error("PI must not start with xml");
                    }
                    parseStartTag(true);
                    int i3 = 2;
                    if (this.attributeCount < 1 || !"version".equals(this.attributes[2])) {
                        error("version expected");
                    }
                    String[] strArr = this.attributes;
                    this.version = strArr[3];
                    if (1 >= this.attributeCount || !"encoding".equals(strArr[6])) {
                        i3 = 1;
                    } else {
                        this.encoding = this.attributes[7];
                    }
                    if (i3 < this.attributeCount) {
                        int i4 = i3 * 4;
                        if ("standalone".equals(this.attributes[i4 + 2])) {
                            String str3 = this.attributes[i4 + 3];
                            if ("yes".equals(str3)) {
                                bool = new Boolean(true);
                            } else if (SVGParser.XML_STYLESHEET_ATTR_ALTERNATE_NO.equals(str3)) {
                                bool = new Boolean(false);
                            } else {
                                error(new StringBuffer().append("illegal standalone value: ").append(str3).toString());
                                i3++;
                            }
                            this.standalone = bool;
                            i3++;
                        }
                    }
                    if (i3 != this.attributeCount) {
                        error("illegal xmldecl");
                    }
                    this.isWhitespace = true;
                    this.txtPos = 0;
                    return XML_DECL;
                }
            }
            i = 8;
            str2 = "";
            i2 = 63;
        } else if (read != 33) {
            str = new StringBuffer().append("illegal: <").append(read).toString();
            error(str);
            return 9;
        } else if (peek(0) == 45) {
            i = 9;
            str2 = "--";
            i2 = 45;
        } else if (peek(0) == 91) {
            i = 5;
            str2 = "[CDATA[";
            i2 = 93;
            z = true;
        } else {
            i = 10;
            str2 = "DOCTYPE";
            i2 = -1;
        }
        for (int i5 = 0; i5 < str2.length(); i5++) {
            read(str2.charAt(i5));
        }
        if (i == 10) {
            parseDoctype(z);
        } else {
            int i6 = 0;
            while (true) {
                int read2 = read();
                if (read2 == -1) {
                    str = UNEXPECTED_EOF;
                    break;
                }
                if (z) {
                    push(read2);
                }
                if ((i2 == 63 || read2 == i2) && peek(0) == i2 && peek(1) == 62) {
                    if (i2 == 45 && i6 == 45) {
                        error("illegal comment delimiter: --->");
                    }
                    read();
                    read();
                    if (z && i2 != 63) {
                        this.txtPos--;
                    }
                } else {
                    i6 = read2;
                }
            }
            error(str);
            return 9;
        }
        return i;
    }

    private final void parseStartTag(boolean z) throws IOException, XmlPullParserException {
        if (!z) {
            read();
        }
        this.name = readName();
        this.attributeCount = 0;
        while (true) {
            skip();
            int peek2 = peek(0);
            if (!z) {
                if (peek2 != 47) {
                    if (peek2 == 62 && !z) {
                        read();
                        break;
                    }
                } else {
                    this.degenerated = true;
                    read();
                    skip();
                    read(Typography.greater);
                    break;
                }
            } else if (peek2 == 63) {
                read();
                read(Typography.greater);
                return;
            }
            if (peek2 == -1) {
                error(UNEXPECTED_EOF);
                return;
            }
            String readName = readName();
            if (readName.length() == 0) {
                error("attr name expected");
                break;
            }
            int i = this.attributeCount;
            this.attributeCount = i + 1;
            int i2 = i << 2;
            String[] ensureCapacity = ensureCapacity(this.attributes, i2 + 4);
            this.attributes = ensureCapacity;
            int i3 = i2 + 1;
            ensureCapacity[i2] = "";
            int i4 = i3 + 1;
            ensureCapacity[i3] = null;
            int i5 = i4 + 1;
            ensureCapacity[i4] = readName;
            skip();
            if (peek(0) != 61) {
                error(new StringBuffer().append("Attr.value missing f. ").append(readName).toString());
                this.attributes[i5] = "1";
            } else {
                read(Tag.KEY_VALUE_SEPARATOR);
                skip();
                int peek3 = peek(0);
                if (peek3 == 39 || peek3 == 34) {
                    read();
                } else {
                    error("attr value delimiter missing!");
                    peek3 = 32;
                }
                int i6 = this.txtPos;
                pushText(peek3, true);
                this.attributes[i5] = get(i6);
                this.txtPos = i6;
                if (peek3 != 32) {
                    read();
                }
            }
        }
        int i7 = this.depth;
        this.depth = i7 + 1;
        int i8 = i7 << 2;
        String[] ensureCapacity2 = ensureCapacity(this.elementStack, i8 + 4);
        this.elementStack = ensureCapacity2;
        ensureCapacity2[i8 + 3] = this.name;
        int i9 = this.depth;
        int[] iArr = this.nspCounts;
        if (i9 >= iArr.length) {
            int[] iArr2 = new int[(i9 + 4)];
            System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
            this.nspCounts = iArr2;
        }
        int[] iArr3 = this.nspCounts;
        int i10 = this.depth;
        iArr3[i10] = iArr3[i10 - 1];
        if (this.processNsp) {
            adjustNsp();
        } else {
            this.namespace = "";
        }
        String[] strArr = this.elementStack;
        strArr[i8] = this.namespace;
        strArr[i8 + 1] = this.prefix;
        strArr[i8 + 2] = this.name;
    }

    private final int peek(int i) throws IOException {
        int i2;
        while (i >= this.peekCount) {
            char[] cArr = this.srcBuf;
            if (cArr.length <= 1) {
                i2 = this.reader.read();
            } else {
                int i3 = this.srcPos;
                if (i3 < this.srcCount) {
                    this.srcPos = i3 + 1;
                    i2 = cArr[i3];
                } else {
                    int read = this.reader.read(cArr, 0, cArr.length);
                    this.srcCount = read;
                    char c = read <= 0 ? 65535 : this.srcBuf[0];
                    this.srcPos = 1;
                    i2 = c;
                }
            }
            if (i2 == 13) {
                this.wasCR = true;
                int[] iArr = this.peek;
                int i4 = this.peekCount;
                this.peekCount = i4 + 1;
                iArr[i4] = 10;
            } else {
                if (i2 != 10) {
                    int[] iArr2 = this.peek;
                    int i5 = this.peekCount;
                    this.peekCount = i5 + 1;
                    iArr2[i5] = i2;
                } else if (!this.wasCR) {
                    int[] iArr3 = this.peek;
                    int i6 = this.peekCount;
                    this.peekCount = i6 + 1;
                    iArr3[i6] = 10;
                }
                this.wasCR = false;
            }
        }
        return this.peek[i];
    }

    private final int peekType() throws IOException {
        int peek2 = peek(0);
        if (peek2 == -1) {
            return 1;
        }
        if (peek2 == 38) {
            return 6;
        }
        if (peek2 != 60) {
            return 4;
        }
        int peek3 = peek(1);
        if (peek3 == 33) {
            return 999;
        }
        if (peek3 != 47) {
            return peek3 != 63 ? 2 : 999;
        }
        return 3;
    }

    private final void push(int i) {
        this.isWhitespace &= i <= 32;
        int i2 = this.txtPos;
        char[] cArr = this.txtBuf;
        if (i2 == cArr.length) {
            char[] cArr2 = new char[(((i2 * 4) / 3) + 4)];
            System.arraycopy(cArr, 0, cArr2, 0, i2);
            this.txtBuf = cArr2;
        }
        char[] cArr3 = this.txtBuf;
        int i3 = this.txtPos;
        this.txtPos = i3 + 1;
        cArr3[i3] = (char) i;
    }

    private final void pushEntity() throws IOException, XmlPullParserException {
        push(read());
        int i = this.txtPos;
        while (true) {
            int read = read();
            if (read == 59) {
                String str = get(i);
                boolean z = true;
                this.txtPos = i - 1;
                if (this.token && this.type == 6) {
                    this.name = str;
                }
                if (str.charAt(0) == '#') {
                    push(str.charAt(1) == 'x' ? Integer.parseInt(str.substring(2), 16) : Integer.parseInt(str.substring(1)));
                    return;
                }
                String str2 = (String) this.entityMap.get(str);
                if (str2 != null) {
                    z = false;
                }
                this.unresolved = z;
                if (!z) {
                    for (int i2 = 0; i2 < str2.length(); i2++) {
                        push(str2.charAt(i2));
                    }
                    return;
                } else if (!this.token) {
                    error(new StringBuffer().append("unresolved: &").append(str).append(";").toString());
                    return;
                } else {
                    return;
                }
            } else if (read >= 128 || ((read >= 48 && read <= 57) || ((read >= 97 && read <= 122) || ((read >= 65 && read <= 90) || read == 95 || read == 45 || read == 35)))) {
                push(read);
            } else {
                if (!this.relaxed) {
                    error("unterminated entity ref");
                }
                if (read != -1) {
                    push(read);
                    return;
                }
                return;
            }
        }
    }

    private final void pushText(int i, boolean z) throws IOException, XmlPullParserException {
        int peek2 = peek(0);
        int i2 = 0;
        while (peek2 != -1 && peek2 != i) {
            int i3 = 32;
            if (i != 32 || (peek2 > 32 && peek2 != 62)) {
                if (peek2 != 38) {
                    if (peek2 == 10 && this.type == 2) {
                        read();
                    } else {
                        i3 = read();
                    }
                    push(i3);
                } else if (z) {
                    pushEntity();
                } else {
                    return;
                }
                if (peek2 == 62 && i2 >= 2 && i != 93) {
                    error("Illegal: ]]>");
                }
                i2 = peek2 == 93 ? i2 + 1 : 0;
                peek2 = peek(0);
            } else {
                return;
            }
        }
    }

    private final int read() throws IOException {
        int i;
        if (this.peekCount == 0) {
            i = peek(0);
        } else {
            int[] iArr = this.peek;
            int i2 = iArr[0];
            iArr[0] = iArr[1];
            i = i2;
        }
        this.peekCount--;
        this.column++;
        if (i == 10) {
            this.line++;
            this.column = 1;
        }
        return i;
    }

    private final void read(char c) throws IOException, XmlPullParserException {
        int read = read();
        if (read != c) {
            error(new StringBuffer().append("expected: '").append(c).append("' actual: '").append((char) read).append("'").toString());
        }
    }

    private final String readName() throws IOException, XmlPullParserException {
        int i = this.txtPos;
        int peek2 = peek(0);
        if ((peek2 < 97 || peek2 > 122) && ((peek2 < 65 || peek2 > 90) && peek2 != 95 && peek2 != 58 && peek2 < 192 && !this.relaxed)) {
            error("name expected");
        }
        while (true) {
            push(read());
            int peek3 = peek(0);
            if ((peek3 < 97 || peek3 > 122) && ((peek3 < 65 || peek3 > 90) && !((peek3 >= 48 && peek3 <= 57) || peek3 == 95 || peek3 == 45 || peek3 == 58 || peek3 == 46 || peek3 >= 183))) {
                String str = get(i);
                this.txtPos = i;
                return str;
            }
        }
    }

    private final void skip() throws IOException {
        while (true) {
            int peek2 = peek(0);
            if (peek2 <= 32 && peek2 != -1) {
                read();
            } else {
                return;
            }
        }
    }

    public void defineEntityReplacementText(String str, String str2) throws XmlPullParserException {
        Hashtable hashtable = this.entityMap;
        if (hashtable != null) {
            hashtable.put(str, str2);
            return;
        }
        throw new RuntimeException("entity replacement text must be defined after setInput!");
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
        return this.column;
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
        if (isProp(str, false, "relaxed")) {
            return this.relaxed;
        }
        return false;
    }

    public String getInputEncoding() {
        return this.encoding;
    }

    public int getLineNumber() {
        return this.line;
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
        String text;
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
                text = getText();
            } else if (this.isWhitespace) {
                text = "(whitespace)";
            } else {
                text = getText();
                if (text.length() > 16) {
                    text = new StringBuffer().append(text.substring(0, 16)).append("...").toString();
                }
            }
            stringBuffer.append(text);
        }
        stringBuffer.append(new StringBuffer().append("@").append(this.line).append(":").append(this.column).toString());
        if (this.location != null) {
            stringBuffer.append(" in ");
            stringBuffer.append(this.location);
        } else if (this.reader != null) {
            stringBuffer.append(" in ");
            stringBuffer.append(this.reader.toString());
        }
        return stringBuffer.toString();
    }

    public String getPrefix() {
        return this.prefix;
    }

    public Object getProperty(String str) {
        if (isProp(str, true, "xmldecl-version")) {
            return this.version;
        }
        if (isProp(str, true, "xmldecl-standalone")) {
            return this.standalone;
        }
        if (!isProp(str, true, "location")) {
            return null;
        }
        Object obj = this.location;
        return obj != null ? obj : this.reader.toString();
    }

    public String getText() {
        int i = this.type;
        if (i < 4 || (i == 6 && this.unresolved)) {
            return null;
        }
        return get(0);
    }

    public char[] getTextCharacters(int[] iArr) {
        int i = this.type;
        if (i < 4) {
            iArr[0] = -1;
            iArr[1] = -1;
            return null;
        } else if (i == 6) {
            iArr[0] = 0;
            iArr[1] = this.name.length();
            return this.name.toCharArray();
        } else {
            iArr[0] = 0;
            iArr[1] = this.txtPos;
            return this.txtBuf;
        }
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
        this.txtPos = 0;
        this.isWhitespace = true;
        this.token = false;
        int i = 9999;
        while (true) {
            nextImpl();
            int i2 = this.type;
            if (i2 < i) {
                i = i2;
            }
            if (i > 6 || (i >= 4 && peekType() >= 4)) {
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
        this.txtPos = 0;
        this.token = true;
        nextImpl();
        return this.type;
    }

    public void require(int i, String str, String str2) throws XmlPullParserException, IOException {
        if (i != this.type || ((str != null && !str.equals(getNamespace())) || (str2 != null && !str2.equals(getName())))) {
            exception(new StringBuffer().append("expected: ").append(XmlPullParser.TYPES[i]).append(" {").append(str).append("}").append(str2).toString());
        }
    }

    public void setFeature(String str, boolean z) throws XmlPullParserException {
        if (XmlPullParser.FEATURE_PROCESS_NAMESPACES.equals(str)) {
            this.processNsp = z;
        } else if (isProp(str, false, "relaxed")) {
            this.relaxed = z;
        } else {
            exception(new StringBuffer().append("unsupported feature: ").append(str).toString());
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003f, code lost:
        r3 = r14.read();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0043, code lost:
        if (r3 != -1) goto L_0x0047;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0047, code lost:
        r4 = r13.srcBuf;
        r7 = r13.srcCount;
        r10 = r7 + 1;
        r13.srcCount = r10;
        r4[r7] = (char) r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0054, code lost:
        if (r3 != 62) goto L_0x003f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0056, code lost:
        r3 = new java.lang.String(r4, 0, r10);
        r4 = r3.indexOf("encoding");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0061, code lost:
        if (r4 == -1) goto L_0x00ae;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0069, code lost:
        if (r3.charAt(r4) == '\"') goto L_0x0076;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0071, code lost:
        if (r3.charAt(r4) == '\'') goto L_0x0076;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0073, code lost:
        r4 = r4 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0076, code lost:
        r7 = r4 + 1;
        r3 = r3.substring(r7, r3.indexOf(r3.charAt(r4), r7));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x009e, code lost:
        r1 = "UTF-16BE";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00ac, code lost:
        r1 = "UTF-32LE";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00ae, code lost:
        r3 = r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00af, code lost:
        r4 = -65536 & r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00b5, code lost:
        if (r4 != -16842752) goto L_0x00c6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00b7, code lost:
        r1 = r13.srcBuf;
        r1[0] = (char) ((r1[2] << 8) | r1[3]);
        r13.srcCount = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00c8, code lost:
        if (r4 != -131072) goto L_0x00d9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00ca, code lost:
        r2 = r13.srcBuf;
        r2[0] = (char) ((r2[3] << 8) | r2[2]);
        r13.srcCount = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00de, code lost:
        if ((r6 & androidx.core.view.InputDeviceCompat.SOURCE_ANY) != -272908544) goto L_0x00ea;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00e0, code lost:
        r1 = r13.srcBuf;
        r1[0] = r1[3];
        r13.srcCount = 1;
        r1 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00ea, code lost:
        r1 = r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setInput(java.io.InputStream r14, java.lang.String r15) throws org.xmlpull.p018v1.XmlPullParserException {
        /*
            r13 = this;
            r0 = 0
            r13.srcPos = r0
            r13.srcCount = r0
            if (r14 == 0) goto L_0x011e
            java.lang.String r1 = "UTF-16LE"
            java.lang.String r2 = "UTF-16BE"
            java.lang.String r3 = "UTF-32BE"
            java.lang.String r4 = "UTF-32LE"
            java.lang.String r5 = "UTF-8"
            if (r15 != 0) goto L_0x00ec
            r6 = r0
        L_0x0014:
            int r7 = r13.srcCount     // Catch:{ Exception -> 0x0100 }
            r8 = 4
            r9 = -1
            if (r7 >= r8) goto L_0x0030
            int r7 = r14.read()     // Catch:{ Exception -> 0x0100 }
            if (r7 != r9) goto L_0x0021
            goto L_0x0030
        L_0x0021:
            int r6 = r6 << 8
            r6 = r6 | r7
            char[] r8 = r13.srcBuf     // Catch:{ Exception -> 0x0100 }
            int r9 = r13.srcCount     // Catch:{ Exception -> 0x0100 }
            int r10 = r9 + 1
            r13.srcCount = r10     // Catch:{ Exception -> 0x0100 }
            char r7 = (char) r7     // Catch:{ Exception -> 0x0100 }
            r8[r9] = r7     // Catch:{ Exception -> 0x0100 }
            goto L_0x0014
        L_0x0030:
            int r7 = r13.srcCount     // Catch:{ Exception -> 0x0100 }
            if (r7 != r8) goto L_0x00ec
            r7 = 63
            r8 = 2
            r10 = 60
            r11 = 1
            switch(r6) {
                case -131072: goto L_0x00aa;
                case 60: goto L_0x00a3;
                case 65279: goto L_0x00a0;
                case 3932223: goto L_0x0096;
                case 1006632960: goto L_0x008f;
                case 1006649088: goto L_0x0085;
                case 1010792557: goto L_0x003f;
                default: goto L_0x003d;
            }     // Catch:{ Exception -> 0x0100 }
        L_0x003d:
            goto L_0x00ae
        L_0x003f:
            int r3 = r14.read()     // Catch:{ Exception -> 0x0100 }
            if (r3 != r9) goto L_0x0047
            goto L_0x00ae
        L_0x0047:
            char[] r4 = r13.srcBuf     // Catch:{ Exception -> 0x0100 }
            int r7 = r13.srcCount     // Catch:{ Exception -> 0x0100 }
            int r10 = r7 + 1
            r13.srcCount = r10     // Catch:{ Exception -> 0x0100 }
            char r12 = (char) r3     // Catch:{ Exception -> 0x0100 }
            r4[r7] = r12     // Catch:{ Exception -> 0x0100 }
            r7 = 62
            if (r3 != r7) goto L_0x003f
            java.lang.String r3 = new java.lang.String     // Catch:{ Exception -> 0x0100 }
            r3.<init>(r4, r0, r10)     // Catch:{ Exception -> 0x0100 }
            java.lang.String r4 = "encoding"
            int r4 = r3.indexOf(r4)     // Catch:{ Exception -> 0x0100 }
            if (r4 == r9) goto L_0x00ae
        L_0x0063:
            char r7 = r3.charAt(r4)     // Catch:{ Exception -> 0x0100 }
            r9 = 34
            if (r7 == r9) goto L_0x0076
            char r7 = r3.charAt(r4)     // Catch:{ Exception -> 0x0100 }
            r9 = 39
            if (r7 == r9) goto L_0x0076
            int r4 = r4 + 1
            goto L_0x0063
        L_0x0076:
            int r7 = r4 + 1
            char r4 = r3.charAt(r4)     // Catch:{ Exception -> 0x0100 }
            int r4 = r3.indexOf(r4, r7)     // Catch:{ Exception -> 0x0100 }
            java.lang.String r3 = r3.substring(r7, r4)     // Catch:{ Exception -> 0x0100 }
            goto L_0x00af
        L_0x0085:
            char[] r2 = r13.srcBuf     // Catch:{ Exception -> 0x0100 }
            r2[r0] = r10     // Catch:{ Exception -> 0x0100 }
            r2[r11] = r7     // Catch:{ Exception -> 0x0100 }
            r13.srcCount = r8     // Catch:{ Exception -> 0x0100 }
            goto L_0x00ed
        L_0x008f:
            char[] r1 = r13.srcBuf     // Catch:{ Exception -> 0x0100 }
            r1[r0] = r10     // Catch:{ Exception -> 0x0100 }
            r13.srcCount = r11     // Catch:{ Exception -> 0x0100 }
            goto L_0x00ac
        L_0x0096:
            char[] r1 = r13.srcBuf     // Catch:{ Exception -> 0x0100 }
            r1[r0] = r10     // Catch:{ Exception -> 0x0100 }
            r1[r11] = r7     // Catch:{ Exception -> 0x0100 }
            r13.srcCount = r8     // Catch:{ Exception -> 0x0100 }
        L_0x009e:
            r1 = r2
            goto L_0x00ed
        L_0x00a0:
            r13.srcCount = r0     // Catch:{ Exception -> 0x0100 }
            goto L_0x00ea
        L_0x00a3:
            char[] r1 = r13.srcBuf     // Catch:{ Exception -> 0x0100 }
            r1[r0] = r10     // Catch:{ Exception -> 0x0100 }
            r13.srcCount = r11     // Catch:{ Exception -> 0x0100 }
            goto L_0x00ea
        L_0x00aa:
            r13.srcCount = r0     // Catch:{ Exception -> 0x0100 }
        L_0x00ac:
            r1 = r4
            goto L_0x00ed
        L_0x00ae:
            r3 = r15
        L_0x00af:
            r4 = -65536(0xffffffffffff0000, float:NaN)
            r4 = r4 & r6
            r7 = -16842752(0xfffffffffeff0000, float:-1.6947657E38)
            r9 = 3
            if (r4 != r7) goto L_0x00c6
            char[] r1 = r13.srcBuf     // Catch:{ Exception -> 0x0100 }
            char r3 = r1[r8]     // Catch:{ Exception -> 0x0100 }
            int r3 = r3 << 8
            char r4 = r1[r9]     // Catch:{ Exception -> 0x0100 }
            r3 = r3 | r4
            char r3 = (char) r3     // Catch:{ Exception -> 0x0100 }
            r1[r0] = r3     // Catch:{ Exception -> 0x0100 }
            r13.srcCount = r11     // Catch:{ Exception -> 0x0100 }
            goto L_0x009e
        L_0x00c6:
            r2 = -131072(0xfffffffffffe0000, float:NaN)
            if (r4 != r2) goto L_0x00d9
            char[] r2 = r13.srcBuf     // Catch:{ Exception -> 0x0100 }
            char r3 = r2[r9]     // Catch:{ Exception -> 0x0100 }
            int r3 = r3 << 8
            char r4 = r2[r8]     // Catch:{ Exception -> 0x0100 }
            r3 = r3 | r4
            char r3 = (char) r3     // Catch:{ Exception -> 0x0100 }
            r2[r0] = r3     // Catch:{ Exception -> 0x0100 }
            r13.srcCount = r11     // Catch:{ Exception -> 0x0100 }
            goto L_0x00ed
        L_0x00d9:
            r1 = r6 & -256(0xffffffffffffff00, float:NaN)
            r2 = -272908544(0xffffffffefbbbf00, float:-1.162092E29)
            if (r1 != r2) goto L_0x00ea
            char[] r1 = r13.srcBuf     // Catch:{ Exception -> 0x0100 }
            char r2 = r1[r9]     // Catch:{ Exception -> 0x0100 }
            r1[r0] = r2     // Catch:{ Exception -> 0x0100 }
            r13.srcCount = r11     // Catch:{ Exception -> 0x0100 }
            r1 = r5
            goto L_0x00ed
        L_0x00ea:
            r1 = r3
            goto L_0x00ed
        L_0x00ec:
            r1 = r15
        L_0x00ed:
            if (r1 != 0) goto L_0x00f0
            goto L_0x00f1
        L_0x00f0:
            r5 = r1
        L_0x00f1:
            int r0 = r13.srcCount     // Catch:{ Exception -> 0x0100 }
            java.io.InputStreamReader r1 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x0100 }
            r1.<init>(r14, r5)     // Catch:{ Exception -> 0x0100 }
            r13.setInput(r1)     // Catch:{ Exception -> 0x0100 }
            r13.encoding = r15     // Catch:{ Exception -> 0x0100 }
            r13.srcCount = r0     // Catch:{ Exception -> 0x0100 }
            return
        L_0x0100:
            r14 = move-exception
            org.xmlpull.v1.XmlPullParserException r15 = new org.xmlpull.v1.XmlPullParserException
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r0.<init>()
            java.lang.String r1 = "Invalid stream or encoding: "
            java.lang.StringBuffer r0 = r0.append(r1)
            java.lang.String r1 = r14.toString()
            java.lang.StringBuffer r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r15.<init>(r0, r13, r14)
            throw r15
        L_0x011e:
            java.lang.IllegalArgumentException r14 = new java.lang.IllegalArgumentException
            r14.<init>()
            throw r14
        */
        throw new UnsupportedOperationException("Method not decompiled: org.kxml2.p015io.KXmlParser.setInput(java.io.InputStream, java.lang.String):void");
    }

    public void setInput(Reader reader2) throws XmlPullParserException {
        this.reader = reader2;
        this.line = 1;
        this.column = 0;
        this.type = 0;
        this.name = null;
        this.namespace = null;
        this.degenerated = false;
        this.attributeCount = -1;
        this.encoding = null;
        this.version = null;
        this.standalone = null;
        if (reader2 != null) {
            this.srcPos = 0;
            this.srcCount = 0;
            this.peekCount = 0;
            this.depth = 0;
            Hashtable hashtable = new Hashtable();
            this.entityMap = hashtable;
            hashtable.put("amp", "&");
            this.entityMap.put("apos", "'");
            this.entityMap.put("gt", SimpleComparison.GREATER_THAN_OPERATION);
            this.entityMap.put("lt", SimpleComparison.LESS_THAN_OPERATION);
            this.entityMap.put("quot", AngleFormat.STR_SEC_SYMBOL);
        }
    }

    public void setProperty(String str, Object obj) throws XmlPullParserException {
        if (isProp(str, true, "location")) {
            this.location = obj;
            return;
        }
        throw new XmlPullParserException(new StringBuffer().append("unsupported property: ").append(str).toString());
    }

    public void skipSubTree() throws XmlPullParserException, IOException {
        require(2, (String) null, (String) null);
        int i = 1;
        while (i > 0) {
            int next = next();
            if (next == 3) {
                i--;
            } else if (next == 2) {
                i++;
            }
        }
    }
}
