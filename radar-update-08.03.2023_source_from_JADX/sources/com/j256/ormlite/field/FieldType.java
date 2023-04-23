package com.j256.ormlite.field;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.BaseForeignCollection;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.EagerForeignCollection;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.LazyForeignCollection;
import com.j256.ormlite.dao.ObjectCache;
import com.j256.ormlite.field.types.VoidType;
import com.j256.ormlite.logger.Log;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.p006db.DatabaseType;
import com.j256.ormlite.stmt.mapped.MappedQueryForFieldEq;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.DatabaseResults;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableInfo;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import mil.nga.geopackage.extension.Extensions;
import org.osgeo.proj4j.units.AngleFormat;

public class FieldType {
    private static boolean DEFAULT_VALUE_BOOLEAN = false;
    private static byte DEFAULT_VALUE_BYTE = 0;
    private static char DEFAULT_VALUE_CHAR = '\u0000';
    private static double DEFAULT_VALUE_DOUBLE = 0.0d;
    private static float DEFAULT_VALUE_FLOAT = 0.0f;
    private static int DEFAULT_VALUE_INT = 0;
    private static long DEFAULT_VALUE_LONG = 0;
    private static short DEFAULT_VALUE_SHORT = 0;
    public static final String FOREIGN_ID_FIELD_SUFFIX = "_id";
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) FieldType.class);
    private static final ThreadLocal<LevelCounters> threadLevelCounters = new ThreadLocal<>();
    private final String columnName;
    private final ConnectionSource connectionSource;
    private DataPersister dataPersister;
    private Object dataTypeConfigObj;
    private Object defaultValue;
    private final Field field;
    private final DatabaseFieldConfig fieldConfig;
    private FieldConverter fieldConverter;
    private final Method fieldGetMethod;
    private final Method fieldSetMethod;
    private BaseDaoImpl<?, ?> foreignDao;
    private FieldType foreignFieldType;
    private FieldType foreignIdField;
    private FieldType foreignRefField;
    private TableInfo<?, ?> foreignTableInfo;
    private final String generatedIdSequence;
    private final boolean isGeneratedId;
    private final boolean isId;
    private MappedQueryForFieldEq<Object, Object> mappedQueryForForeignField;
    private final Class<?> parentClass;
    private final String tableName;

    public FieldType(ConnectionSource connectionSource2, String str, Field field2, DatabaseFieldConfig databaseFieldConfig, Class<?> cls) throws SQLException {
        DataPersister dataPersister2;
        String str2;
        this.connectionSource = connectionSource2;
        this.tableName = str;
        DatabaseType databaseType = connectionSource2.getDatabaseType();
        this.field = field2;
        this.parentClass = cls;
        databaseFieldConfig.postProcess();
        Class<?> type = field2.getType();
        if (databaseFieldConfig.getDataPersister() == null) {
            Class<? extends DataPersister> persisterClass = databaseFieldConfig.getPersisterClass();
            if (persisterClass == null || persisterClass == VoidType.class) {
                dataPersister2 = DataPersisterManager.lookupForField(field2);
            } else {
                try {
                    try {
                        Object invoke = persisterClass.getDeclaredMethod("getSingleton", new Class[0]).invoke((Object) null, new Object[0]);
                        if (invoke != null) {
                            try {
                                dataPersister2 = (DataPersister) invoke;
                            } catch (Exception e) {
                                throw SqlExceptionUtil.create("Could not cast result of static getSingleton method to DataPersister from class " + persisterClass, e);
                            }
                        } else {
                            throw new SQLException("Static getSingleton method should not return null on class " + persisterClass);
                        }
                    } catch (InvocationTargetException e2) {
                        throw SqlExceptionUtil.create("Could not run getSingleton method on class " + persisterClass, e2.getTargetException());
                    } catch (Exception e3) {
                        throw SqlExceptionUtil.create("Could not run getSingleton method on class " + persisterClass, e3);
                    }
                } catch (Exception e4) {
                    throw SqlExceptionUtil.create("Could not find getSingleton static method on class " + persisterClass, e4);
                }
            }
        } else {
            dataPersister2 = databaseFieldConfig.getDataPersister();
            if (!dataPersister2.isValidForField(field2)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Field class ");
                sb.append(type.getName());
                sb.append(" for field ");
                sb.append(this);
                sb.append(" is not valid for type ");
                sb.append(dataPersister2);
                Class<?> primaryClass = dataPersister2.getPrimaryClass();
                if (primaryClass != null) {
                    sb.append(", maybe should be " + primaryClass);
                }
                throw new IllegalArgumentException(sb.toString());
            }
        }
        String foreignColumnName = databaseFieldConfig.getForeignColumnName();
        String name = field2.getName();
        if (databaseFieldConfig.isForeign() || databaseFieldConfig.isForeignAutoRefresh() || foreignColumnName != null) {
            if (dataPersister2 == null || !dataPersister2.isPrimitive()) {
                if (foreignColumnName == null) {
                    str2 = name + FOREIGN_ID_FIELD_SUFFIX;
                } else {
                    str2 = name + Extensions.EXTENSION_NAME_DIVIDER + foreignColumnName;
                }
                name = str2;
                if (ForeignCollection.class.isAssignableFrom(type)) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Field '");
                    sb2.append(field2.getName());
                    sb2.append("' in class ");
                    sb2.append(type);
                    sb2.append("' should use the @");
                    Class<ForeignCollectionField> cls2 = ForeignCollectionField.class;
                    sb2.append("ForeignCollectionField");
                    sb2.append(" annotation not foreign=true");
                    throw new SQLException(sb2.toString());
                }
            } else {
                throw new IllegalArgumentException("Field " + this + " is a primitive class " + type + " but marked as foreign");
            }
        } else if (databaseFieldConfig.isForeignCollection()) {
            if (type == Collection.class || ForeignCollection.class.isAssignableFrom(type)) {
                Type genericType = field2.getGenericType();
                if (!(genericType instanceof ParameterizedType)) {
                    throw new SQLException("Field class for '" + field2.getName() + "' must be a parameterized Collection.");
                } else if (((ParameterizedType) genericType).getActualTypeArguments().length == 0) {
                    throw new SQLException("Field class for '" + field2.getName() + "' must be a parameterized Collection with at least 1 type.");
                }
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Field class for '");
                sb3.append(field2.getName());
                sb3.append("' must be of class ");
                Class<ForeignCollection> cls3 = ForeignCollection.class;
                sb3.append("ForeignCollection");
                sb3.append(" or Collection.");
                throw new SQLException(sb3.toString());
            }
        } else if (dataPersister2 == null && !databaseFieldConfig.isForeignCollection()) {
            if (byte[].class.isAssignableFrom(type)) {
                throw new SQLException("ORMLite does not know how to store " + type + " for field '" + field2.getName() + "'. byte[] fields must specify dataType=DataType.BYTE_ARRAY or SERIALIZABLE");
            } else if (Serializable.class.isAssignableFrom(type)) {
                throw new SQLException("ORMLite does not know how to store " + type + " for field '" + field2.getName() + "'.  Use another class, custom persister, or to serialize it use " + "dataType=DataType.SERIALIZABLE");
            } else {
                throw new IllegalArgumentException("ORMLite does not know how to store " + type + " for field " + field2.getName() + ". Use another class or a custom persister.");
            }
        }
        if (databaseFieldConfig.getColumnName() == null) {
            this.columnName = name;
        } else {
            this.columnName = databaseFieldConfig.getColumnName();
        }
        this.fieldConfig = databaseFieldConfig;
        if (databaseFieldConfig.isId()) {
            if (databaseFieldConfig.isGeneratedId() || databaseFieldConfig.getGeneratedIdSequence() != null) {
                throw new IllegalArgumentException("Must specify one of id, generatedId, and generatedIdSequence with " + field2.getName());
            }
            this.isId = true;
            this.isGeneratedId = false;
            this.generatedIdSequence = null;
        } else if (databaseFieldConfig.isGeneratedId()) {
            if (databaseFieldConfig.getGeneratedIdSequence() == null) {
                this.isId = true;
                this.isGeneratedId = true;
                if (databaseType.isIdSequenceNeeded()) {
                    this.generatedIdSequence = databaseType.generateIdSequenceName(str, this);
                } else {
                    this.generatedIdSequence = null;
                }
            } else {
                throw new IllegalArgumentException("Must specify one of id, generatedId, and generatedIdSequence with " + field2.getName());
            }
        } else if (databaseFieldConfig.getGeneratedIdSequence() != null) {
            this.isId = true;
            this.isGeneratedId = true;
            String generatedIdSequence2 = databaseFieldConfig.getGeneratedIdSequence();
            this.generatedIdSequence = databaseType.isEntityNamesMustBeUpCase() ? databaseType.upCaseEntityName(generatedIdSequence2) : generatedIdSequence2;
        } else {
            this.isId = false;
            this.isGeneratedId = false;
            this.generatedIdSequence = null;
        }
        if (!this.isId || (!databaseFieldConfig.isForeign() && !databaseFieldConfig.isForeignAutoRefresh())) {
            if (databaseFieldConfig.isUseGetSet()) {
                this.fieldGetMethod = DatabaseFieldConfig.findGetMethod(field2, true);
                this.fieldSetMethod = DatabaseFieldConfig.findSetMethod(field2, true);
            } else {
                if (!field2.isAccessible()) {
                    try {
                        field2.setAccessible(true);
                    } catch (SecurityException unused) {
                        throw new IllegalArgumentException("Could not open access to field " + field2.getName() + ".  You may have to set useGetSet=true to fix.");
                    }
                }
                this.fieldGetMethod = null;
                this.fieldSetMethod = null;
            }
            if (databaseFieldConfig.isAllowGeneratedIdInsert() && !databaseFieldConfig.isGeneratedId()) {
                throw new IllegalArgumentException("Field " + field2.getName() + " must be a generated-id if allowGeneratedIdInsert = true");
            } else if (databaseFieldConfig.isForeignAutoRefresh() && !databaseFieldConfig.isForeign()) {
                throw new IllegalArgumentException("Field " + field2.getName() + " must have foreign = true if foreignAutoRefresh = true");
            } else if (databaseFieldConfig.isForeignAutoCreate() && !databaseFieldConfig.isForeign()) {
                throw new IllegalArgumentException("Field " + field2.getName() + " must have foreign = true if foreignAutoCreate = true");
            } else if (databaseFieldConfig.getForeignColumnName() != null && !databaseFieldConfig.isForeign()) {
                throw new IllegalArgumentException("Field " + field2.getName() + " must have foreign = true if foreignColumnName is set");
            } else if (!databaseFieldConfig.isVersion() || (dataPersister2 != null && dataPersister2.isValidForVersion())) {
                assignDataType(databaseType, dataPersister2);
            } else {
                throw new IllegalArgumentException("Field " + field2.getName() + " is not a valid type to be a version field");
            }
        } else {
            throw new IllegalArgumentException("Id field " + field2.getName() + " cannot also be a foreign object");
        }
    }

    public void configDaoInformation(ConnectionSource connectionSource2, Class<?> cls) throws SQLException {
        TableInfo<?, ?> tableInfo;
        FieldType fieldType;
        FieldType fieldType2;
        FieldType fieldType3;
        BaseDaoImpl<?, ?> baseDaoImpl;
        BaseDaoImpl<?, ?> baseDaoImpl2;
        TableInfo<?, ?> tableInfo2;
        BaseDaoImpl<?, ?> baseDaoImpl3;
        BaseDaoImpl<?, ?> baseDaoImpl4;
        Class<?> type = this.field.getType();
        DatabaseType databaseType = connectionSource2.getDatabaseType();
        String foreignColumnName = this.fieldConfig.getForeignColumnName();
        MappedQueryForFieldEq<Object, Object> mappedQueryForFieldEq = null;
        if (this.fieldConfig.isForeignAutoRefresh() || foreignColumnName != null) {
            DatabaseTableConfig<?> foreignTableConfig = this.fieldConfig.getForeignTableConfig();
            if (foreignTableConfig == null) {
                baseDaoImpl2 = (BaseDaoImpl) DaoManager.createDao(connectionSource2, type);
                tableInfo2 = baseDaoImpl2.getTableInfo();
            } else {
                foreignTableConfig.extractFieldTypes(connectionSource2);
                baseDaoImpl2 = (BaseDaoImpl) DaoManager.createDao(connectionSource2, foreignTableConfig);
                tableInfo2 = baseDaoImpl2.getTableInfo();
            }
            fieldType3 = tableInfo.getIdField();
            if (fieldType3 != null) {
                if (foreignColumnName == null) {
                    fieldType2 = fieldType3;
                } else {
                    fieldType2 = tableInfo.getFieldTypeByColumnName(foreignColumnName);
                    if (fieldType2 == null) {
                        throw new IllegalArgumentException("Foreign field " + type + " does not have field named '" + foreignColumnName + "'");
                    }
                }
                baseDaoImpl = baseDaoImpl2;
                fieldType = null;
                mappedQueryForFieldEq = MappedQueryForFieldEq.build(databaseType, tableInfo, fieldType2);
            } else {
                throw new IllegalArgumentException("Foreign field " + type + " does not have id field");
            }
        } else if (this.fieldConfig.isForeign()) {
            DataPersister dataPersister2 = this.dataPersister;
            if (dataPersister2 == null || !dataPersister2.isPrimitive()) {
                DatabaseTableConfig<?> foreignTableConfig2 = this.fieldConfig.getForeignTableConfig();
                if (foreignTableConfig2 != null) {
                    foreignTableConfig2.extractFieldTypes(connectionSource2);
                    baseDaoImpl4 = (BaseDaoImpl) DaoManager.createDao(connectionSource2, foreignTableConfig2);
                } else {
                    baseDaoImpl4 = (BaseDaoImpl) DaoManager.createDao(connectionSource2, type);
                }
                tableInfo = baseDaoImpl4.getTableInfo();
                fieldType3 = tableInfo.getIdField();
                if (fieldType3 == null) {
                    throw new IllegalArgumentException("Foreign field " + type + " does not have id field");
                } else if (!isForeignAutoCreate() || fieldType3.isGeneratedId()) {
                    baseDaoImpl = baseDaoImpl4;
                    fieldType2 = fieldType3;
                    fieldType = null;
                } else {
                    throw new IllegalArgumentException("Field " + this.field.getName() + ", if foreignAutoCreate = true then class " + type.getSimpleName() + " must have id field with generatedId = true");
                }
            } else {
                throw new IllegalArgumentException("Field " + this + " is a primitive class " + type + " but marked as foreign");
            }
        } else {
            if (!this.fieldConfig.isForeignCollection()) {
                fieldType = null;
                tableInfo = null;
                baseDaoImpl = null;
                fieldType3 = null;
            } else if (type == Collection.class || ForeignCollection.class.isAssignableFrom(type)) {
                Type genericType = this.field.getGenericType();
                if (genericType instanceof ParameterizedType) {
                    Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
                    if (actualTypeArguments.length != 0) {
                        if (actualTypeArguments[0] instanceof TypeVariable) {
                            actualTypeArguments = ((ParameterizedType) cls.getGenericSuperclass()).getActualTypeArguments();
                        }
                        Type type2 = actualTypeArguments[0];
                        if (type2 instanceof Class) {
                            Class cls2 = (Class) type2;
                            DatabaseTableConfig<?> foreignTableConfig3 = this.fieldConfig.getForeignTableConfig();
                            if (foreignTableConfig3 == null) {
                                baseDaoImpl3 = (BaseDaoImpl) DaoManager.createDao(connectionSource2, cls2);
                            } else {
                                baseDaoImpl3 = (BaseDaoImpl) DaoManager.createDao(connectionSource2, foreignTableConfig3);
                            }
                            baseDaoImpl = baseDaoImpl3;
                            fieldType = findForeignFieldType(cls2, cls, baseDaoImpl3);
                            tableInfo = null;
                            fieldType3 = null;
                        } else {
                            throw new SQLException("Field class for '" + this.field.getName() + "' must be a parameterized Collection whose generic argument is an entity class not: " + actualTypeArguments[0]);
                        }
                    } else {
                        throw new SQLException("Field class for '" + this.field.getName() + "' must be a parameterized Collection with at least 1 type.");
                    }
                } else {
                    throw new SQLException("Field class for '" + this.field.getName() + "' must be a parameterized Collection.");
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Field class for '");
                sb.append(this.field.getName());
                sb.append("' must be of class ");
                Class<ForeignCollection> cls3 = ForeignCollection.class;
                sb.append("ForeignCollection");
                sb.append(" or Collection.");
                throw new SQLException(sb.toString());
            }
            fieldType2 = fieldType3;
        }
        this.mappedQueryForForeignField = mappedQueryForFieldEq;
        this.foreignTableInfo = tableInfo;
        this.foreignFieldType = fieldType;
        this.foreignDao = baseDaoImpl;
        this.foreignIdField = fieldType3;
        this.foreignRefField = fieldType2;
        if (fieldType2 != null) {
            assignDataType(databaseType, fieldType2.getDataPersister());
        }
    }

    public Field getField() {
        return this.field;
    }

    public String getTableName() {
        return this.tableName;
    }

    public String getFieldName() {
        return this.field.getName();
    }

    public Class<?> getType() {
        return this.field.getType();
    }

    public Type getGenericType() {
        return this.field.getGenericType();
    }

    public String getColumnName() {
        return this.columnName;
    }

    public DataPersister getDataPersister() {
        return this.dataPersister;
    }

    public Object getDataTypeConfigObj() {
        return this.dataTypeConfigObj;
    }

    public SqlType getSqlType() {
        return this.fieldConverter.getSqlType();
    }

    public Object getDefaultValue() {
        return this.defaultValue;
    }

    public int getWidth() {
        return this.fieldConfig.getWidth();
    }

    public boolean isCanBeNull() {
        return this.fieldConfig.isCanBeNull();
    }

    public boolean isId() {
        return this.isId;
    }

    public boolean isGeneratedId() {
        return this.isGeneratedId;
    }

    public boolean isGeneratedIdSequence() {
        return this.generatedIdSequence != null;
    }

    public String getGeneratedIdSequence() {
        return this.generatedIdSequence;
    }

    public boolean isForeign() {
        return this.fieldConfig.isForeign();
    }

    public void assignField(Object obj, Object obj2, boolean z, ObjectCache objectCache) throws SQLException {
        Object obj3;
        Object obj4;
        Logger logger2 = logger;
        if (logger2.isLevelEnabled(Log.Level.TRACE)) {
            Object obj5 = "null";
            if (obj == null) {
                obj4 = obj5;
            } else {
                obj4 = obj.getClass();
            }
            if (obj2 != null) {
                obj5 = obj2.getClass();
            }
            logger2.trace("assiging from data {}, val {}: {}", obj4, obj5, obj2);
        }
        if (!(this.foreignRefField == null || obj2 == null)) {
            Object extractJavaFieldValue = extractJavaFieldValue(obj);
            if (extractJavaFieldValue == null || !extractJavaFieldValue.equals(obj2)) {
                ObjectCache objectCache2 = this.foreignDao.getObjectCache();
                if (objectCache2 == null) {
                    obj3 = null;
                } else {
                    obj3 = objectCache2.get(getType(), obj2);
                }
                if (obj3 != null) {
                    obj2 = obj3;
                } else if (!z) {
                    obj2 = createForeignObject(obj2, objectCache);
                }
            } else {
                return;
            }
        }
        Method method = this.fieldSetMethod;
        if (method == null) {
            try {
                this.field.set(obj, obj2);
            } catch (IllegalArgumentException e) {
                throw SqlExceptionUtil.create("Could not assign object '" + obj2 + "' of type " + obj2.getClass() + " to field " + this, e);
            } catch (IllegalAccessException e2) {
                throw SqlExceptionUtil.create("Could not assign object '" + obj2 + "' of type " + obj2.getClass() + "' to field " + this, e2);
            }
        } else {
            try {
                method.invoke(obj, new Object[]{obj2});
            } catch (Exception e3) {
                throw SqlExceptionUtil.create("Could not call " + this.fieldSetMethod + " on object with '" + obj2 + "' for " + this, e3);
            }
        }
    }

    public Object assignIdValue(Object obj, Number number, ObjectCache objectCache) throws SQLException {
        Object convertIdNumber = this.dataPersister.convertIdNumber(number);
        if (convertIdNumber != null) {
            assignField(obj, convertIdNumber, false, objectCache);
            return convertIdNumber;
        }
        throw new SQLException("Invalid class " + this.dataPersister + " for sequence-id " + this);
    }

    public <FV> FV extractRawJavaFieldValue(Object obj) throws SQLException {
        Method method = this.fieldGetMethod;
        if (method == null) {
            try {
                return this.field.get(obj);
            } catch (Exception e) {
                throw SqlExceptionUtil.create("Could not get field value for " + this, e);
            }
        } else {
            try {
                return method.invoke(obj, new Object[0]);
            } catch (Exception e2) {
                throw SqlExceptionUtil.create("Could not call " + this.fieldGetMethod + " for " + this, e2);
            }
        }
    }

    public Object extractJavaFieldValue(Object obj) throws SQLException {
        Object extractRawJavaFieldValue = extractRawJavaFieldValue(obj);
        FieldType fieldType = this.foreignRefField;
        return (fieldType == null || extractRawJavaFieldValue == null) ? extractRawJavaFieldValue : fieldType.extractRawJavaFieldValue(extractRawJavaFieldValue);
    }

    public Object extractJavaFieldToSqlArgValue(Object obj) throws SQLException {
        return convertJavaFieldToSqlArgValue(extractJavaFieldValue(obj));
    }

    public Object convertJavaFieldToSqlArgValue(Object obj) throws SQLException {
        if (obj == null) {
            return null;
        }
        return this.fieldConverter.javaToSqlArg(this, obj);
    }

    public Object convertStringToJavaField(String str, int i) throws SQLException {
        if (str == null) {
            return null;
        }
        return this.fieldConverter.resultStringToJava(this, str, i);
    }

    public Object moveToNextValue(Object obj) throws SQLException {
        DataPersister dataPersister2 = this.dataPersister;
        if (dataPersister2 == null) {
            return null;
        }
        return dataPersister2.moveToNextValue(obj);
    }

    public FieldType getForeignIdField() {
        return this.foreignIdField;
    }

    public FieldType getForeignRefField() {
        return this.foreignRefField;
    }

    public boolean isEscapedValue() {
        return this.dataPersister.isEscapedValue();
    }

    public Enum<?> getUnknownEnumVal() {
        return this.fieldConfig.getUnknownEnumValue();
    }

    public String getFormat() {
        return this.fieldConfig.getFormat();
    }

    public boolean isUnique() {
        return this.fieldConfig.isUnique();
    }

    public boolean isUniqueCombo() {
        return this.fieldConfig.isUniqueCombo();
    }

    public String getIndexName() {
        return this.fieldConfig.getIndexName(this.tableName);
    }

    public String getUniqueIndexName() {
        return this.fieldConfig.getUniqueIndexName(this.tableName);
    }

    public boolean isEscapedDefaultValue() {
        return this.dataPersister.isEscapedDefaultValue();
    }

    public boolean isComparable() throws SQLException {
        if (this.fieldConfig.isForeignCollection()) {
            return false;
        }
        DataPersister dataPersister2 = this.dataPersister;
        if (dataPersister2 != null) {
            return dataPersister2.isComparable();
        }
        throw new SQLException("Internal error.  Data-persister is not configured for field.  Please post _full_ exception with associated data objects to mailing list: " + this);
    }

    public boolean isArgumentHolderRequired() {
        return this.dataPersister.isArgumentHolderRequired();
    }

    public boolean isForeignCollection() {
        return this.fieldConfig.isForeignCollection();
    }

    public <FT, FID> BaseForeignCollection<FT, FID> buildForeignCollection(Object obj, FID fid) throws SQLException {
        if (this.foreignFieldType == null) {
            return null;
        }
        BaseDaoImpl<?, ?> baseDaoImpl = this.foreignDao;
        if (!this.fieldConfig.isForeignCollectionEager()) {
            return new LazyForeignCollection(baseDaoImpl, obj, fid, this.foreignFieldType, this.fieldConfig.getForeignCollectionOrderColumnName(), this.fieldConfig.isForeignCollectionOrderAscending());
        }
        ThreadLocal<LevelCounters> threadLocal = threadLevelCounters;
        LevelCounters levelCounters = threadLocal.get();
        if (levelCounters == null) {
            if (this.fieldConfig.getForeignCollectionMaxEagerLevel() == 0) {
                return new LazyForeignCollection(baseDaoImpl, obj, fid, this.foreignFieldType, this.fieldConfig.getForeignCollectionOrderColumnName(), this.fieldConfig.isForeignCollectionOrderAscending());
            }
            levelCounters = new LevelCounters();
            threadLocal.set(levelCounters);
        }
        LevelCounters levelCounters2 = levelCounters;
        if (levelCounters2.foreignCollectionLevel == 0) {
            levelCounters2.foreignCollectionLevelMax = this.fieldConfig.getForeignCollectionMaxEagerLevel();
        }
        if (levelCounters2.foreignCollectionLevel >= levelCounters2.foreignCollectionLevelMax) {
            return new LazyForeignCollection(baseDaoImpl, obj, fid, this.foreignFieldType, this.fieldConfig.getForeignCollectionOrderColumnName(), this.fieldConfig.isForeignCollectionOrderAscending());
        }
        levelCounters2.foreignCollectionLevel++;
        try {
            return new EagerForeignCollection(baseDaoImpl, obj, fid, this.foreignFieldType, this.fieldConfig.getForeignCollectionOrderColumnName(), this.fieldConfig.isForeignCollectionOrderAscending());
        } finally {
            levelCounters2.foreignCollectionLevel--;
        }
    }

    public <T> T resultToJava(DatabaseResults databaseResults, Map<String, Integer> map) throws SQLException {
        Integer num = map.get(this.columnName);
        if (num == null) {
            num = Integer.valueOf(databaseResults.findColumn(this.columnName));
            map.put(this.columnName, num);
        }
        T resultToJava = this.fieldConverter.resultToJava(this, databaseResults, num.intValue());
        if (this.fieldConfig.isForeign()) {
            if (databaseResults.wasNull(num.intValue())) {
                return null;
            }
        } else if (this.dataPersister.isPrimitive()) {
            if (this.fieldConfig.isThrowIfNull() && databaseResults.wasNull(num.intValue())) {
                throw new SQLException("Results value for primitive field '" + this.field.getName() + "' was an invalid null value");
            }
        } else if (this.fieldConverter.isStreamType() || !databaseResults.wasNull(num.intValue())) {
            return resultToJava;
        } else {
            return null;
        }
        return resultToJava;
    }

    public boolean isSelfGeneratedId() {
        return this.dataPersister.isSelfGeneratedId();
    }

    public boolean isAllowGeneratedIdInsert() {
        return this.fieldConfig.isAllowGeneratedIdInsert();
    }

    public String getColumnDefinition() {
        return this.fieldConfig.getColumnDefinition();
    }

    public boolean isForeignAutoCreate() {
        return this.fieldConfig.isForeignAutoCreate();
    }

    public boolean isVersion() {
        return this.fieldConfig.isVersion();
    }

    public Object generateId() {
        return this.dataPersister.generateId();
    }

    public boolean isReadOnly() {
        return this.fieldConfig.isReadOnly();
    }

    public <FV> FV getFieldValueIfNotDefault(Object obj) throws SQLException {
        FV extractJavaFieldValue = extractJavaFieldValue(obj);
        if (isFieldValueDefault(extractJavaFieldValue)) {
            return null;
        }
        return extractJavaFieldValue;
    }

    public boolean isObjectsFieldValueDefault(Object obj) throws SQLException {
        return isFieldValueDefault(extractJavaFieldValue(obj));
    }

    public Object getJavaDefaultValueDefault() {
        if (this.field.getType() == Boolean.TYPE) {
            return Boolean.valueOf(DEFAULT_VALUE_BOOLEAN);
        }
        if (this.field.getType() == Byte.TYPE || this.field.getType() == Byte.class) {
            return Byte.valueOf(DEFAULT_VALUE_BYTE);
        }
        if (this.field.getType() == Character.TYPE || this.field.getType() == Character.class) {
            return Character.valueOf(DEFAULT_VALUE_CHAR);
        }
        if (this.field.getType() == Short.TYPE || this.field.getType() == Short.class) {
            return Short.valueOf(DEFAULT_VALUE_SHORT);
        }
        if (this.field.getType() == Integer.TYPE || this.field.getType() == Integer.class) {
            return Integer.valueOf(DEFAULT_VALUE_INT);
        }
        if (this.field.getType() == Long.TYPE || this.field.getType() == Long.class) {
            return Long.valueOf(DEFAULT_VALUE_LONG);
        }
        if (this.field.getType() == Float.TYPE || this.field.getType() == Float.class) {
            return Float.valueOf(DEFAULT_VALUE_FLOAT);
        }
        if (this.field.getType() == Double.TYPE || this.field.getType() == Double.class) {
            return Double.valueOf(DEFAULT_VALUE_DOUBLE);
        }
        return null;
    }

    public <T> int createWithForeignDao(T t) throws SQLException {
        return this.foreignDao.create(t);
    }

    public static FieldType createFieldType(ConnectionSource connectionSource2, String str, Field field2, Class<?> cls) throws SQLException {
        DatabaseFieldConfig fromField = DatabaseFieldConfig.fromField(connectionSource2.getDatabaseType(), str, field2);
        if (fromField == null) {
            return null;
        }
        return new FieldType(connectionSource2, str, field2, fromField, cls);
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0029 A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = 0
            if (r4 == 0) goto L_0x002a
            java.lang.Class r1 = r4.getClass()
            java.lang.Class r2 = r3.getClass()
            if (r1 == r2) goto L_0x000e
            goto L_0x002a
        L_0x000e:
            com.j256.ormlite.field.FieldType r4 = (com.j256.ormlite.field.FieldType) r4
            java.lang.reflect.Field r1 = r3.field
            java.lang.reflect.Field r2 = r4.field
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x002a
            java.lang.Class<?> r1 = r3.parentClass
            java.lang.Class<?> r4 = r4.parentClass
            if (r1 != 0) goto L_0x0023
            if (r4 != 0) goto L_0x002a
            goto L_0x0029
        L_0x0023:
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x002a
        L_0x0029:
            r0 = 1
        L_0x002a:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.j256.ormlite.field.FieldType.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return this.field.hashCode();
    }

    public String toString() {
        return getClass().getSimpleName() + ":name=" + this.field.getName() + ",class=" + this.field.getDeclaringClass().getSimpleName();
    }

    private Object createForeignObject(Object obj, ObjectCache objectCache) throws SQLException {
        DatabaseConnection readOnlyConnection;
        ThreadLocal<LevelCounters> threadLocal = threadLevelCounters;
        LevelCounters levelCounters = threadLocal.get();
        if (levelCounters == null) {
            if (!this.fieldConfig.isForeignAutoRefresh()) {
                return createForeignShell(obj, objectCache);
            }
            levelCounters = new LevelCounters();
            threadLocal.set(levelCounters);
        }
        if (levelCounters.autoRefreshLevel == 0) {
            if (!this.fieldConfig.isForeignAutoRefresh()) {
                return createForeignShell(obj, objectCache);
            }
            levelCounters.autoRefreshLevelMax = this.fieldConfig.getMaxForeignAutoRefreshLevel();
        }
        if (levelCounters.autoRefreshLevel >= levelCounters.autoRefreshLevelMax) {
            return createForeignShell(obj, objectCache);
        }
        if (this.mappedQueryForForeignField == null) {
            this.mappedQueryForForeignField = MappedQueryForFieldEq.build(this.connectionSource.getDatabaseType(), this.foreignDao.getTableInfo(), this.foreignIdField);
        }
        levelCounters.autoRefreshLevel++;
        try {
            readOnlyConnection = this.connectionSource.getReadOnlyConnection(this.tableName);
            Object execute = this.mappedQueryForForeignField.execute(readOnlyConnection, obj, objectCache);
            this.connectionSource.releaseConnection(readOnlyConnection);
            levelCounters.autoRefreshLevel--;
            if (levelCounters.autoRefreshLevel <= 0) {
                threadLocal.remove();
            }
            return execute;
        } catch (Throwable th) {
            levelCounters.autoRefreshLevel--;
            if (levelCounters.autoRefreshLevel <= 0) {
                threadLevelCounters.remove();
            }
            throw th;
        }
    }

    private Object createForeignShell(Object obj, ObjectCache objectCache) throws SQLException {
        Object createObject = this.foreignTableInfo.createObject();
        this.foreignIdField.assignField(createObject, obj, false, objectCache);
        return createObject;
    }

    private boolean isFieldValueDefault(Object obj) {
        if (obj == null) {
            return true;
        }
        return obj.equals(getJavaDefaultValueDefault());
    }

    private FieldType findForeignFieldType(Class<?> cls, Class<?> cls2, BaseDaoImpl<?, ?> baseDaoImpl) throws SQLException {
        String foreignCollectionForeignFieldName = this.fieldConfig.getForeignCollectionForeignFieldName();
        FieldType[] fieldTypes = baseDaoImpl.getTableInfo().getFieldTypes();
        int length = fieldTypes.length;
        int i = 0;
        while (i < length) {
            FieldType fieldType = fieldTypes[i];
            if (fieldType.getType() != cls2 || (foreignCollectionForeignFieldName != null && !fieldType.getField().getName().equals(foreignCollectionForeignFieldName))) {
                i++;
            } else if (fieldType.fieldConfig.isForeign() || fieldType.fieldConfig.isForeignAutoRefresh()) {
                return fieldType;
            } else {
                throw new SQLException("Foreign collection object " + cls + " for field '" + this.field.getName() + "' contains a field of class " + cls2 + " but it's not foreign");
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Foreign collection class ");
        sb.append(cls.getName());
        sb.append(" for field '");
        sb.append(this.field.getName());
        sb.append("' column-name does not contain a foreign field");
        if (foreignCollectionForeignFieldName != null) {
            sb.append(" named '");
            sb.append(foreignCollectionForeignFieldName);
            sb.append(AngleFormat.CH_MIN_SYMBOL);
        }
        sb.append(" of class ");
        sb.append(cls2.getName());
        throw new SQLException(sb.toString());
    }

    private void assignDataType(DatabaseType databaseType, DataPersister dataPersister2) throws SQLException {
        DataPersister dataPersister3 = databaseType.getDataPersister(dataPersister2, this);
        this.dataPersister = dataPersister3;
        if (dataPersister3 != null) {
            this.fieldConverter = databaseType.getFieldConverter(dataPersister3, this);
            if (this.isGeneratedId && !dataPersister3.isValidGeneratedType()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Generated-id field '");
                sb.append(this.field.getName());
                sb.append("' in ");
                sb.append(this.field.getDeclaringClass().getSimpleName());
                sb.append(" can't be type ");
                sb.append(dataPersister3.getSqlType());
                sb.append(".  Must be one of: ");
                for (DataType dataType : DataType.values()) {
                    DataPersister dataPersister4 = dataType.getDataPersister();
                    if (dataPersister4 != null && dataPersister4.isValidGeneratedType()) {
                        sb.append(dataType);
                        sb.append(' ');
                    }
                }
                throw new IllegalArgumentException(sb.toString());
            } else if (this.fieldConfig.isThrowIfNull() && !dataPersister3.isPrimitive()) {
                throw new SQLException("Field " + this.field.getName() + " must be a primitive if set with throwIfNull");
            } else if (!this.isId || dataPersister3.isAppropriateId()) {
                this.dataTypeConfigObj = dataPersister3.makeConfigObject(this);
                String defaultValue2 = this.fieldConfig.getDefaultValue();
                if (defaultValue2 == null) {
                    this.defaultValue = null;
                } else if (!this.isGeneratedId) {
                    this.defaultValue = this.fieldConverter.parseDefaultString(this, defaultValue2);
                } else {
                    throw new SQLException("Field '" + this.field.getName() + "' cannot be a generatedId and have a default value '" + defaultValue2 + "'");
                }
            } else {
                throw new SQLException("Field '" + this.field.getName() + "' is of data type " + dataPersister3 + " which cannot be the ID field");
            }
        } else if (!this.fieldConfig.isForeign() && !this.fieldConfig.isForeignCollection()) {
            throw new SQLException("Data persister for field " + this + " is null but the field is not a foreign or foreignCollection");
        }
    }

    private static class LevelCounters {
        int autoRefreshLevel;
        int autoRefreshLevelMax;
        int foreignCollectionLevel;
        int foreignCollectionLevelMax;

        private LevelCounters() {
        }
    }
}
