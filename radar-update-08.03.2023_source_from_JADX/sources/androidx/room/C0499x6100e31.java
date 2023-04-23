package androidx.room;

import androidx.sqlite.p004db.SupportSQLiteStatement;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo19893d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, mo19894d2 = {"<anonymous>", "", "obj", "Landroidx/sqlite/db/SupportSQLiteStatement;", "invoke", "(Landroidx/sqlite/db/SupportSQLiteStatement;)Ljava/lang/Long;"}, mo19895k = 3, mo19896mv = {1, 7, 1}, mo19898xi = 48)
/* renamed from: androidx.room.AutoClosingRoomOpenHelper$AutoClosingSupportSqliteStatement$simpleQueryForLong$1 */
/* compiled from: AutoClosingRoomOpenHelper.kt */
final class C0499x6100e31 extends Lambda implements Function1<SupportSQLiteStatement, Long> {
    public static final C0499x6100e31 INSTANCE = new C0499x6100e31();

    C0499x6100e31() {
        super(1);
    }

    public final Long invoke(SupportSQLiteStatement supportSQLiteStatement) {
        Intrinsics.checkNotNullParameter(supportSQLiteStatement, "obj");
        return Long.valueOf(supportSQLiteStatement.simpleQueryForLong());
    }
}
