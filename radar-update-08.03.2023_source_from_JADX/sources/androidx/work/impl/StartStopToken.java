package androidx.work.impl;

import androidx.work.impl.model.WorkGenerationalId;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo19893d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, mo19894d2 = {"Landroidx/work/impl/StartStopToken;", "", "id", "Landroidx/work/impl/model/WorkGenerationalId;", "(Landroidx/work/impl/model/WorkGenerationalId;)V", "getId", "()Landroidx/work/impl/model/WorkGenerationalId;", "work-runtime_release"}, mo19895k = 1, mo19896mv = {1, 7, 1}, mo19898xi = 48)
/* compiled from: StartStopToken.kt */
public final class StartStopToken {

    /* renamed from: id */
    private final WorkGenerationalId f107id;

    public StartStopToken(WorkGenerationalId workGenerationalId) {
        Intrinsics.checkNotNullParameter(workGenerationalId, "id");
        this.f107id = workGenerationalId;
    }

    public final WorkGenerationalId getId() {
        return this.f107id;
    }
}
