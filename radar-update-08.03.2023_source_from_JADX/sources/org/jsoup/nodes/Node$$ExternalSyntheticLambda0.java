package org.jsoup.nodes;

import org.jsoup.helper.Consumer;
import org.jsoup.select.NodeVisitor;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Node$$ExternalSyntheticLambda0 implements NodeVisitor {
    public final /* synthetic */ Consumer f$0;

    public /* synthetic */ Node$$ExternalSyntheticLambda0(Consumer consumer) {
        this.f$0 = consumer;
    }

    public final void head(Node node, int i) {
        this.f$0.accept(node);
    }

    public /* synthetic */ void tail(Node node, int i) {
        NodeVisitor.CC.$default$tail(this, node, i);
    }
}
