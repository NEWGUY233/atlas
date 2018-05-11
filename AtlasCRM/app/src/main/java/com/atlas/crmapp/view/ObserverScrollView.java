package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.orhanobut.logger.Logger;

/**
 * Created by hoda on 2017/9/21.
 */

public class ObserverScrollView extends ScrollView {

    private OnTopBarShowListener onTopBarShowListener;
    private boolean isGetToBottom;
    private boolean isShow;

    public void setOnTopBarShowListener(OnTopBarShowListener onTopBarShowListener) {
        this.onTopBarShowListener = onTopBarShowListener;
    }

    public ObserverScrollView(Context context) {
        super(context);
    }

    public ObserverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObserverScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ObserverScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged( x, y, oldx, oldy);
        if (onTopBarShowListener != null) {

           /* if(y + getHeight() >=  computeVerticalScrollRange())
            {
                isGetToBottom = true;
            }
            else
            {
                isGetToBottom = false;
            }*/
        }

        Log.d("scrollViewListener :" ,"getScrollY():"+ getScrollY() + "  x :"  + x + "  y :" + y + "  oldx:" + oldx + "  oldy:" + oldy);
       /* if(getScrollY() + getHeight() >=  computeVerticalScrollRange())
        {
            Log.d("","------滚动到最下方------");
        }
        else
        {
            Log.d("","没有到最下方");
        }*/
       if(onTopBarShowListener != null){
           setTopBarTransparent(y, onTopBarShowListener, isGetToBottom);
       }

    }


    public interface OnTopBarShowListener{
        void onTopBarShow(boolean isShow, float alpha);
    }

    public interface ScrollViewListener {
        void onScrollChanged(ObserverScrollView scrollView, int x, int y, int oldx, int oldy, boolean isGetToBottom);
    }



    public  void setTopBarTransparent( int y,OnTopBarShowListener onTopBarShowListener, boolean isGetToBottom ){
        if(onTopBarShowListener == null){
            Logger.d("onTopBarShowListener  is null");
            return;
        }
        if(y > 100){
            if(!isShow){
                isShow = true;
                onTopBarShowListener.onTopBarShow(true,  1);
            }
        }else {
            if(isShow){
                isShow = false;
                onTopBarShowListener.onTopBarShow(false, 0);
            }
        }
       /* if(isGetToBottom && ! isShow){
            isShow = true;
            onTopBarShowListener.onTopBarShow(true, 1 );
        }*/
    }



}
