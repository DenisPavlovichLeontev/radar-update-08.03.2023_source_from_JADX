package org.jsoup.select;

import com.android.tools.r8.annotations.SynthesizedClassV2;
import org.jsoup.nodes.Node;

public interface NodeVisitor {

    @SynthesizedClassV2(kind = 7, versionHash = "5e5398f0546d1d7afd62641edb14d82894f11ddc41bce363a0c8d0dac82c9c5a")
    /* renamed from: org.jsoup.select.NodeVisitor$-CC  reason: invalid class name */
    public final /* synthetic */ class CC {
        public static void $default$tail(NodeVisitor _this, Node node, int i) {
        }
    }

    void head(Node node, int i);

    void tail(Node node, int i);
}
