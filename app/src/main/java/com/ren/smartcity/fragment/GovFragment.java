package com.ren.smartcity.fragment;

import android.view.View;
import android.widget.TextView;

import com.ren.smartcity.BaseFragment;
import com.ren.smartcity.interfacepackage.BaseLoadNetOperator;

public class GovFragment extends BaseFragment implements BaseLoadNetOperator{

    @Override
    public void initTitle() {
        setLeftMenuVisibility(true);
        setRightMenuVisibility(false);
        setTitleText("人口管理");
    }

    @Override
    public View createView() {
        return null;
    }

    /**
     * 加载网络数据
     */
    @Override
    public void onLoadOperator() {

    }
}
