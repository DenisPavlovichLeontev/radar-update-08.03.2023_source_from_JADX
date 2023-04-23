package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlinx.coroutines.channels.ReceiveChannel;

@Metadata(mo19895k = 3, mo19896mv = {1, 6, 0}, mo19898xi = 48)
@DebugMetadata(mo20608c = "kotlinx.coroutines.flow.FlowKt__ChannelsKt", mo20609f = "Channels.kt", mo20610i = {0, 0, 0, 1, 1, 1}, mo20611l = {51, 62}, mo20612m = "emitAllImpl$FlowKt__ChannelsKt", mo20613n = {"$this$emitAllImpl", "channel", "consume", "$this$emitAllImpl", "channel", "consume"}, mo20614s = {"L$0", "L$1", "Z$0", "L$0", "L$1", "Z$0"})
/* compiled from: Channels.kt */
final class FlowKt__ChannelsKt$emitAllImpl$1<T> extends ContinuationImpl {
    Object L$0;
    Object L$1;
    boolean Z$0;
    int label;
    /* synthetic */ Object result;

    FlowKt__ChannelsKt$emitAllImpl$1(Continuation<? super FlowKt__ChannelsKt$emitAllImpl$1> continuation) {
        super(continuation);
    }

    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return FlowKt__ChannelsKt.emitAllImpl$FlowKt__ChannelsKt((FlowCollector) null, (ReceiveChannel) null, false, this);
    }
}
