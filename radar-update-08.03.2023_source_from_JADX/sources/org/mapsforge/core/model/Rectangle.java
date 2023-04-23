package org.mapsforge.core.model;

import java.io.Serializable;

public class Rectangle implements Serializable {
    private static final long serialVersionUID = 1;
    public final double bottom;
    public final double left;
    public final double right;
    public final double top;

    public Rectangle(double d, double d2, double d3, double d4) {
        if (d > d3) {
            throw new IllegalArgumentException("left: " + d + ", right: " + d3);
        } else if (d2 <= d4) {
            this.left = d;
            this.top = d2;
            this.right = d3;
            this.bottom = d4;
        } else {
            throw new IllegalArgumentException("top: " + d2 + ", bottom: " + d4);
        }
    }

    public boolean contains(Point point) {
        return this.left <= point.f381x && this.right >= point.f381x && this.top <= point.f382y && this.bottom >= point.f382y;
    }

    public Rectangle enlarge(double d, double d2, double d3, double d4) {
        return new Rectangle(this.left - d, this.top - d2, this.right + d3, this.bottom + d4);
    }

    public Rectangle envelope(double d) {
        return new Rectangle(this.left - d, this.top - d, this.right + d, this.bottom + d);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Rectangle)) {
            return false;
        }
        Rectangle rectangle = (Rectangle) obj;
        return Double.doubleToLongBits(this.left) == Double.doubleToLongBits(rectangle.left) && Double.doubleToLongBits(this.top) == Double.doubleToLongBits(rectangle.top) && Double.doubleToLongBits(this.right) == Double.doubleToLongBits(rectangle.right) && Double.doubleToLongBits(this.bottom) == Double.doubleToLongBits(rectangle.bottom);
    }

    public Point getCenter() {
        return new Point(getCenterX(), getCenterY());
    }

    public double getCenterX() {
        return (this.left + this.right) / 2.0d;
    }

    public double getCenterY() {
        return (this.top + this.bottom) / 2.0d;
    }

    public double getHeight() {
        return this.bottom - this.top;
    }

    public double getWidth() {
        return this.right - this.left;
    }

    public int hashCode() {
        long doubleToLongBits = Double.doubleToLongBits(this.left);
        long doubleToLongBits2 = Double.doubleToLongBits(this.top);
        int i = ((((int) (doubleToLongBits ^ (doubleToLongBits >>> 32))) + 31) * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)));
        long doubleToLongBits3 = Double.doubleToLongBits(this.right);
        int i2 = (i * 31) + ((int) (doubleToLongBits3 ^ (doubleToLongBits3 >>> 32)));
        long doubleToLongBits4 = Double.doubleToLongBits(this.bottom);
        return (i2 * 31) + ((int) ((doubleToLongBits4 >>> 32) ^ doubleToLongBits4));
    }

    public boolean intersects(Rectangle rectangle) {
        if (this == rectangle) {
            return true;
        }
        return this.left <= rectangle.right && rectangle.left <= this.right && this.top <= rectangle.bottom && rectangle.top <= this.bottom;
    }

    public boolean intersectsCircle(double d, double d2, double d3) {
        double width = getWidth() / 2.0d;
        double height = getHeight() / 2.0d;
        double abs = Math.abs(d - getCenterX());
        double abs2 = Math.abs(d2 - getCenterY());
        if (abs > width + d3 || abs2 > height + d3) {
            return false;
        }
        if (abs <= width || abs2 <= height) {
            return true;
        }
        double d4 = abs - width;
        double d5 = abs2 - height;
        return (d4 * d4) + (d5 * d5) <= d3 * d3;
    }

    public Rectangle shift(Point point) {
        if (point.f381x == 0.0d && point.f382y == 0.0d) {
            return this;
        }
        return new Rectangle(this.left + point.f381x, this.top + point.f382y, this.right + point.f381x, this.bottom + point.f382y);
    }

    public String toString() {
        return "left=" + this.left + ", top=" + this.top + ", right=" + this.right + ", bottom=" + this.bottom;
    }
}
