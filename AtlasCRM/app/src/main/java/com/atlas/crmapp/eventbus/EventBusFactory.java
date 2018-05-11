package com.atlas.crmapp.eventbus;



import android.support.annotation.IntDef;
import android.util.SparseArray;

import org.greenrobot.eventbus.EventBus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by hoda on 2017/7/12.
 * http://www.jianshu.com/p/31e3528ca7e5
 */
/**
 * 方法1
 * 用annotation配合使用工厂
 * EventBusFactory.getBus(EventBusFactory.START);
 * EventBusFactory.getBus();
 */
public class EventBusFactory {
    private static SparseArray<EventBus> mBusSparseArray = new SparseArray<>(2);

    @IntDef({CREATE, START})
    @Retention(RetentionPolicy.SOURCE)
    public @interface BusType {
    }

    public static final int CREATE = 0;
    public static final int START = 1;

    static {
        mBusSparseArray.put(CREATE, EventBus.builder().build());
        mBusSparseArray.put(START, EventBus.getDefault());
    }

    public static EventBus getBus() {
        return getBus(START);
    }

    public static EventBus getBus(@BusType int type) {
        return mBusSparseArray.get(type);
    }

}
