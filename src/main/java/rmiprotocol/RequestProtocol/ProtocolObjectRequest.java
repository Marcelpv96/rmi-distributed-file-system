package rmiprotocol.RequestProtocol;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import webservice.model.File;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ProtocolObjectRequest {


    public static void POST_call(String urlString, String name, Long id, String category, String address){
        try {
            URL url = new URL (urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            File file = new File();
            file.setId(id);
            file.setFileName(name);
            file.setCategory(category);
            file.setAddress(address);

            String input = new Gson().toJson(file);

            OutputStream wr= (conn.getOutputStream());
            wr.write(input.getBytes());
            wr.flush();


            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONArray GET_call_category(String url) throws JSONException, IOException {
        try {
            URL obj = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection) obj.openConnection();
            httpCon.setRequestMethod("GET");
            httpCon.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            httpCon.setDoInput(true);

            InputStream in = httpCon.getInputStream();
            String result = getStringFromInputStream(in);
            in.close();
            httpCon.disconnect();
            return new JSONArray(result);


        } catch (Exception e) {
            return new JSONArray("[{}]");
        }
    }

    public static JSONObject GET_call(String url) throws JSONException, IOException {
        try {
            URL obj = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection) obj.openConnection();
            httpCon.setRequestMethod("GET");
            httpCon.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            httpCon.setDoInput(true);

            InputStream in = httpCon.getInputStream();
            String result = getStringFromInputStream(in);
            in.close();
            httpCon.disconnect();
            return new JSONObject(result);


        } catch (Exception e) {
            return new JSONObject("{}");
        }
    }

    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();


    }
    public static void main(String[] args) throws JSONException, IOException {
        JSONArray res = GET_call_category("http://e451f45f.ngrok.io/server/category/1");
        ArrayList<String> results = new ArrayList<>();
        for (int i = 0;i<res.length();i++){
            results.add(res.getJSONObject(i).getString("fileName"));
        }
        System.out.println(results);
    }

}
