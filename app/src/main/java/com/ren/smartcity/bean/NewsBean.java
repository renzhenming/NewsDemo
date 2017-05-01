package com.ren.smartcity.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/25.
 */
public class NewsBean {
    public int code;
    public ArrayList<NewsLeftBean> data;
    public ArrayList<Integer> extend;

    public class NewsLeftBean{
        public int id;
        public int type;
        public String title;
        public ArrayList<NewsTopBean> children;

        public class NewsTopBean{
            public int id;
            public String title;
            public int type;
            public String url;
        }
    }
}
