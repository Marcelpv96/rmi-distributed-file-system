package rmiserver.Interface;

import org.json.JSONException;
import rmiprotocol.DataTransferProtocol.ObjectContent;
import rmiprotocol.DataTransferProtocol.ObjectRequest;

import java.io.IOException;
import java.math.BigInteger;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public interface FileStorage extends Remote {

    void storeObject(ObjectContent obj, BigInteger checksum) throws RemoteException, IOException, NoSuchAlgorithmException;
    ObjectContent getObject(ObjectRequest request) throws IOException, NotBoundException, ClassNotFoundException, NoSuchAlgorithmException, JSONException;
    void addCallback(ClientNotifier client) throws RemoteException;
    void removeCallback(ClientNotifier client) throws RemoteException;
    boolean modifyObject(ObjectRequest obj, String newTitle) throws RemoteException, IOException, NoSuchAlgorithmException, NotBoundException, ClassNotFoundException, InterruptedException, JSONException;
    boolean deleteObject(ObjectRequest request) throws IOException, ClassNotFoundException, NotBoundException, NoSuchAlgorithmException, JSONException;
    ArrayList<String> getCategoryFilter(String category) throws RemoteException;
    ObjectContent getLocalObject(String title, String extension) throws IOException, NotBoundException, ClassNotFoundException, NoSuchAlgorithmException;

    boolean modifyObject(ObjectContent obj) throws IOException, JSONException;
}
