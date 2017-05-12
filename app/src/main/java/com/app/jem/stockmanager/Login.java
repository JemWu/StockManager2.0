package com.app.jem.stockmanager;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jem on 2017/4/26.
 */

public class Login extends Activity implements View.OnClickListener {
    Button login,signup,forget;
    EditText login_name,login_psw;
    private String name,psw;
    LinearLayout login_layout;
    CircleImageView user_image;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //bmob初始化
        Bmob.initialize(this, "5a1f7bd3b425c060663a5b341ab5316c");
        initView();


    }

    private void initView() {
        login= (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.login_signup);
        forget = (Button) findViewById(R.id.forget_psw);
        login_name = (EditText) findViewById(R.id.login_name);
        login_psw = (EditText) findViewById(R.id.login_psw);
        login_layout= (LinearLayout) findViewById(R.id.login_layout);
        user_image = (CircleImageView) findViewById(R.id.user_image);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);
        forget.setOnClickListener(this);
        ObjectAnimator translation = ObjectAnimator.ofFloat(user_image,"translationX",-500f,0f);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(user_image,"rotation",180f,360f);
        AnimatorSet set_1 = new AnimatorSet();
        set_1.setDuration(1000);
        set_1.setInterpolator(new LinearInterpolator());
        set_1.playTogether(translation,rotation);
        set_1.start();

        BmobUser bmobUser = BmobUser.getCurrentUser();
        if(bmobUser != null){
            Intent start = new Intent(this,MainActivity.class);
            startActivity(start);
        }else{
            login_layout.setVisibility(View.VISIBLE);
            ObjectAnimator alpha = ObjectAnimator.ofFloat(login_layout,"alpha",0f,1f).setDuration(1300);
            alpha.start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_signup:
                Intent signup = new Intent(this,Signup.class);
                startActivityForResult(signup,1);
                break;

            case R.id.login:
                BmobUser bu2 = new BmobUser();
                bu2.setUsername(login_name.getText().toString());
                bu2.setPassword(login_psw.getText().toString());
                bu2.login(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if(e==null){
                            Intent start = new Intent(Login.this,MainActivity.class);
                            startActivity(start);
                        }else{
                            Toast.makeText(Login.this,"登录失败,"+e,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.forget_psw:
                break;
            case R.id.log_sign:
                Intent sign_up = new Intent(Login.this, Signup.class);
                startActivityForResult(sign_up,1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==2){
            login_name.setText(data.getStringExtra("name"));
            login_psw.setText(data.getStringExtra("psw"));
        }
    }
}
