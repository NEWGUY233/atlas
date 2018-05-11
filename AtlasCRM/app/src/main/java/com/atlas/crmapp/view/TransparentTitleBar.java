package com.atlas.crmapp.view;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;

/**
 * Created by hoda on 2017/10/24.
 */

public class TransparentTitleBar extends RelativeLayout {

    private ImageButton ibBack, ibHome;
    private TextView tvTitle, tvRight;


    public TransparentTitleBar(Context context) {
        super(context);
        initViews(context);

    }

    public TransparentTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);

    }

    public TransparentTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TransparentTitleBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        LayoutInflater.from(context).inflate(R.layout.titlebar_tp, this, true);
        ibBack = (ImageButton) findViewById(R.id.ibBack);
        tvTitle = (TextView) findViewById(R.id.textViewTitle);
        ibHome = (ImageButton) findViewById(R.id.ibHome);
        tvRight = (TextView) findViewById(R.id.tvText);
    }

    private void updateViews(Activity activity){

    }


}
