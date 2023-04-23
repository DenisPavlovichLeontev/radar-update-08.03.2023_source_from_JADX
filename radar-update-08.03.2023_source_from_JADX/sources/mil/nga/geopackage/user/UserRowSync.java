package mil.nga.geopackage.user;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserCoreRow;
import mil.nga.geopackage.user.UserTable;

public class UserRowSync<TColumn extends UserColumn, TTable extends UserTable<TColumn>, TRow extends UserCoreRow<TColumn, TTable>> {
    protected Lock lock = new ReentrantLock();
    protected Map<Long, UserRowSync<TColumn, TTable, TRow>.RowCondition> rows = new HashMap();

    protected class RowCondition {
        /* access modifiers changed from: private */
        public Condition condition;
        /* access modifiers changed from: private */
        public TRow row;

        protected RowCondition() {
        }
    }

    protected UserRowSync() {
    }

    public TRow getRowOrLock(long j) {
        TRow trow;
        this.lock.lock();
        try {
            RowCondition rowCondition = this.rows.get(Long.valueOf(j));
            if (rowCondition != null) {
                trow = rowCondition.row;
                if (trow == null) {
                    rowCondition.condition.await();
                    trow = rowCondition.row;
                }
            } else {
                RowCondition rowCondition2 = new RowCondition();
                Condition unused = rowCondition2.condition = this.lock.newCondition();
                this.rows.put(Long.valueOf(j), rowCondition2);
                trow = null;
            }
            this.lock.unlock();
            return trow;
        } catch (InterruptedException e) {
            throw new GeoPackageException("Interruption obtaining cached row or row lock. id: " + j, e);
        } catch (Throwable th) {
            this.lock.unlock();
            throw th;
        }
    }

    public void setRow(long j, TRow trow) {
        this.lock.lock();
        try {
            RowCondition remove = this.rows.remove(Long.valueOf(j));
            if (remove != null) {
                UserCoreRow unused = remove.row = trow;
                remove.condition.signalAll();
            }
        } finally {
            this.lock.unlock();
        }
    }
}
