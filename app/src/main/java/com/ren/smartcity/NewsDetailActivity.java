package com.ren.smartcity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Apple on 2016/9/30.
 */
public class NewsDetailActivity extends Activity {

    @InjectView(R.id.ib_back)
    ImageButton ibBack;
    @InjectView(R.id.ib_textsize)
    ImageButton ibTextsize;
    @InjectView(R.id.ib_share)
    ImageButton ibShare;
    @InjectView(R.id.webView)
    WebView webView;
    @InjectView(R.id.pb)
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_detail);
        ButterKnife.inject(this);
        String url = getIntent().getStringExtra("url");
        //让WebView支持javaScript
        webView.getSettings().setJavaScriptEnabled(true);
        //设置WebView的客户端
        webView.setWebViewClient(new WebViewClient(){
            //页面加载完成
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb.setVisibility(View.GONE);
            }
        });
        //让WebView去加载网页
        webView.loadUrl(url);
    }

    @OnClick({R.id.ib_back, R.id.ib_textsize, R.id.ib_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_textsize:
                changeWebViewTextSize();
                break;
            case R.id.ib_share:
                showShare();
                break;
        }
    }

    private String[] types = new String[]{
            "超大号字体",
            "大号字体",
            "正常字体",
            "小号字体",
            "超小号字体",
    };
    private WebSettings.TextSize[] textSizes = new WebSettings.TextSize[]{
            WebSettings.TextSize.LARGEST,
            WebSettings.TextSize.LARGER,
            WebSettings.TextSize.NORMAL,
            WebSettings.TextSize.SMALLER,
            WebSettings.TextSize.SMALLEST,
    };

    int selectPosition;
    //修改网页字体大小
    private void changeWebViewTextSize() {
        //单选的对话框   AlertDialog
        //设置字体大小 webView.getSettings().setTextSize(TextS);
        new AlertDialog.Builder(this)
                .setTitle("选择字体的大小")
                .setSingleChoiceItems(types, 2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectPosition = which;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        webView.getSettings().setTextSize(textSizes[selectPosition]);
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("黑马程序员分享的");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是黑马程序员，我骄傲！");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Toast.makeText(NewsDetailActivity.this,platform.getName(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        // 启动分享GUI
        oks.show(this);
    }
    //友盟统计
    public void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }
    //友盟统计
    public void onPause() {
        super.onPause();
       // MobclickAgent.onPause(this);
    }
}
