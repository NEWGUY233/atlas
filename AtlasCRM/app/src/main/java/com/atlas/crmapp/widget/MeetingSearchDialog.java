package com.atlas.crmapp.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.wheel.NumericWheelAdapter;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.view.wheel.OnWheelScrollListener;
import com.atlas.crmapp.view.wheel.WheelView;
import com.orhanobut.logger.Logger;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.shaohui.bottomdialog.BottomDialog;

/**
 * Created by Alex on 2017/5/9.
 */

public class MeetingSearchDialog extends DialogFragment {
    private final int VISIBLE_ITEMS_SUM = 7;
    private Unbinder unbinder;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    private WheelView hour;
    private WheelView mins;
    int curYear, curMonth, curDate ,curHour, curMins;
    private DialogFragment startDateDialog;

    @BindView(R.id.et_people)
    EditText mEtPeople;

    @BindView(R.id.tv_date)
    TextView mTvDate;

    @BindView(R.id.tv_search)
    TextView tvSearch;

    @OnClick(R.id.tv_search)
    void onSearch(){
        Intent intent = getActivity().getIntent();
        intent.putExtra("amount",Integer.valueOf(mEtPeople.getText().toString()));
        intent.putExtra("date",mTvDate.getText().toString());

        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
    }

    @OnClick(R.id.tv_cancel)
    void  onClickCancel(){
        this.dismiss();
    }

    @OnClick(R.id.rl_item_date)
    void onClick(){

        startDateDialog = BottomDialog.create(getFragmentManager())
                .setViewListener(new BottomDialog.ViewListener() {
                    @Override
                    public void bindView(View view) {
                        year = (WheelView) view.findViewById(R.id.year);
                        initYear(curYear+1);
                        month = (WheelView) view.findViewById(R.id.month);
                        initMonth();
                        day = (WheelView) view.findViewById(R.id.day);
                        initDay(curYear,curMonth);
                        hour = (WheelView) view.findViewById(R.id.hour);
                        initHour();
                        mins = (WheelView) view.findViewById(R.id.mins);
                        initMins();
                        year.setCurrentItem(0);
                        month.setCurrentItem(curMonth - 1);
                        day.setCurrentItem(curDate - 1);
                        int minScale = (int) Math.ceil(curMins/15.0);
                        hour.setCurrentItem(minScale == 4?curHour+1: curHour);
                        mins.setCurrentItem(minScale < 4 ? minScale : 0);
                        year.setVisibleItems(VISIBLE_ITEMS_SUM);
                        month.setVisibleItems(VISIBLE_ITEMS_SUM);
                        day.setVisibleItems(VISIBLE_ITEMS_SUM);
                        hour.setVisibleItems(VISIBLE_ITEMS_SUM);
                        mins.setVisibleItems(7);
                        year.addScrollingListener(scrollListener);
                        month.addScrollingListener(scrollListener);
                        // 设置监听
                        TextView ok = (TextView) view.findViewById(R.id.set);
                        TextView cancel = (TextView) view.findViewById(R.id.cancel);

                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String selectDate= FormatCouponInfo.getFormatDate(year.getCurrentItem()+curYear, month.getCurrentItem()+1,day.getCurrentItem()+1, hour.getCurrentItem(),mins.getCurrentItem()*15);
                                Logger.d("selectDate---" + selectDate);
                                if(FormatCouponInfo.isBeyondLimtDay(15, selectDate)){
                                    Toast.makeText(getActivity(),getActivity().getString(R.string.limt_select_time_district), Toast.LENGTH_LONG).show();
                                }else{
                                    startDateDialog.dismiss();
                                    mTvDate.setText(selectDate);
                                }

                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startDateDialog.dismiss();
                            }
                        });
                        LinearLayout cancelLayout = (LinearLayout) view.findViewById(R.id.view_none);
                        cancelLayout.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                startDateDialog.dismiss();
                                return false;
                            }
                        });

                    }
                })
                .setLayoutRes(R.layout.view_data_and_time_picker)
                .setDimAmount(0.5f)
                .setCancelOutside(true)
                .setTag("DateDialog")
                .show();
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

    public interface SearchListener
    {
        void onSearchComplete(String amount, String date);
    }

    public static MeetingSearchDialog newInstance() {
        return new MeetingSearchDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_dialog_meeting_search, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSearchCanPass(true);
        mEtPeople.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String personNum = charSequence.toString();
                if(StringUtils.isEmpty(personNum)){
                    setSearchCanPass(false);

                }else{
                    if(Integer.parseInt(personNum) == 0){
                        setSearchCanPass(false);
                    }else{
                        setSearchCanPass(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setSearchCanPass(boolean canPass ){
        if(canPass){
            tvSearch.setClickable(true);
            tvSearch.setBackgroundResource(R.color.btn_yellow);
            tvSearch.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_color));
        }else{
            tvSearch.setClickable(false);
            tvSearch.setBackgroundResource(R.color.white_color);
            tvSearch.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark));
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        /*final DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        final WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.width = dm.widthPixels;
        layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.meeting_item);
        layoutParams.gravity = Gravity.TOP;
        getDialog().getWindow().setAttributes(layoutParams);*/

        Calendar c = Calendar.getInstance();
        curYear = c.get(Calendar.YEAR);
        curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
        curDate = c.get(Calendar.DATE);
        curHour = c.get(Calendar.HOUR_OF_DAY);
        curMins = c.get(Calendar.MINUTE);
        mTvDate.setText(FormatCouponInfo.getFormatDate(curYear, curMonth, curDate , curHour, curMins));

    }



    /**
     * 初始化年
     */
    private void initYear(int curYear) {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(getActivity(),MeetingSearchDialog.this.curYear, curYear);
        numericWheelAdapter.setLabel(" " + getString(R.string.year));
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        year.setViewAdapter(numericWheelAdapter);
        year.setCyclic(true);
    }

    /**
     * 初始化月
     */
    private void initMonth() {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(getActivity(),1, 12, "%02d");
        numericWheelAdapter.setLabel(" " + getString(R.string.month));
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        month.setViewAdapter(numericWheelAdapter);
        month.setCyclic(true);
    }

    /**
     * 初始化天
     */
    private void initDay(int curYear, int curMonth) {
        NumericWheelAdapter numericWheelAdapter=new NumericWheelAdapter(getActivity(),1, DateUtil.getDay(curYear, curMonth), "%02d");
        numericWheelAdapter.setLabel(" " + getString(R.string.day));
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        day.setViewAdapter(numericWheelAdapter);
        day.setCyclic(true);
    }

    /**
     * 初始化时
     */
    private void initHour() {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(getActivity() ,0, 23, "%02d");
        numericWheelAdapter.setLabel(" " + getString(R.string.hour));
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        hour.setViewAdapter(numericWheelAdapter);
        hour.setCyclic(true);
    }

    /**
     * 初始化分
     */
    private void initMins() {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(getActivity(),0, 3, "%02d",15);
        numericWheelAdapter.setLabel(" " + getString(R.string.min));
//		numericWheelAdapter.setTextSize(15);  设置字体大小
        mins.setViewAdapter(numericWheelAdapter);
        mins.setCyclic(true);
    }

}