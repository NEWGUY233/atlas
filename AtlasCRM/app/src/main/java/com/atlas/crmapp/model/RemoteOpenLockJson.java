package com.atlas.crmapp.model;

/**
 * Created by hoda on 2017/10/10.
 */

public class RemoteOpenLockJson {



    /** 远程开门结果
     * isOpen : true
     * doorId : 186
     */

    private String isOpen;
    private float doorId;

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public float getDoorId() {
        return doorId;
    }

    public void setDoorId(int doorId) {
        this.doorId = doorId;
    }
}
