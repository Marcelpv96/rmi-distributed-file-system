package Client;


import Interface.StoreData;
import Implementation.Notifier;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by arnau on 11/11/2017.
 */
public class Client {

    private static String RMI_STORE;


    private static void getContent(StoreData storage, String title) throws RemoteException {
        ObjectContent recover = storage.getObject(title);
        System.out.println("RECOVER CONTENT:");
        System.out.println(recover.getTitle());
        System.out.println(recover.getCategory());
        System.out.println(recover.getDuration());
        System.out.println("END RECOVER CONTENT:");

    }

    private static void getFromCategory(StoreData storage, String category) throws RemoteException{
        ObjectContent recoveryFromList = storage.getObject(storage.getCategoryFilter("Action").get(0));
        System.out.println("RECOVER CONTENT FROM LIST:");
        System.out.println(recoveryFromList.getTitle());
        System.out.println(recoveryFromList.getCategory());
        System.out.println(recoveryFromList.getDuration());
        System.out.println("END RECOVER CONTENT FROM LIST:");

        ObjectContent recoveryFromList1 = storage.getObject(storage.getCategoryFilter("Action").get(1));
        System.out.println("RECOVER CONTENT FROM LIST:");
        System.out.println(recoveryFromList1.getTitle());
        System.out.println(recoveryFromList1.getCategory());
        System.out.println(recoveryFromList1.getDuration());
        System.out.println("END RECOVER CONTENT FROM LIST:");

    }

    private static void newContent(StoreData storage, String title, String Category)throws RemoteException{
        ObjectContent obj = new ObjectContent(title, 162, Category);
        Long key = storage.storeObject(obj);
    }

    private static void actionAddContent(StoreData storage) throws IOException {
        System.out.println("Title of content : ");
        String contentName = new BufferedReader(new InputStreamReader(System.in)).readLine();

        System.out.println("Category of content : ");
        String category = new BufferedReader(new InputStreamReader(System.in)).readLine();

        newContent(storage,contentName,category);
    }

    private static void actionGetContent(String contentName, StoreData storage) throws RemoteException {
        getContent(storage,contentName);
    }

    public static void main(String[] args) {
        try {
            BufferedReader bufferRead;

            System.out.println("Give me, Server IP (never use 127.0.0.1 or localhost): ");
            bufferRead = new BufferedReader(new InputStreamReader(System.in));
            String ip = bufferRead.readLine();

            System.out.println("Give me, Server PORT (never use 127.0.0.1 or localhost): ");
            bufferRead = new BufferedReader(new InputStreamReader(System.in));
            String port = bufferRead.readLine();

            RMI_STORE = "rmi://"+ip+":"+port+"/storage";

            //RECUPEREM EL OBJECTE REMOT REGISTRAT PEL SERVIDOR, ENS AFEGIM COM A CALLBACK.
            StoreData storage = (StoreData) Naming.lookup(RMI_STORE);
            storage.addCallback(new Notifier());

            System.out.println("Client connected to "+ RMI_STORE);
            while (true){
                System.out.println("What do you want to do? (upload) (download) (exit)");
                String choice = new BufferedReader(new InputStreamReader(System.in)).readLine();
                if (choice.equals("upload")){
                    actionAddContent(storage);
                }
                else if (choice.equals("download")){
                    System.out.println("Title of content : ");
                    String contentName = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    actionGetContent(contentName, storage);
                }else if (choice.equals("exit")){
                    System.exit(1);
                }
                else{
                    System.out.println("Commands: <download>, <upload>, <exit>");
                }
            }


        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }  catch(IOException e) {
            e.printStackTrace();
        }
    }


}