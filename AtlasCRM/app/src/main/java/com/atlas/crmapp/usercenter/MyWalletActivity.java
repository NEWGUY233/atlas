package com.atlas.crmapp.usercenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.eventbus.Event;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.model.ResponseMyInfoJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.UiUtil;
import com.michael.easydialog.EasyDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class MyWalletActivity extends BaseStatusActivity implements View.OnClickListener {

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;
    @BindView(R.id.ibHome)
    ImageButton ivMore;

    private EasyDialog easyDialog;
    private boolean isSettingedPsw;

    @OnClick(R.id.ibBack)
    void onBack() {
        this.finish();
    }

    @OnClick(R.id.ibHome)
    public void onClick() {
        showDialog();
    }


    @BindView(R.id.tv_balance)
    TextView tvBalance;

    @OnClick(R.id.btn_submit)
    void onPay(){
        Intent intent = new Intent(MyWalletActivity.this,RechargeActivity.class);
        this.startActivityForResult(intent,1000);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        EventBusFactory.getBus().register(this);
        umengPageTitle = getString(R.string.my_wallet);
        textViewTitle.setText(umengPageTitle);

        ivMore.setImageResource(R.drawable.ic_more);
        ivMore.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusFactory.getBus().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPaySuccessLookCode(Event.EventObject object){
        if(object != null && Constants.EventType.RECHARGE_LOOK_CODE.equals(object.type)){
            this.finish();
        }
    }

    @Override
    protected int setTopBar() {
        return R.layout.titlebar;
    }

    private void showDialog() {
        int[] attachedViewLocation = new int[2];
        ivMore.getLocationOnScreen(attachedViewLocation);
        attachedViewLocation[0] += ivMore.getWidth() / 4 + UiUtil.dipToPx(this, 5);
        attachedViewLocation[1] += ivMore.getHeight();

        View view = getLayoutInflater().inflate(R.layout.layout_my_wallet_pop, null);
        easyDialog = new EasyDialog(this)
                .setLayout(view)
                .setBackgroundColor(this.getResources().getColor(R.color.white_color))
                .setGravity(EasyDialog.GRAVITY_BOTTOM)
                .setLocation(attachedViewLocation)
                .setTouchOutsideDismiss(true)
                .setMatchParent(false)
                .setOutsideColor(this.getResources().getColor(R.color.popup_bg))
                .show();
        view.findViewById(R.id.wallet_recharge_record).setOnClickListener(this);
        view.findViewById(R.id.wallet_paw_manager).setOnClickListener(this);
        view.findViewById(R.id.ll_pay_security).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wallet_paw_manager:
                skipToActivity(PayPasswordActivity.class);
                break;
            case R.id.wallet_recharge_record:
                skipToActivity(RechargeRecordActivity.class);
                break;
            case R.id.ll_pay_security:
                PaySecurityActivity.newInstance(MyWalletActivity.this, isSettingedPsw);
                if (easyDialog != null) {
                    easyDialog.dismiss();
                }
                //skipToActivity(PaySecurityActivity.class);
                break;
        }
    }

    private void skipToActivity(Class cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        if (easyDialog != null) {
            easyDialog.dismiss();
        }
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        statusLayout.setShowStatusLayout(true);
        prepareActivityData();
    }


    private AlertDialog dialog;
    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
    BizDataRequest.requestMyInfo(MyWalletActivity.this, false, statusLayout, new BizDataRequest.OnResponseMyInfoJson() {
            @Override
            public void onSuccess(ResponseMyInfoJson responseMyInfoJson) {
                if(responseMyInfoJson!=null) {
                    tvBalance.setText(FormatCouponInfo.formatDoublePrice(responseMyInfoJson.getAmount(),2));
                }
                if(TextUtils.isEmpty(responseMyInfoJson.getPassword())){
                    if(dialog == null){
                        dialog = new AlertDialog.Builder(MyWalletActivity.this)
                                .setTitle(R.string.t90)
                                .setMessage(R.string.t91)
                                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        MyWalletActivity.this.startActivity(new Intent(MyWalletActivity.this,PayPasswordActivity.class));
                                    }
                                })
                                .setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialog.dismiss();
                                    }
                                })
                                .setCancelable(false)
                                .show();
                    }else {
                        dialog.show();
                    }
                }else{
                    isSettingedPsw = true;
                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareActivityData();
        statusLayout.setShowStatusLayout(false);
    }
}
