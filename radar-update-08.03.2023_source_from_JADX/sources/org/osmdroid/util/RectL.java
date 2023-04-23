package org.osmdroid.util;

import android.graphics.Rect;

public class RectL {
    public long bottom;
    public long left;
    public long right;
    public long top;

    public RectL() {
    }

    public RectL(long j, long j2, long j3, long j4) {
        set(j, j2, j3, j4);
    }

    public RectL(RectL rectL) {
        set(rectL);
    }

    public void set(long j, long j2, long j3, long j4) {
        this.left = j;
        this.top = j2;
        this.right = j3;
        this.bottom = j4;
    }

    public void set(RectL rectL) {
        this.left = rectL.left;
        this.top = rectL.top;
        this.right = rectL.right;
        this.bottom = rectL.bottom;
    }

    public void union(long j, long j2) {
        if (j < this.left) {
            this.left = j;
        } else if (j > this.right) {
            this.right = j;
        }
        if (j2 < this.top) {
            this.top = j2;
        } else if (j2 > this.bottom) {
            this.bottom = j2;
        }
    }

    public static boolean intersects(RectL rectL, RectL rectL2) {
        return rectL.left < rectL2.right && rectL2.left < rectL.right && rectL.top < rectL2.bottom && rectL2.top < rectL.bottom;
    }

    public boolean contains(long j, long j2) {
        long j3 = this.left;
        long j4 = this.right;
        if (j3 < j4) {
            long j5 = this.top;
            long j6 = this.bottom;
            return j5 < j6 && j >= j3 && j < j4 && j2 >= j5 && j2 < j6;
        }
    }

    public void inset(long j, long j2) {
        this.left += j;
        this.top += j2;
        this.right -= j;
        this.bottom -= j2;
    }

    public final long width() {
        return this.right - this.left;
    }

    public final long height() {
        return this.bottom - this.top;
    }

    public String toString() {
        return "RectL(" + this.left + ", " + this.top + " - " + this.right + ", " + this.bottom + ")";
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RectL rectL = (RectL) obj;
        if (this.left == rectL.left && this.top == rectL.top && this.right == rectL.right && this.bottom == rectL.bottom) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (int) (((((((this.left * 31) + this.top) * 31) + this.right) * 31) + this.bottom) % 2147483647L);
    }

    public static RectL getBounds(RectL rectL, long j, long j2, double d, RectL rectL2) {
        RectL rectL3 = rectL;
        RectL rectL4 = rectL2 != null ? rectL2 : new RectL();
        if (d == 0.0d) {
            rectL4.top = rectL3.top;
            rectL4.left = rectL3.left;
            rectL4.bottom = rectL3.bottom;
            rectL4.right = rectL3.right;
            return rectL4;
        }
        double d2 = (3.141592653589793d * d) / 180.0d;
        double cos = Math.cos(d2);
        double sin = Math.sin(d2);
        long j3 = rectL3.left;
        long j4 = rectL3.top;
        long j5 = j4;
        long j6 = j;
        long j7 = j2;
        long j8 = j4;
        double d3 = cos;
        long rotatedY = getRotatedY(j3, j8, j6, j7, d3, sin);
        rectL4.bottom = rotatedY;
        rectL4.top = rotatedY;
        long rotatedX = getRotatedX(j3, j5, j6, j7, d3, sin);
        rectL4.right = rotatedX;
        rectL4.left = rotatedX;
        long j9 = rectL3.right;
        long j10 = rectL3.top;
        long j11 = j10;
        long j12 = j10;
        double d4 = cos;
        long rotatedX2 = getRotatedX(j9, j11, j6, j7, d4, sin);
        long rotatedY2 = getRotatedY(j9, j12, j6, j7, d4, sin);
        if (rectL4.top > rotatedY2) {
            rectL4.top = rotatedY2;
        }
        if (rectL4.bottom < rotatedY2) {
            rectL4.bottom = rotatedY2;
        }
        long j13 = rotatedX2;
        if (rectL4.left > j13) {
            rectL4.left = j13;
        }
        if (rectL4.right < j13) {
            rectL4.right = j13;
        }
        long j14 = rectL3.right;
        long j15 = rectL3.bottom;
        long j16 = j15;
        long j17 = j;
        long j18 = j2;
        long j19 = j15;
        double d5 = cos;
        long rotatedX3 = getRotatedX(j14, j16, j17, j18, d5, sin);
        long rotatedY3 = getRotatedY(j14, j19, j17, j18, d5, sin);
        if (rectL4.top > rotatedY3) {
            rectL4.top = rotatedY3;
        }
        if (rectL4.bottom < rotatedY3) {
            rectL4.bottom = rotatedY3;
        }
        long j20 = rotatedX3;
        if (rectL4.left > j20) {
            rectL4.left = j20;
        }
        if (rectL4.right < j20) {
            rectL4.right = j20;
        }
        long j21 = rectL3.left;
        long j22 = rectL3.bottom;
        long j23 = j22;
        long j24 = j;
        long j25 = j2;
        long j26 = j22;
        double d6 = cos;
        long rotatedX4 = getRotatedX(j21, j23, j24, j25, d6, sin);
        long rotatedY4 = getRotatedY(j21, j26, j24, j25, d6, sin);
        if (rectL4.top > rotatedY4) {
            rectL4.top = rotatedY4;
        }
        if (rectL4.bottom < rotatedY4) {
            rectL4.bottom = rotatedY4;
        }
        long j27 = rotatedX4;
        if (rectL4.left > j27) {
            rectL4.left = j27;
        }
        if (rectL4.right < j27) {
            rectL4.right = j27;
        }
        return rectL4;
    }

    public static Rect getBounds(Rect rect, int i, int i2, double d, Rect rect2) {
        Rect rect3 = rect;
        Rect rect4 = rect2 != null ? rect2 : new Rect();
        if (d == 0.0d) {
            rect4.top = rect3.top;
            rect4.left = rect3.left;
            rect4.bottom = rect3.bottom;
            rect4.right = rect3.right;
            return rect4;
        }
        double d2 = (3.141592653589793d * d) / 180.0d;
        double cos = Math.cos(d2);
        double sin = Math.sin(d2);
        long j = (long) rect3.left;
        long j2 = (long) rect3.top;
        long j3 = (long) i;
        long j4 = j2;
        long j5 = (long) i2;
        long j6 = j3;
        long j7 = j3;
        long j8 = j5;
        long j9 = j2;
        double d3 = cos;
        long j10 = j;
        int rotatedX = (int) getRotatedX(j, j4, j6, j8, d3, sin);
        long j11 = j7;
        int i3 = rotatedX;
        int rotatedY = (int) getRotatedY(j10, j9, j11, j8, d3, sin);
        rect4.bottom = rotatedY;
        rect4.top = rotatedY;
        rect4.right = i3;
        rect4.left = i3;
        Rect rect5 = rect;
        long j12 = (long) rect5.right;
        long j13 = (long) rect5.top;
        long j14 = j13;
        long j15 = j13;
        double d4 = cos;
        int rotatedX2 = (int) getRotatedX(j12, j14, j11, j8, d4, sin);
        int rotatedY2 = (int) getRotatedY(j12, j15, j11, j8, d4, sin);
        if (rect4.top > rotatedY2) {
            rect4.top = rotatedY2;
        }
        if (rect4.bottom < rotatedY2) {
            rect4.bottom = rotatedY2;
        }
        if (rect4.left > rotatedX2) {
            rect4.left = rotatedX2;
        }
        if (rect4.right < rotatedX2) {
            rect4.right = rotatedX2;
        }
        Rect rect6 = rect;
        long j16 = (long) rect6.right;
        long j17 = (long) rect6.bottom;
        long j18 = j17;
        long j19 = j7;
        long j20 = j5;
        long j21 = j17;
        double d5 = cos;
        int rotatedX3 = (int) getRotatedX(j16, j18, j19, j20, d5, sin);
        int rotatedY3 = (int) getRotatedY(j16, j21, j19, j20, d5, sin);
        if (rect4.top > rotatedY3) {
            rect4.top = rotatedY3;
        }
        if (rect4.bottom < rotatedY3) {
            rect4.bottom = rotatedY3;
        }
        if (rect4.left > rotatedX3) {
            rect4.left = rotatedX3;
        }
        if (rect4.right < rotatedX3) {
            rect4.right = rotatedX3;
        }
        Rect rect7 = rect;
        long j22 = (long) rect7.left;
        long j23 = (long) rect7.bottom;
        long j24 = j22;
        long j25 = j23;
        long j26 = j7;
        long j27 = j5;
        long j28 = j23;
        double d6 = cos;
        long j29 = j22;
        double d7 = sin;
        int rotatedX4 = (int) getRotatedX(j24, j25, j26, j27, d6, d7);
        int rotatedY4 = (int) getRotatedY(j29, j28, j26, j27, d6, d7);
        if (rect4.top > rotatedY4) {
            rect4.top = rotatedY4;
        }
        if (rect4.bottom < rotatedY4) {
            rect4.bottom = rotatedY4;
        }
        if (rect4.left > rotatedX4) {
            rect4.left = rotatedX4;
        }
        if (rect4.right < rotatedX4) {
            rect4.right = rotatedX4;
        }
        return rect4;
    }

    public static long getRotatedX(long j, long j2, double d, long j3, long j4) {
        if (d == 0.0d) {
            return j;
        }
        double d2 = (3.141592653589793d * d) / 180.0d;
        return getRotatedX(j, j2, j3, j4, Math.cos(d2), Math.sin(d2));
    }

    public static long getRotatedY(long j, long j2, double d, long j3, long j4) {
        if (d == 0.0d) {
            return j2;
        }
        double d2 = (3.141592653589793d * d) / 180.0d;
        return getRotatedY(j, j2, j3, j4, Math.cos(d2), Math.sin(d2));
    }

    public static long getRotatedX(long j, long j2, long j3, long j4, double d, double d2) {
        return j3 + Math.round((((double) (j - j3)) * d) - (((double) (j2 - j4)) * d2));
    }

    public static long getRotatedY(long j, long j2, long j3, long j4, double d, double d2) {
        return j4 + Math.round((((double) (j - j3)) * d2) + (((double) (j2 - j4)) * d));
    }

    public void offset(long j, long j2) {
        this.left += j;
        this.top += j2;
        this.right += j;
        this.bottom += j2;
    }

    public void union(long j, long j2, long j3, long j4) {
        long j5 = j;
        long j6 = j2;
        long j7 = j3;
        long j8 = j4;
        if (j5 < j7 && j6 < j8) {
            long j9 = this.left;
            long j10 = this.right;
            if (j9 < j10) {
                long j11 = this.top;
                long j12 = this.bottom;
                if (j11 < j12) {
                    if (j9 > j5) {
                        this.left = j5;
                    }
                    if (j11 > j6) {
                        this.top = j6;
                    }
                    if (j10 < j7) {
                        this.right = j7;
                    }
                    long j13 = j12;
                    long j14 = j4;
                    if (j13 < j14) {
                        this.bottom = j14;
                        return;
                    }
                    return;
                }
                j8 = j4;
            }
            this.left = j5;
            this.top = j6;
            this.right = j7;
            this.bottom = j8;
        }
    }

    public void union(RectL rectL) {
        union(rectL.left, rectL.top, rectL.right, rectL.bottom);
    }

    public long centerX() {
        return (this.left + this.right) / 2;
    }

    public long centerY() {
        return (this.top + this.bottom) / 2;
    }
}
