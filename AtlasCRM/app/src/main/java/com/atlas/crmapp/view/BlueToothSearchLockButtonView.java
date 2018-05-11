package com.atlas.crmapp.view;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.atlas.crmapp.R;
import com.atlas.crmapp.model.LockJson;
import com.atlas.crmapp.util.UiUtil;
import com.github.sundeepk.compactcalendarview.AnimatorListener;
import com.orhanobut.logger.Logger;

/**
 * Created by hoda on 2017/10/11.
 */

public class BlueToothSearchLockButtonView  extends RelativeLayout{
    private ImageView ivArr;
    private TextView tvDoorName;
    private LottieAnimationView vCenterLottie;
    private View vCover;
    private ImageView ivLock;
    private int lastX;
    private Context context;
    private OnCanOpenDoor onCanOpenDoor;

    private int ivLockLeft;
    private int ivArrLeft;
    private int ivArrRight;
    private int ivLockWidth;
    private int validDistance;
    private int ivArrMoveToMaxLeft;//箭头移动到最右边的距离
    private boolean isReachMaxLeft; //是否到达 最右边
    private boolean isLoading ;

    public static final String LOAD_FINISH = "LOAD_FINISH";//开锁打勾
    public static final String LOAD_UN_FINISH = "LOAD_UN_FINISH";//开锁 转圆圈



    public BlueToothSearchLockButtonView(Context context) {
        super(context);
        initViews(context);
    }

    public BlueToothSearchLockButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);

    }

    public BlueToothSearchLockButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BlueToothSearchLockButtonView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_button_blue_tooth_search_lock, this, true);
        ivArr = (ImageView) findViewById(R.id.iv_arr);
        tvDoorName = (TextView) findViewById(R.id.tv_door_name);
        vCover = findViewById(R.id.v_cover);
        ivLock = (ImageView) findViewById(R.id.iv_lock);
        vCenterLottie = (LottieAnimationView) findViewById(R.id.v_center_lottie);
        validDistance = UiUtil.dipToPx(context, 80);
        initParams();
    }


    private void initParams(){
        ivLock.post(new Runnable() {
            @Override
            public void run() {
                ivLockLeft = ivLock.getLeft();
                ivLockWidth = ivLock.getWidth();
                ivArrLeft =  ivArr.getLeft();
                ivArrRight = ivArr.getRight();
                ivArrMoveToMaxLeft = ivLock.getLeft() - 2 * ivLockWidth;
                Logger.d(" ivLockLeft :" + ivLockLeft + " ivArrLeft:" + ivArrLeft);
            }
        });
    }

    public void updateViews(LockJson lockJson, OnCanOpenDoor onCanOpenDoor){
        this.onCanOpenDoor = onCanOpenDoor;
        tvDoorName.setText(lockJson.getDoorName());
        if(lockJson.isCollected()){
            ivLock.setImageResource(R.drawable.blue_tooth_tag);
        }else{
            ivLock.setImageResource(R.drawable.blue_tooth_lock);
        }
        if(ivArrMoveToMaxLeft == 0){
            initParams();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(isLoading){//防止多次 滑动。
            return  true;
        }

        if(ivArrMoveToMaxLeft == 0){
            initParams();
        }
        Logger.d("event.getAction()" + event.getAction() );
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                if(lastX - ivArr.getLeft() > validDistance){//控制滑动 点击时， 有效区域
                    return true;
                }
                isReachMaxLeft = false;
                break;

            case MotionEvent.ACTION_MOVE:
                int currentIvArrLeft = ivArr.getLeft();
                int dx = (int) event.getRawX() - lastX;
                lastX = (int) event.getRawX();

                Log.d("ACTION_MOVE"," lastX：" + lastX  + "   dy："+ dx + "  currentIvArrLeft: "  + currentIvArrLeft  + "  ivArrMoveToMaxLeft:" +ivArrMoveToMaxLeft);

                if(currentIvArrLeft >= ivArrMoveToMaxLeft ){//到达最右边
                    isReachMaxLeft = true;
                    return true;
                }else{
                    isReachMaxLeft = false;
                }

                if(dx > 0){
                    ivArr.offsetLeftAndRight(dx);
                    if(vCover.getVisibility() == INVISIBLE){
                        vCover.setVisibility(VISIBLE);
                        ivArr.setImageResource(R.drawable.blue_tooth_white_right_arr);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(!isReachMaxLeft){
                    moveToStart();
                }else {
                    if(onCanOpenDoor != null){
                        showLoading(LOAD_UN_FINISH);
                        onCanOpenDoor.onCanOpenListener();
                        isReachMaxLeft = false;
                    }
                }
                break;
        }

        return true;

    }



    private void moveToStart(){
        final int dis = -(ivArr.getLeft() - ivArrLeft);
        ivArr.offsetLeftAndRight(dis);
        ivArr.setImageResource(R.drawable.blue_tooth_black_right_arr);
        ivArr.setVisibility(VISIBLE);
        ivLock.setVisibility(VISIBLE);
        vCover.setVisibility(INVISIBLE);
        tvDoorName.setVisibility(VISIBLE);
    }

    public void showLoading(String loadingState){
        isReachMaxLeft = false;
        vCenterLottie.setVisibility(VISIBLE);
        showLoading(true);
        if(LOAD_UN_FINISH.equals(loadingState)){
            vCenterLottie.setAnimation("btn_loading_unflish.json");
            vCenterLottie.loop(true);
        }else if(LOAD_FINISH.equals(loadingState)){
            vCenterLottie.setAnimation("btn_loading_flish.json");
            vCenterLottie.loop(false);
            vCenterLottie.addAnimatorListener(new AnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    showLoading(false);

                }
            });
        }
        vCenterLottie.playAnimation();
    }

    private void showLoading(boolean isShowLoading){
        isLoading = isShowLoading;
        if(isShowLoading){
            vCenterLottie.setVisibility(VISIBLE);
            ivArr.setVisibility(GONE);
            ivLock.setVisibility(GONE);
            tvDoorName.setVisibility(GONE);
            vCover.setVisibility(INVISIBLE);
        }else{
            vCenterLottie.cancelAnimation();
            vCenterLottie.clearAnimation();
            vCenterLottie.setVisibility(GONE);
            moveToStart();
        }
    }

    public void showError(){
        showLoading(false);
        TranslateAnimation  translateAnimation = new TranslateAnimation(-15, 15, 0, 0);
        translateAnimation.setDuration(100);
        translateAnimation.setRepeatCount(4);
        this.startAnimation(translateAnimation);
    }

    public interface OnCanOpenDoor{

        //滑动到 最右边 可以开门时
        void onCanOpenListener();
    }



}
