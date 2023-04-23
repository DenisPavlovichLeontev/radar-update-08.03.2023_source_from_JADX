package androidx.room;

import androidx.sqlite.p004db.SupportSQLiteStatement;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo19893d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo19894d2 = {"<anonymous>", "", "statement", "Landroidx/sqlite/db/SupportSQLiteStatement;", "invoke"}, mo19895k = 3, mo19896mv = {1, 7, 1}, mo19898xi = 48)
/* renamed from: androidx.room.AutoClosingRoomOpenHelper$AutoClosingSupportSqliteStatement$execute$1 */
/* compiled from: AutoClosingRoomOpenHelper.kt */
final class C0495xc356304d extends Lambda implements Function1<SupportSQLiteStatement, Object> {
    public static final C0495xc356304d INSTANCE = new C0495xc356304d();

    C0495xc356304d() {
        super(1);
    }

    public final Object invoke(SupportSQLiteStatement supportSQLiteStatement) {
        Intrinsics.checkNotNullParameter(supportSQLiteStatement, "statement");
        supportSQLiteStatement.execute();
        return null;
    }
}
