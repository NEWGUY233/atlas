package com.atlas.crmapp.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.CompnayInfoJson;
import com.atlas.crmapp.model.VisibleCompanysJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.util.UiUtil;
import com.atlas.crmapp.widget.CircleImageView;
import com.michael.easydialog.EasyDialog;
import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyCompany_Activity extends BaseStatusActivity implements MyCompanyDialogListener {

    @BindView(R.id.titleButton)
    Button titleButton;

    @BindView(R.id.iv_logo)
    CircleImageView mIvLogo;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_description)
    TextView mTvDescription;
    @BindView(R.id.tv_contact)
    TextView mTvContract;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;

    @BindView(R.id.ibHome)
    ImageButton ibHome;
    @BindView(R.id.linearLayoutContent)
    LinearLayout linearLayoutContent;
    @BindView(R.id.item_user_info)
    LinearLayout itemUserInfo;
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @BindView(R.id.textView12)
    TextView textView12;
    @BindView(R.id.tv_middle)
    TextView tv_middle;
    @BindView(R.id.tv_phone_)
    TextView tv_phone_;
    @BindView(R.id.linearLayout3)
    LinearLayout linearLayout3;

    @OnClick(R.id.ibBack)
    void onBack() {
        this.finish();
    }

    @OnClick(R.id.ibHome)
    void onMoreButtonClick() {
        showShopChoiceDialog();
    }

    EasyDialog easy;
    List<String> list;
    List<CompnayInfoJson> companies;
    CompnayInfoJson company;
    private int currentCompanyIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        umengPageTitle = "企业账号";
        setContentView(R.layout.activity_my_company_);
        ButterKnife.bind(this);
        prepareActivityData();
    }

    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }

    @Override
    protected void initActivityViews() {
        super.initActivityViews();
        ibHome.setVisibility(View.VISIBLE);
    }

    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
        BizDataRequest.requestGetVisibleCompanys(MyCompany_Activity.this, false, statusLayout, new BizDataRequest.OnVisibleCompanysRequestResult() {
            @Override
            public void onSuccess(VisibleCompanysJson visibleCompanysJson) {
                statusLayout.setShowStatusLayout(false);
                companies = visibleCompanysJson.rows;
                if (companies.size() > 0) {
                    company = companies.get(currentCompanyIndex);
                    configWithCompany(companies.get(currentCompanyIndex));
                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareActivityData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareActivityData();
    }

    private void showShopChoiceDialog() {
//        int[] attachedViewLocation = new int[2];
//        ibHome.getLocationOnScreen(attachedViewLocation);
//        attachedViewLocation[0] += ibHome.getWidth() / 4 + UiUtil.dipToPx(this, 5);
//        attachedViewLocation[1] += ibHome.getHeight();
//        View view = getLayoutInflater().inflate(R.layout.layout_dialog_my_company_more, null);
//        easy = new EasyDialog(this)
//                .setLayout(view)
//                .setBackgroundColor(MyCompany_Activity.this.getResources().getColor(R.color.white_color))
//                .setMarginLeftAndRight(0, 10)
//                .setGravity(EasyDialog.GRAVITY_BOTTOM)
//                .setLocationByAttachedView(ibHome)
//                .setTouchOutsideDismiss(true)
//                .setLocation(attachedViewLocation)
//                .setMatchParent(false)
//                .setOutsideColor(MyCompany_Activity.this.getResources().getColor(R.color.dialog_outside_bg))
//                .show();
//        View v1 = easy.getTipViewInstance().findViewById(R.id.tv_corporation_rights);
//
//        v1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CorporationCouponActivity.newInstance(MyCompany_Activity.this, company.name);
//                easy.dismiss();
//            }
//        });
//
//        View v2 = easy.getTipViewInstance().findViewById(R.id.tv_corporation_bill);
//
//        v2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CorporationBillActivity.startActivity(MyCompany_Activity.this, company.name);
//                easy.dismiss();
//            }
//        });
//
//        View v3 = easy.getTipViewInstance().findViewById(R.id.tv_corporation_person);
//
//        v3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MyCompany_Activity.this, CorporationMemberActivity.class);
//                intent.putExtra("id", company.id);
//                intent.putExtra("companyName", company.name);
//                MyCompany_Activity.this.startActivity(intent);
//                easy.dismiss();
//            }
//        });
//
//        View v4 = easy.getTipViewInstance().findViewById(R.id.tv_corporation_info_edit);
//
//        v4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
                Intent intent = new Intent(MyCompany_Activity.this, CorporationInfoEditActivity.class);
                intent.putExtra("company", (Serializable) company);
                MyCompany_Activity.this.startActivity(intent);
//                easy.dismiss();
//            }
//        });
    }


    private void configWithCompany(CompnayInfoJson company) {
        this.company = company;
        Logger.d("company.thumbnail----" + company.thumbnail);
        GlideUtils.loadCustomImageView(this, R.drawable.ic_reg_logo, LoadImageUtils.loadSmallImage(company.thumbnail), mIvLogo);
        titleButton.setText(company.name);
        mTvName.setText(company.name);
        mTvDescription.setText(company.description);
        mTvContract.setText(company.contact);
        mTvPhone.setText(company.phone);
        tv_middle.setText(company.contact);
        tv_phone_.setText(company.phone);
    }


    public void switchCompany(View view) {
        MyCompanyDialog dialog = new MyCompanyDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("companies", (Serializable) companies);
        dialog.setArguments(bundle);
        dialog.listener = this;
        dialog.show(getSupportFragmentManager(), "MyCompanyDialog");
    }

    @Override
    public void onMyCompanyDialogSelected(CompnayInfoJson company) {
        configWithCompany(company);
        int size = companies.size();
        for (int i = 0; i < size; i++) {
            if (company.name.equals(companies.get(i).name)) {
                currentCompanyIndex = i;
            }
        }
    }

    @OnClick({R.id.tv_balance, R.id.tv_bill, R.id.tv_vip, R.id.tv_employees})
    public void onViewClicked(View view) {
        switch (view.getId()) {
                //额度查询
            case R.id.tv_balance:
                CorporationMoneyActivity.startActivity(MyCompany_Activity.this, company.name);
                break;
                //企业账单
            case R.id.tv_bill:
                CorporationBillActivity.startActivity(MyCompany_Activity.this, company.name);
                break;
                //企业权益
            case R.id.tv_vip:
                CorporationCouponActivity.newInstance(MyCompany_Activity.this, company.name);
                break;
                //企业员工
            case R.id.tv_employees:
                Intent intent = new Intent(MyCompany_Activity.this, CorporationMemberActivity.class);
                intent.putExtra("id", company.id);
                intent.putExtra("companyName", company.name);
                MyCompany_Activity.this.startActivity(intent);
                break;
        }
    }
}
