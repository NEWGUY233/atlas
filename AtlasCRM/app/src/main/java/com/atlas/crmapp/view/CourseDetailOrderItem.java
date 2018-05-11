package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.ClassJson;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.FormatCouponInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hoda_fang on 2017/5/31.
 */

public class CourseDetailOrderItem extends LinearLayout {
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.tv_venue)
    TextView mTvVenue;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_coach)
    TextView mTvCoach;
    @BindView(R.id.tv_leave)
    TextView mTvLeave;
    @BindView(R.id.btn_booking)
    Button mBtnBooking;
    @BindView(R.id.v_divider)
    View vDivider;

    private Context context;
    public CourseDetailOrderItem(Context context) {
        super(context);
        initViews(context);
    }

    public CourseDetailOrderItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public CourseDetailOrderItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CourseDetailOrderItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        this.context = context;
    }

    public void updateViews(ClassJson classJson, View.OnClickListener onClickBooking, boolean isFitContractOrAllowance, boolean isShowDivider){
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_fitness_detail, this, true);
        ButterKnife.bind(this, itemView);
        mTvDate.setText(DateUtil.times(classJson.startTime, getContext().getString(R.string.s83)));
        mTvVenue.setText(classJson.venue);
        mTvPrice.setText(FormatCouponInfo.getFitContractOrAllowancePrice(classJson.price, 0, isFitContractOrAllowance));
        mTvTime.setText(DateUtil.times(classJson.startTime, "HH:mm") + "-" + DateUtil.times(classJson.endTime, "HH:mm"));
        mTvCoach.setText(classJson.coachName+ " "+ classJson.major);
        mTvLeave.setText(getContext().getString(R.string.s84) +  String.valueOf(classJson.maxSize - classJson.bookNum));
        mBtnBooking.setTag(classJson);
        mBtnBooking.setOnClickListener(onClickBooking);
        if(isShowDivider){
            vDivider.setVisibility(VISIBLE);
        }else{
            vDivider.setVisibility(GONE);
        }

    }

}
