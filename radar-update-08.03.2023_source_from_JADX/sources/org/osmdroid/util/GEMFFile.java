package org.osmdroid.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class GEMFFile {
    private static final int FILE_COPY_BUFFER_SIZE = 1024;
    private static final long FILE_SIZE_LIMIT = 1073741824;
    private static final int TILE_SIZE = 256;
    private static final int U32_SIZE = 4;
    private static final int U64_SIZE = 8;
    private static final int VERSION = 4;
    private int mCurrentSource;
    private final List<String> mFileNames;
    private final List<Long> mFileSizes;
    private final List<RandomAccessFile> mFiles;
    private final String mLocation;
    private final List<GEMFRange> mRangeData;
    private boolean mSourceLimited;
    private final LinkedHashMap<Integer, String> mSources;

    public GEMFFile(File file) throws FileNotFoundException, IOException {
        this(file.getAbsolutePath());
    }

    public GEMFFile(String str) throws FileNotFoundException, IOException {
        this.mFiles = new ArrayList();
        this.mFileNames = new ArrayList();
        this.mRangeData = new ArrayList();
        this.mFileSizes = new ArrayList();
        this.mSources = new LinkedHashMap<>();
        this.mSourceLimited = false;
        this.mCurrentSource = 0;
        this.mLocation = str;
        openFiles();
        readHeader();
    }

    public GEMFFile(String str, List<File> list) throws FileNotFoundException, IOException {
        Iterator it;
        Iterator<File> it2;
        int i;
        File[] fileArr;
        Iterator<File> it3;
        int i2;
        File[] fileArr2;
        int i3;
        File[] fileArr3;
        int i4;
        File[] fileArr4;
        this.mFiles = new ArrayList();
        this.mFileNames = new ArrayList();
        this.mRangeData = new ArrayList();
        this.mFileSizes = new ArrayList();
        this.mSources = new LinkedHashMap<>();
        int i5 = 0;
        this.mSourceLimited = false;
        this.mCurrentSource = 0;
        this.mLocation = str;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        Iterator<File> it4 = list.iterator();
        while (it4.hasNext()) {
            File next = it4.next();
            LinkedHashMap linkedHashMap2 = new LinkedHashMap();
            File[] listFiles = next.listFiles();
            int length = listFiles.length;
            int i6 = i5;
            while (i6 < length) {
                File file = listFiles[i6];
                try {
                    Integer.parseInt(file.getName());
                    LinkedHashMap linkedHashMap3 = new LinkedHashMap();
                    File[] listFiles2 = file.listFiles();
                    int length2 = listFiles2.length;
                    int i7 = i5;
                    while (i7 < length2) {
                        File file2 = listFiles2[i7];
                        try {
                            Integer.parseInt(file2.getName());
                            LinkedHashMap linkedHashMap4 = new LinkedHashMap();
                            it3 = it4;
                            File[] listFiles3 = file2.listFiles();
                            fileArr3 = listFiles;
                            int length3 = listFiles3.length;
                            i3 = length;
                            int i8 = 0;
                            while (i8 < length3) {
                                int i9 = length3;
                                File file3 = listFiles3[i8];
                                File[] fileArr5 = listFiles3;
                                try {
                                    fileArr4 = listFiles2;
                                    try {
                                        i4 = length2;
                                        try {
                                            Integer.parseInt(file3.getName().substring(0, file3.getName().indexOf(46)));
                                            linkedHashMap4.put(Integer.valueOf(Integer.parseInt(file3.getName().substring(0, file3.getName().indexOf(46)))), file3);
                                        } catch (NumberFormatException unused) {
                                        }
                                    } catch (NumberFormatException unused2) {
                                        i4 = length2;
                                        i8++;
                                        String str2 = str;
                                        length3 = i9;
                                        listFiles3 = fileArr5;
                                        listFiles2 = fileArr4;
                                        length2 = i4;
                                    }
                                } catch (NumberFormatException unused3) {
                                    fileArr4 = listFiles2;
                                    i4 = length2;
                                    i8++;
                                    String str22 = str;
                                    length3 = i9;
                                    listFiles3 = fileArr5;
                                    listFiles2 = fileArr4;
                                    length2 = i4;
                                }
                                i8++;
                                String str222 = str;
                                length3 = i9;
                                listFiles3 = fileArr5;
                                listFiles2 = fileArr4;
                                length2 = i4;
                            }
                            fileArr2 = listFiles2;
                            i2 = length2;
                            linkedHashMap3.put(new Integer(file2.getName()), linkedHashMap4);
                        } catch (NumberFormatException unused4) {
                            it3 = it4;
                            fileArr3 = listFiles;
                            i3 = length;
                            fileArr2 = listFiles2;
                            i2 = length2;
                        }
                        i7++;
                        String str3 = str;
                        it4 = it3;
                        listFiles = fileArr3;
                        length = i3;
                        listFiles2 = fileArr2;
                        length2 = i2;
                    }
                    it2 = it4;
                    fileArr = listFiles;
                    i = length;
                    linkedHashMap2.put(Integer.valueOf(Integer.parseInt(file.getName())), linkedHashMap3);
                } catch (NumberFormatException unused5) {
                    it2 = it4;
                    fileArr = listFiles;
                    i = length;
                }
                i6++;
                String str4 = str;
                it4 = it2;
                listFiles = fileArr;
                length = i;
                i5 = 0;
            }
            Iterator<File> it5 = it4;
            linkedHashMap.put(next.getName(), linkedHashMap2);
            String str5 = str;
            i5 = 0;
        }
        LinkedHashMap linkedHashMap5 = new LinkedHashMap();
        LinkedHashMap linkedHashMap6 = new LinkedHashMap();
        int i10 = 0;
        for (String str6 : linkedHashMap.keySet()) {
            linkedHashMap5.put(str6, new Integer(i10));
            linkedHashMap6.put(new Integer(i10), str6);
            i10++;
        }
        ArrayList<GEMFRange> arrayList = new ArrayList<>();
        Iterator it6 = linkedHashMap.keySet().iterator();
        while (it6.hasNext()) {
            String str7 = (String) it6.next();
            for (Integer num : ((LinkedHashMap) linkedHashMap.get(str7)).keySet()) {
                LinkedHashMap linkedHashMap7 = new LinkedHashMap();
                Iterator it7 = new TreeSet(((LinkedHashMap) ((LinkedHashMap) linkedHashMap.get(str7)).get(num)).keySet()).iterator();
                while (it7.hasNext()) {
                    Integer num2 = (Integer) it7.next();
                    ArrayList arrayList2 = new ArrayList();
                    for (Integer add : ((LinkedHashMap) ((LinkedHashMap) ((LinkedHashMap) linkedHashMap.get(str7)).get(num)).get(num2)).keySet()) {
                        arrayList2.add(add);
                    }
                    if (arrayList2.size() != 0) {
                        Collections.sort(arrayList2);
                        if (!linkedHashMap7.containsKey(arrayList2)) {
                            linkedHashMap7.put(arrayList2, new ArrayList());
                        }
                        ((List) linkedHashMap7.get(arrayList2)).add(num2);
                    }
                }
                LinkedHashMap linkedHashMap8 = new LinkedHashMap();
                for (List list2 : linkedHashMap7.keySet()) {
                    TreeSet treeSet = new TreeSet((Collection) linkedHashMap7.get(list2));
                    ArrayList arrayList3 = new ArrayList();
                    int intValue = ((Integer) treeSet.first()).intValue();
                    while (true) {
                        it = it6;
                        if (intValue >= ((Integer) treeSet.last()).intValue() + 1) {
                            break;
                        }
                        if (treeSet.contains(new Integer(intValue))) {
                            arrayList3.add(new Integer(intValue));
                        } else if (arrayList3.size() > 0) {
                            linkedHashMap8.put(list2, arrayList3);
                            arrayList3 = new ArrayList();
                        }
                        intValue++;
                        it6 = it;
                    }
                    if (arrayList3.size() > 0) {
                        linkedHashMap8.put(list2, arrayList3);
                    }
                    it6 = it;
                }
                Iterator it8 = it6;
                for (List list3 : linkedHashMap8.keySet()) {
                    TreeSet treeSet2 = new TreeSet(list3);
                    TreeSet treeSet3 = new TreeSet((Collection) linkedHashMap7.get(list3));
                    GEMFRange gEMFRange = new GEMFRange();
                    gEMFRange.zoom = num;
                    gEMFRange.sourceIndex = (Integer) linkedHashMap5.get(str7);
                    gEMFRange.xMin = (Integer) treeSet3.first();
                    gEMFRange.xMax = (Integer) treeSet3.last();
                    for (int intValue2 = ((Integer) treeSet2.first()).intValue(); intValue2 < ((Integer) treeSet2.last()).intValue() + 1; intValue2++) {
                        if (treeSet2.contains(new Integer(intValue2))) {
                            if (gEMFRange.yMin == null) {
                                gEMFRange.yMin = Integer.valueOf(intValue2);
                            }
                            gEMFRange.yMax = Integer.valueOf(intValue2);
                        } else if (gEMFRange.yMin != null) {
                            arrayList.add(gEMFRange);
                            gEMFRange = new GEMFRange();
                            gEMFRange.zoom = num;
                            gEMFRange.sourceIndex = (Integer) linkedHashMap5.get(str7);
                            gEMFRange.xMin = (Integer) treeSet3.first();
                            gEMFRange.xMax = (Integer) treeSet3.last();
                        }
                    }
                    if (gEMFRange.yMin != null) {
                        arrayList.add(gEMFRange);
                    }
                }
                it6 = it8;
            }
        }
        int i11 = 0;
        for (String length4 : linkedHashMap5.keySet()) {
            i11 += length4.length() + 8;
        }
        long size = (long) (i11 + 12 + (arrayList.size() * 32) + 4);
        for (GEMFRange gEMFRange2 : arrayList) {
            gEMFRange2.offset = Long.valueOf(size);
            for (int intValue3 = gEMFRange2.xMin.intValue(); intValue3 < gEMFRange2.xMax.intValue() + 1; intValue3++) {
                for (int intValue4 = gEMFRange2.yMin.intValue(); intValue4 < gEMFRange2.yMax.intValue() + 1; intValue4++) {
                    size += 12;
                }
            }
        }
        String str8 = str;
        RandomAccessFile randomAccessFile = new RandomAccessFile(str8, "rw");
        randomAccessFile.writeInt(4);
        randomAccessFile.writeInt(256);
        randomAccessFile.writeInt(linkedHashMap5.size());
        for (String str9 : linkedHashMap5.keySet()) {
            randomAccessFile.writeInt(((Integer) linkedHashMap5.get(str9)).intValue());
            randomAccessFile.writeInt(str9.length());
            randomAccessFile.write(str9.getBytes());
        }
        randomAccessFile.writeInt(arrayList.size());
        for (GEMFRange gEMFRange3 : arrayList) {
            randomAccessFile.writeInt(gEMFRange3.zoom.intValue());
            randomAccessFile.writeInt(gEMFRange3.xMin.intValue());
            randomAccessFile.writeInt(gEMFRange3.xMax.intValue());
            randomAccessFile.writeInt(gEMFRange3.yMin.intValue());
            randomAccessFile.writeInt(gEMFRange3.yMax.intValue());
            randomAccessFile.writeInt(gEMFRange3.sourceIndex.intValue());
            randomAccessFile.writeLong(gEMFRange3.offset.longValue());
        }
        Iterator it9 = arrayList.iterator();
        long j = size;
        while (it9.hasNext()) {
            GEMFRange gEMFRange4 = (GEMFRange) it9.next();
            int intValue5 = gEMFRange4.xMin.intValue();
            while (intValue5 < gEMFRange4.xMax.intValue() + 1) {
                int intValue6 = gEMFRange4.yMin.intValue();
                while (intValue6 < gEMFRange4.yMax.intValue() + 1) {
                    randomAccessFile.writeLong(j);
                    long length5 = ((File) ((LinkedHashMap) ((LinkedHashMap) ((LinkedHashMap) linkedHashMap.get(linkedHashMap6.get(gEMFRange4.sourceIndex))).get(gEMFRange4.zoom)).get(Integer.valueOf(intValue5))).get(Integer.valueOf(intValue6))).length();
                    randomAccessFile.writeInt((int) length5);
                    j += length5;
                    intValue6++;
                    it9 = it9;
                }
                Iterator it10 = it9;
                intValue5++;
            }
        }
        byte[] bArr = new byte[1024];
        int i12 = 0;
        for (GEMFRange gEMFRange5 : arrayList) {
            for (int intValue7 = gEMFRange5.xMin.intValue(); intValue7 < gEMFRange5.xMax.intValue() + 1; intValue7++) {
                int intValue8 = gEMFRange5.yMin.intValue();
                while (intValue8 < gEMFRange5.yMax.intValue() + 1) {
                    size += ((File) ((LinkedHashMap) ((LinkedHashMap) ((LinkedHashMap) linkedHashMap.get(linkedHashMap6.get(gEMFRange5.sourceIndex))).get(gEMFRange5.zoom)).get(Integer.valueOf(intValue7))).get(Integer.valueOf(intValue8))).length();
                    if (size > FILE_SIZE_LIMIT) {
                        randomAccessFile.close();
                        i12++;
                        randomAccessFile = new RandomAccessFile(str8 + "-" + i12, "rw");
                        size = 0;
                    }
                    FileInputStream fileInputStream = new FileInputStream((File) ((LinkedHashMap) ((LinkedHashMap) ((LinkedHashMap) linkedHashMap.get(linkedHashMap6.get(gEMFRange5.sourceIndex))).get(gEMFRange5.zoom)).get(Integer.valueOf(intValue7))).get(Integer.valueOf(intValue8)));
                    LinkedHashMap linkedHashMap9 = linkedHashMap6;
                    for (int read = fileInputStream.read(bArr, 0, 1024); read != -1; read = fileInputStream.read(bArr, 0, 1024)) {
                        randomAccessFile.write(bArr, 0, read);
                    }
                    fileInputStream.close();
                    intValue8++;
                    linkedHashMap6 = linkedHashMap9;
                }
                LinkedHashMap linkedHashMap10 = linkedHashMap6;
            }
        }
        randomAccessFile.close();
        openFiles();
        readHeader();
    }

    public void close() throws IOException {
        for (RandomAccessFile close : this.mFiles) {
            close.close();
        }
    }

    private void openFiles() throws FileNotFoundException {
        File file = new File(this.mLocation);
        this.mFiles.add(new RandomAccessFile(file, "r"));
        this.mFileNames.add(file.getPath());
        int i = 0;
        while (true) {
            i++;
            File file2 = new File(this.mLocation + "-" + i);
            if (file2.exists()) {
                this.mFiles.add(new RandomAccessFile(file2, "r"));
                this.mFileNames.add(file2.getPath());
            } else {
                return;
            }
        }
    }

    private void readHeader() throws IOException {
        RandomAccessFile randomAccessFile = this.mFiles.get(0);
        for (RandomAccessFile length : this.mFiles) {
            this.mFileSizes.add(Long.valueOf(length.length()));
        }
        int readInt = randomAccessFile.readInt();
        if (readInt == 4) {
            int readInt2 = randomAccessFile.readInt();
            if (readInt2 == 256) {
                int readInt3 = randomAccessFile.readInt();
                for (int i = 0; i < readInt3; i++) {
                    int readInt4 = randomAccessFile.readInt();
                    int readInt5 = randomAccessFile.readInt();
                    byte[] bArr = new byte[readInt5];
                    randomAccessFile.read(bArr, 0, readInt5);
                    this.mSources.put(new Integer(readInt4), new String(bArr));
                }
                int readInt6 = randomAccessFile.readInt();
                for (int i2 = 0; i2 < readInt6; i2++) {
                    GEMFRange gEMFRange = new GEMFRange();
                    gEMFRange.zoom = Integer.valueOf(randomAccessFile.readInt());
                    gEMFRange.xMin = Integer.valueOf(randomAccessFile.readInt());
                    gEMFRange.xMax = Integer.valueOf(randomAccessFile.readInt());
                    gEMFRange.yMin = Integer.valueOf(randomAccessFile.readInt());
                    gEMFRange.yMax = Integer.valueOf(randomAccessFile.readInt());
                    gEMFRange.sourceIndex = Integer.valueOf(randomAccessFile.readInt());
                    gEMFRange.offset = Long.valueOf(randomAccessFile.readLong());
                    this.mRangeData.add(gEMFRange);
                }
                return;
            }
            throw new IOException("Bad tile size: " + readInt2);
        }
        throw new IOException("Bad file version: " + readInt);
    }

    public String getName() {
        return this.mLocation;
    }

    public LinkedHashMap<Integer, String> getSources() {
        return this.mSources;
    }

    public void selectSource(int i) {
        if (this.mSources.containsKey(new Integer(i))) {
            this.mSourceLimited = true;
            this.mCurrentSource = i;
        }
    }

    public void acceptAnySource() {
        this.mSourceLimited = false;
    }

    public Set<Integer> getZoomLevels() {
        TreeSet treeSet = new TreeSet();
        for (GEMFRange gEMFRange : this.mRangeData) {
            treeSet.add(gEMFRange.zoom);
        }
        return treeSet;
    }

    /* JADX WARNING: Removed duplicated region for block: B:107:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0143 A[SYNTHETIC, Splitter:B:67:0x0143] */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x014d A[SYNTHETIC, Splitter:B:72:0x014d] */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x015a A[SYNTHETIC, Splitter:B:79:0x015a] */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x0164 A[SYNTHETIC, Splitter:B:84:0x0164] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.io.InputStream getInputStream(int r11, int r12, int r13) {
        /*
            r10 = this;
            java.util.List<org.osmdroid.util.GEMFFile$GEMFRange> r0 = r10.mRangeData
            java.util.Iterator r0 = r0.iterator()
        L_0x0006:
            boolean r1 = r0.hasNext()
            r2 = 0
            if (r1 == 0) goto L_0x004a
            java.lang.Object r1 = r0.next()
            org.osmdroid.util.GEMFFile$GEMFRange r1 = (org.osmdroid.util.GEMFFile.GEMFRange) r1
            java.lang.Integer r3 = r1.zoom
            int r3 = r3.intValue()
            if (r13 != r3) goto L_0x0006
            java.lang.Integer r3 = r1.xMin
            int r3 = r3.intValue()
            if (r11 < r3) goto L_0x0006
            java.lang.Integer r3 = r1.xMax
            int r3 = r3.intValue()
            if (r11 > r3) goto L_0x0006
            java.lang.Integer r3 = r1.yMin
            int r3 = r3.intValue()
            if (r12 < r3) goto L_0x0006
            java.lang.Integer r3 = r1.yMax
            int r3 = r3.intValue()
            if (r12 > r3) goto L_0x0006
            boolean r3 = r10.mSourceLimited
            if (r3 == 0) goto L_0x004b
            java.lang.Integer r3 = r1.sourceIndex
            int r3 = r3.intValue()
            int r4 = r10.mCurrentSource
            if (r3 != r4) goto L_0x0006
            goto L_0x004b
        L_0x004a:
            r1 = r2
        L_0x004b:
            if (r1 != 0) goto L_0x004e
            return r2
        L_0x004e:
            java.lang.Integer r13 = r1.yMax     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            int r13 = r13.intValue()     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            int r13 = r13 + 1
            java.lang.Integer r0 = r1.yMin     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            int r0 = r0.intValue()     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            int r13 = r13 - r0
            java.lang.Integer r0 = r1.xMin     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            int r0 = r0.intValue()     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            int r11 = r11 - r0
            java.lang.Integer r0 = r1.yMin     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            int r0 = r0.intValue()     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            int r12 = r12 - r0
            int r11 = r11 * r13
            int r11 = r11 + r12
            long r11 = (long) r11     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            r3 = 12
            long r11 = r11 * r3
            java.lang.Long r13 = r1.offset     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            long r0 = r13.longValue()     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            long r11 = r11 + r0
            java.util.List<java.io.RandomAccessFile> r13 = r10.mFiles     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            r0 = 0
            java.lang.Object r13 = r13.get(r0)     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            java.io.RandomAccessFile r13 = (java.io.RandomAccessFile) r13     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            r13.seek(r11)     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            long r11 = r13.readLong()     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            int r8 = r13.readInt()     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            java.util.List<java.io.RandomAccessFile> r13 = r10.mFiles     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            java.lang.Object r13 = r13.get(r0)     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            java.io.RandomAccessFile r13 = (java.io.RandomAccessFile) r13     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            java.util.List<java.lang.Long> r1 = r10.mFileSizes     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            java.lang.Object r1 = r1.get(r0)     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            java.lang.Long r1 = (java.lang.Long) r1     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            long r3 = r1.longValue()     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            int r1 = (r11 > r3 ? 1 : (r11 == r3 ? 0 : -1))
            if (r1 <= 0) goto L_0x00d9
            java.util.List<java.lang.Long> r13 = r10.mFileSizes     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            int r13 = r13.size()     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            r1 = r0
        L_0x00ab:
            int r3 = r13 + -1
            if (r1 >= r3) goto L_0x00cf
            java.util.List<java.lang.Long> r3 = r10.mFileSizes     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            java.lang.Object r3 = r3.get(r1)     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            java.lang.Long r3 = (java.lang.Long) r3     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            long r3 = r3.longValue()     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            int r3 = (r11 > r3 ? 1 : (r11 == r3 ? 0 : -1))
            if (r3 <= 0) goto L_0x00cf
            java.util.List<java.lang.Long> r3 = r10.mFileSizes     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            java.lang.Object r3 = r3.get(r1)     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            java.lang.Long r3 = (java.lang.Long) r3     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            long r3 = r3.longValue()     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            long r11 = r11 - r3
            int r1 = r1 + 1
            goto L_0x00ab
        L_0x00cf:
            java.util.List<java.io.RandomAccessFile> r13 = r10.mFiles     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            java.lang.Object r13 = r13.get(r1)     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            java.io.RandomAccessFile r13 = (java.io.RandomAccessFile) r13     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            r6 = r11
            goto L_0x00db
        L_0x00d9:
            r6 = r11
            r1 = r0
        L_0x00db:
            r13.seek(r6)     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            org.osmdroid.util.GEMFFile$GEMFInputStream r11 = new org.osmdroid.util.GEMFFile$GEMFInputStream     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            java.util.List<java.lang.String> r12 = r10.mFileNames     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            java.lang.Object r12 = r12.get(r1)     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            r5 = r12
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            r3 = r11
            r4 = r10
            r3.<init>(r5, r6, r8)     // Catch:{ IOException -> 0x013b, all -> 0x0138 }
            java.io.ByteArrayOutputStream r12 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x0132, all -> 0x012d }
            r12.<init>()     // Catch:{ IOException -> 0x0132, all -> 0x012d }
            r13 = 1024(0x400, float:1.435E-42)
            byte[] r13 = new byte[r13]     // Catch:{ IOException -> 0x0127, all -> 0x0122 }
        L_0x00f7:
            int r1 = r11.available()     // Catch:{ IOException -> 0x0127, all -> 0x0122 }
            if (r1 <= 0) goto L_0x0107
            int r1 = r11.read(r13)     // Catch:{ IOException -> 0x0127, all -> 0x0122 }
            if (r1 <= 0) goto L_0x00f7
            r12.write(r13, r0, r1)     // Catch:{ IOException -> 0x0127, all -> 0x0122 }
            goto L_0x00f7
        L_0x0107:
            byte[] r13 = r12.toByteArray()     // Catch:{ IOException -> 0x0127, all -> 0x0122 }
            java.io.ByteArrayInputStream r0 = new java.io.ByteArrayInputStream     // Catch:{ IOException -> 0x0127, all -> 0x0122 }
            r0.<init>(r13)     // Catch:{ IOException -> 0x0127, all -> 0x0122 }
            r12.close()     // Catch:{ IOException -> 0x0114 }
            goto L_0x0118
        L_0x0114:
            r12 = move-exception
            r12.printStackTrace()
        L_0x0118:
            r11.close()     // Catch:{ IOException -> 0x011c }
            goto L_0x0120
        L_0x011c:
            r11 = move-exception
            r11.printStackTrace()
        L_0x0120:
            r2 = r0
            goto L_0x0155
        L_0x0122:
            r13 = move-exception
            r2 = r12
            r12 = r11
            r11 = r13
            goto L_0x0158
        L_0x0127:
            r13 = move-exception
            r9 = r12
            r12 = r11
            r11 = r13
            r13 = r9
            goto L_0x013e
        L_0x012d:
            r12 = move-exception
            r9 = r12
            r12 = r11
            r11 = r9
            goto L_0x0158
        L_0x0132:
            r12 = move-exception
            r13 = r2
            r9 = r12
            r12 = r11
            r11 = r9
            goto L_0x013e
        L_0x0138:
            r11 = move-exception
            r12 = r2
            goto L_0x0158
        L_0x013b:
            r11 = move-exception
            r12 = r2
            r13 = r12
        L_0x013e:
            r11.printStackTrace()     // Catch:{ all -> 0x0156 }
            if (r13 == 0) goto L_0x014b
            r13.close()     // Catch:{ IOException -> 0x0147 }
            goto L_0x014b
        L_0x0147:
            r11 = move-exception
            r11.printStackTrace()
        L_0x014b:
            if (r12 == 0) goto L_0x0155
            r12.close()     // Catch:{ IOException -> 0x0151 }
            goto L_0x0155
        L_0x0151:
            r11 = move-exception
            r11.printStackTrace()
        L_0x0155:
            return r2
        L_0x0156:
            r11 = move-exception
            r2 = r13
        L_0x0158:
            if (r2 == 0) goto L_0x0162
            r2.close()     // Catch:{ IOException -> 0x015e }
            goto L_0x0162
        L_0x015e:
            r13 = move-exception
            r13.printStackTrace()
        L_0x0162:
            if (r12 == 0) goto L_0x016c
            r12.close()     // Catch:{ IOException -> 0x0168 }
            goto L_0x016c
        L_0x0168:
            r12 = move-exception
            r12.printStackTrace()
        L_0x016c:
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.util.GEMFFile.getInputStream(int, int, int):java.io.InputStream");
    }

    private class GEMFRange {
        Long offset;
        Integer sourceIndex;
        Integer xMax;
        Integer xMin;
        Integer yMax;
        Integer yMin;
        Integer zoom;

        private GEMFRange() {
        }

        public String toString() {
            return String.format("GEMF Range: source=%d, zoom=%d, x=%d-%d, y=%d-%d, offset=0x%08X", new Object[]{this.sourceIndex, this.zoom, this.xMin, this.xMax, this.yMin, this.yMax, this.offset});
        }
    }

    class GEMFInputStream extends InputStream {
        RandomAccessFile raf;
        int remainingBytes;

        public boolean markSupported() {
            return false;
        }

        public long skip(long j) {
            return 0;
        }

        GEMFInputStream(String str, long j, int i) throws IOException {
            RandomAccessFile randomAccessFile = new RandomAccessFile(str, "r");
            this.raf = randomAccessFile;
            randomAccessFile.seek(j);
            this.remainingBytes = i;
        }

        public int available() {
            return this.remainingBytes;
        }

        public void close() throws IOException {
            this.raf.close();
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            RandomAccessFile randomAccessFile = this.raf;
            int i3 = this.remainingBytes;
            if (i2 > i3) {
                i2 = i3;
            }
            int read = randomAccessFile.read(bArr, i, i2);
            this.remainingBytes -= read;
            return read;
        }

        public int read() throws IOException {
            int i = this.remainingBytes;
            if (i > 0) {
                this.remainingBytes = i - 1;
                return this.raf.read();
            }
            throw new IOException("End of stream");
        }
    }
}
