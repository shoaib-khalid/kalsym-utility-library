package com.kalsym.utility;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Muhammad Waqas
 */
public class XMLManipulation {

    static Logger logger = LoggerFactory.getLogger("com.kalsym.utility");

    /**
     * Gets attribute value of the xmlNode, returns empty String if attribute
     * does not exist
     *
     * @param xmlNode
     * @param attributeName
     * @return
     */
    public static String getXmlNodesAttributeValueAsString(Node xmlNode, String attributeName) {
        String attributeValue = "";
        try {
            Node attributeNode = xmlNode.getAttributes().getNamedItem(attributeName.trim());
            attributeValue = attributeNode.getTextContent().trim();
        } catch (Exception exp) {

        }
        return attributeValue;
    }

    /**
     * Reads child element of parent node
     *
     * @param tagName
     * @param subTagName
     * @return
     */
    public static List readSubElement(Document doc, String tagName,
            String subTagName) {

        String tagOneValue = "";
        List tagValue = new ArrayList();
        try {
            NodeList nodeLst = doc.getElementsByTagName(tagName);
            Node fstNode = nodeLst.item(0);
            if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
                Element fstElmnt = (Element) fstNode;
                NodeList fstNmElmntLst2 = fstElmnt.getElementsByTagName(subTagName);
                //System.out.println("length:"+fstNmElmntLst2.getLength());
                for (int s = 0; s < fstNmElmntLst2.getLength(); s++) {
                    Element fstNmElmnt2 = (Element) fstNmElmntLst2.item(s);
                    NodeList fstNm2 = fstNmElmnt2.getChildNodes();
                    tagOneValue = ((Node) fstNm2.item(0)).getNodeValue();
//                    tagOneValue = formatPhoneNumber(tagOneValue, subTagName);
                    tagValue.add(tagOneValue);
                    //System.out.println("Msisdn : "  + ((Node) fstNm2.item(0)).getNodeValue());
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return tagValue;
    }

    /**
     * Gets the First node after evaluating XPath Expression, returns null on
     * any error or if no node is found
     *
     * @param xmlNode
     * @param xPath
     * @return
     */
    public static Node getFirstNodeInEvaluatingXPathExpression(Node xmlNode,
            String xPath) {
        try {
            NodeList nodeList = evaluateXPathExpression(xmlNode, xPath, "");
            if (nodeList != null && nodeList.getLength() > 0) {
                return nodeList.item(0);
            } else {
                return null;
            }
        } catch (Exception exp) {
            return null;
        }
    }

    /**
     * Gets the First node after evaluating XPath Expression, returns null on
     * any error or if no node is found
     *
     * @param xmlDoc
     * @param xPath
     * @return
     */
    public static Node getFirstNodeInEvaluatingXPathExpression(Document xmlDoc,
            String xPath) {
        try {
            NodeList nodeList = evaluateXPathExpression(xmlDoc, xPath, "");
            if (nodeList != null && nodeList.getLength() > 0) {
                return nodeList.item(0);
            } else {
                return null;
            }
        } catch (Exception exp) {
            return null;
        }
    }

    /**
     * Evaluates a valid XPath expression on the given Xml Document object and
     * returns the matched nodes as NodeList object. Returns null on an invalid
     * xpath expression
     *
     * @param doc
     * @param XPathExpression
     * @param refId
     * @return
     */
    public static NodeList evaluateXPathExpression(Document doc,
            String XPathExpression, String refId) {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        XPathExpression expr = null;
        try {
            expr = xpath.compile(XPathExpression);
        } catch (XPathExpressionException ex) {
            //LogProperties.WriteLog("[" + refId + "] Exception in xpath compilation" + ex);
            logger.error("[" + refId + "] Exception in xpath compilation ", ex);
        }
        Object result = null;
        try {
            result = expr.evaluate(doc, XPathConstants.NODESET);
            System.out.println("result " + result);
            System.out.println("result" + result.toString());
        } catch (XPathExpressionException ex) {
//            LogProperties.WriteLog("[" + refId + "] Exception in expression evaluation" + ex);
            logger.error("[" + refId + "] Exception in expression evaluation ", ex);
            System.out.println("[" + refId + "] Exception in expression evaluation" + ex);
        }
        NodeList nodes = (NodeList) result;
        return nodes;
    }

    /**
     * Evaluates a valid XPath expression on the given Xml Node object and
     * returns the matched nodes as NodeList object. Returns null on an invalid
     * xpath expression
     *
     * @param XmlNode
     * @param XPathExpression
     * @param refId
     * @return
     */
    public static NodeList evaluateXPathExpression(Node XmlNode,
            String XPathExpression, String refId) {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        XPathExpression expr = null;
        try {
            expr = xpath.compile(XPathExpression);
        } catch (XPathExpressionException ex) {
            //LogProperties.WriteLog("[" + refId + "] Exception in xpath compilation" + ex);
            logger.error("[" + refId + "] Exception in xpath compilation ", ex);
        }
        Object result = null;
        try {
            result = expr.evaluate(XmlNode, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            //LogProperties.WriteLog("[" + refId + "] Exception in expression evaluation" + ex);
            logger.error("[" + refId + "] Exception in expression evaluation ", ex);
        }
        NodeList nodes = (NodeList) result;
        return nodes;
    }

    /**
     * Reads Xml from a file, from teh file path provided. Returns null if file
     * does not exist or the document is not well formed
     *
     * @param XmlFilePath
     * @return
     */
    public static Document readXML(String XmlFilePath) {
        try {
            File file = new File(XmlFilePath);
            //TODO: careate the file from the xml recieved, in the temp directory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);

            doc.getDocumentElement().normalize();

            return doc;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Reads Xml as String and returns a DOM xml doument object. Returns null if
     * the string is not well formed
     *
     * @param XMLStirng
     * @return
     */
    public static Document loadXML(String XMLStirng) throws Exception {
        XMLStirng = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + XMLStirng;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            InputSource in = new InputSource(new StringReader(XMLStirng));
            Document document = builder.parse(in);
            return document;
        } catch (ParserConfigurationException | SAXException | IOException e) {
//            throw e;babar
            System.out.println(e);
            logger.error("Error while loading XML ", e);
            return null;
        }

    }

    /**
     * Gets inner xml of the xml node object as String.
     *
     * @param node
     * @return
     */
    public static String getInnerXml(Node node) {
        DOMImplementationLS lsImpl = (DOMImplementationLS) node.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
        LSSerializer lsSerializer = lsImpl.createLSSerializer();
        lsSerializer.getDomConfig().setParameter("xml-declaration", false);
        NodeList childNodes = node.getChildNodes();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < childNodes.getLength(); i++) {
            sb.append(lsSerializer.writeToString(childNodes.item(i)));
        }
        return sb.toString();
    }

    /**
     * Gets outer xml of the xml node object, as String. Outer xml contains the
     * start and end tags of the node as well
     *
     * @param node
     * @return
     */
    public static String getOuterXml(Node node) {
        DOMImplementationLS lsImpl = (DOMImplementationLS) node.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
        LSSerializer lsSerializer = lsImpl.createLSSerializer();
        lsSerializer.getDomConfig().setParameter("xml-declaration", false);
        StringBuilder sb = new StringBuilder();
        sb.append(lsSerializer.writeToString(node));
        return sb.toString();
    }

    /**
     * Gets inner text of the xml node, without the tags
     *
     * @param ParentNode
     * @param AddSpaces
     * @return
     */
    public static String getInnerText(Node ParentNode, boolean AddSpaces) {
        NodeList childNodes = ParentNode.getChildNodes();
        String innerTextWithSpaces = "";
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node nChild = childNodes.item(i);
            if (nChild.getNodeType() == 3)//TextELEMENT Node)
            {
                if (AddSpaces) {
                    innerTextWithSpaces += nChild.getTextContent() + " ";
                } else {
                    innerTextWithSpaces += nChild.getTextContent();
                }
            }
        }
        System.out.println(innerTextWithSpaces);
        return innerTextWithSpaces.trim();
    }

    /**
     * Adds the given string to an xml element and gets its inner text
     *
     * @param XmlFragment
     * @return
     */
    public static String getInnerTextFromString(String XmlFragment) throws IOException, SAXException, ParserConfigurationException {
        try {
            Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(XmlFragment.getBytes())).getDocumentElement();
            String innerText = getInnerText(node, true);
            return innerText;
        } catch (ParserConfigurationException ex) {
            throw ex;
        } catch (SAXException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        }
    }
}
