package Client;

import SecurityLayer.AESSecurity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ObjectContent implements Serializable {

    private static final long serialVersionUID = 1L;
    private String title;
    private String extension;
    private byte[] data;

    public ObjectContent (){}

    public ObjectContent(String path, String extension, String name, AESSecurity aes) throws Exception {
        this.title = name;
        this.extension = extension;
        byte[] raw = getBytes(Paths.get(path));
        data = aes.encryptFromBytes(raw);
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
            byte[] decrypted = aes.decryptFromBytes(data);
            if (decrypted != null) {
                out.write(aes.decryptFromBytes(data));
            }
            out.close();
        } catch (IOException e) {
            System.out.println("WRITE ERROR: content doesn't exist");
        }
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
}
