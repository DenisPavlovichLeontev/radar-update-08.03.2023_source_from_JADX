package org.osmdroid.views.overlay.simplefastpoint;

import android.graphics.Color;
import android.graphics.Paint;
import androidx.recyclerview.widget.ItemTouchHelper;

public class SimpleFastPointOverlayOptions {
    protected RenderingAlgorithm mAlgorithm = RenderingAlgorithm.MAXIMUM_OPTIMIZATION;
    protected int mCellSize = 10;
    protected float mCircleRadius = 5.0f;
    protected boolean mClickable = true;
    protected LabelPolicy mLabelPolicy = LabelPolicy.ZOOM_THRESHOLD;
    protected int mMaxNShownLabels = ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
    protected int mMinZoomShowLabels = 11;
    protected Paint mPointStyle;
    protected float mSelectedCircleRadius = 13.0f;
    protected Paint mSelectedPointStyle;
    protected Shape mSymbol = Shape.SQUARE;
    protected Paint mTextStyle;

    public enum LabelPolicy {
        ZOOM_THRESHOLD,
        DENSITY_THRESHOLD
    }

    public enum RenderingAlgorithm {
        NO_OPTIMIZATION,
        MEDIUM_OPTIMIZATION,
        MAXIMUM_OPTIMIZATION
    }

    public enum Shape {
        CIRCLE,
        SQUARE
    }

    public SimpleFastPointOverlayOptions() {
        Paint paint = new Paint();
        this.mPointStyle = paint;
        paint.setStyle(Paint.Style.FILL);
        this.mPointStyle.setColor(Color.parseColor("#ff7700"));
        Paint paint2 = new Paint();
        this.mSelectedPointStyle = paint2;
        paint2.setStrokeWidth(5.0f);
        this.mSelectedPointStyle.setStyle(Paint.Style.STROKE);
        this.mSelectedPointStyle.setColor(Color.parseColor("#ffff00"));
        Paint paint3 = new Paint();
        this.mTextStyle = paint3;
        paint3.setStyle(Paint.Style.FILL);
        this.mTextStyle.setColor(Color.parseColor("#ffff00"));
        this.mTextStyle.setTextAlign(Paint.Align.CENTER);
        this.mTextStyle.setTextSize(24.0f);
    }

    public static SimpleFastPointOverlayOptions getDefaultStyle() {
        return new SimpleFastPointOverlayOptions();
    }

    public SimpleFastPointOverlayOptions setPointStyle(Paint paint) {
        this.mPointStyle = paint;
        return this;
    }

    public SimpleFastPointOverlayOptions setSelectedPointStyle(Paint paint) {
        this.mSelectedPointStyle = paint;
        return this;
    }

    public SimpleFastPointOverlayOptions setRadius(float f) {
        this.mCircleRadius = f;
        return this;
    }

    public SimpleFastPointOverlayOptions setSelectedRadius(float f) {
        this.mSelectedCircleRadius = f;
        return this;
    }

    public SimpleFastPointOverlayOptions setIsClickable(boolean z) {
        this.mClickable = z;
        return this;
    }

    public SimpleFastPointOverlayOptions setCellSize(int i) {
        this.mCellSize = i;
        return this;
    }

    public SimpleFastPointOverlayOptions setAlgorithm(RenderingAlgorithm renderingAlgorithm) {
        this.mAlgorithm = renderingAlgorithm;
        return this;
    }

    public SimpleFastPointOverlayOptions setSymbol(Shape shape) {
        this.mSymbol = shape;
        return this;
    }

    public SimpleFastPointOverlayOptions setTextStyle(Paint paint) {
        this.mTextStyle = paint;
        return this;
    }

    public SimpleFastPointOverlayOptions setMinZoomShowLabels(int i) {
        this.mMinZoomShowLabels = i;
        return this;
    }

    public SimpleFastPointOverlayOptions setMaxNShownLabels(int i) {
        this.mMaxNShownLabels = i;
        return this;
    }

    public SimpleFastPointOverlayOptions setLabelPolicy(LabelPolicy labelPolicy) {
        this.mLabelPolicy = labelPolicy;
        return this;
    }

    public Paint getPointStyle() {
        return this.mPointStyle;
    }

    public Paint getSelectedPointStyle() {
        return this.mSelectedPointStyle;
    }

    public Paint getTextStyle() {
        return this.mTextStyle;
    }

    public float getCircleRadius() {
        return this.mCircleRadius;
    }

    public float getSelectedCircleRadius() {
        return this.mSelectedCircleRadius;
    }

    public boolean isClickable() {
        return this.mClickable;
    }

    public int getCellSize() {
        return this.mCellSize;
    }

    public RenderingAlgorithm getAlgorithm() {
        return this.mAlgorithm;
    }

    public Shape getSymbol() {
        return this.mSymbol;
    }

    public LabelPolicy getLabelPolicy() {
        return this.mLabelPolicy;
    }

    public int getMaxNShownLabels() {
        return this.mMaxNShownLabels;
    }

    public int getMinZoomShowLabels() {
        return this.mMinZoomShowLabels;
    }
}
