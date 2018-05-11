package com.atlas.crmapp.util;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;

import com.atlas.crmapp.R;
import com.orhanobut.logger.Logger;

/**
 *
 * 滚动顶部 渐变 工具类
 * Created by hoda on 2017/9/21.
 */

public class TopBarScrollTransUtils {
    public static void setTopBarTransparent( int y,OnTopBarShowListener onTopBarShowListener, boolean isGetToBottom ){
        if(onTopBarShowListener == null){
            Logger.d("onTopBarShowListener  is null");
            return;
        }
        if(y > 150){
            onTopBarShowListener.onTopBarShow(true);
        }else {
            onTopBarShowListener.onTopBarShow(false);
        }
        if(isGetToBottom){
            onTopBarShowListener.onTopBarShow(true);
        }
    }

    public interface OnTopBarShowListener{
        void onTopBarShow(boolean isShow);
    }


    public static void setLeftImageRightImage(ImageButton ibLeft, int resLeft, ImageButton ibRight, int resRight){
        setImage(ibLeft, resLeft);
        setImage(ibRight, resRight);
    }

    public static void setImage(ImageButton imageButton, int res){
        if(imageButton != null){
            if(imageButton.getVisibility() != View.VISIBLE){
                imageButton.setVisibility(View.VISIBLE);
            }
            imageButton.setImageResource(res);
        }
    }


    public static void setTitleBarBg(Context context, View mainTitle, boolean isWhite){
        if(mainTitle == null){
            return;
        }
        View line = mainTitle.findViewById(R.id.top_line);
        View textViewTitle = mainTitle.findViewById(R.id.textViewTitle);
        if(mainTitle != null && line != null  && textViewTitle != null){
            if(isWhite){
                mainTitle.setBackgroundColor(Color.WHITE);
                line.setBackgroundColor(ContextCompat.getColor(context, R.color.divider_gray));
                if(textViewTitle.getVisibility() == View.GONE){
                    textViewTitle.setVisibility(View.VISIBLE);
                }
            }else{
                mainTitle.setBackgroundColor(Color.TRANSPARENT);
                line.setBackgroundColor(Color.TRANSPARENT);
                if(textViewTitle.getVisibility() == View.VISIBLE){
                    textViewTitle.setVisibility(View.GONE);
                }
            }
        }
    }


}
