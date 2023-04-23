package org.jsoup.nodes;

import org.jsoup.helper.Consumer;
import org.jsoup.select.NodeVisitor;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Element$$ExternalSyntheticLambda1 implements NodeVisitor {
    public final /* synthetic */ Consumer f$0;

    public /* synthetic */ Element$$ExternalSyntheticLambda1(Consumer consumer) {
        this.f$0 = consumer;
    }

    public final void head(Node node, int i) {
        Element.lambda$forEach$1(this.f$0, node, i);
    }

    public /* synthetic */ void tail(Node node, int i) {
        NodeVisitor.CC.$default$tail(this, node, i);
    }
}
