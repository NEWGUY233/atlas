package com.atlas.crmapp.coffee;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

import com.atlas.crmapp.MainActivity;
import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.activity.index.IndexActivity;
import com.atlas.crmapp.adapter.navadapter.MainFragmentNavAdapter;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.eventbus.Event;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.usercenter.RechargeRecordActivity;
import com.atlas.crmapp.util.FormatCouponInfo;

import butterknife.BindView;
import butterknife.OnClick;

public class PaySucceedActivity extends BaseActivity {

    private double amount;
    private String type;
    public static int RESULT_COMPELE = 999;


    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.tv_amount)
    TextView mTvAount;

    @BindView(R.id.btn_order)
    Button btnOrder;
    @BindView(R.id.btn_complete)
    Button btnComplete;

    @BindView(R.id.tv_info)
    TextView tvInfo;


    private static String KEY_AMOUNT = "amount";
    private static String KEY_TYPE = "type";

    @OnClick(R.id.ibBack)
    void onBack() {
        onCilckCompleteOrBack();
    }

    private void onCilckCompleteOrBack(){
        EventBusFactory.getBus().post(new Event.EventObject(Constants.EventType.CLICK_COMPLETE_OR_BACK, null));
        this.finish();
    }

    @OnClick(R.id.btn_complete)
    void onComplate() {
        if(Constants.ToPaySuccessType.RECHARGE.equals(type)){
            //startActivity(new Intent(this, MyCodeActivity.class));
//            MainActivity.newInstance(this, MainFragmentNavAdapter.FM_MAIN);
            startActivity(new Intent(this, IndexActivity.class));
            EventBusFactory.getBus().post(new Event.EventObject(Constants.EventType.RECHARGE_LOOK_CODE, null));
            finish();
        }else {
            onCilckCompleteOrBack();
        }
    }

    @OnClick(R.id.btn_order)
    void onCheckOrder() {
        if(Constants.ToPaySuccessType.RECHARGE.equals(type)){
            startActivity(new Intent(PaySucceedActivity.this, RechargeRecordActivity.class));
        }else{
            EventBusFactory.getBus().post(new Event.EventObject(Constants.EventType.ORDER_LOOK_DETAIL, null));
        }
        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onCilckCompleteOrBack();
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_succeed);
        amount = getIntent().getDoubleExtra(KEY_AMOUNT,0.00);
        type = getIntent().getStringExtra(KEY_TYPE);
        if(!TextUtils.isEmpty(type)){//充值
            textViewTitle.setText(getString(R.string.pay_recharge_detail));
            if(type.equals(Constants.ToPaySuccessType.RECHARGE)){
                btnOrder.setText(getString(R.string.recharge_record));
                tvInfo.setText(getString(R.string.recharge_complete));
                btnComplete.setText(getString(R.string.look_my_code));
            }
        }else{
            textViewTitle.setText(getString(R.string.complete_order));
        }
        mTvAount.setText("¥"+ FormatCouponInfo.formatDoublePrice(amount, 2));




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
