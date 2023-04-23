package androidx.room;

import androidx.sqlite.p004db.SupportSQLiteOpenHelper;
import kotlin.Metadata;

@Metadata(mo19893d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b`\u0018\u00002\u00020\u0001R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0006À\u0006\u0001"}, mo19894d2 = {"Landroidx/room/DelegatingOpenHelper;", "", "delegate", "Landroidx/sqlite/db/SupportSQLiteOpenHelper;", "getDelegate", "()Landroidx/sqlite/db/SupportSQLiteOpenHelper;", "room-runtime_release"}, mo19895k = 1, mo19896mv = {1, 7, 1}, mo19898xi = 48)
/* compiled from: DelegatingOpenHelper.kt */
public interface DelegatingOpenHelper {
    SupportSQLiteOpenHelper getDelegate();
}
