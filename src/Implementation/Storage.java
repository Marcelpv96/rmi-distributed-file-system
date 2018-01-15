package Implementation;

import Client.ObjectContent;
import Interface.ClientNotifier;
import Interface.StoreData;
import Interface.StoreServers;
import Server.Server;

import java.io.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by arnau on 11/11/2017.
 */
public class Storage extends UnicastRemoteObject implements StoreData {

    private static Map<String, ArrayList<String>> categoryRegister;
    private StoreServers storageServers;
    private String address;

    public Storage(StoreServers storageServers, String address) throws RemoteException {
        super();
        this.storageServers = storageServers;
        this.address = address;
        recoverData();
    }

    //actualitzem el hash.
    private void update_FileHash(Map <String,?> hashMap, String file_name){
        try{
            FileOutputStream f = new FileOutputStream(file_name);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(hashMap);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //recuperem el hash de la execució anterior.
    private HashMap<String, ?> recoverHash(Map <String, ?> hashMap, String file_name){
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

    private void recoverData(){
        categoryRegister= (Map<String, ArrayList<String>>) recoverHash(categoryRegister ,"CategoryRegister_hash.data");
    }

    @Override
    public void storeObject(ObjectContent obj) throws RemoteException {
        if (obj == null) return;
        String serial = String.valueOf((obj.getTitle()+obj.getExtension()).hashCode());

        try {
            addToCategoryFilter(obj.getExtension(), obj.getTitle());
            new File(serial).mkdirs();

            FileOutputStream f = new FileOutputStream(new File(serial + "/" + serial + "out.data"));
            ObjectOutputStream o = new ObjectOutputStream(f);

            newContent();

            o.writeObject(obj);
            o.close();
            storageServers.addServer(address, serial);
            f.close();

        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }

    }



    @Override
    public ObjectContent getObject(String title, String extension) throws IOException, NotBoundException, ClassNotFoundException {

        ObjectContent object;
        String serial = String.valueOf((title+extension).hashCode());
        System.out.println(serial);
        System.out.println(title);
        File f = new File(serial + "/" + serial + "out.data");
        if (f.exists()) {
            FileInputStream fi = new FileInputStream(f);
            ObjectInputStream oi = new ObjectInputStream(fi);
            // Read objects
            object = (ObjectContent) oi.readObject();
            oi.close();
            fi.close();
        } else {
            String serverAdress = storageServers.getServer(String.valueOf((title+extension).hashCode()));
            return recoverRemote(title, extension, serverAdress);
        }
        return object;
    }

    public ObjectContent recoverRemote(String title, String extension, String remote) throws IOException, NotBoundException, ClassNotFoundException {
        StoreData storage = (StoreData) Naming.lookup(remote);
        ObjectContent object = recoverOtherServer(title, extension,storage);
        return object;
    }

    private ObjectContent recoverOtherServer(String title, String extension, StoreData storage) throws IOException, NotBoundException, ClassNotFoundException {
        ObjectContent obj = storage.getObject(title, extension);
        return obj;
    }

    @Override
    public void removeCallback(ClientNotifier client) throws RemoteException {
        Server.removeClientCallback(client);
    }

    @Override
    public void addCallback(ClientNotifier client) throws RemoteException {
        Server.addClientCallback(client);
    }

    public void newContent() throws RemoteException{
        Server.callBack();
    }
    @Override
    public ArrayList<String> getCategoryFilter(String category) {
        ArrayList<String> itemsList = categoryRegister.get(category);
        return itemsList;
    }

    @Override
    public void addToCategoryFilter(String category, String title) throws IOException {
        ArrayList<String> itemsList = categoryRegister.get(category);
        // if list does not exist create it
        if(itemsList == null) {
            itemsList = new ArrayList<String>();
            itemsList.add(title);
            categoryRegister.put(category, itemsList);
        } else {
            // add if item is not already in list
            if(!itemsList.contains(title)) itemsList.add(title);
        }
        update_FileHash(categoryRegister,"CategoryRegister_hash.data");
    }



}
