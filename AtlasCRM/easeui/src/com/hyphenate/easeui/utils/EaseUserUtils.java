package com.hyphenate.easeui.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.widget.GlideCircleTransform;

public class EaseUserUtils {
    
    static EaseUserProfileProvider userProvider;
    
    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }
    
    /**
     * get EaseUser according username
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username){
        if(userProvider != null)
            return userProvider.getUser(username);
        
        return null;
    }
    
    /**
     * set user avatar
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
        EaseUser user = getUserInfo(username);
        if(user != null && user.getAvatar() != null){
            try {
                int avatarResId = Integer.parseInt(user.getAvatar());
//                Glide.with(context).load(avatarResId).into(imageView);
                Glide.with(context).load(avatarResId).transform(new GlideCircleTransform(context)).into(imageView);
            } catch (Exception e) {
                //use default avatar
//                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(imageView);
                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL). placeholder(R.drawable.ease_default_avatar).transform(new GlideCircleTransform(context)).into(imageView);
            }
        }else{
//            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
            Glide.with(context).load(R.drawable.ease_default_avatar).transform(new GlideCircleTransform(context)).into(imageView);
        }
    }
    
    /**
     * set user's nickname
     */
    public static void setUserNick(String username,TextView textView){
        if(textView != null){
        	EaseUser user = getUserInfo(username);
            Log.d("user.getNick(---",user.getNick()  + "  username====" + username);
        	if(user != null && user.getNick() != null){
                //TODO
                if(user.getNick().length()== 32){
                    textView.setText("匿名好友");
                }else{
                    textView.setText(user.getNick());
                }
            }else{
        		textView.setText("匿名好友");
        	}
        }
    }

    public static String  getUserNick(String username){
        EaseUser user = getUserInfo(username);
        String nick = user.getNick();
        if(TextUtils.isEmpty(nick) ||nick.equals(username)){
            nick ="匿名好友";
        }
        return nick;
    }
    
}
