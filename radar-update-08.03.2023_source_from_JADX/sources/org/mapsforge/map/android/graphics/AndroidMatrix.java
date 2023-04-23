package org.mapsforge.map.android.graphics;

import org.mapsforge.core.graphics.Matrix;

class AndroidMatrix implements Matrix {
    final android.graphics.Matrix matrix = new android.graphics.Matrix();

    AndroidMatrix() {
    }

    public void reset() {
        this.matrix.reset();
    }

    public void rotate(float f) {
        this.matrix.preRotate((float) Math.toDegrees((double) f));
    }

    public void rotate(float f, float f2, float f3) {
        this.matrix.preRotate((float) Math.toDegrees((double) f), f2, f3);
    }

    public void scale(float f, float f2) {
        this.matrix.preScale(f, f2);
    }

    public void scale(float f, float f2, float f3, float f4) {
        this.matrix.preScale(f, f2, f3, f4);
    }

    public void translate(float f, float f2) {
        this.matrix.preTranslate(f, f2);
    }
}
