package base.contentdetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.app.jem.stockmanager.Login;
import com.app.jem.stockmanager.R;
import com.app.jem.stockmanager.User;

import java.io.File;

import base.BaseContentDetailPager;
import cn.bmob.v3.BmobUser;

/**
 * Created by jem on 2017/4/27.
 */

public class Setting extends BaseContentDetailPager implements View.OnClickListener {

    RelativeLayout login, clear;

    public Setting(AppCompatActivity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        View rootView = View.inflate(mActivity, R.layout.setting, null);
        login = (RelativeLayout) rootView.findViewById(R.id.log_sign);
        login.setOnClickListener(this);
        clear = (RelativeLayout) rootView.findViewById(R.id.clearCahce);
        clear.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.log_sign:
                BmobUser bmobUser = BmobUser.getCurrentUser();
                if (bmobUser != null) {
                    Intent userActivity = new Intent(mActivity,User.class);
                    mActivity.startActivity(userActivity);
                } else {
                    Intent login = new Intent(mActivity, Login.class);
                    mActivity.startActivity(login);
                }

                break;
            case R.id.clearCahce:
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(mActivity);
                normalDialog.setIcon(R.drawable.clear);
                normalDialog.setTitle("清除数据");
                normalDialog.setMessage("点击确定将不能离线加载数据");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/stockManager/cache");
                                file.delete();
                            }
                        });
                normalDialog.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //...To-do
                            }
                        });
                // 显示
                normalDialog.show();

                break;
        }
    }


}
