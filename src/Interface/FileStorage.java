package Interface;

import Client.ObjectContent;

import java.io.IOException;
import java.math.BigInteger;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by arnau on 11/11/2017.
 */
public interface FileStorage extends Remote {

    void storeObject(ObjectContent obj, BigInteger checksum) throws IOException, NoSuchAlgorithmException;
    ObjectContent getObject(String title, String extension) throws IOException, NotBoundException, ClassNotFoundException, NoSuchAlgorithmException;
    void addCallback(ClientNotifier client) throws RemoteException;
    void removeCallback(ClientNotifier client) throws RemoteException;
    ArrayList<String> getCategoryFilter(String category) throws RemoteException;

}
