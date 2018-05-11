package com.atlas.crmapp.model;

import java.util.List;

/**
 * Created by Administrator on 2018/2/2.
 */

public class ScoreGetDetailJson {


    /**
     * recordsFiltered : 10
     * draw : 0
     * rows : [{"id":6,"createBy":"Leo,5703","createTime":1517884943000,"appuserId":5703,"points":100,"unitId":1,"type":"SPEND","spendOn":"coupon","refNo":"3","description":"测试"}]
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
         * id : 6
         * createBy : Leo,5703
         * createTime : 1517884943000
         * appuserId : 5703
         * points : 100
         * unitId : 1
         * type : SPEND
         * spendOn : coupon
         * refNo : 3
         * description : 测试
         */

        private int id;
        private String createBy;
        private long createTime;
        private int appuserId;
        private int points;
        private int unitId;
        private String type;
        private String spendOn;
        private String refNo;
        private String description;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getAppuserId() {
            return appuserId;
        }

        public void setAppuserId(int appuserId) {
            this.appuserId = appuserId;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
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

        public String getSpendOn() {
            return spendOn;
        }

        public void setSpendOn(String spendOn) {
            this.spendOn = spendOn;
        }

        public String getRefNo() {
            return refNo;
        }

        public void setRefNo(String refNo) {
            this.refNo = refNo;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
