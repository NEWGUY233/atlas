package com.atlas.crmapp.model;

import java.util.List;

/**
 * Created by Administrator on 2018/2/2.
 */

public class ScoreDetailJson {

    /**
     * recordsFiltered : 3
     * draw : 0
     * rows : [{"id":1,"createBy":"Leo,5703","createTime":1517549606000,"appuserId":5703,"points":20000,"unitId":7,"action":"booking_meeting_room","type":"GAIN","refNo":"61962","description":"会议室预订"},{"id":4,"createBy":"Leo,5703","createTime":1517794589000,"appuserId":5703,"points":20000,"unitId":7,"action":"booking_meeting_room","type":"GAIN","refNo":"61973","description":"会议室预订"},{"id":5,"createBy":"Leo,5703","createTime":1517814861000,"appuserId":5703,"points":20000,"unitId":7,"action":"booking_meeting_room","type":"GAIN","refNo":"61975","description":"会议室预订"}]
     * recordsTotal : 3
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
         * id : 1
         * createBy : Leo,5703
         * createTime : 1517549606000
         * appuserId : 5703
         * points : 20000
         * unitId : 7
         * action : booking_meeting_room
         * type : GAIN
         * refNo : 61962
         * description : 会议室预订
         */

        private int id;
        private String createBy;
        private long createTime;
        private int appuserId;
        private int points;
        private int unitId;
        private String action;
        private String type;
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

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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
