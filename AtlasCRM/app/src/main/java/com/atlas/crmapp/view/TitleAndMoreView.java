package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;


/**
 * Created by Hoda_fang on 2017/4/30.
 *
 * item  标题跟 更多
 */

public class TitleAndMoreView extends RelativeLayout {
    private View rlMian;
    private TextView tvTitle;
    private TextView tvMore;
    private String title;
    //private View vMore;
    public TitleAndMoreView(Context context) {
        super(context);
        initView(context);
    }

    public TitleAndMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TitleAndMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TitleAndMoreView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context){
        rlMian = LayoutInflater.from(context).inflate(R.layout.view_title_and_more, this,true);
        tvTitle = (TextView) rlMian.findViewById(R.id.tv_title);
        tvMore = (TextView) rlMian.findViewById(R.id.tv_more);
        //vMore = rlMian.findViewById(R.id.ll_more);
    }


    public void updateViews(String title){
        if(!TextUtils.isEmpty(title)){
            tvTitle.setText(title);
        }
    }

    private void updateViews(String title, String more){
        if(!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(more)){
            tvTitle.setText(title);
            tvMore.setText(more);
    }


    }
}
