package com.atlas.crmapp.network;

/**
 * Created by Harry on 2017-03-22.
 */

public class DcnException extends Exception {

    private int mCode = 0;
    private String mDescription = "";

    public DcnException() {
        super("");
        mCode = 0;
        mDescription = "";
    }

    public DcnException(int code, String message) {
        super(message);
        mCode = code;
        mDescription = "";
    }

    public DcnException(int code, String message, String description) {
        super(message);
        mCode = code;
        mDescription = description;

    }

    public DcnException(Exception e) {
        super(e.getMessage());
        mCode = -1;
        mDescription = "";
    }

    public int getCode() {
        return mCode;
    }

    public void setCode(int mCode) {
        this.mCode = mCode;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }
}
