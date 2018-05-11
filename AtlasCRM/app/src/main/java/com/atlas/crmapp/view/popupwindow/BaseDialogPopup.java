package com.atlas.crmapp.view.popupwindow;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by hoda on 2017/9/6.
 */

public abstract class BaseDialogPopup extends BasePopupWindow {
    private View contentView;
    private View dialogContentView;
    private TextView tvCancel, tvEnter, tvTitle;

    public BaseDialogPopup(Activity context) {
        super(context);
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return contentView.findViewById(R.id.rl_base_popup);
    }

    @Override
    public View onCreatePopupView() {
        contentView = createPopupById(R.layout.popup_base_dialog);
        dialogContentView = LayoutInflater.from(getContext()).inflate(setDialogContentView(), null);
        LinearLayout linearLayout = (LinearLayout) contentView.findViewById(R.id.ll_content);
        linearLayout.addView(dialogContentView);
        tvTitle = (TextView) contentView.findViewById(R.id.tv_dialog_title);
        tvCancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        tvEnter = (TextView) contentView.findViewById(R.id.tv_enter);

        return contentView;
    }

    @Override
    public View initAnimaView() {
        return null;
    }


    /**
     * PopupWindow展示出来后，需要执行动画的View.一般为蒙层之上的View
     */
    protected abstract int setDialogContentView();
}
