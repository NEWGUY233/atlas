package com.atlas.crmapp.coffee.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.MainActivity;
import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.CoffeeBannerAdapter;
import com.atlas.crmapp.adapter.RecommendAdapter;
import com.atlas.crmapp.adapter.navadapter.MainFragmentNavAdapter;
import com.atlas.crmapp.coffee.CoffeeDetailActivity;
import com.atlas.crmapp.coffee.CoffeeListActivity;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.ecosphere.EcoActivity;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.MTAUtils;
import com.atlas.crmapp.view.TitleAndMoreView;
import com.atlas.crmapp.view.ViewPagerIndicatorOnBottomView;
import com.atlas.crmapp.view.ViewPagerIndicatorOnBottomView_;
import com.atlas.crmapp.widget.MyListView;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/13
 *         Description :
 */

public class CoffeeFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    public static final String TAG = CoffeeFragment.class.getSimpleName();

    @BindView(R.id.recommend_listview)
    MyListView listViewRecommend;

    @BindView(R.id.v_title_more_month)
    TitleAndMoreView vTitleMoreMonth;
    @BindView(R.id.v_title_more_activity)
    TitleAndMoreView vTitleMoreActivity;
    private View vMoreActivity;
    private View vMoreMoth;

    private CoffeeBannerAdapter coffeeBannerAdapter;

    @BindView(R.id.vp_recommd)
    ViewPagerIndicatorOnBottomView vpRecommd;
//
//    @BindView(R.id.dsv_banner)
//    DiscreteScrollView mDsvBanner;

    @BindView(R.id.textViewTitle)
    TextView mTvTitle;

    @BindView(R.id.rl_online_clock)
    RelativeLayout rlOnlineClock;


    @BindView(R.id.pager)
    ViewPagerIndicatorOnBottomView_ pager;

    @OnClick(R.id.ibBack)
    public void back() {
        if (getHoldingActivity() instanceof  MainActivity) {
            ((MainActivity) getHoldingActivity()).setCurrentTab(MainFragmentNavAdapter.FM_MAIN);
        }
    }
    @OnClick(R.id.tv_now_online)
    public void onClickToCoffeeList(){
        MTAUtils.trackCustomEvent(getActivity(), "coffee_online_order");
        toCoffeeList();
    }

    private String strCode = "";

    public static CoffeeFragment newInstance() {
        CoffeeFragment fragment = new CoffeeFragment();
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_coffee;
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {
        umengPageTitle = getGlobalParams().getCurrentBizCode().getBizName();
        setUmengPageTitle(umengPageTitle);
        mTvTitle.setText(umengPageTitle);

        Log.i("initFragmentViews","umengPageTitle = " + umengPageTitle);

        if("gogreen".equalsIgnoreCase(getGlobalParams().getCurrentBizCode().getBizCode())){
            vTitleMoreMonth.updateViews(getString(R.string.gogrenn_course));
        }else{
            vTitleMoreMonth.updateViews(getString(R.string.this_monent_recommend));
        }

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
        prepareFragmentData();

        if(Constants.BIZ_CODE.COFFEE.equalsIgnoreCase(getGlobalParams().getCurrentBizCode().getBizCode())){
            rlOnlineClock.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {

    }


    void toCoffeeList() {
        Log.i("initFragmentViews","toCoffeeList = " + getGlobalParams().getCurrentBizCode().getBizName());
        Intent intent =new Intent(getHoldingActivity(), CoffeeListActivity.class);
        intent.putExtra("type",getGlobalParams().getCurrentBizCode().getBizCode());
        intent.putExtra("unitId",getGlobalParams().getCurrentBizCode().getUnitId());
        intent.putExtra("name",getGlobalParams().getCurrentBizCode().getBizName());
        startActivity(intent);
        //getHoldingActivity().showToast("本月推荐-更多");
    }

    //
    void toCoffeeShop() {
        Log.i("initFragmentViews","toCoffeeShop = " + mTvTitle.getText());
        Intent intent =new Intent(getHoldingActivity(), EcoActivity.class);
        intent.putExtra(EcoActivity.InParamter.titleStr,mTvTitle.getText() );
        intent.putExtra("bizcode",getGlobalParams().getCurrentBizCode().getBizCode());
        startActivity(intent);
        //getHoldingActivity().showToast("活动推荐-更多");
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareFragmentData();
    }

    //初始化数据
    @Override
    protected void prepareFragmentData() {
        super.prepareFragmentData();
        BizDataRequest.requestResource(getHoldingActivity(),String.valueOf(getGlobalParams().getAtlasId()),
                "app/business/main/ad/"+getGlobalParams().getCurrentBizCode().getBizCode()+",app/business/main/recommend/"
                        +getGlobalParams().getCurrentBizCode().getBizCode()+",app/business/main/activity/"
                        +getGlobalParams().getCurrentBizCode().getBizCode(), statusLayout, new BizDataRequest.OnRequestResource() {
            @Override
            public void onSuccess(ResourceJson resourceJson) {
                if(resourceJson.rows!=null&&resourceJson.rows.size()>0) {

                    int rowsSize = resourceJson.rows.size();

                    if (resourceJson.rows.get(0) != null && rowsSize > 0) {
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

//                    if (mDsvBanner != null) {
//                        if (resourceJson.rows.get(0) != null && rowsSize > 0) {
//                            List<ResourceJson.ResourceMedia> resList = resourceJson.rows.get(0).resourceMedias;
//                            for (int i = 0; i < resList.size(); i++) {
//                                resList.get(i).thumbnail = String.valueOf(R.drawable.ic_product_thum);
//                            }
//                            coffeeBannerAdapter = new CoffeeBannerAdapter(getHoldingActivity(), getGlobalParams().getCurrentBizCode().getBizCode(), getGlobalParams().getCurrentBizCode().getUnitId(), resList);
//                            mDsvBanner.setAdapter(coffeeBannerAdapter);
//                            mDsvBanner.scrollToPosition(coffeeBannerAdapter.getItemCount()/2);
//                        }
//                    }
                    if (vpRecommd != null ) {
                        if (resourceJson.rows.get(1) != null && rowsSize > 1) {
                            List<ResourceJson.ResourceMedia> resList = resourceJson.rows.get(1).resourceMedias;
                            vpRecommd.updateViews(resList, getGlobalParams().getCurrentBizCode().getBizCode(), getGlobalParams().getCurrentBizCode().getUnitId());
                        }
                    }
                    if (listViewRecommend != null && rowsSize > 2) {
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

    //响应item点击事件
     @Override
     public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
//                 Toast.makeText(getActivity(), "listview的item被点击了！，点击的位置是-->" + position,
//                                 Toast.LENGTH_SHORT).show();
         getActivity().startActivity(new Intent(getHoldingActivity(), CoffeeDetailActivity.class));
     }


}
