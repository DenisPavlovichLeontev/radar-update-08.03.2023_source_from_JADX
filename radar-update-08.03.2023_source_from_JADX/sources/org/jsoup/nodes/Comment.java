package org.jsoup.nodes;

import com.j256.ormlite.stmt.query.SimpleComparison;
import java.io.IOException;
import javax.annotation.Nullable;
import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;

public class Comment extends LeafNode {
    public String nodeName() {
        return "#comment";
    }

    /* access modifiers changed from: package-private */
    public void outerHtmlTail(Appendable appendable, int i, Document.OutputSettings outputSettings) {
    }

    public /* bridge */ /* synthetic */ String absUrl(String str) {
        return super.absUrl(str);
    }

    public /* bridge */ /* synthetic */ String attr(String str) {
        return super.attr(str);
    }

    public /* bridge */ /* synthetic */ Node attr(String str, String str2) {
        return super.attr(str, str2);
    }

    public /* bridge */ /* synthetic */ String baseUri() {
        return super.baseUri();
    }

    public /* bridge */ /* synthetic */ int childNodeSize() {
        return super.childNodeSize();
    }

    public /* bridge */ /* synthetic */ Node empty() {
        return super.empty();
    }

    public /* bridge */ /* synthetic */ boolean hasAttr(String str) {
        return super.hasAttr(str);
    }

    public /* bridge */ /* synthetic */ Node removeAttr(String str) {
        return super.removeAttr(str);
    }

    public Comment(String str) {
        this.value = str;
    }

    public String getData() {
        return coreValue();
    }

    public Comment setData(String str) {
        coreValue(str);
        return this;
    }

    /* access modifiers changed from: package-private */
    public void outerHtmlHead(Appendable appendable, int i, Document.OutputSettings outputSettings) throws IOException {
        if (outputSettings.prettyPrint() && ((siblingIndex() == 0 && (this.parentNode instanceof Element) && ((Element) this.parentNode).tag().formatAsBlock()) || outputSettings.outline())) {
            indent(appendable, i, outputSettings);
        }
        appendable.append("<!--").append(getData()).append("-->");
    }

    public String toString() {
        return outerHtml();
    }

    public Comment clone() {
        return (Comment) super.clone();
    }

    public boolean isXmlDeclaration() {
        return isXmlDeclarationData(getData());
    }

    private static boolean isXmlDeclarationData(String str) {
        return str.length() > 1 && (str.startsWith("!") || str.startsWith("?"));
    }

    @Nullable
    public XmlDeclaration asXmlDeclaration() {
        String data = getData();
        String substring = data.substring(1, data.length() - 1);
        if (isXmlDeclarationData(substring)) {
            return null;
        }
        Document parseInput = Parser.htmlParser().settings(ParseSettings.preserveCase).parseInput(SimpleComparison.LESS_THAN_OPERATION + substring + SimpleComparison.GREATER_THAN_OPERATION, baseUri());
        if (parseInput.body().children().size() <= 0) {
            return null;
        }
        Element child = parseInput.body().child(0);
        XmlDeclaration xmlDeclaration = new XmlDeclaration(NodeUtils.parser(parseInput).settings().normalizeTag(child.tagName()), data.startsWith("!"));
        xmlDeclaration.attributes().addAll(child.attributes());
        return xmlDeclaration;
    }
}
