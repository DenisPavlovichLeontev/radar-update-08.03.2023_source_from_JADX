package org.jsoup.parser;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Range;
import org.jsoup.parser.Token;

abstract class TreeBuilder {
    protected String baseUri;
    protected Token currentToken;
    protected Document doc;
    private Token.EndTag end = new Token.EndTag();
    protected Parser parser;
    CharacterReader reader;
    protected Map<String, Tag> seenTags;
    protected ParseSettings settings;
    protected ArrayList<Element> stack;
    private Token.StartTag start = new Token.StartTag();
    Tokeniser tokeniser;
    private boolean trackSourceRange;

    /* access modifiers changed from: package-private */
    public abstract ParseSettings defaultSettings();

    /* access modifiers changed from: protected */
    public boolean isContentForTagData(String str) {
        return false;
    }

    /* access modifiers changed from: package-private */
    public abstract TreeBuilder newInstance();

    /* access modifiers changed from: package-private */
    public abstract List<Node> parseFragment(String str, Element element, String str2, Parser parser2);

    /* access modifiers changed from: protected */
    public abstract boolean process(Token token);

    TreeBuilder() {
    }

    /* access modifiers changed from: protected */
    @ParametersAreNonnullByDefault
    public void initialiseParse(Reader reader2, String str, Parser parser2) {
        Validate.notNullParam(reader2, "input");
        Validate.notNullParam(str, "baseUri");
        Validate.notNull(parser2);
        Document document = new Document(str);
        this.doc = document;
        document.parser(parser2);
        this.parser = parser2;
        this.settings = parser2.settings();
        this.reader = new CharacterReader(reader2);
        this.trackSourceRange = parser2.isTrackPosition();
        this.reader.trackNewlines(parser2.isTrackErrors() || this.trackSourceRange);
        this.currentToken = null;
        this.tokeniser = new Tokeniser(this.reader, parser2.getErrors());
        this.stack = new ArrayList<>(32);
        this.seenTags = new HashMap();
        this.baseUri = str;
    }

    /* access modifiers changed from: package-private */
    @ParametersAreNonnullByDefault
    public Document parse(Reader reader2, String str, Parser parser2) {
        initialiseParse(reader2, str, parser2);
        runParser();
        this.reader.close();
        this.reader = null;
        this.tokeniser = null;
        this.stack = null;
        this.seenTags = null;
        return this.doc;
    }

    /* access modifiers changed from: protected */
    public void runParser() {
        Token read;
        Tokeniser tokeniser2 = this.tokeniser;
        Token.TokenType tokenType = Token.TokenType.EOF;
        do {
            read = tokeniser2.read();
            process(read);
            read.reset();
        } while (read.type != tokenType);
    }

    /* access modifiers changed from: protected */
    public boolean processStartTag(String str) {
        Token.StartTag startTag = this.start;
        if (this.currentToken == startTag) {
            return process(new Token.StartTag().name(str));
        }
        return process(startTag.reset().name(str));
    }

    public boolean processStartTag(String str, Attributes attributes) {
        Token.StartTag startTag = this.start;
        if (this.currentToken == startTag) {
            return process(new Token.StartTag().nameAttr(str, attributes));
        }
        startTag.reset();
        startTag.nameAttr(str, attributes);
        return process(startTag);
    }

    /* access modifiers changed from: protected */
    public boolean processEndTag(String str) {
        Token token = this.currentToken;
        Token.EndTag endTag = this.end;
        if (token == endTag) {
            return process(new Token.EndTag().name(str));
        }
        return process(endTag.reset().name(str));
    }

    /* access modifiers changed from: protected */
    public Element currentElement() {
        int size = this.stack.size();
        return size > 0 ? this.stack.get(size - 1) : this.doc;
    }

    /* access modifiers changed from: protected */
    public boolean currentElementIs(String str) {
        Element currentElement;
        if (this.stack.size() == 0 || (currentElement = currentElement()) == null || !currentElement.normalName().equals(str)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void error(String str) {
        Object[] objArr = null;
        error(str, (Object[]) null);
    }

    /* access modifiers changed from: protected */
    public void error(String str, Object... objArr) {
        ParseErrorList errors = this.parser.getErrors();
        if (errors.canAddError()) {
            errors.add(new ParseError(this.reader, str, objArr));
        }
    }

    /* access modifiers changed from: protected */
    public Tag tagFor(String str, ParseSettings parseSettings) {
        Tag tag = this.seenTags.get(str);
        if (tag != null) {
            return tag;
        }
        Tag valueOf = Tag.valueOf(str, parseSettings);
        this.seenTags.put(str, valueOf);
        return valueOf;
    }

    /* access modifiers changed from: protected */
    public void onNodeInserted(Node node, @Nullable Token token) {
        trackNodePosition(node, token, true);
    }

    /* access modifiers changed from: protected */
    public void onNodeClosed(Node node, Token token) {
        trackNodePosition(node, token, false);
    }

    private void trackNodePosition(Node node, @Nullable Token token, boolean z) {
        int startPos;
        if (this.trackSourceRange && token != null && (startPos = token.startPos()) != -1) {
            Range.Position position = new Range.Position(startPos, this.reader.lineNumber(startPos), this.reader.columnNumber(startPos));
            int endPos = token.endPos();
            new Range(position, new Range.Position(endPos, this.reader.lineNumber(endPos), this.reader.columnNumber(endPos))).track(node, z);
        }
    }
}
