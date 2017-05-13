package com.ren.smartcity.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ren.smartcity.NewsDetailActivity;
import com.ren.smartcity.R;
import com.ren.smartcity.bean.NewsCenterTabBean;
import com.ren.smartcity.db.DbDao;
import com.ren.smartcity.db.DbHelper;
import com.ren.smartcity.utils.Constant;
import com.ren.smartcity.utils.SPUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Apple on 2016/9/28.
 */
public class NewsListAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<NewsCenterTabBean.DataBean.NewsBean> newsBeanList;
    private final DbDao dao;

    public NewsListAdapter(Context context, List<NewsCenterTabBean.DataBean.NewsBean> newsBeanList) {
        this.context = context;
        this.newsBeanList = newsBeanList;
        dao = new DbDao(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //绑定数据
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final NewsCenterTabBean.DataBean.NewsBean newsBean = newsBeanList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;
        Picasso.with(context).load(Constant.replaceImageUrl(newsBean.getListimage())).into(viewHolder.ivIcon);
        viewHolder.tvTitle.setText(newsBean.getTitle());
        viewHolder.tvTime.setText(newsBean.getPubdate());
        final ArrayList<String> query = dao.query();
        if (query.contains(newsBean.getId()+"")){
            viewHolder.tvTitle.setTextColor(Color.GRAY);
        }else{
            viewHolder.tvTitle.setTextColor(Color.BLACK);
        }
        //条目点击事件
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到新闻详情界面
                Intent intent = new Intent(context,NewsDetailActivity.class);
                intent.putExtra("url",Constant.replaceImageUrl(newsBean.getUrl()));
                context.startActivity(intent);
                if (!query.contains(newsBean.getId())){
                    dao.insert(newsBean.getId()+"");
                    viewHolder.tvTitle.setTextColor(Color.GRAY);
                }

//                //存储该条新闻的唯一标识：
//                String id = newsBean.getId()+"";
//                //存储在哪里？Sp   File   DB（）
//                String readNews = SPUtils.getString(context, Constant.KEY_HAS_READ,"");
//                if(!readNews.contains(id)){
//                    String value = readNews+ "," + id;
//                    //存储
//                    SPUtils.saveString(context,Constant.KEY_HAS_READ,value);
//                    //刷新界面
//                    //notifyDataSetChanged();
//                    viewHolder.tvTitle.setTextColor(Color.GRAY);
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsBeanList != null ? newsBeanList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.iv_icon)
        ImageView ivIcon;
        @InjectView(R.id.tv_title)
        TextView tvTitle;
        @InjectView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
