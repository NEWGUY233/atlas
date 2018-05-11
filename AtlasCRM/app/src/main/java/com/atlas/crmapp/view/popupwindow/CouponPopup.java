package com.atlas.crmapp.view.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ListView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.OrderConfirmCouponAdapter;
import com.atlas.crmapp.model.UseableCouponsModel;

import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by hoda on 2017/10/25.
 */

public class CouponPopup  extends BasePopupWindow {
    private List<UseableCouponsModel> useableCoupons;
    private View contentView ;
    private  OrderConfirmCouponAdapter.OnClickListener onClickListener;
    private Context context;


    public CouponPopup(Activity context , List<UseableCouponsModel> useableCoupons, OrderConfirmCouponAdapter.OnClickListener onClickListener) {
        super(context);
        this.context = context;
        this.useableCoupons = useableCoupons;
        this.onClickListener = onClickListener;
        bindEvent();
    }

    private void bindEvent() {

        OrderConfirmCouponAdapter adapter = new OrderConfirmCouponAdapter(context, useableCoupons);
        adapter.setOnClickListener(onClickListener);

        ListView listView = (ListView) contentView.findViewById(R.id.mylist);
        listView.setAdapter(adapter);

        contentView.findViewById(R.id.bt_not_use_coupon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClickListener(-1);
            }
        });
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return contentView.findViewById(R.id.rl_order_coupon_dismiss);
    }

    @Override
    public View onCreatePopupView() {
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.popup_order_coupon, null);
        return contentView;
    }

    @Override
    public View initAnimaView() {
        return null;
    }
}
