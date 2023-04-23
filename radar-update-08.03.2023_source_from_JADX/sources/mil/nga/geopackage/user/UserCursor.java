package mil.nga.geopackage.user;

import android.database.Cursor;
import android.database.CursorWrapper;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import mil.nga.geopackage.p009db.GeoPackageDataType;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserRow;
import mil.nga.geopackage.user.UserTable;

public abstract class UserCursor<TColumn extends UserColumn, TTable extends UserTable<TColumn>, TRow extends UserRow<TColumn, TTable>> extends CursorWrapper implements UserCoreResult<TColumn, TTable, TRow> {
    private UserDao<TColumn, TTable, TRow, ? extends UserCursor<TColumn, TTable, TRow>> dao;
    private UserInvalidCursor<TColumn, TTable, TRow, ? extends UserCursor<TColumn, TTable, TRow>, ? extends UserDao<TColumn, TTable, TRow, ? extends UserCursor<TColumn, TTable, TRow>>> invalidCursor;
    private Set<Integer> invalidPositions = new LinkedHashSet();
    private UserQuery query;
    private final TTable table;

    /* access modifiers changed from: protected */
    public abstract UserInvalidCursor<TColumn, TTable, TRow, ? extends UserCursor<TColumn, TTable, TRow>, ? extends UserDao<TColumn, TTable, TRow, ? extends UserCursor<TColumn, TTable, TRow>>> createInvalidCursor(UserDao userDao, UserCursor userCursor, List<Integer> list, List<TColumn> list2);

    public boolean wasNull() {
        return false;
    }

    protected UserCursor(TTable ttable, Cursor cursor) {
        super(cursor);
        this.table = ttable;
    }

    public Object getValue(TColumn tcolumn) {
        return getValue(tcolumn.getIndex(), tcolumn.getDataType());
    }

    public TTable getTable() {
        return this.table;
    }

    public List<Integer> getInvalidPositions() {
        return new ArrayList(this.invalidPositions);
    }

    public boolean hasInvalidPositions() {
        return !this.invalidPositions.isEmpty();
    }

    public boolean moveToNext() {
        boolean moveToNext = super.moveToNext();
        return !moveToNext ? moveToNextInvalid() : moveToNext;
    }

    public TRow getRow() {
        UserInvalidCursor<TColumn, TTable, TRow, ? extends UserCursor<TColumn, TTable, TRow>, ? extends UserDao<TColumn, TTable, TRow, ? extends UserCursor<TColumn, TTable, TRow>>> userInvalidCursor = this.invalidCursor;
        if (userInvalidCursor == null) {
            return getCurrentRow();
        }
        return userInvalidCursor.getRow();
    }

    private TRow getCurrentRow() {
        TTable ttable = this.table;
        if (ttable == null) {
            return null;
        }
        int[] iArr = new int[ttable.columnCount()];
        Object[] objArr = new Object[this.table.columnCount()];
        boolean z = true;
        for (UserColumn userColumn : this.table.getColumns()) {
            int index = userColumn.getIndex();
            int type = getType(index);
            if (userColumn.isPrimaryKey() && type == 0) {
                z = false;
            }
            iArr[index] = type;
            objArr[index] = getValue(userColumn);
        }
        TRow trow = (UserRow) getRow(iArr, objArr);
        if (z) {
            return trow;
        }
        this.invalidPositions.add(Integer.valueOf(getPosition()));
        trow.setValid(false);
        return trow;
    }

    public Object getValue(int i, GeoPackageDataType geoPackageDataType) {
        UserCoreResult userCoreResult = this.invalidCursor;
        if (userCoreResult == null) {
            userCoreResult = this;
        }
        return UserCoreResultUtils.getValue(userCoreResult, i, geoPackageDataType);
    }

    public void close() {
        super.close();
        UserInvalidCursor<TColumn, TTable, TRow, ? extends UserCursor<TColumn, TTable, TRow>, ? extends UserDao<TColumn, TTable, TRow, ? extends UserCursor<TColumn, TTable, TRow>>> userInvalidCursor = this.invalidCursor;
        if (userInvalidCursor != null) {
            userInvalidCursor.close();
        }
    }

    public void setQuery(UserQuery userQuery) {
        this.query = userQuery;
    }

    public UserQuery getQuery() {
        return this.query;
    }

    /* access modifiers changed from: protected */
    public void enableInvalidRequery(UserDao<TColumn, TTable, TRow, ? extends UserCursor<TColumn, TTable, TRow>> userDao) {
        this.dao = userDao;
    }

    private boolean moveToNextInvalid() {
        if (this.invalidCursor == null && this.dao != null && hasInvalidPositions()) {
            super.close();
            List columnsOfType = this.dao.getTable().columnsOfType(GeoPackageDataType.BLOB);
            this.query.set(UserQueryParamType.COLUMNS_AS, this.dao.buildColumnsAsNull(columnsOfType));
            this.invalidCursor = createInvalidCursor(this.dao, this.dao.query(this.query), getInvalidPositions(), columnsOfType);
        }
        UserInvalidCursor<TColumn, TTable, TRow, ? extends UserCursor<TColumn, TTable, TRow>, ? extends UserDao<TColumn, TTable, TRow, ? extends UserCursor<TColumn, TTable, TRow>>> userInvalidCursor = this.invalidCursor;
        if (userInvalidCursor != null) {
            return userInvalidCursor.moveToNext();
        }
        return false;
    }
}
