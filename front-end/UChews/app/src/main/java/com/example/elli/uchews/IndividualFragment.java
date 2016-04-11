package com.example.elli.uchews;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;


public class IndividualFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private static final String PREFS_UNIQUE_IDENTIFIER = "com.example.uchews.user.data";
    private static final String USER_FNAME_KEY = "FIRST_NAME";
    private static final String USER_EMAIL_KEY = "EMAIL";
    private static final String USER_PASS_KEY = "PASSWORD";

    private TextView mCenterPlate;
    private Button notoday_btn;
    private Button yes_btn;
    private Button no_btn;
    private User user;
    private StandardUserDao userDao;
    private RestaurantSelector selector;
    private ArrayList<Restaurant> mRestaurants;
    private String rest_id;
    private String name;
    private String email;
    private String password;
    private int index = 0;


    public IndividualFragment() {
        // Required empty public constructor
    }

    /** Factory Method for Fragment creation **/
    public static IndividualFragment newInstance(String param1, String param2) {
        IndividualFragment fragment = new IndividualFragment();

        return fragment;
    }

    /** Fragment lifecycle methods **/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences(PREFS_UNIQUE_IDENTIFIER, Context.MODE_PRIVATE);
        userDao = new StandardUserDao();
        selector = new RestaurantSelector();

        email = sharedPrefs.getString(USER_EMAIL_KEY, "johnDoe@example.com");
        password = sharedPrefs.getString(USER_PASS_KEY, "1234");
        name = sharedPrefs.getString(USER_FNAME_KEY, "John");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_individual, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        //Restaurant display
        mCenterPlate = (TextView) getActivity().findViewById(R.id.rest_plate);
        mCenterPlate.setText(R.string.welcome);
        //"No" button
        no_btn = (Button) getActivity().findViewById(R.id.no_btn);
        //"Not Today" button
        notoday_btn = (Button) getActivity().findViewById(R.id.notToday_btn);
        //"Yes" button
        yes_btn = (Button) getActivity().findViewById(R.id.yes_btn);

        setOnClickListeners();
        //retrieveUserData();

        //String firstRest = getNextRestaurantName();
        //mCenterPlate.setText(firstRest);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        retrieveUserData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onIndividualFragmentInteraction(int position);
    }

    /**
     * Animates plate when user wants another suggestion
     */
    private void animatePlate() {
        ObjectAnimator animation = ObjectAnimator.ofFloat(mCenterPlate, "rotationY", 0.0f, 360f);
        animation.setDuration(3000);
        //animation.setRepeatCount(ObjectAnimator.INFINITE);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();
    }

    private void setOnClickListeners() {
        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animatePlate();
                //Rating -1
                next(Rating.NEGATIVE);
                mCenterPlate.setText(getNextRestaurantName());
            }
        });

        notoday_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animatePlate();
                //Rating 0
                next(Rating.NEUTRAL);
                mCenterPlate.setText(getNextRestaurantName());
            }
        });

        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Rating 1
                next(Rating.POSITIVE);
                //possibly lead to a fragment that gives restaurant info
            }
        });
    }

    private void next(Rating rating) {
        //TODO: log history needs to run asynchronously
        logUserHistory(rating);
        incrementIndex();
    }

    private void incrementIndex() {
        index++;
    }

    private String getNextRestaurantName() {
        Restaurant next = mRestaurants.get(index);
        setNextRestaurantId(next);

        return next.getName();
    }

    private void setNextRestaurantId(Restaurant restaurant) {
        this.rest_id = restaurant.getId();
    }

    private void retrieveUserData() {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                user = userDao.getUser(email, password);
                mRestaurants = selector.individualSelect(user);

                return true;
            }

            @Override
            protected void onPostExecute(Boolean b) {
                String firstRest = getNextRestaurantName();
                mCenterPlate.setText(firstRest);
                Log.d("Retrieving Data Test", "GetUser() successful?: " + b);
            }
        }.execute();
    }

    private void logUserHistory(Rating r) {
        final Rating userRating = r;
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                return userDao.logHistory(email, rest_id, userRating);
            }

            protected void onPostExecute(Boolean bool) {
                Log.d("Logging History", "Successfully Logged History? " + bool);
            }
        }.execute();
    }
}
