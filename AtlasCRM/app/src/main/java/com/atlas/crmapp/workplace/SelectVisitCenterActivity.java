package com.atlas.crmapp.workplace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.SelectVisitCenterAdapter;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.eventbus.Event;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.model.CityCenterJson;
import com.atlas.crmapp.model.VisitCentersBean;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SelectVisitCenterActivity extends BaseStatusActivity {
    @BindView(R.id.rv_select_center)
    RecyclerView rvSelectCenter;




    private SelectVisitCenterAdapter selectVisitCenterAdapter ;
    private List<VisitCentersBean> visitCentersBeanList = new ArrayList<>();
    private VisitCentersBean currentVisitCentersBean;
    private static final String KEY_VISIT_CENTER = "KEY_VISIT_CENTER";
    public static final String KEY_CURR_VISIT_CENTER = "KEY_CURR_VISIT_CENTER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_visit_center);
        setTitle(R.string.visit_select_center);

        setTopLeftButton(R.drawable.white_back, new OnClickListener() {
            @Override
            public void onClick() {
                EventBusFactory.getBus().post(new Event.EventObject(Constants.EventType.CLICK_SELECT_CENTER_BACK, currentVisitCentersBean));
                finish();
            }
        });

        setTitle(getString(R.string.visit_select_center));
        Intent intent = getIntent();
        if(intent != null){
            currentVisitCentersBean = (VisitCentersBean) intent.getSerializableExtra(KEY_VISIT_CENTER);
        }
        prepareActivityData();
    }

    public static void newInstance(Activity activity, VisitCentersBean visitCentersBean){
        Intent intent = new Intent(activity, SelectVisitCenterActivity.class);
        intent.putExtra(KEY_VISIT_CENTER, visitCentersBean);
        activity.startActivity(intent);
    }



    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();


        BizDataRequest.requestVisitCityCenterList(this, statusLayout, new BizDataRequest.OnResponseGetCenterList() {
            @Override
            public void onSuccess(List<CityCenterJson> centerListJsons) {
                visitCentersBeanList = getVisitCentersBeanList(centerListJsons);
                updateActivityViews();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    @Override
    protected void updateActivityViews() {
        super.updateActivityViews();

        if(selectVisitCenterAdapter == null){
            rvSelectCenter.setLayoutManager(new LinearLayoutManager(this));
            selectVisitCenterAdapter = new SelectVisitCenterAdapter(visitCentersBeanList);
            selectVisitCenterAdapter.setEmptyView(R.layout.view_product_null, rvSelectCenter);
            rvSelectCenter.setAdapter(selectVisitCenterAdapter);
        }

        selectVisitCenterAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId() == R.id.tv_center_name){

                    if(!visitCentersBeanList.get(position).isSelected()){
                        for(VisitCentersBean visitCentersBean : visitCentersBeanList){
                            visitCentersBean.setSelected(false);
                        }
                        visitCentersBeanList.get(position).setSelected(true);
                        currentVisitCentersBean = visitCentersBeanList.get(position);
                        selectVisitCenterAdapter.notifyDataSetChanged();
                        EventBusFactory.getBus().post(new Event.EventObject(Constants.EventType.CLICK_SELECT_CENTER_BACK, currentVisitCentersBean));
                        finish();
                    }

                }
            }
        });

    }

    private  List<VisitCentersBean> getVisitCentersBeanList(List<CityCenterJson> centerListJsons){
        if(centerListJsons != null  && centerListJsons.size() > 0){
            for (CityCenterJson cityCenterJson : centerListJsons){
                cityCenterJson.getCity();
                List<CityCenterJson.CentersBean> centersBeans = cityCenterJson.getCenters();
                if(centersBeans != null && centersBeans.size() > 0){
                    int size = centersBeans.size();
                    for(int i = 0; i < size ; i++){
                        VisitCentersBean visitCentersBean = new VisitCentersBean();
                        CityCenterJson.CentersBean centersBean = centersBeans.get(i);
                        if(i == 0){
                            visitCentersBean.setCtiyName(cityCenterJson.getCity().getName());
                        }
                        if(i == size -1){
                            visitCentersBean.setShowLine(false);
                        }
                        visitCentersBean.setId(centersBean.getId());
                        visitCentersBean.setName(centersBean.getName());
                        if(currentVisitCentersBean != null){
                            if (currentVisitCentersBean.getId() == visitCentersBean.getId()){
                                visitCentersBean.setSelected(true);
                            }
                        }
                        visitCentersBeanList.add(visitCentersBean);
                    }
                }
            }
        }
        return  visitCentersBeanList;
    }
}
