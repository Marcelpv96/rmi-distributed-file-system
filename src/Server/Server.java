package Server;

import Implementation.ClientCallbackImpl;
import Implementation.Storage;
import Interface.ClientCallback;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arnau on 11/11/2017.
 */
public class Server  extends UnicastRemoteObject {

    private static final int PORT = 40000;
    private static final String RMI_STORE = "rmi://localhost:" + PORT + "/storage";
    private static List<ClientCallback> clientsCallback;

    public Server () throws RemoteException {
        super();
        clientsCallback = new ArrayList<ClientCallback>();

    }

    public static void addClientCallback(ClientCallback client) throws RemoteException {
        //if (client != null)
           // clientsCallback.add(client);
        client.callMe("hola");

    }

    public static void callBack() throws RemoteException {
        for (int i =0; i<clientsCallback.size();i++){
            ClientCallbackImpl client = (ClientCallbackImpl) clientsCallback.get(i);
            client.callMe("Server calling client: "+i);
        }
    }

    public static void main(String[] args) {
        try {

            Storage store = new Storage();

            startRegistry();

            Naming.rebind(RMI_STORE, store);
            System.out.println("Server started storage at "+ RMI_STORE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startRegistry() throws RemoteException{
        try {
            Registry registry = LocateRegistry.getRegistry(PORT);
            registry.list();
        } catch (RemoteException ex) {
            System.out.println( "RMI registry cannot be located at port " + PORT);
            Registry registry= LocateRegistry.createRegistry(PORT);
            System.out.println("RMI registry created at port " + PORT);
        }
    }
}
