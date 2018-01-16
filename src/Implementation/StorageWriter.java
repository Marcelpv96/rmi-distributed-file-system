package Implementation;

import Client.ObjectContent;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by adt5 on 16/01/18.
 */
public class StorageWriter implements StorageWriterBase {

    @Override
    public void updateLocalHash(Map<String, ?> hashMap, String file_name) {
        try{
            FileOutputStream f = new FileOutputStream(file_name);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(hashMap);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HashMap<String, ?> recoverLocalHash(String file_name) {
        try {
            FileInputStream f = new FileInputStream(file_name);
            ObjectInputStream oi = new ObjectInputStream(f);
            return (HashMap<String, ?>) oi.readObject();
        }catch (IOException e){
            return new HashMap<>();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void writeObjectContent(ObjectContent obj, String serial) throws IOException {
        FileOutputStream f = new FileOutputStream(new File(serial + "/" + serial + "out.data"));
        ObjectOutputStream o = new ObjectOutputStream(f);

        o.writeObject(obj);
        o.close();
        f.close();
    }
}
