package p005ar.com.hjg.pngj.chunks;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import p005ar.com.hjg.pngj.PngHelperInternal;
import p005ar.com.hjg.pngj.PngjException;

/* renamed from: ar.com.hjg.pngj.chunks.ChunkHelper */
public class ChunkHelper {
    public static final String IDAT = "IDAT";
    public static final String IEND = "IEND";
    public static final String IHDR = "IHDR";
    public static final String PLTE = "PLTE";
    public static final String bKGD = "bKGD";
    public static final byte[] b_IDAT = toBytes("IDAT");
    public static final byte[] b_IEND = toBytes("IEND");
    public static final byte[] b_IHDR = toBytes("IHDR");
    public static final byte[] b_PLTE = toBytes("PLTE");
    public static final String cHRM = "cHRM";
    public static final String gAMA = "gAMA";
    public static final String hIST = "hIST";
    public static final String iCCP = "iCCP";
    public static final String iTXt = "iTXt";
    public static final String pHYs = "pHYs";
    public static final String sBIT = "sBIT";
    public static final String sPLT = "sPLT";
    public static final String sRGB = "sRGB";
    public static final String tEXt = "tEXt";
    public static final String tIME = "tIME";
    public static final String tRNS = "tRNS";
    private static byte[] tmpbuffer = new byte[4096];
    public static final String zTXt = "zTXt";

    ChunkHelper() {
    }

    public static byte[] toBytes(String str) {
        try {
            return str.getBytes(PngHelperInternal.charsetLatin1name);
        } catch (UnsupportedEncodingException e) {
            throw new PngBadCharsetException((Throwable) e);
        }
    }

    public static String toString(byte[] bArr) {
        try {
            return new String(bArr, PngHelperInternal.charsetLatin1name);
        } catch (UnsupportedEncodingException e) {
            throw new PngBadCharsetException((Throwable) e);
        }
    }

    public static String toString(byte[] bArr, int i, int i2) {
        try {
            return new String(bArr, i, i2, PngHelperInternal.charsetLatin1name);
        } catch (UnsupportedEncodingException e) {
            throw new PngBadCharsetException((Throwable) e);
        }
    }

    public static byte[] toBytesUTF8(String str) {
        try {
            return str.getBytes(PngHelperInternal.charsetUTF8name);
        } catch (UnsupportedEncodingException e) {
            throw new PngBadCharsetException((Throwable) e);
        }
    }

    public static String toStringUTF8(byte[] bArr) {
        try {
            return new String(bArr, PngHelperInternal.charsetUTF8name);
        } catch (UnsupportedEncodingException e) {
            throw new PngBadCharsetException((Throwable) e);
        }
    }

    public static String toStringUTF8(byte[] bArr, int i, int i2) {
        try {
            return new String(bArr, i, i2, PngHelperInternal.charsetUTF8name);
        } catch (UnsupportedEncodingException e) {
            throw new PngBadCharsetException((Throwable) e);
        }
    }

    public static boolean isCritical(String str) {
        return Character.isUpperCase(str.charAt(0));
    }

    public static boolean isPublic(String str) {
        return Character.isUpperCase(str.charAt(1));
    }

    public static boolean isSafeToCopy(String str) {
        return !Character.isUpperCase(str.charAt(3));
    }

    public static boolean isUnknown(PngChunk pngChunk) {
        return pngChunk instanceof PngChunkUNKNOWN;
    }

    public static int posNullByte(byte[] bArr) {
        for (int i = 0; i < bArr.length; i++) {
            if (bArr[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    public static boolean shouldLoad(String str, ChunkLoadBehaviour chunkLoadBehaviour) {
        int i;
        if (isCritical(str) || (i = C06871.$SwitchMap$ar$com$hjg$pngj$chunks$ChunkLoadBehaviour[chunkLoadBehaviour.ordinal()]) == 1) {
            return true;
        }
        if (i != 2) {
            return false;
        }
        return isSafeToCopy(str);
    }

    /* renamed from: ar.com.hjg.pngj.chunks.ChunkHelper$1 */
    static /* synthetic */ class C06871 {
        static final /* synthetic */ int[] $SwitchMap$ar$com$hjg$pngj$chunks$ChunkLoadBehaviour;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                ar.com.hjg.pngj.chunks.ChunkLoadBehaviour[] r0 = p005ar.com.hjg.pngj.chunks.ChunkLoadBehaviour.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$ar$com$hjg$pngj$chunks$ChunkLoadBehaviour = r0
                ar.com.hjg.pngj.chunks.ChunkLoadBehaviour r1 = p005ar.com.hjg.pngj.chunks.ChunkLoadBehaviour.LOAD_CHUNK_ALWAYS     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$ar$com$hjg$pngj$chunks$ChunkLoadBehaviour     // Catch:{ NoSuchFieldError -> 0x001d }
                ar.com.hjg.pngj.chunks.ChunkLoadBehaviour r1 = p005ar.com.hjg.pngj.chunks.ChunkLoadBehaviour.LOAD_CHUNK_IF_SAFE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$ar$com$hjg$pngj$chunks$ChunkLoadBehaviour     // Catch:{ NoSuchFieldError -> 0x0028 }
                ar.com.hjg.pngj.chunks.ChunkLoadBehaviour r1 = p005ar.com.hjg.pngj.chunks.ChunkLoadBehaviour.LOAD_CHUNK_NEVER     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p005ar.com.hjg.pngj.chunks.ChunkHelper.C06871.<clinit>():void");
        }
    }

    public static final byte[] compressBytes(byte[] bArr, boolean z) {
        return compressBytes(bArr, 0, bArr.length, z);
    }

    public static byte[] compressBytes(byte[] bArr, int i, int i2, boolean z) {
        try {
            InputStream byteArrayInputStream = new ByteArrayInputStream(bArr, i, i2);
            if (!z) {
                byteArrayInputStream = new InflaterInputStream(byteArrayInputStream);
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            OutputStream deflaterOutputStream = z ? new DeflaterOutputStream(byteArrayOutputStream) : byteArrayOutputStream;
            shovelInToOut(byteArrayInputStream, deflaterOutputStream);
            byteArrayInputStream.close();
            deflaterOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new PngjException((Throwable) e);
        }
    }

    private static void shovelInToOut(InputStream inputStream, OutputStream outputStream) throws IOException {
        synchronized (tmpbuffer) {
            while (true) {
                int read = inputStream.read(tmpbuffer);
                if (read > 0) {
                    outputStream.write(tmpbuffer, 0, read);
                }
            }
        }
    }

    public static List<PngChunk> filterList(List<PngChunk> list, ChunkPredicate chunkPredicate) {
        ArrayList arrayList = new ArrayList();
        for (PngChunk next : list) {
            if (chunkPredicate.match(next)) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    public static int trimList(List<PngChunk> list, ChunkPredicate chunkPredicate) {
        Iterator<PngChunk> it = list.iterator();
        int i = 0;
        while (it.hasNext()) {
            if (chunkPredicate.match(it.next())) {
                it.remove();
                i++;
            }
        }
        return i;
    }

    public static final boolean equivalent(PngChunk pngChunk, PngChunk pngChunk2) {
        if (pngChunk == pngChunk2) {
            return true;
        }
        if (pngChunk == null || pngChunk2 == null || !pngChunk.f122id.equals(pngChunk2.f122id) || pngChunk.crit || pngChunk.getClass() != pngChunk2.getClass()) {
            return false;
        }
        if (!pngChunk2.allowsMultiple()) {
            return true;
        }
        if (pngChunk instanceof PngChunkTextVar) {
            return ((PngChunkTextVar) pngChunk).getKey().equals(((PngChunkTextVar) pngChunk2).getKey());
        }
        if (pngChunk instanceof PngChunkSPLT) {
            return ((PngChunkSPLT) pngChunk).getPalName().equals(((PngChunkSPLT) pngChunk2).getPalName());
        }
        return false;
    }

    public static boolean isText(PngChunk pngChunk) {
        return pngChunk instanceof PngChunkTextVar;
    }
}
