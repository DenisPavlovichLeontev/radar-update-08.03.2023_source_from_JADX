package mil.nga.geopackage.tiles.features.custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;
import androidx.core.content.ContextCompat;
import mil.nga.geopackage.C1157R;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.features.index.FeatureIndexResults;
import mil.nga.geopackage.features.user.FeatureCursor;
import mil.nga.geopackage.tiles.features.CustomFeaturesTile;

public class NumberFeaturesTile implements CustomFeaturesTile {
    private Paint circleFillPaint = null;
    private float circlePaddingPercentage;
    private Paint circlePaint = null;
    private boolean drawUnindexedTiles;
    private Paint textPaint = new Paint(1);
    private Paint tileBorderPaint = null;
    private Paint tileFillPaint = null;

    public NumberFeaturesTile(Context context) {
        Resources resources = context.getResources();
        this.textPaint.setColor(ContextCompat.getColor(context, C1157R.C1158color.number_features_tile_text_color));
        this.textPaint.setTextSize((float) resources.getDimensionPixelSize(C1157R.dimen.number_features_tile_text_size));
        if (resources.getBoolean(C1157R.bool.number_features_tile_circle_draw)) {
            Paint paint = new Paint(1);
            this.circlePaint = paint;
            paint.setStyle(Paint.Style.STROKE);
            this.circlePaint.setColor(ContextCompat.getColor(context, C1157R.C1158color.number_features_tile_circle_color));
            TypedValue typedValue = new TypedValue();
            resources.getValue(C1157R.dimen.number_features_tile_circle_stroke_width, typedValue, true);
            this.circlePaint.setStrokeWidth(typedValue.getFloat());
        }
        if (resources.getBoolean(C1157R.bool.number_features_tile_circle_fill_draw)) {
            Paint paint2 = new Paint(1);
            this.circleFillPaint = paint2;
            paint2.setStyle(Paint.Style.FILL_AND_STROKE);
            this.circleFillPaint.setColor(ContextCompat.getColor(context, C1157R.C1158color.number_features_tile_circle_fill_color));
        }
        if (resources.getBoolean(C1157R.bool.number_features_tile_border_draw)) {
            Paint paint3 = new Paint(1);
            this.tileBorderPaint = paint3;
            paint3.setStyle(Paint.Style.STROKE);
            this.tileBorderPaint.setColor(ContextCompat.getColor(context, C1157R.C1158color.number_features_tile_border_color));
            TypedValue typedValue2 = new TypedValue();
            resources.getValue(C1157R.dimen.number_features_tile_border_stroke_width, typedValue2, true);
            this.tileBorderPaint.setStrokeWidth(typedValue2.getFloat());
        }
        if (resources.getBoolean(C1157R.bool.number_features_tile_fill_draw)) {
            Paint paint4 = new Paint(1);
            this.tileFillPaint = paint4;
            paint4.setStyle(Paint.Style.FILL_AND_STROKE);
            this.tileFillPaint.setColor(ContextCompat.getColor(context, C1157R.C1158color.number_features_tile_fill_color));
        }
        TypedValue typedValue3 = new TypedValue();
        resources.getValue(C1157R.dimen.number_features_tile_circle_padding_percentage, typedValue3, true);
        this.circlePaddingPercentage = typedValue3.getFloat();
        this.drawUnindexedTiles = resources.getBoolean(C1157R.bool.number_features_tile_unindexed_draw);
    }

    public Paint getTextPaint() {
        return this.textPaint;
    }

    public void setTextPaint(Paint paint) {
        if (paint != null) {
            this.textPaint = paint;
            return;
        }
        throw new GeoPackageException("Text Paint can not be null");
    }

    public Paint getCirclePaint() {
        return this.circlePaint;
    }

    public void setCirclePaint(Paint paint) {
        this.circlePaint = paint;
    }

    public Paint getCircleFillPaint() {
        return this.circleFillPaint;
    }

    public void setCircleFillPaint(Paint paint) {
        this.circleFillPaint = paint;
    }

    public float getCirclePaddingPercentage() {
        return this.circlePaddingPercentage;
    }

    public void setCirclePaddingPercentage(float f) {
        double d = (double) f;
        if (d < 0.0d || d > 1.0d) {
            throw new GeoPackageException("Circle padding percentage must be between 0.0 and 1.0: " + f);
        }
        this.circlePaddingPercentage = f;
    }

    public Paint getTileBorderPaint() {
        return this.tileBorderPaint;
    }

    public void setTileBorderPaint(Paint paint) {
        this.tileBorderPaint = paint;
    }

    public Paint getTileFillPaint() {
        return this.tileFillPaint;
    }

    public void setTileFillPaint(Paint paint) {
        this.tileFillPaint = paint;
    }

    public boolean isDrawUnindexedTiles() {
        return this.drawUnindexedTiles;
    }

    public void setDrawUnindexedTiles(boolean z) {
        this.drawUnindexedTiles = z;
    }

    public Bitmap drawTile(int i, int i2, long j, FeatureIndexResults featureIndexResults) {
        return drawTile(i, i2, String.valueOf(j));
    }

    public Bitmap drawUnindexedTile(int i, int i2, long j, FeatureCursor featureCursor) {
        if (this.drawUnindexedTiles) {
            return drawTile(i, i2, "?");
        }
        return null;
    }

    private Bitmap drawTile(int i, int i2, String str) {
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = this.tileFillPaint;
        if (paint != null) {
            canvas.drawRect(0.0f, 0.0f, (float) i, (float) i2, paint);
        }
        Paint paint2 = this.tileBorderPaint;
        if (paint2 != null) {
            canvas.drawRect(0.0f, 0.0f, (float) i, (float) i2, paint2);
        }
        Rect rect = new Rect();
        this.textPaint.getTextBounds(str, 0, str.length(), rect);
        int width = (int) (((float) createBitmap.getWidth()) / 2.0f);
        int height = (int) (((float) createBitmap.getHeight()) / 2.0f);
        if (!(this.circlePaint == null && this.circleFillPaint == null)) {
            float max = (float) Math.max(rect.width(), rect.height());
            float f = (max / 2.0f) + (max * this.circlePaddingPercentage);
            Paint paint3 = this.circleFillPaint;
            if (paint3 != null) {
                canvas.drawCircle((float) width, (float) height, f, paint3);
            }
            Paint paint4 = this.circlePaint;
            if (paint4 != null) {
                canvas.drawCircle((float) width, (float) height, f, paint4);
            }
        }
        canvas.drawText(str, ((float) width) - rect.exactCenterX(), ((float) height) - rect.exactCenterY(), this.textPaint);
        return createBitmap;
    }
}
