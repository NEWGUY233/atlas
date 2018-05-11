package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.StringUtils;

/**
* @author: Hoda_fang
* @date: 2017/6/19 16:46
* @desc: 咖啡产品详情 价格view
*/

public class ProductInfoView extends LinearLayout{
    private TextView tvName, tvPrice ,tvDesc;
    public ProductInfoView(Context context) {
        super(context);
        initViews(context);
    }

    public ProductInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public ProductInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProductInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        LayoutInflater.from(context).inflate(R.layout.view_product_info, this, true);
        tvName = (TextView) findViewById(R.id.tv_coffer_name);
        tvPrice = (TextView) findViewById(R.id.tv_coffer_price);
        tvDesc = (TextView) findViewById(R.id.tv_coffer_description);
    }

    public void updateViews(String name, double price ,String desc){
        updateViews(name, price, desc, false);
    }

    public void updateViews(String name, double price ,String desc, boolean haveFitContractOrAllowance){
        if(StringUtils.isNotEmpty(name)){
            tvName.setText(name);
        }
        tvPrice.setText(FormatCouponInfo.getFitContractOrAllowancePrice(price, 2, haveFitContractOrAllowance));
        if(StringUtils.isNotEmpty(desc)){
            tvDesc.setText(desc);
        }else{
            tvDesc.setVisibility(GONE);
        }
    }
}
