package com.example.elli.uchews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Chris on 2/23/2016.
 */
public class Restaurant {

    private String id;
    private String name;
    private String address;
    private String locality;
    private String region;
    private String zip_code;
    private double latitude;
    private double longitude;
    private JSONArray cuisines;
    private JSONObject hours; //TODO: Make a RestaurantHours class
    private String website;

    //Flags for fields that may be null
    private boolean hasCuisines;
    private boolean hasHours;
    private boolean hasWebsite;


    public Restaurant(String factual_id, String name, String address, String locality, String region, String zip_code, double latitude, double longitude, JSONArray cuisines, JSONObject hours, String website) {
        this.id = factual_id;
        this.name = name;
        this.address = address;
        this.locality = locality;
        this.region = region;
        this.zip_code = zip_code;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cuisines = cuisines;
        this.hours = hours;
        this.website = website;

        checkCuisines();
        checkHours();
        checkWebsite();
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getLocality() {
        return locality;
    }

    public String getRegion() {
        return region;
    }

    public String getZip_code() {
        return zip_code;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public JSONArray getCuisines() {
        return cuisines;
    }

    public JSONObject getHours() {
        return hours;
    }

    public String getWebsite() {
        return website;
    }

    public ArrayList<Cuisine> getCuisineEnums(){
        ArrayList<Cuisine> enums = new ArrayList<Cuisine>();
        for(int i = 0; i < this.cuisines.length(); i++){

            try {
                int id = this.cuisines.getInt(i);
                Cuisine curr = Cuisine.getCuisineById(id);
                enums.add(curr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return enums;
    }

    private void checkCuisines(){
        if(cuisines == null)
            hasCuisines = false;
        else
            hasCuisines = true;
    }

    private void checkHours(){
        if(hours == null)
            hasHours = false;
        else
            hasHours = true;
    }

    private void checkWebsite(){
        if(website == null)
            hasWebsite = false;
        else
            hasWebsite = true;
    }

}
