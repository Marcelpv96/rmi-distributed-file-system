package Implementation;

import Client.ObjectContent;
import Interface.ClientNotifier;
import Interface.StoreData;
import Server.Server;
import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by arnau on 11/11/2017.
 */
public class Storage extends UnicastRemoteObject implements StoreData {

    private static Map<String, Long> storedData;
    private static Map<String, ArrayList<String>> categoryRegister;

    public Storage() throws RemoteException {
        super();
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
        storedData = (Map<String, Long>) recoverHash(storedData ,"storedData_hash.data");
        categoryRegister= (Map<String, ArrayList<String>>) recoverHash(categoryRegister ,"CategoryRegister_hash.data");
    }
    // TODO create handler before storing object
    @Override
    public Long storeObject(ObjectContent obj) throws RemoteException {
        //Initialized so try/catch doesn't kek
        Long serial = 0L;

        if (!storedData.containsKey(obj.getTitle())) {
            try {
                serial = System.currentTimeMillis();
                storedData.put(obj.getTitle(),serial);
                update_FileHash(storedData, "storedData_hash.data");
                addToCategoryFilter(obj.getCategory(), obj.getTitle());
                new File(Long.toString(serial)).mkdirs();

                FileOutputStream f = new FileOutputStream(new File(Long.toString(serial) + "/" + Long.toString(serial) + "out.data"));
                ObjectOutputStream o = new ObjectOutputStream(f);

                newContent();

                o.writeObject(obj);
                o.close();
                f.close();

                return serial;
            } catch (Exception e) {//Catch exception if any
                System.err.println("Error: " + e.getMessage());
            }
        }
        return storedData.get(obj.getTitle());
    }

    @Override
    public ObjectContent getObject(String title) throws RemoteException {

        ObjectContent object = new ObjectContent();

        try {
            Long serial = storedData.get(title);
            System.out.println(serial);
            System.out.println(title);

            FileInputStream fi = new FileInputStream(new File(Long.toString(serial)+ "/" + Long.toString(serial)  + "out.data"));
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects
            object = (ObjectContent) oi.readObject();
            System.out.println(object.getCategory());
            oi.close();
            fi.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return object;

    }

    @Override
    public int getSize() throws RemoteException {
        return storedData.size();
    }

    @Override
    public boolean isEmpty() throws RemoteException {
        return storedData.isEmpty();
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
