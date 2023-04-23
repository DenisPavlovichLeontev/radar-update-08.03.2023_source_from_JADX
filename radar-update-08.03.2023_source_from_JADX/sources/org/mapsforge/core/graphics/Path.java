package org.mapsforge.core.graphics;

public interface Path {
    void clear();

    void close();

    boolean isEmpty();

    void lineTo(float f, float f2);

    void moveTo(float f, float f2);

    void setFillRule(FillRule fillRule);
}
