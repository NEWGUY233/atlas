package com.atlas.crmapp.model;

import java.util.List;

/**
 * Created by Harry on 2017-03-26.
 */

public class UnitBizJson {

    public int recordsFiltered;
    public int draw;
    public List<Row> rows;
    public int recordsTotal;

    public class Row {

        public long id;
        public String name;
        public String lat;
        public String lng;
        public String bizCode;
    }
}
