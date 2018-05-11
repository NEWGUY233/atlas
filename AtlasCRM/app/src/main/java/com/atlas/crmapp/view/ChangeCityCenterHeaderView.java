package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.atlas.crmapp.R;

/**
 * Created by hoda on 2017/9/23.
 */

public class ChangeCityCenterHeaderView extends LinearLayout{
    private Context context;

    public ChangeCityCenterHeaderView(Context context) {
        super(context);
        initViews(context);
    }

    public ChangeCityCenterHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);

    }

    public ChangeCityCenterHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ChangeCityCenterHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);

    }

    private void initViews(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_change_city_center_header, this, true);
    }
}
