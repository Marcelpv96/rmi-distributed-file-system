package Implementation;

import Client.ObjectContent;
import Interface.StoreData;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import static javafx.scene.input.KeyCode.L;

/**
 * Created by arnau on 11/11/2017.
 */
public class Storage extends UnicastRemoteObject implements StoreData {

    Map<String, Long> storedData = new HashMap<String, Long>();

    public Storage() throws RemoteException {
        super();
    }

    // TODO create handler before storing object
    @Override
    public Long storeObject(ObjectContent obj) throws RemoteException {
        //Initialized so try/catch doesn't kek
        Long serial = 0L;

        if (!storedData.containsKey(obj.getTitle())) {
            try {
                serial = System.currentTimeMillis();
                storedData.put(obj.getTitle(), serial);
                new File(serial.toString()).mkdirs();

                FileOutputStream f = new FileOutputStream(new File(serial.toString() + "/" + serial + "out.txt"));
                ObjectOutputStream o = new ObjectOutputStream(f);

                o.writeObject(obj);
                o.close();
                f.close();

            } catch (Exception e) {//Catch exception if any
                System.err.println("Error: " + e.getMessage());
            }
        }
        return storedData.get(obj.getTitle());
    }

    @Override
    public ObjectContent getObject(String title) throws RemoteException {

        ObjectContent object = new ObjectContent();

        try {
            Long serial = storedData.get(title);
            System.out.println(serial);
            System.out.println(title);

            FileInputStream fi = new FileInputStream(new File(serial.toString() + "/" + serial + "out.txt"));
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects
           object = (ObjectContent) oi.readObject();

            oi.close();
            fi.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return object;

    }

        @Override
    public int getSize() throws RemoteException {
        return storedData.size();
    }

    @Override
    public boolean isEmpty() throws RemoteException {
        return storedData.isEmpty();
    }
}
