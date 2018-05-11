package com.atlas.crmapp.usercenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.MyScoreDetailAdapter;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.commonactivity.WebActivity;
import com.atlas.crmapp.model.MyScoreJson;
import com.atlas.crmapp.model.ScoreDetailJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.ActionUriUtils;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.GetCommonObjectUtils;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.view.RefreshFootView;
import com.liaoinstan.springview.widget.SpringView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Leo on 2018/1/31.
 */

public class MyScoreActivity extends BaseStatusActivity {

    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.springview)
    SpringView springView;
    private RefreshFootView refreshFootView;


    MyScoreDetailAdapter adapter;
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
        setContentView(R.layout.activity_my_score);
        ButterKnife.bind(this);

        initView();
        initListener();
        if (!getGlobalParams().isLogin()){
            showAskLoginDialog(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            return;
        }
        getData();
    }

    private void initView(){
        initToolbar();
        setTitle(getString(R.string.user_center_score));
        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));

        adapter = new MyScoreDetailAdapter(this);

        LinearLayoutManager ll = new LinearLayoutManager(this);
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(ll);
        recyclerView.setAdapter(adapter);

        refreshFootView = new RefreshFootView(this);
        springView.setFooter(refreshFootView);
        springView.setType(SpringView.Type.FOLLOW);

    }

    private void initListener(){
        setTopRightButton(getString(R.string.user_center_score_gz), clickRight);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadmore() {
                getList();
                GetCommonObjectUtils.onFinishFreshAndLoad(springView);
            }
        });
    }

    OnClickListener clickRight = new OnClickListener() {
        @Override
        public void onClick() {
            Intent i = new Intent(MyScoreActivity.this, WebActivity.class);
            i.putExtra(ActionUriUtils.url, GlobalParams.getInstance().requestUrl.APPSERVER + "app/html/rules.html");
            i.putExtra(ActionUriUtils.content, getString(R.string.t86));
            startActivity(i);

        }
    };

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        if (!getGlobalParams().isLogin()){
            showAskLoginDialog(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            return;
        }
        startActivity(new Intent(MyScoreActivity.this, MyScoreGetActivity.class));
    }

    private void getData() {

        BizDataRequest.requestMyScore(this, false, null, new BizDataRequest.OnResponseMyScoreJson() {
            @Override
            public void onSuccess(MyScoreJson responseMyScoreJson) {
                if (responseMyScoreJson != null) {
                    String price = FormatCouponInfo.formatDoublePrice(responseMyScoreJson.getPoints(), 0);
                    if (StringUtils.isNotEmpty(price)) {
                        if (tvScore != null)
                            tvScore.setText(price);
                    }
                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });
        page = 0;
        getList();
    }

    private void getList(){
        BizDataRequest.requestMyScoreDetail(this, false, null, page, new BizDataRequest.OnResponseScoreDetailListJson() {
            @Override
            public void onSuccess(ScoreDetailJson json) {
                if (page == 0)
                    adapter.setList(json.getRows());
                else
                    adapter.addLit(json.getRows());
                page++;

            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getData();
    }
}
