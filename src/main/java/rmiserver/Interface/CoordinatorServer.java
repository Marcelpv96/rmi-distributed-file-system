package rmiserver.Interface;

import org.json.JSONException;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface CoordinatorServer extends Remote{

    String getServer(String serial) throws IOException, JSONException;

    ArrayList<String> getExtension(String extension) throws IOException, JSONException;
    ArrayList<String> getFileFrom(String user) throws IOException, JSONException;

    void addFileFromUser(String user, String serial, String address, String extension, String title, Boolean isEncrypted)  throws  RemoteException;
    boolean removeFileFromUser(String user, String serial) throws  RemoteException;

    boolean removeServer(String address, String serial) throws RemoteException;
    boolean removeCategory(String extension, String title) throws RemoteException;

}
