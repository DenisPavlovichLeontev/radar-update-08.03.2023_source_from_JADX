package mil.nga.geopackage.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.p009db.GeoPackageDataType;
import mil.nga.geopackage.user.UserColumn;

public abstract class UserTable<TColumn extends UserColumn> {
    private static final Logger log = Logger.getLogger(UserTable.class.getName());
    private final String[] columnNames;
    private final List<TColumn> columns;
    private final Map<String, Integer> nameToIndex = new HashMap();
    private final int pkIndex;
    private final String tableName;
    private final List<UserUniqueConstraint<TColumn>> uniqueConstraints = new ArrayList();

    protected UserTable(String str, List<TColumn> list) {
        this.tableName = str;
        this.columns = list;
        HashSet hashSet = new HashSet();
        this.columnNames = new String[list.size()];
        Integer num = null;
        for (TColumn tcolumn : list) {
            int index = tcolumn.getIndex();
            if (tcolumn.isPrimaryKey()) {
                if (num == null) {
                    num = Integer.valueOf(index);
                } else {
                    throw new GeoPackageException("More than one primary key column was found for table '" + str + "'. Index " + num + " and " + index);
                }
            }
            if (!hashSet.contains(Integer.valueOf(index))) {
                hashSet.add(Integer.valueOf(index));
                this.columnNames[index] = tcolumn.getName();
                this.nameToIndex.put(tcolumn.getName(), Integer.valueOf(index));
            } else {
                throw new GeoPackageException("Duplicate index: " + index + ", Table Name: " + str);
            }
        }
        if (num != null) {
            this.pkIndex = num.intValue();
        } else {
            Logger logger = log;
            Level level = Level.WARNING;
            logger.log(level, "No primary key column was found for table '" + str + "'");
            this.pkIndex = -1;
        }
        int i = 0;
        while (i < list.size()) {
            if (hashSet.contains(Integer.valueOf(i))) {
                i++;
            } else {
                throw new GeoPackageException("No column found at index: " + i + ", Table Name: " + str);
            }
        }
        Collections.sort(list);
    }

    /* access modifiers changed from: protected */
    public void duplicateCheck(int i, Integer num, String str) {
        if (num != null) {
            throw new GeoPackageException("More than one " + str + " column was found for table '" + this.tableName + "'. Index " + num + " and " + i);
        }
    }

    /* access modifiers changed from: protected */
    public void typeCheck(GeoPackageDataType geoPackageDataType, TColumn tcolumn) {
        GeoPackageDataType dataType = tcolumn.getDataType();
        if (dataType == null || !dataType.equals(geoPackageDataType)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected ");
            sb.append(tcolumn.getName());
            sb.append(" column data type was found for table '");
            sb.append(this.tableName);
            sb.append("', expected: ");
            sb.append(geoPackageDataType.name());
            sb.append(", actual: ");
            sb.append(dataType != null ? dataType.name() : "null");
            throw new GeoPackageException(sb.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void missingCheck(Integer num, String str) {
        if (num == null) {
            throw new GeoPackageException("No " + str + " column was found for table '" + this.tableName + "'");
        }
    }

    public int getColumnIndex(String str) {
        Integer num = this.nameToIndex.get(str);
        if (num != null) {
            return num.intValue();
        }
        throw new GeoPackageException("Column does not exist in table '" + this.tableName + "', column: " + str);
    }

    public String[] getColumnNames() {
        return this.columnNames;
    }

    public String getColumnName(int i) {
        return this.columnNames[i];
    }

    public List<TColumn> getColumns() {
        return this.columns;
    }

    public TColumn getColumn(int i) {
        return (UserColumn) this.columns.get(i);
    }

    public TColumn getColumn(String str) {
        return getColumn(getColumnIndex(str));
    }

    public int columnCount() {
        return this.columns.size();
    }

    public String getTableName() {
        return this.tableName;
    }

    public int getPkColumnIndex() {
        return this.pkIndex;
    }

    public TColumn getPkColumn() {
        int i = this.pkIndex;
        if (i >= 0) {
            return (UserColumn) this.columns.get(i);
        }
        return null;
    }

    public void addUniqueConstraint(UserUniqueConstraint<TColumn> userUniqueConstraint) {
        this.uniqueConstraints.add(userUniqueConstraint);
    }

    public List<UserUniqueConstraint<TColumn>> getUniqueConstraints() {
        return this.uniqueConstraints;
    }

    public List<TColumn> columnsOfType(GeoPackageDataType geoPackageDataType) {
        ArrayList arrayList = new ArrayList();
        for (TColumn tcolumn : this.columns) {
            if (tcolumn.getDataType() == geoPackageDataType) {
                arrayList.add(tcolumn);
            }
        }
        return arrayList;
    }
}
