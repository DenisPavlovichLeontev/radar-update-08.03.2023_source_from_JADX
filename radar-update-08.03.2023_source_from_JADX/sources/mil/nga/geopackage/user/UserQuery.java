package mil.nga.geopackage.user;

import java.util.HashMap;
import java.util.Map;

public class UserQuery {
    private final Map<UserQueryParamType, Object> params;

    public UserQuery() {
        this.params = new HashMap();
    }

    public UserQuery(String str, String[] strArr) {
        this.params = new HashMap();
        if (str != null) {
            set(UserQueryParamType.SQL, str);
        }
        if (strArr != null) {
            set(UserQueryParamType.SELECTION_ARGS, strArr);
        }
    }

    public UserQuery(String str, String[] strArr, String str2, String[] strArr2, String str3, String str4, String str5) {
        this(str, strArr, (String[]) null, str2, strArr2, str3, str4, str5, (String) null);
    }

    public UserQuery(String str, String[] strArr, String[] strArr2, String str2, String[] strArr3, String str3, String str4, String str5) {
        this(str, strArr, strArr2, str2, strArr3, str3, str4, str5, (String) null);
    }

    public UserQuery(String str, String[] strArr, String str2, String[] strArr2, String str3, String str4, String str5, String str6) {
        this(str, strArr, (String[]) null, str2, strArr2, str3, str4, str5, str6);
    }

    public UserQuery(String str, String[] strArr, String[] strArr2, String str2, String[] strArr3, String str3, String str4, String str5, String str6) {
        this.params = new HashMap();
        if (str != null) {
            set(UserQueryParamType.TABLE, str);
        }
        if (strArr != null) {
            set(UserQueryParamType.COLUMNS, strArr);
        }
        if (strArr2 != null) {
            set(UserQueryParamType.COLUMNS_AS, strArr2);
        }
        if (str2 != null) {
            set(UserQueryParamType.SELECTION, str2);
        }
        if (strArr3 != null) {
            set(UserQueryParamType.SELECTION_ARGS, strArr3);
        }
        if (str3 != null) {
            set(UserQueryParamType.GROUP_BY, str3);
        }
        if (str4 != null) {
            set(UserQueryParamType.HAVING, str4);
        }
        if (str5 != null) {
            set(UserQueryParamType.ORDER_BY, str5);
        }
        if (str6 != null) {
            set(UserQueryParamType.LIMIT, str6);
        }
    }

    public void set(UserQueryParamType userQueryParamType, Object obj) {
        this.params.put(userQueryParamType, obj);
    }

    public Object get(UserQueryParamType userQueryParamType) {
        return this.params.get(userQueryParamType);
    }

    public boolean has(UserQueryParamType userQueryParamType) {
        return get(userQueryParamType) != null;
    }

    public String getSql() {
        return (String) get(UserQueryParamType.SQL);
    }

    public String[] getSelectionArgs() {
        return (String[]) get(UserQueryParamType.SELECTION_ARGS);
    }

    public String getTable() {
        return (String) get(UserQueryParamType.TABLE);
    }

    public String[] getColumns() {
        return (String[]) get(UserQueryParamType.COLUMNS);
    }

    public String[] getColumnsAs() {
        return (String[]) get(UserQueryParamType.COLUMNS_AS);
    }

    public String getSelection() {
        return (String) get(UserQueryParamType.SELECTION);
    }

    public String getGroupBy() {
        return (String) get(UserQueryParamType.GROUP_BY);
    }

    public String getHaving() {
        return (String) get(UserQueryParamType.HAVING);
    }

    public String getOrderBy() {
        return (String) get(UserQueryParamType.ORDER_BY);
    }

    public String getLimit() {
        return (String) get(UserQueryParamType.LIMIT);
    }
}
