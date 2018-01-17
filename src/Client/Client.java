package Client;


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

    private static void getContent(FileStorage storage, String title, String extension, String savePath) throws IOException, NotBoundException, ClassNotFoundException, NoSuchAlgorithmException {
        ObjectContent recover = storage.getObject(title, extension);
        if (recover == null) {
            return;
        }
        try {
            System.out.println("Content downloaded");
            recover.writeFile(savePath, aes);
        }catch (IOException e){
            System.out.println("File not avaible");
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getFromCategory(FileStorage storage, String category) throws RemoteException{
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

    private static void newContent(FileStorage storage, String path, String extension, String name, Boolean encrypt) throws Exception {
        ObjectContent obj = new ObjectContent(path, extension, name, encrypt, aes, user);

        storage.storeObject(obj, CheckSum.getFrom(obj));
    }

    private static void actionAddContent(FileStorage storage, Boolean encrypt) throws Exception {
        System.out.println("Title of content : ");
        String contentName = new BufferedReader(new InputStreamReader(System.in)).readLine();

        System.out.println("extension of content, IMPORTANT follow this format , example for a pdf file: 'pdf'  : ");
        String extension = new BufferedReader(new InputStreamReader(System.in)).readLine();

        System.out.println("Path of content (give me full path or drop the file in the console): ");
        String path = new BufferedReader(new InputStreamReader(System.in)).readLine();
        newContent(storage, path, extension, contentName, encrypt);
    }

    private static void actionGetContent(String contentName, String extension, FileStorage storage, String savePath) throws RemoteException {
        try {
            getContent(storage, contentName, extension, savePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

            //RECUPEREM EL OBJECTE REMOT REGISTRAT PEL SERVIDOR, ENS AFEGIM COM A CALLBACK.
            FileStorage storage = (FileStorage) Naming.lookup(RMI_STORE);
            Notifier notifier = new Notifier();
            storage.addCallback(notifier);

            System.out.println("Client connected to "+ RMI_STORE);
            while (true){
                System.out.println("What do you want to do? (upload) (download) (exit) (list)");
                String choice = new BufferedReader(new InputStreamReader(System.in)).readLine();
                if (choice.equals("upload")){
                    uploadInterface(storage);
                }
                else if (choice.equals("download")){
                    downloadInterface(storage);
                }else if (choice.equals("exit")){
                    storage.removeCallback(notifier);
                    System.exit(1);
                }else if (choice.equals("list")){
                    listInterface(storage);
                }
                else{
                    System.out.println("Commands: <download>, <upload>, <exit>");
                }
            }

    }

    private static void uploadInterface(FileStorage storage) throws Exception {
        System.out.println("Encrpytion(y/n) : ");
        String encryption = new BufferedReader(new InputStreamReader(System.in)).readLine();
        if(encryption.equals("y")) {
            actionAddContent(storage, true);
        } else if (encryption.equals("n")) {
            actionAddContent(storage, false);
        } else {
            System.out.println("Commands: <download>, <upload>, <exit>");
        }
    }

    private static void listInterface(FileStorage storage) throws IOException {
        System.out.println("Give me a extension type and I will list all content with that extension , like for example 'pdf' :");
        String extension = new BufferedReader(new InputStreamReader(System.in)).readLine();
        getFromCategory(storage, extension);
    }

    private static void downloadInterface(FileStorage storage) throws IOException {
        System.out.println("Title of content : ");
        String contentName = new BufferedReader(new InputStreamReader(System.in)).readLine();
        System.out.println("Extension of content : ");
        String extension = new BufferedReader(new InputStreamReader(System.in)).readLine();
        System.out.println("Path where you (client) want to save the content : ");
        String savePath = new BufferedReader(new InputStreamReader(System.in)).readLine();
        actionGetContent(contentName, extension, storage, savePath);
    }


}