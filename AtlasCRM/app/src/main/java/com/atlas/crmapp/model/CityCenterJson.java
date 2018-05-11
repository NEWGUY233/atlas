package com.atlas.crmapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hoda on 2017/9/23.
 */

public class CityCenterJson implements Serializable {


    private static final long serialVersionUID = -1468042187348638869L;
    /**
     * city : {"id":3,"createBy":"","createTime":1491529945000,"code":"guangzhou","name":"广州","parentId":2,"enabled":true,"level":2,"displayOrder":0}
     * centers : [{"id":4,"createBy":"","createTime":1491529945000,"code":"yajule","name":"雅居乐中心","parentId":3,"lat":23.124319,"lng":113.321869,"address":"雅居乐中心","enabled":true,"level":3,"displayOrder":0},{"id":18,"createBy":"管理员,1","createTime":1497943740000,"code":"fazhanzhongxin","name":"发展中心","parentId":3,"lat":23.114665,"lng":113.316984,"address":"广州发展中心","enabled":true,"level":3,"displayOrder":0},{"id":27,"createBy":"管理员,1","createTime":1505811603000,"code":"huochezhan","name":"火车站","parentId":3,"lat":23.14776,"lng":113.25806,"address":"广州火车站","enabled":true,"level":3,"displayOrder":0},{"id":28,"createBy":"管理员,1","createTime":1505816032000,"code":"chebie","name":"车陂","parentId":3,"lat":28.070522,"lng":114.171715,"address":"车陂","enabled":true,"level":3,"displayOrder":0}]
     */

    private CityBean city;
    private List<CentersBean> centers;

    public CityBean getCity() {
        return city;
    }

    public void setCity(CityBean city) {
        this.city = city;
    }

    public List<CentersBean> getCenters() {
        return centers;
    }

    public void setCenters(List<CentersBean> centers) {
        this.centers = centers;
    }

    public static class CityBean implements Serializable {
        private static final long serialVersionUID = 634780831250244737L;
        /**
         * id : 3
         * createBy :
         * createTime : 1491529945000
         * code : guangzhou
         * name : 广州
         * parentId : 2
         * enabled : true
         * level : 2
         * displayOrder : 0
         */

        private int id;
        private String createBy;
        private long createTime;
        private String code;
        private String name;
        private int parentId;
        private boolean enabled;
        private int level;
        private int displayOrder;

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

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getDisplayOrder() {
            return displayOrder;
        }

        public void setDisplayOrder(int displayOrder) {
            this.displayOrder = displayOrder;
        }
    }

    public static class CentersBean {
        /**
         * id : 4
         * createBy :
         * createTime : 1491529945000
         * code : yajule
         * name : 雅居乐中心
         * parentId : 3
         * lat : 23.124319
         * lng : 113.321869
         * address : 雅居乐中心
         * enabled : true
         * level : 3
         * displayOrder : 0
         */

        private long id;
        private String createBy;
        private long createTime;
        private String code;
        private String name;
        private int parentId;
        private double lat;
        private double lng;
        private String address;
        private boolean enabled;
        private int level;
        private boolean keyfree;
        private int displayOrder;

        public long getId() {
            return id;
        }

        public void setId(long id) {
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

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getDisplayOrder() {
            return displayOrder;
        }

        public void setDisplayOrder(int displayOrder) {
            this.displayOrder = displayOrder;
        }

        public boolean isKeyfree() {
            return keyfree;
        }

        public void setKeyfree(boolean keyfree) {
            this.keyfree = keyfree;
        }
    }
}
