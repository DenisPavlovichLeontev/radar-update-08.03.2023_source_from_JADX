package org.osmdroid.util;

public class Distance {
    private static double dotProduct(double d, double d2, double d3, double d4, double d5, double d6) {
        return ((d3 - d) * (d5 - d)) + ((d4 - d2) * (d6 - d2));
    }

    public static double getSquaredDistanceToPoint(double d, double d2, double d3, double d4) {
        double d5 = d - d3;
        double d6 = d2 - d4;
        return (d5 * d5) + (d6 * d6);
    }

    public static double getSquaredDistanceToLine(double d, double d2, double d3, double d4, double d5, double d6) {
        return getSquaredDistanceToProjection(d, d2, d3, d4, d5, d6, getProjectionFactorToLine(d, d2, d3, d4, d5, d6));
    }

    public static double getSquaredDistanceToSegment(double d, double d2, double d3, double d4, double d5, double d6) {
        return getSquaredDistanceToProjection(d, d2, d3, d4, d5, d6, getProjectionFactorToSegment(d, d2, d3, d4, d5, d6));
    }

    public static double getSquaredDistanceToProjection(double d, double d2, double d3, double d4, double d5, double d6, double d7) {
        return getSquaredDistanceToPoint(d, d2, ((d5 - d3) * d7) + d3, ((d6 - d4) * d7) + d4);
    }

    public static double getProjectionFactorToLine(double d, double d2, double d3, double d4, double d5, double d6) {
        if (d3 == d5 && d4 == d6) {
            return 0.0d;
        }
        return dotProduct(d3, d4, d5, d6, d, d2) / getSquaredDistanceToPoint(d3, d4, d5, d6);
    }

    public static double getProjectionFactorToSegment(double d, double d2, double d3, double d4, double d5, double d6) {
        double projectionFactorToLine = getProjectionFactorToLine(d, d2, d3, d4, d5, d6);
        if (projectionFactorToLine < 0.0d) {
            return 0.0d;
        }
        if (projectionFactorToLine > 1.0d) {
            return 1.0d;
        }
        return projectionFactorToLine;
    }
}
