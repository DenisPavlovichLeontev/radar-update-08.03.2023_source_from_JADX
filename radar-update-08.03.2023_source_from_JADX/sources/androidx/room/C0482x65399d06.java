package androidx.room;

import androidx.sqlite.p004db.SupportSQLiteDatabase;
import kotlin.Metadata;
import kotlin.jvm.internal.MutablePropertyReference1Impl;

@Metadata(mo19895k = 3, mo19896mv = {1, 7, 1}, mo19898xi = 48)
/* renamed from: androidx.room.AutoClosingRoomOpenHelper$AutoClosingSupportSQLiteDatabase$pageSize$1 */
/* compiled from: AutoClosingRoomOpenHelper.kt */
/* synthetic */ class C0482x65399d06 extends MutablePropertyReference1Impl {
    public static final C0482x65399d06 INSTANCE = new C0482x65399d06();

    C0482x65399d06() {
        super(SupportSQLiteDatabase.class, "pageSize", "getPageSize()J", 0);
    }

    public Object get(Object obj) {
        return Long.valueOf(((SupportSQLiteDatabase) obj).getPageSize());
    }

    public void set(Object obj, Object obj2) {
        ((SupportSQLiteDatabase) obj).setPageSize(((Number) obj2).longValue());
    }
}
