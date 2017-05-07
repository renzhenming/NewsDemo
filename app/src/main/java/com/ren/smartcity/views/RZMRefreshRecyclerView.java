package com.ren.smartcity.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ren.smartcity.R;

/**
 * Created by rzm on 2017/5/7.
 * 目前只支持LinearLayout布局管理器
 */
public class RZMRefreshRecyclerView extends RecyclerView {
    /**
     * 头布局
     */
    private ViewGroup mHeaderView;
    /**
     * 脚布局
     */
    private ViewGroup mFooterView;
    /**
     * 底部刷新的布局高度
     */
    private int mFooterViewHeight;
    /**
     * 顶部刷新的布局高度
     */
    private int mHeaderViewHeight;
    /**
     * 垂直布局管理器
     */
    private LinearLayoutManager linearLayoutManager;
    /**
     * 顶部刷新的布局
     */
    private LinearLayout refreshLayout;
    /**
     * 头布局刷新状态
     */
    private int mHeaderRefreshState =REFRESH_DOWN_STATE;
    /**
     * 头布局下拉状态
     */
    private static final int REFRESH_DOWN_STATE = 1;
    /**
     * 头布局将要释放状态
     */
    private static final int REFRESH_UP_STATE = 2;
    /**
     * 头布局正在刷新状态
     */
    private static final int REFRESH_LOADING_STATE = 3;
    /**
     * 状态文字显示
     */
    private TextView mState;
    /**
     * 下拉动画
     */
    private Animation pullAnimation;
    /**
     * 释放动画
     */
    private Animation releaseAnimation;
    /**
     * 头布局箭头
     */
    private ImageView mArrow;
    /**
     * 进度条
     */
    private ProgressBar mProgress;

    public RZMRefreshRecyclerView(Context context) {
        this(context,null);
    }

    public RZMRefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public RZMRefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        initHeaderView();
        initFooterView();
        initAnimation();
    }

    private void initAnimation() {
        pullAnimation = createPullAnimation();
        releaseAnimation = createReleaseAnimation();
    }

    /**
     * 下拉时刷新布局控件动画
     * @return
     */
    private Animation createPullAnimation() {
        Animation animation = new RotateAnimation(0,-180,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(400);
        animation.setFillAfter(true);
        return animation;
    }

    /**
     * 释放时刷新布局控件动画
     * @return
     */
    private Animation createReleaseAnimation() {
        Animation animation = new RotateAnimation(-180,-360,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(400);
        animation.setFillAfter(true);
        return animation;
    }

    /**
     * 初始化头布局
     * header_view 内部嵌套了两个LinearLayout，目的就是方便继续往里添加view
     * mHeaderView.getChildAt(0)获取到的就是刷新的布局，其余的为添加的头布局
     * 并且这里规定，刷新布局必须为LinearLayout
     */
    private void initHeaderView() {
        mHeaderView = (ViewGroup) inflate(getContext(), R.layout.header_view, null);
        mState = (TextView) mHeaderView.findViewById(R.id.tv_state);
        mArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
        mProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb);
        mProgress.setVisibility(View.GONE);
        View view = mHeaderView.getChildAt(0);
        if(view instanceof LinearLayout){
            //测量并隐藏头布局
            refreshLayout = (LinearLayout)view;
            refreshLayout.measure(0,0);
            mHeaderViewHeight = refreshLayout.getMeasuredHeight();
            refreshLayout.setPadding(0,-mHeaderViewHeight,0,0);
        }else{
            throw new IllegalArgumentException("headerView的第一个child view必须为LinearLayout");
        }
    }

    /**
     * 添加布局到头布局中，作为头布局的子布局存在，自始自终recyclerview都只有一个headview
     * @param view
     */
    public void addChildHeaderView(View view){
        if (mHeaderView != null && view != null)
            mHeaderView.addView(view);
    }

    /**
     * 初始化尾布局并默认隐藏
     */
    private void initFooterView() {
        mFooterView = (ViewGroup) inflate(getContext(), R.layout.footer_view, null);
        mFooterView.measure(0,0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        mFooterView.setPadding(0,-mFooterViewHeight,0,0);
    }

    @Override
    public void setLayoutManager(LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager)
            linearLayoutManager = (LinearLayoutManager) layoutManager;
        super.setLayoutManager(layoutManager);
    }

    /**
     * 处理拖拽事件，不使用touchEvent方法是因为dispatchTouchEvent回调频率高一些
     * @param ev
     * @return
     */
    int downX = 0;
    int downY = 0;
    int distanceX =0;
    int distanceY = 0;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();

                distanceX = moveX - downX;
                distanceY = moveY - downY;

                int top = -mHeaderViewHeight + distanceY;
                int firstVisibleItemPosition;
                //可见条目为第一个，滑动距离大于0
                if (linearLayoutManager != null){
                    firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    if (distanceY > distanceX && distanceY > 0 && firstVisibleItemPosition == 0){
                        if (refreshLayout != null) {
                            //切换刷新布局中控件状态
                            if (mHeaderRefreshState == REFRESH_DOWN_STATE && top >= 0){
                                mHeaderRefreshState = REFRESH_UP_STATE;
                                mState.setText("释放更新");
                                mArrow.startAnimation(pullAnimation);
                            }else if (mHeaderRefreshState == REFRESH_UP_STATE && top < 0){
                                mHeaderRefreshState = REFRESH_DOWN_STATE;
                                mState.setText("下拉刷新");
                                mArrow.startAnimation(releaseAnimation);
                            }
                            refreshLayout.setPadding(0, top, 0, 0);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //释放时刷新布局的处理
                if (linearLayoutManager != null) {
                    int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();
                    //释放时也要判断当前条目位置和top值
                    if (firstVisiblePosition == 0 && distanceY > 0){
                        if (mHeaderRefreshState == REFRESH_DOWN_STATE){
                            //这个状态下直接隐藏刷新布局
                            refreshLayout.setPadding(0,-mHeaderViewHeight,0,0);
                        }else if (mHeaderRefreshState == REFRESH_UP_STATE){
                            //状态切换为正在加载
                            mHeaderRefreshState = REFRESH_LOADING_STATE;
                            mState.setText("正在加载。。");
                            //刷新布局缩回本身高度
                            refreshLayout.setPadding(0,0,0,0);
                            //隐藏箭头，显示进度条(清除箭头动画才能隐藏箭头)
                            mArrow.clearAnimation();
                            mArrow.setVisibility(View.GONE);
                            mProgress.setVisibility(View.VISIBLE);
                            //开始加载数据
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_OUTSIDE:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        //包装成RAMWraperAdapter
        adapter = new RZMWrapterAdapter(mHeaderView,mFooterView,adapter);
        super.setAdapter(adapter);
    }
}
