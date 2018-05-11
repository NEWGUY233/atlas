package com.atlas.crmapp.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.EaseCubicInterpolator;
import com.atlas.crmapp.widget.BackgroundLayout;
import com.github.sundeepk.compactcalendarview.AnimatorListener;

/**
 * Created by hoda on 2017/8/9.
 * 自定义loading 页面
 */

public class KProgressHUDView  extends RelativeLayout{
    private Context context;

    private BackgroundLayout backgroundLayout;
    private ImageView ivLoading;


    public KProgressHUDView(Context context) {
        super(context);
        initViews(context);
    }

    public KProgressHUDView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public KProgressHUDView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public KProgressHUDView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_kp_progress, this, true);
        ivLoading = (ImageView) findViewById(R.id.tv_loading_top);
        backgroundLayout = (BackgroundLayout) findViewById(R.id.bg_layout);
        backgroundLayout.setBaseColor(context.getResources().getColor(R.color.kprogresshud_default_color));
        backgroundLayout.setCornerRadius(10);

    }
    public void updateViews(boolean isVisiable){
        if(backgroundLayout != null){
            if(isVisiable){
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
                this.setVisibility(VISIBLE);
            }else{
                this.setVisibility(GONE);
            }
        }
    }


}
