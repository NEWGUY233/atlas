package com.atlas.crmapp.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/4/23.
 */

public class IndexMomentBean implements Serializable{

    private int recordsFiltered;
    private int draw;
    private int recordsTotal;
    private List<RowsBean> rows;

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean implements Serializable,Comparable<RowsBean>{
        /**
         * id : 5ad98a5277c82f1d82239e5a
         * content : 第一 你好啊，我是帅哥,我的动态
         * appuserId : 7223
         * unitId : 4
         * type : NORMAL
         * state : NORMAL
         * commentQuantity : 1
         * praiseQuantity : 0
         * readQuantity : 166
         * deletable : false
         * praised : false
         * updateTime : 1524206162227
         * createTime : 1524206162227
         * user : {"id":7223,"uid":"A022E4D5314E4B6595D6A4EDEE175D5B","nick":"08","avatar":"http://appassets.crm.atlasoffice.cn/atlas/avata/20170519/eb0f43ed16.jpg","company":"哦"}
         * subDetail : {"commonId":"9","title":"面对生活服务商户的新刚需，团购时代的O2O平台发展要怎么走？","content":"相比稳坐钓鱼台的新美大，后O2O时代的平台竞争依然颇具看点。","img":"http://assets.crm.atlasoffice.cn/atlas/cms/2017055/niaZynWkwb.jpg","actionUri":"topic:9","actionType":"TOPIC","source":"旧的话题"}
         * imgs : ["http://testassets.crm.atlasoffice.cn/atlas/moment/20180419/3S4xt8KKcB.png"]
         */

        private String id;
        private String content;
        private int appuserId;
        private int unitId;
        private String type;
        private String state;
        private String unitName;
        private int commentQuantity;
        private int praiseQuantity;
        private int readQuantity;
        private boolean deletable;
        private boolean praised;
        private boolean isPost;
        private boolean isFailed;
        private long updateTime;
        private long createTime;
        private UserBean user;
        private SubDetailBean subDetail;
        private List<String> imgs;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getAppuserId() {
            return appuserId;
        }

        public void setAppuserId(int appuserId) {
            this.appuserId = appuserId;
        }

        public int getUnitId() {
            return unitId;
        }

        public void setUnitId(int unitId) {
            this.unitId = unitId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getCommentQuantity() {
            return commentQuantity;
        }

        public void setCommentQuantity(int commentQuantity) {
            this.commentQuantity = commentQuantity;
        }

        public int getPraiseQuantity() {
            return praiseQuantity;
        }

        public void setPraiseQuantity(int praiseQuantity) {
            this.praiseQuantity = praiseQuantity;
        }

        public int getReadQuantity() {
            return readQuantity;
        }

        public void setReadQuantity(int readQuantity) {
            this.readQuantity = readQuantity;
        }

        public boolean isDeletable() {
            return deletable;
        }

        public void setDeletable(boolean deletable) {
            this.deletable = deletable;
        }

        public boolean isPraised() {
            return praised;
        }

        public void setPraised(boolean praised) {
            this.praised = praised;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public SubDetailBean getSubDetail() {
            return subDetail;
        }

        public void setSubDetail(SubDetailBean subDetail) {
            this.subDetail = subDetail;
        }

        public List<String> getImgs() {
            return imgs;
        }

        public void setImgs(List<String> imgs) {
            this.imgs = imgs;
        }

        @Override
        public int compareTo(@NonNull RowsBean o) {
            if (isPost)
                return 1;
            return 0;
        }

        public boolean isPost() {
            return isPost;
        }

        public void setPost(boolean post) {
            isPost = post;
        }

        public boolean isFailed() {
            return isFailed;
        }

        public void setFailed(boolean failed) {
            isFailed = failed;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public static class UserBean implements Serializable{
            /**
             * id : 7223
             * uid : A022E4D5314E4B6595D6A4EDEE175D5B
             * nick : 08
             * avatar : http://appassets.crm.atlasoffice.cn/atlas/avata/20170519/eb0f43ed16.jpg
             * company : 哦
             */

            private long id;
            private String uid;
            private String nick;
            private String avatar;
            private String company;
            private String relate;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getNick() {
                return nick;
            }

            public void setNick(String nick) {
                this.nick = nick;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getCompany() {
                return company;
            }

            public void setCompany(String company) {
                this.company = company;
            }

            public String getRelate() {
                return relate;
            }

            public void setRelate(String relate) {
                this.relate = relate;
            }
        }

        public static class SubDetailBean implements Serializable{
            /**
             * commonId : 9
             * title : 面对生活服务商户的新刚需，团购时代的O2O平台发展要怎么走？
             * content : 相比稳坐钓鱼台的新美大，后O2O时代的平台竞争依然颇具看点。
             * img : http://assets.crm.atlasoffice.cn/atlas/cms/2017055/niaZynWkwb.jpg
             * actionUri : topic:9
             * actionType : TOPIC
             * source : 旧的话题
             */

            private String commonId;
            private String title;
            private String content;
            private String img;
            private String actionUri;
            private String actionType;
            private String source;
            private long forumId;

            public String getCommonId() {
                return commonId;
            }

            public void setCommonId(String commonId) {
                this.commonId = commonId;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getActionUri() {
                return actionUri;
            }

            public void setActionUri(String actionUri) {
                this.actionUri = actionUri;
            }

            public String getActionType() {
                return actionType;
            }

            public void setActionType(String actionType) {
                this.actionType = actionType;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public long getForumId() {
                return forumId;
            }

            public void setForumId(long forumId) {
                this.forumId = forumId;
            }
        }
    }
}
