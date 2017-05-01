package com.ren.smartcity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ren.smartcity.adapter.GuideVPAdapter;
import com.ren.smartcity.utils.Constant;
import com.ren.smartcity.utils.Dp2PxUtils;
import com.ren.smartcity.utils.MyLogger;
import com.ren.smartcity.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Apple on 2016/9/23.
 */
public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener {

    private static final String TAG = "GuideActivity";
    @InjectView(R.id.vp)
    ViewPager vp;
    @InjectView(R.id.bt_start)
    Button btStart;
    @InjectView(R.id.container_gray_point)
    LinearLayout containerGrayPoint;
    @InjectView(R.id.red_point)
    View redPoint;
    //向导图片
    private int[] imgs = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
    private List<ImageView> views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        ButterKnife.inject(this);

        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initViewPager();
        initGrayPoint();
    }

    //动态的创建灰色的点
    private void initGrayPoint() {
        for (int resId : imgs) {
            View view = new View(this);
            view.setBackgroundResource(R.drawable.point_gray_bg);
            //原因：布局中红色点的单位：10dp   java代码中的10：是px
            //dp转换成px
            int width = Dp2PxUtils.dp2px(this, 10);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
            params.rightMargin = 20;//设置右边距
            containerGrayPoint.addView(view, params);
        }
    }

    //初始化ViewPager的数据
    private void initViewPager() {
        views = new ArrayList<>();
        for (int resId : imgs) {
            ImageView iv = new ImageView(this);
            iv.setBackgroundResource(resId);
            views.add(iv);
        }

        vp.setAdapter(new GuideVPAdapter(views));
        //设置页面的滑动监听
        vp.addOnPageChangeListener(this);
    }

    @OnClick(R.id.bt_start)
    public void onClick() {
        //进入主界面
        SPUtils.saveBoolean(this, Constant.KEY_HAS_GUIDE, true);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //小灰点之间的宽度
    private int width;

    //position:当前滑动页面的下标    positionOffset：页面的滑动比率    positionOffsetPixels：页面滑动的实际像素
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        MyLogger.i(TAG, "position:" + position + ",positionOffset:" + positionOffset + ",positionOffsetPixels:" + positionOffsetPixels);

        if (width == 0) {
            width = containerGrayPoint.getChildAt(1).getLeft() - containerGrayPoint.getChildAt(0).getLeft();
            MyLogger.i(TAG, "width:" + width);
        }

        //修改小红点与相对布局的左边距
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) redPoint.getLayoutParams();
        params.leftMargin = (int) (position * width + width * positionOffset);
        redPoint.setLayoutParams(params);
    }

    //页面选中的时候调用
    @Override
    public void onPageSelected(int position) {
        if (position == imgs.length - 1) {
            //显示按钮
            btStart.setVisibility(View.VISIBLE);
        } else {
            //隐藏按钮
            btStart.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //友盟统计
    public void onResume() {
        super.onResume();
    }

    //友盟统计
    public void onPause() {
        super.onPause();
    }

}
