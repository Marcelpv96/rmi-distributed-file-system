package Server;

import Implementation.Storage;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by arnau on 11/11/2017.
 */
public class Server {

    private static final int PORT = 40000;
    private static final String RMI_STORE = "rmi://localhost:" + PORT + "/storage";


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
