package androidx.work.impl.constraints;

import androidx.work.impl.model.WorkSpec;
import java.util.List;
import kotlin.Metadata;

@Metadata(mo19893d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H&J\u0016\u0010\u0007\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H&¨\u0006\b"}, mo19894d2 = {"Landroidx/work/impl/constraints/WorkConstraintsCallback;", "", "onAllConstraintsMet", "", "workSpecs", "", "Landroidx/work/impl/model/WorkSpec;", "onAllConstraintsNotMet", "work-runtime_release"}, mo19895k = 1, mo19896mv = {1, 7, 1}, mo19898xi = 48)
/* compiled from: WorkConstraintsCallback.kt */
public interface WorkConstraintsCallback {
    void onAllConstraintsMet(List<WorkSpec> list);

    void onAllConstraintsNotMet(List<WorkSpec> list);
}
