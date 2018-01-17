package Implementation;

import Interface.ClientNotifier;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Notifier extends UnicastRemoteObject implements ClientNotifier {

    public Notifier() throws RemoteException {
        super();
    }
    @Override
    public void callMe(String message) throws RemoteException {
        System.out.println(message);
    }
}
