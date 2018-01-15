package Interface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Marcelpv96 on 15/1/18.
 */

public interface StoreServers extends Remote{
    void addServer(String address, String content) throws RemoteException;;
    String getServer(String content) throws  RemoteException;;
}
