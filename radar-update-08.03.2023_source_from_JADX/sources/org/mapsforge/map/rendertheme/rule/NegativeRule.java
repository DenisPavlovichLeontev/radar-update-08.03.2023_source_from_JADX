package org.mapsforge.map.rendertheme.rule;

import java.util.List;
import org.mapsforge.core.model.Tag;

class NegativeRule extends Rule {
    private final AttributeMatcher attributeMatcher;

    NegativeRule(RuleBuilder ruleBuilder, AttributeMatcher attributeMatcher2) {
        super(ruleBuilder);
        this.attributeMatcher = attributeMatcher2;
    }

    /* access modifiers changed from: package-private */
    public boolean matchesNode(List<Tag> list, byte b) {
        return this.zoomMin <= b && this.zoomMax >= b && this.elementMatcher.matches(Element.NODE) && this.attributeMatcher.matches(list);
    }

    /* access modifiers changed from: package-private */
    public boolean matchesWay(List<Tag> list, byte b, Closed closed) {
        return this.zoomMin <= b && this.zoomMax >= b && this.elementMatcher.matches(Element.WAY) && this.closedMatcher.matches(closed) && this.attributeMatcher.matches(list);
    }
}
