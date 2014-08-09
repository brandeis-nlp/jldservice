package org.jldservice.server;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.rmi.RemoteException;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.net.URL;
import java.net.Proxy;
import java.net.URLConnection;
import java.io.StringWriter;
import java.io.FileInputStream;
import java.io.File;
import java.util.AbstractList;
import java.util.List;
import java.io.StringReader;
import java.net.MalformedURLException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ser.BeanSerializerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;

/**
 *
 * Provide interface for mapping identity to services.
 *
 * @author Chunqi Shi(chqshi@gmail.com)
 *
 */
public class WSDLClient {

    public static final void main(String[] args) throws Exception {
        WSDLClient ws = new WSDLClient();
        ws.init(new URL(
                "http://129.64.55.184:8080/service_manager/wsdl/AnotherLappLandGrid:AnotherExclamationService"));
        ws.authorize("marc", "marc");
        String s = ws.callService("addExclamation", "xxx").toString();
        System.out.println(s);
    }


    Service service = null;
    Call call = null;
    ServiceConf conf = null;

    final public ServiceConf getConf() {
        return conf;
    }


    String wsdl = null;
    XPathUtils xpath = null;

    public void init(String url) throws ServiceException {
        try {
            init(new URL(url));
        }catch(MalformedURLException e){
            throw new ServiceException(e);
        }
    }

    public void init(URL url) throws ServiceException {
        Service service = new Service();
        call = (Call) service.createCall();
        conf = new ServiceConf();
        try {
            StringWriter writer = new StringWriter();
            IOUtils.copy(url.openStream(), writer);
            conf.setWsdlAddress(url.toString());
            wsdl = writer.toString();
            xpath = XPathUtils.newInstance(wsdl);
            conf.setSoapAddress(xpath
                    .getText("definitions/service/port/address/@location"));
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public void init(File file) throws ServiceException {
        Service service = new Service();
        call = (Call) service.createCall();
        conf = new ServiceConf();
        try {
            StringWriter writer = new StringWriter();
            IOUtils.copy(new FileInputStream(file), writer);
            conf.setWsdlAddress(file.getAbsolutePath());
            wsdl = writer.toString();
            xpath = XPathUtils.newInstance(wsdl);
            conf.setSoapAddress(xpath
                    .getText("definitions/service/port/address/@location"));
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public void init(URL url, Proxy proxy) throws ServiceException {
        Service service = new Service();
        call = (Call) service.createCall();
        conf = new ServiceConf();
        try {
            StringWriter writer = new StringWriter();
            URLConnection con = url.openConnection(proxy);
            IOUtils.copy(con.getInputStream(), writer);
            conf.setWsdlAddress(url.toString());
            wsdl = writer.toString();
            xpath = XPathUtils.newInstance(wsdl);
            conf.setSoapAddress(xpath
                    .getText("definitions/service/port/address/@location"));
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public void register(@SuppressWarnings("rawtypes") Class cls, QName qname) {
//        call.registerTypeMapping(cls, qname, BeanSerializerFactory.class,
//                BeanDeserializerFactory.class);
        call.registerTypeMapping(cls, qname,
                new org.apache.axis.encoding.ser.BeanSerializerFactory(cls, qname),
                new org.apache.axis.encoding.ser.BeanDeserializerFactory(cls, qname));
    }


    public void authorize(String username, String password) {
        call.setUsername(username);
        call.setPassword(password);
        conf.setUsername(username);
        conf.setPassword(password);
    }

    public Object callService(String operationName, Object... params)
            throws MalformedURLException, RemoteException {
        call.setTargetEndpointAddress(new java.net.URL(conf.getSoapAddress()));
        // prepare operator & parameters.
        // method.
        call.setOperationName(new QName(null, operationName));
        // parameters.
        return call.invoke(params);
    }

    public String[] getParameterOrder(String operation) {
        String list = null;
        if (operation == null) {
            list = xpath
                    .getText("definitions/portType/operation/@parameterOrder");

        } else {
            list = xpath
                    .getText("definitions/portType/operation[@name='"
                    + operation + "']/@parameterOrder");
        }
        if (list == null || list.length() == 0) {
            return new String []{};
        }
        // System.out.println("list=" + list + ".");
        return list.split("\\s+");
    }

    public static class XPathUtils {
        public static XPathUtils newInstance(String obj) {
            XPathUtils instance = new XPathUtils();
            try {
                instance.init(obj);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return instance;
        }

        public void init(String obj) throws SAXException, IOException,
                ParserConfigurationException {
            if (obj != null) {
                if (obj.indexOf('<') > -1) {
                    this.doc = parse(new InputSource(new StringReader(obj)));
                } else {
                    this.xmlPath = obj;
                    this.doc = parse(obj);
                }
            }
        }

        public String getXmlPath() {
            return xmlPath;
        }

        public Document getDoc() {
            return doc;
        }

        String xmlPath;
        Document doc;

        public String getText(String xpath) {
            return getText(doc, xpath);
        }

        public Node getNode(String xpath) {
            return getNode(doc, xpath);
        }

        public List<Node> getNodes(String xpath) {
            return getNodes(doc, xpath);
        }

        public Map<String, String> getAttrs(String xpath) {
            return getAttrs(getNode(xpath));
        }

        public static Document parse(String xmlPath) throws SAXException,
                IOException, ParserConfigurationException {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(xmlPath);
        }

        public static Document parse(InputSource is) throws SAXException,
                IOException, ParserConfigurationException {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(is);
        }

        public static XPathExpression xpath(String xpath)
                throws XPathExpressionException {
            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();
            XPathExpression xPathExpression = xPath.compile(xpath);
            return xPathExpression;
        }

        public static String getText(Node node, String xpath) {
            if (node == null || xpath == null) {
                return null;
            }
            try {
                XPathExpression xPathExpression = xpath(xpath);
                String result = (String) xPathExpression.evaluate(node,
                        XPathConstants.STRING);
                if (result != null) {
                    return result.trim();
                } else {
                    return null;
                }
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static Node getNode(Node node, String xpath) {
            if (node == null || xpath == null) {
                return null;
            }
            try {
                XPathExpression xPathExpression = xpath(xpath);
                return (Node) xPathExpression.evaluate(node,
                        XPathConstants.NODE);
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static List<Node> getNodes(Node node, String xpath) {
            if (node == null || xpath == null) {
                return null;
            }
            try {
                XPathExpression xPathExpression = xpath(xpath);
                final DTMNodeList nodes = (DTMNodeList) xPathExpression
                        .evaluate(node, XPathConstants.NODESET);

                if (nodes == null) {
                    return null;
                }

                return new AbstractList<Node>() {
                    @Override
                    public Node get(int index) {
                        return nodes.item(index);
                    }

                    @Override
                    public int size() {
                        return nodes.getLength();
                    }
                };
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static Map<String, String> getAttrs(Node node) {
            if (node == null) {
                return null;
            }
            Map<String, String> map = new HashMap<String, String>();
            NamedNodeMap nnm = node.getAttributes();
            for (int i = 0; i < nnm.getLength(); i++) {
                Attr attr = (Attr) nnm.item(i);
                map.put(attr.getName(), attr.getValue().trim());
            }
            return map;
        }
    }

    public static class ServiceConf {

        public String getWsdlAddress() {
            return wsdlAddress;
        }

        public void setWsdlAddress(String wsdlAddress) {
            this.wsdlAddress = wsdlAddress;
        }

        protected String name;
        protected String username;
        protected String password;
        protected String wsdlAddress;
        protected String soapAddress;

        public String getSoapAddress() {
            return this.soapAddress;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setSoapAddress(String soapAddress) {
            this.soapAddress = soapAddress;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String toString() {
            return toString(this);
        }

        public static String toString(Object obj) {
            StringBuilder result = new StringBuilder();
            result.append("{");
            // determine fields declared in this class only (no fields of
            // superclass)
            Field[] fields = obj.getClass().getDeclaredFields();
            // print field names paired with their values
            for (Field field : fields) {
                try {
                    if (Modifier.isStatic(field.getModifiers())
                            || Modifier.isFinal(field.getModifiers())
                            || Modifier.isPrivate(field.getModifiers())) {
                        continue;
                    }
                    result.append(field.getName());
                    result.append(":");
                    // requires access to private field:
                    result.append(field.get(obj));
                } catch (IllegalAccessException ex) {
                    System.out.println(ex);
                }
                result.append(";\t");
            }
            result.append("}.");
            return result.toString();
        }
    }
}
