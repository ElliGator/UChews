package cw.wheel.widget;

/**
 * Created by Elli on 1/15/2016.
 */
public interface OnWheelClickedListener {

    /**
     * Callback method to be invoked when current item clicked
     * @param wheel the wheel view
     * @param itemIndex the index of clicked item
     */
    void onItemClicked(WheelView wheel, int itemIndex);
}
