package com.example.elli.uchews;

/**
 * Created by Chris on 3/6/2016.
 */
public enum Rating {
    POSITIVE(1),
    NEUTRAL(0),
    NEGATIVE(-1);

    private int magnitude;

    Rating(int magnitude){
        this.magnitude = magnitude;
    }

    public int getMagnitude() {
        return magnitude;
    }
}
