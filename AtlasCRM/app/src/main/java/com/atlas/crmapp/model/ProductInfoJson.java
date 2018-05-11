package com.atlas.crmapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Harry on 2017-03-26.
 */

public class ProductInfoJson implements Serializable {


    private static final long serialVersionUID = 1650864559371857100L;
    public long id;
    public int categoryId;
    public String categoryName;
    public int unitId;
    public String name;
    public int number;
    public String code;
    public String description;
    public String detail;
    public String recommend;
    public String state;
    public boolean onlineOnly;
    public List<SKUJson> productSkus;
    public List<DetailMediaJson> detailMedias;
    public String bizCode;
}
