package com.atlas.crmapp.coffee;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.OrderConfirmRvAdapter;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.commonactivity.OrderPayActivity;
import com.atlas.crmapp.eventbus.Event;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.model.ContractJson;
import com.atlas.crmapp.model.ContractsJson;
import com.atlas.crmapp.model.OriginModel;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.model.SuggestOrderModel;
import com.atlas.crmapp.model.SuggestResponseModel;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.usercenter.MyContractsDialog;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.util.UiUtil;
import com.atlas.crmapp.view.OrderCouponAndBenefitView;

import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderConfirmActivity extends BaseStatusActivity {

    private String type;
    private SuggestResponseModel mSuggestResponseModel;
    private String mPromoType;
    private long mPromoId;
    //private boolean isComplete;
    private long contractId;// 等于0时使用企业支付
    private ResponseOpenOrderJson confirmOrder;
    private List<ContractJson> contracts;
    private long orderId;
    private static final String KEY_ORDER_ID = "KEY_ORDER_ID";

    @BindView(R.id.tv_title_consignee)
    TextView tvOrderId;

    @BindView(R.id.tv_price_z)
    TextView tvAmount;

    @BindView(R.id.rv_order_list)
    RecyclerView mRvOrderList;

    @BindView(R.id.order_bottom_bar)
    View bottomBar;

    @BindView(R.id.tv_zf)
    TextView mZf;//去结算

    @BindView(R.id.tv_order_num)
    TextView tvOrderNum;

    @BindView(R.id.tv_state)
    TextView tvState;


    @BindView(R.id.tv_discount_money)
    TextView tvDiscountMoney;

    @BindView(R.id.cb_use_contract_pay)
    SwitchCompat cbUseContractPay;

    @BindView(R.id.v_coupon_benefit)
    OrderCouponAndBenefitView vCouponBenefit;

    @BindView(R.id.ll_cb_contract_pay)
    LinearLayout llCbContractPay;

    public void onClickBack() {
        onBack();
    }

    public static void newInstance(Context activity, long orderId){
        Intent intent = new Intent(activity, OrderConfirmActivity.class);
        intent.putExtra(KEY_ORDER_ID, orderId);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_confirm);
        EventBusFactory.getBus().register(this);
        setTitle(getString(R.string.order_detail));
        setTopLeftButton(R.drawable.white_back, new OnClickListener() {
            @Override
            public void onClick() {
                onClickBack();
            }
        });

        Intent intent = getIntent();
        if(intent!= null){
            confirmOrder =  (ResponseOpenOrderJson) intent.getSerializableExtra("confirmOrder");
            orderId = intent.getLongExtra(KEY_ORDER_ID , 0);
            type = intent.getStringExtra("type");//用于统计。
        }

        if(confirmOrder !=null){
            orderId = confirmOrder.getId();
            setConfirmOrderPriceView(confirmOrder);
            initView();
        }else{
            requestOrderDetail();
        }

       /* if (isComplete) {
            vCouponBenefit.updateViews(confirmOrder);
            mZf.setVisibility(View.GONE);
            bottomBar.setVisibility(mZf.getVisibility());
        } else {
            if(confirmOrder != null){
                loadSuggest(confirmOrder.getId(), 0);
            }
        }*/

        mZf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long deduction = 0;
                if(mSuggestResponseModel != null){
                    deduction = (long)mSuggestResponseModel.origin.deduction;
                }
                OrderPayActivity.newInstance(OrderConfirmActivity.this, confirmOrder, contractId, deduction, mPromoId, mPromoType);
            }
        });
    }



    //设置友盟统计 事件页面名称
    private void setUmengAnalyseName(boolean isComplete){
        if(isComplete){
            umengPageTitle = getString(R.string.umeng_complete_order);
        }else{
            if(Constants.ORDER_TYPE.FITNESS.equals(type)){
                umengPageTitle = getString(R.string.umeng_fitness_order);
            }else if(Constants.ORDER_TYPE.MR.equals(type)){
                umengPageTitle = getString(R.string.umeng_metting_order);
            }
        }
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        loadSuggest(confirmOrder.getId(), contractId);
    }

    public void loadSuggest(final long orderId, final long contractId) {

        BizDataRequest.requestSuggest(this, orderId, contractId, statusLayout, new BizDataRequest.OnSuggestRequestResult() {
            @Override
            public void onSuccess(SuggestResponseModel suggestResponseModel) {
                statusLayout.showContent();
                OriginModel origin = suggestResponseModel.origin;
                OrderConfirmActivity.this.mSuggestResponseModel = suggestResponseModel;
                if(origin != null){
                    confirmOrder.setActualAmount(origin.actualAmount);
                }
                confirmOrder.setDiscount(0);

                vCouponBenefit.setOnSelectCouponOrBenefitListener(new OrderCouponAndBenefitView.OnSelectCouponOrBenefitListener() {
                    @Override
                    public void onSelectCouponOrBenefit(SuggestOrderModel suggestOrderModel) {
                        if(suggestOrderModel != null){
                            confirmOrder.setActualAmount(suggestOrderModel.actualAmount);
                            //confirmOrder.setAmount(suggestOrderModel.amount);
                            confirmOrder.setDiscount(suggestOrderModel.discount);
                            mPromoType = suggestOrderModel.promoType;
                            mPromoId = suggestOrderModel.promoId;
                        }else{
                            mPromoType = "";
                            mPromoId = 0;
                            confirmOrder.setActualAmount(confirmOrder.getAmount());
                            confirmOrder.setDiscount(0);
                        }
                        setConfirmOrderPriceView(confirmOrder);
                    }

                    @Override
                    public void onHaveDeduction(OriginModel originModel, SuggestOrderModel suggestOrderModel) {
                        double discount = 0;
                        double actualAmount = 0;

                        if(suggestOrderModel != null){
                            mPromoType = suggestOrderModel.promoType;
                            mPromoId = suggestOrderModel.promoId;
                            discount = originModel.amount - suggestOrderModel.actualAmount;
                            actualAmount = suggestOrderModel.actualAmount;
                        }else{
                            mPromoType = "";
                            mPromoId = 0;
                            actualAmount = originModel.actualAmount;
                            discount = originModel.amount - originModel.actualAmount;
                        }

                        confirmOrder.setActualAmount(actualAmount);
                        confirmOrder.setDiscount(discount);
                        setConfirmOrderPriceView(confirmOrder);
                    }
                });
                vCouponBenefit.updateViews(suggestResponseModel);

               /* if(confirmOrder != null){// 待支付不能点击
                    if(Constants.ORDER_STATE.CONFIRM.equalsIgnoreCase(confirmOrder.getState())){
                        vCouponBenefit.setNotClickable();
                    }
                }*/
            }

            @Override
            public void onError(DcnException error) {
                if(contractId != 0){
                    cbUseContractPay.setChecked(false);
                }
            }
        });


    }

    //设置确认的钱数
    private void setConfirmOrderPriceView(ResponseOpenOrderJson confirmOrder){
        tvDiscountMoney.setText(String.format("%.2f元", confirmOrder.getDiscount()));
        tvAmount.setText(Html.fromHtml(getString(R.string.real_pay_money, FormatCouponInfo.formatDoublePrice(confirmOrder.getActualAmount(),2))));
    }


    @OnClick(R.id.cb_use_contract_pay)
    void doContractPayClick() {
        if (cbUseContractPay.isChecked()) {
            loadContracts();
        } else {
            contractId = 0;
            loadSuggest(confirmOrder.getId(), contractId);
        }
    }

    //通过中心ID获取我是法人或授权人的合同列表
    private void loadContracts() {
        BizDataRequest.requestMyContracts(this, getGlobalParams().getAtlasId(), true, statusLayout,  new BizDataRequest.OnContractsRequestRescult() {
            @Override
            public void onSuccess(ContractsJson contractsJson) {
                contracts = contractsJson.rows;
                MyContractsDialog dialog = new MyContractsDialog();
                Bundle bundle = new Bundle();
                bundle.putSerializable("contracts",(Serializable) contracts);
                dialog.setArguments(bundle);
                dialog.listener = new MyContractsDialog.MyContractsDialogListener() {
                    @Override
                    public void onMyContractDialogSelected(ContractJson contract) {
                        if (contract != null) {
                            OrderConfirmActivity.this.contractId = contract.id;
                            loadSuggest(confirmOrder.getId(), contractId);
                        } else {
                            cbUseContractPay.setChecked(false);
                        }

                    }
                };
                dialog.show(getSupportFragmentManager(), "MyContractDialog");
            }

            @Override
            public void onError(DcnException error) {
                cbUseContractPay.setChecked(false);
                //Toast.makeText(OrderConfirmActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void requestOrderDetail(){
        BizDataRequest.requestOrderDetail(this, orderId, statusLayout, new BizDataRequest.OnResponseOrderDetail() {
            @Override
            public void onSuccess(ResponseOpenOrderJson orderJson) {
                OrderConfirmActivity.this.confirmOrder = orderJson;
                initView();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }


    private void initView() {

        if(!Constants.ORDER_STATE.COMPLETE.equals(confirmOrder.getState())){
            loadSuggest(confirmOrder.getId(), 0);
        }

        OrderConfirmRvAdapter adapter = new OrderConfirmRvAdapter(this,confirmOrder);
        mRvOrderList.setHasFixedSize(true);
        mRvOrderList.setLayoutManager(new LinearLayoutManager(this));
        mRvOrderList.setAdapter(adapter);

        if (StringUtils.isEmpty(confirmOrder.getOfflineCode())){
            tvOrderNum.setText(String.valueOf(confirmOrder.getId()));
        }else {
            tvOrderNum.setText(String.valueOf(confirmOrder.getId())+ Html.fromHtml(getString(R.string.offlineCode,confirmOrder.getOfflineCode())));
        }
        tvState.setText(FormatCouponInfo.getOrderState(confirmOrder.getState()));

        if (getGlobalParams().isHasContract() && confirmOrder.isBillable()&&!"PRINT".equals(confirmOrder.getType())) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mZf.getLayoutParams();
            params.width =  UiUtil.dipToPx(this, 170);
            llCbContractPay.setVisibility(View.VISIBLE);
            mZf.setLayoutParams(params);
        } else {
            llCbContractPay.setVisibility(View.GONE);
        }

        if(Constants.ORDER_STATE.COMPLETE.equals(confirmOrder.getState())){
            vCouponBenefit.updateViews(confirmOrder);
            mZf.setVisibility(View.GONE);
            bottomBar.setVisibility(mZf.getVisibility());
            vCouponBenefit.setNotClickable();
            vCouponBenefit.findViewById(R.id.switch_benefit).setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return  onBack();
        }
        return false;
    }


    private boolean onBack(){

       /* if(isComplete){
            setResult(999);
            finish();
            return false;
        }*/
        if (confirmOrder != null && !Constants.ORDER_STATE.COMPLETE.equalsIgnoreCase(confirmOrder.getState())) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(R.string.text_67)
                    .setMessage(R.string.text_90)
                    .setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setResult(999);
                            finish();
                        }
                    })
                    .setCancelable(true)
                    .show();

            return true;
        } else {
            setResult(999);
            finish();
            return false;
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusFactory.getBus().unregister(this);
    }

    @Subscribe
    public void onPaySuccess(Event.EventObject eventObject){
        if(eventObject != null){
            if(Constants.EventType.CLICK_COMPLETE_OR_BACK.equals(eventObject.type)){
                OrderConfirmActivity.this.finish();
            }else if(Constants.EventType.ORDER_LOOK_DETAIL.equals(eventObject.type)){
                requestOrderDetail();
                /*tvState.setText("已完成");
                vCouponBenefit.setNotClickable();
                vCouponBenefit.findViewById(R.id.switch_benefit).setVisibility(View.GONE);
                mZf.setVisibility(View.GONE);
                bottomBar.setVisibility(mZf.getVisibility());*/
            }
        }
    }

}
