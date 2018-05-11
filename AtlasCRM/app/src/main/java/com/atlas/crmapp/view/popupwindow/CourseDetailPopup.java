package com.atlas.crmapp.view.popupwindow;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.ClassJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.GlideUtils;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by hoda on 2017/9/19.
 */

public class CourseDetailPopup extends BasePopupWindow {
    private View contentView;
    private ClassJson currentClass;
    private boolean isFitContractOrAllowance;
    private View.OnClickListener onClickListener;
    private TextView btnPay;


    public CourseDetailPopup(Activity context, ClassJson currentClass,  boolean isFitContractOrAllowance) {
        super(context);
        this.currentClass = currentClass;
        this.isFitContractOrAllowance = isFitContractOrAllowance;
        bindEvent();
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return contentView.findViewById(R.id.ll_dismiss);
    }

    @Override
    public View onCreatePopupView() {
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.view_fitness_bottom_dialog, null);
        return contentView;
    }

    @Override
    public View initAnimaView() {
        return null;
    }


    private void bindEvent(){
        TextView tv_name = (TextView) contentView.findViewById(R.id.tv_name);
        TextView tv_sub_title = (TextView) contentView.findViewById(R.id.tv_sub_title);
        TextView tv_price = (TextView) contentView.findViewById(R.id.tv_price);
        ImageView shape_iv = (ImageView) contentView.findViewById(R.id.shape_iv);
        TextView tv_date = (TextView) contentView.findViewById(R.id.tv_date);
        TextView tv_address = (TextView) contentView.findViewById(R.id.tv_address);
        btnPay = (TextView)contentView.findViewById(R.id.btn_pay);
        tv_name.setText(currentClass.lessonName);
        tv_sub_title.setText(currentClass.coachName);//+currentClass.major
        tv_price.setText(FormatCouponInfo.getFitContractOrAllowancePrice(currentClass.price, 0, isFitContractOrAllowance));
        tv_date.setText(DateUtil.times(currentClass.startTime, "HH:mm") + "-" + DateUtil.times(currentClass.endTime, "HH:mm")); //显示当前的年月日
        tv_address.setText(currentClass.venue);
        GlideUtils.loadCustomImageView(getContext(), R.drawable.product, LoadImageUtils.loadMiddleImage(currentClass.thumbnail),shape_iv);
    }


    public void setBottomOnClickListener(View.OnClickListener onClickListener){
        if(onClickListener != null &&  btnPay != null){
            btnPay.setOnClickListener( onClickListener);
        }
    }


}
