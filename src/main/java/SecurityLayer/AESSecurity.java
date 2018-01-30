package SecurityLayer;

import java.security.Key;
import javax.crypto.Cipher;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;
import javax.crypto.spec.SecretKeySpec;

public class AESSecurity implements FileSecurity {


    private static final String algorithm = "AES";
    private static byte[] keyValue;

    public AESSecurity(String password) {
        this.keyValue = password.getBytes();
    }

    @Override
    public String encryptFromString(String data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(algorithm);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return new BASE64Encoder().encode(encVal);
    }
    @Override
    public String decryptFromString(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(algorithm);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
    }

    @Override
    public byte[] encryptFromBytes(byte[] data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(algorithm);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data);
        return encVal;
    }

    @Override
    public byte[] decryptFromBytes(byte[] data) throws Exception {
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(algorithm);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decValue = c.doFinal(data);
            return decValue;

        } catch (Exception e) {
            System.out.println("Wrong decryption key.");
            return null;
        }
    }

    private static Key generateKey() throws Exception {
        return new SecretKeySpec(keyValue, algorithm);
    }
    

}

