package Implementation;

import DataTransferProtocol.ObjectContent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public interface StorageWriterBase {

    void updateLocalHash(Map<String, ?> hashMap, String file_name);
    HashMap<String, ?> recoverLocalHash(String file_name);
    void writeObjectContent(ObjectContent obj, String serial) throws IOException;

}
