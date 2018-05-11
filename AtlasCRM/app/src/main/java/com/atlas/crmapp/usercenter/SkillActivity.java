package com.atlas.crmapp.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.AppUtil;
import com.atlas.crmapp.util.StringUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/2/9.
 */

public class SkillActivity extends BaseActivity {

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.etSkill)
    EditText etSkill;
    @BindView(R.id.tvText)
    TextView tvText;
    @BindView(R.id.iv_clear)
    ImageView ivClear;

    PersonInfoJson info;
    @OnClick(R.id.ibBack)
    void onBack() {
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill);
        ButterKnife.bind(this);

        initView();
    }

    String skill = "";
    private void initView(){
        tvText.setVisibility(View.VISIBLE);
        tvText.setText(getResources().getString(R.string.save));
        textViewTitle.setText(R.string.s1);
        if (getIntent() != null)
            skill = getIntent().getStringExtra("skill");
            etSkill.setText(skill);
        info = (PersonInfoJson) getIntent().getSerializableExtra("info");
    }

    @OnClick({R.id.tvText, R.id.iv_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvText:
                saveSkill();
                break;
            case R.id.iv_clear:
                etSkill.setText("");
                break;
        }
    }

    private void saveSkill(){
        if (skill.equals(etSkill.getText().toString())) {
            finish();
            return;
        }

        if(StringUtils.isEmpty(etSkill.getText().toString())){
            showToast(getString(R.string.s2));
            return;
        }

        HashMap params;
//                = new HashMap();
        skill = etSkill.getText().toString();
//        params.put("skill",skill);
        params = AppUtil.getInfoParams(info,"skill",skill);
        BizDataRequest.requestModifyUserInfo(this, params, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                setResult(0x101, new Intent().putExtra("skill",skill));
                finish();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }
}
