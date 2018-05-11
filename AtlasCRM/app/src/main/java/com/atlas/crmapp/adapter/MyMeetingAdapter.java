package com.atlas.crmapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.ResponseMyAppointmentJson;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by A.Developer on 2017/3/22.
 */

public class MyMeetingAdapter extends BaseQuickAdapter<ResponseMyAppointmentJson.MyAppointment, BaseViewHolder> {

    private int meetingGreen , completeGray, simpleGray;
    private Date selectDate;
    private String dayOfMonth = "" ;
    Context context;

    public MyMeetingAdapter(Context context, List<ResponseMyAppointmentJson.MyAppointment> data) {
        super(R.layout.item_my_meeting, data);
        this.context = context;
        meetingGreen = ContextCompat.getColor(context, R.color.light_green);
        completeGray = ContextCompat.getColor(context, R.color.gray_simple);
        simpleGray = ContextCompat.getColor(context, R.color.gray_simple);
    }


    public void setSelectDate(Date selectDate) {
        this.selectDate = selectDate;
        Calendar cal= Calendar.getInstance();
        cal.setTime(selectDate);
        dayOfMonth = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        Logger.d(cal.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    protected void convert(BaseViewHolder helper, ResponseMyAppointmentJson.MyAppointment item) {
        String title = FormatCouponInfo.getAppointmentTitle(item.getType());

        helper.setText(R.id.meeting_title, item.getRefName())
                .setText(R.id.meeting_location, title)
                .setText(R.id.meeting_user, item.getUserName())
                .setText(R.id.tv_month,  dayOfMonth)
                .setText(R.id.meeting_time, DateUtil.formatTime(item.getStartTime(), "HH:mm")+"-"+DateUtil.formatTime(item.getEndTime(), "HH:mm"));
        TextView tvState = helper.getView(R.id.meeting_status);
        CardView cvItemBg = helper.getView(R.id.cv_item_bg);
        TextView tvUser = helper.getView(R.id.meeting_user);

        TextView tvWeek = helper.getView(R.id.tv_week);
        tvWeek.setText(DateUtil.getWeek(selectDate.getTime()));
        if("COMPLETE".equalsIgnoreCase(item.getStatus())){
            tvState.setText(context.getString(R.string.status_done));
            cvItemBg.setCardBackgroundColor(completeGray);
            tvState.setTextColor(simpleGray);
            tvUser.setTextColor(simpleGray);
        }else{
            tvState.setText(context.getString(R.string.text_16));
            cvItemBg.setCardBackgroundColor(meetingGreen);
            tvState.setTextColor(meetingGreen);
            tvUser.setTextColor(meetingGreen);

        }
    }
}
