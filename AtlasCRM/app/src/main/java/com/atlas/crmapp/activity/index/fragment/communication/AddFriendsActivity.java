package com.atlas.crmapp.activity.index.fragment.communication;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.tim.adapter.AddFriendsAdapter;
import com.atlas.crmapp.tim.model.FriendFuture;
import com.atlas.crmapp.tim.presenter.FriendshipManagerPresenter;
import com.atlas.crmapp.tim.viewfeatures.FriendshipMessageView;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMSNSSystemElem;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMFriendFutureItem;
import com.tencent.imsdk.ext.sns.TIMFriendFutureMeta;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;
import com.tencent.imsdk.ext.sns.TIMGetFriendFutureListSucc;
import com.tencent.imsdk.ext.sns.TIMPageDirectionType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/18.
 */

public class AddFriendsActivity extends BaseStatusActivity implements FriendshipMessageView {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.empty)
    View empty;

    AddFriendsAdapter adapter;

    private List<FriendFuture> list= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tim_activity_add_friends);
        ButterKnife.bind(this);

        initToolbar();
        setTitle(getString(R.string.contact));

        initView();
        initData();

    }

    private void initView(){
        setVerticalManager(recyclerView);
        adapter = new AddFriendsAdapter(this);

        recyclerView.setAdapter(adapter);
        adapter.setConversationList(list);
        empty.setVisibility(View.VISIBLE);
    }

    FriendshipManagerPresenter presenter;
    private void initData() {
        presenter = new FriendshipManagerPresenter(this);
        presenter.getFriendshipMessage();
    }


    @Override
    public void onGetFriendshipLastMessage(TIMFriendFutureItem message, long unreadCount) {

    }

    @Override
    public void onGetFriendshipMessage(List<TIMFriendFutureItem> message) {
        if (message != null && message.size() != 0){
            for (TIMFriendFutureItem item : message){
                list.add(new FriendFuture(item));
            }
            presenter.readFriendshipMessage(message.get(0).getAddTime());
        }
        if (adapter.getItemCount() > 0)
            empty.setVisibility(View.GONE);
        else
            empty.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();

    }
}
