package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

@Metadata(mo19895k = 3, mo19896mv = {1, 6, 0}, mo19898xi = 48)
@DebugMetadata(mo20608c = "kotlinx.coroutines.flow.FlowKt__ReduceKt", mo20609f = "Reduce.kt", mo20610i = {0}, mo20611l = {155}, mo20612m = "last", mo20613n = {"result"}, mo20614s = {"L$0"})
/* compiled from: Reduce.kt */
final class FlowKt__ReduceKt$last$1<T> extends ContinuationImpl {
    Object L$0;
    int label;
    /* synthetic */ Object result;

    FlowKt__ReduceKt$last$1(Continuation<? super FlowKt__ReduceKt$last$1> continuation) {
        super(continuation);
    }

    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return FlowKt.last((Flow) null, this);
    }
}
