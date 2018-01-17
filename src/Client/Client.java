package Client;


import DataTransferProtocol.ObjectContent;
import DataTransferProtocol.ObjectRequest;
import Implementation.Notifier;
import Interface.FileStorage;
import SecurityLayer.AESSecurity;
import SecurityLayer.CheckSum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class Client {

    private static String RMI_STORE;
    private static String user;
    private static AESSecurity aes;

    public static void main(String[] args) throws Exception {


        System.out.println(" ██████╗██╗     ██╗███████╗███╗   ██╗████████╗\n" +
                "██╔════╝██║     ██║██╔════╝████╗  ██║╚══██╔══╝\n" +
                "██║     ██║     ██║█████╗  ██╔██╗ ██║   ██║   \n" +
                "██║     ██║     ██║██╔══╝  ██║╚██╗██║   ██║   \n" +
                "╚██████╗███████╗██║███████╗██║ ╚████║   ██║   \n" +
                " ╚═════╝╚══════╝╚═╝╚══════╝╚═╝  ╚═══╝   ╚═╝   \n" +
                "                                             ");
        System.out.println("");
        BufferedReader bufferRead;

        System.out.println("Give me your user(REMEMBER IT): ");
        bufferRead = new BufferedReader(new InputStreamReader(System.in));
        user = bufferRead.readLine();

        System.out.println("Give me, Server IP (never use 127.0.0.1 or localhost): ");
        bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String ip = bufferRead.readLine();

        System.out.println("Give me, Server PORT (never use 127.0.0.1 or localhost): ");
        bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String port = bufferRead.readLine();

        RMI_STORE = "rmi://"+ip+":"+port+"/storage";
        aes = new AESSecurity("paSSwordPassword");

        ClientService service = new ClientService(user, aes);
        ClientInterface clientInterface = new ClientInterface(service);
        FileStorage storage = (FileStorage) Naming.lookup(RMI_STORE);
        Notifier notifier = new Notifier();
        storage.addCallback(notifier);

        System.out.println("Client connected to "+ RMI_STORE);
        while (true){
            clientInterface.showInterface(storage, notifier);
        }

    }

}