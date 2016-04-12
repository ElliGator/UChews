package com.example.elli.uchews;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;

/**
 * Created by Elli on 1/3/2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> mFragList = new ArrayList<>();
    private final String mIndividualTab = "";
    private final String mGroupTab = "";

    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public int getCount(){
        return mFragList.size();
    }

    @Override
    public Fragment getItem(int position){
        return mFragList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position){
        if(getItem(position) instanceof IndividualFragment)
            return mIndividualTab;
        else if(getItem(position) instanceof  GroupFragment)
            return mGroupTab;
        else
            throw new Resources.NotFoundException("Cannot find tab title");
    }

    public void addFragment(Fragment f){
        mFragList.add(f);
    }
}
