package com.atlas.crmapp.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.orhanobut.logger.Logger;

/**
 * Created by hoda on 2017/9/22.
 */

public class ObserverRecyclerView extends RecyclerView {

    private OnTopBarShowListener onTopBarShowListener;
    private boolean isShow;
    public void setOnTopBarShowListener(OnTopBarShowListener onTopBarShowListener) {
        this.onTopBarShowListener = onTopBarShowListener;
    }
    public ObserverRecyclerView(Context context) {
        super(context);
    }

    public ObserverRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ObserverRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    int oldDy = 0;
    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        oldDy = dy + oldDy;
        Logger.d("dx " + dx + " dy " + dy + " " + getHeight()) ;


        if(onTopBarShowListener != null){
            setTopBarTransparent(oldDy, onTopBarShowListener, false);
        }
    }

    public interface OnTopBarShowListener{
        void onTopBarShow(boolean isShow, float alpha);
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
      /*  if(isGetToBottom && ! isShow){
            isShow = true;
            onTopBarShowListener.onTopBarShow(true, 1 );
        }*/
    }


    public  boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

}
