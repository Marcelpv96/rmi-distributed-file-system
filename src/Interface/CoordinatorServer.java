package Interface;

import java.math.BigInteger;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Marcelpv96 on 15/1/18.
 */

public interface CoordinatorServer extends Remote{
    void addServer(String address, String content) throws RemoteException;;
    String getServer(String content) throws  RemoteException;
    void addCategory(String extension, String title)throws  RemoteException;
    ArrayList<String> getCategory(String category)throws  RemoteException;
}
