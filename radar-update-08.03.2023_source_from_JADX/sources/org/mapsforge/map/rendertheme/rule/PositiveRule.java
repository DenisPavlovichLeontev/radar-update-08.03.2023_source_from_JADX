package org.mapsforge.map.rendertheme.rule;

import java.util.List;
import org.mapsforge.core.model.Tag;

class PositiveRule extends Rule {
    final AttributeMatcher keyMatcher;
    final AttributeMatcher valueMatcher;

    PositiveRule(RuleBuilder ruleBuilder, AttributeMatcher attributeMatcher, AttributeMatcher attributeMatcher2) {
        super(ruleBuilder);
        this.keyMatcher = attributeMatcher;
        this.valueMatcher = attributeMatcher2;
    }

    /* access modifiers changed from: package-private */
    public boolean matchesNode(List<Tag> list, byte b) {
        return this.zoomMin <= b && this.zoomMax >= b && this.elementMatcher.matches(Element.NODE) && this.keyMatcher.matches(list) && this.valueMatcher.matches(list);
    }

    /* access modifiers changed from: package-private */
    public boolean matchesWay(List<Tag> list, byte b, Closed closed) {
        return this.zoomMin <= b && this.zoomMax >= b && this.elementMatcher.matches(Element.WAY) && this.closedMatcher.matches(closed) && this.keyMatcher.matches(list) && this.valueMatcher.matches(list);
    }
}
