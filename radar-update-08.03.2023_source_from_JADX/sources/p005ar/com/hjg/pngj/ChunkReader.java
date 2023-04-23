package p005ar.com.hjg.pngj;

import p005ar.com.hjg.pngj.chunks.ChunkRaw;

/* renamed from: ar.com.hjg.pngj.ChunkReader */
public abstract class ChunkReader {
    private final ChunkRaw chunkRaw;
    private boolean crcCheck;
    private int crcn = 0;
    public final ChunkReaderMode mode;
    protected int read = 0;

    /* renamed from: ar.com.hjg.pngj.ChunkReader$ChunkReaderMode */
    public enum ChunkReaderMode {
        BUFFER,
        PROCESS,
        SKIP
    }

    /* access modifiers changed from: protected */
    public abstract void chunkDone();

    /* access modifiers changed from: protected */
    public abstract void processData(int i, byte[] bArr, int i2, int i3);

    public ChunkReader(int i, String str, long j, ChunkReaderMode chunkReaderMode) {
        boolean z = false;
        if (chunkReaderMode == null || str.length() != 4 || i < 0) {
            throw new PngjExceptionInternal("Bad chunk paramenters: " + chunkReaderMode);
        }
        this.mode = chunkReaderMode;
        ChunkRaw chunkRaw2 = new ChunkRaw(i, str, chunkReaderMode == ChunkReaderMode.BUFFER);
        this.chunkRaw = chunkRaw2;
        chunkRaw2.setOffset(j);
        this.crcCheck = chunkReaderMode != ChunkReaderMode.SKIP ? true : z;
    }

    public ChunkRaw getChunkRaw() {
        return this.chunkRaw;
    }

    public final int feedBytes(byte[] bArr, int i, int i2) {
        int i3 = 0;
        if (i2 == 0) {
            return 0;
        }
        if (i2 >= 0) {
            if (this.read == 0 && this.crcn == 0 && this.crcCheck) {
                ChunkRaw chunkRaw2 = this.chunkRaw;
                chunkRaw2.updateCrc(chunkRaw2.idbytes, 0, 4);
            }
            int i4 = this.chunkRaw.len - this.read;
            if (i4 > i2) {
                i4 = i2;
            }
            if (i4 > 0 || this.crcn == 0) {
                if (this.crcCheck && this.mode != ChunkReaderMode.BUFFER && i4 > 0) {
                    this.chunkRaw.updateCrc(bArr, i, i4);
                }
                if (this.mode == ChunkReaderMode.BUFFER) {
                    if (this.chunkRaw.data != bArr && i4 > 0) {
                        System.arraycopy(bArr, i, this.chunkRaw.data, this.read, i4);
                    }
                } else if (this.mode == ChunkReaderMode.PROCESS) {
                    processData(this.read, bArr, i, i4);
                }
                this.read += i4;
                i += i4;
                i2 -= i4;
            }
            if (this.read == this.chunkRaw.len) {
                int i5 = 4 - this.crcn;
                if (i5 <= i2) {
                    i2 = i5;
                }
                if (i2 > 0) {
                    if (bArr != this.chunkRaw.crcval) {
                        System.arraycopy(bArr, i, this.chunkRaw.crcval, this.crcn, i2);
                    }
                    int i6 = this.crcn + i2;
                    this.crcn = i6;
                    if (i6 == 4) {
                        if (this.crcCheck) {
                            if (this.mode == ChunkReaderMode.BUFFER) {
                                ChunkRaw chunkRaw3 = this.chunkRaw;
                                chunkRaw3.updateCrc(chunkRaw3.data, 0, this.chunkRaw.len);
                            }
                            this.chunkRaw.checkCrc();
                        }
                        chunkDone();
                    }
                }
                i3 = i2;
            }
            return i4 + i3;
        }
        throw new PngjException("negative length??");
    }

    public final boolean isDone() {
        return this.crcn == 4;
    }

    public void setCrcCheck(boolean z) {
        if (this.read == 0 || !z || this.crcCheck) {
            this.crcCheck = z;
            return;
        }
        throw new PngjException("too late!");
    }

    public int hashCode() {
        ChunkRaw chunkRaw2 = this.chunkRaw;
        return 31 + (chunkRaw2 == null ? 0 : chunkRaw2.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ChunkReader chunkReader = (ChunkReader) obj;
        ChunkRaw chunkRaw2 = this.chunkRaw;
        if (chunkRaw2 == null) {
            if (chunkReader.chunkRaw != null) {
                return false;
            }
        } else if (!chunkRaw2.equals(chunkReader.chunkRaw)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return this.chunkRaw.toString();
    }
}
