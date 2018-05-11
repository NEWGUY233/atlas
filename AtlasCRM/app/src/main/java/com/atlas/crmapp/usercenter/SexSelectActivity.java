package com.atlas.crmapp.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SexSelectActivity extends BaseActivity {

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;
    @BindView(R.id.ibHome)
    View ibHome;
    @BindView(R.id.tvText)
    TextView tvText;
    @BindView(R.id.iv_wuman_ok)
    ImageView ivWuManOk;
    @BindView(R.id.iv_man_ok)
    ImageView ivManOk;

    private Integer sex;

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("sex", sex);
    }

    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        sex = bundle.getInt("sex");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sex_select);
        ButterKnife.bind(this);
        textViewTitle.setText(R.string.t100);
        ibHome.setVisibility(View.GONE);
        tvText.setVisibility(View.VISIBLE);
        tvText.setText(R.string.save);
        tvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != sex){

                    editCompany(sex);

                }
            }
        });

        if (getIntent() != null){
            if (getString(R.string.male).equals(getIntent().getStringExtra("sex"))){
                ivWuManOk.setVisibility(View.GONE);
                ivManOk.setVisibility(View.VISIBLE);
            }else if (getString(R.string.female).equals(getIntent().getStringExtra("sex"))){
                ivWuManOk.setVisibility(View.VISIBLE);
                ivManOk.setVisibility(View.GONE);
            }else {
                ivWuManOk.setVisibility(View.GONE);
                ivManOk.setVisibility(View.GONE);
            }
        }
    }

    @OnClick(R.id.ibBack)
    void onBack() {
        SexSelectActivity.this.finish();
    }


    public void onSelectSex(View view){
        sex = Integer.parseInt(view.getTag().toString());
        if (1 == sex){
            ivWuManOk.setVisibility(View.INVISIBLE);
            ivManOk.setVisibility(View.VISIBLE);
        }else {
            ivWuManOk.setVisibility(View.VISIBLE);
            ivManOk.setVisibility(View.INVISIBLE);
        }
    }


    private void editCompany(final int sex){
        final String sexStr = sex ==1 ? Constants.Sex.MALE : Constants.Sex.FEMALE;
        PersonInfoJson personInfoJson = getGlobalParams().getPersonInfoJson();
        personInfoJson.setGender(sexStr);
        BizDataRequest.requestModifyUserInfo(SexSelectActivity.this, personInfoJson, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                getGlobalParams().getPersonInfoJson().setGender(sexStr);
                Intent intent = new Intent();
                intent.putExtra(UserInfoActivity.SEX, sex);
                setResult(UserInfoActivity.SEX_RESULT, intent);
                SexSelectActivity.this.finish();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }
}
