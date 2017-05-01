package com.ren.smartcity.fragment;

import android.view.View;
import android.widget.TextView;

import com.ren.smartcity.BaseFragment;

public class SettingFragment extends BaseFragment {

    @Override
    public void initTitle() {
        setLeftMenuVisibility(false);
        setRightMenuVisibility(false);
        setTitleText("设置");
    }

    @Override
    public View createView() {
        return null;
    }

}
