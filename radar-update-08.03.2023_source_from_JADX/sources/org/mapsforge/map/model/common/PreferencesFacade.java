package org.mapsforge.map.model.common;

public interface PreferencesFacade {
    void clear();

    boolean getBoolean(String str, boolean z);

    byte getByte(String str, byte b);

    double getDouble(String str, double d);

    float getFloat(String str, float f);

    int getInt(String str, int i);

    long getLong(String str, long j);

    String getString(String str, String str2);

    void putBoolean(String str, boolean z);

    void putByte(String str, byte b);

    void putDouble(String str, double d);

    void putFloat(String str, float f);

    void putInt(String str, int i);

    void putLong(String str, long j);

    void putString(String str, String str2);

    void save();
}
