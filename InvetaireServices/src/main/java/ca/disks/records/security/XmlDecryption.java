package ca.disks.records.security;

import java.security.Key;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.utils.EncryptionConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class XmlDecryption {

    private Document document;
    
    public XmlDecryption() {
        org.apache.xml.security.Init.init();
    }

    public void decryptXmlDocument(String path,Key encryptionKey) throws Exception {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        documentFactory.setNamespaceAware(true);
        DocumentBuilder builder = documentFactory.newDocumentBuilder();
        document = builder.parse(path);
        decryptDocument(encryptionKey);
    }
    
    private void decryptDocument(Key encryptionKey) throws Exception {
        XMLCipher cipher = XMLCipher.getInstance();
        cipher.init(XMLCipher.DECRYPT_MODE, null);
        cipher.setKEK(encryptionKey);
        cipher.doFinal(document, getEncryptedData());
    }
    
    private Element getEncryptedData() {
        String namespaceURI = EncryptionConstants.EncryptionSpecNS;
        String localName = EncryptionConstants._TAG_ENCRYPTEDDATA;
        return (Element) document.getElementsByTagNameNS(namespaceURI, localName).item(0);
    }
    
    public Document getDocument(){
        return document;
    }
}