package org.osmdroid.views.overlay.gestures;

import android.view.MotionEvent;

public class RotationGestureDetector {
    private boolean mEnabled = true;
    private RotationListener mListener;
    protected float mRotation;

    public interface RotationListener {
        void onRotate(float f);
    }

    public RotationGestureDetector(RotationListener rotationListener) {
        this.mListener = rotationListener;
    }

    private static float rotation(MotionEvent motionEvent) {
        return (float) Math.toDegrees(Math.atan2((double) (motionEvent.getY(0) - motionEvent.getY(1)), (double) (motionEvent.getX(0) - motionEvent.getX(1))));
    }

    public void onTouch(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() == 2) {
            if (motionEvent.getActionMasked() == 5) {
                this.mRotation = rotation(motionEvent);
            }
            float rotation = rotation(motionEvent);
            float f = this.mRotation;
            float f2 = rotation - f;
            if (this.mEnabled) {
                this.mRotation = f + f2;
                this.mListener.onRotate(f2);
                return;
            }
            this.mRotation = rotation;
        }
    }

    public void setEnabled(boolean z) {
        this.mEnabled = z;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }
}
