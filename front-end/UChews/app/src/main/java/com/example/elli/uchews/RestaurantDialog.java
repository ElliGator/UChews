package com.example.elli.uchews;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Elli on 2/29/2016.
 */
public class RestaurantDialog extends DialogFragment {
    private String title;
    private ArrayList<Restaurant> mRestaurants;
    private int index = 0;

    public RestaurantDialog() {
        //required default constructor
    }

    public RestaurantDialog newInstance(String title, ArrayList<Restaurant> list) {
        RestaurantDialog dialog = new RestaurantDialog();
        this.title = title;
        this.mRestaurants = list;

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View view = li.inflate(R.layout.dialog_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        // set dialog_layout.xml to alertdialog builder
        alertDialogBuilder.setView(view);

        final TextView name = (TextView) view.findViewById(R.id.restaurant_name);
        final TextView address = (TextView) view.findViewById(R.id.restaurant_address);

        Restaurant restaurant = mRestaurants.get(index);

        name.setText(restaurant.getName());
        address.setText(restaurant.getAddress());

        // set dialog message
        alertDialogBuilder
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton("Reselect", null)
                .setNegativeButton("Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button posButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                Button negButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

                LinearLayout.LayoutParams posParams = new LinearLayout.LayoutParams
                        (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, (float) 1);

                LayoutParams negParams = new LinearLayout.LayoutParams
                        (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, (float) 1);

                posButton.setLayoutParams(posParams);
                negButton.setLayoutParams(negParams);

                posButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Restaurant next;
                        index++;
                        if (index != mRestaurants.size()-1) {
                            next = mRestaurants.get(index);
                            name.setText(next.getName());
                            address.setText(next.getAddress());
                        }
                        else {
                            index = 0;
                            next = mRestaurants.get(index);
                            name.setText(next.getName());
                            address.setText(next.getAddress());
                        }
                    }
                });
            }
        });

        return alertDialog;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRestaurantList(ArrayList<Restaurant> list) {
        this.mRestaurants = list;
    }
}
