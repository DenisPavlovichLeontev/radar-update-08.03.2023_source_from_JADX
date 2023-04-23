package org.mapsforge.map.rendertheme.rule;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;
import mil.nga.geopackage.schema.constraints.DataColumnConstraints;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.util.IOUtils;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderThemeStyleLayer;
import org.mapsforge.map.rendertheme.XmlRenderThemeStyleMenu;
import org.mapsforge.map.rendertheme.XmlUtils;
import org.mapsforge.map.rendertheme.renderinstruction.Area;
import org.mapsforge.map.rendertheme.renderinstruction.Caption;
import org.mapsforge.map.rendertheme.renderinstruction.Circle;
import org.mapsforge.map.rendertheme.renderinstruction.Hillshading;
import org.mapsforge.map.rendertheme.renderinstruction.Line;
import org.mapsforge.map.rendertheme.renderinstruction.LineSymbol;
import org.mapsforge.map.rendertheme.renderinstruction.PathText;
import org.mapsforge.map.rendertheme.renderinstruction.RenderInstruction;
import org.mapsforge.map.rendertheme.renderinstruction.Symbol;
import org.xmlpull.p018v1.XmlPullParser;
import org.xmlpull.p018v1.XmlPullParserException;
import org.xmlpull.p018v1.XmlPullParserFactory;

public final class RenderThemeHandler {
    private static final String ELEMENT_NAME_RULE = "rule";
    private static final Logger LOGGER = Logger.getLogger(RenderThemeHandler.class.getName());
    private static final String UNEXPECTED_ELEMENT = "unexpected element: ";
    private static XmlPullParserFactory xmlPullParserFactory = null;
    private Set<String> categories;
    private XmlRenderThemeStyleLayer currentLayer;
    private Rule currentRule;
    private final DisplayModel displayModel;
    private final Stack<Element> elementStack = new Stack<>();
    private final GraphicFactory graphicFactory;
    private int level;
    private final XmlPullParser pullParser;
    private String qName;
    private final String relativePathPrefix;
    private RenderTheme renderTheme;
    private XmlRenderThemeStyleMenu renderThemeStyleMenu;
    private final Stack<Rule> ruleStack = new Stack<>();
    private Map<String, Symbol> symbols = new HashMap();
    private final XmlRenderTheme xmlRenderTheme;

    private enum Element {
        RENDER_THEME,
        RENDERING_INSTRUCTION,
        RULE,
        RENDERING_STYLE
    }

    public static RenderTheme getRenderTheme(GraphicFactory graphicFactory2, DisplayModel displayModel2, XmlRenderTheme xmlRenderTheme2) throws IOException, XmlPullParserException {
        InputStream inputStream;
        Throwable th;
        XmlPullParser newPullParser = getXmlPullParserFactory().newPullParser();
        RenderThemeHandler renderThemeHandler = new RenderThemeHandler(graphicFactory2, displayModel2, xmlRenderTheme2.getRelativePathPrefix(), xmlRenderTheme2, newPullParser);
        try {
            inputStream = xmlRenderTheme2.getRenderThemeAsStream();
            try {
                newPullParser.setInput(inputStream, (String) null);
                renderThemeHandler.processRenderTheme();
                RenderTheme renderTheme2 = renderThemeHandler.renderTheme;
                IOUtils.closeQuietly(inputStream);
                return renderTheme2;
            } catch (Throwable th2) {
                th = th2;
                IOUtils.closeQuietly(inputStream);
                throw th;
            }
        } catch (Throwable th3) {
            Throwable th4 = th3;
            inputStream = null;
            th = th4;
            IOUtils.closeQuietly(inputStream);
            throw th;
        }
    }

    public static XmlPullParserFactory getXmlPullParserFactory() throws XmlPullParserException {
        if (xmlPullParserFactory == null) {
            xmlPullParserFactory = XmlPullParserFactory.newInstance();
        }
        return xmlPullParserFactory;
    }

    public static void setXmlPullParserFactory(XmlPullParserFactory xmlPullParserFactory2) {
        xmlPullParserFactory = xmlPullParserFactory2;
    }

    private RenderThemeHandler(GraphicFactory graphicFactory2, DisplayModel displayModel2, String str, XmlRenderTheme xmlRenderTheme2, XmlPullParser xmlPullParser) {
        this.pullParser = xmlPullParser;
        this.graphicFactory = graphicFactory2;
        this.displayModel = displayModel2;
        this.relativePathPrefix = str;
        this.xmlRenderTheme = xmlRenderTheme2;
    }

    public void processRenderTheme() throws XmlPullParserException, IOException {
        int eventType = this.pullParser.getEventType();
        do {
            if (eventType != 0) {
                if (eventType == 2) {
                    startElement();
                } else if (eventType == 3) {
                    endElement();
                }
            }
            eventType = this.pullParser.next();
        } while (eventType != 1);
        endDocument();
    }

    private void endDocument() {
        RenderTheme renderTheme2 = this.renderTheme;
        if (renderTheme2 != null) {
            renderTheme2.setLevels(this.level);
            this.renderTheme.complete();
            return;
        }
        throw new IllegalArgumentException("missing element: rules");
    }

    private void endElement() {
        this.qName = this.pullParser.getName();
        this.elementStack.pop();
        if (ELEMENT_NAME_RULE.equals(this.qName)) {
            this.ruleStack.pop();
            if (!this.ruleStack.empty()) {
                this.currentRule = this.ruleStack.peek();
            } else if (isVisible(this.currentRule)) {
                this.renderTheme.addRule(this.currentRule);
            }
        } else if ("stylemenu".equals(this.qName) && this.xmlRenderTheme.getMenuCallback() != null) {
            this.categories = this.xmlRenderTheme.getMenuCallback().getCategories(this.renderThemeStyleMenu);
        }
    }

    private void startElement() throws XmlPullParserException {
        XmlRenderThemeStyleLayer layer;
        String name = this.pullParser.getName();
        this.qName = name;
        try {
            if ("rendertheme".equals(name)) {
                checkState(this.qName, Element.RENDER_THEME);
                this.renderTheme = new RenderThemeBuilder(this.graphicFactory, this.displayModel, this.qName, this.pullParser).build();
            } else if (ELEMENT_NAME_RULE.equals(this.qName)) {
                checkState(this.qName, Element.RULE);
                Rule build = new RuleBuilder(this.qName, this.pullParser, this.ruleStack).build();
                if (!this.ruleStack.empty() && isVisible(build)) {
                    this.currentRule.addSubRule(build);
                }
                this.currentRule = build;
                this.ruleStack.push(build);
            } else if ("area".equals(this.qName)) {
                checkState(this.qName, Element.RENDERING_INSTRUCTION);
                GraphicFactory graphicFactory2 = this.graphicFactory;
                DisplayModel displayModel2 = this.displayModel;
                String str = this.qName;
                XmlPullParser xmlPullParser = this.pullParser;
                int i = this.level;
                this.level = i + 1;
                Area area = new Area(graphicFactory2, displayModel2, str, xmlPullParser, i, this.relativePathPrefix);
                if (isVisible((RenderInstruction) area)) {
                    this.currentRule.addRenderingInstruction(area);
                }
            } else if ("caption".equals(this.qName)) {
                checkState(this.qName, Element.RENDERING_INSTRUCTION);
                Caption caption = new Caption(this.graphicFactory, this.displayModel, this.qName, this.pullParser, this.symbols);
                if (isVisible((RenderInstruction) caption)) {
                    this.currentRule.addRenderingInstruction(caption);
                }
            } else if ("cat".equals(this.qName)) {
                checkState(this.qName, Element.RENDERING_STYLE);
                this.currentLayer.addCategory(getStringAttribute("id"));
            } else if ("circle".equals(this.qName)) {
                checkState(this.qName, Element.RENDERING_INSTRUCTION);
                GraphicFactory graphicFactory3 = this.graphicFactory;
                DisplayModel displayModel3 = this.displayModel;
                String str2 = this.qName;
                XmlPullParser xmlPullParser2 = this.pullParser;
                int i2 = this.level;
                this.level = i2 + 1;
                Circle circle = new Circle(graphicFactory3, displayModel3, str2, xmlPullParser2, i2);
                if (isVisible((RenderInstruction) circle)) {
                    this.currentRule.addRenderingInstruction(circle);
                }
            } else {
                boolean z = false;
                if ("layer".equals(this.qName)) {
                    checkState(this.qName, Element.RENDERING_STYLE);
                    if (getStringAttribute("enabled") != null) {
                        z = Boolean.valueOf(getStringAttribute("enabled")).booleanValue();
                    }
                    this.currentLayer = this.renderThemeStyleMenu.createLayer(getStringAttribute("id"), Boolean.valueOf(getStringAttribute("visible")).booleanValue(), z);
                    String stringAttribute = getStringAttribute("parent");
                    if (stringAttribute != null && (layer = this.renderThemeStyleMenu.getLayer(stringAttribute)) != null) {
                        for (String addCategory : layer.getCategories()) {
                            this.currentLayer.addCategory(addCategory);
                        }
                        for (XmlRenderThemeStyleLayer addOverlay : layer.getOverlays()) {
                            this.currentLayer.addOverlay(addOverlay);
                        }
                    }
                } else if ("line".equals(this.qName)) {
                    checkState(this.qName, Element.RENDERING_INSTRUCTION);
                    GraphicFactory graphicFactory4 = this.graphicFactory;
                    DisplayModel displayModel4 = this.displayModel;
                    String str3 = this.qName;
                    XmlPullParser xmlPullParser3 = this.pullParser;
                    int i3 = this.level;
                    this.level = i3 + 1;
                    Line line = new Line(graphicFactory4, displayModel4, str3, xmlPullParser3, i3, this.relativePathPrefix);
                    if (isVisible((RenderInstruction) line)) {
                        this.currentRule.addRenderingInstruction(line);
                    }
                } else if ("lineSymbol".equals(this.qName)) {
                    checkState(this.qName, Element.RENDERING_INSTRUCTION);
                    LineSymbol lineSymbol = new LineSymbol(this.graphicFactory, this.displayModel, this.qName, this.pullParser, this.relativePathPrefix);
                    if (isVisible((RenderInstruction) lineSymbol)) {
                        this.currentRule.addRenderingInstruction(lineSymbol);
                    }
                } else if ("name".equals(this.qName)) {
                    checkState(this.qName, Element.RENDERING_STYLE);
                    this.currentLayer.addTranslation(getStringAttribute("lang"), getStringAttribute(DataColumnConstraints.COLUMN_VALUE));
                } else if ("overlay".equals(this.qName)) {
                    checkState(this.qName, Element.RENDERING_STYLE);
                    XmlRenderThemeStyleLayer layer2 = this.renderThemeStyleMenu.getLayer(getStringAttribute("id"));
                    if (layer2 != null) {
                        this.currentLayer.addOverlay(layer2);
                    }
                } else if ("pathText".equals(this.qName)) {
                    checkState(this.qName, Element.RENDERING_INSTRUCTION);
                    PathText pathText = new PathText(this.graphicFactory, this.displayModel, this.qName, this.pullParser);
                    if (isVisible((RenderInstruction) pathText)) {
                        this.currentRule.addRenderingInstruction(pathText);
                    }
                } else if ("stylemenu".equals(this.qName)) {
                    checkState(this.qName, Element.RENDERING_STYLE);
                    this.renderThemeStyleMenu = new XmlRenderThemeStyleMenu(getStringAttribute("id"), getStringAttribute("defaultlang"), getStringAttribute("defaultvalue"));
                } else if ("symbol".equals(this.qName)) {
                    checkState(this.qName, Element.RENDERING_INSTRUCTION);
                    Symbol symbol = new Symbol(this.graphicFactory, this.displayModel, this.qName, this.pullParser, this.relativePathPrefix);
                    if (isVisible((RenderInstruction) symbol)) {
                        this.currentRule.addRenderingInstruction(symbol);
                    }
                    String id = symbol.getId();
                    if (id != null) {
                        this.symbols.put(id, symbol);
                    }
                } else if ("hillshading".equals(this.qName)) {
                    checkState(this.qName, Element.RULE);
                    String str4 = null;
                    boolean z2 = false;
                    byte b = 5;
                    byte b2 = 17;
                    short s = 64;
                    byte b3 = 5;
                    for (int i4 = 0; i4 < this.pullParser.getAttributeCount(); i4++) {
                        String attributeName = this.pullParser.getAttributeName(i4);
                        String attributeValue = this.pullParser.getAttributeValue(i4);
                        if ("cat".equals(attributeName)) {
                            str4 = attributeValue;
                        } else if ("zoom-min".equals(attributeName)) {
                            b3 = XmlUtils.parseNonNegativeByte("zoom-min", attributeValue);
                        } else if ("zoom-max".equals(attributeName)) {
                            b2 = XmlUtils.parseNonNegativeByte("zoom-max", attributeValue);
                        } else if ("magnitude".equals(attributeName)) {
                            s = (short) XmlUtils.parseNonNegativeInteger("magnitude", attributeValue);
                            if (s > 255) {
                                throw new XmlPullParserException("Attribute 'magnitude' must not be > 255");
                            }
                        } else if ("always".equals(attributeName)) {
                            z2 = Boolean.valueOf(attributeValue).booleanValue();
                        } else if ("layer".equals(attributeName)) {
                            b = XmlUtils.parseNonNegativeByte("layer", attributeValue);
                        }
                    }
                    int i5 = this.level;
                    this.level = i5 + 1;
                    Hillshading hillshading = new Hillshading(b3, b2, s, b, z2, i5, this.graphicFactory);
                    Set<String> set = this.categories;
                    if (set == null || str4 == null || set.contains(str4)) {
                        this.renderTheme.addHillShadings(hillshading);
                    }
                } else {
                    throw new XmlPullParserException("unknown element: " + this.qName);
                }
            }
        } catch (IOException e) {
            LOGGER.warning("Rendertheme missing or invalid resource " + e.getMessage());
        }
    }

    /* renamed from: org.mapsforge.map.rendertheme.rule.RenderThemeHandler$1 */
    static /* synthetic */ class C13301 {

        /* renamed from: $SwitchMap$org$mapsforge$map$rendertheme$rule$RenderThemeHandler$Element */
        static final /* synthetic */ int[] f399x3c9777ea;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                org.mapsforge.map.rendertheme.rule.RenderThemeHandler$Element[] r0 = org.mapsforge.map.rendertheme.rule.RenderThemeHandler.Element.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f399x3c9777ea = r0
                org.mapsforge.map.rendertheme.rule.RenderThemeHandler$Element r1 = org.mapsforge.map.rendertheme.rule.RenderThemeHandler.Element.RENDER_THEME     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f399x3c9777ea     // Catch:{ NoSuchFieldError -> 0x001d }
                org.mapsforge.map.rendertheme.rule.RenderThemeHandler$Element r1 = org.mapsforge.map.rendertheme.rule.RenderThemeHandler.Element.RULE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f399x3c9777ea     // Catch:{ NoSuchFieldError -> 0x0028 }
                org.mapsforge.map.rendertheme.rule.RenderThemeHandler$Element r1 = org.mapsforge.map.rendertheme.rule.RenderThemeHandler.Element.RENDERING_INSTRUCTION     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = f399x3c9777ea     // Catch:{ NoSuchFieldError -> 0x0033 }
                org.mapsforge.map.rendertheme.rule.RenderThemeHandler$Element r1 = org.mapsforge.map.rendertheme.rule.RenderThemeHandler.Element.RENDERING_STYLE     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.rendertheme.rule.RenderThemeHandler.C13301.<clinit>():void");
        }
    }

    private void checkElement(String str, Element element) throws XmlPullParserException {
        int i = C13301.f399x3c9777ea[element.ordinal()];
        if (i != 1) {
            if (i == 2) {
                Element peek = this.elementStack.peek();
                if (peek != Element.RENDER_THEME && peek != Element.RULE) {
                    throw new XmlPullParserException(UNEXPECTED_ELEMENT + str);
                }
            } else if (i != 3) {
                if (i != 4) {
                    throw new XmlPullParserException("unknown enum value: " + element);
                }
            } else if (this.elementStack.peek() != Element.RULE) {
                throw new XmlPullParserException(UNEXPECTED_ELEMENT + str);
            }
        } else if (!this.elementStack.empty()) {
            throw new XmlPullParserException(UNEXPECTED_ELEMENT + str);
        }
    }

    private void checkState(String str, Element element) throws XmlPullParserException {
        checkElement(str, element);
        this.elementStack.push(element);
    }

    private String getStringAttribute(String str) {
        int attributeCount = this.pullParser.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            if (this.pullParser.getAttributeName(i).equals(str)) {
                return this.pullParser.getAttributeValue(i);
            }
        }
        return null;
    }

    private boolean isVisible(RenderInstruction renderInstruction) {
        return this.categories == null || renderInstruction.getCategory() == null || this.categories.contains(renderInstruction.getCategory());
    }

    private boolean isVisible(Rule rule) {
        return this.categories == null || rule.cat == null || this.categories.contains(rule.cat);
    }
}
