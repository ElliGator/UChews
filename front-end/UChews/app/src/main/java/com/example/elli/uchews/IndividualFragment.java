package com.example.elli.uchews;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
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



public class IndividualFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private TextView mCenterPlate;
    private Button notoday_btn;
    private Button yes_btn;
    private Button no_btn;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_individual, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        //"No" button
        no_btn = (Button) getActivity().findViewById(R.id.no_btn);
        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animatePlate();
            }
        });
        //"Not Today" button
        notoday_btn = (Button) getActivity().findViewById(R.id.notToday_btn);
        notoday_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animatePlate();
            }
        });
        //"Yes" button
        yes_btn = (Button) getActivity().findViewById(R.id.yes_btn);

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
        void onIndividualFragmentInteraction(int position);
    }

    /**
     * Animates plate when user wants another suggestion
     */
    private void animatePlate() {
        mCenterPlate = (TextView) getActivity().findViewById(R.id.rest_plate);

        ObjectAnimator animation = ObjectAnimator.ofFloat(mCenterPlate, "rotationY", 0.0f, 360f);
        animation.setDuration(3000);
        //animation.setRepeatCount(ObjectAnimator.INFINITE);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();
    }
}
