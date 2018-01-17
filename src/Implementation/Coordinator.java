package Implementation;

import Interface.CoordinatorServer;

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

    public Coordinator() throws RemoteException {
        super();
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
    public void addFileFromUser(String user, String file) {
        if (users.get(user) == null) {
            ArrayList<String> list = new ArrayList<>();
            list.add(file);
            users.put(user, list);
        }
        else {
            users.get(user).add(file);
        }
        update_FileHash(users, "usersDB.data");

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

    private void recoverCategoriesData(){
        categories = (Map<String, ArrayList<String>>) recoverHash(categories ,"categoriesDB.data");
    }

    private void recoverUsersData(){
        users = (Map<String, ArrayList<String>>) recoverHash(users ,"usersDB.data");
    }

}
