package org.osmdroid.util;

import androidx.work.PeriodicWorkRequest;
import java.util.HashMap;
import java.util.Map;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;

public class UrlBackoff {
    private static final long[] mExponentialBackoffDurationInMillisDefault = {5000, 15000, OpenStreetMapTileProviderConstants.ONE_MINUTE, 120000, PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS};
    private final Map<String, Delay> mDelays = new HashMap();
    private long[] mExponentialBackoffDurationInMillis = mExponentialBackoffDurationInMillisDefault;

    public void next(String str) {
        Delay delay;
        synchronized (this.mDelays) {
            delay = this.mDelays.get(str);
        }
        if (delay == null) {
            Delay delay2 = new Delay(this.mExponentialBackoffDurationInMillis);
            synchronized (this.mDelays) {
                this.mDelays.put(str, delay2);
            }
            return;
        }
        delay.next();
    }

    public Delay remove(String str) {
        Delay remove;
        synchronized (this.mDelays) {
            remove = this.mDelays.remove(str);
        }
        return remove;
    }

    public boolean shouldWait(String str) {
        Delay delay;
        synchronized (this.mDelays) {
            delay = this.mDelays.get(str);
        }
        return delay != null && delay.shouldWait();
    }

    public void clear() {
        synchronized (this.mDelays) {
            this.mDelays.clear();
        }
    }

    public void setExponentialBackoffDurationInMillis(long[] jArr) {
        this.mExponentialBackoffDurationInMillis = jArr;
    }
}
