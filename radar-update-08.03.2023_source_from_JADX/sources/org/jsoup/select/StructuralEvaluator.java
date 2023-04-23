package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Collector;

abstract class StructuralEvaluator extends Evaluator {
    Evaluator evaluator;

    StructuralEvaluator() {
    }

    static class Root extends Evaluator {
        public boolean matches(Element element, Element element2) {
            return element == element2;
        }

        Root() {
        }
    }

    static class Has extends StructuralEvaluator {
        final Collector.FirstFinder finder;

        public Has(Evaluator evaluator) {
            this.evaluator = evaluator;
            this.finder = new Collector.FirstFinder(evaluator);
        }

        public boolean matches(Element element, Element element2) {
            for (int i = 0; i < element2.childNodeSize(); i++) {
                Node childNode = element2.childNode(i);
                if ((childNode instanceof Element) && this.finder.find(element2, (Element) childNode) != null) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            return String.format(":has(%s)", new Object[]{this.evaluator});
        }
    }

    static class Not extends StructuralEvaluator {
        public Not(Evaluator evaluator) {
            this.evaluator = evaluator;
        }

        public boolean matches(Element element, Element element2) {
            return !this.evaluator.matches(element, element2);
        }

        public String toString() {
            return String.format(":not(%s)", new Object[]{this.evaluator});
        }
    }

    static class Parent extends StructuralEvaluator {
        public Parent(Evaluator evaluator) {
            this.evaluator = evaluator;
        }

        public boolean matches(Element element, Element element2) {
            if (element == element2) {
                return false;
            }
            for (Element parent = element2.parent(); parent != null; parent = parent.parent()) {
                if (this.evaluator.matches(element, parent)) {
                    return true;
                }
                if (parent == element) {
                    break;
                }
            }
            return false;
        }

        public String toString() {
            return String.format("%s ", new Object[]{this.evaluator});
        }
    }

    static class ImmediateParent extends StructuralEvaluator {
        public ImmediateParent(Evaluator evaluator) {
            this.evaluator = evaluator;
        }

        public boolean matches(Element element, Element element2) {
            Element parent;
            if (element == element2 || (parent = element2.parent()) == null || !this.evaluator.matches(element, parent)) {
                return false;
            }
            return true;
        }

        public String toString() {
            return String.format("%s > ", new Object[]{this.evaluator});
        }
    }

    static class PreviousSibling extends StructuralEvaluator {
        public PreviousSibling(Evaluator evaluator) {
            this.evaluator = evaluator;
        }

        public boolean matches(Element element, Element element2) {
            if (element == element2) {
                return false;
            }
            for (Element previousElementSibling = element2.previousElementSibling(); previousElementSibling != null; previousElementSibling = previousElementSibling.previousElementSibling()) {
                if (this.evaluator.matches(element, previousElementSibling)) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            return String.format("%s ~ ", new Object[]{this.evaluator});
        }
    }

    static class ImmediatePreviousSibling extends StructuralEvaluator {
        public ImmediatePreviousSibling(Evaluator evaluator) {
            this.evaluator = evaluator;
        }

        public boolean matches(Element element, Element element2) {
            Element previousElementSibling;
            if (element == element2 || (previousElementSibling = element2.previousElementSibling()) == null || !this.evaluator.matches(element, previousElementSibling)) {
                return false;
            }
            return true;
        }

        public String toString() {
            return String.format("%s + ", new Object[]{this.evaluator});
        }
    }
}
