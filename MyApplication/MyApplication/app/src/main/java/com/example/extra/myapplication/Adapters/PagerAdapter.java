package com.example.extra.myapplication.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.extra.myapplication.Fragments.ExploreFragment;
import com.example.extra.myapplication.Fragments.HomeFragment;
import com.example.extra.myapplication.Fragments.RecomFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;
    public PagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0:
                RecomFragment f0 = new RecomFragment();
                return  f0;
            case 1:
                HomeFragment f1 = new HomeFragment();
                return  f1;
            case 2:
                ExploreFragment f2 = new ExploreFragment();
                return f2;
            default:
                return  null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
