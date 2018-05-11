package com.atlas.crmapp.model;

import java.io.Serializable;

/**
 * Created by wu on 2017/4/1
 */

public class VThreadsJson implements Serializable {
//             "id": 8,
//            "title": "主题A8",
//            "image": "http://test-byron-atlas-resources.oss-cn-shenzhen.aliyuncs.com/atlas/cms/20170323/PKwzxYRHAN.jpg",
//            "content": "内容A8",
//            "authorName": "作者A8",
//            "likeCnt": 0,
//            "commentCnt": 0,
//            "createTime": 1490585955000,
//            "like": false

    public long id;
    public String title;
    public String image;
    public String content;
    private String forumName;
    private String authorName;
    private long forumId;
    public UserInfoJson appUser;
    public long likeCnt;
    public long commentCnt;
    private long foucsCnt;
    public long createTime;
    public boolean like;
    private boolean foucs;
    private boolean deletable;
    private boolean isMore;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserInfoJson getAppUser() {
        return appUser;
    }

    public void setAppUser(UserInfoJson appUser) {
        this.appUser = appUser;
    }

    public long getLikeCnt() {
        return likeCnt;
    }

    public void setLikeCnt(long likeCnt) {
        this.likeCnt = likeCnt;
    }

    public long getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(long commentCnt) {
        this.commentCnt = commentCnt;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public String getForumName() {
        return forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public long getFoucsCnt() {
        return foucsCnt;
    }

    public void setFoucsCnt(long foucsCnt) {
        this.foucsCnt = foucsCnt;
    }

    public boolean isFoucs() {
        return foucs;
    }

    public void setFoucs(boolean foucs) {
        this.foucs = foucs;
    }

    public long getForumId() {
        return forumId;
    }

    public void setForumId(long forumId) {
        this.forumId = forumId;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }
}
