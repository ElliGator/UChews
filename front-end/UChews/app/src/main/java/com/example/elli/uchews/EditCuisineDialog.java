package com.example.elli.uchews;


import android.app.Dialog;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.AlertDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Elli on 4/4/2016.
 */
public class EditCuisineDialog extends DialogFragment {
    private String title;
    private Cuisine c;
    private String cuisine_name;
    private int weight;
    private DialogDataInterface mDialogInterface;

    public EditCuisineDialog() {
        //required empty constructor
    }

    public static EditCuisineDialog newInstance(String c, int weight) {
        EditCuisineDialog dialog = new EditCuisineDialog();
        Bundle bundle = new Bundle();

        bundle.putInt("Weight", weight);
        bundle.putString("Cuisine", c);
        dialog.setArguments(bundle);

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View view = li.inflate(R.layout.edit_cuisine_dialog, null);
        mDialogInterface = (DialogDataInterface) getTargetFragment();

        savedInstanceState = getArguments();

        if(savedInstanceState != null){
            this.cuisine_name = savedInstanceState.getString("Cuisine");
            this.c = Cuisine.getCuisine(cuisine_name);
            this.weight = savedInstanceState.getInt("Weight");
        }


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);


        // set edit_cuisine_dialog.xml to alertdialog builder
        alertDialogBuilder.setView(view);

        final TextView cuisine = (TextView) view.findViewById(R.id.cuisine_label);
        final EditText editWeight = (EditText) view.findViewById(R.id.edit_weight);

        cuisine.setText(cuisine_name);
        editWeight.setText(Integer.toString(weight));

        alertDialogBuilder
                .setTitle(title)
                .setIcon(android.R.drawable.ic_menu_edit)
                .setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String temp = editWeight.getText().toString();
                        int newWeight = Integer.parseInt(temp);
                        mDialogInterface.editCuisineWeight(c, newWeight);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel",
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
                        (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 1);

                ViewGroup.LayoutParams negParams = new LinearLayout.LayoutParams
                        (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 1);

                posButton.setLayoutParams(posParams);
                negButton.setLayoutParams(negParams);

                Resources res = getResources();
                int titleDividerId = res.getIdentifier("titleDivider", "id", "android");
                View titleDivider = alertDialog.findViewById(titleDividerId);
                int color = ContextCompat.getColor(getContext(), R.color.testing4);
                if (titleDivider != null)
                    titleDivider.setBackgroundColor(color);
            }
        });

        return alertDialog;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public interface DialogDataInterface {
        void editCuisineWeight(Cuisine c, int weight);
    }
}
