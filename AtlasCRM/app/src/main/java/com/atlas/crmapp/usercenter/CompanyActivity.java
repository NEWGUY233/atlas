package com.atlas.crmapp.usercenter;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompanyActivity extends BaseActivity {

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.etCompany)
    EditText etCompany;

    @OnClick(R.id.ibBack)
    void onBack() {
        this.finish();
    }
    @BindView(R.id.tvText)
    TextView tvText;


    @OnClick(R.id.tvText)
    void onEdit() {
        editCompany(etCompany.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        ButterKnife.bind(this);
        tvText.setVisibility(View.VISIBLE);
        tvText.setText(R.string.save);
        textViewTitle.setText(getResources().getString(R.string.company));
        etCompany.setText(getIntent().getStringExtra("value"));

    }

    private void editCompany(final String mCompanyName){
        if(StringUtils.isEmpty(mCompanyName.trim())){
            showToast(getString(R.string.t49));
            return;
        }
        PersonInfoJson personInfoJson = getGlobalParams().getPersonInfoJson();
        personInfoJson.setCompany(mCompanyName);
        BizDataRequest.requestModifyUserInfo(CompanyActivity.this, personInfoJson, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                getGlobalParams().getPersonInfoJson().setCompany(mCompanyName);
                setResult(UserInfoActivity.COMPANY_RESUT_CODE);
                CompanyActivity.this.finish();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }
}
