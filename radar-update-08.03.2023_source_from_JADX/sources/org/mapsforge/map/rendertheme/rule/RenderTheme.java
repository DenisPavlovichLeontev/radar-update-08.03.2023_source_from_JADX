package org.mapsforge.map.rendertheme.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.mapsforge.core.util.LRUCache;
import org.mapsforge.map.datastore.PointOfInterest;
import org.mapsforge.map.layer.renderer.PolylineContainer;
import org.mapsforge.map.layer.renderer.StandardRenderer;
import org.mapsforge.map.rendertheme.RenderCallback;
import org.mapsforge.map.rendertheme.RenderContext;
import org.mapsforge.map.rendertheme.renderinstruction.Hillshading;
import org.mapsforge.map.rendertheme.renderinstruction.RenderInstruction;
import org.mapsforge.map.rendertheme.rule.Rule;

public class RenderTheme {
    private static final int MATCHING_CACHE_SIZE = 1024;
    private final float baseStrokeWidth;
    private final float baseTextSize;
    private final boolean hasBackgroundOutside;
    private ArrayList<Hillshading> hillShadings = new ArrayList<>();
    private int levels;
    private final int mapBackground;
    private final int mapBackgroundOutside;
    private final LRUCache<MatchingCacheKey, List<RenderInstruction>> poiMatchingCache;
    private final ArrayList<Rule> rulesList;
    private final Map<Byte, Float> strokeScales = new HashMap();
    private final Map<Byte, Float> textScales = new HashMap();
    private final LRUCache<MatchingCacheKey, List<RenderInstruction>> wayMatchingCache;

    RenderTheme(RenderThemeBuilder renderThemeBuilder) {
        this.baseStrokeWidth = renderThemeBuilder.baseStrokeWidth;
        this.baseTextSize = renderThemeBuilder.baseTextSize;
        this.hasBackgroundOutside = renderThemeBuilder.hasBackgroundOutside;
        this.mapBackground = renderThemeBuilder.mapBackground;
        this.mapBackgroundOutside = renderThemeBuilder.mapBackgroundOutside;
        this.rulesList = new ArrayList<>();
        this.poiMatchingCache = new LRUCache<>(1024);
        this.wayMatchingCache = new LRUCache<>(1024);
    }

    public void destroy() {
        this.poiMatchingCache.clear();
        this.wayMatchingCache.clear();
        Iterator<Rule> it = this.rulesList.iterator();
        while (it.hasNext()) {
            it.next().destroy();
        }
    }

    public int getLevels() {
        return this.levels;
    }

    public int getMapBackground() {
        return this.mapBackground;
    }

    public int getMapBackgroundOutside() {
        return this.mapBackgroundOutside;
    }

    public boolean hasMapBackgroundOutside() {
        return this.hasBackgroundOutside;
    }

    public void matchClosedWay(RenderCallback renderCallback, RenderContext renderContext, PolylineContainer polylineContainer) {
        matchWay(renderCallback, renderContext, Closed.YES, polylineContainer);
    }

    public void matchLinearWay(RenderCallback renderCallback, RenderContext renderContext, PolylineContainer polylineContainer) {
        matchWay(renderCallback, renderContext, Closed.NO, polylineContainer);
    }

    public synchronized void matchNode(RenderCallback renderCallback, RenderContext renderContext, PointOfInterest pointOfInterest) {
        MatchingCacheKey matchingCacheKey = new MatchingCacheKey(pointOfInterest.tags, renderContext.rendererJob.tile.zoomLevel, Closed.NO);
        List list = (List) this.poiMatchingCache.get(matchingCacheKey);
        int i = 0;
        if (list != null) {
            int size = list.size();
            while (i < size) {
                ((RenderInstruction) list.get(i)).renderNode(renderCallback, renderContext, pointOfInterest);
                i++;
            }
            return;
        }
        ArrayList arrayList = new ArrayList();
        int size2 = this.rulesList.size();
        while (i < size2) {
            this.rulesList.get(i).matchNode(renderCallback, renderContext, arrayList, pointOfInterest);
            i++;
        }
        this.poiMatchingCache.put(matchingCacheKey, arrayList);
    }

    public synchronized void scaleStrokeWidth(float f, byte b) {
        if (!this.strokeScales.containsKey(Byte.valueOf(b)) || f != this.strokeScales.get(Byte.valueOf(b)).floatValue()) {
            int size = this.rulesList.size();
            for (int i = 0; i < size; i++) {
                Rule rule = this.rulesList.get(i);
                if (rule.zoomMin <= b && rule.zoomMax >= b) {
                    rule.scaleStrokeWidth(this.baseStrokeWidth * f, b);
                }
            }
            this.strokeScales.put(Byte.valueOf(b), Float.valueOf(f));
        }
    }

    public synchronized void scaleTextSize(float f, byte b) {
        if (!this.textScales.containsKey(Byte.valueOf(b)) || f != this.textScales.get(Byte.valueOf(b)).floatValue()) {
            int size = this.rulesList.size();
            for (int i = 0; i < size; i++) {
                Rule rule = this.rulesList.get(i);
                if (rule.zoomMin <= b && rule.zoomMax >= b) {
                    rule.scaleTextSize(this.baseTextSize * f, b);
                }
            }
            this.textScales.put(Byte.valueOf(b), Float.valueOf(f));
        }
    }

    /* access modifiers changed from: package-private */
    public void addRule(Rule rule) {
        this.rulesList.add(rule);
    }

    /* access modifiers changed from: package-private */
    public void addHillShadings(Hillshading hillshading) {
        this.hillShadings.add(hillshading);
    }

    /* access modifiers changed from: package-private */
    public void complete() {
        this.rulesList.trimToSize();
        this.hillShadings.trimToSize();
        int size = this.rulesList.size();
        for (int i = 0; i < size; i++) {
            this.rulesList.get(i).onComplete();
        }
    }

    /* access modifiers changed from: package-private */
    public void setLevels(int i) {
        this.levels = i;
    }

    private synchronized void matchWay(RenderCallback renderCallback, RenderContext renderContext, Closed closed, PolylineContainer polylineContainer) {
        MatchingCacheKey matchingCacheKey = new MatchingCacheKey(polylineContainer.getTags(), polylineContainer.getUpperLeft().zoomLevel, closed);
        List list = (List) this.wayMatchingCache.get(matchingCacheKey);
        if (list != null) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                ((RenderInstruction) list.get(i)).renderWay(renderCallback, renderContext, polylineContainer);
            }
            return;
        }
        ArrayList arrayList = new ArrayList();
        int size2 = this.rulesList.size();
        for (int i2 = 0; i2 < size2; i2++) {
            this.rulesList.get(i2).matchWay(renderCallback, polylineContainer, polylineContainer.getUpperLeft(), closed, arrayList, renderContext);
        }
        this.wayMatchingCache.put(matchingCacheKey, arrayList);
    }

    public void traverseRules(Rule.RuleVisitor ruleVisitor) {
        Iterator<Rule> it = this.rulesList.iterator();
        while (it.hasNext()) {
            it.next().apply(ruleVisitor);
        }
    }

    public void matchHillShadings(StandardRenderer standardRenderer, RenderContext renderContext) {
        Iterator<Hillshading> it = this.hillShadings.iterator();
        while (it.hasNext()) {
            it.next().render(renderContext, standardRenderer.hillsRenderConfig);
        }
    }
}
