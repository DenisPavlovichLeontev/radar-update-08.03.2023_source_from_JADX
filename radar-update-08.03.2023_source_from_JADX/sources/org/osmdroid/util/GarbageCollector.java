package org.osmdroid.util;

import java.util.concurrent.atomic.AtomicBoolean;

public class GarbageCollector {
    /* access modifiers changed from: private */
    public final Runnable mAction;
    /* access modifiers changed from: private */
    public final AtomicBoolean mRunning = new AtomicBoolean(false);

    public GarbageCollector(Runnable runnable) {
        this.mAction = runnable;
    }

    /* renamed from: gc */
    public boolean mo29215gc() {
        if (this.mRunning.getAndSet(true)) {
            return false;
        }
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    GarbageCollector.this.mAction.run();
                } finally {
                    GarbageCollector.this.mRunning.set(false);
                }
            }
        });
        thread.setName("GarbageCollector");
        thread.setPriority(1);
        thread.start();
        return true;
    }

    public boolean isRunning() {
        return this.mRunning.get();
    }
}
