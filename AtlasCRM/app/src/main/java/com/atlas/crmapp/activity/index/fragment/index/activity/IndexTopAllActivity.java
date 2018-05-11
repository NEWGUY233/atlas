package com.atlas.crmapp.activity.index.fragment.index.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.index.adapter.IndexTopAllAdapter;
import com.atlas.crmapp.bean.IndexTopBean;
import com.atlas.crmapp.common.activity.BaseStatusActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/19.
 */

public class IndexTopAllActivity extends BaseStatusActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    IndexTopAllAdapter allAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_top_all_activity);
        ButterKnife.bind(this);

        initToolbar();
        setTitle(getResources().getString(R.string.index_top_all));

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        allAdapter = new IndexTopAllAdapter(this);
        recyclerView.setAdapter(allAdapter);
        allAdapter.setList(initItem());

    }

    private List<IndexTopBean> initItem(){
        List<IndexTopBean> list = new ArrayList<>();

        IndexTopBean bean = new IndexTopBean();
        bean.setName(getContext().getString(R.string.index_top_bluetooth));
        bean.setId(R.mipmap.subnav_icon_bluetoothdoor);
        list.add(bean);

        bean = new IndexTopBean();
        bean.setName(getContext().getString(R.string.index_top_food));
        bean.setId(R.mipmap.subnav_icon_coffee);
        list.add(bean);

        bean = new IndexTopBean();
        bean.setName(getContext().getString(R.string.index_top_class));
        bean.setId(R.mipmap.subnav_icon_class);
        list.add(bean);

        bean = new IndexTopBean();
        bean.setName(getContext().getString(R.string.index_top_meeting));
        bean.setId(R.mipmap.subnav_icon_meeting);
        list.add(bean);

        bean = new IndexTopBean();
        bean.setName(getContext().getString(R.string.index_top_yhq));
        bean.setId(R.mipmap.subnav_icon_coupon);
        list.add(bean);

        bean = new IndexTopBean();
        bean.setName(getContext().getString(R.string.index_top_invite));
        bean.setId(R.mipmap.subnav_icon_visit);
        list.add(bean);

        bean = new IndexTopBean();
        bean.setName(getContext().getString(R.string.index_top_money));
        bean.setId(R.mipmap.subnav_icon_rechange);
        list.add(bean);

//        bean = new IndexTopBean();
//        bean.setName(getContext().getString(R.string.index_top_all));
//        bean.setId(R.mipmap.subnav_icon_more);
//        list.add(bean);

        return list;
    }
}
