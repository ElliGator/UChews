package com.example.elli.uchews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
    private OnFragmentInteractionListener mListener;
    private Button chews_btn;
    private LinearLayout cuisineList;
    private SlotMachineAdapter slotMachineAdapter;
    // Wheel scrolled flag
    private boolean wheelScrolled;
    private HashMap<Cuisine, Integer> mCuisine_weights;
    private ArrayList<Restaurant> mRestaurants;
    private Cuisine[] indexes;
    private int global_index = 0;
    private InitWheelTask mTask;
    private SpinWheelTask mSpinTask;
    private RestaurantDialog restaurantDialog;


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
        indexes = new Cuisine[17];
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
        mTask = new InitWheelTask();
        mTask.execute(R.id.slot_1);

        //Chews button
        chews_btn = (Button) getActivity().findViewById(R.id.grp_chews_btn);
        chews_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mixWheel(R.id.slot_1);

                mSpinTask = new SpinWheelTask();
                mSpinTask.execute();

                //TODO: will return arraylist of restaurants to display
                //mRestaurants = mSelector.groupSelect(mCuisine_weights, mUser.getLocality(), FactualRegion.FLORIDA);
            }
        });
        chews_btn.setEnabled(false);
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
        mTask.cancel(true);
        mSpinTask.cancel(true);
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
                    chews_btn.setEnabled(true);
                    TextView tv = (TextView) v;
                    String sel_cuisine = tv.getText().toString();
                    addCuisine(sel_cuisine);
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
            Log.d("DEBUG ==>", "PRE - checking setInWheel value " + c.getName()+ " " + c.isInWheel());
            if(c.getName().equals(text) && !c.isInWheel()) {
                slotMachineAdapter.addImage(c.getImage());

                //adds weight to the cuisine
                addCuisineWeight(c);
                //cuisine has been added to wheel
                c.setInWheel(true);
                Log.d("DEBUG ==>", "POST - checking setInWheel value " + c.getName()+ " " + c.isInWheel());
                break;
            }
            else if(c.getName().equals(text) && c.isInWheel()){
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
            indexes[global_index] = c;
            incrementGlobalIndex();
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

        //Cuisine chosen = mSelector.weightedSelect(mCuisine_weights);
        //wheel.setCurrentItem(getIndexOfCuisine(chosen));
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
     **************HELPER METHODS****************
     ********************************************
     ********************************************/
    private int getIndexOfCuisine(Cuisine c) {
        int i;
        for(i = 0; i < indexes.length; i++) {
            if(indexes[i].equals(c))
                Log.d("DEBUG ==>", "index being returned is " + i);
                return i+1;
        }

        return i;
    }

    private void incrementGlobalIndex() {
        global_index++;
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
                R.mipmap.wheel_ic
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

    /********************************************
     ********************************************
     *************ASYNCHRONOUS TASKS*************
     ********************************************
     ********************************************/

    private class InitWheelTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... id) {
            initWheel(id[0]);
            return null;
        }
    }

    private class SpinWheelTask extends AsyncTask<Void, Integer, Cuisine> {
        WheelView wheel = getWheel(R.id.slot_1);
        RestaurantSelector mSelector = new RestaurantSelector();

        @Override
        protected Cuisine doInBackground(Void... v) {
            Cuisine chosen = mSelector.weightedSelect(mCuisine_weights);
            mRestaurants = mSelector.groupSelect(mCuisine_weights, FactualLocality.GAINESVILLE, FactualRegion.FLORIDA);
            Log.d("DEBUG ==>", "Size of restaurants arraylist" + mRestaurants.size());
            return chosen;
        }

        @Override
        protected void onPostExecute(Cuisine c) {
            restaurantDialog = new RestaurantDialog();
            if(wheel != null) {
                Log.d("DEBUG ==>", "Index of " + c.getName() + " is " + getIndexOfCuisine(c));
                wheel.setCurrentItem(getIndexOfCuisine(c));
                //Dialog
                restaurantDialog = RestaurantDialog.newInstance(c.getName() + "Restaurants", mRestaurants);
                restaurantDialog.show(getFragmentManager(), c.getName() + "Restaurants");
            }
        }
    }
}
