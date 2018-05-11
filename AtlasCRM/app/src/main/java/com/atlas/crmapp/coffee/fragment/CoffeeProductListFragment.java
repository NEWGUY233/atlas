package com.atlas.crmapp.coffee.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.CoffeeProductRvAdapter;
import com.atlas.crmapp.coffee.CoffeeListActivity;
import com.atlas.crmapp.db.hepler.ProductCartHelper;
import com.atlas.crmapp.db.model.ProductCart;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.OnlineSaleProductsJson;
import com.atlas.crmapp.model.ProductInfoJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.UiUtil;
import com.atlas.crmapp.widget.RecycleViewListViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/19
 *         Description :
 */

public class CoffeeProductListFragment extends BaseFragment {
    public CoffeeProductRvAdapter adapter;
    @BindView(R.id.vs_null)
    ViewStub vsNull;

    @BindView(R.id.rv_coffee_product)
    RecyclerView mRvCoffeeProduct;

    @BindView(R.id.rl_main)
    RelativeLayout rlMain;

    long categoryId;
    long mUnitId;
    private String type;

    public ArrayList<ProductInfoJson> al_proinfojson = new ArrayList<>();

    public CoffeeProductListFragment() {

    }
    @SuppressLint("ValidFragment")
    public CoffeeProductListFragment(long id,long unitId, String type) {
        categoryId = id;
        mUnitId= unitId;
        this.type = type;
    }

    public static CoffeeProductListFragment newInstance(long id,long unitId, String type) {

        return new CoffeeProductListFragment(id,unitId, type);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_coffee_product_list;

    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {
        adapter = new CoffeeProductRvAdapter(getHoldingActivity(), al_proinfojson, type);
        mRvCoffeeProduct.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getHoldingActivity());
        mRvCoffeeProduct.setLayoutManager(layoutManager);
        mRvCoffeeProduct.addItemDecoration(new RecycleViewListViewDivider(getActivity(), LinearLayout.HORIZONTAL, UiUtil.dipToPx(getActivity(),1), Color.parseColor("#ebebeb")));
        mRvCoffeeProduct.setAdapter(adapter);

        prepareFragmentData();
    }


    @Override
    protected void initFragmentData(Bundle savedInstanceState) {

    }

    int page = 0;
    int pagesize = 100;

    @Override
    protected void prepareFragmentData() {
        super.prepareFragmentData();
        BizDataRequest.requestOnlineSaleProducts(getContext(), page, pagesize, mUnitId, categoryId, statusLayout, new BizDataRequest.OnOnlineSaleProducts() {
            @Override
            public void onSuccess(OnlineSaleProductsJson onlineSaleProductsJson) {
                if(page<=0) {
                    List<ProductInfoJson> proinfo = onlineSaleProductsJson.rows;
                    page++;
                    al_proinfojson.addAll(proinfo);
                    handler.sendEmptyMessageDelayed(10, 500);
                    if(al_proinfojson.size() == 0){
                        mRvCoffeeProduct.setVisibility(View.GONE);
                        //vsNull.inflate();
                        statusLayout.showEmpty();
                    }
                }
            }

            @Override
            public void onError(DcnException error) {
                Message message = new Message();
                message.obj = error.getDescription();
                message.what = 404;
                handler.sendMessage(message);
            }
        });
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareFragmentData();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 10:
                    adapter.notifyDataSetChanged();
                    List<ProductCart> productCartList = ProductCartHelper.getAllProduct(mUnitId);
                    if (productCartList == null) {
                        return;
                    }
                    for (int i=0; i<al_proinfojson.size(); i++) {
                        for (int j=0; j<productCartList.size(); j++) {
                            if (al_proinfojson.get(i).id == productCartList.get(j).getProductId() && al_proinfojson.get(i).unitId == productCartList.get(j).getUnitId()) {
                                al_proinfojson.get(i).number = productCartList.get(j).getNum();
                                adapter.notifyDataSetChanged();
                                ((CoffeeListActivity)getHoldingActivity()).proinfojson.add(al_proinfojson.get(i));
                            }
                        }
                    }
                    ((CoffeeListActivity)getHoldingActivity()).updateBottom();
                    break;
                case 404:
                    //Toast.makeText(getContext(), msg.obj.toString(), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

}
