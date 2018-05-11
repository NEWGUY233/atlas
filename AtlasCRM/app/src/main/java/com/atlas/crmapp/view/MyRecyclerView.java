package com.atlas.crmapp.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.orhanobut.logger.Logger;

/**
 * Created by hoda on 2017/10/20.
 */

public class MyRecyclerView extends RecyclerView {

    private boolean isHorizontal;//是否横向滑动
    float downY, downX;

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isHorizontal = false;
                downY = event.getRawY();
                downX = event.getRawX();
                Logger.d( "down : " + downY);
                break;
            case MotionEvent.ACTION_MOVE:
                float lastY = event.getRawY();
                float lastX = event.getRawX();
                float absY =  Math.abs(downY - lastY);
                float absX =  Math.abs(downX - lastX);
                if(absX > absY  && !isHorizontal){
                    isHorizontal = true;
                }
                Log.d("abs :", " " +  absY);
                if(isHorizontal){/*absY < 80*/
                    return false;// 横向滑动不拦截事件 ，然子控件处理。否则子控件会产生MotionEvent.ACTION_CANCEL 事件
                }
                break;
        }


         return super.onInterceptTouchEvent(event);
    }


}
