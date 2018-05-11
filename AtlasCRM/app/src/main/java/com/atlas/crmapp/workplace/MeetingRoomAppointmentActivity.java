package com.atlas.crmapp.workplace;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.wheel.NumericWheelAdapter;
import com.atlas.crmapp.coffee.OrderConfirmActivity;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.eventbus.Event;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.model.AddedValueJson;
import com.atlas.crmapp.model.MeetingRoomJson;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.model.MeetingRoomComboJson;
import com.atlas.crmapp.model.bean.AddedValueModel;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.view.AddedValueSelectPriceView;
import com.atlas.crmapp.view.popupwindow.AddedValuePriceTablePopup;
import com.atlas.crmapp.view.wheel.WheelView;
import com.atlas.crmapp.workplace.view.MeetingScheduleView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import me.shaohui.bottomdialog.BaseBottomDialog;
import me.shaohui.bottomdialog.BottomDialog;

public class MeetingRoomAppointmentActivity extends BaseStatusActivity {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_person)
    TextView tvPerson;
    @BindView(R.id.tv_device_detail)
    TextView tvDeviceDetail;
    @BindView(R.id.schedule_view_bar)
    MeetingScheduleView scheduleViewBar;
    @BindView(R.id.iv_banner)
    ImageView ivBanner;
    @BindView(R.id.tv_added_value)
    TextView tvAddedValue;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_people)
    EditText etPeopleNum;
    @BindView(R.id.et_require)
    EditText etOtherRequire;
    @BindView(R.id.tv_startdate)
    TextView tvStartDate;
    @BindView(R.id.tv_enddate)
    TextView tvEndDate;

    String id;
    String date;

    private BaseBottomDialog dateDialog;
    private WheelView hour;
    private WheelView mins;

    private final int DATE_DIALOG_TYPE_START = 0;
    private final int DATE_DIALOG_TYPE_END = 1;
    private final int VISIBLE_ITEMS_SUM = 7;

    private String startTime = "09:00";
    private String endTime = "10:00";
    private MeetingRoomJson data;
    private List<AddedValueJson> addedValueJsons = new ArrayList<>();
    private AddedValueJson addedValueJson;

    private AddedValueSelectPriceView.OnItemSelect onItemSelect;
    private AddedValuePriceTablePopup priceTablePopup;
    private List<MeetingRoomComboJson> roomComboJsons;
    private MeetingRoomComboJson roomComboJson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        umengPageTitle = getString(R.string.umeng_lesson_list);
        EventBusFactory.getBus().register(this);

        setContentView(R.layout.activity_meeting_room_appointment);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        date = getIntent().getStringExtra("date");
        date = date + " ";
        if (System.currentTimeMillis() > DateUtil.dataOne(date + startTime)) {
            startTime = FormatCouponInfo.getMeetingStartTime(true);
            endTime = FormatCouponInfo.getMeetingStartTime(false);
        }
        setStartEndTime();
        prepareActivityData();

    }



    @OnClick({R.id.tv_added_value, R.id.btn_booking, R.id.tv_price_table, R.id.ibBack})
    void onClick(View view){
        switch (view.getId()){

            case R.id.tv_added_value://增值服务
                AddedValueServiceActivity.newInstance(MeetingRoomAppointmentActivity.this, id, startTime.trim() + "-" + endTime.trim() ,addedValueJsons);

                break;

            case R.id.btn_booking://确定预约
                onSubmit();
                break;

            case R.id.tv_price_table://价目表
                if(onItemSelect == null){
                    onItemSelect =  new AddedValueSelectPriceView.OnItemSelect() {
                        @Override
                        public void onSelect(MeetingRoomComboJson combo) {
                            if(priceTablePopup != null ){
                                priceTablePopup.dismiss();
                            }
                            if(combo != null){
                                MeetingRoomAppointmentActivity.this.roomComboJson = combo;
                                if(StringUtils.isNotEmpty(combo.getPeroid())){
                                    String preoid[] = combo.getPeroid().split("-");
                                    if(preoid!= null && preoid.length == 2){
                                        startTime = preoid[0];
                                        endTime = preoid[1];
                                    }
                                    setStartEndTime();
                                    updateAddedValueClear();
                                }
                            }else{

                            }
                        }
                    };
                }

                if(priceTablePopup == null){
                    long unitPrice = 0;
                    if(data != null){
                        unitPrice = (long) data.facility.price;
                    }
                    priceTablePopup = new AddedValuePriceTablePopup(this, roomComboJsons, unitPrice , onItemSelect );
                }
                hideKeyboard(tvPrice);
                priceTablePopup.setPopupWindowFullScreen(true);
                priceTablePopup.showPopupWindow();
                break;

            case R.id.ibBack:
                MeetingRoomAppointmentActivity.this.finish();
                break;
        }
    }


    @OnTouch({R.id.textView18, R.id.tv_startdate})
    boolean onSelectedStart(View view, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            showTimeDialog(startTime, DATE_DIALOG_TYPE_START);
        }
        return true;
    }

    @OnTouch({R.id.textView8, R.id.tv_enddate})
    boolean onSelectedEnd(View view, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            showTimeDialog(endTime, DATE_DIALOG_TYPE_END);
        }
        return true;
    }


    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }

    @Override
    protected boolean translucentStatusBar() {
        return true;
    }



    private void onSubmit(){
        if(GlobalParams.getInstance().isLogin()){
            createMeetingRoomBooking(Long.valueOf(id), etTitle.getText().toString(), DateUtil.dataOne(date + startTime), DateUtil.dataOne(date + endTime));
        }else{
            showAskLoginDialog();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPaySuccessFinish(Event.EventObject eventObject){
        if(eventObject != null && Constants.EventType.ORDER_COMPLETE.equals(eventObject.type)){
            this.finish();
        }

        if(Constants.EventType.ADDED_VALUE_BACK.equals(eventObject.type) && eventObject != null){
            addedValueJsons.clear();
            addedValueJsons.addAll((Collection<? extends AddedValueJson>) eventObject.object);
            updateAddedValue();
        }
    }


    private void updateAddedValue(){
        tvAddedValue.setText(FormatCouponInfo.getAddedValueText(addedValueJsons));
    }

    private void updateAddedValueClear(){
        addedValueJsons.clear();
        tvAddedValue.setText(FormatCouponInfo.getAddedValueText(addedValueJsons));
    }

    private void showTimeDialog(String curTime, final int type) {

        if (TextUtils.isEmpty(curTime)) {
            return;
        }
        curTime = curTime.trim();
        final String[] curTimes = curTime.split(":");
        if (curTimes.length != 2) {
            return;
        }


        dateDialog = BottomDialog.create(getSupportFragmentManager())
                .setViewListener(new BottomDialog.ViewListener() {
                    @Override
                    public void bindView(View view) {


                        hour = (WheelView) view.findViewById(R.id.hour);
                        initHour();
                        mins = (WheelView) view.findViewById(R.id.mins);
                        initMins();
                        hour.setCurrentItem(Integer.valueOf(curTimes[0]));
                        mins.setCurrentItem((int) Math.round(Integer.valueOf(curTimes[1]) / 15.0));
                        hour.setVisibleItems(VISIBLE_ITEMS_SUM);
                        mins.setVisibleItems(VISIBLE_ITEMS_SUM);
                        // 设置监听
                        TextView ok = (TextView) view.findViewById(R.id.set);
                        TextView cancel = (TextView) view.findViewById(R.id.cancel);


                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String selectDate = FormatCouponInfo.getFormatTime(hour.getCurrentItem(), mins.getCurrentItem() * 15);
                                String strStartTime = date + selectDate;
                                String strEndTime = date + endTime;
                                if (type == DATE_DIALOG_TYPE_START) {
                                    if (DateUtil.dataOne(strStartTime) < System.currentTimeMillis()) {
                                        Toast.makeText(MeetingRoomAppointmentActivity.this, getString(R.string.need_beyond_current_time), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    if(!strEndTime.equals(selectDate)){
                                        updateAddedValueClear();
                                    }
                                    startTime = selectDate;
                                    if (isBeyondLimt(strStartTime, date + endTime)) {
                                        endTime = FormatCouponInfo.getMeetingEndTIme(startTime);
                                        tvEndDate.setText(date + endTime);
                                        //Toast.makeText(MeetingRoomAppointmentActivity.this, getString(R.string.limt_order_hour),Toast.LENGTH_LONG).show();
                                    } else {

                                    }
                                    tvStartDate.setText(date + startTime);
                                } else if (type == DATE_DIALOG_TYPE_END) {
                                    if (isBeyondLimt(date + startTime, date + selectDate)) {
                                        //Toast.makeText(MeetingRoomAppointmentActivity.this, getString(R.string.limt_order_hour),Toast.LENGTH_LONG).show();
                                        endTime = FormatCouponInfo.getMeetingEndTIme(startTime);
                                    } else {
                                        if(!endTime.equals(selectDate)){
                                            updateAddedValueClear();
                                        }
                                        endTime = selectDate;
                                    }

                                    tvEndDate.setText(date + endTime);
                                }
                                dateDialog.dismiss();
                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dateDialog.dismiss();
                            }
                        });
                        LinearLayout cancelLayout = (LinearLayout) view.findViewById(R.id.view_none);
                        cancelLayout.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                return false;
                            }
                        });

                    }
                })
                .setLayoutRes(R.layout.view_timepick)
                .setDimAmount(0.5f)
                .setCancelOutside(true)
                .setTag("DateDialog")
                .show();
    }

    //时间间隔 最小为一小时
    private boolean isBeyondLimt(String start, String end) {
        return DateUtil.dataOne(end) - DateUtil.dataOne(start) < 1000 * 60 * 60;
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        if(data == null){
            prepareActivityData();
        }else{
            onSubmit();
        }
    }

    private void setStartEndTime(){
        tvStartDate.setText(date + startTime);
        tvEndDate.setText(date + endTime);
    }

    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
        BizDataRequest.requestMeetingRoomInfo(this, id, date, statusLayout, new BizDataRequest.OnMeetingRoomInfo() {
            @Override
            public void onSuccess(MeetingRoomJson meetingRoomJson) {
                data = meetingRoomJson;
                setStartEndTime();
                Message msg = myHandler.obtainMessage() ;
                msg.obj = meetingRoomJson;
                myHandler.sendMessageDelayed(msg, 200);

            }

            @Override
            public void onError(DcnException error) {

            }
        });

        BizDataRequest.requestAddedValue(this, id, "", null, new BizDataRequest.OnResponseAddedValue() {
            @Override
            public void onSuccess(List<AddedValueJson> addedValueJsons) {
                MeetingRoomAppointmentActivity.this.addedValueJsons.clear();
                MeetingRoomAppointmentActivity.this.addedValueJsons.addAll(addedValueJsons);
            }

            @Override
            public void onError(DcnException error) {

            }
        });

        BizDataRequest.requestMeetingroomCombo(this, id, null, new BizDataRequest.OnResponseMeetingroomCombo() {
            @Override
            public void onSuccess(List<MeetingRoomComboJson> combos) {
                MeetingRoomAppointmentActivity.this.roomComboJsons = combos;
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    private void setTvAddedValue(){



    }

    Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MeetingRoomJson meetingRoomJson  = (MeetingRoomJson) msg.obj;
            updateRoomInfo(meetingRoomJson);
        }
    };

    private void updateRoomInfo(MeetingRoomJson meetingRoomJson) {
        MeetingRoomJson.Facility facility = meetingRoomJson.facility;
        tvPrice.setText(FormatCouponInfo.formatDoublePrice(facility.price, 0));
        tvPerson.setText(meetingRoomJson.capacity + "");
        tvName.setText(facility.name);
        tvDeviceDetail.setText(meetingRoomJson.descript == null ? "" : meetingRoomJson.descript );
        GlideUtils.loadCustomImageView(this, R.drawable.product_thum, LoadImageUtils.loadMiddleImage(facility.thumbnail), ivBanner);
        scheduleViewBar.showScheduleView(meetingRoomJson.occupyTimes, meetingRoomJson.openTime, meetingRoomJson.closeTime);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusFactory.getBus().unregister(this);
    }


    //预约会议室
    private void createMeetingRoomBooking(long productId, String title, long startTime, long endTime) {
        List<AddedValueModel> addedValueModels = new ArrayList<>();
        for (AddedValueJson addedValueJson : addedValueJsons ){
            if(addedValueJson.isSelect()){
                AddedValueModel addedValueModel = new AddedValueModel();
                addedValueModel.setCount(1);
                addedValueModel.setId(addedValueJson.getId());
                addedValueModel.setName(addedValueJson.getName());
                if(addedValueJson.getCombos() != null && addedValueJson.getCombos().size() > 0){
                    addedValueModel.setPrice(addedValueJson.getCombos().get(0).getPrice());
                }
                addedValueModels.add(addedValueModel);
            }
        }
        BizDataRequest.requestCreateMeetingRoomBooking(MeetingRoomAppointmentActivity.this, productId, title, startTime, endTime, etPeopleNum.getText().toString().trim(), etOtherRequire.getText().toString().trim(),addedValueModels, statusLayout, new BizDataRequest.OnResponseOpenOrderJson() {
            @Override
            public void onSuccess(ResponseOpenOrderJson responseMyOrderJson) {
                ResponseOpenOrderJson confirmOrder = responseMyOrderJson;
                Intent intent = new Intent(MeetingRoomAppointmentActivity.this, OrderConfirmActivity.class);
                intent.putExtra("type", Constants.ORDER_TYPE.MR);
                intent.putExtra("confirmOrder", (Serializable) confirmOrder);
                startActivityForResult(intent, 999);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }



    /**
     * 初始化时
     */
    private void initHour() {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this, 0, 23, "%02d");
        numericWheelAdapter.setLabel(" " + getString(R.string.hour));
        hour.setViewAdapter(numericWheelAdapter);
        hour.setCyclic(true);
    }

    /**
     * 初始化分
     */
    private void initMins() {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this, 0, 3, "%02d", 15);
        numericWheelAdapter.setLabel(" " + getString(R.string.min));
        mins.setViewAdapter(numericWheelAdapter);
        mins.setCyclic(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999) {
            if (resultCode == 999) {
                setResult(999);
                finish();
            }
        }
    }

}
