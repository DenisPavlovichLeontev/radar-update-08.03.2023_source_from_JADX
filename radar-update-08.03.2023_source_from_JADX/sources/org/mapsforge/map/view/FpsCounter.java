package org.mapsforge.map.view;

import java.util.concurrent.TimeUnit;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.FontFamily;
import org.mapsforge.core.graphics.FontStyle;
import org.mapsforge.core.graphics.GraphicContext;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.map.model.DisplayModel;

public class FpsCounter {
    private static final long ONE_SECOND = TimeUnit.SECONDS.toNanos(1);
    private final DisplayModel displayModel;
    private String fps;
    private int frameCounter;
    private long lastTime;
    private final Paint paintBack;
    private final Paint paintFront;
    private boolean visible;

    private static Paint createPaintFront(GraphicFactory graphicFactory, DisplayModel displayModel2) {
        Paint createPaint = graphicFactory.createPaint();
        createPaint.setColor(Color.RED);
        createPaint.setTypeface(FontFamily.DEFAULT, FontStyle.BOLD);
        createPaint.setTextSize(displayModel2.getScaleFactor() * 25.0f);
        return createPaint;
    }

    private static Paint createPaintBack(GraphicFactory graphicFactory, DisplayModel displayModel2) {
        Paint createPaint = graphicFactory.createPaint();
        createPaint.setColor(Color.WHITE);
        createPaint.setTypeface(FontFamily.DEFAULT, FontStyle.BOLD);
        createPaint.setTextSize(displayModel2.getScaleFactor() * 25.0f);
        createPaint.setStrokeWidth(displayModel2.getScaleFactor() * 2.0f);
        createPaint.setStyle(Style.STROKE);
        return createPaint;
    }

    public FpsCounter(GraphicFactory graphicFactory, DisplayModel displayModel2) {
        this.displayModel = displayModel2;
        this.paintBack = createPaintBack(graphicFactory, displayModel2);
        this.paintFront = createPaintFront(graphicFactory, displayModel2);
    }

    public FpsCounter(DisplayModel displayModel2, Paint paint, Paint paint2) {
        this.displayModel = displayModel2;
        this.paintBack = paint;
        this.paintFront = paint2;
    }

    public void draw(GraphicContext graphicContext) {
        if (this.visible) {
            long nanoTime = System.nanoTime();
            long j = nanoTime - this.lastTime;
            long j2 = ONE_SECOND;
            if (j > j2) {
                this.fps = String.valueOf(Math.round(((float) (((long) this.frameCounter) * j2)) / ((float) j)));
                this.lastTime = nanoTime;
                this.frameCounter = 0;
            }
            int scaleFactor = (int) (this.displayModel.getScaleFactor() * 20.0f);
            int scaleFactor2 = (int) (this.displayModel.getScaleFactor() * 40.0f);
            graphicContext.drawText(this.fps, scaleFactor, scaleFactor2, this.paintBack);
            graphicContext.drawText(this.fps, scaleFactor, scaleFactor2, this.paintFront);
            this.frameCounter++;
        }
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean z) {
        this.visible = z;
    }
}
