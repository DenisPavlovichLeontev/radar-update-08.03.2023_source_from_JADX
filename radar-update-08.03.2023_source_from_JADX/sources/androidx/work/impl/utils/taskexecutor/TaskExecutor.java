package androidx.work.impl.utils.taskexecutor;

import com.android.tools.r8.annotations.SynthesizedClassV2;
import java.util.concurrent.Executor;

public interface TaskExecutor {
    void executeOnTaskThread(Runnable runnable);

    Executor getMainThreadExecutor();

    SerialExecutor getSerialTaskExecutor();

    @SynthesizedClassV2(kind = 7, versionHash = "5e5398f0546d1d7afd62641edb14d82894f11ddc41bce363a0c8d0dac82c9c5a")
    /* renamed from: androidx.work.impl.utils.taskexecutor.TaskExecutor$-CC  reason: invalid class name */
    public final /* synthetic */ class CC {
        public static void $default$executeOnTaskThread(TaskExecutor _this, Runnable runnable) {
            _this.getSerialTaskExecutor().execute(runnable);
        }
    }
}
