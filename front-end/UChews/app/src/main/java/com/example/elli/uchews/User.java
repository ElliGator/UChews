package com.example.elli.uchews;

import org.json.JSONObject;

/**
 * Created by Chris on 2/29/2016.
 */
public class User {
    private String email;
    private String fname;
    private String lname;
    private FactualLocality locality;
    private JSONObject cuisine_stats;

    public User(String email, String fname, String lname, FactualLocality locality, JSONObject cuisine_stats) {
        this.email = email;
        this.fname = fname;
        this.lname = lname;
        this.locality = locality;
        this.cuisine_stats = cuisine_stats;
    }

    public String getEmail() {
        return email;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public FactualLocality getLocality() {
        return locality;
    }

    public JSONObject getCuisine_stats() {
        return cuisine_stats;
    }
}
