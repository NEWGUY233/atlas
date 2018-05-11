package com.atlas.crmapp.usercenter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.AppUtil;
import com.atlas.crmapp.util.StringUtils;
import com.orhanobut.logger.Logger;
import com.tencent.mid.api.MidCallback;
import com.tencent.stat.StatConfig;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyOpinionActivity extends BaseActivity {

    @BindView(R.id.et_content)
    EditText mEtContent;

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.tvText)
    TextView tvText;

    @OnClick(R.id.ibBack)
    void onBack() {
        this.finish();
    }

    private static final int MAX_AVAILABLE_TIMES = 3;

    @OnClick(R.id.tvText)
    void onSubmit(){
        String content = StringUtils.filterEmoji(mEtContent.getText().toString().trim(),"?");
        if(!TextUtils.isEmpty(content)) {
            if(!GlobalParams.getInstance().production){//更改地址测试专用
                if(content.contains("http://")){
                    GlobalParams.RequestUrl requestUrl = GlobalParams.getInstance().requestUrl;
                    requestUrl.baseUrl = content + "/";
                    requestUrl.setBaseUrl();
                    BizDataRequest.initBaseUrl();
                    GlobalParams.getInstance().saveGlobalParamsToCache();
                    showToast(getString(R.string.t79) + content);
                    return;
                }

                if(content.equals("显示token")){
                    mEtContent.setText("token:\n" + GlobalParams.getInstance().getAccessToken() + getString(R.string.t80) + GlobalParams.getInstance().requestUrl.APPSERVER);
                    return;
                }
                if(content.equals("mid")){
                    requestMidEntitys();
                    return;
                }
            }
            submitContent(content.trim());
        }else{
            Toast.makeText(MyOpinionActivity.this, R.string.t81,Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_opinion);
        ButterKnife.bind(this);
        textViewTitle.setText(R.string.t82);
//        tvText.setVisibility(View.VISIBLE);
        tvText.setVisibility(View.VISIBLE);

        tvText.setText(getResources().getString(R.string.submit));



        //requestMidEntitys();
    }


    private void requestMidEntitys(){
        String mid = StatConfig.getMid(this);
        Logger.d("mid---- " + mid );

        if(StringUtils.isNotEmpty(mid)){
            mEtContent.setText(mid);
        }

    }

    private MidCallback midCallback = new MidCallback() {
        @Override
        public void onSuccess(Object o) {

        }

        @Override
        public void onFail(int i, String s) {

        }
    };


    private void submitContent(String content){
        content = "版本号：" +  AppUtil.getVersionName(this) + "." + AppUtil.getVersionCode(this)+ "   " + content ;
        if(GlobalParams.getInstance().isLogin()){
            BizDataRequest.requestFeedback(MyOpinionActivity.this, content, new BizDataRequest.OnRequestResult() {
                @Override
                public void onSuccess() {
                    Toast.makeText(MyOpinionActivity.this, R.string.t83,Toast.LENGTH_LONG).show();
                    MyOpinionActivity.this.finish();
                }

                @Override
                public void onError(DcnException error) {

                }
            });
        }else {
            BizDataRequest.requestFeedbackNotLogin(MyOpinionActivity.this, content, new BizDataRequest.OnRequestResult() {
                @Override
                public void onSuccess() {
                    Toast.makeText(MyOpinionActivity.this,R.string.t83,Toast.LENGTH_LONG).show();
                    MyOpinionActivity.this.finish();
                }

                @Override
                public void onError(DcnException error) {

                }
            });
        }
    }


    private void append(String msg) {
        //mEtContent.append(msg);
    }



}
