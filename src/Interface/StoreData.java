package Interface;

import Client.ObjectContent;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by arnau on 11/11/2017.
 */
public interface StoreData extends Remote {

    Long storeObject(ObjectContent obj) throws RemoteException;
    ObjectContent getObject(String title) throws  RemoteException;
    int getSize() throws RemoteException;
    boolean isEmpty() throws RemoteException;
    void addCallback(ClientCallback client) throws RemoteException;
    ArrayList<String> getCategoryFilter(String category) throws RemoteException;
    void addToCategoryFilter(String cat, String title) throws IOException;

}
