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
import java.util.ArrayList;

/**
 * Created by arnau on 11/11/2017.
 */
public class Client {

    private static String RMI_STORE;


    private static void getContent(StoreData storage, String title, String extension, String savePath) throws RemoteException {
        ObjectContent recover = storage.getObject(title, extension);
        if (recover == null) {
            return;
        }
        try {
            System.out.println("Content downloaded");
            recover.writeFile(savePath);
        }catch (IOException e){
            System.out.println("File not avaible");
            return;
        }
    }

    private static void getFromCategory(StoreData storage, String category) throws RemoteException{
        ArrayList<String> listContents = storage.getCategoryFilter(category);
        if (listContents == null) {
            System.out.println("List of contents with a extension  '" + category + "' is empty.");
            return;
        }
        System.out.println("List of contents with a extension  '" + category + "' :");
        for ( String content : listContents) {
            System.out.println("- NAME: '" + content+"'.");
        }

    }

    private static void newContent(StoreData storage, String path, String extension, String name)throws RemoteException{
        ObjectContent obj = new ObjectContent(path, extension, name);
        if (obj.getTitle() == null) {
            //System.out.println();
            return;
        }
        storage.storeObject(obj);
    }

    private static void actionAddContent(StoreData storage) throws IOException {
        System.out.println("Title of content : ");
        String contentName = new BufferedReader(new InputStreamReader(System.in)).readLine();

        System.out.println("extension of content, IMPORTANT follow this format , example for a pdf file: 'pdf'  : ");
        String extension = new BufferedReader(new InputStreamReader(System.in)).readLine();

        System.out.println("Path of content (give me full path or drop the file in the console): ");
        String path = new BufferedReader(new InputStreamReader(System.in)).readLine();
        newContent(storage, path, extension, contentName);
    }

    private static void actionGetContent(String contentName, String extension, StoreData storage, String savePath) throws RemoteException {
        try {
            getContent(storage, contentName, extension, savePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            Notifier notifier = new Notifier();
            storage.addCallback(notifier);

            System.out.println("Client connected to "+ RMI_STORE);
            while (true){
                System.out.println("What do you want to do? (upload) (download) (exit) (list)");
                String choice = new BufferedReader(new InputStreamReader(System.in)).readLine();
                if (choice.equals("upload")){
                    actionAddContent(storage);
                }
                else if (choice.equals("download")){
                    System.out.println("Title of content : ");
                    String contentName = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    System.out.println("Extension of content : ");
                    String extension = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    System.out.println("Path where you (client) want to save the content : ");
                    String savePath = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    actionGetContent(contentName, extension, storage, savePath);
                }else if (choice.equals("exit")){
                    storage.removeCallback(notifier);
                    System.exit(1);
                }else if (choice.equals("list")){
                    System.out.println("Give me a extension type and I will list all content with that extension , like for example 'pdf' :");
                    String extension = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    getFromCategory(storage, extension);
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