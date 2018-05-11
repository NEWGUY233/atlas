package com.atlas.crmapp.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.MyScoreGetAdapter;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.MyScoreJson;
import com.atlas.crmapp.model.ScoreGetJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/1.
 */

public class MyScoreGetActivity extends BaseStatusActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    MyScoreGetAdapter adapter;

    String score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);
        ButterKnife.bind(this);

        initView();
        initListener();
        getData();
    }

    private void initView(){
        initToolbar();
        setTitle(getString(R.string.user_center_score_dh));
        setTopRightButton(getString(R.string.user_center_score_jl), clickRight);

        adapter = new MyScoreGetAdapter(this);

        LinearLayoutManager ll = new LinearLayoutManager(this);
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(ll);
        recyclerView.setAdapter(adapter);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void initListener(){
        adapter.setClick(new MyScoreGetAdapter.OnTransformationClick() {
            @Override
            public void onTransformClick(int id, int score, int position) {
                getTransform(id);
            }
        });
    }


    OnClickListener clickRight = new OnClickListener() {
        @Override
        public void onClick() {
            startActivity(new Intent(MyScoreGetActivity.this,MyScoreLogDetailActivty.class));
        }
    };

    @Override
    protected boolean translucentStatusBar() {
        return true;
    }

    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }

    private void getData(){
        BizDataRequest.requestMyScore(this, false, statusLayout, new BizDataRequest.OnResponseMyScoreJson() {
            @Override
            public void onSuccess(MyScoreJson responseMyScoreJson) {
                if (responseMyScoreJson != null) {
                    String price = FormatCouponInfo.formatDoublePrice(responseMyScoreJson.getPoints(), 0);
                    if (StringUtils.isNotEmpty(price)) {
                        score = price;
                        adapter.setScore(price);
                    }
                    getTrans();
                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });

    }

    private void getTrans(){
        BizDataRequest.requestMyScore(this, false, statusLayout, new BizDataRequest.OnResponseScoreListJson() {
            @Override
            public void onSuccess(List<ScoreGetJson> obj) {
                adapter.setList(obj);
//                if (obj == null || obj.size() == 0)
//                    statusLayout.showEmpty();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    private void getTransform(int id){
        BizDataRequest.requestMyScoreTrans(this, false, null,id, new BizDataRequest.OnResponseScoreTranJson() {
            @Override
            public void onSuccess() {
                getData();
                showToast(getString(R.string.t87));
            }

            @Override
            public void onError(DcnException error) {
                showToast(error.getDescription());
            }
        });
    }
}
