package rmiserver.Interface;

import org.json.JSONException;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface CoordinatorServer extends Remote{

    boolean deleteFile(String serial) throws IOException, JSONException;

    String getServer(String serial) throws IOException, JSONException;

    ArrayList<String> getExtension(String extension) throws IOException, JSONException;

    ArrayList<String> getFileNameFrom(String userName) throws IOException, JSONException;
    ArrayList<String> getFileIdFrom(String userName) throws IOException, JSONException;

    void addFileFromUser(String user, String serial, String address, String extension, String title, Boolean isEncrypted)  throws  RemoteException;


}
