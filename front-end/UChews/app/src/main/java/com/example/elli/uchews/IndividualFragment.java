package com.example.elli.uchews;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ViewFlipper;



public class IndividualFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private ViewFlipper mViewFlipper;
    private Button chews_btn;
    private Button works_btn;
    private Button nah_btn;


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
        chews_btn = (Button) getActivity().findViewById(R.id.chews_btn);
        works_btn = (Button) getActivity().findViewById(R.id.no_btn);
        nah_btn = (Button) getActivity().findViewById(R.id.yes_btn);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_individual, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mViewFlipper = (ViewFlipper) getActivity().findViewById(R.id.view_flipper);
        mViewFlipper.setDisplayedChild(1);

        final GestureDetector gesture = new GestureDetector(getContext(), new MyGestureListener(mViewFlipper));

        mViewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("OnTouch", "In onTouch" + gesture.onTouchEvent(event));

                return gesture.onTouchEvent(event);
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int position) {
        if (mListener != null) {
            mListener.onIndividualFragmentInteraction(position);
        }
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
}
