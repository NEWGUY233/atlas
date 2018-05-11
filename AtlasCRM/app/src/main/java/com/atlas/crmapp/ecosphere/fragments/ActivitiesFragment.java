package com.atlas.crmapp.ecosphere.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atlas.crmapp.MainActivity;
import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.ActAdapter;
import com.atlas.crmapp.adapter.navadapter.MainFragmentNavAdapter;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.ActivityJson;
import com.atlas.crmapp.model.VisibleActivitiesJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.GetCommonObjectUtils;
import com.atlas.crmapp.view.RefreshFootView;
import com.atlas.crmapp.view.RefreshHeaderView;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/20
 *         Description :
 */

public class ActivitiesFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "bizcode";
    private static final String ARG_PARAM2 = "displayTitleBar";
    private String mBizcode;
    private boolean displayTitleBar;//判断是否为生态圈， 显示标题  true时候不显示。
    private boolean isShowDialog ;

    private ArrayList<ActivityJson> activities = new ArrayList<ActivityJson>();
    private ActAdapter adapter;
    private long lastRecId = 0;
    private RefreshFootView refreshFootView;

    @BindView(R.id.rv_act)
    RecyclerView mRvAct;

    @BindView(R.id.springview)
    SpringView springView;

    @BindView(R.id.include_title)
    ViewGroup titleView;

    @OnClick(R.id.ibBack)
    public void backToHome() {
        if (getHoldingActivity() instanceof MainActivity) {
            ((MainActivity) getHoldingActivity()).setCurrentTab(MainFragmentNavAdapter.FM_MAIN);
        }
    }


    public static ActivitiesFragment newInstance(String bizCode) {
        return newInstance(bizCode, false);
    }

    public  static ActivitiesFragment newInstance(String bizCode, boolean displayTitleBar) {
        ActivitiesFragment fragment = new ActivitiesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, bizCode);
        args.putBoolean(ARG_PARAM2,displayTitleBar);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBizcode = getArguments().getString(ARG_PARAM1);
            displayTitleBar = getArguments().getBoolean(ARG_PARAM2);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_activities;
    }


    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {
        titleView.setVisibility(displayTitleBar ? View.VISIBLE : View.GONE);
//        ((TextView)titleView.findViewById(R.id.textViewTitle)).setText(getGlobalParams().getCurrentBizCode().getBizName());
        adapter = new ActAdapter(getContext(),activities, displayTitleBar);
        mRvAct.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvAct.setAdapter(adapter);
        springView.setHeader(new RefreshHeaderView(getActivity()));
        refreshFootView = new RefreshFootView(getActivity());
        springView.setFooter(refreshFootView);
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                lastRecId = 0;
                isShowDialog = false;
                statusLayout.setShowStatusLayout(false);
                prepareFragmentData();
                GetCommonObjectUtils.onFinishFreshAndLoad(springView);
            }

            @Override
            public void onLoadmore() {
                isShowDialog = true;
                prepareFragmentData();
                GetCommonObjectUtils.onFinishFreshAndLoad(springView);
            }
        });
        prepareFragmentData();
    }


    @Override
    protected void initFragmentData(Bundle savedInstanceState) {

    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        isShowDialog = false;
        prepareFragmentData();

    }

    @Override
    protected void prepareFragmentData() {
        super.prepareFragmentData();
        //getGlobalParams().isLogin()
        if (false) {
            BizDataRequest.requestGetVisibleActivitys(getHoldingActivity(), getGlobalParams().getAtlasId(), lastRecId, mBizcode, isShowDialog, statusLayout, new BizDataRequest.OnVisibleActivities() {
                @Override
                public void onSuccess(VisibleActivitiesJson visibleActivitiesJson) {
                    statusLayout.setShowStatusLayout(true);
                    updateView(visibleActivitiesJson);
                }
                @Override
                public void onError(DcnException error) {

                }
            });
        } else {
            BizDataRequest.requestGetVisibleActivitysForVisitor(getContext(), getGlobalParams().getAtlasId(), lastRecId, mBizcode, isShowDialog , statusLayout, new BizDataRequest.OnVisibleActivities() {
                @Override
                public void onSuccess(VisibleActivitiesJson visibleActivitiesJson) {
                    statusLayout.setShowStatusLayout(true);
                    updateView(visibleActivitiesJson);
                }
                @Override
                public void onError(DcnException error) {

                }
            });
        }
    }

    private void updateView(VisibleActivitiesJson visibleActivitiesJson){
        List<ActivityJson> rows = visibleActivitiesJson.rows;
        if(lastRecId == 0){
            activities.clear();
        }
        if(rows != null && rows.size() > 0) {
            lastRecId = rows.get(rows.size() - 1).id;
            activities.addAll(rows);
        }else{
            if(refreshFootView!= null){
                refreshFootView.setLoadingFinish();
            }
        }
        //Collections.sort(activities,new ASortClass());
        if(activities.size() == 0){
            springView.setEnable(false);
            adapter.setEmptyView(R.layout.view_product_null, mRvAct);
        }
        adapter.notifyDataSetChanged();
    }


}
