package com.atlas.crmapp.view;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by hoda on 2017/9/28.
 */

public class CheckOverSizeTextView extends TextView {
    public CheckOverSizeTextView(Context context) {
        super(context);
    }

    public CheckOverSizeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckOverSizeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CheckOverSizeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private int getAvailableWidth()
    {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }
    public  boolean isOverFlowed()
    {
        Paint paint = getPaint();
        float width = paint.measureText(getText().toString());
        if (width > getAvailableWidth()) return true;
        return false;
    }

}
