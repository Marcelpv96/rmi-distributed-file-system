package Implementation;

import Client.ObjectContent;
import DataTransferProtocol.ObjectRequest;
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

public class Storage extends UnicastRemoteObject implements FileStorage {

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
            new File(serial).mkdirs();
            writeObjectContent(obj, serial);
            storageServers.addCategory(obj.getExtension(), obj.getTitle());
            storageServers.addServer(address, serial);
            storageServers.addFileFromUser(obj.getUser(), serial);

        } catch (Exception e) {
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
    public ObjectContent getObjectFromUser( ObjectRequest request) throws IOException, NotBoundException, ClassNotFoundException, NoSuchAlgorithmException {

        ObjectContent object;
        String title = request.getTitle();
        System.out.println(title);
        String extension = request.getExtension();
        System.out.println(extension);

        String user = request.getUser();
        String serial = getSerialValue(title, extension);


        System.out.println("Server retrieving "+ title+ "." + extension + ":" + serial);
        File f = new File(serial + "/" + serial + "out.data");
        String serverAdress = storageServers.getServer(getSerialValue(title, extension));

        ArrayList<String> owned = storageServers.getFileFrom(user);
        if(owned == null) {
            System.out.println("USER NO UPLOAD");
            return null;
        }
        if (owned.stream().anyMatch(o -> o.equals(getSerialValue(title, extension)))) {
            System.out.println("USER IS OWNER");
            return recoverRemote(title, extension, serverAdress);
        }
        System.out.println("USER NOT OWNER");
        return null;

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
            return null;
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
        ObjectContent object = recoverOtherServer(title, extension, storage);
        return object;
    }


    private void newContentCallback() throws RemoteException{
        Server.callBack();
    }

    private ObjectContent recoverOtherServer(String title, String extension, FileStorage storage) throws IOException, NotBoundException, ClassNotFoundException, NoSuchAlgorithmException {
        ObjectContent obj = storage.getObject(title, extension);
        return obj;
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


}
