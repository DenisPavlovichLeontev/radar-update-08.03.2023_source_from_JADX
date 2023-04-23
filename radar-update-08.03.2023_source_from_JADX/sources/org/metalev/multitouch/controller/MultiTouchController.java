package org.metalev.multitouch.controller;

import android.util.Log;
import android.view.MotionEvent;
import java.lang.reflect.Method;

public class MultiTouchController<T> {
    private static int ACTION_POINTER_INDEX_SHIFT = 8;
    private static int ACTION_POINTER_UP = 6;
    public static final boolean DEBUG = false;
    private static final long EVENT_SETTLE_TIME_INTERVAL = 20;
    private static final float MAX_MULTITOUCH_DIM_JUMP_SIZE = 40.0f;
    private static final float MAX_MULTITOUCH_POS_JUMP_SIZE = 30.0f;
    public static final int MAX_TOUCH_POINTS = 20;
    private static final float MIN_MULTITOUCH_SEPARATION = 30.0f;
    private static final int MODE_DRAG = 1;
    private static final int MODE_NOTHING = 0;
    private static final int MODE_PINCH = 2;
    private static Method m_getHistoricalPressure;
    private static Method m_getHistoricalX;
    private static Method m_getHistoricalY;
    private static Method m_getPointerCount;
    private static Method m_getPointerId;
    private static Method m_getPressure;
    private static Method m_getX;
    private static Method m_getY;
    public static final boolean multiTouchSupported;
    private static final int[] pointerIds = new int[20];
    private static final float[] pressureVals = new float[20];
    private static final float[] xVals = new float[20];
    private static final float[] yVals = new float[20];
    private boolean handleSingleTouchEvents;
    private PointInfo mCurrPt;
    private float mCurrPtAng;
    private float mCurrPtDiam;
    private float mCurrPtHeight;
    private float mCurrPtWidth;
    private float mCurrPtX;
    private float mCurrPtY;
    private PositionAndScale mCurrXform;
    private int mMode;
    private PointInfo mPrevPt;
    private long mSettleEndTime;
    private long mSettleStartTime;
    MultiTouchObjectCanvas<T> objectCanvas;
    private T selectedObject;
    private float startAngleMinusPinchAngle;
    private float startPosX;
    private float startPosY;
    private float startScaleOverPinchDiam;
    private float startScaleXOverPinchWidth;
    private float startScaleYOverPinchHeight;

    public interface MultiTouchObjectCanvas<T> {
        T getDraggableObjectAtPoint(PointInfo pointInfo);

        void getPositionAndScale(T t, PositionAndScale positionAndScale);

        void selectObject(T t, PointInfo pointInfo);

        boolean setPositionAndScale(T t, PositionAndScale positionAndScale, PointInfo pointInfo);
    }

    private void extractCurrPtInfo() {
        this.mCurrPtX = this.mCurrPt.getX();
        this.mCurrPtY = this.mCurrPt.getY();
        float f = 0.0f;
        this.mCurrPtDiam = Math.max(21.3f, !this.mCurrXform.updateScale ? 0.0f : this.mCurrPt.getMultiTouchDiameter());
        this.mCurrPtWidth = Math.max(30.0f, !this.mCurrXform.updateScaleXY ? 0.0f : this.mCurrPt.getMultiTouchWidth());
        this.mCurrPtHeight = Math.max(30.0f, !this.mCurrXform.updateScaleXY ? 0.0f : this.mCurrPt.getMultiTouchHeight());
        if (this.mCurrXform.updateAngle) {
            f = this.mCurrPt.getMultiTouchAngle();
        }
        this.mCurrPtAng = f;
    }

    public MultiTouchController(MultiTouchObjectCanvas<T> multiTouchObjectCanvas) {
        this(multiTouchObjectCanvas, true);
    }

    public MultiTouchController(MultiTouchObjectCanvas<T> multiTouchObjectCanvas, boolean z) {
        this.selectedObject = null;
        this.mCurrXform = new PositionAndScale();
        this.mMode = 0;
        this.mCurrPt = new PointInfo();
        this.mPrevPt = new PointInfo();
        this.handleSingleTouchEvents = z;
        this.objectCanvas = multiTouchObjectCanvas;
    }

    /* access modifiers changed from: protected */
    public void setHandleSingleTouchEvents(boolean z) {
        this.handleSingleTouchEvents = z;
    }

    /* access modifiers changed from: protected */
    public boolean getHandleSingleTouchEvents() {
        return this.handleSingleTouchEvents;
    }

    static {
        boolean z = true;
        try {
            m_getPointerCount = MotionEvent.class.getMethod("getPointerCount", new Class[0]);
            m_getPointerId = MotionEvent.class.getMethod("getPointerId", new Class[]{Integer.TYPE});
            m_getPressure = MotionEvent.class.getMethod("getPressure", new Class[]{Integer.TYPE});
            m_getHistoricalX = MotionEvent.class.getMethod("getHistoricalX", new Class[]{Integer.TYPE, Integer.TYPE});
            m_getHistoricalY = MotionEvent.class.getMethod("getHistoricalY", new Class[]{Integer.TYPE, Integer.TYPE});
            m_getHistoricalPressure = MotionEvent.class.getMethod("getHistoricalPressure", new Class[]{Integer.TYPE, Integer.TYPE});
            m_getX = MotionEvent.class.getMethod("getX", new Class[]{Integer.TYPE});
            m_getY = MotionEvent.class.getMethod("getY", new Class[]{Integer.TYPE});
        } catch (Exception e) {
            Log.e("MultiTouchController", "static initializer failed", e);
            z = false;
        }
        multiTouchSupported = z;
        if (z) {
            try {
                ACTION_POINTER_UP = MotionEvent.class.getField("ACTION_POINTER_UP").getInt((Object) null);
                ACTION_POINTER_INDEX_SHIFT = MotionEvent.class.getField("ACTION_POINTER_INDEX_SHIFT").getInt((Object) null);
            } catch (Exception unused) {
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:51:0x012a A[Catch:{ Exception -> 0x016b }] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x012c A[Catch:{ Exception -> 0x016b }] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0132 A[Catch:{ Exception -> 0x016b }] */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0146 A[Catch:{ Exception -> 0x016b }] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x014b A[Catch:{ Exception -> 0x016b }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(android.view.MotionEvent r21) {
        /*
            r20 = this;
            r11 = r20
            r0 = r21
            r12 = 0
            boolean r1 = multiTouchSupported     // Catch:{ Exception -> 0x016b }
            r13 = 1
            if (r1 == 0) goto L_0x001a
            java.lang.reflect.Method r1 = m_getPointerCount     // Catch:{ Exception -> 0x016b }
            java.lang.Object[] r2 = new java.lang.Object[r12]     // Catch:{ Exception -> 0x016b }
            java.lang.Object r1 = r1.invoke(r0, r2)     // Catch:{ Exception -> 0x016b }
            java.lang.Integer r1 = (java.lang.Integer) r1     // Catch:{ Exception -> 0x016b }
            int r1 = r1.intValue()     // Catch:{ Exception -> 0x016b }
            r14 = r1
            goto L_0x001b
        L_0x001a:
            r14 = r13
        L_0x001b:
            int r1 = r11.mMode     // Catch:{ Exception -> 0x016b }
            if (r1 != 0) goto L_0x0026
            boolean r1 = r11.handleSingleTouchEvents     // Catch:{ Exception -> 0x016b }
            if (r1 != 0) goto L_0x0026
            if (r14 != r13) goto L_0x0026
            return r12
        L_0x0026:
            int r15 = r21.getAction()     // Catch:{ Exception -> 0x016b }
            int r1 = r21.getHistorySize()     // Catch:{ Exception -> 0x016b }
            int r9 = r1 / r14
            r10 = r12
        L_0x0031:
            if (r10 > r9) goto L_0x016a
            if (r10 >= r9) goto L_0x0037
            r1 = r13
            goto L_0x0038
        L_0x0037:
            r1 = r12
        L_0x0038:
            boolean r2 = multiTouchSupported     // Catch:{ Exception -> 0x016b }
            r3 = 2
            if (r2 == 0) goto L_0x00f3
            if (r14 != r13) goto L_0x0041
            goto L_0x00f3
        L_0x0041:
            r2 = 20
            int r2 = java.lang.Math.min(r14, r2)     // Catch:{ Exception -> 0x016b }
            r4 = r12
        L_0x0048:
            if (r4 >= r2) goto L_0x0120
            java.lang.reflect.Method r5 = m_getPointerId     // Catch:{ Exception -> 0x016b }
            java.lang.Object[] r6 = new java.lang.Object[r13]     // Catch:{ Exception -> 0x016b }
            java.lang.Integer r7 = java.lang.Integer.valueOf(r4)     // Catch:{ Exception -> 0x016b }
            r6[r12] = r7     // Catch:{ Exception -> 0x016b }
            java.lang.Object r5 = r5.invoke(r0, r6)     // Catch:{ Exception -> 0x016b }
            java.lang.Integer r5 = (java.lang.Integer) r5     // Catch:{ Exception -> 0x016b }
            int r5 = r5.intValue()     // Catch:{ Exception -> 0x016b }
            int[] r6 = pointerIds     // Catch:{ Exception -> 0x016b }
            r6[r4] = r5     // Catch:{ Exception -> 0x016b }
            float[] r5 = xVals     // Catch:{ Exception -> 0x016b }
            if (r1 == 0) goto L_0x007b
            java.lang.reflect.Method r6 = m_getHistoricalX     // Catch:{ Exception -> 0x016b }
            java.lang.Object[] r7 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x016b }
            java.lang.Integer r8 = java.lang.Integer.valueOf(r4)     // Catch:{ Exception -> 0x016b }
            r7[r12] = r8     // Catch:{ Exception -> 0x016b }
            java.lang.Integer r8 = java.lang.Integer.valueOf(r10)     // Catch:{ Exception -> 0x016b }
            r7[r13] = r8     // Catch:{ Exception -> 0x016b }
            java.lang.Object r6 = r6.invoke(r0, r7)     // Catch:{ Exception -> 0x016b }
            goto L_0x0089
        L_0x007b:
            java.lang.reflect.Method r6 = m_getX     // Catch:{ Exception -> 0x016b }
            java.lang.Object[] r7 = new java.lang.Object[r13]     // Catch:{ Exception -> 0x016b }
            java.lang.Integer r8 = java.lang.Integer.valueOf(r4)     // Catch:{ Exception -> 0x016b }
            r7[r12] = r8     // Catch:{ Exception -> 0x016b }
            java.lang.Object r6 = r6.invoke(r0, r7)     // Catch:{ Exception -> 0x016b }
        L_0x0089:
            java.lang.Float r6 = (java.lang.Float) r6     // Catch:{ Exception -> 0x016b }
            float r6 = r6.floatValue()     // Catch:{ Exception -> 0x016b }
            r5[r4] = r6     // Catch:{ Exception -> 0x016b }
            float[] r5 = yVals     // Catch:{ Exception -> 0x016b }
            if (r1 == 0) goto L_0x00aa
            java.lang.reflect.Method r6 = m_getHistoricalY     // Catch:{ Exception -> 0x016b }
            java.lang.Object[] r7 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x016b }
            java.lang.Integer r8 = java.lang.Integer.valueOf(r4)     // Catch:{ Exception -> 0x016b }
            r7[r12] = r8     // Catch:{ Exception -> 0x016b }
            java.lang.Integer r8 = java.lang.Integer.valueOf(r10)     // Catch:{ Exception -> 0x016b }
            r7[r13] = r8     // Catch:{ Exception -> 0x016b }
            java.lang.Object r6 = r6.invoke(r0, r7)     // Catch:{ Exception -> 0x016b }
            goto L_0x00b8
        L_0x00aa:
            java.lang.reflect.Method r6 = m_getY     // Catch:{ Exception -> 0x016b }
            java.lang.Object[] r7 = new java.lang.Object[r13]     // Catch:{ Exception -> 0x016b }
            java.lang.Integer r8 = java.lang.Integer.valueOf(r4)     // Catch:{ Exception -> 0x016b }
            r7[r12] = r8     // Catch:{ Exception -> 0x016b }
            java.lang.Object r6 = r6.invoke(r0, r7)     // Catch:{ Exception -> 0x016b }
        L_0x00b8:
            java.lang.Float r6 = (java.lang.Float) r6     // Catch:{ Exception -> 0x016b }
            float r6 = r6.floatValue()     // Catch:{ Exception -> 0x016b }
            r5[r4] = r6     // Catch:{ Exception -> 0x016b }
            float[] r5 = pressureVals     // Catch:{ Exception -> 0x016b }
            if (r1 == 0) goto L_0x00d9
            java.lang.reflect.Method r6 = m_getHistoricalPressure     // Catch:{ Exception -> 0x016b }
            java.lang.Object[] r7 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x016b }
            java.lang.Integer r8 = java.lang.Integer.valueOf(r4)     // Catch:{ Exception -> 0x016b }
            r7[r12] = r8     // Catch:{ Exception -> 0x016b }
            java.lang.Integer r8 = java.lang.Integer.valueOf(r10)     // Catch:{ Exception -> 0x016b }
            r7[r13] = r8     // Catch:{ Exception -> 0x016b }
            java.lang.Object r6 = r6.invoke(r0, r7)     // Catch:{ Exception -> 0x016b }
            goto L_0x00e7
        L_0x00d9:
            java.lang.reflect.Method r6 = m_getPressure     // Catch:{ Exception -> 0x016b }
            java.lang.Object[] r7 = new java.lang.Object[r13]     // Catch:{ Exception -> 0x016b }
            java.lang.Integer r8 = java.lang.Integer.valueOf(r4)     // Catch:{ Exception -> 0x016b }
            r7[r12] = r8     // Catch:{ Exception -> 0x016b }
            java.lang.Object r6 = r6.invoke(r0, r7)     // Catch:{ Exception -> 0x016b }
        L_0x00e7:
            java.lang.Float r6 = (java.lang.Float) r6     // Catch:{ Exception -> 0x016b }
            float r6 = r6.floatValue()     // Catch:{ Exception -> 0x016b }
            r5[r4] = r6     // Catch:{ Exception -> 0x016b }
            int r4 = r4 + 1
            goto L_0x0048
        L_0x00f3:
            float[] r2 = xVals     // Catch:{ Exception -> 0x016b }
            if (r1 == 0) goto L_0x00fc
            float r4 = r0.getHistoricalX(r10)     // Catch:{ Exception -> 0x016b }
            goto L_0x0100
        L_0x00fc:
            float r4 = r21.getX()     // Catch:{ Exception -> 0x016b }
        L_0x0100:
            r2[r12] = r4     // Catch:{ Exception -> 0x016b }
            float[] r2 = yVals     // Catch:{ Exception -> 0x016b }
            if (r1 == 0) goto L_0x010b
            float r4 = r0.getHistoricalY(r10)     // Catch:{ Exception -> 0x016b }
            goto L_0x010f
        L_0x010b:
            float r4 = r21.getY()     // Catch:{ Exception -> 0x016b }
        L_0x010f:
            r2[r12] = r4     // Catch:{ Exception -> 0x016b }
            float[] r2 = pressureVals     // Catch:{ Exception -> 0x016b }
            if (r1 == 0) goto L_0x011a
            float r4 = r0.getHistoricalPressure(r10)     // Catch:{ Exception -> 0x016b }
            goto L_0x011e
        L_0x011a:
            float r4 = r21.getPressure()     // Catch:{ Exception -> 0x016b }
        L_0x011e:
            r2[r12] = r4     // Catch:{ Exception -> 0x016b }
        L_0x0120:
            float[] r4 = xVals     // Catch:{ Exception -> 0x016b }
            float[] r5 = yVals     // Catch:{ Exception -> 0x016b }
            float[] r6 = pressureVals     // Catch:{ Exception -> 0x016b }
            int[] r7 = pointerIds     // Catch:{ Exception -> 0x016b }
            if (r1 == 0) goto L_0x012c
            r8 = r3
            goto L_0x012d
        L_0x012c:
            r8 = r15
        L_0x012d:
            if (r1 == 0) goto L_0x0132
        L_0x012f:
            r16 = r13
            goto L_0x0144
        L_0x0132:
            if (r15 == r13) goto L_0x0142
            int r2 = ACTION_POINTER_INDEX_SHIFT     // Catch:{ Exception -> 0x016b }
            int r2 = r13 << r2
            int r2 = r2 - r13
            r2 = r2 & r15
            int r3 = ACTION_POINTER_UP     // Catch:{ Exception -> 0x016b }
            if (r2 == r3) goto L_0x0142
            r2 = 3
            if (r15 == r2) goto L_0x0142
            goto L_0x012f
        L_0x0142:
            r16 = r12
        L_0x0144:
            if (r1 == 0) goto L_0x014b
            long r1 = r0.getHistoricalEventTime(r10)     // Catch:{ Exception -> 0x016b }
            goto L_0x014f
        L_0x014b:
            long r1 = r21.getEventTime()     // Catch:{ Exception -> 0x016b }
        L_0x014f:
            r17 = r1
            r1 = r20
            r2 = r14
            r3 = r4
            r4 = r5
            r5 = r6
            r6 = r7
            r7 = r8
            r8 = r16
            r16 = r9
            r19 = r10
            r9 = r17
            r1.decodeTouchEvent(r2, r3, r4, r5, r6, r7, r8, r9)     // Catch:{ Exception -> 0x016b }
            int r10 = r19 + 1
            r9 = r16
            goto L_0x0031
        L_0x016a:
            return r13
        L_0x016b:
            r0 = move-exception
            java.lang.String r1 = "MultiTouchController"
            java.lang.String r2 = "onTouchEvent() failed"
            android.util.Log.e(r1, r2, r0)
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: org.metalev.multitouch.controller.MultiTouchController.onTouchEvent(android.view.MotionEvent):boolean");
    }

    private void decodeTouchEvent(int i, float[] fArr, float[] fArr2, float[] fArr3, int[] iArr, int i2, boolean z, long j) {
        PointInfo pointInfo = this.mPrevPt;
        this.mPrevPt = this.mCurrPt;
        this.mCurrPt = pointInfo;
        pointInfo.set(i, fArr, fArr2, fArr3, iArr, i2, z, j);
        multiTouchController();
    }

    private void anchorAtThisPositionAndScale() {
        T t = this.selectedObject;
        if (t != null) {
            this.objectCanvas.getPositionAndScale(t, this.mCurrXform);
            float access$400 = 1.0f / ((this.mCurrXform.updateScale && this.mCurrXform.scale != 0.0f) ? this.mCurrXform.scale : 1.0f);
            extractCurrPtInfo();
            this.startPosX = (this.mCurrPtX - this.mCurrXform.xOff) * access$400;
            this.startPosY = (this.mCurrPtY - this.mCurrXform.yOff) * access$400;
            this.startScaleOverPinchDiam = this.mCurrXform.scale / this.mCurrPtDiam;
            this.startScaleXOverPinchWidth = this.mCurrXform.scaleX / this.mCurrPtWidth;
            this.startScaleYOverPinchHeight = this.mCurrXform.scaleY / this.mCurrPtHeight;
            this.startAngleMinusPinchAngle = this.mCurrXform.angle - this.mCurrPtAng;
        }
    }

    private void performDragOrPinch() {
        if (this.selectedObject != null) {
            float f = 1.0f;
            if (this.mCurrXform.updateScale && this.mCurrXform.scale != 0.0f) {
                f = this.mCurrXform.scale;
            }
            extractCurrPtInfo();
            this.mCurrXform.set(this.mCurrPtX - (this.startPosX * f), this.mCurrPtY - (this.startPosY * f), this.startScaleOverPinchDiam * this.mCurrPtDiam, this.startScaleXOverPinchWidth * this.mCurrPtWidth, this.startScaleYOverPinchHeight * this.mCurrPtHeight, this.startAngleMinusPinchAngle + this.mCurrPtAng);
            this.objectCanvas.setPositionAndScale(this.selectedObject, this.mCurrXform, this.mCurrPt);
        }
    }

    public boolean isPinching() {
        return this.mMode == 2;
    }

    private void multiTouchController() {
        int i = this.mMode;
        if (i != 0) {
            if (i != 1) {
                if (i == 2) {
                    if (!this.mCurrPt.isMultiTouch() || !this.mCurrPt.isDown()) {
                        if (!this.mCurrPt.isDown()) {
                            this.mMode = 0;
                            MultiTouchObjectCanvas<T> multiTouchObjectCanvas = this.objectCanvas;
                            this.selectedObject = null;
                            multiTouchObjectCanvas.selectObject(null, this.mCurrPt);
                            return;
                        }
                        this.mMode = 1;
                        anchorAtThisPositionAndScale();
                        long eventTime = this.mCurrPt.getEventTime();
                        this.mSettleStartTime = eventTime;
                        this.mSettleEndTime = eventTime + EVENT_SETTLE_TIME_INTERVAL;
                    } else if (Math.abs(this.mCurrPt.getX() - this.mPrevPt.getX()) > 30.0f || Math.abs(this.mCurrPt.getY() - this.mPrevPt.getY()) > 30.0f || Math.abs(this.mCurrPt.getMultiTouchWidth() - this.mPrevPt.getMultiTouchWidth()) * 0.5f > MAX_MULTITOUCH_DIM_JUMP_SIZE || Math.abs(this.mCurrPt.getMultiTouchHeight() - this.mPrevPt.getMultiTouchHeight()) * 0.5f > MAX_MULTITOUCH_DIM_JUMP_SIZE) {
                        anchorAtThisPositionAndScale();
                        long eventTime2 = this.mCurrPt.getEventTime();
                        this.mSettleStartTime = eventTime2;
                        this.mSettleEndTime = eventTime2 + EVENT_SETTLE_TIME_INTERVAL;
                    } else if (this.mCurrPt.eventTime < this.mSettleEndTime) {
                        anchorAtThisPositionAndScale();
                    } else {
                        performDragOrPinch();
                    }
                }
            } else if (!this.mCurrPt.isDown()) {
                this.mMode = 0;
                MultiTouchObjectCanvas<T> multiTouchObjectCanvas2 = this.objectCanvas;
                this.selectedObject = null;
                multiTouchObjectCanvas2.selectObject(null, this.mCurrPt);
            } else if (this.mCurrPt.isMultiTouch()) {
                this.mMode = 2;
                anchorAtThisPositionAndScale();
                long eventTime3 = this.mCurrPt.getEventTime();
                this.mSettleStartTime = eventTime3;
                this.mSettleEndTime = eventTime3 + EVENT_SETTLE_TIME_INTERVAL;
            } else if (this.mCurrPt.getEventTime() < this.mSettleEndTime) {
                anchorAtThisPositionAndScale();
            } else {
                performDragOrPinch();
            }
        } else if (this.mCurrPt.isDown()) {
            T draggableObjectAtPoint = this.objectCanvas.getDraggableObjectAtPoint(this.mCurrPt);
            this.selectedObject = draggableObjectAtPoint;
            if (draggableObjectAtPoint != null) {
                this.mMode = 1;
                this.objectCanvas.selectObject(draggableObjectAtPoint, this.mCurrPt);
                anchorAtThisPositionAndScale();
                long eventTime4 = this.mCurrPt.getEventTime();
                this.mSettleEndTime = eventTime4;
                this.mSettleStartTime = eventTime4;
            }
        }
    }

    public int getMode() {
        return this.mMode;
    }

    public static class PointInfo {
        private int action;
        private float angle;
        private boolean angleIsCalculated;
        private float diameter;
        private boolean diameterIsCalculated;
        private float diameterSq;
        private boolean diameterSqIsCalculated;

        /* renamed from: dx */
        private float f405dx;

        /* renamed from: dy */
        private float f406dy;
        /* access modifiers changed from: private */
        public long eventTime;
        private boolean isDown;
        private boolean isMultiTouch;
        private int numPoints;
        private int[] pointerIds = new int[20];
        private float pressureMid;
        private float[] pressures = new float[20];
        private float xMid;

        /* renamed from: xs */
        private float[] f407xs = new float[20];
        private float yMid;

        /* renamed from: ys */
        private float[] f408ys = new float[20];

        private int julery_isqrt(int i) {
            int i2 = 0;
            int i3 = 32768;
            int i4 = 15;
            while (true) {
                int i5 = i4 - 1;
                int i6 = ((i2 << 1) + i3) << i4;
                if (i >= i6) {
                    i2 += i3;
                    i -= i6;
                }
                i3 >>= 1;
                if (i3 <= 0) {
                    return i2;
                }
                i4 = i5;
            }
        }

        /* access modifiers changed from: private */
        public void set(int i, float[] fArr, float[] fArr2, float[] fArr3, int[] iArr, int i2, boolean z, long j) {
            this.eventTime = j;
            this.action = i2;
            this.numPoints = i;
            for (int i3 = 0; i3 < i; i3++) {
                this.f407xs[i3] = fArr[i3];
                this.f408ys[i3] = fArr2[i3];
                this.pressures[i3] = fArr3[i3];
                this.pointerIds[i3] = iArr[i3];
            }
            this.isDown = z;
            boolean z2 = i >= 2;
            this.isMultiTouch = z2;
            if (z2) {
                float f = fArr[0];
                float f2 = fArr[1];
                this.xMid = (f + f2) * 0.5f;
                this.yMid = (fArr2[0] + fArr2[1]) * 0.5f;
                this.pressureMid = (fArr3[0] + fArr3[1]) * 0.5f;
                this.f405dx = Math.abs(f2 - f);
                this.f406dy = Math.abs(fArr2[1] - fArr2[0]);
            } else {
                this.xMid = fArr[0];
                this.yMid = fArr2[0];
                this.pressureMid = fArr3[0];
                this.f406dy = 0.0f;
                this.f405dx = 0.0f;
            }
            this.angleIsCalculated = false;
            this.diameterIsCalculated = false;
            this.diameterSqIsCalculated = false;
        }

        public void set(PointInfo pointInfo) {
            this.numPoints = pointInfo.numPoints;
            for (int i = 0; i < this.numPoints; i++) {
                this.f407xs[i] = pointInfo.f407xs[i];
                this.f408ys[i] = pointInfo.f408ys[i];
                this.pressures[i] = pointInfo.pressures[i];
                this.pointerIds[i] = pointInfo.pointerIds[i];
            }
            this.xMid = pointInfo.xMid;
            this.yMid = pointInfo.yMid;
            this.pressureMid = pointInfo.pressureMid;
            this.f405dx = pointInfo.f405dx;
            this.f406dy = pointInfo.f406dy;
            this.diameter = pointInfo.diameter;
            this.diameterSq = pointInfo.diameterSq;
            this.angle = pointInfo.angle;
            this.isDown = pointInfo.isDown;
            this.action = pointInfo.action;
            this.isMultiTouch = pointInfo.isMultiTouch;
            this.diameterIsCalculated = pointInfo.diameterIsCalculated;
            this.diameterSqIsCalculated = pointInfo.diameterSqIsCalculated;
            this.angleIsCalculated = pointInfo.angleIsCalculated;
            this.eventTime = pointInfo.eventTime;
        }

        public boolean isMultiTouch() {
            return this.isMultiTouch;
        }

        public float getMultiTouchWidth() {
            if (this.isMultiTouch) {
                return this.f405dx;
            }
            return 0.0f;
        }

        public float getMultiTouchHeight() {
            if (this.isMultiTouch) {
                return this.f406dy;
            }
            return 0.0f;
        }

        public float getMultiTouchDiameterSq() {
            float f;
            if (!this.diameterSqIsCalculated) {
                if (this.isMultiTouch) {
                    float f2 = this.f405dx;
                    float f3 = this.f406dy;
                    f = (f2 * f2) + (f3 * f3);
                } else {
                    f = 0.0f;
                }
                this.diameterSq = f;
                this.diameterSqIsCalculated = true;
            }
            return this.diameterSq;
        }

        public float getMultiTouchDiameter() {
            if (!this.diameterIsCalculated) {
                float f = 0.0f;
                if (!this.isMultiTouch) {
                    this.diameter = 0.0f;
                } else {
                    float multiTouchDiameterSq = getMultiTouchDiameterSq();
                    if (multiTouchDiameterSq != 0.0f) {
                        f = ((float) julery_isqrt((int) (multiTouchDiameterSq * 256.0f))) / 16.0f;
                    }
                    this.diameter = f;
                    float f2 = this.f405dx;
                    if (f < f2) {
                        this.diameter = f2;
                    }
                    float f3 = this.diameter;
                    float f4 = this.f406dy;
                    if (f3 < f4) {
                        this.diameter = f4;
                    }
                }
                this.diameterIsCalculated = true;
            }
            return this.diameter;
        }

        public float getMultiTouchAngle() {
            if (!this.angleIsCalculated) {
                if (!this.isMultiTouch) {
                    this.angle = 0.0f;
                } else {
                    float[] fArr = this.f408ys;
                    float[] fArr2 = this.f407xs;
                    this.angle = (float) Math.atan2((double) (fArr[1] - fArr[0]), (double) (fArr2[1] - fArr2[0]));
                }
                this.angleIsCalculated = true;
            }
            return this.angle;
        }

        public int getNumTouchPoints() {
            return this.numPoints;
        }

        public float getX() {
            return this.xMid;
        }

        public float[] getXs() {
            return this.f407xs;
        }

        public float getY() {
            return this.yMid;
        }

        public float[] getYs() {
            return this.f408ys;
        }

        public int[] getPointerIds() {
            return this.pointerIds;
        }

        public float getPressure() {
            return this.pressureMid;
        }

        public float[] getPressures() {
            return this.pressures;
        }

        public boolean isDown() {
            return this.isDown;
        }

        public int getAction() {
            return this.action;
        }

        public long getEventTime() {
            return this.eventTime;
        }
    }

    public static class PositionAndScale {
        /* access modifiers changed from: private */
        public float angle;
        /* access modifiers changed from: private */
        public float scale;
        /* access modifiers changed from: private */
        public float scaleX;
        /* access modifiers changed from: private */
        public float scaleY;
        /* access modifiers changed from: private */
        public boolean updateAngle;
        /* access modifiers changed from: private */
        public boolean updateScale;
        /* access modifiers changed from: private */
        public boolean updateScaleXY;
        /* access modifiers changed from: private */
        public float xOff;
        /* access modifiers changed from: private */
        public float yOff;

        public void set(float f, float f2, boolean z, float f3, boolean z2, float f4, float f5, boolean z3, float f6) {
            this.xOff = f;
            this.yOff = f2;
            this.updateScale = z;
            if (f3 == 0.0f) {
                f3 = 1.0f;
            }
            this.scale = f3;
            this.updateScaleXY = z2;
            if (f4 == 0.0f) {
                f4 = 1.0f;
            }
            this.scaleX = f4;
            if (f5 == 0.0f) {
                f5 = 1.0f;
            }
            this.scaleY = f5;
            this.updateAngle = z3;
            this.angle = f6;
        }

        /* access modifiers changed from: protected */
        public void set(float f, float f2, float f3, float f4, float f5, float f6) {
            this.xOff = f;
            this.yOff = f2;
            if (f3 == 0.0f) {
                f3 = 1.0f;
            }
            this.scale = f3;
            if (f4 == 0.0f) {
                f4 = 1.0f;
            }
            this.scaleX = f4;
            if (f5 == 0.0f) {
                f5 = 1.0f;
            }
            this.scaleY = f5;
            this.angle = f6;
        }

        public float getXOff() {
            return this.xOff;
        }

        public float getYOff() {
            return this.yOff;
        }

        public float getScale() {
            if (!this.updateScale) {
                return 1.0f;
            }
            return this.scale;
        }

        public float getScaleX() {
            if (!this.updateScaleXY) {
                return 1.0f;
            }
            return this.scaleX;
        }

        public float getScaleY() {
            if (!this.updateScaleXY) {
                return 1.0f;
            }
            return this.scaleY;
        }

        public float getAngle() {
            if (!this.updateAngle) {
                return 0.0f;
            }
            return this.angle;
        }
    }
}
