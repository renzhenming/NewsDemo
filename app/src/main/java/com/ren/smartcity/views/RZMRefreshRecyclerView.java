package com.ren.smartcity.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
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
    /**
     * 刷新时间
     */
    private TextView mTime;
    /**
     * 额外添加的头布局
     */
    private View mChildHeadView;
    /**
     * 是否正在加载更多数据
     */
    private boolean isLoadingMore = false;

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
        mTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
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
        if (mHeaderView != null && view != null){
            mChildHeadView = view;
            mHeaderView.addView(view);
        }
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

    /**
     * 隐藏头布局
     * @param loadState 加载成功与否，成功刷新布局，失败不刷新
     * 隐藏进度条，显示箭头，修改文字，修改状态，loadState决定是否更改刷新时间
     * 总之：恢复现场到默认状态
     */
    public void hideRefreshView(boolean loadState){
        mProgress.setVisibility(View.GONE);
        mArrow.setVisibility(View.VISIBLE);
        mState.setText("下拉刷新");
        mHeaderRefreshState = REFRESH_DOWN_STATE;
        if (loadState){
            String date = DateFormat.getDateFormat(getContext()).format(System.currentTimeMillis());
            String time = DateFormat.getTimeFormat(getContext()).format(System.currentTimeMillis());
            mTime.setText(date+" "+time);
        }
        getAdapter().notifyDataSetChanged();
        refreshLayout.setPadding(0,-mHeaderViewHeight,0,0);
    }

    /**
     * 隐藏加载更多布局
     */
    public void hideLoadMoreView(){
        //修改加载状态
        isLoadingMore = false;
        //隐藏加载布局
        mFooterView.setPadding(0,-mFooterViewHeight,0,0);
        //刷新数据
        getAdapter().notifyDataSetChanged();
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

                /**
                 * 这里存在一个bug,当可见条目为第一个，并且滑动距离dy大于0大于dx的时候
                 * 由于在刷新布局下还添加了额外的头布局，这就导致当添加的头布局显示一部分并未全部显示的时候
                 * 也就是这个头布局的最顶端并没有显示出来的时候，由于满足下面的判断条件，就会执行下面的
                 * refreshLayout.setPadding(0, top, 0, 0);方法，所以就会有一种现象，轻轻拉动一下松手后会有
                 * 缩回的现象，这里就要判断一下，如果顶部并没有显示的时候不进行处理
                 */
                //refreshview在window中的位置
                int[] rvLocation = new int[2];
                getLocationInWindow(rvLocation);
                //额外添加的头布局在window中的位置
                int[] childViewLocation = new int[2];
                mChildHeadView.getLocationInWindow(childViewLocation);
                //对比RecyclerView和ChildHeadView在window中的位置,判断第一个条目是否到达顶端
                System.out.println("头布局："+childViewLocation[1]+",recycler:"+rvLocation[1]);
                if (childViewLocation[1]<rvLocation[1]){
                    return super.dispatchTouchEvent(ev);
                }
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
                            //开始刷新加载数据
                            if (listener != null){
                                listener.onRefresh();
                            }
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

    /**
     * 根据滑动状态显示脚布局
     * isLoadingMore为判断是否正在加载中的标记，如果正在加载中，那么不再重新加载
     * @param state
     */
    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (linearLayoutManager != null){
            //静止状态并且当前条目位置为adapter适配器中最后一个
            boolean idleState = state == RecyclerView.SCROLL_STATE_IDLE;
            int position = linearLayoutManager.findLastVisibleItemPosition();
            //最后一个条目
            boolean isLast = position == getAdapter().getItemCount()-1;
            if (idleState && isLast&&listener != null&& !isLoadingMore){
                //修改加载状态
                isLoadingMore = true;
                //显示加载布局
                mFooterView.setPadding(0,0,0,0);
                //让脚布局滑动出来
                smoothScrollToPosition(position);
                listener.onLoadMore();
            }
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        //包装成RAMWraperAdapter
        adapter = new RZMWrapterAdapter(mHeaderView,mFooterView,adapter);
        super.setAdapter(adapter);
    }

    /**
     * 回调接口
     */
    public OnLoadListener listener;

    public void setOnLoadListener(OnLoadListener listener) {
        this.listener = listener;
    }

    public interface OnLoadListener {
        void onRefresh();
        void onLoadMore();
    }
}
