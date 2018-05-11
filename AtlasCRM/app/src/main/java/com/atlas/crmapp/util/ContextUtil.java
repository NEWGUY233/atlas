package com.atlas.crmapp.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2018/4/10.
 */

public class ContextUtil {
    private Context context;
    public void setContext(Application context){
        this.context = context;
    }

    private static ContextUtil util;
    private ContextUtil(){

    }

    public synchronized static ContextUtil getUtil(){
        if (util == null)
            util = new ContextUtil();
        return util;
    }

    public Context getContext(){
        return this.context;
    }
}
