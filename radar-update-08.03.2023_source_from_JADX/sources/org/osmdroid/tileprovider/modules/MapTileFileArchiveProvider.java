package org.osmdroid.tileprovider.modules;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.TileSystem;

public class MapTileFileArchiveProvider extends MapTileFileStorageProviderBase {
    private final boolean ignoreTileSource;
    private final ArrayList<IArchiveFile> mArchiveFiles;
    private final boolean mSpecificArchivesProvided;
    /* access modifiers changed from: private */
    public final AtomicReference<ITileSource> mTileSource;

    /* access modifiers changed from: protected */
    public String getName() {
        return "File Archive Provider";
    }

    /* access modifiers changed from: protected */
    public String getThreadGroupName() {
        return "filearchive";
    }

    public boolean getUsesDataConnection() {
        return false;
    }

    public MapTileFileArchiveProvider(IRegisterReceiver iRegisterReceiver, ITileSource iTileSource, IArchiveFile[] iArchiveFileArr) {
        this(iRegisterReceiver, iTileSource, iArchiveFileArr, false);
    }

    public MapTileFileArchiveProvider(IRegisterReceiver iRegisterReceiver, ITileSource iTileSource, IArchiveFile[] iArchiveFileArr, boolean z) {
        super(iRegisterReceiver, Configuration.getInstance().getTileFileSystemThreads(), Configuration.getInstance().getTileFileSystemMaxQueueSize());
        this.mArchiveFiles = new ArrayList<>();
        this.mTileSource = new AtomicReference<>();
        this.ignoreTileSource = z;
        setTileSource(iTileSource);
        if (iArchiveFileArr == null) {
            this.mSpecificArchivesProvided = false;
            findArchiveFiles();
            return;
        }
        this.mSpecificArchivesProvided = true;
        for (int length = iArchiveFileArr.length - 1; length >= 0; length--) {
            this.mArchiveFiles.add(iArchiveFileArr[length]);
        }
    }

    public MapTileFileArchiveProvider(IRegisterReceiver iRegisterReceiver, ITileSource iTileSource) {
        this(iRegisterReceiver, iTileSource, (IArchiveFile[]) null);
    }

    public TileLoader getTileLoader() {
        return new TileLoader();
    }

    public int getMinimumZoomLevel() {
        ITileSource iTileSource = this.mTileSource.get();
        if (iTileSource != null) {
            return iTileSource.getMinimumZoomLevel();
        }
        return 0;
    }

    public int getMaximumZoomLevel() {
        ITileSource iTileSource = this.mTileSource.get();
        if (iTileSource != null) {
            return iTileSource.getMaximumZoomLevel();
        }
        return TileSystem.getMaximumZoomLevel();
    }

    /* access modifiers changed from: protected */
    public void onMediaMounted() {
        if (!this.mSpecificArchivesProvided) {
            findArchiveFiles();
        }
    }

    /* access modifiers changed from: protected */
    public void onMediaUnmounted() {
        if (!this.mSpecificArchivesProvided) {
            findArchiveFiles();
        }
    }

    public void setTileSource(ITileSource iTileSource) {
        this.mTileSource.set(iTileSource);
    }

    public void detach() {
        clearArcives();
        super.detach();
    }

    private void clearArcives() {
        while (!this.mArchiveFiles.isEmpty()) {
            IArchiveFile iArchiveFile = this.mArchiveFiles.get(0);
            if (iArchiveFile != null) {
                iArchiveFile.close();
            }
            this.mArchiveFiles.remove(0);
        }
    }

    private void findArchiveFiles() {
        File[] listFiles;
        clearArcives();
        File osmdroidBasePath = Configuration.getInstance().getOsmdroidBasePath();
        if (osmdroidBasePath != null && (listFiles = osmdroidBasePath.listFiles()) != null) {
            for (File archiveFile : listFiles) {
                IArchiveFile archiveFile2 = ArchiveFileFactory.getArchiveFile(archiveFile);
                if (archiveFile2 != null) {
                    archiveFile2.setIgnoreTileSource(this.ignoreTileSource);
                    this.mArchiveFiles.add(archiveFile2);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0048, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.io.InputStream getInputStream(long r5, org.osmdroid.tileprovider.tilesource.ITileSource r7) {
        /*
            r4 = this;
            monitor-enter(r4)
            java.util.ArrayList<org.osmdroid.tileprovider.modules.IArchiveFile> r0 = r4.mArchiveFiles     // Catch:{ all -> 0x004c }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ all -> 0x004c }
        L_0x0007:
            boolean r1 = r0.hasNext()     // Catch:{ all -> 0x004c }
            if (r1 == 0) goto L_0x0049
            java.lang.Object r1 = r0.next()     // Catch:{ all -> 0x004c }
            org.osmdroid.tileprovider.modules.IArchiveFile r1 = (org.osmdroid.tileprovider.modules.IArchiveFile) r1     // Catch:{ all -> 0x004c }
            if (r1 == 0) goto L_0x0007
            java.io.InputStream r2 = r1.getInputStream(r7, r5)     // Catch:{ all -> 0x004c }
            if (r2 == 0) goto L_0x0007
            org.osmdroid.config.IConfigurationProvider r7 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ all -> 0x004c }
            boolean r7 = r7.isDebugMode()     // Catch:{ all -> 0x004c }
            if (r7 == 0) goto L_0x0047
            java.lang.String r7 = "OsmDroid"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x004c }
            r0.<init>()     // Catch:{ all -> 0x004c }
            java.lang.String r3 = "Found tile "
            r0.append(r3)     // Catch:{ all -> 0x004c }
            java.lang.String r5 = org.osmdroid.util.MapTileIndex.toString(r5)     // Catch:{ all -> 0x004c }
            r0.append(r5)     // Catch:{ all -> 0x004c }
            java.lang.String r5 = " in "
            r0.append(r5)     // Catch:{ all -> 0x004c }
            r0.append(r1)     // Catch:{ all -> 0x004c }
            java.lang.String r5 = r0.toString()     // Catch:{ all -> 0x004c }
            android.util.Log.d(r7, r5)     // Catch:{ all -> 0x004c }
        L_0x0047:
            monitor-exit(r4)
            return r2
        L_0x0049:
            r5 = 0
            monitor-exit(r4)
            return r5
        L_0x004c:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider.getInputStream(long, org.osmdroid.tileprovider.tilesource.ITileSource):java.io.InputStream");
    }

    protected class TileLoader extends MapTileModuleProviderBase.TileLoader {
        protected TileLoader() {
            super();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0065, code lost:
            if (r3 != null) goto L_0x0067;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0067, code lost:
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0072, code lost:
            if (r3 != null) goto L_0x0067;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0075, code lost:
            return r2;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public android.graphics.drawable.Drawable loadTile(long r7) {
            /*
                r6 = this;
                java.lang.String r0 = "OsmDroid"
                org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider r1 = org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider.this
                java.util.concurrent.atomic.AtomicReference r1 = r1.mTileSource
                java.lang.Object r1 = r1.get()
                org.osmdroid.tileprovider.tilesource.ITileSource r1 = (org.osmdroid.tileprovider.tilesource.ITileSource) r1
                r2 = 0
                if (r1 != 0) goto L_0x0012
                return r2
            L_0x0012:
                org.osmdroid.config.IConfigurationProvider r3 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ all -> 0x006b }
                boolean r3 = r3.isDebugMode()     // Catch:{ all -> 0x006b }
                if (r3 == 0) goto L_0x0034
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x006b }
                r3.<init>()     // Catch:{ all -> 0x006b }
                java.lang.String r4 = "Archives - Tile doesn't exist: "
                r3.append(r4)     // Catch:{ all -> 0x006b }
                java.lang.String r4 = org.osmdroid.util.MapTileIndex.toString(r7)     // Catch:{ all -> 0x006b }
                r3.append(r4)     // Catch:{ all -> 0x006b }
                java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x006b }
                android.util.Log.d(r0, r3)     // Catch:{ all -> 0x006b }
            L_0x0034:
                org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider r3 = org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider.this     // Catch:{ all -> 0x006b }
                java.io.InputStream r3 = r3.getInputStream(r7, r1)     // Catch:{ all -> 0x006b }
                if (r3 == 0) goto L_0x0065
                org.osmdroid.config.IConfigurationProvider r4 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ all -> 0x0063 }
                boolean r4 = r4.isDebugMode()     // Catch:{ all -> 0x0063 }
                if (r4 == 0) goto L_0x005e
                java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0063 }
                r4.<init>()     // Catch:{ all -> 0x0063 }
                java.lang.String r5 = "Use tile from archive: "
                r4.append(r5)     // Catch:{ all -> 0x0063 }
                java.lang.String r7 = org.osmdroid.util.MapTileIndex.toString(r7)     // Catch:{ all -> 0x0063 }
                r4.append(r7)     // Catch:{ all -> 0x0063 }
                java.lang.String r7 = r4.toString()     // Catch:{ all -> 0x0063 }
                android.util.Log.d(r0, r7)     // Catch:{ all -> 0x0063 }
            L_0x005e:
                android.graphics.drawable.Drawable r2 = r1.getDrawable((java.io.InputStream) r3)     // Catch:{ all -> 0x0063 }
                goto L_0x0065
            L_0x0063:
                r7 = move-exception
                goto L_0x006d
            L_0x0065:
                if (r3 == 0) goto L_0x0075
            L_0x0067:
                org.osmdroid.tileprovider.util.StreamUtils.closeStream(r3)
                goto L_0x0075
            L_0x006b:
                r7 = move-exception
                r3 = r2
            L_0x006d:
                java.lang.String r8 = "Error loading tile"
                android.util.Log.e(r0, r8, r7)     // Catch:{ all -> 0x0076 }
                if (r3 == 0) goto L_0x0075
                goto L_0x0067
            L_0x0075:
                return r2
            L_0x0076:
                r7 = move-exception
                if (r3 == 0) goto L_0x007c
                org.osmdroid.tileprovider.util.StreamUtils.closeStream(r3)
            L_0x007c:
                throw r7
            */
            throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider.TileLoader.loadTile(long):android.graphics.drawable.Drawable");
        }
    }
}
