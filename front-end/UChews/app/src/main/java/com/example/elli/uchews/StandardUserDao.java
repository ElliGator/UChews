package com.example.elli.uchews;

import android.util.Log;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Chris on 2/29/2016.
 * Default UserDao that should be used for user CRUD operations
 * Note: This class's methods will hash all passwords it takes as input
 */
public class StandardUserDao implements UserDAO{

    private String BASE_WEBSERVICE_URL = "https://www.cise.ufl.edu/~cwhitten/UChews/";
    private String ADD_USER_PATH = "add_user.php";
    private String VALIDATE_USER_PATH = "validate_user.php";
    private String DELETE_USER_PATH = "delete_user.php";
    private String GET_USER_PATH = "get_user.php";
    private String LOG_HISTORY_PATH = "log_history.php";

    private int DEFAULT_PRIMARY_CUISINE_WEIGHT = 10;
    private int DEFAULT_SECONDARY_CUISINE_WEIGHT = 5;

    @Override
    public boolean addUser(String email, String password, String fname, String lname, FactualLocality locality) {
        password = hashPassword(password);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);
        params.put("fname", fname);
        params.put("lname", lname);
        params.put("locality", locality.getName());

        try {
            HttpURLConnection conn = WebServiceConnector.openWebServiceConnection(BASE_WEBSERVICE_URL + ADD_USER_PATH, WebServiceConnector.Method.POST, params);
            String response = WebServiceConnector.readResponse(conn);

            if(response.charAt(0) == 't')
                return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public boolean addUser(String email, String password, String fname, String lname, FactualLocality locality, Cuisine primaryC, Cuisine secondaryC) {
        password = hashPassword(password);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);
        params.put("fname", fname);
        params.put("lname", lname);
        params.put("locality", locality.getName());

        //Attempt to create string param for cuisine stats
        String cuisineParams = "{}";
        try {
            cuisineParams = getBasicCuisineStatsParams(primaryC, secondaryC);
        } catch (JSONException e) {
            e.printStackTrace();
            cuisineParams = "{}";
        }

        params.put("cuisines", cuisineParams);

        try {
            HttpURLConnection conn = WebServiceConnector.openWebServiceConnection(BASE_WEBSERVICE_URL + ADD_USER_PATH, WebServiceConnector.Method.POST, params);
            String response = WebServiceConnector.readResponse(conn);

            if(response.charAt(0) == 't')
                return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    /**
     * Takes in a users primary and secondary cuisine favorites and creates a string representation
     * of a JSON object containing the cuisines and some default weights.
     * @param primaryC
     * @param secondaryC
     * @return
     */
    private String getBasicCuisineStatsParams(Cuisine primaryC, Cuisine secondaryC) throws JSONException {
        JSONObject o = new JSONObject();
        o.put(Integer.toString(primaryC.getFactual_id()), DEFAULT_PRIMARY_CUISINE_WEIGHT);
        o.put(Integer.toString(secondaryC.getFactual_id()), DEFAULT_SECONDARY_CUISINE_WEIGHT);

        return o.toString();
    }

    @Override
    public User getUser(String email, String password) {
        String hashedPassword = hashPassword(password);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", hashedPassword);

        try {
            HttpURLConnection conn = WebServiceConnector.openWebServiceConnection(BASE_WEBSERVICE_URL + GET_USER_PATH, WebServiceConnector.Method.POST, params);

            String response = WebServiceConnector.readResponse(conn);
            return parseResponse(response, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private User parseResponse(String response, String password) throws JSONException {
        JSONObject resp = new JSONObject(response);
        String email = resp.getString("email");
        String fname = resp.getString("fname");
        String lname = resp.getString("lname");
        FactualLocality locality = FactualLocality.getLocalityByName(resp.getString("locality"));
        JSONObject cuisines = resp.getJSONObject("cuisine_stats");

        return new User(email, password , fname, lname, locality, cuisines);
    }

    @Override
    public boolean validateUser(String email, String password) {
        password = hashPassword(password);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);

        try {
            HttpURLConnection conn = WebServiceConnector.openWebServiceConnection(BASE_WEBSERVICE_URL + VALIDATE_USER_PATH, WebServiceConnector.Method.POST, params);
            String response = WebServiceConnector.readResponse(conn);

            if(response.charAt(0) == 't')
                return true;

        } catch (IOException e) {
            e.printStackTrace();
            //Log.e("validateSuccess", e.getMessage());
            return false;
        }

        return false;
    }

    @Override
    public boolean deleteUser(String email, String password) {
        password = hashPassword(password);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);

        try {
            HttpURLConnection conn = WebServiceConnector.openWebServiceConnection(BASE_WEBSERVICE_URL + DELETE_USER_PATH, WebServiceConnector.Method.POST, params);

            String response = WebServiceConnector.readResponse(conn);
            if(response.charAt(0) == 't')
                return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }


    public boolean logHistory(String email, String password, Restaurant restaurant, Rating rating) {
        Date currTime = Calendar.getInstance().getTime();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currTime);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);
        params.put("rest_id", restaurant.getId());
        params.put("rating", Integer.toString(rating.getMagnitude()));
        params.put("timestamp", timestamp);
        params.put("cuisines", restaurant.getCuisines().toString());


        try {
            HttpURLConnection conn = WebServiceConnector.openWebServiceConnection(BASE_WEBSERVICE_URL + LOG_HISTORY_PATH, WebServiceConnector.Method.POST, params);

            String response = WebServiceConnector.readResponse(conn);
            //Log.d("Backend Test", response);
            if (response.charAt(0) == 't')
                return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }


    //Helper Methods//
    private String hashPassword(String unhashedPwd){
        return new String(Hex.encodeHex(DigestUtils.sha1(unhashedPwd)));
    }
}
