package com.example.elli.uchews;

import android.graphics.Matrix;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;

/**
 * Created by Elli on 1/12/2016.
 */
public class MyGestureListener extends SimpleOnGestureListener {

    View mView;
    private Matrix translate;
    private Matrix animateStart;
    private Interpolator animateInterpolator;
    private long startTime;
    private long endTime;
    private float totalAnimDx;
    private float totalAnimDy;

    public MyGestureListener(View v) {
        mView = v;
        translate = new Matrix();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        /*Log.i("OnFling", "OnFling called");
        final int SWIPE_MIN_DISTANCE = 120;
        final int SWIPE_THRESHOLD_VELOCITY = 200;

        if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            return false; // Bottom to top
        }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            return false; // Top to bottom
        }*/

        Log.v("DEBUGGING", "onFling");
        final float distanceTimeFactor = 0.4f;
        final float totalDx = (distanceTimeFactor * velocityX / 2);
        final float totalDy = (distanceTimeFactor * velocityY / 2);

        //onAnimateMove(totalDx, totalDy, (long) (1000 * distanceTimeFactor));
        return true;
    }

    public void onAnimateMove(float dx, float dy, long duration) {
        animateStart = new Matrix(translate);
        animateInterpolator = new OvershootInterpolator();
        startTime = System.currentTimeMillis();
        endTime = startTime + duration;
        totalAnimDx = dx;
        totalAnimDy = dy;
        mView.post(new Runnable() {
            @Override
            public void run() {
                onAnimateStep();
            }
        });
    }

    private void onAnimateStep() {
        long curTime = System.currentTimeMillis();
        float percentTime = (float) (curTime - startTime)
                / (float) (endTime - startTime);
        float percentDistance = animateInterpolator
                .getInterpolation(percentTime);
        //float curDx = percentDistance * totalAnimDx;
        //float curDy = percentDistance * totalAnimDy;
        translate.set(animateStart);
        //onMove(curDx, curDy);

        Log.v("DEBUGGING", "We're " + percentDistance + " of the way there!");
    }
}