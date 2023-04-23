package mil.nga.geopackage.tiles.features;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.util.Log;
import java.io.IOException;
import java.util.List;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.C1157R;
import mil.nga.geopackage.features.index.FeatureIndexManager;
import mil.nga.geopackage.features.index.FeatureIndexResults;
import mil.nga.geopackage.features.user.FeatureCursor;
import mil.nga.geopackage.features.user.FeatureDao;
import mil.nga.geopackage.features.user.FeatureRow;
import mil.nga.geopackage.p010io.BitmapConverter;
import mil.nga.geopackage.projection.Projection;
import mil.nga.geopackage.projection.ProjectionFactory;
import mil.nga.geopackage.projection.ProjectionTransform;
import mil.nga.geopackage.tiles.TileBoundingBoxUtils;
import mil.nga.wkb.geom.Point;
import mil.nga.wkb.util.GeometryUtils;
import org.osgeo.proj4j.units.Units;

public abstract class FeatureTiles {
    protected static final Projection WEB_MERCATOR_PROJECTION = ProjectionFactory.getProjection(3857);
    protected static final Projection WGS_84_PROJECTION = ProjectionFactory.getProjection(4326);
    protected Bitmap.CompressFormat compressFormat;
    protected final Context context;
    protected final FeatureDao featureDao;
    protected boolean fillPolygon;
    protected float heightOverlap;
    protected FeatureIndexManager indexManager;
    protected Paint linePaint = new Paint();
    protected Integer maxFeaturesPerTile;
    protected CustomFeaturesTile maxFeaturesTileDraw;
    protected FeatureTilePointIcon pointIcon;
    protected Paint pointPaint = new Paint();
    protected float pointRadius;
    protected Paint polygonFillPaint = new Paint();
    protected Paint polygonPaint = new Paint();
    protected Projection projection;
    protected boolean simplifyGeometries = true;
    protected int tileHeight;
    protected int tileWidth;
    protected float widthOverlap;

    public abstract Bitmap drawTile(int i, BoundingBox boundingBox, List<FeatureRow> list);

    public abstract Bitmap drawTile(int i, BoundingBox boundingBox, FeatureIndexResults featureIndexResults);

    public abstract Bitmap drawTile(int i, BoundingBox boundingBox, FeatureCursor featureCursor);

    public FeatureTiles(Context context2, FeatureDao featureDao2) {
        this.context = context2;
        this.featureDao = featureDao2;
        if (featureDao2 != null) {
            this.projection = featureDao2.getProjection();
        }
        Resources resources = context2.getResources();
        this.tileWidth = resources.getInteger(C1157R.integer.feature_tiles_width);
        this.tileHeight = resources.getInteger(C1157R.integer.feature_tiles_height);
        this.compressFormat = Bitmap.CompressFormat.valueOf(context2.getString(C1157R.string.feature_tiles_compress_format));
        this.pointPaint.setAntiAlias(true);
        this.pointRadius = Float.valueOf(context2.getString(C1157R.string.feature_tiles_point_radius)).floatValue();
        this.linePaint.setAntiAlias(true);
        this.linePaint.setStrokeWidth(Float.valueOf(context2.getString(C1157R.string.feature_tiles_line_stroke_width)).floatValue());
        this.linePaint.setStyle(Paint.Style.STROKE);
        this.polygonPaint.setAntiAlias(true);
        this.polygonPaint.setStrokeWidth(Float.valueOf(context2.getString(C1157R.string.feature_tiles_polygon_stroke_width)).floatValue());
        this.polygonPaint.setStyle(Paint.Style.STROKE);
        this.fillPolygon = resources.getBoolean(C1157R.bool.feature_tiles_polygon_fill);
        this.polygonFillPaint.setAntiAlias(true);
        this.polygonFillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.polygonFillPaint.setAlpha(resources.getInteger(C1157R.integer.feature_tiles_polygon_fill_alpha));
        calculateDrawOverlap();
    }

    public void close() {
        FeatureIndexManager featureIndexManager = this.indexManager;
        if (featureIndexManager != null) {
            featureIndexManager.close();
        }
    }

    public void calculateDrawOverlap() {
        FeatureTilePointIcon featureTilePointIcon = this.pointIcon;
        if (featureTilePointIcon != null) {
            this.heightOverlap = (float) featureTilePointIcon.getHeight();
            this.widthOverlap = (float) this.pointIcon.getWidth();
        } else {
            float f = this.pointRadius;
            this.heightOverlap = f;
            this.widthOverlap = f;
        }
        float strokeWidth = this.linePaint.getStrokeWidth() / 2.0f;
        this.heightOverlap = Math.max(this.heightOverlap, strokeWidth);
        this.widthOverlap = Math.max(this.widthOverlap, strokeWidth);
        float strokeWidth2 = this.polygonPaint.getStrokeWidth() / 2.0f;
        this.heightOverlap = Math.max(this.heightOverlap, strokeWidth2);
        this.widthOverlap = Math.max(this.widthOverlap, strokeWidth2);
    }

    public void setDrawOverlap(float f) {
        setWidthDrawOverlap(f);
        setHeightDrawOverlap(f);
    }

    public float getWidthDrawOverlap() {
        return this.widthOverlap;
    }

    public void setWidthDrawOverlap(float f) {
        this.widthOverlap = f;
    }

    public float getHeightDrawOverlap() {
        return this.heightOverlap;
    }

    public void setHeightDrawOverlap(float f) {
        this.heightOverlap = f;
    }

    public FeatureDao getFeatureDao() {
        return this.featureDao;
    }

    public boolean isIndexQuery() {
        FeatureIndexManager featureIndexManager = this.indexManager;
        return featureIndexManager != null && featureIndexManager.isIndexed();
    }

    public FeatureIndexManager getIndexManager() {
        return this.indexManager;
    }

    public void setIndexManager(FeatureIndexManager featureIndexManager) {
        this.indexManager = featureIndexManager;
    }

    public int getTileWidth() {
        return this.tileWidth;
    }

    public void setTileWidth(int i) {
        this.tileWidth = i;
    }

    public int getTileHeight() {
        return this.tileHeight;
    }

    public void setTileHeight(int i) {
        this.tileHeight = i;
    }

    public Bitmap.CompressFormat getCompressFormat() {
        return this.compressFormat;
    }

    public void setCompressFormat(Bitmap.CompressFormat compressFormat2) {
        this.compressFormat = compressFormat2;
    }

    public float getPointRadius() {
        return this.pointRadius;
    }

    public void setPointRadius(float f) {
        this.pointRadius = f;
    }

    public Paint getPointPaint() {
        return this.pointPaint;
    }

    public void setPointPaint(Paint paint) {
        this.pointPaint = paint;
    }

    public FeatureTilePointIcon getPointIcon() {
        return this.pointIcon;
    }

    public void setPointIcon(FeatureTilePointIcon featureTilePointIcon) {
        this.pointIcon = featureTilePointIcon;
    }

    public Paint getLinePaint() {
        return this.linePaint;
    }

    public void setLinePaint(Paint paint) {
        this.linePaint = paint;
    }

    public Paint getPolygonPaint() {
        return this.polygonPaint;
    }

    public void setPolygonPaint(Paint paint) {
        this.polygonPaint = paint;
    }

    public boolean isFillPolygon() {
        return this.fillPolygon;
    }

    public void setFillPolygon(boolean z) {
        this.fillPolygon = z;
    }

    public Paint getPolygonFillPaint() {
        return this.polygonFillPaint;
    }

    public void setPolygonFillPaint(Paint paint) {
        this.polygonFillPaint = paint;
    }

    public Integer getMaxFeaturesPerTile() {
        return this.maxFeaturesPerTile;
    }

    public void setMaxFeaturesPerTile(Integer num) {
        this.maxFeaturesPerTile = num;
    }

    public CustomFeaturesTile getMaxFeaturesTileDraw() {
        return this.maxFeaturesTileDraw;
    }

    public void setMaxFeaturesTileDraw(CustomFeaturesTile customFeaturesTile) {
        this.maxFeaturesTileDraw = customFeaturesTile;
    }

    public boolean isSimplifyGeometries() {
        return this.simplifyGeometries;
    }

    public void setSimplifyGeometries(boolean z) {
        this.simplifyGeometries = z;
    }

    public byte[] drawTileBytes(int i, int i2, int i3) {
        Bitmap drawTile = drawTile(i, i2, i3);
        if (drawTile != null) {
            try {
                return BitmapConverter.toBytes(drawTile, this.compressFormat);
            } catch (IOException e) {
                Log.e("FeatureTiles", "Failed to create tile. x: " + i + ", y: " + i2 + ", zoom: " + i3, e);
            } finally {
                drawTile.recycle();
            }
        }
        return null;
    }

    public Bitmap drawTile(int i, int i2, int i3) {
        if (isIndexQuery()) {
            return drawTileQueryIndex(i, i2, i3);
        }
        return drawTileQueryAll(i, i2, i3);
    }

    public Bitmap drawTileQueryIndex(int i, int i2, int i3) {
        BoundingBox webMercatorBoundingBox = TileBoundingBoxUtils.getWebMercatorBoundingBox((long) i, (long) i2, i3);
        FeatureIndexResults queryIndexedFeatures = queryIndexedFeatures(webMercatorBoundingBox);
        try {
            Bitmap bitmap = null;
            Long valueOf = this.maxFeaturesPerTile != null ? Long.valueOf(queryIndexedFeatures.count()) : null;
            if (this.maxFeaturesPerTile != null) {
                if (valueOf.longValue() > this.maxFeaturesPerTile.longValue()) {
                    CustomFeaturesTile customFeaturesTile = this.maxFeaturesTileDraw;
                    if (customFeaturesTile != null) {
                        bitmap = customFeaturesTile.drawTile(this.tileWidth, this.tileHeight, valueOf.longValue(), queryIndexedFeatures);
                    }
                    return bitmap;
                }
            }
            bitmap = drawTile(i3, webMercatorBoundingBox, queryIndexedFeatures);
            return bitmap;
        } finally {
            queryIndexedFeatures.close();
        }
    }

    public long queryIndexedFeaturesCount(int i, int i2, int i3) {
        FeatureIndexResults queryIndexedFeatures = queryIndexedFeatures(TileBoundingBoxUtils.getWebMercatorBoundingBox((long) i, (long) i2, i3));
        try {
            return queryIndexedFeatures.count();
        } finally {
            queryIndexedFeatures.close();
        }
    }

    public FeatureIndexResults queryIndexedFeatures(BoundingBox boundingBox) {
        double longitudeFromPixel = TileBoundingBoxUtils.getLongitudeFromPixel((long) this.tileWidth, boundingBox, 0.0f - this.widthOverlap);
        int i = this.tileWidth;
        double longitudeFromPixel2 = TileBoundingBoxUtils.getLongitudeFromPixel((long) i, boundingBox, ((float) i) + this.widthOverlap);
        double latitudeFromPixel = TileBoundingBoxUtils.getLatitudeFromPixel((long) this.tileHeight, boundingBox, 0.0f - this.heightOverlap);
        int i2 = this.tileHeight;
        return this.indexManager.query(new BoundingBox(longitudeFromPixel, TileBoundingBoxUtils.getLatitudeFromPixel((long) i2, boundingBox, ((float) i2) + this.heightOverlap), longitudeFromPixel2, latitudeFromPixel), WEB_MERCATOR_PROJECTION);
    }

    public Bitmap drawTileQueryAll(int i, int i2, int i3) {
        BoundingBox webMercatorBoundingBox = TileBoundingBoxUtils.getWebMercatorBoundingBox((long) i, (long) i2, i3);
        FeatureCursor featureCursor = (FeatureCursor) this.featureDao.queryForAll();
        try {
            Bitmap bitmap = null;
            Integer valueOf = this.maxFeaturesPerTile != null ? Integer.valueOf(featureCursor.getCount()) : null;
            if (this.maxFeaturesPerTile != null) {
                if (valueOf.intValue() > this.maxFeaturesPerTile.intValue()) {
                    CustomFeaturesTile customFeaturesTile = this.maxFeaturesTileDraw;
                    if (customFeaturesTile != null) {
                        bitmap = customFeaturesTile.drawUnindexedTile(this.tileWidth, this.tileHeight, (long) valueOf.intValue(), featureCursor);
                    }
                    return bitmap;
                }
            }
            bitmap = drawTile(i3, webMercatorBoundingBox, featureCursor);
            return bitmap;
        } finally {
            featureCursor.close();
        }
    }

    /* access modifiers changed from: protected */
    public Bitmap createNewBitmap() {
        return Bitmap.createBitmap(this.tileWidth, this.tileHeight, Bitmap.Config.ARGB_8888);
    }

    /* access modifiers changed from: protected */
    public ProjectionTransform getWgs84ToWebMercatorTransform() {
        return WGS_84_PROJECTION.getTransformation(3857);
    }

    /* access modifiers changed from: protected */
    public ProjectionTransform getProjectionToWebMercatorTransform(Projection projection2) {
        return projection2.getTransformation(3857);
    }

    /* access modifiers changed from: protected */
    public List<Point> simplifyPoints(double d, List<Point> list) {
        if (!this.simplifyGeometries) {
            return list;
        }
        Projection projection2 = this.projection;
        if (!(projection2 == null || projection2.getUnit() == Units.METRES)) {
            list = this.projection.getTransformation(WEB_MERCATOR_PROJECTION).transform(list);
        }
        List<Point> simplifyPoints = GeometryUtils.simplifyPoints(list, d);
        Projection projection3 = this.projection;
        return (projection3 == null || projection3.getUnit() == Units.METRES) ? simplifyPoints : WEB_MERCATOR_PROJECTION.getTransformation(this.projection).transform(simplifyPoints);
    }
}
