package com.atlas.crmapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.atlas.crmapp.coffee.OrderConfirmActivity;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.PrintStateJson;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/6.
 */

public class PrintDetailActivity extends BaseStatusActivity {

    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_close)
    TextView tv_close;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    long currentTime;
    private static final String KEY_ORDER_ID = "KEY_ORDER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_detail);
        ButterKnife.bind(this);

        initView();
        startLoop();
    }

    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }

    private void initView(){
        currentTime = System.currentTimeMillis();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCheck = false;
                finish();
            }
        });

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCheck = false;
                finish();
            }
        });
    }
    long id = 0;
    public void queryOrder(){
        BizDataRequest.requestCodeOrder(this, currentTime, 0, new BizDataRequest.OnResponseOpenOrderJson() {
            @Override
            public void onSuccess(ResponseOpenOrderJson responseOpenOrderJson) {
                if (responseOpenOrderJson == null)
                    return;
                //灵异bug，强制使用id和获取的json做订单对比，相同就不再弹出
                // 可能是网络请求的缓存问题，请求并没有获取到数据，重复返回之前的数据
                //只在页面被覆盖是发生
                if (id == responseOpenOrderJson.getId())
                    return;
                id = responseOpenOrderJson.getId();
                currentTime = System.currentTimeMillis();
                Intent orderIntent = new Intent(PrintDetailActivity.this, OrderConfirmActivity.class);
                orderIntent.putExtra("confirmOrder",responseOpenOrderJson);
                orderIntent.putExtra(KEY_ORDER_ID,responseOpenOrderJson.getId());
                orderIntent.putExtra("type",responseOpenOrderJson.getType());
                startActivity(orderIntent);
            }

            @Override
            public void onError(DcnException error) {

            }
        });

    }

    public void queryPrint(){
        BizDataRequest.getPrintState(this, null, new BizDataRequest.PrintStateRequestRescult() {
            @Override
            public void onSuccess(PrintStateJson printJason) {
                if (printJason == null)
                    return;
                if ("LOGOUT".equals(printJason.getState())){
                    btnSubmit.setVisibility(View.VISIBLE);
                    tvContent.setText(getResources().getString(R.string.print_detail_content_2));
                    btnSubmit.setText(getResources().getString(R.string.print_detail_btn_2));
                    tv_close.setText("");
                    isCheck = false;
                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });

    }

    private boolean isCheck = true;
    private boolean isQueryOut = true;
    private void startLoop(){
        new Thread(){
            @Override
            public void run() {
                while (isCheck){
                    queryOrder();
                    if (isQueryOut) {
                        queryPrint();
                      handler.sendEmptyMessage(1);
                    }
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        isCheck = false;
                    }
                    isQueryOut = !isQueryOut;
                }
                handler.sendEmptyMessage(2);
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isCheck = false;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
//                    showToast("查询是否退出");
                    break;
                case 2:
//                    showToast("退出循环");
                    break;
            }
        }
    };
}
