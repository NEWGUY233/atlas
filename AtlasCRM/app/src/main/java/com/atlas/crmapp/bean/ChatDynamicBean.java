package com.atlas.crmapp.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/3.
 */

public class ChatDynamicBean {

    /**
     * recordsFiltered : 10
     * draw : 0
     * rows : [{"id":"5aea79c20cf2b8d25ad009d3","createTime":1525316034836,"commentContent":"哈哈哈","user":{"id":6019,"uid":"1C690495BE3F415E824F63614F835B1F","nick":"Leo","avatar":"http://testassets.crm.atlasoffice.cn/atlas/avata/20180425/f70e996f22.jpg","company":"Atlas","relate":"OWN"},"subDetail":{"commonId":"1491","title":"1","content":"1","img":"http://testassets.crm.atlasoffice.cn/atlas/cms/2018052/7DpzYRk6Sa.png","actionUri":"topic:1491","actionType":"TOPIC","source":"雅居乐办公空间"},"msgType":"TOPIC_COMMENT"}]
     * recordsTotal : 10
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
         * id : 5aea79c20cf2b8d25ad009d3
         * createTime : 1525316034836
         * commentContent : 哈哈哈
         * user : {"id":6019,"uid":"1C690495BE3F415E824F63614F835B1F","nick":"Leo","avatar":"http://testassets.crm.atlasoffice.cn/atlas/avata/20180425/f70e996f22.jpg","company":"Atlas","relate":"OWN"}
         * subDetail : {"commonId":"1491","title":"1","content":"1","img":"http://testassets.crm.atlasoffice.cn/atlas/cms/2018052/7DpzYRk6Sa.png","actionUri":"topic:1491","actionType":"TOPIC","source":"雅居乐办公空间"}
         * msgType : TOPIC_COMMENT
         */

        private String id;
        private long createTime;
        private String commentContent;
        private UserBean user;
        private SubDetailBean subDetail;
        private String msgType;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getCommentContent() {
            return commentContent;
        }

        public void setCommentContent(String commentContent) {
            this.commentContent = commentContent;
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

        public String getMsgType() {
            return msgType;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }

        public static class UserBean {
            /**
             * id : 6019
             * uid : 1C690495BE3F415E824F63614F835B1F
             * nick : Leo
             * avatar : http://testassets.crm.atlasoffice.cn/atlas/avata/20180425/f70e996f22.jpg
             * company : Atlas
             * relate : OWN
             */

            private int id;
            private String uid;
            private String nick;
            private String avatar;
            private String company;
            private String relate;

            public int getId() {
                return id;
            }

            public void setId(int id) {
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

        public static class SubDetailBean {
            /**
             * commonId : 1491
             * title : 1
             * content : 1
             * img : http://testassets.crm.atlasoffice.cn/atlas/cms/2018052/7DpzYRk6Sa.png
             * actionUri : topic:1491
             * actionType : TOPIC
             * source : 雅居乐办公空间
             */

            private String commonId;
            private String title;
            private String content;
            private String img;
            private String actionUri;
            private String actionType;
            private String source;

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
        }
    }
}
