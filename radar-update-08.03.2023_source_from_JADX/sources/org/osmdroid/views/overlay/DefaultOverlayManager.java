package org.osmdroid.views.overlay;

import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;
import org.osmdroid.api.IMapView;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay;

public class DefaultOverlayManager extends AbstractList<Overlay> implements OverlayManager {
    /* access modifiers changed from: private */
    public final CopyOnWriteArrayList<Overlay> mOverlayList = new CopyOnWriteArrayList<>();
    private TilesOverlay mTilesOverlay;

    public DefaultOverlayManager(TilesOverlay tilesOverlay) {
        setTilesOverlay(tilesOverlay);
    }

    public Overlay get(int i) {
        return this.mOverlayList.get(i);
    }

    public int size() {
        return this.mOverlayList.size();
    }

    public void add(int i, Overlay overlay) {
        if (overlay == null) {
            Log.e(IMapView.LOGTAG, "Attempt to add a null overlay to the collection. This is probably a bug and should be reported!", new Exception());
        } else {
            this.mOverlayList.add(i, overlay);
        }
    }

    public Overlay remove(int i) {
        return this.mOverlayList.remove(i);
    }

    public Overlay set(int i, Overlay overlay) {
        if (overlay != null) {
            return this.mOverlayList.set(i, overlay);
        }
        Log.e(IMapView.LOGTAG, "Attempt to set a null overlay to the collection. This is probably a bug and should be reported!", new Exception());
        return null;
    }

    public TilesOverlay getTilesOverlay() {
        return this.mTilesOverlay;
    }

    public void setTilesOverlay(TilesOverlay tilesOverlay) {
        this.mTilesOverlay = tilesOverlay;
    }

    public Iterable<Overlay> overlaysReversed() {
        return new Iterable<Overlay>() {
            /* JADX WARNING: Can't wrap try/catch for region: R(3:0|1|2) */
            /* JADX WARNING: Missing exception handler attribute for start block: B:0:0x0000 */
            /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[MTH_ENTER_BLOCK, SYNTHETIC, Splitter:B:0:0x0000] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            private java.util.ListIterator<org.osmdroid.views.overlay.Overlay> bulletProofReverseListIterator() {
                /*
                    r2 = this;
                L_0x0000:
                    org.osmdroid.views.overlay.DefaultOverlayManager r0 = org.osmdroid.views.overlay.DefaultOverlayManager.this     // Catch:{ IndexOutOfBoundsException -> 0x0000 }
                    java.util.concurrent.CopyOnWriteArrayList r0 = r0.mOverlayList     // Catch:{ IndexOutOfBoundsException -> 0x0000 }
                    org.osmdroid.views.overlay.DefaultOverlayManager r1 = org.osmdroid.views.overlay.DefaultOverlayManager.this     // Catch:{ IndexOutOfBoundsException -> 0x0000 }
                    java.util.concurrent.CopyOnWriteArrayList r1 = r1.mOverlayList     // Catch:{ IndexOutOfBoundsException -> 0x0000 }
                    int r1 = r1.size()     // Catch:{ IndexOutOfBoundsException -> 0x0000 }
                    java.util.ListIterator r0 = r0.listIterator(r1)     // Catch:{ IndexOutOfBoundsException -> 0x0000 }
                    return r0
                */
                throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.views.overlay.DefaultOverlayManager.C13801.bulletProofReverseListIterator():java.util.ListIterator");
            }

            public Iterator<Overlay> iterator() {
                final ListIterator<Overlay> bulletProofReverseListIterator = bulletProofReverseListIterator();
                return new Iterator<Overlay>() {
                    public boolean hasNext() {
                        return bulletProofReverseListIterator.hasPrevious();
                    }

                    public Overlay next() {
                        return (Overlay) bulletProofReverseListIterator.previous();
                    }

                    public void remove() {
                        bulletProofReverseListIterator.remove();
                    }
                };
            }
        };
    }

    public List<Overlay> overlays() {
        return this.mOverlayList;
    }

    public void onDraw(Canvas canvas, MapView mapView) {
        onDrawHelper(canvas, mapView, mapView.getProjection());
    }

    public void onDraw(Canvas canvas, Projection projection) {
        onDrawHelper(canvas, (MapView) null, projection);
    }

    private void onDrawHelper(Canvas canvas, MapView mapView, Projection projection) {
        TilesOverlay tilesOverlay = this.mTilesOverlay;
        if (tilesOverlay != null) {
            tilesOverlay.protectDisplayedTilesForCache(canvas, projection);
        }
        Iterator<Overlay> it = this.mOverlayList.iterator();
        while (it.hasNext()) {
            Overlay next = it.next();
            if (next != null && next.isEnabled() && (next instanceof TilesOverlay)) {
                ((TilesOverlay) next).protectDisplayedTilesForCache(canvas, projection);
            }
        }
        TilesOverlay tilesOverlay2 = this.mTilesOverlay;
        if (tilesOverlay2 != null && tilesOverlay2.isEnabled()) {
            if (mapView != null) {
                this.mTilesOverlay.draw(canvas, mapView, false);
            } else {
                this.mTilesOverlay.draw(canvas, projection);
            }
        }
        Iterator<Overlay> it2 = this.mOverlayList.iterator();
        while (it2.hasNext()) {
            Overlay next2 = it2.next();
            if (next2 != null && next2.isEnabled()) {
                if (mapView != null) {
                    next2.draw(canvas, mapView, false);
                } else {
                    next2.draw(canvas, projection);
                }
            }
        }
    }

    public void onDetach(MapView mapView) {
        TilesOverlay tilesOverlay = this.mTilesOverlay;
        if (tilesOverlay != null) {
            tilesOverlay.onDetach(mapView);
        }
        for (Overlay onDetach : overlaysReversed()) {
            onDetach.onDetach(mapView);
        }
        clear();
    }

    public void onPause() {
        TilesOverlay tilesOverlay = this.mTilesOverlay;
        if (tilesOverlay != null) {
            tilesOverlay.onPause();
        }
        for (Overlay onPause : overlaysReversed()) {
            onPause.onPause();
        }
    }

    public void onResume() {
        TilesOverlay tilesOverlay = this.mTilesOverlay;
        if (tilesOverlay != null) {
            tilesOverlay.onResume();
        }
        for (Overlay onResume : overlaysReversed()) {
            onResume.onResume();
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent, MapView mapView) {
        for (Overlay onKeyDown : overlaysReversed()) {
            if (onKeyDown.onKeyDown(i, keyEvent, mapView)) {
                return true;
            }
        }
        return false;
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent, MapView mapView) {
        for (Overlay onKeyUp : overlaysReversed()) {
            if (onKeyUp.onKeyUp(i, keyEvent, mapView)) {
                return true;
            }
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        for (Overlay onTouchEvent : overlaysReversed()) {
            if (onTouchEvent.onTouchEvent(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }

    public boolean onTrackballEvent(MotionEvent motionEvent, MapView mapView) {
        for (Overlay onTrackballEvent : overlaysReversed()) {
            if (onTrackballEvent.onTrackballEvent(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }

    public boolean onSnapToItem(int i, int i2, Point point, IMapView iMapView) {
        for (Overlay next : overlaysReversed()) {
            if ((next instanceof Overlay.Snappable) && ((Overlay.Snappable) next).onSnapToItem(i, i2, point, iMapView)) {
                return true;
            }
        }
        return false;
    }

    public boolean onDoubleTap(MotionEvent motionEvent, MapView mapView) {
        for (Overlay onDoubleTap : overlaysReversed()) {
            if (onDoubleTap.onDoubleTap(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }

    public boolean onDoubleTapEvent(MotionEvent motionEvent, MapView mapView) {
        for (Overlay onDoubleTapEvent : overlaysReversed()) {
            if (onDoubleTapEvent.onDoubleTapEvent(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }

    public boolean onSingleTapConfirmed(MotionEvent motionEvent, MapView mapView) {
        for (Overlay onSingleTapConfirmed : overlaysReversed()) {
            if (onSingleTapConfirmed.onSingleTapConfirmed(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }

    public boolean onDown(MotionEvent motionEvent, MapView mapView) {
        for (Overlay onDown : overlaysReversed()) {
            if (onDown.onDown(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2, MapView mapView) {
        for (Overlay onFling : overlaysReversed()) {
            if (onFling.onFling(motionEvent, motionEvent2, f, f2, mapView)) {
                return true;
            }
        }
        return false;
    }

    public boolean onLongPress(MotionEvent motionEvent, MapView mapView) {
        for (Overlay onLongPress : overlaysReversed()) {
            if (onLongPress.onLongPress(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2, MapView mapView) {
        for (Overlay onScroll : overlaysReversed()) {
            if (onScroll.onScroll(motionEvent, motionEvent2, f, f2, mapView)) {
                return true;
            }
        }
        return false;
    }

    public void onShowPress(MotionEvent motionEvent, MapView mapView) {
        for (Overlay onShowPress : overlaysReversed()) {
            onShowPress.onShowPress(motionEvent, mapView);
        }
    }

    public boolean onSingleTapUp(MotionEvent motionEvent, MapView mapView) {
        for (Overlay onSingleTapUp : overlaysReversed()) {
            if (onSingleTapUp.onSingleTapUp(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }

    public void setOptionsMenusEnabled(boolean z) {
        Iterator<Overlay> it = this.mOverlayList.iterator();
        while (it.hasNext()) {
            Overlay next = it.next();
            if (next instanceof IOverlayMenuProvider) {
                IOverlayMenuProvider iOverlayMenuProvider = (IOverlayMenuProvider) next;
                if (iOverlayMenuProvider.isOptionsMenuEnabled()) {
                    iOverlayMenuProvider.setOptionsMenuEnabled(z);
                }
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu, int i, MapView mapView) {
        boolean z = true;
        for (Overlay next : overlaysReversed()) {
            if (next instanceof IOverlayMenuProvider) {
                IOverlayMenuProvider iOverlayMenuProvider = (IOverlayMenuProvider) next;
                if (iOverlayMenuProvider.isOptionsMenuEnabled()) {
                    z &= iOverlayMenuProvider.onCreateOptionsMenu(menu, i, mapView);
                }
            }
        }
        TilesOverlay tilesOverlay = this.mTilesOverlay;
        return (tilesOverlay == null || !tilesOverlay.isOptionsMenuEnabled()) ? z : z & this.mTilesOverlay.onCreateOptionsMenu(menu, i, mapView);
    }

    public boolean onPrepareOptionsMenu(Menu menu, int i, MapView mapView) {
        for (Overlay next : overlaysReversed()) {
            if (next instanceof IOverlayMenuProvider) {
                IOverlayMenuProvider iOverlayMenuProvider = (IOverlayMenuProvider) next;
                if (iOverlayMenuProvider.isOptionsMenuEnabled()) {
                    iOverlayMenuProvider.onPrepareOptionsMenu(menu, i, mapView);
                }
            }
        }
        TilesOverlay tilesOverlay = this.mTilesOverlay;
        if (tilesOverlay == null || !tilesOverlay.isOptionsMenuEnabled()) {
            return true;
        }
        this.mTilesOverlay.onPrepareOptionsMenu(menu, i, mapView);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem, int i, MapView mapView) {
        for (Overlay next : overlaysReversed()) {
            if (next instanceof IOverlayMenuProvider) {
                IOverlayMenuProvider iOverlayMenuProvider = (IOverlayMenuProvider) next;
                if (iOverlayMenuProvider.isOptionsMenuEnabled() && iOverlayMenuProvider.onOptionsItemSelected(menuItem, i, mapView)) {
                    return true;
                }
            }
        }
        TilesOverlay tilesOverlay = this.mTilesOverlay;
        if (tilesOverlay == null || !tilesOverlay.isOptionsMenuEnabled() || !this.mTilesOverlay.onOptionsItemSelected(menuItem, i, mapView)) {
            return false;
        }
        return true;
    }
}
