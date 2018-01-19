package DataTransferProtocol;

import SecurityLayer.AESSecurity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ObjectContent implements Serializable {

    private static final long serialVersionUID = 1L;
    private String title;
    private String extension;
    private String user;
    private boolean encrypted;
    private byte[] data;
    private AESSecurity aes;

    public ObjectContent (){}

    public ObjectContent(String path, String extension, String name, Boolean encrypt, AESSecurity aes, String user) throws Exception {
        this.title = name;
        this.extension = extension;
        this.encrypted = encrypt;
        this.user = user;
        byte[] raw = getBytes(Paths.get(path));
        this.aes = aes;
        if (encrypt) data = aes.encryptFromBytes(raw);
        else data = raw;

    }

    public ObjectContent(ObjectContent obj, String path) throws Exception {
        this.title = obj.getTitle();
        this.extension = obj.getExtension();
        this.encrypted = obj.isEncrypted();
        this.user = obj.getUser();
        byte[] raw = getBytes(Paths.get(path));
        this.aes = obj.getAes();
        if (encrypted) data = aes.encryptFromBytes(raw);
        else data = raw;

    }

    public ObjectContent(ObjectContent obj) throws Exception {
        this.title = obj.getTitle();
        this.extension = obj.getExtension();
        this.encrypted = obj.isEncrypted();
        this.user = obj.getUser();
        this.aes = obj.getAes();
        byte[] raw = obj.getData();

    }

    private byte[] getBytes(Path filePath) throws IOException {
        return Files.readAllBytes(filePath);
    }

    public void writeFile(String savePath, AESSecurity aes) throws Exception {
        File multimedia_file = new File(savePath + title + "." + extension);
        multimedia_file.createNewFile();
        OutputStream out;
        try {
            out = new FileOutputStream(savePath + title + "." + extension);
            if(encrypted) {
                byte[] decrypted = aes.decryptFromBytes(data);

                if (contentNotNull(decrypted)) {
                    out.write(decrypted);
                }

                out.close();
            } else {

                if (contentNotNull(data)) {
                    out.write(data);
                }

                out.close();
            }
        } catch (IOException e) {
            System.out.println("WRITE ERROR: content doesn't exist");
        }
    }


    public AESSecurity getAes(){ return aes;}

    private boolean contentNotNull(byte[] content) {
        return content != null;
    }

    public String getTitle() {
        return title;
    }

    public String getExtension () {return extension; };

    public void setTitle(String title) {
        this.title = title;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }
}
