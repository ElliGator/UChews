package com.example.elli.uchews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Elli on 3/15/2016.
 */
public class RegistrationActivity extends FragmentActivity implements RegisterFragment.OnFragmentInteractionListener,
        RegisterPrefsFragment.OnFragmentInteractionListener {

    private StandardUserDao userDao;
    private Bundle bundle;
    private static final String PREFS_UNIQUE_IDENTIFIER = "com.example.uchews.user.data";
    private static final String USER_FNAME_KEY = "FIRST_NAME";
    private static final String USER_EMAIL_KEY = "EMAIL";
    private static final String USER_PASS_KEY = "PASSWORD";

    /**
     * Factory Method for Fragment creation
     **/
    public static RegistrationActivity newInstance(String param1, String param2) {
        RegistrationActivity fragment = new RegistrationActivity();

        return fragment;
    }

    /**
     * Fragment lifecycle methods
     **/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDao = new StandardUserDao();
        if(findViewById(R.id.fragment_container) != null) {
            if(savedInstanceState != null) {
                return;
            }

            RegisterFragment registerFragment = new RegisterFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, registerFragment).commit();
        }
    }

    @Override
    public void onRegisterBasicInfo(Bundle userData) {
        bundle = new Bundle(userData);
        RegisterPrefsFragment cuisinePrefs = new RegisterPrefsFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, cuisinePrefs);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onRegisterPrefs(final Cuisine pref1, final Cuisine pref2) {
        if(bundle != null) {
            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params) {
                    boolean wasAdded = userDao.addUser(bundle.getString("USER_EMAIL"), bundle.getString("USER_PASS"),
                            bundle.getString("USER_FNAME"), bundle.getString("USER_LNAME"),
                            FactualLocality.GAINESVILLE, pref1, pref2);
                    return wasAdded;
                }

                @Override
                protected void onPostExecute(Boolean b) {
                    Log.d("Registration Test", "AddedUser() response: " + b);
                }
            }.execute();

        }

        Intent intent = new Intent(this, MainActivity.class);
        saveDataToSharedPrefs(bundle);
        intent.putExtras(bundle); //to get extras in next activity "getIntent().getExtras()
        startActivity(intent);
    }

    private void saveDataToSharedPrefs(Bundle ubundle) {
        SharedPreferences sharedPreferences = this.getSharedPreferences
                (PREFS_UNIQUE_IDENTIFIER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_EMAIL_KEY, ubundle.getString("USER_EMAIL"));
        editor.putString(USER_PASS_KEY, ubundle.getString("USER_PASS"));
        editor.putString(USER_FNAME_KEY, ubundle.getString("USER_FNAME"));

        editor.apply();
    }
}
