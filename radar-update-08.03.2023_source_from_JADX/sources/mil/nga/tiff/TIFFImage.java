package mil.nga.tiff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TIFFImage {
    private final List<FileDirectory> fileDirectories;

    public TIFFImage() {
        this.fileDirectories = new ArrayList();
    }

    public TIFFImage(FileDirectory fileDirectory) {
        ArrayList arrayList = new ArrayList();
        this.fileDirectories = arrayList;
        arrayList.add(fileDirectory);
    }

    public TIFFImage(List<FileDirectory> list) {
        ArrayList arrayList = new ArrayList();
        this.fileDirectories = arrayList;
        arrayList.addAll(list);
    }

    public void add(FileDirectory fileDirectory) {
        this.fileDirectories.add(fileDirectory);
    }

    public List<FileDirectory> getFileDirectories() {
        return Collections.unmodifiableList(this.fileDirectories);
    }

    public FileDirectory getFileDirectory() {
        return getFileDirectory(0);
    }

    public FileDirectory getFileDirectory(int i) {
        return this.fileDirectories.get(i);
    }

    public long sizeHeaderAndDirectories() {
        long j = 8;
        for (FileDirectory size : this.fileDirectories) {
            j += size.size();
        }
        return j;
    }

    public long sizeHeaderAndDirectoriesWithValues() {
        long j = 8;
        for (FileDirectory sizeWithValues : this.fileDirectories) {
            j += sizeWithValues.sizeWithValues();
        }
        return j;
    }
}
