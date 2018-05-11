package com.atlas.crmapp.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/23.
 */

public class DynamicCommentBean {
    /**
     * recordsFiltered : 1
     * draw : 0
     * rows : [{"id":"5ad98ac277c82f1d82239e5b","content":"火钳刘明","appuserId":7228,"momentId":"5ad98a5277c82f1d82239e5a","createTime":1524206274107,"state":"NORMAL","praiseQuantity":0,"updateTime":1524206274107,"user":{"id":58,"uid":"A022E4D5314E4B6595D6A4EDEE175D5B","avatar":"http://appassets.crm.atlasoffice.cn/atlas/avata/20170519/eb0f43ed16.jpg","nick":"雅居乐中心-测试号","company":"寰图（中国）有限公司","relate":"OFFICIAL"},"like":false}]
     * recordsTotal : 1
     */

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

    public static class RowsBean {
        /**
         * id : 5ad98ac277c82f1d82239e5b
         * content : 火钳刘明
         * appuserId : 7228
         * momentId : 5ad98a5277c82f1d82239e5a
         * createTime : 1524206274107
         * state : NORMAL
         * praiseQuantity : 0
         * updateTime : 1524206274107
         * user : {"id":58,"uid":"A022E4D5314E4B6595D6A4EDEE175D5B","avatar":"http://appassets.crm.atlasoffice.cn/atlas/avata/20170519/eb0f43ed16.jpg","nick":"雅居乐中心-测试号","company":"寰图（中国）有限公司","relate":"OFFICIAL"}
         * like : false
         */

        private String id;
        private String content;
        private int appuserId;
        private String momentId;
        private long createTime;
        private String state;
        private int praiseQuantity;
        private long updateTime;
        private UserBean user;
        private boolean like;

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

        public String getMomentId() {
            return momentId;
        }

        public void setMomentId(String momentId) {
            this.momentId = momentId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getPraiseQuantity() {
            return praiseQuantity;
        }

        public void setPraiseQuantity(int praiseQuantity) {
            this.praiseQuantity = praiseQuantity;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public boolean isLike() {
            return like;
        }

        public void setLike(boolean like) {
            this.like = like;
        }

        public static class UserBean {
            /**
             * id : 58
             * uid : A022E4D5314E4B6595D6A4EDEE175D5B
             * avatar : http://appassets.crm.atlasoffice.cn/atlas/avata/20170519/eb0f43ed16.jpg
             * nick : 雅居乐中心-测试号
             * company : 寰图（中国）有限公司
             * relate : OFFICIAL
             */

            private long id;
            private String uid;
            private String avatar;
            private String nick;
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

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getNick() {
                return nick;
            }

            public void setNick(String nick) {
                this.nick = nick;
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
    }
}
