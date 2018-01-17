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

    void storeObject(ObjectContent obj, BigInteger checksum) throws IOException, NoSuchAlgorithmException;
    ObjectContent getObjectFromUser(ObjectRequest request) throws IOException, NotBoundException, ClassNotFoundException, NoSuchAlgorithmException;
    void addCallback(ClientNotifier client) throws RemoteException;
    void removeCallback(ClientNotifier client) throws RemoteException;
    ArrayList<String> getCategoryFilter(String category) throws RemoteException;
    ObjectContent getObject(String title, String extension) throws IOException, NotBoundException, ClassNotFoundException, NoSuchAlgorithmException;
}
