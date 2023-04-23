package org.jsoup.nodes;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import javax.annotation.Nullable;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;

public class Document extends Element {
    private static final Evaluator titleEval = new Evaluator.Tag("title");
    @Nullable
    private Connection connection;
    private final String location;
    private OutputSettings outputSettings = new OutputSettings();
    private Parser parser;
    private QuirksMode quirksMode = QuirksMode.noQuirks;
    private boolean updateMetaCharset = false;

    public enum QuirksMode {
        noQuirks,
        quirks,
        limitedQuirks
    }

    public String nodeName() {
        return "#document";
    }

    public Document(String str) {
        super(Tag.valueOf("#root", ParseSettings.htmlDefault), str);
        this.location = str;
        this.parser = Parser.htmlParser();
    }

    public static Document createShell(String str) {
        Validate.notNull(str);
        Document document = new Document(str);
        document.parser = document.parser();
        Element appendElement = document.appendElement("html");
        appendElement.appendElement("head");
        appendElement.appendElement("body");
        return document;
    }

    public String location() {
        return this.location;
    }

    public Connection connection() {
        Connection connection2 = this.connection;
        return connection2 == null ? Jsoup.newSession() : connection2;
    }

    @Nullable
    public DocumentType documentType() {
        for (Node node : this.childNodes) {
            if (node instanceof DocumentType) {
                return (DocumentType) node;
            }
            if (!(node instanceof LeafNode)) {
                return null;
            }
        }
        return null;
    }

    private Element htmlEl() {
        for (Element next : childElementsList()) {
            if (next.normalName().equals("html")) {
                return next;
            }
        }
        return appendElement("html");
    }

    public Element head() {
        Element htmlEl = htmlEl();
        for (Element next : htmlEl.childElementsList()) {
            if (next.normalName().equals("head")) {
                return next;
            }
        }
        return htmlEl.prependElement("head");
    }

    /* JADX WARNING: Removed duplicated region for block: B:3:0x0014  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.jsoup.nodes.Element body() {
        /*
            r5 = this;
            org.jsoup.nodes.Element r0 = r5.htmlEl()
            java.util.List r1 = r0.childElementsList()
            java.util.Iterator r1 = r1.iterator()
        L_0x000c:
            boolean r2 = r1.hasNext()
            java.lang.String r3 = "body"
            if (r2 == 0) goto L_0x0031
            java.lang.Object r2 = r1.next()
            org.jsoup.nodes.Element r2 = (org.jsoup.nodes.Element) r2
            java.lang.String r4 = r2.normalName()
            boolean r3 = r3.equals(r4)
            if (r3 != 0) goto L_0x0030
            java.lang.String r3 = r2.normalName()
            java.lang.String r4 = "frameset"
            boolean r3 = r4.equals(r3)
            if (r3 == 0) goto L_0x000c
        L_0x0030:
            return r2
        L_0x0031:
            org.jsoup.nodes.Element r0 = r0.appendElement(r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jsoup.nodes.Document.body():org.jsoup.nodes.Element");
    }

    public String title() {
        Element selectFirst = head().selectFirst(titleEval);
        return selectFirst != null ? StringUtil.normaliseWhitespace(selectFirst.text()).trim() : "";
    }

    public void title(String str) {
        Validate.notNull(str);
        Element selectFirst = head().selectFirst(titleEval);
        if (selectFirst == null) {
            selectFirst = head().appendElement("title");
        }
        selectFirst.text(str);
    }

    public Element createElement(String str) {
        return new Element(Tag.valueOf(str, ParseSettings.preserveCase), baseUri());
    }

    public Document normalise() {
        Element htmlEl = htmlEl();
        Element head = head();
        body();
        normaliseTextNodes(head);
        normaliseTextNodes(htmlEl);
        normaliseTextNodes(this);
        normaliseStructure("head", htmlEl);
        normaliseStructure("body", htmlEl);
        ensureMetaCharsetElement();
        return this;
    }

    private void normaliseTextNodes(Element element) {
        ArrayList arrayList = new ArrayList();
        for (Node next : element.childNodes) {
            if (next instanceof TextNode) {
                TextNode textNode = (TextNode) next;
                if (!textNode.isBlank()) {
                    arrayList.add(textNode);
                }
            }
        }
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            Node node = (Node) arrayList.get(size);
            element.removeChild(node);
            body().prependChild(new TextNode(" "));
            body().prependChild(node);
        }
    }

    private void normaliseStructure(String str, Element element) {
        Elements elementsByTag = getElementsByTag(str);
        Element first = elementsByTag.first();
        if (elementsByTag.size() > 1) {
            ArrayList<Node> arrayList = new ArrayList<>();
            for (int i = 1; i < elementsByTag.size(); i++) {
                Node node = (Node) elementsByTag.get(i);
                arrayList.addAll(node.ensureChildNodes());
                node.remove();
            }
            for (Node appendChild : arrayList) {
                first.appendChild(appendChild);
            }
        }
        if (first.parent() != null && !first.parent().equals(element)) {
            element.appendChild(first);
        }
    }

    public String outerHtml() {
        return super.html();
    }

    public Element text(String str) {
        body().text(str);
        return this;
    }

    public void charset(Charset charset) {
        updateMetaCharsetElement(true);
        this.outputSettings.charset(charset);
        ensureMetaCharsetElement();
    }

    public Charset charset() {
        return this.outputSettings.charset();
    }

    public void updateMetaCharsetElement(boolean z) {
        this.updateMetaCharset = z;
    }

    public boolean updateMetaCharsetElement() {
        return this.updateMetaCharset;
    }

    public Document clone() {
        Document document = (Document) super.clone();
        document.outputSettings = this.outputSettings.clone();
        return document;
    }

    public Document shallowClone() {
        Document document = new Document(baseUri());
        if (this.attributes != null) {
            document.attributes = this.attributes.clone();
        }
        document.outputSettings = this.outputSettings.clone();
        return document;
    }

    private void ensureMetaCharsetElement() {
        if (this.updateMetaCharset) {
            OutputSettings.Syntax syntax = outputSettings().syntax();
            if (syntax == OutputSettings.Syntax.html) {
                Element selectFirst = selectFirst("meta[charset]");
                if (selectFirst != null) {
                    selectFirst.attr("charset", charset().displayName());
                } else {
                    head().appendElement("meta").attr("charset", charset().displayName());
                }
                select("meta[name=charset]").remove();
            } else if (syntax == OutputSettings.Syntax.xml) {
                Node node = ensureChildNodes().get(0);
                if (node instanceof XmlDeclaration) {
                    XmlDeclaration xmlDeclaration = (XmlDeclaration) node;
                    if (xmlDeclaration.name().equals("xml")) {
                        xmlDeclaration.attr("encoding", charset().displayName());
                        if (xmlDeclaration.hasAttr("version")) {
                            xmlDeclaration.attr("version", "1.0");
                            return;
                        }
                        return;
                    }
                    XmlDeclaration xmlDeclaration2 = new XmlDeclaration("xml", false);
                    xmlDeclaration2.attr("version", "1.0");
                    xmlDeclaration2.attr("encoding", charset().displayName());
                    prependChild(xmlDeclaration2);
                    return;
                }
                XmlDeclaration xmlDeclaration3 = new XmlDeclaration("xml", false);
                xmlDeclaration3.attr("version", "1.0");
                xmlDeclaration3.attr("encoding", charset().displayName());
                prependChild(xmlDeclaration3);
            }
        }
    }

    public static class OutputSettings implements Cloneable {
        private Charset charset = DataUtil.UTF_8;
        @Nullable
        Entities.CoreCharset coreCharset;
        private final ThreadLocal<CharsetEncoder> encoderThreadLocal = new ThreadLocal<>();
        private Entities.EscapeMode escapeMode = Entities.EscapeMode.base;
        private int indentAmount = 1;
        private int maxPaddingWidth = 30;
        private boolean outline = false;
        private boolean prettyPrint = true;
        private Syntax syntax = Syntax.html;

        public enum Syntax {
            html,
            xml
        }

        public Entities.EscapeMode escapeMode() {
            return this.escapeMode;
        }

        public OutputSettings escapeMode(Entities.EscapeMode escapeMode2) {
            this.escapeMode = escapeMode2;
            return this;
        }

        public Charset charset() {
            return this.charset;
        }

        public OutputSettings charset(Charset charset2) {
            this.charset = charset2;
            return this;
        }

        public OutputSettings charset(String str) {
            charset(Charset.forName(str));
            return this;
        }

        /* access modifiers changed from: package-private */
        public CharsetEncoder prepareEncoder() {
            CharsetEncoder newEncoder = this.charset.newEncoder();
            this.encoderThreadLocal.set(newEncoder);
            this.coreCharset = Entities.CoreCharset.byName(newEncoder.charset().name());
            return newEncoder;
        }

        /* access modifiers changed from: package-private */
        public CharsetEncoder encoder() {
            CharsetEncoder charsetEncoder = this.encoderThreadLocal.get();
            return charsetEncoder != null ? charsetEncoder : prepareEncoder();
        }

        public Syntax syntax() {
            return this.syntax;
        }

        public OutputSettings syntax(Syntax syntax2) {
            this.syntax = syntax2;
            return this;
        }

        public boolean prettyPrint() {
            return this.prettyPrint;
        }

        public OutputSettings prettyPrint(boolean z) {
            this.prettyPrint = z;
            return this;
        }

        public boolean outline() {
            return this.outline;
        }

        public OutputSettings outline(boolean z) {
            this.outline = z;
            return this;
        }

        public int indentAmount() {
            return this.indentAmount;
        }

        public OutputSettings indentAmount(int i) {
            Validate.isTrue(i >= 0);
            this.indentAmount = i;
            return this;
        }

        public int maxPaddingWidth() {
            return this.maxPaddingWidth;
        }

        public OutputSettings maxPaddingWidth(int i) {
            Validate.isTrue(i >= -1);
            this.maxPaddingWidth = i;
            return this;
        }

        public OutputSettings clone() {
            try {
                OutputSettings outputSettings = (OutputSettings) super.clone();
                outputSettings.charset(this.charset.name());
                outputSettings.escapeMode = Entities.EscapeMode.valueOf(this.escapeMode.name());
                return outputSettings;
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public OutputSettings outputSettings() {
        return this.outputSettings;
    }

    public Document outputSettings(OutputSettings outputSettings2) {
        Validate.notNull(outputSettings2);
        this.outputSettings = outputSettings2;
        return this;
    }

    public QuirksMode quirksMode() {
        return this.quirksMode;
    }

    public Document quirksMode(QuirksMode quirksMode2) {
        this.quirksMode = quirksMode2;
        return this;
    }

    public Parser parser() {
        return this.parser;
    }

    public Document parser(Parser parser2) {
        this.parser = parser2;
        return this;
    }

    public Document connection(Connection connection2) {
        Validate.notNull(connection2);
        this.connection = connection2;
        return this;
    }
}
