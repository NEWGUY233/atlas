package com.atlas.crmapp.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.EaseCubicInterpolator;
import com.github.sundeepk.compactcalendarview.AnimatorListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.orhanobut.logger.Logger;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by hoda on 2017/7/10.
 */

public class KProgressHUDUtils {


    public static KProgressHUD getKProgressHUD(Context context){
        return  KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCustomView(getCustomerView(context))
                .setWindowColor(ContextCompat.getColor(context, R.color.popup_bg))
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }

    public static KProgressHUD showLoading(Context context, String message) {
        return  showLoading(null ,context, message);
    }

    public static KProgressHUD showLoading(KProgressHUD kProgressHUD , Context context, String message) {

        //TODO
        try{
            if(kProgressHUD == null){
                kProgressHUD = getKProgressHUD(context);
            }
            if(context instanceof Activity){
                Activity activity = (Activity) context;
                if(activity.isFinishing()){
                    return kProgressHUD;
                }
            }
            if(StringUtils.isNotEmpty(message)){
                return kProgressHUD
                        .setLabel(message)
                        .show();
            }else{
                return kProgressHUD.show();
            }
        }catch (Exception ex){
            Logger.e(ex.getMessage());
        }
        return  kProgressHUD;

    }

    public static void dismissLoading(final Context context, final KProgressHUD dialog, int afterSeconds) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            try {
                                dialog.dismiss();
                            } catch (Exception e) {

                            }
                        }
                    }
                });
            }
        }, afterSeconds );
    }

    public static View getCustomerView(Context context){
        View gifView =  LayoutInflater.from(context).inflate(R.layout.view_gif, null, false);
        final ImageView ivLoading = (ImageView) gifView.findViewById(R.id.tv_loading_top);
        ObjectAnimator animatorA = ObjectAnimator.ofFloat(ivLoading, "scaleX", 1,1,1,1);
        animatorA.setDuration(1000);
        animatorA.start();
        animatorA.addListener(new AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ObjectAnimator animatorB = ObjectAnimator.ofFloat(ivLoading, "scaleX", 1,0,1,1);
                animatorB.setRepeatCount(ValueAnimator.INFINITE);
                AnimatorSet animatorSet3 = new AnimatorSet();
                animatorSet3.play(animatorB);
                animatorSet3.setDuration(1100);
                animatorSet3.setInterpolator(new EaseCubicInterpolator(0.42f, 0f, 0.58f, 1f));
                animatorSet3.start();
            }
        });


       /* AnimationDrawable animationDrawable = (AnimationDrawable) ivLoading.getDrawable();
        animationDrawable.start();*/

        return gifView;
    }



}
