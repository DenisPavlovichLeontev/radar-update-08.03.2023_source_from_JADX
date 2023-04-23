package org.osmdroid.tileprovider.modules;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.BitmapPool;
import org.osmdroid.tileprovider.ExpirableBitmapDrawable;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.util.TileSystem;

public class MapTileApproximater extends MapTileModuleProviderBase {
    private final List<MapTileModuleProviderBase> mProviders;
    private int minZoomLevel;

    /* access modifiers changed from: protected */
    public String getName() {
        return "Offline Tile Approximation Provider";
    }

    /* access modifiers changed from: protected */
    public String getThreadGroupName() {
        return "approximater";
    }

    public boolean getUsesDataConnection() {
        return false;
    }

    @Deprecated
    public void setTileSource(ITileSource iTileSource) {
    }

    public MapTileApproximater() {
        this(Configuration.getInstance().getTileFileSystemThreads(), Configuration.getInstance().getTileFileSystemMaxQueueSize());
    }

    public MapTileApproximater(int i, int i2) {
        super(i, i2);
        this.mProviders = new CopyOnWriteArrayList();
    }

    public void addProvider(MapTileModuleProviderBase mapTileModuleProviderBase) {
        this.mProviders.add(mapTileModuleProviderBase);
        computeZoomLevels();
    }

    private void computeZoomLevels() {
        this.minZoomLevel = 0;
        boolean z = true;
        for (MapTileModuleProviderBase minimumZoomLevel : this.mProviders) {
            int minimumZoomLevel2 = minimumZoomLevel.getMinimumZoomLevel();
            if (z) {
                this.minZoomLevel = minimumZoomLevel2;
                z = false;
            } else {
                this.minZoomLevel = Math.min(this.minZoomLevel, minimumZoomLevel2);
            }
        }
    }

    public TileLoader getTileLoader() {
        return new TileLoader();
    }

    public int getMinimumZoomLevel() {
        return this.minZoomLevel;
    }

    public int getMaximumZoomLevel() {
        return TileSystem.getMaximumZoomLevel();
    }

    protected class TileLoader extends MapTileModuleProviderBase.TileLoader {
        protected TileLoader() {
            super();
        }

        public Drawable loadTile(long j) {
            Bitmap approximateTileFromLowerZoom = MapTileApproximater.this.approximateTileFromLowerZoom(j);
            if (approximateTileFromLowerZoom == null) {
                return null;
            }
            BitmapDrawable bitmapDrawable = new BitmapDrawable(approximateTileFromLowerZoom);
            ExpirableBitmapDrawable.setState(bitmapDrawable, -3);
            return bitmapDrawable;
        }
    }

    public Bitmap approximateTileFromLowerZoom(long j) {
        for (int i = 1; MapTileIndex.getZoom(j) - i >= 0; i++) {
            Bitmap approximateTileFromLowerZoom = approximateTileFromLowerZoom(j, i);
            if (approximateTileFromLowerZoom != null) {
                return approximateTileFromLowerZoom;
            }
        }
        return null;
    }

    public Bitmap approximateTileFromLowerZoom(long j, int i) {
        for (MapTileModuleProviderBase approximateTileFromLowerZoom : this.mProviders) {
            Bitmap approximateTileFromLowerZoom2 = approximateTileFromLowerZoom(approximateTileFromLowerZoom, j, i);
            if (approximateTileFromLowerZoom2 != null) {
                return approximateTileFromLowerZoom2;
            }
        }
        return null;
    }

    public static Bitmap approximateTileFromLowerZoom(MapTileModuleProviderBase mapTileModuleProviderBase, long j, int i) {
        int zoom;
        if (i <= 0 || (zoom = MapTileIndex.getZoom(j) - i) < mapTileModuleProviderBase.getMinimumZoomLevel() || zoom > mapTileModuleProviderBase.getMaximumZoomLevel()) {
            return null;
        }
        try {
            Drawable loadTileIfReachable = mapTileModuleProviderBase.getTileLoader().loadTileIfReachable(MapTileIndex.getTileIndex(zoom, MapTileIndex.getX(j) >> i, MapTileIndex.getY(j) >> i));
            if (!(loadTileIfReachable instanceof BitmapDrawable)) {
                return null;
            }
            return approximateTileFromLowerZoom((BitmapDrawable) loadTileIfReachable, j, i);
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x005a  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x005f A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0060 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.Bitmap approximateTileFromLowerZoom(android.graphics.drawable.BitmapDrawable r10, long r11, int r13) {
        /*
            r0 = 0
            if (r13 > 0) goto L_0x0004
            return r0
        L_0x0004:
            android.graphics.Bitmap r1 = r10.getBitmap()
            int r1 = r1.getWidth()
            android.graphics.Bitmap r2 = getTileBitmap(r1)
            android.graphics.Canvas r3 = new android.graphics.Canvas
            r3.<init>(r2)
            boolean r4 = r10 instanceof org.osmdroid.tileprovider.ReusableBitmapDrawable
            if (r4 == 0) goto L_0x001d
            r5 = r10
            org.osmdroid.tileprovider.ReusableBitmapDrawable r5 = (org.osmdroid.tileprovider.ReusableBitmapDrawable) r5
            goto L_0x001e
        L_0x001d:
            r5 = r0
        L_0x001e:
            if (r4 == 0) goto L_0x0023
            r5.beginUsingDrawable()
        L_0x0023:
            r6 = 1
            r7 = 0
            if (r4 == 0) goto L_0x0030
            boolean r8 = r5.isBitmapValid()     // Catch:{ all -> 0x002e }
            if (r8 == 0) goto L_0x0034
            goto L_0x0030
        L_0x002e:
            r10 = move-exception
            goto L_0x0061
        L_0x0030:
            int r8 = r1 >> r13
            if (r8 != 0) goto L_0x0036
        L_0x0034:
            r6 = r7
            goto L_0x0058
        L_0x0036:
            int r9 = org.osmdroid.util.MapTileIndex.getX(r11)     // Catch:{ all -> 0x002e }
            int r13 = r6 << r13
            int r9 = r9 % r13
            int r9 = r9 * r8
            int r11 = org.osmdroid.util.MapTileIndex.getY(r11)     // Catch:{ all -> 0x002e }
            int r11 = r11 % r13
            int r11 = r11 * r8
            android.graphics.Rect r12 = new android.graphics.Rect     // Catch:{ all -> 0x002e }
            int r13 = r9 + r8
            int r8 = r8 + r11
            r12.<init>(r9, r11, r13, r8)     // Catch:{ all -> 0x002e }
            android.graphics.Rect r11 = new android.graphics.Rect     // Catch:{ all -> 0x002e }
            r11.<init>(r7, r7, r1, r1)     // Catch:{ all -> 0x002e }
            android.graphics.Bitmap r10 = r10.getBitmap()     // Catch:{ all -> 0x002e }
            r3.drawBitmap(r10, r12, r11, r0)     // Catch:{ all -> 0x002e }
        L_0x0058:
            if (r4 == 0) goto L_0x005d
            r5.finishUsingDrawable()
        L_0x005d:
            if (r6 != 0) goto L_0x0060
            return r0
        L_0x0060:
            return r2
        L_0x0061:
            if (r4 == 0) goto L_0x0066
            r5.finishUsingDrawable()
        L_0x0066:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.modules.MapTileApproximater.approximateTileFromLowerZoom(android.graphics.drawable.BitmapDrawable, long, int):android.graphics.Bitmap");
    }

    public static Bitmap getTileBitmap(int i) {
        Bitmap obtainSizedBitmapFromPool = BitmapPool.getInstance().obtainSizedBitmapFromPool(i, i);
        if (obtainSizedBitmapFromPool == null) {
            return Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
        }
        if (Build.VERSION.SDK_INT >= 12) {
            obtainSizedBitmapFromPool.setHasAlpha(true);
        }
        obtainSizedBitmapFromPool.eraseColor(0);
        return obtainSizedBitmapFromPool;
    }

    public void detach() {
        super.detach();
        this.mProviders.clear();
    }
}
