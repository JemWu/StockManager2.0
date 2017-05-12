package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.jem.stockmanager.R;

import bean.Commodity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by jem on 2017/5/2.
 */

public class Commodity_item_detail extends Fragment implements View.OnClickListener {
    private String commodityId;
    private EditText textKind, textBrand, textModel, textPrice,textNumber;

    private int number;


    public Commodity_item_detail(String commodityId) {
        this.commodityId = commodityId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.commodity_detail, null);
        textKind = (EditText) view.findViewById(R.id.detail_kind);
        textBrand = (EditText) view.findViewById(R.id.detail_brand);
        textModel = (EditText) view.findViewById(R.id.detail_model);
        textPrice = (EditText) view.findViewById(R.id.detail_price);
        textNumber = (EditText) view.findViewById(R.id.detail_number);
        textPrice.setOnClickListener(this);
        textModel.setOnClickListener(this);
        textBrand.setOnClickListener(this);
        textKind.setOnClickListener(this);
        textNumber.setOnClickListener(this);
        initView();
        return view;
    }

    private void initView() {
        BmobQuery<Commodity> detail = new BmobQuery<>();
        detail.getObject(commodityId, new QueryListener<Commodity>() {
            @Override
            public void done(Commodity commodity, BmobException e) {
                if (e == null) {
                    textKind.setText(commodity.getKind());
                    textBrand.setText(commodity.getBrand());
                    textModel.setText(commodity.getModel());
                    textPrice.setText(commodity.getPrice().toString());
                    textNumber.setText(commodity.getNumber().toString());
                    number = commodity.getNumber();
                } else {
                    Log.i("bmob", "获取商品列表失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Commodity updataContent = new Commodity();
        switch (v.getId()) {
            case R.id.detail_kind:
                updataContent.setKind(textKind.getText().toString());
                break;
            case R.id.detail_brand:
                updataContent.setBrand(textBrand.getText().toString());
                break;
            case R.id.detail_model:
                updataContent.setModel(textModel.getText().toString());
                break;
            case R.id.detail_price:
                updataContent.setPrice(Integer.valueOf(textPrice.getText().toString()));
                break;
            case R.id.detail_number:
                updataContent.setNumber(Integer.valueOf(textNumber.getText().toString()));
                break;

        }

        updataContent.update(commodityId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(getActivity(),"商品修改成功",Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(),"商品修改失败，请重新修改",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


}
