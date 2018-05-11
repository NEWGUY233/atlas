package com.atlas.crmapp.model;

/**
 * Created by wu on 2017/4/4
 */

public class DateModl {
    private String yeardate;
    private String dayydate;
    private String week;

    private String date;
    private String dateweek;

    public String getDateweek() {
        return dateweek;
    }

    public void setDateweek(String dateweek) {
        this.dateweek = dateweek;
    }

    public String getYeardate() {
        return yeardate;
    }

    public void setYeardate(String yeardate) {
        this.yeardate = yeardate;
    }

    public String getDayydate() {
        return dayydate;
    }

    public void setDayydate(String dayydate) {
        this.dayydate = dayydate;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
