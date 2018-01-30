package rmiserver.Implementation;

import org.json.JSONException;
import org.json.JSONObject;
import rmiprotocol.RequestProtocol.ProtocolObjectRequest;
import rmiserver.Interface.CoordinatorServer;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Coordinator extends UnicastRemoteObject implements CoordinatorServer {

    private static Map<String, String> serverContents;
    private static Map<String, ArrayList<String>> categories;
    private static Map<String, ArrayList<String>> users;
    private static StorageWriter storageWriter;
    private static String webserviceAddress;

    public Coordinator(String webservice) throws RemoteException {
        super();
        webserviceAddress = webservice;
        storageWriter = new StorageWriter();
        serverContents = new HashMap<>();
        categories = new HashMap<>();
        users = new HashMap<>();
        recoverCategoriesData();
        recoverUsersData();
    }

    @Override
    public ArrayList<String> getCategory(String category){

        return categories.get(category);
    }

    @Override
    public ArrayList<String> getFileFrom(String user){

        return users.get(user);
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
            update_FileHash(categories, "categoriesDB.data");

    }

    @Override
    public void addFileFromUser(String user, String serial, String address, String extension, String title, Boolean isEncrypted) {

        ProtocolObjectRequest.POST_call(
                webserviceAddress+"/file",
                user,
                serial,
                address,
                extension,
                title,
                isEncrypted);

        if (users.get(user) == null) {
            ArrayList<String> list = new ArrayList<>();
            list.add(serial);
            users.put(user, list);
        }
        else {
            users.get(user).add(serial);
        }
        update_FileHash(users, "usersDB.data");

    }

    @Override
    public boolean removeFileFromUser(String user, String serial) throws RemoteException {
        return users.remove(user, serial);

    }

    private void update_FileHash(Map <String,?> hashMap, String file_name){
        storageWriter.updateLocalHash(hashMap, file_name);
    }

    @Override
    public void addServer(String address, String content) throws RemoteException{
        serverContents.put(content, address);
    }

    @Override
    public String getServer(String serial) throws IOException, JSONException {
        JSONObject res = ProtocolObjectRequest.GET_call(webserviceAddress+"/file/id/"+serial);
        // TODO maybe check if res == null
        return res.getString("address");
    }

    @Override
    public boolean removeServer(String address, String content) throws RemoteException {
        return serverContents.remove(address, content);
    }

    @Override
    public boolean removeCategory(String extension, String title) throws RemoteException {
        categories.get(extension).remove(title);
        if (categories.get(extension).size() == 0) {
            categories.remove(extension);
        }
        return true;
    }

    private HashMap<String, ?> recoverHash(Map <String, ?> hashMap, String file_name){
        return storageWriter.recoverLocalHash(file_name);
    }

    private void recoverCategoriesData(){
        categories = (Map<String, ArrayList<String>>) recoverHash(categories ,"categoriesDB.data");
    }

    private void recoverUsersData(){
        users = (Map<String, ArrayList<String>>) recoverHash(users ,"usersDB.data");
    }



}
