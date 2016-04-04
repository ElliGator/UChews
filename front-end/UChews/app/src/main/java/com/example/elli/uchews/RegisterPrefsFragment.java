package com.example.elli.uchews;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import java.util.ArrayList;


/**
 * Created by Elli on 3/15/2016.
 */
public class RegisterPrefsFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private int mNumSelected = 0;
    private int MAX_PREFS = 2;
    private ArrayList<CompoundButton> selected;
    private Button submit;

    //Checkboxes
    private CheckBox american;
    private CheckBox bbq;
    private CheckBox burgers;
    private CheckBox chinese;
    private CheckBox french;
    private CheckBox indian;
    private CheckBox italian;
    private CheckBox japanese;
    private CheckBox korean;
    private CheckBox mexican;
    private CheckBox midEast;
    private CheckBox pizza;
    private CheckBox seafood;
    private CheckBox sushi;
    private CheckBox thai;
    private CheckBox vege;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selected = new ArrayList<>(2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_cuisines_layout, container, false);

        american = (CheckBox) view.findViewById(R.id.american_box);
        american.setOnCheckedChangeListener(checker);
        bbq = (CheckBox) view.findViewById(R.id.bbq_box);
        bbq.setOnCheckedChangeListener(checker);
        burgers = (CheckBox) view.findViewById(R.id.burger_box);
        burgers.setOnCheckedChangeListener(checker);
        chinese = (CheckBox) view.findViewById(R.id.chinese_box);
        chinese.setOnCheckedChangeListener(checker);
        french = (CheckBox) view.findViewById(R.id.french_box);
        french.setOnCheckedChangeListener(checker);
        indian = (CheckBox) view.findViewById(R.id.indian_box);
        indian.setOnCheckedChangeListener(checker);
        italian = (CheckBox) view.findViewById(R.id.italian_box);
        italian.setOnCheckedChangeListener(checker);
        japanese = (CheckBox) view.findViewById(R.id.japan_box);
        japanese.setOnCheckedChangeListener(checker);
        korean = (CheckBox) view.findViewById(R.id.korean_box);
        korean.setOnCheckedChangeListener(checker);
        mexican = (CheckBox) view.findViewById(R.id.mexican_box);
        mexican.setOnCheckedChangeListener(checker);
        midEast = (CheckBox) view.findViewById(R.id.mideast_box);
        midEast.setOnCheckedChangeListener(checker);
        pizza = (CheckBox) view.findViewById(R.id.pizza_box);
        pizza.setOnCheckedChangeListener(checker);
        seafood = (CheckBox) view.findViewById(R.id.cfood_box);
        seafood.setOnCheckedChangeListener(checker);
        sushi = (CheckBox) view.findViewById(R.id.sushi_box);
        sushi.setOnCheckedChangeListener(checker);
        thai = (CheckBox) view.findViewById(R.id.thai_box);
        thai.setOnCheckedChangeListener(checker);
        vege = (CheckBox) view.findViewById(R.id.vege_box);
        vege.setOnCheckedChangeListener(checker);

        submit = (Button) view.findViewById(R.id.submit_data);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String p1 = selected.get(0).getText().toString();
                String p2 = selected.get(1).getText().toString();
                Cuisine pref1 = Cuisine.getCuisine(p1);
                Cuisine pref2 = Cuisine.getCuisine(p2);
                mListener.onRegisterPrefs(pref1, pref2);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        void onRegisterPrefs(Cuisine pref1, Cuisine pref2);
    }

    OnCheckedChangeListener checker = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if(mNumSelected == MAX_PREFS && isChecked) {
                compoundButton.setChecked(false);
            } else if(isChecked){
                mNumSelected++;
                selected.add(compoundButton);
            } else if(!isChecked){
                mNumSelected--;
                selected.remove(compoundButton);
            }
        }
    };
}
