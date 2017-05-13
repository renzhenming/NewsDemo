package com.ren.smartcity.fragment.tab;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ren.smartcity.R;
import com.ren.smartcity.adapter.NewsListAdapter;
import com.ren.smartcity.adapter.TabTopNewsAdapter;
import com.ren.smartcity.bean.NewsCenterTabBean;
import com.ren.smartcity.utils.CacheUtils;
import com.ren.smartcity.utils.Constant;
import com.ren.smartcity.utils.MyLogger;
import com.ren.smartcity.views.RZMRefreshRecyclerView;
import com.ren.smartcity.views.RecycleViewDivider;
import com.ren.smartcity.views.SwitchImageViewPager;
import com.ren.smartcity.views.ViewPagerController;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/5/1.
 */
public class NewsCenterContentTabPager {

    private String TAG = this.getClass().getSimpleName();
    private final Context context;
    public final View view;
    private SwitchImageViewPager mViewPager;
    private TextView mTitle;
    private RZMRefreshRecyclerView mRecyclerView;
    private LinearLayout mPointContainer;
    private NewsCenterTabBean tabBean;
    //新闻数据列表
    List<NewsCenterTabBean.DataBean.NewsBean> mList = new ArrayList<>();

    public NewsCenterContentTabPager(Context context) {
        this.context = context;
        view = initView();
    }

    private View initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.newscenter_content_tab,null,false);
        View childHeaderView = LayoutInflater.from(context).inflate(R.layout.child_header_view,null,false);
        ViewPagerController controller = new ViewPagerController(context,new AccelerateDecelerateInterpolator());
        controller.setSrollRate(1000);
        mViewPager = (SwitchImageViewPager) childHeaderView.findViewById(R.id.vp_switch_image);
        controller.bindViewPager(mViewPager);
        mTitle = (TextView) childHeaderView.findViewById(R.id.tv_title);
        mPointContainer = (LinearLayout) childHeaderView.findViewById(R.id.ll_point_container);
        mRecyclerView = (RZMRefreshRecyclerView) view.findViewById(R.id.rv_news);
        mRecyclerView.addChildHeaderView(childHeaderView);
        mRecyclerView.setOnLoadListener(new RZMRefreshRecyclerView.OnLoadListener() {
            @Override
            public void onRefresh() {
                OkHttpUtils.get()
                        .url(Constant.HOST+tabBean.getData().getMore())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                MyLogger.d(TAG,e.getMessage());
                                mRecyclerView.hideRefreshView(true);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                MyLogger.d(TAG,response);
                                Gson gson = new Gson();
                                NewsCenterTabBean moreBean = gson.fromJson(response, NewsCenterTabBean.class);
                                mList.addAll(0,moreBean.getData().getNews());
                                mRecyclerView.hideRefreshView(true);
                            }
                        });
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpUtils.get()
                                .url(Constant.HOST+tabBean.getData().getMore())
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        MyLogger.d(TAG,e.getMessage());
                                        mRecyclerView.hideLoadMoreView();
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        MyLogger.d(TAG,response);
                                        Gson gson = new Gson();
                                        NewsCenterTabBean moreBean = gson.fromJson(response, NewsCenterTabBean.class);
                                        mList.addAll(moreBean.getData().getNews());
                                        mRecyclerView.hideLoadMoreView();
                                    }
                                });
                    }
                },2000);

            }
        });
        return view;
    }

    public void loadTabNetData(final String url){
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyLogger.d(TAG,e.getMessage());
                        mRecyclerView.hideRefreshView(false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        MyLogger.d(TAG,response);
                        parseData(response);
//                        try {
//                            CacheUtils.saveCache(context,url,response);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    }
                });
    }

    private Handler handler = new Handler();
    private boolean isSwitching = false;
    class SwitchTask implements Runnable{

        @Override
        public void run() {
            int currentItem = mViewPager.getCurrentItem();
            if (currentItem == tabBean.getData().getTopnews().size()-1){
                currentItem = 0;
            }else{
                currentItem ++ ;
            }
            mViewPager.setCurrentItem(currentItem,true);
            handler.postDelayed(this,2000);
        }
    }
    public void startSwicthPic(){
        if (!isSwitching){
            handler.postDelayed(new SwitchTask(),2000);
            isSwitching = true;
        }
    }

    public void stopSwitchPic(){
        handler.removeCallbacksAndMessages(null);
        isSwitching = false;
    }

    private void parseData(String response) {
        Gson gson = new Gson();
        tabBean = gson.fromJson(response, NewsCenterTabBean.class);
        //绑定数据
        bindViewData(tabBean);
    }

    private void bindViewData(NewsCenterTabBean tabBean) {
        //轮播图数据
        bindTopImages(tabBean.getData().getTopnews());
        //xinwen数据
        mList.addAll(tabBean.getData().getNews());
        bindRVNews(mList);
    }

    private void bindRVNews(List<NewsCenterTabBean.DataBean.NewsBean> news) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new RecycleViewDivider(context,LinearLayoutManager.HORIZONTAL,2, Color.BLACK));
        NewsListAdapter adapter = new NewsListAdapter(context,news);
        mRecyclerView.setAdapter(adapter);
    }

    private void bindTopImages(final List<NewsCenterTabBean.DataBean.TopnewsBean> topnews) {
        mPointContainer.removeAllViews();
        ArrayList<ImageView> images = new ArrayList<>();
        for (int i = 0 ; i < topnews.size() ; i ++){
            //图片
            ImageView view = new ImageView(context);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            String topimage = topnews.get(i).getTopimage();
            Picasso.with(context).load(Constant.replaceImageUrl(topimage)).placeholder(R.mipmap.ic_launcher).into(view);
            images.add(view);
        }

        for (int i = 0 ; i < topnews.size() ; i ++){
            //指示器
            View pointView = new View(context);
            pointView.setBackgroundResource(R.drawable.point_gray_bg);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(5,5);
            params.rightMargin = 10;
            mPointContainer.addView(pointView,params);
        }
        //设置第一个点为红点
        mPointContainer.getChildAt(0).setBackgroundResource(R.drawable.point_red_bg);
        TabTopNewsAdapter adapter = new TabTopNewsAdapter(topnews,images,context);
        mViewPager.setAdapter(adapter);

        mTitle.setText(topnews.get(0).getTitle());

        startSwicthPic();
        mViewPager.setPager(this);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTitle.setText(topnews.get(position).getTitle());
                for (int i = 0 ; i <mPointContainer.getChildCount();i++ ){
                    if (position == i){
                        mPointContainer.getChildAt(i).setBackgroundResource(R.drawable.point_red_bg);
                    }else{
                        mPointContainer.getChildAt(i).setBackgroundResource(R.drawable.point_gray_bg);
                    }
                }
                stopSwitchPic();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
