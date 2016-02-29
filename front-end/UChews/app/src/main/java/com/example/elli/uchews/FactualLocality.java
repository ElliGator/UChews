package com.example.elli.uchews;

/**
 * Created by Chris on 2/24/2016.
 * Serves as container for our app's supported localities
 */
public enum FactualLocality {
    GAINESVILLE("Gainesville");

    private String name;

    FactualLocality(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static FactualLocality getLocalityByName(String name){
        for(FactualLocality f: FactualLocality.values()){
            if(f.name.equals(name))
                return f;
        }

        return null;
    }
}
