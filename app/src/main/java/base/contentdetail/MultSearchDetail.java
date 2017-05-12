package base.contentdetail;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.jem.stockmanager.R;

import java.util.ArrayList;
import java.util.List;

import base.BaseContentDetailPager;
import bean.Commodity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by jem on 2017/4/27.
 */

public class MultSearchDetail extends BaseContentDetailPager implements View.OnClickListener {

    private Spinner brand, kind;
    private String set_kind, set_brand;
    private TextView resultNumber;
    private ListView resultListView;
    private LinearLayout result;
    private List<String> brand_list = new ArrayList<>();
    private List<String> kind_list = new ArrayList<>();
    private List<String> result_list = new ArrayList<>();


    public MultSearchDetail(AppCompatActivity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        View rootView = View.inflate(mActivity, R.layout.mult_search, null);
        brand = (Spinner) rootView.findViewById(R.id.multsearch_brand);
        kind = (Spinner) rootView.findViewById(R.id.mutisearch_kind);
        Button search = (Button) rootView.findViewById(R.id.muti_search);
        resultNumber = (TextView) rootView.findViewById(R.id.mutisearch_number);
        resultListView = (ListView) rootView.findViewById(R.id.mutisearch_list);
        result = (LinearLayout) rootView.findViewById(R.id.mutisearch_numberlist);
        search.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void initData() {
        if (isNetworkConnected(mActivity))
            initKind();
    }

    private void initKind() {
        BmobQuery<Commodity> bmobKindQuery = new BmobQuery<Commodity>();
        bmobKindQuery.addQueryKeys("kind");
        bmobKindQuery.setLimit(1000);
        boolean isCache = bmobKindQuery.hasCachedResult(Commodity.class);
        if (isCache) {
            bmobKindQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        } else {
            bmobKindQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        bmobKindQuery.findObjects(new FindListener<Commodity>() {
            @Override
            public void done(List<Commodity> object, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < object.size(); i++) {
                        if (!kind_list.contains(object.get(i).getKind())) {
                            kind_list.add(object.get(i).getKind());
                        }
                    }

                    ArrayAdapter<String> kindAdapter = new ArrayAdapter<String>(mActivity,
                            android.R.layout.simple_spinner_item, kind_list);
                    kindAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kind.setAdapter(kindAdapter);
                    kind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            set_kind = kind_list.get(position);
                            initBrand();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            set_kind = kind_list.get(0);
                        }
                    });
                } else {
                    Log.i("bmob", "加载kind失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void initBrand() {
        brand_list.clear();
        brand_list.add("全部品牌");
        BmobQuery<Commodity> b1 = new BmobQuery<Commodity>();
        b1.addWhereEqualTo("kind", set_kind);
        BmobQuery<Commodity> b2 = new BmobQuery<Commodity>();
        b2.addQueryKeys("brand");
        BmobQuery<Commodity> bmobBrandQuery = new BmobQuery<Commodity>();
        List<BmobQuery<Commodity>> andQuerys = new ArrayList<BmobQuery<Commodity>>();
        andQuerys.add(b1);
        andQuerys.add(b2);
        bmobBrandQuery.and(andQuerys);
        b1.setLimit(1000);
        boolean isCache = bmobBrandQuery.hasCachedResult(Commodity.class);
        if (isCache) {
            bmobBrandQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        } else {
            bmobBrandQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        b1.findObjects(new FindListener<Commodity>() {
            @Override
            public void done(List<Commodity> object, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < object.size(); i++) {
                        if (!brand_list.contains(object.get(i).getBrand())) {
                            brand_list.add(object.get(i).getBrand());
                            Log.v("tag", "brandlist = " + object.get(i).getBrand());
                        }


                    }


                    ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(mActivity,
                            android.R.layout.simple_spinner_item, brand_list);
                    brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    brand.setAdapter(brandAdapter);
                    brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            set_brand = brand_list.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            set_brand = brand_list.get(0);
                        }
                    });
                } else {
                    Log.i("bmob", "加载brand失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        BmobQuery<Commodity> query = new BmobQuery<Commodity>();
        result_list.clear();
        if (set_brand.equals("全部品牌")) {
            query.addWhereEqualTo("kind", set_kind);
        } else {
            BmobQuery<Commodity> b1 = new BmobQuery<Commodity>();
            b1.addWhereEqualTo("kind", set_kind);
            BmobQuery<Commodity> b2 = new BmobQuery<Commodity>();
            b2.addWhereEqualTo("brand", set_brand);
            List<BmobQuery<Commodity>> andQuerys = new ArrayList<BmobQuery<Commodity>>();
            andQuerys.add(b1);
            andQuerys.add(b2);
            query.and(andQuerys);
        }
        boolean isCache = query.hasCachedResult(Commodity.class);
        if (isCache) {
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        } else {
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        query.findObjects(new FindListener<Commodity>() {
            @Override
            public void done(List<Commodity> list, BmobException e) {
                if (e == null) {
                    result.setVisibility(View.VISIBLE);
                    int count = 0;

                    for (int i = 0; i < list.size(); i++) {
                        count = count + list.get(i).getNumber();
                        System.out.print("成功 " + i);
                        if (!result_list.contains(list.get(i).getBrand() + "/" + list.get(i).getModel()
                                + "/" + list.get(i).getNumber())) {
                            result_list.add(list.get(i).getBrand() + "/" + list.get(i).getModel() +
                                    "/" + list.get(i).getNumber());
                        }
                    }

                    resultNumber.setText(String.valueOf(count));
                    ArrayAdapter<String> resultAdapter = new ArrayAdapter<>(mActivity,
                            android.R.layout.simple_expandable_list_item_1, result_list);
                    resultListView.setAdapter(resultAdapter);
                } else {
                    Toast.makeText(mActivity, "查找出错", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    //判断网络是否可用
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] info = mConnectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
}
