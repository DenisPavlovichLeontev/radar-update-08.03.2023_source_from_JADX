package org.jsoup.nodes;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.annotation.Nullable;
import kotlin.text.Typography;
import mil.nga.geopackage.schema.constraints.DataColumnConstraints;
import org.jsoup.helper.ChangeNotifyingArrayList;
import org.jsoup.helper.Consumer;
import org.jsoup.helper.Validate;
import org.jsoup.internal.Normalizer;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.select.Collector;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.jsoup.select.NodeFilter;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Selector;

public class Element extends Node {
    private static final String BaseUriKey = Attributes.internalKey("baseUri");
    private static final Pattern ClassSplit = Pattern.compile("\\s+");
    private static final List<Element> EmptyChildren = Collections.emptyList();
    @Nullable
    Attributes attributes;
    List<Node> childNodes;
    @Nullable
    private WeakReference<List<Element>> shadowChildrenRef;
    /* access modifiers changed from: private */
    public Tag tag;

    public Element(String str) {
        this(Tag.valueOf(str), "", (Attributes) null);
    }

    public Element(Tag tag2, @Nullable String str, @Nullable Attributes attributes2) {
        Validate.notNull(tag2);
        this.childNodes = EmptyNodes;
        this.attributes = attributes2;
        this.tag = tag2;
        if (str != null) {
            setBaseUri(str);
        }
    }

    public Element(Tag tag2, @Nullable String str) {
        this(tag2, str, (Attributes) null);
    }

    /* access modifiers changed from: protected */
    public boolean hasChildNodes() {
        return this.childNodes != EmptyNodes;
    }

    /* access modifiers changed from: protected */
    public List<Node> ensureChildNodes() {
        if (this.childNodes == EmptyNodes) {
            this.childNodes = new NodeList(this, 4);
        }
        return this.childNodes;
    }

    /* access modifiers changed from: protected */
    public boolean hasAttributes() {
        return this.attributes != null;
    }

    public Attributes attributes() {
        if (this.attributes == null) {
            this.attributes = new Attributes();
        }
        return this.attributes;
    }

    public String baseUri() {
        return searchUpForAttribute(this, BaseUriKey);
    }

    private static String searchUpForAttribute(Element element, String str) {
        while (element != null) {
            Attributes attributes2 = element.attributes;
            if (attributes2 != null && attributes2.hasKey(str)) {
                return element.attributes.get(str);
            }
            element = element.parent();
        }
        return "";
    }

    /* access modifiers changed from: protected */
    public void doSetBaseUri(String str) {
        attributes().put(BaseUriKey, str);
    }

    public int childNodeSize() {
        return this.childNodes.size();
    }

    public String nodeName() {
        return this.tag.getName();
    }

    public String tagName() {
        return this.tag.getName();
    }

    public String normalName() {
        return this.tag.normalName();
    }

    public Element tagName(String str) {
        Validate.notEmptyParam(str, "tagName");
        this.tag = Tag.valueOf(str, NodeUtils.parser(this).settings());
        return this;
    }

    public Tag tag() {
        return this.tag;
    }

    public boolean isBlock() {
        return this.tag.isBlock();
    }

    /* renamed from: id */
    public String mo26701id() {
        Attributes attributes2 = this.attributes;
        return attributes2 != null ? attributes2.getIgnoreCase("id") : "";
    }

    /* renamed from: id */
    public Element mo26702id(String str) {
        Validate.notNull(str);
        attr("id", str);
        return this;
    }

    public Element attr(String str, String str2) {
        super.attr(str, str2);
        return this;
    }

    public Element attr(String str, boolean z) {
        attributes().put(str, z);
        return this;
    }

    public Map<String, String> dataset() {
        return attributes().dataset();
    }

    @Nullable
    public final Element parent() {
        return (Element) this.parentNode;
    }

    public Elements parents() {
        Elements elements = new Elements();
        accumulateParents(this, elements);
        return elements;
    }

    private static void accumulateParents(Element element, Elements elements) {
        Element parent = element.parent();
        if (parent != null && !parent.tagName().equals("#root")) {
            elements.add(parent);
            accumulateParents(parent, elements);
        }
    }

    public Element child(int i) {
        return childElementsList().get(i);
    }

    public int childrenSize() {
        return childElementsList().size();
    }

    public Elements children() {
        return new Elements(childElementsList());
    }

    /* access modifiers changed from: package-private */
    public List<Element> childElementsList() {
        List<Element> list;
        if (childNodeSize() == 0) {
            return EmptyChildren;
        }
        WeakReference<List<Element>> weakReference = this.shadowChildrenRef;
        if (weakReference != null && (list = (List) weakReference.get()) != null) {
            return list;
        }
        int size = this.childNodes.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            Node node = this.childNodes.get(i);
            if (node instanceof Element) {
                arrayList.add((Element) node);
            }
        }
        this.shadowChildrenRef = new WeakReference<>(arrayList);
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public void nodelistChanged() {
        super.nodelistChanged();
        this.shadowChildrenRef = null;
    }

    public List<TextNode> textNodes() {
        ArrayList arrayList = new ArrayList();
        for (Node next : this.childNodes) {
            if (next instanceof TextNode) {
                arrayList.add((TextNode) next);
            }
        }
        return Collections.unmodifiableList(arrayList);
    }

    public List<DataNode> dataNodes() {
        ArrayList arrayList = new ArrayList();
        for (Node next : this.childNodes) {
            if (next instanceof DataNode) {
                arrayList.add((DataNode) next);
            }
        }
        return Collections.unmodifiableList(arrayList);
    }

    public Elements select(String str) {
        return Selector.select(str, this);
    }

    public Elements select(Evaluator evaluator) {
        return Selector.select(evaluator, this);
    }

    @Nullable
    public Element selectFirst(String str) {
        return Selector.selectFirst(str, this);
    }

    @Nullable
    public Element selectFirst(Evaluator evaluator) {
        return Collector.findFirst(evaluator, this);
    }

    public Element expectFirst(String str) {
        return (Element) Validate.ensureNotNull(Selector.selectFirst(str, this), parent() != null ? "No elements matched the query '%s' on element '%s'." : "No elements matched the query '%s' in the document.", str, tagName());
    }

    /* renamed from: is */
    public boolean mo26705is(String str) {
        return mo26706is(QueryParser.parse(str));
    }

    /* renamed from: is */
    public boolean mo26706is(Evaluator evaluator) {
        return evaluator.matches(root(), this);
    }

    @Nullable
    public Element closest(String str) {
        return closest(QueryParser.parse(str));
    }

    @Nullable
    public Element closest(Evaluator evaluator) {
        Validate.notNull(evaluator);
        Element root = root();
        Element element = this;
        while (!evaluator.matches(root, element)) {
            element = element.parent();
            if (element == null) {
                return null;
            }
        }
        return element;
    }

    public Elements selectXpath(String str) {
        return new Elements(NodeUtils.selectXpath(str, this, Element.class));
    }

    public <T extends Node> List<T> selectXpath(String str, Class<T> cls) {
        return NodeUtils.selectXpath(str, this, cls);
    }

    public Element appendChild(Node node) {
        Validate.notNull(node);
        reparentChild(node);
        ensureChildNodes();
        this.childNodes.add(node);
        node.setSiblingIndex(this.childNodes.size() - 1);
        return this;
    }

    public Element appendChildren(Collection<? extends Node> collection) {
        insertChildren(-1, collection);
        return this;
    }

    public Element appendTo(Element element) {
        Validate.notNull(element);
        element.appendChild(this);
        return this;
    }

    public Element prependChild(Node node) {
        Validate.notNull(node);
        addChildren(0, node);
        return this;
    }

    public Element prependChildren(Collection<? extends Node> collection) {
        insertChildren(0, collection);
        return this;
    }

    public Element insertChildren(int i, Collection<? extends Node> collection) {
        Validate.notNull(collection, "Children collection to be inserted must not be null.");
        int childNodeSize = childNodeSize();
        if (i < 0) {
            i += childNodeSize + 1;
        }
        Validate.isTrue(i >= 0 && i <= childNodeSize, "Insert position out of bounds.");
        addChildren(i, (Node[]) new ArrayList(collection).toArray(new Node[0]));
        return this;
    }

    public Element insertChildren(int i, Node... nodeArr) {
        Validate.notNull(nodeArr, "Children collection to be inserted must not be null.");
        int childNodeSize = childNodeSize();
        if (i < 0) {
            i += childNodeSize + 1;
        }
        Validate.isTrue(i >= 0 && i <= childNodeSize, "Insert position out of bounds.");
        addChildren(i, nodeArr);
        return this;
    }

    public Element appendElement(String str) {
        Element element = new Element(Tag.valueOf(str, NodeUtils.parser(this).settings()), baseUri());
        appendChild(element);
        return element;
    }

    public Element prependElement(String str) {
        Element element = new Element(Tag.valueOf(str, NodeUtils.parser(this).settings()), baseUri());
        prependChild(element);
        return element;
    }

    public Element appendText(String str) {
        Validate.notNull(str);
        appendChild(new TextNode(str));
        return this;
    }

    public Element prependText(String str) {
        Validate.notNull(str);
        prependChild(new TextNode(str));
        return this;
    }

    public Element append(String str) {
        Validate.notNull(str);
        addChildren((Node[]) NodeUtils.parser(this).parseFragmentInput(str, this, baseUri()).toArray(new Node[0]));
        return this;
    }

    public Element prepend(String str) {
        Validate.notNull(str);
        addChildren(0, (Node[]) NodeUtils.parser(this).parseFragmentInput(str, this, baseUri()).toArray(new Node[0]));
        return this;
    }

    public Element before(String str) {
        return (Element) super.before(str);
    }

    public Element before(Node node) {
        return (Element) super.before(node);
    }

    public Element after(String str) {
        return (Element) super.after(str);
    }

    public Element after(Node node) {
        return (Element) super.after(node);
    }

    public Element empty() {
        this.childNodes.clear();
        return this;
    }

    public Element wrap(String str) {
        return (Element) super.wrap(str);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0035, code lost:
        if (r3.get(0) == r5) goto L_0x0037;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String cssSelector() {
        /*
            r5 = this;
            java.lang.String r0 = r5.mo26701id()
            int r0 = r0.length()
            r1 = 0
            r2 = 1
            if (r0 <= 0) goto L_0x0038
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = "#"
            r0.append(r3)
            java.lang.String r3 = r5.mo26701id()
            r0.append(r3)
            java.lang.String r0 = r0.toString()
            org.jsoup.nodes.Document r3 = r5.ownerDocument()
            if (r3 == 0) goto L_0x0037
            org.jsoup.select.Elements r3 = r3.select((java.lang.String) r0)
            int r4 = r3.size()
            if (r4 != r2) goto L_0x0038
            java.lang.Object r3 = r3.get(r1)
            if (r3 != r5) goto L_0x0038
        L_0x0037:
            return r0
        L_0x0038:
            java.lang.String r0 = r5.tagName()
            r3 = 58
            r4 = 124(0x7c, float:1.74E-43)
            java.lang.String r0 = r0.replace(r3, r4)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>(r0)
            java.util.Set r0 = r5.classNames()
            java.lang.String r4 = "."
            java.lang.String r0 = org.jsoup.internal.StringUtil.join((java.util.Collection<?>) r0, (java.lang.String) r4)
            int r4 = r0.length()
            if (r4 <= 0) goto L_0x0061
            r4 = 46
            r3.append(r4)
            r3.append(r0)
        L_0x0061:
            org.jsoup.nodes.Element r0 = r5.parent()
            if (r0 == 0) goto L_0x00b9
            org.jsoup.nodes.Element r0 = r5.parent()
            boolean r0 = r0 instanceof org.jsoup.nodes.Document
            if (r0 == 0) goto L_0x0070
            goto L_0x00b9
        L_0x0070:
            java.lang.String r0 = " > "
            r3.insert(r1, r0)
            org.jsoup.nodes.Element r0 = r5.parent()
            java.lang.String r4 = r3.toString()
            org.jsoup.select.Elements r0 = r0.select((java.lang.String) r4)
            int r0 = r0.size()
            if (r0 <= r2) goto L_0x009d
            java.lang.Object[] r0 = new java.lang.Object[r2]
            int r4 = r5.elementSiblingIndex()
            int r4 = r4 + r2
            java.lang.Integer r2 = java.lang.Integer.valueOf(r4)
            r0[r1] = r2
            java.lang.String r1 = ":nth-child(%d)"
            java.lang.String r0 = java.lang.String.format(r1, r0)
            r3.append(r0)
        L_0x009d:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            org.jsoup.nodes.Element r1 = r5.parent()
            java.lang.String r1 = r1.cssSelector()
            r0.append(r1)
            java.lang.String r1 = r3.toString()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            return r0
        L_0x00b9:
            java.lang.String r0 = r3.toString()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jsoup.nodes.Element.cssSelector():java.lang.String");
    }

    public Elements siblingElements() {
        if (this.parentNode == null) {
            return new Elements(0);
        }
        List<Element> childElementsList = parent().childElementsList();
        Elements elements = new Elements(childElementsList.size() - 1);
        for (Element next : childElementsList) {
            if (next != this) {
                elements.add(next);
            }
        }
        return elements;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0006, code lost:
        r0 = parent().childElementsList();
     */
    @javax.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.jsoup.nodes.Element nextElementSibling() {
        /*
            r4 = this;
            org.jsoup.nodes.Node r0 = r4.parentNode
            r1 = 0
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            org.jsoup.nodes.Element r0 = r4.parent()
            java.util.List r0 = r0.childElementsList()
            int r2 = indexInList(r4, r0)
            int r3 = r0.size()
            int r2 = r2 + 1
            if (r3 <= r2) goto L_0x0021
            java.lang.Object r0 = r0.get(r2)
            org.jsoup.nodes.Element r0 = (org.jsoup.nodes.Element) r0
            return r0
        L_0x0021:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jsoup.nodes.Element.nextElementSibling():org.jsoup.nodes.Element");
    }

    public Elements nextElementSiblings() {
        return nextElementSiblings(true);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0006, code lost:
        r0 = parent().childElementsList();
     */
    @javax.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.jsoup.nodes.Element previousElementSibling() {
        /*
            r3 = this;
            org.jsoup.nodes.Node r0 = r3.parentNode
            r1 = 0
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            org.jsoup.nodes.Element r0 = r3.parent()
            java.util.List r0 = r0.childElementsList()
            int r2 = indexInList(r3, r0)
            if (r2 <= 0) goto L_0x001d
            int r2 = r2 + -1
            java.lang.Object r0 = r0.get(r2)
            org.jsoup.nodes.Element r0 = (org.jsoup.nodes.Element) r0
            return r0
        L_0x001d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jsoup.nodes.Element.previousElementSibling():org.jsoup.nodes.Element");
    }

    public Elements previousElementSiblings() {
        return nextElementSiblings(false);
    }

    private Elements nextElementSiblings(boolean z) {
        Elements elements = new Elements();
        if (this.parentNode == null) {
            return elements;
        }
        elements.add(this);
        return z ? elements.nextAll() : elements.prevAll();
    }

    public Element firstElementSibling() {
        if (parent() == null) {
            return this;
        }
        List<Element> childElementsList = parent().childElementsList();
        return childElementsList.size() > 1 ? childElementsList.get(0) : this;
    }

    public int elementSiblingIndex() {
        if (parent() == null) {
            return 0;
        }
        return indexInList(this, parent().childElementsList());
    }

    public Element lastElementSibling() {
        if (parent() == null) {
            return this;
        }
        List<Element> childElementsList = parent().childElementsList();
        return childElementsList.size() > 1 ? childElementsList.get(childElementsList.size() - 1) : this;
    }

    private static <E extends Element> int indexInList(Element element, List<E> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (list.get(i) == element) {
                return i;
            }
        }
        return 0;
    }

    @Nullable
    public Element firstElementChild() {
        int childNodeSize = childNodeSize();
        if (childNodeSize == 0) {
            return null;
        }
        List<Node> ensureChildNodes = ensureChildNodes();
        for (int i = 0; i < childNodeSize; i++) {
            Node node = ensureChildNodes.get(i);
            if (node instanceof Element) {
                return (Element) node;
            }
        }
        return null;
    }

    @Nullable
    public Element lastElementChild() {
        int childNodeSize = childNodeSize();
        if (childNodeSize == 0) {
            return null;
        }
        List<Node> ensureChildNodes = ensureChildNodes();
        for (int i = childNodeSize - 1; i >= 0; i--) {
            Node node = ensureChildNodes.get(i);
            if (node instanceof Element) {
                return (Element) node;
            }
        }
        return null;
    }

    public Elements getElementsByTag(String str) {
        Validate.notEmpty(str);
        return Collector.collect(new Evaluator.Tag(Normalizer.normalize(str)), this);
    }

    @Nullable
    public Element getElementById(String str) {
        Validate.notEmpty(str);
        Elements collect = Collector.collect(new Evaluator.C1306Id(str), this);
        if (collect.size() > 0) {
            return (Element) collect.get(0);
        }
        return null;
    }

    public Elements getElementsByClass(String str) {
        Validate.notEmpty(str);
        return Collector.collect(new Evaluator.Class(str), this);
    }

    public Elements getElementsByAttribute(String str) {
        Validate.notEmpty(str);
        return Collector.collect(new Evaluator.Attribute(str.trim()), this);
    }

    public Elements getElementsByAttributeStarting(String str) {
        Validate.notEmpty(str);
        return Collector.collect(new Evaluator.AttributeStarting(str.trim()), this);
    }

    public Elements getElementsByAttributeValue(String str, String str2) {
        return Collector.collect(new Evaluator.AttributeWithValue(str, str2), this);
    }

    public Elements getElementsByAttributeValueNot(String str, String str2) {
        return Collector.collect(new Evaluator.AttributeWithValueNot(str, str2), this);
    }

    public Elements getElementsByAttributeValueStarting(String str, String str2) {
        return Collector.collect(new Evaluator.AttributeWithValueStarting(str, str2), this);
    }

    public Elements getElementsByAttributeValueEnding(String str, String str2) {
        return Collector.collect(new Evaluator.AttributeWithValueEnding(str, str2), this);
    }

    public Elements getElementsByAttributeValueContaining(String str, String str2) {
        return Collector.collect(new Evaluator.AttributeWithValueContaining(str, str2), this);
    }

    public Elements getElementsByAttributeValueMatching(String str, Pattern pattern) {
        return Collector.collect(new Evaluator.AttributeWithValueMatching(str, pattern), this);
    }

    public Elements getElementsByAttributeValueMatching(String str, String str2) {
        try {
            return getElementsByAttributeValueMatching(str, Pattern.compile(str2));
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("Pattern syntax error: " + str2, e);
        }
    }

    public Elements getElementsByIndexLessThan(int i) {
        return Collector.collect(new Evaluator.IndexLessThan(i), this);
    }

    public Elements getElementsByIndexGreaterThan(int i) {
        return Collector.collect(new Evaluator.IndexGreaterThan(i), this);
    }

    public Elements getElementsByIndexEquals(int i) {
        return Collector.collect(new Evaluator.IndexEquals(i), this);
    }

    public Elements getElementsContainingText(String str) {
        return Collector.collect(new Evaluator.ContainsText(str), this);
    }

    public Elements getElementsContainingOwnText(String str) {
        return Collector.collect(new Evaluator.ContainsOwnText(str), this);
    }

    public Elements getElementsMatchingText(Pattern pattern) {
        return Collector.collect(new Evaluator.Matches(pattern), this);
    }

    public Elements getElementsMatchingText(String str) {
        try {
            return getElementsMatchingText(Pattern.compile(str));
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("Pattern syntax error: " + str, e);
        }
    }

    public Elements getElementsMatchingOwnText(Pattern pattern) {
        return Collector.collect(new Evaluator.MatchesOwn(pattern), this);
    }

    public Elements getElementsMatchingOwnText(String str) {
        try {
            return getElementsMatchingOwnText(Pattern.compile(str));
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("Pattern syntax error: " + str, e);
        }
    }

    public Elements getAllElements() {
        return Collector.collect(new Evaluator.AllElements(), this);
    }

    public String text() {
        final StringBuilder borrowBuilder = StringUtil.borrowBuilder();
        NodeTraversor.traverse((NodeVisitor) new NodeVisitor() {
            public void head(Node node, int i) {
                if (node instanceof TextNode) {
                    Element.appendNormalisedText(borrowBuilder, (TextNode) node);
                } else if (node instanceof Element) {
                    Element element = (Element) node;
                    if (borrowBuilder.length() <= 0) {
                        return;
                    }
                    if ((element.isBlock() || element.tag.normalName().equals("br")) && !TextNode.lastCharIsWhitespace(borrowBuilder)) {
                        borrowBuilder.append(' ');
                    }
                }
            }

            public void tail(Node node, int i) {
                if ((node instanceof Element) && ((Element) node).isBlock() && (node.nextSibling() instanceof TextNode) && !TextNode.lastCharIsWhitespace(borrowBuilder)) {
                    borrowBuilder.append(' ');
                }
            }
        }, (Node) this);
        return StringUtil.releaseBuilder(borrowBuilder).trim();
    }

    public String wholeText() {
        StringBuilder borrowBuilder = StringUtil.borrowBuilder();
        NodeTraversor.traverse((NodeVisitor) new Element$$ExternalSyntheticLambda0(borrowBuilder), (Node) this);
        return StringUtil.releaseBuilder(borrowBuilder);
    }

    /* access modifiers changed from: private */
    public static void appendWholeText(Node node, StringBuilder sb) {
        if (node instanceof TextNode) {
            sb.append(((TextNode) node).getWholeText());
        } else if (node instanceof Element) {
            appendNewlineIfBr((Element) node, sb);
        }
    }

    public String wholeOwnText() {
        StringBuilder borrowBuilder = StringUtil.borrowBuilder();
        int childNodeSize = childNodeSize();
        for (int i = 0; i < childNodeSize; i++) {
            appendWholeText(this.childNodes.get(i), borrowBuilder);
        }
        return StringUtil.releaseBuilder(borrowBuilder);
    }

    public String ownText() {
        StringBuilder borrowBuilder = StringUtil.borrowBuilder();
        ownText(borrowBuilder);
        return StringUtil.releaseBuilder(borrowBuilder).trim();
    }

    private void ownText(StringBuilder sb) {
        for (int i = 0; i < childNodeSize(); i++) {
            Node node = this.childNodes.get(i);
            if (node instanceof TextNode) {
                appendNormalisedText(sb, (TextNode) node);
            } else if (node instanceof Element) {
                appendWhitespaceIfBr((Element) node, sb);
            }
        }
    }

    /* access modifiers changed from: private */
    public static void appendNormalisedText(StringBuilder sb, TextNode textNode) {
        String wholeText = textNode.getWholeText();
        if (preserveWhitespace(textNode.parentNode) || (textNode instanceof CDataNode)) {
            sb.append(wholeText);
        } else {
            StringUtil.appendNormalisedWhitespace(sb, wholeText, TextNode.lastCharIsWhitespace(sb));
        }
    }

    private static void appendWhitespaceIfBr(Element element, StringBuilder sb) {
        if (element.tag.normalName().equals("br") && !TextNode.lastCharIsWhitespace(sb)) {
            sb.append(" ");
        }
    }

    private static void appendNewlineIfBr(Element element, StringBuilder sb) {
        if (element.tag.normalName().equals("br")) {
            sb.append("\n");
        }
    }

    static boolean preserveWhitespace(@Nullable Node node) {
        if (node instanceof Element) {
            Element element = (Element) node;
            int i = 0;
            while (!element.tag.preserveWhitespace()) {
                element = element.parent();
                i++;
                if (i < 6) {
                    if (element == null) {
                    }
                }
            }
            return true;
        }
        return false;
    }

    public Element text(String str) {
        Validate.notNull(str);
        empty();
        Document ownerDocument = ownerDocument();
        if (ownerDocument == null || !ownerDocument.parser().isContentForTagData(normalName())) {
            appendChild(new TextNode(str));
        } else {
            appendChild(new DataNode(str));
        }
        return this;
    }

    public boolean hasText() {
        for (Node next : this.childNodes) {
            if (next instanceof TextNode) {
                if (!((TextNode) next).isBlank()) {
                    return true;
                }
            } else if ((next instanceof Element) && ((Element) next).hasText()) {
                return true;
            }
        }
        return false;
    }

    public String data() {
        StringBuilder borrowBuilder = StringUtil.borrowBuilder();
        for (Node next : this.childNodes) {
            if (next instanceof DataNode) {
                borrowBuilder.append(((DataNode) next).getWholeData());
            } else if (next instanceof Comment) {
                borrowBuilder.append(((Comment) next).getData());
            } else if (next instanceof Element) {
                borrowBuilder.append(((Element) next).data());
            } else if (next instanceof CDataNode) {
                borrowBuilder.append(((CDataNode) next).getWholeText());
            }
        }
        return StringUtil.releaseBuilder(borrowBuilder);
    }

    public String className() {
        return attr("class").trim();
    }

    public Set<String> classNames() {
        LinkedHashSet linkedHashSet = new LinkedHashSet(Arrays.asList(ClassSplit.split(className())));
        linkedHashSet.remove("");
        return linkedHashSet;
    }

    public Element classNames(Set<String> set) {
        Validate.notNull(set);
        if (set.isEmpty()) {
            attributes().remove("class");
        } else {
            attributes().put("class", StringUtil.join((Collection<?>) set, " "));
        }
        return this;
    }

    public boolean hasClass(String str) {
        Attributes attributes2 = this.attributes;
        if (attributes2 == null) {
            return false;
        }
        String ignoreCase = attributes2.getIgnoreCase("class");
        int length = ignoreCase.length();
        int length2 = str.length();
        if (length != 0 && length >= length2) {
            if (length == length2) {
                return str.equalsIgnoreCase(ignoreCase);
            }
            boolean z = false;
            int i = 0;
            for (int i2 = 0; i2 < length; i2++) {
                if (Character.isWhitespace(ignoreCase.charAt(i2))) {
                    if (!z) {
                        continue;
                    } else if (i2 - i == length2 && ignoreCase.regionMatches(true, i, str, 0, length2)) {
                        return true;
                    } else {
                        z = false;
                    }
                } else if (!z) {
                    i = i2;
                    z = true;
                }
            }
            if (z && length - i == length2) {
                return ignoreCase.regionMatches(true, i, str, 0, length2);
            }
        }
        return false;
    }

    public Element addClass(String str) {
        Validate.notNull(str);
        Set<String> classNames = classNames();
        classNames.add(str);
        classNames(classNames);
        return this;
    }

    public Element removeClass(String str) {
        Validate.notNull(str);
        Set<String> classNames = classNames();
        classNames.remove(str);
        classNames(classNames);
        return this;
    }

    public Element toggleClass(String str) {
        Validate.notNull(str);
        Set<String> classNames = classNames();
        if (classNames.contains(str)) {
            classNames.remove(str);
        } else {
            classNames.add(str);
        }
        classNames(classNames);
        return this;
    }

    public String val() {
        if (normalName().equals("textarea")) {
            return text();
        }
        return attr(DataColumnConstraints.COLUMN_VALUE);
    }

    public Element val(String str) {
        if (normalName().equals("textarea")) {
            text(str);
        } else {
            attr(DataColumnConstraints.COLUMN_VALUE, str);
        }
        return this;
    }

    public Range endSourceRange() {
        return Range.m151of(this, false);
    }

    /* access modifiers changed from: package-private */
    public boolean shouldIndent(Document.OutputSettings outputSettings) {
        return outputSettings.prettyPrint() && isFormatAsBlock(outputSettings) && !isInlineable(outputSettings);
    }

    /* access modifiers changed from: package-private */
    public void outerHtmlHead(Appendable appendable, int i, Document.OutputSettings outputSettings) throws IOException {
        if (shouldIndent(outputSettings)) {
            if (!(appendable instanceof StringBuilder)) {
                indent(appendable, i, outputSettings);
            } else if (((StringBuilder) appendable).length() > 0) {
                indent(appendable, i, outputSettings);
            }
        }
        appendable.append(Typography.less).append(tagName());
        Attributes attributes2 = this.attributes;
        if (attributes2 != null) {
            attributes2.html(appendable, outputSettings);
        }
        if (!this.childNodes.isEmpty() || !this.tag.isSelfClosing()) {
            appendable.append(Typography.greater);
        } else if (outputSettings.syntax() != Document.OutputSettings.Syntax.html || !this.tag.isEmpty()) {
            appendable.append(" />");
        } else {
            appendable.append(Typography.greater);
        }
    }

    /* access modifiers changed from: package-private */
    public void outerHtmlTail(Appendable appendable, int i, Document.OutputSettings outputSettings) throws IOException {
        if (!this.childNodes.isEmpty() || !this.tag.isSelfClosing()) {
            if (outputSettings.prettyPrint() && !this.childNodes.isEmpty() && (this.tag.formatAsBlock() || (outputSettings.outline() && (this.childNodes.size() > 1 || (this.childNodes.size() == 1 && (this.childNodes.get(0) instanceof Element)))))) {
                indent(appendable, i, outputSettings);
            }
            appendable.append("</").append(tagName()).append(Typography.greater);
        }
    }

    public String html() {
        StringBuilder borrowBuilder = StringUtil.borrowBuilder();
        html(borrowBuilder);
        String releaseBuilder = StringUtil.releaseBuilder(borrowBuilder);
        return NodeUtils.outputSettings(this).prettyPrint() ? releaseBuilder.trim() : releaseBuilder;
    }

    public <T extends Appendable> T html(T t) {
        int size = this.childNodes.size();
        for (int i = 0; i < size; i++) {
            this.childNodes.get(i).outerHtml(t);
        }
        return t;
    }

    public Element html(String str) {
        empty();
        append(str);
        return this;
    }

    public Element clone() {
        return (Element) super.clone();
    }

    public Element shallowClone() {
        Tag tag2 = this.tag;
        String baseUri = baseUri();
        Attributes attributes2 = this.attributes;
        return new Element(tag2, baseUri, attributes2 == null ? null : attributes2.clone());
    }

    /* access modifiers changed from: protected */
    public Element doClone(@Nullable Node node) {
        Element element = (Element) super.doClone(node);
        Attributes attributes2 = this.attributes;
        element.attributes = attributes2 != null ? attributes2.clone() : null;
        NodeList nodeList = new NodeList(element, this.childNodes.size());
        element.childNodes = nodeList;
        nodeList.addAll(this.childNodes);
        return element;
    }

    public Element clearAttributes() {
        if (this.attributes != null) {
            super.clearAttributes();
            this.attributes = null;
        }
        return this;
    }

    public Element removeAttr(String str) {
        return (Element) super.removeAttr(str);
    }

    public Element root() {
        return (Element) super.root();
    }

    public Element traverse(NodeVisitor nodeVisitor) {
        return (Element) super.traverse(nodeVisitor);
    }

    public Element forEachNode(Consumer<? super Node> consumer) {
        return (Element) super.forEachNode(consumer);
    }

    public Element forEach(Consumer<? super Element> consumer) {
        Validate.notNull(consumer);
        NodeTraversor.traverse((NodeVisitor) new Element$$ExternalSyntheticLambda1(consumer), (Node) this);
        return this;
    }

    static /* synthetic */ void lambda$forEach$1(Consumer consumer, Node node, int i) {
        if (node instanceof Element) {
            consumer.accept((Element) node);
        }
    }

    public Element filter(NodeFilter nodeFilter) {
        return (Element) super.filter(nodeFilter);
    }

    private static final class NodeList extends ChangeNotifyingArrayList<Node> {
        private final Element owner;

        NodeList(Element element, int i) {
            super(i);
            this.owner = element;
        }

        public void onContentsChanged() {
            this.owner.nodelistChanged();
        }
    }

    private boolean isFormatAsBlock(Document.OutputSettings outputSettings) {
        return this.tag.formatAsBlock() || (parent() != null && parent().tag().formatAsBlock()) || outputSettings.outline();
    }

    private boolean isInlineable(Document.OutputSettings outputSettings) {
        return tag().isInline() && (parent() == null || parent().isBlock()) && previousSibling() != null && !outputSettings.outline();
    }
}
