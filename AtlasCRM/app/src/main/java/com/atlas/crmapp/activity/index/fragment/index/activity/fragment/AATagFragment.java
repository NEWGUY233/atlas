package com.atlas.crmapp.activity.index.fragment.index.activity.fragment;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.index.adapter.AATagBodyAdapter;
import com.atlas.crmapp.activity.index.fragment.index.adapter.AATagHeadAdapter;
import com.atlas.crmapp.bean.TagBean;
import com.atlas.crmapp.dagger.component.index.DaggerAATagFragmentComponent;
import com.atlas.crmapp.dagger.module.index.AATagFragmentModule;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.VThreadsJson;
import com.atlas.crmapp.presenter.AATagFragmentPresenter;
import com.atlas.crmapp.service.NetChangeBroadCaster;
import com.atlas.crmapp.util.GetCommonObjectUtils;
import com.atlas.crmapp.view.RefreshFootView;
import com.atlas.crmapp.view.RefreshHeaderView;
import com.liaoinstan.springview.widget.SpringView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/3/20.
 */

public class AATagFragment extends BaseFragment {
    @BindView(R.id.recyclerView_head)
    RecyclerView recyclerViewHead;
    @BindView(R.id.recyclerView_body)
    RecyclerView recyclerViewBody;
    @BindView(R.id.btn)
    TextView btn;
    Unbinder unbinder;
    @BindView(R.id.no_net)
    View noNet;

    @Inject
    AATagFragmentPresenter presenter;
    @BindView(R.id.springview)
    SpringView springView;
    private RefreshFootView refreshFootView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_aat_tag;
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {


    }

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        initData();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    Animation animation;
    AATagHeadAdapter headAdapter;
    AATagBodyAdapter bodyAdapter;

    private void initView() {
        DaggerAATagFragmentComponent.builder().aATagFragmentModule(new AATagFragmentModule(this))
                .build().inject(this);

        LinearLayoutManager ll = new LinearLayoutManager(getContext());
        ll.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewHead.setLayoutManager(ll);
        headAdapter = new AATagHeadAdapter(getContext());
        recyclerViewHead.setAdapter(headAdapter);

        LinearLayoutManager ll1 = new LinearLayoutManager(getContext());
        ll1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewBody.setLayoutManager(ll1);
        recyclerViewBody.setNestedScrollingEnabled(false);
        bodyAdapter = new AATagBodyAdapter(getContext());
        recyclerViewBody.setAdapter(bodyAdapter);

        animation = new TranslateAnimation(0, 0
                , -(int) getResources().getDisplayMetrics().density * 50, 0);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        startNetListener();

        springView.setHeader(new RefreshHeaderView(getActivity()));
        refreshFootView = new RefreshFootView(getActivity());
        springView.setFooter(refreshFootView);
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                maxId = 0;
                initData();
                refreshFootView.setLoadingFinish();
                GetCommonObjectUtils.onFinishFreshAndLoad(springView);
            }

            @Override
            public void onLoadmore() {
                if (bodyAdapter.getVthre() != null && bodyAdapter.getVthre().size() != 0){
                    maxId = bodyAdapter.getVthre().get(bodyAdapter.getItemCount() - 1).getId();
                }
                presenter.getBodyList(maxId);
            }
        });
    }

    NetChangeBroadCaster broadCaster;

    private void startNetListener() {
        broadCaster = new NetChangeBroadCaster();
        broadCaster.setOnNetChanged(new NetChangeBroadCaster.OnNetChanged() {
            @Override
            public void onNetChanged(boolean isConnected) {
                if (isConnected)
                    noNet.setVisibility(View.GONE);
                else {
                    animation.setDuration(500);
                    noNet.setAnimation(animation);
                    noNet.setVisibility(View.VISIBLE);
                    animation.start();
                }
            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(broadCaster, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broadCaster != null)
            context.unregisterReceiver(broadCaster);
    }

    long maxId = 0;
    private void initData() {
        presenter.getTagList();
        presenter.getBodyList(maxId);
    }

    public void setList(List<TagBean> list) {
        headAdapter.setList(list);
    }

    public void setBodyList(List<VThreadsJson> vthre) {
        bodyAdapter.setVthre(vthre);
    }

    public void addBodyList(List<VThreadsJson> vthre) {
        bodyAdapter.getVthre().addAll(vthre);
        bodyAdapter.notifyDataSetChanged();
        GetCommonObjectUtils.onFinishFreshAndLoad(springView);
    }

    public void onNetError(){
        refreshFootView.setLoadingFinish();
        GetCommonObjectUtils.onFinishFreshAndLoad(springView);
    }
}
