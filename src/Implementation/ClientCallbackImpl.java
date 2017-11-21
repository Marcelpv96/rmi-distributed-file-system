package Implementation;

import Interface.ClientCallback;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Marcelpv96 on 21/11/17.
 */
public class ClientCallbackImpl extends UnicastRemoteObject implements ClientCallback {

    public ClientCallbackImpl () throws RemoteException {
        super();
    }
    @Override
    public void callMe(String message) throws RemoteException {
        System.out.println(message);
    }
}
