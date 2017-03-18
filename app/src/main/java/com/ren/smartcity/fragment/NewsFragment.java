package com.ren.smartcity.fragment;

import android.widget.TextView;

import com.ren.smartcity.BaseFragment;

public class NewsFragment extends BaseFragment {
    @Override
    public void setContent() {
        TextView view = (TextView)getView();
        view.setText("News");
    }

    @Override
    public void onLoadOperator() {

    }
}
