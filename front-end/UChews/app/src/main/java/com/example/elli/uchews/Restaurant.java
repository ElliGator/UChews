package com.example.elli.uchews;

import org.json.JSONObject;

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
    private JSONObject cuisines; //TODO: Actually parse these into an arraylist
    private JSONObject hours; //TODO: Make a RestaurantHours class
    private String website;


    public Restaurant(String factual_id, String name, String address, String locality, String region, String zip_code, double latitude, double longitude, JSONObject cuisines, JSONObject hours, String website) {
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

    public JSONObject getCuisine() {
        return cuisines;
    }

    public JSONObject getHours() {
        return hours;
    }

    public String getWebsite() {
        return website;
    }
}
