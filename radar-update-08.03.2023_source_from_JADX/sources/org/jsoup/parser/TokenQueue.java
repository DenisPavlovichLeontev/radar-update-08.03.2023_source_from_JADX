package org.jsoup.parser;

import mil.nga.geopackage.extension.Extensions;
import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;

public class TokenQueue {
    private static final char ESC = '\\';
    private int pos = 0;
    private String queue;

    public TokenQueue(String str) {
        Validate.notNull(str);
        this.queue = str;
    }

    public boolean isEmpty() {
        return remainingLength() == 0;
    }

    private int remainingLength() {
        return this.queue.length() - this.pos;
    }

    public void addFirst(String str) {
        this.queue = str + this.queue.substring(this.pos);
        this.pos = 0;
    }

    public boolean matches(String str) {
        return this.queue.regionMatches(true, this.pos, str, 0, str.length());
    }

    public boolean matchesAny(String... strArr) {
        for (String matches : strArr) {
            if (matches(matches)) {
                return true;
            }
        }
        return false;
    }

    public boolean matchesAny(char... cArr) {
        if (isEmpty()) {
            return false;
        }
        for (char c : cArr) {
            if (this.queue.charAt(this.pos) == c) {
                return true;
            }
        }
        return false;
    }

    public boolean matchChomp(String str) {
        if (!matches(str)) {
            return false;
        }
        this.pos += str.length();
        return true;
    }

    public boolean matchesWhitespace() {
        return !isEmpty() && StringUtil.isWhitespace(this.queue.charAt(this.pos));
    }

    public boolean matchesWord() {
        return !isEmpty() && Character.isLetterOrDigit(this.queue.charAt(this.pos));
    }

    public void advance() {
        if (!isEmpty()) {
            this.pos++;
        }
    }

    public char consume() {
        String str = this.queue;
        int i = this.pos;
        this.pos = i + 1;
        return str.charAt(i);
    }

    public void consume(String str) {
        if (matches(str)) {
            int length = str.length();
            if (length <= remainingLength()) {
                this.pos += length;
                return;
            }
            throw new IllegalStateException("Queue not long enough to consume sequence");
        }
        throw new IllegalStateException("Queue did not match expected sequence");
    }

    public String consumeTo(String str) {
        int indexOf = this.queue.indexOf(str, this.pos);
        if (indexOf == -1) {
            return remainder();
        }
        String substring = this.queue.substring(this.pos, indexOf);
        this.pos += substring.length();
        return substring;
    }

    public String consumeToIgnoreCase(String str) {
        int i = this.pos;
        String substring = str.substring(0, 1);
        boolean equals = substring.toLowerCase().equals(substring.toUpperCase());
        while (!isEmpty() && !matches(str)) {
            if (equals) {
                int indexOf = this.queue.indexOf(substring, this.pos);
                int i2 = this.pos;
                int i3 = indexOf - i2;
                if (i3 == 0) {
                    this.pos = i2 + 1;
                } else if (i3 < 0) {
                    this.pos = this.queue.length();
                } else {
                    this.pos = i2 + i3;
                }
            } else {
                this.pos++;
            }
        }
        return this.queue.substring(i, this.pos);
    }

    public String consumeToAny(String... strArr) {
        int i = this.pos;
        while (!isEmpty() && !matchesAny(strArr)) {
            this.pos++;
        }
        return this.queue.substring(i, this.pos);
    }

    public String chompTo(String str) {
        String consumeTo = consumeTo(str);
        matchChomp(str);
        return consumeTo;
    }

    public String chompToIgnoreCase(String str) {
        String consumeToIgnoreCase = consumeToIgnoreCase(str);
        matchChomp(str);
        return consumeToIgnoreCase;
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x007c A[LOOP:0: B:1:0x0009->B:40:0x007c, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0055 A[EDGE_INSN: B:42:0x0055->B:34:0x0055 ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String chompBalanced(char r12, char r13) {
        /*
            r11 = this;
            r0 = -1
            r1 = 0
            r5 = r0
            r6 = r5
            r2 = r1
            r3 = r2
            r4 = r3
            r7 = r4
            r8 = r7
        L_0x0009:
            boolean r9 = r11.isEmpty()
            if (r9 == 0) goto L_0x0010
            goto L_0x0055
        L_0x0010:
            char r9 = r11.consume()
            r10 = 92
            if (r2 == r10) goto L_0x0042
            r10 = 39
            if (r9 != r10) goto L_0x0023
            if (r9 == r12) goto L_0x0023
            if (r3 != 0) goto L_0x0023
            r4 = r4 ^ 1
            goto L_0x002d
        L_0x0023:
            r10 = 34
            if (r9 != r10) goto L_0x002d
            if (r9 == r12) goto L_0x002d
            if (r4 != 0) goto L_0x002d
            r3 = r3 ^ 1
        L_0x002d:
            if (r4 != 0) goto L_0x0053
            if (r3 != 0) goto L_0x0053
            if (r8 == 0) goto L_0x0034
            goto L_0x0053
        L_0x0034:
            if (r9 != r12) goto L_0x003d
            int r7 = r7 + 1
            if (r5 != r0) goto L_0x004d
            int r5 = r11.pos
            goto L_0x004d
        L_0x003d:
            if (r9 != r13) goto L_0x004d
            int r7 = r7 + -1
            goto L_0x004d
        L_0x0042:
            r10 = 81
            if (r9 != r10) goto L_0x0048
            r8 = 1
            goto L_0x004d
        L_0x0048:
            r10 = 69
            if (r9 != r10) goto L_0x004d
            r8 = r1
        L_0x004d:
            if (r7 <= 0) goto L_0x0053
            if (r2 == 0) goto L_0x0053
            int r6 = r11.pos
        L_0x0053:
            if (r7 > 0) goto L_0x007c
        L_0x0055:
            if (r6 < 0) goto L_0x005e
            java.lang.String r12 = r11.queue
            java.lang.String r12 = r12.substring(r5, r6)
            goto L_0x0060
        L_0x005e:
            java.lang.String r12 = ""
        L_0x0060:
            if (r7 <= 0) goto L_0x007b
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r0 = "Did not find balanced marker at '"
            r13.append(r0)
            r13.append(r12)
            java.lang.String r0 = "'"
            r13.append(r0)
            java.lang.String r13 = r13.toString()
            org.jsoup.helper.Validate.fail(r13)
        L_0x007b:
            return r12
        L_0x007c:
            r2 = r9
            goto L_0x0009
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jsoup.parser.TokenQueue.chompBalanced(char, char):java.lang.String");
    }

    public static String unescape(String str) {
        StringBuilder borrowBuilder = StringUtil.borrowBuilder();
        char[] charArray = str.toCharArray();
        int length = charArray.length;
        int i = 0;
        char c = 0;
        while (i < length) {
            char c2 = charArray[i];
            if (c2 != '\\') {
                borrowBuilder.append(c2);
            } else if (c == '\\') {
                borrowBuilder.append(c2);
            }
            i++;
            c = c2;
        }
        return StringUtil.releaseBuilder(borrowBuilder);
    }

    public boolean consumeWhitespace() {
        boolean z = false;
        while (matchesWhitespace()) {
            this.pos++;
            z = true;
        }
        return z;
    }

    public String consumeWord() {
        int i = this.pos;
        while (matchesWord()) {
            this.pos++;
        }
        return this.queue.substring(i, this.pos);
    }

    public String consumeElementSelector() {
        int i = this.pos;
        while (!isEmpty() && (matchesWord() || matchesAny("*|", "|", Extensions.EXTENSION_NAME_DIVIDER, "-"))) {
            this.pos++;
        }
        return this.queue.substring(i, this.pos);
    }

    public String consumeCssIdentifier() {
        int i = this.pos;
        while (!isEmpty() && (matchesWord() || matchesAny('-', '_'))) {
            this.pos++;
        }
        return this.queue.substring(i, this.pos);
    }

    public String remainder() {
        String substring = this.queue.substring(this.pos);
        this.pos = this.queue.length();
        return substring;
    }

    public String toString() {
        return this.queue.substring(this.pos);
    }
}
