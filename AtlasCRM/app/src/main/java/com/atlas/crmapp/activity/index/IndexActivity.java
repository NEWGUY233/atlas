package com.atlas.crmapp.activity.index;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;
import com.atlas.crmapp.MainActivity;
import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.NavFragmentsActivity;
import com.atlas.crmapp.activity.index.fragment.communication.CommunicationFragment;
import com.atlas.crmapp.activity.index.fragment.index.IndexFragment;
import com.atlas.crmapp.adapter.navadapter.MainFragmentNavAdapter;
import com.atlas.crmapp.coffee.OrderConfirmActivity;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.dagger.component.index.DaggerIndexActivityComponent;
import com.atlas.crmapp.dagger.component.index.IndexActivityComponent;
import com.atlas.crmapp.db.hepler.PushMsgHepler;
import com.atlas.crmapp.eventbus.Event;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.push.ReadPushMsg;
import com.atlas.crmapp.util.MTAUtils;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.view.popupwindow.PromotionImagePopup;
import com.atlas.crmapp.widget.CodeDialog;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.atlas.crmapp.db.hepler.PushMsgHepler.selectAllUnreadMsgNumber;

/**
 * Created by Administrator on 2018/3/15.
 */

public class IndexActivity extends NavFragmentsActivity implements CommunicationFragment.onMSGChanged {

    boolean isWorkPlace = true;
    @Override
    protected boolean translucentStatusBar() {
        return isWorkPlace;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);
        EventBusFactory.getBus().register(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        initDagger();
        mFragmentNavigator.setDefaultPosition(3);
        initBtn(3);
        initBtn(2);
    }

    @Override
    protected FragmentNavigatorAdapter getNavAdapter() {
        return new IndexFragmentAdapter();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.index_fragment;
    }

    IndexActivityComponent component;

    private void initDagger() {
        component = DaggerIndexActivityComponent.builder().appComponent(getAppComponent()).build();
        component.inject(this);
    }

    public IndexActivityComponent getComponent() {
        return component;
    }

    //初始化按钮
    @BindView(R.id.bn_iv_workplace)
    ImageView bnIvWorkplace;
    @BindView(R.id.unread_wp_number)
    TextView unreadWpNumber;
    @BindView(R.id.bn_fl_workplace)
    RelativeLayout bnFlWorkplace;

    @BindView(R.id.bn_iv_livingspace)
    ImageView bnIvLivingspace;
    @BindView(R.id.unread_lp_number)
    TextView unreadLpNumber;
    @BindView(R.id.bn_fl_livingspace)
    RelativeLayout bnFlLivingspace;

    @BindView(R.id.bn_fl_index)
    LinearLayout bnFlIndex;
    @BindView(R.id.bn_iv_index)
    ImageView bnIvIndex;

    @BindView(R.id.bn_iv_communication)
    ImageView bnIvCommunication;
    @BindView(R.id.unread_cm_number)
    TextView unreadCmNumber;
    @BindView(R.id.bn_fl_communication)
    RelativeLayout bnFlCommunication;

    @BindView(R.id.bn_iv_mine)
    ImageView bnIvMine;
    @BindView(R.id.unread_mine_number)
    TextView unreadMineNumber;
    @BindView(R.id.bn_fl_mine)
    RelativeLayout bnFlMine;

    private void initBtn(int position) {
        bnIvWorkplace.setImageResource(R.mipmap.menu_icon_workspace_normal);
        bnIvLivingspace.setImageResource(R.mipmap.menu_icon_livingspace_normal);
        bnIvIndex.setImageResource(R.mipmap.menu_icon_home_noraml);
        bnIvCommunication.setImageResource(R.mipmap.menu_icon_community_normal);
        bnIvMine.setImageResource(R.mipmap.menu_icon_me_normal);

        switch (position) {
            case 0:
                bnIvWorkplace.setImageResource(R.mipmap.menu_icon_workspace_sel);
                break;
            case 1:
                bnIvLivingspace.setImageResource(R.mipmap.menu_icon_livingspace_sel);
                break;
            case 2:
                bnIvIndex.setImageResource(R.mipmap.menu_icon_home_sel);
                break;
            case 3:
                bnIvCommunication.setImageResource(R.mipmap.menu_icon_community_sel);
                break;
            case 4:
                bnIvMine.setImageResource(R.mipmap.menu_icon_me_sel);
                break;
        }
//        if (position == 0){
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        }else {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
        mFragmentNavigator.showFragment(position,false);
    }

    @OnClick({R.id.bn_fl_workplace, R.id.bn_fl_livingspace, R.id.bn_fl_index, R.id.bn_fl_communication, R.id.bn_fl_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bn_fl_workplace:
                initBtn(0);
                break;
            case R.id.bn_fl_livingspace:
                initBtn(1);
                break;
            case R.id.bn_fl_index:
                if (mFragmentNavigator.getCurrentPosition() == 2 && mFragmentNavigator.getCurrentFragment() instanceof IndexFragment)
                    ((IndexFragment)mFragmentNavigator.getCurrentFragment()).click();
                initBtn(2);
                break;
            case R.id.bn_fl_communication:
                initBtn(3);
                break;
            case R.id.bn_fl_mine:
                initBtn(4);
                break;
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
            exit();
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && !StringUtils.isEmpty(intent.getStringExtra("from"))){
            if ("SettingLanguageActivity".equals(intent.getStringExtra("from")))
                recreate();
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

    @Override
    protected void onRestart() {
        super.onRestart();

        if (mFragmentNavigator != null && mFragmentNavigator.getFragment(3) != null
                && mFragmentNavigator.getFragment(3) instanceof CommunicationFragment)
            ((CommunicationFragment)mFragmentNavigator.getFragment(3)).onRestart();
    }

    private final long checkAllTime = 1000 * 60 * 10;
    private final long checkInterval = 1000 * 2;
    private final long activityFinshCheckTime = 1000 * 60;
    private long currentCheckAllTime;//当前检查还剩下的时间
    private Event.CheckConfirmOrder confirmOrder;
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
            BizDataRequest.requestCodeOrder(IndexActivity.this, confirmOrder.timestamp, 0, new BizDataRequest.OnResponseOpenOrderJson() {
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

    private int currFragmentPosition;
    public void setCurrentTab(int position) {
        currFragmentPosition = position;
        boolean reset = true;
        if(position == MainFragmentNavAdapter.FM_ECOSPHERE){//设置生态圈重新点击时不初始化
            reset = false;
        }

//        mFragmentNavigator.showFragment(position, reset);
//        setCurrentFragment(position);
    }


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

    public static int TO_ORDER_DETAIL_REQUST_CODE = 999;
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
                unreadMineNumber.setVisibility(readPushMsg.isHaveUnRead()? View.VISIBLE : View.GONE);
            }else{
            }
        }else{
        }
    }

    public void initChatMsg(){
        if (mFragmentNavigator != null && mFragmentNavigator.getFragment(3) != null
                && mFragmentNavigator.getFragment(3) instanceof CommunicationFragment) {
            ((CommunicationFragment) mFragmentNavigator.getFragment(3)).setOnChanged(this);
        }
    }

    @Override
    public void onChanged(long number) {
        if (number == 0){
            unreadCmNumber.setVisibility(View.GONE);
        }else {
            unreadCmNumber.setVisibility(View.VISIBLE);
        }
        unreadCmNumber.setText(number > 99 ? String.valueOf(99) : String.valueOf(number));
    }
}
