package org.jsoup.select;

import java.util.Iterator;
import java.util.regex.Pattern;
import org.jsoup.helper.Validate;
import org.jsoup.internal.Normalizer;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.PseudoTextElement;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.osgeo.proj4j.units.AngleFormat;

public abstract class Evaluator {

    public static final class AllElements extends Evaluator {
        public boolean matches(Element element, Element element2) {
            return true;
        }

        public String toString() {
            return "*";
        }
    }

    public abstract boolean matches(Element element, Element element2);

    protected Evaluator() {
    }

    public static final class Tag extends Evaluator {
        private final String tagName;

        public Tag(String str) {
            this.tagName = str;
        }

        public boolean matches(Element element, Element element2) {
            return element2.normalName().equals(this.tagName);
        }

        public String toString() {
            return String.format("%s", new Object[]{this.tagName});
        }
    }

    public static final class TagEndsWith extends Evaluator {
        private final String tagName;

        public TagEndsWith(String str) {
            this.tagName = str;
        }

        public boolean matches(Element element, Element element2) {
            return element2.normalName().endsWith(this.tagName);
        }

        public String toString() {
            return String.format("%s", new Object[]{this.tagName});
        }
    }

    /* renamed from: org.jsoup.select.Evaluator$Id */
    public static final class C1306Id extends Evaluator {

        /* renamed from: id */
        private final String f376id;

        public C1306Id(String str) {
            this.f376id = str;
        }

        public boolean matches(Element element, Element element2) {
            return this.f376id.equals(element2.mo26701id());
        }

        public String toString() {
            return String.format("#%s", new Object[]{this.f376id});
        }
    }

    public static final class Class extends Evaluator {
        private final String className;

        public Class(String str) {
            this.className = str;
        }

        public boolean matches(Element element, Element element2) {
            return element2.hasClass(this.className);
        }

        public String toString() {
            return String.format(".%s", new Object[]{this.className});
        }
    }

    public static final class Attribute extends Evaluator {
        private final String key;

        public Attribute(String str) {
            this.key = str;
        }

        public boolean matches(Element element, Element element2) {
            return element2.hasAttr(this.key);
        }

        public String toString() {
            return String.format("[%s]", new Object[]{this.key});
        }
    }

    public static final class AttributeStarting extends Evaluator {
        private final String keyPrefix;

        public AttributeStarting(String str) {
            Validate.notEmpty(str);
            this.keyPrefix = Normalizer.lowerCase(str);
        }

        public boolean matches(Element element, Element element2) {
            for (org.jsoup.nodes.Attribute key : element2.attributes().asList()) {
                if (Normalizer.lowerCase(key.getKey()).startsWith(this.keyPrefix)) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            return String.format("[^%s]", new Object[]{this.keyPrefix});
        }
    }

    public static final class AttributeWithValue extends AttributeKeyPair {
        public AttributeWithValue(String str, String str2) {
            super(str, str2);
        }

        public boolean matches(Element element, Element element2) {
            return element2.hasAttr(this.key) && this.value.equalsIgnoreCase(element2.attr(this.key).trim());
        }

        public String toString() {
            return String.format("[%s=%s]", new Object[]{this.key, this.value});
        }
    }

    public static final class AttributeWithValueNot extends AttributeKeyPair {
        public AttributeWithValueNot(String str, String str2) {
            super(str, str2);
        }

        public boolean matches(Element element, Element element2) {
            return !this.value.equalsIgnoreCase(element2.attr(this.key));
        }

        public String toString() {
            return String.format("[%s!=%s]", new Object[]{this.key, this.value});
        }
    }

    public static final class AttributeWithValueStarting extends AttributeKeyPair {
        public AttributeWithValueStarting(String str, String str2) {
            super(str, str2, false);
        }

        public boolean matches(Element element, Element element2) {
            return element2.hasAttr(this.key) && Normalizer.lowerCase(element2.attr(this.key)).startsWith(this.value);
        }

        public String toString() {
            return String.format("[%s^=%s]", new Object[]{this.key, this.value});
        }
    }

    public static final class AttributeWithValueEnding extends AttributeKeyPair {
        public AttributeWithValueEnding(String str, String str2) {
            super(str, str2, false);
        }

        public boolean matches(Element element, Element element2) {
            return element2.hasAttr(this.key) && Normalizer.lowerCase(element2.attr(this.key)).endsWith(this.value);
        }

        public String toString() {
            return String.format("[%s$=%s]", new Object[]{this.key, this.value});
        }
    }

    public static final class AttributeWithValueContaining extends AttributeKeyPair {
        public AttributeWithValueContaining(String str, String str2) {
            super(str, str2);
        }

        public boolean matches(Element element, Element element2) {
            return element2.hasAttr(this.key) && Normalizer.lowerCase(element2.attr(this.key)).contains(this.value);
        }

        public String toString() {
            return String.format("[%s*=%s]", new Object[]{this.key, this.value});
        }
    }

    public static final class AttributeWithValueMatching extends Evaluator {
        String key;
        Pattern pattern;

        public AttributeWithValueMatching(String str, Pattern pattern2) {
            this.key = Normalizer.normalize(str);
            this.pattern = pattern2;
        }

        public boolean matches(Element element, Element element2) {
            return element2.hasAttr(this.key) && this.pattern.matcher(element2.attr(this.key)).find();
        }

        public String toString() {
            return String.format("[%s~=%s]", new Object[]{this.key, this.pattern.toString()});
        }
    }

    public static abstract class AttributeKeyPair extends Evaluator {
        String key;
        String value;

        public AttributeKeyPair(String str, String str2) {
            this(str, str2, true);
        }

        public AttributeKeyPair(String str, String str2, boolean z) {
            Validate.notEmpty(str);
            Validate.notEmpty(str2);
            this.key = Normalizer.normalize(str);
            boolean z2 = (str2.startsWith("'") && str2.endsWith("'")) || (str2.startsWith(AngleFormat.STR_SEC_SYMBOL) && str2.endsWith(AngleFormat.STR_SEC_SYMBOL));
            str2 = z2 ? str2.substring(1, str2.length() - 1) : str2;
            this.value = z ? Normalizer.normalize(str2) : Normalizer.normalize(str2, z2);
        }
    }

    public static final class IndexLessThan extends IndexEvaluator {
        public IndexLessThan(int i) {
            super(i);
        }

        public boolean matches(Element element, Element element2) {
            return element != element2 && element2.elementSiblingIndex() < this.index;
        }

        public String toString() {
            return String.format(":lt(%d)", new Object[]{Integer.valueOf(this.index)});
        }
    }

    public static final class IndexGreaterThan extends IndexEvaluator {
        public IndexGreaterThan(int i) {
            super(i);
        }

        public boolean matches(Element element, Element element2) {
            return element2.elementSiblingIndex() > this.index;
        }

        public String toString() {
            return String.format(":gt(%d)", new Object[]{Integer.valueOf(this.index)});
        }
    }

    public static final class IndexEquals extends IndexEvaluator {
        public IndexEquals(int i) {
            super(i);
        }

        public boolean matches(Element element, Element element2) {
            return element2.elementSiblingIndex() == this.index;
        }

        public String toString() {
            return String.format(":eq(%d)", new Object[]{Integer.valueOf(this.index)});
        }
    }

    public static final class IsLastChild extends Evaluator {
        public String toString() {
            return ":last-child";
        }

        public boolean matches(Element element, Element element2) {
            Element parent = element2.parent();
            if (parent == null || (parent instanceof Document) || element2.elementSiblingIndex() != parent.children().size() - 1) {
                return false;
            }
            return true;
        }
    }

    public static final class IsFirstOfType extends IsNthOfType {
        public String toString() {
            return ":first-of-type";
        }

        public IsFirstOfType() {
            super(0, 1);
        }
    }

    public static final class IsLastOfType extends IsNthLastOfType {
        public String toString() {
            return ":last-of-type";
        }

        public IsLastOfType() {
            super(0, 1);
        }
    }

    public static abstract class CssNthEvaluator extends Evaluator {

        /* renamed from: a */
        protected final int f374a;

        /* renamed from: b */
        protected final int f375b;

        /* access modifiers changed from: protected */
        public abstract int calculatePosition(Element element, Element element2);

        /* access modifiers changed from: protected */
        public abstract String getPseudoClass();

        public CssNthEvaluator(int i, int i2) {
            this.f374a = i;
            this.f375b = i2;
        }

        public CssNthEvaluator(int i) {
            this(0, i);
        }

        public boolean matches(Element element, Element element2) {
            Element parent = element2.parent();
            if (parent == null || (parent instanceof Document)) {
                return false;
            }
            int calculatePosition = calculatePosition(element, element2);
            int i = this.f374a;
            if (i != 0) {
                int i2 = this.f375b;
                if ((calculatePosition - i2) * i < 0 || (calculatePosition - i2) % i != 0) {
                    return false;
                }
                return true;
            } else if (calculatePosition == this.f375b) {
                return true;
            } else {
                return false;
            }
        }

        public String toString() {
            if (this.f374a == 0) {
                return String.format(":%s(%d)", new Object[]{getPseudoClass(), Integer.valueOf(this.f375b)});
            } else if (this.f375b == 0) {
                return String.format(":%s(%dn)", new Object[]{getPseudoClass(), Integer.valueOf(this.f374a)});
            } else {
                return String.format(":%s(%dn%+d)", new Object[]{getPseudoClass(), Integer.valueOf(this.f374a), Integer.valueOf(this.f375b)});
            }
        }
    }

    public static final class IsNthChild extends CssNthEvaluator {
        /* access modifiers changed from: protected */
        public String getPseudoClass() {
            return "nth-child";
        }

        public IsNthChild(int i, int i2) {
            super(i, i2);
        }

        /* access modifiers changed from: protected */
        public int calculatePosition(Element element, Element element2) {
            return element2.elementSiblingIndex() + 1;
        }
    }

    public static final class IsNthLastChild extends CssNthEvaluator {
        /* access modifiers changed from: protected */
        public String getPseudoClass() {
            return "nth-last-child";
        }

        public IsNthLastChild(int i, int i2) {
            super(i, i2);
        }

        /* access modifiers changed from: protected */
        public int calculatePosition(Element element, Element element2) {
            if (element2.parent() == null) {
                return 0;
            }
            return element2.parent().children().size() - element2.elementSiblingIndex();
        }
    }

    public static class IsNthOfType extends CssNthEvaluator {
        /* access modifiers changed from: protected */
        public String getPseudoClass() {
            return "nth-of-type";
        }

        public IsNthOfType(int i, int i2) {
            super(i, i2);
        }

        /* access modifiers changed from: protected */
        public int calculatePosition(Element element, Element element2) {
            int i = 0;
            if (element2.parent() == null) {
                return 0;
            }
            Iterator it = element2.parent().children().iterator();
            while (it.hasNext()) {
                Element element3 = (Element) it.next();
                if (element3.tag().equals(element2.tag())) {
                    i++;
                    continue;
                }
                if (element3 == element2) {
                    break;
                }
            }
            return i;
        }
    }

    public static class IsNthLastOfType extends CssNthEvaluator {
        /* access modifiers changed from: protected */
        public String getPseudoClass() {
            return "nth-last-of-type";
        }

        public IsNthLastOfType(int i, int i2) {
            super(i, i2);
        }

        /* access modifiers changed from: protected */
        public int calculatePosition(Element element, Element element2) {
            int i = 0;
            if (element2.parent() == null) {
                return 0;
            }
            Elements children = element2.parent().children();
            for (int elementSiblingIndex = element2.elementSiblingIndex(); elementSiblingIndex < children.size(); elementSiblingIndex++) {
                if (((Element) children.get(elementSiblingIndex)).tag().equals(element2.tag())) {
                    i++;
                }
            }
            return i;
        }
    }

    public static final class IsFirstChild extends Evaluator {
        public String toString() {
            return ":first-child";
        }

        public boolean matches(Element element, Element element2) {
            Element parent = element2.parent();
            return parent != null && !(parent instanceof Document) && element2.elementSiblingIndex() == 0;
        }
    }

    public static final class IsRoot extends Evaluator {
        public String toString() {
            return ":root";
        }

        public boolean matches(Element element, Element element2) {
            if (element instanceof Document) {
                element = element.child(0);
            }
            return element2 == element;
        }
    }

    public static final class IsOnlyChild extends Evaluator {
        public String toString() {
            return ":only-child";
        }

        public boolean matches(Element element, Element element2) {
            Element parent = element2.parent();
            return parent != null && !(parent instanceof Document) && element2.siblingElements().isEmpty();
        }
    }

    public static final class IsOnlyOfType extends Evaluator {
        public String toString() {
            return ":only-of-type";
        }

        public boolean matches(Element element, Element element2) {
            Element parent = element2.parent();
            if (parent == null || (parent instanceof Document)) {
                return false;
            }
            Iterator it = parent.children().iterator();
            int i = 0;
            while (it.hasNext()) {
                if (((Element) it.next()).tag().equals(element2.tag())) {
                    i++;
                }
            }
            if (i == 1) {
                return true;
            }
            return false;
        }
    }

    public static final class IsEmpty extends Evaluator {
        public String toString() {
            return ":empty";
        }

        public boolean matches(Element element, Element element2) {
            for (Node next : element2.childNodes()) {
                if (!(next instanceof Comment) && !(next instanceof XmlDeclaration) && !(next instanceof DocumentType)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static abstract class IndexEvaluator extends Evaluator {
        int index;

        public IndexEvaluator(int i) {
            this.index = i;
        }
    }

    public static final class ContainsText extends Evaluator {
        private final String searchText;

        public ContainsText(String str) {
            this.searchText = Normalizer.lowerCase(StringUtil.normaliseWhitespace(str));
        }

        public boolean matches(Element element, Element element2) {
            return Normalizer.lowerCase(element2.text()).contains(this.searchText);
        }

        public String toString() {
            return String.format(":contains(%s)", new Object[]{this.searchText});
        }
    }

    public static final class ContainsWholeText extends Evaluator {
        private final String searchText;

        public ContainsWholeText(String str) {
            this.searchText = str;
        }

        public boolean matches(Element element, Element element2) {
            return element2.wholeText().contains(this.searchText);
        }

        public String toString() {
            return String.format(":containsWholeText(%s)", new Object[]{this.searchText});
        }
    }

    public static final class ContainsWholeOwnText extends Evaluator {
        private final String searchText;

        public ContainsWholeOwnText(String str) {
            this.searchText = str;
        }

        public boolean matches(Element element, Element element2) {
            return element2.wholeOwnText().contains(this.searchText);
        }

        public String toString() {
            return String.format(":containsWholeOwnText(%s)", new Object[]{this.searchText});
        }
    }

    public static final class ContainsData extends Evaluator {
        private final String searchText;

        public ContainsData(String str) {
            this.searchText = Normalizer.lowerCase(str);
        }

        public boolean matches(Element element, Element element2) {
            return Normalizer.lowerCase(element2.data()).contains(this.searchText);
        }

        public String toString() {
            return String.format(":containsData(%s)", new Object[]{this.searchText});
        }
    }

    public static final class ContainsOwnText extends Evaluator {
        private final String searchText;

        public ContainsOwnText(String str) {
            this.searchText = Normalizer.lowerCase(StringUtil.normaliseWhitespace(str));
        }

        public boolean matches(Element element, Element element2) {
            return Normalizer.lowerCase(element2.ownText()).contains(this.searchText);
        }

        public String toString() {
            return String.format(":containsOwn(%s)", new Object[]{this.searchText});
        }
    }

    public static final class Matches extends Evaluator {
        private final Pattern pattern;

        public Matches(Pattern pattern2) {
            this.pattern = pattern2;
        }

        public boolean matches(Element element, Element element2) {
            return this.pattern.matcher(element2.text()).find();
        }

        public String toString() {
            return String.format(":matches(%s)", new Object[]{this.pattern});
        }
    }

    public static final class MatchesOwn extends Evaluator {
        private final Pattern pattern;

        public MatchesOwn(Pattern pattern2) {
            this.pattern = pattern2;
        }

        public boolean matches(Element element, Element element2) {
            return this.pattern.matcher(element2.ownText()).find();
        }

        public String toString() {
            return String.format(":matchesOwn(%s)", new Object[]{this.pattern});
        }
    }

    public static final class MatchesWholeText extends Evaluator {
        private final Pattern pattern;

        public MatchesWholeText(Pattern pattern2) {
            this.pattern = pattern2;
        }

        public boolean matches(Element element, Element element2) {
            return this.pattern.matcher(element2.wholeText()).find();
        }

        public String toString() {
            return String.format(":matchesWholeText(%s)", new Object[]{this.pattern});
        }
    }

    public static final class MatchesWholeOwnText extends Evaluator {
        private final Pattern pattern;

        public MatchesWholeOwnText(Pattern pattern2) {
            this.pattern = pattern2;
        }

        public boolean matches(Element element, Element element2) {
            return this.pattern.matcher(element2.wholeOwnText()).find();
        }

        public String toString() {
            return String.format(":matchesWholeOwnText(%s)", new Object[]{this.pattern});
        }
    }

    public static final class MatchText extends Evaluator {
        public String toString() {
            return ":matchText";
        }

        public boolean matches(Element element, Element element2) {
            if (element2 instanceof PseudoTextElement) {
                return true;
            }
            for (TextNode next : element2.textNodes()) {
                PseudoTextElement pseudoTextElement = new PseudoTextElement(org.jsoup.parser.Tag.valueOf(element2.tagName()), element2.baseUri(), element2.attributes());
                next.replaceWith(pseudoTextElement);
                pseudoTextElement.appendChild(next);
            }
            return false;
        }
    }
}
