package Interface;

import DataTransferProtocol.ObjectContent;
import DataTransferProtocol.ObjectRequest;

import java.io.IOException;
import java.math.BigInteger;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public interface FileStorage extends Remote {

    void storeObject(ObjectContent obj, BigInteger checksum) throws RemoteException, IOException, NoSuchAlgorithmException;
    ObjectContent getObject(ObjectRequest request) throws IOException, NotBoundException, ClassNotFoundException, NoSuchAlgorithmException;
    void addCallback(ClientNotifier client) throws RemoteException;
    void removeCallback(ClientNotifier client) throws RemoteException;
    boolean modifyObject(ObjectRequest obj, String newTitle) throws RemoteException, IOException, NoSuchAlgorithmException, NotBoundException, ClassNotFoundException, InterruptedException;
    boolean deleteObject(ObjectRequest request) throws IOException, ClassNotFoundException, NotBoundException, NoSuchAlgorithmException;
    ArrayList<String> getCategoryFilter(String category) throws RemoteException;
    ObjectContent getLocalObject(String title, String extension) throws IOException, NotBoundException, ClassNotFoundException, NoSuchAlgorithmException;

    Boolean modifyObject(ObjectContent obj) throws RemoteException;
}
