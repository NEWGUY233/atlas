package com.atlas.crmapp.workplace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.AddedValueServiceAdapter;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.eventbus.Event;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.model.AddedValueJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.widget.RecycleViewListViewDivider;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AddedValueServiceActivity extends BaseStatusActivity {
    @BindView(R.id.rv_added_value)
    RecyclerView rvAddedValue;

    private AddedValueServiceAdapter serviceAdapter ;
    private List<AddedValueJson> addedValueJsonList = new ArrayList<>();
    private List<AddedValueJson> preAddedValueJsonList = new ArrayList<>();
    private static String KEY_ID = "KEY_ID";
    private static String KEY_PEROID = "KEY_PEROID";
    private static String KEY_ADDEDS = "KEY_ADDEDS";
    private String id;
    private String peroid;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_value_service);
        setTitle(getString(R.string.added_value));
        Intent intent = getIntent();
        if(intent != null){
            peroid = intent.getStringExtra(KEY_PEROID);
            id = intent.getStringExtra(KEY_ID);
            preAddedValueJsonList = (List<AddedValueJson>) intent.getSerializableExtra(KEY_ADDEDS);
        }
        prepareActivityData();
    }


    public static void newInstance(Activity activity, String id, String peroid , List<AddedValueJson> addedValueJsonList){
        Intent intent = new Intent(activity, AddedValueServiceActivity.class);
        intent.putExtra(KEY_ID, id);
        intent.putExtra(KEY_PEROID, peroid);
        intent.putExtra(KEY_ADDEDS, (Serializable) addedValueJsonList);
        activity.startActivity(intent);
    }


    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();

        BizDataRequest.requestAddedValue(this, id, peroid, statusLayout, new BizDataRequest.OnResponseAddedValue() {
            @Override
            public void onSuccess(List<AddedValueJson> addedValueJsons) {
                AddedValueServiceActivity.this.addedValueJsonList.clear();
                if(preAddedValueJsonList != null){
                    for(AddedValueJson preAddedValueJson: preAddedValueJsonList){
                        for(AddedValueJson addedValueJson : addedValueJsons){
                            if(addedValueJson.getId() == preAddedValueJson.getId() && preAddedValueJson.isSelect()){
                                addedValueJson.setSelect(true);
                            }
                        }
                    }
                }
                AddedValueServiceActivity.this.addedValueJsonList.addAll(addedValueJsons);
                updateActivityViews();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareActivityData();
    }

    @Override
    protected void updateActivityViews() {
        super.updateActivityViews();

        if(serviceAdapter== null){
            rvAddedValue.setLayoutManager(new LinearLayoutManager(this));
            rvAddedValue.addItemDecoration(new RecycleViewListViewDivider(this, LinearLayout.HORIZONTAL, R.drawable.bg_divider_margin_left));
            serviceAdapter = new AddedValueServiceAdapter(this, addedValueJsonList);
            rvAddedValue.setAdapter(serviceAdapter);
            serviceAdapter.setEmptyView(R.layout.view_product_null, rvAddedValue);
            serviceAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    AddedValueJson addedValueJson = addedValueJsonList.get(position);
                    addedValueJson.setSelect(!addedValueJson.isSelect());
                    serviceAdapter.notifyDataSetChanged();
                    EventBusFactory.getBus().post(new Event.EventObject(Constants.EventType.ADDED_VALUE_BACK, addedValueJsonList));
                }
            });
        }
        serviceAdapter.notifyDataSetChanged();
    }


}
