package com.atlas.crmapp.usercenter;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.SpUtil;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.view.VerifyCodeView;
import com.jungly.gridpasswordview.GridPasswordView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.atlas.crmapp.R.drawable.hide_pws;
import static com.atlas.crmapp.R.drawable.show_pws;

public class  PayPasswordActivity extends BaseActivity {

    @BindView(R.id.btnCode)
    Button btnCode;

    @BindView(R.id.etCode)
    EditText etCode;

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.btn_submit)
    Button btnSubmit;

    @BindView(R.id.v_code)
    VerifyCodeView vCode;

    private String psw;//密码
    private String authCode;//验证码

    @BindView(R.id.iv_ear)
    ImageView ivEar;

    @OnClick(R.id.btnCode)
    void onPostCode() {
        btnCode.setEnabled(false);
        FormatCouponInfo.setLeftRightToGrayWhite(btnCode);
        timer.start();
        postCode();
    }

    @BindView(R.id.gpv_normal)
    GridPasswordView gpv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_password);
        ButterKnife.bind(this);
        textViewTitle.setText(getResources().getString(R.string.set_pay_password));
        etCode = (EditText) vCode.findViewById(R.id.etCode);
        gpv.setOnPasswordChangedListener(onPasswordChangedListener);
        vCode.setOnTextChangedListener(new VerifyCodeView.OnTextChangedListener() {
            @Override
            public void onTextChanged(String code) {
                authCode = code;
                setBtnSubmitColor(psw, authCode);
            }
        });
        ivEar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isShowPsw = (boolean) v.getTag();
                if(isShowPsw){
                    setPwsEar(false);
                }else{
                    setPwsEar(true);
                }
            }
        });
        setPwsEar(false);
    }

    private void setPwsEar(boolean isShowPsw){
        if(isShowPsw){
            ivEar.setTag(true);
            ivEar.setImageResource(show_pws);
            gpv.setPasswordVisibility(true);
        }else{
            ivEar.setTag(false);
            ivEar.setImageResource(hide_pws);
            gpv.setPasswordVisibility(false);
        }

    }

    private GridPasswordView.OnPasswordChangedListener onPasswordChangedListener = new GridPasswordView.OnPasswordChangedListener() {
        @Override
        public void onTextChanged(String psw) {
            PayPasswordActivity.this.psw = psw;
            if(psw.length() == 6){
                hideKeyboard(gpv);
            }
            setBtnSubmitColor(psw, authCode);
        }

        @Override
        public void onInputFinish(String psw) {

        }
    };

    //设置提交按钮变颜色
    private void setBtnSubmitColor(String psw ,String authCode){
        if(StringUtils.isNotEmpty(psw) && psw.length() == 6){
            btnCode.setClickable(true);
            FormatCouponInfo.setLeftRightToYellowWhite(btnCode);
        }else{
            btnCode.setClickable(false);
            FormatCouponInfo.setLeftRightToGrayWhite(btnCode);
        }

        if(!TextUtils.isEmpty(psw) && !TextUtils.isEmpty(authCode)){
            if(psw.length() == 6 && authCode.length() == 4){
                hideKeyboard(etCode);
                FormatCouponInfo.setViewToTransparent(btnSubmit, false);
            }else{
                FormatCouponInfo.setViewToTransparent(btnSubmit, true);
            }
        }else{
            FormatCouponInfo.setViewToTransparent(btnSubmit, true);
        }
    }


    @OnClick(R.id.ibBack)
    void onBack() {
        PayPasswordActivity.this.finish();
    }

    @OnClick(R.id.btn_submit)
    void onSubmit() {
        String pwd = gpv.getPassWord();
        String code = etCode.getText().toString().trim();
        if(pwd.length() != 6) {
            Toast.makeText(PayPasswordActivity.this, R.string.t93, Toast.LENGTH_LONG).show();
        }

        if(code.length() == 0) {
            Toast.makeText(PayPasswordActivity.this, R.string.t94, Toast.LENGTH_LONG).show();
            return;
        }

        BizDataRequest.requestUpdateUserAccountPassword(PayPasswordActivity.this, pwd, code, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                Toast.makeText(PayPasswordActivity.this, R.string.t95,Toast.LENGTH_LONG).show();
                PayPasswordActivity.this.finish();
            }

            @Override
            public void onError(DcnException error) {
                //Toast.makeText(PayPasswordActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void postCode(){
        BizDataRequest.requestSendSmsByUser(PayPasswordActivity.this, StringUtils.getBizCode(SpUtil.getString(this,SpUtil.PHONE,"")),
                SpUtil.getString(this,SpUtil.PHONE,""), new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                Toast.makeText(PayPasswordActivity.this,R.string.t32,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(DcnException error) {
                Toast.makeText(PayPasswordActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            btnCode.setText((getResources().getString(R.string.retry_send)+ millisUntilFinished / 1000) );
        }

        @Override
        public void onFinish() {
            FormatCouponInfo.setLeftRightToYellowWhite(btnCode);
            btnCode.setEnabled(true);
            btnCode.setText(getResources().getString(R.string.click_retry_get));
            cancel();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer!= null){
            timer.cancel();
        }
    }

    public void hideKeyboard(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

    }
}
