
package com.atlas.crmapp.huanxin;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.huanxin.db.InviteMessgeDao;
import com.atlas.crmapp.huanxin.db.UserDao;
import com.atlas.crmapp.huanxin.widget.ContactItemView;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.NetUtils;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * contact list
 * 
 */
public class ContactListFragment extends EaseContactListFragment {
	
    private static final String TAG = ContactListFragment.class.getSimpleName();
    private ContactSyncListener contactSyncListener;
    private BlackListSyncListener blackListSyncListener;
    private ContactInfoSyncListener contactInfoSyncListener;
    private View loadingView;
    private ContactItemView applicationItem;
    private InviteMessgeDao inviteMessgeDao;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;

    public static ContactListFragment newInstance() {
        ContactListFragment fragment = new ContactListFragment();

        return fragment;
    }


    @SuppressLint("InflateParams")
    @Override
    protected void initView() {
        super.initView();
        @SuppressLint("InflateParams")
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.em_contacts_header, null);
        HeaderItemClickListener clickListener = new HeaderItemClickListener();
        applicationItem = (ContactItemView) headerView.findViewById(R.id.application_item);
//        applicationItem.setVisibility(View.INVISIBLE);
        applicationItem.setOnClickListener(clickListener);
//        headerView.findViewById(R.id.group_item).setOnClickListener(clickListener);
//        headerView.findViewById(R.id.chat_room_item).setOnClickListener(clickListener);
//        headerView.findViewById(R.id.robot_item).setOnClickListener(clickListener);
        listView.addHeaderView(headerView);
        //add loading view
        loadingView = LayoutInflater.from(getActivity()).inflate(R.layout.em_layout_loading_data, null);
        contentContainer.addView(loadingView);

        registerForContextMenu(listView);
        registerBroadcastReceiver();
        broadcastManager.sendBroadcast(new Intent(Constant.ACTION_CONTACT_CHANAGED));
    }
    
    @Override
    public void refresh() {
        Map<String, EaseUser> m = IMHelper.getInstance().getContactList();
        if (m instanceof Hashtable<?, ?>) {
            //noinspection unchecked
            m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>)m).clone();
        }
        setContactsMap(m);
        super.refresh();
        if(inviteMessgeDao == null){
            inviteMessgeDao = new InviteMessgeDao(getActivity());
        }
        if(inviteMessgeDao.getUnreadMessagesCount() > 0){
            applicationItem.showUnreadMsgView();
        }else{
            applicationItem.hideUnreadMsgView();
        }
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    protected void setUpView() {
        titleBar.setRightImageResource(R.drawable.em_add);
        titleBar.setRightLayoutClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), AddContactActivity.class));
                NetUtils.hasDataConnection(getActivity());
            }
        });
        titleBar.getRightLayout().setVisibility(View.INVISIBLE);
        //设置联系人数据
        Map<String, EaseUser> m = IMHelper.getInstance().getContactList();
        if (m instanceof Hashtable<?, ?>) {
            m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>)m).clone();
        }
        setContactsMap(m);
        super.setUpView();
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EaseUser user = (EaseUser)listView.getItemAtPosition(position);
                if (user != null) {
                    String username = user.getUsername();
                    // demo中直接进入聊天页面，实际一般是进入用户详情页
                    startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("userId", username));
                }
            }
        });

        
        // 进入添加好友页
        titleBar.getRightLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddContactActivity.class));
            }
        });
        
        
        contactSyncListener = new ContactSyncListener();
        IMHelper.getInstance().addSyncContactListener(contactSyncListener);
        
        blackListSyncListener = new BlackListSyncListener();
        IMHelper.getInstance().addSyncBlackListListener(blackListSyncListener);
        
        contactInfoSyncListener = new ContactInfoSyncListener();
//        IMHelper.getInstance().getUserProfileManager().addSyncContactInfoListener(contactInfoSyncListener);
        
        if (IMHelper.getInstance().isContactsSyncedWithServer()) {
            loadingView.setVisibility(View.GONE);
        } else if (IMHelper.getInstance().isSyncingContactsWithServer()) {
            loadingView.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (contactSyncListener != null) {
            IMHelper.getInstance().removeSyncContactListener(contactSyncListener);
            contactSyncListener = null;
        }
        
        if(blackListSyncListener != null){
            IMHelper.getInstance().removeSyncBlackListListener(blackListSyncListener);
        }
        
//        if(contactInfoSyncListener != null){
//            IMHelper.getInstance().getUserProfileManager().removeSyncContactInfoListener(contactInfoSyncListener);
//        }
        unregisterBroadcastReceiver();
    }
    
	
	protected class HeaderItemClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.application_item:
                // 进入申请与通知页面
                startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));
                break;
//            case R.id.group_item:
//                // 进入群聊列表页面
//                startActivity(new Intent(getActivity(), GroupsActivity.class));
//                break;
//            case R.id.chat_room_item:
//                //进入聊天室列表页面
//                startActivity(new Intent(getActivity(), PublicChatRoomsActivity.class));
//                break;
//            case R.id.robot_item:
//                //进入Robot列表页面
//                startActivity(new Intent(getActivity(), RobotsActivity.class));
//                break;

            default:
                break;
            }
        }
	    
	}
	

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    toBeProcessUser = (EaseUser) listView.getItemAtPosition(((AdapterContextMenuInfo) menuInfo).position);
	    toBeProcessUsername = toBeProcessUser.getUsername();
		getActivity().getMenuInflater().inflate(R.menu.em_context_contact_list, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete_contact) {
			try {
                // delete contact
                deleteContact(toBeProcessUser);
                // remove invitation message
                InviteMessgeDao dao = new InviteMessgeDao(getActivity());
                dao.deleteMessage(toBeProcessUser.getUsername());
            } catch (Exception e) {
                e.printStackTrace();
            }
			return true;
		}else if(item.getItemId() == R.id.add_to_blacklist){
			moveToBlacklist(toBeProcessUsername);
			return true;
		}
		return super.onContextItemSelected(item);
	}


	/**
	 * delete contact
	 * 
	 */
	public void deleteContact(final EaseUser tobeDeleteUser) {
		String st1 = getResources().getString(R.string.deleting);
		final String st2 = getResources().getString(R.string.Delete_failed);
		final KProgressHUD pd = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(st1)
                //.setDetailsLabel("Downloading data")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
		new Thread(new Runnable() {
			public void run() {
				try {
					EMClient.getInstance().contactManager().deleteContact(tobeDeleteUser.getUsername());
					// remove user from memory and database
					UserDao dao = new UserDao(getActivity());
					dao.deleteContact(tobeDeleteUser.getUsername());
					IMHelper.getInstance().getContactList().remove(tobeDeleteUser.getUsername());
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							contactList.remove(tobeDeleteUser);
							contactListLayout.refresh();

						}
					});
				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(getActivity(), st2 + e.getMessage(), Toast.LENGTH_LONG).show();
						}
					});

				}

			}
		}).start();

	}
	
	class ContactSyncListener implements IMHelper.DataSyncListener {
        @Override
        public void onSyncComplete(final boolean success) {
            EMLog.d(TAG, "on contact list sync success:" + success);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    getActivity().runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            if(success){
                                loadingView.setVisibility(View.GONE);
                                refresh();
                            }else{
                                String s1 = getResources().getString(R.string.get_failed_please_check);
                                Toast.makeText(getActivity(), s1, Toast.LENGTH_LONG).show();
                                loadingView.setVisibility(View.GONE);
                            }
                        }
                        
                    });
                }
            });
        }
    }
    
    class BlackListSyncListener implements IMHelper.DataSyncListener {

        @Override
        public void onSyncComplete(boolean success) {
            getActivity().runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    refresh();
                }
            });
        }

    }

    class ContactInfoSyncListener implements IMHelper.DataSyncListener {

        @Override
        public void onSyncComplete(final boolean success) {
            EMLog.d(TAG, "on contactinfo list sync success:" + success);
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    loadingView.setVisibility(View.GONE);
                    if(success){
                        refresh();
                    }
                }
            });
        }

    }

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                getAllEaseContacts();
                //end of red packet code
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
        getActivity().sendBroadcast(new Intent(Constant.ACTION_CONTACT_CHANAGED));
    }

    private void getAllEaseContacts(){
        try {
            Map<String, EaseUser> localUsers = IMHelper.getInstance().getContactList();
            if(localUsers!=null&&localUsers.size()>0) {
                ArrayList<String> list = new ArrayList<String>();
                ArrayList<EaseUser> users = new ArrayList<EaseUser>();
                for (Map.Entry<String, EaseUser> entry : localUsers.entrySet()) {
                    list.add(entry.getKey());
                    users.add(entry.getValue());
                }

                getUserInfoList(list,users);
            }
        }catch (Exception e) {

        }
    }

    private void getUserInfoList(ArrayList<String> list,final ArrayList<EaseUser> localUsers){
        BizDataRequest.requestUidUserInfoNormal(getActivity(), list, new BizDataRequest.OnUidUserInfo() {
            @Override
            public void onSuccess(List<PersonInfoJson> personList) {
                for(int i=0;i<personList.size();i++){
                    for(int j=0;j<localUsers.size();j++){
                        if(localUsers.get(j).getUsername().equals(personList.get(i).uid.toLowerCase())){
                            EaseUser user= localUsers.get(j);
                            user.setAvatar(personList.get(i).avatar);
                            user.setNickname(personList.get(i).nick);
                            IMHelper.getInstance().saveContact(user);
                        }
                    }
                }
                refresh();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    private void unregisterBroadcastReceiver(){
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

	
}
