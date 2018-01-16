package Implementation;

import Interface.CoordinatorServer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marcelpv96 on 15/1/18.
 */

public class Coordinator extends UnicastRemoteObject implements CoordinatorServer {

    private static Map<String, String> serverContents;
    private static Map<String, ArrayList<String>> categories;
    private static StorageWriter storageWriter;

    public Coordinator() throws RemoteException {
        super();
        this.storageWriter = new StorageWriter();
        serverContents = new HashMap<>();
        categories = new HashMap<>();
        recoverData();
    }

    @Override
    public ArrayList<String> getCategory(String category){
        return categories.get(category);
    }

    @Override
    public void addCategory(String extension, String title){
            if (categories.get(extension) == null) {
                ArrayList<String> list = new ArrayList<>();
                list.add(title);
                categories.put(extension, list);
            }
            else {
                categories.get(extension).add(title);
            }
            update_FileHash(categories, "CategoryRegister_hash.data");

    }

    private void update_FileHash(Map <String,?> hashMap, String file_name){
        storageWriter.updateLocalHash(hashMap, file_name);
    }

    @Override
    public void addServer(String address, String content) throws RemoteException{
        serverContents.put(content, address);
    }

    @Override
    public String getServer(String content) throws RemoteException{
        return serverContents.get(content);
    }

    private HashMap<String, ?> recoverHash(Map <String, ?> hashMap, String file_name){
        return storageWriter.recoverLocalHash(file_name);
    }


    private void recoverData(){
        categories = (Map<String, ArrayList<String>>) recoverHash(categories ,"CategoryRegister_hash.data");
    }

}
