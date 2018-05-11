package com.atlas.crmapp.model;

import java.util.List;

/**
 * Created by Harry on 2017-03-26.
 */

public class ProductCategoryJson {
    /**
     * recordsFiltered : 2
     * draw : 0
     * rows : [{"id":61,"bizCode":"coffee","name":"手调饮品","sortValue":0,"childrens":[{"id":55,"bizCode":"coffee","name":"滤式咖啡","sortValue":0,"childrens":[]},{"id":59,"bizCode":"coffee","name":"茶","sortValue":0,"childrens":[]},{"id":5,"bizCode":"coffee","name":"饮品","sortValue":1,"childrens":[]}]},{"id":56,"bizCode":"coffee","name":"瓶装饮品","sortValue":10,"childrens":[]}]
     * recordsTotal : 2
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
         * id : 61
         * bizCode : coffee
         * name : 手调饮品
         * sortValue : 0
         * childrens : [{"id":55,"bizCode":"coffee","name":"滤式咖啡","sortValue":0,"childrens":[]},{"id":59,"bizCode":"coffee","name":"茶","sortValue":0,"childrens":[]},{"id":5,"bizCode":"coffee","name":"饮品","sortValue":1,"childrens":[]}]
         */

        private int id;
        private String bizCode;
        private String name;
        private int sortValue;
        private List<ChildrensBean> childrens;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBizCode() {
            return bizCode;
        }

        public void setBizCode(String bizCode) {
            this.bizCode = bizCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSortValue() {
            return sortValue;
        }

        public void setSortValue(int sortValue) {
            this.sortValue = sortValue;
        }

        public List<ChildrensBean> getChildrens() {
            return childrens;
        }

        public void setChildrens(List<ChildrensBean> childrens) {
            this.childrens = childrens;
        }

        public static class ChildrensBean {
            /**
             * id : 55
             * bizCode : coffee
             * name : 滤式咖啡
             * sortValue : 0
             * childrens : []
             */

            private int id;
            private String bizCode;
            private String name;
            private int sortValue;
            private List<?> childrens;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getBizCode() {
                return bizCode;
            }

            public void setBizCode(String bizCode) {
                this.bizCode = bizCode;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getSortValue() {
                return sortValue;
            }

            public void setSortValue(int sortValue) {
                this.sortValue = sortValue;
            }

            public List<?> getChildrens() {
                return childrens;
            }

            public void setChildrens(List<?> childrens) {
                this.childrens = childrens;
            }
        }
    }

//    public int recordsFiltered;
//    public int draw;
//    public int recordsTotal;
//    public List<Row> rows;
//
//    public class Row {
//
//        public long id;
//        public String bizCode;
//        public String name;
//        public int sortValue;
//        public List<Row> rows;
//    }
}
