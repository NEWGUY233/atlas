package com.atlas.crmapp.usercenter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.RechargeAdapter;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.TransactionsJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.GetCommonObjectUtils;
import com.atlas.crmapp.view.RefreshFootView;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RechargeRecordActivity extends BaseStatusActivity {



    @BindView(R.id.rv_recharge_record)
    RecyclerView rvRechargeRecord;

    private RechargeAdapter adapter;
    private List<TransactionsJson> transactionsJsons = new ArrayList<>();

    @BindView(R.id.springview)
    SpringView springView;

    private long id;
    private boolean showProgreeDialog;
    private RefreshFootView refreshFootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_record);
        umengPageTitle = getString(R.string.recharge_detail);
        setTitle(umengPageTitle);
    }

    @Override
    protected void initActivityViews() {
        super.initActivityViews();
        if(adapter == null){

            adapter = new RechargeAdapter(this,transactionsJsons);
        }
        rvRechargeRecord.setLayoutManager(new LinearLayoutManager(this));
        rvRechargeRecord.setAdapter(adapter);
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(onFreshListener);
        refreshFootView = new RefreshFootView(this);
        springView.setFooter(refreshFootView);
        prepareActivityData();
    }

    private SpringView.OnFreshListener onFreshListener = new SpringView.OnFreshListener() {
        @Override
        public void onRefresh() {
        }

        @Override
        public void onLoadmore() {
            showProgreeDialog = true;
            prepareActivityData();
            GetCommonObjectUtils.onFinishFreshAndLoad(springView);
        }
    };



    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
        BizDataRequest.requestTransactions(this, id, true, statusLayout, new BizDataRequest.OnResponseTransactionsJson() {
            @Override
            public void onSuccess(List<TransactionsJson> transactionsJson) {
                if(transactionsJson != null && transactionsJson.size() > 0){
                    transactionsJsons.addAll(transactionsJson);
                }
                if(transactionsJsons.size()>0){
                    id = transactionsJsons.get(transactionsJsons.size()-1).getId();
                }else{
                    springView.setEnable(false);
                    adapter.setEmptyView(R.layout.view_product_null,rvRechargeRecord);
                }
                if(transactionsJson == null || transactionsJson.size() == 0){
                    refreshFootView.setLoadingFinish();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        showProgreeDialog = false;
        prepareActivityData();
    }
}
