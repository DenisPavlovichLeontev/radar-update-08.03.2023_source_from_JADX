package org.mapsforge.map.layer.hills;

import com.google.android.gms.common.api.internal.zap$$ExternalSyntheticBackportWithForwarding0;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public abstract class LatchedLazyFuture<X> implements Future<X> {
    private static final ExecutionException CANCELLED = new DummyExecutionException("cancelled");
    private static final ExecutionException DONE = new DummyExecutionException("done");
    private static final ExecutionException STARTED = new DummyExecutionException("started");
    private final CountDownLatch latch = new CountDownLatch(1);
    private volatile X result;
    private final AtomicReference<ExecutionException> state = new AtomicReference<>((Object) null);
    private volatile Thread thread;

    /* access modifiers changed from: protected */
    public abstract X calculate() throws ExecutionException, InterruptedException;

    private static class DummyExecutionException extends ExecutionException {
        DummyExecutionException(String str) {
            super(str, (Throwable) null);
        }

        public synchronized Throwable fillInStackTrace() {
            return null;
        }

        public String toString() {
            return "[state marker " + getMessage() + "]";
        }
    }

    public boolean cancel(boolean z) {
        Thread thread2;
        ExecutionException executionException = this.state.get();
        ExecutionException executionException2 = CANCELLED;
        if (executionException == executionException2) {
            return true;
        }
        if (this.state.get() == DONE) {
            return false;
        }
        if (!z || (thread2 = this.thread) == null || !zap$$ExternalSyntheticBackportWithForwarding0.m52m(this.state, STARTED, executionException2)) {
            return zap$$ExternalSyntheticBackportWithForwarding0.m52m(this.state, (Object) null, executionException2);
        }
        thread2.interrupt();
        return true;
    }

    public boolean isCancelled() {
        return this.state.get() == CANCELLED;
    }

    public boolean isDone() {
        ExecutionException executionException = this.state.get();
        return (executionException == null || executionException == STARTED) ? false : true;
    }

    public X get() throws InterruptedException, ExecutionException {
        if (zap$$ExternalSyntheticBackportWithForwarding0.m52m(this.state, (Object) null, STARTED)) {
            internalCalc();
        } else {
            this.latch.await();
        }
        throwIfException();
        return this.result;
    }

    private void throwIfException() throws ExecutionException {
        ExecutionException executionException = this.state.get();
        if (executionException != null && !(executionException instanceof DummyExecutionException)) {
            throw executionException;
        }
    }

    public X get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        if (zap$$ExternalSyntheticBackportWithForwarding0.m52m(this.state, (Object) null, STARTED)) {
            internalCalc();
        } else {
            this.latch.await(j, timeUnit);
        }
        throwIfException();
        return this.result;
    }

    /* access modifiers changed from: private */
    public void internalCalc() throws ExecutionException, InterruptedException {
        this.thread = Thread.currentThread();
        try {
            this.result = calculate();
            zap$$ExternalSyntheticBackportWithForwarding0.m52m(this.state, STARTED, DONE);
        } catch (RuntimeException e) {
            zap$$ExternalSyntheticBackportWithForwarding0.m52m(this.state, STARTED, new ExecutionException(e));
        } catch (ExecutionException e2) {
            zap$$ExternalSyntheticBackportWithForwarding0.m52m(this.state, STARTED, e2);
        } catch (Throwable th) {
            this.thread = null;
            this.latch.countDown();
            throw th;
        }
        this.thread = null;
        this.latch.countDown();
    }

    public LatchedLazyFuture<X> withRunningThread() {
        if (this.state.get() != DONE && zap$$ExternalSyntheticBackportWithForwarding0.m52m(this.state, (Object) null, STARTED)) {
            new Thread(getClass().getName() + ".withRunningThread") {
                public void run() {
                    try {
                        LatchedLazyFuture.this.internalCalc();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        return this;
    }
}
