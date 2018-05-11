package com.atlas.crmapp.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/10.
 */

public class ChatNoticeBean {
    /**
     * recordsFiltered : 1
     * draw : 0
     * rows : [{"id":12506,"title":"ATLAS 寰图","content":"恭喜您获得评论话题50积分","type":"bonuspoints"}]
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
         * id : 12506
         * title : ATLAS 寰图
         * content : 恭喜您获得评论话题50积分
         * type : bonuspoints
         */

        private int id;
        private String title;
        private String content;
        private String type;
        private String actionUri;
        private long createTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getActionUri() {
            return actionUri;
        }

        public void setActionUri(String actionUri) {
            this.actionUri = actionUri;
        }
    }
}
