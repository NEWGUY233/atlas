package com.atlas.crmapp.ecosphere.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.TalkAdapter;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.VThreadsJson;
import com.atlas.crmapp.model.VisibleThreadsJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.GetCommonObjectUtils;
import com.atlas.crmapp.view.RefreshFootView;
import com.atlas.crmapp.view.RefreshHeaderView;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Alex on 2017/5/16.
 */

public class TalkFragment extends BaseFragment{
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private TalkAdapter adapter;
    ArrayList<VThreadsJson> data = new ArrayList<VThreadsJson>();
    long lastRecId = 0;

    @BindView(R.id.springview)
    SpringView springView;

    private RefreshFootView refreshFootView;
    private boolean isShowProgreeDialog;

    public static TalkFragment newInstance() {
        return new TalkFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_talk;
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {
        adapter= new TalkAdapter(getHoldingActivity(), data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        springView.setHeader(new RefreshHeaderView(getActivity()));
        refreshFootView = new RefreshFootView(getActivity());
        springView.setFooter(refreshFootView);
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(onFreshListener);
        prepareFragmentData();

    }


    private SpringView.OnFreshListener onFreshListener =new SpringView.OnFreshListener() {
        @Override
        public void onRefresh() {
            isShowProgreeDialog = false;
            statusLayout.setShowStatusLayout(false);
            lastRecId = 0 ;
            prepareFragmentData();
            GetCommonObjectUtils.onFinishFreshAndLoad(springView);
        }

        @Override
        public void onLoadmore() {
            isShowProgreeDialog  = true;
            prepareFragmentData();
            GetCommonObjectUtils.onFinishFreshAndLoad(springView);
        }
    };

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {

    }

    @Override
    protected void prepareFragmentData() {
        super.prepareFragmentData();

        if (getGlobalParams().isLogin()) {
            BizDataRequest.requestVisibleThreads(getContext(), getGlobalParams().getAtlasId(), lastRecId, isShowProgreeDialog, statusLayout,new BizDataRequest.OnVisibleThreads() {
                @Override
                public void onSuccess(VisibleThreadsJson visibleThreadsJson) {
                    statusLayout.setShowStatusLayout(true);
                    updateView(visibleThreadsJson);
                }
                @Override
                public void onError(DcnException error) {

                }
            });
        } else {
            BizDataRequest.requestVisibleThreadsForVisitor(getContext(), getGlobalParams().getAtlasId(), lastRecId, isShowProgreeDialog, statusLayout, new BizDataRequest.OnVisibleThreads() {
                @Override
                public void onSuccess(VisibleThreadsJson visibleThreadsJson) {
                    statusLayout.setShowStatusLayout(true);
                    updateView(visibleThreadsJson);
                }

                @Override
                public void onError(DcnException error) {

                }
            });
        }
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        isShowProgreeDialog = false;
        prepareFragmentData();
    }

    private void updateView(VisibleThreadsJson visibleThreadsJson){
        if (lastRecId == 0) {
            data.clear();
        }
        List<VThreadsJson> vthre = visibleThreadsJson.rows;
        if (vthre != null && vthre.size() > 0) {
            lastRecId = vthre.get(vthre.size() - 1).id;
            data.addAll(vthre);
        }else{
            if(refreshFootView!= null){
                refreshFootView.setLoadingFinish();
            }
        }
        if(data.size() == 0){
            adapter.setEmptyView(R.layout.view_product_null, recyclerView);
        }
        adapter.notifyDataSetChanged();
    }


}