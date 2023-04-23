package org.jsoup.parser;

import javax.annotation.Nullable;
import org.jsoup.internal.Normalizer;
import org.jsoup.nodes.Attributes;

public class ParseSettings {
    public static final ParseSettings htmlDefault = new ParseSettings(false, false);
    public static final ParseSettings preserveCase = new ParseSettings(true, true);
    private final boolean preserveAttributeCase;
    private final boolean preserveTagCase;

    public boolean preserveTagCase() {
        return this.preserveTagCase;
    }

    public boolean preserveAttributeCase() {
        return this.preserveAttributeCase;
    }

    public ParseSettings(boolean z, boolean z2) {
        this.preserveTagCase = z;
        this.preserveAttributeCase = z2;
    }

    ParseSettings(ParseSettings parseSettings) {
        this(parseSettings.preserveTagCase, parseSettings.preserveAttributeCase);
    }

    public String normalizeTag(String str) {
        String trim = str.trim();
        return !this.preserveTagCase ? Normalizer.lowerCase(trim) : trim;
    }

    public String normalizeAttribute(String str) {
        String trim = str.trim();
        return !this.preserveAttributeCase ? Normalizer.lowerCase(trim) : trim;
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public Attributes normalizeAttributes(@Nullable Attributes attributes) {
        if (attributes != null && !this.preserveAttributeCase) {
            attributes.normalize();
        }
        return attributes;
    }

    static String normalName(String str) {
        return Normalizer.lowerCase(str.trim());
    }
}
