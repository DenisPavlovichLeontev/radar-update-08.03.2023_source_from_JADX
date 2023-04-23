package org.osmdroid.views.overlay;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import java.util.List;
import org.osmdroid.api.IMapView;
import org.osmdroid.library.C1340R;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;

public class ItemizedIconOverlay<Item extends OverlayItem> extends ItemizedOverlay<Item> {
    protected List<Item> mItemList;
    protected OnItemGestureListener<Item> mOnItemGestureListener;

    public interface ActiveItem {
        boolean run(int i);
    }

    public interface OnItemGestureListener<T> {
        boolean onItemLongPress(int i, T t);

        boolean onItemSingleTapUp(int i, T t);
    }

    public boolean onSnapToItem(int i, int i2, Point point, IMapView iMapView) {
        return false;
    }

    public ItemizedIconOverlay(List<Item> list, Drawable drawable, OnItemGestureListener<Item> onItemGestureListener, Context context) {
        super(drawable);
        this.mItemList = list;
        this.mOnItemGestureListener = onItemGestureListener;
        populate();
    }

    public ItemizedIconOverlay(List<Item> list, OnItemGestureListener<Item> onItemGestureListener, Context context) {
        this(list, context.getResources().getDrawable(C1340R.C1341drawable.marker_default), onItemGestureListener, context);
    }

    public ItemizedIconOverlay(Context context, List<Item> list, OnItemGestureListener<Item> onItemGestureListener) {
        this(list, context.getResources().getDrawable(C1340R.C1341drawable.marker_default), onItemGestureListener, context);
    }

    public void onDetach(MapView mapView) {
        List<Item> list = this.mItemList;
        if (list != null) {
            list.clear();
        }
        this.mItemList = null;
        this.mOnItemGestureListener = null;
    }

    /* access modifiers changed from: protected */
    public Item createItem(int i) {
        return (OverlayItem) this.mItemList.get(i);
    }

    public int size() {
        return Math.min(this.mItemList.size(), this.mDrawnItemsLimit);
    }

    public boolean addItem(Item item) {
        boolean add = this.mItemList.add(item);
        populate();
        return add;
    }

    public void addItem(int i, Item item) {
        this.mItemList.add(i, item);
        populate();
    }

    public boolean addItems(List<Item> list) {
        boolean addAll = this.mItemList.addAll(list);
        populate();
        return addAll;
    }

    public void removeAllItems() {
        removeAllItems(true);
    }

    public void removeAllItems(boolean z) {
        this.mItemList.clear();
        if (z) {
            populate();
        }
    }

    public boolean removeItem(Item item) {
        boolean remove = this.mItemList.remove(item);
        populate();
        return remove;
    }

    public Item removeItem(int i) {
        Item item = (OverlayItem) this.mItemList.remove(i);
        populate();
        return item;
    }

    public boolean onSingleTapConfirmed(MotionEvent motionEvent, final MapView mapView) {
        if (activateSelectedItems(motionEvent, mapView, new ActiveItem() {
            public boolean run(int i) {
                ItemizedIconOverlay itemizedIconOverlay = ItemizedIconOverlay.this;
                if (itemizedIconOverlay.mOnItemGestureListener == null) {
                    return false;
                }
                return ItemizedIconOverlay.this.onSingleTapUpHelper(i, (OverlayItem) itemizedIconOverlay.mItemList.get(i), mapView);
            }
        })) {
            return true;
        }
        return super.onSingleTapConfirmed(motionEvent, mapView);
    }

    /* access modifiers changed from: protected */
    public boolean onSingleTapUpHelper(int i, Item item, MapView mapView) {
        return this.mOnItemGestureListener.onItemSingleTapUp(i, item);
    }

    public boolean onLongPress(MotionEvent motionEvent, MapView mapView) {
        if (activateSelectedItems(motionEvent, mapView, new ActiveItem() {
            public boolean run(int i) {
                if (ItemizedIconOverlay.this.mOnItemGestureListener == null) {
                    return false;
                }
                ItemizedIconOverlay itemizedIconOverlay = ItemizedIconOverlay.this;
                return itemizedIconOverlay.onLongPressHelper(i, itemizedIconOverlay.getItem(i));
            }
        })) {
            return true;
        }
        return super.onLongPress(motionEvent, mapView);
    }

    /* access modifiers changed from: protected */
    public boolean onLongPressHelper(int i, Item item) {
        return this.mOnItemGestureListener.onItemLongPress(i, item);
    }

    private boolean activateSelectedItems(MotionEvent motionEvent, MapView mapView, ActiveItem activeItem) {
        int round = Math.round(motionEvent.getX());
        int round2 = Math.round(motionEvent.getY());
        for (int i = 0; i < this.mItemList.size(); i++) {
            if (isEventOnItem(getItem(i), round, round2, mapView) && activeItem.run(i)) {
                return true;
            }
        }
        return false;
    }
}
