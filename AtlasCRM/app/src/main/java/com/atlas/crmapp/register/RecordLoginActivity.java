package com.atlas.crmapp.register;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.IndexActivity;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.dagger.component.DaggerRecordLoginActivityComponent;
import com.atlas.crmapp.dagger.module.RecordLoginActivityModule;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.presenter.RecordLoginActivityPresenter;
import com.atlas.crmapp.util.ClickUtil;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.RegisterCommonUtils;
import com.atlas.crmapp.util.SpUtil;
import com.atlas.crmapp.view.VerifyCodeView;
import com.atlas.crmapp.widget.CircleImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/29.
 */

public class RecordLoginActivity extends BaseStatusActivity {

    @BindView(R.id.iv_icon)
    CircleImageView ivIcon;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.v_code)
    VerifyCodeView vCode;
    @BindView(R.id.btnCode)
    public Button btnCode;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    EditText etCode;

    @Inject
    RecordLoginActivityPresenter presenter;

    public PersonInfoJson mPersonInfoJson;
    @BindView(R.id.tv_voice)
    TextView tvVoice;
    @BindView(R.id.ll_voice)
    LinearLayout llVoice;

    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_record);
        ButterKnife.bind(this);

        initView();
    }

    @OnClick({R.id.iv_back, R.id.tv_other, R.id.btnCode, R.id.btnSubmit,R.id.tv_voice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_other:
                startActivity(RegisterActivity.class);
                finish();
                break;
            case R.id.btnCode:
                presenter.postCode(zipCode, phone);
                FormatCouponInfo.setLeftRightToGrayWhite(btnCode);
                btnCode.setClickable(false);
                break;
            case R.id.btnSubmit:
                presenter.reg(phone, etCode.getText().toString());
                break;
            case R.id.tv_voice:
                presenter.postCodeVoice(zipCode, phone);
                break;
        }
    }

    private String phone;
    private String zipCode;

    private void initView() {
        Glide.with(getContext()).load(SpUtil.getString(getContext(), SpUtil.ICON, ""))
                .apply(new RequestOptions().error(R.drawable.icon_bg_c)).into(ivIcon);
        phone = SpUtil.getString(getContext(), SpUtil.PHONE, "");
        zipCode = SpUtil.getString(this, SpUtil.AREA, "86");
        if (phone != null && phone.length() > 8)
            tvMobile.setText(phone.replace(phone.substring(3, 7), "****"));
        else
            tvMobile.setText(phone);
        etCode = (EditText) vCode.findViewById(R.id.etCode);
        etCode.setGravity(Gravity.CENTER);

        DaggerRecordLoginActivityComponent.builder().appComponent(getAppComponent())
                .recordLoginActivityModule(new RecordLoginActivityModule(this)).build().inject(this);
        FormatCouponInfo.setLeftRightToYellowWhite(btnCode);

        llVoice.setVisibility(View.INVISIBLE);

    }

    public void showVoice(){
        if ("86".equals(zipCode)){
            llVoice.setVisibility(View.VISIBLE);
        }else {
            llVoice.setVisibility(View.INVISIBLE);
        }
    }

    public CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            btnCode.setText((millisUntilFinished / 1000) + getString(R.string.t35));
        }

        @Override
        public void onFinish() {
            btnCode.setEnabled(true);
            btnCode.setText(R.string.t36);
            cancel();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }


//    public void initData(String nickName) {
//        EventBusFactory.getBus().post(mPersonInfoJson);
//        if (TextUtils.isEmpty(nickName)) {
//            if (ClickUtil.isFastDoubleClick()) {
//                return;
//            }
////            Intent intent = new Intent(this, RegInfoActivity.class);
//            Intent intent = new Intent(this, RegInfoActivity_.class);
//            this.startActivity(intent);
//            finish();
//        } else {
//            //HuanXinManager.login(getGlobalParams().getPersonInfoJson().getId()+"",getGlobalParams().getPersonInfoJson().getUid());
//            SpUtil.putBoolean(this, "isLogin", true);
//            RegisterCommonUtils.loginRegister(this, mPersonInfoJson);
//            GlobalParams.getInstance().setPersonInfoJson(mPersonInfoJson);
////            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//            Intent intent = new Intent(this, IndexActivity.class);
//            this.startActivity(intent);
//            finish();
//        }
//
//    }
}
