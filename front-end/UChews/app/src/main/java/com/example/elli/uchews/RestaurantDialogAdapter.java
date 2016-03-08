package com.example.elli.uchews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Elli on 3/4/2016.
 */
public class RestaurantDialogAdapter extends ArrayAdapter<Restaurant> {

    public RestaurantDialogAdapter(Context context, ArrayList<Restaurant> restaurants) {
        super(context, 0, restaurants);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Restaurant restaurant = getItem(position);

        if (convertView == null){
            //convertView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_row_item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.restaurant_name);
        TextView address = (TextView) convertView.findViewById(R.id.restaurant_address);

        name.setText(restaurant.getName());
        address.setText(restaurant.getAddress());

        return convertView;
    }
}
