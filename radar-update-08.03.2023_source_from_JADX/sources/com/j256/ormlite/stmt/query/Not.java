package com.j256.ormlite.stmt.query;

import com.j256.ormlite.p006db.DatabaseType;
import com.j256.ormlite.stmt.ArgumentHolder;
import java.sql.SQLException;
import java.util.List;

public class Not implements Clause, NeedsFutureClause {
    private Comparison comparison = null;
    private Exists exists = null;

    public Not() {
    }

    public Not(Clause clause) {
        setMissingClause(clause);
    }

    public void setMissingClause(Clause clause) {
        if (this.comparison != null) {
            throw new IllegalArgumentException("NOT operation already has a comparison set");
        } else if (clause instanceof Comparison) {
            this.comparison = (Comparison) clause;
        } else if (clause instanceof Exists) {
            this.exists = (Exists) clause;
        } else {
            throw new IllegalArgumentException("NOT operation can only work with comparison SQL clauses, not " + clause);
        }
    }

    public void appendSql(DatabaseType databaseType, String str, StringBuilder sb, List<ArgumentHolder> list) throws SQLException {
        Comparison comparison2 = this.comparison;
        if (comparison2 == null && this.exists == null) {
            throw new IllegalStateException("Clause has not been set in NOT operation");
        }
        if (comparison2 == null) {
            sb.append("(NOT ");
            this.exists.appendSql(databaseType, str, sb, list);
        } else {
            sb.append("(NOT ");
            if (str != null) {
                databaseType.appendEscapedEntityName(sb, str);
                sb.append('.');
            }
            databaseType.appendEscapedEntityName(sb, this.comparison.getColumnName());
            sb.append(' ');
            this.comparison.appendOperation(sb);
            this.comparison.appendValue(databaseType, sb, list);
        }
        sb.append(") ");
    }

    public String toString() {
        if (this.comparison == null) {
            return "NOT without comparison";
        }
        return "NOT comparison " + this.comparison;
    }
}
