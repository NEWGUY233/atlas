package com.atlas.crmapp.model;

import java.io.Serializable;

/**
 * Created by Jason on 2017/4/21.
 */

public class ClassJson implements Serializable {

    private static final long serialVersionUID = -8231539660966301653L;
    public long id;
    public long lessonId;
    public long coachId;
    public String venue;//地址
    public int maxSize;
    public long startTime;
    public long endTime;
    public String lessonName;
    public int duration;
    public double price;
    public String thumbnail;
    public String description;
    public String detail;
    public String major;
    public long unitId;
    public String coachName;
    public String coachThumbnail;
    public int bookNum;
}
