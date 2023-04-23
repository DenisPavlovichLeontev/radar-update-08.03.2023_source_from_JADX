package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

@Metadata(mo19895k = 3, mo19896mv = {1, 6, 0}, mo19898xi = 48)
@DebugMetadata(mo20608c = "kotlinx.coroutines.flow.FlowKt__ReduceKt$reduce$2", mo20609f = "Reduce.kt", mo20610i = {}, mo20611l = {25}, mo20612m = "emit", mo20613n = {}, mo20614s = {})
/* compiled from: Reduce.kt */
final class FlowKt__ReduceKt$reduce$2$emit$1 extends ContinuationImpl {
    Object L$0;
    int label;
    /* synthetic */ Object result;
    final /* synthetic */ FlowKt__ReduceKt$reduce$2<T> this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    FlowKt__ReduceKt$reduce$2$emit$1(FlowKt__ReduceKt$reduce$2<? super T> flowKt__ReduceKt$reduce$2, Continuation<? super FlowKt__ReduceKt$reduce$2$emit$1> continuation) {
        super(continuation);
        this.this$0 = flowKt__ReduceKt$reduce$2;
    }

    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return this.this$0.emit(null, this);
    }
}
