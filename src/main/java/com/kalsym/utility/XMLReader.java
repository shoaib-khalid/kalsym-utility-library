package com.kalsym.utility;

/**
 *
 * @author taufik
 */
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLReader {

    Document doc;

    InputStream inputStream = null;

    String xmlString = "";

    public XMLReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public XMLReader(String xmlString) {
        this.xmlString = xmlString;
    }

    public void load() throws Exception {
        if (this.inputStream == null && this.xmlString == "") {
            throw new Exception("InputStream and/or XmlString is null");
        } else if (this.inputStream != null) {
            loadXmlFromStream();
        } else{
            loadXmlFromString();
        }
    }

    /**
     * Loads input stream into private document
     *
     */
    private void loadXmlFromStream() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(this.inputStream);
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException e) {

        } catch (SAXException e) {
        } catch (IOException e) {
        }
    }

    /**
     * Loads XML string into private variable doc
     *
     */
    private void loadXmlFromString() {
        try {
            InputStream is = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(is);
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException e) {
            //e.printStackTrace();
        } catch (SAXException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    /**
     * Reads the element from xmldoc, returns empty string if element not found
     *
     * @param tagName
     * @return
     */
    public String readOneElement(String tagName) {
        String tagValue = "";
        try {
            NodeList lstNmElmntLst = doc.getElementsByTagName(tagName);
            Element fstNmElmnt = (Element) lstNmElmntLst.item(0);
            NodeList fstNm = fstNmElmnt.getChildNodes();
            tagValue = ((Node) fstNm.item(0)).getNodeValue();
//            tagValue = formatPhoneNumber(tagValue, tagName);
            System.out.println("Tag Value : " + tagValue);
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return tagValue;
    }

    /**
     * Reads tagName array from xml document and returns in a List
     *
     * @param tagName
     * @return
     */
    public List readArrayElement(String tagName) {
        List tagValue = new ArrayList();
        String tagOneValue = "";
        try {
            NodeList lstNmElmntLst = doc.getElementsByTagName(tagName);
            for (int s = 0; s < lstNmElmntLst.getLength(); s++) {
                Element fstNmElmnt = (Element) lstNmElmntLst.item(s);
                NodeList fstNm = fstNmElmnt.getChildNodes();
                tagOneValue = ((Node) fstNm.item(0)).getNodeValue();
//                tagOneValue = formatPhoneNumber(tagOneValue, tagName);
                tagValue.add(tagOneValue);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return tagValue;
    }

    /**
     * Reads child element of parent node
     *
     * @param tagName
     * @param subTagName
     * @return
     */
    public List readSubElement(String tagName, String subTagName) {

        String tagOneValue = "";
        List tagValue = new ArrayList();
        try {
            NodeList nodeLst = doc.getElementsByTagName(tagName);
            Node fstNode = nodeLst.item(0);
            if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
                Element fstElmnt = (Element) fstNode;
                NodeList fstNmElmntLst2 = fstElmnt.getElementsByTagName(subTagName);
                for (int s = 0; s < fstNmElmntLst2.getLength(); s++) {
                    Element fstNmElmnt2 = (Element) fstNmElmntLst2.item(s);
                    NodeList fstNm2 = fstNmElmnt2.getChildNodes();
                    tagOneValue = ((Node) fstNm2.item(0)).getNodeValue();
//                    tagOneValue = formatPhoneNumber(tagOneValue, subTagName);
                    tagValue.add(tagOneValue);
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return tagValue;
    }
//
//    /**
//     * Formats phone
//     *
//     * @param tagValue
//     * @param tagName
//     * @return
//     */
//    private String formatPhoneNumber(String tagValue, String tagName) {
//        if (tagName.equalsIgnoreCase("parentMsisdn") || tagName.equalsIgnoreCase("childMsisdn")) {
//            if (tagValue.length() < 10) {
//                tagValue = tagValue;
//            } else if (tagValue.startsWith("0") == true) {
//                //03
//                tagValue = "92" + tagValue.substring(1);
//            } else if (tagValue.startsWith("92") == true) {
//                //923
//                tagValue = tagValue;
//            } else {
//                //33
//                tagValue = "92" + tagValue;
//            }
//        } else if (tagName.equalsIgnoreCase("blackListMsisdn") || tagName.equalsIgnoreCase("whiteListMsisdn")) {
//            if (tagValue.length() < 10) {
//                tagValue = tagValue;
//            } else if (tagValue.startsWith("0") == true) {
//                //03
//                tagValue = "92" + tagValue.substring(1);
//            } else {
//                //33
//                tagValue = tagValue;
//            }
//        }
//        return tagValue;
//    }
}
