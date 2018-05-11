package com.atlas.crmapp.activity.index.fragment.communication;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.bean.ChatDynamicBean;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.dagger.component.chat.DaggerChatDynamicActivityComponent;
import com.atlas.crmapp.dagger.module.chat.ChatDynamicActivityModule;
import com.atlas.crmapp.presenter.ChatDynamicActivityPresenter;
import com.atlas.crmapp.tim.adapter.ChatDynamicAdapter;
import com.atlas.crmapp.view.RefreshFootView;
import com.atlas.crmapp.view.RefreshHeaderView;
import com.liaoinstan.springview.widget.SpringView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/5/2.
 */

public class ChatDynamicActivity extends BaseStatusActivity {

    @Inject
    ChatDynamicActivityPresenter presenter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.springview)
    SpringView springView;
    private RefreshFootView refreshFootView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_recycler);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    ChatDynamicAdapter adapter;

    private void initView() {
        DaggerChatDynamicActivityComponent.builder().chatDynamicActivityModule(new ChatDynamicActivityModule(this))
                .build().inject(this);

        initToolbar();
        setTitle(getString(R.string.chat_dynamic));
        setTopRightButton("", R.mipmap.nav_icon_more, new OnClickListener() {
            @Override
            public void onClick() {

            }
        });

        setVerticalManager(recyclerView);
        adapter = new ChatDynamicAdapter(this);
        recyclerView.setAdapter(adapter);

        refreshFootView = new RefreshFootView(this);
        springView.setHeader(new RefreshHeaderView(this));
        springView.setFooter(refreshFootView);
        springView.setType(SpringView.Type.FOLLOW);

        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                presenter.getDynamicList(0);
            }

            @Override
            public void onLoadmore() {
                try {
                    presenter.getDynamicList(adapter.getList().get(adapter.getItemCount()-1).getCreateTime());
                }catch (Exception e){}
            }
        });
    }

    private void initData() {
        presenter.getDynamicList(0);
    }

    public void setData(ChatDynamicBean bean) {
        adapter.setList(bean.getRows());
        springView.onFinishFreshAndLoad();
    }

    public void addData(ChatDynamicBean bean){
        adapter.addList(bean.getRows());
        springView.onFinishFreshAndLoad();
    }
}
