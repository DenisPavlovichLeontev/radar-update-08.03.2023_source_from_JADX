package org.jsoup.select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.annotation.Nullable;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Element;

public abstract class CombiningEvaluator extends Evaluator {
    final ArrayList<Evaluator> evaluators;
    int num;

    CombiningEvaluator() {
        this.num = 0;
        this.evaluators = new ArrayList<>();
    }

    CombiningEvaluator(Collection<Evaluator> collection) {
        this();
        this.evaluators.addAll(collection);
        updateNumEvaluators();
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public Evaluator rightMostEvaluator() {
        int i = this.num;
        if (i > 0) {
            return this.evaluators.get(i - 1);
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void replaceRightMostEvaluator(Evaluator evaluator) {
        this.evaluators.set(this.num - 1, evaluator);
    }

    /* access modifiers changed from: package-private */
    public void updateNumEvaluators() {
        this.num = this.evaluators.size();
    }

    public static final class And extends CombiningEvaluator {
        And(Collection<Evaluator> collection) {
            super(collection);
        }

        And(Evaluator... evaluatorArr) {
            this((Collection<Evaluator>) Arrays.asList(evaluatorArr));
        }

        public boolean matches(Element element, Element element2) {
            for (int i = this.num - 1; i >= 0; i--) {
                if (!((Evaluator) this.evaluators.get(i)).matches(element, element2)) {
                    return false;
                }
            }
            return true;
        }

        public String toString() {
            return StringUtil.join((Collection<?>) this.evaluators, "");
        }
    }

    /* renamed from: org.jsoup.select.CombiningEvaluator$Or */
    public static final class C1305Or extends CombiningEvaluator {
        C1305Or(Collection<Evaluator> collection) {
            if (this.num > 1) {
                this.evaluators.add(new And(collection));
            } else {
                this.evaluators.addAll(collection);
            }
            updateNumEvaluators();
        }

        C1305Or(Evaluator... evaluatorArr) {
            this((Collection<Evaluator>) Arrays.asList(evaluatorArr));
        }

        C1305Or() {
        }

        public void add(Evaluator evaluator) {
            this.evaluators.add(evaluator);
            updateNumEvaluators();
        }

        public boolean matches(Element element, Element element2) {
            for (int i = 0; i < this.num; i++) {
                if (((Evaluator) this.evaluators.get(i)).matches(element, element2)) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            return StringUtil.join((Collection<?>) this.evaluators, ", ");
        }
    }
}
