package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.util.FormatCouponInfo;

import cn.bingoogolapple.badgeview.BGABadgeFrameLayout;

/**
 * Created by hoda_fang on 2017/5/13.
 *
 * 购物底部 去结账
 */

public class BottomAccountView extends RelativeLayout{
    private Context context;
    private Button btnBooking;
    private BGABadgeFrameLayout vBadge;
    private TextView tvTotalPrice, tvShoppingNull;
    private ImageView ivShopIcon;

    public BottomAccountView(Context context) {
        super(context);
        initViews(context);
    }

    public BottomAccountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public BottomAccountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BottomAccountView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_bottom_account, this, true);
        btnBooking = (Button) findViewById(R.id.btn_booking);
        vBadge = (BGABadgeFrameLayout)findViewById(R.id.badge_view);
        tvShoppingNull = (TextView) findViewById(R.id.tv_shoping_null);
        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        ivShopIcon = (ImageView) findViewById(R.id.iv_shop_icon);
        vBadge.hiddenBadge();
    }

    //刷新购物车样式
    public void updateVies(double totalPrice , int productionSum){
        if(productionSum == 0){
            btnBooking.setBackground(context.getResources().getDrawable(R.drawable.button_gray));
            btnBooking.setText(context.getString(R.string.select_shopping));
            ivShopIcon.setImageResource(R.drawable.ic_circle_null_shop);
            tvTotalPrice.setVisibility(GONE);
            tvShoppingNull.setVisibility(VISIBLE);
            vBadge.hiddenBadge();
            vBadge.setClickable(false);
            btnBooking.setClickable(false);
        }else{
            tvShoppingNull.setVisibility(GONE);
            tvTotalPrice.setVisibility(VISIBLE);
            tvTotalPrice.setText(FormatCouponInfo.getYuanStr() + FormatCouponInfo.formatDoubleKeepTwo(totalPrice));
            btnBooking.setBackground(context.getResources().getDrawable(R.drawable.button_yellow));
            btnBooking.setText(context.getString(R.string.to_account));
            ivShopIcon.setImageResource(R.drawable.ic_circle_have_shop);
            vBadge.showTextBadge(String.valueOf(productionSum));
            vBadge.setClickable(true);
            btnBooking.setClickable(true);
        }

    }

}
