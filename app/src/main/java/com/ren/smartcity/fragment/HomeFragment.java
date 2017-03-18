package com.ren.smartcity.fragment;

import android.widget.TextView;

import com.ren.smartcity.BaseFragment;

public class HomeFragment extends BaseFragment {
    @Override
    public void setContent() {
        TextView view = (TextView)getView();
        view.setText("Home");
    }

    @Override
    public void onLoadOperator() {

    }
}
