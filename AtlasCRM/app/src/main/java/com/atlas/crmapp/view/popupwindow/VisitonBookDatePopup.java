package com.atlas.crmapp.view.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.wheel.NumericWheelAdapter;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.view.wheel.OnWheelScrollListener;
import com.atlas.crmapp.view.wheel.WheelView;
import com.orhanobut.logger.Logger;

import java.util.Calendar;

import butterknife.Unbinder;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by hoda
 */

public class VisitonBookDatePopup extends BasePopupWindow {

    private final int VISIBLE_ITEMS_SUM = 7;
    private Unbinder unbinder;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    int curYear, curMonth, curDate ;
    private View contentView;
    private Context context;
    private OnClickOkListener onClickOkListener;


    public VisitonBookDatePopup(Activity context, OnClickOkListener onClickOkListener) {
        super(context);
        this.context = context;
        this.onClickOkListener = onClickOkListener;
        bindEvent();

    }

    private void bindEvent(){

        Calendar c = Calendar.getInstance();
        curYear = c.get(Calendar.YEAR);
        curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
        curDate = c.get(Calendar.DATE);

        year = (WheelView) contentView.findViewById(R.id.year);
        initYear(curYear+1);
        month = (WheelView) contentView.findViewById(R.id.month);
        initMonth();
        day = (WheelView) contentView.findViewById(R.id.day);
        initDay(curYear,curMonth);

        year.setCurrentItem(0);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);

        year.setVisibleItems(VISIBLE_ITEMS_SUM);
        month.setVisibleItems(VISIBLE_ITEMS_SUM);
        day.setVisibleItems(VISIBLE_ITEMS_SUM);

        year.addScrollingListener(scrollListener);
        month.addScrollingListener(scrollListener);
        // 设置监听
        TextView ok = (TextView) contentView.findViewById(R.id.set);
        TextView cancel = (TextView) contentView.findViewById(R.id.cancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectDate = FormatCouponInfo.getFormatDate(year.getCurrentItem()+curYear, month.getCurrentItem()+1, day.getCurrentItem()+1);
                Logger.d("selectDate---" + selectDate);
                if(FormatCouponInfo.isBeyondLimtDay(30, selectDate + " 23:59")){
                    Toast.makeText(context, context.getString(R.string.limt_select_time_district_30), Toast.LENGTH_LONG).show();
                }else{
                    if(onClickOkListener != null){
                        onClickOkListener.onClickOk(selectDate);
                    }
                    dismiss();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.popup_visition_book_date, null);
        return contentView;
    }

    @Override
    public View initAnimaView() {
        return contentView.findViewById(R.id.ll_main);
    }


    /**
     * 活动结束的监听  用于刷新日期
     */
    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            initDay(year.getCurrentItem()+ curYear, month.getCurrentItem()+1);
        }
    };


    /**
     * 初始化年
     */
    private void initYear(int curYear) {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(context, VisitonBookDatePopup.this.curYear, curYear);
        numericWheelAdapter.setLabel(" 年");
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        year.setViewAdapter(numericWheelAdapter);
        year.setCyclic(true);
    }

    /**
     * 初始化月
     */
    private void initMonth() {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(context ,1, 12, "%02d");
        numericWheelAdapter.setLabel(" 月");
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        month.setViewAdapter(numericWheelAdapter);
        month.setCyclic(true);
    }

    /**
     * 初始化天
     */
    private void initDay(int curYear, int curMonth) {
        NumericWheelAdapter numericWheelAdapter=new NumericWheelAdapter(context,1, DateUtil.getDay(curYear, curMonth), "%02d");
        numericWheelAdapter.setLabel(" 日");
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        day.setViewAdapter(numericWheelAdapter);
        day.setCyclic(true);
    }




    public interface OnClickOkListener{
        void onClickOk(String selectDate);
    }



}
