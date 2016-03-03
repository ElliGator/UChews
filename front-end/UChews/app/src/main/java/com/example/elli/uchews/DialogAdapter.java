package com.example.elli.uchews;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Elli on 3/1/2016.
 */
public class DialogAdapter extends ArrayAdapter<Restaurant> {
    private List<Restaurant> restaurantList;

    public DialogAdapter(Context context, List<Restaurant> objects) {
        super(context, 0, objects);

        restaurantList = objects;
    }
}
