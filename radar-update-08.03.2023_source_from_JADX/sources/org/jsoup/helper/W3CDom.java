package org.jsoup.helper;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;
import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.jsoup.select.Selector;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

public class W3CDom {
    private static final String ContextNodeProperty = "jsoupContextNode";
    private static final String ContextProperty = "jsoupContextSource";
    public static final String SourceProperty = "jsoupSource";
    public static final String XPathFactoryProperty = "javax.xml.xpath.XPathFactory:jsoup";
    protected DocumentBuilderFactory factory;
    private boolean namespaceAware = true;

    public W3CDom() {
        DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
        this.factory = newInstance;
        newInstance.setNamespaceAware(true);
    }

    public boolean namespaceAware() {
        return this.namespaceAware;
    }

    public W3CDom namespaceAware(boolean z) {
        this.namespaceAware = z;
        this.factory.setNamespaceAware(z);
        return this;
    }

    public static Document convert(org.jsoup.nodes.Document document) {
        return new W3CDom().fromJsoup(document);
    }

    public static String asString(Document document, @Nullable Map<String, String> map) {
        try {
            DOMSource dOMSource = new DOMSource(document);
            StringWriter stringWriter = new StringWriter();
            StreamResult streamResult = new StreamResult(stringWriter);
            Transformer newTransformer = TransformerFactory.newInstance().newTransformer();
            if (map != null) {
                newTransformer.setOutputProperties(propertiesFromMap(map));
            }
            if (document.getDoctype() != null) {
                DocumentType doctype = document.getDoctype();
                if (!StringUtil.isBlank(doctype.getPublicId())) {
                    newTransformer.setOutputProperty("doctype-public", doctype.getPublicId());
                }
                if (!StringUtil.isBlank(doctype.getSystemId())) {
                    newTransformer.setOutputProperty("doctype-system", doctype.getSystemId());
                } else if (doctype.getName().equalsIgnoreCase("html") && StringUtil.isBlank(doctype.getPublicId()) && StringUtil.isBlank(doctype.getSystemId())) {
                    newTransformer.setOutputProperty("doctype-system", "about:legacy-compat");
                }
            }
            newTransformer.transform(dOMSource, streamResult);
            return stringWriter.toString();
        } catch (TransformerException e) {
            throw new IllegalStateException(e);
        }
    }

    static Properties propertiesFromMap(Map<String, String> map) {
        Properties properties = new Properties();
        properties.putAll(map);
        return properties;
    }

    public static HashMap<String, String> OutputHtml() {
        return methodMap("html");
    }

    public static HashMap<String, String> OutputXml() {
        return methodMap("xml");
    }

    private static HashMap<String, String> methodMap(String str) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("method", str);
        return hashMap;
    }

    public Document fromJsoup(org.jsoup.nodes.Document document) {
        return fromJsoup((Element) document);
    }

    public Document fromJsoup(Element element) {
        Validate.notNull(element);
        try {
            DocumentBuilder newDocumentBuilder = this.factory.newDocumentBuilder();
            DOMImplementation dOMImplementation = newDocumentBuilder.getDOMImplementation();
            Document newDocument = newDocumentBuilder.newDocument();
            org.jsoup.nodes.Document ownerDocument = element.ownerDocument();
            org.jsoup.nodes.DocumentType documentType = ownerDocument != null ? ownerDocument.documentType() : null;
            if (documentType != null) {
                newDocument.appendChild(dOMImplementation.createDocumentType(documentType.name(), documentType.publicId(), documentType.systemId()));
            }
            newDocument.setXmlStandalone(true);
            newDocument.setUserData(ContextProperty, element instanceof org.jsoup.nodes.Document ? element.child(0) : element, (UserDataHandler) null);
            if (ownerDocument != null) {
                element = ownerDocument;
            }
            convert(element, newDocument);
            return newDocument;
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    public void convert(org.jsoup.nodes.Document document, Document document2) {
        convert((Element) document, document2);
    }

    public void convert(Element element, Document document) {
        W3CBuilder w3CBuilder = new W3CBuilder(document);
        boolean unused = w3CBuilder.namespaceAware = this.namespaceAware;
        org.jsoup.nodes.Document ownerDocument = element.ownerDocument();
        if (ownerDocument != null) {
            if (!StringUtil.isBlank(ownerDocument.location())) {
                document.setDocumentURI(ownerDocument.location());
            }
            Document.OutputSettings.Syntax unused2 = w3CBuilder.syntax = ownerDocument.outputSettings().syntax();
        }
        if (element instanceof org.jsoup.nodes.Document) {
            element = element.child(0);
        }
        NodeTraversor.traverse((NodeVisitor) w3CBuilder, (Node) element);
    }

    public NodeList selectXpath(String str, org.w3c.dom.Document document) {
        return selectXpath(str, (org.w3c.dom.Node) document);
    }

    public NodeList selectXpath(String str, org.w3c.dom.Node node) {
        XPathFactory xPathFactory;
        Validate.notEmptyParam(str, "xpath");
        Validate.notNullParam(node, "contextNode");
        try {
            if (System.getProperty(XPathFactoryProperty) != null) {
                xPathFactory = XPathFactory.newInstance("jsoup");
            } else {
                xPathFactory = XPathFactory.newInstance();
            }
            NodeList nodeList = (NodeList) xPathFactory.newXPath().compile(str).evaluate(node, XPathConstants.NODESET);
            Validate.notNull(nodeList);
            return nodeList;
        } catch (XPathExpressionException | XPathFactoryConfigurationException e) {
            throw new Selector.SelectorParseException("Could not evaluate XPath query [%s]: %s", str, e.getMessage());
        }
    }

    public <T extends Node> List<T> sourceNodes(NodeList nodeList, Class<T> cls) {
        Validate.notNull(nodeList);
        Validate.notNull(cls);
        ArrayList arrayList = new ArrayList(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            Object userData = nodeList.item(i).getUserData(SourceProperty);
            if (cls.isInstance(userData)) {
                arrayList.add((Node) cls.cast(userData));
            }
        }
        return arrayList;
    }

    public org.w3c.dom.Node contextNode(org.w3c.dom.Document document) {
        return (org.w3c.dom.Node) document.getUserData(ContextNodeProperty);
    }

    public String asString(org.w3c.dom.Document document) {
        return asString(document, (Map<String, String>) null);
    }

    protected static class W3CBuilder implements NodeVisitor {
        private static final String xmlnsKey = "xmlns";
        private static final String xmlnsPrefix = "xmlns:";
        @Nullable
        private final Element contextElement;
        private org.w3c.dom.Node dest;
        private final org.w3c.dom.Document doc;
        /* access modifiers changed from: private */
        public boolean namespaceAware = true;
        private final Stack<HashMap<String, String>> namespacesStack;
        /* access modifiers changed from: private */
        public Document.OutputSettings.Syntax syntax;

        public W3CBuilder(org.w3c.dom.Document document) {
            Stack<HashMap<String, String>> stack = new Stack<>();
            this.namespacesStack = stack;
            this.syntax = Document.OutputSettings.Syntax.xml;
            this.doc = document;
            stack.push(new HashMap());
            this.dest = document;
            this.contextElement = (Element) document.getUserData(W3CDom.ContextProperty);
        }

        /* JADX WARNING: Removed duplicated region for block: B:16:0x0058 A[Catch:{ DOMException -> 0x0062 }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void head(org.jsoup.nodes.Node r5, int r6) {
            /*
                r4 = this;
                java.util.Stack<java.util.HashMap<java.lang.String, java.lang.String>> r6 = r4.namespacesStack
                java.util.HashMap r0 = new java.util.HashMap
                java.util.Stack<java.util.HashMap<java.lang.String, java.lang.String>> r1 = r4.namespacesStack
                java.lang.Object r1 = r1.peek()
                java.util.Map r1 = (java.util.Map) r1
                r0.<init>(r1)
                r6.push(r0)
                boolean r6 = r5 instanceof org.jsoup.nodes.Element
                if (r6 == 0) goto L_0x0082
                org.jsoup.nodes.Element r5 = (org.jsoup.nodes.Element) r5
                java.lang.String r6 = r4.updateNamespaces(r5)
                boolean r0 = r4.namespaceAware
                r1 = 0
                if (r0 == 0) goto L_0x0030
                java.util.Stack<java.util.HashMap<java.lang.String, java.lang.String>> r0 = r4.namespacesStack
                java.lang.Object r0 = r0.peek()
                java.util.HashMap r0 = (java.util.HashMap) r0
                java.lang.Object r6 = r0.get(r6)
                java.lang.String r6 = (java.lang.String) r6
                goto L_0x0031
            L_0x0030:
                r6 = r1
            L_0x0031:
                java.lang.String r0 = r5.tagName()
                if (r6 != 0) goto L_0x0048
                java.lang.String r2 = ":"
                boolean r2 = r0.contains(r2)     // Catch:{ DOMException -> 0x0062 }
                if (r2 == 0) goto L_0x0048
                org.w3c.dom.Document r6 = r4.doc     // Catch:{ DOMException -> 0x0062 }
                java.lang.String r2 = ""
                org.w3c.dom.Element r6 = r6.createElementNS(r2, r0)     // Catch:{ DOMException -> 0x0062 }
                goto L_0x004e
            L_0x0048:
                org.w3c.dom.Document r2 = r4.doc     // Catch:{ DOMException -> 0x0062 }
                org.w3c.dom.Element r6 = r2.createElementNS(r6, r0)     // Catch:{ DOMException -> 0x0062 }
            L_0x004e:
                r4.copyAttributes(r5, r6)     // Catch:{ DOMException -> 0x0062 }
                r4.append(r6, r5)     // Catch:{ DOMException -> 0x0062 }
                org.jsoup.nodes.Element r2 = r4.contextElement     // Catch:{ DOMException -> 0x0062 }
                if (r5 != r2) goto L_0x005f
                org.w3c.dom.Document r2 = r4.doc     // Catch:{ DOMException -> 0x0062 }
                java.lang.String r3 = "jsoupContextNode"
                r2.setUserData(r3, r6, r1)     // Catch:{ DOMException -> 0x0062 }
            L_0x005f:
                r4.dest = r6     // Catch:{ DOMException -> 0x0062 }
                goto L_0x00bd
            L_0x0062:
                org.w3c.dom.Document r6 = r4.doc
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r2 = "<"
                r1.append(r2)
                r1.append(r0)
                java.lang.String r0 = ">"
                r1.append(r0)
                java.lang.String r0 = r1.toString()
                org.w3c.dom.Text r6 = r6.createTextNode(r0)
                r4.append(r6, r5)
                goto L_0x00bd
            L_0x0082:
                boolean r6 = r5 instanceof org.jsoup.nodes.TextNode
                if (r6 == 0) goto L_0x0096
                org.jsoup.nodes.TextNode r5 = (org.jsoup.nodes.TextNode) r5
                org.w3c.dom.Document r6 = r4.doc
                java.lang.String r0 = r5.getWholeText()
                org.w3c.dom.Text r6 = r6.createTextNode(r0)
                r4.append(r6, r5)
                goto L_0x00bd
            L_0x0096:
                boolean r6 = r5 instanceof org.jsoup.nodes.Comment
                if (r6 == 0) goto L_0x00aa
                org.jsoup.nodes.Comment r5 = (org.jsoup.nodes.Comment) r5
                org.w3c.dom.Document r6 = r4.doc
                java.lang.String r0 = r5.getData()
                org.w3c.dom.Comment r6 = r6.createComment(r0)
                r4.append(r6, r5)
                goto L_0x00bd
            L_0x00aa:
                boolean r6 = r5 instanceof org.jsoup.nodes.DataNode
                if (r6 == 0) goto L_0x00bd
                org.jsoup.nodes.DataNode r5 = (org.jsoup.nodes.DataNode) r5
                org.w3c.dom.Document r6 = r4.doc
                java.lang.String r0 = r5.getWholeData()
                org.w3c.dom.Text r6 = r6.createTextNode(r0)
                r4.append(r6, r5)
            L_0x00bd:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.jsoup.helper.W3CDom.W3CBuilder.head(org.jsoup.nodes.Node, int):void");
        }

        private void append(org.w3c.dom.Node node, Node node2) {
            node.setUserData(W3CDom.SourceProperty, node2, (UserDataHandler) null);
            this.dest.appendChild(node);
        }

        public void tail(Node node, int i) {
            if ((node instanceof Element) && (this.dest.getParentNode() instanceof org.w3c.dom.Element)) {
                this.dest = this.dest.getParentNode();
            }
            this.namespacesStack.pop();
        }

        private void copyAttributes(Node node, org.w3c.dom.Element element) {
            Iterator<Attribute> it = node.attributes().iterator();
            while (it.hasNext()) {
                Attribute next = it.next();
                String validKey = Attribute.getValidKey(next.getKey(), this.syntax);
                if (validKey != null) {
                    element.setAttribute(validKey, next.getValue());
                }
            }
        }

        private String updateNamespaces(Element element) {
            String str;
            Iterator<Attribute> it = element.attributes().iterator();
            while (true) {
                str = "";
                if (!it.hasNext()) {
                    break;
                }
                Attribute next = it.next();
                String key = next.getKey();
                if (!key.equals(xmlnsKey)) {
                    if (key.startsWith(xmlnsPrefix)) {
                        str = key.substring(6);
                    }
                }
                this.namespacesStack.peek().put(str, next.getValue());
            }
            int indexOf = element.tagName().indexOf(58);
            return indexOf > 0 ? element.tagName().substring(0, indexOf) : str;
        }
    }
}
