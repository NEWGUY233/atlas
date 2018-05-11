package com.atlas.crmapp.model;

import java.util.List;

/**
 * Created by Administrator on 2018/2/22.
 */

public class PrintDataJson {

    /**
     * recordsFiltered : 4
     * draw : 0
     * rows : [{"id":58,"jobId":63,"color":"COLOR","pageSize":"A4","finishedPages":0,"totalPages":0,"copies":1,"hpJobId":"","state":"PAID","type":"COPY","orderId":62371,"appuserId":140,"createTime":1520222253000},{"id":59,"jobId":64,"color":"COLOR","pageSize":"A4","finishedPages":0,"totalPages":0,"copies":1,"hpJobId":"","state":"PAID","type":"COPY","orderId":62372,"appuserId":140,"createTime":1520227305000},{"id":60,"jobId":65,"color":"COLOR","pageSize":"A4","finishedPages":0,"totalPages":0,"copies":1,"hpJobId":"","state":"PAID","type":"COPY","orderId":62373,"appuserId":140,"createTime":1520227558000},{"id":61,"jobId":66,"color":"COLOR","pageSize":"A4","finishedPages":0,"totalPages":0,"copies":1,"hpJobId":"","state":"PAID","type":"COPY","orderId":62375,"appuserId":140,"createTime":1520227666000}]
     * recordsTotal : 4
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
         * id : 58
         * jobId : 63
         * color : COLOR
         * pageSize : A4
         * finishedPages : 0
         * totalPages : 0
         * copies : 1
         * hpJobId :
         * state : PAID
         * type : COPY
         * orderId : 62371
         * appuserId : 140
         * createTime : 1520222253000
         */

        private int id;
        private int jobId;
        private String color;
        private String pageSize;
        private int finishedPages;
        private int totalPages;
        private int copies;
        private String hpJobId;
        private String state;
        private String type;
        private int orderId;
        private int appuserId;
        private long createTime;
        private String documentName;
        private String jobSize;

        public String getDocumentName() {
            return documentName;
        }

        public void setDocumentName(String documentName) {
            this.documentName = documentName;
        }

        public String getJobSize() {
            return jobSize;
        }

        public void setJobSize(String jobSize) {
            this.jobSize = jobSize;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getJobId() {
            return jobId;
        }

        public void setJobId(int jobId) {
            this.jobId = jobId;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public int getFinishedPages() {
            return finishedPages;
        }

        public void setFinishedPages(int finishedPages) {
            this.finishedPages = finishedPages;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getCopies() {
            return copies;
        }

        public void setCopies(int copies) {
            this.copies = copies;
        }

        public String getHpJobId() {
            return hpJobId;
        }

        public void setHpJobId(String hpJobId) {
            this.hpJobId = hpJobId;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getAppuserId() {
            return appuserId;
        }

        public void setAppuserId(int appuserId) {
            this.appuserId = appuserId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
    }
}
