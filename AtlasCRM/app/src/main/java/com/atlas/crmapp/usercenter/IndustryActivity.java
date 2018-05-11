package com.atlas.crmapp.usercenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.IndustryAdapter;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.IndustryJson;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.AppUtil;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.StringUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Leo on 2018/2/9.
 */

public class IndustryActivity extends BaseStatusActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    PersonInfoJson info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_);
        ButterKnife.bind(this);

        initView();
        getIndustry();
    }

    IndustryAdapter adapter;

    private void initView() {
        initToolbar();
        setTitle(getString(R.string.t62));
        setTopRightButton(getString(R.string.save), new OnClickListener() {
            @Override
            public void onClick() {
                setIndustry("industryId", adapter.getBean().getId() + "");
            }
        });
        info = (PersonInfoJson) getIntent().getSerializableExtra("info");
        LinearLayoutManager ll = new LinearLayoutManager(this);
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(ll);
        adapter = new IndustryAdapter(this);
        adapter.setName(getIntent() == null ? "" : getIntent().getStringExtra("industry"));
        recyclerView.setAdapter(adapter);
        adapter.setOnCheck(new IndustryAdapter.OnCheck() {
            @Override
            public void onCheck() {
                showOtherDialog1();
            }
        });
        if (info != null)
        adapter.setOther(info.getOtherIndustry());

    }

    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }

    private void getIndustry() {
        BizDataRequest.requestIndustry(this, new BizDataRequest.IndustryInfo() {
            @Override
            public void onSuccess(List<IndustryJson> list) {
                adapter.setList(list);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    private void setIndustry(final String key, final String values){
        HashMap params;
//        = new HashMap();
//        params.put(key, values);
        params = AppUtil.getInfoParams(info,key,values);
        BizDataRequest.requestModifyUserInfo(this, params, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                setResult(0x102, new Intent().putExtra("industry"
                        ,"otherIndustry".equals(key)? values:adapter.getBean().getName()
                ));
                finish();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    private void showOtherDialog1(){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.t63);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_industry_edit,null,false);
        final EditText ed = (EditText) v.findViewById(R.id.et_industry);
        builder.setView(v);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNeutralButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!StringUtils.isEmpty(ed.getText().toString())) {
                    adapter.setOther(ed.getText().toString());
                    setIndustry("otherIndustry", ed.getText().toString());
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
