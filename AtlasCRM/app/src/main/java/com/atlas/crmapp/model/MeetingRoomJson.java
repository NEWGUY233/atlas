package com.atlas.crmapp.model;

import java.util.List;

/**
 * Created by Harry on 2017-03-28.
 */

public class MeetingRoomJson {

    public int id;
    public Facility facility;
    public int capacity;
    public double area;
    public List<Feature> featureList;
    public String compositeName;
    public List<OccupyTime> occupyTimes;
    public int interval;
    public String openTime;
    public String closeTime;
    public int unit;
    public String descript;

    public class Facility {
        public int id;
        public String type;
        public String name;
        public String code;
        public String state;
        public double price;
        public boolean enabled;
        public String thumbnail;

    }

    public class Feature {

        public int id;
        public String name;
        public String code;
    }

    public class OccupyTime {
        public String startTime;
        public String endTime;
        public long startTimestamp;
        public long endTimestamp;
    }


}
