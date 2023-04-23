package p005ar.com.hjg.pngj;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import p005ar.com.hjg.pngj.ChunkReader;
import p005ar.com.hjg.pngj.chunks.ChunkFactory;
import p005ar.com.hjg.pngj.chunks.ChunkHelper;
import p005ar.com.hjg.pngj.chunks.ChunkLoadBehaviour;
import p005ar.com.hjg.pngj.chunks.ChunksList;
import p005ar.com.hjg.pngj.chunks.PngChunk;
import p005ar.com.hjg.pngj.chunks.PngChunkIHDR;

/* renamed from: ar.com.hjg.pngj.ChunkSeqReaderPng */
public class ChunkSeqReaderPng extends ChunkSeqReader {
    private long bytesChunksLoaded = 0;
    protected final boolean callbackMode;
    private boolean checkCrc = true;
    private IChunkFactory chunkFactory;
    private ChunkLoadBehaviour chunkLoadBehaviour = ChunkLoadBehaviour.LOAD_CHUNK_ALWAYS;
    protected ChunksList chunksList = null;
    private Set<String> chunksToSkip = new HashSet();
    protected int currentChunkGroup = -1;
    protected Deinterlacer deinterlacer;
    protected ImageInfo imageInfo;
    private boolean includeNonBufferedChunks = false;
    private long maxBytesMetadata = 0;
    private long maxTotalBytesRead = 0;
    private long skipChunkMaxSize = 0;

    /* access modifiers changed from: protected */
    public void processEndPng() {
    }

    public ChunkSeqReaderPng(boolean z) {
        this.callbackMode = z;
        this.chunkFactory = new ChunkFactory();
    }

    private void updateAndCheckChunkGroup(String str) {
        if (str.equals("IHDR")) {
            if (this.currentChunkGroup < 0) {
                this.currentChunkGroup = 0;
                return;
            }
            throw new PngjInputException("unexpected chunk " + str);
        } else if (str.equals("PLTE")) {
            int i = this.currentChunkGroup;
            if (i == 0 || i == 1) {
                this.currentChunkGroup = 2;
                return;
            }
            throw new PngjInputException("unexpected chunk " + str);
        } else if (str.equals("IDAT")) {
            int i2 = this.currentChunkGroup;
            if (i2 < 0 || i2 > 4) {
                throw new PngjInputException("unexpected chunk " + str);
            }
            this.currentChunkGroup = 4;
        } else if (!str.equals("IEND")) {
            int i3 = this.currentChunkGroup;
            if (i3 <= 1) {
                this.currentChunkGroup = 1;
            } else if (i3 <= 3) {
                this.currentChunkGroup = 3;
            } else {
                this.currentChunkGroup = 5;
            }
        } else if (this.currentChunkGroup >= 4) {
            this.currentChunkGroup = 6;
        } else {
            throw new PngjInputException("unexpected chunk " + str);
        }
    }

    public boolean shouldSkipContent(int i, String str) {
        if (super.shouldSkipContent(i, str)) {
            return true;
        }
        if (ChunkHelper.isCritical(str)) {
            return false;
        }
        if (this.maxTotalBytesRead > 0 && ((long) i) + getBytesCount() > this.maxTotalBytesRead) {
            throw new PngjInputException("Maximum total bytes to read exceeeded: " + this.maxTotalBytesRead + " offset:" + getBytesCount() + " len=" + i);
        } else if (this.chunksToSkip.contains(str)) {
            return true;
        } else {
            long j = this.skipChunkMaxSize;
            if (j > 0 && ((long) i) > j) {
                return true;
            }
            long j2 = this.maxBytesMetadata;
            if (j2 > 0 && ((long) i) > j2 - this.bytesChunksLoaded) {
                return true;
            }
            int i2 = C06751.$SwitchMap$ar$com$hjg$pngj$chunks$ChunkLoadBehaviour[this.chunkLoadBehaviour.ordinal()];
            if (i2 != 1) {
                if (i2 != 2) {
                    return false;
                }
                return true;
            } else if (!ChunkHelper.isSafeToCopy(str)) {
                return true;
            }
            return false;
        }
    }

    /* renamed from: ar.com.hjg.pngj.ChunkSeqReaderPng$1 */
    static /* synthetic */ class C06751 {
        static final /* synthetic */ int[] $SwitchMap$ar$com$hjg$pngj$chunks$ChunkLoadBehaviour;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                ar.com.hjg.pngj.chunks.ChunkLoadBehaviour[] r0 = p005ar.com.hjg.pngj.chunks.ChunkLoadBehaviour.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$ar$com$hjg$pngj$chunks$ChunkLoadBehaviour = r0
                ar.com.hjg.pngj.chunks.ChunkLoadBehaviour r1 = p005ar.com.hjg.pngj.chunks.ChunkLoadBehaviour.LOAD_CHUNK_IF_SAFE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$ar$com$hjg$pngj$chunks$ChunkLoadBehaviour     // Catch:{ NoSuchFieldError -> 0x001d }
                ar.com.hjg.pngj.chunks.ChunkLoadBehaviour r1 = p005ar.com.hjg.pngj.chunks.ChunkLoadBehaviour.LOAD_CHUNK_NEVER     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p005ar.com.hjg.pngj.ChunkSeqReaderPng.C06751.<clinit>():void");
        }
    }

    public long getBytesChunksLoaded() {
        return this.bytesChunksLoaded;
    }

    public int getCurrentChunkGroup() {
        return this.currentChunkGroup;
    }

    public void setChunksToSkip(String... strArr) {
        this.chunksToSkip.clear();
        for (String add : strArr) {
            this.chunksToSkip.add(add);
        }
    }

    public void addChunkToSkip(String str) {
        this.chunksToSkip.add(str);
    }

    public boolean firstChunksNotYetRead() {
        return getCurrentChunkGroup() < 4;
    }

    /* access modifiers changed from: protected */
    public void postProcessChunk(ChunkReader chunkReader) {
        super.postProcessChunk(chunkReader);
        if (chunkReader.getChunkRaw().f121id.equals("IHDR")) {
            PngChunkIHDR pngChunkIHDR = new PngChunkIHDR((ImageInfo) null);
            pngChunkIHDR.parseFromRaw(chunkReader.getChunkRaw());
            this.imageInfo = pngChunkIHDR.createImageInfo();
            if (pngChunkIHDR.isInterlaced()) {
                this.deinterlacer = new Deinterlacer(this.imageInfo);
            }
            this.chunksList = new ChunksList(this.imageInfo);
        }
        if (chunkReader.mode == ChunkReader.ChunkReaderMode.BUFFER || this.includeNonBufferedChunks) {
            this.chunksList.appendReadChunk(this.chunkFactory.createChunk(chunkReader.getChunkRaw(), getImageInfo()), this.currentChunkGroup);
        }
        if (isDone()) {
            processEndPng();
        }
    }

    /* access modifiers changed from: protected */
    public DeflatedChunksSet createIdatSet(String str) {
        IdatSet idatSet = new IdatSet(str, this.imageInfo, this.deinterlacer);
        idatSet.setCallbackMode(this.callbackMode);
        return idatSet;
    }

    public IdatSet getIdatSet() {
        DeflatedChunksSet curReaderDeflatedSet = getCurReaderDeflatedSet();
        if (curReaderDeflatedSet instanceof IdatSet) {
            return (IdatSet) curReaderDeflatedSet;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public boolean isIdatKind(String str) {
        return str.equals("IDAT");
    }

    public int consume(byte[] bArr, int i, int i2) {
        return super.consume(bArr, i, i2);
    }

    public void setChunkFactory(IChunkFactory iChunkFactory) {
        this.chunkFactory = iChunkFactory;
    }

    public ImageInfo getImageInfo() {
        return this.imageInfo;
    }

    public boolean isInterlaced() {
        return this.deinterlacer != null;
    }

    public Deinterlacer getDeinterlacer() {
        return this.deinterlacer;
    }

    /* access modifiers changed from: protected */
    public void startNewChunk(int i, String str, long j) {
        updateAndCheckChunkGroup(str);
        super.startNewChunk(i, str, j);
    }

    public void close() {
        if (this.currentChunkGroup != 6) {
            this.currentChunkGroup = 6;
        }
        super.close();
    }

    public List<PngChunk> getChunks() {
        return this.chunksList.getChunks();
    }

    public void setMaxTotalBytesRead(long j) {
        this.maxTotalBytesRead = j;
    }

    public long getSkipChunkMaxSize() {
        return this.skipChunkMaxSize;
    }

    public void setSkipChunkMaxSize(long j) {
        this.skipChunkMaxSize = j;
    }

    public long getMaxBytesMetadata() {
        return this.maxBytesMetadata;
    }

    public void setMaxBytesMetadata(long j) {
        this.maxBytesMetadata = j;
    }

    public long getMaxTotalBytesRead() {
        return this.maxTotalBytesRead;
    }

    /* access modifiers changed from: protected */
    public boolean shouldCheckCrc(int i, String str) {
        return this.checkCrc;
    }

    public boolean isCheckCrc() {
        return this.checkCrc;
    }

    public void setCheckCrc(boolean z) {
        this.checkCrc = z;
    }

    public boolean isCallbackMode() {
        return this.callbackMode;
    }

    public Set<String> getChunksToSkip() {
        return this.chunksToSkip;
    }

    public void setChunkLoadBehaviour(ChunkLoadBehaviour chunkLoadBehaviour2) {
        this.chunkLoadBehaviour = chunkLoadBehaviour2;
    }

    public void setIncludeNonBufferedChunks(boolean z) {
        this.includeNonBufferedChunks = z;
    }
}
