package com.atlas.crmapp;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;
import com.atlas.crmapp.activity.base.NavFragmentsActivity;
import com.atlas.crmapp.adapter.navadapter.MainFragmentNavAdapter;
import com.atlas.crmapp.coffee.OrderConfirmActivity;
import com.atlas.crmapp.common.BizCode;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.db.hepler.PushMsgHepler;
import com.atlas.crmapp.eventbus.Event;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.huanxin.ChatActivity;
import com.atlas.crmapp.huanxin.Constant;
import com.atlas.crmapp.huanxin.IMHelper;
import com.atlas.crmapp.huanxin.db.InviteMessgeDao;
import com.atlas.crmapp.huanxin.runtimepermissions.PermissionsManager;
import com.atlas.crmapp.huanxin.runtimepermissions.PermissionsResultAction;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.push.ReadPushMsg;
import com.atlas.crmapp.register.RegInfoActivity;
import com.atlas.crmapp.util.MTAUtils;
import com.atlas.crmapp.util.SpUtil;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.util.UiUtil;
import com.atlas.crmapp.view.FindFloatView;
import com.atlas.crmapp.view.popupwindow.PromotionImagePopup;
import com.atlas.crmapp.widget.CodeDialog;
import com.atlas.crmapp.widget.FindDialog;
import com.atlas.crmapp.widget.MainGuideDialog;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

import static com.atlas.crmapp.db.hepler.PushMsgHepler.selectAllUnreadMsgNumber;
import static com.atlas.crmapp.db.hepler.PushMsgHepler.selectUnreadMsgNumber;


public class MainActivity extends NavFragmentsActivity {

    @BindView(R.id.unread_msg_number)
    TextView unreadLabel;

    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.bn_iv_ecosphere)
    ImageView mBnIvEcosphere;
    @BindView(R.id.bn_iv_user)
    ImageView mBnIvUser;

    @BindView(R.id.iv_user_red_dot)
    View ivUserRedDot;

    @BindView(R.id.rl_main)
    RelativeLayout rlMain;

    int findLastX;
    int findLastY;
    Boolean isMove = false;
    FindFloatView mIvFloatingFind;
    public Boolean bIsHidden = false;
    private int screenWidth;
    private int screenHeight;
    private int btnHeight;


    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;
    public static int TO_ORDER_DETAIL_REQUST_CODE = 999;
    public static String KEY_SELECT_FRAGMENT = "KEY_SELECT_FRAGMENT";
    private int currFragmentPosition;
    @Override
    protected FragmentNavigatorAdapter getNavAdapter() {
        return new MainFragmentNavAdapter(MainActivity.this);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fl_container;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBusFactory.getBus().register(this);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        //requestPermissions();//环信请求权限
        // 启动百度push

        initData();

        registerBroadcastReceiver();
        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());//监听好友状态变化
        //List<String> usernames = HuanXinManager.getFriends();
        updateUnreadLabel();
        //regIM();
        //getAllEaseContacts();

        mIvFloatingFind = new FindFloatView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = UiUtil.dipToPx(this, 80);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mIvFloatingFind.setLayoutParams(params);

        if(savedInstanceState == null){
            rlMain.addView(mIvFloatingFind);
        }
        mIvFloatingFind.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(floatingFindBottom!= 0){
                    mIvFloatingFind.setLeft(floatingFindLeft);
                    mIvFloatingFind.setTop(floatingFindTop);
                    mIvFloatingFind.setRight(floatingFindRight);
                    mIvFloatingFind.setBottom(floatingFindBottom);
                }
            }
        });
        mIvFloatingFind.setOnTouchListener(onTouchFindImageView);
        initMainGuideView();
        initRedDot();
        startPromotion();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Logger.d("---JPushInterface-RegistrationID--" + JPushInterface.getRegistrationID(this));

    }

    public static void newInstance(Context context, int selectFragment){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(KEY_SELECT_FRAGMENT, selectFragment);
        context.startActivity(intent);
    }


    private void startPromotion(){
        //未完成注册 跳转至于注册
        PersonInfoJson userInfo = getGlobalParams().getPersonInfoJson();
        if(userInfo != null && getGlobalParams().isLogin()){
            if(StringUtils.isEmpty(userInfo.getNick())){
                Intent intent = new Intent(this, RegInfoActivity.class);
                startActivity(intent);
            }
        }

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        //弹出推广页
        List<ResourceJson.ResourceMedia> resourceMedias = GlobalParams.getInstance().getmAdList();
        if(resourceMedias != null && resourceMedias.size() >0){
           /*Intent intent = ActionUriUtils.getIntent(this, resourceMedias.get(0));
            startActivity(intent);
            GlobalParams.getInstance().setmAdList(null);*/
            PromotionImagePopup promotionImagePopup = new PromotionImagePopup(this, resourceMedias.get(0));
            promotionImagePopup.setPopupWindowFullScreen(true);
            promotionImagePopup.showPopupWindow();
            promotionImagePopup.getPopupWindow().setBackgroundDrawable(new ColorDrawable());
            GlobalParams.getInstance().getmAdList().clear();
            MTAUtils.trackCustomEvent(this, "main_promotion");
        }
    }

    private void initRedDot(){
        if(GlobalParams.getInstance().isLogin()){
            ivUserRedDot.setVisibility(selectUnreadMsgNumber(Constants.PushMsgTpye.COUPON_BIND_MSG) > 0? View.VISIBLE : View.GONE);
            mIvFloatingFind.updateView(selectAllUnreadMsgNumber()> 0 );
        }else{
            ivUserRedDot.setVisibility(View.GONE);
            mIvFloatingFind.updateView(false);
        }

    }

    private CodeDialog mCodeDialog;


    @Subscribe
    public void onPaySuccessToLookCode(Event.EventObject object){
        if(object!= null && Constants.EventType.RECHARGE_LOOK_CODE.equals(object.type)){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setCurrentTab(MainFragmentNavAdapter.FM_MAIN);
                    if (getGlobalParams().isLogin()) {
                        if (mCodeDialog == null) {
                            mCodeDialog = CodeDialog.newInstance();
                        }

                        if (!mCodeDialog.isAdded() && !mCodeDialog.isVisible() && !mCodeDialog.isRemoving()) {
                            mCodeDialog.show(getSupportFragmentManager(), "code_dialog");
                        }
                    } else {
                        showAskLoginDialog();
                    }
                }
            }, 300);
        }
    }



    private final long checkAllTime = 1000 * 60 * 10;
    private final long checkInterval = 1000 * 2;
    private final long activityFinshCheckTime = 1000 * 60;
    private long currentCheckAllTime;//当前检查还剩下的时间
    private Event.CheckConfirmOrder confirmOrder;
    //扫码跳转
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void startCheckConfirmOrder(Event.CheckConfirmOrder confirmOrder){
        myHander.removeCallbacks(checkConfirmCoderThread);
        if(confirmOrder.isStopCheckThread){//二维码页面销毁后 confirmOrder 不能重新赋值 否则confirmOrder.timestamp 会被赋值。
            if(currentCheckAllTime !=0){
                currentCheckAllTime = activityFinshCheckTime;
            }
        }else{
            this.confirmOrder = confirmOrder;
            currentCheckAllTime = checkAllTime;
        }

        if(currentCheckAllTime > 0 ){
            myHander.postDelayed(checkConfirmCoderThread, checkInterval);
        }else{
            myHander.removeCallbacks(checkConfirmCoderThread);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void startOrderConfirmActivity(ResponseOpenOrderJson openOrderJson){
        mHandler.removeCallbacks(checkConfirmCoderThread);
        Intent intent = new Intent(this, OrderConfirmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("type", openOrderJson.getBizCode());
        intent.putExtra("confirmOrder", (Serializable) openOrderJson);
        startActivityForResult(intent, TO_ORDER_DETAIL_REQUST_CODE);
        overridePendingTransition(R.anim.push_bottom_out, R.anim.push_bottom_in);
    }

    //显示底部个人中心小红点
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getReadPushMsgShowUserRedDot(ReadPushMsg readPushMsg){
        if(readPushMsg!= null){
            if(Constants.PushMsgTpye.COUPON_BIND_MSG.equals(readPushMsg.getType())){
                if(!readPushMsg.isHaveUnRead()){
                    PushMsgHepler.updateUnReadMsgTypeToRead(Constants.PushMsgTpye.COUPON_BIND_MSG);
                }
                ivUserRedDot.setVisibility(readPushMsg.isHaveUnRead()? View.VISIBLE : View.GONE);
                mIvFloatingFind.updateView(readPushMsg.isHaveUnRead());
            }else{
                int num = selectAllUnreadMsgNumber();
                if(num > 0){
                    mIvFloatingFind.updateView(true);
                }else{
                    mIvFloatingFind.updateView(false);
                }
            }
        }else{
            mIvFloatingFind.updateView(false);
        }
    }


    Handler myHander = new Handler();
    Runnable checkConfirmCoderThread = new Runnable() {
        @Override
        public void run() {
            currentCheckAllTime = currentCheckAllTime - checkInterval;
            if(currentCheckAllTime > 0){
                myHander.postDelayed(this, checkInterval);
            }else{
                myHander.removeCallbacks(this);
            }
            Logger.d("currentCheckAllTime ---" + currentCheckAllTime );
            BizDataRequest.requestCodeOrder(MainActivity.this, confirmOrder.timestamp, 0, new BizDataRequest.OnResponseOpenOrderJson() {
                @Override
                public void onSuccess(ResponseOpenOrderJson responseOpenOrderJson) {
                    if (responseOpenOrderJson != null) {
                        if(currentCheckAllTime >0){
                            currentCheckAllTime = 0;
                            EventBusFactory.getBus().post(responseOpenOrderJson);
                        }
                    }
                }
                @Override
                public void onError(DcnException error) {

                }
            });
        }
    };



    private void initMainGuideView(){

        if(!SpUtil.getBoolean(this, Constants.SpKey.MAIN_GUIDE_LOOKED, false)){
            MainGuideDialog mainGuideDialog = new MainGuideDialog();
            if (!mainGuideDialog.isAdded() && !mainGuideDialog.isVisible() && !mainGuideDialog.isRemoving()) {
                mainGuideDialog.show(this.getSupportFragmentManager(), "mainguidedialog");
                mainGuideDialog.setCancelable(false);
            }
        }
    }


    private View.OnTouchListener onTouchFindImageView = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    findLastY = (int) event.getRawY();
                    findLastX = (int) event.getRawX();
                    isMove = false;
                    btnHeight = mIvFloatingFind.getHeight();
                    return true;
                case MotionEvent.ACTION_MOVE:

                    int dy = (int) event.getRawY() - findLastY;
                    int dx = (int) event.getRawX() - findLastX;

                    if (Math.abs(dx) > 5 || Math.abs(dy) > 5) {
                        isMove = true;
                    }
                    findLastY = (int) event.getRawY();
                    findLastX = (int) event.getRawX();
                    v.offsetTopAndBottom(dy);
                    v.offsetLeftAndRight(dx);

                    break;

                case MotionEvent.ACTION_UP:

                    if (isMove == false) {
                        if(!getGlobalParams().isLogin()){
                            showAskLoginDialog();
                            return false;
                        }
                        if (mFindDialog == null) {

                            mFindDialog = FindDialog.newInstance(MainActivity.this);
                        }
                        if (!mFindDialog.isAdded() && !mFindDialog.isVisible() && !mFindDialog.isRemoving()) {
                            mFindDialog.show(getSupportFragmentManager(), "find_dialog");
                            MTAUtils.trackCustomEvent(MainActivity.this, "main_find");
                        }
                    } else {
                        int dx1 = (int) event.getRawX() - findLastX;
                        int dy1 = (int) event.getRawY() - findLastY;
                        int left1 = v.getLeft() + dx1;
                        int top1 = v.getTop() + dy1;
                        int right1 = v.getRight() + dx1;
                        int bottom1 = v.getBottom() + dy1;
                        if (left1 < (screenWidth / 2)) {
                            if (top1 < 100) {
//                                v.layout(left1, 0, right1, btnHeight);
                                v.layout(left1 > 0 ? left1 : 0, 0
                                        , (left1 > 0 ? left1 : 0) + v.getWidth()
                                        , btnHeight);
                            } else if (bottom1 > (screenHeight - 200)) {
                                v.layout(left1 > 0 ? left1 : 0, (screenHeight - btnHeight), (left1 > 0 ? left1 : 0) + v.getWidth(), screenHeight);
//                                v.layout(left1, (screenHeight - btnHeight), right1, screenHeight);
                            } else {
                                v.layout(0, top1, btnHeight, bottom1);
                            }
                        } else {
                            if (top1 < 100) {
//                                v.layout(left1, 0, right1, btnHeight);
                                v.layout((right1 > screenWidth ? screenWidth : right1) - v.getWidth()
                                        , 0, right1 > screenWidth ? screenWidth : right1, btnHeight);
                            } else if (bottom1 > (screenHeight - 200)) {
//                                v.layout(left1, (screenHeight - btnHeight), right1, screenHeight);
                                v.layout((right1 > screenWidth ? screenWidth : right1) - v.getWidth()
                                        , (screenHeight - btnHeight), right1 > screenWidth ? screenWidth : right1, screenHeight);
                            } else {
                                v.layout((screenWidth - btnHeight), top1, screenWidth, bottom1);
                            }
                        }

                        floatingFindLeft = mIvFloatingFind.getLeft();
                        floatingFindTop = mIvFloatingFind.getTop();
                        floatingFindBottom = mIvFloatingFind.getBottom();
                        floatingFindRight = mIvFloatingFind.getRight();
                        break;
                    }
                    isMove = false;
                    break;
            }
            return false;
        }
    };

    private void initData() {
        mFragmentNavigator.setDefaultPosition(MainFragmentNavAdapter.FM_MAIN);
        setCurrentTab(mFragmentNavigator.getCurrentPosition());
    }

    private BizCode currentBiz(long id, String code){
        BizCode bizCode = new BizCode();
        bizCode.setUnitId(id);
        bizCode.setBizName(GlobalParams.getInstance().getBusinesseName(code));
        bizCode.setBizCode(code);
        return  bizCode;
    }

    @Override
    protected boolean translucentStatusBar() {
        return true;
    }

    boolean isNeedChange ;
    public void setCurrentTab(int position) {
        currFragmentPosition = position;
        boolean reset = true;
        if(position == MainFragmentNavAdapter.FM_ECOSPHERE){//设置生态圈重新点击时不初始化
            reset = false;
        }

        mFragmentNavigator.showFragment(position, reset);
        setCurrentFragment(position);
    }

    public void setCurrentFragment(int position){
        switch (position) {
            case MainFragmentNavAdapter.FM_COFFEE:
                getGlobalParams().setCurrentBizCode(currentBiz(getGlobalParams().getCoffeeId(), getGlobalParams().getCoffeeCode()));
                break;
            case MainFragmentNavAdapter.FM_WORK_PLACE:
                getGlobalParams().setCurrentBizCode(currentBiz(getGlobalParams().getWorkplaceId(), getGlobalParams().getWorkplaceCode()));
                break;
            case MainFragmentNavAdapter.FM_ECOSPHERE:
                break;
            case MainFragmentNavAdapter.FM_KITCHEN:
                getGlobalParams().setCurrentBizCode(currentBiz(getGlobalParams().getKitchenId(), getGlobalParams().getKitchenCode()));
                break;
            case MainFragmentNavAdapter.FM_FITNESS:
                getGlobalParams().setCurrentBizCode(currentBiz(getGlobalParams().getFitnessId(), getGlobalParams().getFitnessCode()));
                break;
            case MainFragmentNavAdapter.FM_GOGREEN:
                getGlobalParams().setCurrentBizCode(currentBiz(getGlobalParams().getGogreenId(), getGlobalParams().getGogreenCode()));
                break;
            case MainFragmentNavAdapter.FM_STUDIO:
                getGlobalParams().setCurrentBizCode(currentBiz(getGlobalParams().getStudioId(), getGlobalParams().getStudioCode()));
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }


    private void cleanAllTabSelect() {
        mBnIvEcosphere.setSelected(false);
        mBnIvUser.setSelected(false);
    }

    /* 生态圈 */
    @OnClick(R.id.bn_fl_ecosphere)
    public void onEcosphereClick() {
        if (mFragmentNavigator.getCurrentPosition() != MainFragmentNavAdapter.FM_ECOSPHERE) {
            cleanAllTabSelect();
            mBnIvEcosphere.setSelected(true);
            setCurrentTab(MainFragmentNavAdapter.FM_ECOSPHERE);
            bIsHidden = false;
            topViewHidden();
        }
    }

    /* 个人中心 */
    @OnClick(R.id.bn_fl_user)
    public void onUserClick() {
        if (mFragmentNavigator.getCurrentPosition() != MainFragmentNavAdapter.FM_USER) {
            cleanAllTabSelect();
            mBnIvUser.setSelected(true);
            setCurrentTab(MainFragmentNavAdapter.FM_USER);
            bIsHidden = false;
            topViewHidden();
        }
        //initPustCoupon();
    }

    @BindView(R.id.bn_iv_wheel_menu_trigger)
    ImageView btn_menu;

    //主页
    @OnClick(R.id.bn_iv_wheel_menu_trigger)
    public void onMenuClick() {
        if (mFragmentNavigator.getCurrentPosition() != MainFragmentNavAdapter.FM_MAIN) {
            setCurrentTab(MainFragmentNavAdapter.FM_MAIN);
            cleanAllTabSelect();
            bIsHidden = true;
            topViewHidden();
        }
    }

    private int floatingFindLeft,floatingFindBottom,floatingFindRight,floatingFindTop;
   /* @OnTouch(R.id.iv_floating_find)
    boolean onFindMove(View v, MotionEvent event) {

    }*/


    private FindDialog mFindDialog;


    private int menuType = 1;

    private void animaMin() {
        /** 设置缩放动画 */
        final ScaleAnimation animation = new ScaleAnimation(1.0f, 0.85f, 1.0f, 0.85f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
        animation.setDuration(1000);//设置动画持续时间
        /** 常用方法 */
        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        btn_menu.startAnimation(animation);

        menuType = 0;
    }

    private void animaMax() {
        /** 设置缩放动画 */
        final ScaleAnimation animation = new ScaleAnimation(0.85f, 1f, 0.85f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
        animation.setDuration(1000);//设置动画持续时间
        /** 常用方法 */
        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        btn_menu.startAnimation(animation);
        menuType = 1;
    }

/*    @OnClick(R.id.ib_location)
    public void showLocation() {
        MainActivity.this.startActivity(new Intent(MainActivity.this, CoffeeShopActivity.class));
    }*/

    public void topViewHidden() {
        if (bIsHidden) {
            if (menuType == 0) {
                //animaMax();
                btn_menu.setImageResource(R.drawable.ic_bottom_nav_wheel_select);
                menuType = 1;
            }
        } else {
            if (menuType == 1) {
               // animaMin();
                btn_menu.setImageResource(R.drawable.ic_bottom_nav_wheel);
                menuType = 0;
            }
        }
    }

    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(currFragmentPosition != MainFragmentNavAdapter.FM_MAIN
                    && currFragmentPosition != MainFragmentNavAdapter.FM_USER
                    && currFragmentPosition!= MainFragmentNavAdapter.FM_ECOSPHERE){//在业态主页，点击手机物理返回键，会退出应用，应该先返回首页，再按2次返回才退出应用
                setCurrentTab(MainFragmentNavAdapter.FM_MAIN);
            }else{
                exit();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), R.string.s97,
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    private void getAllEaseContacts(){
        try {

                    Map<String, EaseUser> localUsers = IMHelper.getInstance().getContactList();
                    if(localUsers!=null&&localUsers.size()>0) {
                        ArrayList<String> list = new ArrayList<String>();
                        ArrayList<EaseUser> users = new ArrayList<EaseUser>();
                        for (Map.Entry<String, EaseUser> entry : localUsers.entrySet()) {
                            list.add(entry.getKey());
                            users.add(entry.getValue());
                        }

                        getUserInfoList(list,users);
                    }

        }catch (Exception e) {

        }
    }

    private void getUserInfoList(ArrayList<String> list,final ArrayList<EaseUser> localUsers){
        BizDataRequest.requestUidUserInfoNormal(MainActivity.this, list, new BizDataRequest.OnUidUserInfo() {
            @Override
            public void onSuccess(List<PersonInfoJson> personList) {
                for(int i=0;i<personList.size();i++){
                    for(int j=0;j<localUsers.size();j++){
                        if(localUsers.get(j).getUsername().equals(personList.get(i).uid.toLowerCase())){
                            EaseUser user= localUsers.get(j);
                            user.setAvatar(personList.get(i).avatar);
                            user.setNickname(personList.get(i).nick);
                            IMHelper.getInstance().saveContact(user);
                        }
                    }
                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getGlobalParams().isLogin()){
            if(broadcastManager != null){
                broadcastManager.sendBroadcast(new Intent(Constant.ACTION_CONTACT_CHANAGED));
            }
        }

        // unregister this event listener when this activity enters the
        // background
        IMHelper sdkHelper = IMHelper.getInstance();
        sdkHelper.pushActivity(this);
        EMClient.getInstance().chatManager().addMessageListener(messageListener);


        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        IMHelper sdkHelper = IMHelper.getInstance();
        sdkHelper.popActivity(this);

        super.onStop();
    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            EaseUI easeUI = EaseUI.getInstance();
            // notify new message
            for (EMMessage message : messages) {
                IMHelper.getInstance().getNotifier().onNewMsg(message);
                if (!easeUI.hasForegroundActivies()) {
                    easeUI.getNotifier().onNewMsg(message);
                }
            }
            refreshUIWithMessage();
            broadcastManager.sendBroadcast(new Intent(Constant.NEW_MESSAGE));

        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //red packet code : 处理红包回执透传消息

            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            refreshUIWithMessage();
        }
    };

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
            Toast.makeText(getApplicationContext(), "onContactAdded", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
                            username.equals(ChatActivity.activityInstance.toChatUsername)) {
                        String st10 = getResources().getString(R.string.have_you_removed);
                        Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
                                .show();
                        ChatActivity.activityInstance.finish();
                    }
                }
            });
        }
        @Override
        public void onContactInvited(String username, String reason) {
            Toast.makeText(getApplicationContext(), "onContactInvited", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onFriendRequestAccepted(String username) {
            Toast.makeText(getApplicationContext(), "onFriendRequestAccepted", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onFriendRequestDeclined(String username) {}
    }


    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.NEW_MESSAGE);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Constant.NEW_MESSAGE)){
                    updateUnreadLabel();
                }
                if(intent.getAction().equals(Constant.ACTION_CONTACT_CHANAGED)){
                    getAllEaseContacts();
                    updateUnreadLabel();
                }
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private void unregisterBroadcastReceiver(){
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusFactory.getBus().unregister(this);
        unregisterBroadcastReceiver();
    }


    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                updateUnreadLabel();

            }
        });
    }

    /**
     * update unread message count
     */
    InviteMessgeDao inviteMessgeDao;
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if(inviteMessgeDao == null){
            inviteMessgeDao = new InviteMessgeDao(this);
        }
        if (count > 0  || inviteMessgeDao.getUnreadMessagesCount()>0) {
            unreadLabel.setText("");
            unreadLabel.setVisibility(View.VISIBLE);
        } else {
            unreadLabel.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * get unread message count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMessageCount();
        for(EMConversation conversation:EMClient.getInstance().chatManager().getAllConversations().values()){
            if(conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount=chatroomUnreadMsgCount+conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal-chatroomUnreadMsgCount;
    }


}
