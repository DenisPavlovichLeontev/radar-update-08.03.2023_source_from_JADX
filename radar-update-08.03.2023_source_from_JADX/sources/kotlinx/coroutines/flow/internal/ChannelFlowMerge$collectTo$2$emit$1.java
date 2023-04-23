package kotlinx.coroutines.flow.internal;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlinx.coroutines.flow.Flow;

@Metadata(mo19895k = 3, mo19896mv = {1, 6, 0}, mo19898xi = 48)
@DebugMetadata(mo20608c = "kotlinx.coroutines.flow.internal.ChannelFlowMerge$collectTo$2", mo20609f = "Merge.kt", mo20610i = {0, 0}, mo20611l = {66}, mo20612m = "emit", mo20613n = {"this", "inner"}, mo20614s = {"L$0", "L$1"})
/* compiled from: Merge.kt */
final class ChannelFlowMerge$collectTo$2$emit$1 extends ContinuationImpl {
    Object L$0;
    Object L$1;
    int label;
    /* synthetic */ Object result;
    final /* synthetic */ ChannelFlowMerge$collectTo$2<T> this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ChannelFlowMerge$collectTo$2$emit$1(ChannelFlowMerge$collectTo$2<? super T> channelFlowMerge$collectTo$2, Continuation<? super ChannelFlowMerge$collectTo$2$emit$1> continuation) {
        super(continuation);
        this.this$0 = channelFlowMerge$collectTo$2;
    }

    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return this.this$0.emit((Flow) null, (Continuation<? super Unit>) this);
    }
}
