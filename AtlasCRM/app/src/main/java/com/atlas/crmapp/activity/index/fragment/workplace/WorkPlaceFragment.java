package com.atlas.crmapp.activity.index.fragment.workplace;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.PrintDetailActivity;
import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.RecommendAdapter;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.commonactivity.WebActivity;
import com.atlas.crmapp.ecosphere.EcoActivity;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.CityCenterJson;
import com.atlas.crmapp.model.PrintJson;
import com.atlas.crmapp.model.PrintLogin;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.usercenter.ActivitiesVerificationActivity;
import com.atlas.crmapp.usercenter.RechargeActivity;
import com.atlas.crmapp.usercenter.UserCardActivity;
import com.atlas.crmapp.util.ActionUriUtils;
import com.atlas.crmapp.util.AppUtil;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.view.ImageAndTextBtnView;
import com.atlas.crmapp.view.TitleAndMoreView;
import com.atlas.crmapp.view.ViewPagerIndicatorOnBottomView_;
import com.atlas.crmapp.widget.CodeDialog;
import com.atlas.crmapp.widget.MyListView;
import com.atlas.crmapp.widget.PagerScrollView;
import com.atlas.crmapp.workplace.MeetingRoomActivity;
import com.atlas.crmapp.workplace.ServiceActivity;
import com.atlas.crmapp.workplace.VisitInviteActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/3/15.
 */

public class WorkPlaceFragment extends BaseFragment {

    public static final String TAG = com.atlas.crmapp.workplace.WorkPlaceFragment.class.getSimpleName();
    @BindView(R.id.recommend_listview)
    MyListView listViewRecommend;

    @BindView(R.id.v_work_nav)
    TitleAndMoreView vWorkNav;

    @BindView(R.id.pager)
    ViewPagerIndicatorOnBottomView_ pager;


    @BindView(R.id.btn_meeting_room)
    ImageAndTextBtnView vMeetingRoom;

    @BindView(R.id.btn_reserve_server)
    ImageAndTextBtnView vReserveServer;

    @BindView(R.id.btn_add_vip)
    ImageAndTextBtnView vAddVip;
    @BindView(R.id.btn_visit_invite)
    ImageAndTextBtnView vVisit;

    @BindView(R.id.scroll_view)
    PagerScrollView scroll_view;
    @BindView(R.id.textView)
    TextView textView;
    Unbinder unbinder;


//    @OnClick(R.id.ibBack)
//    public void back() {
//        if (getHoldingActivity() instanceof MainActivity) {
//            ((MainActivity) getHoldingActivity()).setCurrentTab(MainFragmentNavAdapter.FM_MAIN);
//        }
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_index_workplace;
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {
//        umengPageTitle = "";
//        if(getGlobalParams().getCurrentBizCode() != null){
//            umengPageTitle = getGlobalParams().getCurrentBizCode().getBizName();
//        }
//        setUmengPageTitle(umengPageTitle);
//        tvTitle.setText(umengPageTitle);
        textView.setText(getString(R.string.workplace));
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

    private void updateMyView() {
        vWorkNav.updateViews(getString(R.string.activity_recommend));
        vMeetingRoom.updateViews(R.drawable.ic_wp_booking_meeting_room, getString(R.string.reserve_room));
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
    public void toVisitInvite() {
        if (GlobalParams.getInstance().isLogin()) {
            BizDataRequest.requestVisitCityCenterList(getActivity(), null, new BizDataRequest.OnResponseGetCenterList() {
                @Override
                public void onSuccess(List<CityCenterJson> centerListJsons) {
                    if (centerListJsons != null && centerListJsons.size() > 0) {
                        VisitInviteActivity.newInstance(getActivity());
                    } else {
                        Toast.makeText(getActivity(), getActivity().getString(R.string.you_unser_visit), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onError(DcnException error) {

                }
            });

        } else {
            showAskLoginDialog(getActivity());
        }
    }

    @OnClick(R.id.ll_more)
    public void onMoreActClick() {
        Intent intent = new Intent(getHoldingActivity(), EcoActivity.class);
        intent.putExtra(EcoActivity.InParamter.titleStr, getString(R.string.workplace));
        intent.putExtra("bizcode", getGlobalParams().getWorkplaceCode());
        startActivity(intent);
        //getHoldingActivity().showToast("活动推荐-更多");
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareFragmentData();
    }

    private void initCoffeeVideoData() {
        BizDataRequest.requestResource(getActivity(), String.valueOf(getGlobalParams().getAtlasId()), "app/business/main/ad/workplace", statusLayout, new BizDataRequest.OnRequestResource() {
            @Override
            public void onSuccess(ResourceJson resourceJson) {
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

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    private void initCoffeeTopicData() {
        BizDataRequest.requestResource(getActivity(), String.valueOf(getGlobalParams().getAtlasId()), "app/business/main/activity/workplace", new BizDataRequest.OnRequestResource() {
            @Override
            public void onSuccess(ResourceJson resourceJson) {
                if (resourceJson.rows != null && resourceJson.rows.size() > 0) {
                    List<ResourceJson.ResourceMedia> resList = resourceJson.rows.get(0).resourceMedias;
                    RecommendAdapter adapter = new RecommendAdapter(getHoldingActivity(), resList);
                    listViewRecommend.setAdapter(adapter);
                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    @OnClick({R.id.iv_code, R.id.iv_scan_to_scan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_code:
                initScan();
                break;
            case R.id.iv_scan_to_scan:
                toScan();
                break;
        }
    }

    CodeDialog mCodeDialog;

    private void initScan() {
        if ((getHoldingActivity()).getGlobalParams().isLogin()) {

            if (mCodeDialog == null) {
                mCodeDialog = CodeDialog.newInstance();
            }

            if (!mCodeDialog.isAdded() && !mCodeDialog.isVisible() && !mCodeDialog.isRemoving()) {
                mCodeDialog.show(getHoldingActivity().getSupportFragmentManager(), "code_dialog");
            }
        } else {
            showAskLoginDialog(getHoldingActivity());
        }
    }

    private void toScan() {
        if (getGlobalParams().isLogin()) {
            IntentIntegrator.forSupportFragment(this)
                    .setBarcodeImageEnabled(false)
                    .setOrientationLocked(true)
                    .setPrompt(getString(R.string.text_70))
                    .initiateScan(IntentIntegrator.QR_CODE_TYPES);
        } else {
            showAskLoginDialog(getHoldingActivity());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
            } else {
                if (requestCode == IntentIntegrator.REQUEST_CODE) {

                    final String urlCode = result.getContents();
                    if (TextUtils.isEmpty(urlCode)) {
                        return;
                    }
                    Logger.d("result.getContents()====" + urlCode);
                    Intent intent = null;
                    if (urlCode.contains(Constants.CodeSkipType.TO_USER)) {
                        intent = new Intent(getHoldingActivity(), UserCardActivity.class);
                        intent.putExtra("code", result.getContents());
                        startActivity(intent);
                    } else if (urlCode.contains(Constants.CodeSkipType.TO_RECHARGE)) {
                        intent = new Intent(getActivity(), RechargeActivity.class);
                        String[] urlSplit = urlCode.split("/");
                        intent.putExtra("referral", urlSplit[urlSplit.length - 1]);
                        startActivity(intent);
                    } else if (urlCode.contains(Constants.CodeSkipType.TO_ACTIVITY_VERIFI)) {
                        requestAppUserGeteam(urlCode);
                    } else if (urlCode.contains(Constants.CodeSkipType.LOGIN_PC)) {
                        printLog(StringUtils.getLoginInfo(urlCode));
                    } else if (urlCode.contains(Constants.CodeSkipType.UNLOCK_PRINT)) {
                        unlockPrint(StringUtils.unLockPrint(urlCode));
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void printLog(PrintJson json) {
        BizDataRequest.requestPrintLogin(context, json, new BizDataRequest.PrintLoginInfo() {
            @Override
            public void onSuccess(PrintLogin bean) {
                Toast.makeText(context, context.getString(R.string.t30), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(DcnException error) {

            }
        });

    }

    public void unlockPrint(String stationId) {
//        Log.i("unlockPrint","stationId = " + stationId);
        BizDataRequest.requestPrintUnlock(context, stationId, AppUtil.getIPAddress(context), new BizDataRequest.PrintUnlockInfo() {
            @Override
            public void onSuccess() {
//                startServiceForPrint();
                context.startActivity(new Intent(context, PrintDetailActivity.class));
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    public void requestAppUserGeteam(final String code) {
        BizDataRequest.requestAppUserGeteam(context, new BizDataRequest.OnResponseAppUserGeteam() {
            @Override
            public void onSuccess(boolean isGeteam) {
                if (isGeteam) {
                    ActivitiesVerificationActivity.newInstance(getActivity(), code);
                } else {
                    Logger.e(" is not csr-----");
                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
