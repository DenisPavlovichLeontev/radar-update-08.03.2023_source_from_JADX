package androidx.sqlite.p004db.framework;

import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import androidx.sqlite.p004db.SupportSQLiteOpenHelper;
import androidx.sqlite.p004db.framework.FrameworkSQLiteOpenHelper;

/* renamed from: androidx.sqlite.db.framework.FrameworkSQLiteOpenHelper$OpenHelper$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class FrameworkSQLiteOpenHelper$OpenHelper$$ExternalSyntheticLambda0 implements DatabaseErrorHandler {
    public final /* synthetic */ SupportSQLiteOpenHelper.Callback f$0;
    public final /* synthetic */ FrameworkSQLiteOpenHelper.DBRefHolder f$1;

    public /* synthetic */ FrameworkSQLiteOpenHelper$OpenHelper$$ExternalSyntheticLambda0(SupportSQLiteOpenHelper.Callback callback, FrameworkSQLiteOpenHelper.DBRefHolder dBRefHolder) {
        this.f$0 = callback;
        this.f$1 = dBRefHolder;
    }

    public final void onCorruption(SQLiteDatabase sQLiteDatabase) {
        FrameworkSQLiteOpenHelper.OpenHelper._init_$lambda$0(this.f$0, this.f$1, sQLiteDatabase);
    }
}
