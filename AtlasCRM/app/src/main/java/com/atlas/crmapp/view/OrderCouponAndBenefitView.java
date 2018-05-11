package com.atlas.crmapp.view;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.OrderConfirmCouponAdapter;
import com.atlas.crmapp.model.OriginModel;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.model.SuggestListModel;
import com.atlas.crmapp.model.SuggestOrderModel;
import com.atlas.crmapp.model.SuggestResponseModel;
import com.atlas.crmapp.model.UseableBenefitModel;
import com.atlas.crmapp.model.UseableCouponsModel;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.view.popupwindow.CouponPopup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hoda on 2017/9/14.
 */

public class OrderCouponAndBenefitView extends LinearLayout {
    @BindView(R.id.tv_icon_benefit)
    TextView tvIconBenefit;
    @BindView(R.id.tv_benefits)
    TextView tvBenefits;
    @BindView(R.id.tv_benefit_name)
    TextView tvBenefitName;
    @BindView(R.id.switch_benefit)
    SwitchCompat switchBenefit;
    @BindView(R.id.tv_icon_coupon)
    TextView tvIconCoupon;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.tv_coupon_name)
    TextView tvCouponName;
    @BindView(R.id.iv_coupon_arr)
    ImageView ivCouponArr;
    @BindView(R.id.rl_benefit)
    RelativeLayout rlBenefit;
    @BindView(R.id.rl_coupon)
    RelativeLayout rlCoupon;
    @BindView(R.id.tv_deduction)
    TextView tvDeduction;
    @BindView(R.id.rl_deduction)
    RelativeLayout rlDeduction;

    private int orange, gray;

    private SuggestResponseModel suggestResponseModel;
    private SuggestOrderModel suggestOrderModel;
    private ResponseOpenOrderJson confirmOrder;
    private String promoType;
    private long promoId;
    private int useableBenefitSize;
    private int useableCouponSize;

    private CouponPopup couponPopu;

    private Context context;
    private double deduction;


    public OrderCouponAndBenefitView(Context context) {
        super(context);
        initViews(context);
    }

    public OrderCouponAndBenefitView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public OrderCouponAndBenefitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public OrderCouponAndBenefitView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);

    }

    private void initViews(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_order_coupon_benefit, this, true);
        ButterKnife.bind(this);
        orange = ContextCompat.getColor(context, R.color.orange);
        gray = ContextCompat.getColor(context, R.color.gray_simple);
    }


    public void updateViews(SuggestResponseModel suggestResponseModel) {
        setVisibility(VISIBLE);
        if(suggestResponseModel != null){
            if (suggestResponseModel.origin != null){
                if(suggestResponseModel.origin.amount == 0){
                    //健身会籍的用户，预约课程，免费\所以订单价格为 0，此时默认应该不需要选中优惠券
                    suggestResponseModel.suggests.clear();// 实际价格为0 时， 不使用优惠券。

                }
            }
        }
        this.suggestResponseModel = suggestResponseModel;
        /*if(updateDeduction()){
            goneCouponBenfit();
            return;
        }*/


        if (suggestResponseModel.suggests != null  && suggestResponseModel.suggests.size() > 0 ) {
            SuggestListModel suggest = suggestResponseModel.suggests.get(0);
            useableBenefitSize = suggestResponseModel.useableBenefits.size();
            useableCouponSize = suggestResponseModel.useableCoupons.size();
            useDiscount(suggest.order.promoId);
        } else {
            useDiscount(0);
            suggestOrderModel = null;
            rlBenefit.setVisibility(View.GONE);
            rlCoupon.setVisibility(View.GONE);
        }


    }

    //免费使用点数。
    private boolean updateDeduction(SuggestOrderModel suggestOrderModel){
        OriginModel origin = suggestResponseModel.origin;
        if(origin != null){
            this.deduction = origin.deduction;
            if(origin.deduction > 0){
                tvDeduction.setText(Html.fromHtml(context.getString(R.string.free_user_min_and_remain_min, FormatCouponInfo.formatDoublePrice(origin.deduction, 0), FormatCouponInfo.formatDoublePrice(origin.remain, 0))));
                rlDeduction.setVisibility(View.VISIBLE);
                if(onSelectCouponOrBenefitListener != null){
                    if(origin.actualAmount == 0){// 免费点数足够时 不显示优惠与促销
                        suggestOrderModel = null;
                        goneCouponBenfit();
                    }
                    onSelectCouponOrBenefitListener.onHaveDeduction(origin, suggestOrderModel);
                    return true;
                }
            }else{
                if(origin.remain > 0){// && !isComplete
                    tvDeduction.setText(Html.fromHtml(context.getString(R.string.free_user_less_min_second, FormatCouponInfo.formatDoublePrice(origin.remain, 0))));
                    rlDeduction.setVisibility(View.VISIBLE);
                }else{
                    rlDeduction.setVisibility(View.GONE);
                }
            }
        }else{
            rlDeduction.setVisibility(GONE);
        }
        return false;
    }

    /**
     * 订单完成
     *
     * @param confirmOrder
     */
    public void updateViews(ResponseOpenOrderJson confirmOrder) {
        setVisibility(VISIBLE);
        this.confirmOrder = confirmOrder;
        if (confirmOrder != null && confirmOrder.getPromo() != null) {
            this.deduction = confirmOrder.getDeduction();

            if (confirmOrder.getPromo().getPromoType().equalsIgnoreCase("BENEFIT")) {
                selectCoupon(false, confirmOrder.getPromo().getName());
            } else if (confirmOrder.getPromo().getPromoType().equalsIgnoreCase("COUPONBIND")) {
                selectCoupon(true, confirmOrder.getPromo().getName());
            } else {
                rlCoupon.setVisibility(View.GONE);
                rlBenefit.setVisibility(View.GONE);
            }
        } else {
            rlCoupon.setVisibility(View.GONE);
            rlBenefit.setVisibility(View.GONE);
        }
        switchBenefit.setVisibility(GONE);

        // 免费使用点数
        if(confirmOrder.getDeduction() > 0){
            if ("PRINT".equals(confirmOrder.getType())){
                tvDeduction.setText(Html.fromHtml(context.getString(R.string.free_user_min_, FormatCouponInfo.formatDoublePrice(confirmOrder.getDeduction(), 2))));
            }else {
                tvDeduction.setText(Html.fromHtml(context.getString(R.string.free_user_min, FormatCouponInfo.formatDoublePrice(confirmOrder.getDeduction(), 0))));
            }
            rlDeduction.setVisibility(View.VISIBLE);
        }else{
            rlDeduction.setVisibility(View.GONE);
        }
    }


    private boolean isCanUseBenefit ;
    private boolean isCanUseCouponbind;

    @OnClick(R.id.switch_benefit)
    void onClickSwitchBenefit() {
        isCanUseBenefit = false;
        isCanUseCouponbind = false;
        if (switchBenefit.isChecked()) {
            if (useableBenefitSize > 0) {
                for (SuggestListModel suggestListModel : suggestResponseModel.suggests) {
                    if (suggestListModel.order.promoType.equalsIgnoreCase("BENEFIT")) {
                        isCanUseBenefit = true ;
                        useDiscount(suggestListModel.order.promoId);
                        break;
                    }
                }
                if(!isCanUseBenefit){
                    useDiscount(0);
                }
            }
        } else {
            for (SuggestListModel suggestListModel : suggestResponseModel.suggests) {
                if (suggestListModel.order.promoType.equalsIgnoreCase("COUPONBIND")) {
                    isCanUseCouponbind = true;
                    useDiscount(suggestListModel.order.promoId);
                    break;
                }
            }

            if(!isCanUseCouponbind){
                useDiscount(0);
            }
        }
    }

    private void useDiscount(long discountId) {
        promoId = 0;
        promoType = "";
        suggestOrderModel = null;
        for (SuggestListModel suggestListModel : suggestResponseModel.suggests) {

            if (suggestListModel.order.promoId == discountId) {
                suggestOrderModel = suggestListModel.order;
//                confirmOrder.setActualAmount(suggestOrderModel.actualAmount);
//                confirmOrder.setAmount(suggestOrderModel.amount);
//                confirmOrder.setDiscount(suggestOrderModel.discount);
                promoType = suggestOrderModel.promoType;
                promoId = suggestOrderModel.promoId;
                if (promoType.equals("BENEFIT")) {
                    for (UseableBenefitModel useableBenefitModel : suggestResponseModel.useableBenefits) {
                        if (useableBenefitModel.benefit.id == promoId) {
                            selectCoupon(false, useableBenefitModel.benefit.name);
                        }
                    }
                } else if (promoType.equals("COUPONBIND")) {
                    for (UseableCouponsModel useableCouponsModel : suggestResponseModel.useableCoupons) {
                        if (useableCouponsModel.bind.id == promoId) {
                            selectCoupon(true, useableCouponsModel.coupon.coupon.name);
                        }
                    }
                }
            }
        }

        if (promoId == 0) {
            selectCoupon(true, context.getString(R.string.not_user_coupon));
            tvIconCoupon.setBackgroundResource(R.drawable.order_not_selected);
            tvCouponName.setTextColor(gray);
            tvCoupon.setTextColor(gray);
        }
    }

    private void goneCouponBenfit(){
        rlCoupon.setVisibility(View.GONE);
        rlBenefit.setVisibility(View.GONE);
    }

    private void visibilityCouponBenfit(){
        rlCoupon.setVisibility(View.VISIBLE);
        rlBenefit.setVisibility(View.VISIBLE);
    }


    private void selectCoupon(boolean isSelect, String orderName) {
        if (suggestOrderModel != null ) {
            if (useableCouponSize == 0) {
                rlCoupon.setVisibility(GONE);
            }else{
                rlCoupon.setVisibility(VISIBLE);

            }
            if (useableBenefitSize == 0) {
                rlBenefit.setVisibility(GONE);
            }else{
                rlBenefit.setVisibility(VISIBLE);
            }
        }
        if (isSelect) {
            tvIconCoupon.setBackgroundResource(R.drawable.order_selected);
            tvCoupon.setTextColor(orange);
            tvCouponName.setTextColor(orange);
            ivCouponArr.setVisibility(VISIBLE);

            tvIconBenefit.setBackgroundResource(R.drawable.order_not_selected);
            tvBenefits.setTextColor(gray);
            tvBenefitName.setTextColor(gray);
            switchBenefit.setChecked(false);
            rlCoupon.setClickable(true);


            tvCouponName.setText(orderName);
            if (useableBenefitSize > 0) {
                tvBenefitName.setText(R.string.s85);
            } else {
                rlBenefit.setVisibility(GONE);
                //tvBenefitName.setText("没有促销活动");
            }

            if (confirmOrder != null) {
                switchBenefit.setEnabled(false);
                ivCouponArr.setVisibility(GONE);
                rlCoupon.setClickable(false);
                rlBenefit.setVisibility(GONE);
            }

        } else {
            tvIconBenefit.setBackgroundResource(R.drawable.order_selected);
            tvBenefits.setTextColor(orange);
            tvBenefitName.setTextColor(orange);
            switchBenefit.setChecked(true);
            switchBenefit.setClickable(true);

            tvIconCoupon.setBackgroundResource(R.drawable.order_not_selected);
            tvCouponName.setTextColor(gray);
            tvCoupon.setTextColor(gray);
            ivCouponArr.setVisibility(GONE);
            //rlCoupon.setClickable(false);

            tvBenefitName.setText(orderName);
            if (useableCouponSize > 0) {
                tvCouponName.setText(context.getString(R.string.coup_and_benefit_not_used));
            } else {
                rlCoupon.setVisibility(GONE);
                //tvBenefitName.setText("没有促销活动");
            }
            if (confirmOrder != null) {
                switchBenefit.setClickable(false);
                rlBenefit.setClickable(false);
                rlCoupon.setVisibility(GONE);
            }
        }

        if (suggestResponseModel != null) {
            if (onSelectCouponOrBenefitListener != null) {
                if(updateDeduction(suggestOrderModel)){
                    return;
                }
                onSelectCouponOrBenefitListener.onSelectCouponOrBenefit(suggestOrderModel);
            }
        }

    }


    //=============== 显示 优惠券 列表
    @OnClick(R.id.rl_coupon)
    void onClickCoupon(View view) {
        showCouponPopup(view);
    }

    OrderConfirmCouponAdapter.OnClickListener onClickListener = new OrderConfirmCouponAdapter.OnClickListener() {
        @Override
        public void onClickListener(int position) {
            if(position == -1){
                useDiscount(0);
                couponPopu.dismiss();
                return;
            }
            if (useableCouponSize > position && position >= 0) {
                UseableCouponsModel useableCouponsModel = suggestResponseModel.useableCoupons.get(position);
                useDiscount(useableCouponsModel.bind.id);
                couponPopu.dismiss();
            }
        }
    };

    public void showCouponPopup(View v) {
        couponPopu = new CouponPopup((Activity) context, suggestResponseModel.useableCoupons, onClickListener);
        couponPopu.setPopupWindowFullScreen(true);
        couponPopu.showPopupWindow();

    }
    //=============================


    //点击 我的优惠券或者 促销 回调
    private OnSelectCouponOrBenefitListener onSelectCouponOrBenefitListener;

    public void setOnSelectCouponOrBenefitListener(OnSelectCouponOrBenefitListener onSelectCouponOrBenefitListener) {
        this.onSelectCouponOrBenefitListener = onSelectCouponOrBenefitListener;
    }

    public interface OnSelectCouponOrBenefitListener {
        void onSelectCouponOrBenefit(SuggestOrderModel suggestOrderModel);
        void onHaveDeduction(OriginModel originModel, SuggestOrderModel suggestOrderModel);
    }

    public void setNotClickable (){
        this.setClickable(false);
        rlCoupon.setClickable(false);
        rlBenefit.setClickable(false);
        switchBenefit.setEnabled(false);
    }
}
