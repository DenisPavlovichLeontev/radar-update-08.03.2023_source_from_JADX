package org.jsoup.select;

import com.android.tools.r8.annotations.SynthesizedClassV2;
import org.jsoup.nodes.Node;

public interface NodeFilter {

    public enum FilterResult {
        CONTINUE,
        SKIP_CHILDREN,
        SKIP_ENTIRELY,
        REMOVE,
        STOP
    }

    FilterResult head(Node node, int i);

    FilterResult tail(Node node, int i);

    @SynthesizedClassV2(kind = 7, versionHash = "5e5398f0546d1d7afd62641edb14d82894f11ddc41bce363a0c8d0dac82c9c5a")
    /* renamed from: org.jsoup.select.NodeFilter$-CC  reason: invalid class name */
    public final /* synthetic */ class CC {
        public static FilterResult $default$tail(NodeFilter _this, Node node, int i) {
            return FilterResult.CONTINUE;
        }
    }
}
