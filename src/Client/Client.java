package Client;


import Implementation.Notifier;
import Interface.StoreData;

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


    private static void getContent(StoreData storage, String title, String savePath) throws RemoteException {
        ObjectContent recover = storage.getObject(title);
        try {
            System.out.println("Content downloaded");
            recover.writeFile(savePath);
        }catch (IOException e){
            System.out.println("SOME ERROR");
        }
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

    private static void newContent(StoreData storage, String path, String extension, String name)throws RemoteException{
        ObjectContent obj = new ObjectContent(path, extension, name);
        Long key = storage.storeObject(obj);
    }

    private static void actionAddContent(StoreData storage) throws IOException {
        System.out.println("Title of content : ");
        String contentName = new BufferedReader(new InputStreamReader(System.in)).readLine();

        System.out.println("extension of content, IMPORTANT follow this format , example for a pdf file: '.pdf'  : ");
        String extension = new BufferedReader(new InputStreamReader(System.in)).readLine();

        System.out.println("Path of content (give me full path or drop the file in the console): ");
        String path = new BufferedReader(new InputStreamReader(System.in)).readLine();
        newContent(storage, path, extension, contentName);
    }

    private static void actionGetContent(String contentName, StoreData storage, String savePath) throws RemoteException {
        getContent(storage, contentName, savePath);
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
                    System.out.println("Path where you (client) want to save the content : ");
                    String savePath = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    actionGetContent(contentName, storage, savePath);
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