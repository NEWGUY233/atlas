package com.atlas.crmapp.view.popupwindow;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.ResponseOpenOrderJson;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by hoda on 2017/9/4.
 */

public class FingerprintCheckPopup extends BasePopupWindow{

    private View contentView;
    private ResponseOpenOrderJson confirmOrder;
    private TextView tvCancel, tvFingerTip, tvUserPassword;
    private View vLine;
    private boolean isOnlyCancel ;

    public FingerprintCheckPopup(Activity context) {
        super(context);

    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return contentView.findViewById(R.id.fg_click_dismiss);
    }

    @Override
    public View onCreatePopupView() {
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.popwindow_fingerprint_check, null);
        tvFingerTip  = (TextView) contentView.findViewById(R.id.tv_finger_tip);
        tvCancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        tvUserPassword = (TextView) contentView.findViewById(R.id.tv_used_password);
        vLine = contentView.findViewById(R.id.v_line_vertical);
        return contentView;
    }

    @Override
    public View initAnimaView() {
        return null;
    }

    public void setOnOutSideClick (View.OnClickListener onClickListener){
        if(mDismissView != null){
            dismiss();
            mDismissView.setOnClickListener(onClickListener);
        }
    }

    public void setMyOnClickListener (View.OnClickListener onClickListener){
        if(tvUserPassword != null){
            tvUserPassword.setOnClickListener(onClickListener);
        }
        if(tvCancel != null){
            tvCancel.setOnClickListener(onClickListener);
        }
        if(tvFingerTip != null){
            tvFingerTip.setOnClickListener(onClickListener);
        }
        if(mDismissView != null){
            mDismissView.setOnClickListener(onClickListener);
        }
    }

    public void setFingerTip(String tip){
        if(tvFingerTip != null){
            tvFingerTip.setText(tip);
        }
    }

    public void updateView( boolean isOnlyCancel, View.OnClickListener onClickListener){
        updateView(isOnlyCancel);
        setMyOnClickListener(onClickListener);
    }

    public void updateView(boolean isOnlyCancel){
        this.isOnlyCancel = isOnlyCancel;
        if(isOnlyCancel){
            tvUserPassword.setVisibility(View.GONE);
            vLine.setVisibility(View.GONE);
        }else{
            tvUserPassword.setVisibility(View.VISIBLE);
            vLine.setVisibility(View.VISIBLE);
        }
    }





}
