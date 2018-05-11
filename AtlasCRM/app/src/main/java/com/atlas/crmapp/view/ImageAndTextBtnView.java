package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;

/**
 * Created by hoda_fang on 2017/5/5.
 *  左边图片右边文字按钮
 *  办公空间  健身工仿
 */

public class ImageAndTextBtnView extends RelativeLayout{
    private Context context;
    private TextView tvBtnRight;
    private ImageView ivBtnLeft;
    public ImageAndTextBtnView(Context context) {
        super(context);
        initView(context);
    }

    public ImageAndTextBtnView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ImageAndTextBtnView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImageAndTextBtnView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private  void  initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.view_btn_image_and_text, this, true);
        tvBtnRight = (TextView) findViewById(R.id.tv_btn_right);
        ivBtnLeft = (ImageView) findViewById(R.id.iv_btn_left);
    }
    // 左边图片id ， rightText右边文字
    public void updateViews(int leftImageRes, String rightText){
        if (!TextUtils.isEmpty(rightText)){
            tvBtnRight.setText(rightText);
            ivBtnLeft.setBackground(getResources().getDrawable(leftImageRes));
        }
    }

}
