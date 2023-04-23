package androidx.work.impl.utils;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;

public class LiveDataUtils {
    public static <In, Out> LiveData<Out> dedupedMappedLiveDataFor(LiveData<In> liveData, final Function<In, Out> function, final TaskExecutor taskExecutor) {
        final Object obj = new Object();
        final MediatorLiveData mediatorLiveData = new MediatorLiveData();
        mediatorLiveData.addSource(liveData, new Observer<In>() {
            Out mCurrentOutput = null;

            public void onChanged(final In in) {
                TaskExecutor.this.executeOnTaskThread(new Runnable() {
                    public void run() {
                        synchronized (obj) {
                            Out apply = function.apply(in);
                            if (C06591.this.mCurrentOutput == null && apply != null) {
                                C06591.this.mCurrentOutput = apply;
                                mediatorLiveData.postValue(apply);
                            } else if (C06591.this.mCurrentOutput != null && !C06591.this.mCurrentOutput.equals(apply)) {
                                C06591.this.mCurrentOutput = apply;
                                mediatorLiveData.postValue(apply);
                            }
                        }
                    }
                });
            }
        });
        return mediatorLiveData;
    }

    private LiveDataUtils() {
    }
}
