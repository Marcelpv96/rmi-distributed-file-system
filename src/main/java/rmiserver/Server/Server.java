package rmiserver.Server;

import rmiserver.Interface.ClientNotifier;
import rmiserver.Interface.CoordinatorServer;
import rmiserver.Implementation.Storage;

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

public class Server extends UnicastRemoteObject {

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
            CoordinatorServer storageServers = connectToMaster();
            Storage store = new Storage(storageServers, RMI_STORE);
            startRegistry();

            Naming.rebind(RMI_STORE, store);
            System.out.println("rmiserver.File started storage at "+ RMI_STORE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CoordinatorServer connectToMaster() throws IOException, NotBoundException {
        System.out.println("Give me, IP addres of master server: ");
        String ip = new BufferedReader(new InputStreamReader(System.in)).readLine();

        System.out.println("Give me, PORT of master server: ");
        String port = new BufferedReader(new InputStreamReader(System.in)).readLine();

        String RMI_MASTER_STORE = "rmi://"+ip+":"+port+"/storage";

        return (CoordinatorServer) Naming.lookup(RMI_MASTER_STORE);
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

    public static void notUser(ClientNotifier client) throws RemoteException {
        client.callMe("You haven't permissions to do that.");
    }
}

