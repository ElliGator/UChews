package com.example.elli.uchews;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by Chris on 2/29/2016.
 * Default UserDao that should be used for user CRUD operations
 */
public class StandardUserDao implements UserDAO{

    private String BASE_WEBSERVICE_URL = "https://www.cise.ufl.edu/~cwhitten/UChews/";
    private String ADD_USER_PATH = "add_user.php";
    private String VALIDATE_USER_PATH = "validate_user.php";
    private String DELETE_USER_PATH = "delete_user.php";
    private String GET_USER_PATH = "get_user.php";

    @Override
    public boolean addUser(String email, String password, String fname, String lname, FactualLocality locality, JSONObject cuisine_stats) {
        String params = "email=" + email + "&password=" + password
                + "&fname=" + fname
                + "&lname=" + lname
                + "&locality=" + locality.getName();
        byte[] paramData = params.getBytes(StandardCharsets.UTF_8);
        int paramLength = paramData.length;

        try {
            HttpURLConnection conn = openWebServiceConnection(BASE_WEBSERVICE_URL + ADD_USER_PATH, "POST");
            conn.setRequestProperty("Content-Length", Integer.toString(paramLength));
            DataOutputStream wr = new DataOutputStream( conn.getOutputStream());
            wr.write(paramData);

            String response = readResponse(conn);
            if(response.charAt(0) == 't')
                return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    @Override
    public User getUser(String email, String password) {
        String params = "email=" + email + "&password=" + password;
        byte[] paramData = params.getBytes(StandardCharsets.UTF_8);
        int paramLength = paramData.length;

        try {
            HttpURLConnection conn = openWebServiceConnection(BASE_WEBSERVICE_URL + GET_USER_PATH, "POST");
            conn.setRequestProperty("Content-Length", Integer.toString(paramLength));
            DataOutputStream wr = new DataOutputStream( conn.getOutputStream());
            wr.write(paramData);

            String response = readResponse(conn);
            return parseResponse(response);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private User parseResponse(String response) throws JSONException {
        JSONObject resp = new JSONObject(response);
        String email = resp.getString("email");
        String fname = resp.getString("fname");
        String lname = resp.getString("lname");
        FactualLocality locality = FactualLocality.getLocalityByName(resp.getString("lname"));
        JSONObject cuisines = resp.getJSONObject("cuisine_stats");

        return new User(email, fname, lname, locality, cuisines);
    }

    @Override
    public boolean validateUser(String email, String password) {
        String params = "email=" + email + "&password=" + password;
        byte[] paramData = params.getBytes(StandardCharsets.UTF_8);
        int paramLength = paramData.length;

        try {
            HttpURLConnection conn = openWebServiceConnection(BASE_WEBSERVICE_URL + VALIDATE_USER_PATH, "POST");
            conn.setRequestProperty("Content-Length", Integer.toString(paramLength));
            DataOutputStream wr = new DataOutputStream( conn.getOutputStream());
            wr.write(paramData);

            String response = readResponse(conn);
            if(response.charAt(0) == 't')
                return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }


    @Override
    public boolean deleteUser(String email, String password) {
        String params = "email=" + email + "&password=" + password;
        byte[] paramData = params.getBytes(StandardCharsets.UTF_8);
        int paramLength = paramData.length;

        try {
            HttpURLConnection conn = openWebServiceConnection(BASE_WEBSERVICE_URL + DELETE_USER_PATH, "POST");
            conn.setRequestProperty("Content-Length", Integer.toString(paramLength));
            DataOutputStream wr = new DataOutputStream( conn.getOutputStream());
            wr.write(paramData);

            String response = readResponse(conn);
            if(response.charAt(0) == 't')
                return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    //Opens up a connection ready for a POST request
    private HttpURLConnection openWebServiceConnection(String url_path, String methodType) throws IOException {
        URL url = new URL(url_path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod(methodType);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("charset", "utf-8");

        return conn;
    }

    private String readResponse(HttpURLConnection conn) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response ="";
        String line ="";
        while((line = reader.readLine()) != null)
        {
            response = response.concat(line + "\n");
        }

        reader.close();

        return response;
    }
}
