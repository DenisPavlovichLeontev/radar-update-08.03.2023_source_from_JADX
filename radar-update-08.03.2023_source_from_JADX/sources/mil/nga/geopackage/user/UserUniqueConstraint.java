package mil.nga.geopackage.user;

import java.util.ArrayList;
import java.util.List;
import mil.nga.geopackage.user.UserColumn;

public class UserUniqueConstraint<TColumn extends UserColumn> {
    private final List<TColumn> columns = new ArrayList();

    public UserUniqueConstraint() {
    }

    public UserUniqueConstraint(TColumn... tcolumnArr) {
        for (TColumn add : tcolumnArr) {
            add(add);
        }
    }

    public void add(TColumn tcolumn) {
        this.columns.add(tcolumn);
    }

    public List<TColumn> getColumns() {
        return this.columns;
    }
}
