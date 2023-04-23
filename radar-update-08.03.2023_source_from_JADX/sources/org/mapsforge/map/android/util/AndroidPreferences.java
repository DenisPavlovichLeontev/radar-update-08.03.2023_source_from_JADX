package org.mapsforge.map.android.util;

import android.content.SharedPreferences;
import org.mapsforge.map.model.common.PreferencesFacade;

public class AndroidPreferences implements PreferencesFacade {
    private SharedPreferences.Editor editor;
    private final SharedPreferences sharedPreferences;

    public AndroidPreferences(SharedPreferences sharedPreferences2) {
        this.sharedPreferences = sharedPreferences2;
    }

    public synchronized void clear() {
        createEditor();
        this.editor.clear();
    }

    public synchronized boolean getBoolean(String str, boolean z) {
        return this.sharedPreferences.getBoolean(str, z);
    }

    public synchronized byte getByte(String str, byte b) {
        int i;
        i = this.sharedPreferences.getInt(str, b);
        if (i < -128 || i > 127) {
            throw new IllegalStateException("byte value out of range: " + i);
        }
        return (byte) i;
    }

    public synchronized double getDouble(String str, double d) {
        return Double.longBitsToDouble(this.sharedPreferences.getLong(str, Double.doubleToLongBits(d)));
    }

    public synchronized float getFloat(String str, float f) {
        return this.sharedPreferences.getFloat(str, f);
    }

    public synchronized int getInt(String str, int i) {
        return this.sharedPreferences.getInt(str, i);
    }

    public synchronized long getLong(String str, long j) {
        return this.sharedPreferences.getLong(str, j);
    }

    public synchronized String getString(String str, String str2) {
        return this.sharedPreferences.getString(str, str2);
    }

    public synchronized void putBoolean(String str, boolean z) {
        createEditor();
        this.editor.putBoolean(str, z);
    }

    public synchronized void putByte(String str, byte b) {
        createEditor();
        this.editor.putInt(str, b);
    }

    public synchronized void putDouble(String str, double d) {
        createEditor();
        this.editor.putLong(str, Double.doubleToLongBits(d));
    }

    public synchronized void putFloat(String str, float f) {
        createEditor();
        this.editor.putFloat(str, f);
    }

    public synchronized void putInt(String str, int i) {
        createEditor();
        this.editor.putInt(str, i);
    }

    public synchronized void putLong(String str, long j) {
        createEditor();
        this.editor.putLong(str, j);
    }

    public synchronized void putString(String str, String str2) {
        createEditor();
        this.editor.putString(str, str2);
    }

    public synchronized void save() {
        SharedPreferences.Editor editor2 = this.editor;
        if (editor2 != null) {
            editor2.apply();
            this.editor = null;
        }
    }

    private void createEditor() {
        if (this.editor == null) {
            this.editor = this.sharedPreferences.edit();
        }
    }
}
