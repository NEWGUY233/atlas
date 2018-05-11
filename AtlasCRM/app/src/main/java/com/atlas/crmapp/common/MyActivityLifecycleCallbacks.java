package com.atlas.crmapp.common;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.orhanobut.logger.Logger;

import java.util.LinkedList;

/**
 * Created by hoda on 2017/7/31.
 */

public class MyActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    private IAppBackToGroundForeGround iAppBackToGroundForeGround;
    private int mCount;
    // 此处采用 LinkedList作为容器，增删速度快
    public LinkedList<Activity> activityLinkedList = new LinkedList<>();

    public void setiAppBackToGroundForeGround(IAppBackToGroundForeGround iAppBackToGroundForeGround){
        this.iAppBackToGroundForeGround = iAppBackToGroundForeGround;
    }

    /**
     * application下的每个Activity声明周期改变时，都会触发以下的函数。 
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        //如何区别参数中activity代表你写的哪个activity。
        Logger.w("current activity  " +  activity.getLocalClassName());
        activityLinkedList.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        //Logger.d("onActivityStarted.");
        mCount++;
        //如果mCount==1，说明是从后台到前台
        if (mCount == 1) {
            //执行app跳转到前台的逻辑
            Logger.d("执行app跳转到前台的逻辑 ");
            if(iAppBackToGroundForeGround != null){
                iAppBackToGroundForeGround.isBgToFg(true, activity);
            }
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        //Logger.d("onActivityResumed.");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        //Logger.d("onActivityPaused.");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        //Logger.d("onActivityStopped.");
        mCount--;
        //如果mCount==0，说明是前台到后台
        if (mCount == 0){
            //执行应用切换到后台的逻辑
            Logger.d("执行应用切换到后台的逻辑");
            if(iAppBackToGroundForeGround != null){
                iAppBackToGroundForeGround.isBgToFg(false, activity);
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //Logger.d("onActivityDestroyed.");
        activityLinkedList.remove(activity);
    }

    public interface IAppBackToGroundForeGround{
        /**
         * 应用恢复前台，退后台时调用
         * @param isBgToFg
         * @param currActivity
         */
        void isBgToFg(boolean isBgToFg, Activity currActivity);
    }

    public  void exitApp(){
        for(Activity activity : activityLinkedList){
            Logger.d("exit activity is " + activity.getLocalClassName());
            activity.finish();
        }
    }


};


