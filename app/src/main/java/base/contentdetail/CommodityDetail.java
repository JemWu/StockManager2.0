package base.contentdetail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.app.jem.stockmanager.R;

import base.BaseContentDetailPager;
import fragment.Commodity_list_detail_one;

/**
 * Created by jem on 2017/4/27.
 */

public class CommodityDetail extends BaseContentDetailPager {

    private Fragment fragment_one;
    private FragmentTransaction ft;

    public CommodityDetail(AppCompatActivity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        View rootView = View.inflate(mActivity, R.layout.commodity_content, null);

        return rootView;
    }

    @Override
    public void initData() {
       ft = mActivity.getSupportFragmentManager().beginTransaction();
        if (fragment_one==null){
            fragment_one = new Commodity_list_detail_one();
        }
        ft.replace(R.id.main_fl,fragment_one);
        ft.addToBackStack(null);
        ft.commit();

    }
}
