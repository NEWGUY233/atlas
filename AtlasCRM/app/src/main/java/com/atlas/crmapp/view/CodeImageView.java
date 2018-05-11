package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.util.QRCodeUtil;
import com.atlas.crmapp.util.StringUtils;

/**
 * Created by hoda on 2017/12/13.
 */

public class CodeImageView extends RelativeLayout{
    public final static String TYPE_ACTIVITY_VERIFICATION = "TYPE_ACTIVITY_VERIFICATION"; //活动核销

    private Context context;
    private ImageView ivCode, ivMask;
    public CodeImageView(Context context) {
        super(context);
        initViews(context);
    }

    public CodeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public CodeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CodeImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);

    }

    private void initViews(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_code_image, this, true);
        ivCode = (ImageView) findViewById(R.id.iv_qrcode);
        ivMask = (ImageView) findViewById(R.id.iv_mask_code);
    }

    public void updateView(String stutas, String url){
        // UNPAID- 未付款，PAID - 已支付，COMPLETE - 已核消，EXPIRED- 过期
            if(StringUtils.isNotEmpty(url)){
                ivCode.setImageBitmap(QRCodeUtil.encodeAsBitmap(context, url));
            }
            if(Constants.ACTIVITIES_APPLY_STATUS.PAID.equals(stutas)){
                ivMask.setVisibility(GONE);
            }else{
                ivMask.setVisibility(VISIBLE);
                if(Constants.ACTIVITIES_APPLY_STATUS.EXPIRED.equals(stutas)){
                    ivMask.setImageResource(R.drawable.coupon_code_mask_out);
                }else{
                    ivMask.setImageResource(R.drawable.coupon_code_mask_used);
                }
            }

    }







}
