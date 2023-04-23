package p005ar.com.hjg.pngj;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.logging.Logger;
import kotlin.UByte;
import mil.nga.geopackage.property.PropertyConstants;

/* renamed from: ar.com.hjg.pngj.PngHelperInternal */
public final class PngHelperInternal {
    private static ThreadLocal<Boolean> DEBUG = new ThreadLocal<Boolean>() {
        /* access modifiers changed from: protected */
        public Boolean initialValue() {
            return Boolean.FALSE;
        }
    };
    public static final String KEY_LOGGER = "ar.com.pngj";
    public static final Logger LOGGER = Logger.getLogger(KEY_LOGGER);
    public static Charset charsetLatin1 = Charset.forName("ISO-8859-1");
    public static String charsetLatin1name = "ISO-8859-1";
    public static Charset charsetUTF8 = Charset.forName("UTF-8");
    public static String charsetUTF8name = "UTF-8";

    public static int doubleToInt100000(double d) {
        return (int) ((d * 100000.0d) + 0.5d);
    }

    static final int filterPaethPredictor(int i, int i2, int i3) {
        int i4 = (i + i2) - i3;
        int i5 = i4 >= i ? i4 - i : i - i4;
        int i6 = i4 >= i2 ? i4 - i2 : i2 - i4;
        int i7 = i4 >= i3 ? i4 - i3 : i3 - i4;
        return (i5 > i6 || i5 > i7) ? i6 <= i7 ? i2 : i3 : i;
    }

    public static int filterRowNone(int i) {
        return i & 255;
    }

    public static int filterRowSub(int i, int i2) {
        return (i - i2) & 255;
    }

    public static int filterRowUp(int i, int i2) {
        return (i - i2) & 255;
    }

    public static double intToDouble100000(int i) {
        return ((double) i) / 100000.0d;
    }

    public static byte[] getPngIdSignature() {
        return new byte[]{-119, 80, 78, 71, 13, 10, 26, 10};
    }

    public static int readByte(InputStream inputStream) {
        try {
            return inputStream.read();
        } catch (IOException e) {
            throw new PngjInputException("error reading byte", e);
        }
    }

    public static int readInt2(InputStream inputStream) {
        try {
            int read = inputStream.read();
            int read2 = inputStream.read();
            if (read == -1 || read2 == -1) {
                return -1;
            }
            return read2 | (read << 8);
        } catch (IOException e) {
            throw new PngjInputException("error reading Int2", e);
        }
    }

    public static int readInt4(InputStream inputStream) {
        try {
            int read = inputStream.read();
            int read2 = inputStream.read();
            int read3 = inputStream.read();
            int read4 = inputStream.read();
            if (read == -1 || read2 == -1 || read3 == -1 || read4 == -1) {
                return -1;
            }
            return (read << 24) | (read2 << 16) | ((read3 << 8) + read4);
        } catch (IOException e) {
            throw new PngjInputException("error reading Int4", e);
        }
    }

    public static int readInt1fromByte(byte[] bArr, int i) {
        return bArr[i] & UByte.MAX_VALUE;
    }

    public static int readInt2fromBytes(byte[] bArr, int i) {
        return (bArr[i + 1] & UByte.MAX_VALUE) | ((bArr[i] & UByte.MAX_VALUE) << 8);
    }

    public static final int readInt4fromBytes(byte[] bArr, int i) {
        return (bArr[i + 3] & UByte.MAX_VALUE) | ((bArr[i] & UByte.MAX_VALUE) << 24) | ((bArr[i + 1] & UByte.MAX_VALUE) << 16) | ((bArr[i + 2] & UByte.MAX_VALUE) << 8);
    }

    public static void writeByte(OutputStream outputStream, byte b) {
        try {
            outputStream.write(b);
        } catch (IOException e) {
            throw new PngjOutputException((Throwable) e);
        }
    }

    public static void writeByte(OutputStream outputStream, byte[] bArr) {
        try {
            outputStream.write(bArr);
        } catch (IOException e) {
            throw new PngjOutputException((Throwable) e);
        }
    }

    public static void writeInt2(OutputStream outputStream, int i) {
        writeBytes(outputStream, new byte[]{(byte) ((i >> 8) & 255), (byte) (i & 255)});
    }

    public static void writeInt4(OutputStream outputStream, int i) {
        byte[] bArr = new byte[4];
        writeInt4tobytes(i, bArr, 0);
        writeBytes(outputStream, bArr);
    }

    public static void writeInt2tobytes(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) ((i >> 8) & 255);
        bArr[i2 + 1] = (byte) (i & 255);
    }

    public static void writeInt4tobytes(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) ((i >> 24) & 255);
        bArr[i2 + 1] = (byte) ((i >> 16) & 255);
        bArr[i2 + 2] = (byte) ((i >> 8) & 255);
        bArr[i2 + 3] = (byte) (i & 255);
    }

    public static void readBytes(InputStream inputStream, byte[] bArr, int i, int i2) {
        if (i2 != 0) {
            int i3 = 0;
            while (i3 < i2) {
                try {
                    int read = inputStream.read(bArr, i + i3, i2 - i3);
                    if (read >= 1) {
                        i3 += read;
                    } else {
                        throw new PngjInputException("error reading bytes, " + read + " !=" + i2);
                    }
                } catch (IOException e) {
                    throw new PngjInputException("error reading", e);
                }
            }
        }
    }

    public static void skipBytes(InputStream inputStream, long j) {
        while (j > 0) {
            try {
                long skip = inputStream.skip(j);
                int i = (skip > 0 ? 1 : (skip == 0 ? 0 : -1));
                if (i > 0) {
                    j -= skip;
                } else if (i != 0) {
                    throw new IOException("skip() returned a negative value ???");
                } else if (inputStream.read() != -1) {
                    j--;
                } else {
                    return;
                }
            } catch (IOException e) {
                throw new PngjInputException((Throwable) e);
            }
        }
    }

    public static void writeBytes(OutputStream outputStream, byte[] bArr) {
        try {
            outputStream.write(bArr);
        } catch (IOException e) {
            throw new PngjOutputException((Throwable) e);
        }
    }

    public static void writeBytes(OutputStream outputStream, byte[] bArr, int i, int i2) {
        try {
            outputStream.write(bArr, i, i2);
        } catch (IOException e) {
            throw new PngjOutputException((Throwable) e);
        }
    }

    public static void logdebug(String str) {
        if (isDebug()) {
            PrintStream printStream = System.err;
            printStream.println("logdebug: " + str);
        }
    }

    public static int filterRowAverage(int i, int i2, int i3) {
        return (i - ((i2 + i3) / 2)) & 255;
    }

    public static int filterRowPaeth(int i, int i2, int i3, int i4) {
        return (i - filterPaethPredictor(i2, i3, i4)) & 255;
    }

    public static void debug(Object obj) {
        debug(obj, 1, true);
    }

    static void debug(Object obj, int i) {
        debug(obj, i, true);
    }

    public static InputStream istreamFromFile(File file) {
        try {
            return new FileInputStream(file);
        } catch (Exception e) {
            throw new PngjInputException("Could not open " + file, e);
        }
    }

    static OutputStream ostreamFromFile(File file) {
        return ostreamFromFile(file, true);
    }

    static OutputStream ostreamFromFile(File file, boolean z) {
        return PngHelperInternal2.ostreamFromFile(file, z);
    }

    static void debug(Object obj, int i, boolean z) {
        StackTraceElement stackTraceElement = new Exception().getStackTrace()[i + 1];
        String className = stackTraceElement.getClassName();
        String substring = className.substring(className.lastIndexOf(46) + 1);
        StringBuilder sb = new StringBuilder();
        sb.append(substring);
        sb.append(PropertyConstants.PROPERTY_DIVIDER);
        sb.append(stackTraceElement.getMethodName());
        sb.append("(");
        sb.append(stackTraceElement.getLineNumber());
        sb.append("): ");
        sb.append(obj == null ? null : obj.toString());
        System.err.println(sb.toString());
    }

    public static void setDebug(boolean z) {
        DEBUG.set(Boolean.valueOf(z));
    }

    public static boolean isDebug() {
        return DEBUG.get().booleanValue();
    }

    public static long getDigest(PngReader pngReader) {
        return pngReader.getSimpleDigest();
    }

    public static void initCrcForTests(PngReader pngReader) {
        pngReader.prepareSimpleDigestComputation();
    }

    public static long getRawIdatBytes(PngReader pngReader) {
        return pngReader.interlaced ? pngReader.getChunkseq().getDeinterlacer().getTotalRawBytes() : pngReader.imgInfo.getTotalRawBytes();
    }
}
