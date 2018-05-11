package com.atlas.crmapp.activity.index.fragment.communication;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.bean.ChatNoticeBean;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.dagger.component.chat.DaggerChatNoticeActivityComponent;
import com.atlas.crmapp.dagger.module.chat.ChatNoticeActivityModule;
import com.atlas.crmapp.db.hepler.PushMsgHepler;
import com.atlas.crmapp.db.model.PushMsg;
import com.atlas.crmapp.presenter.ChatNoticeActivityPresenter;
import com.atlas.crmapp.tim.adapter.ChatNoticeAdapter;
import com.atlas.crmapp.view.RefreshFootView;
import com.atlas.crmapp.view.RefreshHeaderView;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/5/3.
 */

public class ChatNoticeActivity extends BaseStatusActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.springview)
    SpringView springView;
    private RefreshFootView refreshFootView;

    @Inject
    ChatNoticeActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_recycler);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    ChatNoticeAdapter adapter;
    String title;
    String id;
    private void initView() {
        DaggerChatNoticeActivityComponent.builder()
                .chatNoticeActivityModule(new ChatNoticeActivityModule(this)).build()
                .inject(this);

        initToolbar();
        title = getIntent().getStringExtra("title");
        id = getIntent().getStringExtra("identify");
        setTitle(title);
        setTopRightButton("", R.mipmap.nav_icon_more, new OnClickListener() {
            @Override
            public void onClick() {

            }
        });

        setVerticalManager(recyclerView);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.bg_gray));
        adapter = new ChatNoticeAdapter(this);
        recyclerView.setAdapter(adapter);

        refreshFootView = new RefreshFootView(this);
        springView.setHeader(new RefreshHeaderView(this));
        springView.setFooter(refreshFootView);
        springView.setType(SpringView.Type.FOLLOW);

        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                initData();
            }

            @Override
            public void onLoadmore() {
                initData();
            }
        });
    }

    int page = 0;
    private void initData() {
        presenter.getNotice(page,id);
    }

    public void setData(ChatNoticeBean bean){
        adapter.setList(bean.getRows());
        springView.onFinishFreshAndLoad();
        ++page;
    }

    public void addData(ChatNoticeBean bean){
        adapter.addList(bean.getRows());
        springView.onFinishFreshAndLoad();
        ++page;
    }



//        List<PushMsg> list =  PushMsgHepler.getAllMsg();
//        list = initList(list);
//        adapter.setList(list);
//    private List<PushMsg> initList(List<PushMsg> list){
//        if (list == null)
//            return list;
//
//        List<PushMsg> msgs = new ArrayList<>();
//
//        String type = "";
//        for (PushMsg msg : list){
//            type = msg.actionType;
//            if (!"REAL_MSG".equals(msg.getType()))
//                continue;
//
//            if (getString(R.string.chat_sports).equals(title) && PushMsgHepler.TYPE_FITNESS.equals(type)){
//                msgs.add(msg);
//            }
//
//            if (getString(R.string.chat_workplace).equals(title) &&
//                    (PushMsgHepler.TYPE_WORKPLACE_PRINT.equals(type) || PushMsgHepler.TYPE_WORKPLACE_VISITOR.equals(type))){
//                msgs.add(msg);
//            }
//
//            if (getString(R.string.chat_kitchen).equals(title) && PushMsgHepler.TYPE_KITCHEN.equals(type)){
//                msgs.add(msg);
//            }
//
//            if (getString(R.string.chat_notice).equals(title) &&
//                    (PushMsgHepler.TYPE_NORMAL.equals(type)|| PushMsgHepler.TYPE_COUPON_BIND.equals(type)
//                            || PushMsgHepler.TYPE_BONUSPOINTS.equals(type))){
//                msgs.add(msg);
//            }
//
//        }
//        return msgs;
//    }
}
