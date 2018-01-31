package rmiserver.Implementation;

import org.json.JSONException;
import rmiprotocol.DataTransferProtocol.ObjectContent;
import rmiprotocol.DataTransferProtocol.ObjectRequest;
import rmiserver.Interface.ClientNotifier;
import rmiserver.Interface.CoordinatorServer;
import rmiserver.Interface.FileStorage;
import SecurityLayer.CheckSum;
import rmiserver.Server.Server;

import java.io.*;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Storage extends UnicastRemoteObject implements FileStorage {

    private CoordinatorServer coordinatorServer;
    private StorageWriter storageWriter;
    private String address;

    public Storage(CoordinatorServer coordinatorServer, String address) throws RemoteException {
        super();
        this.coordinatorServer = coordinatorServer;
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
            coordinatorServer.addFileFromUser(obj.getUser(), serial, address, obj.getExtension(), obj.getTitle(), obj.isEncrypted());

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

    @Override
    public boolean deleteObject(ObjectRequest request) throws IOException, ClassNotFoundException, NotBoundException, NoSuchAlgorithmException, JSONException {

        String title = request.getTitle();
        String extension = request.getExtension();
        String user = request.getUser();
        String serial = getSerialValue(title, extension);

        String serverAddress = coordinatorServer.getServer(getSerialValue(title, extension));
        System.out.println("rmiserver.File deleteObject "+ title+ "." + extension + ":" + serial);

        ArrayList<String> owned = coordinatorServer.getFileIdFrom(user);
        try {
            if (owned.stream().anyMatch(o -> o.equals(getSerialValue(title, extension)))) {
                System.out.println("is owner");
                if (address.equals(serverAddress)) {
                    return deleteLocal(title, extension);
                }
                return deleteRemote(request, serverAddress);
            }
        } catch (Exception e){
            return false;
        }
        return false;
    }

    private boolean deleteRemote(ObjectRequest request, String serverAddress) throws IOException, NotBoundException, NoSuchAlgorithmException, ClassNotFoundException, JSONException {
        FileStorage storage = (FileStorage) Naming.lookup(serverAddress);
        return storage.deleteObject(request);
    }

    private boolean deleteLocal(String title, String extension) throws IOException, JSONException {
        String serial = getSerialValue(title, extension);
        System.out.println("rmiserver.File deleteLocal "+ title+ "." + extension + ":" + serial);
        File f = new File(serial + "/" + serial + "out.data");
        File folder = new File(serial);
        if(f.delete()){
            folder.delete();
            System.out.println(f.getName() + " is deleted!");
            coordinatorServer.deleteFile(serial);
            return true;
        }else{
            System.out.println("Delete operation is failed.");
            return false;
        }

    }

    @Override
    public ArrayList<String> getCategoryFilter(String category) throws IOException, JSONException {
        ArrayList<String> itemsList =  coordinatorServer.getExtension(category);
        return itemsList;
    }

    @Override
    public ArrayList<String> getUserFilter(String user) throws IOException, JSONException {
        ArrayList<String> itemsList =  coordinatorServer.getFileNameFrom(user);
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
    public ObjectContent getObject(ObjectRequest request) throws IOException, NotBoundException, ClassNotFoundException, NoSuchAlgorithmException, JSONException {

        String title = request.getTitle();
        String extension = request.getExtension();
        String serial = getSerialValue(title, extension);

        System.out.println("rmiserver.File getObject "+ title+ "." + extension + ":" + serial);
        String serverAddress = coordinatorServer.getServer(getSerialValue(title, extension));

        if(address.equals(serverAddress)) {
            return getLocalObject(title, extension);
        }
        return recoverRemote(title,extension,serverAddress);

    }

    @Override
    public ObjectContent getLocalObject(String title, String extension) throws IOException, NotBoundException, ClassNotFoundException, NoSuchAlgorithmException {

        ObjectContent object;
        String serial = getSerialValue(title, extension);
        System.out.println("rmiserver.File getLocalObject "+ title+ "." + extension + ":" + serial);
        File f = new File(serial + "/" + serial + "out.data");
        if (f.exists()) {
            object = readObjectContent(f);
        } else {
            return null;
        }
        return object;
    }

    @Override
    public boolean modifyObject(ObjectContent obj) throws IOException, JSONException {

        String title = obj.getTitle();
        String extension = obj.getExtension();
        String user = obj.getUser();
        String serial = getSerialValue(title, extension);

        String serverAddress = coordinatorServer.getServer(getSerialValue(title, extension));
        System.out.println("rmiserver.File modifyObject "+ title+ "." + extension + ":" + serial);

        ArrayList<String> owned = coordinatorServer.getFileIdFrom(user);
        try {
            if (owned.stream().anyMatch(o -> o.equals(getSerialValue(title, extension)))) {
                System.out.println("is owner");
                if (address.equals(serverAddress)) {
                    return modifyLocalObject(title, extension, obj);
                }
                return modifyRemote(obj, serverAddress);
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }



    private Boolean modifyLocalObject(String title, String extension, ObjectContent obj) throws IOException, JSONException {
        deleteLocal(title, extension);
        String serial = getSerialValue(title, extension);
        System.out.println("Modifying Local Object Content");
        try {
            new File(serial).mkdirs();
            writeObjectContent(obj, serial);
            coordinatorServer.addFileFromUser(obj.getUser(), serial, address, obj.getExtension(), obj.getTitle(), obj.isEncrypted());

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        return true;
    }

    private ObjectContent recoverRemote(String title, String extension, String remote) throws IOException, NotBoundException, ClassNotFoundException, NoSuchAlgorithmException {
        FileStorage storage = (FileStorage) Naming.lookup(remote);
        ObjectContent object = recoverOtherServer(title, extension, storage);
        return object;
    }


    private void newContentCallback() throws RemoteException{
        Server.callBack();
    }

    private void notUserCallback(ClientNotifier client) throws RemoteException{
        Server.notUser(client);
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


    @Override
    public boolean modifyObject(ObjectRequest request, String newTitle) throws IOException, NoSuchAlgorithmException, NotBoundException, ClassNotFoundException, InterruptedException, JSONException {
        String title = request.getTitle();
        String extension = request.getExtension();
        String user = request.getUser();
        String oldSerial = getSerialValue(title, extension);

        String serverAddress = coordinatorServer.getServer(getSerialValue(title, extension));
        System.out.println("rmiserver.File modifyObject "+ title+ "." + extension + ":" + oldSerial);

        ArrayList<String> owned = coordinatorServer.getFileIdFrom(user);
        try {
            if (owned.stream().anyMatch(o -> o.equals(getSerialValue(title, extension)))) {
                System.out.println("is owner");
                if (address.equals(serverAddress)) {
                    return modifyLocal(user, title, extension, newTitle);
                }
                return modifyRemote(request, newTitle, serverAddress);
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    private boolean modifyRemote(ObjectRequest request, String newTitle, String serverAddress) throws IOException, NotBoundException, ClassNotFoundException, NoSuchAlgorithmException, InterruptedException, JSONException {
        FileStorage storage = (FileStorage) Naming.lookup(serverAddress);
        return storage.modifyObject(request, newTitle);
    }

    private boolean modifyRemote(ObjectContent obj, String serverAddress) throws IOException, NotBoundException, JSONException {
        FileStorage storage = (FileStorage) Naming.lookup(serverAddress);
        return storage.modifyObject(obj);
    }

    private boolean modifyLocal(String user, String title, String extension, String newTitle) throws IOException, ClassNotFoundException, NotBoundException, NoSuchAlgorithmException, InterruptedException, JSONException {
        String newSerial = getSerialValue(newTitle, extension);

        ObjectContent obj = getLocalObject(title, extension);
        obj.setTitle(newTitle);
        System.out.println("Modifying local:");
        try {
            new File(newSerial).mkdirs();
            writeObjectContent(obj, newSerial);

            coordinatorServer.addFileFromUser(user, newSerial, address, extension, newTitle, obj.isEncrypted());

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        return deleteLocal(title, extension);


    }


}
