package p005ar.com.hjg.pngj.chunks;

import p005ar.com.hjg.pngj.IChunkFactory;
import p005ar.com.hjg.pngj.ImageInfo;

/* renamed from: ar.com.hjg.pngj.chunks.ChunkFactory */
public class ChunkFactory implements IChunkFactory {
    boolean parse;

    public ChunkFactory() {
        this(true);
    }

    public ChunkFactory(boolean z) {
        this.parse = z;
    }

    public final PngChunk createChunk(ChunkRaw chunkRaw, ImageInfo imageInfo) {
        PngChunk createEmptyChunkKnown = createEmptyChunkKnown(chunkRaw.f121id, imageInfo);
        if (createEmptyChunkKnown == null) {
            createEmptyChunkKnown = createEmptyChunkExtended(chunkRaw.f121id, imageInfo);
        }
        if (createEmptyChunkKnown == null) {
            createEmptyChunkKnown = createEmptyChunkUnknown(chunkRaw.f121id, imageInfo);
        }
        createEmptyChunkKnown.setRaw(chunkRaw);
        if (this.parse && chunkRaw.data != null) {
            createEmptyChunkKnown.parseFromRaw(chunkRaw);
        }
        return createEmptyChunkKnown;
    }

    /* access modifiers changed from: protected */
    public final PngChunk createEmptyChunkKnown(String str, ImageInfo imageInfo) {
        if (str.equals("IDAT")) {
            return new PngChunkIDAT(imageInfo);
        }
        if (str.equals("IHDR")) {
            return new PngChunkIHDR(imageInfo);
        }
        if (str.equals("PLTE")) {
            return new PngChunkPLTE(imageInfo);
        }
        if (str.equals("IEND")) {
            return new PngChunkIEND(imageInfo);
        }
        if (str.equals("tEXt")) {
            return new PngChunkTEXT(imageInfo);
        }
        if (str.equals("iTXt")) {
            return new PngChunkITXT(imageInfo);
        }
        if (str.equals("zTXt")) {
            return new PngChunkZTXT(imageInfo);
        }
        if (str.equals("bKGD")) {
            return new PngChunkBKGD(imageInfo);
        }
        if (str.equals("gAMA")) {
            return new PngChunkGAMA(imageInfo);
        }
        if (str.equals("pHYs")) {
            return new PngChunkPHYS(imageInfo);
        }
        if (str.equals("iCCP")) {
            return new PngChunkICCP(imageInfo);
        }
        if (str.equals("tIME")) {
            return new PngChunkTIME(imageInfo);
        }
        if (str.equals("tRNS")) {
            return new PngChunkTRNS(imageInfo);
        }
        if (str.equals("cHRM")) {
            return new PngChunkCHRM(imageInfo);
        }
        if (str.equals("sBIT")) {
            return new PngChunkSBIT(imageInfo);
        }
        if (str.equals("sRGB")) {
            return new PngChunkSRGB(imageInfo);
        }
        if (str.equals("hIST")) {
            return new PngChunkHIST(imageInfo);
        }
        if (str.equals("sPLT")) {
            return new PngChunkSPLT(imageInfo);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public final PngChunk createEmptyChunkUnknown(String str, ImageInfo imageInfo) {
        return new PngChunkUNKNOWN(str, imageInfo);
    }

    /* access modifiers changed from: protected */
    public PngChunk createEmptyChunkExtended(String str, ImageInfo imageInfo) {
        if (str.equals(PngChunkOFFS.f133ID)) {
            return new PngChunkOFFS(imageInfo);
        }
        if (str.equals(PngChunkSTER.f139ID)) {
            return new PngChunkSTER(imageInfo);
        }
        return null;
    }
}
