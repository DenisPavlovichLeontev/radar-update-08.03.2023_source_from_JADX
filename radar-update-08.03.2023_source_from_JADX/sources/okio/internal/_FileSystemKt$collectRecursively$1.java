package okio.internal;

import kotlin.Metadata;
import kotlin.collections.ArrayDeque;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.sequences.SequenceScope;
import okio.FileSystem;
import okio.Path;

@Metadata(mo19895k = 3, mo19896mv = {1, 5, 1}, mo19898xi = 48)
@DebugMetadata(mo20608c = "okio.internal._FileSystemKt", mo20609f = "-FileSystem.kt", mo20610i = {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1}, mo20611l = {113, 132, 142}, mo20612m = "collectRecursively", mo20613n = {"$this$collectRecursively", "fileSystem", "stack", "path", "followSymlinks", "postorder", "$this$collectRecursively", "fileSystem", "stack", "path", "followSymlinks", "postorder"}, mo20614s = {"L$0", "L$1", "L$2", "L$3", "Z$0", "Z$1", "L$0", "L$1", "L$2", "L$3", "Z$0", "Z$1"})
/* compiled from: -FileSystem.kt */
final class _FileSystemKt$collectRecursively$1 extends ContinuationImpl {
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    boolean Z$0;
    boolean Z$1;
    int label;
    /* synthetic */ Object result;

    _FileSystemKt$collectRecursively$1(Continuation<? super _FileSystemKt$collectRecursively$1> continuation) {
        super(continuation);
    }

    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return _FileSystemKt.collectRecursively((SequenceScope<? super Path>) null, (FileSystem) null, (ArrayDeque<Path>) null, (Path) null, false, false, this);
    }
}
