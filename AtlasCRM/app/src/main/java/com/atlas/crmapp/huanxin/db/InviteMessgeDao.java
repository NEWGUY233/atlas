
package com.atlas.crmapp.huanxin.db;

import android.content.ContentValues;
import android.content.Context;

import com.atlas.crmapp.huanxin.domain.InviteMessage;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class InviteMessgeDao {
	static final String TABLE_NAME = "new_friends_msgs";
	static final String COLUMN_NAME_ID = "id";
	static final String COLUMN_NAME_FROM = "username";
	static final String COLUMN_NAME_GROUP_ID = "groupid";
	static final String COLUMN_NAME_GROUP_Name = "groupname";
	
	static final String COLUMN_NAME_TIME = "time";
	static final String COLUMN_NAME_REASON = "reason";
	public static final String COLUMN_NAME_STATUS = "status";
	static final String COLUMN_NAME_ISINVITEFROMME = "isInviteFromMe";
	static final String COLUMN_NAME_GROUPINVITER = "groupinviter";
	
	static final String COLUMN_NAME_UNREAD_MSG_COUNT = "unreadMsgCount";
	
		
	public InviteMessgeDao(Context context){
	}
	
	/**
	 * save message
	 * @param message
	 * @return  return cursor of the message
	 */
	public Integer saveMessage(InviteMessage message){
		return IMDBManager.getInstance().saveMessage(message);
	}
	
	/**
	 * update message
	 * @param msgId
	 * @param values
	 */
	public void updateMessage(int msgId,ContentValues values){
	    IMDBManager.getInstance().updateMessage(msgId, values);
	}
	
	/**
	 * get messges
	 * @return
	 */
	public List<InviteMessage> getMessagesList(){
		return IMDBManager.getInstance().getMessagesList();
	}
	
	public void deleteMessage(String from){
	    IMDBManager.getInstance().deleteMessage(from);
	}
	
	public int getUnreadMessagesCount(){
	    return IMDBManager.getInstance().getUnreadNotifyCount();
	}
	
	public void saveUnreadMessageCount(int count){
	    IMDBManager.getInstance().setUnreadNotifyCount(count);
	}

	public void updateMessageRefused(InviteMessage msg){
		try {
		if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEINVITEED) {//decline the invitation
			EMClient.getInstance().contactManager().declineInvitation(msg.getFrom());
		} else if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEAPPLYED) { //decline application to join group
			EMClient.getInstance().groupManager().declineApplication(msg.getFrom(), msg.getGroupId(), "");
		} else if (msg.getStatus() == InviteMessage.InviteMesageStatus.GROUPINVITATION) {
			EMClient.getInstance().groupManager().declineInvitation(msg.getGroupId(), msg.getGroupInviter(), "");
		}
		msg.setStatus(InviteMessage.InviteMesageStatus.REFUSED);
		// update database
		ContentValues values = new ContentValues();
		values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
		this.updateMessage(msg.getId(), values);
		} catch (HyphenateException e) {
			e.printStackTrace();
		}
	}


	public void updateMessageAgreed(InviteMessage msg){
		try {
			if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEINVITEED) {//accept be friends
				EMClient.getInstance().contactManager().acceptInvitation(msg.getFrom());
			} else if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEAPPLYED) { //accept application to join group
				EMClient.getInstance().groupManager().acceptApplication(msg.getFrom(), msg.getGroupId());
			} else if (msg.getStatus() == InviteMessage.InviteMesageStatus.GROUPINVITATION) {
				EMClient.getInstance().groupManager().acceptInvitation(msg.getGroupId(), msg.getGroupInviter());
			}
			msg.setStatus(InviteMessage.InviteMesageStatus.AGREED);
			// update database
			ContentValues values = new ContentValues();
			values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
			this.updateMessage(msg.getId(), values);
		} catch (HyphenateException e) {
			e.printStackTrace();
		}
	}
}
