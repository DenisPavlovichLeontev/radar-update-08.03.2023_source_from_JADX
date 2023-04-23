package org.kxml2.kdom;

import java.io.IOException;
import java.util.Vector;
import org.xmlpull.p018v1.XmlPullParser;
import org.xmlpull.p018v1.XmlPullParserException;
import org.xmlpull.p018v1.XmlSerializer;

public class Element extends Node {
    protected Vector attributes;
    protected String name;
    protected String namespace;
    protected Node parent;
    protected Vector prefixes;

    public void clear() {
        this.attributes = null;
        this.children = null;
    }

    public Element createElement(String str, String str2) {
        Node node = this.parent;
        return node == null ? super.createElement(str, str2) : node.createElement(str, str2);
    }

    public int getAttributeCount() {
        Vector vector = this.attributes;
        if (vector == null) {
            return 0;
        }
        return vector.size();
    }

    public String getAttributeName(int i) {
        return ((String[]) this.attributes.elementAt(i))[1];
    }

    public String getAttributeNamespace(int i) {
        return ((String[]) this.attributes.elementAt(i))[0];
    }

    public String getAttributeValue(int i) {
        return ((String[]) this.attributes.elementAt(i))[2];
    }

    public String getAttributeValue(String str, String str2) {
        for (int i = 0; i < getAttributeCount(); i++) {
            if (str2.equals(getAttributeName(i)) && (str == null || str.equals(getAttributeNamespace(i)))) {
                return getAttributeValue(i);
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public int getNamespaceCount() {
        Vector vector = this.prefixes;
        if (vector == null) {
            return 0;
        }
        return vector.size();
    }

    public String getNamespacePrefix(int i) {
        return ((String[]) this.prefixes.elementAt(i))[0];
    }

    public String getNamespaceUri(int i) {
        return ((String[]) this.prefixes.elementAt(i))[1];
    }

    public String getNamespaceUri(String str) {
        int namespaceCount = getNamespaceCount();
        for (int i = 0; i < namespaceCount; i++) {
            if (str == getNamespacePrefix(i) || (str != null && str.equals(getNamespacePrefix(i)))) {
                return getNamespaceUri(i);
            }
        }
        Node node = this.parent;
        if (node instanceof Element) {
            return ((Element) node).getNamespaceUri(str);
        }
        return null;
    }

    public Node getParent() {
        return this.parent;
    }

    public Node getRoot() {
        Element element = this;
        while (true) {
            Node node = element.parent;
            if (node == null) {
                return element;
            }
            if (!(node instanceof Element)) {
                return node;
            }
            element = (Element) node;
        }
    }

    public void init() {
    }

    public void parse(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        for (int namespaceCount = xmlPullParser.getNamespaceCount(xmlPullParser.getDepth() - 1); namespaceCount < xmlPullParser.getNamespaceCount(xmlPullParser.getDepth()); namespaceCount++) {
            setPrefix(xmlPullParser.getNamespacePrefix(namespaceCount), xmlPullParser.getNamespaceUri(namespaceCount));
        }
        for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
            setAttribute(xmlPullParser.getAttributeNamespace(i), xmlPullParser.getAttributeName(i), xmlPullParser.getAttributeValue(i));
        }
        init();
        boolean isEmptyElementTag = xmlPullParser.isEmptyElementTag();
        xmlPullParser.nextToken();
        if (!isEmptyElementTag) {
            super.parse(xmlPullParser);
            if (getChildCount() == 0) {
                addChild(7, "");
            }
        }
        xmlPullParser.require(3, getNamespace(), getName());
        xmlPullParser.nextToken();
    }

    public void setAttribute(String str, String str2, String str3) {
        if (this.attributes == null) {
            this.attributes = new Vector();
        }
        if (str == null) {
            str = "";
        }
        int size = this.attributes.size() - 1;
        while (size >= 0) {
            String[] strArr = (String[]) this.attributes.elementAt(size);
            if (!strArr[0].equals(str) || !strArr[1].equals(str2)) {
                size--;
            } else if (str3 == null) {
                this.attributes.removeElementAt(size);
                return;
            } else {
                strArr[2] = str3;
                return;
            }
        }
        this.attributes.addElement(new String[]{str, str2, str3});
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setNamespace(String str) {
        if (str != null) {
            this.namespace = str;
            return;
        }
        throw new NullPointerException("Use \"\" for empty namespace");
    }

    /* access modifiers changed from: protected */
    public void setParent(Node node) {
        this.parent = node;
    }

    public void setPrefix(String str, String str2) {
        if (this.prefixes == null) {
            this.prefixes = new Vector();
        }
        this.prefixes.addElement(new String[]{str, str2});
    }

    public void write(XmlSerializer xmlSerializer) throws IOException {
        if (this.prefixes != null) {
            for (int i = 0; i < this.prefixes.size(); i++) {
                xmlSerializer.setPrefix(getNamespacePrefix(i), getNamespaceUri(i));
            }
        }
        xmlSerializer.startTag(getNamespace(), getName());
        int attributeCount = getAttributeCount();
        for (int i2 = 0; i2 < attributeCount; i2++) {
            xmlSerializer.attribute(getAttributeNamespace(i2), getAttributeName(i2), getAttributeValue(i2));
        }
        writeChildren(xmlSerializer);
        xmlSerializer.endTag(getNamespace(), getName());
    }
}
