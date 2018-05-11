package com.atlas.crmapp.workplace;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.wheel.NumericWheelAdapter;
import com.atlas.crmapp.coffee.OrderConfirmActivity;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.ContractProductJson;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.view.ProductInfoView;
import com.atlas.crmapp.view.ProductIntroduceView;
import com.atlas.crmapp.view.wheel.OnWheelScrollListener;
import com.atlas.crmapp.view.wheel.WheelView;
import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.shaohui.bottomdialog.BaseBottomDialog;
import me.shaohui.bottomdialog.BottomDialog;

import static me.shaohui.bottomdialog.BottomDialog.create;

public class RentWorkPlaceDetailActivity extends BaseStatusActivity {
    TextView tv_date;
    TextView btnPay;
    Calendar calendar;
    private BaseBottomDialog mPayDialog;
    private final int VISIBLE_ITEMS_SUM = 7;

    @BindView(R.id.iv_header_img)
    ImageView iv_header_img;

    @BindView(R.id.v_product_info)
    ProductInfoView vProductInfo;

    @BindView(R.id.v_produt_inroduce)
    ProductIntroduceView vIntroduce;

    public static final int RESULT_CODE = 101;
    private long productId;
    private ContractProductJson data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_work_place_detail);
        ButterKnife.bind(this);
        setTitle(getString(R.string.product_detail));
        productId = getIntent().getLongExtra("id", 0);
        prepareActivityData();
        calendar = Calendar.getInstance();

    }

    private void showPayBottomDialog() {
        mPayDialog = create(getSupportFragmentManager())
                .setViewListener(new BottomDialog.ViewListener() {
                    @Override
                    public void bindView(View v) {
                        Calendar c = Calendar.getInstance();
                        curYear = c.get(Calendar.YEAR);
                        curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
                        curDate = c.get(Calendar.DATE);


                        TextView tv_name = (TextView) v.findViewById(R.id.tv_name);
                        TextView tv_sub_title = (TextView) v.findViewById(R.id.tv_sub_title);
                        TextView tv_price = (TextView) v.findViewById(R.id.tv_price);
                        ImageView shape_iv = (ImageView) v.findViewById(R.id.shape_iv);
                        final EditText et_count = (EditText) v.findViewById(R.id.et_number);
                        final EditText et_time = (EditText) v.findViewById(R.id.et_time);
                        btnPay = (TextView)v.findViewById(R.id.btn_pay);

                        btnPay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                createOrder(productId,Integer.valueOf(et_count.getText().toString()),Integer.valueOf(et_time.getText().toString()),getDateStrTime(setDateStr(curYear, curMonth, curDate)));
                            }
                        });

                        tv_date = (TextView) v.findViewById(R.id.tv_date);
                        tv_name.setText(data.name);
                        tv_sub_title.setText(data.description+"");
                        tv_price.setText(FormatCouponInfo.getYuanStr() + FormatCouponInfo.formatDoublePrice(data.price, 2));
                        tv_date.setText(setDateStr(curYear, curMonth, curDate)); //显示当前的年月日

                        GlideUtils.loadCustomImageView(RentWorkPlaceDetailActivity.this, R.drawable.product, LoadImageUtils.loadMiddleImage(data.thumbnail),shape_iv);
                        v.findViewById(R.id.tv_date).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showTimePicker();
                                //创建DatePickerDialog对象
                                /*DatePickerDialog dpd=new DatePickerDialog(RentWorkPlaceDetailActivity.this,Datelistener,year ,month, day);
                                dpd.show();*/
                            }
                        });

                    }
                })
                .setLayoutRes(R.layout.view_rent_wp_detail_bottom_dialog)
                .setDimAmount(0.5f)
                .setCancelOutside(true)
                .setTag("PayDialog")
                .show();
    }

    private String setDateStr(int year, int month, int day){
        return year+"." + month+"."+ day;
    }

    private long getDateStrTime(String dateStr){
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        try {
            Date date = dateFormat.parse(dateStr);
            long time = date.getTime();
            Logger.d("time----" + time);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }
    private int curYear, curMonth, curDate ;
    private WheelView vYear , vMonth, vDay;
    BaseBottomDialog timePickerDialog;
    private void showTimePicker(){

         timePickerDialog =  BottomDialog.create(getSupportFragmentManager())
                .setViewListener(new BottomDialog.ViewListener() {
                    @Override
                    public void bindView(View view) {
                        vYear =(WheelView) view.findViewById(R.id.year);
                        vMonth = (WheelView) view.findViewById(R.id.month);
                        vDay = (WheelView) view.findViewById(R.id.day);
                        initYear(curYear + 1);
                        initMonth();
                        initDay(curYear, curMonth);
                        vYear.setCurrentItem(0);
                        vMonth.setCurrentItem(curMonth - 1);
                        vDay.setCurrentItem(curDate - 1);
                        vYear.setVisibleItems(VISIBLE_ITEMS_SUM);
                        vMonth.setVisibleItems(VISIBLE_ITEMS_SUM);
                        vDay.setVisibleItems(VISIBLE_ITEMS_SUM);
                        vYear.addScrollingListener(scrollListener);
                        vMonth.addScrollingListener(scrollListener);

                        // 设置监听
                        TextView ok = (TextView) view.findViewById(R.id.set);
                        TextView cancel = (TextView) view.findViewById(R.id.cancel);

                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                curYear = vYear.getCurrentItem()+curYear;
                                curMonth = vMonth.getCurrentItem()+1;
                                curDate = vDay.getCurrentItem()+1;
                                String selectDate = FormatCouponInfo.getFormatDate(vYear.getCurrentItem()+curYear, vMonth.getCurrentItem()+1,vDay.getCurrentItem()+1);
                                Logger.d("selectDate---" + selectDate);
                                tv_date.setText(setDateStr(curYear, curMonth, curDate));
                                timePickerDialog.dismiss();
                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                timePickerDialog.dismiss();
                            }
                        });
                        LinearLayout cancelLayout = (LinearLayout) view.findViewById(R.id.view_none);
                        cancelLayout.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                timePickerDialog.dismiss();
                                return false;
                            }
                        });



                    }
                })
                .setLayoutRes(R.layout.view_datepick)
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
            initDay(vYear.getCurrentItem()+ curYear, vMonth.getCurrentItem()+1);
        }
    };


    /**
     * 初始化年
     */
    private void initYear(int curYear) {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this, this.curYear, curYear);
        numericWheelAdapter.setLabel(" " + getString(R.string.year));
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        vYear.setViewAdapter(numericWheelAdapter);
        vYear.setCyclic(true);
    }

    /**
     * 初始化月
     */
    private void initMonth() {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this,1, 12, "%02d");
        numericWheelAdapter.setLabel(" " + getString(R.string.month));
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        vMonth.setViewAdapter(numericWheelAdapter);
        vMonth.setCyclic(true);
    }

    /**
     * 初始化天
     */
    private void initDay(int curYear, int curMonth) {
        NumericWheelAdapter numericWheelAdapter=new NumericWheelAdapter(this,1, DateUtil.getDay(curYear, curMonth), "%02d");
        numericWheelAdapter.setLabel(" " + getString(R.string.day));
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        vDay.setViewAdapter(numericWheelAdapter);
        vDay.setCyclic(true);
    }

    @OnClick(R.id.btn_buy)
    void onBuyNowClick() {
        if (getGlobalParams().isLogin()) {
            showPayBottomDialog();
        } else {
            showAskLoginDialog();
        }
    }

    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
    BizDataRequest.requestContractProductInfo(this, productId + "", statusLayout, new BizDataRequest.OnContractProduct() {
            @Override
            public void onSuccess(ContractProductJson contractProductJson) {
                data = contractProductJson;
                handler.sendEmptyMessageAtTime(10, 500);
            }

            @Override
            public void onError(DcnException error) {
                Message message = new Message();
                message.obj = error.getDescription();
                message.what = 404;
            }
        });

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
                    GlideUtils.loadCustomImageView(RentWorkPlaceDetailActivity.this, R.drawable.product, LoadImageUtils.loadMiddleImage(data.thumbnail), iv_header_img);
                    vProductInfo.updateViews(data.name,data.price, data.description);
                    vIntroduce.updateVies(data.medias,"","");
                    break;
                case 404:
                    //Toast.makeText(RentWorkPlaceDetailActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };



    //工位出租
    private void createOrder(long productId,int count,int month,long startTime){

        BizDataRequest.requestCreateContractOrder(RentWorkPlaceDetailActivity.this, productId, count, month,  getGlobalParams().getWorkplaceId(), startTime, new BizDataRequest.OnResponseCreateContractOrderJson() {
            @Override
            public void onSuccess(ResponseOpenOrderJson responseOpenOrderJson) {
                ResponseOpenOrderJson confirmOrder = responseOpenOrderJson;

                Intent intent = new Intent(RentWorkPlaceDetailActivity.this, OrderConfirmActivity.class);
                intent.putExtra("type","ms");
                intent.putExtra("confirmOrder", (Serializable) confirmOrder);
                startActivityForResult(intent,999);
            }
            @Override
            public void onError(DcnException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 999){
            this.finish();
        }
    }
}
