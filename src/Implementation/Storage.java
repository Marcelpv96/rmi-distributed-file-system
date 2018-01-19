package Implementation;

import DataTransferProtocol.ObjectContent;
import DataTransferProtocol.ObjectRequest;
import Interface.ClientNotifier;
import Interface.CoordinatorServer;
import Interface.FileStorage;
import SecurityLayer.CheckSum;
import Server.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
        System.out.println("Writing file.");
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
    public boolean deleteObject(ObjectRequest request) throws IOException, ClassNotFoundException, NotBoundException, NoSuchAlgorithmException {

        String title = request.getTitle();
        String extension = request.getExtension();
        String user = request.getUser();
        String serial = getSerialValue(title, extension);

        String serverAddress = storageServers.getServer(getSerialValue(title, extension));
        System.out.println("Server deleteObject "+ title+ "." + extension + ":" + serial);

        ArrayList<String> owned = storageServers.getFileFrom(user);
        if (owned.stream().anyMatch(o -> o.equals(getSerialValue(title, extension)))) {
            System.out.println("is owner");
            if (address.equals(serverAddress)) {
                return deleteLocal(title, extension);
            }
            return deleteRemote(request, serverAddress);
        }
        return false;
    }

    private boolean deleteRemote(ObjectRequest request, String serverAddress) throws IOException, NotBoundException, NoSuchAlgorithmException, ClassNotFoundException {
        FileStorage storage = (FileStorage) Naming.lookup(serverAddress);
        return storage.deleteObject(request);
    }

    private boolean deleteLocal(String title, String extension) throws RemoteException {
        String serial = getSerialValue(title, extension);
        System.out.println("Server deleteLocal "+ title+ "." + extension + ":" + serial);
        File f = new File(serial + "/" + serial + "out.data");
        File folder = new File(serial);
        if(f.delete()){
            folder.delete();
            System.out.println(f.getName() + " is deleted!");
            storageServers.removeServer(address, serial);
            storageServers.removeCategory(extension, title);
            return true;
        }else{
            System.out.println("Delete operation is failed.");
            return false;
        }

    }

    @Override
    public boolean modifyObject(ObjectRequest request, String newTitle) throws Exception {
        String title = request.getTitle();
        String extension = request.getExtension();
        String user = request.getUser();
        String serial = getSerialValue(title, extension);
        ObjectContent newObjcontent;
        System.out.println("Server modifyObject "+ title+ "." + extension + ":" + serial);

        ArrayList<String> owned = storageServers.getFileFrom(user);
        if (owned.stream().anyMatch(o -> o.equals(getSerialValue(title, extension)))) {
            System.out.println("is owner");
            newObjcontent = new ObjectContent(getObject(request));
            newObjcontent.setTitle(newTitle);
            if (deleteObject(request)){
                storeObject(newObjcontent, CheckSum.getFrom(newObjcontent));
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean modifyContentObject(ObjectRequest request, String path) throws Exception {
        String title = request.getTitle();
        String extension = request.getExtension();
        String user = request.getUser();
        String serial = getSerialValue(title, extension);
        ObjectContent newObjcontent;
        System.out.println("Server modify content of Object "+ title+ "." + extension + ":" + serial);

        ArrayList<String> owned = storageServers.getFileFrom(user);
        if (owned.stream().anyMatch(o -> o.equals(getSerialValue(title, extension)))) {
            System.out.println("is owner");
            newObjcontent = new ObjectContent(getObject(request), path);
            if (deleteObject(request)){
                storeObject(newObjcontent, CheckSum.getFrom(newObjcontent));
                return true;
            }else{
                return false;
            }
        }
        return false;
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
    public ObjectContent getObject(ObjectRequest request) throws IOException, NotBoundException, ClassNotFoundException, NoSuchAlgorithmException {

        String title = request.getTitle();
        String extension = request.getExtension();
        String serial = getSerialValue(title, extension);

        System.out.println("Server getObject "+ title+ "." + extension + ":" + serial);
        String serverAddress = storageServers.getServer(getSerialValue(title, extension));

        if(address.equals(serverAddress)) {
            return getLocalObject(title, extension);
        }
        return recoverRemote(title,extension,serverAddress);

    }

    @Override
    public ObjectContent getLocalObject(String title, String extension) throws IOException, NotBoundException, ClassNotFoundException, NoSuchAlgorithmException {

        ObjectContent object;
        String serial = getSerialValue(title, extension);
        System.out.println("Server getLocalObject "+ title+ "." + extension + ":" + serial);
        File f = new File(serial + "/" + serial + "out.data");
        if (f.exists()) {
            object = readObjectContent(f);
        } else {
            return null;
        }
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
        ObjectContent obj = storage.getLocalObject(title, extension);
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

    private ObjectContent readObjectContent(File f) throws IOException, ClassNotFoundException {

        ObjectContent object;FileInputStream fi = new FileInputStream(f);
        ObjectInputStream oi = new ObjectInputStream(fi);

        object = (ObjectContent) oi.readObject();

        oi.close();
        fi.close();

        return object;
    }

}
