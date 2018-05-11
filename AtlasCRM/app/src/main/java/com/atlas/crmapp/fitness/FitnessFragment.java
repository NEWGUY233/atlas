package com.atlas.crmapp.fitness;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.atlas.crmapp.MainActivity;
import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.CoffeeBannerAdapter;
import com.atlas.crmapp.adapter.MonthRecommendAdapter;
import com.atlas.crmapp.adapter.RecommendAdapter;
import com.atlas.crmapp.adapter.navadapter.MainFragmentNavAdapter;
import com.atlas.crmapp.coffee.CoffeeDetailActivity;
import com.atlas.crmapp.coffee.CoffeeListActivity;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.commonactivity.WebActivity;
import com.atlas.crmapp.ecosphere.EcoActivity;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.ActionUriUtils;
import com.atlas.crmapp.view.ImageAndTextBtnView;
import com.atlas.crmapp.view.TitleAndMoreView;
import com.atlas.crmapp.view.ViewPagerIndicatorOnBottomView;
import com.atlas.crmapp.view.ViewPagerIndicatorOnBottomView_;
import com.atlas.crmapp.widget.MyListView;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class FitnessFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    public static final String TAG = FitnessFragment.class.getSimpleName();

    @BindView(R.id.v_title_more_month)
    TitleAndMoreView vTitleMoreMonth;
    @BindView(R.id.v_title_more_activity)
    TitleAndMoreView vTitleMoreActivity;
    private View vMoreActivity;
    private View vMoreMoth;

    private CoffeeBannerAdapter bannerAdapter;

    @BindView(R.id.btn_private_introducer)
    ImageAndTextBtnView vPrivateIntoducer;

    @BindView(R.id.btn_reserve_course)
    ImageAndTextBtnView vReserveCoutse;

    @BindView(R.id.btn_reserve_experience)
    ImageAndTextBtnView vReserveExperience;


    @BindView(R.id.vp_recommd)
    ViewPagerIndicatorOnBottomView vpRecommd;

//    @BindView(R.id.dsv_banner)
//    DiscreteScrollView mDsvBanner;

    @BindView(R.id.pager)
    ViewPagerIndicatorOnBottomView_ pager;

    @BindView(R.id.recommend_listview)
    MyListView listViewRecommend;

    @BindView(R.id.textViewTitle)
    TextView tvTitle;


    @OnClick(R.id.ibBack)
    public void back() {
        if (getHoldingActivity() instanceof MainActivity) {
            ((MainActivity) getHoldingActivity()).setCurrentTab(MainFragmentNavAdapter.FM_MAIN);
        }
    }


    @OnClick(R.id.btn_private_introducer)
    void onCoach(){
        startActivity(new Intent(getHoldingActivity(), PersonalCoachActivity.class));
    }

    @OnClick(R.id.btn_reserve_course)
    void onCourse(){
        startActivity(new Intent(getHoldingActivity(), AppointmentClassActivity.class));
    }

    @OnClick(R.id.btn_reserve_experience)
    void onExperience(){
        Intent intent = new Intent(getHoldingActivity(), WebActivity.class);
        GlobalParams globalParams = GlobalParams.getInstance();
        intent.putExtra(ActionUriUtils.content, globalParams.getBusinesseName(globalParams.getFitnessCode()));
        intent.putExtra(ActionUriUtils.url, Constants.CUSTOM_URL.RESERVE_EXPERIENCE);
        getActivity().startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fitness;
    }

    public static FitnessFragment newInstance() {
        FitnessFragment fragment = new FitnessFragment();
        return fragment;
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {
        umengPageTitle = getGlobalParams().getCurrentBizCode().getBizName();
        setUmengPageTitle(umengPageTitle);
        tvTitle.setText(umengPageTitle);
        initTitleMore();
        prepareFragmentData();
    }

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {
    }


    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareFragmentData();

    }

    @Override
    protected void prepareFragmentData() {
        super.prepareFragmentData();

        BizDataRequest.requestResource(getActivity(),String.valueOf(getGlobalParams().getAtlasId()), "app/business/main/ad/"+getGlobalParams().getCurrentBizCode().getBizCode()+",app/business/main/recommend/"+getGlobalParams().getCurrentBizCode().getBizCode()+",app/business/main/activity/"+getGlobalParams().getCurrentBizCode().getBizCode(), statusLayout, new BizDataRequest.OnRequestResource() {
            @Override
            public void onSuccess(ResourceJson resourceJson) {
                if(resourceJson.rows!=null&&resourceJson.rows.size()>0) {
                    if (pager != null) {

                        if (resourceJson.rows.get(0) != null && resourceJson.rows.size() > 0) {
                            List<ResourceJson.ResourceMedia> resList = resourceJson.rows.get(0).resourceMedias;
                            if (resList == null) {
                                pager.setVisibility(View.GONE);
                                return;
                            }
                            pager.setVisibility(View.VISIBLE);
                            pager.updateViews((ArrayList<ResourceJson.ResourceMedia>) resourceJson.rows.get(0).resourceMedias);
                            pager.cancel();
                            pager.startTimer();
                        }

                    }
                    if (vpRecommd != null) {
                        if (resourceJson.rows.get(1) != null) {
                            List<ResourceJson.ResourceMedia> resList = resourceJson.rows.get(1).resourceMedias;
                            vpRecommd.updateViews(resList, getGlobalParams().getCurrentBizCode().getBizCode(), getGlobalParams().getCurrentBizCode().getUnitId());
                        }
                    }
                    if (listViewRecommend != null) {
                        if (resourceJson.rows.get(2) != null) {
                            List<ResourceJson.ResourceMedia> resList = resourceJson.rows.get(2).resourceMedias;
                            RecommendAdapter adapter = new RecommendAdapter(getHoldingActivity(), resList);
                            listViewRecommend.setAdapter(adapter);
                        }
                    }
                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    private void initTitleMore(){
        vPrivateIntoducer.updateViews(R.drawable.ic_fitness_coach, getString(R.string.private_introducer));
        vReserveCoutse.updateViews(R.drawable.ic_fitness_course, getString(R.string.reserve_course));
        vReserveExperience.updateViews(R.drawable.ic_wp_rent, getString(R.string.reserve_experience));

        vTitleMoreMonth.updateViews(getString(R.string.this_monent_recommend));
        vMoreMoth = vTitleMoreMonth.findViewById(R.id.ll_more);
        vMoreMoth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCoffeeList();
            }
        });

        vTitleMoreActivity.updateViews(getString(R.string.activity_recommend));
        vMoreActivity = vTitleMoreActivity.findViewById(R.id.ll_more);
        vMoreActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCoffeeShop();
            }
        });
    }

    private void toCoffeeList() {

        Intent intent =new Intent(getHoldingActivity(), CoffeeListActivity.class);
        intent.putExtra("type",getGlobalParams().getCurrentBizCode().getBizCode());
        intent.putExtra("unitId",getGlobalParams().getCurrentBizCode().getUnitId());
        intent.putExtra("name",getGlobalParams().getCurrentBizCode().getBizName());
        startActivity(intent);

    }

    // TODO  仅用于测试入口
    private void toCoffeeShop() {
        Intent intent =new Intent(getHoldingActivity(), EcoActivity.class);
        intent.putExtra(EcoActivity.InParamter.titleStr,tvTitle.getText() );
        intent.putExtra("bizcode",getGlobalParams().getCurrentBizCode().getBizCode());
        startActivity(intent);

    }

    //响应item点击事件
    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {

        getActivity().startActivity(new Intent(getActivity(), CoffeeDetailActivity.class));
    }

    /**
     * 实现类，响应按钮点击事件
     */private MonthRecommendAdapter.MyClickListener mListener = new MonthRecommendAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {

            getActivity().startActivity(new Intent(getActivity(), CoffeeDetailActivity.class));
        }
    };


}


