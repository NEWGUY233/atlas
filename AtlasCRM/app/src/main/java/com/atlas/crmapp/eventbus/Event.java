package com.atlas.crmapp.eventbus;

/**
 * Created by hoda on 2017/7/12.
 */

public class Event  {
    public static class CheckConfirmOrder {
        public long timestamp;
        public boolean isStopCheckThread;
    }

    public static class EventObject{
        public String type;
        public Object object;

        public EventObject(String type, Object object) {
            this.type = type;
            this.object = object;
        }
    }

}

