package com.atlas.crmapp.ecosphere;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.ParticipantsAdapter;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.ActivityUserJson;
import com.atlas.crmapp.model.ActivityUsersJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.usercenter.UserCardActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by tabzhou on 01/06/2017.
 */

public class ParticipantsActivity extends BaseStatusActivity {


    @BindView(R.id.generic_recyclerview)
    RecyclerView recyclerView;
    private ParticipantsAdapter adapter;
    private List<ActivityUserJson> dataSource = new ArrayList<ActivityUserJson>();
    public long activityId = 0; //活动ID

    public final static String EXTRA_KEY_ACTIVITY_ID = "activityId";
    public static void startActivity(Context fromActivity,long activityId) {
        Intent intent = new Intent(fromActivity,ParticipantsActivity.class);
        intent.putExtra(EXTRA_KEY_ACTIVITY_ID,activityId);
        fromActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_titile_list);
        setTitle(getString(R.string.t17));
        activityId = getIntent().getLongExtra(EXTRA_KEY_ACTIVITY_ID,0);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ParticipantsAdapter(this,dataSource);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(!getGlobalParams().isLogin()){
                    showAskLoginDialog();
                    return;
                }
                String uid = dataSource.get(position).appUser.getUid();
                Intent intent = new Intent(ParticipantsActivity.this, UserCardActivity.class);
                intent.putExtra(UserCardActivity.KEY_UID, uid);
                startActivity(intent);
            }
        });
        prepareActivityData();
    }


    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
        BizDataRequest.requestGetActivityUsers(ParticipantsActivity.this, 0, 500, activityId, false, statusLayout, new BizDataRequest.OnActivityUsers() {
                @Override
                public void onSuccess(ActivityUsersJson activityUsersJson) {
                    dataSource.clear();
                    dataSource.addAll(activityUsersJson.rows);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(DcnException error) {
                    Toast.makeText(ParticipantsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareActivityData();
    }
}
