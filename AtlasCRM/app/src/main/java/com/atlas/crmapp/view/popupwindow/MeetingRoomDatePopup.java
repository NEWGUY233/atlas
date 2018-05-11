package com.atlas.crmapp.view.popupwindow;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.wheel.NumericWheelAdapter;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.view.wheel.WheelView;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by hoda
 */

public class MeetingRoomDatePopup extends BasePopupWindow {

    private WheelView hour;
    private WheelView mins;
    private View contentView;
    private Context context;
    private String curTime;
    private OnClickOkListener onClickOkListener;
    private final int VISIBLE_ITEMS_SUM = 7;


    public MeetingRoomDatePopup(Activity context, String curTime, OnClickOkListener onClickOkListener) {
        super(context);
        this.context = context;
        this.onClickOkListener = onClickOkListener;
        this.curTime = curTime;
        bindEvent();

    }

    private void bindEvent(){
        hour = (WheelView) contentView.findViewById(R.id.hour);
        initHour();
        mins = (WheelView) contentView.findViewById(R.id.mins);
        initMins();

        if (TextUtils.isEmpty(curTime)) {
            return;
        }
        curTime = curTime.trim();
        final String[] curTimes = curTime.split(":");
        if (curTimes.length != 2) {
            return;
        }
        hour.setCurrentItem(Integer.valueOf(curTimes[0]));
        mins.setCurrentItem((int) Math.round(Integer.valueOf(curTimes[1]) / 15.0));
        hour.setVisibleItems(VISIBLE_ITEMS_SUM);
        mins.setVisibleItems(VISIBLE_ITEMS_SUM);
        contentView.findViewById(R.id.set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectDate = FormatCouponInfo.getFormatTime(hour.getCurrentItem(), mins.getCurrentItem() * 15);
                onClickOkListener.onClickOk(selectDate);
            }
        });
        contentView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return contentView.findViewById(R.id.rl_click_dismiss_visit);
    }

    @Override
    public View onCreatePopupView() {
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.popup_meeting_room_date, null);
        return contentView;
    }

    @Override
    public View initAnimaView() {
        return contentView.findViewById(R.id.ll_main);
    }




    /**
     * 初始化时
     */
    private void initHour() {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(context, 0, 23, "%02d");
        numericWheelAdapter.setLabel(" " + getContext().getString(R.string.hour));
        hour.setViewAdapter(numericWheelAdapter);
        hour.setCyclic(true);
    }

    /**
     * 初始化分
     */
    private void initMins() {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(context, 0, 3, "%02d", 15);
        numericWheelAdapter.setLabel(" " + getContext().getString(R.string.min));
        mins.setViewAdapter(numericWheelAdapter);
        mins.setCyclic(true);
    }





    public interface OnClickOkListener{
        void onClickOk(String selectDate);
    }

    @Override
    protected Animator initExitAnimator() {
        return ObjectAnimator.ofFloat(mAnimaView, "translationY", 0, 400).setDuration(600);
    }
}
