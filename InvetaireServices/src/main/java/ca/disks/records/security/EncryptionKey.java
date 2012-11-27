package ca.disks.records.security;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import org.apache.xml.security.utils.JavaUtils;

public class EncryptionKey {

    private SecretKey encryptKey;

    public EncryptionKey() throws Exception {
        generateEncryptionKey();
    }

    private void generateEncryptionKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
        encryptKey = keyGenerator.generateKey();
    }

    public Key getKey() {
        return encryptKey;
    }

    public void saveToFile(String filePath) throws IOException {
        byte[] rawData = encryptKey.getEncoded();
        FileOutputStream outputStream = new FileOutputStream(new File(filePath));
        outputStream.write(rawData);
        outputStream.close();
    }
    
    public void loadFromFile(String filePath) throws Exception {
        DESedeKeySpec keySpec = new DESedeKeySpec(JavaUtils.getBytesFromFile(filePath));
        SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
        encryptKey = factory.generateSecret(keySpec);
    }
}
