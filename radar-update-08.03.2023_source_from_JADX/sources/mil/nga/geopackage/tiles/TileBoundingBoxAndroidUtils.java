package mil.nga.geopackage.tiles;

import android.graphics.Rect;
import android.graphics.RectF;
import mil.nga.geopackage.BoundingBox;

public class TileBoundingBoxAndroidUtils {
    public static Rect getRectangle(long j, long j2, BoundingBox boundingBox, BoundingBox boundingBox2) {
        RectF floatRectangle = getFloatRectangle(j, j2, boundingBox, boundingBox2);
        return new Rect(Math.round(floatRectangle.left), Math.round(floatRectangle.top), Math.round(floatRectangle.right), Math.round(floatRectangle.bottom));
    }

    public static RectF getRoundedFloatRectangle(long j, long j2, BoundingBox boundingBox, BoundingBox boundingBox2) {
        return new RectF(getRectangle(j, j2, boundingBox, boundingBox2));
    }

    public static RectF getFloatRectangle(long j, long j2, BoundingBox boundingBox, BoundingBox boundingBox2) {
        return new RectF(TileBoundingBoxUtils.getXPixel(j, boundingBox, boundingBox2.getMinLongitude()), TileBoundingBoxUtils.getYPixel(j2, boundingBox, boundingBox2.getMaxLatitude()), TileBoundingBoxUtils.getXPixel(j, boundingBox, boundingBox2.getMaxLongitude()), TileBoundingBoxUtils.getYPixel(j2, boundingBox, boundingBox2.getMinLatitude()));
    }

    public static boolean isValid(Rect rect) {
        return rect.left < rect.right && rect.top < rect.bottom;
    }

    public static boolean isValidAllowEmpty(Rect rect) {
        return rect.left <= rect.right && rect.top <= rect.bottom;
    }

    public static boolean isValid(RectF rectF) {
        return rectF.left < rectF.right && rectF.top < rectF.bottom;
    }

    public static boolean isValidAllowEmpty(RectF rectF) {
        return rectF.left <= rectF.right && rectF.top <= rectF.bottom;
    }
}
