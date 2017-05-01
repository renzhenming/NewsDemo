package com.ren.smartcity.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ren.smartcity.BaseFragment;
import com.ren.smartcity.MainActivity;
import com.ren.smartcity.R;
import com.ren.smartcity.bean.NewsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/26.
 */
public class LeftMenuAdapter extends RecyclerView.Adapter {

    private List<NewsBean.NewsLeftBean> list;
    private final Context context;


    public LeftMenuAdapter(Context context , List<NewsBean.NewsLeftBean> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_left_menu,parent,false);
        return new LeftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        LeftViewHolder leftViewHolder = (LeftViewHolder) holder;
        leftViewHolder.setData(list.get(position),position);
        if (positionClicked == position){
            leftViewHolder.title.setEnabled(true);
        }else{
            leftViewHolder.title.setEnabled(false);
        }

    }

    @Override
    public int getItemCount() {
        return list != null? list.size():0;
    }

    public void setData(ArrayList<NewsBean.NewsLeftBean> newsBeens) {
        list = newsBeens;
        notifyDataSetChanged();
    }
    private int positionClicked = 0;
    class LeftViewHolder extends RecyclerView.ViewHolder{
        private final TextView title;
        private final View view;

        public LeftViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            title = (TextView) itemView.findViewById(R.id.menu_title);

        }

        public void setData(final NewsBean.NewsLeftBean newsLeftBean, final int position) {
            title.setText(newsLeftBean.title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"点击了",Toast.LENGTH_SHORT).show();
                    if (positionClicked != position){
                        positionClicked = position;
                        notifyDataSetChanged();
                        //点击修改主页内容
                        MainActivity activity = (MainActivity) context;
                        BaseFragment currentFragment = activity.getCurrentFragment();
                        currentFragment.setTitleText(newsLeftBean.title);
                        //关闭侧滑菜单
                        activity.closeLeftMenu();
                    }
                }
            });
        }
    }
}
