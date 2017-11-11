package Client;

import Interface.StoreData;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by arnau on 11/11/2017.
 */
public class Client {

    private static final int PORT = 40000;
    private static final String RMI_STORE = "rmi://localhost:" + PORT + "/storage";

    // TODO Filter by categories, List<>
    // storage.getListByCategory(category)

    ObjectContent obj = new ObjectContent("Star Wars", 162, "Action");

    public static void main(String[] args) {
        try {

            StoreData storage = (StoreData) Naming.lookup(RMI_STORE);
            System.out.println("Client connected to "+ RMI_STORE);

            Long key = storage.storeData("Hello");
            System.out.println(storage.getData(key) + key);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}