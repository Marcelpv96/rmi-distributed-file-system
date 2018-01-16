package Implementation;

import Client.ObjectContent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by adt5 on 16/01/18.
 */
public interface StorageWriterBase {

    void updateLocalHash(Map<String, ?> hashMap, String file_name);
    HashMap<String, ?> recoverLocalHash(String file_name);
    void writeObjectContent(ObjectContent obj, String serial) throws IOException;

}
