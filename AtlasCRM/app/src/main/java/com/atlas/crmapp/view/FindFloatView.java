package com.atlas.crmapp.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.atlas.crmapp.R;

/**
 * Created by hoda on 2017/6/28.
 */

@SuppressLint("AppCompatCustomView")
public class FindFloatView extends RelativeLayout{
    private Context context;


    public FindFloatView(Context context) {
        super(context);
        initViews(context);
    }

    public FindFloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public FindFloatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FindFloatView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_find_float, this, true);
    }

    public void updateView(boolean isShowRedDot){
        findViewById(R.id.iv_find_red_dot).setVisibility(isShowRedDot == true ?VISIBLE : GONE);
    }
}


