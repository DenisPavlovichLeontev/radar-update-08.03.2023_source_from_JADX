package org.jsoup.select;

import com.j256.ormlite.stmt.query.SimpleComparison;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mil.nga.geopackage.property.PropertyConstants;
import org.jsoup.helper.Validate;
import org.jsoup.internal.Normalizer;
import org.jsoup.internal.StringUtil;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.jsoup.select.Selector;
import org.jsoup.select.StructuralEvaluator;

public class QueryParser {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String[] AttributeEvals = {SimpleComparison.EQUAL_TO_OPERATION, "!=", "^=", "$=", "*=", "~="};
    private static final Pattern NTH_AB = Pattern.compile("(([+-])?(\\d+)?)n(\\s*([+-])?\\s*\\d+)?", 2);
    private static final Pattern NTH_B = Pattern.compile("([+-])?(\\d+)");
    private static final String[] combinators = {",", SimpleComparison.GREATER_THAN_OPERATION, "+", "~", " "};
    private final List<Evaluator> evals = new ArrayList();
    private final String query;

    /* renamed from: tq */
    private final TokenQueue f377tq;

    private QueryParser(String str) {
        Validate.notEmpty(str);
        String trim = str.trim();
        this.query = trim;
        this.f377tq = new TokenQueue(trim);
    }

    public static Evaluator parse(String str) {
        try {
            return new QueryParser(str).parse();
        } catch (IllegalArgumentException e) {
            throw new Selector.SelectorParseException(e.getMessage());
        }
    }

    /* access modifiers changed from: package-private */
    public Evaluator parse() {
        this.f377tq.consumeWhitespace();
        if (this.f377tq.matchesAny(combinators)) {
            this.evals.add(new StructuralEvaluator.Root());
            combinator(this.f377tq.consume());
        } else {
            findElements();
        }
        while (!this.f377tq.isEmpty()) {
            boolean consumeWhitespace = this.f377tq.consumeWhitespace();
            if (this.f377tq.matchesAny(combinators)) {
                combinator(this.f377tq.consume());
            } else if (consumeWhitespace) {
                combinator(' ');
            } else {
                findElements();
            }
        }
        if (this.evals.size() == 1) {
            return this.evals.get(0);
        }
        return new CombiningEvaluator.And((Collection<Evaluator>) this.evals);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v2, resolved type: org.jsoup.select.CombiningEvaluator$Or} */
    /* JADX WARNING: type inference failed for: r11v5, types: [org.jsoup.select.CombiningEvaluator$And] */
    /* JADX WARNING: type inference failed for: r11v6, types: [org.jsoup.select.CombiningEvaluator$And] */
    /* JADX WARNING: type inference failed for: r11v7, types: [org.jsoup.select.CombiningEvaluator$And] */
    /* JADX WARNING: type inference failed for: r11v8, types: [org.jsoup.select.CombiningEvaluator$And] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x0046  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00ab  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00be  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00c5  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void combinator(char r11) {
        /*
            r10 = this;
            org.jsoup.parser.TokenQueue r0 = r10.f377tq
            r0.consumeWhitespace()
            java.lang.String r0 = r10.consumeSubQuery()
            org.jsoup.select.Evaluator r0 = parse(r0)
            java.util.List<org.jsoup.select.Evaluator> r1 = r10.evals
            int r1 = r1.size()
            r2 = 44
            r3 = 1
            r4 = 0
            if (r1 != r3) goto L_0x0033
            java.util.List<org.jsoup.select.Evaluator> r1 = r10.evals
            java.lang.Object r1 = r1.get(r4)
            org.jsoup.select.Evaluator r1 = (org.jsoup.select.Evaluator) r1
            boolean r5 = r1 instanceof org.jsoup.select.CombiningEvaluator.C1305Or
            if (r5 == 0) goto L_0x003a
            if (r11 == r2) goto L_0x003a
            r5 = r1
            org.jsoup.select.CombiningEvaluator$Or r5 = (org.jsoup.select.CombiningEvaluator.C1305Or) r5
            org.jsoup.select.Evaluator r5 = r5.rightMostEvaluator()
            r6 = r3
            r9 = r5
            r5 = r1
            r1 = r9
            goto L_0x003c
        L_0x0033:
            org.jsoup.select.CombiningEvaluator$And r1 = new org.jsoup.select.CombiningEvaluator$And
            java.util.List<org.jsoup.select.Evaluator> r5 = r10.evals
            r1.<init>((java.util.Collection<org.jsoup.select.Evaluator>) r5)
        L_0x003a:
            r5 = r1
            r6 = r4
        L_0x003c:
            java.util.List<org.jsoup.select.Evaluator> r7 = r10.evals
            r7.clear()
            r7 = 32
            r8 = 2
            if (r11 == r7) goto L_0x00ab
            r7 = 62
            if (r11 == r7) goto L_0x009a
            r7 = 126(0x7e, float:1.77E-43)
            if (r11 == r7) goto L_0x0089
            r7 = 43
            if (r11 == r7) goto L_0x0078
            if (r11 != r2) goto L_0x0068
            boolean r11 = r1 instanceof org.jsoup.select.CombiningEvaluator.C1305Or
            if (r11 == 0) goto L_0x005b
            org.jsoup.select.CombiningEvaluator$Or r1 = (org.jsoup.select.CombiningEvaluator.C1305Or) r1
            goto L_0x0064
        L_0x005b:
            org.jsoup.select.CombiningEvaluator$Or r11 = new org.jsoup.select.CombiningEvaluator$Or
            r11.<init>()
            r11.add(r1)
            r1 = r11
        L_0x0064:
            r1.add(r0)
            goto L_0x00bc
        L_0x0068:
            org.jsoup.select.Selector$SelectorParseException r0 = new org.jsoup.select.Selector$SelectorParseException
            java.lang.Object[] r1 = new java.lang.Object[r3]
            java.lang.Character r11 = java.lang.Character.valueOf(r11)
            r1[r4] = r11
            java.lang.String r11 = "Unknown combinator '%s'"
            r0.<init>(r11, r1)
            throw r0
        L_0x0078:
            org.jsoup.select.CombiningEvaluator$And r11 = new org.jsoup.select.CombiningEvaluator$And
            org.jsoup.select.Evaluator[] r2 = new org.jsoup.select.Evaluator[r8]
            org.jsoup.select.StructuralEvaluator$ImmediatePreviousSibling r7 = new org.jsoup.select.StructuralEvaluator$ImmediatePreviousSibling
            r7.<init>(r1)
            r2[r4] = r7
            r2[r3] = r0
            r11.<init>((org.jsoup.select.Evaluator[]) r2)
            goto L_0x00bb
        L_0x0089:
            org.jsoup.select.CombiningEvaluator$And r11 = new org.jsoup.select.CombiningEvaluator$And
            org.jsoup.select.Evaluator[] r2 = new org.jsoup.select.Evaluator[r8]
            org.jsoup.select.StructuralEvaluator$PreviousSibling r7 = new org.jsoup.select.StructuralEvaluator$PreviousSibling
            r7.<init>(r1)
            r2[r4] = r7
            r2[r3] = r0
            r11.<init>((org.jsoup.select.Evaluator[]) r2)
            goto L_0x00bb
        L_0x009a:
            org.jsoup.select.CombiningEvaluator$And r11 = new org.jsoup.select.CombiningEvaluator$And
            org.jsoup.select.Evaluator[] r2 = new org.jsoup.select.Evaluator[r8]
            org.jsoup.select.StructuralEvaluator$ImmediateParent r7 = new org.jsoup.select.StructuralEvaluator$ImmediateParent
            r7.<init>(r1)
            r2[r4] = r7
            r2[r3] = r0
            r11.<init>((org.jsoup.select.Evaluator[]) r2)
            goto L_0x00bb
        L_0x00ab:
            org.jsoup.select.CombiningEvaluator$And r11 = new org.jsoup.select.CombiningEvaluator$And
            org.jsoup.select.Evaluator[] r2 = new org.jsoup.select.Evaluator[r8]
            org.jsoup.select.StructuralEvaluator$Parent r7 = new org.jsoup.select.StructuralEvaluator$Parent
            r7.<init>(r1)
            r2[r4] = r7
            r2[r3] = r0
            r11.<init>((org.jsoup.select.Evaluator[]) r2)
        L_0x00bb:
            r1 = r11
        L_0x00bc:
            if (r6 == 0) goto L_0x00c5
            r11 = r5
            org.jsoup.select.CombiningEvaluator$Or r11 = (org.jsoup.select.CombiningEvaluator.C1305Or) r11
            r11.replaceRightMostEvaluator(r1)
            goto L_0x00c6
        L_0x00c5:
            r5 = r1
        L_0x00c6:
            java.util.List<org.jsoup.select.Evaluator> r11 = r10.evals
            r11.add(r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jsoup.select.QueryParser.combinator(char):void");
    }

    private String consumeSubQuery() {
        StringBuilder borrowBuilder = StringUtil.borrowBuilder();
        while (!this.f377tq.isEmpty()) {
            if (this.f377tq.matches("(")) {
                borrowBuilder.append("(");
                borrowBuilder.append(this.f377tq.chompBalanced('(', ')'));
                borrowBuilder.append(")");
            } else if (this.f377tq.matches("[")) {
                borrowBuilder.append("[");
                borrowBuilder.append(this.f377tq.chompBalanced('[', ']'));
                borrowBuilder.append("]");
            } else if (!this.f377tq.matchesAny(combinators)) {
                borrowBuilder.append(this.f377tq.consume());
            } else if (borrowBuilder.length() > 0) {
                break;
            } else {
                this.f377tq.consume();
            }
        }
        return StringUtil.releaseBuilder(borrowBuilder);
    }

    private void findElements() {
        if (this.f377tq.matchChomp("#")) {
            byId();
        } else if (this.f377tq.matchChomp(PropertyConstants.PROPERTY_DIVIDER)) {
            byClass();
        } else if (this.f377tq.matchesWord() || this.f377tq.matches("*|")) {
            byTag();
        } else if (this.f377tq.matches("[")) {
            byAttribute();
        } else if (this.f377tq.matchChomp("*")) {
            allElements();
        } else if (this.f377tq.matchChomp(":lt(")) {
            indexLessThan();
        } else if (this.f377tq.matchChomp(":gt(")) {
            indexGreaterThan();
        } else if (this.f377tq.matchChomp(":eq(")) {
            indexEquals();
        } else if (this.f377tq.matches(":has(")) {
            has();
        } else if (this.f377tq.matches(":contains(")) {
            contains(false);
        } else if (this.f377tq.matches(":containsOwn(")) {
            contains(true);
        } else if (this.f377tq.matches(":containsWholeText(")) {
            containsWholeText(false);
        } else if (this.f377tq.matches(":containsWholeOwnText(")) {
            containsWholeText(true);
        } else if (this.f377tq.matches(":containsData(")) {
            containsData();
        } else if (this.f377tq.matches(":matches(")) {
            matches(false);
        } else if (this.f377tq.matches(":matchesOwn(")) {
            matches(true);
        } else if (this.f377tq.matches(":matchesWholeText(")) {
            matchesWholeText(false);
        } else if (this.f377tq.matches(":matchesWholeOwnText(")) {
            matchesWholeText(true);
        } else if (this.f377tq.matches(":not(")) {
            not();
        } else if (this.f377tq.matchChomp(":nth-child(")) {
            cssNthChild(false, false);
        } else if (this.f377tq.matchChomp(":nth-last-child(")) {
            cssNthChild(true, false);
        } else if (this.f377tq.matchChomp(":nth-of-type(")) {
            cssNthChild(false, true);
        } else if (this.f377tq.matchChomp(":nth-last-of-type(")) {
            cssNthChild(true, true);
        } else if (this.f377tq.matchChomp(":first-child")) {
            this.evals.add(new Evaluator.IsFirstChild());
        } else if (this.f377tq.matchChomp(":last-child")) {
            this.evals.add(new Evaluator.IsLastChild());
        } else if (this.f377tq.matchChomp(":first-of-type")) {
            this.evals.add(new Evaluator.IsFirstOfType());
        } else if (this.f377tq.matchChomp(":last-of-type")) {
            this.evals.add(new Evaluator.IsLastOfType());
        } else if (this.f377tq.matchChomp(":only-child")) {
            this.evals.add(new Evaluator.IsOnlyChild());
        } else if (this.f377tq.matchChomp(":only-of-type")) {
            this.evals.add(new Evaluator.IsOnlyOfType());
        } else if (this.f377tq.matchChomp(":empty")) {
            this.evals.add(new Evaluator.IsEmpty());
        } else if (this.f377tq.matchChomp(":root")) {
            this.evals.add(new Evaluator.IsRoot());
        } else if (this.f377tq.matchChomp(":matchText")) {
            this.evals.add(new Evaluator.MatchText());
        } else {
            throw new Selector.SelectorParseException("Could not parse query '%s': unexpected token at '%s'", this.query, this.f377tq.remainder());
        }
    }

    private void byId() {
        String consumeCssIdentifier = this.f377tq.consumeCssIdentifier();
        Validate.notEmpty(consumeCssIdentifier);
        this.evals.add(new Evaluator.C1306Id(consumeCssIdentifier));
    }

    private void byClass() {
        String consumeCssIdentifier = this.f377tq.consumeCssIdentifier();
        Validate.notEmpty(consumeCssIdentifier);
        this.evals.add(new Evaluator.Class(consumeCssIdentifier.trim()));
    }

    private void byTag() {
        String normalize = Normalizer.normalize(this.f377tq.consumeElementSelector());
        Validate.notEmpty(normalize);
        if (normalize.startsWith("*|")) {
            String substring = normalize.substring(2);
            this.evals.add(new CombiningEvaluator.C1305Or(new Evaluator.Tag(substring), new Evaluator.TagEndsWith(normalize.replace("*|", ":"))));
            return;
        }
        if (normalize.contains("|")) {
            normalize = normalize.replace("|", ":");
        }
        this.evals.add(new Evaluator.Tag(normalize));
    }

    private void byAttribute() {
        TokenQueue tokenQueue = new TokenQueue(this.f377tq.chompBalanced('[', ']'));
        String consumeToAny = tokenQueue.consumeToAny(AttributeEvals);
        Validate.notEmpty(consumeToAny);
        tokenQueue.consumeWhitespace();
        if (tokenQueue.isEmpty()) {
            if (consumeToAny.startsWith("^")) {
                this.evals.add(new Evaluator.AttributeStarting(consumeToAny.substring(1)));
            } else {
                this.evals.add(new Evaluator.Attribute(consumeToAny));
            }
        } else if (tokenQueue.matchChomp(SimpleComparison.EQUAL_TO_OPERATION)) {
            this.evals.add(new Evaluator.AttributeWithValue(consumeToAny, tokenQueue.remainder()));
        } else if (tokenQueue.matchChomp("!=")) {
            this.evals.add(new Evaluator.AttributeWithValueNot(consumeToAny, tokenQueue.remainder()));
        } else if (tokenQueue.matchChomp("^=")) {
            this.evals.add(new Evaluator.AttributeWithValueStarting(consumeToAny, tokenQueue.remainder()));
        } else if (tokenQueue.matchChomp("$=")) {
            this.evals.add(new Evaluator.AttributeWithValueEnding(consumeToAny, tokenQueue.remainder()));
        } else if (tokenQueue.matchChomp("*=")) {
            this.evals.add(new Evaluator.AttributeWithValueContaining(consumeToAny, tokenQueue.remainder()));
        } else if (tokenQueue.matchChomp("~=")) {
            this.evals.add(new Evaluator.AttributeWithValueMatching(consumeToAny, Pattern.compile(tokenQueue.remainder())));
        } else {
            throw new Selector.SelectorParseException("Could not parse attribute query '%s': unexpected token at '%s'", this.query, tokenQueue.remainder());
        }
    }

    private void allElements() {
        this.evals.add(new Evaluator.AllElements());
    }

    private void indexLessThan() {
        this.evals.add(new Evaluator.IndexLessThan(consumeIndex()));
    }

    private void indexGreaterThan() {
        this.evals.add(new Evaluator.IndexGreaterThan(consumeIndex()));
    }

    private void indexEquals() {
        this.evals.add(new Evaluator.IndexEquals(consumeIndex()));
    }

    private void cssNthChild(boolean z, boolean z2) {
        String normalize = Normalizer.normalize(this.f377tq.chompTo(")"));
        Matcher matcher = NTH_AB.matcher(normalize);
        Matcher matcher2 = NTH_B.matcher(normalize);
        int i = 2;
        int i2 = 0;
        if ("odd".equals(normalize)) {
            i2 = 1;
        } else if (!"even".equals(normalize)) {
            if (matcher.matches()) {
                int parseInt = matcher.group(3) != null ? Integer.parseInt(matcher.group(1).replaceFirst("^\\+", "")) : 1;
                if (matcher.group(4) != null) {
                    i2 = Integer.parseInt(matcher.group(4).replaceFirst("^\\+", ""));
                }
                i = parseInt;
            } else if (matcher2.matches()) {
                i = 0;
                i2 = Integer.parseInt(matcher2.group().replaceFirst("^\\+", ""));
            } else {
                throw new Selector.SelectorParseException("Could not parse nth-index '%s': unexpected format", normalize);
            }
        }
        if (z2) {
            if (z) {
                this.evals.add(new Evaluator.IsNthLastOfType(i, i2));
            } else {
                this.evals.add(new Evaluator.IsNthOfType(i, i2));
            }
        } else if (z) {
            this.evals.add(new Evaluator.IsNthLastChild(i, i2));
        } else {
            this.evals.add(new Evaluator.IsNthChild(i, i2));
        }
    }

    private int consumeIndex() {
        String trim = this.f377tq.chompTo(")").trim();
        Validate.isTrue(StringUtil.isNumeric(trim), "Index must be numeric");
        return Integer.parseInt(trim);
    }

    private void has() {
        this.f377tq.consume(":has");
        String chompBalanced = this.f377tq.chompBalanced('(', ')');
        Validate.notEmpty(chompBalanced, ":has(selector) sub-select must not be empty");
        this.evals.add(new StructuralEvaluator.Has(parse(chompBalanced)));
    }

    private void contains(boolean z) {
        Object obj;
        String str = z ? ":containsOwn" : ":contains";
        this.f377tq.consume(str);
        String unescape = TokenQueue.unescape(this.f377tq.chompBalanced('(', ')'));
        Validate.notEmpty(unescape, str + "(text) query must not be empty");
        List<Evaluator> list = this.evals;
        if (z) {
            obj = new Evaluator.ContainsOwnText(unescape);
        } else {
            obj = new Evaluator.ContainsText(unescape);
        }
        list.add(obj);
    }

    private void containsWholeText(boolean z) {
        Object obj;
        String str = z ? ":containsWholeOwnText" : ":containsWholeText";
        this.f377tq.consume(str);
        String unescape = TokenQueue.unescape(this.f377tq.chompBalanced('(', ')'));
        Validate.notEmpty(unescape, str + "(text) query must not be empty");
        List<Evaluator> list = this.evals;
        if (z) {
            obj = new Evaluator.ContainsWholeOwnText(unescape);
        } else {
            obj = new Evaluator.ContainsWholeText(unescape);
        }
        list.add(obj);
    }

    private void containsData() {
        this.f377tq.consume(":containsData");
        String unescape = TokenQueue.unescape(this.f377tq.chompBalanced('(', ')'));
        Validate.notEmpty(unescape, ":containsData(text) query must not be empty");
        this.evals.add(new Evaluator.ContainsData(unescape));
    }

    private void matches(boolean z) {
        Object obj;
        String str = z ? ":matchesOwn" : ":matches";
        this.f377tq.consume(str);
        String chompBalanced = this.f377tq.chompBalanced('(', ')');
        Validate.notEmpty(chompBalanced, str + "(regex) query must not be empty");
        List<Evaluator> list = this.evals;
        if (z) {
            obj = new Evaluator.MatchesOwn(Pattern.compile(chompBalanced));
        } else {
            obj = new Evaluator.Matches(Pattern.compile(chompBalanced));
        }
        list.add(obj);
    }

    private void matchesWholeText(boolean z) {
        Object obj;
        String str = z ? ":matchesWholeOwnText" : ":matchesWholeText";
        this.f377tq.consume(str);
        String chompBalanced = this.f377tq.chompBalanced('(', ')');
        Validate.notEmpty(chompBalanced, str + "(regex) query must not be empty");
        List<Evaluator> list = this.evals;
        if (z) {
            obj = new Evaluator.MatchesWholeOwnText(Pattern.compile(chompBalanced));
        } else {
            obj = new Evaluator.MatchesWholeText(Pattern.compile(chompBalanced));
        }
        list.add(obj);
    }

    private void not() {
        this.f377tq.consume(":not");
        String chompBalanced = this.f377tq.chompBalanced('(', ')');
        Validate.notEmpty(chompBalanced, ":not(selector) subselect must not be empty");
        this.evals.add(new StructuralEvaluator.Not(parse(chompBalanced)));
    }

    public String toString() {
        return this.query;
    }
}
