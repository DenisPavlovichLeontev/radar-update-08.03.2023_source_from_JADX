package org.mapsforge.map.rendertheme.rule;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.xmlpull.p018v1.XmlPullParserException;

public class RenderThemeFuture extends FutureTask<RenderTheme> {
    private final AtomicInteger refCount = new AtomicInteger(1);

    private static class RenderThemeCallable implements Callable<RenderTheme> {
        private final DisplayModel displayModel;
        private final GraphicFactory graphicFactory;
        private final XmlRenderTheme xmlRenderTheme;

        public RenderThemeCallable(GraphicFactory graphicFactory2, XmlRenderTheme xmlRenderTheme2, DisplayModel displayModel2) {
            this.graphicFactory = graphicFactory2;
            this.xmlRenderTheme = xmlRenderTheme2;
            this.displayModel = displayModel2;
        }

        public RenderTheme call() {
            DisplayModel displayModel2;
            XmlRenderTheme xmlRenderTheme2 = this.xmlRenderTheme;
            if (xmlRenderTheme2 == null || (displayModel2 = this.displayModel) == null) {
                return null;
            }
            try {
                return RenderThemeHandler.getRenderTheme(this.graphicFactory, displayModel2, xmlRenderTheme2);
            } catch (XmlPullParserException e) {
                throw new IllegalArgumentException("Parse error for XML rendertheme", e);
            } catch (IOException e2) {
                throw new IllegalArgumentException("File error for XML rendertheme", e2);
            }
        }
    }

    public RenderThemeFuture(GraphicFactory graphicFactory, XmlRenderTheme xmlRenderTheme, DisplayModel displayModel) {
        super(new RenderThemeCallable(graphicFactory, xmlRenderTheme, displayModel));
    }

    public void decrementRefCount() {
        if (this.refCount.decrementAndGet() <= 0) {
            try {
                if (isDone()) {
                    ((RenderTheme) get()).destroy();
                } else {
                    cancel(true);
                }
            } catch (Exception unused) {
            }
        }
    }

    public void incrementRefCount() {
        this.refCount.incrementAndGet();
    }
}
