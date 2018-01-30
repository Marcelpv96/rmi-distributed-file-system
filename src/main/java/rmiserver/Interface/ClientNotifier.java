package rmiserver.Interface;

import java.rmi.Remote;

public interface ClientNotifier extends Remote{
    void callMe	(String	message) throws java.rmi.RemoteException;
}
