package com.atlas.crmapp.huanxin;

import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

/**
 * Created by huangyang on 2017/5/1.
 * 环信的管理类
 */

public class HuanXinManager {

    /**
     * 登录环信
     * @param id 用户的ID
     */
    public static void login(String id,String psw){

        Log.i("Test","id = "+id);
        Log.i("Test","psw = "+psw);

        if (id == null){
            return;
        }
        if (id.length() == 0){
            return;
        }
        if (psw == null){
            return;
        }
        if (psw.length() == 0){
            return;
        }

        EMClient.getInstance().login(id,psw,new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.i("Test","登录聊天服务器成功");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {

                Log.i("Test","登录聊天服务器失败");
            }
        });
    }

    /**
     * 退出登录
     */
    public static void logOut(){
        EMClient.getInstance().logout(true);
    }

    /**
     * 添加好友
     * @param toAddUsername 好友的ID
     * @param reason 添加的理由
     */
    public static void addFriend(String toAddUsername,String reason){

        if (toAddUsername == null){
            return;
        }
        if (toAddUsername.length() == 0){
            return;
        }


        try {
            EMClient.getInstance().contactManager().addContact(toAddUsername, reason);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取好友列表
     * @return 好友的ID
     */
    public static List<String> getFriends(){

        try {
            List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
            return usernames;
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除好友
     * @param userName 好友的ID
     */
    public static void deleteFriend(String userName){

        if (userName == null){
            return;
        }
        if (userName.length() == 0){
            return;
        }

        try {
            EMClient.getInstance().contactManager().deleteContact(userName);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同意添加
     * @param userName 用户的ID
     */
    public static void acceptInvitation(String userName){

        if (userName == null){
            return;
        }
        if (userName.length() == 0){
            return;
        }

        try {
            EMClient.getInstance().contactManager().acceptInvitation(userName);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拒绝添加
     * @param userName 好友的ID
     */
    public static void declineInvitation(String userName){

        if (userName == null){
            return;
        }
        if (userName.length() == 0){
            return;
        }

        try {
            EMClient.getInstance().contactManager().declineInvitation(userName);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }


    }


    /**
     * 好友状态的监听
     */
    public static void contactListener(){
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {



            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
            }

            @Override
            public void onFriendRequestAccepted(String s) {
                //好友请求被同意
            }

            @Override
            public void onFriendRequestDeclined(String s) {
                //好友请求被拒绝
            }

            @Override
            public void onContactDeleted(String username) {
                //被删除时回调此方法
            }


            @Override
            public void onContactAdded(String username) {
                //增加了联系人时回调此方法
            }
        });
    }



}
