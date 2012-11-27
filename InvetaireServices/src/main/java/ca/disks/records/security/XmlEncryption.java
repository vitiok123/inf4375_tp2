package ca.disks.records.security;

import java.io.File;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.keys.KeyInfo;
import org.w3c.dom.Document;


public class XmlEncryption {

    private Document document;
    
    public XmlEncryption(Document doc) {
        org.apache.xml.security.Init.init();
        document = doc;
    }
    
    public void encryptXmlDocument(Key encryptionKey) throws Exception {
        encryptDocument(encryptionKey);
    }
    
    private void encryptDocument(Key encryptionKey) throws Exception {
        Key symmetricKey = generateSymmetricKey();
        EncryptedKey encryptedKey = getEncryptedKey(encryptionKey, symmetricKey);
        
        XMLCipher symmetricCipher = XMLCipher.getInstance(XMLCipher.AES_128);
        symmetricCipher.init(XMLCipher.ENCRYPT_MODE, symmetricKey);

        EncryptedData encryptedData = symmetricCipher.getEncryptedData();
        KeyInfo info = new KeyInfo(document);
        info.add(encryptedKey);
        encryptedData.setKeyInfo(info);

        symmetricCipher.doFinal(document, document.getDocumentElement(), true);
    }
    
    private EncryptedKey getEncryptedKey(Key encryptionKey, Key symmetricKey) throws Exception {
        XMLCipher keyCipher = XMLCipher.getInstance(XMLCipher.TRIPLEDES_KeyWrap);
        keyCipher.init(XMLCipher.WRAP_MODE, encryptionKey);
        return keyCipher.encryptKey(document, symmetricKey);
    }


    private Key generateSymmetricKey() throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        return generator.generateKey();
    }
    
    public Document getDocument(){
        return document;
    }
}