package rmiclient.Client;

import org.json.JSONException;
import rmiprotocol.DataTransferProtocol.ObjectContent;
import rmiprotocol.DataTransferProtocol.ObjectRequest;
import rmiserver.Interface.FileStorage;
import SecurityLayer.AESSecurity;
import SecurityLayer.CheckSum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class ClientService {

    private String user;
    private AESSecurity aes;

    public ClientService(String user,  AESSecurity aes) {
        this.user = user;
        this.aes = aes;
    }

    public void getContent(FileStorage storage, String title, String extension, String savePath) throws IOException, NotBoundException, ClassNotFoundException, NoSuchAlgorithmException, JSONException {

        ObjectRequest request = new ObjectRequest();
        request.setExtension(extension);
        request.setTitle(title);
        request.setUser(user);

        ObjectContent recover = storage.getObject(request);
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

    public void getFromCategory(FileStorage storage, String category) throws IOException, JSONException {
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

    public void newContent(FileStorage storage, String path, String extension, String name, Boolean encrypt) throws Exception {
        ObjectContent obj = new ObjectContent(path, extension, name, encrypt, aes, user);

        storage.storeObject(obj, CheckSum.getFrom(obj));
    }

    public void actionAddContent(FileStorage storage, Boolean encrypt) throws Exception {
        System.out.println("Title of content : ");
        String contentName = new BufferedReader(new InputStreamReader(System.in)).readLine();

        System.out.println("extension of content, IMPORTANT follow this format , example for a pdf file: 'pdf'  : ");
        String extension = new BufferedReader(new InputStreamReader(System.in)).readLine();

        System.out.println("Path of content (give me full path or drop the file in the console): ");
        String path = new BufferedReader(new InputStreamReader(System.in)).readLine();
        newContent(storage, path, extension, contentName, encrypt);
    }

    public void actionGetContent(String contentName, String extension, FileStorage storage, String savePath) throws RemoteException {
        try {
            getContent(storage, contentName, extension, savePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteContent(String contentName, String extension, FileStorage storage) throws ClassNotFoundException, NotBoundException, NoSuchAlgorithmException, IOException, JSONException {
        ObjectRequest request = new ObjectRequest();
        request.setUser(user);
        request.setTitle(contentName);
        request.setExtension(extension);
        System.out.println("Deleting Content:");
        if (storage.deleteObject(request)) {
            System.out.println("Delete succeed.");
        } else {
            System.out.println("Delete failed.");
        }
    }

    public void modifyTitle(String oldTitle, String extension, String newTitle, FileStorage storage) throws IOException, NoSuchAlgorithmException, NotBoundException, ClassNotFoundException, InterruptedException, JSONException {
        ObjectRequest request = new ObjectRequest();
        request.setUser(user);
        request.setTitle(oldTitle);
        request.setExtension(extension);
        System.out.println("Modifying Title of Content:");
        if (storage.modifyObject(request, newTitle)) {
            System.out.println("Modify succeed.");
        } else {
            System.out.println("Modify failed.");
        }
    }

    public void modifyContent(String path, String extension, String title, boolean encryption, FileStorage storage) throws Exception {
        ObjectContent obj = new ObjectContent(path, extension, title, encryption, aes, user);

        if (storage.modifyObject(obj)) {
            System.out.println("Modify succeed.");
        } else {
            System.out.println("Modify failed.");
        }
    }

    public void getFromUser(FileStorage storage, String user) throws IOException, JSONException {
        ArrayList<String> listContents = storage.getUserFilter(user);
        if (listContents == null) {
            System.out.println("List of contents from user  '" + user + "' is empty.");
            return;
        }
        System.out.println("List of contents with a extension  '" + user + "' :");
        for ( String content : listContents) {
            System.out.println("- NAME: '" + content+"'.");
        }
    }
}
