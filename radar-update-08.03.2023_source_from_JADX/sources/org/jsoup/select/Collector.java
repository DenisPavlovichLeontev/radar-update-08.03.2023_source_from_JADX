package org.jsoup.select;

import javax.annotation.Nullable;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeFilter;

public class Collector {
    private Collector() {
    }

    public static Elements collect(Evaluator evaluator, Element element) {
        Elements elements = new Elements();
        NodeTraversor.traverse((NodeVisitor) new Accumulator(element, elements, evaluator), (Node) element);
        return elements;
    }

    private static class Accumulator implements NodeVisitor {
        private final Elements elements;
        private final Evaluator eval;
        private final Element root;

        public void tail(Node node, int i) {
        }

        Accumulator(Element element, Elements elements2, Evaluator evaluator) {
            this.root = element;
            this.elements = elements2;
            this.eval = evaluator;
        }

        public void head(Node node, int i) {
            if (node instanceof Element) {
                Element element = (Element) node;
                if (this.eval.matches(this.root, element)) {
                    this.elements.add(element);
                }
            }
        }
    }

    @Nullable
    public static Element findFirst(Evaluator evaluator, Element element) {
        return new FirstFinder(evaluator).find(element, element);
    }

    static class FirstFinder implements NodeFilter {
        private final Evaluator eval;
        @Nullable
        private Element evalRoot = null;
        @Nullable
        private Element match = null;

        FirstFinder(Evaluator evaluator) {
            this.eval = evaluator;
        }

        /* access modifiers changed from: package-private */
        @Nullable
        public Element find(Element element, Element element2) {
            this.evalRoot = element;
            this.match = null;
            NodeTraversor.filter((NodeFilter) this, (Node) element2);
            return this.match;
        }

        public NodeFilter.FilterResult head(Node node, int i) {
            if (node instanceof Element) {
                Element element = (Element) node;
                if (this.eval.matches(this.evalRoot, element)) {
                    this.match = element;
                    return NodeFilter.FilterResult.STOP;
                }
            }
            return NodeFilter.FilterResult.CONTINUE;
        }

        public NodeFilter.FilterResult tail(Node node, int i) {
            return NodeFilter.FilterResult.CONTINUE;
        }
    }
}
