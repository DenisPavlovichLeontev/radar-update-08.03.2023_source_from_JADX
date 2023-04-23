package p005ar.com.hjg.pngj;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import p005ar.com.hjg.pngj.chunks.ChunkLoadBehaviour;
import p005ar.com.hjg.pngj.chunks.ChunksList;
import p005ar.com.hjg.pngj.chunks.PngMetadata;

/* renamed from: ar.com.hjg.pngj.PngReader */
public class PngReader {
    public static final long MAX_BYTES_METADATA_DEFAULT = 5024024;
    public static final long MAX_CHUNK_SIZE_SKIP = 2024024;
    public static final long MAX_TOTAL_BYTES_READ_DEFAULT = 901001001;
    protected ChunkSeqReaderPng chunkseq;
    CRC32 idatCrca;
    Adler32 idatCrcb;
    private IImageLineSetFactory<? extends IImageLine> imageLineSetFactory;
    public final ImageInfo imgInfo;
    protected IImageLineSet<? extends IImageLine> imlinesSet;
    public final boolean interlaced;
    protected final PngMetadata metadata;
    protected int rowNum;
    protected BufferedStreamFeeder streamFeeder;

    public PngReader(InputStream inputStream) {
        this(inputStream, true);
    }

    public PngReader(InputStream inputStream, boolean z) {
        this.rowNum = -1;
        try {
            BufferedStreamFeeder bufferedStreamFeeder = new BufferedStreamFeeder(inputStream);
            this.streamFeeder = bufferedStreamFeeder;
            bufferedStreamFeeder.setCloseStream(z);
            boolean z2 = false;
            this.chunkseq = new ChunkSeqReaderPng(false);
            this.streamFeeder.setFailIfNoFeed(true);
            if (this.streamFeeder.feedFixed(this.chunkseq, 36)) {
                this.imgInfo = this.chunkseq.getImageInfo();
                this.interlaced = this.chunkseq.getDeinterlacer() != null ? true : z2;
                setMaxBytesMetadata(MAX_BYTES_METADATA_DEFAULT);
                setMaxTotalBytesRead(MAX_TOTAL_BYTES_READ_DEFAULT);
                setSkipChunkMaxSize(MAX_CHUNK_SIZE_SKIP);
                this.metadata = new PngMetadata(this.chunkseq.chunksList);
                setLineSetFactory(ImageLineSetDefault.getFactoryInt());
                this.rowNum = -1;
                return;
            }
            throw new PngjInputException("error reading first 21 bytes");
        } catch (RuntimeException e) {
            this.streamFeeder.close();
            ChunkSeqReaderPng chunkSeqReaderPng = this.chunkseq;
            if (chunkSeqReaderPng != null) {
                chunkSeqReaderPng.close();
            }
            throw e;
        }
    }

    public PngReader(File file) {
        this(PngHelperInternal.istreamFromFile(file), true);
    }

    /* access modifiers changed from: protected */
    public void readFirstChunks() {
        while (this.chunkseq.currentChunkGroup < 4) {
            this.streamFeeder.feed(this.chunkseq);
        }
    }

    public void setChunkLoadBehaviour(ChunkLoadBehaviour chunkLoadBehaviour) {
        this.chunkseq.setChunkLoadBehaviour(chunkLoadBehaviour);
    }

    public ChunksList getChunksList() {
        if (this.chunkseq.firstChunksNotYetRead()) {
            readFirstChunks();
        }
        return this.chunkseq.chunksList;
    }

    /* access modifiers changed from: package-private */
    public int getCurrentChunkGroup() {
        return this.chunkseq.currentChunkGroup;
    }

    public PngMetadata getMetadata() {
        if (this.chunkseq.firstChunksNotYetRead()) {
            readFirstChunks();
        }
        return this.metadata;
    }

    public IImageLine readRow() {
        return readRow(this.rowNum + 1);
    }

    public boolean hasMoreRows() {
        return this.rowNum < this.imgInfo.rows - 1;
    }

    public IImageLine readRow(int i) {
        if (this.chunkseq.firstChunksNotYetRead()) {
            readFirstChunks();
        }
        if (!this.interlaced) {
            if (this.imlinesSet == null) {
                this.imlinesSet = createLineSet(true, 1, 0, 1);
            }
            IImageLine imageLine = this.imlinesSet.getImageLine(i);
            int i2 = this.rowNum;
            if (i == i2) {
                return imageLine;
            }
            if (i >= i2) {
                while (this.rowNum < i) {
                    while (!this.chunkseq.getIdatSet().isRowReady()) {
                        this.streamFeeder.feed(this.chunkseq);
                    }
                    this.rowNum++;
                    this.chunkseq.getIdatSet().updateCrcs(this.idatCrca, this.idatCrcb);
                    if (this.rowNum == i) {
                        imageLine.readFromPngRaw(this.chunkseq.getIdatSet().getUnfilteredRow(), this.imgInfo.bytesPerRow + 1, 0, 1);
                        imageLine.endReadFromPngRaw();
                    }
                    this.chunkseq.getIdatSet().advanceToNextRow();
                }
                return imageLine;
            }
            throw new PngjInputException("rows must be read in increasing order: " + i);
        }
        if (this.imlinesSet == null) {
            this.imlinesSet = createLineSet(false, this.imgInfo.rows, 0, 1);
            loadAllInterlaced(this.imgInfo.rows, 0, 1);
        }
        this.rowNum = i;
        return this.imlinesSet.getImageLine(i);
    }

    public IImageLineSet<? extends IImageLine> readRows() {
        return readRows(this.imgInfo.rows, 0, 1);
    }

    public IImageLineSet<? extends IImageLine> readRows(int i, int i2, int i3) {
        if (this.chunkseq.firstChunksNotYetRead()) {
            readFirstChunks();
        }
        if (i < 0) {
            i = (this.imgInfo.rows - i2) / i3;
        }
        if (i3 < 1 || i2 < 0 || i == 0 || (i * i3) + i2 > this.imgInfo.rows) {
            throw new PngjInputException("bad args");
        } else if (this.rowNum < 0) {
            this.imlinesSet = createLineSet(false, i, i2, i3);
            if (!this.interlaced) {
                int i4 = -1;
                while (i4 < i - 1) {
                    while (!this.chunkseq.getIdatSet().isRowReady()) {
                        this.streamFeeder.feed(this.chunkseq);
                    }
                    this.rowNum++;
                    this.chunkseq.getIdatSet().updateCrcs(this.idatCrca, this.idatCrcb);
                    int i5 = this.rowNum;
                    int i6 = (i5 - i2) / i3;
                    if (i5 >= i2 && (i3 * i6) + i2 == i5) {
                        IImageLine imageLine = this.imlinesSet.getImageLine(i5);
                        imageLine.readFromPngRaw(this.chunkseq.getIdatSet().getUnfilteredRow(), this.imgInfo.bytesPerRow + 1, 0, 1);
                        imageLine.endReadFromPngRaw();
                    }
                    this.chunkseq.getIdatSet().advanceToNextRow();
                    i4 = i6;
                }
            } else {
                loadAllInterlaced(i, i2, i3);
            }
            this.chunkseq.getIdatSet().done();
            end();
            return this.imlinesSet;
        } else {
            throw new PngjInputException("readRows cannot be mixed with readRow");
        }
    }

    public void setLineSetFactory(IImageLineSetFactory<? extends IImageLine> iImageLineSetFactory) {
        this.imageLineSetFactory = iImageLineSetFactory;
    }

    /* access modifiers changed from: protected */
    public IImageLineSet<? extends IImageLine> createLineSet(boolean z, int i, int i2, int i3) {
        return this.imageLineSetFactory.create(this.imgInfo, z, i, i2, i3);
    }

    /* access modifiers changed from: protected */
    public void loadAllInterlaced(int i, int i2, int i3) {
        IdatSet idatSet = this.chunkseq.getIdatSet();
        int i4 = 0;
        int i5 = 0;
        while (true) {
            if (this.chunkseq.getIdatSet().isRowReady()) {
                boolean z = true;
                this.chunkseq.getIdatSet().updateCrcs(this.idatCrca, this.idatCrcb);
                int i6 = idatSet.rowinfo.rowNreal;
                if ((i6 - i2) % i3 != 0) {
                    z = false;
                }
                if (z) {
                    this.imlinesSet.getImageLine(i6).readFromPngRaw(idatSet.getUnfilteredRow(), idatSet.rowinfo.buflen, idatSet.rowinfo.f119oX, idatSet.rowinfo.f117dX);
                    i5++;
                }
                idatSet.advanceToNextRow();
                if (i5 >= i && idatSet.isDone()) {
                    break;
                }
            } else {
                this.streamFeeder.feed(this.chunkseq);
            }
        }
        idatSet.done();
        while (i4 < i) {
            this.imlinesSet.getImageLine(i2).endReadFromPngRaw();
            i4++;
            i2 += i3;
        }
    }

    public void readSkippingAllRows() {
        this.chunkseq.addChunkToSkip("IDAT");
        if (this.chunkseq.firstChunksNotYetRead()) {
            readFirstChunks();
        }
        end();
    }

    public void setMaxTotalBytesRead(long j) {
        this.chunkseq.setMaxTotalBytesRead(j);
    }

    public void setMaxBytesMetadata(long j) {
        this.chunkseq.setMaxBytesMetadata(j);
    }

    public void setSkipChunkMaxSize(long j) {
        this.chunkseq.setSkipChunkMaxSize(j);
    }

    public void setChunksToSkip(String... strArr) {
        this.chunkseq.setChunksToSkip(strArr);
    }

    public void addChunkToSkip(String str) {
        this.chunkseq.addChunkToSkip(str);
    }

    public void setShouldCloseStream(boolean z) {
        this.streamFeeder.setCloseStream(z);
    }

    public void end() {
        try {
            if (this.chunkseq.firstChunksNotYetRead()) {
                readFirstChunks();
            }
            if (this.chunkseq.getIdatSet() != null && !this.chunkseq.getIdatSet().isDone()) {
                this.chunkseq.getIdatSet().done();
            }
            while (!this.chunkseq.isDone()) {
                this.streamFeeder.feed(this.chunkseq);
            }
        } finally {
            close();
        }
    }

    public void close() {
        try {
            ChunkSeqReaderPng chunkSeqReaderPng = this.chunkseq;
            if (chunkSeqReaderPng != null) {
                chunkSeqReaderPng.close();
            }
        } catch (Exception e) {
            Logger logger = PngHelperInternal.LOGGER;
            logger.warning("error closing chunk sequence:" + e.getMessage());
        }
        BufferedStreamFeeder bufferedStreamFeeder = this.streamFeeder;
        if (bufferedStreamFeeder != null) {
            bufferedStreamFeeder.close();
        }
    }

    public boolean isInterlaced() {
        return this.interlaced;
    }

    public void setCrcCheckDisabled() {
        this.chunkseq.setCheckCrc(false);
    }

    public ChunkSeqReaderPng getChunkseq() {
        return this.chunkseq;
    }

    public void prepareSimpleDigestComputation() {
        CRC32 crc32 = this.idatCrca;
        if (crc32 == null) {
            this.idatCrca = new CRC32();
        } else {
            crc32.reset();
        }
        Adler32 adler32 = this.idatCrcb;
        if (adler32 == null) {
            this.idatCrcb = new Adler32();
        } else {
            adler32.reset();
        }
        this.idatCrca.update((byte) this.imgInfo.rows);
        this.idatCrca.update((byte) (this.imgInfo.rows >> 8));
        this.idatCrca.update((byte) (this.imgInfo.rows >> 16));
        this.idatCrca.update((byte) this.imgInfo.cols);
        this.idatCrca.update((byte) (this.imgInfo.cols >> 8));
        this.idatCrca.update((byte) (this.imgInfo.cols >> 16));
        this.idatCrca.update((byte) this.imgInfo.channels);
        this.idatCrca.update((byte) this.imgInfo.bitDepth);
        this.idatCrca.update((byte) (this.imgInfo.indexed ? 10 : 20));
        this.idatCrcb.update((byte) this.imgInfo.bytesPerRow);
        this.idatCrcb.update((byte) this.imgInfo.channels);
        this.idatCrcb.update((byte) this.imgInfo.rows);
    }

    /* access modifiers changed from: package-private */
    public long getSimpleDigest() {
        CRC32 crc32 = this.idatCrca;
        if (crc32 == null) {
            return 0;
        }
        return crc32.getValue() ^ (this.idatCrcb.getValue() << 31);
    }

    public String getSimpleDigestHex() {
        return String.format("%016X", new Object[]{Long.valueOf(getSimpleDigest())});
    }

    public String toString() {
        return this.imgInfo.toString() + " interlaced=" + this.interlaced;
    }

    public String toStringCompact() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.imgInfo.toStringBrief());
        sb.append(this.interlaced ? "i" : "");
        return sb.toString();
    }
}
