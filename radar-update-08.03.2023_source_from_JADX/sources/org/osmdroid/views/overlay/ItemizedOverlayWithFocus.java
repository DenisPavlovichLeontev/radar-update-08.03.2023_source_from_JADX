package org.osmdroid.views.overlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.view.ViewCompat;
import java.util.List;
import org.osmdroid.library.C1340R;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

@Deprecated
public class ItemizedOverlayWithFocus<Item extends OverlayItem> extends ItemizedIconOverlay<Item> {
    private final int DEFAULTMARKER_BACKGROUNDCOLOR;
    private int DESCRIPTION_BOX_CORNERWIDTH;
    private int DESCRIPTION_BOX_PADDING;
    private int DESCRIPTION_LINE_HEIGHT;
    private int DESCRIPTION_MAXWIDTH;
    private int DESCRIPTION_TITLE_EXTRA_LINE_HEIGHT;
    private int FONT_SIZE_DP;
    private String UNKNOWN;
    private int fontSizePixels;
    private Context mContext;
    protected Paint mDescriptionPaint;
    protected boolean mFocusItemsOnTap;
    protected int mFocusedItemIndex;
    private final Point mFocusedScreenCoords;
    protected Paint mMarkerBackgroundPaint;
    protected int mMarkerFocusedBackgroundColor;
    protected Drawable mMarkerFocusedBase;
    private final Rect mRect;
    protected Paint mTitlePaint;

    public ItemizedOverlayWithFocus(Context context, List<Item> list, ItemizedIconOverlay.OnItemGestureListener<Item> onItemGestureListener) {
        this(list, onItemGestureListener, context);
    }

    public ItemizedOverlayWithFocus(List<Item> list, ItemizedIconOverlay.OnItemGestureListener<Item> onItemGestureListener, Context context) {
        this(list, context.getResources().getDrawable(C1340R.C1341drawable.marker_default), (Drawable) null, Integer.MIN_VALUE, onItemGestureListener, context);
    }

    public ItemizedOverlayWithFocus(List<Item> list, Drawable drawable, Drawable drawable2, int i, ItemizedIconOverlay.OnItemGestureListener<Item> onItemGestureListener, Context context) {
        super(list, drawable, onItemGestureListener, context);
        int rgb = Color.rgb(101, 185, 74);
        this.DEFAULTMARKER_BACKGROUNDCOLOR = rgb;
        this.DESCRIPTION_BOX_PADDING = 3;
        this.DESCRIPTION_BOX_CORNERWIDTH = 3;
        this.DESCRIPTION_TITLE_EXTRA_LINE_HEIGHT = 2;
        this.FONT_SIZE_DP = 14;
        this.DESCRIPTION_MAXWIDTH = TypedValues.Motion.TYPE_STAGGER;
        this.DESCRIPTION_LINE_HEIGHT = 30;
        this.mFocusedScreenCoords = new Point();
        this.mRect = new Rect();
        this.mContext = context;
        if (drawable2 == null) {
            this.mMarkerFocusedBase = boundToHotspot(context.getResources().getDrawable(C1340R.C1341drawable.marker_default_focused_base), OverlayItem.HotspotPlace.BOTTOM_CENTER);
        } else {
            this.mMarkerFocusedBase = drawable2;
        }
        this.mMarkerFocusedBackgroundColor = i == Integer.MIN_VALUE ? rgb : i;
        calculateDrawSettings();
        unSetFocusedItem();
    }

    private void calculateDrawSettings() {
        int applyDimension = (int) TypedValue.applyDimension(1, (float) this.FONT_SIZE_DP, this.mContext.getResources().getDisplayMetrics());
        this.fontSizePixels = applyDimension;
        this.DESCRIPTION_LINE_HEIGHT = applyDimension + 5;
        this.DESCRIPTION_MAXWIDTH = (int) (((double) this.mContext.getResources().getDisplayMetrics().widthPixels) * 0.8d);
        this.UNKNOWN = this.mContext.getResources().getString(C1340R.string.unknown);
        this.mMarkerBackgroundPaint = new Paint();
        Paint paint = new Paint();
        this.mDescriptionPaint = paint;
        paint.setAntiAlias(true);
        this.mDescriptionPaint.setTextSize((float) this.fontSizePixels);
        Paint paint2 = new Paint();
        this.mTitlePaint = paint2;
        paint2.setTextSize((float) this.fontSizePixels);
        this.mTitlePaint.setFakeBoldText(true);
        this.mTitlePaint.setAntiAlias(true);
    }

    public void setDescriptionBoxPadding(int i) {
        this.DESCRIPTION_BOX_PADDING = i;
    }

    public void setDescriptionBoxCornerWidth(int i) {
        this.DESCRIPTION_BOX_CORNERWIDTH = i;
    }

    public void setDescriptionTitleExtraLineHeight(int i) {
        this.DESCRIPTION_TITLE_EXTRA_LINE_HEIGHT = i;
    }

    public void setMarkerBackgroundColor(int i) {
        this.mMarkerFocusedBackgroundColor = i;
    }

    public void setMarkerTitleForegroundColor(int i) {
        this.mTitlePaint.setColor(i);
    }

    public void setMarkerDescriptionForegroundColor(int i) {
        this.mDescriptionPaint.setColor(i);
    }

    public void setFontSize(int i) {
        this.FONT_SIZE_DP = i;
        calculateDrawSettings();
    }

    public void setDescriptionMaxWidth(int i) {
        this.DESCRIPTION_MAXWIDTH = i;
        calculateDrawSettings();
    }

    public void setDescriptionLineHeight(int i) {
        this.DESCRIPTION_LINE_HEIGHT = i;
        calculateDrawSettings();
    }

    public Item getFocusedItem() {
        if (this.mFocusedItemIndex == Integer.MIN_VALUE) {
            return null;
        }
        return (OverlayItem) this.mItemList.get(this.mFocusedItemIndex);
    }

    public void setFocusedItem(int i) {
        this.mFocusedItemIndex = i;
    }

    public void unSetFocusedItem() {
        this.mFocusedItemIndex = Integer.MIN_VALUE;
    }

    public void setFocusedItem(Item item) {
        int indexOf = this.mItemList.indexOf(item);
        if (indexOf >= 0) {
            setFocusedItem(indexOf);
            return;
        }
        throw new IllegalArgumentException();
    }

    public void setFocusItemsOnTap(boolean z) {
        this.mFocusItemsOnTap = z;
    }

    /* access modifiers changed from: protected */
    public boolean onSingleTapUpHelper(int i, Item item, MapView mapView) {
        if (this.mFocusItemsOnTap) {
            this.mFocusedItemIndex = i;
            mapView.postInvalidate();
        }
        return this.mOnItemGestureListener.onItemSingleTapUp(i, item);
    }

    public void draw(Canvas canvas, Projection projection) {
        String str;
        String str2;
        Canvas canvas2 = canvas;
        super.draw(canvas, projection);
        if (this.mFocusedItemIndex != Integer.MIN_VALUE && this.mItemList != null) {
            OverlayItem overlayItem = (OverlayItem) this.mItemList.get(this.mFocusedItemIndex);
            Drawable marker = overlayItem.getMarker(4);
            if (marker == null) {
                marker = this.mMarkerFocusedBase;
            }
            Drawable drawable = marker;
            projection.toPixels(overlayItem.getPoint(), this.mFocusedScreenCoords);
            drawable.copyBounds(this.mRect);
            this.mRect.offset(this.mFocusedScreenCoords.x, this.mFocusedScreenCoords.y);
            if (overlayItem.getTitle() == null) {
                str = this.UNKNOWN;
            } else {
                str = overlayItem.getTitle();
            }
            if (overlayItem.getSnippet() == null) {
                str2 = this.UNKNOWN;
            } else {
                str2 = overlayItem.getSnippet();
            }
            int length = str2.length();
            float[] fArr = new float[length];
            this.mDescriptionPaint.getTextWidths(str2, fArr);
            StringBuilder sb = new StringBuilder();
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            while (i < length) {
                if (!Character.isLetter(str2.charAt(i))) {
                    i5 = i;
                }
                float f = fArr[i];
                if (str2.charAt(i) == 10) {
                    int i6 = i + 1;
                    sb.append(str2.subSequence(i2, i6));
                    i5 = i6;
                    i3 = Math.max(i3, i4);
                    i4 = 0;
                    i2 = i5;
                } else {
                    if (((float) i4) + f > ((float) this.DESCRIPTION_MAXWIDTH)) {
                        boolean z = i2 == i5;
                        if (!z) {
                            i = i5;
                        }
                        sb.append(str2.subSequence(i2, i));
                        sb.append(10);
                        i3 = Math.max(i3, i4);
                        if (z) {
                            i2 = i;
                            i5 = i2;
                            i4 = 0;
                            i--;
                        } else {
                            i2 = i;
                            i5 = i2;
                            i4 = 0;
                        }
                    }
                    i4 = (int) (((float) i4) + f);
                }
                i++;
            }
            if (i != i2) {
                String substring = str2.substring(i2, i);
                i3 = Math.max(i3, (int) this.mDescriptionPaint.measureText(substring));
                sb.append(substring);
            }
            String[] split = sb.toString().split("\n");
            int min = Math.min(Math.max(i3, (int) this.mDescriptionPaint.measureText(str)), this.DESCRIPTION_MAXWIDTH);
            int width = ((this.mRect.left - (min / 2)) - this.DESCRIPTION_BOX_PADDING) + (this.mRect.width() / 2);
            int i7 = min + width + (this.DESCRIPTION_BOX_PADDING * 2);
            int i8 = this.mRect.top;
            int length2 = ((i8 - this.DESCRIPTION_TITLE_EXTRA_LINE_HEIGHT) - ((split.length + 1) * this.DESCRIPTION_LINE_HEIGHT)) - (this.DESCRIPTION_BOX_PADDING * 2);
            if (projection.getOrientation() != 0.0f) {
                canvas.save();
                canvas2.rotate(-projection.getOrientation(), (float) this.mFocusedScreenCoords.x, (float) this.mFocusedScreenCoords.y);
            }
            this.mMarkerBackgroundPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
            RectF rectF = new RectF((float) (width - 1), (float) (length2 - 1), (float) (i7 + 1), (float) (i8 + 1));
            int i9 = this.DESCRIPTION_BOX_CORNERWIDTH;
            canvas2.drawRoundRect(rectF, (float) i9, (float) i9, this.mDescriptionPaint);
            this.mMarkerBackgroundPaint.setColor(this.mMarkerFocusedBackgroundColor);
            float f2 = (float) width;
            float f3 = (float) i7;
            RectF rectF2 = new RectF(f2, (float) length2, f3, (float) i8);
            int i10 = this.DESCRIPTION_BOX_CORNERWIDTH;
            canvas2.drawRoundRect(rectF2, (float) i10, (float) i10, this.mMarkerBackgroundPaint);
            int i11 = this.DESCRIPTION_BOX_PADDING;
            int i12 = width + i11;
            int i13 = i8 - i11;
            for (int length3 = split.length - 1; length3 >= 0; length3--) {
                canvas2.drawText(split[length3].trim(), (float) i12, (float) i13, this.mDescriptionPaint);
                i13 -= this.DESCRIPTION_LINE_HEIGHT;
            }
            canvas2.drawText(str, (float) i12, (float) (i13 - this.DESCRIPTION_TITLE_EXTRA_LINE_HEIGHT), this.mTitlePaint);
            float f4 = (float) i13;
            canvas.drawLine(f2, f4, f3, f4, this.mDescriptionPaint);
            drawable.setBounds(this.mRect);
            drawable.draw(canvas2);
            this.mRect.offset(-this.mFocusedScreenCoords.x, -this.mFocusedScreenCoords.y);
            drawable.setBounds(this.mRect);
            if (projection.getOrientation() != 0.0f) {
                canvas.restore();
            }
        }
    }

    public void onDetach(MapView mapView) {
        super.onDetach(mapView);
        this.mContext = null;
    }
}
