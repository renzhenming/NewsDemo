package com.ren.smartcity.utils;

import android.content.Context;

/**
 * Created by Apple on 2016/10/7.
 * 像素转换的工具类
 */
public class Dp2PxUtils {

    //dp转换成px
    public static int dp2px(Context context,int dp){
        return (int)(dp * context.getResources().getDisplayMetrics().density + 0.5);
    }
}
