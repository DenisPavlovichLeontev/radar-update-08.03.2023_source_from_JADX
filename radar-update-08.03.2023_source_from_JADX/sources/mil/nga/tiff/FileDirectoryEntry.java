package mil.nga.tiff;

public class FileDirectoryEntry implements Comparable<FileDirectoryEntry> {
    private final FieldTagType fieldTag;
    private final FieldType fieldType;
    private final long typeCount;
    private final Object values;

    public FileDirectoryEntry(FieldTagType fieldTagType, FieldType fieldType2, long j, Object obj) {
        this.fieldTag = fieldTagType;
        this.fieldType = fieldType2;
        this.typeCount = j;
        this.values = obj;
    }

    public FieldTagType getFieldTag() {
        return this.fieldTag;
    }

    public FieldType getFieldType() {
        return this.fieldType;
    }

    public long getTypeCount() {
        return this.typeCount;
    }

    public Object getValues() {
        return this.values;
    }

    public long sizeWithValues() {
        return sizeOfValues() + 12;
    }

    public long sizeOfValues() {
        long bytes = ((long) this.fieldType.getBytes()) * this.typeCount;
        if (bytes > 4) {
            return bytes;
        }
        return 0;
    }

    public int compareTo(FileDirectoryEntry fileDirectoryEntry) {
        return this.fieldTag.getId() - fileDirectoryEntry.getFieldTag().getId();
    }

    public int hashCode() {
        return this.fieldTag.getId();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.fieldTag == ((FileDirectoryEntry) obj).fieldTag;
    }
}
