package org.mapsforge.core.graphics;

public interface Matrix {
    void reset();

    void rotate(float f);

    void rotate(float f, float f2, float f3);

    void scale(float f, float f2);

    void scale(float f, float f2, float f3, float f4);

    void translate(float f, float f2);
}
