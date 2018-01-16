package Implementation;

import Interface.CoordinatorServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marcelpv96 on 15/1/18.
 */

public class Coordinator extends UnicastRemoteObject implements CoordinatorServer {

    private static Map<String, String> serverContents;
    private static Map<String, ArrayList<String>> categories;

    public Coordinator() throws RemoteException {
        super();
        serverContents = new HashMap<>();
        categories = new HashMap<>();
    }


    @Override
    public ArrayList<String> getCategory(String category){
        return categories.get(category);
    }

    @Override
    public void addCategory(String extension, String title){
        categories.get(extension).add(title);
    }



    @Override
    public void addServer(String address, String content) throws RemoteException{
        serverContents.put(content, address);
    }

    @Override
    public String getServer(String content) throws RemoteException{
        return serverContents.get(content);
    }


}
