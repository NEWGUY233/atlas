package com.atlas.crmapp.model;

import java.util.List;

/**
 * Created by Harry on 2017-03-26.
 */

public class ResourceJson {

    public int recordsFiltered;
    public int draw;
    public int recordsTotal;
    public List<Row> rows;

    public class Row {
        public long id;
        public String uri;
        public String type;
        public List<ResourceMedia> resourceMedias;
    }

    public class ResourceMedia {

        public long id;
        public String content;
        public String url;
        public String thumbnail;
        public String actionType;
        public String actionUri;
        public String source;
    }
}
