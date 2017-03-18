package com.ren.smartcity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/15.
 */
public class TabAdapter extends FragmentPagerAdapter {
    private final FragmentManager fm;
    private final ArrayList<Fragment> mList;

    public TabAdapter(ArrayList<Fragment> mList, FragmentManager fm) {
        super(fm);
        this.mList = mList;
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
