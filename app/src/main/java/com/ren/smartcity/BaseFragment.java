package com.ren.smartcity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/3/17.
 */
public abstract class BaseFragment extends Fragment {

    @InjectView(R.id.ib_menu)
    ImageButton ibMenu;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ib_pic_type)
    ImageButton ibPicType;
    @InjectView(R.id.container)
    FrameLayout container;
    public MainActivity mMainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_base, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMainActivity = (MainActivity) getActivity();
        initTitle();
    }

    /**
     * 初始化标题
     */
    public abstract void initTitle();

    /**
     * 用于在子fragment中创建布局，创建之后需要调用本类中的addView添加布局到framelayout中
     */
    public abstract View createView();
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.ib_menu, R.id.ib_pic_type})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_menu:
                MainActivity activity = (MainActivity) getActivity();
                activity.mainDrawer.openDrawer(Gravity.LEFT);
                break;
            case R.id.ib_pic_type:
                break;
        }
    }

    /**
     * 向容器中添加布局
     * @param view
     */
    public void addView(View view){
        container.removeAllViews();
        container.addView(view);
    }
    /*********************************************************************
    *在initTitle抽象方法中调用这些方法设置标题
    *************************************************************************/
    /**
     * 设置左菜单显示隐藏
     * @param visibile
     */
    public void setLeftMenuVisibility(boolean visibile){
        ibMenu.setVisibility(visibile?View.VISIBLE:View.GONE);
    }
    /**
     * 设置右菜单显示隐藏
     * @param visibile
     */
    public void setRightMenuVisibility(boolean visibile){
        ibPicType.setVisibility(visibile?View.VISIBLE:View.GONE);
    }
    /**
     * 设置标题
     */
    public void setTitleText(String titleText){
        tvTitle.setText(titleText);
    }
}