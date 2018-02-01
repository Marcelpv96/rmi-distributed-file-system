package rmiclient.Client;

import SecurityLayer.AESSecurity;
import exceptions.BadPassword;
import rmiserver.Implementation.Notifier;
import rmiserver.Interface.FileStorage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;

public class Client {

    private static String RMI_STORE;
    private static String user;
    private static String password;
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

        System.out.println("Give me your password(REMEMBER IT): ");
        bufferRead = new BufferedReader(new InputStreamReader(System.in));
        password = bufferRead.readLine();

        System.out.println("Give me, rmiserver.File IP (never use 127.0.0.1 or localhost): ");
        bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String ip = bufferRead.readLine();

        System.out.println("Give me, rmiserver.File PORT (never use 127.0.0.1 or localhost): ");
        bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String port = bufferRead.readLine();

        RMI_STORE = "rmi://"+ip+":"+port+"/storage";
        aes = new AESSecurity("paSSwordPassword");


        try {
            FileStorage storage = (FileStorage) Naming.lookup(RMI_STORE);
            ClientService service = new ClientService(user, aes, password, storage);
            ClientInterface clientInterface = new ClientInterface(service);
            Notifier notifier = new Notifier();
            storage.addCallback(notifier);
            System.out.println("rmiclient.Client connected to "+ RMI_STORE);
            while (true){
                clientInterface.showInterface(storage, notifier);
            }
        }catch (BadPassword e){
            System.out.println("Bad user or password try another time.");
        }


    }

}