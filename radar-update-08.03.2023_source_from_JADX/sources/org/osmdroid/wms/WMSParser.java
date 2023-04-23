package org.osmdroid.wms;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class WMSParser {
    public static WMSEndpoint parse(InputStream inputStream) throws Exception {
        DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        newDocumentBuilder.setEntityResolver(new EntityResolver() {
            public InputSource resolveEntity(String str, String str2) throws SAXException, IOException {
                return new InputSource(new StringReader(""));
            }
        });
        Element documentElement = newDocumentBuilder.parse(inputStream).getDocumentElement();
        documentElement.normalize();
        if (documentElement.getNodeName().contains("WMT_MS_Capabilities")) {
            return DomParserWms111.parse(documentElement);
        }
        if (documentElement.getNodeName().contains("WMS_Capabilities")) {
            return DomParserWms111.parse(documentElement);
        }
        throw new IllegalArgumentException("Unknown root element: " + documentElement.getNodeName());
    }
}
