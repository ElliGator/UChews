package com.example.elli.uchews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import cw.wheel.widget.AbstractWheelAdapter;
import cw.wheel.widget.OnWheelChangedListener;
import cw.wheel.widget.OnWheelScrollListener;
import cw.wheel.widget.WheelView;


public class GroupFragment extends Fragment implements EditCuisineDialog.DialogDataInterface {
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
    private SpinWheelTask mSpinTask;
    private RestaurantDialog restaurantDialog;
    private EditCuisineDialog editDialog;
    private int mFragId;


    public GroupFragment() {
        // Required empty public constructor
    }

    public static GroupFragment newInstance(String param1, String param2) {
        GroupFragment fragment = new GroupFragment();

        return fragment;
    }

    /**
     ============================================
     =============LIFE CYCLE METHODS=============
     ============================================ **/
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
        mFragId = this.getId();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        //Initialize wheel view
        initWheel(R.id.slot_1);

        //Chews button
        chews_btn = (Button) getActivity().findViewById(R.id.grp_chews_btn);
        chews_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mixWheel(R.id.slot_1);

                mSpinTask = new SpinWheelTask();
                mSpinTask.execute();
            }
        });
        chews_btn.setEnabled(false);

        //Supported cuisines
        cuisineList = (LinearLayout) getActivity().findViewById(R.id.cuisine_list);
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
        mSpinTask.cancel(true);
    }

    /**
     ============================================
     =================LISTENERS==================
     ============================================ **/

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
            //no need of updating status
        }
    };

    // Wheel changed listener
    private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            /*if (!wheelScrolled) {
                no need of updating status
            }*/
        }
    };


    /**
     ============================================
     ===========CUISINE LIST METHODS=============
     ============================================ **/

    /**
     * Loads supported Cuisines into group tab
     */
    //TODO: find out how to dynamically add nested views into linear layout
    private void loadCuisines(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v;

        /*Populates Cuisine list with supported cuisines*/
        for(Cuisine c : Cuisine.values()){
            v = inflater.inflate(R.layout.cuisine_box_layout, cuisineList, false);
            TextView cuisine = (TextView) v.findViewById(R.id.box);
            //TextView decrement = (TextView) v.findViewById(R.id.decrement);
            //TextView display_weight = (TextView) v.findViewById(R.id.count);

            cuisine.setText(c.getName());
            cuisine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chews_btn.setEnabled(true);
                    TextView tv = (TextView) v;
                    String sel_cuisine = tv.getText().toString();
                    addCuisine(sel_cuisine);
                }
            });
            cuisine.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    TextView v = (TextView) view;
                    String c = v.getText().toString();

                    Cuisine f = Cuisine.getCuisine(c);
                    if (mCuisine_weights.containsKey(f)) {
                        int fWeight = mCuisine_weights.get(f);
                        editDialog = EditCuisineDialog.newInstance(c, fWeight);
                        editDialog.setTargetFragment(getFragmentManager().findFragmentById(mFragId), 0);
                        editDialog.setTitle("Edit " + c);
                        editDialog.show(getFragmentManager(), "display");
                    } else {
                        return false;
                    }

                    return true;
                }
            });

            /*decrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });*/


            cuisineList.addView(cuisine);
        }
    }

    /**
     * Adds selected cuisine to wheel
     * @param text selected cuisine String (text)
     */
    public void addCuisine(String text) {
        for(Cuisine c: Cuisine.values()){
            if(c.getName().equals(text) && !c.isInWheel()) {
                slotMachineAdapter.addImage(c.getImage());

                //adds weight to the cuisine
                addCuisineWeight(c);
                //cuisine has been added to wheel
                c.setInWheel(true);
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

    /**
     * Changes weight of user selected cuisine
     * @param c Cuisine that user selected
     * @param newWeight that user entered
     */
    @Override
    public void editCuisineWeight(Cuisine c, int newWeight) {
        if(mCuisine_weights.containsKey(c)) {
            mCuisine_weights.put(c, newWeight);
        }
        else {
            Toast msg = Toast.makeText(getContext(), "Cuisine has not been added",
                    Toast.LENGTH_LONG);
            msg.show();
        }
    }

    /**
     ============================================
     =============WHEELVIEW METHODS==============
     ============================================ **/

    /**
     * Initializes wheel
     * @param id the wheel widget Id
     */
    private void initWheel(int id) {
        WheelView wheel = getWheel(id);
        wheel.setViewAdapter(slotMachineAdapter);
        wheel.setCurrentItem((int) (Math.random() * 10));

        wheel.addChangingListener(changedListener);
        wheel.addScrollingListener(scrolledListener);
        wheel.setInterpolator(new LinearInterpolator());
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
     ============================================
     ==============HELPER METHODS================
     ============================================ **/

    /**
     * Gets index of the Cuisine to set current item in wheel
     * @param c Cuisine that was selected by weight
     * @return the index of the Cuisine
     */
    private int getIndexOfCuisine(Cuisine c) {
        /*WheelView wheel = getWheel(R.id.slot_1);
        int currentItem = wheel.getCurrentItem();
        if(currentItem == c.getImage()) {
            return
        }*/
        int i;
        for(i = 0; i < indexes.length; i++) {
            Log.d("DEBUG ==>", "Current cuisine in array" + indexes[i].getName());
            Log.d("DEBUG ==>", "Passed in cuisine" + c.getName());
            if(indexes[i].equals(c)) {
                Log.d("DEBUG ==>", "index being returned is " + i);
                return i;
            }
        }

        Log.d("DEBUG ==>", "outside loop normal return is " + i);
        return i;
    }

    /**
     * Increments the global index for inserting and
     * tracking the index of Cuisines in the wheel
     */
    private void incrementGlobalIndex() {
        global_index++;
        Log.d("DEBUG ==>", "The global index is " + global_index);
    }

    /**
     ============================================
     ===========SLOT MACHINE ADAPTER=============
     ============================================ **/
    private class SlotMachineAdapter extends AbstractWheelAdapter {
        // Image size
        final int IMAGE_WIDTH = 700;
        final int IMAGE_HEIGHT = 190;

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

    /**
     ============================================
     =============ASYNCHRONOUS TASK==============
     ============================================ **/

    private class SpinWheelTask extends AsyncTask<Void, Integer, Cuisine> {
        WheelView wheel = getWheel(R.id.slot_1);
        RestaurantSelector mSelector = new RestaurantSelector();

        @Override
        protected Cuisine doInBackground(Void... v) {
            Cuisine chosen = mSelector.weightedSelect(mCuisine_weights);
            mRestaurants = mSelector.restaurantSelect(chosen, FactualLocality.GAINESVILLE, FactualRegion.FLORIDA);
            return chosen;
        }

        @Override
        protected void onPostExecute(Cuisine c) {
            restaurantDialog = new RestaurantDialog();
            restaurantDialog.setTitle(c.getName() + " Restaurants");
            restaurantDialog.setRestaurantList(mRestaurants);

            //Wheel index starts @ 1 for added images
            int temp = getIndexOfCuisine(c) + 1;
            wheel.setCurrentItem(temp, true);
            //Dialog
            restaurantDialog.show(getFragmentManager(), "restaurant_dialog");
        }
    }
}
