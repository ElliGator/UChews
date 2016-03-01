package com.example.elli.uchews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import cw.wheel.widget.AbstractWheelAdapter;
import cw.wheel.widget.OnWheelChangedListener;
import cw.wheel.widget.OnWheelScrollListener;
import cw.wheel.widget.WheelView;

// TODO: make sure images are added only once to wheel and pass weights to backend
// TODO: make dialog popup after cuisine is selected and show available restaurants
public class GroupFragment extends Fragment {
    private User mUser;
    private RestaurantSelector mSelector;
    private OnFragmentInteractionListener mListener;
    private Button chews_btn;
    private LinearLayout cuisineList;
    private SlotMachineAdapter slotMachineAdapter;
    // Wheel scrolled flag
    private boolean wheelScrolled;
    private HashMap<Cuisine, Integer> mCuisine_weights;
    private ArrayList<Restaurant> mRestaurants;


    public GroupFragment() {
        // Required empty public constructor
    }

    public static GroupFragment newInstance(String param1, String param2) {
        GroupFragment fragment = new GroupFragment();

        return fragment;
    }

    /********************************************
     ********************************************
     *************LIFE CYCLE METHODS*************
     ********************************************
     ********************************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wheelScrolled = false;
        slotMachineAdapter = new SlotMachineAdapter(getContext());
        mCuisine_weights = new HashMap<Cuisine, Integer>(17);
        //getActivity().setContentView(R.layout.fragment_group);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        //Thread initializes wheel view
        // TODO: Does this cause a memory leak?
        Thread workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                initWheel(R.id.slot_1);
            }
        });

        workerThread.start();

        //Chews button
        chews_btn = (Button) getActivity().findViewById(R.id.grp_chews_btn);
        chews_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mixWheel(R.id.slot_1);
                //TODO: will return arraylist of restaurants to display
                mRestaurants = mSelector.groupSelect(mCuisine_weights, mUser.getLocality(), FactualRegion.FLORIDA);
            }
        });
        //Supported cuisines
        cuisineList = (LinearLayout) getActivity().findViewById(R.id.cuisine_list);

        //updateStatus();
        loadCuisines();
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

    /********************************************
     ********************************************
     *****************LISTENERS******************
     ********************************************
     ********************************************/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onGroupFragmentInteraction(int position);
    }

    // Wheel scrolled listener
    OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        public void onScrollingStarted(WheelView wheel) {
            wheelScrolled = true;
        }
        public void onScrollingFinished(WheelView wheel) {
            wheelScrolled = false;
            updateStatus();
        }
    };

    // Wheel changed listener
    private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if (!wheelScrolled) {
                updateStatus();
            }
        }
    };

    /********************************************
     ********************************************
     ***********CUISINE LIST METHODS*************
     ********************************************
     ********************************************/
    private void loadCuisines(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v;

        /*Populates Cuisine list with supported cuisines*/
        for(Cuisine c : Cuisine.values()){
            v = inflater.inflate(R.layout.cuisine_box_layout, cuisineList, false);
            TextView cuisine = (TextView) v.findViewById(R.id.box);

            cuisine.setText(c.getName());
            cuisine.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    TextView tv = (TextView) v;
                    String sel_cuisine = tv.getText().toString();
                    addCuisine(sel_cuisine);
                    //weights.add(tv.getText().toString());
                }
            });

            cuisineList.addView(cuisine);
        }
    }

    /**
     * Adds selected cuisine to wheel
     * @param text selected cuisine String (text)
     */
    private void addCuisine(String text) {
        for(Cuisine c: Cuisine.values()){
            if(c.getName().equals(text)) {
                slotMachineAdapter.addImage(c.getImage());
                addCuisineWeight(c);
                break;
            }
        }
        slotMachineAdapter.notifyDataChangedEvent();
    }

    /**
     * Adds weight to selected cuisine
     * @param c is the selected cuisine
     */
    private void addCuisineWeight(Cuisine c) {
        if(!mCuisine_weights.containsKey(c)) {
            mCuisine_weights.put(c, 1);
        }
        else {
            int curr_weight = mCuisine_weights.get(c);
            curr_weight = curr_weight + 1;
            mCuisine_weights.put(c, curr_weight);
        }
    }

    /********************************************
     ********************************************
     ************WHEELVIEW METHODS***************
     ********************************************
     ********************************************/

    /**
     * Initializes wheel
     * @param id the wheel widget Id
     */
    private void initWheel(int id) {
        WheelView wheel = getWheel(id);
        wheel.setViewAdapter(slotMachineAdapter);
        wheel.setCurrentItem((int)(Math.random() * 10));

        wheel.addChangingListener(changedListener);
        wheel.addScrollingListener(scrolledListener);
        wheel.setCyclic(true);
        wheel.setEnabled(false);
    }

    /**
     * Returns wheel by Id
     * @param id the wheel Id
     * @return the wheel with passed Id
     */
    private WheelView getWheel(int id) {
        return (WheelView) getActivity().findViewById(id);
    }

    /**
     * Mixes wheel
     * @param id the wheel id
     */
    private void mixWheel(int id) {
        WheelView wheel = getWheel(id);
        wheel.scroll(-350 + (int) (Math.random() * 50), 2000);
    }

    /**
     * Tests wheels
     * @return true
     */
    private boolean test() {
        int value = getWheel(R.id.slot_1).getCurrentItem();
        return true;//testWheelValue(R.id.slot_2, value) && testWheelValue(R.id.slot_3, value);
    }

    /**
     * Tests wheel value
     * @param id the wheel Id
     * @param value the value to test
     * @return true if wheel value is equal to passed value
     */
    private boolean testWheelValue(int id, int value) {
        return getWheel(id).getCurrentItem() == value;
    }

    /**
     * Updates status
     */
    private void updateStatus() {
        //called test
    }



    /********************************************
     ********************************************
     ***********SLOT MACHINE ADAPTER*************
     ********************************************
     ********************************************/
    private class SlotMachineAdapter extends AbstractWheelAdapter {
        // Image size
        final int IMAGE_WIDTH = 700;
        final int IMAGE_HEIGHT = 150;

        // Slot machine symbols
        private final int items[] = new int[] {
                R.mipmap.ic_flipper
        };

        // Cached images
        private List<SoftReference<Bitmap>> images;

        // Layout inflater
        private Context context;

        /**
         * Constructor
         */
        public SlotMachineAdapter(Context context) {
            this.context = context;
            images = new ArrayList<SoftReference<Bitmap>>(17);
            for (int id : items) {
                images.add(new SoftReference<Bitmap>(loadImage(id)));
            }
        }

        /**
         * Loads image from resources
         */
        private Bitmap loadImage(int id) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, IMAGE_WIDTH, IMAGE_HEIGHT, true);
            bitmap.recycle();
            return scaled;
        }

        @Override
        public int getItemsCount() {
            return images.size();
        }

        // Layout params for image view
        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT);

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            ImageView img;
            if (cachedView != null) {
                img = (ImageView) cachedView;
            } else {
                img = new ImageView(context);
            }
            img.setLayoutParams(params);
            SoftReference<Bitmap> bitmapRef = images.get(index);
            Bitmap bitmap = bitmapRef.get();
            if (bitmap == null) {
                bitmap = loadImage(items[index]);
                images.set(index, new SoftReference<Bitmap>(bitmap));
            }
            img.setImageBitmap(bitmap);

            return img;
        }

        //Adds Cuisine image to list of images
        public void addImage(int img){
            images.add(new SoftReference<Bitmap>(loadImage(img)));
        }
    }
}
