package Client;

import Interface.StoreData;
import Implementation.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by arnau on 11/11/2017.
 */
public class Client {

    private static final int PORT = 40003;
    private static final String RMI_STORE = "rmi://localhost:" + PORT + "/storage";

    // TODO Filter by categories, List<>
    // storage.getListByCategory(category)



    private static void getContent(StoreData storage, String title) throws RemoteException {
        ObjectContent recover = storage.getObject(title);
        System.out.println(recover.getTitle());
        System.out.println(recover.getCategory());
        System.out.println(recover.getDuration());
    }

    private static void getFromCategory(StoreData storage, String category) throws RemoteException{
        ObjectContent recoveryFromList = storage.getObject(storage.getCategoryFilter("Action").get(0));
        System.out.println(recoveryFromList.getTitle());
        System.out.println(recoveryFromList.getCategory());
        System.out.println(recoveryFromList.getDuration());
    }
    private static void newContent(StoreData storage, String title, String Category)throws RemoteException{
        ObjectContent obj = new ObjectContent(title, 162, Category);
        Long key = storage.storeObject(obj);
    }
    public static void main(String[] args) {
        try {

            //RECUPEREM EL OBJECTE REMOT REGISTRAT PEL SERVIDOR, ENS AFEGIM COM A CALLBACK.
            StoreData storage = (StoreData) Naming.lookup(RMI_STORE);
            storage.addCallback(new Notifier());

            System.out.println("Client connected to "+ RMI_STORE);

            newContent(storage,"Film5","Action");
            newContent(storage,"Film6","Action");
            newContent(storage,"Film3","Action");
            newContent(storage,"Film4","Drama");

            getContent(storage,"Film4");

            getFromCategory(storage, "Action");


        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}