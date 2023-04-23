package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

@Metadata(mo19895k = 3, mo19896mv = {1, 6, 0}, mo19898xi = 48)
@DebugMetadata(mo20608c = "kotlinx.coroutines.flow.FlowKt__ReduceKt", mo20609f = "Reduce.kt", mo20610i = {0, 0}, mo20611l = {183}, mo20612m = "first", mo20613n = {"result", "collector$iv"}, mo20614s = {"L$0", "L$1"})
/* compiled from: Reduce.kt */
final class FlowKt__ReduceKt$first$1<T> extends ContinuationImpl {
    Object L$0;
    Object L$1;
    int label;
    /* synthetic */ Object result;

    FlowKt__ReduceKt$first$1(Continuation<? super FlowKt__ReduceKt$first$1> continuation) {
        super(continuation);
    }

    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return FlowKt.first((Flow) null, this);
    }
}
