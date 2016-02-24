package com.example.elli.uchews;

import org.json.JSONObject;

/**
 * Created by Chris on 2/23/2016.
 */
public class Restaurant {

    private String name;
    private String address;
    private String locality;
    private String region;
    private String zip_code;
    private float latitude;
    private float longitude;
    private Cuisine cuisine;
    private JSONObject hours; //TODO: Make a RestaurantHours class
    private String website;


    public Restaurant(String name, String address, String locality, String region, String zip_code, float latitude, float longitude, Cuisine cuisine, JSONObject hours, String website) {
        this.name = name;
        this.address = address;
        this.locality = locality;
        this.region = region;
        this.zip_code = zip_code;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cuisine = cuisine;
        this.hours = hours;
        this.website = website;
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

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public Cuisine getCuisine() {
        return cuisine;
    }

    public JSONObject getHours() {
        return hours;
    }

    public String getWebsite() {
        return website;
    }
}
