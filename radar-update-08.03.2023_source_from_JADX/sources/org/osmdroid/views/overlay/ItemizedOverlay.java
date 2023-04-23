package org.osmdroid.views.overlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.List;
import org.osmdroid.util.RectL;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

public abstract class ItemizedOverlay<Item extends OverlayItem> extends Overlay implements Overlay.Snappable {
    private Rect itemRect;
    private final Point mCurScreenCoords;
    protected final Drawable mDefaultMarker;
    protected boolean mDrawFocusedItem;
    protected int mDrawnItemsLimit;
    private Item mFocusedItem;
    private boolean[] mInternalItemDisplayedList;
    private final ArrayList<Item> mInternalItemList;
    private final Rect mMarkerRect;
    private OnFocusChangeListener mOnFocusChangeListener;
    private final Rect mOrientedMarkerRect;
    private boolean mPendingFocusChangedEvent;
    private final Rect mRect;
    private Rect screenRect;

    public interface OnFocusChangeListener {
        void onFocusChanged(ItemizedOverlay<?> itemizedOverlay, OverlayItem overlayItem);
    }

    /* access modifiers changed from: protected */
    public abstract Item createItem(int i);

    public void onDetach(MapView mapView) {
    }

    /* access modifiers changed from: protected */
    public boolean onTap(int i) {
        return false;
    }

    public abstract int size();

    @Deprecated
    public ItemizedOverlay(Context context, Drawable drawable) {
        this(drawable);
    }

    public ItemizedOverlay(Drawable drawable) {
        this.mDrawnItemsLimit = Integer.MAX_VALUE;
        this.mRect = new Rect();
        this.mMarkerRect = new Rect();
        this.mOrientedMarkerRect = new Rect();
        this.mCurScreenCoords = new Point();
        this.mDrawFocusedItem = true;
        this.mPendingFocusChangedEvent = false;
        this.itemRect = new Rect();
        this.screenRect = new Rect();
        if (drawable != null) {
            this.mDefaultMarker = drawable;
            this.mInternalItemList = new ArrayList<>();
            return;
        }
        throw new IllegalArgumentException("You must pass a default marker to ItemizedOverlay.");
    }

    public int getDrawnItemsLimit() {
        return this.mDrawnItemsLimit;
    }

    public void setDrawnItemsLimit(int i) {
        this.mDrawnItemsLimit = i;
    }

    public void draw(Canvas canvas, Projection projection) {
        OnFocusChangeListener onFocusChangeListener;
        if (this.mPendingFocusChangedEvent && (onFocusChangeListener = this.mOnFocusChangeListener) != null) {
            onFocusChangeListener.onFocusChanged(this, this.mFocusedItem);
        }
        this.mPendingFocusChangedEvent = false;
        int min = Math.min(this.mInternalItemList.size(), this.mDrawnItemsLimit);
        boolean[] zArr = this.mInternalItemDisplayedList;
        if (zArr == null || zArr.length != min) {
            this.mInternalItemDisplayedList = new boolean[min];
        }
        for (int i = min - 1; i >= 0; i--) {
            OverlayItem item = getItem(i);
            if (item != null) {
                projection.toPixels(item.getPoint(), this.mCurScreenCoords);
                calculateItemRect(item, this.mCurScreenCoords, this.itemRect);
                this.mInternalItemDisplayedList[i] = onDrawItem(canvas, item, this.mCurScreenCoords, projection);
            }
        }
    }

    /* access modifiers changed from: protected */
    public final void populate() {
        int size = size();
        this.mInternalItemList.clear();
        this.mInternalItemList.ensureCapacity(size);
        for (int i = 0; i < size; i++) {
            this.mInternalItemList.add(createItem(i));
        }
        this.mInternalItemDisplayedList = null;
    }

    public final Item getItem(int i) {
        try {
            return (OverlayItem) this.mInternalItemList.get(i);
        } catch (IndexOutOfBoundsException unused) {
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public boolean onDrawItem(Canvas canvas, Item item, Point point, Projection projection) {
        Drawable drawable;
        int i = (!this.mDrawFocusedItem || this.mFocusedItem != item) ? 0 : 4;
        if (item.getMarker(i) == null) {
            drawable = getDefaultMarker(i);
        } else {
            drawable = item.getMarker(i);
        }
        boundToHotspot(drawable, item.getMarkerHotspot());
        int i2 = this.mCurScreenCoords.x;
        int i3 = this.mCurScreenCoords.y;
        drawable.copyBounds(this.mRect);
        this.mMarkerRect.set(this.mRect);
        this.mRect.offset(i2, i3);
        RectL.getBounds(this.mRect, i2, i3, (double) projection.getOrientation(), this.mOrientedMarkerRect);
        boolean intersects = Rect.intersects(this.mOrientedMarkerRect, canvas.getClipBounds());
        if (intersects) {
            if (projection.getOrientation() != 0.0f) {
                canvas.save();
                canvas.rotate(-projection.getOrientation(), (float) i2, (float) i3);
            }
            drawable.setBounds(this.mRect);
            drawable.draw(canvas);
            if (projection.getOrientation() != 0.0f) {
                canvas.restore();
            }
            drawable.setBounds(this.mMarkerRect);
        }
        return intersects;
    }

    public List<Item> getDisplayedItems() {
        ArrayList arrayList = new ArrayList();
        if (this.mInternalItemDisplayedList == null) {
            return arrayList;
        }
        int i = 0;
        while (true) {
            boolean[] zArr = this.mInternalItemDisplayedList;
            if (i >= zArr.length) {
                return arrayList;
            }
            if (zArr[i]) {
                arrayList.add(getItem(i));
            }
            i++;
        }
    }

    /* access modifiers changed from: protected */
    public Drawable getDefaultMarker(int i) {
        OverlayItem.setState(this.mDefaultMarker, i);
        return this.mDefaultMarker;
    }

    /* access modifiers changed from: protected */
    public boolean hitTest(Item item, Drawable drawable, int i, int i2) {
        return drawable.getBounds().contains(i, i2);
    }

    public boolean onSingleTapConfirmed(MotionEvent motionEvent, MapView mapView) {
        int size = size();
        int round = Math.round(motionEvent.getX());
        int round2 = Math.round(motionEvent.getY());
        for (int i = 0; i < size; i++) {
            if (isEventOnItem(getItem(i), round, round2, mapView) && onTap(i)) {
                return true;
            }
        }
        return super.onSingleTapConfirmed(motionEvent, mapView);
    }

    public void setDrawFocusedItem(boolean z) {
        this.mDrawFocusedItem = z;
    }

    public void setFocus(Item item) {
        this.mPendingFocusChangedEvent = item != this.mFocusedItem;
        this.mFocusedItem = item;
    }

    public Item getFocus() {
        return this.mFocusedItem;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0038, code lost:
        if (r8 != 6) goto L_0x0040;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.drawable.Drawable boundToHotspot(android.graphics.drawable.Drawable r7, org.osmdroid.views.overlay.OverlayItem.HotspotPlace r8) {
        /*
            r6 = this;
            if (r8 != 0) goto L_0x0004
            org.osmdroid.views.overlay.OverlayItem$HotspotPlace r8 = org.osmdroid.views.overlay.OverlayItem.HotspotPlace.BOTTOM_CENTER
        L_0x0004:
            int r0 = r7.getIntrinsicWidth()
            int r1 = r7.getIntrinsicHeight()
            int[] r2 = org.osmdroid.views.overlay.ItemizedOverlay.C13841.$SwitchMap$org$osmdroid$views$overlay$OverlayItem$HotspotPlace
            int r3 = r8.ordinal()
            r2 = r2[r3]
            r3 = 0
            r4 = 2
            switch(r2) {
                case 5: goto L_0x001d;
                case 6: goto L_0x001d;
                case 7: goto L_0x001d;
                case 8: goto L_0x001b;
                case 9: goto L_0x001b;
                case 10: goto L_0x001b;
                default: goto L_0x0019;
            }
        L_0x0019:
            r2 = r3
            goto L_0x001f
        L_0x001b:
            int r2 = -r0
            goto L_0x001f
        L_0x001d:
            int r2 = -r0
            int r2 = r2 / r4
        L_0x001f:
            int[] r5 = org.osmdroid.views.overlay.ItemizedOverlay.C13841.$SwitchMap$org$osmdroid$views$overlay$OverlayItem$HotspotPlace
            int r8 = r8.ordinal()
            r8 = r5[r8]
            if (r8 == r4) goto L_0x003d
            r5 = 8
            if (r8 == r5) goto L_0x003d
            r5 = 10
            if (r8 == r5) goto L_0x003b
            r5 = 4
            if (r8 == r5) goto L_0x003b
            r5 = 5
            if (r8 == r5) goto L_0x003d
            r4 = 6
            if (r8 == r4) goto L_0x003b
            goto L_0x0040
        L_0x003b:
            int r3 = -r1
            goto L_0x0040
        L_0x003d:
            int r8 = -r1
            int r3 = r8 / 2
        L_0x0040:
            int r0 = r0 + r2
            int r1 = r1 + r3
            r7.setBounds(r2, r3, r0, r1)
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.views.overlay.ItemizedOverlay.boundToHotspot(android.graphics.drawable.Drawable, org.osmdroid.views.overlay.OverlayItem$HotspotPlace):android.graphics.drawable.Drawable");
    }

    /* access modifiers changed from: protected */
    public Rect calculateItemRect(Item item, Point point, Rect rect) {
        if (rect == null) {
            rect = new Rect();
        }
        OverlayItem.HotspotPlace markerHotspot = item.getMarkerHotspot();
        if (markerHotspot == null) {
            markerHotspot = OverlayItem.HotspotPlace.BOTTOM_CENTER;
        }
        int i = (!this.mDrawFocusedItem || this.mFocusedItem != item) ? 0 : 4;
        Drawable defaultMarker = item.getMarker(i) == null ? getDefaultMarker(i) : item.getMarker(i);
        int intrinsicWidth = defaultMarker.getIntrinsicWidth();
        int intrinsicHeight = defaultMarker.getIntrinsicHeight();
        switch (C13841.$SwitchMap$org$osmdroid$views$overlay$OverlayItem$HotspotPlace[markerHotspot.ordinal()]) {
            case 1:
                int i2 = intrinsicWidth / 2;
                int i3 = intrinsicHeight / 2;
                rect.set(point.x - i2, point.y - i3, point.x + i2, point.y + i3);
                break;
            case 2:
                int i4 = intrinsicHeight / 2;
                rect.set(point.x, point.y - i4, point.x + intrinsicWidth, point.y + i4);
                break;
            case 3:
                rect.set(point.x, point.y, point.x + intrinsicWidth, point.y + intrinsicHeight);
                break;
            case 4:
                rect.set(point.x, point.y - intrinsicHeight, point.x + intrinsicWidth, point.y);
                break;
            case 5:
                int i5 = intrinsicWidth / 2;
                int i6 = intrinsicHeight / 2;
                rect.set(point.x - i5, point.y - i6, point.x + i5, point.y + i6);
                break;
            case 6:
                int i7 = intrinsicWidth / 2;
                rect.set(point.x - i7, point.y - intrinsicHeight, point.x + i7, point.y);
                break;
            case 7:
                int i8 = intrinsicWidth / 2;
                rect.set(point.x - i8, point.y, point.x + i8, point.y + intrinsicHeight);
                break;
            case 8:
                int i9 = intrinsicHeight / 2;
                rect.set(point.x - intrinsicWidth, point.y - i9, point.x, point.y + i9);
                break;
            case 9:
                rect.set(point.x - intrinsicWidth, point.y, point.x, point.y + intrinsicHeight);
                break;
            case 10:
                rect.set(point.x - intrinsicWidth, point.y - intrinsicHeight, point.x, point.y);
                break;
        }
        return rect;
    }

    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        this.mOnFocusChangeListener = onFocusChangeListener;
    }

    /* access modifiers changed from: protected */
    public boolean isEventOnItem(Item item, int i, int i2, MapView mapView) {
        int i3 = 0;
        if (item == null) {
            return false;
        }
        mapView.getProjection().toPixels(item.getPoint(), this.mCurScreenCoords);
        if (this.mDrawFocusedItem && this.mFocusedItem == item) {
            i3 = 4;
        }
        Drawable marker = item.getMarker(i3);
        if (marker == null) {
            marker = getDefaultMarker(i3);
        }
        boundToHotspot(marker, item.getMarkerHotspot());
        marker.copyBounds(this.mRect);
        this.mRect.offset(this.mCurScreenCoords.x, this.mCurScreenCoords.y);
        RectL.getBounds(this.mRect, this.mCurScreenCoords.x, this.mCurScreenCoords.y, (double) (-mapView.getMapOrientation()), this.mOrientedMarkerRect);
        return this.mOrientedMarkerRect.contains(i, i2);
    }
}
