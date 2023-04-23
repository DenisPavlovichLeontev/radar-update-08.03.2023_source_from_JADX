package org.jsoup.helper;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import org.jsoup.UncheckedIOException;
import org.jsoup.internal.ConstrainableInputStream;
import org.jsoup.internal.Normalizer;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

public final class DataUtil {
    public static final Charset UTF_8;
    static final int boundaryLength = 32;
    static final int bufferSize = 32768;
    private static final Pattern charsetPattern = Pattern.compile("(?i)\\bcharset=\\s*(?:[\"'])?([^\\s,;\"']*)");
    static final String defaultCharsetName;
    private static final int firstReadBufferSize = 5120;
    private static final char[] mimeBoundaryChars = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    static {
        Charset forName = Charset.forName("UTF-8");
        UTF_8 = forName;
        defaultCharsetName = forName.name();
    }

    private DataUtil() {
    }

    public static Document load(File file, @Nullable String str, String str2) throws IOException {
        return load(file, str, str2, Parser.htmlParser());
    }

    public static Document load(File file, @Nullable String str, String str2, Parser parser) throws IOException {
        InputStream fileInputStream = new FileInputStream(file);
        String lowerCase = Normalizer.lowerCase(file.getName());
        if (lowerCase.endsWith(".gz") || lowerCase.endsWith(".z")) {
            try {
                fileInputStream = fileInputStream.read() == 31 && fileInputStream.read() == 139 ? new GZIPInputStream(new FileInputStream(file)) : new FileInputStream(file);
            } finally {
                fileInputStream.close();
            }
        }
        return parseInputStream(fileInputStream, str, str2, parser);
    }

    public static Document load(@WillClose InputStream inputStream, @Nullable String str, String str2) throws IOException {
        return parseInputStream(inputStream, str, str2, Parser.htmlParser());
    }

    public static Document load(@WillClose InputStream inputStream, @Nullable String str, String str2, Parser parser) throws IOException {
        return parseInputStream(inputStream, str, str2, parser);
    }

    static void crossStreams(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[32768];
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                outputStream.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }

    static Document parseInputStream(@Nullable @WillClose InputStream inputStream, @Nullable String str, String str2, Parser parser) throws IOException {
        BufferedReader bufferedReader;
        Document document;
        XmlDeclaration xmlDeclaration;
        if (inputStream == null) {
            return new Document(str2);
        }
        boolean z = false;
        ConstrainableInputStream wrap = ConstrainableInputStream.wrap(inputStream, 32768, 0);
        try {
            wrap.mark(32768);
            ByteBuffer readToByteBuffer = readToByteBuffer(wrap, 5119);
            boolean z2 = wrap.read() == -1;
            wrap.reset();
            BomCharset detectCharsetFromBom = detectCharsetFromBom(readToByteBuffer);
            if (detectCharsetFromBom != null) {
                str = detectCharsetFromBom.charset;
            }
            Document document2 = null;
            if (str == null) {
                CharBuffer decode = UTF_8.decode(readToByteBuffer);
                if (decode.hasArray()) {
                    document = parser.parseInput((Reader) new CharArrayReader(decode.array(), decode.arrayOffset(), decode.limit()), str2);
                } else {
                    document = parser.parseInput(decode.toString(), str2);
                }
                Iterator it = document.select("meta[http-equiv=content-type], meta[charset]").iterator();
                String str3 = null;
                while (it.hasNext()) {
                    Element element = (Element) it.next();
                    if (element.hasAttr("http-equiv")) {
                        str3 = getCharsetFromContentType(element.attr("content"));
                    }
                    if (str3 == null && element.hasAttr("charset")) {
                        str3 = element.attr("charset");
                        continue;
                    }
                    if (str3 != null) {
                        break;
                    }
                }
                if (str3 == null && document.childNodeSize() > 0) {
                    Node childNode = document.childNode(0);
                    if (childNode instanceof XmlDeclaration) {
                        xmlDeclaration = (XmlDeclaration) childNode;
                    } else {
                        if (childNode instanceof Comment) {
                            Comment comment = (Comment) childNode;
                            if (comment.isXmlDeclaration()) {
                                xmlDeclaration = comment.asXmlDeclaration();
                            }
                        }
                        xmlDeclaration = null;
                    }
                    if (xmlDeclaration != null && xmlDeclaration.name().equalsIgnoreCase("xml")) {
                        str3 = xmlDeclaration.attr("encoding");
                    }
                }
                String validateCharset = validateCharset(str3);
                if (validateCharset != null && !validateCharset.equalsIgnoreCase(defaultCharsetName)) {
                    str = validateCharset.trim().replaceAll("[\"']", "");
                } else if (z2) {
                    document2 = document;
                }
            } else {
                Validate.notEmpty(str, "Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML");
            }
            if (document2 == null) {
                if (str == null) {
                    str = defaultCharsetName;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(wrap, Charset.forName(str)), 32768);
                if (detectCharsetFromBom != null) {
                    if (detectCharsetFromBom.offset) {
                        if (bufferedReader.skip(1) == 1) {
                            z = true;
                        }
                        Validate.isTrue(z);
                    }
                }
                document2 = parser.parseInput((Reader) bufferedReader, str2);
                Charset forName = str.equals(defaultCharsetName) ? UTF_8 : Charset.forName(str);
                document2.outputSettings().charset(forName);
                if (!forName.canEncode()) {
                    document2.charset(UTF_8);
                }
                bufferedReader.close();
            }
            wrap.close();
            return document2;
        } catch (UncheckedIOException e) {
            throw e.ioException();
        } catch (UncheckedIOException e2) {
            throw e2.ioException();
        } catch (Throwable th) {
            wrap.close();
            throw th;
        }
    }

    public static ByteBuffer readToByteBuffer(InputStream inputStream, int i) throws IOException {
        Validate.isTrue(i >= 0, "maxSize must be 0 (unlimited) or larger");
        return ConstrainableInputStream.wrap(inputStream, 32768, i).readToByteBuffer(i);
    }

    static ByteBuffer emptyByteBuffer() {
        return ByteBuffer.allocate(0);
    }

    @Nullable
    static String getCharsetFromContentType(@Nullable String str) {
        if (str == null) {
            return null;
        }
        Matcher matcher = charsetPattern.matcher(str);
        if (matcher.find()) {
            return validateCharset(matcher.group(1).trim().replace("charset=", ""));
        }
        return null;
    }

    @Nullable
    private static String validateCharset(@Nullable String str) {
        if (!(str == null || str.length() == 0)) {
            String replaceAll = str.trim().replaceAll("[\"']", "");
            try {
                if (Charset.isSupported(replaceAll)) {
                    return replaceAll;
                }
                String upperCase = replaceAll.toUpperCase(Locale.ENGLISH);
                if (Charset.isSupported(upperCase)) {
                    return upperCase;
                }
            } catch (IllegalCharsetNameException unused) {
            }
        }
        return null;
    }

    static String mimeBoundary() {
        StringBuilder borrowBuilder = StringUtil.borrowBuilder();
        Random random = new Random();
        for (int i = 0; i < 32; i++) {
            char[] cArr = mimeBoundaryChars;
            borrowBuilder.append(cArr[random.nextInt(cArr.length)]);
        }
        return StringUtil.releaseBuilder(borrowBuilder);
    }

    @Nullable
    private static BomCharset detectCharsetFromBom(ByteBuffer byteBuffer) {
        byteBuffer.mark();
        byte[] bArr = new byte[4];
        if (byteBuffer.remaining() >= 4) {
            byteBuffer.get(bArr);
            byteBuffer.rewind();
        }
        byte b = bArr[0];
        if ((b == 0 && bArr[1] == 0 && bArr[2] == -2 && bArr[3] == -1) || (b == -1 && bArr[1] == -2 && bArr[2] == 0 && bArr[3] == 0)) {
            return new BomCharset("UTF-32", false);
        }
        if ((b == -2 && bArr[1] == -1) || (b == -1 && bArr[1] == -2)) {
            return new BomCharset("UTF-16", false);
        }
        if (b == -17 && bArr[1] == -69 && bArr[2] == -65) {
            return new BomCharset("UTF-8", true);
        }
        return null;
    }

    private static class BomCharset {
        /* access modifiers changed from: private */
        public final String charset;
        /* access modifiers changed from: private */
        public final boolean offset;

        public BomCharset(String str, boolean z) {
            this.charset = str;
            this.offset = z;
        }
    }
}
