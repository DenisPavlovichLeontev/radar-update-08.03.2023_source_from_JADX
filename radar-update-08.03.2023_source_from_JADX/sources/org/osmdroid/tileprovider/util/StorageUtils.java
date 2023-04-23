package org.osmdroid.tileprovider.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class StorageUtils {
    public static final String EXTERNAL_SD_CARD = "externalSdCard";
    public static final String SD_CARD = "sdCard";
    private static final String TAG = "StorageUtils";

    public static class StorageInfo {
        public String displayName;
        public final int display_number;
        public long freeSpace = 0;
        public final boolean internal;
        public final String path;
        public boolean readonly;

        public StorageInfo(String str, boolean z, boolean z2, int i) {
            this.path = str;
            this.internal = z;
            this.display_number = i;
            if (Build.VERSION.SDK_INT >= 18) {
                this.freeSpace = new StatFs(str).getAvailableBytes();
            } else if (Build.VERSION.SDK_INT >= 9) {
                this.freeSpace = new File(str).getFreeSpace();
            }
            if (!z2) {
                this.readonly = !StorageUtils.isWritable(new File(str));
            }
            StringBuilder sb = new StringBuilder();
            if (z) {
                sb.append("Internal SD card");
            } else if (i > 1) {
                sb.append("SD card ");
                sb.append(i);
            } else {
                sb.append("SD card");
            }
            if (z2) {
                sb.append(" (Read only)");
            }
            this.displayName = sb.toString();
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public void setDisplayName(String str) {
            this.displayName = str;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            StorageInfo storageInfo = (StorageInfo) obj;
            if (this.internal != storageInfo.internal || this.readonly != storageInfo.readonly || this.display_number != storageInfo.display_number || this.freeSpace != storageInfo.freeSpace) {
                return false;
            }
            String str = this.path;
            if (str == null ? storageInfo.path != null : !str.equals(storageInfo.path)) {
                return false;
            }
            String str2 = this.displayName;
            String str3 = storageInfo.displayName;
            if (str2 != null) {
                return str2.equals(str3);
            }
            if (str3 == null) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            String str = this.path;
            int i = 0;
            int hashCode = str != null ? str.hashCode() : 0;
            long j = this.freeSpace;
            int i2 = ((((((((hashCode * 31) + (this.internal ? 1 : 0)) * 31) + (this.readonly ? 1 : 0)) * 31) + this.display_number) * 31) + ((int) (j ^ (j >>> 32)))) * 31;
            String str2 = this.displayName;
            if (str2 != null) {
                i = str2.hashCode();
            }
            return i2 + i;
        }
    }

    public static List<StorageInfo> getStorageList() {
        return getStorageList((Context) null);
    }

    public static List<StorageInfo> getStorageList(Context context) {
        List<StorageInfo> list;
        if (Build.VERSION.SDK_INT < 29) {
            if (Build.VERSION.SDK_INT >= 19) {
                list = getStorageListPreApi19();
                if (context != null) {
                    List<StorageInfo> storageListApi19 = getStorageListApi19(context);
                    storageListApi19.removeAll(list);
                    list.addAll(storageListApi19);
                }
            } else {
                list = getStorageListPreApi19();
                if (list.size() == 0 && context != null) {
                    String replace = context.getDatabasePath("temp.sqlite").getAbsolutePath().replace("temp.sqlite", "");
                    if (isWritable(new File(replace))) {
                        list.add(new StorageInfo(replace, true, false, -1));
                    }
                }
            }
            return list;
        } else if (context != null) {
            return getStorageListApi19(context);
        } else {
            return getStorageListPreApi19();
        }
    }

    private static List<StorageInfo> getStorageListPreApi19() {
        boolean z;
        ArrayList arrayList = new ArrayList();
        StorageInfo primarySharedStorage = getPrimarySharedStorage();
        if (primarySharedStorage != null) {
            arrayList.add(primarySharedStorage);
        }
        arrayList.addAll(tryToFindOtherVoIdManagedStorages(primarySharedStorage != null ? primarySharedStorage.path : ""));
        for (File next : getAllWritableStorageLocations()) {
            Iterator it = arrayList.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (((StorageInfo) it.next()).path.equals(next.getAbsolutePath())) {
                        z = true;
                        break;
                    }
                } else {
                    z = false;
                    break;
                }
            }
            if (!z) {
                arrayList.add(new StorageInfo(next.getAbsolutePath(), false, false, -1));
            }
        }
        return arrayList;
    }

    private static List<StorageInfo> getStorageListApi19(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new StorageInfo(context.getFilesDir().getAbsolutePath(), true, false, -1));
        ArrayList arrayList2 = new ArrayList();
        for (File file : context.getExternalFilesDirs((String) null)) {
            if (file != null && "mounted".equals(Environment.getStorageState(file))) {
                arrayList2.add(file);
            }
        }
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            arrayList.add(new StorageInfo(((File) it.next()).getAbsolutePath(), false, false, -1));
        }
        return arrayList;
    }

    @Deprecated
    public static File getStorage() {
        return getStorage((Context) null);
    }

    public static StorageInfo getBestWritableStorage() {
        return getBestWritableStorage((Context) null);
    }

    @Deprecated
    public static File getStorage(Context context) {
        StorageInfo bestWritableStorage = getBestWritableStorage(context);
        if (bestWritableStorage != null) {
            return new File(bestWritableStorage.path);
        }
        return null;
    }

    public static StorageInfo getBestWritableStorage(Context context) {
        List<StorageInfo> storageList = getStorageList(context);
        StorageInfo storageInfo = null;
        for (int i = 0; i < storageList.size(); i++) {
            StorageInfo storageInfo2 = storageList.get(i);
            if (!storageInfo2.readonly && isWritable(new File(storageInfo2.path)) && (storageInfo == null || storageInfo.freeSpace < storageInfo2.freeSpace)) {
                storageInfo = storageInfo2;
            }
        }
        return storageInfo;
    }

    @Deprecated
    public static boolean isAvailable() {
        return isPrimarySharedStorageAvailable();
    }

    private static boolean isPrimarySharedStorageAvailable() {
        String externalStorageState = Environment.getExternalStorageState();
        return "mounted".equals(externalStorageState) || "mounted_ro".equals(externalStorageState);
    }

    @Deprecated
    public static String getSdCardPath() {
        return Environment.getExternalStorageDirectory().getPath() + "/";
    }

    @Deprecated
    public static boolean isWritable() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static boolean isWritable(File file) {
        try {
            File file2 = new File(file.getAbsolutePath() + File.separator + UUID.randomUUID().toString());
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            fileOutputStream.write("hi".getBytes());
            fileOutputStream.close();
            file2.delete();
            Log.i(TAG, file.getAbsolutePath() + " is writable");
            return true;
        } catch (Throwable unused) {
            Log.i(TAG, file.getAbsolutePath() + " is NOT writable");
            return false;
        }
    }

    public static Map<String, File> getAllStorageLocations() {
        HashMap hashMap = new HashMap(10);
        hashMap.putAll(tryToGetMountedStoragesFromFilesystem());
        if (!hashMap.containsValue(Environment.getExternalStorageDirectory())) {
            hashMap.put(SD_CARD, Environment.getExternalStorageDirectory());
        }
        for (File next : tryToGetStorageFromSystemEnv()) {
            if (next.exists() && !hashMap.containsValue(next)) {
                hashMap.put(SD_CARD, next);
            }
        }
        return hashMap;
    }

    private static Set<File> getAllWritableStorageLocations() {
        HashSet hashSet = new HashSet();
        for (File next : tryToGetStorageFromSystemEnv()) {
            if (isWritable(next)) {
                hashSet.add(next);
            }
        }
        if (Environment.getExternalStorageDirectory() != null) {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            if (isWritable(externalStorageDirectory)) {
                hashSet.add(externalStorageDirectory);
            }
        }
        for (File next2 : tryToGetMountedStoragesFromFilesystem().values()) {
            if (isWritable(next2)) {
                hashSet.add(next2);
            }
        }
        return hashSet;
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0043  */
    /* JADX WARNING: Removed duplicated region for block: B:28:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static org.osmdroid.tileprovider.util.StorageUtils.StorageInfo getPrimarySharedStorage() {
        /*
            java.lang.String r0 = ""
            java.io.File r1 = android.os.Environment.getExternalStorageDirectory()     // Catch:{ all -> 0x0011 }
            if (r1 == 0) goto L_0x0015
            java.io.File r1 = android.os.Environment.getExternalStorageDirectory()     // Catch:{ all -> 0x0011 }
            java.lang.String r0 = r1.getPath()     // Catch:{ all -> 0x0011 }
            goto L_0x0015
        L_0x0011:
            r1 = move-exception
            r1.printStackTrace()
        L_0x0015:
            r1 = 1
            r2 = 0
            int r3 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x0023 }
            r4 = 9
            if (r3 < r4) goto L_0x0027
            boolean r3 = android.os.Environment.isExternalStorageRemovable()     // Catch:{ all -> 0x0023 }
            r3 = r3 ^ r1
            goto L_0x0028
        L_0x0023:
            r3 = move-exception
            r3.printStackTrace()
        L_0x0027:
            r3 = r2
        L_0x0028:
            boolean r2 = isPrimarySharedStorageAvailable()     // Catch:{ all -> 0x002d }
            goto L_0x0031
        L_0x002d:
            r4 = move-exception
            r4.printStackTrace()
        L_0x0031:
            java.lang.String r4 = android.os.Environment.getExternalStorageState()     // Catch:{ all -> 0x003c }
            java.lang.String r5 = "mounted_ro"
            boolean r1 = r4.equals(r5)     // Catch:{ all -> 0x003c }
            goto L_0x0040
        L_0x003c:
            r4 = move-exception
            r4.printStackTrace()
        L_0x0040:
            r4 = 0
            if (r2 == 0) goto L_0x0049
            org.osmdroid.tileprovider.util.StorageUtils$StorageInfo r4 = new org.osmdroid.tileprovider.util.StorageUtils$StorageInfo
            r2 = -1
            r4.<init>(r0, r3, r1, r2)
        L_0x0049:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.util.StorageUtils.getPrimarySharedStorage():org.osmdroid.tileprovider.util.StorageUtils$StorageInfo");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00dc, code lost:
        if (r3 != null) goto L_0x00de;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:?, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00e6, code lost:
        if (r3 != null) goto L_0x00de;
     */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00ec A[SYNTHETIC, Splitter:B:55:0x00ec] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:49:0x00e3=Splitter:B:49:0x00e3, B:43:0x00d9=Splitter:B:43:0x00d9} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.util.List<org.osmdroid.tileprovider.util.StorageUtils.StorageInfo> tryToFindOtherVoIdManagedStorages(java.lang.String r10) {
        /*
            java.lang.String r0 = "StorageUtils"
            java.lang.String r1 = "/proc/mounts"
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            r3 = 0
            java.util.HashSet r4 = new java.util.HashSet     // Catch:{ FileNotFoundException -> 0x00e2, IOException -> 0x00d8 }
            r4.<init>()     // Catch:{ FileNotFoundException -> 0x00e2, IOException -> 0x00d8 }
            java.io.BufferedReader r5 = new java.io.BufferedReader     // Catch:{ FileNotFoundException -> 0x00e2, IOException -> 0x00d8 }
            java.io.FileReader r6 = new java.io.FileReader     // Catch:{ FileNotFoundException -> 0x00e2, IOException -> 0x00d8 }
            r6.<init>(r1)     // Catch:{ FileNotFoundException -> 0x00e2, IOException -> 0x00d8 }
            r5.<init>(r6)     // Catch:{ FileNotFoundException -> 0x00e2, IOException -> 0x00d8 }
            android.util.Log.d(r0, r1)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            r1 = 1
        L_0x001d:
            java.lang.String r3 = r5.readLine()     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            if (r3 == 0) goto L_0x00c9
            android.util.Log.d(r0, r3)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            java.lang.String r6 = "vfat"
            boolean r6 = r3.contains(r6)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            if (r6 != 0) goto L_0x0036
            java.lang.String r6 = "/mnt"
            boolean r6 = r3.contains(r6)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            if (r6 == 0) goto L_0x001d
        L_0x0036:
            java.util.StringTokenizer r6 = new java.util.StringTokenizer     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            java.lang.String r7 = " "
            r6.<init>(r3, r7)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            r6.nextToken()     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            java.lang.String r7 = r6.nextToken()     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            boolean r8 = r4.contains(r7)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            if (r8 == 0) goto L_0x004b
            goto L_0x001d
        L_0x004b:
            r6.nextToken()     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            java.lang.String r6 = r6.nextToken()     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            java.lang.String r8 = ","
            java.lang.String[] r6 = r6.split(r8)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            java.util.List r6 = java.util.Arrays.asList(r6)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            java.lang.String r8 = "ro"
            boolean r6 = r6.contains(r8)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            boolean r8 = r7.equals(r10)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            if (r8 == 0) goto L_0x006c
            r4.add(r10)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            goto L_0x001d
        L_0x006c:
            java.lang.String r8 = "/dev/block/vold"
            boolean r8 = r3.contains(r8)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            if (r8 == 0) goto L_0x001d
            java.lang.String r8 = "/mnt/secure"
            boolean r8 = r3.contains(r8)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            if (r8 != 0) goto L_0x001d
            java.lang.String r8 = "/mnt/asec"
            boolean r8 = r3.contains(r8)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            if (r8 != 0) goto L_0x001d
            java.lang.String r8 = "/mnt/obb"
            boolean r8 = r3.contains(r8)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            if (r8 != 0) goto L_0x001d
            java.lang.String r8 = "/dev/mapper"
            boolean r8 = r3.contains(r8)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            if (r8 != 0) goto L_0x001d
            java.lang.String r8 = "tmpfs"
            boolean r3 = r3.contains(r8)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            if (r3 != 0) goto L_0x001d
            r4.add(r7)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            java.io.File r3 = new java.io.File     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            r8.<init>()     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            r8.append(r7)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            java.lang.String r9 = java.io.File.separator     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            r8.append(r9)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            java.lang.String r8 = r8.toString()     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            r3.<init>(r8)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            boolean r3 = r3.exists()     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            if (r3 == 0) goto L_0x001d
            org.osmdroid.tileprovider.util.StorageUtils$StorageInfo r3 = new org.osmdroid.tileprovider.util.StorageUtils$StorageInfo     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            r8 = 0
            int r9 = r1 + 1
            r3.<init>(r7, r8, r6, r1)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            r2.add(r3)     // Catch:{ FileNotFoundException -> 0x00d3, IOException -> 0x00d0, all -> 0x00cd }
            r1 = r9
            goto L_0x001d
        L_0x00c9:
            r5.close()     // Catch:{ IOException -> 0x00e9 }
            goto L_0x00e9
        L_0x00cd:
            r10 = move-exception
            r3 = r5
            goto L_0x00ea
        L_0x00d0:
            r10 = move-exception
            r3 = r5
            goto L_0x00d9
        L_0x00d3:
            r10 = move-exception
            r3 = r5
            goto L_0x00e3
        L_0x00d6:
            r10 = move-exception
            goto L_0x00ea
        L_0x00d8:
            r10 = move-exception
        L_0x00d9:
            r10.printStackTrace()     // Catch:{ all -> 0x00d6 }
            if (r3 == 0) goto L_0x00e9
        L_0x00de:
            r3.close()     // Catch:{ IOException -> 0x00e9 }
            goto L_0x00e9
        L_0x00e2:
            r10 = move-exception
        L_0x00e3:
            r10.printStackTrace()     // Catch:{ all -> 0x00d6 }
            if (r3 == 0) goto L_0x00e9
            goto L_0x00de
        L_0x00e9:
            return r2
        L_0x00ea:
            if (r3 == 0) goto L_0x00ef
            r3.close()     // Catch:{ IOException -> 0x00ef }
        L_0x00ef:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.util.StorageUtils.tryToFindOtherVoIdManagedStorages(java.lang.String):java.util.List");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0056, code lost:
        if (r10 != null) goto L_0x0058;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        r10.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0064, code lost:
        if (r10 != null) goto L_0x0058;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00b4, code lost:
        if (r7 != null) goto L_0x00b6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:?, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00c1, code lost:
        if (r7 != null) goto L_0x00b6;
     */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x0182 A[SYNTHETIC, Splitter:B:87:0x0182] */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x018a A[SYNTHETIC, Splitter:B:94:0x018a] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.util.Map<java.lang.String, java.io.File> tryToGetMountedStoragesFromFilesystem() {
        /*
            java.lang.String r0 = " "
            java.lang.String r1 = ":"
            java.util.HashMap r2 = new java.util.HashMap
            r2.<init>()
            java.util.ArrayList r3 = new java.util.ArrayList
            r4 = 10
            r3.<init>(r4)
            java.util.ArrayList r5 = new java.util.ArrayList
            r5.<init>(r4)
            java.lang.String r6 = "/mnt/sdcard"
            r3.add(r6)
            r5.add(r6)
            r7 = 0
            r8 = 1
            java.io.File r9 = new java.io.File     // Catch:{ Exception -> 0x005f, all -> 0x005c }
            java.lang.String r10 = "/proc/mounts"
            r9.<init>(r10)     // Catch:{ Exception -> 0x005f, all -> 0x005c }
            boolean r10 = r9.exists()     // Catch:{ Exception -> 0x005f, all -> 0x005c }
            if (r10 == 0) goto L_0x0055
            java.util.Scanner r10 = new java.util.Scanner     // Catch:{ Exception -> 0x005f, all -> 0x005c }
            r10.<init>(r9)     // Catch:{ Exception -> 0x005f, all -> 0x005c }
        L_0x0031:
            boolean r9 = r10.hasNext()     // Catch:{ Exception -> 0x0053 }
            if (r9 == 0) goto L_0x0056
            java.lang.String r9 = r10.nextLine()     // Catch:{ Exception -> 0x0053 }
            java.lang.String r11 = "/dev/block/vold/"
            boolean r11 = r9.startsWith(r11)     // Catch:{ Exception -> 0x0053 }
            if (r11 == 0) goto L_0x0031
            java.lang.String[] r9 = r9.split(r0)     // Catch:{ Exception -> 0x0053 }
            r9 = r9[r8]     // Catch:{ Exception -> 0x0053 }
            boolean r11 = r9.equals(r6)     // Catch:{ Exception -> 0x0053 }
            if (r11 != 0) goto L_0x0031
            r3.add(r9)     // Catch:{ Exception -> 0x0053 }
            goto L_0x0031
        L_0x0053:
            r9 = move-exception
            goto L_0x0061
        L_0x0055:
            r10 = r7
        L_0x0056:
            if (r10 == 0) goto L_0x0067
        L_0x0058:
            r10.close()     // Catch:{ Exception -> 0x0067 }
            goto L_0x0067
        L_0x005c:
            r0 = move-exception
            goto L_0x0188
        L_0x005f:
            r9 = move-exception
            r10 = r7
        L_0x0061:
            r9.printStackTrace()     // Catch:{ all -> 0x0186 }
            if (r10 == 0) goto L_0x0067
            goto L_0x0058
        L_0x0067:
            r9 = 0
            java.io.File r10 = new java.io.File     // Catch:{ Exception -> 0x00bd }
            java.lang.String r11 = "/system/etc/vold.fstab"
            r10.<init>(r11)     // Catch:{ Exception -> 0x00bd }
            boolean r11 = r10.exists()     // Catch:{ Exception -> 0x00bd }
            if (r11 == 0) goto L_0x00b4
            java.util.Scanner r11 = new java.util.Scanner     // Catch:{ Exception -> 0x00bd }
            r11.<init>(r10)     // Catch:{ Exception -> 0x00bd }
        L_0x007a:
            boolean r7 = r11.hasNext()     // Catch:{ Exception -> 0x00b1, all -> 0x00ad }
            if (r7 == 0) goto L_0x00ab
            java.lang.String r7 = r11.nextLine()     // Catch:{ Exception -> 0x00b1, all -> 0x00ad }
            java.lang.String r10 = "dev_mount"
            boolean r10 = r7.startsWith(r10)     // Catch:{ Exception -> 0x00b1, all -> 0x00ad }
            if (r10 == 0) goto L_0x007a
            java.lang.String[] r7 = r7.split(r0)     // Catch:{ Exception -> 0x00b1, all -> 0x00ad }
            r10 = 2
            r7 = r7[r10]     // Catch:{ Exception -> 0x00b1, all -> 0x00ad }
            boolean r10 = r7.contains(r1)     // Catch:{ Exception -> 0x00b1, all -> 0x00ad }
            if (r10 == 0) goto L_0x00a1
            int r10 = r7.indexOf(r1)     // Catch:{ Exception -> 0x00b1, all -> 0x00ad }
            java.lang.String r7 = r7.substring(r9, r10)     // Catch:{ Exception -> 0x00b1, all -> 0x00ad }
        L_0x00a1:
            boolean r10 = r7.equals(r6)     // Catch:{ Exception -> 0x00b1, all -> 0x00ad }
            if (r10 != 0) goto L_0x007a
            r5.add(r7)     // Catch:{ Exception -> 0x00b1, all -> 0x00ad }
            goto L_0x007a
        L_0x00ab:
            r7 = r11
            goto L_0x00b4
        L_0x00ad:
            r0 = move-exception
            r7 = r11
            goto L_0x0180
        L_0x00b1:
            r0 = move-exception
            r7 = r11
            goto L_0x00be
        L_0x00b4:
            if (r7 == 0) goto L_0x00c4
        L_0x00b6:
            r7.close()     // Catch:{ Exception -> 0x00c4 }
            goto L_0x00c4
        L_0x00ba:
            r0 = move-exception
            goto L_0x0180
        L_0x00bd:
            r0 = move-exception
        L_0x00be:
            r0.printStackTrace()     // Catch:{ all -> 0x00ba }
            if (r7 == 0) goto L_0x00c4
            goto L_0x00b6
        L_0x00c4:
            r0 = r9
        L_0x00c5:
            int r6 = r3.size()
            if (r0 >= r6) goto L_0x00df
            java.lang.Object r6 = r3.get(r0)
            java.lang.String r6 = (java.lang.String) r6
            boolean r6 = r5.contains(r6)
            if (r6 != 0) goto L_0x00dd
            int r6 = r0 + -1
            r3.remove(r0)
            r0 = r6
        L_0x00dd:
            int r0 = r0 + r8
            goto L_0x00c5
        L_0x00df:
            r5.clear()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>(r4)
            java.util.Iterator r3 = r3.iterator()
        L_0x00eb:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L_0x017f
            java.lang.Object r4 = r3.next()
            java.lang.String r4 = (java.lang.String) r4
            java.io.File r5 = new java.io.File
            r5.<init>(r4)
            boolean r4 = r5.exists()
            if (r4 == 0) goto L_0x00eb
            boolean r4 = r5.isDirectory()
            if (r4 == 0) goto L_0x00eb
            boolean r4 = r5.canWrite()
            if (r4 == 0) goto L_0x00eb
            java.io.File[] r4 = r5.listFiles()
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            java.lang.String r7 = "["
            r6.<init>(r7)
            if (r4 == 0) goto L_0x013e
            int r7 = r4.length
            r10 = r9
        L_0x011d:
            if (r10 >= r7) goto L_0x013e
            r11 = r4[r10]
            java.lang.String r12 = r11.getName()
            int r12 = r12.hashCode()
            r6.append(r12)
            r6.append(r1)
            long r11 = r11.length()
            r6.append(r11)
            java.lang.String r11 = ", "
            r6.append(r11)
            int r10 = r10 + 1
            goto L_0x011d
        L_0x013e:
            java.lang.String r4 = "]"
            r6.append(r4)
            java.lang.String r4 = r6.toString()
            boolean r4 = r0.contains(r4)
            if (r4 != 0) goto L_0x00eb
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r7 = "sdCard_"
            r4.append(r7)
            int r7 = r2.size()
            r4.append(r7)
            java.lang.String r4 = r4.toString()
            int r7 = r2.size()
            if (r7 != 0) goto L_0x016b
            java.lang.String r4 = "sdCard"
            goto L_0x0173
        L_0x016b:
            int r7 = r2.size()
            if (r7 != r8) goto L_0x0173
            java.lang.String r4 = "externalSdCard"
        L_0x0173:
            java.lang.String r6 = r6.toString()
            r0.add(r6)
            r2.put(r4, r5)
            goto L_0x00eb
        L_0x017f:
            return r2
        L_0x0180:
            if (r7 == 0) goto L_0x0185
            r7.close()     // Catch:{ Exception -> 0x0185 }
        L_0x0185:
            throw r0
        L_0x0186:
            r0 = move-exception
            r7 = r10
        L_0x0188:
            if (r7 == 0) goto L_0x018d
            r7.close()     // Catch:{ Exception -> 0x018d }
        L_0x018d:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.util.StorageUtils.tryToGetMountedStoragesFromFilesystem():java.util.Map");
    }

    private static Set<File> tryToGetStorageFromSystemEnv() {
        HashSet hashSet = new HashSet();
        String str = System.getenv("EXTERNAL_STORAGE");
        if (str != null) {
            hashSet.add(new File(str + File.separator));
        }
        String str2 = System.getenv("SECONDARY_STORAGE");
        if (str2 != null) {
            for (String str3 : str2.split(File.pathSeparator)) {
                hashSet.add(new File(str3 + File.separator));
            }
        }
        return hashSet;
    }
}
