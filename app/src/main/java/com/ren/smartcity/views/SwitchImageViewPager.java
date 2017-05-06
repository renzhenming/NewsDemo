package com.ren.smartcity.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.ren.smartcity.fragment.tab.NewsCenterContentTabPager;

/**
 * Created by Administrator on 2017/5/6.
 */
public class SwitchImageViewPager extends ViewPager {
    public SwitchImageViewPager(Context context) {
        super(context);
    }

    public SwitchImageViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private NewsCenterContentTabPager pager;

    public void setPager(NewsCenterContentTabPager pager){
        this.pager = pager;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (pager != null){
            switch (ev.getAction()){
                case MotionEvent.ACTION_DOWN:
                    pager.stopSwitchPic();
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    pager.startSwicthPic();
                    break;

            }
        }

        return super.dispatchTouchEvent(ev);
    }
}
