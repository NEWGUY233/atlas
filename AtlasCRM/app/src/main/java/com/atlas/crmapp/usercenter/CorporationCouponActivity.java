package com.atlas.crmapp.usercenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.ViewPagerAdapter;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.AllowanceContractCouponsListJson;
import com.atlas.crmapp.model.ContractJson;
import com.atlas.crmapp.model.ContractsJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.usercenter.fragment.CorporationCouponFragment;
import com.atlas.crmapp.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CorporationCouponActivity extends BaseStatusActivity {

    List<ContractJson> contracts;
    long contractId;
    private ArrayList<String> mTabTitles = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    FragmentManager fragmentManager = getSupportFragmentManager();

    private static int TAB_MARGIN_DIP = 5;
    public static final String INTENT_KEY_COMPANY_NAME = "companyName";
    private String companyName;

    @BindView(R.id.titleButton)
    TextView titleButton;

    @BindView(R.id.coupon_viewPager)
    ViewPager mViewPager;

    @BindView(R.id.tab_layout)
    TabLayout mTablayout;

    @OnClick(R.id.ibBack)
    void onBack() {
        this.finish();
    }

    @OnClick(R.id.titleButton)
    void switchContact(View view) {
        MyContractsDialog dialog = new MyContractsDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("contracts",(Serializable) contracts);
        dialog.setArguments(bundle);
        dialog.listener = new MyContractsDialog.MyContractsDialogListener() {
            @Override
            public void onMyContractDialogSelected(ContractJson contract) {
                if (contract != null) {
                    titleButton.setText(contract.companyName);
                    contractId = contract.id;
                    loadAllowanceCoupon(contractId);
                }
            }
        };
        dialog.show(getSupportFragmentManager(), "MyContractDialog");
    }

    public static void newInstance(Context context, String companyName){
        Intent intent = new Intent(context, CorporationCouponActivity.class);
        intent.putExtra(INTENT_KEY_COMPANY_NAME, companyName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporation_coupon);
        Intent intent = getIntent();
        if(intent!= null){
            companyName = intent.getStringExtra(INTENT_KEY_COMPANY_NAME);
        }
        if(StringUtils.isNotEmpty(companyName)){
            titleButton.setText(companyName);
            prepareActivityData();
        }else{
            titleButton.setText(R.string.c_qy);
            statusLayout.showEmpty();
        }
    }

    @Override
    protected int setTopBar() {
        return R.layout.view_action_bar_arr_down;
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareActivityData();
    }




    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
    BizDataRequest.requestMyContracts(this, getGlobalParams().getAtlasId(), false, statusLayout, new BizDataRequest.OnContractsRequestRescult() {
            @Override
            public void onSuccess(ContractsJson contractsJson) {
                contracts = contractsJson.rows;
                boolean haveContract = false;
                if(contracts.size() > 0) {
                    for (ContractJson contract : contracts){
                        if(contract.companyName.equals(companyName)){
                            contractId = contract.id;
                            loadAllowanceCoupon(contractId);
                            haveContract = true;
                        }
                    }

                    if(!haveContract){
                        statusLayout.showEmpty();
                    }
                } else {
                    statusLayout.showEmpty();
                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    public void loadAllowanceCoupon(long contractId) {
        FragmentTransaction tm = fragmentManager.beginTransaction();
        if(mFragments != null){
            for(Fragment fragment : mFragments){
                tm.remove(fragment);
            }
        }
        tm.commit();
        BizDataRequest.requestListAllowanceContractCoupons(this, contractId, new BizDataRequest.OnListAllowanceContractCouponsRequestResult() {
            @Override
            public void onSuccess(AllowanceContractCouponsListJson allowanceContractCouponsListJson) {
                if (allowanceContractCouponsListJson != null) {
                    mTabTitles.clear();
                    mFragments.clear();
                    mViewPager.removeAllViews();
                    if(allowanceContractCouponsListJson.recordsFiltered == 0){
                        statusLayout.showEmpty();
                        return;
                    }
                    for (int i=0; i<allowanceContractCouponsListJson.rows.size(); i++) {
                        mTabTitles.add(allowanceContractCouponsListJson.rows.get(i).name);
                        Fragment fragment = CorporationCouponFragment.newInstance(allowanceContractCouponsListJson.rows.get(i).coupons, 0);//未使用
                        mFragments.add(fragment);
                    }
                    statusLayout.showContent();
                    mViewPager.setAdapter(new ViewPagerAdapter(fragmentManager, mFragments, mTabTitles));
                    mTablayout.setupWithViewPager(mViewPager);
                    setIndicator(CorporationCouponActivity.this, mTablayout, TAB_MARGIN_DIP, TAB_MARGIN_DIP);
                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    public static void setIndicator(Context context, TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout ll_tab = null;
        try {
            ll_tab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) (getDisplayMetrics(context).density * leftDip);
        int right = (int) (getDisplayMetrics(context).density * rightDip);

        for (int i = 0; i < ll_tab.getChildCount(); i++) {
            View child = ll_tab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric;
    }
}
