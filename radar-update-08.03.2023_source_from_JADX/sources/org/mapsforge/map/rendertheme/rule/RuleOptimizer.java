package org.mapsforge.map.rendertheme.rule;

import java.util.Stack;
import java.util.logging.Logger;

final class RuleOptimizer {
    private static final Logger LOGGER = Logger.getLogger(RuleOptimizer.class.getName());

    static AttributeMatcher optimize(AttributeMatcher attributeMatcher, Stack<Rule> stack) {
        if ((attributeMatcher instanceof AnyMatcher) || (attributeMatcher instanceof NegativeMatcher)) {
            return attributeMatcher;
        }
        if (attributeMatcher instanceof KeyMatcher) {
            return optimizeKeyMatcher(attributeMatcher, stack);
        }
        if (attributeMatcher instanceof ValueMatcher) {
            return optimizeValueMatcher(attributeMatcher, stack);
        }
        throw new IllegalArgumentException("unknown AttributeMatcher: " + attributeMatcher);
    }

    static ClosedMatcher optimize(ClosedMatcher closedMatcher, Stack<Rule> stack) {
        if (closedMatcher instanceof AnyMatcher) {
            return closedMatcher;
        }
        int size = stack.size();
        for (int i = 0; i < size; i++) {
            if (((Rule) stack.get(i)).closedMatcher.isCoveredBy(closedMatcher)) {
                return AnyMatcher.INSTANCE;
            }
            if (!closedMatcher.isCoveredBy(((Rule) stack.get(i)).closedMatcher)) {
                LOGGER.warning("unreachable rule (closed)");
            }
        }
        return closedMatcher;
    }

    static ElementMatcher optimize(ElementMatcher elementMatcher, Stack<Rule> stack) {
        if (elementMatcher instanceof AnyMatcher) {
            return elementMatcher;
        }
        int size = stack.size();
        for (int i = 0; i < size; i++) {
            Rule rule = (Rule) stack.get(i);
            if (rule.elementMatcher.isCoveredBy(elementMatcher)) {
                return AnyMatcher.INSTANCE;
            }
            if (!elementMatcher.isCoveredBy(rule.elementMatcher)) {
                LOGGER.warning("unreachable rule (e)");
            }
        }
        return elementMatcher;
    }

    private static AttributeMatcher optimizeKeyMatcher(AttributeMatcher attributeMatcher, Stack<Rule> stack) {
        int size = stack.size();
        for (int i = 0; i < size; i++) {
            if ((stack.get(i) instanceof PositiveRule) && ((PositiveRule) stack.get(i)).keyMatcher.isCoveredBy(attributeMatcher)) {
                return AnyMatcher.INSTANCE;
            }
        }
        return attributeMatcher;
    }

    private static AttributeMatcher optimizeValueMatcher(AttributeMatcher attributeMatcher, Stack<Rule> stack) {
        int size = stack.size();
        for (int i = 0; i < size; i++) {
            if ((stack.get(i) instanceof PositiveRule) && ((PositiveRule) stack.get(i)).valueMatcher.isCoveredBy(attributeMatcher)) {
                return AnyMatcher.INSTANCE;
            }
        }
        return attributeMatcher;
    }

    private RuleOptimizer() {
        throw new IllegalStateException();
    }
}
