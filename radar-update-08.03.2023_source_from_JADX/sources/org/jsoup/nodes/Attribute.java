package org.jsoup.nodes;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import kotlin.text.Typography;
import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import org.jsoup.internal.Normalizer;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;

public class Attribute implements Map.Entry<String, String>, Cloneable {
    private static final String[] booleanAttributes = {"allowfullscreen", "async", "autofocus", "checked", "compact", "declare", "default", "defer", "disabled", "formnovalidate", "hidden", "inert", "ismap", "itemscope", "multiple", "muted", "nohref", "noresize", "noshade", "novalidate", "nowrap", "open", "readonly", "required", "reversed", "seamless", "selected", "sortable", "truespeed", "typemustmatch"};
    private static final Pattern htmlKeyReplace = Pattern.compile("[\\x00-\\x1f\\x7f-\\x9f \"'/=]");
    private static final Pattern htmlKeyValid = Pattern.compile("[^\\x00-\\x1f\\x7f-\\x9f \"'/=]+");
    private static final Pattern xmlKeyReplace = Pattern.compile("[^-a-zA-Z0-9_:.]");
    private static final Pattern xmlKeyValid = Pattern.compile("[a-zA-Z_:][-a-zA-Z0-9_:.]*");
    private String key;
    @Nullable
    Attributes parent;
    @Nullable
    private String val;

    public Attribute(String str, @Nullable String str2) {
        this(str, str2, (Attributes) null);
    }

    public Attribute(String str, @Nullable String str2, @Nullable Attributes attributes) {
        Validate.notNull(str);
        String trim = str.trim();
        Validate.notEmpty(trim);
        this.key = trim;
        this.val = str2;
        this.parent = attributes;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String str) {
        int indexOfKey;
        Validate.notNull(str);
        String trim = str.trim();
        Validate.notEmpty(trim);
        Attributes attributes = this.parent;
        if (!(attributes == null || (indexOfKey = attributes.indexOfKey(this.key)) == -1)) {
            this.parent.keys[indexOfKey] = trim;
        }
        this.key = trim;
    }

    public String getValue() {
        return Attributes.checkNotNull(this.val);
    }

    public boolean hasDeclaredValue() {
        return this.val != null;
    }

    public String setValue(@Nullable String str) {
        int indexOfKey;
        String str2 = this.val;
        Attributes attributes = this.parent;
        if (!(attributes == null || (indexOfKey = attributes.indexOfKey(this.key)) == -1)) {
            str2 = this.parent.get(this.key);
            this.parent.vals[indexOfKey] = str;
        }
        this.val = str;
        return Attributes.checkNotNull(str2);
    }

    public String html() {
        StringBuilder borrowBuilder = StringUtil.borrowBuilder();
        try {
            html(borrowBuilder, new Document("").outputSettings());
            return StringUtil.releaseBuilder(borrowBuilder);
        } catch (IOException e) {
            throw new SerializationException((Throwable) e);
        }
    }

    /* access modifiers changed from: protected */
    public void html(Appendable appendable, Document.OutputSettings outputSettings) throws IOException {
        html(this.key, this.val, appendable, outputSettings);
    }

    protected static void html(String str, @Nullable String str2, Appendable appendable, Document.OutputSettings outputSettings) throws IOException {
        String validKey = getValidKey(str, outputSettings.syntax());
        if (validKey != null) {
            htmlNoValidate(validKey, str2, appendable, outputSettings);
        }
    }

    static void htmlNoValidate(String str, @Nullable String str2, Appendable appendable, Document.OutputSettings outputSettings) throws IOException {
        appendable.append(str);
        if (!shouldCollapseAttribute(str, str2, outputSettings)) {
            appendable.append("=\"");
            Entities.escape(appendable, Attributes.checkNotNull(str2), outputSettings, true, false, false, false);
            appendable.append(Typography.quote);
        }
    }

    @Nullable
    public static String getValidKey(String str, Document.OutputSettings.Syntax syntax) {
        if (syntax == Document.OutputSettings.Syntax.xml) {
            Pattern pattern = xmlKeyValid;
            if (!pattern.matcher(str).matches()) {
                String replaceAll = xmlKeyReplace.matcher(str).replaceAll("");
                if (pattern.matcher(replaceAll).matches()) {
                    return replaceAll;
                }
                return null;
            }
        }
        if (syntax == Document.OutputSettings.Syntax.html) {
            Pattern pattern2 = htmlKeyValid;
            if (!pattern2.matcher(str).matches()) {
                String replaceAll2 = htmlKeyReplace.matcher(str).replaceAll("");
                if (pattern2.matcher(replaceAll2).matches()) {
                    return replaceAll2;
                }
                return null;
            }
        }
        return str;
    }

    public String toString() {
        return html();
    }

    public static Attribute createFromEncoded(String str, String str2) {
        return new Attribute(str, Entities.unescape(str2, true), (Attributes) null);
    }

    /* access modifiers changed from: protected */
    public boolean isDataAttribute() {
        return isDataAttribute(this.key);
    }

    protected static boolean isDataAttribute(String str) {
        return str.startsWith("data-") && str.length() > 5;
    }

    /* access modifiers changed from: protected */
    public final boolean shouldCollapseAttribute(Document.OutputSettings outputSettings) {
        return shouldCollapseAttribute(this.key, this.val, outputSettings);
    }

    protected static boolean shouldCollapseAttribute(String str, @Nullable String str2, Document.OutputSettings outputSettings) {
        return outputSettings.syntax() == Document.OutputSettings.Syntax.html && (str2 == null || ((str2.isEmpty() || str2.equalsIgnoreCase(str)) && isBooleanAttribute(str)));
    }

    public static boolean isBooleanAttribute(String str) {
        return Arrays.binarySearch(booleanAttributes, Normalizer.lowerCase(str)) >= 0;
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Attribute attribute = (Attribute) obj;
        String str = this.key;
        if (str == null ? attribute.key != null : !str.equals(attribute.key)) {
            return false;
        }
        String str2 = this.val;
        String str3 = attribute.val;
        if (str2 != null) {
            return str2.equals(str3);
        }
        if (str3 == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        String str = this.key;
        int i = 0;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.val;
        if (str2 != null) {
            i = str2.hashCode();
        }
        return hashCode + i;
    }

    public Attribute clone() {
        try {
            return (Attribute) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
