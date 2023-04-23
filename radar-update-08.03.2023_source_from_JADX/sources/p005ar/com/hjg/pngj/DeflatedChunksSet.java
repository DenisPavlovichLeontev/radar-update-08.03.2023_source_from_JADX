package p005ar.com.hjg.pngj;

import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/* renamed from: ar.com.hjg.pngj.DeflatedChunksSet */
public class DeflatedChunksSet {
    private boolean callbackMode;
    public final String chunkid;
    private DeflatedChunkReader curChunk;
    private Inflater inf;
    private final boolean infOwn;
    private long nBytesIn;
    private long nBytesOut;
    protected byte[] row;
    private int rowfilled;
    private int rowlen;
    private int rown;
    State state;

    public boolean allowOtherChunksInBetween(String str) {
        return false;
    }

    /* access modifiers changed from: protected */
    public void preProcessRow() {
    }

    /* access modifiers changed from: protected */
    public void processDoneCallback() {
    }

    /* renamed from: ar.com.hjg.pngj.DeflatedChunksSet$State */
    private enum State {
        WAITING_FOR_INPUT,
        ROW_READY,
        WORK_DONE,
        TERMINATED;

        public boolean isDone() {
            return this == WORK_DONE || this == TERMINATED;
        }

        public boolean isTerminated() {
            return this == TERMINATED;
        }
    }

    public DeflatedChunksSet(String str, int i, int i2, Inflater inflater, byte[] bArr) {
        this.state = State.WAITING_FOR_INPUT;
        this.callbackMode = true;
        this.nBytesIn = 0;
        this.nBytesOut = 0;
        this.chunkid = str;
        this.rowlen = i;
        if (i < 1 || i2 < i) {
            throw new PngjException("bad inital row len " + i);
        }
        if (inflater != null) {
            this.inf = inflater;
            this.infOwn = false;
        } else {
            this.inf = new Inflater();
            this.infOwn = true;
        }
        this.row = (bArr == null || bArr.length < i) ? new byte[i2] : bArr;
        this.rown = -1;
        this.state = State.WAITING_FOR_INPUT;
        try {
            prepareForNextRow(i);
        } catch (RuntimeException e) {
            close();
            throw e;
        }
    }

    public DeflatedChunksSet(String str, int i, int i2) {
        this(str, i, i2, (Inflater) null, (byte[]) null);
    }

    /* access modifiers changed from: protected */
    public void appendNewChunk(DeflatedChunkReader deflatedChunkReader) {
        if (this.chunkid.equals(deflatedChunkReader.getChunkRaw().f121id)) {
            this.curChunk = deflatedChunkReader;
            return;
        }
        throw new PngjInputException("Bad chunk inside IdatSet, id:" + deflatedChunkReader.getChunkRaw().f121id + ", expected:" + this.chunkid);
    }

    /* access modifiers changed from: protected */
    public void processBytes(byte[] bArr, int i, int i2) {
        this.nBytesIn += (long) i2;
        if (i2 >= 1 && !this.state.isDone()) {
            if (this.state == State.ROW_READY) {
                throw new PngjInputException("this should only be called if waitingForMoreInput");
            } else if (this.inf.needsDictionary() || !this.inf.needsInput()) {
                throw new RuntimeException("should not happen");
            } else {
                this.inf.setInput(bArr, i, i2);
                if (isCallbackMode()) {
                    while (inflateData()) {
                        prepareForNextRow(processRowCallback());
                        if (isDone()) {
                            processDoneCallback();
                        }
                    }
                    return;
                }
                inflateData();
            }
        }
    }

    private boolean inflateData() {
        State state2;
        try {
            if (this.state == State.ROW_READY) {
                throw new PngjException("invalid state");
            } else if (this.state.isDone()) {
                return false;
            } else {
                byte[] bArr = this.row;
                if (bArr == null || bArr.length < this.rowlen) {
                    this.row = new byte[this.rowlen];
                }
                if (this.rowfilled < this.rowlen && !this.inf.finished()) {
                    Inflater inflater = this.inf;
                    byte[] bArr2 = this.row;
                    int i = this.rowfilled;
                    int inflate = inflater.inflate(bArr2, i, this.rowlen - i);
                    this.rowfilled += inflate;
                    this.nBytesOut += (long) inflate;
                }
                if (this.rowfilled == this.rowlen) {
                    state2 = State.ROW_READY;
                } else if (!this.inf.finished()) {
                    state2 = State.WAITING_FOR_INPUT;
                } else if (this.rowfilled > 0) {
                    state2 = State.ROW_READY;
                } else {
                    state2 = State.WORK_DONE;
                }
                this.state = state2;
                if (state2 != State.ROW_READY) {
                    return false;
                }
                preProcessRow();
                return true;
            }
        } catch (DataFormatException e) {
            throw new PngjInputException("error decompressing zlib stream ", e);
        } catch (RuntimeException e2) {
            close();
            throw e2;
        }
    }

    /* access modifiers changed from: protected */
    public int processRowCallback() {
        throw new PngjInputException("not implemented");
    }

    public byte[] getInflatedRow() {
        return this.row;
    }

    public void prepareForNextRow(int i) {
        this.rowfilled = 0;
        this.rown++;
        if (i < 1) {
            this.rowlen = 0;
            done();
        } else if (this.inf.finished()) {
            this.rowlen = 0;
            done();
        } else {
            this.state = State.WAITING_FOR_INPUT;
            this.rowlen = i;
            if (!this.callbackMode) {
                inflateData();
            }
        }
    }

    public boolean isWaitingForMoreInput() {
        return this.state == State.WAITING_FOR_INPUT;
    }

    public boolean isRowReady() {
        return this.state == State.ROW_READY;
    }

    public boolean isDone() {
        return this.state.isDone();
    }

    public boolean isTerminated() {
        return this.state.isTerminated();
    }

    public boolean ackNextChunkId(String str) {
        if (this.state.isTerminated()) {
            return false;
        }
        if (str.equals(this.chunkid) || allowOtherChunksInBetween(str)) {
            return true;
        }
        if (this.state.isDone()) {
            if (!isTerminated()) {
                terminate();
            }
            return false;
        }
        throw new PngjInputException("Unexpected chunk " + str + " while " + this.chunkid + " set is not done");
    }

    /* access modifiers changed from: protected */
    public void terminate() {
        close();
    }

    public void close() {
        Inflater inflater;
        try {
            if (!this.state.isTerminated()) {
                this.state = State.TERMINATED;
            }
            if (this.infOwn && (inflater = this.inf) != null) {
                inflater.end();
                this.inf = null;
            }
        } catch (Exception unused) {
        }
    }

    public void done() {
        if (!isDone()) {
            this.state = State.WORK_DONE;
        }
    }

    public int getRowLen() {
        return this.rowlen;
    }

    public int getRowFilled() {
        return this.rowfilled;
    }

    public int getRown() {
        return this.rown;
    }

    public boolean isCallbackMode() {
        return this.callbackMode;
    }

    public void setCallbackMode(boolean z) {
        this.callbackMode = z;
    }

    public long getBytesIn() {
        return this.nBytesIn;
    }

    public long getBytesOut() {
        return this.nBytesOut;
    }

    public String toString() {
        return new StringBuilder("idatSet : " + this.curChunk.getChunkRaw().f121id + " state=" + this.state + " rows=" + this.rown + " bytes=" + this.nBytesIn + "/" + this.nBytesOut).toString();
    }
}
