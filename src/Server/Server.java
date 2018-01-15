package Server;

import Implementation.StorageServers;
import Interface.ClientNotifier;
import Implementation.Storage;
import Interface.StoreServers;

import java.rmi.Naming;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
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

    private static String RMI_STORE;
    private static String IP;
    private static String PORT;
    private static List<ClientNotifier> clientsCallback = new ArrayList<ClientNotifier>();

    public Server () throws RemoteException {
        super();
    }

    public static void addClientCallback(ClientNotifier client) throws RemoteException {
        Server.clientsCallback.add(client);
    }

    public static void callBack() throws RemoteException {
        for (ClientNotifier client : Server.clientsCallback){
            client.callMe("<ALERT> Some client has added new multimedia Content.");
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("███████╗███████╗██████╗ ██╗   ██╗███████╗██████╗ \n" +
                    "██╔════╝██╔════╝██╔══██╗██║   ██║██╔════╝██╔══██╗\n" +
                    "███████╗█████╗  ██████╔╝██║   ██║█████╗  ██████╔╝\n" +
                    "╚════██║██╔══╝  ██╔══██╗╚██╗ ██╔╝██╔══╝  ██╔══██╗\n" +
                    "███████║███████╗██║  ██║ ╚████╔╝ ███████╗██║  ██║\n" +
                    "╚══════╝╚══════╝╚═╝  ╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝");
            System.out.println("");
            System.out.println("Give me, your IP_Address (never use 127.0.0.1 or localhost): ");
            try{
                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                IP = bufferRead.readLine();
                System.out.println("Give me a PORT: ");
                bufferRead = new BufferedReader(new InputStreamReader(System.in));
                PORT = bufferRead.readLine();
                RMI_STORE = "rmi://" + IP + ":" + PORT + "/storage";
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

            Storage store = new Storage((StorageServers) connectToMaster(), RMI_STORE);
            startRegistry();

            Naming.rebind(RMI_STORE, store);
            System.out.println("Server started storage at "+ RMI_STORE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static StoreServers connectToMaster(){
        try {
            System.out.println("Give me, IP addres of master server: ");
            String ip = new BufferedReader(new InputStreamReader(System.in)).readLine();
            System.out.println("Give me, PORT of master server: ");
            String port = new BufferedReader(new InputStreamReader(System.in)).readLine();
            String RMI_MASTER_STORE = "rmi://"+ip+":"+port+"/storage";
            StoreServers storage = (StoreServers) Naming.lookup(RMI_MASTER_STORE);
            return storage;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void startRegistry() throws RemoteException{
        try {
            Registry registry = LocateRegistry.getRegistry(PORT);
            registry.list();
        } catch (RemoteException ex) {
            Registry registry = LocateRegistry.createRegistry(Integer.parseInt(PORT));
        }
        System.out.println("RMI registry created at port " + PORT);
    }

    public static void removeClientCallback(ClientNotifier client) {
        Server.clientsCallback.remove(client);
    }
}

