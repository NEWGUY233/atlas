package com.atlas.crmapp.usercenter;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.LinearLayout;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.CorporationMemberAdapter;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.CompanyMemberJson;
import com.atlas.crmapp.model.CompanyMembersJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.view.CorporationPersonView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CorporationMemberActivity extends BaseStatusActivity {

    @BindView(R.id.ll_person)
    LinearLayout llPersons;

    private ArrayList<CompanyMemberJson> members = new ArrayList<CompanyMemberJson>();
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporation_member);
        String companyName = getIntent().getStringExtra("companyName");
        setTitle(companyName== null?getString(R.string.c_yg): companyName);
        id = getIntent().getLongExtra("id", 0);
        prepareActivityData();
    }

    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
        BizDataRequest.requestGetCompanyMembers(CorporationMemberActivity.this, id, statusLayout, new BizDataRequest.OnCompanyMembersRequestResult() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(CompanyMembersJson companyMembersJson) {
                List<CompanyMemberJson> rows = companyMembersJson.rows;
                setList(rows);
                for(CompanyMemberJson memberJson : members){
                    CorporationPersonView personView = new CorporationPersonView(CorporationMemberActivity.this);
                    personView.updateView(memberJson);
                    llPersons.addView(personView);
                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareActivityData();
    }

    public void setList(List<CompanyMemberJson> rows) {
        members.clear();
        List<CompanyMemberJson> owners = new ArrayList<CompanyMemberJson>();
        List<CompanyMemberJson> authorizeds = new ArrayList<CompanyMemberJson>();
        List<CompanyMemberJson> commons = new ArrayList<CompanyMemberJson>();
        for(CompanyMemberJson row : rows) {
            if(row.type.equals("OWNER")) {
                owners.add(row);
            } else if(row.type.equals("AUTHORIZED")) {
                authorizeds.add(row);
            } else {
                commons.add(row);
            }
        }
        members.addAll(owners);
        members.addAll(authorizeds);
        members.addAll(commons);
    }

}
