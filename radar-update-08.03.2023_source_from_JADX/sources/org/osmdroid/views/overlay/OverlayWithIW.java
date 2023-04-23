package org.osmdroid.views.overlay;

import android.content.Context;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

public abstract class OverlayWithIW extends Overlay {
    protected String mId;
    protected InfoWindow mInfoWindow;
    protected Object mRelatedObject;
    protected String mSnippet;
    protected String mSubDescription;
    protected String mTitle;

    @Deprecated
    public OverlayWithIW(Context context) {
        this();
    }

    public OverlayWithIW() {
    }

    public void setTitle(String str) {
        this.mTitle = str;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setSnippet(String str) {
        this.mSnippet = str;
    }

    public String getSnippet() {
        return this.mSnippet;
    }

    public void setSubDescription(String str) {
        this.mSubDescription = str;
    }

    public String getSubDescription() {
        return this.mSubDescription;
    }

    public void setRelatedObject(Object obj) {
        this.mRelatedObject = obj;
    }

    public Object getRelatedObject() {
        return this.mRelatedObject;
    }

    public void setId(String str) {
        this.mId = str;
    }

    public String getId() {
        return this.mId;
    }

    public void setInfoWindow(InfoWindow infoWindow) {
        this.mInfoWindow = infoWindow;
    }

    public InfoWindow getInfoWindow() {
        return this.mInfoWindow;
    }

    public void closeInfoWindow() {
        InfoWindow infoWindow = this.mInfoWindow;
        if (infoWindow != null) {
            infoWindow.close();
        }
    }

    public void onDestroy() {
        InfoWindow infoWindow = this.mInfoWindow;
        if (infoWindow != null) {
            infoWindow.close();
            this.mInfoWindow.onDetach();
            this.mInfoWindow = null;
            this.mRelatedObject = null;
        }
    }

    public boolean isInfoWindowOpen() {
        InfoWindow infoWindow = this.mInfoWindow;
        return infoWindow != null && infoWindow.isOpen();
    }
}
