package com.app.jem.stockmanager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;

import base.BaseContentDetailPager;
import base.contentdetail.CommodityDetail;
import base.contentdetail.MultSearchDetail;
import base.contentdetail.Setting;
import cn.bmob.v3.Bmob;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private long exitTime = 0;
    public boolean isConnected;
    Toolbar toolbar;
    FrameLayout fl;
    ArrayList<BaseContentDetailPager> contentList = new ArrayList<BaseContentDetailPager>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bmob初始化
        Bmob.initialize(this, "5a1f7bd3b425c060663a5b341ab5316c");
        isConnected = isNetworkConnected(this);
        initView();
        initBaseView();
        initContentView();
    }

    private void initView() {
        fl = (FrameLayout) findViewById(R.id.fl_content);

    }

    private void initContentView() {
        contentList.add(new CommodityDetail(MainActivity.this));
        contentList.add(new MultSearchDetail(MainActivity.this));
        contentList.add(new Setting(MainActivity.this));
        setCurrentContentView(0);
    }

    private void initBaseView() {
        //侧拉菜单
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("商品总列表");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    protected void setCurrentContentView(int position) {
        BaseContentDetailPager pager = contentList.get(position);
        fl.removeAllViews();
        fl.addView(pager.mRootView);
        pager.initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        NavigationMenuItemView menuItemView = (NavigationMenuItemView) findViewById(R.id.nav_multi);
        if (requestCode == 1 && resultCode == 2) {
            setCurrentContentView(0);
            Snackbar.make(null, "新商品已添加成功", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_multi) {
            item.setChecked(true);
            setCurrentContentView(0);
            getSupportActionBar().setTitle("商品总列表");
        } else if (id == R.id.nav_search1) {
            item.setChecked(true);
            Intent add = new Intent(MainActivity.this, AddCommodity.class);
            startActivityForResult(add, 1);
            getSupportActionBar().setTitle("商品快捷查询");
        } else if (id == R.id.nav_search2) {
            item.setChecked(true);
            setCurrentContentView(1);
            getSupportActionBar().setTitle("商品总数查询");
        } else if (id == R.id.nav_setting) {
            setCurrentContentView(2);
            item.setChecked(true);
            getSupportActionBar().setTitle("设置");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count <= 1) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                    System.exit(0);

                }
            } else {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStackImmediate(null, 0);
                } else {
                    finish();
                }
            }

        }
    }

    //判断网络是否可用
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
