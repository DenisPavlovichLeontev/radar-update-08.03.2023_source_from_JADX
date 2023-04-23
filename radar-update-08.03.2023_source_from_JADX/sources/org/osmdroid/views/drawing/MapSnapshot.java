package org.osmdroid.views.drawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Looper;
import java.io.File;
import java.util.List;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.tileprovider.TileStates;
import org.osmdroid.util.RectL;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.TilesOverlay;

public class MapSnapshot implements Runnable {
    public static final int INCLUDE_FLAGS_ALL = 15;
    public static final int INCLUDE_FLAG_EXPIRED = 2;
    public static final int INCLUDE_FLAG_NOTFOUND = 8;
    public static final int INCLUDE_FLAG_SCALED = 4;
    public static final int INCLUDE_FLAG_UPTODATE = 1;
    private boolean mAlreadyFinished;
    private Bitmap mBitmap;
    private boolean mCurrentlyRunning;
    private MapSnapshotHandler mHandler;
    private final int mIncludeFlags;
    private boolean mIsDetached;
    private MapSnapshotable mMapSnapshotable;
    private boolean mOneMoreTime;
    private List<Overlay> mOverlays;
    private Projection mProjection;
    private Status mStatus;
    private MapTileProviderBase mTileProvider;
    private TilesOverlay mTilesOverlay;
    private final RectL mViewPort;

    public interface MapSnapshotable {
        void callback(MapSnapshot mapSnapshot);
    }

    public enum Status {
        NOTHING,
        STARTED,
        TILES_OK,
        PAINTING,
        CANVAS_OK
    }

    public static boolean isUIThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public MapSnapshot(MapSnapshotable mapSnapshotable, int i, MapView mapView) {
        this(mapSnapshotable, i, mapView.getTileProvider(), mapView.getOverlays(), mapView.getProjection());
    }

    public MapSnapshot(MapSnapshotable mapSnapshotable, int i, MapTileProviderBase mapTileProviderBase, List<Overlay> list, Projection projection) {
        RectL rectL = new RectL();
        this.mViewPort = rectL;
        this.mStatus = Status.NOTHING;
        this.mMapSnapshotable = mapSnapshotable;
        this.mIncludeFlags = i;
        this.mTileProvider = mapTileProviderBase;
        this.mOverlays = list;
        this.mProjection = projection;
        projection.getMercatorViewPort(rectL);
        TilesOverlay tilesOverlay = new TilesOverlay(this.mTileProvider, (Context) null);
        this.mTilesOverlay = tilesOverlay;
        tilesOverlay.setHorizontalWrapEnabled(this.mProjection.isHorizontalWrapEnabled());
        this.mTilesOverlay.setVerticalWrapEnabled(this.mProjection.isVerticalWrapEnabled());
        this.mHandler = new MapSnapshotHandler(this);
        this.mTileProvider.getTileRequestCompleteHandlers().add(this.mHandler);
    }

    public void run() {
        this.mStatus = Status.STARTED;
        refreshASAP();
    }

    public Status getStatus() {
        return this.mStatus;
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public boolean save(File file) {
        return save(this.mBitmap, file);
    }

    public void onDetach() {
        this.mIsDetached = true;
        this.mProjection = null;
        this.mTileProvider.getTileRequestCompleteHandlers().remove(this.mHandler);
        this.mTileProvider.detach();
        this.mTileProvider = null;
        this.mHandler.destroy();
        this.mHandler = null;
        this.mMapSnapshotable = null;
        this.mTilesOverlay = null;
        this.mOverlays = null;
        this.mBitmap = null;
    }

    private void draw() {
        this.mBitmap = Bitmap.createBitmap(this.mProjection.getWidth(), this.mProjection.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(this.mBitmap);
        this.mProjection.save(canvas, true, false);
        TilesOverlay tilesOverlay = this.mTilesOverlay;
        Projection projection = this.mProjection;
        tilesOverlay.drawTiles(canvas, projection, projection.getZoomLevel(), this.mViewPort);
        List<Overlay> list = this.mOverlays;
        if (list != null) {
            for (Overlay next : list) {
                if (next != null && next.isEnabled()) {
                    next.draw(canvas, this.mProjection);
                }
            }
        }
        this.mProjection.restore(canvas, false);
    }

    private void refresh() {
        if (refreshCheckStart()) {
            TileStates tileStates = this.mTilesOverlay.getTileStates();
            do {
                TilesOverlay tilesOverlay = this.mTilesOverlay;
                Projection projection = this.mProjection;
                tilesOverlay.drawTiles((Canvas) null, projection, projection.getZoomLevel(), this.mViewPort);
                int i = this.mIncludeFlags;
                boolean z = true;
                if (!(i == 0 || i == 15)) {
                    if ((i & 1) == 0 && tileStates.getUpToDate() != 0) {
                        z = false;
                    }
                    if (z && (this.mIncludeFlags & 2) == 0 && tileStates.getExpired() != 0) {
                        z = false;
                    }
                    if (z && (this.mIncludeFlags & 4) == 0 && tileStates.getScaled() != 0) {
                        z = false;
                    }
                    if (z && (this.mIncludeFlags & 8) == 0 && tileStates.getNotFound() != 0) {
                        z = false;
                    }
                }
                if (z) {
                    if (this.mStatus != Status.CANVAS_OK && this.mStatus != Status.PAINTING && refreshCheckFinish()) {
                        this.mStatus = Status.PAINTING;
                        if (!this.mIsDetached) {
                            draw();
                            this.mStatus = Status.CANVAS_OK;
                            MapSnapshotable mapSnapshotable = this.mMapSnapshotable;
                            if (mapSnapshotable != null) {
                                mapSnapshotable.callback(this);
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                }
            } while (refreshCheckEnd());
        }
    }

    private synchronized boolean refreshCheckStart() {
        if (this.mIsDetached) {
            return false;
        }
        if (this.mAlreadyFinished) {
            return false;
        }
        if (!this.mOneMoreTime) {
            return false;
        }
        if (this.mCurrentlyRunning) {
            return false;
        }
        this.mOneMoreTime = false;
        this.mCurrentlyRunning = true;
        return true;
    }

    private synchronized boolean refreshCheckEnd() {
        if (this.mIsDetached) {
            return false;
        }
        if (this.mAlreadyFinished) {
            return false;
        }
        if (!this.mOneMoreTime) {
            this.mCurrentlyRunning = false;
            return false;
        }
        this.mOneMoreTime = false;
        return true;
    }

    private synchronized boolean refreshCheckFinish() {
        boolean z;
        z = !this.mAlreadyFinished;
        this.mAlreadyFinished = true;
        return z;
    }

    private synchronized boolean refreshAgain() {
        this.mOneMoreTime = true;
        return true ^ this.mCurrentlyRunning;
    }

    public void refreshASAP() {
        if (refreshAgain()) {
            refresh();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0029 A[SYNTHETIC, Splitter:B:20:0x0029] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0035 A[SYNTHETIC, Splitter:B:25:0x0035] */
    /* JADX WARNING: Removed duplicated region for block: B:31:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean save(android.graphics.Bitmap r2, java.io.File r3) {
        /*
            r0 = 0
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0023 }
            java.lang.String r3 = r3.getAbsolutePath()     // Catch:{ Exception -> 0x0023 }
            r1.<init>(r3)     // Catch:{ Exception -> 0x0023 }
            android.graphics.Bitmap$CompressFormat r3 = android.graphics.Bitmap.CompressFormat.PNG     // Catch:{ Exception -> 0x001e, all -> 0x001b }
            r0 = 100
            r2.compress(r3, r0, r1)     // Catch:{ Exception -> 0x001e, all -> 0x001b }
            r2 = 1
            r1.close()     // Catch:{ IOException -> 0x0016 }
            goto L_0x001a
        L_0x0016:
            r3 = move-exception
            r3.printStackTrace()
        L_0x001a:
            return r2
        L_0x001b:
            r2 = move-exception
            r0 = r1
            goto L_0x0033
        L_0x001e:
            r2 = move-exception
            r0 = r1
            goto L_0x0024
        L_0x0021:
            r2 = move-exception
            goto L_0x0033
        L_0x0023:
            r2 = move-exception
        L_0x0024:
            r2.printStackTrace()     // Catch:{ all -> 0x0021 }
            if (r0 == 0) goto L_0x0031
            r0.close()     // Catch:{ IOException -> 0x002d }
            goto L_0x0031
        L_0x002d:
            r2 = move-exception
            r2.printStackTrace()
        L_0x0031:
            r2 = 0
            return r2
        L_0x0033:
            if (r0 == 0) goto L_0x003d
            r0.close()     // Catch:{ IOException -> 0x0039 }
            goto L_0x003d
        L_0x0039:
            r3 = move-exception
            r3.printStackTrace()
        L_0x003d:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.views.drawing.MapSnapshot.save(android.graphics.Bitmap, java.io.File):boolean");
    }
}
