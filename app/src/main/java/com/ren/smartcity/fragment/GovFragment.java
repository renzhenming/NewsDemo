package com.ren.smartcity.fragment;

import android.widget.TextView;

import com.ren.smartcity.BaseFragment;

public class GovFragment extends BaseFragment {
    @Override
    public void setContent() {
        TextView view = (TextView)getView();
        view.setText("Gov");
    }

    @Override
    public void onLoadOperator() {

    }
}
