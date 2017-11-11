package Implementation;

import Interface.StoreData;
import sun.misc.IOUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by arnau on 11/11/2017.
 */
public class Storage extends UnicastRemoteObject implements StoreData {

    ArrayList<String> storedData = new ArrayList<String>();
    Map<Long, String> myMap = new HashMap<Long, String>();


    public Storage() throws RemoteException {
        super();
    }

    @Override
    public Long storeData(String data) throws RemoteException {

        Long serial_key = System.currentTimeMillis();
        myMap.put(serial_key, data);

        try{
            // Create file
            new File(serial_key.toString()).mkdirs();
            FileWriter fstream = new FileWriter(serial_key.toString()+"/" + serial_key + "out.txt");
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(data);
            //Close the output stream
            out.close();
        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        return serial_key;
    }

    @Override
    public String getData(Long serial_key) throws RemoteException{
        return myMap.get(serial_key);
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
