package com.atlas.crmapp.fragment.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.common.statusLayout.OnRetryListener;
import com.atlas.crmapp.common.statusLayout.StatusLayoutImpl;
import com.atlas.crmapp.register.RecordLoginActivity;
import com.atlas.crmapp.register.RegisterActivity;
import com.atlas.crmapp.util.SpUtil;
import com.atlas.crmapp.util.StringUtils;
import com.lzy.okgo.OkGo;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/13
 *         Description :
 */

public abstract class BaseFragment extends Fragment{

    protected BaseActivity mBaseActivity;
    private boolean istranslucentStatusBar;

    protected Context context;
    /**
     * Fragment Content view
     */
    private View inflateView;
    /**
     * 此方法用于返回Fragment设置ContentView的布局文件资源ID
     *
     * @return 布局文件资源ID
     */
    @LayoutRes
    protected abstract int getLayoutId();
    /**
     * 此方法用于设置View显示数据
     */
    protected abstract void initFragmentViews(View inflateView, Bundle savedInstanceState);
    /**
     * 此方法用于初始化成员变量及获取Intent传递过来的数据
     * 注意：这个方法中不能调用所有的View，因为View还没有被初始化，要使用View在initView方法中调用
     */
    protected abstract void initFragmentData(Bundle savedInstanceState);

    public Unbinder unbinder;
    /**
     * 记录是否已经创建了,防止重复创建
     */
    private boolean viewCreated;

    public StatusLayoutImpl statusLayout;

    public GlobalParams getGlobalParams() {
        return GlobalParams.getInstance();
    }

    private boolean canUmengStart = true;
    protected String umengPageTitle ;

    public void setUmengPageTitle(String umengPageTitle) {
        this.umengPageTitle = umengPageTitle;
        if(canUmengStart){
            canUmengStart = false;
            setUmengResume();
        }
    }

    private void setUmengResume(){
        if(StringUtils.isNotEmpty(umengPageTitle)){
            MobclickAgent.onPageStart(umengPageTitle); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        }
    }

    private void setUmengPause(){
        if(StringUtils.isNotEmpty(umengPageTitle)){
            MobclickAgent.onPageEnd(umengPageTitle); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 防止重复调用onCreate方法，造成在initData方法中adapter重复初始化问题
        if (!viewCreated) {
            viewCreated = true;
        }
        initFragmentData(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        setUmengResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        setUmengPause();
    }


    // 网络请求
    protected void prepareFragmentData(){

    }

    //更新界面
    protected void updateFragmentViews(){

    }

    //是否设置带状态页
    protected boolean setStatusView(){
        return true;
    }

    protected int setTopBar(){
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (inflateView == null) {
            // 强制竖屏显示
            //mBaseActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            int layoutResId = getLayoutId();
            if (layoutResId > 0) {
                if(setStatusView()){
                    if(setTopBar() == 0){
                        statusLayout = new StatusLayoutImpl(getHoldingActivity(), getLayoutId(), onRetryListener);
                    }else{
                        statusLayout = new StatusLayoutImpl(getHoldingActivity(), getLayoutId(), setTopBar(), onRetryListener, istranslucentStatusBar);
                    }
                    inflateView = statusLayout.getStatusContenView();
                }else{
                    inflateView = inflater.inflate(layoutResId, container, false);
                }
            }
            //解决点击穿透问题
            inflateView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
        return inflateView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(unbinder == null){
            unbinder = ButterKnife.bind(this, view);
        }
        if (viewCreated) {
            initFragmentViews(view, savedInstanceState);
            viewCreated = false;
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.mBaseActivity = (BaseActivity) context;


    }

    protected BaseActivity getHoldingActivity() {
        return mBaseActivity;
    }


    OnRetryListener onRetryListener = new OnRetryListener() {
        @Override
        public void onRetry(View view, int id) {
            BaseFragment.this.onRefresh(view, id);
        }
    };
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        OkGo.getInstance().cancelTag(this);
        // 解决ViewPager中的问题
        if (null != inflateView) {
            try {
                ((ViewGroup) inflateView.getParent()).removeView(inflateView);
            } catch (Exception ex) {

            }
        }
    }

    @Override
    public void onDestroy() {
        try {
            if (unbinder != null) {
                unbinder.unbind();
            }
        } catch (Exception e) {
            Log.e("Debug", e.getMessage());
        }
        super.onDestroy();


    }
    
    public void showAskLoginDialog(final Context context) {
        new AlertDialog.Builder(context).setTitle(R.string.text_67)
                .setMessage(R.string.text_68)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (StringUtils.isEmpty(SpUtil.getString(context,SpUtil.PHONE,"")))
                            startActivity(new Intent(getHoldingActivity(), RegisterActivity.class));
                        else
                            startActivity(new Intent(getHoldingActivity(), RecordLoginActivity.class));
                    }
                })
                .show();
    }

    protected void onRefresh(View view, int id){

    }


    /*    public static KProgressHUD showLoading(Context context, String message) {
        return KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(message)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    public static void dismissLoading(KProgressHUD dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }*/
}
