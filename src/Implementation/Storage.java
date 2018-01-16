package Implementation;

import Client.ObjectContent;
import Interface.ClientNotifier;
import Interface.FileStorage;
import Interface.CoordinatorServer;
import SecurityLayer.CheckSum;
import Server.Server;

import java.io.*;
import java.math.BigInteger;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Storage extends UnicastRemoteObject implements FileStorage {

    private static Map<String, ArrayList<String>> categoryRegister;
    private CoordinatorServer storageServers;
    private StorageWriter storageWriter;
    private String address;

    public Storage(CoordinatorServer storageServers, String address) throws RemoteException {
        super();
        this.storageServers = storageServers;
        this.address = address;
        this.storageWriter = new StorageWriter();
    }

    @Override
    public void storeObject(ObjectContent obj, BigInteger checksum) throws IOException, NoSuchAlgorithmException {
        if (obj == null) return;
        if (checkIntegrity(obj, checksum)) return;

        String serial = getSerialValue(obj);

        try {
            addToCategoryFilter(obj.getExtension(), obj.getTitle());
            new File(serial).mkdirs();
            writeObjectContent(obj, serial);
            storageServers.addCategory(obj.getExtension(), obj.getTitle());
            storageServers.addServer(address, serial);

        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }

    }

    @Override
    public ArrayList<String> getCategoryFilter(String category) throws RemoteException {
        ArrayList<String> itemsList =  storageServers.getCategory(category);
        return itemsList;
    }

    @Override
    public void removeCallback(ClientNotifier client) throws RemoteException {
        Server.removeClientCallback(client);
    }

    @Override
    public void addCallback(ClientNotifier client) throws RemoteException {
        Server.addClientCallback(client);
    }

    @Override
    public ObjectContent getObject(String title, String extension) throws IOException, NotBoundException, ClassNotFoundException, NoSuchAlgorithmException {

        ObjectContent object;
        String serial = getSerialValue(title, extension);
        System.out.println("Server retrieving "+ title+ "." + extension + ":" + serial);
        File f = new File(serial + "/" + serial + "out.data");
        if (f.exists()) {
            object = getObjectContent(f);
        } else {
            String serverAdress = storageServers.getServer(String.valueOf((title+extension).hashCode()));
            return recoverRemote(title, extension, serverAdress);
        }
        return object;
    }

    private ObjectContent getObjectContent(File f) throws IOException, ClassNotFoundException {

        ObjectContent object;FileInputStream fi = new FileInputStream(f);
        ObjectInputStream oi = new ObjectInputStream(fi);

        object = (ObjectContent) oi.readObject();

        oi.close();
        fi.close();

        return object;
    }

    private ObjectContent recoverRemote(String title, String extension, String remote) throws IOException, NotBoundException, ClassNotFoundException, NoSuchAlgorithmException {
        FileStorage storage = (FileStorage) Naming.lookup(remote);
        ObjectContent object = recoverOtherServer(title, extension,storage);
        return object;
    }


    private void newContentCallback() throws RemoteException{
        Server.callBack();
    }

    private ObjectContent recoverOtherServer(String title, String extension, FileStorage storage) throws IOException, NotBoundException, ClassNotFoundException, NoSuchAlgorithmException {
        ObjectContent obj = storage.getObject(title, extension);
        return obj;
    }

    private void addToCategoryFilter(String category, String title) throws IOException {
        ArrayList<String> itemsList = categoryRegister.get(category);
        if(itemsList == null) {
            addElement(category, title);
        } else {
            if(!itemsList.contains(title)) itemsList.add(title);
        }
        update_FileHash(categoryRegister,"CategoryRegister_hash.data");
    }

    private void addElement(String category, String title) {
        ArrayList<String> itemsList;
        itemsList = new ArrayList<>();
        itemsList.add(title);
        categoryRegister.put(category, itemsList);
    }

    private String getSerialValue(ObjectContent obj) {
        return String.valueOf((obj.getTitle()+obj.getExtension()).hashCode());
    }

    private String getSerialValue(String title, String extension) {
        return String.valueOf((title+extension).hashCode());
    }

    private boolean checkIntegrity(ObjectContent obj, BigInteger checksum) throws IOException, NoSuchAlgorithmException {
        if (checksum.compareTo(CheckSum.getFrom(obj)) != 0) {
            System.out.println("Wrong checksum");
            return true;
        }
        return false;
    }

    private void writeObjectContent(ObjectContent obj, String serial) throws IOException {
        storageWriter.writeObjectContent(obj, serial);
        newContentCallback();
    }

    private void update_FileHash(Map <String,?> hashMap, String file_name){
        storageWriter.updateLocalHash(hashMap, file_name);
    }



}
