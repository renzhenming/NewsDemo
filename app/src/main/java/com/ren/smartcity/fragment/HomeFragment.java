package com.ren.smartcity.fragment;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ren.smartcity.BaseFragment;
import com.ren.smartcity.interfacepackage.BaseLoadNetOperator;
import com.ren.smartcity.utils.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class HomeFragment extends BaseFragment implements BaseLoadNetOperator{

    @Override
    public void initTitle() {
        setLeftMenuVisibility(false);
        setRightMenuVisibility(false);
        setTitleText("首页");
    }

    @Override
    public View createView() {
        return null;
    }

    /**
     * 加载网络数据
     */
    @Override
    public void onLoadOperator() {
        String url = Constant.NEWSCENTER_URL;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                System.out.println(e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                System.out.println(response);
            }
        });
    }
}
