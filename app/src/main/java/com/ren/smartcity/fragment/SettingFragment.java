package com.ren.smartcity.fragment;

import android.widget.TextView;

import com.ren.smartcity.BaseFragment;

public class SettingFragment extends BaseFragment {
    @Override
    public void setContent() {
        TextView view = (TextView)getView();
        view.setText("Setting");
    }

    @Override
    public void onLoadOperator() {

    }
}
