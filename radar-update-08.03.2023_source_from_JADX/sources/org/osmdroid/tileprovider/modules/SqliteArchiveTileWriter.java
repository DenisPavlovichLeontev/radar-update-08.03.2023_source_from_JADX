package org.osmdroid.tileprovider.modules;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.io.File;
import mil.nga.tiff.util.TiffConstants;
import org.osmdroid.api.IMapView;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;

public class SqliteArchiveTileWriter implements IFilesystemCache {
    static boolean hasInited = false;
    private static final String[] queryColumns = {DatabaseFileArchive.COLUMN_TILE};
    final File db_file;
    final SQLiteDatabase mDatabase;
    final int questimate = TiffConstants.DEFAULT_MAX_BYTES_PER_STRIP;

    public Long getExpirationTimestamp(ITileSource iTileSource, long j) {
        return null;
    }

    public boolean remove(ITileSource iTileSource, long j) {
        return false;
    }

    public SqliteArchiveTileWriter(String str) throws Exception {
        File file = new File(str);
        this.db_file = file;
        try {
            SQLiteDatabase openOrCreateDatabase = SQLiteDatabase.openOrCreateDatabase(file.getAbsolutePath(), (SQLiteDatabase.CursorFactory) null);
            this.mDatabase = openOrCreateDatabase;
            try {
                openOrCreateDatabase.execSQL("CREATE TABLE IF NOT EXISTS tiles (key INTEGER , provider TEXT, tile BLOB, PRIMARY KEY (key, provider));");
            } catch (Throwable th) {
                th.printStackTrace();
                Log.d(IMapView.LOGTAG, "error setting db schema, it probably exists already", th);
            }
        } catch (Exception e) {
            throw new Exception("Trouble creating database file at " + str, e);
        }
    }

    /* JADX WARNING: type inference failed for: r14v3, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r14v4, types: [java.io.ByteArrayOutputStream] */
    /* JADX WARNING: type inference failed for: r14v5 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean saveFile(org.osmdroid.tileprovider.tilesource.ITileSource r10, long r11, java.io.InputStream r13, java.lang.Long r14) {
        /*
            r9 = this;
            android.database.sqlite.SQLiteDatabase r14 = r9.mDatabase
            r0 = 0
            java.lang.String r1 = "OsmDroid"
            if (r14 == 0) goto L_0x00ae
            boolean r14 = r14.isOpen()
            if (r14 != 0) goto L_0x000f
            goto L_0x00ae
        L_0x000f:
            r14 = 0
            android.content.ContentValues r2 = new android.content.ContentValues     // Catch:{ all -> 0x0080 }
            r2.<init>()     // Catch:{ all -> 0x0080 }
            long r3 = org.osmdroid.tileprovider.modules.SqlTileWriter.getIndex(r11)     // Catch:{ all -> 0x0080 }
            java.lang.String r5 = "provider"
            java.lang.String r6 = r10.name()     // Catch:{ all -> 0x0080 }
            r2.put(r5, r6)     // Catch:{ all -> 0x0080 }
            r5 = 512(0x200, float:7.175E-43)
            byte[] r5 = new byte[r5]     // Catch:{ all -> 0x0080 }
            java.io.ByteArrayOutputStream r6 = new java.io.ByteArrayOutputStream     // Catch:{ all -> 0x0080 }
            r6.<init>()     // Catch:{ all -> 0x0080 }
        L_0x002b:
            int r7 = r13.read(r5)     // Catch:{ all -> 0x007d }
            r8 = -1
            if (r7 == r8) goto L_0x0036
            r6.write(r5, r0, r7)     // Catch:{ all -> 0x007d }
            goto L_0x002b
        L_0x0036:
            byte[] r13 = r6.toByteArray()     // Catch:{ all -> 0x007d }
            java.lang.String r5 = "key"
            java.lang.Long r3 = java.lang.Long.valueOf(r3)     // Catch:{ all -> 0x007d }
            r2.put(r5, r3)     // Catch:{ all -> 0x007d }
            java.lang.String r3 = "tile"
            r2.put(r3, r13)     // Catch:{ all -> 0x007d }
            android.database.sqlite.SQLiteDatabase r13 = r9.mDatabase     // Catch:{ all -> 0x007d }
            java.lang.String r3 = "tiles"
            r13.insert(r3, r14, r2)     // Catch:{ all -> 0x007d }
            r0 = 1
            org.osmdroid.config.IConfigurationProvider r13 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ all -> 0x007d }
            boolean r13 = r13.isDebugMode()     // Catch:{ all -> 0x007d }
            if (r13 == 0) goto L_0x0079
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x007d }
            r13.<init>()     // Catch:{ all -> 0x007d }
            java.lang.String r14 = "tile inserted "
            r13.append(r14)     // Catch:{ all -> 0x007d }
            java.lang.String r14 = r10.name()     // Catch:{ all -> 0x007d }
            r13.append(r14)     // Catch:{ all -> 0x007d }
            java.lang.String r14 = org.osmdroid.util.MapTileIndex.toString(r11)     // Catch:{ all -> 0x007d }
            r13.append(r14)     // Catch:{ all -> 0x007d }
            java.lang.String r13 = r13.toString()     // Catch:{ all -> 0x007d }
            android.util.Log.d(r1, r13)     // Catch:{ all -> 0x007d }
        L_0x0079:
            r6.close()     // Catch:{ IOException -> 0x00a8 }
            goto L_0x00a8
        L_0x007d:
            r13 = move-exception
            r14 = r6
            goto L_0x0081
        L_0x0080:
            r13 = move-exception
        L_0x0081:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x00a9 }
            r2.<init>()     // Catch:{ all -> 0x00a9 }
            java.lang.String r3 = "Unable to store cached tile from "
            r2.append(r3)     // Catch:{ all -> 0x00a9 }
            java.lang.String r10 = r10.name()     // Catch:{ all -> 0x00a9 }
            r2.append(r10)     // Catch:{ all -> 0x00a9 }
            java.lang.String r10 = " "
            r2.append(r10)     // Catch:{ all -> 0x00a9 }
            java.lang.String r10 = org.osmdroid.util.MapTileIndex.toString(r11)     // Catch:{ all -> 0x00a9 }
            r2.append(r10)     // Catch:{ all -> 0x00a9 }
            java.lang.String r10 = r2.toString()     // Catch:{ all -> 0x00a9 }
            android.util.Log.e(r1, r10, r13)     // Catch:{ all -> 0x00a9 }
            r14.close()     // Catch:{ IOException -> 0x00a8 }
        L_0x00a8:
            return r0
        L_0x00a9:
            r10 = move-exception
            r14.close()     // Catch:{ IOException -> 0x00ad }
        L_0x00ad:
            throw r10
        L_0x00ae:
            java.lang.String r10 = "Skipping SqlArchiveTileWriter saveFile, database is closed"
            android.util.Log.d(r1, r10)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.modules.SqliteArchiveTileWriter.saveFile(org.osmdroid.tileprovider.tilesource.ITileSource, long, java.io.InputStream, java.lang.Long):boolean");
    }

    public boolean exists(ITileSource iTileSource, long j) {
        try {
            Cursor tileCursor = getTileCursor(SqlTileWriter.getPrimaryKeyParameters(SqlTileWriter.getIndex(j), iTileSource));
            boolean z = tileCursor.getCount() != 0;
            tileCursor.close();
            return z;
        } catch (Throwable th) {
            Log.e(IMapView.LOGTAG, "Unable to store cached tile from " + iTileSource.name() + " " + MapTileIndex.toString(j), th);
            return false;
        }
    }

    public void onDetach() {
        SQLiteDatabase sQLiteDatabase = this.mDatabase;
        if (sQLiteDatabase != null) {
            sQLiteDatabase.close();
        }
    }

    public Cursor getTileCursor(String[] strArr) {
        SQLiteDatabase sQLiteDatabase = this.mDatabase;
        if (sQLiteDatabase != null && sQLiteDatabase.isOpen()) {
            return this.mDatabase.query("tiles", queryColumns, SqlTileWriter.getPrimaryKey(), strArr, (String) null, (String) null, (String) null);
        }
        Log.w(IMapView.LOGTAG, "Skipping SqlArchiveTileWriter getTileCursor, database is closed");
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0071  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.drawable.Drawable loadTile(org.osmdroid.tileprovider.tilesource.ITileSource r6, long r7) throws java.lang.Exception {
        /*
            r5 = this;
            android.database.sqlite.SQLiteDatabase r0 = r5.mDatabase
            java.lang.String r1 = "OsmDroid"
            r2 = 0
            if (r0 == 0) goto L_0x0075
            boolean r0 = r0.isOpen()
            if (r0 != 0) goto L_0x000e
            goto L_0x0075
        L_0x000e:
            long r3 = org.osmdroid.tileprovider.modules.SqlTileWriter.getIndex(r7)     // Catch:{ all -> 0x006e }
            java.lang.String[] r0 = org.osmdroid.tileprovider.modules.SqlTileWriter.getPrimaryKeyParameters((long) r3, (org.osmdroid.tileprovider.tilesource.ITileSource) r6)     // Catch:{ all -> 0x006e }
            android.database.Cursor r0 = r5.getTileCursor(r0)     // Catch:{ all -> 0x006e }
            if (r0 != 0) goto L_0x001d
            return r2
        L_0x001d:
            boolean r3 = r0.moveToFirst()     // Catch:{ all -> 0x006e }
            if (r3 == 0) goto L_0x002e
            java.lang.String r3 = "tile"
            int r3 = r0.getColumnIndex(r3)     // Catch:{ all -> 0x006e }
            byte[] r3 = r0.getBlob(r3)     // Catch:{ all -> 0x006e }
            goto L_0x002f
        L_0x002e:
            r3 = r2
        L_0x002f:
            r0.close()     // Catch:{ all -> 0x006e }
            if (r3 != 0) goto L_0x005e
            org.osmdroid.config.IConfigurationProvider r0 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ all -> 0x006e }
            boolean r0 = r0.isDebugMode()     // Catch:{ all -> 0x006e }
            if (r0 == 0) goto L_0x005d
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x006e }
            r0.<init>()     // Catch:{ all -> 0x006e }
            java.lang.String r3 = "SqlCache - Tile doesn't exist: "
            r0.append(r3)     // Catch:{ all -> 0x006e }
            java.lang.String r6 = r6.name()     // Catch:{ all -> 0x006e }
            r0.append(r6)     // Catch:{ all -> 0x006e }
            java.lang.String r6 = org.osmdroid.util.MapTileIndex.toString(r7)     // Catch:{ all -> 0x006e }
            r0.append(r6)     // Catch:{ all -> 0x006e }
            java.lang.String r6 = r0.toString()     // Catch:{ all -> 0x006e }
            android.util.Log.d(r1, r6)     // Catch:{ all -> 0x006e }
        L_0x005d:
            return r2
        L_0x005e:
            java.io.ByteArrayInputStream r7 = new java.io.ByteArrayInputStream     // Catch:{ all -> 0x006e }
            r7.<init>(r3)     // Catch:{ all -> 0x006e }
            android.graphics.drawable.Drawable r6 = r6.getDrawable((java.io.InputStream) r7)     // Catch:{ all -> 0x006b }
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r7)
            return r6
        L_0x006b:
            r6 = move-exception
            r2 = r7
            goto L_0x006f
        L_0x006e:
            r6 = move-exception
        L_0x006f:
            if (r2 == 0) goto L_0x0074
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r2)
        L_0x0074:
            throw r6
        L_0x0075:
            java.lang.String r6 = "Skipping SqlArchiveTileWriter loadTile, database is closed"
            android.util.Log.w(r1, r6)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.modules.SqliteArchiveTileWriter.loadTile(org.osmdroid.tileprovider.tilesource.ITileSource, long):android.graphics.drawable.Drawable");
    }
}
