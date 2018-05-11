package com.atlas.crmapp.view.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.model.SuggestOrderModel;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.view.PayChannelView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by hoda on 2017/8/30.
 */

public class OrderPayChannelPopup extends BasePopupWindow {
    private Context context;
    private View contentView;
    private OnEnterPayConfirmListener onEnterPayConfirmListener;

    private SuggestOrderModel suggestOrderModel;
    private ResponseOpenOrderJson confirmOrder;
    //支付popu
    private PayChannelView vPayAccout;
    private PayChannelView vPayAli;
    private PayChannelView vPayWx;
    private ImageView ivBg;
    private String channelType;
    private double balance;

    public double discount;


    public void setOnEnterPayConfirmListener(OnEnterPayConfirmListener onEnterPayConfirmListener){
        this.onEnterPayConfirmListener = onEnterPayConfirmListener;
    }

   public OrderPayChannelPopup(Activity context, SuggestOrderModel suggestOrderModel, ResponseOpenOrderJson confirmOrder,  double balance) {
        super(context);
       this.context = context;
       this.suggestOrderModel = suggestOrderModel;
       this.confirmOrder = confirmOrder;
       this.balance = balance;
       bindEvent();
    }

    public OrderPayChannelPopup(Activity context, double discount, ResponseOpenOrderJson confirmOrder,  double balance) {
        super(context);
        this.discount = discount;
        this.context = context;
        this.confirmOrder = confirmOrder;
        this.balance = balance;
        bindEvent();
    }



        @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return contentView.findViewById(R.id.order_click_dismiss);
    }

    @Override
    public View onCreatePopupView() {
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.popwindow_order_zf, null);
        return contentView;
    }

    @Override
    public View initAnimaView() {
        return null;
    }


    public void setOnOutSideClick (View.OnClickListener onClickListener){
        if(mDismissView != null){
            mDismissView.setOnClickListener(onClickListener);
        }
    }


    private void bindEvent(){
        if(contentView == null){
            return;
        }
        ivBg = (ImageView) contentView.findViewById(R.id.iv_bg);
        vPayAccout = (PayChannelView) contentView.findViewById(R.id.v_pay_accout);
        vPayAli = (PayChannelView) contentView.findViewById(R.id.v_pay_ali);
        vPayWx = (PayChannelView) contentView.findViewById(R.id.v_pay_wx);
        vPayAccout.setOnClickListener(onClickChannelView);
        vPayAli.setOnClickListener(onClickChannelView);
        vPayWx.setOnClickListener(onClickChannelView);
        TextView tvFreeMoney = (TextView) contentView.findViewById(R.id.tv_free_money);
        TextView tvNeedPay = (TextView) contentView.findViewById(R.id.tv_need_yay);
        Button button = (Button) contentView.findViewById(R.id.bt_ok);
        Glide.with(getContext()).load(R.drawable.bg_my_wallet).apply(new RequestOptions().dontAnimate().centerCrop()).into(ivBg);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onEnterPayConfirmListener != null){
                    onEnterPayConfirmListener.onEnterPayConfirm(channelType);
                }
                dismiss();
            }
        });
        if(confirmOrder == null){//支付金额

        }else{
            if(suggestOrderModel!= null ){
                tvFreeMoney.setText(Html.fromHtml(context.getString(R.string.free_money_, FormatCouponInfo.formatDoublePrice(suggestOrderModel.discount, 2))));
            }else if(discount > 0){
                tvFreeMoney.setVisibility(View.VISIBLE);
                tvFreeMoney.setText(Html.fromHtml(context.getString(R.string.free_money_, FormatCouponInfo.formatDoublePrice(discount, 2))));
            }else {
                tvFreeMoney.setVisibility(View.GONE);
                tvFreeMoney.setText(R.string.s79);
            }
            tvNeedPay.setText(FormatCouponInfo.formatDoublePrice(confirmOrder.getActualAmount(), 2)+getContext().getString(R.string.yuan));
        }

        if(balance>0) {
            if(confirmOrder.getActualAmount()> balance){
                selectChannel(Constants.PayChannel.ALIPAY);
            }else{
                selectChannel(Constants.PayChannel.ACCOUNT);
            }
        }else{
            selectChannel(Constants.PayChannel.ALIPAY);
        }
    }

    View.OnClickListener onClickChannelView = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.v_pay_accout:
                    selectChannel(Constants.PayChannel.ACCOUNT);
                    break;
                case R.id.v_pay_ali:
                    selectChannel(Constants.PayChannel.ALIPAY);
                    break;
                case R.id.v_pay_wx:
                    selectChannel(Constants.PayChannel.WXPAY);
                    break;
            }
        }
    };


    private void selectChannel(String channelType){
        this.channelType = channelType;
        int[] drawableIds={R.drawable.ic_user_moeny, R.drawable.ic_zf_b ,R.drawable.ic_zf_wx};
        String[] channels = {context.getString(R.string.s80),context.getString(R.string.s81),context.getString(R.string.s82)};
        String balanceStr = FormatCouponInfo.formatDoublePrice(balance, 2);
        switch (channelType){
            case Constants.PayChannel.ACCOUNT:
                vPayAccout.updateViews(drawableIds[0], channels[0],balanceStr,true);
                vPayAli.updateViews(drawableIds[1], channels[1],"",false);
                vPayWx.updateViews(drawableIds[2], channels[2],"",false);
                break;
            case Constants.PayChannel.ALIPAY:
                vPayAccout.updateViews(drawableIds[0], channels[0],balanceStr,false);
                vPayAli.updateViews(drawableIds[1], channels[1],"",true);
                vPayWx.updateViews(drawableIds[2], channels[2],"",false);
                break;
            case  Constants.PayChannel.WXPAY:
                vPayAccout.updateViews(drawableIds[0], channels[0],balanceStr,false);
                vPayAli.updateViews(drawableIds[1], channels[1],"",false);
                vPayWx.updateViews(drawableIds[2], channels[2],"",true);
                break;
        }
    }

    public interface OnEnterPayConfirmListener{
        void onEnterPayConfirm(String channelType);
    }


}
