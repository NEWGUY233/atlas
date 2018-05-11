package com.atlas.crmapp.view.popupwindow;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.view.KProgressHUDView;
import com.atlas.crmapp.widget.BackgroundLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by hoda on 2017/10/10.
 */

public class CodePopup extends BasePopupWindow {
    private View contentView;
    private Unbinder unbinder;


    private long timestamp;
    private View mainView;
    private int curWindowLight;

    @BindView(R.id.tv_company)
    TextView tvCompany;

    @BindView(R.id.tv_name)
    TextView tvName;


    @BindView(R.id.iv_code)
    ImageView ivCode;

    @BindView(R.id.iv_header)
    ImageView ivHeader;

    @BindView(R.id.bg_layout)
    BackgroundLayout backgroundLayout;

    @BindView(R.id.rl_loading)
    RelativeLayout rlLoading;

    @BindView(R.id.v_loading)
    KProgressHUDView vLoading;

    public CodePopup(Activity context) {
        super(context);
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return contentView.findViewById(R.id.code_click_dismiss);
    }

    @Override
    public View onCreatePopupView() {
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.popup_code, null);
        unbinder = ButterKnife.bind(this, contentView);
        return contentView;
    }

    @Override
    public View initAnimaView() {
        return null;
    }

    @Override
    public void onDismiss() {
        super.onDismiss();
        unbinder.unbind();
    }
}
