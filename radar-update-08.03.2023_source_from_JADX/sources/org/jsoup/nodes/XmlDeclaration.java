package org.jsoup.nodes;

import com.j256.ormlite.stmt.query.SimpleComparison;
import java.io.IOException;
import java.util.Iterator;
import kotlin.text.Typography;
import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;

public class XmlDeclaration extends LeafNode {
    private final boolean isProcessingInstruction;

    public String nodeName() {
        return "#declaration";
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

    public XmlDeclaration(String str, boolean z) {
        Validate.notNull(str);
        this.value = str;
        this.isProcessingInstruction = z;
    }

    public String name() {
        return coreValue();
    }

    public String getWholeDeclaration() {
        StringBuilder borrowBuilder = StringUtil.borrowBuilder();
        try {
            getWholeDeclaration(borrowBuilder, new Document.OutputSettings());
            return StringUtil.releaseBuilder(borrowBuilder).trim();
        } catch (IOException e) {
            throw new SerializationException((Throwable) e);
        }
    }

    private void getWholeDeclaration(Appendable appendable, Document.OutputSettings outputSettings) throws IOException {
        Iterator<Attribute> it = attributes().iterator();
        while (it.hasNext()) {
            Attribute next = it.next();
            String key = next.getKey();
            String value = next.getValue();
            if (!key.equals(nodeName())) {
                appendable.append(' ');
                appendable.append(key);
                if (!value.isEmpty()) {
                    appendable.append("=\"");
                    Entities.escape(appendable, value, outputSettings, true, false, false, false);
                    appendable.append(Typography.quote);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void outerHtmlHead(Appendable appendable, int i, Document.OutputSettings outputSettings) throws IOException {
        String str = "!";
        appendable.append(SimpleComparison.LESS_THAN_OPERATION).append(this.isProcessingInstruction ? str : "?").append(coreValue());
        getWholeDeclaration(appendable, outputSettings);
        if (!this.isProcessingInstruction) {
            str = "?";
        }
        appendable.append(str).append(SimpleComparison.GREATER_THAN_OPERATION);
    }

    public String toString() {
        return outerHtml();
    }

    public XmlDeclaration clone() {
        return (XmlDeclaration) super.clone();
    }
}
