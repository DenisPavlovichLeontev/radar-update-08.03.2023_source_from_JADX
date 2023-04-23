package org.mapsforge.map.layer.hills;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class SyncLazyFuture<X> implements Future<X> {
    private static final ExecutionException CANCELLED = new DummyExecutionException("cancelled");
    private static final ExecutionException DONE = new DummyExecutionException("done");
    private static final ExecutionException STARTED = new DummyExecutionException("started");
    private volatile X result;
    private volatile ExecutionException state = null;
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
        ExecutionException executionException = this.state;
        ExecutionException executionException2 = CANCELLED;
        boolean z2 = true;
        if (executionException == executionException2) {
            return true;
        }
        if (this.state == DONE) {
            return false;
        }
        if (!z || (thread2 = this.thread) == null || this.state != STARTED) {
            if (this.state != null) {
                z2 = false;
            }
            this.state = executionException2;
            return z2;
        }
        this.state = executionException2;
        thread2.interrupt();
        return true;
    }

    public boolean isCancelled() {
        return this.state == CANCELLED;
    }

    public boolean isDone() {
        return (this.state == null || this.state == STARTED) ? false : true;
    }

    public X get() throws InterruptedException, ExecutionException {
        synchronized (this) {
            if (this.state == null) {
                this.state = STARTED;
                internalCalc();
            }
        }
        throwIfException();
        return this.result;
    }

    private void throwIfException() throws ExecutionException {
        ExecutionException executionException = this.state;
        if (executionException != null && !(executionException instanceof DummyExecutionException)) {
            throw executionException;
        }
    }

    public X get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        wait();
        return get();
    }

    /* access modifiers changed from: private */
    public void internalCalc() throws ExecutionException, InterruptedException {
        this.state = STARTED;
        try {
            this.thread = Thread.currentThread();
            this.result = calculate();
            this.state = DONE;
        } catch (RuntimeException e) {
            this.state = new ExecutionException(e);
        } catch (ExecutionException e2) {
            this.state = e2;
        } catch (Throwable th) {
            this.thread = null;
            throw th;
        }
        this.thread = null;
    }

    public SyncLazyFuture<X> withRunningThread() {
        if (this.state != null) {
            return this;
        }
        new Thread(getClass().getName() + ".withRunningThread") {
            public void run() {
                try {
                    SyncLazyFuture.this.internalCalc();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return this;
    }
}
