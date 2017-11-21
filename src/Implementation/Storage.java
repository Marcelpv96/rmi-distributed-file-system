package Implementation;

import Client.ObjectContent;
import Interface.ClientCallback;
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

    Map<String, Long> storedData = new HashMap<String, Long>();
    private static Map<String, ArrayList<String>> categoryRegister;

    public Storage() throws RemoteException {
        super();
        recoverData();
    }

    private void update_CategoryFilterHash(){
        try{
            System.out.println("updating");
            FileOutputStream f = new FileOutputStream("CategoryFilter_hash.txt");
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(categoryRegister);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void newCatergoryFilter(){
        try {
            FileOutputStream f = new FileOutputStream( new File("CategoryFilter_hash.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);
            categoryRegister = new HashMap<String, ArrayList<String>>();
            o.writeObject(categoryRegister);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void recoverData(){
        try {
            FileInputStream f = new FileInputStream("CategoryFilter_hash.txt");
            ObjectInputStream oi = new ObjectInputStream(f);
            categoryRegister = (HashMap<String,ArrayList<String>>) oi.readObject();
        }catch (IOException e){
            newCatergoryFilter();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    // TODO create handler before storing object
    @Override
    public Long storeObject(ObjectContent obj) throws RemoteException {
        //Initialized so try/catch doesn't kek
        Long serial = 0L;

        if (storedData.put(obj.getTitle(), serial) == null) {
            try {
                serial = System.currentTimeMillis();
                addToCategoryFilter(obj.getCategory(), obj.getTitle());
                new File(obj.getTitle()).mkdirs();

                FileOutputStream f = new FileOutputStream(new File(obj.getTitle() + "/" + obj.getTitle() + "out.txt"));
                ObjectOutputStream o = new ObjectOutputStream(f);

                o.writeObject(obj);
                o.close();
                f.close();

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
            /*Long serial = storedData.get(title);
            System.out.println(serial);
            System.out.println(title);*/

            FileInputStream fi = new FileInputStream(new File(title + "/" + title + "out.txt"));
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
    public void addCallback(ClientCallback client) throws RemoteException {
        Server.addClientCallback(client);
        //Server.callBack();
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
        update_CategoryFilterHash();
    }



}
