package com.ren.smartcity.fragment;

import android.widget.TextView;

import com.ren.smartcity.BaseFragment;

public class SmartFragment extends BaseFragment {
    @Override
    public void setContent() {
        TextView view = (TextView)getView();
        view.setText("Smart");
    }

    @Override
    public void onLoadOperator() {

    }
}
