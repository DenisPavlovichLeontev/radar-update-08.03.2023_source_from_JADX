package androidx.room;

import androidx.room.AutoClosingRoomOpenHelper;
import androidx.sqlite.p004db.SupportSQLiteDatabase;
import androidx.sqlite.p004db.SupportSQLiteStatement;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo19893d1 = {"\u0000\f\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, mo19894d2 = {"<anonymous>", "T", "db", "Landroidx/sqlite/db/SupportSQLiteDatabase;", "invoke", "(Landroidx/sqlite/db/SupportSQLiteDatabase;)Ljava/lang/Object;"}, mo19895k = 3, mo19896mv = {1, 7, 1}, mo19898xi = 48)
/* renamed from: androidx.room.AutoClosingRoomOpenHelper$AutoClosingSupportSqliteStatement$executeSqliteStatementWithRefCount$1 */
/* compiled from: AutoClosingRoomOpenHelper.kt */
final class C0497xdd6c1cf2 extends Lambda implements Function1<SupportSQLiteDatabase, T> {
    final /* synthetic */ Function1<SupportSQLiteStatement, T> $block;
    final /* synthetic */ AutoClosingRoomOpenHelper.AutoClosingSupportSqliteStatement this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    C0497xdd6c1cf2(AutoClosingRoomOpenHelper.AutoClosingSupportSqliteStatement autoClosingSupportSqliteStatement, Function1<? super SupportSQLiteStatement, ? extends T> function1) {
        super(1);
        this.this$0 = autoClosingSupportSqliteStatement;
        this.$block = function1;
    }

    public final T invoke(SupportSQLiteDatabase supportSQLiteDatabase) {
        Intrinsics.checkNotNullParameter(supportSQLiteDatabase, "db");
        SupportSQLiteStatement compileStatement = supportSQLiteDatabase.compileStatement(this.this$0.sql);
        this.this$0.doBinds(compileStatement);
        return this.$block.invoke(compileStatement);
    }
}
