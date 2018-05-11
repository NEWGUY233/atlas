package com.atlas.crmapp.activity.index.fragment.livingspace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.IndexActivity;
import com.atlas.crmapp.activity.index.fragment.livingspace.adapter.LivingSpaceFoodAdapter;
import com.atlas.crmapp.activity.index.fragment.livingspace.adapter.LivingSpaceStudioAdapter;
import com.atlas.crmapp.bean.LivingBizBean;
import com.atlas.crmapp.common.BizCode;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.dagger.component.living.DaggerLivingSpaceFragmentComponent;
import com.atlas.crmapp.dagger.module.living.LivingSpaceFragmentModule;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.presenter.LivingSpaceFragmentPresenter;
import com.atlas.crmapp.usercenter.RechargeActivity;
import com.atlas.crmapp.usercenter.UserCardActivity;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.view.ViewPagerIndicatorOnBottomView_;
import com.atlas.crmapp.widget.CodeDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Leo on 2018/3/15.
 */

public class LivingSpaceFragment extends BaseFragment {
    @BindView(R.id.iv_box)
    ImageView ivBox;
    Unbinder unbinder;
    @BindView(R.id.recyclerView_food)
    RecyclerView recyclerViewFood;
    @BindView(R.id.viewpager)
    ViewPagerIndicatorOnBottomView_ viewpager;
    @BindView(R.id.recyclerView_studio)
    RecyclerView recyclerViewStudio;

    @Inject
    LivingSpaceFragmentPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_index_living_space;
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {
        initView();
        initData();
    }

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    LivingSpaceFoodAdapter foodAdapter;
    LivingSpaceStudioAdapter studioAdapter;
    long uid = 0;
    private void initView() {
        if (getActivity() != null && getActivity() instanceof IndexActivity){
            DaggerLivingSpaceFragmentComponent.builder().appComponent(((IndexActivity) getActivity()).getAppComponent())
                    .livingSpaceFragmentModule(new LivingSpaceFragmentModule(this)).build().inject(this);
        }

        ivBox.setVisibility(View.GONE);
        viewpager.setVisibility(View.GONE);
        LinearLayoutManager ll = new LinearLayoutManager(getContext());
        ll.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewFood.setLayoutManager(ll);
        foodAdapter = new LivingSpaceFoodAdapter(getContext());
        recyclerViewFood.setAdapter(foodAdapter);

        LinearLayoutManager ll1 = new LinearLayoutManager(getContext());
        ll1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewStudio.setLayoutManager(ll1);
        studioAdapter = new LivingSpaceStudioAdapter(getContext());
        recyclerViewStudio.setAdapter(studioAdapter);

        uid = getGlobalParams().getAtlasId();
    }

    private void initData(){
        presenter.getKitchenAndCoffee();
        presenter.getSports();
        presenter.getStudio();
        presenter.checkBiz(presenter.bizCF);
        presenter.checkBiz(presenter.bizKT);
        presenter.checkBiz(presenter.bizSP);
        presenter.checkBiz(presenter.bizGF);
    }

    @OnClick({R.id.iv_code, R.id.iv_scan_to_scan,R.id.rl_kitchen, R.id.rl_coffee, R.id.rl_fitness, R.id.rl_golf})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_code:
                initScan();
                break;
            case R.id.iv_scan_to_scan:
                toScan();
                break;
            case R.id.rl_kitchen:
                getGlobalParams().setCurrentBizCode(currentBiz(isKitchen?getGlobalParams().getCoffeeId() : kitchenId, getGlobalParams().getKitchenCode(),getString(R.string.index_living_space_food_kitchen)));
                if (isKitchen){
                    toLivingAll(getString(R.string.index_living_space_food_kitchen));
                }else {
                    toMore(getString(R.string.index_living_space_food_kitchen),kitchenName);
                }
                break;
            case R.id.rl_coffee:
                getGlobalParams().setCurrentBizCode(currentBiz(isCoffee? getGlobalParams().getCoffeeId() : coffeeId, getGlobalParams().getCoffeeCode(),getString(R.string.index_living_space_food_coffe)));
                if (isCoffee){
                    toLivingAll(getString(R.string.index_living_space_food_coffe));
                }else {
                    toMore(getString(R.string.index_living_space_food_coffe),coffeeName);
                }
                break;
            case R.id.rl_fitness:
                getGlobalParams().setCurrentBizCode(currentBiz(isSports ? getGlobalParams().getFitnessId() : sportsId, getGlobalParams().getFitnessCode(),getString(R.string.index_living_space_sport_fitness)));
                if (isSports){
                    toLivingAll(getString(R.string.index_living_space_sport_fitness));
                }else {
                    toMore(getString(R.string.index_living_space_sport_fitness),sportsName);
                }
                break;
            case R.id.rl_golf:
                getGlobalParams().setCurrentBizCode(currentBiz(isGolf ? getGlobalParams().getCoffeeId() : golfId, getGlobalParams().getGogreenCode(),getString(R.string.index_living_space_sport_golf)));
                if (isGolf){
                    toLivingAll(getString(R.string.index_living_space_sport_golf));
                }else {
                    toMore(getString(R.string.index_living_space_sport_golf),golfName);
                }
                break;
        }
    }

    private void toLivingAll(String title){
        Intent i = new Intent(context,LivingAllActivity.class);
        i.putExtra("title",title);
        startActivity(i);
    }

    private void toMore(String title,String biz){
        if (StringUtils.isEmpty(biz))
            return;
        Intent i = new Intent(context,LivingNoneActivity.class);
        i.putExtra("title",title);
        i.putExtra("biz",biz);
        startActivity(i);
    }


    private BizCode currentBiz(long id, String code,String title){
        BizCode bizCode = new BizCode();
        bizCode.setUnitId(id);
        bizCode.setBizName(title);
        bizCode.setBizCode(code);
        return  bizCode;
    }

    private void toScan(){
        if(getGlobalParams().isLogin()) {
            IntentIntegrator.forSupportFragment(this)
                    .setBarcodeImageEnabled(false)
                    .setOrientationLocked(true)
                    .setPrompt(getString(R.string.text_70))
                    .initiateScan(IntentIntegrator.QR_CODE_TYPES);
        } else {
            showAskLoginDialog(getHoldingActivity());
        }
    }

    CodeDialog mCodeDialog;
    private void initScan(){
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
            } else {
                if(requestCode == IntentIntegrator.REQUEST_CODE){

                    final String urlCode = result.getContents();
                    if(TextUtils.isEmpty(urlCode)){
                        return;
                    }
                    Logger.d("result.getContents()====" + urlCode);
                    Intent intent = null;
                    if(urlCode.contains(Constants.CodeSkipType.TO_USER)){
                        intent = new Intent(getHoldingActivity(), UserCardActivity.class);
                        intent.putExtra("code",result.getContents());
                        startActivity(intent);
                    }else if(urlCode.contains(Constants.CodeSkipType.TO_RECHARGE)){
                        intent = new Intent(getActivity(),RechargeActivity.class);
                        String[]  urlSplit =urlCode.split("/");
                        intent.putExtra("referral", urlSplit[urlSplit.length-1]);
                        startActivity(intent);
                    }else if(urlCode.contains(Constants.CodeSkipType.TO_ACTIVITY_VERIFI)){
                        presenter.requestAppUserGeteam(urlCode);
                    }else if(urlCode.contains(Constants.CodeSkipType.LOGIN_PC)){
                        presenter.printLog(StringUtils.getLoginInfo(urlCode));
                    }else if (urlCode.contains(Constants.CodeSkipType.UNLOCK_PRINT)){
                        presenter.unlockPrint(StringUtils.unLockPrint(urlCode));
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setKAC(ResourceJson resourceJson){
        if (resourceJson == null || resourceJson.rows == null || resourceJson.rows.size() == 0)
            return;
        foodAdapter.setmList(resourceJson.rows.get(0).resourceMedias);
    }

    public void setSports(ResourceJson resourceJson){
        if (resourceJson == null || resourceJson.rows == null || resourceJson.rows.size() == 0) {
            viewpager.setVisibility(View.GONE);
            return;
        }
        viewpager.setVisibility(View.VISIBLE);
        viewpager.updateViews((ArrayList<ResourceJson.ResourceMedia>) resourceJson.rows.get(0).resourceMedias);
        viewpager.startTimer();
    }

    public void setStudio(ResourceJson resourceJson){
        if (resourceJson == null || resourceJson.rows == null || resourceJson.rows.size() == 0) {
            return;
        }
        studioAdapter.setList(resourceJson.rows.get(0).resourceMedias);

    }


    boolean isKitchen = false;
    long kitchenId = 0;
    String kitchenName = "";
    public void initKitchen(LivingBizBean bizBean){
        if (GlobalParams.getInstance().getAtlasId() == bizBean.getParentUnit().getId()){
            isKitchen = true;
        }else {
            isKitchen = false;
            kitchenId = bizBean.getId();
            kitchenName = bizBean.getParentUnit().getName();
        }
    }

    boolean isCoffee = false;
    long coffeeId = 0;
    String coffeeName = "";
    public void initCoffee(LivingBizBean bizBean){
        if (GlobalParams.getInstance().getAtlasId() == bizBean.getParentUnit().getId()){
            isCoffee = true;
        }else {
            isCoffee = false;
            coffeeId = bizBean.getId();
            coffeeName = bizBean.getParentUnit().getName();
        }
    }

    boolean isSports = false;
    long sportsId = 0;
    String sportsName = "";
    public void initSports(LivingBizBean bizBean){
        if (GlobalParams.getInstance().getAtlasId() == bizBean.getParentUnit().getId()){
            isSports = true;
        }else {
            isSports = false;
            sportsId = bizBean.getId();
            sportsName = bizBean.getParentUnit().getName();
        }
    }

    boolean isGolf = false;
    long golfId = 0;
    String golfName = "";
    public void initGolf(LivingBizBean bizBean){
        if (GlobalParams.getInstance().getAtlasId() == bizBean.getParentUnit().getId()){
            isGolf = true;
        }else {
            isGolf = false;
            golfId = bizBean.getId();
            golfName = bizBean.getParentUnit().getName();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            if (uid == getGlobalParams().getAtlasId()){

            }else {
                uid = getGlobalParams().getAtlasId();
                initData();
            }
        }
    }
}
