package org.osmdroid.mapsforge;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.modules.IFilesystemCache;
import org.osmdroid.tileprovider.modules.MapTileFileStorageProviderBase;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.ITileSource;

public class MapsForgeTileModuleProvider extends MapTileFileStorageProviderBase {
    protected MapsForgeTileSource tileSource;
    protected IFilesystemCache tilewriter;

    /* access modifiers changed from: protected */
    public String getName() {
        return "MapsforgeTiles Provider";
    }

    /* access modifiers changed from: protected */
    public String getThreadGroupName() {
        return "mapsforgetilesprovider";
    }

    public boolean getUsesDataConnection() {
        return false;
    }

    public MapsForgeTileModuleProvider(IRegisterReceiver iRegisterReceiver, MapsForgeTileSource mapsForgeTileSource, IFilesystemCache iFilesystemCache) {
        super(iRegisterReceiver, Configuration.getInstance().getTileFileSystemThreads(), Configuration.getInstance().getTileFileSystemMaxQueueSize());
        this.tileSource = mapsForgeTileSource;
        this.tilewriter = iFilesystemCache;
    }

    public TileLoader getTileLoader() {
        return new TileLoader();
    }

    public int getMinimumZoomLevel() {
        return this.tileSource.getMinimumZoomLevel();
    }

    public int getMaximumZoomLevel() {
        return this.tileSource.getMaximumZoomLevel();
    }

    public void setTileSource(ITileSource iTileSource) {
        if (iTileSource instanceof MapsForgeTileSource) {
            this.tileSource = (MapsForgeTileSource) iTileSource;
        }
    }

    private class TileLoader extends MapTileModuleProviderBase.TileLoader {
        private TileLoader() {
            super();
        }

        /* JADX WARNING: Removed duplicated region for block: B:31:0x00be A[SYNTHETIC, Splitter:B:31:0x00be] */
        /* JADX WARNING: Removed duplicated region for block: B:34:0x00c4 A[SYNTHETIC, Splitter:B:34:0x00c4] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public android.graphics.drawable.Drawable loadTile(long r13) {
            /*
                r12 = this;
                org.osmdroid.config.IConfigurationProvider r0 = org.osmdroid.config.Configuration.getInstance()
                boolean r0 = r0.isDebugTileProviders()
                r1 = 0
                java.lang.String r2 = "OsmDroid"
                if (r0 == 0) goto L_0x003c
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                r0.<init>()
                java.lang.String r3 = "MapsForgeTileModuleProvider.TileLoader.loadTile("
                r0.append(r3)
                java.lang.String r3 = org.osmdroid.util.MapTileIndex.toString(r13)
                r0.append(r3)
                java.lang.String r3 = "): "
                r0.append(r3)
                java.lang.String r0 = r0.toString()
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                r3.<init>()
                r3.append(r0)
                java.lang.String r4 = "tileSource.renderTile"
                r3.append(r4)
                java.lang.String r3 = r3.toString()
                android.util.Log.d(r2, r3)
                goto L_0x003d
            L_0x003c:
                r0 = r1
            L_0x003d:
                org.osmdroid.mapsforge.MapsForgeTileModuleProvider r3 = org.osmdroid.mapsforge.MapsForgeTileModuleProvider.this
                org.osmdroid.mapsforge.MapsForgeTileSource r3 = r3.tileSource
                android.graphics.drawable.Drawable r3 = r3.renderTile(r13)
                if (r3 == 0) goto L_0x00c8
                boolean r4 = r3 instanceof android.graphics.drawable.BitmapDrawable
                if (r4 == 0) goto L_0x00c8
                java.io.ByteArrayOutputStream r4 = new java.io.ByteArrayOutputStream
                r4.<init>()
                r5 = r3
                android.graphics.drawable.BitmapDrawable r5 = (android.graphics.drawable.BitmapDrawable) r5
                android.graphics.Bitmap r5 = r5.getBitmap()
                android.graphics.Bitmap$CompressFormat r6 = android.graphics.Bitmap.CompressFormat.PNG
                r7 = 100
                r5.compress(r6, r7, r4)
                byte[] r5 = r4.toByteArray()
                r4.close()     // Catch:{ IOException -> 0x0065 }
            L_0x0065:
                org.osmdroid.config.IConfigurationProvider r4 = org.osmdroid.config.Configuration.getInstance()
                boolean r4 = r4.isDebugTileProviders()
                if (r4 == 0) goto L_0x0097
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                r4.append(r0)
                java.lang.String r0 = "save tile "
                r4.append(r0)
                int r0 = r5.length
                r4.append(r0)
                java.lang.String r0 = " bytes to "
                r4.append(r0)
                org.osmdroid.mapsforge.MapsForgeTileModuleProvider r0 = org.osmdroid.mapsforge.MapsForgeTileModuleProvider.this
                org.osmdroid.mapsforge.MapsForgeTileSource r0 = r0.tileSource
                java.lang.String r0 = r0.getTileRelativeFilenameString(r13)
                r4.append(r0)
                java.lang.String r0 = r4.toString()
                android.util.Log.d(r2, r0)
            L_0x0097:
                java.io.ByteArrayInputStream r0 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x00b6 }
                r0.<init>(r5)     // Catch:{ Exception -> 0x00b6 }
                org.osmdroid.mapsforge.MapsForgeTileModuleProvider r1 = org.osmdroid.mapsforge.MapsForgeTileModuleProvider.this     // Catch:{ Exception -> 0x00b1, all -> 0x00ae }
                org.osmdroid.tileprovider.modules.IFilesystemCache r6 = r1.tilewriter     // Catch:{ Exception -> 0x00b1, all -> 0x00ae }
                org.osmdroid.mapsforge.MapsForgeTileModuleProvider r1 = org.osmdroid.mapsforge.MapsForgeTileModuleProvider.this     // Catch:{ Exception -> 0x00b1, all -> 0x00ae }
                org.osmdroid.mapsforge.MapsForgeTileSource r7 = r1.tileSource     // Catch:{ Exception -> 0x00b1, all -> 0x00ae }
                r11 = 0
                r8 = r13
                r10 = r0
                r6.saveFile(r7, r8, r10, r11)     // Catch:{ Exception -> 0x00b1, all -> 0x00ae }
                r0.close()     // Catch:{ IOException -> 0x00c8 }
                goto L_0x00c8
            L_0x00ae:
                r13 = move-exception
                r1 = r0
                goto L_0x00c2
            L_0x00b1:
                r13 = move-exception
                r1 = r0
                goto L_0x00b7
            L_0x00b4:
                r13 = move-exception
                goto L_0x00c2
            L_0x00b6:
                r13 = move-exception
            L_0x00b7:
                java.lang.String r14 = "forge error storing tile cache"
                android.util.Log.w(r2, r14, r13)     // Catch:{ all -> 0x00b4 }
                if (r1 == 0) goto L_0x00c8
                r1.close()     // Catch:{ IOException -> 0x00c8 }
                goto L_0x00c8
            L_0x00c2:
                if (r1 == 0) goto L_0x00c7
                r1.close()     // Catch:{ IOException -> 0x00c7 }
            L_0x00c7:
                throw r13
            L_0x00c8:
                return r3
            */
            throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.mapsforge.MapsForgeTileModuleProvider.TileLoader.loadTile(long):android.graphics.drawable.Drawable");
        }
    }
}
