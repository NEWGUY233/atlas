package com.atlas.crmapp.usercenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.adapter.IndustryAdapter;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.IndustryJson;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.AppUtil;
import com.atlas.crmapp.util.StringUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Leo on 2018/2/9.
 */

public class JobActivity extends BaseActivity {

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.etSkill)
    EditText etSkill;
    @BindView(R.id.tvText)
    TextView tvText;
    @BindView(R.id.iv_clear)
    ImageView ivClear;

    @OnClick(R.id.ibBack)
    void onBack() {
        this.finish();
    }

    PersonInfoJson info;

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
        textViewTitle.setText(R.string.t64);
        etSkill.setHint(R.string.t65);
        if (getIntent() != null)
            skill = getIntent().getStringExtra("job");
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

//        if(StringUtils.isEmpty(etSkill.getText().toString())){
//            showToast("请输入职位");
//            return;
//        }

        skill = etSkill.getText().toString();
        setInfo("job",skill);
    }

    private void setInfo(String key, final String values){
        HashMap params;
//        params.put(key, values);
        params = AppUtil.getInfoParams(info,key,values);
        BizDataRequest.requestModifyUserInfo(this, params, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                setResult(0x103, new Intent().putExtra("job",skill));
                finish();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }
}
