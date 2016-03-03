package com.example.elli.uchews;

import android.util.Log;

import com.factual.driver.Factual;
import com.factual.driver.Query;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Chris on 2/18/2016.
 * This class is responsible for selecting performing the logic to select a restaurant
 * for both the group and the individual feature. It should certainly allow for restricting
 * queries by factual id so that the same restaurant cannot be selected twice.
 */
public class RestaurantSelector {

    //TODO: Make a Factual Connector class that handles factual connections and stores this data
    private String FACTUAL_KEY = "QP92ZfMIRTSIWyao6UKYjcIXERdnOqyZ3yL6371a";
    private String FACTUAL_SECRET = "xofCpGJEnxOo4mND1M6KtygL369uNSWY3Fx3IZwR";
    private String FACTUAL_RESTAURANT_TABLE = "restaurants";
    private String FACTUAL_ID_FIELD = "factual_id";
    private String FACTUAL_NAME_FIELD = "name";
    private String FACTUAL_ZIPCODE_FIELD = "postcode";
    private String FACTUAL_LATITUDE_FIELD = "latitude";
    private String FACTUAL_LONGITUDE_FIELD = "longitude";
    private String FACTUAL_LOCALITY_FIELD = "locality";
    private String FACTUAL_REGION_FIELD = "region";
    private String FACTUAL_COUNTRY_FIELD = "country";
    private String FACTUAL_CUISINE_ID_FIELD = "category_ids";
    private String FACTUAL_RATING_FIELD = "rating";
    private String FACTUAL_ADDRESS_FIELD = "address";
    private String FACTUAL_HOURS_FIELD = "hours";
    private String FACTUAL_WEBSITE_FIELD = "name";
    private int queryLimit = 50;

    /**
     *
     * @param groupPrefs Hashmap containing the group's weighted cuisine preferences as (Cuisine, integer weight) pairs.
     * @return An arraylist of at most 50 restaurants from a given cuisine type.
     */
    public ArrayList<Restaurant> groupSelect(HashMap<Cuisine, Integer> groupPrefs,FactualLocality locality, FactualRegion region ){
        ArrayList<Restaurant> selectedRestaurants = new ArrayList<Restaurant>();

        //Select cuisine type
        Cuisine cSelection = weightedSelect(groupPrefs);

        //Perform factual connection
        Factual factual = new Factual(FACTUAL_KEY, FACTUAL_SECRET);

        //Construct and run query
        Query query = new Query().limit(queryLimit)
                .field(FACTUAL_LOCALITY_FIELD).beginsWith(locality.getName())
                .field(FACTUAL_REGION_FIELD).beginsWith(region.getName())
                .field(FACTUAL_COUNTRY_FIELD).beginsWith("us")
                .field(FACTUAL_CUISINE_ID_FIELD).includes(cSelection.getFactual_id())
                .sortDesc(FACTUAL_RATING_FIELD);

        String response = factual.fetch(FACTUAL_RESTAURANT_TABLE, query).getJson();

        try {
            selectedRestaurants = parseRestaurants(new JSONObject(response));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return selectedRestaurants;
    }

    public ArrayList<Restaurant> groupSelect(Cuisine cSelection,FactualLocality locality, FactualRegion region ){
        ArrayList<Restaurant> selectedRestaurants = new ArrayList<Restaurant>();

        //Perform factual connection
        Factual factual = new Factual(FACTUAL_KEY, FACTUAL_SECRET);

        //Construct and run query
        Query query = new Query().limit(queryLimit)
                .field(FACTUAL_LOCALITY_FIELD).beginsWith(locality.getName())
                .field(FACTUAL_REGION_FIELD).beginsWith(region.getName())
                .field(FACTUAL_COUNTRY_FIELD).beginsWith("us")
                .field(FACTUAL_CUISINE_ID_FIELD).includes(cSelection.getFactual_id())
                .sortDesc(FACTUAL_RATING_FIELD);

        String response = factual.fetch(FACTUAL_RESTAURANT_TABLE, query).getJson();
        Log.e("response", response);
        try {
            selectedRestaurants = parseRestaurants(new JSONObject(response));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("jsonerror", e.toString());
        }

        return selectedRestaurants;
    }

    public Cuisine weightedSelect(HashMap<Cuisine, Integer> weights){
        int totalWeight = 0;
        Iterator<Cuisine> i = weights.keySet().iterator();

        //Calculate total weight
        while(i.hasNext()) {
            Integer weight = weights.get(i.next());
            totalWeight += weight;
        }

        //Reset iterator
        i = weights.keySet().iterator();


        //Choose randomly
        int random = (int) (Math.random() * totalWeight);

        Cuisine selectedCuisine = Cuisine.BBQ;

        while(i.hasNext()) {
            Cuisine curr = i.next();
            Integer weight = weights.get(curr);
            random -= weight;

            if(random < 0)
            {
                selectedCuisine = curr;
                break;
            }
        }

        return selectedCuisine;
    }

    private ArrayList<Restaurant> parseRestaurants(JSONObject json){
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();


        JSONArray data = null;
        try {
            data = (JSONArray) ((JSONObject) json.get("response")).get("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int size = data.length();

        for(int i = 0; i < size; i++) {
            boolean discardRest = false;
            Restaurant curr = null;
            try {
                JSONObject currRest = data.getJSONObject(i);

                String id = currRest.getString(FACTUAL_ID_FIELD);
                String name = currRest.getString(FACTUAL_NAME_FIELD);
                String zipcode = currRest.getString(FACTUAL_ZIPCODE_FIELD);
                double latitude = currRest.getDouble(FACTUAL_LATITUDE_FIELD);
                double longitude = currRest.getDouble(FACTUAL_LONGITUDE_FIELD);
                String locality = currRest.getString(FACTUAL_LOCALITY_FIELD);
                String region = currRest.getString(FACTUAL_REGION_FIELD);
                String country = currRest.getString(FACTUAL_COUNTRY_FIELD);
                String address = currRest.getString(FACTUAL_ADDRESS_FIELD);
                JSONObject hours = null;
                JSONArray cuisines = null;
                String website = "";

                //These are the only fields that won't cause the restaurant to be discarded
                try {
                    hours = currRest.getJSONObject(FACTUAL_HOURS_FIELD);
                    cuisines = currRest.getJSONArray(FACTUAL_CUISINE_ID_FIELD);
                    website = currRest.getString(FACTUAL_WEBSITE_FIELD);
                }
                catch (JSONException e){
                    discardRest = false;
                }

                curr = new Restaurant(id, name, address, locality, region, zipcode, latitude, longitude, cuisines, hours, website);
            }
            catch (JSONException e){
                discardRest = true;
            }


            if(curr != null && !discardRest)
                restaurants.add(curr);
        }

        return restaurants;
    }

}
