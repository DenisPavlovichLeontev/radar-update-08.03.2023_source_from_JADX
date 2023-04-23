package org.mapsforge.map.util;

public abstract class PausableThread extends Thread {
    private boolean pausing;
    private boolean shouldPause;
    private boolean shouldStop;
    protected boolean waitForWork;

    /* access modifiers changed from: protected */
    public void afterRun() {
    }

    /* access modifiers changed from: protected */
    public abstract void doWork() throws InterruptedException;

    /* access modifiers changed from: protected */
    public abstract ThreadPriority getThreadPriority();

    /* access modifiers changed from: protected */
    public abstract boolean hasWork();

    protected enum ThreadPriority {
        ABOVE_NORMAL(7),
        BELOW_NORMAL(3),
        HIGHEST(10),
        LOWEST(1),
        NORMAL(5);
        
        final int priority;

        private ThreadPriority(int i) {
            if (i < 1 || i > 10) {
                throw new IllegalArgumentException("invalid priority: " + i);
            }
            this.priority = i;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0001, code lost:
        continue;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0013 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void awaitPausing() {
        /*
            r2 = this;
            monitor-enter(r2)
        L_0x0001:
            boolean r0 = r2.isInterrupted()     // Catch:{ all -> 0x001d }
            if (r0 != 0) goto L_0x001b
            boolean r0 = r2.isPausing()     // Catch:{ all -> 0x001d }
            if (r0 != 0) goto L_0x001b
            r0 = 100
            r2.wait(r0)     // Catch:{ InterruptedException -> 0x0013 }
            goto L_0x0001
        L_0x0013:
            java.lang.Thread r0 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x001d }
            r0.interrupt()     // Catch:{ all -> 0x001d }
            goto L_0x0001
        L_0x001b:
            monitor-exit(r2)     // Catch:{ all -> 0x001d }
            return
        L_0x001d:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x001d }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.util.PausableThread.awaitPausing():void");
    }

    public final synchronized void finish() {
        this.shouldStop = true;
        interrupt();
    }

    public void interrupt() {
        synchronized (this) {
            super.interrupt();
        }
    }

    public final synchronized boolean isPausing() {
        return this.pausing;
    }

    public final synchronized void pause() {
        if (!this.shouldPause) {
            this.shouldPause = true;
            notify();
        }
    }

    public final synchronized void proceed() {
        if (this.shouldPause) {
            this.shouldPause = false;
            this.pausing = false;
            notify();
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:24|25|57) */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        interrupt();
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0048 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
            r1 = this;
            java.lang.Class r0 = r1.getClass()
            java.lang.String r0 = r0.getSimpleName()
            r1.setName(r0)
            org.mapsforge.map.util.PausableThread$ThreadPriority r0 = r1.getThreadPriority()
            int r0 = r0.priority
            r1.setPriority(r0)
        L_0x0014:
            boolean r0 = r1.shouldStop
            if (r0 != 0) goto L_0x001e
            boolean r0 = r1.isInterrupted()
            if (r0 == 0) goto L_0x0028
        L_0x001e:
            boolean r0 = r1.waitForWork
            if (r0 == 0) goto L_0x006d
            boolean r0 = r1.hasWork()
            if (r0 == 0) goto L_0x006d
        L_0x0028:
            monitor-enter(r1)
        L_0x0029:
            boolean r0 = r1.shouldStop     // Catch:{ all -> 0x006a }
            if (r0 != 0) goto L_0x004c
            boolean r0 = r1.isInterrupted()     // Catch:{ all -> 0x006a }
            if (r0 != 0) goto L_0x004c
            boolean r0 = r1.shouldPause     // Catch:{ all -> 0x006a }
            if (r0 != 0) goto L_0x003d
            boolean r0 = r1.hasWork()     // Catch:{ all -> 0x006a }
            if (r0 != 0) goto L_0x004c
        L_0x003d:
            boolean r0 = r1.shouldPause     // Catch:{ InterruptedException -> 0x0048 }
            if (r0 == 0) goto L_0x0044
            r0 = 1
            r1.pausing = r0     // Catch:{ InterruptedException -> 0x0048 }
        L_0x0044:
            r1.wait()     // Catch:{ InterruptedException -> 0x0048 }
            goto L_0x0029
        L_0x0048:
            r1.interrupt()     // Catch:{ all -> 0x006a }
            goto L_0x0029
        L_0x004c:
            monitor-exit(r1)     // Catch:{ all -> 0x006a }
            boolean r0 = r1.shouldStop
            if (r0 != 0) goto L_0x0057
            boolean r0 = r1.isInterrupted()
            if (r0 == 0) goto L_0x0062
        L_0x0057:
            boolean r0 = r1.waitForWork
            if (r0 == 0) goto L_0x006d
            boolean r0 = r1.hasWork()
            if (r0 != 0) goto L_0x0062
            goto L_0x006d
        L_0x0062:
            r1.doWork()     // Catch:{ InterruptedException -> 0x0066 }
            goto L_0x0014
        L_0x0066:
            r1.interrupt()
            goto L_0x0014
        L_0x006a:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x006a }
            throw r0
        L_0x006d:
            r1.afterRun()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.util.PausableThread.run():void");
    }
}
