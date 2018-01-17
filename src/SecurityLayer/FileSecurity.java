package SecurityLayer;

public interface FileSecurity {

    String encryptFromString(String data) throws Exception;
    String decryptFromString(String encryptedData) throws Exception;
    byte[] encryptFromBytes(byte[] data) throws Exception;
    byte[] decryptFromBytes(byte[] data) throws Exception;


}
