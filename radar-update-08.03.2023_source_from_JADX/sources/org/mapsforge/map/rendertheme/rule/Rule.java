package org.mapsforge.map.rendertheme.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.mapsforge.core.model.Tag;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.datastore.PointOfInterest;
import org.mapsforge.map.layer.renderer.PolylineContainer;
import org.mapsforge.map.rendertheme.RenderCallback;
import org.mapsforge.map.rendertheme.RenderContext;
import org.mapsforge.map.rendertheme.renderinstruction.RenderInstruction;

public abstract class Rule {
    static final Map<List<String>, AttributeMatcher> MATCHERS_CACHE_KEY = new HashMap();
    static final Map<List<String>, AttributeMatcher> MATCHERS_CACHE_VALUE = new HashMap();
    String cat;
    final ClosedMatcher closedMatcher;
    final ElementMatcher elementMatcher;
    public final ArrayList<RenderInstruction> renderInstructions = new ArrayList<>(4);
    public final ArrayList<Rule> subRules = new ArrayList<>(4);
    final byte zoomMax;
    final byte zoomMin;

    /* access modifiers changed from: package-private */
    public abstract boolean matchesNode(List<Tag> list, byte b);

    /* access modifiers changed from: package-private */
    public abstract boolean matchesWay(List<Tag> list, byte b, Closed closed);

    public static class RuleVisitor {
        public void apply(Rule rule) {
            Iterator<Rule> it = rule.subRules.iterator();
            while (it.hasNext()) {
                apply(it.next());
            }
        }
    }

    Rule(RuleBuilder ruleBuilder) {
        this.cat = ruleBuilder.cat;
        this.closedMatcher = ruleBuilder.closedMatcher;
        this.elementMatcher = ruleBuilder.elementMatcher;
        this.zoomMax = ruleBuilder.zoomMax;
        this.zoomMin = ruleBuilder.zoomMin;
    }

    /* access modifiers changed from: package-private */
    public void addRenderingInstruction(RenderInstruction renderInstruction) {
        this.renderInstructions.add(renderInstruction);
    }

    /* access modifiers changed from: package-private */
    public void addSubRule(Rule rule) {
        this.subRules.add(rule);
    }

    /* access modifiers changed from: package-private */
    public void apply(RuleVisitor ruleVisitor) {
        ruleVisitor.apply(this);
    }

    /* access modifiers changed from: package-private */
    public void destroy() {
        Iterator<RenderInstruction> it = this.renderInstructions.iterator();
        while (it.hasNext()) {
            it.next().destroy();
        }
        Iterator<Rule> it2 = this.subRules.iterator();
        while (it2.hasNext()) {
            it2.next().destroy();
        }
    }

    /* access modifiers changed from: package-private */
    public void matchNode(RenderCallback renderCallback, RenderContext renderContext, List<RenderInstruction> list, PointOfInterest pointOfInterest) {
        if (matchesNode(pointOfInterest.tags, renderContext.rendererJob.tile.zoomLevel)) {
            int size = this.renderInstructions.size();
            for (int i = 0; i < size; i++) {
                this.renderInstructions.get(i).renderNode(renderCallback, renderContext, pointOfInterest);
                list.add(this.renderInstructions.get(i));
            }
            int size2 = this.subRules.size();
            for (int i2 = 0; i2 < size2; i2++) {
                this.subRules.get(i2).matchNode(renderCallback, renderContext, list, pointOfInterest);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void matchWay(RenderCallback renderCallback, PolylineContainer polylineContainer, Tile tile, Closed closed, List<RenderInstruction> list, RenderContext renderContext) {
        if (matchesWay(polylineContainer.getTags(), tile.zoomLevel, closed)) {
            int size = this.renderInstructions.size();
            for (int i = 0; i < size; i++) {
                this.renderInstructions.get(i).renderWay(renderCallback, renderContext, polylineContainer);
                list.add(this.renderInstructions.get(i));
            }
            RenderCallback renderCallback2 = renderCallback;
            PolylineContainer polylineContainer2 = polylineContainer;
            List<RenderInstruction> list2 = list;
            RenderContext renderContext2 = renderContext;
            int size2 = this.subRules.size();
            for (int i2 = 0; i2 < size2; i2++) {
                this.subRules.get(i2).matchWay(renderCallback, polylineContainer, tile, closed, list, renderContext);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void onComplete() {
        MATCHERS_CACHE_KEY.clear();
        MATCHERS_CACHE_VALUE.clear();
        this.renderInstructions.trimToSize();
        this.subRules.trimToSize();
        int size = this.subRules.size();
        for (int i = 0; i < size; i++) {
            this.subRules.get(i).onComplete();
        }
    }

    /* access modifiers changed from: package-private */
    public void scaleStrokeWidth(float f, byte b) {
        int size = this.renderInstructions.size();
        for (int i = 0; i < size; i++) {
            this.renderInstructions.get(i).scaleStrokeWidth(f, b);
        }
        int size2 = this.subRules.size();
        for (int i2 = 0; i2 < size2; i2++) {
            this.subRules.get(i2).scaleStrokeWidth(f, b);
        }
    }

    /* access modifiers changed from: package-private */
    public void scaleTextSize(float f, byte b) {
        int size = this.renderInstructions.size();
        for (int i = 0; i < size; i++) {
            this.renderInstructions.get(i).scaleTextSize(f, b);
        }
        int size2 = this.subRules.size();
        for (int i2 = 0; i2 < size2; i2++) {
            this.subRules.get(i2).scaleTextSize(f, b);
        }
    }
}
