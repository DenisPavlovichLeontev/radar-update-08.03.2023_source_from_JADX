package org.jsoup.nodes;

import org.jsoup.helper.Validate;

public class Range {
    private static final String EndRangeKey = Attributes.internalKey("jsoup.endSourceRange");
    private static final String RangeKey = Attributes.internalKey("jsoup.sourceRange");
    private static final Range Untracked;
    /* access modifiers changed from: private */
    public static final Position UntrackedPos;
    private final Position end;
    private final Position start;

    static {
        Position position = new Position(-1, -1, -1);
        UntrackedPos = position;
        Untracked = new Range(position, position);
    }

    public Range(Position position, Position position2) {
        this.start = position;
        this.end = position2;
    }

    public Position start() {
        return this.start;
    }

    public Position end() {
        return this.end;
    }

    public boolean isTracked() {
        return this != Untracked;
    }

    /* renamed from: of */
    static Range m151of(Node node, boolean z) {
        String str = z ? RangeKey : EndRangeKey;
        if (!node.hasAttr(str)) {
            return Untracked;
        }
        return (Range) Validate.ensureNotNull(node.attributes().getUserData(str));
    }

    public void track(Node node, boolean z) {
        node.attributes().putUserData(z ? RangeKey : EndRangeKey, this);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Range range = (Range) obj;
        if (!this.start.equals(range.start)) {
            return false;
        }
        return this.end.equals(range.end);
    }

    public int hashCode() {
        return (this.start.hashCode() * 31) + this.end.hashCode();
    }

    public String toString() {
        return this.start + "-" + this.end;
    }

    public static class Position {
        private final int columnNumber;
        private final int lineNumber;
        private final int pos;

        public Position(int i, int i2, int i3) {
            this.pos = i;
            this.lineNumber = i2;
            this.columnNumber = i3;
        }

        public int pos() {
            return this.pos;
        }

        public int lineNumber() {
            return this.lineNumber;
        }

        public int columnNumber() {
            return this.columnNumber;
        }

        public boolean isTracked() {
            return this != Range.UntrackedPos;
        }

        public String toString() {
            return this.lineNumber + "," + this.columnNumber + ":" + this.pos;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Position position = (Position) obj;
            if (this.pos != position.pos || this.lineNumber != position.lineNumber) {
                return false;
            }
            if (this.columnNumber == position.columnNumber) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (((this.pos * 31) + this.lineNumber) * 31) + this.columnNumber;
        }
    }
}
