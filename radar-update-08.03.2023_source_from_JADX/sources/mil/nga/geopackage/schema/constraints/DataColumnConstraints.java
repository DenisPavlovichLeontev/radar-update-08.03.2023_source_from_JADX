package mil.nga.geopackage.schema.constraints;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.schema.columns.DataColumns;
import mil.nga.geopackage.schema.columns.DataColumnsDao;

@DatabaseTable(daoClass = DataColumnConstraintsDao.class, tableName = "gpkg_data_column_constraints")
public class DataColumnConstraints {
    public static final String COLUMN_CONSTRAINT_NAME = "constraint_name";
    public static final String COLUMN_CONSTRAINT_TYPE = "constraint_type";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_MAX = "max";
    public static final String COLUMN_MAX_IS_INCLUSIVE = "max_is_inclusive";
    public static final String COLUMN_MIN = "min";
    public static final String COLUMN_MIN_IS_INCLUSIVE = "min_is_inclusive";
    public static final String COLUMN_VALUE = "value";
    public static final String TABLE_NAME = "gpkg_data_column_constraints";
    @DatabaseField(canBeNull = false, columnName = "constraint_name", uniqueCombo = true)
    private String constraintName;
    @DatabaseField(canBeNull = false, columnName = "constraint_type", uniqueCombo = true)
    private String constraintType;
    @DatabaseField(columnName = "description")
    private String description;
    @DatabaseField(columnName = "max")
    private BigDecimal max;
    @DatabaseField(columnName = "max_is_inclusive")
    private Boolean maxIsInclusive;
    @DatabaseField(columnName = "min")
    private BigDecimal min;
    @DatabaseField(columnName = "min_is_inclusive")
    private Boolean minIsInclusive;
    @DatabaseField(columnName = "value", uniqueCombo = true)
    private String value;

    public DataColumnConstraints() {
    }

    public DataColumnConstraints(DataColumnConstraints dataColumnConstraints) {
        this.constraintName = dataColumnConstraints.constraintName;
        this.constraintType = dataColumnConstraints.constraintType;
        this.value = dataColumnConstraints.value;
        this.min = dataColumnConstraints.min;
        this.minIsInclusive = dataColumnConstraints.minIsInclusive;
        this.max = dataColumnConstraints.max;
        this.maxIsInclusive = dataColumnConstraints.maxIsInclusive;
        this.description = dataColumnConstraints.description;
    }

    public String getConstraintName() {
        return this.constraintName;
    }

    public void setConstraintName(String str) {
        this.constraintName = str;
    }

    public DataColumnConstraintType getConstraintType() {
        return DataColumnConstraintType.fromValue(this.constraintType);
    }

    public void setConstraintType(String str) {
        setConstraintType(DataColumnConstraintType.fromValue(str));
    }

    /* renamed from: mil.nga.geopackage.schema.constraints.DataColumnConstraints$1 */
    static /* synthetic */ class C11761 {

        /* renamed from: $SwitchMap$mil$nga$geopackage$schema$constraints$DataColumnConstraintType */
        static final /* synthetic */ int[] f354xad901429;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                mil.nga.geopackage.schema.constraints.DataColumnConstraintType[] r0 = mil.nga.geopackage.schema.constraints.DataColumnConstraintType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f354xad901429 = r0
                mil.nga.geopackage.schema.constraints.DataColumnConstraintType r1 = mil.nga.geopackage.schema.constraints.DataColumnConstraintType.RANGE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f354xad901429     // Catch:{ NoSuchFieldError -> 0x001d }
                mil.nga.geopackage.schema.constraints.DataColumnConstraintType r1 = mil.nga.geopackage.schema.constraints.DataColumnConstraintType.ENUM     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f354xad901429     // Catch:{ NoSuchFieldError -> 0x0028 }
                mil.nga.geopackage.schema.constraints.DataColumnConstraintType r1 = mil.nga.geopackage.schema.constraints.DataColumnConstraintType.GLOB     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.schema.constraints.DataColumnConstraints.C11761.<clinit>():void");
        }
    }

    public void setConstraintType(DataColumnConstraintType dataColumnConstraintType) {
        this.constraintType = dataColumnConstraintType.getValue();
        int i = C11761.f354xad901429[dataColumnConstraintType.ordinal()];
        if (i == 1) {
            setValue((String) null);
        } else if (i == 2 || i == 3) {
            setMin((BigDecimal) null);
            setMax((BigDecimal) null);
            setMinIsInclusive((Boolean) null);
            setMaxIsInclusive((Boolean) null);
        }
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        if (this.constraintType == null || str == null || !getConstraintType().equals(DataColumnConstraintType.RANGE)) {
            this.value = str;
            return;
        }
        throw new GeoPackageException("The value must be null for " + DataColumnConstraintType.RANGE + " constraints");
    }

    public BigDecimal getMin() {
        return this.min;
    }

    public void setMin(BigDecimal bigDecimal) {
        validateRangeValue("min", bigDecimal);
        this.min = bigDecimal;
    }

    public Boolean getMinIsInclusive() {
        return this.minIsInclusive;
    }

    public void setMinIsInclusive(Boolean bool) {
        validateRangeValue(COLUMN_MIN_IS_INCLUSIVE, bool);
        this.minIsInclusive = bool;
    }

    public BigDecimal getMax() {
        return this.max;
    }

    public void setMax(BigDecimal bigDecimal) {
        validateRangeValue("max", bigDecimal);
        this.max = bigDecimal;
    }

    public Boolean getMaxIsInclusive() {
        return this.maxIsInclusive;
    }

    public void setMaxIsInclusive(Boolean bool) {
        validateRangeValue(COLUMN_MAX_IS_INCLUSIVE, bool);
        this.maxIsInclusive = bool;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public List<DataColumns> getColumns(DataColumnsDao dataColumnsDao) throws SQLException {
        String str = this.constraintName;
        if (str != null) {
            return dataColumnsDao.queryByConstraintName(str);
        }
        return null;
    }

    private void validateRangeValue(String str, Object obj) {
        if (this.constraintType != null && obj != null && !getConstraintType().equals(DataColumnConstraintType.RANGE)) {
            throw new GeoPackageException("The " + str + " must be null for " + DataColumnConstraintType.ENUM + " and " + DataColumnConstraintType.GLOB + " constraints");
        }
    }
}
