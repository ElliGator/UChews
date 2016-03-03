package com.example.elli.uchews;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Elli on 2/29/2016.
 */
public class RestaurantDialog extends DialogFragment {
    private ListView listView;
    private DialogAdapter adapter;
    private ArrayList<Restaurant> mRestaurants;
    private String title;

    //TODO: need to create adapter
    public RestaurantDialog() {
        //required empty constructor
    }

    public static RestaurantDialog newInstance(String title, ArrayList<Restaurant> list) {
        RestaurantDialog dialog = new RestaurantDialog();
        dialog.setTitle(title);
        dialog.setList(list);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_layout, container);
        getDialog().setTitle(title);

        listView = (ListView) view.findViewById(R.id.restaurant_list);
        adapter = new DialogAdapter(getContext(), mRestaurants);
        //TODO: causing null pointer
        listView.setAdapter(adapter);

        return view;
    }

    private void setTitle(String t) {
        this.title = t;
    }

    private void setList(ArrayList<Restaurant> list) {
        this.mRestaurants = list;
    }
}
