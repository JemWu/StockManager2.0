package com.app.jem.stockmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by jem on 2017/4/27.
 */

public class Signup extends Activity {
    Button signup, smsbt;
    EditText signup_name, signup_psw, signup_sms;
    Integer sms_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        initView();
    }

    private void initView() {
        signup = (Button) findViewById(R.id.signup);
        smsbt = (Button) findViewById(R.id.sms);
        signup_name = (EditText) findViewById(R.id.signup_name);
        signup_psw = (EditText) findViewById(R.id.signup_psw);
        signup_sms = (EditText) findViewById(R.id.signup_sms);
        smsbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobSMS.requestSMSCode(signup_name.getText().toString(), "模板名称", new QueryListener<Integer>() {

                    @Override
                    public void done(Integer smsId, BmobException ex) {
                        if (ex == null) {
                            smsbt.setText("验证码已发送");
                            sms_id = smsId;
                        }
                    }
                });
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sms_id.equals(Integer.valueOf(signup_sms.getText().toString())) ) {
                    BmobUser bu = new BmobUser();
                    bu.setUsername(signup_name.getText().toString());
                    bu.setPassword(signup_psw.getText().toString());
                    bu.signUp(new SaveListener<BmobUser>() {
                        @Override
                        public void done(BmobUser s, BmobException e) {
                            if (e == null) {
                                Intent nameAndPsw = new Intent();
                                nameAndPsw.putExtra("name", signup_name.getText().toString());
                                nameAndPsw.putExtra("psw", signup_psw.getText().toString());
                                Signup.this.setResult(2, nameAndPsw);
                                Signup.this.finish();
                            } else {
                                Toast.makeText(Signup.this, "注册失败：" + e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }

        });
    }

}
