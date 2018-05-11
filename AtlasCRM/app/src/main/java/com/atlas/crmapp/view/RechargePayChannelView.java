package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.util.StringUtils;

/**
 * Created by hoda on 2017/9/12.
 */

public class RechargePayChannelView extends RelativeLayout {
    private TextView tvPayName;
    private ImageView ivPayLogo, ivSelection;
    public RechargePayChannelView(Context context) {
        super(context);
        initView(context);
    }

    public RechargePayChannelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);

    }

    public RechargePayChannelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RechargePayChannelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);

    }

    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.view_channel_pay_recharge, this, true);

        tvPayName = (TextView) findViewById(R.id.tv_pay_name);
        ivPayLogo = (ImageView) findViewById(R.id.iv_pay_logo);
        ivSelection = (ImageView) findViewById(R.id.iv_selection);
    }

    public void updateView(boolean isSelected, String payName, int res){
        ivSelection.setImageResource(isSelected ? R.drawable.pay_selected : R.drawable.pay_not_selected);
        if(StringUtils.isNotEmpty(payName)){
            tvPayName.setText(payName);
        }
        ivPayLogo.setImageResource(res);
    }
}
