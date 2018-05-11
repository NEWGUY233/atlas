package com.atlas.crmapp.ecosphere.fragments;

import com.atlas.crmapp.model.ActivityJson;

import java.util.Comparator;

/**
 * Created by huangyang on 2017/4/30.
 */

public class ASortClass implements Comparator {
    public int compare(Object arg0, Object arg1) {
        ActivityJson user0 = (ActivityJson) arg0;
        ActivityJson user1 = (ActivityJson) arg1;
        int flag = (user0.getStartTime() + "").compareTo(user1.getStartTime() + "");
        return flag;
    }
}
