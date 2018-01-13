package Client;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * Created by arnau on 11/11/2017.
 */
public class ObjectContent implements Serializable {


    private static final long serialVersionUID = 1L;
    private String title;
    private int duration;
    private String category;
    private String extension;
    private byte[] data;


    public ObjectContent (){}

    public ObjectContent(String path, String extension, String name)  {
        this.title = name;
        this.extension = extension;
        try{
            Path filePath = Paths.get(path);
            data = Files.readAllBytes(filePath);
        } catch (IOException e){
            System.out.println("READ ERROR: content doesn't exist");
            this.title = null;
        }
    }
    public ObjectContent(String title, int duration, String category) {
        this.title = title;
        this.duration = duration;
        this.category = category;
    }


    public void writeFile(String savePath) throws IOException {
        File multimedia_file = new File(savePath + title + "." + extension);
        multimedia_file.createNewFile();
        OutputStream out;
        try {
            out = new FileOutputStream(savePath + title + "." + extension);
            out.write(data);
            out.close();
        } catch (IOException e) {
            System.out.println("WRITE ERROR: content doesn't exist");
        }
    }

    protected long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public String getExtension () {return extension; };



    @Override
    public String toString() {
        return "Title:" + title + "\nDuration: " + duration + "\nCategory: " + category;
    }

    public String getCategory() {
        return category;
    }
}
