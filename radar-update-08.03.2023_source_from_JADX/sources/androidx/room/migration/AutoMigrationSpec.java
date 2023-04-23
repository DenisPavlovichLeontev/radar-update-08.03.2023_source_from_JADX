package androidx.room.migration;

import androidx.sqlite.p004db.SupportSQLiteDatabase;
import com.android.tools.r8.annotations.SynthesizedClassV2;

public interface AutoMigrationSpec {

    @SynthesizedClassV2(kind = 7, versionHash = "5e5398f0546d1d7afd62641edb14d82894f11ddc41bce363a0c8d0dac82c9c5a")
    /* renamed from: androidx.room.migration.AutoMigrationSpec$-CC  reason: invalid class name */
    public final /* synthetic */ class CC {
        public static void $default$onPostMigrate(AutoMigrationSpec _this, SupportSQLiteDatabase supportSQLiteDatabase) {
        }
    }

    void onPostMigrate(SupportSQLiteDatabase supportSQLiteDatabase);
}
