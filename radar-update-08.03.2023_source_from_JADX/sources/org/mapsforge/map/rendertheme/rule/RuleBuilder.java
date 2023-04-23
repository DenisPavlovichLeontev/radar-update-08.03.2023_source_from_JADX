package org.mapsforge.map.rendertheme.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;
import kotlin.jvm.internal.ByteCompanionObject;
import org.mapsforge.map.rendertheme.XmlUtils;
import org.xmlpull.p018v1.XmlPullParser;
import org.xmlpull.p018v1.XmlPullParserException;

public class RuleBuilder {
    private static final String CAT = "cat";
    private static final String CLOSED = "closed";

    /* renamed from: E */
    private static final String f400E = "e";

    /* renamed from: K */
    private static final String f401K = "k";
    private static final Pattern SPLIT_PATTERN = Pattern.compile("\\|");
    private static final String STRING_NEGATION = "~";
    private static final String STRING_WILDCARD = "*";

    /* renamed from: V */
    private static final String f402V = "v";
    private static final String ZOOM_MAX = "zoom-max";
    private static final String ZOOM_MIN = "zoom-min";
    String cat;
    private Closed closed = Closed.ANY;
    ClosedMatcher closedMatcher;
    private Element element;
    ElementMatcher elementMatcher;
    private List<String> keyList;
    private String keys;
    private final Stack<Rule> ruleStack;
    private List<String> valueList;
    private String values;
    byte zoomMax = ByteCompanionObject.MAX_VALUE;
    byte zoomMin = 0;

    private static ClosedMatcher getClosedMatcher(Closed closed2) {
        int i = C13311.$SwitchMap$org$mapsforge$map$rendertheme$rule$Closed[closed2.ordinal()];
        if (i == 1) {
            return ClosedWayMatcher.INSTANCE;
        }
        if (i == 2) {
            return LinearWayMatcher.INSTANCE;
        }
        if (i == 3) {
            return AnyMatcher.INSTANCE;
        }
        throw new IllegalArgumentException("unknown closed value: " + closed2);
    }

    /* renamed from: org.mapsforge.map.rendertheme.rule.RuleBuilder$1 */
    static /* synthetic */ class C13311 {
        static final /* synthetic */ int[] $SwitchMap$org$mapsforge$map$rendertheme$rule$Closed;
        static final /* synthetic */ int[] $SwitchMap$org$mapsforge$map$rendertheme$rule$Element;

        /* JADX WARNING: Can't wrap try/catch for region: R(15:0|(2:1|2)|3|(2:5|6)|7|9|10|11|13|14|15|16|17|18|20) */
        /* JADX WARNING: Can't wrap try/catch for region: R(17:0|1|2|3|5|6|7|9|10|11|13|14|15|16|17|18|20) */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0039 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0043 */
        static {
            /*
                org.mapsforge.map.rendertheme.rule.Element[] r0 = org.mapsforge.map.rendertheme.rule.Element.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$org$mapsforge$map$rendertheme$rule$Element = r0
                r1 = 1
                org.mapsforge.map.rendertheme.rule.Element r2 = org.mapsforge.map.rendertheme.rule.Element.NODE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                r0 = 2
                int[] r2 = $SwitchMap$org$mapsforge$map$rendertheme$rule$Element     // Catch:{ NoSuchFieldError -> 0x001d }
                org.mapsforge.map.rendertheme.rule.Element r3 = org.mapsforge.map.rendertheme.rule.Element.WAY     // Catch:{ NoSuchFieldError -> 0x001d }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                r2 = 3
                int[] r3 = $SwitchMap$org$mapsforge$map$rendertheme$rule$Element     // Catch:{ NoSuchFieldError -> 0x0028 }
                org.mapsforge.map.rendertheme.rule.Element r4 = org.mapsforge.map.rendertheme.rule.Element.ANY     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                org.mapsforge.map.rendertheme.rule.Closed[] r3 = org.mapsforge.map.rendertheme.rule.Closed.values()
                int r3 = r3.length
                int[] r3 = new int[r3]
                $SwitchMap$org$mapsforge$map$rendertheme$rule$Closed = r3
                org.mapsforge.map.rendertheme.rule.Closed r4 = org.mapsforge.map.rendertheme.rule.Closed.YES     // Catch:{ NoSuchFieldError -> 0x0039 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0039 }
                r3[r4] = r1     // Catch:{ NoSuchFieldError -> 0x0039 }
            L_0x0039:
                int[] r1 = $SwitchMap$org$mapsforge$map$rendertheme$rule$Closed     // Catch:{ NoSuchFieldError -> 0x0043 }
                org.mapsforge.map.rendertheme.rule.Closed r3 = org.mapsforge.map.rendertheme.rule.Closed.NO     // Catch:{ NoSuchFieldError -> 0x0043 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0043 }
                r1[r3] = r0     // Catch:{ NoSuchFieldError -> 0x0043 }
            L_0x0043:
                int[] r0 = $SwitchMap$org$mapsforge$map$rendertheme$rule$Closed     // Catch:{ NoSuchFieldError -> 0x004d }
                org.mapsforge.map.rendertheme.rule.Closed r1 = org.mapsforge.map.rendertheme.rule.Closed.ANY     // Catch:{ NoSuchFieldError -> 0x004d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x004d }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x004d }
            L_0x004d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.rendertheme.rule.RuleBuilder.C13311.<clinit>():void");
        }
    }

    private static ElementMatcher getElementMatcher(Element element2) {
        int i = C13311.$SwitchMap$org$mapsforge$map$rendertheme$rule$Element[element2.ordinal()];
        if (i == 1) {
            return ElementNodeMatcher.INSTANCE;
        }
        if (i == 2) {
            return ElementWayMatcher.INSTANCE;
        }
        if (i == 3) {
            return AnyMatcher.INSTANCE;
        }
        throw new IllegalArgumentException("unknown element value: " + element2);
    }

    private static AttributeMatcher getKeyMatcher(List<String> list) {
        if (STRING_WILDCARD.equals(list.get(0))) {
            return AnyMatcher.INSTANCE;
        }
        AttributeMatcher attributeMatcher = Rule.MATCHERS_CACHE_KEY.get(list);
        if (attributeMatcher != null) {
            return attributeMatcher;
        }
        KeyMatcher keyMatcher = new KeyMatcher(list);
        Rule.MATCHERS_CACHE_KEY.put(list, keyMatcher);
        return keyMatcher;
    }

    private static AttributeMatcher getValueMatcher(List<String> list) {
        if (STRING_WILDCARD.equals(list.get(0))) {
            return AnyMatcher.INSTANCE;
        }
        AttributeMatcher attributeMatcher = Rule.MATCHERS_CACHE_VALUE.get(list);
        if (attributeMatcher != null) {
            return attributeMatcher;
        }
        ValueMatcher valueMatcher = new ValueMatcher(list);
        Rule.MATCHERS_CACHE_VALUE.put(list, valueMatcher);
        return valueMatcher;
    }

    public RuleBuilder(String str, XmlPullParser xmlPullParser, Stack<Rule> stack) throws XmlPullParserException {
        this.ruleStack = stack;
        extractValues(str, xmlPullParser);
    }

    public Rule build() {
        if (this.valueList.remove(STRING_NEGATION)) {
            return new NegativeRule(this, new NegativeMatcher(this.keyList, this.valueList));
        }
        return new PositiveRule(this, RuleOptimizer.optimize(getKeyMatcher(this.keyList), this.ruleStack), RuleOptimizer.optimize(getValueMatcher(this.valueList), this.ruleStack));
    }

    private void extractValues(String str, XmlPullParser xmlPullParser) throws XmlPullParserException {
        for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
            String attributeName = xmlPullParser.getAttributeName(i);
            String attributeValue = xmlPullParser.getAttributeValue(i);
            if (f400E.equals(attributeName)) {
                this.element = Element.fromString(attributeValue);
            } else if ("k".equals(attributeName)) {
                this.keys = attributeValue;
            } else if (f402V.equals(attributeName)) {
                this.values = attributeValue;
            } else if (CAT.equals(attributeName)) {
                this.cat = attributeValue;
            } else if (CLOSED.equals(attributeName)) {
                this.closed = Closed.fromString(attributeValue);
            } else if (ZOOM_MIN.equals(attributeName)) {
                this.zoomMin = XmlUtils.parseNonNegativeByte(attributeName, attributeValue);
            } else if (ZOOM_MAX.equals(attributeName)) {
                this.zoomMax = XmlUtils.parseNonNegativeByte(attributeName, attributeValue);
            } else {
                throw XmlUtils.createXmlPullParserException(str, attributeName, attributeValue, i);
            }
        }
        validate(str);
        Pattern pattern = SPLIT_PATTERN;
        this.keyList = new ArrayList(Arrays.asList(pattern.split(this.keys)));
        this.valueList = new ArrayList(Arrays.asList(pattern.split(this.values)));
        this.elementMatcher = getElementMatcher(this.element);
        this.closedMatcher = getClosedMatcher(this.closed);
        this.elementMatcher = RuleOptimizer.optimize(this.elementMatcher, this.ruleStack);
        this.closedMatcher = RuleOptimizer.optimize(this.closedMatcher, this.ruleStack);
    }

    private void validate(String str) throws XmlPullParserException {
        XmlUtils.checkMandatoryAttribute(str, f400E, this.element);
        XmlUtils.checkMandatoryAttribute(str, "k", this.keys);
        XmlUtils.checkMandatoryAttribute(str, f402V, this.values);
        if (this.zoomMin > this.zoomMax) {
            throw new XmlPullParserException("'zoom-min' > 'zoom-max': " + this.zoomMin + ' ' + this.zoomMax);
        }
    }
}
