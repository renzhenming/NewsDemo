package com.ren.smartcity.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ren.smartcity.R;
import com.ren.smartcity.bean.NewsBean;
import com.ren.smartcity.fragment.tab.NewsCenterContentTabPager;
import com.ren.smartcity.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/26.
 */
public class TabPagerAdapter extends PagerAdapter {
    private final ArrayList<NewsBean.NewsLeftBean.NewsTopBean> children;
    private final List<NewsCenterContentTabPager> mViews;

    public TabPagerAdapter(List<NewsCenterContentTabPager> mViews, ArrayList<NewsBean.NewsLeftBean.NewsTopBean> children) {
        this.children = children;
        this.mViews = mViews;
    }

    @Override
    public int getCount() {
        return children == null? 0 : children.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViews.get(position).view;
        NewsCenterContentTabPager tabPager = mViews.get(position);
        tabPager.loadTabNetData(Constant.HOST+children.get(position).url);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return children.get(position).title;
    }
}
