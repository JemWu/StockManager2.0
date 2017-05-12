package fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.jem.stockmanager.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import adapter.MyRecyclerAdapter;
import bean.Commodity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import utils.DataOperation;

/**
 * Created by jem on 2017/5/2.
 */

public class Commodity_list_detail_three extends Fragment {

    private RecyclerView rv;
    private Fragment frag_four;
    private List<String> contentList = new ArrayList<>();
    private List<String> idList = new ArrayList<>();
    private String tab_kind, tab_brand;
    private FragmentTransaction ft;

    public Commodity_list_detail_three(String tab_kind, String tab_brand) {
        this.tab_brand = tab_brand;
        this.tab_kind = tab_kind;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.commodity_list, null);
        rv = (RecyclerView) view.findViewById(R.id.commodity_recyclerview);
        initData();
        return view;
    }

    private void initData() {
        contentList.clear();
            if (isNetworkConnected(getActivity())){
                BmobQuery<Commodity> b1 = new BmobQuery<>();
                b1.addWhereEqualTo("kind", tab_kind);
                BmobQuery<Commodity> b2 = new BmobQuery<>();
                b1.addWhereEqualTo("brand", tab_brand);
                BmobQuery<Commodity> b3 = new BmobQuery<>();
                b2.addQueryKeys("model");
                BmobQuery<Commodity> bmobBrandQuery = new BmobQuery<Commodity>();
                List<BmobQuery<Commodity>> andQuerys = new ArrayList<BmobQuery<Commodity>>();
                andQuerys.add(b1);
                andQuerys.add(b2);
                andQuerys.add(b3);
                bmobBrandQuery.and(andQuerys);
                boolean isCache = bmobBrandQuery.hasCachedResult(Commodity.class);
                if(isCache){
                    bmobBrandQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
                    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
                }else{
                    bmobBrandQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
                    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
                }
                bmobBrandQuery.findObjects(new FindListener<Commodity>() {
                    @Override
                    public void done(final List<Commodity> list, BmobException e) {
                        if (e == null) {
                            for (int i = 0; i < list.size(); i++) {
                                if (!contentList.contains(list.get(i).getModel())) {
                                    contentList.add(list.get(i).getModel());
                                    idList.add(list.get(i).getObjectId());
                                }
                            }
                            MyRecyclerAdapter mAdapter = new MyRecyclerAdapter(getActivity(),contentList);
                            rv.setLayoutManager(new GridLayoutManager(getActivity(),2));
                            mAdapter.setOnItemClickListener(new MyRecyclerAdapter.onItemClickListener() {
                                @Override
                                public void onItemClick(View v, int pos) {
                                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                                    frag_four=new Commodity_item_detail(idList.get(pos));
                                    ft.replace(R.id.main_fl,frag_four);
                                    ft.addToBackStack(null);
                                    ft.commit();
                                }


                            });
                            rv.setAdapter(mAdapter);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    new DataOperation().saveStorage2SDCard(contentList, tab_kind+
                                            "_"+tab_brand+"_model");
                                }
                            }).start();
                        } else {
                            Log.i("bmob", "获取商品列表失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }
                });
            }else{
                //从本地获取数据
                File f = new File(Environment.getExternalStorageDirectory().getPath() +
                        "/stockManager/cache/" + tab_kind+"_"+tab_brand+"_model");
                if (f.exists()) {
                    contentList = new DataOperation().getStorageEntities(tab_kind+"_"+tab_brand+"_model");
                    MyRecyclerAdapter mAdapter = new MyRecyclerAdapter(getActivity(),contentList);
                    rv.setLayoutManager(new GridLayoutManager(getActivity(),2));
                    mAdapter.setOnItemClickListener(new MyRecyclerAdapter.onItemClickListener() {
                        @Override
                        public void onItemClick(View v, int pos) {
                            ft = getActivity().getSupportFragmentManager().beginTransaction();
                            frag_four=new Commodity_item_detail(idList.get(pos));
                            ft.replace(R.id.main_fl,frag_four);
                            ft.addToBackStack(null);
                            ft.commit();
                        }


                    });
                    rv.setAdapter(mAdapter);
                }
                Toast.makeText(getActivity(), "网络未连接，请重新连接网络", Toast.LENGTH_SHORT).show();
            }


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
