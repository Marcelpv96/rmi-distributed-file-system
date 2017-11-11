package Interface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by arnau on 11/11/2017.
 */
public interface StoreData extends Remote {

    public Long storeData(String data) throws RemoteException;
    public String getData(Long key) throws  RemoteException;
    public int getSize() throws RemoteException;
    public boolean isEmpty() throws RemoteException;

}
