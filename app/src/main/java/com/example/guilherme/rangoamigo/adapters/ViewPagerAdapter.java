package com.example.guilherme.rangoamigo.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guilherme on 10/02/18.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragamentList = new ArrayList<Fragment>();
    private final List<String> mFragamentTitleList = new ArrayList<String>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragamentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragamentList.size();
    }

    public void addFragment(Fragment fragment, String title){
        mFragamentList.add(fragment);
        mFragamentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragamentTitleList.get(position);
    }
}
