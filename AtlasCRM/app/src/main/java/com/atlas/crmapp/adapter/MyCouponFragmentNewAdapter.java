package com.atlas.crmapp.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.UseableCouponsModel;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by hoda on 2017/9/12.
 */

public class MyCouponFragmentNewAdapter extends BaseQuickAdapter<UseableCouponsModel, BaseViewHolder>{

    private String bizCode;
    private Context mContext;
    private List<UseableCouponsModel> mData;
    private int fragmentIndex ;
    private View contentView;
    private TextView tvType;
    private TextView tvName;
    private View line;
    private TextView tvCanUseNum;
    private TextView tvNumDay;
    private int redColor ;
    private int whiteColor;


    public MyCouponFragmentNewAdapter(Context context, List<UseableCouponsModel> data, int fragmentIndex) {
        super(R.layout.item_my_new_coupon, data);
        mData = data;
        mContext = context;
        redColor = ContextCompat.getColor(context, R.color.red);
        whiteColor = ContextCompat.getColor(context, R.color.white_color);
        this.fragmentIndex = fragmentIndex;
    }

    @Override
    protected void convert(BaseViewHolder helper, UseableCouponsModel item) {
        bizCode = item.coupon.coupon.targetBizCode;
        contentView = helper.getConvertView();
        tvType = helper.getView(R.id.tv_coupon_type);
        tvName = helper.getView(R.id.tv_coupon_name);
        line = helper.getView(R.id.v_line);
        tvCanUseNum = helper.getView(R.id.tv_can_user_num);
        tvNumDay = helper.getView(R.id.tv_valid_end_day_num);

        tvName.setText(item.coupon.coupon.name);//item.coupon.coupon.name
        tvType.setText(FormatCouponInfo.getCouponTypeName(bizCode));

        String couponNum;
        String numDay;
        if (fragmentIndex == 0){
            long numDays =  DateUtil.getInFutureDayNum(item.bind.validEnd);
            numDay = mContext.getString(R.string.text_sheng) + DateUtil.getInFutureDayNum(item.bind.validEnd) + mContext.getString(R.string.text_tian);
            if(numDays < 4){
                tvNumDay.setBackgroundResource(R.drawable.coupon_remain_day_bg);
                tvNumDay.setTextColor(redColor);
            }else{
                tvNumDay.setBackgroundResource(R.drawable.coupon_day_bg);
                tvNumDay.setTextColor(whiteColor);
            }
            tvNumDay.setText(numDay);
            tvNumDay.setVisibility(View.VISIBLE);
            couponNum = mContext.getString(R.string.text_suliang) +item.bind.quota ;
        }else {
            couponNum = mContext.getString(R.string.text_suliang) + item.bind.used ;
            if( fragmentIndex == 2){
                tvCanUseNum.setVisibility(View.GONE);
            }
            tvNumDay.setVisibility(View.GONE);
        }
        tvCanUseNum.setText(couponNum);

        GradientDrawable couponBgDrawable = null;
        if(couponBgDrawable == null){
            couponBgDrawable =(GradientDrawable) mContext.getResources().getDrawable(R.drawable.bg_white_circular);
        }
        couponBgDrawable.setColor(FormatCouponInfo.getCouponBg(item.coupon.coupon.targetBizCode));

        helper.setImageDrawable(R.id.iv_coupon_bg, couponBgDrawable)//背景
                .setImageResource(R.id.iv_coupon_icon, FormatCouponInfo.getCouponBgIcon(item.coupon.coupon.targetBizCode))//图标
            .addOnClickListener(R.id.rl_item_coupon);

    }



}
