package androidx.room;

import androidx.sqlite.p004db.SupportSQLiteDatabase;
import kotlin.Metadata;
import kotlin.jvm.internal.MutablePropertyReference1Impl;

@Metadata(mo19895k = 3, mo19896mv = {1, 7, 1}, mo19898xi = 48)
/* renamed from: androidx.room.AutoClosingRoomOpenHelper$AutoClosingSupportSQLiteDatabase$version$1 */
/* compiled from: AutoClosingRoomOpenHelper.kt */
/* synthetic */ class C0491xff8530fc extends MutablePropertyReference1Impl {
    public static final C0491xff8530fc INSTANCE = new C0491xff8530fc();

    C0491xff8530fc() {
        super(SupportSQLiteDatabase.class, "version", "getVersion()I", 0);
    }

    public Object get(Object obj) {
        return Integer.valueOf(((SupportSQLiteDatabase) obj).getVersion());
    }

    public void set(Object obj, Object obj2) {
        ((SupportSQLiteDatabase) obj).setVersion(((Number) obj2).intValue());
    }
}
