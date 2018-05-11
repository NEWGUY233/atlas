package com.atlas.crmapp.fitness;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.FitnessPersonalCoachAdapter;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.CoachJson;
import com.atlas.crmapp.model.CoachsListJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.UiUtil;
import com.atlas.crmapp.widget.RecycleViewListViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PersonalCoachActivity extends BaseStatusActivity {

    @BindView(R.id.rv_fitness_coach)
    RecyclerView mRvView;

    List<CoachJson> coachs = new ArrayList<CoachJson>();
    FitnessPersonalCoachAdapter adapter = new FitnessPersonalCoachAdapter(PersonalCoachActivity.this, coachs);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_coach);
    }

    @Override
    protected void initActivityViews() {
        super.initActivityViews();
        umengPageTitle = getString(R.string.private_introducer);
        setTitle(umengPageTitle);
        mRvView.setLayoutManager(new LinearLayoutManager(this));
        mRvView.addItemDecoration(new RecycleViewListViewDivider(this, LinearLayout.HORIZONTAL, UiUtil.dipToPx(this,1 ), Color.parseColor("#ebebeb")));
        mRvView.setAdapter(adapter);
        prepareActivityData();
    }

    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
        BizDataRequest.requestGetCoachs(this, 0, 100, getGlobalParams().getFitnessId(), false, statusLayout, new BizDataRequest.OnCoachsRequestResult() {
            @Override
            public void onSuccess(CoachsListJson coachsListJson) {
                coachs.addAll(coachsListJson.rows);
                adapter.notifyDataSetChanged();
                if(coachs.size() == 0){
                    adapter.setEmptyView(R.layout.view_product_null, mRvView);
                }
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
}
