package org.jsoup.parser;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import java.util.ArrayList;
import java.util.Iterator;
import kotlin.text.Typography;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.parser.Token;
import org.osgeo.proj4j.parser.Proj4Keyword;
import org.osgeo.proj4j.units.AngleFormat;

enum HtmlTreeBuilderState {
    Initial {
        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (HtmlTreeBuilderState.isWhitespace(token)) {
                return true;
            }
            if (token.isComment()) {
                htmlTreeBuilder.insert(token.asComment());
            } else if (token.isDoctype()) {
                Token.Doctype asDoctype = token.asDoctype();
                DocumentType documentType = new DocumentType(htmlTreeBuilder.settings.normalizeTag(asDoctype.getName()), asDoctype.getPublicIdentifier(), asDoctype.getSystemIdentifier());
                documentType.setPubSysKey(asDoctype.getPubSysKey());
                htmlTreeBuilder.getDocument().appendChild(documentType);
                htmlTreeBuilder.onNodeInserted(documentType, token);
                if (asDoctype.isForceQuirks()) {
                    htmlTreeBuilder.getDocument().quirksMode(Document.QuirksMode.quirks);
                }
                htmlTreeBuilder.transition(BeforeHtml);
            } else {
                htmlTreeBuilder.transition(BeforeHtml);
                return htmlTreeBuilder.process(token);
            }
            return true;
        }
    },
    BeforeHtml {
        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (token.isDoctype()) {
                htmlTreeBuilder.error(this);
                return false;
            } else if (token.isComment()) {
                htmlTreeBuilder.insert(token.asComment());
                return true;
            } else if (HtmlTreeBuilderState.isWhitespace(token)) {
                htmlTreeBuilder.insert(token.asCharacter());
                return true;
            } else if (token.isStartTag() && token.asStartTag().normalName().equals("html")) {
                htmlTreeBuilder.insert(token.asStartTag());
                htmlTreeBuilder.transition(BeforeHead);
                return true;
            } else if (token.isEndTag() && StringUtil.inSorted(token.asEndTag().normalName(), Constants.BeforeHtmlToHead)) {
                return anythingElse(token, htmlTreeBuilder);
            } else {
                if (!token.isEndTag()) {
                    return anythingElse(token, htmlTreeBuilder);
                }
                htmlTreeBuilder.error(this);
                return false;
            }
        }

        private boolean anythingElse(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            htmlTreeBuilder.insertStartTag("html");
            htmlTreeBuilder.transition(BeforeHead);
            return htmlTreeBuilder.process(token);
        }
    },
    BeforeHead {
        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (HtmlTreeBuilderState.isWhitespace(token)) {
                htmlTreeBuilder.insert(token.asCharacter());
                return true;
            } else if (token.isComment()) {
                htmlTreeBuilder.insert(token.asComment());
                return true;
            } else if (token.isDoctype()) {
                htmlTreeBuilder.error(this);
                return false;
            } else if (token.isStartTag() && token.asStartTag().normalName().equals("html")) {
                return InBody.process(token, htmlTreeBuilder);
            } else {
                if (token.isStartTag() && token.asStartTag().normalName().equals("head")) {
                    htmlTreeBuilder.setHeadElement(htmlTreeBuilder.insert(token.asStartTag()));
                    htmlTreeBuilder.transition(InHead);
                    return true;
                } else if (token.isEndTag() && StringUtil.inSorted(token.asEndTag().normalName(), Constants.BeforeHtmlToHead)) {
                    htmlTreeBuilder.processStartTag("head");
                    return htmlTreeBuilder.process(token);
                } else if (token.isEndTag()) {
                    htmlTreeBuilder.error(this);
                    return false;
                } else {
                    htmlTreeBuilder.processStartTag("head");
                    return htmlTreeBuilder.process(token);
                }
            }
        }
    },
    InHead {
        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (HtmlTreeBuilderState.isWhitespace(token)) {
                htmlTreeBuilder.insert(token.asCharacter());
                return true;
            }
            int i = C122625.$SwitchMap$org$jsoup$parser$Token$TokenType[token.type.ordinal()];
            if (i == 1) {
                htmlTreeBuilder.insert(token.asComment());
            } else if (i == 2) {
                htmlTreeBuilder.error(this);
                return false;
            } else if (i == 3) {
                Token.StartTag asStartTag = token.asStartTag();
                String normalName = asStartTag.normalName();
                if (normalName.equals("html")) {
                    return InBody.process(token, htmlTreeBuilder);
                }
                if (StringUtil.inSorted(normalName, Constants.InHeadEmpty)) {
                    Element insertEmpty = htmlTreeBuilder.insertEmpty(asStartTag);
                    if (normalName.equals("base") && insertEmpty.hasAttr(SVGParser.XML_STYLESHEET_ATTR_HREF)) {
                        htmlTreeBuilder.maybeSetBaseUri(insertEmpty);
                    }
                } else if (normalName.equals("meta")) {
                    htmlTreeBuilder.insertEmpty(asStartTag);
                } else if (normalName.equals("title")) {
                    HtmlTreeBuilderState.handleRcData(asStartTag, htmlTreeBuilder);
                } else if (StringUtil.inSorted(normalName, Constants.InHeadRaw)) {
                    HtmlTreeBuilderState.handleRawtext(asStartTag, htmlTreeBuilder);
                } else if (normalName.equals("noscript")) {
                    htmlTreeBuilder.insert(asStartTag);
                    htmlTreeBuilder.transition(InHeadNoscript);
                } else if (normalName.equals("script")) {
                    htmlTreeBuilder.tokeniser.transition(TokeniserState.ScriptData);
                    htmlTreeBuilder.markInsertionMode();
                    htmlTreeBuilder.transition(Text);
                    htmlTreeBuilder.insert(asStartTag);
                } else if (normalName.equals("head")) {
                    htmlTreeBuilder.error(this);
                    return false;
                } else if (!normalName.equals("template")) {
                    return anythingElse(token, htmlTreeBuilder);
                } else {
                    htmlTreeBuilder.insert(asStartTag);
                    htmlTreeBuilder.insertMarkerToFormattingElements();
                    htmlTreeBuilder.framesetOk(false);
                    htmlTreeBuilder.transition(InTemplate);
                    htmlTreeBuilder.pushTemplateMode(InTemplate);
                }
            } else if (i != 4) {
                return anythingElse(token, htmlTreeBuilder);
            } else {
                String normalName2 = token.asEndTag().normalName();
                if (normalName2.equals("head")) {
                    htmlTreeBuilder.pop();
                    htmlTreeBuilder.transition(AfterHead);
                } else if (StringUtil.inSorted(normalName2, Constants.InHeadEnd)) {
                    return anythingElse(token, htmlTreeBuilder);
                } else {
                    if (!normalName2.equals("template")) {
                        htmlTreeBuilder.error(this);
                        return false;
                    } else if (!htmlTreeBuilder.onStack(normalName2)) {
                        htmlTreeBuilder.error(this);
                    } else {
                        htmlTreeBuilder.generateImpliedEndTags(true);
                        if (!normalName2.equals(htmlTreeBuilder.currentElement().normalName())) {
                            htmlTreeBuilder.error(this);
                        }
                        htmlTreeBuilder.popStackToClose(normalName2);
                        htmlTreeBuilder.clearFormattingElementsToLastMarker();
                        htmlTreeBuilder.popTemplateMode();
                        htmlTreeBuilder.resetInsertionMode();
                    }
                }
            }
            return true;
        }

        private boolean anythingElse(Token token, TreeBuilder treeBuilder) {
            treeBuilder.processEndTag("head");
            return treeBuilder.process(token);
        }
    },
    InHeadNoscript {
        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (token.isDoctype()) {
                htmlTreeBuilder.error(this);
                return true;
            } else if (token.isStartTag() && token.asStartTag().normalName().equals("html")) {
                return htmlTreeBuilder.process(token, InBody);
            } else {
                if (token.isEndTag() && token.asEndTag().normalName().equals("noscript")) {
                    htmlTreeBuilder.pop();
                    htmlTreeBuilder.transition(InHead);
                    return true;
                } else if (HtmlTreeBuilderState.isWhitespace(token) || token.isComment() || (token.isStartTag() && StringUtil.inSorted(token.asStartTag().normalName(), Constants.InHeadNoScriptHead))) {
                    return htmlTreeBuilder.process(token, InHead);
                } else {
                    if (token.isEndTag() && token.asEndTag().normalName().equals("br")) {
                        return anythingElse(token, htmlTreeBuilder);
                    }
                    if ((!token.isStartTag() || !StringUtil.inSorted(token.asStartTag().normalName(), Constants.InHeadNoscriptIgnore)) && !token.isEndTag()) {
                        return anythingElse(token, htmlTreeBuilder);
                    }
                    htmlTreeBuilder.error(this);
                    return false;
                }
            }
        }

        private boolean anythingElse(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            htmlTreeBuilder.error(this);
            htmlTreeBuilder.insert(new Token.Character().data(token.toString()));
            return true;
        }
    },
    AfterHead {
        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (HtmlTreeBuilderState.isWhitespace(token)) {
                htmlTreeBuilder.insert(token.asCharacter());
                return true;
            } else if (token.isComment()) {
                htmlTreeBuilder.insert(token.asComment());
                return true;
            } else if (token.isDoctype()) {
                htmlTreeBuilder.error(this);
                return true;
            } else if (token.isStartTag()) {
                Token.StartTag asStartTag = token.asStartTag();
                String normalName = asStartTag.normalName();
                if (normalName.equals("html")) {
                    return htmlTreeBuilder.process(token, InBody);
                }
                if (normalName.equals("body")) {
                    htmlTreeBuilder.insert(asStartTag);
                    htmlTreeBuilder.framesetOk(false);
                    htmlTreeBuilder.transition(InBody);
                    return true;
                } else if (normalName.equals("frameset")) {
                    htmlTreeBuilder.insert(asStartTag);
                    htmlTreeBuilder.transition(InFrameset);
                    return true;
                } else if (StringUtil.inSorted(normalName, Constants.InBodyStartToHead)) {
                    htmlTreeBuilder.error(this);
                    Element headElement = htmlTreeBuilder.getHeadElement();
                    htmlTreeBuilder.push(headElement);
                    htmlTreeBuilder.process(token, InHead);
                    htmlTreeBuilder.removeFromStack(headElement);
                    return true;
                } else if (normalName.equals("head")) {
                    htmlTreeBuilder.error(this);
                    return false;
                } else {
                    anythingElse(token, htmlTreeBuilder);
                    return true;
                }
            } else if (token.isEndTag()) {
                String normalName2 = token.asEndTag().normalName();
                if (StringUtil.inSorted(normalName2, Constants.AfterHeadBody)) {
                    anythingElse(token, htmlTreeBuilder);
                    return true;
                } else if (normalName2.equals("template")) {
                    htmlTreeBuilder.process(token, InHead);
                    return true;
                } else {
                    htmlTreeBuilder.error(this);
                    return false;
                }
            } else {
                anythingElse(token, htmlTreeBuilder);
                return true;
            }
        }

        private boolean anythingElse(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            htmlTreeBuilder.processStartTag("body");
            htmlTreeBuilder.framesetOk(true);
            return htmlTreeBuilder.process(token);
        }
    },
    InBody {
        private static final int MaxStackScan = 24;

        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            switch (C122625.$SwitchMap$org$jsoup$parser$Token$TokenType[token.type.ordinal()]) {
                case 1:
                    htmlTreeBuilder.insert(token.asComment());
                    return true;
                case 2:
                    htmlTreeBuilder.error(this);
                    return false;
                case 3:
                    return inBodyStartTag(token, htmlTreeBuilder);
                case 4:
                    return inBodyEndTag(token, htmlTreeBuilder);
                case 5:
                    Token.Character asCharacter = token.asCharacter();
                    if (asCharacter.getData().equals(HtmlTreeBuilderState.nullString)) {
                        htmlTreeBuilder.error(this);
                        return false;
                    } else if (!htmlTreeBuilder.framesetOk() || !HtmlTreeBuilderState.isWhitespace((Token) asCharacter)) {
                        htmlTreeBuilder.reconstructFormattingElements();
                        htmlTreeBuilder.insert(asCharacter);
                        htmlTreeBuilder.framesetOk(false);
                        return true;
                    } else {
                        htmlTreeBuilder.reconstructFormattingElements();
                        htmlTreeBuilder.insert(asCharacter);
                        return true;
                    }
                case 6:
                    if (htmlTreeBuilder.templateModeSize() > 0) {
                        return htmlTreeBuilder.process(token, InTemplate);
                    }
                    return true;
                default:
                    return true;
            }
        }

        private boolean inBodyStartTag(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            boolean z;
            Element fromStack;
            FormElement formElement;
            HtmlTreeBuilder htmlTreeBuilder2 = htmlTreeBuilder;
            Token.StartTag asStartTag = token.asStartTag();
            String normalName = asStartTag.normalName();
            normalName.hashCode();
            char c = 65535;
            switch (normalName.hashCode()) {
                case -1644953643:
                    if (normalName.equals("frameset")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1377687758:
                    if (normalName.equals("button")) {
                        c = 1;
                        break;
                    }
                    break;
                case -1191214428:
                    if (normalName.equals("iframe")) {
                        c = 2;
                        break;
                    }
                    break;
                case -1134665583:
                    if (normalName.equals("keygen")) {
                        c = 3;
                        break;
                    }
                    break;
                case -1010136971:
                    if (normalName.equals("option")) {
                        c = 4;
                        break;
                    }
                    break;
                case -1003243718:
                    if (normalName.equals("textarea")) {
                        c = 5;
                        break;
                    }
                    break;
                case -906021636:
                    if (normalName.equals("select")) {
                        c = 6;
                        break;
                    }
                    break;
                case -891985998:
                    if (normalName.equals("strike")) {
                        c = 7;
                        break;
                    }
                    break;
                case -891980137:
                    if (normalName.equals("strong")) {
                        c = 8;
                        break;
                    }
                    break;
                case -80773204:
                    if (normalName.equals("optgroup")) {
                        c = 9;
                        break;
                    }
                    break;
                case 97:
                    if (normalName.equals(Proj4Keyword.f420a)) {
                        c = 10;
                        break;
                    }
                    break;
                case 98:
                    if (normalName.equals(Proj4Keyword.f421b)) {
                        c = 11;
                        break;
                    }
                    break;
                case 105:
                    if (normalName.equals("i")) {
                        c = 12;
                        break;
                    }
                    break;
                case 115:
                    if (normalName.equals(AngleFormat.STR_SEC_ABBREV)) {
                        c = 13;
                        break;
                    }
                    break;
                case 117:
                    if (normalName.equals("u")) {
                        c = 14;
                        break;
                    }
                    break;
                case 3152:
                    if (normalName.equals("br")) {
                        c = 15;
                        break;
                    }
                    break;
                case 3200:
                    if (normalName.equals("dd")) {
                        c = 16;
                        break;
                    }
                    break;
                case 3216:
                    if (normalName.equals("dt")) {
                        c = 17;
                        break;
                    }
                    break;
                case 3240:
                    if (normalName.equals("em")) {
                        c = 18;
                        break;
                    }
                    break;
                case 3273:
                    if (normalName.equals("h1")) {
                        c = 19;
                        break;
                    }
                    break;
                case 3274:
                    if (normalName.equals("h2")) {
                        c = 20;
                        break;
                    }
                    break;
                case 3275:
                    if (normalName.equals("h3")) {
                        c = 21;
                        break;
                    }
                    break;
                case 3276:
                    if (normalName.equals("h4")) {
                        c = 22;
                        break;
                    }
                    break;
                case 3277:
                    if (normalName.equals("h5")) {
                        c = 23;
                        break;
                    }
                    break;
                case 3278:
                    if (normalName.equals("h6")) {
                        c = 24;
                        break;
                    }
                    break;
                case 3338:
                    if (normalName.equals("hr")) {
                        c = 25;
                        break;
                    }
                    break;
                case 3453:
                    if (normalName.equals("li")) {
                        c = 26;
                        break;
                    }
                    break;
                case 3646:
                    if (normalName.equals("rp")) {
                        c = 27;
                        break;
                    }
                    break;
                case 3650:
                    if (normalName.equals("rt")) {
                        c = 28;
                        break;
                    }
                    break;
                case 3712:
                    if (normalName.equals("tt")) {
                        c = 29;
                        break;
                    }
                    break;
                case 97536:
                    if (normalName.equals("big")) {
                        c = 30;
                        break;
                    }
                    break;
                case 104387:
                    if (normalName.equals("img")) {
                        c = 31;
                        break;
                    }
                    break;
                case 111267:
                    if (normalName.equals("pre")) {
                        c = ' ';
                        break;
                    }
                    break;
                case 114276:
                    if (normalName.equals("svg")) {
                        c = '!';
                        break;
                    }
                    break;
                case 117511:
                    if (normalName.equals("wbr")) {
                        c = Typography.quote;
                        break;
                    }
                    break;
                case 118811:
                    if (normalName.equals("xmp")) {
                        c = '#';
                        break;
                    }
                    break;
                case 3002509:
                    if (normalName.equals("area")) {
                        c = Typography.dollar;
                        break;
                    }
                    break;
                case 3029410:
                    if (normalName.equals("body")) {
                        c = '%';
                        break;
                    }
                    break;
                case 3059181:
                    if (normalName.equals("code")) {
                        c = Typography.amp;
                        break;
                    }
                    break;
                case 3148879:
                    if (normalName.equals("font")) {
                        c = AngleFormat.CH_MIN_SYMBOL;
                        break;
                    }
                    break;
                case 3148996:
                    if (normalName.equals("form")) {
                        c = '(';
                        break;
                    }
                    break;
                case 3213227:
                    if (normalName.equals("html")) {
                        c = ')';
                        break;
                    }
                    break;
                case 3344136:
                    if (normalName.equals("math")) {
                        c = '*';
                        break;
                    }
                    break;
                case 3386833:
                    if (normalName.equals("nobr")) {
                        c = '+';
                        break;
                    }
                    break;
                case 3536714:
                    if (normalName.equals("span")) {
                        c = ',';
                        break;
                    }
                    break;
                case 96620249:
                    if (normalName.equals("embed")) {
                        c = '-';
                        break;
                    }
                    break;
                case 100313435:
                    if (normalName.equals("image")) {
                        c = '.';
                        break;
                    }
                    break;
                case 100358090:
                    if (normalName.equals("input")) {
                        c = '/';
                        break;
                    }
                    break;
                case 109548807:
                    if (normalName.equals("small")) {
                        c = '0';
                        break;
                    }
                    break;
                case 110115790:
                    if (normalName.equals("table")) {
                        c = '1';
                        break;
                    }
                    break;
                case 181975684:
                    if (normalName.equals("listing")) {
                        c = '2';
                        break;
                    }
                    break;
                case 1973234167:
                    if (normalName.equals("plaintext")) {
                        c = '3';
                        break;
                    }
                    break;
                case 2091304424:
                    if (normalName.equals("isindex")) {
                        c = '4';
                        break;
                    }
                    break;
                case 2115613112:
                    if (normalName.equals("noembed")) {
                        c = '5';
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    HtmlTreeBuilder htmlTreeBuilder3 = htmlTreeBuilder;
                    htmlTreeBuilder3.error(this);
                    ArrayList<Element> stack = htmlTreeBuilder.getStack();
                    if (stack.size() == 1) {
                        return false;
                    }
                    if ((stack.size() > 2 && !stack.get(1).normalName().equals("body")) || !htmlTreeBuilder.framesetOk()) {
                        return false;
                    }
                    Element element = stack.get(1);
                    if (element.parent() != null) {
                        element.remove();
                    }
                    while (stack.size() > 1) {
                        stack.remove(stack.size() - 1);
                    }
                    htmlTreeBuilder3.insert(asStartTag);
                    htmlTreeBuilder3.transition(InFrameset);
                    return true;
                case 1:
                    HtmlTreeBuilder htmlTreeBuilder4 = htmlTreeBuilder;
                    if (htmlTreeBuilder4.inButtonScope("button")) {
                        htmlTreeBuilder4.error(this);
                        htmlTreeBuilder4.processEndTag("button");
                        htmlTreeBuilder4.process(asStartTag);
                        return true;
                    }
                    htmlTreeBuilder.reconstructFormattingElements();
                    htmlTreeBuilder4.insert(asStartTag);
                    htmlTreeBuilder4.framesetOk(false);
                    return true;
                case 2:
                    HtmlTreeBuilder htmlTreeBuilder5 = htmlTreeBuilder;
                    z = true;
                    htmlTreeBuilder5.framesetOk(false);
                    HtmlTreeBuilderState.handleRawtext(asStartTag, htmlTreeBuilder5);
                    break;
                case 3:
                case 15:
                case 31:
                case '\"':
                case '$':
                case '-':
                    HtmlTreeBuilder htmlTreeBuilder6 = htmlTreeBuilder;
                    z = true;
                    htmlTreeBuilder.reconstructFormattingElements();
                    htmlTreeBuilder6.insertEmpty(asStartTag);
                    htmlTreeBuilder6.framesetOk(false);
                    break;
                case 4:
                case 9:
                    HtmlTreeBuilder htmlTreeBuilder7 = htmlTreeBuilder;
                    z = true;
                    if (htmlTreeBuilder7.currentElementIs("option")) {
                        htmlTreeBuilder7.processEndTag("option");
                    }
                    htmlTreeBuilder.reconstructFormattingElements();
                    htmlTreeBuilder7.insert(asStartTag);
                    break;
                case 5:
                    HtmlTreeBuilder htmlTreeBuilder8 = htmlTreeBuilder;
                    z = true;
                    htmlTreeBuilder8.insert(asStartTag);
                    if (!asStartTag.isSelfClosing()) {
                        htmlTreeBuilder8.tokeniser.transition(TokeniserState.Rcdata);
                        htmlTreeBuilder.markInsertionMode();
                        htmlTreeBuilder8.framesetOk(false);
                        htmlTreeBuilder8.transition(Text);
                        break;
                    }
                    break;
                case 6:
                    HtmlTreeBuilder htmlTreeBuilder9 = htmlTreeBuilder;
                    z = true;
                    htmlTreeBuilder.reconstructFormattingElements();
                    htmlTreeBuilder9.insert(asStartTag);
                    htmlTreeBuilder9.framesetOk(false);
                    if (!asStartTag.selfClosing) {
                        HtmlTreeBuilderState state = htmlTreeBuilder.state();
                        if (!state.equals(InTable) && !state.equals(InCaption) && !state.equals(InTableBody) && !state.equals(InRow) && !state.equals(InCell)) {
                            htmlTreeBuilder9.transition(InSelect);
                            break;
                        } else {
                            htmlTreeBuilder9.transition(InSelectInTable);
                            break;
                        }
                    }
                    break;
                case 7:
                case 8:
                case 11:
                case 12:
                case 13:
                case 14:
                case 18:
                case 29:
                case 30:
                case '&':
                case '\'':
                case '0':
                    HtmlTreeBuilder htmlTreeBuilder10 = htmlTreeBuilder;
                    z = true;
                    htmlTreeBuilder.reconstructFormattingElements();
                    htmlTreeBuilder10.pushActiveFormattingElements(htmlTreeBuilder10.insert(asStartTag));
                    break;
                case 10:
                    String str = Proj4Keyword.f420a;
                    z = true;
                    HtmlTreeBuilder htmlTreeBuilder11 = htmlTreeBuilder;
                    if (htmlTreeBuilder11.getActiveFormattingElement(str) != null) {
                        htmlTreeBuilder11.error(this);
                        htmlTreeBuilder11.processEndTag(str);
                        Element fromStack2 = htmlTreeBuilder11.getFromStack(str);
                        if (fromStack2 != null) {
                            htmlTreeBuilder11.removeFromActiveFormattingElements(fromStack2);
                            htmlTreeBuilder11.removeFromStack(fromStack2);
                        }
                    }
                    htmlTreeBuilder.reconstructFormattingElements();
                    htmlTreeBuilder11.pushActiveFormattingElements(htmlTreeBuilder11.insert(asStartTag));
                    break;
                case 16:
                case 17:
                    HtmlTreeBuilder htmlTreeBuilder12 = htmlTreeBuilder;
                    z = true;
                    htmlTreeBuilder12.framesetOk(false);
                    ArrayList<Element> stack2 = htmlTreeBuilder.getStack();
                    int size = stack2.size() - 1;
                    int i = size >= 24 ? size - 24 : 0;
                    while (true) {
                        if (size >= i) {
                            Element element2 = stack2.get(size);
                            if (StringUtil.inSorted(element2.normalName(), Constants.DdDt)) {
                                htmlTreeBuilder12.processEndTag(element2.normalName());
                            } else if (!htmlTreeBuilder12.isSpecial(element2) || StringUtil.inSorted(element2.normalName(), Constants.InBodyStartLiBreakers)) {
                                size--;
                            }
                        }
                    }
                    if (htmlTreeBuilder12.inButtonScope("p")) {
                        htmlTreeBuilder12.processEndTag("p");
                    }
                    htmlTreeBuilder12.insert(asStartTag);
                    break;
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                    HtmlTreeBuilder htmlTreeBuilder13 = htmlTreeBuilder;
                    z = true;
                    if (htmlTreeBuilder13.inButtonScope("p")) {
                        htmlTreeBuilder13.processEndTag("p");
                    }
                    if (StringUtil.inSorted(htmlTreeBuilder.currentElement().normalName(), Constants.Headings)) {
                        htmlTreeBuilder13.error(this);
                        htmlTreeBuilder.pop();
                    }
                    htmlTreeBuilder13.insert(asStartTag);
                    break;
                case 25:
                    HtmlTreeBuilder htmlTreeBuilder14 = htmlTreeBuilder;
                    z = true;
                    if (htmlTreeBuilder14.inButtonScope("p")) {
                        htmlTreeBuilder14.processEndTag("p");
                    }
                    htmlTreeBuilder14.insertEmpty(asStartTag);
                    htmlTreeBuilder14.framesetOk(false);
                    break;
                case 26:
                    HtmlTreeBuilder htmlTreeBuilder15 = htmlTreeBuilder;
                    z = true;
                    htmlTreeBuilder15.framesetOk(false);
                    ArrayList<Element> stack3 = htmlTreeBuilder.getStack();
                    int size2 = stack3.size() - 1;
                    while (true) {
                        if (size2 > 0) {
                            Element element3 = stack3.get(size2);
                            if (element3.normalName().equals("li")) {
                                htmlTreeBuilder15.processEndTag("li");
                            } else if (!htmlTreeBuilder15.isSpecial(element3) || StringUtil.inSorted(element3.normalName(), Constants.InBodyStartLiBreakers)) {
                                size2--;
                            }
                        }
                    }
                    if (htmlTreeBuilder15.inButtonScope("p")) {
                        htmlTreeBuilder15.processEndTag("p");
                    }
                    htmlTreeBuilder15.insert(asStartTag);
                    break;
                case 27:
                case 28:
                    HtmlTreeBuilder htmlTreeBuilder16 = htmlTreeBuilder;
                    z = true;
                    if (htmlTreeBuilder16.inScope("ruby")) {
                        htmlTreeBuilder.generateImpliedEndTags();
                        if (!htmlTreeBuilder16.currentElementIs("ruby")) {
                            htmlTreeBuilder16.error(this);
                            htmlTreeBuilder16.popStackToBefore("ruby");
                        }
                        htmlTreeBuilder16.insert(asStartTag);
                        break;
                    }
                    break;
                case ' ':
                case '2':
                    HtmlTreeBuilder htmlTreeBuilder17 = htmlTreeBuilder;
                    z = true;
                    if (htmlTreeBuilder17.inButtonScope("p")) {
                        htmlTreeBuilder17.processEndTag("p");
                    }
                    htmlTreeBuilder17.insert(asStartTag);
                    htmlTreeBuilder17.reader.matchConsume("\n");
                    htmlTreeBuilder17.framesetOk(false);
                    break;
                case '!':
                    z = true;
                    htmlTreeBuilder.reconstructFormattingElements();
                    htmlTreeBuilder.insert(asStartTag);
                    break;
                case '#':
                    HtmlTreeBuilder htmlTreeBuilder18 = htmlTreeBuilder;
                    z = true;
                    if (htmlTreeBuilder18.inButtonScope("p")) {
                        htmlTreeBuilder18.processEndTag("p");
                    }
                    htmlTreeBuilder.reconstructFormattingElements();
                    htmlTreeBuilder18.framesetOk(false);
                    HtmlTreeBuilderState.handleRawtext(asStartTag, htmlTreeBuilder18);
                    break;
                case '%':
                    HtmlTreeBuilder htmlTreeBuilder19 = htmlTreeBuilder;
                    z = true;
                    htmlTreeBuilder19.error(this);
                    ArrayList<Element> stack4 = htmlTreeBuilder.getStack();
                    if (stack4.size() == 1) {
                        return false;
                    }
                    if ((stack4.size() <= 2 || stack4.get(1).normalName().equals("body")) && !htmlTreeBuilder19.onStack("template")) {
                        htmlTreeBuilder19.framesetOk(false);
                        if (asStartTag.hasAttributes() && (fromStack = htmlTreeBuilder19.getFromStack("body")) != null) {
                            Iterator<Attribute> it = asStartTag.attributes.iterator();
                            while (it.hasNext()) {
                                Attribute next = it.next();
                                if (!fromStack.hasAttr(next.getKey())) {
                                    fromStack.attributes().put(next);
                                }
                            }
                            break;
                        }
                    } else {
                        return false;
                    }
                    break;
                case '(':
                    HtmlTreeBuilder htmlTreeBuilder20 = htmlTreeBuilder;
                    z = true;
                    if (htmlTreeBuilder.getFormElement() == null || htmlTreeBuilder20.onStack("template")) {
                        if (htmlTreeBuilder20.inButtonScope("p")) {
                            htmlTreeBuilder20.closeElement("p");
                        }
                        htmlTreeBuilder20.insertForm(asStartTag, true, true);
                        break;
                    } else {
                        htmlTreeBuilder20.error(this);
                        return false;
                    }
                    break;
                case ')':
                    HtmlTreeBuilder htmlTreeBuilder21 = htmlTreeBuilder;
                    z = true;
                    htmlTreeBuilder21.error(this);
                    if (!htmlTreeBuilder21.onStack("template")) {
                        if (htmlTreeBuilder.getStack().size() > 0) {
                            Element element4 = htmlTreeBuilder.getStack().get(0);
                            if (asStartTag.hasAttributes()) {
                                Iterator<Attribute> it2 = asStartTag.attributes.iterator();
                                while (it2.hasNext()) {
                                    Attribute next2 = it2.next();
                                    if (!element4.hasAttr(next2.getKey())) {
                                        element4.attributes().put(next2);
                                    }
                                }
                                break;
                            }
                        }
                    } else {
                        return false;
                    }
                    break;
                case '*':
                    z = true;
                    htmlTreeBuilder.reconstructFormattingElements();
                    htmlTreeBuilder.insert(asStartTag);
                    break;
                case '+':
                    HtmlTreeBuilder htmlTreeBuilder22 = htmlTreeBuilder;
                    z = true;
                    htmlTreeBuilder.reconstructFormattingElements();
                    if (htmlTreeBuilder22.inScope("nobr")) {
                        htmlTreeBuilder22.error(this);
                        htmlTreeBuilder22.processEndTag("nobr");
                        htmlTreeBuilder.reconstructFormattingElements();
                    }
                    htmlTreeBuilder22.pushActiveFormattingElements(htmlTreeBuilder22.insert(asStartTag));
                    break;
                case ',':
                    z = true;
                    htmlTreeBuilder.reconstructFormattingElements();
                    htmlTreeBuilder.insert(asStartTag);
                    break;
                case '.':
                    HtmlTreeBuilder htmlTreeBuilder23 = htmlTreeBuilder;
                    z = true;
                    if (htmlTreeBuilder23.getFromStack("svg") != null) {
                        htmlTreeBuilder23.insert(asStartTag);
                        break;
                    } else {
                        return htmlTreeBuilder23.process(asStartTag.name("img"));
                    }
                case '/':
                    HtmlTreeBuilder htmlTreeBuilder24 = htmlTreeBuilder;
                    z = true;
                    htmlTreeBuilder.reconstructFormattingElements();
                    if (!htmlTreeBuilder24.insertEmpty(asStartTag).attr(SVGParser.XML_STYLESHEET_ATTR_TYPE).equalsIgnoreCase("hidden")) {
                        htmlTreeBuilder24.framesetOk(false);
                        break;
                    }
                    break;
                case '1':
                    HtmlTreeBuilder htmlTreeBuilder25 = htmlTreeBuilder;
                    z = true;
                    if (htmlTreeBuilder.getDocument().quirksMode() != Document.QuirksMode.quirks && htmlTreeBuilder25.inButtonScope("p")) {
                        htmlTreeBuilder25.processEndTag("p");
                    }
                    htmlTreeBuilder25.insert(asStartTag);
                    htmlTreeBuilder25.framesetOk(false);
                    htmlTreeBuilder25.transition(InTable);
                    break;
                case '3':
                    HtmlTreeBuilder htmlTreeBuilder26 = htmlTreeBuilder;
                    z = true;
                    if (htmlTreeBuilder26.inButtonScope("p")) {
                        htmlTreeBuilder26.processEndTag("p");
                    }
                    htmlTreeBuilder26.insert(asStartTag);
                    htmlTreeBuilder26.tokeniser.transition(TokeniserState.PLAINTEXT);
                    break;
                case '4':
                    HtmlTreeBuilder htmlTreeBuilder27 = htmlTreeBuilder;
                    z = true;
                    htmlTreeBuilder27.error(this);
                    if (htmlTreeBuilder.getFormElement() == null) {
                        htmlTreeBuilder27.processStartTag("form");
                        if (asStartTag.hasAttribute("action") && (formElement = htmlTreeBuilder.getFormElement()) != null && asStartTag.hasAttribute("action")) {
                            formElement.attributes().put("action", asStartTag.attributes.get("action"));
                        }
                        htmlTreeBuilder27.processStartTag("hr");
                        htmlTreeBuilder27.processStartTag("label");
                        htmlTreeBuilder27.process(new Token.Character().data(asStartTag.hasAttribute("prompt") ? asStartTag.attributes.get("prompt") : "This is a searchable index. Enter search keywords: "));
                        Attributes attributes = new Attributes();
                        if (asStartTag.hasAttributes()) {
                            Iterator<Attribute> it3 = asStartTag.attributes.iterator();
                            while (it3.hasNext()) {
                                Attribute next3 = it3.next();
                                if (!StringUtil.inSorted(next3.getKey(), Constants.InBodyStartInputAttribs)) {
                                    attributes.put(next3);
                                }
                            }
                        }
                        attributes.put("name", "isindex");
                        htmlTreeBuilder27.processStartTag("input", attributes);
                        htmlTreeBuilder27.processEndTag("label");
                        htmlTreeBuilder27.processStartTag("hr");
                        htmlTreeBuilder27.processEndTag("form");
                        break;
                    } else {
                        return false;
                    }
                case '5':
                    z = true;
                    HtmlTreeBuilderState.handleRawtext(asStartTag, htmlTreeBuilder);
                    break;
                default:
                    if (Tag.isKnownTag(normalName)) {
                        HtmlTreeBuilder htmlTreeBuilder28 = htmlTreeBuilder;
                        z = true;
                        if (!StringUtil.inSorted(normalName, Constants.InBodyStartPClosers)) {
                            if (!StringUtil.inSorted(normalName, Constants.InBodyStartToHead)) {
                                if (!StringUtil.inSorted(normalName, Constants.InBodyStartApplets)) {
                                    if (!StringUtil.inSorted(normalName, Constants.InBodyStartMedia)) {
                                        if (!StringUtil.inSorted(normalName, Constants.InBodyStartDrop)) {
                                            htmlTreeBuilder.reconstructFormattingElements();
                                            htmlTreeBuilder28.insert(asStartTag);
                                            break;
                                        } else {
                                            htmlTreeBuilder28.error(this);
                                            return false;
                                        }
                                    } else {
                                        htmlTreeBuilder28.insertEmpty(asStartTag);
                                    }
                                } else {
                                    htmlTreeBuilder.reconstructFormattingElements();
                                    htmlTreeBuilder28.insert(asStartTag);
                                    htmlTreeBuilder.insertMarkerToFormattingElements();
                                    htmlTreeBuilder28.framesetOk(false);
                                }
                            } else {
                                return htmlTreeBuilder28.process(token, InHead);
                            }
                        } else {
                            if (htmlTreeBuilder28.inButtonScope("p")) {
                                htmlTreeBuilder28.processEndTag("p");
                            }
                            htmlTreeBuilder28.insert(asStartTag);
                        }
                    } else {
                        z = true;
                        htmlTreeBuilder.insert(asStartTag);
                    }
                    break;
            }
            return z;
        }

        private boolean inBodyEndTag(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            Token.EndTag asEndTag = token.asEndTag();
            String normalName = asEndTag.normalName();
            normalName.hashCode();
            char c = 65535;
            switch (normalName.hashCode()) {
                case -1321546630:
                    if (normalName.equals("template")) {
                        c = 0;
                        break;
                    }
                    break;
                case 112:
                    if (normalName.equals("p")) {
                        c = 1;
                        break;
                    }
                    break;
                case 3152:
                    if (normalName.equals("br")) {
                        c = 2;
                        break;
                    }
                    break;
                case 3200:
                    if (normalName.equals("dd")) {
                        c = 3;
                        break;
                    }
                    break;
                case 3216:
                    if (normalName.equals("dt")) {
                        c = 4;
                        break;
                    }
                    break;
                case 3273:
                    if (normalName.equals("h1")) {
                        c = 5;
                        break;
                    }
                    break;
                case 3274:
                    if (normalName.equals("h2")) {
                        c = 6;
                        break;
                    }
                    break;
                case 3275:
                    if (normalName.equals("h3")) {
                        c = 7;
                        break;
                    }
                    break;
                case 3276:
                    if (normalName.equals("h4")) {
                        c = 8;
                        break;
                    }
                    break;
                case 3277:
                    if (normalName.equals("h5")) {
                        c = 9;
                        break;
                    }
                    break;
                case 3278:
                    if (normalName.equals("h6")) {
                        c = 10;
                        break;
                    }
                    break;
                case 3453:
                    if (normalName.equals("li")) {
                        c = 11;
                        break;
                    }
                    break;
                case 3029410:
                    if (normalName.equals("body")) {
                        c = 12;
                        break;
                    }
                    break;
                case 3148996:
                    if (normalName.equals("form")) {
                        c = 13;
                        break;
                    }
                    break;
                case 3213227:
                    if (normalName.equals("html")) {
                        c = 14;
                        break;
                    }
                    break;
                case 3536714:
                    if (normalName.equals("span")) {
                        c = 15;
                        break;
                    }
                    break;
                case 1869063452:
                    if (normalName.equals("sarcasm")) {
                        c = 16;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    htmlTreeBuilder.process(token, InHead);
                    break;
                case 1:
                    if (htmlTreeBuilder.inButtonScope(normalName)) {
                        htmlTreeBuilder.generateImpliedEndTags(normalName);
                        if (!htmlTreeBuilder.currentElementIs(normalName)) {
                            htmlTreeBuilder.error(this);
                        }
                        htmlTreeBuilder.popStackToClose(normalName);
                        break;
                    } else {
                        htmlTreeBuilder.error(this);
                        htmlTreeBuilder.processStartTag(normalName);
                        return htmlTreeBuilder.process(asEndTag);
                    }
                case 2:
                    htmlTreeBuilder.error(this);
                    htmlTreeBuilder.processStartTag("br");
                    return false;
                case 3:
                case 4:
                    if (htmlTreeBuilder.inScope(normalName)) {
                        htmlTreeBuilder.generateImpliedEndTags(normalName);
                        if (!htmlTreeBuilder.currentElementIs(normalName)) {
                            htmlTreeBuilder.error(this);
                        }
                        htmlTreeBuilder.popStackToClose(normalName);
                        break;
                    } else {
                        htmlTreeBuilder.error(this);
                        return false;
                    }
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    if (htmlTreeBuilder.inScope(Constants.Headings)) {
                        htmlTreeBuilder.generateImpliedEndTags(normalName);
                        if (!htmlTreeBuilder.currentElementIs(normalName)) {
                            htmlTreeBuilder.error(this);
                        }
                        htmlTreeBuilder.popStackToClose(Constants.Headings);
                        break;
                    } else {
                        htmlTreeBuilder.error(this);
                        return false;
                    }
                case 11:
                    if (htmlTreeBuilder.inListItemScope(normalName)) {
                        htmlTreeBuilder.generateImpliedEndTags(normalName);
                        if (!htmlTreeBuilder.currentElementIs(normalName)) {
                            htmlTreeBuilder.error(this);
                        }
                        htmlTreeBuilder.popStackToClose(normalName);
                        break;
                    } else {
                        htmlTreeBuilder.error(this);
                        return false;
                    }
                case 12:
                    if (htmlTreeBuilder.inScope("body")) {
                        anyOtherEndTag(token, htmlTreeBuilder);
                        htmlTreeBuilder.transition(AfterBody);
                        break;
                    } else {
                        htmlTreeBuilder.error(this);
                        return false;
                    }
                case 13:
                    if (htmlTreeBuilder.onStack("template")) {
                        if (htmlTreeBuilder.inScope(normalName)) {
                            htmlTreeBuilder.generateImpliedEndTags();
                            if (!htmlTreeBuilder.currentElementIs(normalName)) {
                                htmlTreeBuilder.error(this);
                            }
                            htmlTreeBuilder.popStackToClose(normalName);
                            break;
                        } else {
                            htmlTreeBuilder.error(this);
                            return false;
                        }
                    } else {
                        FormElement formElement = htmlTreeBuilder.getFormElement();
                        htmlTreeBuilder.setFormElement((FormElement) null);
                        if (formElement != null && htmlTreeBuilder.inScope(normalName)) {
                            htmlTreeBuilder.generateImpliedEndTags();
                            if (!htmlTreeBuilder.currentElementIs(normalName)) {
                                htmlTreeBuilder.error(this);
                            }
                            htmlTreeBuilder.removeFromStack(formElement);
                            break;
                        } else {
                            htmlTreeBuilder.error(this);
                            return false;
                        }
                    }
                case 14:
                    if (htmlTreeBuilder.processEndTag("body")) {
                        return htmlTreeBuilder.process(asEndTag);
                    }
                    break;
                case 15:
                case 16:
                    return anyOtherEndTag(token, htmlTreeBuilder);
                default:
                    if (StringUtil.inSorted(normalName, Constants.InBodyEndAdoptionFormatters)) {
                        return inBodyEndTagAdoption(token, htmlTreeBuilder);
                    }
                    if (StringUtil.inSorted(normalName, Constants.InBodyEndClosers)) {
                        if (htmlTreeBuilder.inScope(normalName)) {
                            htmlTreeBuilder.generateImpliedEndTags();
                            if (!htmlTreeBuilder.currentElementIs(normalName)) {
                                htmlTreeBuilder.error(this);
                            }
                            htmlTreeBuilder.popStackToClose(normalName);
                            break;
                        } else {
                            htmlTreeBuilder.error(this);
                            return false;
                        }
                    } else if (StringUtil.inSorted(normalName, Constants.InBodyStartApplets)) {
                        if (!htmlTreeBuilder.inScope("name")) {
                            if (htmlTreeBuilder.inScope(normalName)) {
                                htmlTreeBuilder.generateImpliedEndTags();
                                if (!htmlTreeBuilder.currentElementIs(normalName)) {
                                    htmlTreeBuilder.error(this);
                                }
                                htmlTreeBuilder.popStackToClose(normalName);
                                htmlTreeBuilder.clearFormattingElementsToLastMarker();
                                break;
                            } else {
                                htmlTreeBuilder.error(this);
                                return false;
                            }
                        }
                    } else {
                        return anyOtherEndTag(token, htmlTreeBuilder);
                    }
                    break;
            }
            return true;
        }

        /* access modifiers changed from: package-private */
        public boolean anyOtherEndTag(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            String str = token.asEndTag().normalName;
            ArrayList<Element> stack = htmlTreeBuilder.getStack();
            if (htmlTreeBuilder.getFromStack(str) == null) {
                htmlTreeBuilder.error(this);
                return false;
            }
            int size = stack.size() - 1;
            while (true) {
                if (size < 0) {
                    break;
                }
                Element element = stack.get(size);
                if (element.normalName().equals(str)) {
                    htmlTreeBuilder.generateImpliedEndTags(str);
                    if (!htmlTreeBuilder.currentElementIs(str)) {
                        htmlTreeBuilder.error(this);
                    }
                    htmlTreeBuilder.popStackToClose(str);
                } else if (htmlTreeBuilder.isSpecial(element)) {
                    htmlTreeBuilder.error(this);
                    return false;
                } else {
                    size--;
                }
            }
            return true;
        }

        private boolean inBodyEndTagAdoption(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            HtmlTreeBuilder htmlTreeBuilder2 = htmlTreeBuilder;
            String normalName = token.asEndTag().normalName();
            ArrayList<Element> stack = htmlTreeBuilder.getStack();
            boolean z = false;
            int i = 0;
            while (i < 8) {
                Element activeFormattingElement = htmlTreeBuilder2.getActiveFormattingElement(normalName);
                if (activeFormattingElement == null) {
                    return anyOtherEndTag(token, htmlTreeBuilder);
                }
                if (!htmlTreeBuilder2.onStack(activeFormattingElement)) {
                    htmlTreeBuilder2.error(this);
                    htmlTreeBuilder2.removeFromActiveFormattingElements(activeFormattingElement);
                    return true;
                } else if (!htmlTreeBuilder2.inScope(activeFormattingElement.normalName())) {
                    htmlTreeBuilder2.error(this);
                    return z;
                } else {
                    if (htmlTreeBuilder.currentElement() != activeFormattingElement) {
                        htmlTreeBuilder2.error(this);
                    }
                    int size = stack.size();
                    int i2 = -1;
                    Element element = null;
                    boolean z2 = z;
                    int i3 = 1;
                    Element element2 = null;
                    while (true) {
                        if (i3 >= size || i3 >= 64) {
                            break;
                        }
                        Element element3 = stack.get(i3);
                        if (element3 != activeFormattingElement) {
                            if (z2 && htmlTreeBuilder2.isSpecial(element3)) {
                                element = element3;
                                break;
                            }
                        } else {
                            element2 = stack.get(i3 - 1);
                            i2 = htmlTreeBuilder2.positionOfElement(element3);
                            z2 = true;
                        }
                        i3++;
                    }
                    if (element == null) {
                        htmlTreeBuilder2.popStackToClose(activeFormattingElement.normalName());
                        htmlTreeBuilder2.removeFromActiveFormattingElements(activeFormattingElement);
                        return true;
                    }
                    Element element4 = element;
                    Element element5 = element4;
                    for (int i4 = z; i4 < 3; i4++) {
                        if (htmlTreeBuilder2.onStack(element4)) {
                            element4 = htmlTreeBuilder2.aboveOnStack(element4);
                        }
                        if (!htmlTreeBuilder2.isInActiveFormattingElements(element4)) {
                            htmlTreeBuilder2.removeFromStack(element4);
                        } else if (element4 == activeFormattingElement) {
                            break;
                        } else {
                            Element element6 = new Element(htmlTreeBuilder2.tagFor(element4.nodeName(), ParseSettings.preserveCase), htmlTreeBuilder.getBaseUri());
                            htmlTreeBuilder2.replaceActiveFormattingElement(element4, element6);
                            htmlTreeBuilder2.replaceOnStack(element4, element6);
                            if (element5 == element) {
                                i2 = htmlTreeBuilder2.positionOfElement(element6) + 1;
                            }
                            if (element5.parent() != null) {
                                element5.remove();
                            }
                            element6.appendChild(element5);
                            element4 = element6;
                            element5 = element4;
                        }
                    }
                    if (element2 != null) {
                        if (StringUtil.inSorted(element2.normalName(), Constants.InBodyEndTableFosters)) {
                            if (element5.parent() != null) {
                                element5.remove();
                            }
                            htmlTreeBuilder2.insertInFosterParent(element5);
                        } else {
                            if (element5.parent() != null) {
                                element5.remove();
                            }
                            element2.appendChild(element5);
                        }
                    }
                    Element element7 = new Element(activeFormattingElement.tag(), htmlTreeBuilder.getBaseUri());
                    element7.attributes().addAll(activeFormattingElement.attributes());
                    element7.appendChildren(element.childNodes());
                    element.appendChild(element7);
                    htmlTreeBuilder2.removeFromActiveFormattingElements(activeFormattingElement);
                    htmlTreeBuilder2.pushWithBookmark(element7, i2);
                    htmlTreeBuilder2.removeFromStack(activeFormattingElement);
                    htmlTreeBuilder2.insertOnStackAfter(element, element7);
                    i++;
                    z = false;
                }
            }
            return true;
        }
    },
    Text {
        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (token.isCharacter()) {
                htmlTreeBuilder.insert(token.asCharacter());
                return true;
            } else if (token.isEOF()) {
                htmlTreeBuilder.error(this);
                htmlTreeBuilder.pop();
                htmlTreeBuilder.transition(htmlTreeBuilder.originalState());
                return htmlTreeBuilder.process(token);
            } else if (!token.isEndTag()) {
                return true;
            } else {
                htmlTreeBuilder.pop();
                htmlTreeBuilder.transition(htmlTreeBuilder.originalState());
                return true;
            }
        }
    },
    InTable {
        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (token.isCharacter() && StringUtil.inSorted(htmlTreeBuilder.currentElement().normalName(), Constants.InTableFoster)) {
                htmlTreeBuilder.newPendingTableCharacters();
                htmlTreeBuilder.markInsertionMode();
                htmlTreeBuilder.transition(InTableText);
                return htmlTreeBuilder.process(token);
            } else if (token.isComment()) {
                htmlTreeBuilder.insert(token.asComment());
                return true;
            } else if (token.isDoctype()) {
                htmlTreeBuilder.error(this);
                return false;
            } else if (token.isStartTag()) {
                Token.StartTag asStartTag = token.asStartTag();
                String normalName = asStartTag.normalName();
                if (normalName.equals("caption")) {
                    htmlTreeBuilder.clearStackToTableContext();
                    htmlTreeBuilder.insertMarkerToFormattingElements();
                    htmlTreeBuilder.insert(asStartTag);
                    htmlTreeBuilder.transition(InCaption);
                } else if (normalName.equals("colgroup")) {
                    htmlTreeBuilder.clearStackToTableContext();
                    htmlTreeBuilder.insert(asStartTag);
                    htmlTreeBuilder.transition(InColumnGroup);
                } else if (normalName.equals("col")) {
                    htmlTreeBuilder.clearStackToTableContext();
                    htmlTreeBuilder.processStartTag("colgroup");
                    return htmlTreeBuilder.process(token);
                } else if (StringUtil.inSorted(normalName, Constants.InTableToBody)) {
                    htmlTreeBuilder.clearStackToTableContext();
                    htmlTreeBuilder.insert(asStartTag);
                    htmlTreeBuilder.transition(InTableBody);
                } else if (StringUtil.inSorted(normalName, Constants.InTableAddBody)) {
                    htmlTreeBuilder.clearStackToTableContext();
                    htmlTreeBuilder.processStartTag("tbody");
                    return htmlTreeBuilder.process(token);
                } else if (normalName.equals("table")) {
                    htmlTreeBuilder.error(this);
                    if (!htmlTreeBuilder.inTableScope(normalName)) {
                        return false;
                    }
                    htmlTreeBuilder.popStackToClose(normalName);
                    if (htmlTreeBuilder.resetInsertionMode()) {
                        return htmlTreeBuilder.process(token);
                    }
                    htmlTreeBuilder.insert(asStartTag);
                    return true;
                } else if (StringUtil.inSorted(normalName, Constants.InTableToHead)) {
                    return htmlTreeBuilder.process(token, InHead);
                } else {
                    if (normalName.equals("input")) {
                        if (!asStartTag.hasAttributes() || !asStartTag.attributes.get(SVGParser.XML_STYLESHEET_ATTR_TYPE).equalsIgnoreCase("hidden")) {
                            return anythingElse(token, htmlTreeBuilder);
                        }
                        htmlTreeBuilder.insertEmpty(asStartTag);
                    } else if (!normalName.equals("form")) {
                        return anythingElse(token, htmlTreeBuilder);
                    } else {
                        htmlTreeBuilder.error(this);
                        if (htmlTreeBuilder.getFormElement() != null || htmlTreeBuilder.onStack("template")) {
                            return false;
                        }
                        htmlTreeBuilder.insertForm(asStartTag, false, false);
                    }
                }
                return true;
            } else if (token.isEndTag()) {
                String normalName2 = token.asEndTag().normalName();
                if (normalName2.equals("table")) {
                    if (!htmlTreeBuilder.inTableScope(normalName2)) {
                        htmlTreeBuilder.error(this);
                        return false;
                    }
                    htmlTreeBuilder.popStackToClose("table");
                    htmlTreeBuilder.resetInsertionMode();
                } else if (StringUtil.inSorted(normalName2, Constants.InTableEndErr)) {
                    htmlTreeBuilder.error(this);
                    return false;
                } else if (!normalName2.equals("template")) {
                    return anythingElse(token, htmlTreeBuilder);
                } else {
                    htmlTreeBuilder.process(token, InHead);
                }
                return true;
            } else if (!token.isEOF()) {
                return anythingElse(token, htmlTreeBuilder);
            } else {
                if (htmlTreeBuilder.currentElementIs("html")) {
                    htmlTreeBuilder.error(this);
                }
                return true;
            }
        }

        /* access modifiers changed from: package-private */
        public boolean anythingElse(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            htmlTreeBuilder.error(this);
            htmlTreeBuilder.setFosterInserts(true);
            htmlTreeBuilder.process(token, InBody);
            htmlTreeBuilder.setFosterInserts(false);
            return true;
        }
    },
    InTableText {
        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (token.type == Token.TokenType.Character) {
                Token.Character asCharacter = token.asCharacter();
                if (asCharacter.getData().equals(HtmlTreeBuilderState.nullString)) {
                    htmlTreeBuilder.error(this);
                    return false;
                }
                htmlTreeBuilder.getPendingTableCharacters().add(asCharacter.getData());
                return true;
            }
            if (htmlTreeBuilder.getPendingTableCharacters().size() > 0) {
                for (String next : htmlTreeBuilder.getPendingTableCharacters()) {
                    if (!HtmlTreeBuilderState.isWhitespace(next)) {
                        htmlTreeBuilder.error(this);
                        if (StringUtil.inSorted(htmlTreeBuilder.currentElement().normalName(), Constants.InTableFoster)) {
                            htmlTreeBuilder.setFosterInserts(true);
                            htmlTreeBuilder.process(new Token.Character().data(next), InBody);
                            htmlTreeBuilder.setFosterInserts(false);
                        } else {
                            htmlTreeBuilder.process(new Token.Character().data(next), InBody);
                        }
                    } else {
                        htmlTreeBuilder.insert(new Token.Character().data(next));
                    }
                }
                htmlTreeBuilder.newPendingTableCharacters();
            }
            htmlTreeBuilder.transition(htmlTreeBuilder.originalState());
            return htmlTreeBuilder.process(token);
        }
    },
    InCaption {
        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (!token.isEndTag() || !token.asEndTag().normalName().equals("caption")) {
                if ((token.isStartTag() && StringUtil.inSorted(token.asStartTag().normalName(), Constants.InCellCol)) || (token.isEndTag() && token.asEndTag().normalName().equals("table"))) {
                    htmlTreeBuilder.error(this);
                    if (htmlTreeBuilder.processEndTag("caption")) {
                        return htmlTreeBuilder.process(token);
                    }
                    return true;
                } else if (!token.isEndTag() || !StringUtil.inSorted(token.asEndTag().normalName(), Constants.InCaptionIgnore)) {
                    return htmlTreeBuilder.process(token, InBody);
                } else {
                    htmlTreeBuilder.error(this);
                    return false;
                }
            } else if (!htmlTreeBuilder.inTableScope(token.asEndTag().normalName())) {
                htmlTreeBuilder.error(this);
                return false;
            } else {
                htmlTreeBuilder.generateImpliedEndTags();
                if (!htmlTreeBuilder.currentElementIs("caption")) {
                    htmlTreeBuilder.error(this);
                }
                htmlTreeBuilder.popStackToClose("caption");
                htmlTreeBuilder.clearFormattingElementsToLastMarker();
                htmlTreeBuilder.transition(InTable);
                return true;
            }
        }
    },
    InColumnGroup {
        /* access modifiers changed from: package-private */
        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x008d, code lost:
            if (r3.equals("html") == false) goto L_0x0087;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean process(org.jsoup.parser.Token r10, org.jsoup.parser.HtmlTreeBuilder r11) {
            /*
                r9 = this;
                boolean r0 = org.jsoup.parser.HtmlTreeBuilderState.isWhitespace((org.jsoup.parser.Token) r10)
                r1 = 1
                if (r0 == 0) goto L_0x000f
                org.jsoup.parser.Token$Character r10 = r10.asCharacter()
                r11.insert((org.jsoup.parser.Token.Character) r10)
                return r1
            L_0x000f:
                int[] r0 = org.jsoup.parser.HtmlTreeBuilderState.C122625.$SwitchMap$org$jsoup$parser$Token$TokenType
                org.jsoup.parser.Token$TokenType r2 = r10.type
                int r2 = r2.ordinal()
                r0 = r0[r2]
                if (r0 == r1) goto L_0x00c0
                r2 = 2
                if (r0 == r2) goto L_0x00bc
                r3 = 3
                java.lang.String r4 = "html"
                r5 = 0
                java.lang.String r6 = "template"
                if (r0 == r3) goto L_0x0074
                r2 = 4
                if (r0 == r2) goto L_0x003d
                r2 = 6
                if (r0 == r2) goto L_0x0031
                boolean r10 = r9.anythingElse(r10, r11)
                return r10
            L_0x0031:
                boolean r0 = r11.currentElementIs(r4)
                if (r0 == 0) goto L_0x0038
                return r1
            L_0x0038:
                boolean r10 = r9.anythingElse(r10, r11)
                return r10
            L_0x003d:
                org.jsoup.parser.Token$EndTag r0 = r10.asEndTag()
                java.lang.String r0 = r0.normalName()
                r0.hashCode()
                boolean r2 = r0.equals(r6)
                if (r2 != 0) goto L_0x006e
                java.lang.String r2 = "colgroup"
                boolean r2 = r0.equals(r2)
                if (r2 != 0) goto L_0x005b
                boolean r10 = r9.anythingElse(r10, r11)
                return r10
            L_0x005b:
                boolean r10 = r11.currentElementIs(r0)
                if (r10 != 0) goto L_0x0065
                r11.error(r9)
                return r5
            L_0x0065:
                r11.pop()
                org.jsoup.parser.HtmlTreeBuilderState r10 = InTable
                r11.transition(r10)
                goto L_0x00c7
            L_0x006e:
                org.jsoup.parser.HtmlTreeBuilderState r0 = InHead
                r11.process(r10, r0)
                goto L_0x00c7
            L_0x0074:
                org.jsoup.parser.Token$StartTag r0 = r10.asStartTag()
                java.lang.String r3 = r0.normalName()
                r3.hashCode()
                r7 = -1
                int r8 = r3.hashCode()
                switch(r8) {
                    case -1321546630: goto L_0x009b;
                    case 98688: goto L_0x0090;
                    case 3213227: goto L_0x0089;
                    default: goto L_0x0087;
                }
            L_0x0087:
                r2 = r7
                goto L_0x00a3
            L_0x0089:
                boolean r3 = r3.equals(r4)
                if (r3 != 0) goto L_0x00a3
                goto L_0x0087
            L_0x0090:
                java.lang.String r2 = "col"
                boolean r2 = r3.equals(r2)
                if (r2 != 0) goto L_0x0099
                goto L_0x0087
            L_0x0099:
                r2 = r1
                goto L_0x00a3
            L_0x009b:
                boolean r2 = r3.equals(r6)
                if (r2 != 0) goto L_0x00a2
                goto L_0x0087
            L_0x00a2:
                r2 = r5
            L_0x00a3:
                switch(r2) {
                    case 0: goto L_0x00b6;
                    case 1: goto L_0x00b2;
                    case 2: goto L_0x00ab;
                    default: goto L_0x00a6;
                }
            L_0x00a6:
                boolean r10 = r9.anythingElse(r10, r11)
                return r10
            L_0x00ab:
                org.jsoup.parser.HtmlTreeBuilderState r0 = InBody
                boolean r10 = r11.process(r10, r0)
                return r10
            L_0x00b2:
                r11.insertEmpty(r0)
                goto L_0x00c7
            L_0x00b6:
                org.jsoup.parser.HtmlTreeBuilderState r0 = InHead
                r11.process(r10, r0)
                goto L_0x00c7
            L_0x00bc:
                r11.error(r9)
                goto L_0x00c7
            L_0x00c0:
                org.jsoup.parser.Token$Comment r10 = r10.asComment()
                r11.insert((org.jsoup.parser.Token.Comment) r10)
            L_0x00c7:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: org.jsoup.parser.HtmlTreeBuilderState.C121212.process(org.jsoup.parser.Token, org.jsoup.parser.HtmlTreeBuilder):boolean");
        }

        private boolean anythingElse(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (!htmlTreeBuilder.currentElementIs("colgroup")) {
                htmlTreeBuilder.error(this);
                return false;
            }
            htmlTreeBuilder.pop();
            htmlTreeBuilder.transition(InTable);
            htmlTreeBuilder.process(token);
            return true;
        }
    },
    InTableBody {
        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            int i = C122625.$SwitchMap$org$jsoup$parser$Token$TokenType[token.type.ordinal()];
            if (i == 3) {
                Token.StartTag asStartTag = token.asStartTag();
                String normalName = asStartTag.normalName();
                if (normalName.equals("tr")) {
                    htmlTreeBuilder.clearStackToTableBodyContext();
                    htmlTreeBuilder.insert(asStartTag);
                    htmlTreeBuilder.transition(InRow);
                    return true;
                } else if (StringUtil.inSorted(normalName, Constants.InCellNames)) {
                    htmlTreeBuilder.error(this);
                    htmlTreeBuilder.processStartTag("tr");
                    return htmlTreeBuilder.process(asStartTag);
                } else if (StringUtil.inSorted(normalName, Constants.InTableBodyExit)) {
                    return exitTableBody(token, htmlTreeBuilder);
                } else {
                    return anythingElse(token, htmlTreeBuilder);
                }
            } else if (i != 4) {
                return anythingElse(token, htmlTreeBuilder);
            } else {
                String normalName2 = token.asEndTag().normalName();
                if (StringUtil.inSorted(normalName2, Constants.InTableEndIgnore)) {
                    if (!htmlTreeBuilder.inTableScope(normalName2)) {
                        htmlTreeBuilder.error(this);
                        return false;
                    }
                    htmlTreeBuilder.clearStackToTableBodyContext();
                    htmlTreeBuilder.pop();
                    htmlTreeBuilder.transition(InTable);
                    return true;
                } else if (normalName2.equals("table")) {
                    return exitTableBody(token, htmlTreeBuilder);
                } else {
                    if (!StringUtil.inSorted(normalName2, Constants.InTableBodyEndIgnore)) {
                        return anythingElse(token, htmlTreeBuilder);
                    }
                    htmlTreeBuilder.error(this);
                    return false;
                }
            }
        }

        private boolean exitTableBody(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (htmlTreeBuilder.inTableScope("tbody") || htmlTreeBuilder.inTableScope("thead") || htmlTreeBuilder.inScope("tfoot")) {
                htmlTreeBuilder.clearStackToTableBodyContext();
                htmlTreeBuilder.processEndTag(htmlTreeBuilder.currentElement().normalName());
                return htmlTreeBuilder.process(token);
            }
            htmlTreeBuilder.error(this);
            return false;
        }

        private boolean anythingElse(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            return htmlTreeBuilder.process(token, InTable);
        }
    },
    InRow {
        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (token.isStartTag()) {
                Token.StartTag asStartTag = token.asStartTag();
                String normalName = asStartTag.normalName();
                if (StringUtil.inSorted(normalName, Constants.InCellNames)) {
                    htmlTreeBuilder.clearStackToTableRowContext();
                    htmlTreeBuilder.insert(asStartTag);
                    htmlTreeBuilder.transition(InCell);
                    htmlTreeBuilder.insertMarkerToFormattingElements();
                    return true;
                } else if (StringUtil.inSorted(normalName, Constants.InRowMissing)) {
                    return handleMissingTr(token, htmlTreeBuilder);
                } else {
                    return anythingElse(token, htmlTreeBuilder);
                }
            } else if (!token.isEndTag()) {
                return anythingElse(token, htmlTreeBuilder);
            } else {
                String normalName2 = token.asEndTag().normalName();
                if (normalName2.equals("tr")) {
                    if (!htmlTreeBuilder.inTableScope(normalName2)) {
                        htmlTreeBuilder.error(this);
                        return false;
                    }
                    htmlTreeBuilder.clearStackToTableRowContext();
                    htmlTreeBuilder.pop();
                    htmlTreeBuilder.transition(InTableBody);
                    return true;
                } else if (normalName2.equals("table")) {
                    return handleMissingTr(token, htmlTreeBuilder);
                } else {
                    if (StringUtil.inSorted(normalName2, Constants.InTableToBody)) {
                        if (!htmlTreeBuilder.inTableScope(normalName2) || !htmlTreeBuilder.inTableScope("tr")) {
                            htmlTreeBuilder.error(this);
                            return false;
                        }
                        htmlTreeBuilder.clearStackToTableRowContext();
                        htmlTreeBuilder.pop();
                        htmlTreeBuilder.transition(InTableBody);
                        return true;
                    } else if (!StringUtil.inSorted(normalName2, Constants.InRowIgnore)) {
                        return anythingElse(token, htmlTreeBuilder);
                    } else {
                        htmlTreeBuilder.error(this);
                        return false;
                    }
                }
            }
        }

        private boolean anythingElse(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            return htmlTreeBuilder.process(token, InTable);
        }

        private boolean handleMissingTr(Token token, TreeBuilder treeBuilder) {
            if (treeBuilder.processEndTag("tr")) {
                return treeBuilder.process(token);
            }
            return false;
        }
    },
    InCell {
        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (token.isEndTag()) {
                String normalName = token.asEndTag().normalName();
                if (StringUtil.inSorted(normalName, Constants.InCellNames)) {
                    if (!htmlTreeBuilder.inTableScope(normalName)) {
                        htmlTreeBuilder.error(this);
                        htmlTreeBuilder.transition(InRow);
                        return false;
                    }
                    htmlTreeBuilder.generateImpliedEndTags();
                    if (!htmlTreeBuilder.currentElementIs(normalName)) {
                        htmlTreeBuilder.error(this);
                    }
                    htmlTreeBuilder.popStackToClose(normalName);
                    htmlTreeBuilder.clearFormattingElementsToLastMarker();
                    htmlTreeBuilder.transition(InRow);
                    return true;
                } else if (StringUtil.inSorted(normalName, Constants.InCellBody)) {
                    htmlTreeBuilder.error(this);
                    return false;
                } else if (!StringUtil.inSorted(normalName, Constants.InCellTable)) {
                    return anythingElse(token, htmlTreeBuilder);
                } else {
                    if (!htmlTreeBuilder.inTableScope(normalName)) {
                        htmlTreeBuilder.error(this);
                        return false;
                    }
                    closeCell(htmlTreeBuilder);
                    return htmlTreeBuilder.process(token);
                }
            } else if (!token.isStartTag() || !StringUtil.inSorted(token.asStartTag().normalName(), Constants.InCellCol)) {
                return anythingElse(token, htmlTreeBuilder);
            } else {
                if (htmlTreeBuilder.inTableScope("td") || htmlTreeBuilder.inTableScope("th")) {
                    closeCell(htmlTreeBuilder);
                    return htmlTreeBuilder.process(token);
                }
                htmlTreeBuilder.error(this);
                return false;
            }
        }

        private boolean anythingElse(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            return htmlTreeBuilder.process(token, InBody);
        }

        private void closeCell(HtmlTreeBuilder htmlTreeBuilder) {
            if (htmlTreeBuilder.inTableScope("td")) {
                htmlTreeBuilder.processEndTag("td");
            } else {
                htmlTreeBuilder.processEndTag("th");
            }
        }
    },
    InSelect {
        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            switch (C122625.$SwitchMap$org$jsoup$parser$Token$TokenType[token.type.ordinal()]) {
                case 1:
                    htmlTreeBuilder.insert(token.asComment());
                    break;
                case 2:
                    htmlTreeBuilder.error(this);
                    return false;
                case 3:
                    Token.StartTag asStartTag = token.asStartTag();
                    String normalName = asStartTag.normalName();
                    if (normalName.equals("html")) {
                        return htmlTreeBuilder.process(asStartTag, InBody);
                    }
                    if (normalName.equals("option")) {
                        if (htmlTreeBuilder.currentElementIs("option")) {
                            htmlTreeBuilder.processEndTag("option");
                        }
                        htmlTreeBuilder.insert(asStartTag);
                        break;
                    } else if (normalName.equals("optgroup")) {
                        if (htmlTreeBuilder.currentElementIs("option")) {
                            htmlTreeBuilder.processEndTag("option");
                        }
                        if (htmlTreeBuilder.currentElementIs("optgroup")) {
                            htmlTreeBuilder.processEndTag("optgroup");
                        }
                        htmlTreeBuilder.insert(asStartTag);
                        break;
                    } else if (normalName.equals("select")) {
                        htmlTreeBuilder.error(this);
                        return htmlTreeBuilder.processEndTag("select");
                    } else if (StringUtil.inSorted(normalName, Constants.InSelectEnd)) {
                        htmlTreeBuilder.error(this);
                        if (!htmlTreeBuilder.inSelectScope("select")) {
                            return false;
                        }
                        htmlTreeBuilder.processEndTag("select");
                        return htmlTreeBuilder.process(asStartTag);
                    } else if (normalName.equals("script") || normalName.equals("template")) {
                        return htmlTreeBuilder.process(token, InHead);
                    } else {
                        return anythingElse(token, htmlTreeBuilder);
                    }
                    break;
                case 4:
                    String normalName2 = token.asEndTag().normalName();
                    normalName2.hashCode();
                    char c = 65535;
                    switch (normalName2.hashCode()) {
                        case -1321546630:
                            if (normalName2.equals("template")) {
                                c = 0;
                                break;
                            }
                            break;
                        case -1010136971:
                            if (normalName2.equals("option")) {
                                c = 1;
                                break;
                            }
                            break;
                        case -906021636:
                            if (normalName2.equals("select")) {
                                c = 2;
                                break;
                            }
                            break;
                        case -80773204:
                            if (normalName2.equals("optgroup")) {
                                c = 3;
                                break;
                            }
                            break;
                    }
                    switch (c) {
                        case 0:
                            return htmlTreeBuilder.process(token, InHead);
                        case 1:
                            if (!htmlTreeBuilder.currentElementIs("option")) {
                                htmlTreeBuilder.error(this);
                                break;
                            } else {
                                htmlTreeBuilder.pop();
                                break;
                            }
                        case 2:
                            if (htmlTreeBuilder.inSelectScope(normalName2)) {
                                htmlTreeBuilder.popStackToClose(normalName2);
                                htmlTreeBuilder.resetInsertionMode();
                                break;
                            } else {
                                htmlTreeBuilder.error(this);
                                return false;
                            }
                        case 3:
                            if (htmlTreeBuilder.currentElementIs("option") && htmlTreeBuilder.aboveOnStack(htmlTreeBuilder.currentElement()) != null && htmlTreeBuilder.aboveOnStack(htmlTreeBuilder.currentElement()).normalName().equals("optgroup")) {
                                htmlTreeBuilder.processEndTag("option");
                            }
                            if (!htmlTreeBuilder.currentElementIs("optgroup")) {
                                htmlTreeBuilder.error(this);
                                break;
                            } else {
                                htmlTreeBuilder.pop();
                                break;
                            }
                        default:
                            return anythingElse(token, htmlTreeBuilder);
                    }
                case 5:
                    Token.Character asCharacter = token.asCharacter();
                    if (!asCharacter.getData().equals(HtmlTreeBuilderState.nullString)) {
                        htmlTreeBuilder.insert(asCharacter);
                        break;
                    } else {
                        htmlTreeBuilder.error(this);
                        return false;
                    }
                case 6:
                    if (!htmlTreeBuilder.currentElementIs("html")) {
                        htmlTreeBuilder.error(this);
                        break;
                    }
                    break;
                default:
                    return anythingElse(token, htmlTreeBuilder);
            }
            return true;
        }

        private boolean anythingElse(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            htmlTreeBuilder.error(this);
            return false;
        }
    },
    InSelectInTable {
        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (token.isStartTag() && StringUtil.inSorted(token.asStartTag().normalName(), Constants.InSelectTableEnd)) {
                htmlTreeBuilder.error(this);
                htmlTreeBuilder.popStackToClose("select");
                htmlTreeBuilder.resetInsertionMode();
                return htmlTreeBuilder.process(token);
            } else if (!token.isEndTag() || !StringUtil.inSorted(token.asEndTag().normalName(), Constants.InSelectTableEnd)) {
                return htmlTreeBuilder.process(token, InSelect);
            } else {
                htmlTreeBuilder.error(this);
                if (!htmlTreeBuilder.inTableScope(token.asEndTag().normalName())) {
                    return false;
                }
                htmlTreeBuilder.popStackToClose("select");
                htmlTreeBuilder.resetInsertionMode();
                return htmlTreeBuilder.process(token);
            }
        }
    },
    InTemplate {
        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            switch (C122625.$SwitchMap$org$jsoup$parser$Token$TokenType[token.type.ordinal()]) {
                case 1:
                case 2:
                case 5:
                    htmlTreeBuilder.process(token, InBody);
                    break;
                case 3:
                    String normalName = token.asStartTag().normalName();
                    if (StringUtil.inSorted(normalName, Constants.InTemplateToHead)) {
                        htmlTreeBuilder.process(token, InHead);
                        break;
                    } else if (StringUtil.inSorted(normalName, Constants.InTemplateToTable)) {
                        htmlTreeBuilder.popTemplateMode();
                        htmlTreeBuilder.pushTemplateMode(InTable);
                        htmlTreeBuilder.transition(InTable);
                        return htmlTreeBuilder.process(token);
                    } else if (normalName.equals("col")) {
                        htmlTreeBuilder.popTemplateMode();
                        htmlTreeBuilder.pushTemplateMode(InColumnGroup);
                        htmlTreeBuilder.transition(InColumnGroup);
                        return htmlTreeBuilder.process(token);
                    } else if (normalName.equals("tr")) {
                        htmlTreeBuilder.popTemplateMode();
                        htmlTreeBuilder.pushTemplateMode(InTableBody);
                        htmlTreeBuilder.transition(InTableBody);
                        return htmlTreeBuilder.process(token);
                    } else if (normalName.equals("td") || normalName.equals("th")) {
                        htmlTreeBuilder.popTemplateMode();
                        htmlTreeBuilder.pushTemplateMode(InRow);
                        htmlTreeBuilder.transition(InRow);
                        return htmlTreeBuilder.process(token);
                    } else {
                        htmlTreeBuilder.popTemplateMode();
                        htmlTreeBuilder.pushTemplateMode(InBody);
                        htmlTreeBuilder.transition(InBody);
                        return htmlTreeBuilder.process(token);
                    }
                    break;
                case 4:
                    if (token.asEndTag().normalName().equals("template")) {
                        htmlTreeBuilder.process(token, InHead);
                        break;
                    } else {
                        htmlTreeBuilder.error(this);
                        return false;
                    }
                case 6:
                    if (!htmlTreeBuilder.onStack("template")) {
                        return true;
                    }
                    htmlTreeBuilder.error(this);
                    htmlTreeBuilder.popStackToClose("template");
                    htmlTreeBuilder.clearFormattingElementsToLastMarker();
                    htmlTreeBuilder.popTemplateMode();
                    htmlTreeBuilder.resetInsertionMode();
                    if (htmlTreeBuilder.state() == InTemplate || htmlTreeBuilder.templateModeSize() >= 12) {
                        return true;
                    }
                    return htmlTreeBuilder.process(token);
            }
            return true;
        }
    },
    AfterBody {
        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (HtmlTreeBuilderState.isWhitespace(token)) {
                htmlTreeBuilder.insert(token.asCharacter());
                return true;
            } else if (token.isComment()) {
                htmlTreeBuilder.insert(token.asComment());
                return true;
            } else if (token.isDoctype()) {
                htmlTreeBuilder.error(this);
                return false;
            } else if (token.isStartTag() && token.asStartTag().normalName().equals("html")) {
                return htmlTreeBuilder.process(token, InBody);
            } else {
                if (!token.isEndTag() || !token.asEndTag().normalName().equals("html")) {
                    if (token.isEOF()) {
                        return true;
                    }
                    htmlTreeBuilder.error(this);
                    htmlTreeBuilder.resetBody();
                    return htmlTreeBuilder.process(token);
                } else if (htmlTreeBuilder.isFragmentParsing()) {
                    htmlTreeBuilder.error(this);
                    return false;
                } else {
                    if (htmlTreeBuilder.onStack("html")) {
                        htmlTreeBuilder.popStackToClose("html");
                    }
                    htmlTreeBuilder.transition(AfterAfterBody);
                    return true;
                }
            }
        }
    },
    InFrameset {
        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (HtmlTreeBuilderState.isWhitespace(token)) {
                htmlTreeBuilder.insert(token.asCharacter());
            } else if (token.isComment()) {
                htmlTreeBuilder.insert(token.asComment());
            } else if (token.isDoctype()) {
                htmlTreeBuilder.error(this);
                return false;
            } else if (token.isStartTag()) {
                Token.StartTag asStartTag = token.asStartTag();
                String normalName = asStartTag.normalName();
                normalName.hashCode();
                char c = 65535;
                switch (normalName.hashCode()) {
                    case -1644953643:
                        if (normalName.equals("frameset")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 3213227:
                        if (normalName.equals("html")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 97692013:
                        if (normalName.equals(TypedValues.Attributes.S_FRAME)) {
                            c = 2;
                            break;
                        }
                        break;
                    case 1192721831:
                        if (normalName.equals("noframes")) {
                            c = 3;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        htmlTreeBuilder.insert(asStartTag);
                        break;
                    case 1:
                        return htmlTreeBuilder.process(asStartTag, InBody);
                    case 2:
                        htmlTreeBuilder.insertEmpty(asStartTag);
                        break;
                    case 3:
                        return htmlTreeBuilder.process(asStartTag, InHead);
                    default:
                        htmlTreeBuilder.error(this);
                        return false;
                }
            } else if (!token.isEndTag() || !token.asEndTag().normalName().equals("frameset")) {
                if (!token.isEOF()) {
                    htmlTreeBuilder.error(this);
                    return false;
                } else if (!htmlTreeBuilder.currentElementIs("html")) {
                    htmlTreeBuilder.error(this);
                }
            } else if (htmlTreeBuilder.currentElementIs("html")) {
                htmlTreeBuilder.error(this);
                return false;
            } else {
                htmlTreeBuilder.pop();
                if (!htmlTreeBuilder.isFragmentParsing() && !htmlTreeBuilder.currentElementIs("frameset")) {
                    htmlTreeBuilder.transition(AfterFrameset);
                }
            }
            return true;
        }
    },
    AfterFrameset {
        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (HtmlTreeBuilderState.isWhitespace(token)) {
                htmlTreeBuilder.insert(token.asCharacter());
                return true;
            } else if (token.isComment()) {
                htmlTreeBuilder.insert(token.asComment());
                return true;
            } else if (token.isDoctype()) {
                htmlTreeBuilder.error(this);
                return false;
            } else if (token.isStartTag() && token.asStartTag().normalName().equals("html")) {
                return htmlTreeBuilder.process(token, InBody);
            } else {
                if (token.isEndTag() && token.asEndTag().normalName().equals("html")) {
                    htmlTreeBuilder.transition(AfterAfterFrameset);
                    return true;
                } else if (token.isStartTag() && token.asStartTag().normalName().equals("noframes")) {
                    return htmlTreeBuilder.process(token, InHead);
                } else {
                    if (token.isEOF()) {
                        return true;
                    }
                    htmlTreeBuilder.error(this);
                    return false;
                }
            }
        }
    },
    AfterAfterBody {
        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (token.isComment()) {
                htmlTreeBuilder.insert(token.asComment());
                return true;
            } else if (token.isDoctype() || (token.isStartTag() && token.asStartTag().normalName().equals("html"))) {
                return htmlTreeBuilder.process(token, InBody);
            } else {
                if (HtmlTreeBuilderState.isWhitespace(token)) {
                    htmlTreeBuilder.insert(token.asCharacter());
                    return true;
                } else if (token.isEOF()) {
                    return true;
                } else {
                    htmlTreeBuilder.error(this);
                    htmlTreeBuilder.resetBody();
                    return htmlTreeBuilder.process(token);
                }
            }
        }
    },
    AfterAfterFrameset {
        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (token.isComment()) {
                htmlTreeBuilder.insert(token.asComment());
                return true;
            } else if (token.isDoctype() || HtmlTreeBuilderState.isWhitespace(token) || (token.isStartTag() && token.asStartTag().normalName().equals("html"))) {
                return htmlTreeBuilder.process(token, InBody);
            } else {
                if (token.isEOF()) {
                    return true;
                }
                if (token.isStartTag() && token.asStartTag().normalName().equals("noframes")) {
                    return htmlTreeBuilder.process(token, InHead);
                }
                htmlTreeBuilder.error(this);
                return false;
            }
        }
    },
    ForeignContent {
        /* access modifiers changed from: package-private */
        public boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            return true;
        }
    };
    
    /* access modifiers changed from: private */
    public static final String nullString = null;

    /* access modifiers changed from: package-private */
    public abstract boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder);

    static {
        nullString = String.valueOf(0);
    }

    /* renamed from: org.jsoup.parser.HtmlTreeBuilderState$25 */
    static /* synthetic */ class C122625 {
        static final /* synthetic */ int[] $SwitchMap$org$jsoup$parser$Token$TokenType = null;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|14) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                org.jsoup.parser.Token$TokenType[] r0 = org.jsoup.parser.Token.TokenType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$org$jsoup$parser$Token$TokenType = r0
                org.jsoup.parser.Token$TokenType r1 = org.jsoup.parser.Token.TokenType.Comment     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$org$jsoup$parser$Token$TokenType     // Catch:{ NoSuchFieldError -> 0x001d }
                org.jsoup.parser.Token$TokenType r1 = org.jsoup.parser.Token.TokenType.Doctype     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$org$jsoup$parser$Token$TokenType     // Catch:{ NoSuchFieldError -> 0x0028 }
                org.jsoup.parser.Token$TokenType r1 = org.jsoup.parser.Token.TokenType.StartTag     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$org$jsoup$parser$Token$TokenType     // Catch:{ NoSuchFieldError -> 0x0033 }
                org.jsoup.parser.Token$TokenType r1 = org.jsoup.parser.Token.TokenType.EndTag     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$org$jsoup$parser$Token$TokenType     // Catch:{ NoSuchFieldError -> 0x003e }
                org.jsoup.parser.Token$TokenType r1 = org.jsoup.parser.Token.TokenType.Character     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$org$jsoup$parser$Token$TokenType     // Catch:{ NoSuchFieldError -> 0x0049 }
                org.jsoup.parser.Token$TokenType r1 = org.jsoup.parser.Token.TokenType.EOF     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.jsoup.parser.HtmlTreeBuilderState.C122625.<clinit>():void");
        }
    }

    /* access modifiers changed from: private */
    public static boolean isWhitespace(Token token) {
        if (token.isCharacter()) {
            return StringUtil.isBlank(token.asCharacter().getData());
        }
        return false;
    }

    /* access modifiers changed from: private */
    public static boolean isWhitespace(String str) {
        return StringUtil.isBlank(str);
    }

    /* access modifiers changed from: private */
    public static void handleRcData(Token.StartTag startTag, HtmlTreeBuilder htmlTreeBuilder) {
        htmlTreeBuilder.tokeniser.transition(TokeniserState.Rcdata);
        htmlTreeBuilder.markInsertionMode();
        htmlTreeBuilder.transition(Text);
        htmlTreeBuilder.insert(startTag);
    }

    /* access modifiers changed from: private */
    public static void handleRawtext(Token.StartTag startTag, HtmlTreeBuilder htmlTreeBuilder) {
        htmlTreeBuilder.tokeniser.transition(TokeniserState.Rawtext);
        htmlTreeBuilder.markInsertionMode();
        htmlTreeBuilder.transition(Text);
        htmlTreeBuilder.insert(startTag);
    }

    static final class Constants {
        static final String[] AfterHeadBody = null;
        static final String[] BeforeHtmlToHead = null;
        static final String[] DdDt = null;
        static final String[] Headings = null;
        static final String[] InBodyEndAdoptionFormatters = null;
        static final String[] InBodyEndClosers = null;
        static final String[] InBodyEndTableFosters = null;
        static final String[] InBodyStartApplets = null;
        static final String[] InBodyStartDrop = null;
        static final String[] InBodyStartInputAttribs = null;
        static final String[] InBodyStartLiBreakers = null;
        static final String[] InBodyStartMedia = null;
        static final String[] InBodyStartPClosers = null;
        static final String[] InBodyStartToHead = null;
        static final String[] InCaptionIgnore = null;
        static final String[] InCellBody = null;
        static final String[] InCellCol = null;
        static final String[] InCellNames = null;
        static final String[] InCellTable = null;
        static final String[] InHeadEmpty = null;
        static final String[] InHeadEnd = null;
        static final String[] InHeadNoScriptHead = null;
        static final String[] InHeadNoscriptIgnore = null;
        static final String[] InHeadRaw = null;
        static final String[] InRowIgnore = null;
        static final String[] InRowMissing = null;
        static final String[] InSelectEnd = null;
        static final String[] InSelectTableEnd = null;
        static final String[] InTableAddBody = null;
        static final String[] InTableBodyEndIgnore = null;
        static final String[] InTableBodyExit = null;
        static final String[] InTableEndErr = null;
        static final String[] InTableEndIgnore = null;
        static final String[] InTableFoster = null;
        static final String[] InTableToBody = null;
        static final String[] InTableToHead = null;
        static final String[] InTemplateToHead = null;
        static final String[] InTemplateToTable = null;

        Constants() {
        }

        static {
            InHeadEmpty = new String[]{"base", "basefont", "bgsound", "command", "link"};
            InHeadRaw = new String[]{"noframes", "style"};
            InHeadEnd = new String[]{"body", "br", "html"};
            AfterHeadBody = new String[]{"body", "br", "html"};
            BeforeHtmlToHead = new String[]{"body", "br", "head", "html"};
            InHeadNoScriptHead = new String[]{"basefont", "bgsound", "link", "meta", "noframes", "style"};
            InBodyStartToHead = new String[]{"base", "basefont", "bgsound", "command", "link", "meta", "noframes", "script", "style", "template", "title"};
            InBodyStartPClosers = new String[]{"address", "article", "aside", "blockquote", "center", "details", "dir", "div", "dl", "fieldset", "figcaption", "figure", "footer", "header", "hgroup", "menu", "nav", "ol", "p", "section", "summary", "ul"};
            Headings = new String[]{"h1", "h2", "h3", "h4", "h5", "h6"};
            InBodyStartLiBreakers = new String[]{"address", "div", "p"};
            DdDt = new String[]{"dd", "dt"};
            InBodyStartApplets = new String[]{"applet", "marquee", "object"};
            InBodyStartMedia = new String[]{"param", "source", "track"};
            InBodyStartInputAttribs = new String[]{"action", "name", "prompt"};
            InBodyStartDrop = new String[]{"caption", "col", "colgroup", TypedValues.Attributes.S_FRAME, "head", "tbody", "td", "tfoot", "th", "thead", "tr"};
            InBodyEndClosers = new String[]{"address", "article", "aside", "blockquote", "button", "center", "details", "dir", "div", "dl", "fieldset", "figcaption", "figure", "footer", "header", "hgroup", "listing", "menu", "nav", "ol", "pre", "section", "summary", "ul"};
            InBodyEndAdoptionFormatters = new String[]{Proj4Keyword.f420a, Proj4Keyword.f421b, "big", "code", "em", "font", "i", "nobr", AngleFormat.STR_SEC_ABBREV, "small", "strike", "strong", "tt", "u"};
            InBodyEndTableFosters = new String[]{"table", "tbody", "tfoot", "thead", "tr"};
            InTableToBody = new String[]{"tbody", "tfoot", "thead"};
            InTableAddBody = new String[]{"td", "th", "tr"};
            InTableToHead = new String[]{"script", "style", "template"};
            InCellNames = new String[]{"td", "th"};
            InCellBody = new String[]{"body", "caption", "col", "colgroup", "html"};
            InCellTable = new String[]{"table", "tbody", "tfoot", "thead", "tr"};
            InCellCol = new String[]{"caption", "col", "colgroup", "tbody", "td", "tfoot", "th", "thead", "tr"};
            InTableEndErr = new String[]{"body", "caption", "col", "colgroup", "html", "tbody", "td", "tfoot", "th", "thead", "tr"};
            InTableFoster = new String[]{"table", "tbody", "tfoot", "thead", "tr"};
            InTableBodyExit = new String[]{"caption", "col", "colgroup", "tbody", "tfoot", "thead"};
            InTableBodyEndIgnore = new String[]{"body", "caption", "col", "colgroup", "html", "td", "th", "tr"};
            InRowMissing = new String[]{"caption", "col", "colgroup", "tbody", "tfoot", "thead", "tr"};
            InRowIgnore = new String[]{"body", "caption", "col", "colgroup", "html", "td", "th"};
            InSelectEnd = new String[]{"input", "keygen", "textarea"};
            InSelectTableEnd = new String[]{"caption", "table", "tbody", "td", "tfoot", "th", "thead", "tr"};
            InTableEndIgnore = new String[]{"tbody", "tfoot", "thead"};
            InHeadNoscriptIgnore = new String[]{"head", "noscript"};
            InCaptionIgnore = new String[]{"body", "col", "colgroup", "html", "tbody", "td", "tfoot", "th", "thead", "tr"};
            InTemplateToHead = new String[]{"base", "basefont", "bgsound", "link", "meta", "noframes", "script", "style", "template", "title"};
            InTemplateToTable = new String[]{"caption", "colgroup", "tbody", "tfoot", "thead"};
        }
    }
}
