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

    private static final int PORT = 40000;
    private static final String RMI_STORE = "rmi://localhost:" + PORT + "/storage";

    // TODO Filter by categories, List<>
    // storage.getListByCategory(category)


    public static void main(String[] args) {
        try {

            //RECUPEREM EL OBJECTE REMOT REGISTRAT PEL SERVIDOR, ENS AFEGIM COM A CALLBACK.
            StoreData storage = (StoreData) Naming.lookup(RMI_STORE);
            storage.addCallback(new ClientCallbackImpl());

            System.out.println("Client connected to "+ RMI_STORE);
            ObjectContent film1 = new ObjectContent("Star Wars 4", 162, "Action");


            Long key = storage.storeObject(film1);
            System.out.println(storage.getObject("Star Wars 4").getTitle());


        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}