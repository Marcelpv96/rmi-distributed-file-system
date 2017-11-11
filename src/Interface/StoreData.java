package Interface;

import Client.ObjectContent;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by arnau on 11/11/2017.
 */
public interface StoreData extends Remote {

    public Long storeObject(ObjectContent obj) throws RemoteException;
    public ObjectContent getObject(String title) throws  RemoteException;
    public int getSize() throws RemoteException;
    public boolean isEmpty() throws RemoteException;

}
