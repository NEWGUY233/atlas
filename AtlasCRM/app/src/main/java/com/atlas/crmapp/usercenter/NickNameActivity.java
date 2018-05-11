package com.atlas.crmapp.usercenter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NickNameActivity extends BaseActivity {

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.etNickName)
    EditText etNickName;
    @BindView(R.id.tvText)
    TextView tvText;

    @OnClick(R.id.ibBack)
    void onBack() {
        this.finish();
    }

    @OnClick(R.id.tvText)
    void onEdit() {

        editNick(etNickName.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);
        ButterKnife.bind(this);
        tvText.setVisibility(View.VISIBLE);
        tvText.setText(getResources().getString(R.string.save));
        textViewTitle.setText(R.string.t92);
        String name = getIntent().getStringExtra("value");
        if(TextUtils.isEmpty(name)){
            name = "";
        }
        etNickName.setText(name);
    }

    private void editNick(final String nickName) {
        PersonInfoJson personInfoJson = getGlobalParams().getPersonInfoJson();
        personInfoJson.setNick(nickName);
        BizDataRequest.requestModifyUserInfo(NickNameActivity.this, personInfoJson, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                getGlobalParams().getPersonInfoJson().setNick(nickName);
                setResult(UserInfoActivity.NAME_RESUT_CODE);
                NickNameActivity.this.finish();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }
}
