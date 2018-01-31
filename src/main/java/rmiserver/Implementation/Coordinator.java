package rmiserver.Implementation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rmiprotocol.RequestProtocol.ProtocolObjectRequest;
import rmiserver.Interface.CoordinatorServer;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Coordinator extends UnicastRemoteObject implements CoordinatorServer {

    private static String webserviceAddress;

    public Coordinator(String webservice) throws RemoteException {
        super();
        webserviceAddress = webservice;
    }

    @Override
    public ArrayList<String> getExtension(String extension) throws IOException, JSONException {
        JSONArray res = ProtocolObjectRequest.GET_call_extension(webserviceAddress+"/file/extension/"+extension);
        ArrayList<String> results = new ArrayList<>();
        System.out.println("Contents with extension ."+extension+" : ");
        for (int i = 0;i<res.length();i++){
            try {
                results.add(res.getJSONObject(i).getString("fileName"));
            } catch (Exception e) {
                System.out.println("No extension.");
            }
        }
        return results;
    }

    @Override
    public ArrayList<String> getFileNameFrom(String userName) throws IOException, JSONException {
        JSONArray res = ProtocolObjectRequest.GET_call_extension(webserviceAddress+"/file/user/"+userName);
        ArrayList<String> results = new ArrayList<>();
        System.out.println("Contents from user "+userName+" : ");
        for (int i = 0;i<res.length();i++){
            try {
                results.add(res.getJSONObject(i).getString("fileName"));
            } catch (Exception e) {
                System.out.println("No user.");
            }
        }
        return results;

    }

    @Override
    public ArrayList<String> getExtensionFromName(String name) throws IOException, JSONException {
        JSONArray res = ProtocolObjectRequest.GET_call_extension(webserviceAddress+"/file/name/"+name);
        ArrayList<String> results = new ArrayList<>();
        System.out.println("Contents associated with "+name+" : ");
        for (int i = 0;i<res.length();i++){
            try {
                results.add(res.getJSONObject(i).getString("extension"));
            } catch (Exception e) {
                System.out.println("No user.");
            }
        }
        return results;
    }

    @Override
    public ArrayList<String> getFileIdFrom(String userName) throws IOException, JSONException {
        JSONArray res = ProtocolObjectRequest.GET_call_extension(webserviceAddress+"/file/user/"+userName);
        ArrayList<String> results = new ArrayList<>();
        System.out.println("Contents from user "+userName+" : ");
        for (int i = 0;i<res.length();i++){
            try {
                results.add(res.getJSONObject(i).getString("id"));
            } catch (Exception e) {
                System.out.println("No user.");
            }
        }
        return results;

    }

    @Override
    public void addFileFromUser(String user, String serial, String address, String extension, String title, Boolean isEncrypted) {

        ProtocolObjectRequest.POST_call(
                webserviceAddress+"/file",
                user,
                serial,
                address,
                extension,
                title,
                isEncrypted);


    }

    @Override
    public boolean deleteFile(String serial) throws IOException, JSONException {
        return ProtocolObjectRequest.DELETE_call(webserviceAddress+"/file/delete/"+serial);
    }


    @Override
    public String getServer(String serial) throws IOException, JSONException {
        JSONObject res = ProtocolObjectRequest.GET_call(webserviceAddress+"/file/id/"+serial);
        // TODO maybe check if res == null
        return res.getString("address");
    }


}
