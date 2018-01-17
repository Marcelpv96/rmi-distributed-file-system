package CoordinatorModule;


import Implementation.Coordinator;
import Interface.CoordinatorServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class CoordinatorModule extends UnicastRemoteObject {

    private static String RMI_COORDINATOR;
    private static String IP;
    private static String PORT;

    public CoordinatorModule() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        try {
            System.out.println("███╗   ███╗ █████╗ ███████╗████████╗███████╗██████╗     ███████╗███████╗██████╗ ██╗   ██╗███████╗██████╗ \n" +
                    "████╗ ████║██╔══██╗██╔════╝╚══██╔══╝██╔════╝██╔══██╗    ██╔════╝██╔════╝██╔══██╗██║   ██║██╔════╝██╔══██╗\n" +
                    "██╔████╔██║███████║███████╗   ██║   █████╗  ██████╔╝    ███████╗█████╗  ██████╔╝██║   ██║█████╗  ██████╔╝\n" +
                    "██║╚██╔╝██║██╔══██║╚════██║   ██║   ██╔══╝  ██╔══██╗    ╚════██║██╔══╝  ██╔══██╗╚██╗ ██╔╝██╔══╝  ██╔══██╗\n" +
                    "██║ ╚═╝ ██║██║  ██║███████║   ██║   ███████╗██║  ██║    ███████║███████╗██║  ██║ ╚████╔╝ ███████╗██║  ██║\n" +
                    "╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝    ╚══════╝╚══════╝╚═╝  ╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═");
            System.out.println("");
            System.out.println("Give me, your IP_Address (never use 127.0.0.1 or localhost): ");
            try{
                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                IP = bufferRead.readLine();
                System.out.println("Give me a PORT: ");
                bufferRead = new BufferedReader(new InputStreamReader(System.in));
                PORT = bufferRead.readLine();
                RMI_COORDINATOR = "rmi://" + IP + ":" + PORT + "/storage";
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

            CoordinatorServer store = new Coordinator();
            startRegistry();

            Naming.rebind(RMI_COORDINATOR, store);
            System.out.println("Server started storage at "+ RMI_COORDINATOR);

        } catch (Exception e) {
            e.printStackTrace();
        }
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

}

