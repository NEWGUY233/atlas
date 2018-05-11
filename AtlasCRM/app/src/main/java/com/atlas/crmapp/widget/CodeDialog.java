package com.atlas.crmapp.widget;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.commonactivity.BlueToothAcitvity;
import com.atlas.crmapp.eventbus.Event;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.model.LockJson;
import com.atlas.crmapp.model.ResponseMyCodeJson;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.MTAUtils;
import com.atlas.crmapp.util.QRCodeUtil;
import com.atlas.crmapp.util.ScreenUtils;
import com.atlas.crmapp.view.KProgressHUDView;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.atlas.crmapp.util.ScreenUtils.setScreenBrightness;

/**
 * Created by Alex on 2017/3/29.
 *
 * http://blog.csdn.net/johnWcheung/article/details/56015515
 */

public class CodeDialog extends DialogFragment {
    private Unbinder unbinder;
    private long timestamp;
    private View mainView;
    private int curWindowLight;

    @BindView(R.id.tv_company)
    TextView tvCompany;

    @BindView(R.id.tv_name)
    TextView tvName;


    @BindView(R.id.iv_code)
    ImageView ivCode;

    @BindView(R.id.iv_header)
    ImageView ivHeader;

    @BindView(R.id.bg_layout)
    BackgroundLayout backgroundLayout;

    @BindView(R.id.rl_loading)
    RelativeLayout rlLoading;

    @BindView(R.id.v_loading)
    KProgressHUDView vLoading;
    @BindView(R.id.tv_open_room)
    TextView tvOpenRoom;

    @OnClick(R.id.tv_open_room)
    void onClickOpenRoom(){
       /* BlueToothDialog blueToothDialog = BlueToothDialog.newInstance();
        blueToothDialog.show(getFragmentManager(), "BlueToothDialog");
        this.dismiss();*/
        getActivity().startActivity(new Intent(getActivity(), BlueToothAcitvity.class));
        MTAUtils.trackCustomEvent(getActivity(), "main_code_open_door" );
        this.dismiss();
    }

    public static CodeDialog newInstance() {
        return new CodeDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBusFactory.getBus().register(this);
        mainView = inflater.inflate(R.layout.view_dialog_code, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(true);
        unbinder = ButterKnife.bind(this, mainView);
        initView();
        return mainView;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.s88));
        MTAUtils.trackCustomEvent(getActivity(), "main_code" );
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.s88));
    }



    private void initView() {
        //curWindowLight = getScreenBrightness(getActivity());
        requestMyCode();
        requestFindUUidList();
        setScreenBrightness(getActivity(),255);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        ScreenUtils.setWindowBrightness(getActivity(), WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE);
    }

    private void requestMyCode(){
        vLoading.updateViews(true);
       BizDataRequest.requestMyCode(getActivity(), new BizDataRequest.OnResponseMyCodeJson() {
           @Override
           public void onSuccess(ResponseMyCodeJson responseMyInfoJson) {
                   timestamp = responseMyInfoJson.timestamp;
                   if (responseMyInfoJson.errorCode == 0) {
                   Bitmap coderBitmap = QRCodeUtil.encodeAsBitmap(responseMyInfoJson.data, 500, 500 ,1);
                   if(ivCode != null){
                       vLoading.updateViews(false);
                       ivCode.setImageBitmap(coderBitmap);
                   }
                   Event.CheckConfirmOrder checkConfirmOrder = new Event.CheckConfirmOrder();
                   checkConfirmOrder.timestamp = responseMyInfoJson.timestamp;
                   checkConfirmOrder.isStopCheckThread = false;
                   EventBusFactory.getBus().post(checkConfirmOrder);
               }
           }

           @Override
           public void onError(DcnException error) {
           }
       });
    }


    @Subscribe
    public void onStartOrderFinishThis(ResponseOpenOrderJson openOrderJson){
        if(openOrderJson!= null){
            this.dismiss();
        }
    }

    private void requestFindUUidList(){
        BizDataRequest.requestFindUUidList(getActivity(), Constants.DOOR_TYPE.ALL,  new BizDataRequest.OnResponseFindUUidList() {
            @Override
            public void onSuccess(List<LockJson> lockJsons) {
                if(lockJsons != null && tvOpenRoom != null){
                    if(lockJsons.size() > 0){
                        tvOpenRoom.setVisibility(View.VISIBLE);
                    }else{
                        tvOpenRoom.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onError(DcnException error) {
                if(tvOpenRoom != null)
                tvOpenRoom.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Event.CheckConfirmOrder checkConfirmOrder = new Event.CheckConfirmOrder();//关闭页面后继续请求一分钟
        checkConfirmOrder.isStopCheckThread = true;
        EventBusFactory.getBus().post(checkConfirmOrder);
        EventBusFactory.getBus().unregister(this);
    }



}