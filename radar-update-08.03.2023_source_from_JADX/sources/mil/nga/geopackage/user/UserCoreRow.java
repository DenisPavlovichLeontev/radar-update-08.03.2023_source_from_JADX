package mil.nga.geopackage.user;

import java.util.Arrays;
import java.util.Date;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserTable;

public abstract class UserCoreRow<TColumn extends UserColumn, TTable extends UserTable<TColumn>> {
    protected final int[] columnTypes;
    protected final TTable table;
    protected final Object[] values;

    protected UserCoreRow(TTable ttable, int[] iArr, Object[] objArr) {
        this.table = ttable;
        this.columnTypes = iArr;
        this.values = objArr;
    }

    protected UserCoreRow(TTable ttable) {
        this.table = ttable;
        this.columnTypes = new int[ttable.columnCount()];
        this.values = new Object[ttable.columnCount()];
    }

    protected UserCoreRow(UserCoreRow<TColumn, TTable> userCoreRow) {
        this.table = userCoreRow.table;
        this.columnTypes = userCoreRow.columnTypes;
        this.values = new Object[userCoreRow.values.length];
        for (int i = 0; i < this.values.length; i++) {
            Object obj = userCoreRow.values[i];
            if (obj != null) {
                this.values[i] = copyValue(userCoreRow.getColumn(i), obj);
            }
        }
    }

    /* renamed from: mil.nga.geopackage.user.UserCoreRow$1 */
    static /* synthetic */ class C11791 {
        static final /* synthetic */ int[] $SwitchMap$mil$nga$geopackage$db$GeoPackageDataType;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                mil.nga.geopackage.db.GeoPackageDataType[] r0 = mil.nga.geopackage.p009db.GeoPackageDataType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$mil$nga$geopackage$db$GeoPackageDataType = r0
                mil.nga.geopackage.db.GeoPackageDataType r1 = mil.nga.geopackage.p009db.GeoPackageDataType.BLOB     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$mil$nga$geopackage$db$GeoPackageDataType     // Catch:{ NoSuchFieldError -> 0x001d }
                mil.nga.geopackage.db.GeoPackageDataType r1 = mil.nga.geopackage.p009db.GeoPackageDataType.DATE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$mil$nga$geopackage$db$GeoPackageDataType     // Catch:{ NoSuchFieldError -> 0x0028 }
                mil.nga.geopackage.db.GeoPackageDataType r1 = mil.nga.geopackage.p009db.GeoPackageDataType.DATETIME     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.user.UserCoreRow.C11791.<clinit>():void");
        }
    }

    /* access modifiers changed from: protected */
    public Object copyValue(TColumn tcolumn, Object obj) {
        int i = C11791.$SwitchMap$mil$nga$geopackage$db$GeoPackageDataType[tcolumn.getDataType().ordinal()];
        if (i != 1) {
            if (i != 2 && i != 3) {
                return obj;
            }
            if (obj instanceof Date) {
                return new Date(((Date) obj).getTime());
            }
            if (obj instanceof String) {
                return obj;
            }
            throw new GeoPackageException("Unsupported copy value type. column: " + tcolumn.getName() + ", value type: " + obj.getClass().getName() + ", data type: " + tcolumn.getDataType());
        } else if (obj instanceof byte[]) {
            byte[] bArr = (byte[]) obj;
            return Arrays.copyOf(bArr, bArr.length);
        } else {
            throw new GeoPackageException("Unsupported copy value type. column: " + tcolumn.getName() + ", value type: " + obj.getClass().getName() + ", data type: " + tcolumn.getDataType());
        }
    }

    public int columnCount() {
        return this.table.columnCount();
    }

    public String[] getColumnNames() {
        return this.table.getColumnNames();
    }

    public String getColumnName(int i) {
        return this.table.getColumnName(i);
    }

    public int getColumnIndex(String str) {
        return this.table.getColumnIndex(str);
    }

    public Object getValue(int i) {
        return this.values[i];
    }

    public Object getValue(String str) {
        return this.values[this.table.getColumnIndex(str)];
    }

    public Object[] getValues() {
        return this.values;
    }

    public int[] getRowColumnTypes() {
        return this.columnTypes;
    }

    public int getRowColumnType(int i) {
        return this.columnTypes[i];
    }

    public int getRowColumnType(String str) {
        return this.columnTypes[this.table.getColumnIndex(str)];
    }

    public TTable getTable() {
        return this.table;
    }

    public TColumn getColumn(int i) {
        return this.table.getColumn(i);
    }

    public TColumn getColumn(String str) {
        return this.table.getColumn(str);
    }

    public long getId() {
        Object value = getValue(getPkColumnIndex());
        if (value == null) {
            throw new GeoPackageException("Row Id was null. Table: " + this.table.getTableName() + ", Column Index: " + getPkColumnIndex() + ", Column Name: " + getPkColumn().getName());
        } else if (value instanceof Number) {
            return ((Number) value).longValue();
        } else {
            throw new GeoPackageException("Row Id was not a number. Table: " + this.table.getTableName() + ", Column Index: " + getPkColumnIndex() + ", Column Name: " + getPkColumn().getName());
        }
    }

    public boolean hasId() {
        Object value = getValue(getPkColumnIndex());
        return value != null && (value instanceof Number);
    }

    public int getPkColumnIndex() {
        return this.table.getPkColumnIndex();
    }

    public TColumn getPkColumn() {
        return this.table.getPkColumn();
    }

    public void setValue(int i, Object obj) {
        if (i != this.table.getPkColumnIndex()) {
            this.values[i] = obj;
            return;
        }
        throw new GeoPackageException("Can not update the primary key of the row. Table Name: " + this.table.getTableName() + ", Index: " + i + ", Name: " + this.table.getPkColumn().getName());
    }

    public void setValue(String str, Object obj) {
        setValue(getColumnIndex(str), obj);
    }

    /* access modifiers changed from: package-private */
    public void setId(long j) {
        this.values[getPkColumnIndex()] = Long.valueOf(j);
    }

    public void resetId() {
        this.values[getPkColumnIndex()] = null;
    }

    /* access modifiers changed from: protected */
    public void validateValue(TColumn tcolumn, Object obj, Class<?>... clsArr) {
        boolean z;
        Class<?> classType = tcolumn.getDataType().getClassType();
        int length = clsArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                z = false;
                break;
            } else if (clsArr[i].equals(classType)) {
                z = true;
                break;
            } else {
                i++;
            }
        }
        if (!z) {
            throw new GeoPackageException("Illegal value. Column: " + tcolumn.getName() + ", Value: " + obj + ", Expected Type: " + classType.getSimpleName() + ", Actual Type: " + clsArr[0].getSimpleName());
        }
    }
}
