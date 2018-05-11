package com.atlas.crmapp.usercenter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.MyScoreLogDetailAdapter;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.ScoreDetailJson;
import com.atlas.crmapp.model.ScoreGetDetailJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.GetCommonObjectUtils;
import com.atlas.crmapp.view.RefreshFootView;
import com.liaoinstan.springview.widget.SpringView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/1.
 */

public class MyScoreLogDetailActivty extends BaseStatusActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.springview)
    SpringView springView;
    private RefreshFootView refreshFootView;

    MyScoreLogDetailAdapter adapter;
    int page = 0;
    @Override
    protected boolean translucentStatusBar() {
        return true;
    }

    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);
        ButterKnife.bind(this);

        initView();
        getData();
    }

    private void initView() {
        initToolbar();
        setTitle(getString(R.string.user_center_score_jl));

        adapter = new MyScoreLogDetailAdapter(this);

        LinearLayoutManager ll = new LinearLayoutManager(this);
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(ll);
        recyclerView.setAdapter(adapter);

        refreshFootView = new RefreshFootView(this);
        springView.setFooter(refreshFootView);
        springView.setType(SpringView.Type.FOLLOW);

        page = 0;

        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadmore() {
                getData();
                GetCommonObjectUtils.onFinishFreshAndLoad(springView);
            }
        });

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }


    private void getData() {
        BizDataRequest.requestMyScoreGetDetail(this, false, null, page, new BizDataRequest.OnResponseScoreGetListJson() {
            @Override
            public void onSuccess(ScoreGetDetailJson obj) {
                if (page == 0)
                    adapter.setList(obj.getRows());
                else
                    adapter.addLit(obj.getRows());
                page++;
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }
}
