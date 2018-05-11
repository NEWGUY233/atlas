package com.atlas.crmapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jason on 2017/4/21.
 */

public class LessonJson implements Serializable{

    private static final long serialVersionUID = 7026621905914538928L;
    public long id;
    public String name;
    public int duration;
    public double price;
    public String thumbnail;
    public String description;
    public String detail;
    public String major;
    public List<DetailMediaJson> medias;
    public long startTime;
    public long endTime;
}
