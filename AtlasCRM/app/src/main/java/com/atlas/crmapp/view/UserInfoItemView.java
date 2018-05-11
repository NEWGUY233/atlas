package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.google.zxing.common.StringUtils;

/**
 * Created by hoda_fang on 2017/5/23.
 * 个人中心 页面条目
 */

public class UserInfoItemView extends LinearLayout{
    private Context context;
    private TextView tvLeft;
    private TextView tvRight;
    private LinearLayout llRight;

    public UserInfoItemView(Context context) {
        super(context);
        initViews(context);
    }

    public UserInfoItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public UserInfoItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UserInfoItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        LayoutInflater.from(context).inflate(R.layout.view_user_info, this, true);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvRight = (TextView) findViewById(R.id.tv_right);
    }

    public void updateView (String leftStr, String rightStr){
        if(!TextUtils.isEmpty(leftStr)){
            tvLeft.setText(leftStr);
        }

        if(!TextUtils.isEmpty(rightStr)){
            tvRight.setText(rightStr);
        }else{
            tvRight.setText(R.string.s86);
        }
    }

    public void updateView (String leftStr, String rightStr,int color){
        if(!TextUtils.isEmpty(leftStr)){
            tvLeft.setText(leftStr);
        }

        if(!TextUtils.isEmpty(rightStr)){
            tvRight.setText(rightStr);
            tvRight.setTextColor(getResources().getColor(color));
        }else{
            tvRight.setText(R.string.s86);
        }
    }

    public String getText(){
        return getContext().getString(R.string.s86).equals(tvRight.getText().toString())?"":tvRight.getText().toString();
    }
}
