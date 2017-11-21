package Interface;

import java.rmi.Remote;
/**
 * Created by Marcelpv96 on 21/11/17.
 */
public interface ClientCallback extends Remote{
    void callMe	(String	message) throws java.rmi.RemoteException;
}
