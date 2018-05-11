package com.atlas.crmapp.activity.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.atlas.crmapp.Atlas;
import com.atlas.crmapp.R;
import com.atlas.crmapp.annotation.StateVariable;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.dagger.component.AppComponent;
import com.atlas.crmapp.register.RecordLoginActivity;
import com.atlas.crmapp.register.RegisterActivity;
import com.atlas.crmapp.util.BarTextColorUtils;
import com.atlas.crmapp.util.SpUtil;
import com.atlas.crmapp.util.StringUtils;
import com.jaeger.library.StatusBarUtil;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lzy.okgo.OkGo;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/13
 *         Description :
 */

public abstract class BaseActivity extends AppCompatActivity {
    private boolean isAlreadyStart = false;

    private boolean canUmengStart = true;
    public  String umengPageTitle ;

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
            MobclickAgent.onResume(this);          //统计时长
            StatService.trackBeginPage(this, umengPageTitle);
            //StatService.trackBeginPage(Context ctx, String pageName)
        }
    }




    private void setUmengPause(){
        if(StringUtils.isNotEmpty(umengPageTitle)){
            MobclickAgent.onPageEnd(umengPageTitle); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
            MobclickAgent.onPause(this);
            StatService.trackEndPage(this, umengPageTitle);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUmengResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setUmengPause();
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    public GlobalParams getGlobalParams() {
        return GlobalParams.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止横屏
        //Logger.e("BaseActivity ","the start activity is : " + this.getClass().getSimpleName());
        initSystemBarTint();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);

    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!isAlreadyStart){
            initActivityViews();
        }
        isAlreadyStart = true;
       /* int num = PushMsgHepler.selectUnreadMsgNumber(Constants.PushMsgTpye.COUPON_BIND_MSG);
        if(num > 0){
            startActivity(new Intent(this, CouponSuccessActivity.class));
        }*/
    }

    //初始化 界面
    protected void initActivityViews(){

    }

    // 网络请求
    protected void prepareActivityData(){

    }

    //更新界面
    protected void updateActivityViews(){

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** 子类可以重写改变状态栏颜色 */
    protected int setStatusBarColor() {
        return getColorPrimary();
    }

    /** 子类可以重写决定是否使用透明状态栏 */
    protected boolean translucentStatusBar() {
        return false;
    }

    /** 设置状态栏颜色 */
    protected void initSystemBarTint() {
        BarTextColorUtils.StatusBarLightMode(this);//改变状态栏字体颜色， 必须在加  <item name="android:windowLightStatusBar">true</item>
        if(translucentStatusBar()){
            StatusBarUtil.setTransparentForImageView(this, null);
        }else{
            StatusBarUtil.setColorNoTranslucent(this, setStatusBarColor());
        }


    }

    /** 获取主题色 */
    public int getColorPrimary() {
        return ContextCompat.getColor(this, R.color.white_color);
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public KProgressHUD dialog;

    public void showLoading(String message) {
        if (dialog != null && dialog.isShowing()) return;
        dialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(message)
                //.setDetailsLabel("Downloading data")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    public void dismissLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {

        super.onSaveInstanceState(bundle);
        Map<String, Object> map = new HashMap<String, Object>();
        Class cls = getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(StateVariable.class)) {
                try {
                    field.setAccessible(true);
                    map.put(field.getName(), field.get(this));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        if(map.size() > 0) {
            bundle.putSerializable("StateMap", (Serializable) map);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        if(bundle.containsKey("StateMap")) {
            Map<String, Object> map = (Map<String, Object>) bundle.getSerializable("StateMap");
            Class cls = getClass();
            for (String key : map.keySet()) {
                try {
                    Field field = cls.getDeclaredField(key);
                    field.setAccessible(true);
                    field.set(this, map.get(key));
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onRestoreInstanceState(bundle);
    }

    public void showAskLoginDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.text_67)
                .setMessage(getResources().getString(R.string.text_68))
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (StringUtils.isEmpty(SpUtil.getString(getApplication(),SpUtil.PHONE,"")))
                            startActivity(new Intent(getApplication(), RegisterActivity.class));
                        else
                            startActivity(new Intent(getApplication(), RecordLoginActivity.class));
                    }
                })
                .show();
    }

    public void showAskLoginDialog(DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(this).setTitle(R.string.text_67)
                .setMessage(getResources().getString(R.string.text_68))
                .setNegativeButton(R.string.cancel, listener)
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (StringUtils.isEmpty(SpUtil.getString(getApplication(),SpUtil.PHONE,"")))
                            startActivity(new Intent(getApplication(), RegisterActivity.class));
                        else
                            startActivity(new Intent(getApplication(), RecordLoginActivity.class));
                    }
                })
                .show();
    }

    public void setFocus(EditText et){
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    public void hideKeyboard(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

    }


    public AppComponent getAppComponent(){
        return ((Atlas)getApplication()).getComponent();
    }

    public int getWidth(){
        return getResources().getDisplayMetrics().widthPixels;
    }

    public int getHeight(){
        return getResources().getDisplayMetrics().heightPixels;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initLanguage(); // 调用读取语言设置且更换APP语言的方法
    }

    private void initLanguage(){

        String locale = Locale.getDefault().toString();
        int type = (int) SpUtil.getLong(getApplication(),SpUtil.LANGUAGE,-1);
        Log.i("initLanguage","locale = " + locale + " ; =" + Locale.ENGLISH.toString() + "; type = " + type);
        //获取当前资源对象
        Resources resources = getResources();
        //获取设置对象
        Configuration configuration = resources.getConfiguration();
        //获取屏幕参数
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();

        if (type == -1)
            if (Locale.SIMPLIFIED_CHINESE.toString().equals(locale)){
                type = 0;
            }else if (Locale.TRADITIONAL_CHINESE.toString().equals(locale) || "zh-rHK".equals(locale) ){
                type = 1;
            }else if (locale.startsWith("en")){
                type = 2;
            }else{
                type = 0;
            }
        Locale locale_ = Locale.SIMPLIFIED_CHINESE;;
        //发送结束所有activity的广播
        switch (type){
            case 0:
                locale_ = Locale.SIMPLIFIED_CHINESE;
                break;
            case 1:
                locale_ = Locale.TRADITIONAL_CHINESE;
                break;
            case 2:
                locale_ = Locale.ENGLISH;
                break;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale_);
        } else {
            configuration.locale = locale_;
        }

        resources.updateConfiguration(configuration, displayMetrics);
    }

}
