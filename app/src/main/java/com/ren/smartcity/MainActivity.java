package com.ren.smartcity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ren.smartcity.adapter.TabAdapter;
import com.ren.smartcity.fragment.GovFragment;
import com.ren.smartcity.fragment.HomeFragment;
import com.ren.smartcity.fragment.NewsFragment;
import com.ren.smartcity.fragment.SettingFragment;
import com.ren.smartcity.fragment.SmartFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private static final String FRAGMENT_HOME = "fragment_home";
    private static final String FRAGMENT_NEWS = "fragment_news";
    private static final String FRAGMENT_SMART = "fragment_smart";
    private static final String FRAGMENT_GOV = "fragment_gov";
    private static final String FRAGMENT_SETTING = "fragment_setting";
    @InjectView(R.id.main_frame)
    FrameLayout frameLayout;
    @InjectView(R.id.rb_home)
    RadioButton rbHome;
    @InjectView(R.id.rb_newscener)
    RadioButton rbNewscener;
    @InjectView(R.id.rb_smartservice)
    RadioButton rbSmartservice;
    @InjectView(R.id.rb_govaffairs)
    RadioButton rbGovaffairs;
    @InjectView(R.id.rb_setting)
    RadioButton rbSetting;
    @InjectView(R.id.rg_tab)
    RadioGroup rgTab;
    @InjectView(R.id.main_left)
    RecyclerView mainLeft;
    @InjectView(R.id.main_drawer)
    DrawerLayout mainDrawer;
    @InjectView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @InjectView(R.id.main_viewpager)
    ViewPager mainViewpager;
    private ArrayList<Fragment> mList = new ArrayList<>();

    private HomeFragment homeFragment;
    private NewsFragment newsFragment;
    private SmartFragment smartFragment;
    private GovFragment govFragment;
    private SettingFragment settingFragment;
    private FragmentManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initVp();
        manager = getSupportFragmentManager();
        //addFragment(R.id.rb_home);
    }

    private void initVp() {
        setSupportActionBar(mainToolbar);

        ActionBar supportActionBar = getSupportActionBar();
        //设置是否添加显示NavigationIcon
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        //设置标题
        mainToolbar.setTitle("智慧城市");
        //设置颜色
        mainToolbar.setTitleTextColor(Color.WHITE);
        //添加fragment
        mList.add(new HomeFragment());
        mList.add(new NewsFragment());
        mList.add(new SmartFragment());
        mList.add(new GovFragment());
        mList.add(new SettingFragment());

        TabAdapter adapter = new TabAdapter(mList,getSupportFragmentManager());
        mainViewpager.setAdapter(adapter);
        //侧滑设置
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mainDrawer, mainToolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Toast.makeText(getApplicationContext(), "open", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Toast.makeText(getApplicationContext(), "close", Toast.LENGTH_SHORT).show();
            }
        };
        mainDrawer.addDrawerListener(toggle);
        rgTab.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //addFragment(checkedId);
        int item = -1;
        switch (checkedId) {
            case R.id.rb_home:
                item = 0;
                break;
            case R.id.rb_newscener:
                item = 1;
                break;
            case R.id.rb_smartservice:
                item = 2;
                break;
            case R.id.rb_govaffairs:
                item = 3;
                break;
            case R.id.rb_setting:
                item = 4;
                break;
        }
        mainViewpager.setCurrentItem(item,false);
    }

    private void addFragment(int checkedId) {
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        homeFragment = (HomeFragment) manager.findFragmentByTag(FRAGMENT_HOME);
        newsFragment = (NewsFragment) manager.findFragmentByTag(FRAGMENT_NEWS);
        smartFragment = (SmartFragment) manager.findFragmentByTag(FRAGMENT_SMART);
        govFragment = (GovFragment) manager.findFragmentByTag(FRAGMENT_GOV);
        settingFragment = (SettingFragment) manager.findFragmentByTag(FRAGMENT_SETTING);
        if (homeFragment != null)
            fragmentTransaction.hide(homeFragment);
        if (newsFragment != null) {
            fragmentTransaction.hide(newsFragment);
        }
        if (smartFragment != null) {
            fragmentTransaction.hide(smartFragment);
        }
        if (govFragment != null) {
            fragmentTransaction.hide(govFragment);
        }
        if (settingFragment != null) {
            fragmentTransaction.hide(settingFragment);
        }

        switch (checkedId) {
            case R.id.rb_home:

                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.main_frame, homeFragment, FRAGMENT_HOME);
                    fragmentTransaction.addToBackStack(FRAGMENT_HOME);
                } else {
                    fragmentTransaction.show(homeFragment);
                }
                break;
            case R.id.rb_newscener:
                if (newsFragment == null) {
                    newsFragment = new NewsFragment();
                    fragmentTransaction.add(R.id.main_frame, newsFragment, FRAGMENT_NEWS);
                    fragmentTransaction.addToBackStack(FRAGMENT_NEWS);
                } else {
                    fragmentTransaction.show(newsFragment);
                }
                break;
            case R.id.rb_smartservice:
                if (smartFragment == null) {
                    smartFragment = new SmartFragment();
                    fragmentTransaction.add(R.id.main_frame, smartFragment, FRAGMENT_SMART);
                    fragmentTransaction.addToBackStack(FRAGMENT_SMART);
                } else {
                    fragmentTransaction.show(smartFragment);
                }
                break;
            case R.id.rb_govaffairs:
                if (govFragment == null) {
                    govFragment = new GovFragment();
                    fragmentTransaction.add(R.id.main_frame, govFragment, FRAGMENT_GOV);
                    fragmentTransaction.addToBackStack(FRAGMENT_GOV);
                } else {
                    fragmentTransaction.show(govFragment);
                }
                break;
            case R.id.rb_setting:
                if (settingFragment == null) {
                    settingFragment = new SettingFragment();
                    fragmentTransaction.add(R.id.main_frame, settingFragment, FRAGMENT_SETTING);
                    fragmentTransaction.addToBackStack(FRAGMENT_SETTING);
                } else {
                    fragmentTransaction.show(settingFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 获取当前回退栈中的Fragment个数
            int backStackEntryCount = manager.getBackStackEntryCount();
            // 判断当前回退栈中的fragment个数
            if (backStackEntryCount > 1) {
                // 立即回退一步
                manager.popBackStackImmediate();
                // 获取当前退到了哪一个Fragment上,重新获取当前的Fragment回退栈中的个数
                FragmentManager.BackStackEntry backStackEntryAt = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1);
                // 获取当前栈顶的Fragment的标记值
                String tag = backStackEntryAt.getName();
                if (FRAGMENT_HOME.equals(tag)) {
                    rbHome.setChecked(true);
                } else if (FRAGMENT_NEWS.equals(tag)) {
                    rbNewscener.setChecked(true);
                } else if (FRAGMENT_SMART.equals(tag)) {
                    rbSmartservice.setChecked(true);
                } else if (FRAGMENT_GOV.equals(tag)) {
                    rbGovaffairs.setChecked(true);
                } else if (FRAGMENT_SETTING.equals(tag)) {
                    rbSetting.setChecked(true);
                }

            } else {
                //回退栈中只剩一个时,退出应用
                finish();
            }
        }
        return true;
    }
}
