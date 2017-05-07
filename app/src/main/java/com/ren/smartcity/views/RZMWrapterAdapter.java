package com.ren.smartcity.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by rzm on 2017/5/7.
 * 包装类，实现头布局脚布局和正常布局的区分
 */
public class RZMWrapterAdapter extends RecyclerView.Adapter {

    /**
     * 头布局
     */
    private View mHeaderView;
    /**
     * 脚布局
     */
    private View mFooterView;
    /**
     * 正常布局适配器
     */
    private RecyclerView.Adapter mAdapter;
    /**
     * 头布局类型
     */
    private int ITEM_TYPE_HEADER = 2;
    /**
     * 脚布局类型
     */
    private int ITEM_TYPE_FOOTER = 1;
    /**
     * 正常布局类型
     */
    private int ITEM_TYPE_NORMAL = 0;

    public RZMWrapterAdapter(View mHeaderVie, View mFooterView, RecyclerView.Adapter mAdapter) {
        this.mHeaderView = mHeaderVie;
        this.mFooterView = mFooterView;
        this.mAdapter = mAdapter;
    }

    /**
     * 条目类型判断
     */
    @Override
    public int getItemViewType(int position) {
        //头
        if (position == 0){
            return ITEM_TYPE_HEADER;
        }
        //正常布局
        int normalPosition = position - 1;
        int itemCount = mAdapter.getItemCount();
        if (normalPosition < itemCount){
            return ITEM_TYPE_NORMAL;//(==mAdapter.getItemViewType(..))
        }
        //脚
        return ITEM_TYPE_FOOTER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //头
        if (viewType == ITEM_TYPE_HEADER){
            return new HeaderViewHolder(mHeaderView);
        }
        //尾
        if (viewType == ITEM_TYPE_FOOTER){
            return new FooterViewHolder(mFooterView);
        }
        //正常
        return mAdapter.onCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //减去头布局得到的position是正常position
        int normalPosition = position - 1;
        int itemCount = mAdapter.getItemCount();
        //position = 0 为头布局，normalPosition > itemCount为尾，不需要绑定数据
        if (position == 0 || normalPosition >= itemCount)
            return;
        mAdapter.onBindViewHolder(holder,normalPosition);
    }

    @Override
    public int getItemCount() {
        //加上一个头一个尾
        return mAdapter.getItemCount()+2;
    }

    /**
     * 头布局holder
     */
    class HeaderViewHolder extends RecyclerView.ViewHolder{

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
    /**
     * 脚布局holder
     */
    class FooterViewHolder extends RecyclerView.ViewHolder{

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
