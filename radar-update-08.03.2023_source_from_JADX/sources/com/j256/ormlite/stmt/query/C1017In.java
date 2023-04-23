package com.j256.ormlite.stmt.query;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.p006db.DatabaseType;
import com.j256.ormlite.stmt.ArgumentHolder;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/* renamed from: com.j256.ormlite.stmt.query.In */
public class C1017In extends BaseComparison {

    /* renamed from: in */
    private final boolean f284in;
    private Iterable<?> objects;

    public /* bridge */ /* synthetic */ void appendSql(DatabaseType databaseType, String str, StringBuilder sb, List list) throws SQLException {
        super.appendSql(databaseType, str, sb, list);
    }

    public /* bridge */ /* synthetic */ String getColumnName() {
        return super.getColumnName();
    }

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public C1017In(String str, FieldType fieldType, Iterable<?> iterable, boolean z) throws SQLException {
        super(str, fieldType, (Object) null, true);
        this.objects = iterable;
        this.f284in = z;
    }

    public C1017In(String str, FieldType fieldType, Object[] objArr, boolean z) throws SQLException {
        super(str, fieldType, (Object) null, true);
        this.objects = Arrays.asList(objArr);
        this.f284in = z;
    }

    public void appendOperation(StringBuilder sb) {
        if (this.f284in) {
            sb.append("IN ");
        } else {
            sb.append("NOT IN ");
        }
    }

    public void appendValue(DatabaseType databaseType, StringBuilder sb, List<ArgumentHolder> list) throws SQLException {
        sb.append('(');
        boolean z = true;
        for (Object next : this.objects) {
            if (next != null) {
                if (z) {
                    z = false;
                } else {
                    sb.append(',');
                }
                super.appendArgOrValue(databaseType, this.fieldType, sb, list, next);
            } else {
                throw new IllegalArgumentException("one of the IN values for '" + this.columnName + "' is null");
            }
        }
        sb.append(") ");
    }
}
