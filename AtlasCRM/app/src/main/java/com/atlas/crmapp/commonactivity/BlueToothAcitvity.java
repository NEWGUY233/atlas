package com.atlas.crmapp.commonactivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.BlueToothSearchLockAdapter;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.LockJson;
import com.atlas.crmapp.model.RemoteOpenLockJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.AppUtil;
import com.atlas.crmapp.util.GetCommonObjectUtils;
import com.atlas.crmapp.util.SimpleAnimUtil;
import com.atlas.crmapp.util.SpUtil;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.view.BlueToothSearchLockButtonView;
import com.atlas.crmapp.view.MyRecyclerView;
import com.atlas.crmapp.view.RefreshHeaderView;
import com.liaoinstan.springview.widget.SpringView;
import com.minew.beaconset.BluetoothState;
import com.minew.beaconset.MinewBeacon;
import com.minew.beaconset.MinewBeaconManager;
import com.minew.beaconset.MinewBeaconManagerListener;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.reformer.rfBleService.BleDevContext;
import cn.com.reformer.rfBleService.BleService;
import cn.com.reformer.rfBleService.OnCompletedListener;

import static com.atlas.crmapp.R.id.iv_setting_collect;
import static com.minew.beaconset.BluetoothState.BluetoothStatePowerOff;


/**
 * Created by hoda on 2017/10/12.
 */
public class BlueToothAcitvity extends BaseStatusActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.v_search)
    LottieAnimationView vSearch;
    @BindView(R.id.rl_search)
    RelativeLayout rlSearch;
    @BindView(R.id.rv_room)
    MyRecyclerView rvRoom;
    @BindView(R.id.ll_room_list)
    LinearLayout llRoomList;
    @BindView(R.id.iv_blue_open_fail)
    ImageView ivBlueOpenFail;
    @BindView(R.id.tv_search_tip)
    TextView tvSearchTip;
    @BindView(R.id.springview)
    SpringView springView;
    @BindView(R.id.tv_down_refresh)
    TextView tvDownRefersh;
    @BindView(R.id.tv_bottom_err_msg)
    TextView tvBottomErrMsg;
    @BindView(R.id.rl_mask)
    RelativeLayout rlMask;
    @BindView(R.id.iv_setting_collect)
    ImageView ivSettingCollect;

    private Map<String , Integer > mapBeaconInfo  = new HashMap<>();
    private static final int REQUEST_ENABLE_BT = 2;
    private String uuids;
    private List<LockJson> lockJsons = new ArrayList<>();
    private BlueToothSearchLockAdapter lockAdapter ;
    private List<MinewBeacon> beacons ;
    private MinewBeaconManager mMinewBeaconManager;
    private Handler searchHandler = new Handler();
    private int SEARCH_TIME = 5000;


    //key free
    private BleService.RfBleKey rfBleKey;
    private ServiceConnection serviceConnection;
    private BleService kfBleService;
    private String keyFreePws = "43678C31BE721345CD23FF01EF31AB98";
    private boolean isGzCity = !GlobalParams.getInstance().isKeyfree();

    @OnClick(iv_setting_collect)
    void onClickToCollect(){
        if(rlMask != null){
            if(rlMask.getVisibility() == View.GONE){
                DoorLockCollectActivity.newInstance(this, uuids);
            }
        }
    }

    @OnClick(R.id.iv_close)
    void onClickClose(){
        if(rlMask.getVisibility() == View.VISIBLE){
            rlMask.setVisibility(View.GONE);
        }else{
            finish();
        }
    }

    @OnClick(R.id.rl_mask)
    void onClickMask(){
        if(rlMask.getVisibility() == View.VISIBLE){
            rlMask.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_blue_tooth);

        springView.setHeader(new RefreshHeaderView(this));
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                    //delayedStopSearch();
                    showSearch(true);
                    checkBluetooth();
            }

            @Override
            public void onLoadmore() {

            }
        });
        //requestFindLockList("FDA50693-A4E2-4FB1-AFCF-C6EB07647001,FDA50693-A4E2-4FB1-AFCF-C6EB07647002");

        initBeacon();
        checkBluetooth();

    }

    private void initBeacon(){
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
    }

    /**
     * check Bluetooth state
     */
    private void checkBluetooth() {
        BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
        switch (bluetoothState) {
            case BluetoothStateNotSupported:
                Toast.makeText(this, R.string.text_91, Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BluetoothStatePowerOff:
                delayedStopSearch();
                break;
            case BluetoothStatePowerOn:
                delayedStopSearch();
                break;
        }
    }

    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }

    @Override
    protected boolean translucentStatusBar() {
        return true;
    }

    private void showSearch(boolean isShow) {
        if(isShow){
            rlSearch.setVisibility(View.VISIBLE);
            llRoomList.setVisibility(View.GONE);
            tvTitle.setText(getString(R.string.blue_searching));
            vSearch.playAnimation();
        }else {
            rlSearch.setVisibility(View.GONE);
            llRoomList.setVisibility(View.VISIBLE);
            tvTitle.setText(getString(R.string.you_can_open_room));
            vSearch.cancelAnimation();
        }
    }

    private void requestFindLockList(String uuids){
        SEARCH_TIME = 0;
        BizDataRequest.requestFindLockList(this, uuids, new BizDataRequest.OnResponseFindLockList() {
            @Override
            public void onSuccess(final List<LockJson> lockJsons) {
                BlueToothAcitvity.this.lockJsons.clear();
                if(lockJsons != null && lockJsons.size() > 0){
                    BlueToothAcitvity.this.lockJsons.addAll(lockJsons);
                    //lockAdapter.setData(BlueToothAcitvity.this.lockJsons);
                    if(lockJsons.size() > 0){
                        tvDownRefersh.setVisibility(View.VISIBLE);
                    }else{
                        showMyEmptyView();
                    }
                    showSearch(false);
                }else{
                    showMyEmptyView();
                }

                if(lockAdapter != null){
                    lockAdapter.notifyDataSetChanged();
                }

                showGuideMask();
            }

            @Override
            public void onError(DcnException error) {
                showErrorMsg(getString(R.string.ble_net_error_msg));
            }
        });
    }



    private void delayedStopSearch(){
        if(BluetoothStatePowerOff.equals(mMinewBeaconManager.checkBluetoothState())){//获取权限
            //Toast.makeText(BlueToothAcitvity.this, R.string.ble_not_open, Toast.LENGTH_SHORT).show();
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            return;
        }

        if(isGzCity){
            mMinewBeaconManager.startService();
            mMinewBeaconManager.startScan();
            mMinewBeaconManager.setMinewbeaconManagerListener(minewBeaconManagerListener);
        }else{
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    if(kfBleService == null){
                        kfBleService = ((BleService.LocalBinder) iBinder).getService();
                        if(rfBleKey == null){
                            rfBleKey = kfBleService.getRfBleKey();
                            rfBleKey.init(null);//mWhiteList
                        }
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {

                }
            };
            Intent bindIntent = new Intent(getApplicationContext(), BleService.class);
            bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }


        if(lockAdapter == null){
            lockAdapter = new BlueToothSearchLockAdapter(BlueToothAcitvity.this, lockJsons, onOpentDoorListener, rvRoom, statusLayout );
            rvRoom.setAdapter(lockAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(BlueToothAcitvity.this);
            rvRoom.setLayoutManager(layoutManager);
        }

        searchHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                uuids = "";
                if(isGzCity){
                    uuids = getGZUuids(beacons);
                }else{
                    uuids = getKeyFreeUuids();
                }
                if(StringUtils.isNotEmpty(uuids)){
                    requestFindLockList(uuids.substring(0, uuids.length() -1));
                }else{
                    if(lockAdapter != null){
                        BlueToothAcitvity.this.lockJsons.clear();
                        lockAdapter.notifyDataSetChanged();
                    }
                    showMyEmptyView();
                    showGuideMask();
                }
                GetCommonObjectUtils.onFinishFreshAndLoad(springView);
            }
        }, SEARCH_TIME);
    }


    private String getGZUuids(List<MinewBeacon> beacons){
        String uuids = "";
        //按信号排序
        if(beacons != null  && beacons.size() > 0 ) {
            Collections.sort(beacons, new Comparator<MinewBeacon>() {
                @Override
                public int compare(MinewBeacon minewBeacon1, MinewBeacon minewBeacon2) {
                    return new Double(minewBeacon1.getDistance()).compareTo(new Double(minewBeacon2.getDistance()));
                }
            });
            for (MinewBeacon beacon : beacons) {
                mapBeaconInfo.put(beacon.getUuid(), beacon.getBattery());
                uuids = uuids + beacon.getUuid() + "#" + beacon.getMajor() + "#" + beacon.getMinor() + ",";
            }
        }
        return uuids;
    }


    private String getKeyFreeUuids(){
        String uuids = "";
        if(rfBleKey != null){
            ArrayList<BleDevContext> bleDevContexts = rfBleKey.getDiscoveredDevices();
//            AppUtil.initBlueToothList(bleDevContexts);
            if (bleDevContexts != null && bleDevContexts.size() > 0)
                Collections.sort(bleDevContexts, new Comparator<BleDevContext>() {
                    @Override
                    public int compare(BleDevContext o1, BleDevContext o2) {
                        return new Integer(o2.rssi).compareTo(o1.rssi);
                    }
                });
            if(bleDevContexts != null  && bleDevContexts.size() > 0){
                for(BleDevContext bleDevContext : bleDevContexts){
                    uuids = uuids +  bytesToString(bleDevContext.mac) + "#0#0,";
                }
            }
        }

        return  uuids;
    }



    private MinewBeaconManagerListener minewBeaconManagerListener = new MinewBeaconManagerListener() {
        @Override
        public void onUpdateBluetoothState(BluetoothState bluetoothState) {
        }

        // 4.通过代理方法获取扫描数据更新
        // 此方法定时回调用于获取周围设备的最新扫描数据 1秒
        @Override
        public void onRangeBeacons(List<MinewBeacon> list) {
            BlueToothAcitvity.this.beacons = list;
        }

        // 监听新发现的设备  3秒一次
        @Override
        public void onAppearBeacons(List<MinewBeacon> list) {

        }
        /* 如果要监听设备的进出状态，可以通过实现以下方法 */
        // 监听消失设备  1秒
        @Override
        public void onDisappearBeacons(List<MinewBeacon> list) {

        }
    };

    private void showMyEmptyView(){
        lockAdapter.setEmptyView(R.layout.view_product_null, rvRoom);
        lockAdapter.getEmptyView().findViewById(R.id.rl_empty).setBackgroundColor(Color.WHITE);
        showSearch(false);
        tvDownRefersh.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mMinewBeaconManager != null){
            mMinewBeaconManager.stopScan();
            mMinewBeaconManager.stopService();
        }

        if(serviceConnection != null){
            if(rfBleKey != null){
                rfBleKey.free();
            }
            unbindService(serviceConnection);
        }
    }



    private void showBlueFail(boolean isBlueFail){
        if(isBlueFail ){
            rlSearch.setVisibility(View.VISIBLE);
            ivBlueOpenFail.setVisibility(View.VISIBLE);
            ivBlueOpenFail.setImageResource(R.drawable.blue_tooth_open_fial);
            tvSearchTip.setVisibility(View.VISIBLE);
            vSearch.setVisibility(View.GONE);
        }else{
            rlSearch.setVisibility(View.GONE);
            ivBlueOpenFail.setVisibility(View.GONE);
            tvSearchTip.setVisibility(View.GONE);
            vSearch.setVisibility(View.VISIBLE);
        }
    }
    private void showErrorMsg(String resString){
        if(tvBottomErrMsg.getVisibility() ==View.GONE){
            if(tvBottomErrMsg != null){
                tvBottomErrMsg.setText(resString);
                tvBottomErrMsg.setVisibility(View.VISIBLE);
                tvBottomErrMsg.setAnimation(SimpleAnimUtil.getTranslateAnimation(400, 0, 800));
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(tvBottomErrMsg != null){
                        tvBottomErrMsg.setAnimation(SimpleAnimUtil.getTranslateAnimation(0, 400, 800));
                        tvBottomErrMsg.setVisibility(View.GONE);
                    }
                }
            }, 3800);
        }
    }


    BlueToothSearchLockAdapter.OnOpentDoorListener onOpentDoorListener = new BlueToothSearchLockAdapter.OnOpentDoorListener() {

        @Override
        public void onRequestOpneDoor(final LockJson lockJson, final BlueToothSearchLockButtonView buttonView, final boolean isOneDoorOpen) {
            if(isOneDoorOpen){
                buttonView.showLoading(BlueToothSearchLockButtonView.LOAD_UN_FINISH);//一个门时自动开锁
            }
            if(isGzCity){
                requestRemoteOpenLock(lockJson, buttonView);
            }else{
                if(rfBleKey != null){
                    String keyFreeMac = lockJson.getUuid();
                    if(StringUtils.isNotEmpty(keyFreeMac)){
                        rfBleKey.setOnBleDevListChangeListener(new BleService.OnBleDevListChangeListener() {
                            @Override
                            public void onNewBleDev(BleDevContext bleDevContext) {

                            }

                            @Override
                            public void onUpdateBleDev(BleDevContext bleDevContext) {

                            }
                        });
                        if(keyFreeMac.contains("#0#0") && !keyFreeMac.contains("-")){//判断是否keyfree
                            if(!getKeyFreeUuids().contains(lockJson.getUuid().substring(0,10))){//如果不在蓝牙范围内
                                onOpentDoorFail(buttonView, getString(R.string.result_blue_weak));
                            }else {
//                                rfBleKey.openDoor(stringToBytes(lockJson.getUuid().substring(0, 18)), 3, keyFreePws);
                                try {
                                    rfBleKey.openDoor(stringToBytes(lockJson.getUuid().replaceAll("#0#0","")), 3, keyFreePws);
                                }catch (Exception e){
                                    onOpentDoorFail(buttonView, getString(R.string.result_timeout));
                                    BizDataRequest.doorState(BlueToothAcitvity.this,lockJson.getDoorId(),"0",false,  null);
                                }
                                rfBleKey.setOnCompletedListener(new OnCompletedListener() {
                                    @Override
                                    public void OnCompleted(byte[] bytes, final int i) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                switch (i) {
                                                    case 0:
                                                        onOpentDoorSuccess(buttonView, "");
//                                                        BizDataRequest.doorScore(BlueToothAcitvity.this,false,lockJson.getUuid(),  null);
                                                        BizDataRequest.doorState(BlueToothAcitvity.this,lockJson.getDoorId(),"1",false,  null);
                                                        break;
                                                    case 1:
                                                        //showToast(getString(R.string.result_password_error));
                                                        onOpentDoorFail(buttonView, getString(R.string.result_password_error));
                                                        BizDataRequest.doorState(BlueToothAcitvity.this,lockJson.getDoorId(),"0",false,  null);
                                                        break;
                                                    case 2:
                                                        //showToast(getString(R.string.result_bluetooth_break));
                                                        onOpentDoorFail(buttonView, getString(R.string.result_bluetooth_break));
                                                        BizDataRequest.doorState(BlueToothAcitvity.this,lockJson.getDoorId(),"0",false,  null);
                                                        break;
                                                    case 3:
                                                        //showToast(getString(R.string.result_timeout));
                                                        onOpentDoorFail(buttonView, getString(R.string.result_timeout));
                                                        BizDataRequest.doorState(BlueToothAcitvity.this,lockJson.getDoorId(),"0",false,  null);
                                                        break;
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }else{
                            requestRemoteOpenLock(lockJson, buttonView);
                        }
                    }


                }
            }

        }
    };

    private void requestRemoteOpenLock(LockJson lockJson, final BlueToothSearchLockButtonView buttonView){
        String powner = "";
        if(mapBeaconInfo != null){
            if(mapBeaconInfo.get(lockJson.getUuid().split("#")[0])!= null){
                powner = mapBeaconInfo.get(lockJson.getUuid().split("#")[0]).toString();
            }
        }
        BizDataRequest.requestRemoteOpenLock(BlueToothAcitvity.this, lockJson.getDoorId(), powner, new BizDataRequest.OnResponseRemoteOpenLock() {
            @Override
            public void onSuccess(RemoteOpenLockJson remoteOpenLockJson) {
                if("true".equals(remoteOpenLockJson.getIsOpen()) ){
                    onOpentDoorSuccess(buttonView, "");
                }else{
                    onOpentDoorFail(buttonView, BlueToothAcitvity.this.getString(R.string.onpent_door_fail_retry));
                }
            }

            @Override
            public void onError(DcnException error) {
                String errorStr = "";
                if(error != null){
                    if(error.getCode() == Constants.NetWorkCode.NO_NET_WORK){
                        errorStr = getString(R.string.ble_net_error_msg);
                    }else{
                        errorStr = error.getDescription();
                    }
                }
                onOpentDoorFail(buttonView, errorStr);
            }
        });
    }

    private void onOpentDoorSuccess(BlueToothSearchLockButtonView buttonView, String msg) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(100);
        if(buttonView != null){
            buttonView.showLoading(BlueToothSearchLockButtonView.LOAD_FINISH);
        }
    }

    private void onOpentDoorFail(BlueToothSearchLockButtonView buttonView, String errorMsg) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{0,100,200,100}, -1);
        if(buttonView != null){
            buttonView.showError();
        }
        showErrorMsg(errorMsg);
    }




    private void showGuideMask(){
        ivSettingCollect.setVisibility(View.VISIBLE);
        if(SpUtil.getBoolean(this, Constants.SpKey.KEY_IS_FIRST_OPENT_BLUE, true)){
            rlMask.setVisibility(View.VISIBLE);
            SpUtil.putBoolean(this, Constants.SpKey.KEY_IS_FIRST_OPENT_BLUE, false);
        }else{
            rlMask.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        overridePendingTransition(0,0);
        super.onPause();
    }




    private IntentFilter makeBlueFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        return filter;
    }

    private BroadcastReceiver blueReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.e( "onReceive---------");
            switch(intent.getAction()){
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch(blueState){
                        case BluetoothAdapter.STATE_TURNING_ON:
                            Logger.e("onReceive---------STATE_TURNING_ON");
                            break;
                        case BluetoothAdapter.STATE_ON:
                            Logger.e("onReceive---------STATE_ON");
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            Logger.e("onReceive---------STATE_TURNING_OFF");
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            Logger.e("onReceive---------STATE_OFF");
                            break;
                    }
                    break;
            }
        }
    };


    public static String bytesToString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }
        return hexString.toString().toUpperCase();
    }


    public static byte[] stringToBytes(String outStr){
        if (outStr.length()!=18)
            return null;
        int len = outStr.length()/2;
        byte[] mac = new byte[len];
        for (int i = 0; i < len; i++){
            String s = outStr.substring(i*2,i*2+2);
            if (Integer.valueOf(s, 16)>0x7F) {
                mac[i] = (byte)(Integer.valueOf(s, 16) - 0xFF - 1);
            }else {
                mac[i] = Byte.valueOf(s, 16);
            }
        }
        return mac;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //收藏 返回后刷新列表
        if(requestCode == DoorLockCollectActivity.REQUEST_CODE_TO_COLLECT){
            if(StringUtils.isNotEmpty(uuids)){
                requestFindLockList(uuids);
            }
            return;
        }

        if(requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK){
            delayedStopSearch();
        }else{
            showBlueFail(true);
            mMinewBeaconManager.stopScan();
            mMinewBeaconManager.stopService();
        }
    }


}
