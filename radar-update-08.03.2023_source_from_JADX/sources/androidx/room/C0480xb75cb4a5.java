package androidx.room;

import androidx.sqlite.p004db.SupportSQLiteDatabase;
import kotlin.Metadata;
import kotlin.jvm.internal.PropertyReference1Impl;

@Metadata(mo19895k = 3, mo19896mv = {1, 7, 1}, mo19898xi = 48)
/* renamed from: androidx.room.AutoClosingRoomOpenHelper$AutoClosingSupportSQLiteDatabase$maximumSize$1 */
/* compiled from: AutoClosingRoomOpenHelper.kt */
/* synthetic */ class C0480xb75cb4a5 extends PropertyReference1Impl {
    public static final C0480xb75cb4a5 INSTANCE = new C0480xb75cb4a5();

    C0480xb75cb4a5() {
        super(SupportSQLiteDatabase.class, "maximumSize", "getMaximumSize()J", 0);
    }

    public Object get(Object obj) {
        return Long.valueOf(((SupportSQLiteDatabase) obj).getMaximumSize());
    }
}
