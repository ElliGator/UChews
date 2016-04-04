package com.example.elli.uchews;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class RegisterFragment extends Fragment {
    private EditText mEmail;
    private EditText mPassword;
    private EditText mFirst;
    private EditText mLast;
    private Spinner mCity;
    private Spinner mState;
    private Button mNext;
    private TextView message;
    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }


    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mEmail = (EditText) view.findViewById(R.id.email_input);
        mPassword = (EditText) view.findViewById(R.id.pass_input);
        mFirst = (EditText) view.findViewById(R.id.fname_input);
        mLast = (EditText) view.findViewById(R.id.lname_input);
        message = (TextView) view.findViewById(R.id.message);
        mNext = (Button) view.findViewById(R.id.next);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmpty(mEmail) || isEmpty(mFirst) || isEmpty(mLast)) {
                    message.setText(R.string.message);
                } else {
                    collectInfo(savedInstanceState);
                }
            }
        });

        addSpinnerItems(view);

        // Inflate the layout for this fragment
        return view;
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
        void onRegisterBasicInfo(Bundle data);
    }

    private void addSpinnerItems(View view) {
        mCity = (Spinner) view.findViewById(R.id.city);
        mState = (Spinner) view.findViewById(R.id.state);

        //Data structure holding supported cities
        List<String> cities = new ArrayList<>();
        cities.add(FactualLocality.GAINESVILLE.getName());

        //Data structure holding supported states
        List<String> states = new ArrayList<>();
        states.add(FactualRegion.FLORIDA.getName());

        //Cities Adapter for Cities Spinner
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //State Adapter for State Spinner
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_item, states);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mCity.setAdapter(cityAdapter);
        mState.setAdapter(stateAdapter);
    }

    private void collectInfo(Bundle user_info) {
        if(user_info == null) {
            user_info = new Bundle();
        }

        String email = mEmail.getText().toString();
        String pass = mPassword.getText().toString();
        String first = mFirst.getText().toString();
        String last = mLast.getText().toString();
        String city = mCity.getSelectedItem().toString();

        user_info.putString("USER_EMAIL", email);
        user_info.putString("USER_PASS", pass);
        user_info.putString("USER_FNAME", first);
        user_info.putString("USER_LNAME", last);
        user_info.putString("USER_CITY", city);

        mListener.onRegisterBasicInfo(user_info);
    }

    private boolean isEmpty(EditText myText) {
        return myText.getText().toString().trim().length() == 0;
    }
}
