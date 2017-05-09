package com.ren.smartcity.utils;

/**
 * Created by Apple on 2016/9/23.
 * 常量类
 */
public class Constant {

    //是否已经执行完向导
    public final static String KEY_HAS_GUIDE = "key_has_guide";
    public final static String KEY_HAS_READ = "key_has_read";

    //服务器的主机
    public final static String HOST = "http://192.168.1.103:8080/zhbj";

    //新闻中心页面的数据地址
    public final static String NEWSCENTER_URL = HOST + "/categories.json";

    public static String replaceImageUrl(String url){
        String result = "";
        if (url != null && url.contains("http://10.0.2.2:8080/zhbj")){
            result = url.replace("http://10.0.2.2:8080/zhbj", HOST);
        }
        return result;
    }
}
