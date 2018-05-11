package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.ActivityJson;
import com.atlas.crmapp.util.FormatCouponInfo;

/**
 * Created by hoda on 2017/12/12.
 *
 * 活动概要
 */

public class ActivitiesSimpleInfoView  extends LinearLayout{
    private Context context ;
    private TextView tvTitle, tvTime, tvRemainNum, tvAddress;


    public ActivitiesSimpleInfoView(Context context) {
        super(context);
        initViews(context);
    }

    public ActivitiesSimpleInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);

    }

    public ActivitiesSimpleInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ActivitiesSimpleInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);

    }

    private void initViews(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_activities_simple_info, this, true);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvRemainNum = (TextView) findViewById(R.id.tv_remain_num);
    }

    public void updateViews(ActivityJson activityJson){
        updateViews(activityJson, false);
    }


    public void updateViews(ActivityJson activityJson, boolean showedRemainNum){
        if(activityJson != null){
            tvTime.setText(FormatCouponInfo.getActivityValidTime(activityJson.getStartTime(), activityJson.getEndTime()));
            tvAddress.setText(activityJson.getAddress());
            tvTitle.setText(activityJson.getName());
            if(showedRemainNum){
                tvRemainNum.setText(Html.fromHtml(context.getString(R.string.activity_remain_num, activityJson.getRemainNum() + "" )));
            }
        }
    }
}
