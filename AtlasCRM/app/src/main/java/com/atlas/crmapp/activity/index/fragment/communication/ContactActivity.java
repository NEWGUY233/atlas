package com.atlas.crmapp.activity.index.fragment.communication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.tim.model.FriendProfile;
import com.atlas.crmapp.tim.model.FriendshipInfo;
import com.atlas.crmapp.tim.utils.Cn2Spell;
import com.atlas.crmapp.util.GlideUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.tencent.imsdk.ext.sns.TIMDelFriendType;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/18.
 */

public class ContactActivity extends BaseStatusActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_recycler);
        ButterKnife.bind(this);


        initView();
        initData();
    }

    View headView;
    private void initView() {
        initToolbar();
        setTitle(getString(R.string.contact));

        setVerticalManager(recyclerView);
        headView = LayoutInflater.from(this).inflate(R.layout.tim_include_contact,null);
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AddFriendsActivity.class);
            }
        });


        adapter = new BaseQuickAdapter<FriendProfile, BaseViewHolder>(R.layout.tim_item_contact) {
            @Override
            protected void convert(BaseViewHolder helper, final FriendProfile item) {
                String c = Cn2Spell.getPinYin(item.getName()).substring(0, 1).toUpperCase();
                c = compareChar(c);
                ((TextView)helper.getView(R.id.tv_char)).setText(c);
                if (list.indexOf(item) == 0){
                    ((TextView)helper.getView(R.id.tv_char)).setVisibility(View.VISIBLE);
                }else {
                    String compare = Cn2Spell.getPinYin(list.get(list.indexOf(item) - 1).getName()).substring(0, 1).toUpperCase();
                    compare = compareChar(compare);
                    if (c.equals(compare))
                        ((TextView)helper.getView(R.id.tv_char)).setVisibility(View.GONE);
                    else
                        ((TextView)helper.getView(R.id.tv_char)).setVisibility(View.VISIBLE);
                }

                ((TextView)helper.getView(R.id.name)).setText(item.getName());
                Glide.with(ContactActivity.this).load(item.getAvatarUrl())
                        .apply(new RequestOptions().error(R.mipmap.icon_informationheard)).into((ImageView) helper.getView(R.id.icon));

                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChatActivity.navToChat(getContext(),item.getIdentify(), TIMConversationType.C2C);
                    }
                });

                helper.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showDelete(item);

                        return false;
                    }
                });
            }
        };
        list = new ArrayList<>();
        recyclerView.setAdapter(adapter);
        adapter.setNewData(list);
        adapter.addHeaderView(headView);

    }

    Map<String, List<FriendProfile>> friends;
    List<FriendProfile> list;
    BaseQuickAdapter adapter;
    private void initData() {
        friends = FriendshipInfo.getInstance().getFriends();
        list.clear();
        for (String key : friends.keySet()){
            for (FriendProfile profile : friends.get(key)){
                list.add(profile);
            }
        }
        Collections.sort(list);
        adapter.notifyDataSetChanged();

    }

    private String compareChar(String str){
        if (!str.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
            str = "#";
        }
        return str;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    private void showDelete(final FriendProfile item){

        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        dialog.setContentView(R.layout.dialog_delete);
        ((TextView)dialog.findViewById(R.id.title)).setText(getString(R.string.delete_friends));
        dialog.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                TIMFriendshipManagerExt.DeleteFriendParam param = new TIMFriendshipManagerExt.DeleteFriendParam();
                param.setType(TIMDelFriendType.TIM_FRIEND_DEL_BOTH);
                List<String> list = new ArrayList<>();
                list.add(item.getIdentify());
                param.setUsers(list);
                TIMManagerExt.getInstance().deleteConversationAndLocalMsgs(TIMConversationType.C2C,item.getIdentify());
                TIMFriendshipManagerExt.getInstance().delFriend(param, new TIMValueCallBack<List<TIMFriendResult>>() {
                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onSuccess(List<TIMFriendResult> timFriendResults) {
                        initData();
                    }
                });
            }
        });

        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }
}
