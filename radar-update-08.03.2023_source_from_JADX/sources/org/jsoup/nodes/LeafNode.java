package org.jsoup.nodes;

import java.util.List;

abstract class LeafNode extends Node {
    Object value;

    public int childNodeSize() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public void doSetBaseUri(String str) {
    }

    public Node empty() {
        return this;
    }

    LeafNode() {
    }

    /* access modifiers changed from: protected */
    public final boolean hasAttributes() {
        return this.value instanceof Attributes;
    }

    public final Attributes attributes() {
        ensureAttributes();
        return (Attributes) this.value;
    }

    private void ensureAttributes() {
        if (!hasAttributes()) {
            Object obj = this.value;
            Attributes attributes = new Attributes();
            this.value = attributes;
            if (obj != null) {
                attributes.put(nodeName(), (String) obj);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public String coreValue() {
        return attr(nodeName());
    }

    /* access modifiers changed from: package-private */
    public void coreValue(String str) {
        attr(nodeName(), str);
    }

    public String attr(String str) {
        if (!hasAttributes()) {
            return nodeName().equals(str) ? (String) this.value : "";
        }
        return super.attr(str);
    }

    public Node attr(String str, String str2) {
        if (hasAttributes() || !str.equals(nodeName())) {
            ensureAttributes();
            super.attr(str, str2);
        } else {
            this.value = str2;
        }
        return this;
    }

    public boolean hasAttr(String str) {
        ensureAttributes();
        return super.hasAttr(str);
    }

    public Node removeAttr(String str) {
        ensureAttributes();
        return super.removeAttr(str);
    }

    public String absUrl(String str) {
        ensureAttributes();
        return super.absUrl(str);
    }

    public String baseUri() {
        return hasParent() ? parent().baseUri() : "";
    }

    /* access modifiers changed from: protected */
    public List<Node> ensureChildNodes() {
        return EmptyNodes;
    }

    /* access modifiers changed from: protected */
    public LeafNode doClone(Node node) {
        LeafNode leafNode = (LeafNode) super.doClone(node);
        if (hasAttributes()) {
            leafNode.value = ((Attributes) this.value).clone();
        }
        return leafNode;
    }
}
