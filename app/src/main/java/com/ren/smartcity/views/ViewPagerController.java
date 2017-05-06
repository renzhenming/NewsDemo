package com.ren.smartcity.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/5/6.
 * 控制ViewPager的切换速度
 */
public class ViewPagerController extends Scroller {

    private int rate = 300;

    public ViewPagerController(Context context) {
        super(context);
    }

    public ViewPagerController(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public ViewPagerController(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy,rate);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, rate);
    }

    /**
     * 设置切换速率，时间越长，切换越慢
     * @param rate
     */
    public void setSrollRate(int rate){
        this.rate = rate;
    }

    public void bindViewPager(ViewPager viewPager){
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(viewPager,this);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
