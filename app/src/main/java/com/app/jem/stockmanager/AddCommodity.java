package com.app.jem.stockmanager;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bean.Commodity;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


/**
 * Created by jem on 2017/4/27.
 */

public class AddCommodity extends Activity {
    EditText addBrand, addKind, addModel, addNumber, addPrice;
    Button confirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_commodity);
        initView();
    }


    private void initView() {
        addBrand = (EditText) findViewById(R.id.grands);
        addKind = (EditText) findViewById(R.id.kinds);
        addModel = (EditText) findViewById(R.id.model);
        addNumber = (EditText) findViewById(R.id.number);
        addPrice = (EditText) findViewById(R.id.price);
        confirm = (Button) findViewById(R.id.comfirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addPrice.getText() == null && addBrand.getText() == null && addKind.getText() == null
                        && addModel.getText() == null && addNumber.getText() == null && addPrice.getText() == null) {
                    Toast.makeText(AddCommodity.this, "请输入完整的商品信息", Toast.LENGTH_SHORT).show();
                } else {
                    setResult(2);
                    updateData();
                }

            }
        });
    }

    private void updateData() {
        Commodity newCommodity = new Commodity();
        newCommodity.setKind(addKind.getText().toString());
        newCommodity.setBrand(addBrand.getText().toString());
        newCommodity.setModel(addModel.getText().toString());
        newCommodity.setNumber(Integer.valueOf(addNumber.getText().toString()));
        newCommodity.setPrice(Integer.valueOf(addPrice.getText().toString()));
        newCommodity.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    finish();
                } else {
                    Toast.makeText(AddCommodity.this, "商品添加错误，请重新添加", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
