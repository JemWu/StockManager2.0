package base;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by jem on 2017/4/27.
 */

public abstract class BaseContentDetailPager {
    public AppCompatActivity mActivity;

    public View mRootView;

    public BaseContentDetailPager(AppCompatActivity mActivity) {
        this.mActivity = mActivity;
         mRootView =initView();
    }

    public abstract View initView();
    public abstract void initData();
}
