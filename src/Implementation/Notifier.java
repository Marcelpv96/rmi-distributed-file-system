package Implementation;

import Interface.ClientNotifier;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Marcelpv96 on 21/11/17.
 */
public class Notifier extends UnicastRemoteObject implements ClientNotifier {

    public Notifier() throws RemoteException {
        super();
    }
    @Override
    public void callMe(String message) throws RemoteException {
        System.out.println(message);
    }
}
