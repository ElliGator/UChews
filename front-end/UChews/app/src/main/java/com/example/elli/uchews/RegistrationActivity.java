package com.example.elli.uchews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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
    Bundle bundle;


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
    public void onRegisterPrefs(Cuisine pref1, Cuisine pref2) {
        if(bundle != null) {
            userDao.addUser(bundle.getString("USER_EMAIL"), bundle.getString("USER_PASS"),
                    bundle.getString("USER_FNAME"), bundle.getString("USER_LNAME"),
                    FactualLocality.GAINESVILLE, pref1, pref2);
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(bundle); //to get extras in next activity "getIntent().getExtras()
        startActivity(intent);
    }
}
