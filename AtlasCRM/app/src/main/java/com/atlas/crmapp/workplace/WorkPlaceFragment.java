package com.atlas.crmapp.workplace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.MainActivity;
import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.CoffeeBannerAdapter;
import com.atlas.crmapp.adapter.RecommendAdapter;
import com.atlas.crmapp.adapter.navadapter.MainFragmentNavAdapter;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.commonactivity.WebActivity;
import com.atlas.crmapp.ecosphere.EcoActivity;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.CityCenterJson;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.ActionUriUtils;
import com.atlas.crmapp.view.ImageAndTextBtnView;
import com.atlas.crmapp.view.TitleAndMoreView;
import com.atlas.crmapp.widget.MyListView;
import com.orhanobut.logger.Logger;
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

public class WorkPlaceFragment extends BaseFragment {

    public static final String TAG = WorkPlaceFragment.class.getSimpleName();
    private CoffeeBannerAdapter bannerAdapter;
    @BindView(R.id.recommend_listview)
    MyListView listViewRecommend;

    @BindView(R.id.v_work_nav)
    TitleAndMoreView vWorkNav;

    @BindView(R.id.dsv_banner)
    DiscreteScrollView mDsvBanner;


    @BindView(R.id.btn_meeting_room)
    ImageAndTextBtnView vMeetingRoom;

    @BindView(R.id.btn_reserve_server)
    ImageAndTextBtnView vReserveServer;

    @BindView(R.id.btn_add_vip)
    ImageAndTextBtnView vAddVip;
    @BindView(R.id.btn_visit_invite)
    ImageAndTextBtnView vVisit;

    @BindView(R.id.textViewTitle)
    TextView tvTitle;


    @OnClick(R.id.ibBack)
    public void back() {
        if (getHoldingActivity() instanceof MainActivity) {
            ((MainActivity) getHoldingActivity()).setCurrentTab(MainFragmentNavAdapter.FM_MAIN);
        }
    }

    public static WorkPlaceFragment newInstance() {
        WorkPlaceFragment fragment = new WorkPlaceFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_work_place;
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {
        umengPageTitle = "";
        if(getGlobalParams().getCurrentBizCode() != null){
            umengPageTitle = getGlobalParams().getCurrentBizCode().getBizName();
        }
        setUmengPageTitle(umengPageTitle);
        tvTitle.setText(umengPageTitle);
        updateMyView();
        prepareFragmentData();

    }

    @Override
    protected void prepareFragmentData() {
        super.prepareFragmentData();
        initCoffeeVideoData();
        initCoffeeTopicData();
    }

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {

    }

    private void updateMyView(){
        vWorkNav.updateViews(getString(R.string.activity_recommend));
        vMeetingRoom.updateViews(R.drawable.ic_wp_booking_meeting_room,getString(R.string.reserve_room));
        vReserveServer.updateViews(R.drawable.ic_wp_booking_service, getString(R.string.reserve_server));
        vAddVip.updateViews(R.drawable.ic_wp_rent, getString(R.string.meeting_look));
        vVisit.updateViews(R.drawable.ic_visit_invite, getString(R.string.visit_invite));
    }


    @OnClick(R.id.btn_meeting_room)
    void toMettingRoom() {
        startActivity(new Intent(getHoldingActivity(), MeetingRoomActivity.class));
    }

    @OnClick(R.id.btn_reserve_server)
    void toServiceRoom() {
        startActivity(new Intent(getHoldingActivity(), ServiceActivity.class));
    }

    @OnClick(R.id.btn_add_vip)
    public void toRentWorkPlace() {
        Intent intent = new Intent(getHoldingActivity(), WebActivity.class);
        GlobalParams globalParams = GlobalParams.getInstance();
        intent.putExtra(ActionUriUtils.content, globalParams.getBusinesseName(globalParams.getWorkplaceCode()));
        intent.putExtra(ActionUriUtils.url, Constants.CUSTOM_URL.INSERT_VIP);
        getActivity().startActivity(intent);
        //startActivity(new Intent(getHoldingActivity(), RentWorkPlaceActivity.class));
    }
    @OnClick(R.id.btn_visit_invite)
    public void toVisitInvite(){
        if(GlobalParams.getInstance().isLogin()){
            BizDataRequest.requestVisitCityCenterList(getActivity(), null, new BizDataRequest.OnResponseGetCenterList() {
                @Override
                public void onSuccess(List<CityCenterJson> centerListJsons) {
                    if (centerListJsons != null  && centerListJsons.size() > 0) {
                        VisitInviteActivity.newInstance(getActivity());
                    } else {
                        Toast.makeText(getActivity(), getActivity().getString(R.string.you_unser_visit), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onError(DcnException error) {

                }
            });

        }else{
            showAskLoginDialog(getActivity());
        }
    }

    @OnClick(R.id.ll_more)
    public void onMoreActClick() {
        Intent intent = new Intent(getHoldingActivity(),EcoActivity.class);
        intent.putExtra(EcoActivity.InParamter.titleStr,tvTitle.getText() );
        intent.putExtra("bizcode",getGlobalParams().getWorkplaceCode());
        startActivity(intent);
        //getHoldingActivity().showToast("活动推荐-更多");
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareFragmentData();
    }

    private void initCoffeeVideoData(){
        BizDataRequest.requestResource(getActivity(),String.valueOf(getGlobalParams().getAtlasId()), "app/business/main/ad/workplace", statusLayout, new BizDataRequest.OnRequestResource() {
            @Override
            public void onSuccess(ResourceJson resourceJson) {
                List<ResourceJson.ResourceMedia> resList= resourceJson.rows.get(0).resourceMedias;
                if (resList == null)
                    resList = new ArrayList<>();
                for(int i=0;i<resList.size();i++){
                    resList.get(i).thumbnail = String.valueOf(R.drawable.product);
                }
                bannerAdapter = new CoffeeBannerAdapter(getHoldingActivity(), getGlobalParams().getWorkplaceCode(), getGlobalParams().getWorkplaceId(),resList);
                Logger.d( "bannerAdapter ----"  + bannerAdapter);
                mDsvBanner.setAdapter(bannerAdapter);
                mDsvBanner.scrollToPosition(bannerAdapter.getItemCount()/2);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    private void initCoffeeTopicData(){
        BizDataRequest.requestResource(getActivity(),String.valueOf(getGlobalParams().getAtlasId()), "app/business/main/activity/workplace", new BizDataRequest.OnRequestResource() {
            @Override
            public void onSuccess(ResourceJson resourceJson) {
                if(resourceJson.rows!=null&&resourceJson.rows.size()>0) {
                    List<ResourceJson.ResourceMedia> resList = resourceJson.rows.get(0).resourceMedias;
                    RecommendAdapter adapter = new RecommendAdapter(getHoldingActivity(),resList);
                    listViewRecommend.setAdapter(adapter);
                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }
}
