package rmiclient.Client;

import org.json.JSONException;
import rmiserver.Implementation.Notifier;
import rmiserver.Interface.FileStorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.security.NoSuchAlgorithmException;

public class ClientInterface {

    public ClientService service;

    public ClientInterface(ClientService service) {
        this.service = service;
    }

    public void showInterface(FileStorage storage, Notifier notifier) throws Exception {
        System.out.println("What do you want to do? (upload) (download) (delete) (exit) (list) (modify)");
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
        }else if(choice.equals("delete")) {
            deleteInterface(storage);
        }else if(choice.equals("modify")) {
            modifyInterface(storage);
        }
        else{
            System.out.println("Commands: <download>, <upload>, <delete>, <exit>, <list>, <modify>");
        }
    }

    public void uploadInterface(FileStorage storage) throws Exception {
        System.out.println("Encrpytion(y/n) : ");
        String encryption = new BufferedReader(new InputStreamReader(System.in)).readLine();
        if(encryption.equals("y")) {
            service.actionAddContent(storage, true);
        } else if (encryption.equals("n")) {
            service.actionAddContent(storage, false);
        } else {
            System.out.println("Commands: <download>, <upload>, <delete>, <exit>, <list>, <modify>");
        }
    }

    public void listInterface(FileStorage storage) throws IOException, JSONException {
        System.out.println("You can filter by <extension>, <user>, <name> :");
        String by = new BufferedReader(new InputStreamReader(System.in)).readLine();
        if(by.equals("extension")) {
            System.out.println("Give me a extension type and I will list all content with that extension , like for example 'pdf' :");
            String extension = new BufferedReader(new InputStreamReader(System.in)).readLine();
            service.getFromCategory(storage, extension);
        } else if (by.equals("user")) {
            System.out.println("Gimme a user to serach attached files :");
            String user = new BufferedReader(new InputStreamReader(System.in)).readLine();
            service.getFromUser(storage, user);
        } else {
            System.out.println("Commands: <download>, <upload>, <delete>, <exit>, <list>, <modify>");
        }

    }

    public void downloadInterface(FileStorage storage) throws IOException {
        System.out.println("Title of content : ");
        String contentName = new BufferedReader(new InputStreamReader(System.in)).readLine();
        System.out.println("Extension of content : ");
        String extension = new BufferedReader(new InputStreamReader(System.in)).readLine();
        System.out.println("Path where you (client) want to save the content : ");
        String savePath = new BufferedReader(new InputStreamReader(System.in)).readLine();
        service.actionGetContent(contentName, extension, storage, savePath);
    }

    public void deleteInterface(FileStorage storage) throws IOException, NoSuchAlgorithmException, NotBoundException, ClassNotFoundException, JSONException {
        System.out.println("Title of content : ");
        String contentName = new BufferedReader(new InputStreamReader(System.in)).readLine();
        System.out.println("Extension of content : ");
        String extension = new BufferedReader(new InputStreamReader(System.in)).readLine();
        service.deleteContent(contentName, extension, storage);
    }

    public void modifyInterface(FileStorage storage) throws Exception {
        System.out.println("You have two options, modify the title or modify the content, What do you want? say <title> or <content> : ");
        String option = new BufferedReader(new InputStreamReader(System.in)).readLine();
        if (option.equals("title")){
            System.out.println("What title do you want to modify? ");
            String oldTitle = new BufferedReader(new InputStreamReader(System.in)).readLine();
            System.out.println("Extension of content : ");
            String extension = new BufferedReader(new InputStreamReader(System.in)).readLine();
            System.out.println("New title do you want ? ");
            String newTitle = new BufferedReader(new InputStreamReader(System.in)).readLine();
            service.modifyTitle(oldTitle, extension, newTitle, storage);
        }else if (option.equals("content")){
            System.out.println("What title do you want to modify? ");
            String title = new BufferedReader(new InputStreamReader(System.in)).readLine();
            System.out.println("Extension of content : ");
            String extension = new BufferedReader(new InputStreamReader(System.in)).readLine();
            System.out.println("Path of new content: ");
            String path = new BufferedReader(new InputStreamReader(System.in)).readLine();
            System.out.println("Encrpytion(y/n) : ");
            String encryption = new BufferedReader(new InputStreamReader(System.in)).readLine();
            if(encryption.equals("y")) {
                service.modifyContent(path, extension, title, true, storage);
            } else if (encryption.equals("n")) {
                service.modifyContent(path, extension, title, false, storage);
            } else {
                System.out.println("Commands: <download>, <upload>, <delete>, <exit>, <list>, <modify>");
            }
        }else{
            System.out.println("Rememeber the options: ");
            modifyInterface(storage);
        }
    }

}
