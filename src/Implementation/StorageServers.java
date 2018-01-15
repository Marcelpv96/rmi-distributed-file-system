package Implementation;

import Interface.StoreServers;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marcelpv96 on 15/1/18.
 */

public class StorageServers extends UnicastRemoteObject implements StoreServers {

    private static Map<String, String> serverContents;

    public StorageServers() throws RemoteException {
        super();
        serverContents = new HashMap<>();

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
