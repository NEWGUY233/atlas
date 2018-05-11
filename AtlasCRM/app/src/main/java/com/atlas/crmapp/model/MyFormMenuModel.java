package com.atlas.crmapp.model;

/**
 * Created by Alex on 2017/3/20.
 */

public class MyFormMenuModel {
    private String no;
    private String group;
    private String name;
    private int icon;
    private String content;
    private int type;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    private String logo;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private boolean mShowGroup = false;

    private boolean mShowArrow = false;

    public String getNo() {
        return no;
    }
    public void setNo(String no) {
        this.no = no;
    }
    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIcon() {
        return icon;
    }
    public void setIcon(int icon) {
        this.icon = icon;
    }
    public boolean isShowGroup() {
        return mShowGroup;
    }
    public void setShowGroup(boolean showGroup) {
        this.mShowGroup = showGroup;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public boolean isShowArrow() {
        return mShowArrow;
    }

    public void setShowArrow(boolean showGroup) {
        this.mShowArrow = showGroup;
    }
}