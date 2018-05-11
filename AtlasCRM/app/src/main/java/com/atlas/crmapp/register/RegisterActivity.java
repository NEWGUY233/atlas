package com.atlas.crmapp.register;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.activity.index.IndexActivity;
import com.atlas.crmapp.adapter.wheel.PhoneWheelAdapter;
import com.atlas.crmapp.bean.RegionCodeBean;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.commonactivity.WebActivity;
import com.atlas.crmapp.dagger.component.DaggerRegisterActivityComponent;
import com.atlas.crmapp.dagger.module.RegisterActivityModule;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.presenter.RegisterActivityPresenter;
import com.atlas.crmapp.tim.Constant;
import com.atlas.crmapp.util.ClickUtil;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.RegisterCommonUtils;
import com.atlas.crmapp.util.SpUtil;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.view.VerifyCodeView;
import com.atlas.crmapp.view.wheel.WheelView;
import com.jaeger.library.StatusBarUtil;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.shaohui.bottomdialog.BottomDialog;


public class RegisterActivity extends BaseActivity {


    EditText etCode;

    @BindView(R.id.btnCode)
    public Button btnCode;

    @BindView(R.id.etMobile)
    EditText etMobile;

    @BindView(R.id.btnSubmit)
    Button btnSumit;

    @BindView(R.id.tv_tips)
    TextView tvTIps;

    @BindView(R.id.v_code)
    VerifyCodeView vCode;

    @Inject
    RegisterActivityPresenter presenter;


    public PersonInfoJson mPersonInfoJson;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_voice)
    TextView tvVoice;
    @BindView(R.id.ll_voice)
    LinearLayout llVoice;

    private String code = "";
    private String mobile = "";

    private LocalBroadcastManager broadcastManager;

    @Override
    protected boolean translucentStatusBar() {
        return true;
    }

    @OnClick({R.id.tv_tips, R.id.btnCode, R.id.ibBack, R.id.btnSubmit, R.id.ll_area})
    public void onClick(View v) {
        switch (v.getId()) {
            //用户协议
            case R.id.tv_tips:
                WebActivity.newInstance(this, Constants.CUSTOM_URL.REG_USER_CONTACT, getString(R.string.t42), false);
                break;
            //
            case R.id.btnCode:
                mobile = etMobile.getText().toString();
                if (!checkPhoneIsPass()) {
                    return;
                }
                btnCode.setEnabled(false);
                String zipCode = "";
                if (currentItem != -1 && list != null && list.size() != 0)
                    zipCode = list.get(currentItem).getZip_code();
                else
                    zipCode = "86";
                presenter.postCode(etMobile.getText().toString(), zipCode);
                break;
            //返回键
            case R.id.ibBack:
                finish();
                break;
            //提交
            case R.id.btnSubmit:
//                startActivity(new Intent(this,RecordLoginActivity.class));

                if (!checkPhoneIsPass()) {
                    return;
                }
                if (!checkCodeIsPass()) {
                    return;
                }
                presenter.reg(etMobile.getText().toString(), etCode.getText().toString());
                break;
            case R.id.ll_area:
                initAreaDialog();
                break;
        }
    }

    private boolean checkPhoneIsPass() {
        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(RegisterActivity.this, getString(R.string.input_phone_number), Toast.LENGTH_LONG).show();
            return false;
        } else {
            String zipCode = "";
            if (currentItem != -1 && list != null && list.size() != 0)
                zipCode = list.get(currentItem).getZip_code();
            else
                zipCode = "86";
            if (!StringUtils.isPhone(mobile,zipCode)) {
                Toast.makeText(RegisterActivity.this, getString(R.string.t39), Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    private boolean checkCodeIsPass() {
        if (StringUtils.isEmpty(code)) {
            showToast(getString(R.string.please_input_authc_code));
            return false;
        } else {
            if (code.length() != 4) {
                showToast(getString(R.string.t43));
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        DaggerRegisterActivityComponent.builder().appComponent(getAppComponent())
                .registerActivityModule(new RegisterActivityModule(this)).build()
                .inject(this);

        etCode = (EditText) vCode.findViewById(R.id.etCode);
        vCode.setOnTextChangedListener(new VerifyCodeView.OnTextChangedListener() {
            @Override
            public void onTextChanged(String code) {
                RegisterActivity.this.code = code;
                setSubmitButton();
            }
        });

        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mobile = s.toString();
                setSubmitButton();
                String zipCode = "";
                if (currentItem != -1 && list != null && list.size() != 0)
                    zipCode = list.get(currentItem).getZip_code();
                else
                    zipCode = "86";
                int num = 11;
                if ("86".equals(zipCode))
                    num = 11;
                else if ("852".equals(zipCode))
                    num = 8;

                if (mobile.length() == num) {
                    if (StringUtils.isEmpty(code)) {
                        vCode.findViewById(R.id.etCode).requestFocus();
                    }
                    FormatCouponInfo.setLeftRightToYellowWhite(btnCode);
                } else {
                    FormatCouponInfo.setLeftRightToGrayWhite(btnCode);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        broadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        tvTIps.setText(Html.fromHtml(getString(R.string.read_register)));

        StatusBarUtil.setTranslucentForImageView(RegisterActivity.this, Constants.STATUS_BAR_ALPHA.BAR_ALPHA, null);

        presenter.getRegionCode();

        llVoice.setVisibility(View.INVISIBLE);
        tvVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = etMobile.getText().toString();
                if (!checkPhoneIsPass()) {
                    return;
                }
                btnCode.setEnabled(false);
                String zipCode = "";
                if (currentItem != -1 && list != null && list.size() != 0)
                    zipCode = list.get(currentItem).getZip_code();
                else
                    zipCode = "86";
                presenter.postCode(etMobile.getText().toString(), zipCode,"VOICE");
            }
        });
    }

    public void showVoice(){
        String zipCode = "";
        if (currentItem != -1 && list != null && list.size() != 0)
            zipCode = list.get(currentItem).getZip_code();
        else
            zipCode = "86";
        if ("86".equals(zipCode)){
            llVoice.setVisibility(View.VISIBLE);
        }else {
            llVoice.setVisibility(View.INVISIBLE);
        }
    }


    private void setSubmitButton() {
        if (code == null || mobile == null) {
            return;
        }

        String zipCode = "";
        if (currentItem != -1 && list != null && list.size() != 0)
            zipCode = list.get(currentItem).getZip_code();
        else
            zipCode = "86";
        int num = 11;
        if ("86".equals(zipCode))
            num = 11;
        else if ("852".equals(zipCode))
            num = 8;

        if (code.length() == 4 && mobile.length() == num) {
            FormatCouponInfo.setViewToTransparent(btnSumit, false);
            hideKeyboard(etCode);
        } else {
            FormatCouponInfo.setViewToTransparent(btnSumit, true);
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
            btnCode.setText(getString(R.string.t36));
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

    List<RegionCodeBean> list;

    public void setAreaCode(List<RegionCodeBean> list) {
        this.list = list;
    }

    BottomDialog dialog;
    int currentItem = -1;

    private void initAreaDialog() {
        dialog = BottomDialog.create(getSupportFragmentManager()).setViewListener(new BottomDialog.ViewListener() {

            @Override
            public void bindView(View v) {
                final WheelView area = (WheelView) v.findViewById(R.id.phone_area);
                PhoneWheelAdapter adapter = new PhoneWheelAdapter(RegisterActivity.this, list);
                area.setViewAdapter(adapter);
                area.setVisibleItems(7);
                if (currentItem != -1)
                    area.setCurrentItem(currentItem);

                v.findViewById(R.id.set).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentItem = area.getCurrentItem();
                        if (list == null || list.size() == 0){
                            dialog.dismiss();
                            return;
                        }

                        RegionCodeBean bean = list.get(area.getCurrentItem());
                        tvArea.setText(bean.getRegion());
                        tvNumber.setText("+" + bean.getZip_code());
                        if (!"86".equals(bean.getZip_code())){
                            llVoice.setVisibility(View.INVISIBLE);
                        }

                        dialog.dismiss();
                    }
                });

                v.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        }).setLayoutRes(R.layout.view_phone_area)
                .setDimAmount(0.5f)
                .setCancelOutside(true)
                .setTag("PhoneDialog");
        dialog.show();
    }


//    private void regIM() {
//        if(getGlobalParams().getPersonInfoJson()!=null) {
//            BizDataRequest.requestRegisterIM(RegisterActivity.this, new BizDataRequest.OnRequestResult() {
//                @Override
//                public void onSuccess() {
//
//                    EMClient.getInstance().login(getGlobalParams().getPersonInfoJson().getUid(), getGlobalParams().getPersonInfoJson().getEasemobPwd(), new EMCallBack() {//回调
//                        @Override
//                        public void onSuccess() {
//                            // 登陆成功，保存用户昵称与头像URL
//                            SpUtil.putString(RegisterActivity.this, "name", getGlobalParams().getPersonInfoJson().getNick());
//                            SpUtil.putString(RegisterActivity.this, "logoUrl", getGlobalParams().getPersonInfoJson().getAvatar());
//
//                            // 将自己服务器返回的环信账号、昵称和头像URL设置到帮助类中。
//                            IMHelper.getInstance().getUserProfileManager().updateCurrentUserNickName(getGlobalParams().getPersonInfoJson().getNick());
//                            IMHelper.getInstance().getUserProfileManager().setCurrentUserAvatar(getGlobalParams().getPersonInfoJson().getAvatar());
//                            IMHelper.getInstance().setCurrentUserName(getGlobalParams().getPersonInfoJson().getUid()); // 环信Id
//
//                            EMClient.getInstance().groupManager().loadAllGroups();
//                            EMClient.getInstance().chatManager().loadAllConversations();
//                            getAllEaseContacts();
//                        }
//
//                        @Override
//                        public void onProgress(int progress, String status) {
//
//                        }
//
//                        @Override
//                        public void onError(int code, String message) {
//
//
//                        }
//                    });
//                }
//
//                @Override
//                public void onError(DcnException error) {
//
//                }
//            });
//        }
//    }

//    private void getAllEaseContacts(){
//        try {
//            IMHelper.getInstance().asyncFetchContactsFromServer(new EMValueCallBack<List<String>>() {
//                @Override
//                public void onSuccess(List<String> strings) {
//                    broadcastManager.sendBroadcast(new Intent(Constant.ACTION_CONTACT_CHANAGED));
//                }
//
//                @Override
//                public void onError(int i, String s) {
//
//                }
//            });
//
//
//        }catch (Exception e) {
//
//        }
//    }


//    @OnClick(R.id.tv_tips)
//    void onClickToRegContact(){
//        WebActivity.newInstance(this, Constants.CUSTOM_URL.REG_USER_CONTACT, "ATLAS 寰图用户服务协议", false);
//    }
//
//    @OnClick(R.id.btnCode)
//    void onPostCode() {
//        mobile = etMobile.getText().toString();
//        if(!checkPhoneIsPass()){
//            return;
//        }
//        btnCode.setEnabled(false);
//        postCode();
//    }
//
//    @OnClick(R.id.ibBack)
//    void onBack() {
//        RegisterActivity.this.finish();
//    }
//
//    @OnClick(R.id.btnSubmit)
//    void onReg() {
////        startActivity(new Intent(this,RecordLoginActivity.class));
//
//        if(!checkPhoneIsPass()){
//            return;
//        }
//        if(!checkCodeIsPass()){
//            return;
//        }
//        reg();
//    }
}
