package p005ar.com.hjg.pngj;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;
import p005ar.com.hjg.pngj.chunks.ChunkCopyBehaviour;
import p005ar.com.hjg.pngj.chunks.ChunkPredicate;
import p005ar.com.hjg.pngj.chunks.ChunksList;
import p005ar.com.hjg.pngj.chunks.ChunksListForWrite;
import p005ar.com.hjg.pngj.chunks.PngChunk;
import p005ar.com.hjg.pngj.chunks.PngChunkIEND;
import p005ar.com.hjg.pngj.chunks.PngChunkIHDR;
import p005ar.com.hjg.pngj.chunks.PngMetadata;
import p005ar.com.hjg.pngj.pixels.PixelsWriter;
import p005ar.com.hjg.pngj.pixels.PixelsWriterDefault;

/* renamed from: ar.com.hjg.pngj.PngWriter */
public class PngWriter {
    private final ChunksListForWrite chunksList;
    private ChunksList copyFromList;
    private ChunkPredicate copyFromPredicate;
    protected int currentChunkGroup;
    private int currentpass;
    private PngIDatChunkOutputStream datStream;
    protected StringBuilder debuginfo;
    private int idatMaxSize;
    public final ImageInfo imgInfo;
    private final PngMetadata metadata;

    /* renamed from: os */
    private final OutputStream f116os;
    private int passes;
    protected PixelsWriter pixelsWriter;
    protected int rowNum;
    private boolean shouldCloseStream;

    public PngWriter(File file, ImageInfo imageInfo, boolean z) {
        this(PngHelperInternal.ostreamFromFile(file, z), imageInfo);
        setShouldCloseStream(true);
    }

    public PngWriter(File file, ImageInfo imageInfo) {
        this(file, imageInfo, true);
    }

    public PngWriter(OutputStream outputStream, ImageInfo imageInfo) {
        this.rowNum = -1;
        this.currentChunkGroup = -1;
        this.passes = 1;
        this.currentpass = 0;
        this.shouldCloseStream = true;
        this.idatMaxSize = 0;
        this.copyFromPredicate = null;
        this.copyFromList = null;
        this.debuginfo = new StringBuilder();
        this.f116os = outputStream;
        this.imgInfo = imageInfo;
        ChunksListForWrite chunksListForWrite = new ChunksListForWrite(imageInfo);
        this.chunksList = chunksListForWrite;
        this.metadata = new PngMetadata(chunksListForWrite);
        this.pixelsWriter = createPixelsWriter(imageInfo);
        setCompLevel(9);
    }

    private void initIdat() {
        PngIDatChunkOutputStream pngIDatChunkOutputStream = new PngIDatChunkOutputStream(this.f116os, this.idatMaxSize);
        this.datStream = pngIDatChunkOutputStream;
        this.pixelsWriter.setOs(pngIDatChunkOutputStream);
        writeSignatureAndIHDR();
        writeFirstChunks();
    }

    private void writeEndChunk() {
        PngChunkIEND pngChunkIEND = new PngChunkIEND(this.imgInfo);
        pngChunkIEND.createRawChunk().writeChunk(this.f116os);
        this.chunksList.getChunks().add(pngChunkIEND);
    }

    private void writeFirstChunks() {
        if (this.currentChunkGroup < 4) {
            this.currentChunkGroup = 1;
            queueChunksFromOther();
            this.chunksList.writeChunks(this.f116os, this.currentChunkGroup);
            this.currentChunkGroup = 2;
            int writeChunks = this.chunksList.writeChunks(this.f116os, 2);
            if (writeChunks > 0 && this.imgInfo.greyscale) {
                throw new PngjOutputException("cannot write palette for this format");
            } else if (writeChunks != 0 || !this.imgInfo.indexed) {
                this.currentChunkGroup = 3;
                this.chunksList.writeChunks(this.f116os, 3);
                this.currentChunkGroup = 4;
            } else {
                throw new PngjOutputException("missing palette");
            }
        }
    }

    private void writeLastChunks() {
        queueChunksFromOther();
        this.currentChunkGroup = 5;
        this.chunksList.writeChunks(this.f116os, 5);
        List<PngChunk> queuedChunks = this.chunksList.getQueuedChunks();
        if (queuedChunks.isEmpty()) {
            this.currentChunkGroup = 6;
            return;
        }
        throw new PngjOutputException(queuedChunks.size() + " chunks were not written! Eg: " + queuedChunks.get(0).toString());
    }

    private void writeSignatureAndIHDR() {
        this.currentChunkGroup = 0;
        PngHelperInternal.writeBytes(this.f116os, PngHelperInternal.getPngIdSignature());
        PngChunkIHDR pngChunkIHDR = new PngChunkIHDR(this.imgInfo);
        pngChunkIHDR.setCols(this.imgInfo.cols);
        pngChunkIHDR.setRows(this.imgInfo.rows);
        pngChunkIHDR.setBitspc(this.imgInfo.bitDepth);
        int i = this.imgInfo.alpha ? 4 : 0;
        if (this.imgInfo.indexed) {
            i++;
        }
        if (!this.imgInfo.greyscale) {
            i += 2;
        }
        pngChunkIHDR.setColormodel(i);
        pngChunkIHDR.setCompmeth(0);
        pngChunkIHDR.setFilmeth(0);
        pngChunkIHDR.setInterlaced(0);
        pngChunkIHDR.createRawChunk().writeChunk(this.f116os);
        this.chunksList.getChunks().add(pngChunkIHDR);
    }

    private void queueChunksFromOther() {
        int chunkGroup;
        ChunksList chunksList2 = this.copyFromList;
        if (chunksList2 != null && this.copyFromPredicate != null) {
            boolean z = this.currentChunkGroup >= 4;
            for (PngChunk next : chunksList2.getChunks()) {
                if (next.getRaw().data != null && (((chunkGroup = next.getChunkGroup()) > 4 || !z) && ((chunkGroup < 4 || z) && ((!next.crit || next.f122id.equals("PLTE")) && this.copyFromPredicate.match(next) && this.chunksList.getEquivalent(next).isEmpty() && this.chunksList.getQueuedEquivalent(next).isEmpty())))) {
                    this.chunksList.queue(next);
                }
            }
        }
    }

    public void queueChunk(PngChunk pngChunk) {
        for (PngChunk removeChunk : this.chunksList.getQueuedEquivalent(pngChunk)) {
            getChunksList().removeChunk(removeChunk);
        }
        this.chunksList.queue(pngChunk);
    }

    public void copyChunksFrom(ChunksList chunksList2, int i) {
        copyChunksFrom(chunksList2, ChunkCopyBehaviour.createPredicate(i, this.imgInfo));
    }

    public void copyChunksFrom(ChunksList chunksList2) {
        copyChunksFrom(chunksList2, 8);
    }

    public void copyChunksFrom(ChunksList chunksList2, ChunkPredicate chunkPredicate) {
        if (!(this.copyFromList == null || chunksList2 == null)) {
            PngHelperInternal.LOGGER.warning("copyChunksFrom should only be called once");
        }
        if (chunkPredicate != null) {
            this.copyFromList = chunksList2;
            this.copyFromPredicate = chunkPredicate;
            return;
        }
        throw new PngjOutputException("copyChunksFrom requires a predicate");
    }

    public double computeCompressionRatio() {
        if (this.currentChunkGroup >= 6) {
            return ((double) this.datStream.getCountFlushed()) / ((double) ((this.imgInfo.bytesPerRow + 1) * this.imgInfo.rows));
        }
        throw new PngjOutputException("must be called after end()");
    }

    public void end() {
        if (this.rowNum == this.imgInfo.rows - 1) {
            try {
                this.datStream.flush();
                writeLastChunks();
                writeEndChunk();
                close();
            } catch (IOException e) {
                throw new PngjOutputException((Throwable) e);
            } catch (Throwable th) {
                close();
                throw th;
            }
        } else {
            throw new PngjOutputException("all rows have not been written");
        }
    }

    public void close() {
        OutputStream outputStream;
        try {
            PngIDatChunkOutputStream pngIDatChunkOutputStream = this.datStream;
            if (pngIDatChunkOutputStream != null) {
                pngIDatChunkOutputStream.close();
            }
        } catch (Exception unused) {
        }
        PixelsWriter pixelsWriter2 = this.pixelsWriter;
        if (pixelsWriter2 != null) {
            pixelsWriter2.close();
        }
        if (this.shouldCloseStream && (outputStream = this.f116os) != null) {
            try {
                outputStream.close();
            } catch (Exception e) {
                Logger logger = PngHelperInternal.LOGGER;
                logger.warning("Error closing writer " + e.toString());
            }
        }
    }

    public ChunksListForWrite getChunksList() {
        return this.chunksList;
    }

    public PngMetadata getMetadata() {
        return this.metadata;
    }

    public void setFilterType(FilterType filterType) {
        this.pixelsWriter.setFilterType(filterType);
    }

    public void setCompLevel(int i) {
        this.pixelsWriter.setDeflaterCompLevel(Integer.valueOf(i));
    }

    public void setFilterPreserve(boolean z) {
        if (z) {
            this.pixelsWriter.setFilterType(FilterType.FILTER_PRESERVE);
        } else if (this.pixelsWriter.getFilterType() == null) {
            this.pixelsWriter.setFilterType(FilterType.FILTER_DEFAULT);
        }
    }

    public void setIdatMaxSize(int i) {
        this.idatMaxSize = i;
    }

    public void setShouldCloseStream(boolean z) {
        this.shouldCloseStream = z;
    }

    public void writeRow(IImageLine iImageLine) {
        writeRow(iImageLine, this.rowNum + 1);
    }

    /* JADX WARNING: type inference failed for: r3v0, types: [ar.com.hjg.pngj.IImageLineSet, ar.com.hjg.pngj.IImageLineSet<? extends ar.com.hjg.pngj.IImageLine>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeRows(p005ar.com.hjg.pngj.IImageLineSet<? extends p005ar.com.hjg.pngj.IImageLine> r3) {
        /*
            r2 = this;
            r0 = 0
        L_0x0001:
            ar.com.hjg.pngj.ImageInfo r1 = r2.imgInfo
            int r1 = r1.rows
            if (r0 >= r1) goto L_0x0011
            ar.com.hjg.pngj.IImageLine r1 = r3.getImageLine(r0)
            r2.writeRow(r1)
            int r0 = r0 + 1
            goto L_0x0001
        L_0x0011:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p005ar.com.hjg.pngj.PngWriter.writeRows(ar.com.hjg.pngj.IImageLineSet):void");
    }

    public void writeRow(IImageLine iImageLine, int i) {
        int i2 = this.rowNum + 1;
        this.rowNum = i2;
        if (i2 == this.imgInfo.rows) {
            this.rowNum = 0;
        }
        if (i == this.imgInfo.rows) {
            i = 0;
        }
        if (i < 0 || this.rowNum == i) {
            if (this.rowNum == 0) {
                this.currentpass++;
            }
            if (i == 0 && this.currentpass == this.passes) {
                initIdat();
                writeFirstChunks();
            }
            byte[] rowb = this.pixelsWriter.getRowb();
            iImageLine.writeToPngRaw(rowb);
            this.pixelsWriter.processRow(rowb);
            return;
        }
        throw new PngjOutputException("rows must be written in order: expected:" + this.rowNum + " passed:" + i);
    }

    public void writeRowInt(int[] iArr) {
        writeRow(new ImageLineInt(this.imgInfo, iArr));
    }

    /* access modifiers changed from: protected */
    public PixelsWriter createPixelsWriter(ImageInfo imageInfo) {
        return new PixelsWriterDefault(imageInfo);
    }

    public final PixelsWriter getPixelsWriter() {
        return this.pixelsWriter;
    }

    public String getDebuginfo() {
        return this.debuginfo.toString();
    }
}
