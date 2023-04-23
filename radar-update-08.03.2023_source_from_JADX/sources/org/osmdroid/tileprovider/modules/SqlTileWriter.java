package org.osmdroid.tileprovider.modules;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteFullException;
import android.graphics.Rect;
import android.util.Log;
import com.j256.ormlite.stmt.query.SimpleComparison;
import java.io.File;
import java.util.Collection;
import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.util.Counters;
import org.osmdroid.util.GarbageCollector;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.util.SplashScreenable;

public class SqlTileWriter implements IFilesystemCache, SplashScreenable {
    public static final String COLUMN_EXPIRES = "expires";
    public static final String COLUMN_EXPIRES_INDEX = "expires_index";
    public static final String DATABASE_FILENAME = "cache.db";
    private static boolean cleanOnStartup = true;
    protected static File db_file = null;
    private static final String[] expireQueryColumn = {COLUMN_EXPIRES};
    static boolean hasInited = false;
    protected static SQLiteDatabase mDb = null;
    private static final Object mLock = new Object();
    private static final String primaryKey = "key=? and provider=?";
    private static final String[] queryColumns = {DatabaseFileArchive.COLUMN_TILE, COLUMN_EXPIRES};
    private final GarbageCollector garbageCollector;
    protected long lastSizeCheck = 0;

    public static long getIndex(long j, long j2, long j3) {
        int i = (int) j3;
        return (((j3 << i) + j) << i) + j2;
    }

    public static String getPrimaryKey() {
        return primaryKey;
    }

    public void onDetach() {
    }

    public static void setCleanupOnStart(boolean z) {
        cleanOnStartup = z;
    }

    public SqlTileWriter() {
        GarbageCollector garbageCollector2 = new GarbageCollector(new Runnable() {
            public void run() {
                SqlTileWriter.this.runCleanupOperation();
            }
        });
        this.garbageCollector = garbageCollector2;
        getDb();
        if (!hasInited) {
            hasInited = true;
            if (cleanOnStartup) {
                garbageCollector2.mo29215gc();
            }
        }
    }

    public void runCleanupOperation() {
        SQLiteDatabase db = getDb();
        if (db != null && db.isOpen()) {
            createIndex(db);
            long length = db_file.length();
            if (length > Configuration.getInstance().getTileFileSystemCacheMaxBytes()) {
                runCleanupOperation(length - Configuration.getInstance().getTileFileSystemCacheTrimBytes(), Configuration.getInstance().getTileGCBulkSize(), Configuration.getInstance().getTileGCBulkPauseInMillis(), true);
            }
        } else if (Configuration.getInstance().isDebugMode()) {
            Log.d(IMapView.LOGTAG, "Finished init thread, aborted due to null database reference");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x00d7 A[Catch:{ all -> 0x00b4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00da A[Catch:{ all -> 0x00b4 }] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:21:0x00a7=Splitter:B:21:0x00a7, B:37:0x00ef=Splitter:B:37:0x00ef} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean saveFile(org.osmdroid.tileprovider.tilesource.ITileSource r17, long r18, java.io.InputStream r20, java.lang.Long r21) {
        /*
            r16 = this;
            r1 = r16
            r0 = r21
            android.database.sqlite.SQLiteDatabase r2 = r16.getDb()
            java.lang.String r3 = " "
            java.lang.String r4 = "Unable to store cached tile from "
            r5 = 0
            java.lang.String r6 = "OsmDroid"
            if (r2 == 0) goto L_0x0107
            boolean r7 = r2.isOpen()
            if (r7 != 0) goto L_0x0019
            goto L_0x0107
        L_0x0019:
            r7 = 0
            android.content.ContentValues r8 = new android.content.ContentValues     // Catch:{ SQLiteFullException -> 0x00f3, Exception -> 0x00b6 }
            r8.<init>()     // Catch:{ SQLiteFullException -> 0x00f3, Exception -> 0x00b6 }
            long r9 = getIndex(r18)     // Catch:{ SQLiteFullException -> 0x00f3, Exception -> 0x00b6 }
            java.lang.String r11 = "provider"
            java.lang.String r12 = r17.name()     // Catch:{ SQLiteFullException -> 0x00f3, Exception -> 0x00b6 }
            r8.put(r11, r12)     // Catch:{ SQLiteFullException -> 0x00f3, Exception -> 0x00b6 }
            r11 = 512(0x200, float:7.175E-43)
            byte[] r11 = new byte[r11]     // Catch:{ SQLiteFullException -> 0x00f3, Exception -> 0x00b6 }
            java.io.ByteArrayOutputStream r12 = new java.io.ByteArrayOutputStream     // Catch:{ SQLiteFullException -> 0x00f3, Exception -> 0x00b6 }
            r12.<init>()     // Catch:{ SQLiteFullException -> 0x00f3, Exception -> 0x00b6 }
            r13 = r20
        L_0x0037:
            int r14 = r13.read(r11)     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            r15 = -1
            if (r14 == r15) goto L_0x0042
            r12.write(r11, r5, r14)     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            goto L_0x0037
        L_0x0042:
            byte[] r11 = r12.toByteArray()     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            java.lang.String r13 = "key"
            java.lang.Long r9 = java.lang.Long.valueOf(r9)     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            r8.put(r13, r9)     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            java.lang.String r9 = "tile"
            r8.put(r9, r11)     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            if (r0 == 0) goto L_0x005b
            java.lang.String r9 = "expires"
            r8.put(r9, r0)     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
        L_0x005b:
            java.lang.String r0 = "tiles"
            r2.replaceOrThrow(r0, r7, r8)     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            org.osmdroid.config.IConfigurationProvider r0 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            boolean r0 = r0.isDebugMode()     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            if (r0 == 0) goto L_0x0089
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            r0.<init>()     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            java.lang.String r7 = "tile inserted "
            r0.append(r7)     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            java.lang.String r7 = r17.name()     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            r0.append(r7)     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            java.lang.String r7 = org.osmdroid.util.MapTileIndex.toString(r18)     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            r0.append(r7)     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            java.lang.String r0 = r0.toString()     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            android.util.Log.d(r6, r0)     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
        L_0x0089:
            long r7 = java.lang.System.currentTimeMillis()     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            long r9 = r1.lastSizeCheck     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            org.osmdroid.config.IConfigurationProvider r0 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            long r13 = r0.getTileGCFrequencyInMillis()     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            long r9 = r9 + r13
            int r0 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r0 <= 0) goto L_0x00a7
            long r7 = java.lang.System.currentTimeMillis()     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            r1.lastSizeCheck = r7     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            org.osmdroid.util.GarbageCollector r0 = r1.garbageCollector     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
            r0.mo29215gc()     // Catch:{ SQLiteFullException -> 0x00b1, Exception -> 0x00ae, all -> 0x00ab }
        L_0x00a7:
            r12.close()     // Catch:{ IOException -> 0x0102 }
            goto L_0x0102
        L_0x00ab:
            r0 = move-exception
            r7 = r12
            goto L_0x0103
        L_0x00ae:
            r0 = move-exception
            r7 = r12
            goto L_0x00b7
        L_0x00b1:
            r0 = move-exception
            r7 = r12
            goto L_0x00f4
        L_0x00b4:
            r0 = move-exception
            goto L_0x0103
        L_0x00b6:
            r0 = move-exception
        L_0x00b7:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x00b4 }
            r8.<init>()     // Catch:{ all -> 0x00b4 }
            r8.append(r4)     // Catch:{ all -> 0x00b4 }
            java.lang.String r4 = r17.name()     // Catch:{ all -> 0x00b4 }
            r8.append(r4)     // Catch:{ all -> 0x00b4 }
            r8.append(r3)     // Catch:{ all -> 0x00b4 }
            java.lang.String r3 = org.osmdroid.util.MapTileIndex.toString(r18)     // Catch:{ all -> 0x00b4 }
            r8.append(r3)     // Catch:{ all -> 0x00b4 }
            java.lang.String r3 = " db is "
            r8.append(r3)     // Catch:{ all -> 0x00b4 }
            if (r2 != 0) goto L_0x00da
            java.lang.String r2 = "null"
            goto L_0x00dc
        L_0x00da:
            java.lang.String r2 = "not null"
        L_0x00dc:
            r8.append(r2)     // Catch:{ all -> 0x00b4 }
            java.lang.String r2 = r8.toString()     // Catch:{ all -> 0x00b4 }
            android.util.Log.e(r6, r2, r0)     // Catch:{ all -> 0x00b4 }
            int r2 = org.osmdroid.tileprovider.util.Counters.fileCacheSaveErrors     // Catch:{ all -> 0x00b4 }
            int r2 = r2 + 1
            org.osmdroid.tileprovider.util.Counters.fileCacheSaveErrors = r2     // Catch:{ all -> 0x00b4 }
            r1.catchException(r0)     // Catch:{ all -> 0x00b4 }
        L_0x00ef:
            r7.close()     // Catch:{ IOException -> 0x0102 }
            goto L_0x0102
        L_0x00f3:
            r0 = move-exception
        L_0x00f4:
            java.lang.String r2 = "SQLiteFullException while saving tile."
            android.util.Log.e(r6, r2, r0)     // Catch:{ all -> 0x00b4 }
            org.osmdroid.util.GarbageCollector r2 = r1.garbageCollector     // Catch:{ all -> 0x00b4 }
            r2.mo29215gc()     // Catch:{ all -> 0x00b4 }
            r1.catchException(r0)     // Catch:{ all -> 0x00b4 }
            goto L_0x00ef
        L_0x0102:
            return r5
        L_0x0103:
            r7.close()     // Catch:{ IOException -> 0x0106 }
        L_0x0106:
            throw r0
        L_0x0107:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r4)
            java.lang.String r2 = r17.name()
            r0.append(r2)
            r0.append(r3)
            java.lang.String r2 = org.osmdroid.util.MapTileIndex.toString(r18)
            r0.append(r2)
            java.lang.String r2 = ", database not available."
            r0.append(r2)
            java.lang.String r0 = r0.toString()
            android.util.Log.d(r6, r0)
            int r0 = org.osmdroid.tileprovider.util.Counters.fileCacheSaveErrors
            int r0 = r0 + 1
            org.osmdroid.tileprovider.util.Counters.fileCacheSaveErrors = r0
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.modules.SqlTileWriter.saveFile(org.osmdroid.tileprovider.tilesource.ITileSource, long, java.io.InputStream, java.lang.Long):boolean");
    }

    public boolean exists(String str, long j) {
        return 1 == getRowCount(primaryKey, getPrimaryKeyParameters(getIndex(j), str));
    }

    public boolean exists(ITileSource iTileSource, long j) {
        return exists(iTileSource.name(), j);
    }

    public boolean purgeCache() {
        SQLiteDatabase db = getDb();
        if (db == null || !db.isOpen()) {
            return false;
        }
        try {
            db.delete("tiles", (String) null, (String[]) null);
            return true;
        } catch (Exception e) {
            Log.w(IMapView.LOGTAG, "Error purging the db", e);
            catchException(e);
            return false;
        }
    }

    public boolean purgeCache(String str) {
        SQLiteDatabase db = getDb();
        if (db != null && db.isOpen()) {
            try {
                db.delete("tiles", "provider = ?", new String[]{str});
                return true;
            } catch (Exception e) {
                Log.w(IMapView.LOGTAG, "Error purging the db", e);
                catchException(e);
            }
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:113:0x0238  */
    /* JADX WARNING: Removed duplicated region for block: B:114:0x023b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int[] importFromFileCache(boolean r28) {
        /*
            r27 = this;
            r1 = r27
            android.database.sqlite.SQLiteDatabase r2 = r27.getDb()
            r0 = 4
            int[] r3 = new int[r0]
            r3 = {0, 0, 0, 0} // fill-array
            org.osmdroid.config.IConfigurationProvider r0 = org.osmdroid.config.Configuration.getInstance()
            java.io.File r0 = r0.getOsmdroidTileCache()
            boolean r4 = r0.exists()
            if (r4 == 0) goto L_0x0326
            java.io.File[] r4 = r0.listFiles()
            if (r4 == 0) goto L_0x0326
            r6 = 0
        L_0x0021:
            int r0 = r4.length
            if (r6 >= r0) goto L_0x0326
            r0 = r4[r6]
            boolean r0 = r0.isDirectory()
            if (r0 == 0) goto L_0x031e
            r0 = r4[r6]
            boolean r0 = r0.isHidden()
            if (r0 != 0) goto L_0x031e
            r0 = r4[r6]
            java.io.File[] r7 = r0.listFiles()
            java.lang.String r8 = "Unable to delete directory from "
            java.lang.String r10 = "OsmDroid"
            if (r7 == 0) goto L_0x02f1
            r12 = 0
        L_0x0041:
            int r0 = r7.length
            if (r12 >= r0) goto L_0x02f1
            r0 = r7[r12]
            boolean r0 = r0.isDirectory()
            if (r0 == 0) goto L_0x02b7
            r0 = r7[r12]
            boolean r0 = r0.isHidden()
            if (r0 != 0) goto L_0x02b7
            r0 = r7[r12]
            java.io.File[] r13 = r0.listFiles()
            if (r13 == 0) goto L_0x02b7
            r14 = 0
        L_0x005d:
            int r0 = r13.length
            if (r14 >= r0) goto L_0x02b7
            r0 = r13[r14]
            boolean r0 = r0.isDirectory()
            if (r0 == 0) goto L_0x026f
            r0 = r13[r14]
            boolean r0 = r0.isHidden()
            if (r0 != 0) goto L_0x026f
            r0 = r13[r14]
            java.io.File[] r15 = r0.listFiles()
            if (r13 == 0) goto L_0x026f
            r9 = 0
        L_0x0079:
            int r0 = r15.length
            if (r9 >= r0) goto L_0x026f
            r0 = r15[r9]
            boolean r0 = r0.isHidden()
            if (r0 != 0) goto L_0x0251
            r0 = r15[r9]
            boolean r0 = r0.isDirectory()
            if (r0 != 0) goto L_0x0251
            android.content.ContentValues r0 = new android.content.ContentValues     // Catch:{ Exception -> 0x0210 }
            r0.<init>()     // Catch:{ Exception -> 0x0210 }
            r16 = r13[r14]     // Catch:{ Exception -> 0x0210 }
            java.lang.String r16 = r16.getName()     // Catch:{ Exception -> 0x0210 }
            r23 = r12
            long r11 = java.lang.Long.parseLong(r16)     // Catch:{ Exception -> 0x0207 }
            r16 = r15[r9]     // Catch:{ Exception -> 0x0207 }
            java.lang.String r5 = r16.getName()     // Catch:{ Exception -> 0x0207 }
            r16 = r15[r9]     // Catch:{ Exception -> 0x0207 }
            r24 = r8
            java.lang.String r8 = r16.getName()     // Catch:{ Exception -> 0x0201 }
            r16 = r13
            java.lang.String r13 = "."
            int r8 = r8.indexOf(r13)     // Catch:{ Exception -> 0x01fa }
            r13 = 0
            java.lang.String r5 = r5.substring(r13, r8)     // Catch:{ Exception -> 0x01fa }
            r8 = r14
            long r13 = java.lang.Long.parseLong(r5)     // Catch:{ Exception -> 0x01f1 }
            r5 = r7[r23]     // Catch:{ Exception -> 0x01f1 }
            java.lang.String r5 = r5.getName()     // Catch:{ Exception -> 0x01f1 }
            r25 = r7
            r26 = r8
            long r7 = java.lang.Long.parseLong(r5)     // Catch:{ Exception -> 0x01ed }
            r17 = r11
            r19 = r13
            r21 = r7
            long r17 = getIndex(r17, r19, r21)     // Catch:{ Exception -> 0x01ed }
            java.lang.String r5 = "provider"
            r19 = r4[r6]     // Catch:{ Exception -> 0x01ed }
            r20 = r3
            java.lang.String r3 = r19.getName()     // Catch:{ Exception -> 0x01eb }
            r0.put(r5, r3)     // Catch:{ Exception -> 0x01eb }
            r3 = r4[r6]     // Catch:{ Exception -> 0x01eb }
            java.lang.String r3 = r3.getName()     // Catch:{ Exception -> 0x01eb }
            int r5 = (int) r7
            r19 = r10
            int r10 = (int) r11
            r21 = r11
            int r11 = (int) r13
            long r10 = org.osmdroid.util.MapTileIndex.getTileIndex(r5, r10, r11)     // Catch:{ Exception -> 0x01e7 }
            boolean r3 = r1.exists((java.lang.String) r3, (long) r10)     // Catch:{ Exception -> 0x01e7 }
            if (r3 != 0) goto L_0x01e3
            java.io.BufferedInputStream r3 = new java.io.BufferedInputStream     // Catch:{ Exception -> 0x01e7 }
            java.io.FileInputStream r5 = new java.io.FileInputStream     // Catch:{ Exception -> 0x01e7 }
            r10 = r15[r9]     // Catch:{ Exception -> 0x01e7 }
            r5.<init>(r10)     // Catch:{ Exception -> 0x01e7 }
            r3.<init>(r5)     // Catch:{ Exception -> 0x01e7 }
            java.util.ArrayList r5 = new java.util.ArrayList     // Catch:{ Exception -> 0x01e7 }
            r5.<init>()     // Catch:{ Exception -> 0x01e7 }
        L_0x010a:
            int r10 = r3.read()     // Catch:{ Exception -> 0x01e7 }
            r11 = -1
            if (r10 == r11) goto L_0x011a
            byte r10 = (byte) r10     // Catch:{ Exception -> 0x01e7 }
            java.lang.Byte r10 = java.lang.Byte.valueOf(r10)     // Catch:{ Exception -> 0x01e7 }
            r5.add(r10)     // Catch:{ Exception -> 0x01e7 }
            goto L_0x010a
        L_0x011a:
            int r3 = r5.size()     // Catch:{ Exception -> 0x01e7 }
            byte[] r3 = new byte[r3]     // Catch:{ Exception -> 0x01e7 }
            r10 = 0
        L_0x0121:
            int r11 = r5.size()     // Catch:{ Exception -> 0x01e7 }
            if (r10 >= r11) goto L_0x0136
            java.lang.Object r11 = r5.get(r10)     // Catch:{ Exception -> 0x01e7 }
            java.lang.Byte r11 = (java.lang.Byte) r11     // Catch:{ Exception -> 0x01e7 }
            byte r11 = r11.byteValue()     // Catch:{ Exception -> 0x01e7 }
            r3[r10] = r11     // Catch:{ Exception -> 0x01e7 }
            int r10 = r10 + 1
            goto L_0x0121
        L_0x0136:
            java.lang.String r5 = "key"
            java.lang.Long r10 = java.lang.Long.valueOf(r17)     // Catch:{ Exception -> 0x01e7 }
            r0.put(r5, r10)     // Catch:{ Exception -> 0x01e7 }
            java.lang.String r5 = "tile"
            r0.put(r5, r3)     // Catch:{ Exception -> 0x01e7 }
            java.lang.String r3 = "tiles"
            r5 = 0
            long r10 = r2.insert(r3, r5, r0)     // Catch:{ Exception -> 0x01e7 }
            r17 = 0
            int r0 = (r10 > r17 ? 1 : (r10 == r17 ? 0 : -1))
            java.lang.String r3 = "/"
            if (r0 <= 0) goto L_0x01ae
            org.osmdroid.config.IConfigurationProvider r0 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ Exception -> 0x01e7 }
            boolean r0 = r0.isDebugMode()     // Catch:{ Exception -> 0x01e7 }
            if (r0 == 0) goto L_0x018e
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01e7 }
            r0.<init>()     // Catch:{ Exception -> 0x01e7 }
            java.lang.String r5 = "tile inserted "
            r0.append(r5)     // Catch:{ Exception -> 0x01e7 }
            r5 = r4[r6]     // Catch:{ Exception -> 0x01e7 }
            java.lang.String r5 = r5.getName()     // Catch:{ Exception -> 0x01e7 }
            r0.append(r5)     // Catch:{ Exception -> 0x01e7 }
            r0.append(r3)     // Catch:{ Exception -> 0x01e7 }
            r0.append(r7)     // Catch:{ Exception -> 0x01e7 }
            r0.append(r3)     // Catch:{ Exception -> 0x01e7 }
            r10 = r21
            r0.append(r10)     // Catch:{ Exception -> 0x01e7 }
            r0.append(r3)     // Catch:{ Exception -> 0x01e7 }
            r0.append(r13)     // Catch:{ Exception -> 0x01e7 }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x01e7 }
            r5 = r19
            android.util.Log.d(r5, r0)     // Catch:{ Exception -> 0x01e1 }
            goto L_0x0190
        L_0x018e:
            r5 = r19
        L_0x0190:
            r12 = 0
            r0 = r20[r12]     // Catch:{ Exception -> 0x01e1 }
            r3 = 1
            int r0 = r0 + r3
            r20[r12] = r0     // Catch:{ Exception -> 0x01e1 }
            if (r28 == 0) goto L_0x025e
            r0 = r15[r9]     // Catch:{ Exception -> 0x01a6 }
            r0.delete()     // Catch:{ Exception -> 0x01a6 }
            r0 = 2
            r7 = r20[r0]     // Catch:{ Exception -> 0x01a6 }
            int r7 = r7 + r3
            r20[r0] = r7     // Catch:{ Exception -> 0x01a6 }
            goto L_0x025e
        L_0x01a6:
            r7 = 3
            r0 = r20[r7]     // Catch:{ Exception -> 0x01e1 }
            int r0 = r0 + r3
            r20[r7] = r0     // Catch:{ Exception -> 0x01e1 }
            goto L_0x025e
        L_0x01ae:
            r5 = r19
            r10 = r21
            r12 = 0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01e1 }
            r0.<init>()     // Catch:{ Exception -> 0x01e1 }
            java.lang.String r12 = "tile NOT inserted "
            r0.append(r12)     // Catch:{ Exception -> 0x01e1 }
            r12 = r4[r6]     // Catch:{ Exception -> 0x01e1 }
            java.lang.String r12 = r12.getName()     // Catch:{ Exception -> 0x01e1 }
            r0.append(r12)     // Catch:{ Exception -> 0x01e1 }
            r0.append(r3)     // Catch:{ Exception -> 0x01e1 }
            r0.append(r7)     // Catch:{ Exception -> 0x01e1 }
            r0.append(r3)     // Catch:{ Exception -> 0x01e1 }
            r0.append(r10)     // Catch:{ Exception -> 0x01e1 }
            r0.append(r3)     // Catch:{ Exception -> 0x01e1 }
            r0.append(r13)     // Catch:{ Exception -> 0x01e1 }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x01e1 }
            android.util.Log.w(r5, r0)     // Catch:{ Exception -> 0x01e1 }
            goto L_0x025e
        L_0x01e1:
            r0 = move-exception
            goto L_0x021e
        L_0x01e3:
            r5 = r19
            goto L_0x025e
        L_0x01e7:
            r0 = move-exception
            r5 = r19
            goto L_0x021e
        L_0x01eb:
            r0 = move-exception
            goto L_0x01f8
        L_0x01ed:
            r0 = move-exception
            r20 = r3
            goto L_0x01f8
        L_0x01f1:
            r0 = move-exception
            r20 = r3
            r25 = r7
            r26 = r8
        L_0x01f8:
            r5 = r10
            goto L_0x021e
        L_0x01fa:
            r0 = move-exception
            r20 = r3
            r25 = r7
            r5 = r10
            goto L_0x021c
        L_0x0201:
            r0 = move-exception
            r20 = r3
            r25 = r7
            goto L_0x020e
        L_0x0207:
            r0 = move-exception
            r20 = r3
            r25 = r7
            r24 = r8
        L_0x020e:
            r5 = r10
            goto L_0x021a
        L_0x0210:
            r0 = move-exception
            r20 = r3
            r25 = r7
            r24 = r8
            r5 = r10
            r23 = r12
        L_0x021a:
            r16 = r13
        L_0x021c:
            r26 = r14
        L_0x021e:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r7 = "Unable to store cached tile from "
            r3.append(r7)
            r7 = r4[r6]
            java.lang.String r7 = r7.getName()
            r3.append(r7)
            java.lang.String r7 = " db is "
            r3.append(r7)
            if (r2 != 0) goto L_0x023b
            java.lang.String r7 = "null"
            goto L_0x023d
        L_0x023b:
            java.lang.String r7 = "not null"
        L_0x023d:
            r3.append(r7)
            java.lang.String r3 = r3.toString()
            android.util.Log.e(r5, r3, r0)
            r3 = 1
            r7 = r20[r3]
            int r7 = r7 + r3
            r20[r3] = r7
            r1.catchException(r0)
            goto L_0x025e
        L_0x0251:
            r20 = r3
            r25 = r7
            r24 = r8
            r5 = r10
            r23 = r12
            r16 = r13
            r26 = r14
        L_0x025e:
            int r9 = r9 + 1
            r10 = r5
            r13 = r16
            r3 = r20
            r12 = r23
            r8 = r24
            r7 = r25
            r14 = r26
            goto L_0x0079
        L_0x026f:
            r20 = r3
            r25 = r7
            r24 = r8
            r5 = r10
            r23 = r12
            r16 = r13
            r26 = r14
            if (r28 == 0) goto L_0x02a7
            r0 = r16[r26]     // Catch:{ Exception -> 0x0284 }
            r0.delete()     // Catch:{ Exception -> 0x0284 }
            goto L_0x02a7
        L_0x0284:
            r0 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r7 = r24
            r3.append(r7)
            r8 = r16[r26]
            java.lang.String r8 = r8.getAbsolutePath()
            r3.append(r8)
            java.lang.String r3 = r3.toString()
            android.util.Log.e(r5, r3, r0)
            r3 = 3
            r0 = r20[r3]
            r8 = 1
            int r0 = r0 + r8
            r20[r3] = r0
            goto L_0x02a9
        L_0x02a7:
            r7 = r24
        L_0x02a9:
            int r14 = r26 + 1
            r10 = r5
            r8 = r7
            r13 = r16
            r3 = r20
            r12 = r23
            r7 = r25
            goto L_0x005d
        L_0x02b7:
            r20 = r3
            r25 = r7
            r7 = r8
            r5 = r10
            r23 = r12
            if (r28 == 0) goto L_0x02e7
            r0 = r25[r23]     // Catch:{ Exception -> 0x02c7 }
            r0.delete()     // Catch:{ Exception -> 0x02c7 }
            goto L_0x02e7
        L_0x02c7:
            r0 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r7)
            r8 = r25[r23]
            java.lang.String r8 = r8.getAbsolutePath()
            r3.append(r8)
            java.lang.String r3 = r3.toString()
            android.util.Log.e(r5, r3, r0)
            r3 = 3
            r0 = r20[r3]
            r8 = 1
            int r0 = r0 + r8
            r20[r3] = r0
        L_0x02e7:
            int r12 = r23 + 1
            r10 = r5
            r8 = r7
            r3 = r20
            r7 = r25
            goto L_0x0041
        L_0x02f1:
            r20 = r3
            r7 = r8
            r5 = r10
            if (r28 == 0) goto L_0x0320
            r0 = r4[r6]     // Catch:{ Exception -> 0x02fd }
            r0.delete()     // Catch:{ Exception -> 0x02fd }
            goto L_0x0320
        L_0x02fd:
            r0 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r7)
            r7 = r4[r6]
            java.lang.String r7 = r7.getAbsolutePath()
            r3.append(r7)
            java.lang.String r3 = r3.toString()
            android.util.Log.e(r5, r3, r0)
            r3 = 3
            r0 = r20[r3]
            r5 = 1
            int r0 = r0 + r5
            r20[r3] = r0
            goto L_0x0320
        L_0x031e:
            r20 = r3
        L_0x0320:
            int r6 = r6 + 1
            r3 = r20
            goto L_0x0021
        L_0x0326:
            r20 = r3
            return r20
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.modules.SqlTileWriter.importFromFileCache(boolean):int[]");
    }

    public boolean remove(ITileSource iTileSource, long j) {
        SQLiteDatabase db = getDb();
        if (db == null || !db.isOpen()) {
            Log.d(IMapView.LOGTAG, "Unable to delete cached tile from " + iTileSource.name() + " " + MapTileIndex.toString(j) + ", database not available.");
            Counters.fileCacheSaveErrors = Counters.fileCacheSaveErrors + 1;
            return false;
        }
        try {
            db.delete("tiles", primaryKey, getPrimaryKeyParameters(getIndex(j), iTileSource));
            return true;
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to delete cached tile from ");
            sb.append(iTileSource.name());
            sb.append(" ");
            sb.append(MapTileIndex.toString(j));
            sb.append(" db is ");
            sb.append(db == null ? "null" : "not null");
            Log.e(IMapView.LOGTAG, sb.toString(), e);
            Counters.fileCacheSaveErrors++;
            catchException(e);
            return false;
        }
    }

    public long getRowCount(String str) {
        if (str == null) {
            return getRowCount((String) null, (String[]) null);
        }
        return getRowCount("provider=?", new String[]{str});
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x004c, code lost:
        if (r2 != null) goto L_0x0058;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0056, code lost:
        if (r2 == null) goto L_0x005b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0058, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x005b, code lost:
        return -1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long getRowCount(java.lang.String r8, java.lang.String[] r9) {
        /*
            r7 = this;
            r0 = -1
            r2 = 0
            android.database.sqlite.SQLiteDatabase r3 = r7.getDb()     // Catch:{ Exception -> 0x0052 }
            if (r3 == 0) goto L_0x004f
            boolean r4 = r3.isOpen()     // Catch:{ Exception -> 0x0052 }
            if (r4 != 0) goto L_0x0010
            goto L_0x004f
        L_0x0010:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0052 }
            r4.<init>()     // Catch:{ Exception -> 0x0052 }
            java.lang.String r5 = "select count(*) from tiles"
            r4.append(r5)     // Catch:{ Exception -> 0x0052 }
            if (r8 != 0) goto L_0x001f
            java.lang.String r8 = ""
            goto L_0x0030
        L_0x001f:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0052 }
            r5.<init>()     // Catch:{ Exception -> 0x0052 }
            java.lang.String r6 = " where "
            r5.append(r6)     // Catch:{ Exception -> 0x0052 }
            r5.append(r8)     // Catch:{ Exception -> 0x0052 }
            java.lang.String r8 = r5.toString()     // Catch:{ Exception -> 0x0052 }
        L_0x0030:
            r4.append(r8)     // Catch:{ Exception -> 0x0052 }
            java.lang.String r8 = r4.toString()     // Catch:{ Exception -> 0x0052 }
            android.database.Cursor r2 = r3.rawQuery(r8, r9)     // Catch:{ Exception -> 0x0052 }
            boolean r8 = r2.moveToFirst()     // Catch:{ Exception -> 0x0052 }
            if (r8 == 0) goto L_0x004c
            r8 = 0
            long r8 = r2.getLong(r8)     // Catch:{ Exception -> 0x0052 }
            if (r2 == 0) goto L_0x004b
            r2.close()
        L_0x004b:
            return r8
        L_0x004c:
            if (r2 == 0) goto L_0x005b
            goto L_0x0058
        L_0x004f:
            return r0
        L_0x0050:
            r8 = move-exception
            goto L_0x005c
        L_0x0052:
            r8 = move-exception
            r7.catchException(r8)     // Catch:{ all -> 0x0050 }
            if (r2 == 0) goto L_0x005b
        L_0x0058:
            r2.close()
        L_0x005b:
            return r0
        L_0x005c:
            if (r2 == 0) goto L_0x0061
            r2.close()
        L_0x0061:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.modules.SqlTileWriter.getRowCount(java.lang.String, java.lang.String[]):long");
    }

    public long getRowCount(String str, int i, Collection<Rect> collection, Collection<Rect> collection2) {
        StringBuilder sb = new StringBuilder();
        sb.append(getWhereClause(i, collection, collection2));
        sb.append(str != null ? " and provider=?" : "");
        return getRowCount(sb.toString(), str != null ? new String[]{str} : null);
    }

    public long getSize() {
        return db_file.length();
    }

    public long getFirstExpiry() {
        SQLiteDatabase db = getDb();
        if (db != null && db.isOpen()) {
            try {
                Cursor rawQuery = db.rawQuery("select min(expires) from tiles", (String[]) null);
                rawQuery.moveToFirst();
                long j = rawQuery.getLong(0);
                rawQuery.close();
                return j;
            } catch (Exception e) {
                Log.e(IMapView.LOGTAG, "Unable to query for oldest tile", e);
                catchException(e);
            }
        }
        return 0;
    }

    protected static String extractXFromKeyInSQL(int i) {
        return "((key>>" + i + ")%" + (1 << i) + ")";
    }

    protected static String extractYFromKeyInSQL(int i) {
        return "(key%" + (1 << i) + ")";
    }

    public static long getIndex(long j) {
        return getIndex((long) MapTileIndex.getX(j), (long) MapTileIndex.getY(j), (long) MapTileIndex.getZoom(j));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0024, code lost:
        if (r3 != null) goto L_0x0039;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0037, code lost:
        if (r3 != null) goto L_0x0039;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0039, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003c, code lost:
        return null;
     */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0041  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Long getExpirationTimestamp(org.osmdroid.tileprovider.tilesource.ITileSource r3, long r4) {
        /*
            r2 = this;
            r0 = 0
            long r4 = getIndex(r4)     // Catch:{ Exception -> 0x002b, all -> 0x0029 }
            java.lang.String[] r3 = getPrimaryKeyParameters((long) r4, (org.osmdroid.tileprovider.tilesource.ITileSource) r3)     // Catch:{ Exception -> 0x002b, all -> 0x0029 }
            java.lang.String[] r4 = expireQueryColumn     // Catch:{ Exception -> 0x002b, all -> 0x0029 }
            android.database.Cursor r3 = r2.getTileCursor(r3, r4)     // Catch:{ Exception -> 0x002b, all -> 0x0029 }
            boolean r4 = r3.moveToNext()     // Catch:{ Exception -> 0x0027 }
            if (r4 == 0) goto L_0x0024
            r4 = 0
            long r4 = r3.getLong(r4)     // Catch:{ Exception -> 0x0027 }
            java.lang.Long r4 = java.lang.Long.valueOf(r4)     // Catch:{ Exception -> 0x0027 }
            if (r3 == 0) goto L_0x0023
            r3.close()
        L_0x0023:
            return r4
        L_0x0024:
            if (r3 == 0) goto L_0x003c
            goto L_0x0039
        L_0x0027:
            r4 = move-exception
            goto L_0x002d
        L_0x0029:
            r4 = move-exception
            goto L_0x003f
        L_0x002b:
            r4 = move-exception
            r3 = r0
        L_0x002d:
            java.lang.String r5 = "OsmDroid"
            java.lang.String r1 = "error getting expiration date from the tile cache"
            android.util.Log.e(r5, r1, r4)     // Catch:{ all -> 0x003d }
            r2.catchException(r4)     // Catch:{ all -> 0x003d }
            if (r3 == 0) goto L_0x003c
        L_0x0039:
            r3.close()
        L_0x003c:
            return r0
        L_0x003d:
            r4 = move-exception
            r0 = r3
        L_0x003f:
            if (r0 == 0) goto L_0x0044
            r0.close()
        L_0x0044:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.modules.SqlTileWriter.getExpirationTimestamp(org.osmdroid.tileprovider.tilesource.ITileSource, long):java.lang.Long");
    }

    public static String[] getPrimaryKeyParameters(long j, ITileSource iTileSource) {
        return getPrimaryKeyParameters(j, iTileSource.name());
    }

    public static String[] getPrimaryKeyParameters(long j, String str) {
        return new String[]{String.valueOf(j), str};
    }

    public Cursor getTileCursor(String[] strArr, String[] strArr2) {
        return getDb().query("tiles", strArr2, primaryKey, strArr, (String) null, (String) null, (String) null);
    }

    /* JADX WARNING: type inference failed for: r1v3, types: [java.io.ByteArrayInputStream, java.io.Closeable, java.io.InputStream] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00a9  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00bc  */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.drawable.Drawable loadTile(org.osmdroid.tileprovider.tilesource.ITileSource r11, long r12) throws java.lang.Exception {
        /*
            r10 = this;
            r0 = 0
            long r1 = getIndex(r12)     // Catch:{ Exception -> 0x00b5 }
            java.lang.String[] r1 = getPrimaryKeyParameters((long) r1, (org.osmdroid.tileprovider.tilesource.ITileSource) r11)     // Catch:{ Exception -> 0x00b5 }
            java.lang.String[] r2 = queryColumns     // Catch:{ Exception -> 0x00b5 }
            android.database.Cursor r1 = r10.getTileCursor(r1, r2)     // Catch:{ Exception -> 0x00b5 }
            boolean r2 = r1.moveToFirst()     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            r3 = 1
            r4 = 0
            if (r2 == 0) goto L_0x0020
            byte[] r2 = r1.getBlob(r4)     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            long r5 = r1.getLong(r3)     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            goto L_0x0023
        L_0x0020:
            r5 = 0
            r2 = r0
        L_0x0023:
            java.lang.String r7 = "OsmDroid"
            if (r2 != 0) goto L_0x0056
            org.osmdroid.config.IConfigurationProvider r2 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            boolean r2 = r2.isDebugMode()     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            if (r2 == 0) goto L_0x0050
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            r2.<init>()     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            java.lang.String r3 = "SqlCache - Tile doesn't exist: "
            r2.append(r3)     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            java.lang.String r11 = r11.name()     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            r2.append(r11)     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            java.lang.String r11 = org.osmdroid.util.MapTileIndex.toString(r12)     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            r2.append(r11)     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            java.lang.String r11 = r2.toString()     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
            android.util.Log.d(r7, r11)     // Catch:{ Exception -> 0x00b0, all -> 0x00ad }
        L_0x0050:
            if (r1 == 0) goto L_0x0055
            r1.close()
        L_0x0055:
            return r0
        L_0x0056:
            if (r1 == 0) goto L_0x005b
            r1.close()
        L_0x005b:
            java.io.ByteArrayInputStream r1 = new java.io.ByteArrayInputStream     // Catch:{ all -> 0x00a6 }
            r1.<init>(r2)     // Catch:{ all -> 0x00a6 }
            android.graphics.drawable.Drawable r0 = r11.getDrawable((java.io.InputStream) r1)     // Catch:{ all -> 0x00a3 }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x00a3 }
            int r2 = (r5 > r8 ? 1 : (r5 == r8 ? 0 : -1))
            if (r2 >= 0) goto L_0x006d
            goto L_0x006e
        L_0x006d:
            r3 = r4
        L_0x006e:
            if (r3 == 0) goto L_0x009f
            if (r0 == 0) goto L_0x009f
            org.osmdroid.config.IConfigurationProvider r2 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ all -> 0x00a3 }
            boolean r2 = r2.isDebugMode()     // Catch:{ all -> 0x00a3 }
            if (r2 == 0) goto L_0x009b
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x00a3 }
            r2.<init>()     // Catch:{ all -> 0x00a3 }
            java.lang.String r3 = "Tile expired: "
            r2.append(r3)     // Catch:{ all -> 0x00a3 }
            java.lang.String r11 = r11.name()     // Catch:{ all -> 0x00a3 }
            r2.append(r11)     // Catch:{ all -> 0x00a3 }
            java.lang.String r11 = org.osmdroid.util.MapTileIndex.toString(r12)     // Catch:{ all -> 0x00a3 }
            r2.append(r11)     // Catch:{ all -> 0x00a3 }
            java.lang.String r11 = r2.toString()     // Catch:{ all -> 0x00a3 }
            android.util.Log.d(r7, r11)     // Catch:{ all -> 0x00a3 }
        L_0x009b:
            r11 = -2
            org.osmdroid.tileprovider.ExpirableBitmapDrawable.setState(r0, r11)     // Catch:{ all -> 0x00a3 }
        L_0x009f:
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r1)
            return r0
        L_0x00a3:
            r11 = move-exception
            r0 = r1
            goto L_0x00a7
        L_0x00a6:
            r11 = move-exception
        L_0x00a7:
            if (r0 == 0) goto L_0x00ac
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r0)
        L_0x00ac:
            throw r11
        L_0x00ad:
            r11 = move-exception
            r0 = r1
            goto L_0x00ba
        L_0x00b0:
            r11 = move-exception
            r0 = r1
            goto L_0x00b6
        L_0x00b3:
            r11 = move-exception
            goto L_0x00ba
        L_0x00b5:
            r11 = move-exception
        L_0x00b6:
            r10.catchException(r11)     // Catch:{ all -> 0x00b3 }
            throw r11     // Catch:{ all -> 0x00b3 }
        L_0x00ba:
            if (r0 == 0) goto L_0x00bf
            r0.close()
        L_0x00bf:
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.modules.SqlTileWriter.loadTile(org.osmdroid.tileprovider.tilesource.ITileSource, long):android.graphics.drawable.Drawable");
    }

    public void runCleanupOperation(long j, int i, long j2, boolean z) {
        boolean z2;
        String str;
        String str2;
        StringBuilder sb = new StringBuilder();
        SQLiteDatabase db = getDb();
        long j3 = j;
        boolean z3 = true;
        while (j3 > 0) {
            if (z3) {
                z2 = false;
            } else {
                if (j2 > 0) {
                    try {
                        Thread.sleep(j2);
                    } catch (InterruptedException unused) {
                    }
                }
                z2 = z3;
            }
            long currentTimeMillis = System.currentTimeMillis();
            try {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("SELECT key,LENGTH(HEX(tile))/2 FROM tiles WHERE expires IS NOT NULL ");
                String str3 = "";
                if (z) {
                    str = str3;
                } else {
                    str = "AND expires < " + currentTimeMillis + " ";
                }
                sb2.append(str);
                sb2.append("ORDER BY ");
                sb2.append(COLUMN_EXPIRES);
                sb2.append(" ASC LIMIT ");
                sb2.append(i);
                Cursor rawQuery = db.rawQuery(sb2.toString(), (String[]) null);
                rawQuery.moveToFirst();
                sb.setLength(0);
                sb.append("key in (");
                String str4 = str3;
                while (true) {
                    str2 = str3;
                    if (rawQuery.isAfterLast()) {
                        break;
                    }
                    long j4 = rawQuery.getLong(0);
                    long j5 = rawQuery.getLong(1);
                    rawQuery.moveToNext();
                    sb.append(str4);
                    sb.append(j4);
                    str4 = ",";
                    j3 -= j5;
                    if (j3 <= 0) {
                        break;
                    }
                    str3 = str2;
                }
                rawQuery.close();
                if (!str2.equals(str4)) {
                    sb.append(')');
                    try {
                        db.delete("tiles", sb.toString(), (String[]) null);
                    } catch (SQLiteFullException e) {
                        Log.e(IMapView.LOGTAG, "SQLiteFullException while cleanup.", e);
                        catchException(e);
                    } catch (Exception e2) {
                        catchException(e2);
                        return;
                    }
                    z3 = z2;
                } else {
                    return;
                }
            } catch (Exception e3) {
                catchException(e3);
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public SQLiteDatabase getDb() {
        SQLiteDatabase sQLiteDatabase = mDb;
        if (sQLiteDatabase != null) {
            return sQLiteDatabase;
        }
        synchronized (mLock) {
            Configuration.getInstance().getOsmdroidTileCache().mkdirs();
            File file = new File(Configuration.getInstance().getOsmdroidTileCache().getAbsolutePath() + File.separator + DATABASE_FILENAME);
            db_file = file;
            if (mDb == null) {
                try {
                    SQLiteDatabase openOrCreateDatabase = SQLiteDatabase.openOrCreateDatabase(file, (SQLiteDatabase.CursorFactory) null);
                    mDb = openOrCreateDatabase;
                    openOrCreateDatabase.execSQL("CREATE TABLE IF NOT EXISTS tiles (key INTEGER , provider TEXT, tile BLOB, expires INTEGER, PRIMARY KEY (key, provider));");
                } catch (Exception e) {
                    Log.e(IMapView.LOGTAG, "Unable to start the sqlite tile writer. Check external storage availability.", e);
                    catchException(e);
                    return null;
                }
            }
        }
        return mDb;
    }

    public void refreshDb() {
        synchronized (mLock) {
            SQLiteDatabase sQLiteDatabase = mDb;
            if (sQLiteDatabase != null) {
                sQLiteDatabase.close();
                mDb = null;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void catchException(Exception exc) {
        if ((exc instanceof SQLiteException) && !isFunctionalException((SQLiteException) exc)) {
            refreshDb();
        }
    }

    public static boolean isFunctionalException(SQLiteException sQLiteException) {
        String simpleName = sQLiteException.getClass().getSimpleName();
        simpleName.hashCode();
        char c = 65535;
        switch (simpleName.hashCode()) {
            case -1764604492:
                if (simpleName.equals("SQLiteFullException")) {
                    c = 0;
                    break;
                }
                break;
            case -1458338457:
                if (simpleName.equals("SQLiteBindOrColumnIndexOutOfRangeException")) {
                    c = 1;
                    break;
                }
                break;
            case -669227773:
                if (simpleName.equals("SQLiteTableLockedException")) {
                    c = 2;
                    break;
                }
                break;
            case 20404371:
                if (simpleName.equals("SQLiteMisuseException")) {
                    c = 3;
                    break;
                }
                break;
            case 666588538:
                if (simpleName.equals("SQLiteBlobTooBigException")) {
                    c = 4;
                    break;
                }
                break;
            case 1061155622:
                if (simpleName.equals("SQLiteConstraintException")) {
                    c = 5;
                    break;
                }
                break;
            case 1939376593:
                if (simpleName.equals("SQLiteDatatypeMismatchException")) {
                    c = 6;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return true;
            default:
                return false;
        }
    }

    private void createIndex(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE INDEX IF NOT EXISTS expires_index ON tiles (expires);");
    }

    public void runDuringSplashScreen() {
        createIndex(getDb());
    }

    /* access modifiers changed from: protected */
    public StringBuilder getWhereClause(int i, Rect rect) {
        String str;
        long j = (long) ((1 << (i + 1)) - 1);
        long j2 = (long) i;
        long index = getIndex(0, 0, j2);
        long index2 = getIndex(j, j, j2);
        String extractXFromKeyInSQL = extractXFromKeyInSQL(i);
        String extractYFromKeyInSQL = extractYFromKeyInSQL(i);
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append(DatabaseFileArchive.COLUMN_KEY);
        sb.append(" between ");
        sb.append(index);
        String str2 = " and ";
        sb.append(str2);
        sb.append(index2);
        if (rect != null) {
            sb.append(str2);
            if (rect.left == rect.right) {
                sb.append(extractXFromKeyInSQL);
                sb.append(SimpleComparison.EQUAL_TO_OPERATION);
                sb.append(rect.left);
            } else {
                sb.append("(");
                sb.append(extractXFromKeyInSQL);
                sb.append(SimpleComparison.GREATER_THAN_EQUAL_TO_OPERATION);
                sb.append(rect.left);
                if (rect.left < rect.right) {
                    str = str2;
                } else {
                    str = " or ";
                }
                sb.append(str);
                sb.append(extractXFromKeyInSQL);
                sb.append(SimpleComparison.LESS_THAN_EQUAL_TO_OPERATION);
                sb.append(rect.right);
                sb.append(")");
            }
            sb.append(str2);
            if (rect.top == rect.bottom) {
                sb.append(extractYFromKeyInSQL);
                sb.append(SimpleComparison.EQUAL_TO_OPERATION);
                sb.append(rect.top);
            } else {
                sb.append("(");
                sb.append(extractYFromKeyInSQL);
                sb.append(SimpleComparison.GREATER_THAN_EQUAL_TO_OPERATION);
                sb.append(rect.top);
                if (rect.top >= rect.bottom) {
                    str2 = " or ";
                }
                sb.append(str2);
                sb.append(extractYFromKeyInSQL);
                sb.append(SimpleComparison.LESS_THAN_EQUAL_TO_OPERATION);
                sb.append(rect.bottom);
                sb.append(")");
            }
        }
        sb.append(')');
        return sb;
    }

    /* access modifiers changed from: protected */
    public StringBuilder getWhereClause(int i, Collection<Rect> collection, Collection<Rect> collection2) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append(getWhereClause(i, (Rect) null));
        String str = "";
        if (collection != null && collection.size() > 0) {
            sb.append(" and (");
            String str2 = str;
            for (Rect whereClause : collection) {
                sb.append(str2);
                sb.append('(');
                sb.append(getWhereClause(i, whereClause));
                sb.append(')');
                str2 = " or ";
            }
            sb.append(")");
        }
        if (collection2 != null && collection2.size() > 0) {
            sb.append(" and not(");
            for (Rect whereClause2 : collection2) {
                sb.append(str);
                sb.append('(');
                sb.append(getWhereClause(i, whereClause2));
                sb.append(')');
                str = " or ";
            }
            sb.append(")");
        }
        sb.append(')');
        return sb;
    }

    public long delete(String str, int i, Collection<Rect> collection, Collection<Rect> collection2) {
        try {
            SQLiteDatabase db = getDb();
            if (db == null) {
                return -1;
            }
            if (!db.isOpen()) {
                return -1;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(getWhereClause(i, collection, collection2));
            sb.append(str != null ? " and provider=?" : "");
            return (long) db.delete("tiles", sb.toString(), str != null ? new String[]{str} : null);
        } catch (Exception e) {
            catchException(e);
            return 0;
        }
    }
}
