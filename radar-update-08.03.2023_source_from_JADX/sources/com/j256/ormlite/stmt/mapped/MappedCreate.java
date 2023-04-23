package com.j256.ormlite.stmt.mapped;

import com.j256.ormlite.dao.ObjectCache;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.logger.Log;
import com.j256.ormlite.p006db.DatabaseType;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.GeneratedKeyHolder;
import com.j256.ormlite.table.TableInfo;
import java.sql.SQLException;
import java.util.List;

public class MappedCreate<T, ID> extends BaseMappedStatement<T, ID> {
    private String dataClassName;
    private final String queryNextSequenceStmt;
    private int versionFieldTypeIndex;

    private MappedCreate(TableInfo<T, ID> tableInfo, String str, FieldType[] fieldTypeArr, String str2, int i) {
        super(tableInfo, str, fieldTypeArr);
        this.dataClassName = tableInfo.getDataClass().getSimpleName();
        this.queryNextSequenceStmt = str2;
        this.versionFieldTypeIndex = i;
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x0066 A[Catch:{ SQLException -> 0x0120, SQLException -> 0x0138 }] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x009b A[Catch:{ SQLException -> 0x0120, SQLException -> 0x0138 }] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00ae  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00cc A[Catch:{ SQLException -> 0x0120, SQLException -> 0x0138 }] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00d3 A[Catch:{ SQLException -> 0x0120, SQLException -> 0x0138 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int insert(com.j256.ormlite.p006db.DatabaseType r11, com.j256.ormlite.support.DatabaseConnection r12, T r13, com.j256.ormlite.dao.ObjectCache r14) throws java.sql.SQLException {
        /*
            r10 = this;
            java.lang.String r0 = "insert arguments: {}"
            com.j256.ormlite.field.FieldType r1 = r10.idField
            r2 = 0
            r3 = 0
            if (r1 == 0) goto L_0x005d
            com.j256.ormlite.field.FieldType r1 = r10.idField
            boolean r1 = r1.isAllowGeneratedIdInsert()
            if (r1 == 0) goto L_0x001a
            com.j256.ormlite.field.FieldType r1 = r10.idField
            boolean r1 = r1.isObjectsFieldValueDefault(r13)
            if (r1 != 0) goto L_0x001a
            r1 = r2
            goto L_0x001b
        L_0x001a:
            r1 = 1
        L_0x001b:
            com.j256.ormlite.field.FieldType r4 = r10.idField
            boolean r4 = r4.isSelfGeneratedId()
            if (r4 == 0) goto L_0x0039
            com.j256.ormlite.field.FieldType r4 = r10.idField
            boolean r4 = r4.isGeneratedId()
            if (r4 == 0) goto L_0x0039
            if (r1 == 0) goto L_0x005d
            com.j256.ormlite.field.FieldType r11 = r10.idField
            com.j256.ormlite.field.FieldType r1 = r10.idField
            java.lang.Object r1 = r1.generateId()
            r11.assignField(r13, r1, r2, r14)
            goto L_0x005d
        L_0x0039:
            com.j256.ormlite.field.FieldType r4 = r10.idField
            boolean r4 = r4.isGeneratedIdSequence()
            if (r4 == 0) goto L_0x004d
            boolean r11 = r11.isSelectSequenceBeforeInsert()
            if (r11 == 0) goto L_0x004d
            if (r1 == 0) goto L_0x005d
            r10.assignSequenceId(r12, r13, r14)
            goto L_0x005d
        L_0x004d:
            com.j256.ormlite.field.FieldType r11 = r10.idField
            boolean r11 = r11.isGeneratedId()
            if (r11 == 0) goto L_0x005d
            if (r1 == 0) goto L_0x005d
            com.j256.ormlite.stmt.mapped.MappedCreate$KeyHolder r11 = new com.j256.ormlite.stmt.mapped.MappedCreate$KeyHolder
            r11.<init>()
            goto L_0x005e
        L_0x005d:
            r11 = r3
        L_0x005e:
            com.j256.ormlite.table.TableInfo r1 = r10.tableInfo     // Catch:{ SQLException -> 0x0138 }
            boolean r1 = r1.isForeignAutoCreate()     // Catch:{ SQLException -> 0x0138 }
            if (r1 == 0) goto L_0x008f
            com.j256.ormlite.table.TableInfo r1 = r10.tableInfo     // Catch:{ SQLException -> 0x0138 }
            com.j256.ormlite.field.FieldType[] r1 = r1.getFieldTypes()     // Catch:{ SQLException -> 0x0138 }
            int r4 = r1.length     // Catch:{ SQLException -> 0x0138 }
            r5 = r2
        L_0x006e:
            if (r5 >= r4) goto L_0x008f
            r6 = r1[r5]     // Catch:{ SQLException -> 0x0138 }
            boolean r7 = r6.isForeignAutoCreate()     // Catch:{ SQLException -> 0x0138 }
            if (r7 != 0) goto L_0x0079
            goto L_0x008c
        L_0x0079:
            java.lang.Object r7 = r6.extractRawJavaFieldValue(r13)     // Catch:{ SQLException -> 0x0138 }
            if (r7 == 0) goto L_0x008c
            com.j256.ormlite.field.FieldType r8 = r6.getForeignIdField()     // Catch:{ SQLException -> 0x0138 }
            boolean r8 = r8.isObjectsFieldValueDefault(r7)     // Catch:{ SQLException -> 0x0138 }
            if (r8 == 0) goto L_0x008c
            r6.createWithForeignDao(r7)     // Catch:{ SQLException -> 0x0138 }
        L_0x008c:
            int r5 = r5 + 1
            goto L_0x006e
        L_0x008f:
            java.lang.Object[] r1 = r10.getFieldObjects(r13)     // Catch:{ SQLException -> 0x0138 }
            int r4 = r10.versionFieldTypeIndex     // Catch:{ SQLException -> 0x0138 }
            if (r4 < 0) goto L_0x00ae
            r4 = r1[r4]     // Catch:{ SQLException -> 0x0138 }
            if (r4 != 0) goto L_0x00ae
            com.j256.ormlite.field.FieldType[] r4 = r10.argFieldTypes     // Catch:{ SQLException -> 0x0138 }
            int r5 = r10.versionFieldTypeIndex     // Catch:{ SQLException -> 0x0138 }
            r4 = r4[r5]     // Catch:{ SQLException -> 0x0138 }
            java.lang.Object r5 = r4.moveToNextValue(r3)     // Catch:{ SQLException -> 0x0138 }
            int r6 = r10.versionFieldTypeIndex     // Catch:{ SQLException -> 0x0138 }
            java.lang.Object r4 = r4.convertJavaFieldToSqlArgValue(r5)     // Catch:{ SQLException -> 0x0138 }
            r1[r6] = r4     // Catch:{ SQLException -> 0x0138 }
            goto L_0x00af
        L_0x00ae:
            r5 = r3
        L_0x00af:
            java.lang.String r4 = r10.statement     // Catch:{ SQLException -> 0x0120 }
            com.j256.ormlite.field.FieldType[] r6 = r10.argFieldTypes     // Catch:{ SQLException -> 0x0120 }
            int r12 = r12.insert(r4, r1, r6, r11)     // Catch:{ SQLException -> 0x0120 }
            com.j256.ormlite.logger.Logger r4 = logger     // Catch:{ SQLException -> 0x0138 }
            java.lang.String r6 = "insert data with statement '{}' and {} args, changed {} rows"
            java.lang.String r7 = r10.statement     // Catch:{ SQLException -> 0x0138 }
            int r8 = r1.length     // Catch:{ SQLException -> 0x0138 }
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch:{ SQLException -> 0x0138 }
            java.lang.Integer r9 = java.lang.Integer.valueOf(r12)     // Catch:{ SQLException -> 0x0138 }
            r4.debug((java.lang.String) r6, (java.lang.Object) r7, (java.lang.Object) r8, (java.lang.Object) r9)     // Catch:{ SQLException -> 0x0138 }
            int r4 = r1.length     // Catch:{ SQLException -> 0x0138 }
            if (r4 <= 0) goto L_0x00d1
            com.j256.ormlite.logger.Logger r4 = logger     // Catch:{ SQLException -> 0x0138 }
            r4.trace((java.lang.String) r0, (java.lang.Object) r1)     // Catch:{ SQLException -> 0x0138 }
        L_0x00d1:
            if (r12 <= 0) goto L_0x011f
            if (r5 == 0) goto L_0x00de
            com.j256.ormlite.field.FieldType[] r0 = r10.argFieldTypes     // Catch:{ SQLException -> 0x0138 }
            int r1 = r10.versionFieldTypeIndex     // Catch:{ SQLException -> 0x0138 }
            r0 = r0[r1]     // Catch:{ SQLException -> 0x0138 }
            r0.assignField(r13, r5, r2, r3)     // Catch:{ SQLException -> 0x0138 }
        L_0x00de:
            if (r11 == 0) goto L_0x0106
            java.lang.Number r11 = r11.getKey()     // Catch:{ SQLException -> 0x0138 }
            if (r11 == 0) goto L_0x00fe
            long r0 = r11.longValue()     // Catch:{ SQLException -> 0x0138 }
            r2 = 0
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 == 0) goto L_0x00f6
            java.lang.String r0 = "keyholder"
            r10.assignIdValue(r13, r11, r0, r14)     // Catch:{ SQLException -> 0x0138 }
            goto L_0x0106
        L_0x00f6:
            java.sql.SQLException r11 = new java.sql.SQLException     // Catch:{ SQLException -> 0x0138 }
            java.lang.String r12 = "generated-id key must not be 0 value, maybe a schema mismatch between entity and database table?"
            r11.<init>(r12)     // Catch:{ SQLException -> 0x0138 }
            throw r11     // Catch:{ SQLException -> 0x0138 }
        L_0x00fe:
            java.sql.SQLException r11 = new java.sql.SQLException     // Catch:{ SQLException -> 0x0138 }
            java.lang.String r12 = "generated-id key was not set by the update call, maybe a schema mismatch between entity and database table?"
            r11.<init>(r12)     // Catch:{ SQLException -> 0x0138 }
            throw r11     // Catch:{ SQLException -> 0x0138 }
        L_0x0106:
            if (r14 == 0) goto L_0x011f
            com.j256.ormlite.table.TableInfo r11 = r10.tableInfo     // Catch:{ SQLException -> 0x0138 }
            com.j256.ormlite.field.FieldType[] r11 = r11.getForeignCollections()     // Catch:{ SQLException -> 0x0138 }
            boolean r11 = r10.foreignCollectionsAreAssigned(r11, r13)     // Catch:{ SQLException -> 0x0138 }
            if (r11 == 0) goto L_0x011f
            com.j256.ormlite.field.FieldType r11 = r10.idField     // Catch:{ SQLException -> 0x0138 }
            java.lang.Object r11 = r11.extractJavaFieldValue(r13)     // Catch:{ SQLException -> 0x0138 }
            java.lang.Class r0 = r10.clazz     // Catch:{ SQLException -> 0x0138 }
            r14.put(r0, r11, r13)     // Catch:{ SQLException -> 0x0138 }
        L_0x011f:
            return r12
        L_0x0120:
            r11 = move-exception
            com.j256.ormlite.logger.Logger r12 = logger     // Catch:{ SQLException -> 0x0138 }
            java.lang.String r14 = "insert data with statement '{}' and {} args, threw exception: {}"
            java.lang.String r2 = r10.statement     // Catch:{ SQLException -> 0x0138 }
            int r3 = r1.length     // Catch:{ SQLException -> 0x0138 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ SQLException -> 0x0138 }
            r12.debug((java.lang.String) r14, (java.lang.Object) r2, (java.lang.Object) r3, (java.lang.Object) r11)     // Catch:{ SQLException -> 0x0138 }
            int r12 = r1.length     // Catch:{ SQLException -> 0x0138 }
            if (r12 <= 0) goto L_0x0137
            com.j256.ormlite.logger.Logger r12 = logger     // Catch:{ SQLException -> 0x0138 }
            r12.trace((java.lang.String) r0, (java.lang.Object) r1)     // Catch:{ SQLException -> 0x0138 }
        L_0x0137:
            throw r11     // Catch:{ SQLException -> 0x0138 }
        L_0x0138:
            r11 = move-exception
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r14 = "Unable to run insert stmt on object "
            r12.append(r14)
            r12.append(r13)
            java.lang.String r13 = ": "
            r12.append(r13)
            java.lang.String r13 = r10.statement
            r12.append(r13)
            java.lang.String r12 = r12.toString()
            java.sql.SQLException r11 = com.j256.ormlite.misc.SqlExceptionUtil.create(r12, r11)
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.j256.ormlite.stmt.mapped.MappedCreate.insert(com.j256.ormlite.db.DatabaseType, com.j256.ormlite.support.DatabaseConnection, java.lang.Object, com.j256.ormlite.dao.ObjectCache):int");
    }

    public static <T, ID> MappedCreate<T, ID> build(DatabaseType databaseType, TableInfo<T, ID> tableInfo) {
        StringBuilder sb = new StringBuilder(128);
        appendTableName(databaseType, sb, "INSERT INTO ", tableInfo.getTableName());
        int i = 0;
        int i2 = -1;
        for (FieldType fieldType : tableInfo.getFieldTypes()) {
            if (isFieldCreatable(databaseType, fieldType)) {
                if (fieldType.isVersion()) {
                    i2 = i;
                }
                i++;
            }
        }
        FieldType[] fieldTypeArr = new FieldType[i];
        if (i == 0) {
            databaseType.appendInsertNoColumns(sb);
        } else {
            sb.append('(');
            boolean z = true;
            int i3 = 0;
            boolean z2 = true;
            for (FieldType fieldType2 : tableInfo.getFieldTypes()) {
                if (isFieldCreatable(databaseType, fieldType2)) {
                    if (z2) {
                        z2 = false;
                    } else {
                        sb.append(',');
                    }
                    appendFieldColumnName(databaseType, sb, fieldType2, (List<FieldType>) null);
                    fieldTypeArr[i3] = fieldType2;
                    i3++;
                }
            }
            sb.append(") VALUES (");
            for (FieldType isFieldCreatable : tableInfo.getFieldTypes()) {
                if (isFieldCreatable(databaseType, isFieldCreatable)) {
                    if (z) {
                        z = false;
                    } else {
                        sb.append(',');
                    }
                    sb.append('?');
                }
            }
            sb.append(')');
        }
        return new MappedCreate(tableInfo, sb.toString(), fieldTypeArr, buildQueryNextSequence(databaseType, tableInfo.getIdField()), i2);
    }

    private boolean foreignCollectionsAreAssigned(FieldType[] fieldTypeArr, Object obj) throws SQLException {
        for (FieldType extractJavaFieldValue : fieldTypeArr) {
            if (extractJavaFieldValue.extractJavaFieldValue(obj) == null) {
                return false;
            }
        }
        return true;
    }

    private static boolean isFieldCreatable(DatabaseType databaseType, FieldType fieldType) {
        if (fieldType.isForeignCollection() || fieldType.isReadOnly()) {
            return false;
        }
        if ((!databaseType.isIdSequenceNeeded() || !databaseType.isSelectSequenceBeforeInsert()) && fieldType.isGeneratedId() && !fieldType.isSelfGeneratedId() && !fieldType.isAllowGeneratedIdInsert()) {
            return false;
        }
        return true;
    }

    private static String buildQueryNextSequence(DatabaseType databaseType, FieldType fieldType) {
        String generatedIdSequence;
        if (fieldType == null || (generatedIdSequence = fieldType.getGeneratedIdSequence()) == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(64);
        databaseType.appendSelectNextValFromSequence(sb, generatedIdSequence);
        return sb.toString();
    }

    private void assignSequenceId(DatabaseConnection databaseConnection, T t, ObjectCache objectCache) throws SQLException {
        long queryForLong = databaseConnection.queryForLong(this.queryNextSequenceStmt);
        logger.debug("queried for sequence {} using stmt: {}", (Object) Long.valueOf(queryForLong), (Object) this.queryNextSequenceStmt);
        if (queryForLong != 0) {
            assignIdValue(t, Long.valueOf(queryForLong), "sequence", objectCache);
            return;
        }
        throw new SQLException("Should not have returned 0 for stmt: " + this.queryNextSequenceStmt);
    }

    private void assignIdValue(T t, Number number, String str, ObjectCache objectCache) throws SQLException {
        this.idField.assignIdValue(t, number, objectCache);
        if (logger.isLevelEnabled(Log.Level.DEBUG)) {
            logger.debug("assigned id '{}' from {} to '{}' in {} object", new Object[]{number, str, this.idField.getFieldName(), this.dataClassName});
        }
    }

    private static class KeyHolder implements GeneratedKeyHolder {
        Number key;

        private KeyHolder() {
        }

        public Number getKey() {
            return this.key;
        }

        public void addKey(Number number) throws SQLException {
            if (this.key == null) {
                this.key = number;
                return;
            }
            throw new SQLException("generated key has already been set to " + this.key + ", now set to " + number);
        }
    }
}
