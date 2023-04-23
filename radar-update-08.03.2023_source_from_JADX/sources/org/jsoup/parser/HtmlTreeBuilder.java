package org.jsoup.parser;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.view.InputDeviceCompat;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.HtmlTreeBuilderState;
import org.jsoup.parser.Token;

public class HtmlTreeBuilder extends TreeBuilder {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int MaxScopeSearchDepth = 100;
    static final String[] TagSearchButton = {"button"};
    static final String[] TagSearchEndTags = {"dd", "dt", "li", "optgroup", "option", "p", "rb", "rp", "rt", "rtc"};
    static final String[] TagSearchList = {"ol", "ul"};
    static final String[] TagSearchSelectScope = {"optgroup", "option"};
    static final String[] TagSearchSpecial = {"address", "applet", "area", "article", "aside", "base", "basefont", "bgsound", "blockquote", "body", "br", "button", "caption", "center", "col", "colgroup", "command", "dd", "details", "dir", "div", "dl", "dt", "embed", "fieldset", "figcaption", "figure", "footer", "form", TypedValues.Attributes.S_FRAME, "frameset", "h1", "h2", "h3", "h4", "h5", "h6", "head", "header", "hgroup", "hr", "html", "iframe", "img", "input", "isindex", "li", "link", "listing", "marquee", "menu", "meta", "nav", "noembed", "noframes", "noscript", "object", "ol", "p", "param", "plaintext", "pre", "script", "section", "select", "style", "summary", "table", "tbody", "td", "textarea", "tfoot", "th", "thead", "title", "tr", "ul", "wbr", "xmp"};
    static final String[] TagSearchTableScope = {"html", "table"};
    static final String[] TagThoroughSearchEndTags = {"caption", "colgroup", "dd", "dt", "li", "optgroup", "option", "p", "rb", "rp", "rt", "rtc", "tbody", "td", "tfoot", "th", "thead", "tr"};
    static final String[] TagsSearchInScope = {"applet", "caption", "html", "marquee", "object", "table", "td", "th"};
    private static final int maxQueueDepth = 256;
    private static final int maxUsedFormattingElements = 12;
    private boolean baseUriSetFromDoc;
    @Nullable
    private Element contextElement;
    private Token.EndTag emptyEnd;
    @Nullable
    private FormElement formElement;
    private ArrayList<Element> formattingElements;
    private boolean fosterInserts;
    private boolean fragmentParsing;
    private boolean framesetOk;
    @Nullable
    private Element headElement;
    private HtmlTreeBuilderState originalState;
    private List<String> pendingTableCharacters;
    private String[] specificScopeTarget = {null};
    private HtmlTreeBuilderState state;
    private ArrayList<HtmlTreeBuilderState> tmplInsertMode;

    public /* bridge */ /* synthetic */ boolean processStartTag(String str, Attributes attributes) {
        return super.processStartTag(str, attributes);
    }

    /* access modifiers changed from: package-private */
    public ParseSettings defaultSettings() {
        return ParseSettings.htmlDefault;
    }

    /* access modifiers changed from: package-private */
    public HtmlTreeBuilder newInstance() {
        return new HtmlTreeBuilder();
    }

    /* access modifiers changed from: protected */
    @ParametersAreNonnullByDefault
    public void initialiseParse(Reader reader, String str, Parser parser) {
        super.initialiseParse(reader, str, parser);
        this.state = HtmlTreeBuilderState.Initial;
        this.originalState = null;
        this.baseUriSetFromDoc = false;
        this.headElement = null;
        this.formElement = null;
        this.contextElement = null;
        this.formattingElements = new ArrayList<>();
        this.tmplInsertMode = new ArrayList<>();
        this.pendingTableCharacters = new ArrayList();
        this.emptyEnd = new Token.EndTag();
        this.framesetOk = true;
        this.fosterInserts = false;
        this.fragmentParsing = false;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00a6, code lost:
        if (r0.equals("iframe") == false) goto L_0x0035;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<org.jsoup.nodes.Node> parseFragment(java.lang.String r3, @javax.annotation.Nullable org.jsoup.nodes.Element r4, java.lang.String r5, org.jsoup.parser.Parser r6) {
        /*
            r2 = this;
            org.jsoup.parser.HtmlTreeBuilderState r0 = org.jsoup.parser.HtmlTreeBuilderState.Initial
            r2.state = r0
            java.io.StringReader r0 = new java.io.StringReader
            r0.<init>(r3)
            r2.initialiseParse(r0, r5, r6)
            r2.contextElement = r4
            r3 = 1
            r2.fragmentParsing = r3
            r6 = -1
            if (r4 == 0) goto L_0x011b
            org.jsoup.nodes.Document r0 = r4.ownerDocument()
            if (r0 == 0) goto L_0x0027
            org.jsoup.nodes.Document r0 = r2.doc
            org.jsoup.nodes.Document r1 = r4.ownerDocument()
            org.jsoup.nodes.Document$QuirksMode r1 = r1.quirksMode()
            r0.quirksMode(r1)
        L_0x0027:
            java.lang.String r0 = r4.normalName()
            r0.hashCode()
            int r1 = r0.hashCode()
            switch(r1) {
                case -1321546630: goto L_0x00a9;
                case -1191214428: goto L_0x00a0;
                case -1003243718: goto L_0x0095;
                case -907685685: goto L_0x008a;
                case 118807: goto L_0x007f;
                case 109780401: goto L_0x0074;
                case 110371416: goto L_0x0069;
                case 1192721831: goto L_0x005e;
                case 1551550924: goto L_0x0052;
                case 1973234167: goto L_0x0045;
                case 2115613112: goto L_0x0038;
                default: goto L_0x0035;
            }
        L_0x0035:
            r3 = r6
            goto L_0x00b3
        L_0x0038:
            java.lang.String r3 = "noembed"
            boolean r3 = r0.equals(r3)
            if (r3 != 0) goto L_0x0041
            goto L_0x0035
        L_0x0041:
            r3 = 10
            goto L_0x00b3
        L_0x0045:
            java.lang.String r3 = "plaintext"
            boolean r3 = r0.equals(r3)
            if (r3 != 0) goto L_0x004e
            goto L_0x0035
        L_0x004e:
            r3 = 9
            goto L_0x00b3
        L_0x0052:
            java.lang.String r3 = "noscript"
            boolean r3 = r0.equals(r3)
            if (r3 != 0) goto L_0x005b
            goto L_0x0035
        L_0x005b:
            r3 = 8
            goto L_0x00b3
        L_0x005e:
            java.lang.String r3 = "noframes"
            boolean r3 = r0.equals(r3)
            if (r3 != 0) goto L_0x0067
            goto L_0x0035
        L_0x0067:
            r3 = 7
            goto L_0x00b3
        L_0x0069:
            java.lang.String r3 = "title"
            boolean r3 = r0.equals(r3)
            if (r3 != 0) goto L_0x0072
            goto L_0x0035
        L_0x0072:
            r3 = 6
            goto L_0x00b3
        L_0x0074:
            java.lang.String r3 = "style"
            boolean r3 = r0.equals(r3)
            if (r3 != 0) goto L_0x007d
            goto L_0x0035
        L_0x007d:
            r3 = 5
            goto L_0x00b3
        L_0x007f:
            java.lang.String r3 = "xml"
            boolean r3 = r0.equals(r3)
            if (r3 != 0) goto L_0x0088
            goto L_0x0035
        L_0x0088:
            r3 = 4
            goto L_0x00b3
        L_0x008a:
            java.lang.String r3 = "script"
            boolean r3 = r0.equals(r3)
            if (r3 != 0) goto L_0x0093
            goto L_0x0035
        L_0x0093:
            r3 = 3
            goto L_0x00b3
        L_0x0095:
            java.lang.String r3 = "textarea"
            boolean r3 = r0.equals(r3)
            if (r3 != 0) goto L_0x009e
            goto L_0x0035
        L_0x009e:
            r3 = 2
            goto L_0x00b3
        L_0x00a0:
            java.lang.String r1 = "iframe"
            boolean r1 = r0.equals(r1)
            if (r1 != 0) goto L_0x00b3
            goto L_0x0035
        L_0x00a9:
            java.lang.String r3 = "template"
            boolean r3 = r0.equals(r3)
            if (r3 != 0) goto L_0x00b2
            goto L_0x0035
        L_0x00b2:
            r3 = 0
        L_0x00b3:
            switch(r3) {
                case 0: goto L_0x00e6;
                case 1: goto L_0x00de;
                case 2: goto L_0x00d6;
                case 3: goto L_0x00ce;
                case 4: goto L_0x00de;
                case 5: goto L_0x00de;
                case 6: goto L_0x00d6;
                case 7: goto L_0x00de;
                case 8: goto L_0x00c6;
                case 9: goto L_0x00be;
                case 10: goto L_0x00de;
                default: goto L_0x00b6;
            }
        L_0x00b6:
            org.jsoup.parser.Tokeniser r3 = r2.tokeniser
            org.jsoup.parser.TokeniserState r1 = org.jsoup.parser.TokeniserState.Data
            r3.transition(r1)
            goto L_0x00f2
        L_0x00be:
            org.jsoup.parser.Tokeniser r3 = r2.tokeniser
            org.jsoup.parser.TokeniserState r1 = org.jsoup.parser.TokeniserState.PLAINTEXT
            r3.transition(r1)
            goto L_0x00f2
        L_0x00c6:
            org.jsoup.parser.Tokeniser r3 = r2.tokeniser
            org.jsoup.parser.TokeniserState r1 = org.jsoup.parser.TokeniserState.Data
            r3.transition(r1)
            goto L_0x00f2
        L_0x00ce:
            org.jsoup.parser.Tokeniser r3 = r2.tokeniser
            org.jsoup.parser.TokeniserState r1 = org.jsoup.parser.TokeniserState.ScriptData
            r3.transition(r1)
            goto L_0x00f2
        L_0x00d6:
            org.jsoup.parser.Tokeniser r3 = r2.tokeniser
            org.jsoup.parser.TokeniserState r1 = org.jsoup.parser.TokeniserState.Rcdata
            r3.transition(r1)
            goto L_0x00f2
        L_0x00de:
            org.jsoup.parser.Tokeniser r3 = r2.tokeniser
            org.jsoup.parser.TokeniserState r1 = org.jsoup.parser.TokeniserState.Rawtext
            r3.transition(r1)
            goto L_0x00f2
        L_0x00e6:
            org.jsoup.parser.Tokeniser r3 = r2.tokeniser
            org.jsoup.parser.TokeniserState r1 = org.jsoup.parser.TokeniserState.Data
            r3.transition(r1)
            org.jsoup.parser.HtmlTreeBuilderState r3 = org.jsoup.parser.HtmlTreeBuilderState.InTemplate
            r2.pushTemplateMode(r3)
        L_0x00f2:
            org.jsoup.nodes.Element r3 = new org.jsoup.nodes.Element
            org.jsoup.parser.ParseSettings r1 = r2.settings
            org.jsoup.parser.Tag r0 = r2.tagFor(r0, r1)
            r3.<init>(r0, r5)
            org.jsoup.nodes.Document r5 = r2.doc
            r5.appendChild(r3)
            java.util.ArrayList r5 = r2.stack
            r5.add(r3)
            r2.resetInsertionMode()
            r5 = r4
        L_0x010b:
            if (r5 == 0) goto L_0x011c
            boolean r0 = r5 instanceof org.jsoup.nodes.FormElement
            if (r0 == 0) goto L_0x0116
            org.jsoup.nodes.FormElement r5 = (org.jsoup.nodes.FormElement) r5
            r2.formElement = r5
            goto L_0x011c
        L_0x0116:
            org.jsoup.nodes.Element r5 = r5.parent()
            goto L_0x010b
        L_0x011b:
            r3 = 0
        L_0x011c:
            r2.runParser()
            if (r4 == 0) goto L_0x0133
            java.util.List r4 = r3.siblingNodes()
            boolean r5 = r4.isEmpty()
            if (r5 != 0) goto L_0x012e
            r3.insertChildren((int) r6, (java.util.Collection<? extends org.jsoup.nodes.Node>) r4)
        L_0x012e:
            java.util.List r3 = r3.childNodes()
            return r3
        L_0x0133:
            org.jsoup.nodes.Document r3 = r2.doc
            java.util.List r3 = r3.childNodes()
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jsoup.parser.HtmlTreeBuilder.parseFragment(java.lang.String, org.jsoup.nodes.Element, java.lang.String, org.jsoup.parser.Parser):java.util.List");
    }

    /* access modifiers changed from: protected */
    public boolean process(Token token) {
        this.currentToken = token;
        return this.state.process(token, this);
    }

    /* access modifiers changed from: package-private */
    public boolean process(Token token, HtmlTreeBuilderState htmlTreeBuilderState) {
        this.currentToken = token;
        return htmlTreeBuilderState.process(token, this);
    }

    /* access modifiers changed from: package-private */
    public void transition(HtmlTreeBuilderState htmlTreeBuilderState) {
        this.state = htmlTreeBuilderState;
    }

    /* access modifiers changed from: package-private */
    public HtmlTreeBuilderState state() {
        return this.state;
    }

    /* access modifiers changed from: package-private */
    public void markInsertionMode() {
        this.originalState = this.state;
    }

    /* access modifiers changed from: package-private */
    public HtmlTreeBuilderState originalState() {
        return this.originalState;
    }

    /* access modifiers changed from: package-private */
    public void framesetOk(boolean z) {
        this.framesetOk = z;
    }

    /* access modifiers changed from: package-private */
    public boolean framesetOk() {
        return this.framesetOk;
    }

    /* access modifiers changed from: package-private */
    public Document getDocument() {
        return this.doc;
    }

    /* access modifiers changed from: package-private */
    public String getBaseUri() {
        return this.baseUri;
    }

    /* access modifiers changed from: package-private */
    public void maybeSetBaseUri(Element element) {
        if (!this.baseUriSetFromDoc) {
            String absUrl = element.absUrl(SVGParser.XML_STYLESHEET_ATTR_HREF);
            if (absUrl.length() != 0) {
                this.baseUri = absUrl;
                this.baseUriSetFromDoc = true;
                this.doc.setBaseUri(absUrl);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isFragmentParsing() {
        return this.fragmentParsing;
    }

    /* access modifiers changed from: package-private */
    public void error(HtmlTreeBuilderState htmlTreeBuilderState) {
        if (this.parser.getErrors().canAddError()) {
            this.parser.getErrors().add(new ParseError(this.reader, "Unexpected %s token [%s] when in state [%s]", this.currentToken.tokenType(), this.currentToken, htmlTreeBuilderState));
        }
    }

    /* access modifiers changed from: package-private */
    public Element insert(Token.StartTag startTag) {
        if (startTag.hasAttributes() && !startTag.attributes.isEmpty() && startTag.attributes.deduplicate(this.settings) > 0) {
            error("Dropped duplicate attribute(s) in tag [%s]", startTag.normalName);
        }
        if (startTag.isSelfClosing()) {
            Element insertEmpty = insertEmpty(startTag);
            this.stack.add(insertEmpty);
            this.tokeniser.transition(TokeniserState.Data);
            this.tokeniser.emit((Token) this.emptyEnd.reset().name(insertEmpty.tagName()));
            return insertEmpty;
        }
        Element element = new Element(tagFor(startTag.name(), this.settings), (String) null, this.settings.normalizeAttributes(startTag.attributes));
        insert(element, startTag);
        return element;
    }

    /* access modifiers changed from: package-private */
    public Element insertStartTag(String str) {
        Element element = new Element(tagFor(str, this.settings), (String) null);
        insert(element);
        return element;
    }

    /* access modifiers changed from: package-private */
    public void insert(Element element) {
        insertNode(element, (Token) null);
        this.stack.add(element);
    }

    private void insert(Element element, @Nullable Token token) {
        insertNode(element, token);
        this.stack.add(element);
    }

    /* access modifiers changed from: package-private */
    public Element insertEmpty(Token.StartTag startTag) {
        Tag tagFor = tagFor(startTag.name(), this.settings);
        Element element = new Element(tagFor, (String) null, this.settings.normalizeAttributes(startTag.attributes));
        insertNode(element, startTag);
        if (startTag.isSelfClosing()) {
            if (!tagFor.isKnownTag()) {
                tagFor.setSelfClosing();
            } else if (!tagFor.isEmpty()) {
                this.tokeniser.error("Tag [%s] cannot be self closing; not a void tag", tagFor.normalName());
            }
        }
        return element;
    }

    /* access modifiers changed from: package-private */
    public FormElement insertForm(Token.StartTag startTag, boolean z, boolean z2) {
        FormElement formElement2 = new FormElement(tagFor(startTag.name(), this.settings), (String) null, this.settings.normalizeAttributes(startTag.attributes));
        if (!z2) {
            setFormElement(formElement2);
        } else if (!onStack("template")) {
            setFormElement(formElement2);
        }
        insertNode(formElement2, startTag);
        if (z) {
            this.stack.add(formElement2);
        }
        return formElement2;
    }

    /* access modifiers changed from: package-private */
    public void insert(Token.Comment comment) {
        insertNode(new Comment(comment.getData()), comment);
    }

    /* access modifiers changed from: package-private */
    public void insert(Token.Character character) {
        Node node;
        Element currentElement = currentElement();
        String normalName = currentElement.normalName();
        String data = character.getData();
        if (character.isCData()) {
            node = new CDataNode(data);
        } else if (isContentForTagData(normalName)) {
            node = new DataNode(data);
        } else {
            node = new TextNode(data);
        }
        currentElement.appendChild(node);
        onNodeInserted(node, character);
    }

    private void insertNode(Node node, @Nullable Token token) {
        FormElement formElement2;
        if (this.stack.isEmpty()) {
            this.doc.appendChild(node);
        } else if (!isFosterInserts() || !StringUtil.inSorted(currentElement().normalName(), HtmlTreeBuilderState.Constants.InTableFoster)) {
            currentElement().appendChild(node);
        } else {
            insertInFosterParent(node);
        }
        if (node instanceof Element) {
            Element element = (Element) node;
            if (element.tag().isFormListed() && (formElement2 = this.formElement) != null) {
                formElement2.addElement(element);
            }
        }
        onNodeInserted(node, token);
    }

    /* access modifiers changed from: package-private */
    public Element pop() {
        return (Element) this.stack.remove(this.stack.size() - 1);
    }

    /* access modifiers changed from: package-private */
    public void push(Element element) {
        this.stack.add(element);
    }

    /* access modifiers changed from: package-private */
    public ArrayList<Element> getStack() {
        return this.stack;
    }

    /* access modifiers changed from: package-private */
    public boolean onStack(Element element) {
        return onStack(this.stack, element);
    }

    /* access modifiers changed from: package-private */
    public boolean onStack(String str) {
        return getFromStack(str) != null;
    }

    private static boolean onStack(ArrayList<Element> arrayList, Element element) {
        int size = arrayList.size() - 1;
        int i = size >= 256 ? size + InputDeviceCompat.SOURCE_ANY : 0;
        while (size >= i) {
            if (arrayList.get(size) == element) {
                return true;
            }
            size--;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public Element getFromStack(String str) {
        int size = this.stack.size() - 1;
        int i = size >= 256 ? size + InputDeviceCompat.SOURCE_ANY : 0;
        while (size >= i) {
            Element element = (Element) this.stack.get(size);
            if (element.normalName().equals(str)) {
                return element;
            }
            size--;
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public boolean removeFromStack(Element element) {
        for (int size = this.stack.size() - 1; size >= 0; size--) {
            if (((Element) this.stack.get(size)) == element) {
                this.stack.remove(size);
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public Element popStackToClose(String str) {
        for (int size = this.stack.size() - 1; size >= 0; size--) {
            Element element = (Element) this.stack.get(size);
            this.stack.remove(size);
            if (element.normalName().equals(str)) {
                if (this.currentToken instanceof Token.EndTag) {
                    onNodeClosed(element, this.currentToken);
                }
                return element;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void popStackToClose(String... strArr) {
        int size = this.stack.size() - 1;
        while (size >= 0) {
            this.stack.remove(size);
            if (!StringUtil.inSorted(((Element) this.stack.get(size)).normalName(), strArr)) {
                size--;
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void popStackToBefore(String str) {
        int size = this.stack.size() - 1;
        while (size >= 0 && !((Element) this.stack.get(size)).normalName().equals(str)) {
            this.stack.remove(size);
            size--;
        }
    }

    /* access modifiers changed from: package-private */
    public void clearStackToTableContext() {
        clearStackToContext("table", "template");
    }

    /* access modifiers changed from: package-private */
    public void clearStackToTableBodyContext() {
        clearStackToContext("tbody", "tfoot", "thead", "template");
    }

    /* access modifiers changed from: package-private */
    public void clearStackToTableRowContext() {
        clearStackToContext("tr", "template");
    }

    private void clearStackToContext(String... strArr) {
        int size = this.stack.size() - 1;
        while (size >= 0) {
            Element element = (Element) this.stack.get(size);
            if (!StringUtil.m146in(element.normalName(), strArr) && !element.normalName().equals("html")) {
                this.stack.remove(size);
                size--;
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public Element aboveOnStack(Element element) {
        for (int size = this.stack.size() - 1; size >= 0; size--) {
            if (((Element) this.stack.get(size)) == element) {
                return (Element) this.stack.get(size - 1);
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void insertOnStackAfter(Element element, Element element2) {
        int lastIndexOf = this.stack.lastIndexOf(element);
        Validate.isTrue(lastIndexOf != -1);
        this.stack.add(lastIndexOf + 1, element2);
    }

    /* access modifiers changed from: package-private */
    public void replaceOnStack(Element element, Element element2) {
        replaceInQueue(this.stack, element, element2);
    }

    private void replaceInQueue(ArrayList<Element> arrayList, Element element, Element element2) {
        int lastIndexOf = arrayList.lastIndexOf(element);
        Validate.isTrue(lastIndexOf != -1);
        arrayList.set(lastIndexOf, element2);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean resetInsertionMode() {
        /*
            r9 = this;
            java.util.ArrayList r0 = r9.stack
            int r0 = r0.size()
            r1 = 1
            int r0 = r0 - r1
            r2 = 256(0x100, float:3.59E-43)
            r3 = 0
            if (r0 < r2) goto L_0x0010
            int r2 = r0 + -256
            goto L_0x0011
        L_0x0010:
            r2 = r3
        L_0x0011:
            org.jsoup.parser.HtmlTreeBuilderState r4 = r9.state
            java.util.ArrayList r5 = r9.stack
            int r5 = r5.size()
            if (r5 != 0) goto L_0x0020
            org.jsoup.parser.HtmlTreeBuilderState r5 = org.jsoup.parser.HtmlTreeBuilderState.InBody
            r9.transition(r5)
        L_0x0020:
            r5 = r3
        L_0x0021:
            if (r0 < r2) goto L_0x016c
            java.util.ArrayList r6 = r9.stack
            java.lang.Object r6 = r6.get(r0)
            org.jsoup.nodes.Element r6 = (org.jsoup.nodes.Element) r6
            if (r0 != r2) goto L_0x0034
            boolean r5 = r9.fragmentParsing
            if (r5 == 0) goto L_0x0033
            org.jsoup.nodes.Element r6 = r9.contextElement
        L_0x0033:
            r5 = r1
        L_0x0034:
            if (r6 == 0) goto L_0x003b
            java.lang.String r6 = r6.normalName()
            goto L_0x003d
        L_0x003b:
            java.lang.String r6 = ""
        L_0x003d:
            r6.hashCode()
            int r7 = r6.hashCode()
            r8 = -1
            switch(r7) {
                case -1644953643: goto L_0x00f9;
                case -1321546630: goto L_0x00ee;
                case -906021636: goto L_0x00e3;
                case -636197633: goto L_0x00d8;
                case 3696: goto L_0x00cd;
                case 3700: goto L_0x00c2;
                case 3710: goto L_0x00b7;
                case 3029410: goto L_0x00ac;
                case 3198432: goto L_0x009e;
                case 3213227: goto L_0x0090;
                case 110115790: goto L_0x0082;
                case 110157846: goto L_0x0074;
                case 110277346: goto L_0x0066;
                case 110326868: goto L_0x0058;
                case 552573414: goto L_0x004a;
                default: goto L_0x0048;
            }
        L_0x0048:
            goto L_0x0103
        L_0x004a:
            java.lang.String r7 = "caption"
            boolean r6 = r6.equals(r7)
            if (r6 != 0) goto L_0x0054
            goto L_0x0103
        L_0x0054:
            r8 = 14
            goto L_0x0103
        L_0x0058:
            java.lang.String r7 = "thead"
            boolean r6 = r6.equals(r7)
            if (r6 != 0) goto L_0x0062
            goto L_0x0103
        L_0x0062:
            r8 = 13
            goto L_0x0103
        L_0x0066:
            java.lang.String r7 = "tfoot"
            boolean r6 = r6.equals(r7)
            if (r6 != 0) goto L_0x0070
            goto L_0x0103
        L_0x0070:
            r8 = 12
            goto L_0x0103
        L_0x0074:
            java.lang.String r7 = "tbody"
            boolean r6 = r6.equals(r7)
            if (r6 != 0) goto L_0x007e
            goto L_0x0103
        L_0x007e:
            r8 = 11
            goto L_0x0103
        L_0x0082:
            java.lang.String r7 = "table"
            boolean r6 = r6.equals(r7)
            if (r6 != 0) goto L_0x008c
            goto L_0x0103
        L_0x008c:
            r8 = 10
            goto L_0x0103
        L_0x0090:
            java.lang.String r7 = "html"
            boolean r6 = r6.equals(r7)
            if (r6 != 0) goto L_0x009a
            goto L_0x0103
        L_0x009a:
            r8 = 9
            goto L_0x0103
        L_0x009e:
            java.lang.String r7 = "head"
            boolean r6 = r6.equals(r7)
            if (r6 != 0) goto L_0x00a8
            goto L_0x0103
        L_0x00a8:
            r8 = 8
            goto L_0x0103
        L_0x00ac:
            java.lang.String r7 = "body"
            boolean r6 = r6.equals(r7)
            if (r6 != 0) goto L_0x00b5
            goto L_0x0103
        L_0x00b5:
            r8 = 7
            goto L_0x0103
        L_0x00b7:
            java.lang.String r7 = "tr"
            boolean r6 = r6.equals(r7)
            if (r6 != 0) goto L_0x00c0
            goto L_0x0103
        L_0x00c0:
            r8 = 6
            goto L_0x0103
        L_0x00c2:
            java.lang.String r7 = "th"
            boolean r6 = r6.equals(r7)
            if (r6 != 0) goto L_0x00cb
            goto L_0x0103
        L_0x00cb:
            r8 = 5
            goto L_0x0103
        L_0x00cd:
            java.lang.String r7 = "td"
            boolean r6 = r6.equals(r7)
            if (r6 != 0) goto L_0x00d6
            goto L_0x0103
        L_0x00d6:
            r8 = 4
            goto L_0x0103
        L_0x00d8:
            java.lang.String r7 = "colgroup"
            boolean r6 = r6.equals(r7)
            if (r6 != 0) goto L_0x00e1
            goto L_0x0103
        L_0x00e1:
            r8 = 3
            goto L_0x0103
        L_0x00e3:
            java.lang.String r7 = "select"
            boolean r6 = r6.equals(r7)
            if (r6 != 0) goto L_0x00ec
            goto L_0x0103
        L_0x00ec:
            r8 = 2
            goto L_0x0103
        L_0x00ee:
            java.lang.String r7 = "template"
            boolean r6 = r6.equals(r7)
            if (r6 != 0) goto L_0x00f7
            goto L_0x0103
        L_0x00f7:
            r8 = r1
            goto L_0x0103
        L_0x00f9:
            java.lang.String r7 = "frameset"
            boolean r6 = r6.equals(r7)
            if (r6 != 0) goto L_0x0102
            goto L_0x0103
        L_0x0102:
            r8 = r3
        L_0x0103:
            switch(r8) {
                case 0: goto L_0x0167;
                case 1: goto L_0x015a;
                case 2: goto L_0x0154;
                case 3: goto L_0x014e;
                case 4: goto L_0x013a;
                case 5: goto L_0x013a;
                case 6: goto L_0x0134;
                case 7: goto L_0x012e;
                case 8: goto L_0x0126;
                case 9: goto L_0x0119;
                case 10: goto L_0x0113;
                case 11: goto L_0x010d;
                case 12: goto L_0x010d;
                case 13: goto L_0x010d;
                case 14: goto L_0x0107;
                default: goto L_0x0106;
            }
        L_0x0106:
            goto L_0x0142
        L_0x0107:
            org.jsoup.parser.HtmlTreeBuilderState r0 = org.jsoup.parser.HtmlTreeBuilderState.InCaption
            r9.transition(r0)
            goto L_0x016c
        L_0x010d:
            org.jsoup.parser.HtmlTreeBuilderState r0 = org.jsoup.parser.HtmlTreeBuilderState.InTableBody
            r9.transition(r0)
            goto L_0x016c
        L_0x0113:
            org.jsoup.parser.HtmlTreeBuilderState r0 = org.jsoup.parser.HtmlTreeBuilderState.InTable
            r9.transition(r0)
            goto L_0x016c
        L_0x0119:
            org.jsoup.nodes.Element r0 = r9.headElement
            if (r0 != 0) goto L_0x0120
            org.jsoup.parser.HtmlTreeBuilderState r0 = org.jsoup.parser.HtmlTreeBuilderState.BeforeHead
            goto L_0x0122
        L_0x0120:
            org.jsoup.parser.HtmlTreeBuilderState r0 = org.jsoup.parser.HtmlTreeBuilderState.AfterHead
        L_0x0122:
            r9.transition(r0)
            goto L_0x016c
        L_0x0126:
            if (r5 != 0) goto L_0x0142
            org.jsoup.parser.HtmlTreeBuilderState r0 = org.jsoup.parser.HtmlTreeBuilderState.InHead
            r9.transition(r0)
            goto L_0x016c
        L_0x012e:
            org.jsoup.parser.HtmlTreeBuilderState r0 = org.jsoup.parser.HtmlTreeBuilderState.InBody
            r9.transition(r0)
            goto L_0x016c
        L_0x0134:
            org.jsoup.parser.HtmlTreeBuilderState r0 = org.jsoup.parser.HtmlTreeBuilderState.InRow
            r9.transition(r0)
            goto L_0x016c
        L_0x013a:
            if (r5 != 0) goto L_0x0142
            org.jsoup.parser.HtmlTreeBuilderState r0 = org.jsoup.parser.HtmlTreeBuilderState.InCell
            r9.transition(r0)
            goto L_0x016c
        L_0x0142:
            if (r5 == 0) goto L_0x014a
            org.jsoup.parser.HtmlTreeBuilderState r0 = org.jsoup.parser.HtmlTreeBuilderState.InBody
            r9.transition(r0)
            goto L_0x016c
        L_0x014a:
            int r0 = r0 + -1
            goto L_0x0021
        L_0x014e:
            org.jsoup.parser.HtmlTreeBuilderState r0 = org.jsoup.parser.HtmlTreeBuilderState.InColumnGroup
            r9.transition(r0)
            goto L_0x016c
        L_0x0154:
            org.jsoup.parser.HtmlTreeBuilderState r0 = org.jsoup.parser.HtmlTreeBuilderState.InSelect
            r9.transition(r0)
            goto L_0x016c
        L_0x015a:
            org.jsoup.parser.HtmlTreeBuilderState r0 = r9.currentTemplateMode()
            java.lang.String r2 = "Bug: no template insertion mode on stack!"
            org.jsoup.helper.Validate.notNull(r0, r2)
            r9.transition(r0)
            goto L_0x016c
        L_0x0167:
            org.jsoup.parser.HtmlTreeBuilderState r0 = org.jsoup.parser.HtmlTreeBuilderState.InFrameset
            r9.transition(r0)
        L_0x016c:
            org.jsoup.parser.HtmlTreeBuilderState r0 = r9.state
            if (r0 == r4) goto L_0x0171
            goto L_0x0172
        L_0x0171:
            r1 = r3
        L_0x0172:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jsoup.parser.HtmlTreeBuilder.resetInsertionMode():boolean");
    }

    /* access modifiers changed from: package-private */
    public void resetBody() {
        if (!onStack("body")) {
            this.stack.add(this.doc.body());
        }
        transition(HtmlTreeBuilderState.InBody);
    }

    private boolean inSpecificScope(String str, String[] strArr, String[] strArr2) {
        String[] strArr3 = this.specificScopeTarget;
        strArr3[0] = str;
        return inSpecificScope(strArr3, strArr, strArr2);
    }

    private boolean inSpecificScope(String[] strArr, String[] strArr2, String[] strArr3) {
        int size = this.stack.size() - 1;
        int i = size > 100 ? size - 100 : 0;
        while (size >= i) {
            String normalName = ((Element) this.stack.get(size)).normalName();
            if (StringUtil.inSorted(normalName, strArr)) {
                return true;
            }
            if (StringUtil.inSorted(normalName, strArr2)) {
                return false;
            }
            if (strArr3 != null && StringUtil.inSorted(normalName, strArr3)) {
                return false;
            }
            size--;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean inScope(String[] strArr) {
        return inSpecificScope(strArr, TagsSearchInScope, (String[]) null);
    }

    /* access modifiers changed from: package-private */
    public boolean inScope(String str) {
        return inScope(str, (String[]) null);
    }

    /* access modifiers changed from: package-private */
    public boolean inScope(String str, String[] strArr) {
        return inSpecificScope(str, TagsSearchInScope, strArr);
    }

    /* access modifiers changed from: package-private */
    public boolean inListItemScope(String str) {
        return inScope(str, TagSearchList);
    }

    /* access modifiers changed from: package-private */
    public boolean inButtonScope(String str) {
        return inScope(str, TagSearchButton);
    }

    /* access modifiers changed from: package-private */
    public boolean inTableScope(String str) {
        return inSpecificScope(str, TagSearchTableScope, (String[]) null);
    }

    /* access modifiers changed from: package-private */
    public boolean inSelectScope(String str) {
        for (int size = this.stack.size() - 1; size >= 0; size--) {
            String normalName = ((Element) this.stack.get(size)).normalName();
            if (normalName.equals(str)) {
                return true;
            }
            if (!StringUtil.inSorted(normalName, TagSearchSelectScope)) {
                return false;
            }
        }
        Validate.fail("Should not be reachable");
        return false;
    }

    /* access modifiers changed from: package-private */
    public void setHeadElement(Element element) {
        this.headElement = element;
    }

    /* access modifiers changed from: package-private */
    public Element getHeadElement() {
        return this.headElement;
    }

    /* access modifiers changed from: package-private */
    public boolean isFosterInserts() {
        return this.fosterInserts;
    }

    /* access modifiers changed from: package-private */
    public void setFosterInserts(boolean z) {
        this.fosterInserts = z;
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public FormElement getFormElement() {
        return this.formElement;
    }

    /* access modifiers changed from: package-private */
    public void setFormElement(FormElement formElement2) {
        this.formElement = formElement2;
    }

    /* access modifiers changed from: package-private */
    public void newPendingTableCharacters() {
        this.pendingTableCharacters = new ArrayList();
    }

    /* access modifiers changed from: package-private */
    public List<String> getPendingTableCharacters() {
        return this.pendingTableCharacters;
    }

    /* access modifiers changed from: package-private */
    public void generateImpliedEndTags(String str) {
        while (StringUtil.inSorted(currentElement().normalName(), TagSearchEndTags)) {
            if (str == null || !currentElementIs(str)) {
                pop();
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void generateImpliedEndTags() {
        generateImpliedEndTags(false);
    }

    /* access modifiers changed from: package-private */
    public void generateImpliedEndTags(boolean z) {
        String[] strArr = z ? TagThoroughSearchEndTags : TagSearchEndTags;
        while (StringUtil.inSorted(currentElement().normalName(), strArr)) {
            pop();
        }
    }

    /* access modifiers changed from: package-private */
    public void closeElement(String str) {
        generateImpliedEndTags(str);
        if (!str.equals(currentElement().normalName())) {
            error(state());
        }
        popStackToClose(str);
    }

    /* access modifiers changed from: package-private */
    public boolean isSpecial(Element element) {
        return StringUtil.inSorted(element.normalName(), TagSearchSpecial);
    }

    /* access modifiers changed from: package-private */
    public Element lastFormattingElement() {
        if (this.formattingElements.size() <= 0) {
            return null;
        }
        ArrayList<Element> arrayList = this.formattingElements;
        return arrayList.get(arrayList.size() - 1);
    }

    /* access modifiers changed from: package-private */
    public int positionOfElement(Element element) {
        for (int i = 0; i < this.formattingElements.size(); i++) {
            if (element == this.formattingElements.get(i)) {
                return i;
            }
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public Element removeLastFormattingElement() {
        int size = this.formattingElements.size();
        if (size > 0) {
            return this.formattingElements.remove(size - 1);
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void pushActiveFormattingElements(Element element) {
        checkActiveFormattingElements(element);
        this.formattingElements.add(element);
    }

    /* access modifiers changed from: package-private */
    public void pushWithBookmark(Element element, int i) {
        checkActiveFormattingElements(element);
        try {
            this.formattingElements.add(i, element);
        } catch (IndexOutOfBoundsException unused) {
            this.formattingElements.add(element);
        }
    }

    /* access modifiers changed from: package-private */
    public void checkActiveFormattingElements(Element element) {
        int size = this.formattingElements.size() - 1;
        int i = size - 12;
        int i2 = 0;
        if (i < 0) {
            i = 0;
        }
        while (size >= i) {
            Element element2 = this.formattingElements.get(size);
            if (element2 != null) {
                if (isSameFormattingElement(element, element2)) {
                    i2++;
                }
                if (i2 == 3) {
                    this.formattingElements.remove(size);
                    return;
                }
                size--;
            } else {
                return;
            }
        }
    }

    private boolean isSameFormattingElement(Element element, Element element2) {
        return element.normalName().equals(element2.normalName()) && element.attributes().equals(element2.attributes());
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x003f  */
    /* JADX WARNING: Removed duplicated region for block: B:25:? A[RETURN, SYNTHETIC] */
    void reconstructFormattingElements() {
        /*
            r8 = this;
            java.util.ArrayList r0 = r8.stack
            int r0 = r0.size()
            r1 = 256(0x100, float:3.59E-43)
            if (r0 <= r1) goto L_0x000b
            return
        L_0x000b:
            org.jsoup.nodes.Element r0 = r8.lastFormattingElement()
            if (r0 == 0) goto L_0x006e
            boolean r1 = r8.onStack((org.jsoup.nodes.Element) r0)
            if (r1 == 0) goto L_0x0018
            goto L_0x006e
        L_0x0018:
            java.util.ArrayList<org.jsoup.nodes.Element> r1 = r8.formattingElements
            int r1 = r1.size()
            int r2 = r1 + -12
            r3 = 0
            if (r2 >= 0) goto L_0x0024
            r2 = r3
        L_0x0024:
            r4 = 1
            int r1 = r1 - r4
            r5 = r1
        L_0x0027:
            if (r5 != r2) goto L_0x002a
            goto L_0x003d
        L_0x002a:
            java.util.ArrayList<org.jsoup.nodes.Element> r0 = r8.formattingElements
            int r5 = r5 + -1
            java.lang.Object r0 = r0.get(r5)
            org.jsoup.nodes.Element r0 = (org.jsoup.nodes.Element) r0
            if (r0 == 0) goto L_0x003c
            boolean r6 = r8.onStack((org.jsoup.nodes.Element) r0)
            if (r6 == 0) goto L_0x0027
        L_0x003c:
            r4 = r3
        L_0x003d:
            if (r4 != 0) goto L_0x0049
            java.util.ArrayList<org.jsoup.nodes.Element> r0 = r8.formattingElements
            int r5 = r5 + 1
            java.lang.Object r0 = r0.get(r5)
            org.jsoup.nodes.Element r0 = (org.jsoup.nodes.Element) r0
        L_0x0049:
            org.jsoup.helper.Validate.notNull(r0)
            org.jsoup.nodes.Element r2 = new org.jsoup.nodes.Element
            java.lang.String r4 = r0.normalName()
            org.jsoup.parser.ParseSettings r6 = r8.settings
            org.jsoup.parser.Tag r4 = r8.tagFor(r4, r6)
            r6 = 0
            org.jsoup.nodes.Attributes r7 = r0.attributes()
            org.jsoup.nodes.Attributes r7 = r7.clone()
            r2.<init>(r4, r6, r7)
            r8.insert((org.jsoup.nodes.Element) r2)
            java.util.ArrayList<org.jsoup.nodes.Element> r4 = r8.formattingElements
            r4.set(r5, r2)
            if (r5 != r1) goto L_0x003c
        L_0x006e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jsoup.parser.HtmlTreeBuilder.reconstructFormattingElements():void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP:0: B:0:0x0000->B:3:0x000c, LOOP_START, MTH_ENTER_BLOCK] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void clearFormattingElementsToLastMarker() {
        /*
            r1 = this;
        L_0x0000:
            java.util.ArrayList<org.jsoup.nodes.Element> r0 = r1.formattingElements
            boolean r0 = r0.isEmpty()
            if (r0 != 0) goto L_0x000e
            org.jsoup.nodes.Element r0 = r1.removeLastFormattingElement()
            if (r0 != 0) goto L_0x0000
        L_0x000e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jsoup.parser.HtmlTreeBuilder.clearFormattingElementsToLastMarker():void");
    }

    /* access modifiers changed from: package-private */
    public void removeFromActiveFormattingElements(Element element) {
        for (int size = this.formattingElements.size() - 1; size >= 0; size--) {
            if (this.formattingElements.get(size) == element) {
                this.formattingElements.remove(size);
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isInActiveFormattingElements(Element element) {
        return onStack(this.formattingElements, element);
    }

    /* access modifiers changed from: package-private */
    public Element getActiveFormattingElement(String str) {
        for (int size = this.formattingElements.size() - 1; size >= 0; size--) {
            Element element = this.formattingElements.get(size);
            if (element == null) {
                return null;
            }
            if (element.normalName().equals(str)) {
                return element;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void replaceActiveFormattingElement(Element element, Element element2) {
        replaceInQueue(this.formattingElements, element, element2);
    }

    /* access modifiers changed from: package-private */
    public void insertMarkerToFormattingElements() {
        this.formattingElements.add((Object) null);
    }

    /* access modifiers changed from: package-private */
    public void insertInFosterParent(Node node) {
        Element element;
        Element fromStack = getFromStack("table");
        boolean z = false;
        if (fromStack == null) {
            element = (Element) this.stack.get(0);
        } else if (fromStack.parent() != null) {
            element = fromStack.parent();
            z = true;
        } else {
            element = aboveOnStack(fromStack);
        }
        if (z) {
            Validate.notNull(fromStack);
            fromStack.before(node);
            return;
        }
        element.appendChild(node);
    }

    /* access modifiers changed from: package-private */
    public void pushTemplateMode(HtmlTreeBuilderState htmlTreeBuilderState) {
        this.tmplInsertMode.add(htmlTreeBuilderState);
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public HtmlTreeBuilderState popTemplateMode() {
        if (this.tmplInsertMode.size() <= 0) {
            return null;
        }
        ArrayList<HtmlTreeBuilderState> arrayList = this.tmplInsertMode;
        return arrayList.remove(arrayList.size() - 1);
    }

    /* access modifiers changed from: package-private */
    public int templateModeSize() {
        return this.tmplInsertMode.size();
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public HtmlTreeBuilderState currentTemplateMode() {
        if (this.tmplInsertMode.size() <= 0) {
            return null;
        }
        ArrayList<HtmlTreeBuilderState> arrayList = this.tmplInsertMode;
        return arrayList.get(arrayList.size() - 1);
    }

    public String toString() {
        return "TreeBuilder{currentToken=" + this.currentToken + ", state=" + this.state + ", currentElement=" + currentElement() + '}';
    }

    /* access modifiers changed from: protected */
    public boolean isContentForTagData(String str) {
        return str.equals("script") || str.equals("style");
    }
}
