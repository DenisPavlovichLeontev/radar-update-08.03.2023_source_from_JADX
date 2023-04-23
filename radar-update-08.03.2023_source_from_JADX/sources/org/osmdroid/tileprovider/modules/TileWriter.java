package org.osmdroid.tileprovider.modules;

import android.graphics.drawable.Drawable;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.ExpirableBitmapDrawable;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;

public class TileWriter implements IFilesystemCache {
    static boolean hasInited = false;
    /* access modifiers changed from: private */
    public static long mUsedCacheSpace;
    Thread initThread = null;
    private long mMaximumCachedFileAge;

    public Long getExpirationTimestamp(ITileSource iTileSource, long j) {
        return null;
    }

    public TileWriter() {
        if (!hasInited) {
            hasInited = true;
            C13621 r1 = new Thread() {
                public void run() {
                    long unused = TileWriter.mUsedCacheSpace = 0;
                    TileWriter.this.calculateDirectorySize(Configuration.getInstance().getOsmdroidTileCache());
                    if (TileWriter.mUsedCacheSpace > Configuration.getInstance().getTileFileSystemCacheMaxBytes()) {
                        TileWriter.this.cutCurrentCache();
                    }
                    if (Configuration.getInstance().isDebugMode()) {
                        Log.d(IMapView.LOGTAG, "Finished init thread");
                    }
                }
            };
            this.initThread = r1;
            r1.setName("TileWriter#init");
            this.initThread.setPriority(1);
            this.initThread.start();
        }
    }

    public static long getUsedCacheSpace() {
        return mUsedCacheSpace;
    }

    public void setMaximumCachedFileAge(long j) {
        this.mMaximumCachedFileAge = j;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0076, code lost:
        org.osmdroid.tileprovider.util.StreamUtils.closeStream(r5);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:22:0x006f */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0076  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x007c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean saveFile(org.osmdroid.tileprovider.tilesource.ITileSource r4, long r5, java.io.InputStream r7, java.lang.Long r8) {
        /*
            r3 = this;
            java.io.File r4 = r3.getFile(r4, r5)
            org.osmdroid.config.IConfigurationProvider r5 = org.osmdroid.config.Configuration.getInstance()
            boolean r5 = r5.isDebugTileProviders()
            if (r5 == 0) goto L_0x0028
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "TileWrite "
            r5.append(r6)
            java.lang.String r6 = r4.getAbsolutePath()
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            java.lang.String r6 = "OsmDroid"
            android.util.Log.d(r6, r5)
        L_0x0028:
            java.io.File r5 = r4.getParentFile()
            boolean r6 = r5.exists()
            r8 = 0
            if (r6 != 0) goto L_0x003a
            boolean r5 = r3.createFolderAndCheckIfExists(r5)
            if (r5 != 0) goto L_0x003a
            return r8
        L_0x003a:
            r5 = 0
            r6 = 1
            java.io.BufferedOutputStream r0 = new java.io.BufferedOutputStream     // Catch:{ IOException -> 0x006f }
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x006f }
            java.lang.String r4 = r4.getPath()     // Catch:{ IOException -> 0x006f }
            r1.<init>(r4)     // Catch:{ IOException -> 0x006f }
            r4 = 8192(0x2000, float:1.14794E-41)
            r0.<init>(r1, r4)     // Catch:{ IOException -> 0x006f }
            long r4 = org.osmdroid.tileprovider.util.StreamUtils.copy(r7, r0)     // Catch:{ IOException -> 0x006b, all -> 0x0068 }
            long r1 = mUsedCacheSpace     // Catch:{ IOException -> 0x006b, all -> 0x0068 }
            long r1 = r1 + r4
            mUsedCacheSpace = r1     // Catch:{ IOException -> 0x006b, all -> 0x0068 }
            org.osmdroid.config.IConfigurationProvider r4 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ IOException -> 0x006b, all -> 0x0068 }
            long r4 = r4.getTileFileSystemCacheMaxBytes()     // Catch:{ IOException -> 0x006b, all -> 0x0068 }
            int r4 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1))
            if (r4 <= 0) goto L_0x0064
            r3.cutCurrentCache()     // Catch:{ IOException -> 0x006b, all -> 0x0068 }
        L_0x0064:
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r0)
            return r6
        L_0x0068:
            r4 = move-exception
            r5 = r0
            goto L_0x007a
        L_0x006b:
            r5 = r0
            goto L_0x006f
        L_0x006d:
            r4 = move-exception
            goto L_0x007a
        L_0x006f:
            int r4 = org.osmdroid.tileprovider.util.Counters.fileCacheSaveErrors     // Catch:{ all -> 0x006d }
            int r4 = r4 + r6
            org.osmdroid.tileprovider.util.Counters.fileCacheSaveErrors = r4     // Catch:{ all -> 0x006d }
            if (r5 == 0) goto L_0x0079
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r5)
        L_0x0079:
            return r8
        L_0x007a:
            if (r5 == 0) goto L_0x007f
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r5)
        L_0x007f:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.modules.TileWriter.saveFile(org.osmdroid.tileprovider.tilesource.ITileSource, long, java.io.InputStream, java.lang.Long):boolean");
    }

    public void onDetach() {
        Thread thread = this.initThread;
        if (thread != null) {
            try {
                thread.interrupt();
            } catch (Throwable unused) {
            }
        }
    }

    public boolean remove(ITileSource iTileSource, long j) {
        File file = getFile(iTileSource, j);
        if (!file.exists()) {
            return false;
        }
        try {
            return file.delete();
        } catch (Exception e) {
            Log.i(IMapView.LOGTAG, "Unable to delete cached tile from " + iTileSource.name() + " " + MapTileIndex.toString(j), e);
            return false;
        }
    }

    public File getFile(ITileSource iTileSource, long j) {
        File osmdroidTileCache = Configuration.getInstance().getOsmdroidTileCache();
        return new File(osmdroidTileCache, iTileSource.getTileRelativeFilenameString(j) + OpenStreetMapTileProviderConstants.TILE_PATH_EXTENSION);
    }

    public boolean exists(ITileSource iTileSource, long j) {
        return getFile(iTileSource, j).exists();
    }

    private boolean createFolderAndCheckIfExists(File file) {
        if (file.mkdirs()) {
            return true;
        }
        if (Configuration.getInstance().isDebugMode()) {
            Log.d(IMapView.LOGTAG, "Failed to create " + file + " - wait and check again");
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException unused) {
        }
        if (file.exists()) {
            if (Configuration.getInstance().isDebugMode()) {
                Log.d(IMapView.LOGTAG, "Seems like another thread created " + file);
            }
            return true;
        } else if (!Configuration.getInstance().isDebugMode()) {
            return false;
        } else {
            Log.d(IMapView.LOGTAG, "File still doesn't exist: " + file);
            return false;
        }
    }

    /* access modifiers changed from: private */
    public void calculateDirectorySize(File file) {
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                if (file2.isFile()) {
                    mUsedCacheSpace += file2.length();
                }
                if (file2.isDirectory() && !isSymbolicDirectoryLink(file, file2)) {
                    calculateDirectorySize(file2);
                }
            }
        }
    }

    private boolean isSymbolicDirectoryLink(File file, File file2) {
        try {
            return !file.getCanonicalPath().equals(file2.getCanonicalFile().getParent());
        } catch (IOException | NoSuchElementException unused) {
            return true;
        }
    }

    private List<File> getDirectoryFileList(File file) {
        ArrayList arrayList = new ArrayList();
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                if (file2.isFile()) {
                    arrayList.add(file2);
                }
                if (file2.isDirectory()) {
                    arrayList.addAll(getDirectoryFileList(file2));
                }
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    public void cutCurrentCache() {
        synchronized (Configuration.getInstance().getOsmdroidTileCache()) {
            if (mUsedCacheSpace > Configuration.getInstance().getTileFileSystemCacheTrimBytes()) {
                Log.d(IMapView.LOGTAG, "Trimming tile cache from " + mUsedCacheSpace + " to " + Configuration.getInstance().getTileFileSystemCacheTrimBytes());
                int i = 0;
                File[] fileArr = (File[]) getDirectoryFileList(Configuration.getInstance().getOsmdroidTileCache()).toArray(new File[0]);
                Arrays.sort(fileArr, new Comparator<File>() {
                    public int compare(File file, File file2) {
                        return Long.valueOf(file.lastModified()).compareTo(Long.valueOf(file2.lastModified()));
                    }
                });
                int length = fileArr.length;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    File file = fileArr[i];
                    if (mUsedCacheSpace <= Configuration.getInstance().getTileFileSystemCacheTrimBytes()) {
                        break;
                    }
                    long length2 = file.length();
                    if (file.delete()) {
                        if (Configuration.getInstance().isDebugTileProviders()) {
                            Log.d(IMapView.LOGTAG, "Cache trim deleting " + file.getAbsolutePath());
                        }
                        mUsedCacheSpace -= length2;
                    }
                    i++;
                }
                Log.d(IMapView.LOGTAG, "Finished trimming tile cache");
            }
        }
    }

    public Drawable loadTile(ITileSource iTileSource, long j) throws Exception {
        File file = getFile(iTileSource, j);
        if (!file.exists()) {
            return null;
        }
        Drawable drawable = iTileSource.getDrawable(file.getPath());
        if ((file.lastModified() < System.currentTimeMillis() - this.mMaximumCachedFileAge) && drawable != null) {
            if (Configuration.getInstance().isDebugMode()) {
                Log.d(IMapView.LOGTAG, "Tile expired: " + MapTileIndex.toString(j));
            }
            ExpirableBitmapDrawable.setState(drawable, -2);
        }
        return drawable;
    }
}
