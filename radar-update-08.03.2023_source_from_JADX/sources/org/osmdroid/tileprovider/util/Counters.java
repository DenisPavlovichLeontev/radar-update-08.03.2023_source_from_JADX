package org.osmdroid.tileprovider.util;

import android.util.Log;
import java.util.HashMap;
import java.util.Map;

public class Counters {
    static final String TAG = "osmCounters";
    public static int countOOM;
    public static int fileCacheHit;
    public static int fileCacheMiss;
    public static int fileCacheOOM;
    public static int fileCacheSaveErrors;
    private static final Map<String, Integer> sMap = new HashMap();
    public static int tileDownloadErrors;

    public static void printToLogcat() {
        Log.d(TAG, "countOOM " + countOOM);
        Log.d(TAG, "tileDownloadErrors " + tileDownloadErrors);
        Log.d(TAG, "fileCacheSaveErrors " + fileCacheSaveErrors);
        Log.d(TAG, "fileCacheMiss " + fileCacheMiss);
        Log.d(TAG, "fileCacheOOM " + fileCacheOOM);
        Log.d(TAG, "fileCacheHit " + fileCacheHit);
    }

    public static void reset() {
        countOOM = 0;
        tileDownloadErrors = 0;
        fileCacheSaveErrors = 0;
        fileCacheMiss = 0;
        fileCacheOOM = 0;
        fileCacheHit = 0;
    }

    public static void reset(String str) {
        sMap.remove(str);
    }

    public static void increment(String str) {
        Map<String, Integer> map = sMap;
        Integer num = map.get(str);
        if (num == null) {
            map.put(str, 1);
        } else {
            map.put(str, Integer.valueOf(num.intValue() + 1));
        }
    }

    public static int get(String str) {
        Integer num = sMap.get(str);
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }
}
