package com.atlas.crmapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hoda on 2017/12/22.
 */

public class VisitInviteRecordJson implements Serializable{


    private static final long serialVersionUID = -8217895671323425601L;

    /**
     * recordsFiltered : 1
     * draw : 0
     * rows : [{"id":1,"unitId":7,"bookDate":1514014281000,"purpose":"围观开发","visitorNum":4,"state":"NORMAL","createTime":1514014875000,"unitName":"办公空间(雅居乐中心)","invitorPhone":"13425700022","invitorName":"Gary1"}]
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

    public static class RowsBean  implements  Serializable{
        /**
         * id : 1
         * unitId : 7
         * bookDate : 1514014281000
         * purpose : 围观开发
         * visitorNum : 4
         * state : NORMAL
         * createTime : 1514014875000
         * unitName : 办公空间(雅居乐中心)
         * invitorPhone : 13425700022
         * invitorName : Gary1
         * arrivedTime:13425700022
         */

        private long id;
        private long unitId;
        private long bookDate;
        private String purpose;
        private int visitorNum;
        private String state;
        private long createTime;
        private String unitName;
        private String invitorPhone;
        private String invitorName;
        private long arrivedTime;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getUnitId() {
            return unitId;
        }

        public void setUnitId(long unitId) {
            this.unitId = unitId;
        }

        public long getBookDate() {
            return bookDate;
        }

        public void setBookDate(long bookDate) {
            this.bookDate = bookDate;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public int getVisitorNum() {
            return visitorNum;
        }

        public void setVisitorNum(int visitorNum) {
            this.visitorNum = visitorNum;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getInvitorPhone() {
            return invitorPhone;
        }

        public void setInvitorPhone(String invitorPhone) {
            this.invitorPhone = invitorPhone;
        }

        public String getInvitorName() {
            return invitorName;
        }

        public void setInvitorName(String invitorName) {
            this.invitorName = invitorName;
        }

        public long getArrivedTime() {
            return arrivedTime;
        }

        public void setArrivedTime(long arrivedTime) {
            this.arrivedTime = arrivedTime;
        }
    }
}
