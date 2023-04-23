package org.jsoup.nodes;

import org.jsoup.select.NodeVisitor;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Element$$ExternalSyntheticLambda0 implements NodeVisitor {
    public final /* synthetic */ StringBuilder f$0;

    public /* synthetic */ Element$$ExternalSyntheticLambda0(StringBuilder sb) {
        this.f$0 = sb;
    }

    public final void head(Node node, int i) {
        Element.appendWholeText(node, this.f$0);
    }

    public /* synthetic */ void tail(Node node, int i) {
        NodeVisitor.CC.$default$tail(this, node, i);
    }
}
