package com.example.elli.uchews;

/**
 * Created by Chris on 2/24/2016.
 * Serves as container for our app's supported regions.
 */
public enum FactualRegion {
    FLORIDA("FL");

    private String name;

    FactualRegion(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
