package com.atlas.crmapp.model;

/**
 * Created by Administrator on 2018/3/1.
 */

public class CorporationMoneyJson {
    /**
     * id : 41
     * contractId : 1002
     * createTime : 1501295783000
     * bookingMinutes : 99999
     * monthBookingMinutes : 99999
     * bookingMinutesB : 99999
     * monthBookingMinutesB : 99999
     * printingCharges : 666
     * monthPrintingCharges : 0
     * printPages : 0
     */

    private int id;
    private int contractId;
    private long createTime;
    private int bookingMinutes;
    private int monthBookingMinutes;
    private int bookingMinutesB;
    private int monthBookingMinutesB;
    private String printingCharges;
    private int monthPrintingCharges;
    private int printPages;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getBookingMinutes() {
        return bookingMinutes;
    }

    public void setBookingMinutes(int bookingMinutes) {
        this.bookingMinutes = bookingMinutes;
    }

    public int getMonthBookingMinutes() {
        return monthBookingMinutes;
    }

    public void setMonthBookingMinutes(int monthBookingMinutes) {
        this.monthBookingMinutes = monthBookingMinutes;
    }

    public int getBookingMinutesB() {
        return bookingMinutesB;
    }

    public void setBookingMinutesB(int bookingMinutesB) {
        this.bookingMinutesB = bookingMinutesB;
    }

    public int getMonthBookingMinutesB() {
        return monthBookingMinutesB;
    }

    public void setMonthBookingMinutesB(int monthBookingMinutesB) {
        this.monthBookingMinutesB = monthBookingMinutesB;
    }

    public String getPrintingCharges() {
        return printingCharges;
    }

    public void setPrintingCharges(String printingCharges) {
        this.printingCharges = printingCharges;
    }

    public int getMonthPrintingCharges() {
        return monthPrintingCharges;
    }

    public void setMonthPrintingCharges(int monthPrintingCharges) {
        this.monthPrintingCharges = monthPrintingCharges;
    }

    public int getPrintPages() {
        return printPages;
    }

    public void setPrintPages(int printPages) {
        this.printPages = printPages;
    }
}
