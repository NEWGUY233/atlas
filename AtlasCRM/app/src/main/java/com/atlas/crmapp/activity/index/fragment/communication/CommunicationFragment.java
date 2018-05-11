package com.atlas.crmapp.activity.index.fragment.communication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.IndexActivity;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.db.hepler.ChatCountHelper;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.tim.adapter.ConversationAdapter;
import com.atlas.crmapp.tim.model.Conversation;
import com.atlas.crmapp.tim.model.CustomMessage;
import com.atlas.crmapp.tim.model.FriendshipInfo;
import com.atlas.crmapp.tim.model.MessageFactory;
import com.atlas.crmapp.tim.model.NomalConversation;
import com.atlas.crmapp.tim.model.SystemConversation;
import com.atlas.crmapp.tim.model.SystemMessage;
import com.atlas.crmapp.tim.presenter.ConversationPresenter;
import com.atlas.crmapp.tim.viewfeatures.ConversationView;
import com.atlas.crmapp.tim.viewfeatures.FriendshipMessageView;
import com.atlas.crmapp.tim.viewfeatures.GroupManageMessageView;
import com.atlas.crmapp.util.AppUtil;
import com.atlas.crmapp.util.ContextUtil;
import com.atlas.crmapp.util.RegisterCommonUtils;
import com.atlas.crmapp.util.SpUtil;
import com.atlas.crmapp.util.StringUtils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMGroupPendencyItem;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.tencent.imsdk.ext.sns.TIMDelFriendType;
import com.tencent.imsdk.ext.sns.TIMFriendFutureItem;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/3/15.
 */

public class CommunicationFragment extends BaseFragment implements ConversationView, FriendshipMessageView, GroupManageMessageView {
    @BindView(R.id.list_view)
    RecyclerView listView;
    Unbinder unbinder;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_index_communicaiton;
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {

    }

    ConversationPresenter presenter;
    private List<Conversation> conversationList = new LinkedList<>();
    private ConversationAdapter adapter;

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {

    }

    boolean isLogin = false;
    private void initData(){
        isLogin = GlobalParams.getInstance().isLogin();
        setData();
    }

    String id = "";
    private void setData(){
        if (getActivity() instanceof IndexActivity){
            ((IndexActivity) getActivity()).initChatMsg();
        }

        if (conversationList != null)
            conversationList.clear();
        AppUtil.initConversationList(conversationList);
        FriendshipInfo.getInstance().refresh();
        presenter = new ConversationPresenter(this);
        adapter = new ConversationAdapter(getContext());
        LinearLayoutManager ll = new LinearLayoutManager(getContext());
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(ll);
        listView.setAdapter(adapter);
        adapter.setConversationList(conversationList);
//        presenter.getConversation();

        id = TIMManager.getInstance().getLoginUser();
        if (isLogin == true && SpUtil.getLong(context,SpUtil.ID,0) != 0
                && !String.valueOf(SpUtil.getLong(context,SpUtil.ID,0)).equals(id)){
            getTimSig();
        }

    }


    //View Features
    @Override
    public void initView(List<TIMConversation> conversationList) {
        Log.i("CommunicationFragment", "initView");
        this.conversationList.clear();
        AppUtil.initConversationList(this.conversationList);
        for (TIMConversation item:conversationList){
            switch (item.getType()){
                case C2C:
                case Group:
                    deleteSame(item.getPeer());
                    this.conversationList.add(new NomalConversation(item));
                    break;
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateMessage(TIMMessage message) {

        FriendshipInfo.getInstance().refresh();
        if (id != null && !id.equals(TIMManager.getInstance().getLoginUser())){
            conversationList.clear();
            AppUtil.initConversationList(this.conversationList);
            id = TIMManager.getInstance().getLoginUser();
        }


        if (message == null) {
            adapter.notifyDataSetChanged();
            return;
        }
//        if (message.getConversation().getType() == TIMConversationType.System){
//            groupManagerPresenter.getGroupManageLastMessage();
//            return;
//        }
        deleteSame(message.getSender());

        if (MessageFactory.getMessage(message) == null) return;
        if (MessageFactory.getMessage(message) instanceof CustomMessage) return;


        if (MessageFactory.getMessage(message) instanceof SystemMessage){
            if (conversationList.get(0) instanceof SystemConversation)
                ((SystemConversation)conversationList.get(0)).setConversation(message.getConversation());
            adapter.notifyDataSetChanged();
            Log.i("CommunicationFragment","SystemConversation");
            countUnreadMsg();
            return;
        }

        NomalConversation conversation = new NomalConversation(message.getConversation());
        Iterator<Conversation> iterator = conversationList.iterator();
        while (iterator.hasNext()) {
            Conversation c = iterator.next();
            if (conversation.equals(c)) {
                conversation = (NomalConversation) c;
                iterator.remove();
                break;
            }
        }
        conversation.setLastMessage(MessageFactory.getMessage(message));
        conversationList.add(conversation);
        Collections.sort(conversationList);
        adapter.setConversationList(conversationList);
        Log.i("CommunicationFragment", "updateMessage");

        long num = new TIMConversationExt(message.getConversation()).getUnreadMessageNum();
        if (num > 0){
            Log.i("ChatCountHelperupdate", "updateMessage num = " + num);
            ChatCountHelper.update(conversation.getIdentify(),num);
        }

        countUnreadMsg();
    }

    @Override
    public void updateFriendshipMessage() {
        Log.i("CommunicationFragment","updateFriendshipMessage");
    }

    @Override
    public void removeConversation(String identify) {
        Log.i("CommunicationFragment","removeConversation");
        TIMManagerExt.getInstance().deleteConversationAndLocalMsgs(TIMConversationType.C2C,identify);
        if (presenter != null )
            presenter.getConversation();
    }

    @Override
    public void updateGroupInfo(TIMGroupCacheInfo info) {
        Log.i("CommunicationFragment","updateGroupInfo");
    }

    @Override
    public void refresh() {
        Collections.sort(conversationList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onGetGroupManageLastMessage(TIMGroupPendencyItem message, long unreadCount) {

    }

    @Override
    public void onGetGroupManageMessage(List<TIMGroupPendencyItem> message) {

    }

    @Override
    public void onGetFriendshipLastMessage(TIMFriendFutureItem message, long unreadCount) {
        Log.i("CommunicationFragment","onGetFriendshipLastMessage");
    }

    @Override
    public void onGetFriendshipMessage(List<TIMFriendFutureItem> message) {
        Log.i("CommunicationFragment","onGetFriendshipMessage");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        initData();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.i("loginCommunication","getGlobalParams long? =" + getGlobalParams().isLogin() + " ; id = " + TIMManager.getInstance().getLoginUser() + " ; info = " + SpUtil.getLong(context,SpUtil.ID,0));
        if (!getGlobalParams().isLogin()) {
            conversationList.clear();
            AppUtil.initConversationList(conversationList);
            adapter.notifyDataSetChanged();
        }

        if (getGlobalParams().isLogin()
                && SpUtil.getLong(context,SpUtil.ID,0) != 0
                && !String.valueOf(SpUtil.getLong(context,SpUtil.ID,0)).equals(id))
            getTimSig();

        FriendshipInfo.getInstance().refresh();
        if (adapter != null)
            adapter.notifyDataSetChanged();
        super.onHiddenChanged(hidden);
    }

    private void getTimSig(){
        BizDataRequest.getTimSig(getActivity(), String.valueOf(getGlobalParams().getPersonInfoJson().getId()), new BizDataRequest.OnRequestTimSig() {
            @Override
            public void onSuccess(String string) {
                loginTim(string);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    private void loginTim(String sig){
        String count = String.valueOf(getGlobalParams().getPersonInfoJson().getId());
        String psw = sig.replaceAll("\"","");
        SpUtil.putString(ContextUtil.getUtil().getContext(),SpUtil.TIM,count);
        TIMManager.getInstance().login(count,psw,new TIMCallBack(){

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {
                FriendshipInfo.getInstance().refresh();
                SpUtil.putBoolean(context, "isLogin", true);
            }
        });
    }

    private void deleteSame(String identify){

        if ("admin".equals(identify)) {
            checkId(ContextUtil.getUtil().getContext().getString(R.string.chat_dynamic));
            return;
        }
        if ("sport_admin".equals(identify)) {
            checkId(ContextUtil.getUtil().getContext().getString(R.string.chat_sports));
            return;
        }
        if ("workplace_admin".equals(identify)) {
            checkId(ContextUtil.getUtil().getContext().getString(R.string.chat_workplace));
            return;
        }
        if ("kitchen_admin".equals(identify)) {
            checkId(ContextUtil.getUtil().getContext().getString(R.string.chat_kitchen));
            return;
        }
        if ("notice_admin".equals(identify)) {
            checkId(ContextUtil.getUtil().getContext().getString(R.string.chat_notice));
            return;
        }
    }

    private void checkId(String name){
        if (conversationList == null || name == null)return;
        for (int i = 0 ; i < conversationList.size() ; ++i ){
            if (conversationList.get(i) instanceof  SystemConversation && name.equals(conversationList.get(i).getName())) {
                conversationList.remove(i);
                return;
            }
        }
    }

    private void deleteItem(String name){
        if (conversationList == null || name == null)return;
        for (int i = 0 ; i < conversationList.size() ; ++i ){
            if (name.equals(conversationList.get(i).getName())) {
                conversationList.remove(i);
                return;
            }
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onPause() {
        super.onPause();
        FriendshipInfo.getInstance().refresh();
    }

    public void onRestart(){
//        if (presenter != null )
//            presenter.getConversation();
        countUnreadMsg();
    }


    private onMSGChanged onChanged;

    public void setOnChanged(onMSGChanged onChanged) {
        this.onChanged = onChanged;
    }

    public interface onMSGChanged{
        void onChanged(long number);
    }

    private void countUnreadMsg(){
        if (onChanged == null || conversationList == null) {
            return;
        }
        long unread = 0;
        for (Conversation c : conversationList)
            unread += c.getUnreadNum();
        onChanged.onChanged(unread);
    }
}
