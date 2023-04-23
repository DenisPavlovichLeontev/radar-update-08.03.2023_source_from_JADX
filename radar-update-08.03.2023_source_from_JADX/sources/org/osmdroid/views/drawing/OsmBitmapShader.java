package org.osmdroid.views.drawing;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Matrix;
import android.graphics.Shader;
import org.osmdroid.util.PointL;
import org.osmdroid.views.Projection;

@Deprecated
public class OsmBitmapShader extends BitmapShader {
    private static final PointL sPoint = new PointL();
    private int mBitmapHeight;
    private int mBitmapWidth;
    private final Matrix mMatrix = new Matrix();

    public OsmBitmapShader(Bitmap bitmap, Shader.TileMode tileMode, Shader.TileMode tileMode2) {
        super(bitmap, tileMode, tileMode2);
        this.mBitmapWidth = bitmap.getWidth();
        this.mBitmapHeight = bitmap.getHeight();
    }

    public void onDrawCycle(Projection projection) {
        PointL pointL = sPoint;
        projection.toMercatorPixels(0, 0, pointL);
        this.mMatrix.setTranslate((float) ((-pointL.f559x) % ((long) this.mBitmapWidth)), (float) ((-pointL.f560y) % ((long) this.mBitmapHeight)));
        setLocalMatrix(this.mMatrix);
    }
}
