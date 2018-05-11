package com.atlas.crmapp.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.HabitsAdapter;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.HabitsJson;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.AppUtil;
import com.atlas.crmapp.util.StringUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/2/9.
 */

public class HabitActivity extends BaseStatusActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_complete_login)
    TextView tvCompleteLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }

    HabitsAdapter adapter;
    PersonInfoJson info;

    private void initView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new HabitsAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnCheck(new HabitsAdapter.OnCheck() {
            @Override
            public void onCheck(int count) {
                tvNumber.setText("（" + count + "/3）");
            }
        });

        habits = getIntent().getStringExtra("habits");
        if (!StringUtils.isEmpty(habits))
            tvNumber.setText("（" + habits.split("/").length + "/3）");
        info = (PersonInfoJson) getIntent().getSerializableExtra("info");
        getHabit();

        adapter.setOnCheck(new HabitsAdapter.OnCheck() {
            @Override
            public void onCheck(int count) {
                if (count <= 0){
                    tvCompleteLogin.setClickable(false);
                    tvCompleteLogin.setAlpha(0.5f);
                }else {
                    tvCompleteLogin.setClickable(true);
                    tvCompleteLogin.setAlpha(1);
                }
            }
        });
    }

    private void getHabit() {
        BizDataRequest.requestHabits(this, new BizDataRequest.HabitsInfo() {
            @Override
            public void onSuccess(List<HabitsJson> list) {
                adapter.setWidth(recyclerView.getWidth());
                for (String name : habits.split("/")){
                    for (HabitsJson bean : list){
                        if (bean.getName().equals(name))
                            bean.setSelected(true);
                    }
                }
                adapter.setList(list);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    @OnClick({R.id.tv_back, R.id.tv_complete_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_complete_login:
                updateHabits();
                break;
        }
    }

    String habits = "";
    private void updateHabits(){
        HashMap params ;
        String s = "";
        habits = "";
        for (HabitsJson bean : adapter.getList()) {
            if (bean.isSelected()) {
                s += bean.getId() + ",";
                habits += bean.getName() + "/";
            }
        }
        if ("".equals(s) || s == null){
            return;
//            params = AppUtil.getInfoParams(info, "interestIdList", new int[]{});
        }else {

            String[] str = s.substring(0, s.length() - 1).split(",");
            final int[] habit = new int[str.length];
            for (int i = 0; i < str.length; i++)
                habit[i] = Integer.valueOf(str[i]);

//        params.put("interestIdList", habit);
            params = AppUtil.getInfoParams(info, "interestIdList", habit);

        }
        BizDataRequest.requestModifyUserInfo(this, params, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                if (!StringUtils.isEmpty(habits))
                    habits = habits.substring(0,habits.length()-1);
                setResult(0x100, new Intent().putExtra("habits",habits));
                finish();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }
}
