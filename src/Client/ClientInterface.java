package Client;

import Implementation.Notifier;
import Interface.FileStorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientInterface {

    public ClientService service;

    public ClientInterface(ClientService service) {
        this.service = service;
    }

    public void showInterface(FileStorage storage, Notifier notifier) throws Exception {
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

    public void uploadInterface(FileStorage storage) throws Exception {
        System.out.println("Encrpytion(y/n) : ");
        String encryption = new BufferedReader(new InputStreamReader(System.in)).readLine();
        if(encryption.equals("y")) {
            service.actionAddContent(storage, true);
        } else if (encryption.equals("n")) {
            service.actionAddContent(storage, false);
        } else {
            System.out.println("Commands: <download>, <upload>, <exit>");
        }
    }

    public void listInterface(FileStorage storage) throws IOException {
        System.out.println("Give me a extension type and I will list all content with that extension , like for example 'pdf' :");
        String extension = new BufferedReader(new InputStreamReader(System.in)).readLine();
        service.getFromCategory(storage, extension);
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

}
