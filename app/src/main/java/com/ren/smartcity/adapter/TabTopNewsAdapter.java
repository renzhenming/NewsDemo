package com.ren.smartcity.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ren.smartcity.bean.NewsCenterTabBean;
import com.ren.smartcity.fragment.tab.NewsCenterContentTabPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/6.
 */
public class TabTopNewsAdapter extends PagerAdapter {

    private final List<NewsCenterTabBean.DataBean.TopnewsBean> topnews;
    private final Context context;
    private final ArrayList<ImageView> images;

    public TabTopNewsAdapter(List<NewsCenterTabBean.DataBean.TopnewsBean> topnews, ArrayList<ImageView> images, Context context) {
        this.topnews = topnews;
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return topnews == null ? 0 : topnews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = images.get(position);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
