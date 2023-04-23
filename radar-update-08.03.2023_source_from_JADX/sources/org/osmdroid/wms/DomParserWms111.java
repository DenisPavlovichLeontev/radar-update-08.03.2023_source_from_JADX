package org.osmdroid.wms;

import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapView;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import p005ar.com.hjg.pngj.chunks.PngChunkTextVar;

public class DomParserWms111 {
    static final String TAG = "osmdroidwms";

    public static WMSEndpoint parse(Element element) throws Exception {
        WMSEndpoint wMSEndpoint = new WMSEndpoint();
        wMSEndpoint.setWmsVersion(element.getAttribute("version"));
        for (int i = 0; i < element.getChildNodes().getLength(); i++) {
            Node item = element.getChildNodes().item(i);
            if (item.getNodeName().contains("Service")) {
                extractService(item, wMSEndpoint);
            } else if (item.getNodeName().contains("Capability")) {
                extractCapability(item, wMSEndpoint);
            }
        }
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < wMSEndpoint.getLayers().size(); i2++) {
            if (wMSEndpoint.getLayers().get(i2).getName() == null) {
                arrayList.add(wMSEndpoint.getLayers().get(i2));
            } else if (wMSEndpoint.getLayers().get(i2).getTitle() == null) {
                wMSEndpoint.getLayers().get(i2).setTitle(wMSEndpoint.getLayers().get(i2).getName());
            }
        }
        wMSEndpoint.getLayers().removeAll(arrayList);
        return wMSEndpoint;
    }

    private static WMSEndpoint extractCapability(Node node, WMSEndpoint wMSEndpoint) {
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node item = node.getChildNodes().item(i);
            String nodeName = item.getNodeName();
            if (nodeName.contains("Request")) {
                parseRequest(item, wMSEndpoint);
            } else if (!nodeName.contains("Exception") && nodeName.contains("Layer")) {
                wMSEndpoint.getLayers().addAll(parseLayers(item));
            }
        }
        return wMSEndpoint;
    }

    private static void parseRequest(Node node, WMSEndpoint wMSEndpoint) {
        WMSEndpoint wMSEndpoint2 = wMSEndpoint;
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node item = node.getChildNodes().item(i);
            if (item.getNodeName().contains("GetCapabilities")) {
                for (int i2 = 0; i2 < item.getChildNodes().getLength(); i2++) {
                    Node item2 = item.getChildNodes().item(i2);
                    if (item2.getNodeName().contains("DCPType")) {
                        for (int i3 = 0; i3 < item2.getChildNodes().getLength(); i3++) {
                            Node item3 = item2.getChildNodes().item(i3);
                            if (item3.getNodeName().contains("HTTP")) {
                                for (int i4 = 0; i4 < item3.getChildNodes().getLength(); i4++) {
                                    Node item4 = item3.getChildNodes().item(i4);
                                    if (item4.getNodeName().contains("Get")) {
                                        for (int i5 = 0; i5 < item4.getChildNodes().getLength(); i5++) {
                                            Node item5 = item4.getChildNodes().item(i5);
                                            if (item5.getNodeName().contains("OnlineResource")) {
                                                Node namedItem = item5.getAttributes().getNamedItem(SVGParser.XML_STYLESHEET_ATTR_HREF);
                                                Node namedItem2 = item5.getAttributes().getNamedItem("xlink:href");
                                                Node namedItemNS = item5.getAttributes().getNamedItemNS("http://www.w3.org/1999/xlink", SVGParser.XML_STYLESHEET_ATTR_HREF);
                                                if (namedItem != null) {
                                                    wMSEndpoint2.setBaseurl(namedItem.getNodeValue());
                                                } else if (namedItem2 != null) {
                                                    wMSEndpoint2.setBaseurl(namedItem2.getNodeValue());
                                                } else if (namedItemNS != null) {
                                                    wMSEndpoint2.setBaseurl(namedItemNS.getNodeValue());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static Collection<? extends WMSLayer> parseLayers(Node node) {
        TileSystem tileSystem = MapView.getTileSystem();
        ArrayList arrayList = new ArrayList();
        WMSLayer wMSLayer = new WMSLayer();
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node item = node.getChildNodes().item(i);
            String nodeName = item.getNodeName();
            if (nodeName.contains("Name")) {
                wMSLayer.setName(item.getTextContent());
            } else if (nodeName.contains(PngChunkTextVar.KEY_Title)) {
                wMSLayer.setTitle(item.getTextContent());
            } else if (nodeName.contains("Abstract")) {
                wMSLayer.setDescription(item.getTextContent());
            } else if (nodeName.contains("SRS")) {
                wMSLayer.getSrs().add(item.getTextContent());
            } else if (nodeName.contains("CRS")) {
                wMSLayer.getSrs().add(item.getTextContent());
            } else if (nodeName.contains("LatLonBoundingBox")) {
                Double valueOf = Double.valueOf(Double.parseDouble(item.getAttributes().getNamedItem("miny").getNodeValue()));
                if (valueOf.doubleValue() < tileSystem.getMinLatitude()) {
                    valueOf = Double.valueOf(tileSystem.getMinLatitude());
                }
                Double valueOf2 = Double.valueOf(Double.parseDouble(item.getAttributes().getNamedItem("maxy").getNodeValue()));
                if (valueOf2.doubleValue() > tileSystem.getMaxLatitude()) {
                    valueOf2 = Double.valueOf(tileSystem.getMaxLatitude());
                }
                BoundingBox boundingBox = r9;
                BoundingBox boundingBox2 = new BoundingBox(valueOf2.doubleValue(), Double.valueOf(Double.parseDouble(item.getAttributes().getNamedItem("minx").getNodeValue())).doubleValue(), valueOf.doubleValue(), Double.valueOf(Double.parseDouble(item.getAttributes().getNamedItem("maxx").getNodeValue())).doubleValue());
                wMSLayer.setBbox(boundingBox);
            } else if (nodeName.contains("BoundingBox") && wMSLayer.getBbox() == null) {
                Node namedItem = item.getAttributes().getNamedItem("CRS");
                if (!(namedItem == null || namedItem.getAttributes() == null)) {
                    Node namedItem2 = namedItem.getAttributes().getNamedItem("maxx");
                    Node namedItem3 = namedItem.getAttributes().getNamedItem("maxy");
                    Node namedItem4 = namedItem.getAttributes().getNamedItem("miny");
                    Node namedItem5 = namedItem.getAttributes().getNamedItem("minx");
                    if ((namedItem2 == null || namedItem3 == null || namedItem5 == null || namedItem4 == null) ? false : true) {
                        if ("EPSG:4326".equals(namedItem.getNodeValue())) {
                            Double valueOf3 = Double.valueOf(Double.parseDouble(namedItem5.getNodeValue()));
                            Double valueOf4 = Double.valueOf(Double.parseDouble(namedItem2.getNodeValue()));
                            Double valueOf5 = Double.valueOf(Double.parseDouble(namedItem3.getNodeValue()));
                            Double valueOf6 = Double.valueOf(Double.parseDouble(namedItem4.getNodeValue()));
                            double doubleValue = valueOf4.doubleValue();
                            double doubleValue2 = valueOf6.doubleValue();
                            double doubleValue3 = valueOf3.doubleValue();
                            double doubleValue4 = valueOf5.doubleValue();
                            BoundingBox boundingBox3 = r9;
                            BoundingBox boundingBox4 = new BoundingBox(doubleValue, doubleValue2, doubleValue3, doubleValue4);
                            wMSLayer.setBbox(boundingBox3);
                        } else if ("CRS:84".equals(namedItem.getNodeValue())) {
                            Double valueOf7 = Double.valueOf(Double.parseDouble(namedItem4.getNodeValue()));
                            Double valueOf8 = Double.valueOf(Double.parseDouble(namedItem3.getNodeValue()));
                            Double valueOf9 = Double.valueOf(Double.parseDouble(namedItem2.getNodeValue()));
                            Double valueOf10 = Double.valueOf(Double.parseDouble(namedItem5.getNodeValue()));
                            double doubleValue5 = valueOf8.doubleValue();
                            double doubleValue6 = valueOf10.doubleValue();
                            double doubleValue7 = valueOf7.doubleValue();
                            double doubleValue8 = valueOf9.doubleValue();
                            BoundingBox boundingBox5 = r9;
                            BoundingBox boundingBox6 = new BoundingBox(doubleValue5, doubleValue6, doubleValue7, doubleValue8);
                            wMSLayer.setBbox(boundingBox5);
                        } else {
                            Log.w(TAG, "warn, unhandled CRS/SRS " + namedItem.getNodeValue());
                        }
                    }
                }
            } else if (nodeName.contains("Style")) {
                for (int i2 = 0; i2 < item.getChildNodes().getLength(); i2++) {
                    Node item2 = item.getChildNodes().item(i2);
                    if ("Name".equals(item2.getNodeName())) {
                        wMSLayer.getStyles().add(item2.getTextContent());
                    }
                }
            } else if (nodeName.contains("Layer")) {
                arrayList.addAll(parseLayers(item));
            }
        }
        Node namedItem6 = node.getAttributes().getNamedItem("fixedHeight");
        Node namedItem7 = node.getAttributes().getNamedItem("fixedWidth");
        if (!(namedItem7 == null || namedItem6 == null)) {
            if (namedItem6.getNodeValue().equals(namedItem7.getNodeValue())) {
                wMSLayer.setPixelSize(Integer.parseInt(namedItem6.getNodeValue()));
            } else {
                Log.w(TAG, "Layer excluded due to non-equal height,width tile sizes");
                return arrayList;
            }
        }
        arrayList.add(wMSLayer);
        return arrayList;
    }

    private static WMSEndpoint extractService(Node node, WMSEndpoint wMSEndpoint) {
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node item = node.getChildNodes().item(i);
            String nodeName = item.getNodeName();
            if (nodeName.contains("Name")) {
                wMSEndpoint.setName(item.getTextContent());
            } else if (nodeName.contains(PngChunkTextVar.KEY_Title)) {
                wMSEndpoint.setTitle(item.getTextContent());
            } else if (nodeName.contains("Abstract")) {
                wMSEndpoint.setDescription(item.getTextContent());
            } else if (nodeName.contains("OnlineResource")) {
                Node namedItem = item.getAttributes().getNamedItem("xlink:href");
                Node namedItem2 = item.getAttributes().getNamedItem(SVGParser.XML_STYLESHEET_ATTR_HREF);
                String str = null;
                if (namedItem != null) {
                    str = namedItem.getNodeValue();
                }
                if (namedItem2 != null) {
                    str = namedItem2.getNodeValue();
                }
                if (str != null) {
                    wMSEndpoint.setBaseurl(str);
                }
            }
        }
        return wMSEndpoint;
    }
}
