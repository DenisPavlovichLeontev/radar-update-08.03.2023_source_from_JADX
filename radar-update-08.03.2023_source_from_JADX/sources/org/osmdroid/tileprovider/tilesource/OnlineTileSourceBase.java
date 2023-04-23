package org.osmdroid.tileprovider.tilesource;

import java.util.concurrent.Semaphore;

public abstract class OnlineTileSourceBase extends BitmapTileSourceBase {
    private final String[] mBaseUrls;
    private final Semaphore mSemaphore;
    private final TileSourcePolicy mTileSourcePolicy;

    public abstract String getTileURLString(long j);

    public OnlineTileSourceBase(String str, int i, int i2, int i3, String str2, String[] strArr) {
        this(str, i, i2, i3, str2, strArr, (String) null);
    }

    public OnlineTileSourceBase(String str, int i, int i2, int i3, String str2, String[] strArr, String str3) {
        this(str, i, i2, i3, str2, strArr, str3, new TileSourcePolicy());
    }

    public OnlineTileSourceBase(String str, int i, int i2, int i3, String str2, String[] strArr, String str3, TileSourcePolicy tileSourcePolicy) {
        super(str, i, i2, i3, str2, str3);
        this.mBaseUrls = strArr;
        this.mTileSourcePolicy = tileSourcePolicy;
        if (tileSourcePolicy.getMaxConcurrent() > 0) {
            this.mSemaphore = new Semaphore(tileSourcePolicy.getMaxConcurrent(), true);
        } else {
            this.mSemaphore = null;
        }
    }

    public String getBaseUrl() {
        String[] strArr = this.mBaseUrls;
        return (strArr == null || strArr.length <= 0) ? "" : strArr[this.random.nextInt(this.mBaseUrls.length)];
    }

    public void acquire() throws InterruptedException {
        Semaphore semaphore = this.mSemaphore;
        if (semaphore != null) {
            semaphore.acquire();
        }
    }

    public void release() {
        Semaphore semaphore = this.mSemaphore;
        if (semaphore != null) {
            semaphore.release();
        }
    }

    public TileSourcePolicy getTileSourcePolicy() {
        return this.mTileSourcePolicy;
    }
}
