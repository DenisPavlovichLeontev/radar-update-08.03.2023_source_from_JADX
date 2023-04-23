package org.osmdroid.util.constants;

import org.osmdroid.library.C1340R;

public interface GeoConstants {
    @Deprecated
    public static final int EQUATORCIRCUMFENCE = 40075016;
    public static final double FEET_PER_METER = 3.2808399d;
    public static final double METERS_PER_NAUTICAL_MILE = 1852.0d;
    public static final double METERS_PER_STATUTE_MILE = 1609.344d;
    public static final int RADIUS_EARTH_METERS = 6378137;

    public enum UnitOfMeasure {
        meter(1.0d, C1340R.string.format_distance_only_meter),
        kilometer(1000.0d, C1340R.string.format_distance_only_kilometer),
        statuteMile(1609.344d, C1340R.string.format_distance_only_mile),
        nauticalMile(1852.0d, C1340R.string.format_distance_only_nautical_mile),
        foot(0.304799999536704d, C1340R.string.format_distance_only_foot);
        
        private final double mConversionFactorToMeters;
        private final int mStringResId;

        private UnitOfMeasure(double d, int i) {
            this.mConversionFactorToMeters = d;
            this.mStringResId = i;
        }

        public double getConversionFactorToMeters() {
            return this.mConversionFactorToMeters;
        }

        public int getStringResId() {
            return this.mStringResId;
        }
    }
}
