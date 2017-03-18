package com.ren.smartcity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ren.smartcity.interfacepackage.BaseLoadNetOperator;


/**
 * Created by Administrator on 2017/3/17.
 */
public abstract class BaseFragment extends Fragment implements BaseLoadNetOperator{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView view = new TextView(getContext());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setContent();
    }

    public abstract void setContent();
}
