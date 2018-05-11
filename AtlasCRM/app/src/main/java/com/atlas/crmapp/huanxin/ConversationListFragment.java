package com.atlas.crmapp.huanxin;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.huanxin.db.InviteMessgeDao;
import com.atlas.crmapp.register.RegisterActivity;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.widget.EaseConversationList;
import com.hyphenate.util.NetUtils;
import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 生态圈 聊天  界面
 */
public class ConversationListFragment extends EaseConversationListFragment{

    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;

    private View errorView;
    private TextView errorText;

    protected BaseActivity mBaseActivity;

    private Unbinder unbinder;
    private InviteMessgeDao inviteMessgeDao;


    private View topView ;//联系人
    private View redDot ;
    private Intent intentToContentList;
    private boolean showRedDot;

    private static ConversationListFragment fragment;
    public static ConversationListFragment newInstance() {
        if (fragment == null)
            fragment = new ConversationListFragment();
        return fragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mBaseActivity = (BaseActivity) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        clearSearch.setVisibility(View.GONE);
        titleBar.setVisibility(View.GONE);
        errorView = (LinearLayout) View.inflate(getActivity(),R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
        errorView.setVisibility(View.GONE);
        if(inviteMessgeDao == null){
            inviteMessgeDao = new InviteMessgeDao(getActivity());
        }
        titleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        setICanAddTopViewListener();//addHeaderView()调用在setAdapter()之后，并且该代码运行在Android4.3之前的系统版本。http://blog.csdn.net/mtt1987/article/details/38535249
        registerBroadcastReceiver();
    }
    
    @Override
    protected void setUpView() {
        super.setUpView();
        // register context menu
        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Logger.d("position ---" + position);
                EMConversation conversation = conversationListView.getItem(position - 1);// 由于添加了headerview 所以必须-1
                if(conversation == null){
                    return ;
                }
                String username = conversation.conversationId();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    // start chat acitivity
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    if(conversation.isGroup()){
                        if(conversation.getType() == EMConversationType.ChatRoom){
                            // it's group chat
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                        }else{
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                        }
                        
                    }
                    // it's single chat
                    intent.putExtra(Constant.EXTRA_USER_ID, username);

                    if(GlobalParams.getInstance().isLogin()){
                        startActivity(intent);
                    }else{
                        showAskLoginDialog();
                    }

                }

            }
        });
        //red packet code : 红包回执消息在会话列表最后一条消息的展示
//        conversationListView.setConversationListHelper(new EaseConversationListHelper() {
//            @Override
//            public String onSetItemSecondaryText(EMMessage lastMessage) {
//                if (lastMessage.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, false)) {
//                    String sendNick = lastMessage.getStringAttribute(RPConstant.EXTRA_RED_PACKET_SENDER_NAME, "");
//                    String receiveNick = lastMessage.getStringAttribute(RPConstant.EXTRA_RED_PACKET_RECEIVER_NAME, "");
//                    String msg;
//                    if (lastMessage.direct() == EMMessage.Direct.RECEIVE) {
//                        msg = String.format(getResources().getString(R.string.msg_someone_take_red_packet), receiveNick);
//                    } else {
//                        if (sendNick.equals(receiveNick)) {
//                            msg = getResources().getString(R.string.msg_take_red_packet);
//                        } else {
//                            msg = String.format(getResources().getString(R.string.msg_take_someone_red_packet), sendNick);
//                        }
//                    }
//                    return msg;
//                }
//                return null;
//            }
//        });
        super.setUpView();
        //end of red packet code
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())){
            //无需显示没有连接到服务器到提示
         //errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            errorView.setVisibility(View.VISIBLE);
          errorText.setText(R.string.the_current_network);
        }
    }
    
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu); 
    }

    //删除会话
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
    	EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position-1);
    	if (tobeDeleteCons == null) {
    	    return true;
    	}
        if(tobeDeleteCons.getType() == EMConversationType.GroupChat){
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.conversationId());
        }
        try {
            // delete conversation
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.conversationId(), deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.conversationId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //refresh();
        conversationList.clear();
        conversationList.addAll(loadConversationList());
        conversationListView.refresh();

        // update unread count
//        ((MainActivity) getActivity()).updateUnreadLabel();
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBroadcastReceiver();
    }

    @Override
    public void onResume(){
        super.onResume();
        broadcastManager.sendBroadcast(new Intent(Constant.NEW_MESSAGE));
    }


    private void setICanAddTopViewListener(){
        conversationListView.setICanAddTopView(new EaseConversationList.ICanAddTopView() {
            @Override
            public void addTopView() {
                if(topView == null){
                    topView  = LayoutInflater.from(getActivity()).inflate(R.layout.view_top_contact, null, true);
                    conversationListView.addHeaderView(topView);
                    topView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(GlobalParams.getInstance().isLogin()){
                                getActivity().startActivity(new Intent(getActivity(), ContentListActivity.class));//顶部跳转至于联系
                            }else{
                                showAskLoginDialog();
                            }
                        }
                    });
                }
                redDot = topView.findViewById(R.id.unread_msg_number);
            }
        });
    }

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.NEW_MESSAGE);
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                refresh();
                setRedDot();
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private void unregisterBroadcastReceiver(){
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    private void setRedDot(){
        boolean showRedDot = inviteMessgeDao.getUnreadMessagesCount()> 0;
        if(redDot != null){
            redDot.setVisibility( showRedDot == true ? View.VISIBLE : View.GONE);
        }

    }

    public void showAskLoginDialog() {
        new AlertDialog.Builder(getActivity()).setTitle(R.string.text_67)
                .setMessage(R.string.text_68)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity(), RegisterActivity.class));
                    }
                })
                .show();
    }
}
