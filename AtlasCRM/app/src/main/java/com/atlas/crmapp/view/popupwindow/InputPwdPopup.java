package com.atlas.crmapp.view.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.jungly.gridpasswordview.GridPasswordView;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by hoda on 2017/8/30.
 * 密码输入框
 */

public class InputPwdPopup extends BasePopupWindow {
    private Context context;
    private View contentView;
    private ResponseOpenOrderJson confirmOrder;
    private GridPasswordView gpv;
    private OnPwdInputCompleteListener onPwdInputCompleteListener;

    public InputPwdPopup(Activity context, ResponseOpenOrderJson confirmOrder) {
        super(context);
        this.context = context;
        this.confirmOrder = confirmOrder;

        bindEvent();
    }

    public void setOnPwdInputCompleteListener(OnPwdInputCompleteListener onPwdInputCompleteListener) {
        this.onPwdInputCompleteListener = onPwdInputCompleteListener;
    }

    public void setOnOutSideClick (View.OnClickListener onClickListener){
        if(mDismissView != null){
            dismiss();
            mDismissView.setOnClickListener(onClickListener);
        }
    }

    private void bindEvent(){
        if(contentView == null){
            return;
        }

        gpv =(GridPasswordView) contentView.findViewById(R.id.gpv_normal);
        TextView tvPrice = (TextView) contentView.findViewById(R.id.tv_price);
        TextView payInfo = (TextView)contentView.findViewById(R.id.pay_info);
        if(confirmOrder != null){
            tvPrice.setText(FormatCouponInfo.formatDoublePrice(confirmOrder.getActualAmount(), 2)+getContext().getString(R.string.yuan));
        }else{
            tvPrice.setVisibility(View.GONE);
            payInfo.setText(R.string.s78);
        }

        gpv.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                if(!TextUtils.isEmpty(psw)){
                    if(psw.length() == 6){
                        if(onPwdInputCompleteListener != null){
                            onPwdInputCompleteListener.onPwdInputComplete(psw);
                            dismiss();
                        }
                    }
                }
            }

            @Override
            public void onInputFinish(String psw) {

            }
        });
        gpv.requestFocus();

        /*InputMethodManager imm = (InputMethodManager) gpv.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);*/
        ImageView iv_back = (ImageView) contentView.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setAutoShowInputMethod(true);

    }

    @Override
    public EditText getInputView() {
        return (EditText) gpv.findViewById(R.id.inputView);
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return contentView.findViewById(R.id.pwd_click_dismiss);
    }

    @Override
    public View onCreatePopupView() {
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.popwindow_password, null);
        return contentView;
    }

    @Override
    public View initAnimaView() {
        return null;
    }

    public interface OnPwdInputCompleteListener{
        void onPwdInputComplete(String pwd);
    }

}
