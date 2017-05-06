package com.ren.smartcity.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.ren.smartcity.BaseFragment;
import com.ren.smartcity.MainActivity;
import com.ren.smartcity.R;
import com.ren.smartcity.adapter.TabPagerAdapter;
import com.ren.smartcity.bean.NewsBean;
import com.ren.smartcity.fragment.tab.NewsCenterContentTabPager;
import com.ren.smartcity.interfacepackage.BaseLoadNetOperator;
import com.ren.smartcity.utils.Constant;
import com.ren.smartcity.utils.MyLogger;
import com.ren.smartcity.utils.MyToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

public class NewsFragment extends BaseFragment implements BaseLoadNetOperator {

    private static final String TAG = "NewsFragment";
    TabLayout tabLayout;
    ImageButton ibNext;
    ViewPager vpNewscenterContent;
    private NewsBean newsBean;
    private List<NewsCenterContentTabPager> mList;

    @Override
    public void initTitle() {
        setLeftMenuVisibility(true);
        setRightMenuVisibility(false);
        setTitleText("新闻");
    }

    @Override
    public View createView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_news,(ViewGroup)getView(),false);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        ibNext = (ImageButton) view.findViewById(R.id.ib_next);
        vpNewscenterContent = (ViewPager) view.findViewById(R.id.vp_newscenter_content);
        //设置数据
        mList = new ArrayList<>();
        for (int i = 0 ; i < newsBean.data.get(0).children.size();i++){
            NewsCenterContentTabPager tabPager = new NewsCenterContentTabPager(mMainActivity);
            mList.add(tabPager);
        }
        TabPagerAdapter viewPagerAdapter = new TabPagerAdapter(mList,newsBean.data.get(0).children);
        vpNewscenterContent.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(vpNewscenterContent);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        return view;
    }

    /**
     * 加载网络数据
     */
    @Override
    public void onLoadOperator() {
        String url = Constant.NEWSCENTER_URL;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                MyToast.show(mMainActivity,e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                MyLogger.d(TAG,response);
                parseData(response);
            }
        });
    }

    private void parseData(String response) {
        Gson gson = new Gson();
        newsBean = gson.fromJson(response, NewsBean.class);
        MainActivity activity = (MainActivity) getActivity();
        activity.setNewsBeens(newsBean.data);
        //创建布局
        View view = createView();
        //向容器中添加布局
        addView(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
