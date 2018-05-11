package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.atlas.crmapp.R;

/**
 * Created by Administrator on 2017/6/9.
 */

public class TopTabLayoutView extends RelativeLayout{
    private Context context;
    public TopTabLayoutView(Context context) {
        super(context);
        initViews(context);
    }

    public TopTabLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public TopTabLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TopTabLayoutView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_top_tab, this, true);
    }
}
