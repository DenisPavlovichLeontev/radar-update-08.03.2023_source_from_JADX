package org.jsoup.parser;

import com.j256.ormlite.stmt.query.SimpleComparison;
import javax.annotation.Nullable;
import okio.Utf8;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;

abstract class Token {
    static final int Unset = -1;
    private int endPos;
    private int startPos;
    TokenType type;

    public enum TokenType {
        Doctype,
        StartTag,
        EndTag,
        Comment,
        Character,
        EOF
    }

    private Token() {
        this.endPos = -1;
    }

    /* access modifiers changed from: package-private */
    public String tokenType() {
        return getClass().getSimpleName();
    }

    /* access modifiers changed from: package-private */
    public Token reset() {
        this.startPos = -1;
        this.endPos = -1;
        return this;
    }

    /* access modifiers changed from: package-private */
    public int startPos() {
        return this.startPos;
    }

    /* access modifiers changed from: package-private */
    public void startPos(int i) {
        this.startPos = i;
    }

    /* access modifiers changed from: package-private */
    public int endPos() {
        return this.endPos;
    }

    /* access modifiers changed from: package-private */
    public void endPos(int i) {
        this.endPos = i;
    }

    static void reset(StringBuilder sb) {
        if (sb != null) {
            sb.delete(0, sb.length());
        }
    }

    static final class Doctype extends Token {
        boolean forceQuirks = false;
        final StringBuilder name = new StringBuilder();
        String pubSysKey = null;
        final StringBuilder publicIdentifier = new StringBuilder();
        final StringBuilder systemIdentifier = new StringBuilder();

        Doctype() {
            super();
            this.type = TokenType.Doctype;
        }

        /* access modifiers changed from: package-private */
        public Token reset() {
            Token.super.reset();
            reset(this.name);
            this.pubSysKey = null;
            reset(this.publicIdentifier);
            reset(this.systemIdentifier);
            this.forceQuirks = false;
            return this;
        }

        /* access modifiers changed from: package-private */
        public String getName() {
            return this.name.toString();
        }

        /* access modifiers changed from: package-private */
        public String getPubSysKey() {
            return this.pubSysKey;
        }

        /* access modifiers changed from: package-private */
        public String getPublicIdentifier() {
            return this.publicIdentifier.toString();
        }

        public String getSystemIdentifier() {
            return this.systemIdentifier.toString();
        }

        public boolean isForceQuirks() {
            return this.forceQuirks;
        }

        public String toString() {
            return "<!doctype " + getName() + SimpleComparison.GREATER_THAN_OPERATION;
        }
    }

    static abstract class Tag extends Token {
        private static final int MaxAttributes = 512;
        private final StringBuilder attrName = new StringBuilder();
        @Nullable
        private String attrNameS;
        private final StringBuilder attrValue = new StringBuilder();
        @Nullable
        private String attrValueS;
        @Nullable
        Attributes attributes;
        private boolean hasAttrName = false;
        private boolean hasAttrValue = false;
        private boolean hasEmptyAttrValue = false;
        @Nullable
        protected String normalName;
        boolean selfClosing = false;
        @Nullable
        protected String tagName;

        public abstract String toString();

        Tag() {
            super();
        }

        /* access modifiers changed from: package-private */
        public Tag reset() {
            Token.super.reset();
            this.tagName = null;
            this.normalName = null;
            reset(this.attrName);
            this.attrNameS = null;
            this.hasAttrName = false;
            reset(this.attrValue);
            this.attrValueS = null;
            this.hasEmptyAttrValue = false;
            this.hasAttrValue = false;
            this.selfClosing = false;
            this.attributes = null;
            return this;
        }

        /* access modifiers changed from: package-private */
        public final void newAttribute() {
            String str;
            if (this.attributes == null) {
                this.attributes = new Attributes();
            }
            if (this.hasAttrName && this.attributes.size() < 512) {
                String trim = (this.attrName.length() > 0 ? this.attrName.toString() : this.attrNameS).trim();
                if (trim.length() > 0) {
                    if (this.hasAttrValue) {
                        str = this.attrValue.length() > 0 ? this.attrValue.toString() : this.attrValueS;
                    } else {
                        str = this.hasEmptyAttrValue ? "" : null;
                    }
                    this.attributes.add(trim, str);
                }
            }
            reset(this.attrName);
            this.attrNameS = null;
            this.hasAttrName = false;
            reset(this.attrValue);
            this.attrValueS = null;
            this.hasAttrValue = false;
            this.hasEmptyAttrValue = false;
        }

        /* access modifiers changed from: package-private */
        public final boolean hasAttributes() {
            return this.attributes != null;
        }

        /* access modifiers changed from: package-private */
        public final boolean hasAttribute(String str) {
            Attributes attributes2 = this.attributes;
            return attributes2 != null && attributes2.hasKey(str);
        }

        /* access modifiers changed from: package-private */
        public final void finaliseTag() {
            if (this.hasAttrName) {
                newAttribute();
            }
        }

        /* access modifiers changed from: package-private */
        public final String name() {
            String str = this.tagName;
            Validate.isFalse(str == null || str.length() == 0);
            return this.tagName;
        }

        /* access modifiers changed from: package-private */
        public final String normalName() {
            return this.normalName;
        }

        /* access modifiers changed from: package-private */
        public final String toStringName() {
            String str = this.tagName;
            return str != null ? str : "[unset]";
        }

        /* access modifiers changed from: package-private */
        public final Tag name(String str) {
            this.tagName = str;
            this.normalName = ParseSettings.normalName(str);
            return this;
        }

        /* access modifiers changed from: package-private */
        public final boolean isSelfClosing() {
            return this.selfClosing;
        }

        /* access modifiers changed from: package-private */
        public final void appendTagName(String str) {
            String replace = str.replace(0, Utf8.REPLACEMENT_CHARACTER);
            String str2 = this.tagName;
            if (str2 != null) {
                replace = str2.concat(replace);
            }
            this.tagName = replace;
            this.normalName = ParseSettings.normalName(replace);
        }

        /* access modifiers changed from: package-private */
        public final void appendTagName(char c) {
            appendTagName(String.valueOf(c));
        }

        /* access modifiers changed from: package-private */
        public final void appendAttributeName(String str) {
            String replace = str.replace(0, Utf8.REPLACEMENT_CHARACTER);
            ensureAttrName();
            if (this.attrName.length() == 0) {
                this.attrNameS = replace;
            } else {
                this.attrName.append(replace);
            }
        }

        /* access modifiers changed from: package-private */
        public final void appendAttributeName(char c) {
            ensureAttrName();
            this.attrName.append(c);
        }

        /* access modifiers changed from: package-private */
        public final void appendAttributeValue(String str) {
            ensureAttrValue();
            if (this.attrValue.length() == 0) {
                this.attrValueS = str;
            } else {
                this.attrValue.append(str);
            }
        }

        /* access modifiers changed from: package-private */
        public final void appendAttributeValue(char c) {
            ensureAttrValue();
            this.attrValue.append(c);
        }

        /* access modifiers changed from: package-private */
        public final void appendAttributeValue(char[] cArr) {
            ensureAttrValue();
            this.attrValue.append(cArr);
        }

        /* access modifiers changed from: package-private */
        public final void appendAttributeValue(int[] iArr) {
            ensureAttrValue();
            for (int appendCodePoint : iArr) {
                this.attrValue.appendCodePoint(appendCodePoint);
            }
        }

        /* access modifiers changed from: package-private */
        public final void setEmptyAttributeValue() {
            this.hasEmptyAttrValue = true;
        }

        private void ensureAttrName() {
            this.hasAttrName = true;
            String str = this.attrNameS;
            if (str != null) {
                this.attrName.append(str);
                this.attrNameS = null;
            }
        }

        private void ensureAttrValue() {
            this.hasAttrValue = true;
            String str = this.attrValueS;
            if (str != null) {
                this.attrValue.append(str);
                this.attrValueS = null;
            }
        }
    }

    static final class StartTag extends Tag {
        StartTag() {
            this.type = TokenType.StartTag;
        }

        /* access modifiers changed from: package-private */
        public Tag reset() {
            super.reset();
            this.attributes = null;
            return this;
        }

        /* access modifiers changed from: package-private */
        public StartTag nameAttr(String str, Attributes attributes) {
            this.tagName = str;
            this.attributes = attributes;
            this.normalName = ParseSettings.normalName(this.tagName);
            return this;
        }

        public String toString() {
            if (!hasAttributes() || this.attributes.size() <= 0) {
                return SimpleComparison.LESS_THAN_OPERATION + toStringName() + SimpleComparison.GREATER_THAN_OPERATION;
            }
            return SimpleComparison.LESS_THAN_OPERATION + toStringName() + " " + this.attributes.toString() + SimpleComparison.GREATER_THAN_OPERATION;
        }
    }

    static final class EndTag extends Tag {
        EndTag() {
            this.type = TokenType.EndTag;
        }

        public String toString() {
            return "</" + toStringName() + SimpleComparison.GREATER_THAN_OPERATION;
        }
    }

    static final class Comment extends Token {
        boolean bogus = false;
        private final StringBuilder data = new StringBuilder();
        private String dataS;

        /* access modifiers changed from: package-private */
        public Token reset() {
            Token.super.reset();
            reset(this.data);
            this.dataS = null;
            this.bogus = false;
            return this;
        }

        Comment() {
            super();
            this.type = TokenType.Comment;
        }

        /* access modifiers changed from: package-private */
        public String getData() {
            String str = this.dataS;
            return str != null ? str : this.data.toString();
        }

        /* access modifiers changed from: package-private */
        public final Comment append(String str) {
            ensureData();
            if (this.data.length() == 0) {
                this.dataS = str;
            } else {
                this.data.append(str);
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public final Comment append(char c) {
            ensureData();
            this.data.append(c);
            return this;
        }

        private void ensureData() {
            String str = this.dataS;
            if (str != null) {
                this.data.append(str);
                this.dataS = null;
            }
        }

        public String toString() {
            return "<!--" + getData() + "-->";
        }
    }

    static class Character extends Token {
        private String data;

        Character() {
            super();
            this.type = TokenType.Character;
        }

        /* access modifiers changed from: package-private */
        public Token reset() {
            Token.super.reset();
            this.data = null;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Character data(String str) {
            this.data = str;
            return this;
        }

        /* access modifiers changed from: package-private */
        public String getData() {
            return this.data;
        }

        public String toString() {
            return getData();
        }
    }

    static final class CData extends Character {
        CData(String str) {
            data(str);
        }

        public String toString() {
            return "<![CDATA[" + getData() + "]]>";
        }
    }

    static final class EOF extends Token {
        public String toString() {
            return "";
        }

        EOF() {
            super();
            this.type = TokenType.EOF;
        }

        /* access modifiers changed from: package-private */
        public Token reset() {
            Token.super.reset();
            return this;
        }
    }

    /* access modifiers changed from: package-private */
    public final boolean isDoctype() {
        return this.type == TokenType.Doctype;
    }

    /* access modifiers changed from: package-private */
    public final Doctype asDoctype() {
        return (Doctype) this;
    }

    /* access modifiers changed from: package-private */
    public final boolean isStartTag() {
        return this.type == TokenType.StartTag;
    }

    /* access modifiers changed from: package-private */
    public final StartTag asStartTag() {
        return (StartTag) this;
    }

    /* access modifiers changed from: package-private */
    public final boolean isEndTag() {
        return this.type == TokenType.EndTag;
    }

    /* access modifiers changed from: package-private */
    public final EndTag asEndTag() {
        return (EndTag) this;
    }

    /* access modifiers changed from: package-private */
    public final boolean isComment() {
        return this.type == TokenType.Comment;
    }

    /* access modifiers changed from: package-private */
    public final Comment asComment() {
        return (Comment) this;
    }

    /* access modifiers changed from: package-private */
    public final boolean isCharacter() {
        return this.type == TokenType.Character;
    }

    /* access modifiers changed from: package-private */
    public final boolean isCData() {
        return this instanceof CData;
    }

    /* access modifiers changed from: package-private */
    public final Character asCharacter() {
        return (Character) this;
    }

    /* access modifiers changed from: package-private */
    public final boolean isEOF() {
        return this.type == TokenType.EOF;
    }
}
