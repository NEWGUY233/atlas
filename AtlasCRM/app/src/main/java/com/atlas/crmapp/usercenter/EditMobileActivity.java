package com.atlas.crmapp.usercenter;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.adapter.wheel.PhoneWheelAdapter;
import com.atlas.crmapp.bean.RegionCodeBean;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.RegisterCommonUtils;
import com.atlas.crmapp.util.SpUtil;
import com.atlas.crmapp.view.wheel.WheelView;
import com.ta.utdid2.android.utils.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.shaohui.bottomdialog.BottomDialog;

public class EditMobileActivity extends BaseActivity {

    @BindView(R.id.etCode)
    EditText etCode;

    @BindView(R.id.btnCode)
    Button btnCode;

    @BindView(R.id.etMobile)
    EditText etMobile;

    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.btnSubmitLast)
    Button btnSubmitLast;

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.ll_area)
    LinearLayout llArea;
    @BindView(R.id.tv_voice)
    TextView tvVoice;
    @BindView(R.id.ll_voice)
    LinearLayout llVoice;

    @OnClick(R.id.ibBack)
    void onBack() {
        this.finish();
    }

    private PersonInfoJson mPersonInfoJson;

    @OnClick(R.id.btnCode)
    void onPostCode() {
        String mPhoneNo = etMobile.getText().toString();
        if (TextUtils.isEmpty(mPhoneNo)) {
            Toast.makeText(EditMobileActivity.this, getString(R.string.input_phone_number), Toast.LENGTH_LONG).show();
            return;
        }
        btnCode.setEnabled(false);
        timer.start();
        postCode();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mobile);
        ButterKnife.bind(this);
        textViewTitle.setText(R.string.t57);

        etCode.addTextChangedListener(textWatcher);

        etMobile.addTextChangedListener(textWatcher);

        llArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initAreaDialog();
            }
        });
        getRegionCode();
        initVoice();
    }

    private void initVoice(){
        llVoice.setVisibility(View.INVISIBLE);
        tvVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postCodeVoice();
            }
        });
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String code = etCode.getText().toString();
            String mobile = etMobile.getText().toString();
            if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(mobile) && code.length() == 4 && mobile.length() == 11) {
                btnSubmitLast.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.VISIBLE);
            } else {
                btnSubmitLast.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.GONE);
            }
        }
    };

    @OnClick(R.id.btnSubmit)
    void onReg() {
        reg();
    }


    private void postCode() {
        String zipCode = "";
        if (currentItem != -1 && list != null && list.size() != 0)
            zipCode = list.get(currentItem).getZip_code();
        else
            zipCode = "86";
        String mPhoneNo = etMobile.getText().toString();
        BizDataRequest.requestSendCode(EditMobileActivity.this, zipCode, mPhoneNo, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                Toast.makeText(EditMobileActivity.this, getString(R.string.t32), Toast.LENGTH_LONG).show();
                showVoice();
            }

            @Override
            public void onError(DcnException error) {

            }
        }, "MOBILE");
    }


    private void postCodeVoice() {
        String zipCode = "";
        if (currentItem != -1 && list != null && list.size() != 0)
            zipCode = list.get(currentItem).getZip_code();
        else
            zipCode = "86";
        String mPhoneNo = etMobile.getText().toString();
        BizDataRequest.requestSendCode(EditMobileActivity.this, zipCode, mPhoneNo, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                Toast.makeText(EditMobileActivity.this, getString(R.string.t32), Toast.LENGTH_LONG).show();
                showVoice();
            }

            @Override
            public void onError(DcnException error) {

            }
        }, "VOICE");
    }

    private void reg() {
        final String mMobile = etMobile.getText().toString();
        final String mCode = etCode.getText().toString();
        String zipCode = "";
        if (currentItem != -1 && list != null && list.size() != 0)
            zipCode = list.get(currentItem).getZip_code();
        else
            zipCode = "86";
        final String finalZipCode = zipCode;
        BizDataRequest.requestUpdateMobile(EditMobileActivity.this, zipCode, mMobile, mCode, new BizDataRequest.OnRequestValidCode() {
            @Override
            public void onSuccess(PersonInfoJson personInfoJson) {
               /* mPersonInfoJson = personInfoJson;
                auth(mMobile,mCode,personInfoJson.nick);*/
                showToast(getString(R.string.t58));
                SpUtil.putString(EditMobileActivity.this,SpUtil.PHONE,mMobile);
                SpUtil.putString(EditMobileActivity.this,SpUtil.AREA, finalZipCode);
                RegisterCommonUtils.logout(EditMobileActivity.this);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    private CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            btnCode.setText((millisUntilFinished / 1000) + getString(R.string.t35));
        }

        @Override
        public void onFinish() {
            btnCode.setEnabled(true);
            btnCode.setText(getString(R.string.t36));
        }
    };

    public void getRegionCode() {
        BizDataRequest.getRegionCode(this, new BizDataRequest.OnRequestRegionCode() {
            @Override
            public void onSuccess(List<RegionCodeBean> regionCodeBeanList) {
                setAreaCode(regionCodeBeanList);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
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
                PhoneWheelAdapter adapter = new PhoneWheelAdapter(EditMobileActivity.this, list);
                area.setViewAdapter(adapter);
                area.setVisibleItems(7);
                if (currentItem != -1)
                    area.setCurrentItem(currentItem);

                v.findViewById(R.id.set).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentItem = area.getCurrentItem();
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
}
