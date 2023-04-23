package mil.nga.geopackage.user;

import android.content.ContentValues;
import java.util.Date;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.p009db.DateConverter;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserTable;

public abstract class UserRow<TColumn extends UserColumn, TTable extends UserTable<TColumn>> extends UserCoreRow<TColumn, TTable> {
    private boolean valid = true;

    protected UserRow(TTable ttable, int[] iArr, Object[] objArr) {
        super(ttable, iArr, objArr);
    }

    protected UserRow(TTable ttable) {
        super(ttable);
    }

    protected UserRow(UserRow<TColumn, TTable> userRow) {
        super(userRow);
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        for (UserColumn userColumn : this.table.getColumns()) {
            if (!userColumn.isPrimaryKey()) {
                Object obj = this.values[userColumn.getIndex()];
                String name = userColumn.getName();
                if (obj == null) {
                    contentValues.putNull(name);
                } else {
                    columnToContentValue(contentValues, userColumn, obj);
                }
            }
        }
        return contentValues;
    }

    /* access modifiers changed from: protected */
    public void columnToContentValue(ContentValues contentValues, TColumn tcolumn, Object obj) {
        String name = tcolumn.getName();
        if (obj instanceof Number) {
            if (obj instanceof Byte) {
                validateValue(tcolumn, obj, Byte.class, Short.class, Integer.class, Long.class);
                contentValues.put(name, (Byte) obj);
            } else if (obj instanceof Short) {
                validateValue(tcolumn, obj, Short.class, Integer.class, Long.class);
                contentValues.put(name, (Short) obj);
            } else if (obj instanceof Integer) {
                validateValue(tcolumn, obj, Integer.class, Long.class);
                contentValues.put(name, (Integer) obj);
            } else if (obj instanceof Long) {
                validateValue(tcolumn, obj, Long.class, Double.class);
                contentValues.put(name, (Long) obj);
            } else if (obj instanceof Float) {
                validateValue(tcolumn, obj, Float.class);
                contentValues.put(name, (Float) obj);
            } else if (obj instanceof Double) {
                validateValue(tcolumn, obj, Double.class);
                contentValues.put(name, (Double) obj);
            } else {
                throw new GeoPackageException("Unsupported Number type: " + obj.getClass().getSimpleName());
            }
        } else if (obj instanceof String) {
            validateValue(tcolumn, obj, String.class);
            String str = (String) obj;
            if (tcolumn.getMax() == null || ((long) str.length()) <= tcolumn.getMax().longValue()) {
                contentValues.put(name, str);
                return;
            }
            throw new GeoPackageException("String is larger than the column max. Size: " + str.length() + ", Max: " + tcolumn.getMax() + ", Column: " + name);
        } else if (obj instanceof byte[]) {
            validateValue(tcolumn, obj, byte[].class);
            byte[] bArr = (byte[]) obj;
            if (tcolumn.getMax() == null || ((long) bArr.length) <= tcolumn.getMax().longValue()) {
                contentValues.put(name, bArr);
                return;
            }
            throw new GeoPackageException("Byte array is larger than the column max. Size: " + bArr.length + ", Max: " + tcolumn.getMax() + ", Column: " + name);
        } else if (obj instanceof Boolean) {
            validateValue(tcolumn, obj, Boolean.class);
            contentValues.put(name, Short.valueOf(((Boolean) obj).booleanValue() ? (short) 1 : 0));
        } else if (obj instanceof Date) {
            validateValue(tcolumn, obj, Date.class, String.class);
            contentValues.put(name, DateConverter.converter(tcolumn.getDataType()).stringValue((Date) obj));
        } else {
            throw new GeoPackageException("Unsupported update column value. column: " + name + ", value: " + obj);
        }
    }

    public void setValid(boolean z) {
        this.valid = z;
    }

    public boolean isValid() {
        return this.valid;
    }
}
