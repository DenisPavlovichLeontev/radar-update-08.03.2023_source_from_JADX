package org.osmdroid.api;

public interface IMapController {
    void animateTo(int i, int i2);

    void animateTo(IGeoPoint iGeoPoint);

    void animateTo(IGeoPoint iGeoPoint, Double d, Long l);

    void animateTo(IGeoPoint iGeoPoint, Double d, Long l, Float f);

    void animateTo(IGeoPoint iGeoPoint, Double d, Long l, Float f, Boolean bool);

    void scrollBy(int i, int i2);

    void setCenter(IGeoPoint iGeoPoint);

    double setZoom(double d);

    @Deprecated
    int setZoom(int i);

    void stopAnimation(boolean z);

    void stopPanning();

    boolean zoomIn();

    boolean zoomIn(Long l);

    boolean zoomInFixing(int i, int i2);

    boolean zoomInFixing(int i, int i2, Long l);

    boolean zoomOut();

    boolean zoomOut(Long l);

    boolean zoomOutFixing(int i, int i2);

    boolean zoomTo(double d);

    boolean zoomTo(double d, Long l);

    @Deprecated
    boolean zoomTo(int i);

    boolean zoomTo(int i, Long l);

    boolean zoomToFixing(double d, int i, int i2);

    boolean zoomToFixing(double d, int i, int i2, Long l);

    @Deprecated
    boolean zoomToFixing(int i, int i2, int i3);

    boolean zoomToFixing(int i, int i2, int i3, Long l);

    void zoomToSpan(double d, double d2);

    @Deprecated
    void zoomToSpan(int i, int i2);
}
