package androidx.work.impl.model;

import androidx.work.Data;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo19893d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006R\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u000b"}, mo19894d2 = {"Landroidx/work/impl/model/WorkProgress;", "", "workSpecId", "", "progress", "Landroidx/work/Data;", "(Ljava/lang/String;Landroidx/work/Data;)V", "getProgress", "()Landroidx/work/Data;", "getWorkSpecId", "()Ljava/lang/String;", "work-runtime_release"}, mo19895k = 1, mo19896mv = {1, 7, 1}, mo19898xi = 48)
/* compiled from: WorkProgress.kt */
public final class WorkProgress {
    private final Data progress;
    private final String workSpecId;

    public WorkProgress(String str, Data data) {
        Intrinsics.checkNotNullParameter(str, "workSpecId");
        Intrinsics.checkNotNullParameter(data, "progress");
        this.workSpecId = str;
        this.progress = data;
    }

    public final String getWorkSpecId() {
        return this.workSpecId;
    }

    public final Data getProgress() {
        return this.progress;
    }
}
