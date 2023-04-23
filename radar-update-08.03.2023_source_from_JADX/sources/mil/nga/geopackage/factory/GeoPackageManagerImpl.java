package mil.nga.geopackage.factory;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import mil.nga.geopackage.C1157R;
import mil.nga.geopackage.GeoPackage;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.GeoPackageManager;
import mil.nga.geopackage.p009db.GeoPackageConnection;
import mil.nga.geopackage.p009db.GeoPackageDatabase;
import mil.nga.geopackage.p009db.GeoPackageTableCreator;
import mil.nga.geopackage.p009db.metadata.GeoPackageMetadata;
import mil.nga.geopackage.p009db.metadata.GeoPackageMetadataDataSource;
import mil.nga.geopackage.p009db.metadata.GeoPackageMetadataDb;
import mil.nga.geopackage.p010io.GeoPackageIOUtils;
import mil.nga.geopackage.p010io.GeoPackageProgress;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.geopackage.validate.GeoPackageValidate;

class GeoPackageManagerImpl implements GeoPackageManager {
    private final Context context;
    private boolean importHeaderValidation;
    private boolean importIntegrityValidation;
    private boolean openHeaderValidation;
    private boolean openIntegrityValidation;

    GeoPackageManagerImpl(Context context2) {
        this.context = context2;
        Resources resources = context2.getResources();
        this.importHeaderValidation = resources.getBoolean(C1157R.bool.manager_validation_import_header);
        this.importIntegrityValidation = resources.getBoolean(C1157R.bool.manager_validation_import_integrity);
        this.openHeaderValidation = resources.getBoolean(C1157R.bool.manager_validation_open_header);
        this.openIntegrityValidation = resources.getBoolean(C1157R.bool.manager_validation_open_integrity);
    }

    public List<String> databases() {
        TreeSet treeSet = new TreeSet();
        addDatabases(treeSet);
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(treeSet);
        return arrayList;
    }

    /* JADX INFO: finally extract failed */
    public List<String> databasesLike(String str) {
        GeoPackageMetadataDb geoPackageMetadataDb = new GeoPackageMetadataDb(this.context);
        geoPackageMetadataDb.open();
        try {
            List<String> metadataWhereNameLike = new GeoPackageMetadataDataSource(geoPackageMetadataDb).getMetadataWhereNameLike(str, "name");
            geoPackageMetadataDb.close();
            return deleteMissingDatabases(metadataWhereNameLike);
        } catch (Throwable th) {
            geoPackageMetadataDb.close();
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    public List<String> databasesNotLike(String str) {
        GeoPackageMetadataDb geoPackageMetadataDb = new GeoPackageMetadataDb(this.context);
        geoPackageMetadataDb.open();
        try {
            List<String> metadataWhereNameNotLike = new GeoPackageMetadataDataSource(geoPackageMetadataDb).getMetadataWhereNameNotLike(str, "name");
            geoPackageMetadataDb.close();
            return deleteMissingDatabases(metadataWhereNameNotLike);
        } catch (Throwable th) {
            geoPackageMetadataDb.close();
            throw th;
        }
    }

    private List<String> deleteMissingDatabases(List<String> list) {
        ArrayList arrayList = new ArrayList();
        for (String next : list) {
            if (exists(next)) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    public List<String> internalDatabases() {
        TreeSet treeSet = new TreeSet();
        addInternalDatabases(treeSet);
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(treeSet);
        return arrayList;
    }

    public List<String> externalDatabases() {
        TreeSet treeSet = new TreeSet();
        addExternalDatabases(treeSet);
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(treeSet);
        return arrayList;
    }

    public int count() {
        return databaseSet().size();
    }

    public int internalCount() {
        return internalDatabaseSet().size();
    }

    public int externalCount() {
        return externalDatabaseSet().size();
    }

    public Set<String> databaseSet() {
        HashSet hashSet = new HashSet();
        addDatabases(hashSet);
        return hashSet;
    }

    public Set<String> internalDatabaseSet() {
        HashSet hashSet = new HashSet();
        addInternalDatabases(hashSet);
        return hashSet;
    }

    public Set<String> externalDatabaseSet() {
        HashSet hashSet = new HashSet();
        addExternalDatabases(hashSet);
        return hashSet;
    }

    public boolean exists(String str) {
        boolean contains = internalDatabaseSet().contains(str);
        if (!contains) {
            GeoPackageMetadataDb geoPackageMetadataDb = new GeoPackageMetadataDb(this.context);
            geoPackageMetadataDb.open();
            try {
                GeoPackageMetadata geoPackageMetadata = new GeoPackageMetadataDataSource(geoPackageMetadataDb).get(str);
                if (geoPackageMetadata != null) {
                    if (geoPackageMetadata.getExternalPath() == null || new File(geoPackageMetadata.getExternalPath()).exists()) {
                        contains = true;
                    } else {
                        delete(str);
                    }
                }
            } finally {
                geoPackageMetadataDb.close();
            }
        }
        return contains;
    }

    public long size(String str) {
        return getFile(str).length();
    }

    public boolean isExternal(String str) {
        GeoPackageMetadataDb geoPackageMetadataDb = new GeoPackageMetadataDb(this.context);
        geoPackageMetadataDb.open();
        try {
            return new GeoPackageMetadataDataSource(geoPackageMetadataDb).isExternal(str);
        } finally {
            geoPackageMetadataDb.close();
        }
    }

    public boolean existsAtExternalFile(File file) {
        return existsAtExternalPath(file.getAbsolutePath());
    }

    public boolean existsAtExternalPath(String str) {
        return getGeoPackageMetadataAtExternalPath(str) != null;
    }

    public String getPath(String str) {
        return getFile(str).getAbsolutePath();
    }

    public File getFile(String str) {
        File file;
        GeoPackageMetadata geoPackageMetadata = getGeoPackageMetadata(str);
        if (geoPackageMetadata == null || !geoPackageMetadata.isExternal()) {
            file = this.context.getDatabasePath(str);
        } else {
            file = new File(geoPackageMetadata.getExternalPath());
        }
        if (file != null && file.exists()) {
            return file;
        }
        throw new GeoPackageException("GeoPackage does not exist: " + str);
    }

    public String getDatabaseAtExternalFile(File file) {
        return getDatabaseAtExternalPath(file.getAbsolutePath());
    }

    public String getDatabaseAtExternalPath(String str) {
        GeoPackageMetadata geoPackageMetadataAtExternalPath = getGeoPackageMetadataAtExternalPath(str);
        if (geoPackageMetadataAtExternalPath != null) {
            return geoPackageMetadataAtExternalPath.getName();
        }
        return null;
    }

    public String readableSize(String str) {
        return GeoPackageIOUtils.formatBytes(size(str));
    }

    public boolean delete(String str) {
        boolean isExternal = isExternal(str);
        GeoPackageMetadataDb geoPackageMetadataDb = new GeoPackageMetadataDb(this.context);
        geoPackageMetadataDb.open();
        try {
            return !isExternal ? this.context.deleteDatabase(str) : new GeoPackageMetadataDataSource(geoPackageMetadataDb).delete(str);
        } finally {
            geoPackageMetadataDb.close();
        }
    }

    public boolean deleteAll() {
        Iterator<String> it = databaseSet().iterator();
        while (true) {
            boolean z = true;
            while (true) {
                if (!it.hasNext()) {
                    return z;
                }
                if (!delete(it.next()) || !z) {
                    z = false;
                }
            }
        }
    }

    public boolean deleteAllExternal() {
        Iterator<String> it = externalDatabaseSet().iterator();
        while (true) {
            boolean z = true;
            while (true) {
                if (!it.hasNext()) {
                    return z;
                }
                if (!delete(it.next()) || !z) {
                    z = false;
                }
            }
        }
    }

    public boolean deleteAllMissingExternal() {
        Iterator<GeoPackageMetadata> it = getExternalGeoPackages().iterator();
        while (true) {
            boolean z = false;
            while (true) {
                if (!it.hasNext()) {
                    return z;
                }
                GeoPackageMetadata next = it.next();
                if (!new File(next.getExternalPath()).exists()) {
                    if (delete(next.getName()) || z) {
                        z = true;
                    }
                }
            }
        }
    }

    /* JADX INFO: finally extract failed */
    public boolean create(String str) {
        if (!exists(str)) {
            createAndCloseGeoPackage(new GeoPackageDatabase(this.context.openOrCreateDatabase(str, 0, (SQLiteDatabase.CursorFactory) null)));
            GeoPackageMetadataDb geoPackageMetadataDb = new GeoPackageMetadataDb(this.context);
            geoPackageMetadataDb.open();
            try {
                GeoPackageMetadataDataSource geoPackageMetadataDataSource = new GeoPackageMetadataDataSource(geoPackageMetadataDb);
                GeoPackageMetadata geoPackageMetadata = new GeoPackageMetadata();
                geoPackageMetadata.setName(str);
                geoPackageMetadataDataSource.create(geoPackageMetadata);
                geoPackageMetadataDb.close();
                return true;
            } catch (Throwable th) {
                geoPackageMetadataDb.close();
                throw th;
            }
        } else {
            throw new GeoPackageException("GeoPackage already exists: " + str);
        }
    }

    private void createAndCloseGeoPackage(GeoPackageDatabase geoPackageDatabase) {
        GeoPackageConnection geoPackageConnection = new GeoPackageConnection(geoPackageDatabase);
        geoPackageConnection.setApplicationId();
        geoPackageConnection.setUserVersion();
        new GeoPackageTableCreator(geoPackageConnection).createRequired();
        geoPackageConnection.close();
    }

    public boolean createAtPath(String str, File file) {
        return createFile(str, new File(file, str + PropertyConstants.PROPERTY_DIVIDER + "gpkg"));
    }

    public boolean createFile(File file) {
        return createFile(GeoPackageIOUtils.getFileNameWithoutExtension(file), file);
    }

    public boolean createFile(String str, File file) {
        if (!exists(str)) {
            if (!GeoPackageValidate.hasGeoPackageExtension(file)) {
                if (GeoPackageIOUtils.getFileExtension(file) == null) {
                    File parentFile = file.getParentFile();
                    file = new File(parentFile, file.getName() + PropertyConstants.PROPERTY_DIVIDER + "gpkg");
                } else {
                    throw new GeoPackageException("File can not have a non GeoPackage extension. Invalid File: " + file.getAbsolutePath());
                }
            }
            if (!file.exists()) {
                createAndCloseGeoPackage(new GeoPackageDatabase(SQLiteDatabase.openOrCreateDatabase(file, (SQLiteDatabase.CursorFactory) null)));
                return importGeoPackageAsExternalLink(file, str);
            }
            throw new GeoPackageException("GeoPackage file already exists: " + file.getAbsolutePath());
        }
        throw new GeoPackageException("GeoPackage already exists: " + str);
    }

    public boolean importGeoPackage(File file) {
        return importGeoPackage((String) null, file, false);
    }

    public boolean importGeoPackage(File file, boolean z) {
        return importGeoPackage((String) null, file, z);
    }

    public boolean importGeoPackage(String str, InputStream inputStream) {
        return importGeoPackage(str, inputStream, false, (GeoPackageProgress) null);
    }

    public boolean importGeoPackage(String str, InputStream inputStream, GeoPackageProgress geoPackageProgress) {
        return importGeoPackage(str, inputStream, false, geoPackageProgress);
    }

    public boolean importGeoPackage(String str, InputStream inputStream, boolean z) {
        return importGeoPackage(str, inputStream, z, (GeoPackageProgress) null);
    }

    public boolean importGeoPackage(String str, InputStream inputStream, boolean z, GeoPackageProgress geoPackageProgress) {
        if (geoPackageProgress != null) {
            try {
                int available = inputStream.available();
                if (available > 0) {
                    geoPackageProgress.setMax(available);
                }
            } catch (IOException e) {
                Log.w("GeoPackageManagerImpl", "Could not determine stream available size. Database: " + str, e);
            }
        }
        return importGeoPackage(str, z, inputStream, geoPackageProgress);
    }

    public boolean importGeoPackage(String str, File file) {
        return importGeoPackage(str, file, false);
    }

    public boolean importGeoPackage(String str, File file, boolean z) {
        GeoPackageValidate.validateGeoPackageExtension(file);
        if (str == null) {
            str = GeoPackageIOUtils.getFileNameWithoutExtension(file);
        }
        try {
            return importGeoPackage(str, z, (InputStream) new FileInputStream(file), (GeoPackageProgress) null);
        } catch (FileNotFoundException e) {
            throw new GeoPackageException("Failed read or write GeoPackage file '" + file + "' to database: '" + str + "'", e);
        }
    }

    public boolean importGeoPackage(String str, URL url) {
        return importGeoPackage(str, url, false, (GeoPackageProgress) null);
    }

    public boolean importGeoPackage(String str, URL url, GeoPackageProgress geoPackageProgress) {
        return importGeoPackage(str, url, false, geoPackageProgress);
    }

    public boolean importGeoPackage(String str, URL url, boolean z) {
        return importGeoPackage(str, url, z, (GeoPackageProgress) null);
    }

    /* JADX WARNING: type inference failed for: r7v5, types: [java.net.URLConnection] */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0095, code lost:
        r8 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0096, code lost:
        r2 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0099, code lost:
        r6 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x009a, code lost:
        r2 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00c8, code lost:
        r2.disconnect();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0042 A[Catch:{ IOException -> 0x00a1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x005c A[SYNTHETIC, Splitter:B:29:0x005c] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0099 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:3:0x000b] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00c8  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean importGeoPackage(java.lang.String r6, java.net.URL r7, boolean r8, mil.nga.geopackage.p010io.GeoPackageProgress r9) {
        /*
            r5 = this;
            java.lang.String r0 = " from URL: '"
            java.lang.String r1 = "Failed to import GeoPackage "
            r2 = 0
            java.net.URLConnection r3 = r7.openConnection()     // Catch:{ IOException -> 0x00a1 }
            java.net.HttpURLConnection r3 = (java.net.HttpURLConnection) r3     // Catch:{ IOException -> 0x00a1 }
            r3.connect()     // Catch:{ IOException -> 0x009c, all -> 0x0099 }
            int r2 = r3.getResponseCode()     // Catch:{ IOException -> 0x009c, all -> 0x0099 }
            r4 = 301(0x12d, float:4.22E-43)
            if (r2 == r4) goto L_0x0021
            r4 = 302(0x12e, float:4.23E-43)
            if (r2 == r4) goto L_0x0021
            r4 = 303(0x12f, float:4.25E-43)
            if (r2 != r4) goto L_0x001f
            goto L_0x0021
        L_0x001f:
            r2 = r3
            goto L_0x003a
        L_0x0021:
            java.lang.String r2 = "Location"
            java.lang.String r2 = r3.getHeaderField(r2)     // Catch:{ IOException -> 0x009c, all -> 0x0099 }
            r3.disconnect()     // Catch:{ IOException -> 0x009c, all -> 0x0099 }
            java.net.URL r4 = new java.net.URL     // Catch:{ IOException -> 0x009c, all -> 0x0099 }
            r4.<init>(r2)     // Catch:{ IOException -> 0x009c, all -> 0x0099 }
            java.net.URLConnection r7 = r4.openConnection()     // Catch:{ IOException -> 0x0095, all -> 0x0099 }
            r2 = r7
            java.net.HttpURLConnection r2 = (java.net.HttpURLConnection) r2     // Catch:{ IOException -> 0x0095, all -> 0x0099 }
            r2.connect()     // Catch:{ IOException -> 0x0093 }
            r7 = r4
        L_0x003a:
            int r3 = r2.getResponseCode()     // Catch:{ IOException -> 0x00a1 }
            r4 = 200(0xc8, float:2.8E-43)
            if (r3 != r4) goto L_0x005c
            int r3 = r2.getContentLength()     // Catch:{ IOException -> 0x00a1 }
            r4 = -1
            if (r3 == r4) goto L_0x004e
            if (r9 == 0) goto L_0x004e
            r9.setMax(r3)     // Catch:{ IOException -> 0x00a1 }
        L_0x004e:
            java.io.InputStream r3 = r2.getInputStream()     // Catch:{ IOException -> 0x00a1 }
            boolean r6 = r5.importGeoPackage((java.lang.String) r6, (boolean) r8, (java.io.InputStream) r3, (mil.nga.geopackage.p010io.GeoPackageProgress) r9)     // Catch:{ IOException -> 0x00a1 }
            if (r2 == 0) goto L_0x005b
            r2.disconnect()
        L_0x005b:
            return r6
        L_0x005c:
            mil.nga.geopackage.GeoPackageException r8 = new mil.nga.geopackage.GeoPackageException     // Catch:{ IOException -> 0x00a1 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00a1 }
            r9.<init>()     // Catch:{ IOException -> 0x00a1 }
            r9.append(r1)     // Catch:{ IOException -> 0x00a1 }
            r9.append(r6)     // Catch:{ IOException -> 0x00a1 }
            r9.append(r0)     // Catch:{ IOException -> 0x00a1 }
            java.lang.String r3 = r7.toString()     // Catch:{ IOException -> 0x00a1 }
            r9.append(r3)     // Catch:{ IOException -> 0x00a1 }
            java.lang.String r3 = "'. HTTP "
            r9.append(r3)     // Catch:{ IOException -> 0x00a1 }
            int r3 = r2.getResponseCode()     // Catch:{ IOException -> 0x00a1 }
            r9.append(r3)     // Catch:{ IOException -> 0x00a1 }
            java.lang.String r3 = " "
            r9.append(r3)     // Catch:{ IOException -> 0x00a1 }
            java.lang.String r3 = r2.getResponseMessage()     // Catch:{ IOException -> 0x00a1 }
            r9.append(r3)     // Catch:{ IOException -> 0x00a1 }
            java.lang.String r9 = r9.toString()     // Catch:{ IOException -> 0x00a1 }
            r8.<init>((java.lang.String) r9)     // Catch:{ IOException -> 0x00a1 }
            throw r8     // Catch:{ IOException -> 0x00a1 }
        L_0x0093:
            r8 = move-exception
            goto L_0x0097
        L_0x0095:
            r8 = move-exception
            r2 = r3
        L_0x0097:
            r7 = r4
            goto L_0x00a2
        L_0x0099:
            r6 = move-exception
            r2 = r3
            goto L_0x00c6
        L_0x009c:
            r8 = move-exception
            r2 = r3
            goto L_0x00a2
        L_0x009f:
            r6 = move-exception
            goto L_0x00c6
        L_0x00a1:
            r8 = move-exception
        L_0x00a2:
            mil.nga.geopackage.GeoPackageException r9 = new mil.nga.geopackage.GeoPackageException     // Catch:{ all -> 0x009f }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x009f }
            r3.<init>()     // Catch:{ all -> 0x009f }
            r3.append(r1)     // Catch:{ all -> 0x009f }
            r3.append(r6)     // Catch:{ all -> 0x009f }
            r3.append(r0)     // Catch:{ all -> 0x009f }
            java.lang.String r6 = r7.toString()     // Catch:{ all -> 0x009f }
            r3.append(r6)     // Catch:{ all -> 0x009f }
            java.lang.String r6 = "'"
            r3.append(r6)     // Catch:{ all -> 0x009f }
            java.lang.String r6 = r3.toString()     // Catch:{ all -> 0x009f }
            r9.<init>(r6, r8)     // Catch:{ all -> 0x009f }
            throw r9     // Catch:{ all -> 0x009f }
        L_0x00c6:
            if (r2 == 0) goto L_0x00cb
            r2.disconnect()
        L_0x00cb:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.factory.GeoPackageManagerImpl.importGeoPackage(java.lang.String, java.net.URL, boolean, mil.nga.geopackage.io.GeoPackageProgress):boolean");
    }

    public void exportGeoPackage(String str, File file) {
        exportGeoPackage(str, str, file);
    }

    public void exportGeoPackage(String str, String str2, File file) {
        File file2 = new File(file, str2);
        if (!GeoPackageValidate.hasGeoPackageExtension(file2)) {
            file2 = new File(file, str2 + ".gpkg");
        }
        try {
            GeoPackageIOUtils.copyFile(getFile(str), file2);
        } catch (IOException e) {
            throw new GeoPackageException("Failed read or write GeoPackage database '" + str + "' to file: '" + file2, e);
        }
    }

    public GeoPackage open(String str) {
        return open(str, true);
    }

    public GeoPackage open(String str, boolean z) {
        boolean z2;
        String str2;
        SQLiteDatabase sQLiteDatabase = null;
        if (!exists(str)) {
            return null;
        }
        GeoPackageCursorFactory geoPackageCursorFactory = new GeoPackageCursorFactory();
        GeoPackageMetadata geoPackageMetadata = getGeoPackageMetadata(str);
        if (geoPackageMetadata == null || !geoPackageMetadata.isExternal()) {
            z2 = z;
            str2 = null;
            sQLiteDatabase = this.context.openOrCreateDatabase(str, 0, geoPackageCursorFactory);
        } else {
            String externalPath = geoPackageMetadata.getExternalPath();
            if (z) {
                try {
                    sQLiteDatabase = SQLiteDatabase.openDatabase(externalPath, geoPackageCursorFactory, 16);
                } catch (Exception e) {
                    Log.e("GeoPackageManagerImpl", "Failed to open database as writable: " + str, e);
                }
            }
            if (sQLiteDatabase == null) {
                sQLiteDatabase = SQLiteDatabase.openDatabase(externalPath, geoPackageCursorFactory, 17);
                str2 = externalPath;
                z2 = false;
            } else {
                z2 = z;
                str2 = externalPath;
            }
        }
        validateDatabaseAndCloseOnError(sQLiteDatabase, this.openHeaderValidation, this.openIntegrityValidation);
        GeoPackageConnection geoPackageConnection = new GeoPackageConnection(new GeoPackageDatabase(sQLiteDatabase));
        return new GeoPackageImpl(str, str2, geoPackageConnection, geoPackageCursorFactory, new GeoPackageTableCreator(geoPackageConnection), z2);
    }

    public boolean isImportHeaderValidation() {
        return this.importHeaderValidation;
    }

    public void setImportHeaderValidation(boolean z) {
        this.importHeaderValidation = z;
    }

    public boolean isImportIntegrityValidation() {
        return this.importIntegrityValidation;
    }

    public void setImportIntegrityValidation(boolean z) {
        this.importIntegrityValidation = z;
    }

    public boolean isOpenHeaderValidation() {
        return this.openHeaderValidation;
    }

    public void setOpenHeaderValidation(boolean z) {
        this.openHeaderValidation = z;
    }

    public boolean isOpenIntegrityValidation() {
        return this.openIntegrityValidation;
    }

    public void setOpenIntegrityValidation(boolean z) {
        this.openIntegrityValidation = z;
    }

    public boolean validate(String str) {
        return isValid(str, true, true);
    }

    public boolean validateHeader(String str) {
        return isValid(str, true, false);
    }

    public boolean validateIntegrity(String str) {
        return isValid(str, false, true);
    }

    private boolean isValid(String str, boolean z, boolean z2) {
        SQLiteDatabase sQLiteDatabase;
        boolean z3 = false;
        if (exists(str)) {
            GeoPackageCursorFactory geoPackageCursorFactory = new GeoPackageCursorFactory();
            GeoPackageMetadata geoPackageMetadata = getGeoPackageMetadata(str);
            if (geoPackageMetadata == null || !geoPackageMetadata.isExternal()) {
                this.context.getDatabasePath(str).getAbsolutePath();
                sQLiteDatabase = this.context.openOrCreateDatabase(str, 0, geoPackageCursorFactory);
            } else {
                String externalPath = geoPackageMetadata.getExternalPath();
                try {
                    sQLiteDatabase = SQLiteDatabase.openDatabase(externalPath, geoPackageCursorFactory, 16);
                } catch (Exception unused) {
                    sQLiteDatabase = SQLiteDatabase.openDatabase(externalPath, geoPackageCursorFactory, 17);
                }
            }
            if (z) {
                try {
                    if (isDatabaseHeaderValid(sQLiteDatabase)) {
                    }
                } catch (Exception e) {
                    Log.e("GeoPackageManagerImpl", "Failed to validate database", e);
                } catch (Throwable th) {
                    sQLiteDatabase.close();
                    throw th;
                }
                sQLiteDatabase.close();
            }
            if (!z2 || sQLiteDatabase.isDatabaseIntegrityOk()) {
                z3 = true;
            }
            sQLiteDatabase.close();
        }
        return z3;
    }

    public boolean copy(String str, String str2) {
        try {
            GeoPackageIOUtils.copyFile(getFile(str), this.context.getDatabasePath(str2));
            return exists(str2);
        } catch (IOException e) {
            throw new GeoPackageException("Failed to copy GeoPackage database '" + str + "' to '" + str2 + "'", e);
        }
    }

    public boolean rename(String str, String str2) {
        GeoPackageMetadata geoPackageMetadata = getGeoPackageMetadata(str);
        if (geoPackageMetadata != null) {
            GeoPackageMetadataDb geoPackageMetadataDb = new GeoPackageMetadataDb(this.context);
            geoPackageMetadataDb.open();
            try {
                new GeoPackageMetadataDataSource(geoPackageMetadataDb).rename(geoPackageMetadata, str2);
            } finally {
                geoPackageMetadataDb.close();
            }
        }
        if ((geoPackageMetadata == null || !geoPackageMetadata.isExternal()) && copy(str, str2)) {
            delete(str);
        }
        return exists(str2);
    }

    public boolean importGeoPackageAsExternalLink(File file, String str) {
        return importGeoPackageAsExternalLink(file, str, false);
    }

    public boolean importGeoPackageAsExternalLink(File file, String str, boolean z) {
        return importGeoPackageAsExternalLink(file.getAbsolutePath(), str, z);
    }

    public boolean importGeoPackageAsExternalLink(String str, String str2) {
        return importGeoPackageAsExternalLink(str, str2, false);
    }

    public boolean importGeoPackageAsExternalLink(String str, String str2, boolean z) {
        GeoPackageMetadataDataSource geoPackageMetadataDataSource;
        GeoPackage open;
        if (exists(str2)) {
            if (!z) {
                throw new GeoPackageException("GeoPackage database already exists: " + str2);
            } else if (!delete(str2)) {
                throw new GeoPackageException("Failed to delete existing database: " + str2);
            }
        }
        try {
            validateDatabaseAndClose(SQLiteDatabase.openDatabase(str, (SQLiteDatabase.CursorFactory) null, 17), this.importHeaderValidation, this.importIntegrityValidation);
            GeoPackageMetadataDb geoPackageMetadataDb = new GeoPackageMetadataDb(this.context);
            geoPackageMetadataDb.open();
            try {
                geoPackageMetadataDataSource = new GeoPackageMetadataDataSource(geoPackageMetadataDb);
                GeoPackageMetadata geoPackageMetadata = new GeoPackageMetadata();
                geoPackageMetadata.setName(str2);
                geoPackageMetadata.setExternalPath(str);
                geoPackageMetadataDataSource.create(geoPackageMetadata);
                open = open(str2, false);
                if (open != null) {
                    GeoPackageValidate.validateMinimumTables(open);
                    open.close();
                    geoPackageMetadataDb.close();
                    return exists(str2);
                }
                geoPackageMetadataDataSource.delete(str2);
                throw new GeoPackageException("Unable to open GeoPackage database. Database: " + str2);
            } catch (RuntimeException e) {
                geoPackageMetadataDataSource.delete(str2);
                throw e;
            } catch (Throwable th) {
                geoPackageMetadataDb.close();
                throw th;
            }
        } catch (SQLiteException e2) {
            throw new GeoPackageException("Failed to import GeoPackage database as external link: " + str2 + ", Path: " + str, e2);
        }
    }

    private void validateDatabaseAndCloseOnError(SQLiteDatabase sQLiteDatabase, boolean z, boolean z2) {
        validateDatabase(sQLiteDatabase, z, z2, false, true);
    }

    private void validateDatabaseAndClose(SQLiteDatabase sQLiteDatabase, boolean z, boolean z2) {
        validateDatabase(sQLiteDatabase, z, z2, true, true);
    }

    private void validateDatabase(SQLiteDatabase sQLiteDatabase, boolean z, boolean z2, boolean z3, boolean z4) {
        if (z) {
            try {
                validateDatabaseHeader(sQLiteDatabase);
            } catch (Exception e) {
                if (z4) {
                    sQLiteDatabase.close();
                }
                throw e;
            }
        }
        if (z2) {
            validateDatabaseIntegrity(sQLiteDatabase);
        }
        if (z3) {
            sQLiteDatabase.close();
        }
    }

    private void validateDatabaseHeader(SQLiteDatabase sQLiteDatabase) {
        if (!isDatabaseHeaderValid(sQLiteDatabase)) {
            throw new GeoPackageException("GeoPackage SQLite header is not valid: " + sQLiteDatabase.getPath());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0041 A[SYNTHETIC, Splitter:B:21:0x0041] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0047 A[SYNTHETIC, Splitter:B:26:0x0047] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isDatabaseHeaderValid(android.database.sqlite.SQLiteDatabase r5) {
        /*
            r4 = this;
            r0 = 0
            r1 = 0
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0037 }
            java.lang.String r5 = r5.getPath()     // Catch:{ Exception -> 0x0037 }
            r2.<init>(r5)     // Catch:{ Exception -> 0x0037 }
            r5 = 16
            byte[] r1 = new byte[r5]     // Catch:{ Exception -> 0x0032, all -> 0x002f }
            int r3 = r2.read(r1)     // Catch:{ Exception -> 0x0032, all -> 0x002f }
            if (r3 != r5) goto L_0x002b
            mil.nga.wkb.io.ByteReader r3 = new mil.nga.wkb.io.ByteReader     // Catch:{ Exception -> 0x0032, all -> 0x002f }
            r3.<init>(r1)     // Catch:{ Exception -> 0x0032, all -> 0x002f }
            java.lang.String r5 = r3.readString(r5)     // Catch:{ Exception -> 0x0032, all -> 0x002f }
            r1 = 15
            java.lang.String r5 = r5.substring(r0, r1)     // Catch:{ Exception -> 0x0032, all -> 0x002f }
            java.lang.String r1 = "SQLite format 3"
            boolean r5 = r5.equalsIgnoreCase(r1)     // Catch:{ Exception -> 0x0032, all -> 0x002f }
            r0 = r5
        L_0x002b:
            r2.close()     // Catch:{ IOException -> 0x0044 }
            goto L_0x0044
        L_0x002f:
            r5 = move-exception
            r1 = r2
            goto L_0x0045
        L_0x0032:
            r5 = move-exception
            r1 = r2
            goto L_0x0038
        L_0x0035:
            r5 = move-exception
            goto L_0x0045
        L_0x0037:
            r5 = move-exception
        L_0x0038:
            java.lang.String r2 = "GeoPackageManagerImpl"
            java.lang.String r3 = "Failed to retrieve database header"
            android.util.Log.e(r2, r3, r5)     // Catch:{ all -> 0x0035 }
            if (r1 == 0) goto L_0x0044
            r1.close()     // Catch:{ IOException -> 0x0044 }
        L_0x0044:
            return r0
        L_0x0045:
            if (r1 == 0) goto L_0x004a
            r1.close()     // Catch:{ IOException -> 0x004a }
        L_0x004a:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.factory.GeoPackageManagerImpl.isDatabaseHeaderValid(android.database.sqlite.SQLiteDatabase):boolean");
    }

    private void validateDatabaseIntegrity(SQLiteDatabase sQLiteDatabase) {
        if (!sQLiteDatabase.isDatabaseIntegrityOk()) {
            throw new GeoPackageException("GeoPackage SQLite file integrity failed: " + sQLiteDatabase.getPath());
        }
    }

    private void addDatabases(Collection<String> collection) {
        addInternalDatabases(collection);
        addExternalDatabases(collection);
    }

    private void addInternalDatabases(Collection<String> collection) {
        for (String str : this.context.databaseList()) {
            if (!isTemporary(str) && !str.equalsIgnoreCase(GeoPackageMetadataDb.DATABASE_NAME)) {
                collection.add(str);
            }
        }
    }

    private void addExternalDatabases(Collection<String> collection) {
        for (GeoPackageMetadata next : getExternalGeoPackages()) {
            if (new File(next.getExternalPath()).exists()) {
                collection.add(next.getName());
            } else {
                delete(next.getName());
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:35|36|37) */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00c4, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
        delete(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00df, code lost:
        throw new mil.nga.geopackage.GeoPackageException("Invalid GeoPackage database file. Could not verify existence of required tables: gpkg_spatial_ref_sys & gpkg_contents, Database: " + r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00e0, code lost:
        r5.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00e3, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x00c6 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean importGeoPackage(java.lang.String r4, boolean r5, java.io.InputStream r6, mil.nga.geopackage.p010io.GeoPackageProgress r7) {
        /*
            r3 = this;
            boolean r0 = r3.exists(r4)
            if (r0 == 0) goto L_0x003d
            if (r5 == 0) goto L_0x0026
            boolean r5 = r3.delete(r4)
            if (r5 == 0) goto L_0x000f
            goto L_0x003d
        L_0x000f:
            mil.nga.geopackage.GeoPackageException r5 = new mil.nga.geopackage.GeoPackageException
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Failed to delete existing database: "
            r6.append(r7)
            r6.append(r4)
            java.lang.String r4 = r6.toString()
            r5.<init>((java.lang.String) r4)
            throw r5
        L_0x0026:
            mil.nga.geopackage.GeoPackageException r5 = new mil.nga.geopackage.GeoPackageException
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "GeoPackage database already exists: "
            r6.append(r7)
            r6.append(r4)
            java.lang.String r4 = r6.toString()
            r5.<init>((java.lang.String) r4)
            throw r5
        L_0x003d:
            android.content.Context r5 = r3.context
            java.io.File r5 = r5.getDatabasePath(r4)
            android.content.Context r0 = r3.context     // Catch:{ IOException -> 0x010f }
            r1 = 0
            r2 = 0
            android.database.sqlite.SQLiteDatabase r0 = r0.openOrCreateDatabase(r4, r2, r1)     // Catch:{ IOException -> 0x010f }
            r0.close()     // Catch:{ IOException -> 0x010f }
            mil.nga.geopackage.p010io.GeoPackageIOUtils.copyStream((java.io.InputStream) r6, (java.io.File) r5, (mil.nga.geopackage.p010io.GeoPackageProgress) r7)     // Catch:{ IOException -> 0x010f }
            if (r7 == 0) goto L_0x0059
            boolean r5 = r7.isActive()
            if (r5 == 0) goto L_0x00a5
        L_0x0059:
            android.content.Context r5 = r3.context     // Catch:{ Exception -> 0x0103 }
            mil.nga.geopackage.factory.GeoPackageManagerImpl$1 r6 = new mil.nga.geopackage.factory.GeoPackageManagerImpl$1     // Catch:{ Exception -> 0x0103 }
            r6.<init>()     // Catch:{ Exception -> 0x0103 }
            android.database.sqlite.SQLiteDatabase r5 = r5.openOrCreateDatabase(r4, r2, r1, r6)     // Catch:{ Exception -> 0x0103 }
            boolean r6 = r3.importHeaderValidation     // Catch:{ Exception -> 0x0103 }
            boolean r7 = r3.importIntegrityValidation     // Catch:{ Exception -> 0x0103 }
            r3.validateDatabaseAndClose(r5, r6, r7)     // Catch:{ Exception -> 0x0103 }
            mil.nga.geopackage.db.metadata.GeoPackageMetadataDb r5 = new mil.nga.geopackage.db.metadata.GeoPackageMetadataDb     // Catch:{ Exception -> 0x0103 }
            android.content.Context r6 = r3.context     // Catch:{ Exception -> 0x0103 }
            r5.<init>(r6)     // Catch:{ Exception -> 0x0103 }
            r5.open()     // Catch:{ Exception -> 0x0103 }
            mil.nga.geopackage.db.metadata.GeoPackageMetadataDataSource r6 = new mil.nga.geopackage.db.metadata.GeoPackageMetadataDataSource     // Catch:{ all -> 0x00fe }
            r6.<init>((mil.nga.geopackage.p009db.metadata.GeoPackageMetadataDb) r5)     // Catch:{ all -> 0x00fe }
            mil.nga.geopackage.db.metadata.GeoPackageMetadata r7 = new mil.nga.geopackage.db.metadata.GeoPackageMetadata     // Catch:{ all -> 0x00fe }
            r7.<init>()     // Catch:{ all -> 0x00fe }
            r7.setName(r4)     // Catch:{ all -> 0x00fe }
            r6.create(r7)     // Catch:{ all -> 0x00fe }
            r5.close()     // Catch:{ Exception -> 0x0103 }
            mil.nga.geopackage.GeoPackage r5 = r3.open(r4, r2)
            if (r5 == 0) goto L_0x00e4
            mil.nga.geopackage.core.srs.SpatialReferenceSystemDao r6 = r5.getSpatialReferenceSystemDao()     // Catch:{ SQLException -> 0x00c6 }
            boolean r6 = r6.isTableExists()     // Catch:{ SQLException -> 0x00c6 }
            if (r6 == 0) goto L_0x00aa
            mil.nga.geopackage.core.contents.ContentsDao r6 = r5.getContentsDao()     // Catch:{ SQLException -> 0x00c6 }
            boolean r6 = r6.isTableExists()     // Catch:{ SQLException -> 0x00c6 }
            if (r6 == 0) goto L_0x00aa
            r5.close()
        L_0x00a5:
            boolean r4 = r3.exists(r4)
            return r4
        L_0x00aa:
            r3.delete(r4)     // Catch:{ SQLException -> 0x00c6 }
            mil.nga.geopackage.GeoPackageException r6 = new mil.nga.geopackage.GeoPackageException     // Catch:{ SQLException -> 0x00c6 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ SQLException -> 0x00c6 }
            r7.<init>()     // Catch:{ SQLException -> 0x00c6 }
            java.lang.String r0 = "Invalid GeoPackage database file. Does not contain required tables: gpkg_spatial_ref_sys & gpkg_contents, Database: "
            r7.append(r0)     // Catch:{ SQLException -> 0x00c6 }
            r7.append(r4)     // Catch:{ SQLException -> 0x00c6 }
            java.lang.String r7 = r7.toString()     // Catch:{ SQLException -> 0x00c6 }
            r6.<init>((java.lang.String) r7)     // Catch:{ SQLException -> 0x00c6 }
            throw r6     // Catch:{ SQLException -> 0x00c6 }
        L_0x00c4:
            r4 = move-exception
            goto L_0x00e0
        L_0x00c6:
            r3.delete(r4)     // Catch:{ all -> 0x00c4 }
            mil.nga.geopackage.GeoPackageException r6 = new mil.nga.geopackage.GeoPackageException     // Catch:{ all -> 0x00c4 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x00c4 }
            r7.<init>()     // Catch:{ all -> 0x00c4 }
            java.lang.String r0 = "Invalid GeoPackage database file. Could not verify existence of required tables: gpkg_spatial_ref_sys & gpkg_contents, Database: "
            r7.append(r0)     // Catch:{ all -> 0x00c4 }
            r7.append(r4)     // Catch:{ all -> 0x00c4 }
            java.lang.String r4 = r7.toString()     // Catch:{ all -> 0x00c4 }
            r6.<init>((java.lang.String) r4)     // Catch:{ all -> 0x00c4 }
            throw r6     // Catch:{ all -> 0x00c4 }
        L_0x00e0:
            r5.close()
            throw r4
        L_0x00e4:
            r3.delete(r4)
            mil.nga.geopackage.GeoPackageException r5 = new mil.nga.geopackage.GeoPackageException
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Unable to open GeoPackage database. Database: "
            r6.append(r7)
            r6.append(r4)
            java.lang.String r4 = r6.toString()
            r5.<init>((java.lang.String) r4)
            throw r5
        L_0x00fe:
            r6 = move-exception
            r5.close()     // Catch:{ Exception -> 0x0103 }
            throw r6     // Catch:{ Exception -> 0x0103 }
        L_0x0103:
            r5 = move-exception
            r3.delete(r4)
            mil.nga.geopackage.GeoPackageException r4 = new mil.nga.geopackage.GeoPackageException
            java.lang.String r6 = "Invalid GeoPackage database file"
            r4.<init>(r6, r5)
            throw r4
        L_0x010f:
            r5 = move-exception
            mil.nga.geopackage.GeoPackageException r6 = new mil.nga.geopackage.GeoPackageException
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r0 = "Failed to import GeoPackage database: "
            r7.append(r0)
            r7.append(r4)
            java.lang.String r4 = r7.toString()
            r6.<init>(r4, r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.factory.GeoPackageManagerImpl.importGeoPackage(java.lang.String, boolean, java.io.InputStream, mil.nga.geopackage.io.GeoPackageProgress):boolean");
    }

    private List<GeoPackageMetadata> getExternalGeoPackages() {
        GeoPackageMetadataDb geoPackageMetadataDb = new GeoPackageMetadataDb(this.context);
        geoPackageMetadataDb.open();
        try {
            return new GeoPackageMetadataDataSource(geoPackageMetadataDb).getAllExternal();
        } finally {
            geoPackageMetadataDb.close();
        }
    }

    private GeoPackageMetadata getGeoPackageMetadata(String str) {
        GeoPackageMetadataDb geoPackageMetadataDb = new GeoPackageMetadataDb(this.context);
        geoPackageMetadataDb.open();
        try {
            return new GeoPackageMetadataDataSource(geoPackageMetadataDb).get(str);
        } finally {
            geoPackageMetadataDb.close();
        }
    }

    private GeoPackageMetadata getGeoPackageMetadataAtExternalPath(String str) {
        GeoPackageMetadataDb geoPackageMetadataDb = new GeoPackageMetadataDb(this.context);
        geoPackageMetadataDb.open();
        try {
            return new GeoPackageMetadataDataSource(geoPackageMetadataDb).getExternalAtPath(str);
        } finally {
            geoPackageMetadataDb.close();
        }
    }

    private boolean isTemporary(String str) {
        return str.endsWith(this.context.getString(C1157R.string.geopackage_db_rollback_suffix));
    }
}
