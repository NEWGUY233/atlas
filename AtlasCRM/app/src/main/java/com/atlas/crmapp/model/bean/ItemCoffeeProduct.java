package com.atlas.crmapp.model.bean;

import java.util.List;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/16
 *         Description :
 */

public class ItemCoffeeProduct {

    private String categoryName;
    private List<ProductEntity> mProductEntityList;

    private int childOrderCount;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getChildOrderCount() {
        return childOrderCount;
    }

    public void setChildOrderCount(int childOrderCount) {
        this.childOrderCount = childOrderCount;
    }

    public List<ProductEntity> getProductEntityList() {
        return mProductEntityList;
    }

    public void setProductEntityList(List<ProductEntity> productEntityList) {
        mProductEntityList = productEntityList;
    }

    public static class ProductEntity {

        private String imgPath;
        private String name;
        private int price;
        private int num;

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }


}
