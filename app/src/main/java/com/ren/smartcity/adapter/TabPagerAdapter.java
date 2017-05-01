package com.ren.smartcity.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ren.smartcity.R;
import com.ren.smartcity.bean.NewsBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/26.
 */
public class TabPagerAdapter extends PagerAdapter {
    private final ArrayList<NewsBean.NewsLeftBean.NewsTopBean> children;
    private final Context context;

    public TabPagerAdapter(Context context, ArrayList<NewsBean.NewsLeftBean.NewsTopBean> children) {
        this.context = context;
        this.children = children;
    }

    @Override
    public int getCount() {
        return children == null? 0 : children.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_top_tab,container,false);
        TextView textView = (TextView) view.findViewById(R.id.item_tab);
        String title = children.get(position).title;
        textView.setText(title);
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
