package kotlinx.coroutines;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function2;

@Metadata(mo19895k = 3, mo19896mv = {1, 6, 0}, mo19898xi = 48)
@DebugMetadata(mo20608c = "kotlinx.coroutines.TimeoutKt", mo20609f = "Timeout.kt", mo20610i = {0, 0, 0}, mo20611l = {100}, mo20612m = "withTimeoutOrNull", mo20613n = {"block", "coroutine", "timeMillis"}, mo20614s = {"L$0", "L$1", "J$0"})
/* compiled from: Timeout.kt */
final class TimeoutKt$withTimeoutOrNull$1<T> extends ContinuationImpl {
    long J$0;
    Object L$0;
    Object L$1;
    int label;
    /* synthetic */ Object result;

    TimeoutKt$withTimeoutOrNull$1(Continuation<? super TimeoutKt$withTimeoutOrNull$1> continuation) {
        super(continuation);
    }

    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return TimeoutKt.withTimeoutOrNull(0, (Function2) null, this);
    }
}
