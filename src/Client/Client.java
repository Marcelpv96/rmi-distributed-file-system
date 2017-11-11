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


    public static void main(String[] args) {
        try {

            StoreData storage = (StoreData) Naming.lookup(RMI_STORE);
            System.out.println("Client connected to "+ RMI_STORE);


            ObjectContent obj = new ObjectContent("Star Wars 2", 162, "Action");
            Long key = storage.storeObject(obj);
            System.out.println("Debug kekeke");
            ObjectContent obj2 = storage.getObject("Star Wars 2");
            System.out.println(obj2.getTitle());
            System.out.println(obj2.getCategory());
            System.out.println(obj2.getDuration());


        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}