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
            ObjectContent obj2 = new ObjectContent("Kek Lords", 90, "Action");

            //Long key = storage.storeObject(obj);
            //Long key2 = storage.storeObject(obj2);

            ObjectContent recover = storage.getObject("Star Wars 2");
            ObjectContent recover2 = storage.getObject("Kek Lords");

            System.out.println(recover.getTitle());
            System.out.println(recover.getCategory());
            System.out.println(recover.getDuration());

            System.out.println(recover2.getTitle());
            System.out.println(recover2.getCategory());
            System.out.println(recover2.getDuration());

            System.out.println(storage.getCategoryFilter("Action"));
            ObjectContent recoveryFromList = storage.getObject(storage.getCategoryFilter("Action").get(0));

            System.out.println(recoveryFromList.getTitle());
            System.out.println(recoveryFromList.getCategory());
            System.out.println(recoveryFromList.getDuration());


        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}