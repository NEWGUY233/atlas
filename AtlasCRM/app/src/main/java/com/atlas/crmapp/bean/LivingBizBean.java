package com.atlas.crmapp.bean;

/**
 * Created by Administrator on 2018/4/28.
 */

public class LivingBizBean {
    /**
     * id : 8
     * name : 咖啡
     * lat : 23.12431900
     * lng : 113.32186900
     * bizCode : coffee
     * keyfree : false
     * parentId : 4
     * parentUnit : {"id":4,"name":"雅居乐中心","lat":"23.12431900","lng":"113.32186900","keyfree":false}
     */

    private int id;
    private String name;
    private String lat;
    private String lng;
    private String bizCode;
    private boolean keyfree;
    private int parentId;
    private ParentUnitBean parentUnit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public boolean isKeyfree() {
        return keyfree;
    }

    public void setKeyfree(boolean keyfree) {
        this.keyfree = keyfree;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public ParentUnitBean getParentUnit() {
        return parentUnit;
    }

    public void setParentUnit(ParentUnitBean parentUnit) {
        this.parentUnit = parentUnit;
    }

    public static class ParentUnitBean {
        /**
         * id : 4
         * name : 雅居乐中心
         * lat : 23.12431900
         * lng : 113.32186900
         * keyfree : false
         */

        private int id;
        private String name;
        private String lat;
        private String lng;
        private boolean keyfree;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public boolean isKeyfree() {
            return keyfree;
        }

        public void setKeyfree(boolean keyfree) {
            this.keyfree = keyfree;
        }
    }
}
