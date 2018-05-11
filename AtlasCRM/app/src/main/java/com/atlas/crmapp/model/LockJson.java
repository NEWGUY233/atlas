package com.atlas.crmapp.model;

import java.io.Serializable;

/**
 * Created by hoda on 2017/10/10.
 */

public class LockJson implements Serializable{


    private static final long serialVersionUID = 5763636951315906856L;
    /**
     * doorId : 186
     * uuid : 00096547
     * doorName : 30-B17
     *"doorType": "gzzk",
     *"collected": true

     */

    private long doorId;
    private String uuid;
    private String doorName;
    private boolean collected;
    private String doorType;

    public long getDoorId() {
        return doorId;
    }

    public void setDoorId(long doorId) {
        this.doorId = doorId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDoorName() {
        return doorName;
    }

    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public String getDoorType() {
        return doorType;
    }

    public void setDoorType(String doorType) {
        this.doorType = doorType;
    }
}
