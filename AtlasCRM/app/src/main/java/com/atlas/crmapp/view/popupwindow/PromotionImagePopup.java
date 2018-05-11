package com.atlas.crmapp.view.popupwindow;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.util.ActionUriUtils;
import com.atlas.crmapp.util.OverShootInterpolator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by hoda on 2017/9/28.广告
 */

public class PromotionImagePopup extends BasePopupWindow {

    private View contentView;
    private ResourceJson.ResourceMedia resourceMedia;

    public PromotionImagePopup(Activity context, ResourceJson.ResourceMedia resourceMedia) {
        super(context);
        this.resourceMedia = resourceMedia;
        bindEvent();

    }

    private void bindEvent(){
        ImageView ivPromotion = (ImageView) contentView.findViewById(R.id.iv_promotion);
        if(resourceMedia != null){
            Glide.with(getContext()).load(resourceMedia.url).apply(new RequestOptions().placeholder(R.drawable.rechager_banner)
//                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
            ).into(ivPromotion);
            ivPromotion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //点击跳转。
                    Intent intent = ActionUriUtils.getIntent(getContext(), resourceMedia);
                    getContext().startActivity(intent);
                    GlobalParams.getInstance().setmAdList(null);
                }
            });
        }
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return contentView.findViewById(R.id.iv_close);
    }

    @Override
    public View onCreatePopupView() {
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.popup_pomotion_image, null);
        return contentView;
    }

    @Override
    public View initAnimaView() {
        return contentView.findViewById(R.id.ll_main);
    }

    @Override
    protected Animator initExitAnimator() {
        AnimatorSet set;
        set = new AnimatorSet();
        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(mAnimaView, "translationY", 0, 400).setDuration(600);
        transAnimator.setInterpolator(new OverShootInterpolator(-6));
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mAnimaView, "alpha", 1f, 0).setDuration(800);
        set.playTogether(transAnimator, alphaAnimator);
        return set;
    }
}
