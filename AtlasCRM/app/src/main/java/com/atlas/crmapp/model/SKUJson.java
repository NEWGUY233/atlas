package com.atlas.crmapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Harry on 2017-03-26.
 */

public class SKUJson implements Serializable {

    private static final long serialVersionUID = 4251144534208949664L;
    public long id;
    public String options;
    public double price;
    public double onlinePrice;
    public int storage;
    public boolean defaultFlag;
    public String thumbnail;
    public List<Media> productMedias;

    public class Media implements Serializable {

        private static final long serialVersionUID = -2146738542180845573L;
        public long id;
        public String name;
        public String type;
        public String url;
        public int sortValue;

    }
}
