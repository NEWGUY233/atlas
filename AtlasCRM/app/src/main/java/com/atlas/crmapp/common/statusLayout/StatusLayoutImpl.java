package com.atlas.crmapp.common.statusLayout;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.network.NetworkUtil;
import com.atlas.crmapp.util.KProgressHUDUtils;
import com.atlas.crmapp.util.StringUtils;
import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * Created by hoda on 2017/7/2.
 */

public class StatusLayoutImpl implements IStatusLayout {
    private Context context;
    private int contentLayoutId;
    private int topBarLayoutId;

    private OnRetryListener onRetryListener;

    private View contentView;
    private boolean showStatusLayout = true;

    public StatusLayoutManager statusLayoutManager;
    private StatusLayoutManager.Builder statusBuilder;

    private KProgressHUD progressHUD;
    private boolean isTranslucentStatusBar;


    public StatusLayoutImpl(Context context, int contentLayoutId, int topBarLayoutId, OnRetryListener onRetryListener, boolean isTranslucentStatusBar){
        this.context = context;
        this.contentLayoutId = contentLayoutId;
        this.topBarLayoutId = topBarLayoutId;
        this.onRetryListener = onRetryListener;
        this.isTranslucentStatusBar = isTranslucentStatusBar;
        initStatusLayoutManager();
        init();
    }

    public StatusLayoutImpl(Context context, int contentLayoutId, OnRetryListener onRetryListener){
        this.context = context;
        this.contentLayoutId = contentLayoutId;
        this.onRetryListener = onRetryListener;
        initStatusLayoutManager();
        initFragment();
    }

    private void initStatusLayoutManager() {
        statusBuilder = StatusLayoutManager.newBuilder(context)
                .contentView(contentLayoutId)
                .emptyDataView(R.layout.view_status_emptydata)
                .errorView(R.layout.view_status_error)
                .loadingView(R.layout.view_status_loadings)
                .errorRetryViewId(R.id.button_retry)
                .onRetryListener(onRetryListener);

        statusLayoutManager = statusBuilder.build();
        statusLayoutManager.showContent();
    }



    private void initFragment(){
        this.contentView  = statusLayoutManager.getRootLayout();
        //GlideUtils.loadGif(context, R.drawable.loading_yellow, (ImageView) contentView.findViewById(R.id.iv_loading));
    }

    private void init(){
        statusBuilder = StatusLayoutManager.newBuilder(context)
                .contentView(contentLayoutId)
                .emptyDataView(R.layout.view_status_emptydata)
                .errorView(R.layout.view_status_error)
                .loadingView(R.layout.view_status_loadings)
                .errorRetryViewId(R.id.button_retry)
                .emptyDataRetryViewId(R.id.button_retry)
                .onRetryListener(onRetryListener);

        statusLayoutManager = statusBuilder.build();
        statusLayoutManager.showContent();

        View mainView = LayoutInflater.from(context).inflate(R.layout.activity_base_toolbar, null, false);
        RelativeLayout containerView = (RelativeLayout)mainView.findViewById(R.id.rl_mian);//获取基本view 跟布局

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout llTitle = (LinearLayout) containerView.findViewById(R.id.ll_title);
        if(topBarLayoutId != BaseStatusActivity.NO_SHOW_TOP_BAR){
            llTitle.addView(LayoutInflater.from(context).inflate( topBarLayoutId == 0 ? R.layout.view_toolbar : topBarLayoutId, llTitle, false));//默认使用toolbar 头部局
            View line = new View(context);
            LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
            line.setBackgroundResource(R.color.divider_gray);
            line.setLayoutParams(lineParams);
            line.setId(R.id.top_line);
            llTitle.addView(line);
            params.addRule(RelativeLayout.BELOW, R.id.ll_title);
        }else {

            containerView.removeAllViews();
        }

        View contentView = statusLayoutManager.getRootLayout();
        contentView.setLayoutParams(params);
        containerView.addView(contentView);
        this.contentView = mainView;
        //GlideUtils.loadGif(context, R.drawable.loading_yellow, (ImageView) contentView.findViewById(R.id.iv_loading));
    }
    Handler myHandler = new Handler();

    Runnable showContentRunable = new Runnable() {
        @Override
        public void run() {
            statusLayoutManager.showContent();
            dismissDialog(0);
        }
    };

    @Override
    public View getStatusContenView() {
        return contentView;
    }

    @Override
    public void showLoading() {
        showLoading("");
    }

    @Override
    public void showLoading(String msg) {
       /* if(statusLayoutManager != null && showStatusLayout()){
            statusLayoutManager.showLoading();
        }*/
      //
        if(context !=null){

            if(progressHUD == null){
                progressHUD = KProgressHUDUtils.getKProgressHUD(context);
            }
            if(StringUtils.isNotEmpty(msg)){
                progressHUD.setLabel(msg);
            }else{
                progressHUD.setLabel(null);
            }
            if(context instanceof Activity && !((Activity) context).isFinishing()){
                progressHUD.show();
            }
        }

        if(StringUtils.isNotEmpty(msg)){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(progressHUD != null && progressHUD.isShowing()){
                        progressHUD.dismiss();
                        showContent();
                    }
                }
            }, NetworkUtil.dimissAfterSeconds );
        }
    }


    @Override
    public void showError(DcnException error) {
        if(statusLayoutManager != null){
            if(error!= null ){
                if(Constants.NetWorkCode.NO_NET_WORK == error.getCode()){
                    statusLayoutManager.showError();
                    dismissDialog(150);
                }else{
                    showLoading(error.getMessage());
                    statusLayoutManager.showContent();
                }
            }
        }
    }



    @Override
    public void showContent(int delayedTime) {
        if(statusLayoutManager != null){
            if(delayedTime == 0){
                statusLayoutManager.showContent();
            }else{
                myHandler.postDelayed(showContentRunable, delayedTime);
            }
        }

    }

    @Override
    public void showContent() {
        showContent(150);
    }

    @Override
    public boolean showStatusLayout() {
        return showStatusLayout;
    }

    public void setShowStatusLayout(boolean showStatusLayout){
        this.showStatusLayout = showStatusLayout;
    }

    @Override
    public void showEmpty() {
        if(statusLayoutManager != null){
            myHandler.removeCallbacks(showContentRunable);
            statusLayoutManager.showEmptyData();
        }
        dismissDialog(0);
    }

    private void dismissDialog(int time){
        if(progressHUD != null){
            KProgressHUDUtils.dismissLoading(context, progressHUD, time);
        }

    }
}
