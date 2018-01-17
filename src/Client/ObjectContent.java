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
    private boolean encrypted;
    private byte[] data;

    public ObjectContent (){}

    public ObjectContent(String path, String extension, String name, Boolean encrypt, AESSecurity aes) throws Exception {
        this.title = name;
        this.extension = extension;
        this.encrypted = encrypt;
        byte[] raw = getBytes(Paths.get(path));

        if (encrypt) data = aes.encryptFromBytes(raw);
        else data = raw;

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
                    out.write(aes.decryptFromBytes(data));
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
}
