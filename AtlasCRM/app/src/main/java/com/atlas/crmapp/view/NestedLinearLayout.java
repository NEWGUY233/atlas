package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.NestedScrollingParent;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by hoda on 2017/9/21.
 */

public class NestedLinearLayout extends LinearLayout implements NestedScrollingParent{
    public NestedLinearLayout(Context context) {
        super(context);
    }

    public NestedLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NestedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }








}
