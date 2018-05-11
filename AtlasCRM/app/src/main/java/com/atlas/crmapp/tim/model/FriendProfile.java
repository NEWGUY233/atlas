package com.atlas.crmapp.tim.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.atlas.crmapp.R;
import com.atlas.crmapp.tim.utils.Cn2Spell;
import com.tencent.imsdk.TIMUserProfile;

/**
 * 好友资料
 */
public class FriendProfile implements ProfileSummary,Comparable<FriendProfile> {


    private TIMUserProfile profile;
    private boolean isSelected;

    public FriendProfile(TIMUserProfile profile){
        this.profile = profile;
    }


    /**
     * 获取头像资源
     */
    @Override
    public int getAvatarRes() {
        return R.mipmap.icon_informationheard;
    }


    /**
     * 获取头像地址
     */
    @Override
    public String getAvatarUrl() {
        return profile.getFaceUrl();
    }

    /**
     * 获取名字
     */
    @Override
    public String getName() {
        if (!profile.getRemark().equals("")){
            return profile.getRemark();
        }else if (!profile.getNickName().equals("")){
            return profile.getNickName();
        }
        return profile.getIdentifier();
    }

    /**
     * 获取描述信息
     */
    @Override
    public String getDescription() {
        return null;
    }

    /**
     * 显示详情
     *
     * @param context 上下文
     */
    @Override
    public void onClick(Context context) {
        if (FriendshipInfo.getInstance().isFriend(profile.getIdentifier())){
//            ProfileActivity.navToProfile(context, profile.getIdentifier());
        }else{
//            Intent person = new Intent(context,AddFriendActivity.class);
//            person.putExtra("id",profile.getIdentifier());
//            person.putExtra("name",getName());
//            context.startActivity(person);
        }
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    /**
     * 获取用户ID
     */
    @Override
    public String getIdentify(){
        return profile.getIdentifier();
    }


    /**
     * 获取用户备注名
     */
    public String getRemark(){
        return profile.getRemark();
    }


    /**
     * 获取好友分组
     */
    public String getGroupName(){

        if (profile.getFriendGroups().size() == 0){
            return "";
        }else{
            return profile.getFriendGroups().get(0);
        }
    }


    @Override
    public int compareTo(@NonNull FriendProfile o) {
        String firstL = Cn2Spell.getPinYin(getName()).substring(0, 1).toUpperCase();
        if (!firstL.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
            firstL = "#";
        }

        String aFirstL = Cn2Spell.getPinYin(o.getName()).substring(0, 1).toUpperCase();
        if (!aFirstL.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
            aFirstL = "#";
        }

        if (firstL.equals("#") && !aFirstL.equals("#")) {
            return 1;
        } else if (!firstL.equals("#") && aFirstL.equals("#")){
            return -1;
        }


        return Cn2Spell.getPinYin(getName()).compareToIgnoreCase(Cn2Spell.getPinYin(o.getName()));
    }
}
