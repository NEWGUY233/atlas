package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;

/**
 * Created by Administrator on 2017/6/8.
 */

public class PayChannelView extends LinearLayout{
    private Context context;
    private ImageView leftImageViwe ;
    private TextView rightTextView;
    private TextView tvDesc;
    private ImageView rightImageView;


    public PayChannelView(Context context) {
        super(context);
        initViews(context);
    }

    public PayChannelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public PayChannelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PayChannelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_pay_channel, this,true);
        leftImageViwe = (ImageView) findViewById(R.id.left_image);
        rightTextView = (TextView) findViewById(R.id.tv_right_channel);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        rightImageView = (ImageView) findViewById(R.id.iv_right);

    }

    public void updateViews(int drawableId, String channel, String desc, boolean isSelect){
        leftImageViwe.setImageResource(drawableId);
        rightTextView.setText(channel);
        if(!TextUtils.isEmpty(desc)){
            tvDesc.setVisibility(VISIBLE);
            tvDesc.setText(Html.fromHtml(getResources().getString(R.string.balance, desc)));
        }else{
            tvDesc.setVisibility(GONE);
        }
        if(isSelect){
            rightImageView.setImageResource(R.drawable.pay_channel_select);
        }else{
            rightImageView.setImageResource(R.drawable.pay_channel_not_select);
        }
    }
}
