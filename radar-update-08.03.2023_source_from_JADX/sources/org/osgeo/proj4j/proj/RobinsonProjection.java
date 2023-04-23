package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;

public class RobinsonProjection extends PseudoCylindricalProjection {

    /* renamed from: C1 */
    private static final double f544C1 = 11.459155902616464d;
    private static final double EPS = 1.0E-8d;
    private static final double FXC = 0.8487d;
    private static final double FYC = 1.3523d;
    private static final double ONEEPS = 1.000001d;
    private static final double RC1 = 0.08726646259971647d;

    /* renamed from: X */
    private static final double[] f545X = {1.0d, -5.67239E-12d, -7.15511E-5d, 3.11028E-6d, 0.9986d, -4.82241E-4d, -2.4897E-5d, -1.33094E-6d, 0.9954d, -8.31031E-4d, -4.4861E-5d, -9.86588E-7d, 0.99d, -0.00135363d, -5.96598E-5d, 3.67749E-6d, 0.9822d, -0.00167442d, -4.4975E-6d, -5.72394E-6d, 0.973d, -0.00214869d, -9.03565E-5d, 1.88767E-8d, 0.96d, -0.00305084d, -9.00732E-5d, 1.64869E-6d, 0.9427d, -0.00382792d, -6.53428E-5d, -2.61493E-6d, 0.9216d, -0.00467747d, -1.04566E-4d, 4.8122E-6d, 0.8962d, -0.00536222d, -3.23834E-5d, -5.43445E-6d, 0.8679d, -0.00609364d, -1.139E-4d, 3.32521E-6d, 0.835d, -0.00698325d, -6.40219E-5d, 9.34582E-7d, 0.7986d, -0.00755337d, -5.00038E-5d, 9.35532E-7d, 0.7597d, -0.00798325d, -3.59716E-5d, -2.27604E-6d, 0.7186d, -0.00851366d, -7.0112E-5d, -8.63072E-6d, 0.6732d, -0.00986209d, -1.99572E-4d, 1.91978E-5d, 0.6213d, -0.010418d, 8.83948E-5d, 6.24031E-6d, 0.5722d, -0.00906601d, 1.81999E-4d, 6.24033E-6d, 0.5322d, 0.0d, 0.0d, 0.0d};

    /* renamed from: Y */
    private static final double[] f546Y = {0.0d, 0.0124d, 3.72529E-10d, 1.15484E-9d, 0.062d, 0.0124001d, 1.76951E-8d, -5.92321E-9d, 0.124d, 0.0123998d, -7.09668E-8d, 2.25753E-8d, 0.186d, 0.0124008d, 2.66917E-7d, -8.44523E-8d, 0.248d, 0.0123971d, -9.99682E-7d, 3.15569E-7d, 0.31d, 0.0124108d, 3.73349E-6d, -1.1779E-6d, 0.372d, 0.0123598d, -1.3935E-5d, 4.39588E-6d, 0.434d, 0.0125501d, 5.20034E-5d, -1.00051E-5d, 0.4958d, 0.0123198d, -9.80735E-5d, 9.22397E-6d, 0.5571d, 0.0120308d, 4.02857E-5d, -5.2901E-6d, 0.6176d, 0.0120369d, -3.90662E-5d, 7.36117E-7d, 0.6769d, 0.0117015d, -2.80246E-5d, -8.54283E-7d, 0.7346d, 0.0113572d, -4.08389E-5d, -5.18524E-7d, 0.7903d, 0.0109099d, -4.86169E-5d, -1.0718E-6d, 0.8435d, 0.0103433d, -6.46934E-5d, 5.36384E-9d, 0.8936d, 0.00969679d, -6.46129E-5d, -8.54894E-6d, 0.9394d, 0.00840949d, -1.92847E-4d, -4.21023E-6d, 0.9761d, 0.00616525d, -2.56001E-4d, -4.21021E-6d, 1.0d, 0.0d, 0.0d, 0.0d};
    private final int NODES = 18;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Robinson";
    }

    private double poly(double[] dArr, int i, double d) {
        return dArr[i] + (d * (dArr[i + 1] + ((dArr[i + 2] + (dArr[i + 3] * d)) * d)));
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double abs = Math.abs(d2);
        int floor = (int) Math.floor(f544C1 * abs);
        if (floor >= 18) {
            floor = 17;
        }
        double degrees = Math.toDegrees(abs - (((double) floor) * RC1));
        int i = floor * 4;
        projCoordinate.f409x = poly(f545X, i, degrees) * FXC * d;
        projCoordinate.f410y = poly(f546Y, i, degrees) * FYC;
        if (d2 < 0.0d) {
            projCoordinate.f410y = -projCoordinate.f410y;
        }
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        double[] dArr;
        int i;
        ProjCoordinate projCoordinate2 = projCoordinate;
        projCoordinate2.f409x = d / FXC;
        projCoordinate2.f410y = Math.abs(d2 / FYC);
        if (projCoordinate2.f410y < 1.0d) {
            int floor = ((int) Math.floor(projCoordinate2.f410y * 18.0d)) * 4;
            while (true) {
                dArr = f546Y;
                if (dArr[floor] <= projCoordinate2.f410y) {
                    i = floor + 4;
                    if (dArr[i] > projCoordinate2.f410y) {
                        break;
                    }
                    floor = i;
                } else {
                    floor -= 4;
                }
            }
            double d3 = projCoordinate2.f410y;
            double d4 = dArr[floor];
            double d5 = dArr[i];
            int i2 = floor + 1;
            double d6 = dArr[i2];
            double d7 = dArr[floor + 2];
            double d8 = dArr[floor + 3];
            double d9 = ((projCoordinate2.f410y - d4) * 5.0d) / (dArr[i2] - d4);
            double d10 = d4 - projCoordinate2.f410y;
            while (true) {
                double d11 = ((((((d9 * d8) + d7) * d9) + d6) * d9) + d10) / ((((d7 + d7) + ((3.0d * d9) * d8)) * d9) + d6);
                d9 -= d11;
                if (Math.abs(d11) < EPS) {
                    break;
                }
            }
            projCoordinate2.f410y = Math.toRadians(((double) (floor * 5)) + d9);
            if (d2 < 0.0d) {
                projCoordinate2.f410y = -projCoordinate2.f410y;
            }
            projCoordinate2.f409x /= poly(f545X, floor, d9);
        } else if (projCoordinate2.f410y <= ONEEPS) {
            projCoordinate2.f410y = d2 < 0.0d ? -1.5707963267948966d : 1.5707963267948966d;
            projCoordinate2.f409x /= f545X[72];
        } else {
            throw new ProjectionException();
        }
        return projCoordinate2;
    }
}
