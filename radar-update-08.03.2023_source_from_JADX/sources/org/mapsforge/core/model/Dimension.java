package org.mapsforge.core.model;

import java.io.Serializable;

public class Dimension implements Serializable {
    private static final long serialVersionUID = 1;
    public final int height;
    public final int width;

    public Dimension(int i, int i2) {
        if (i < 0) {
            throw new IllegalArgumentException("width must not be negative: " + i);
        } else if (i2 >= 0) {
            this.width = i;
            this.height = i2;
        } else {
            throw new IllegalArgumentException("height must not be negative: " + i2);
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Dimension)) {
            return false;
        }
        Dimension dimension = (Dimension) obj;
        return this.width == dimension.width && this.height == dimension.height;
    }

    public Point getCenter() {
        return new Point((double) (((float) this.width) / 2.0f), (double) (((float) this.height) / 2.0f));
    }

    public int hashCode() {
        return ((this.width + 31) * 31) + this.height;
    }

    public String toString() {
        return "width=" + this.width + ", height=" + this.height;
    }
}
