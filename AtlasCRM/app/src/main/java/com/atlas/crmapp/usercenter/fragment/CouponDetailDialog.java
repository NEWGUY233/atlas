package com.atlas.crmapp.usercenter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.eventbus.Event;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.model.ResponseMyCodeJson;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.QRCodeUtil;
import com.atlas.crmapp.util.StringUtils;
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by hoda on 2017/8/5.
 */

public class CouponDetailDialog extends DialogFragment {
    private Unbinder unbinder;
    @BindView(R.id.tv_coupon_detail_title)
    TextView tvTitle;

    @BindView(R.id.tv_coupon_detail_subtitle)
    TextView tvSubtitle;


    @BindView(R.id.tv_coupon_detail_date)
    TextView tvDate;


    @BindView(R.id.img_qrcode)
    ImageView mIvCode;

    @BindView(R.id.bg_coupon_detail)
    ImageView imgBg;

    private static final String INTENT_KEY_NAME = "name";
    private static final String INTENT_KEY_VALID = "valid";
    private static final String INTENT_KEY_BIZ_CODE  = "bizCode";
    private static final String INTENT_KEY_ID = "id";

    private String tag = "CouponDetailDialog";


    @OnClick(R.id.btn_coupon_detail_cancel)
    void onClickBack(){
        dismiss();
    }
    private long id;
    private String name;
    private String valid;
    private String bizCode;

                 /*   intent.putExtra("id",item.bind.id);
                intent.putExtra("name",item.coupon.coupon.name);
                intent.putExtra("valid", FormatCouponInfo.formatVaildDate(item.bind.validStart, item.bind.validEnd));
                intent.putExtra("bizCode",item.coupon.coupon.targetBizCode);*/

    public static CouponDetailDialog newInstance(long id , String name , String valid, String bizCode){
        CouponDetailDialog detailDialog = new CouponDetailDialog();
        Bundle bundle = new Bundle();
        bundle.putLong(INTENT_KEY_ID, id);
        bundle.putString(INTENT_KEY_NAME, name);
        bundle.putString(INTENT_KEY_VALID, valid);
        bundle.putString(INTENT_KEY_BIZ_CODE, bizCode);
        detailDialog.setArguments(bundle);
        return  detailDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id =  getArguments().getLong(INTENT_KEY_ID, 0);
        name = getArguments().getString(INTENT_KEY_NAME);
        valid = getArguments().getString(INTENT_KEY_VALID);
        bizCode = getArguments().getString(INTENT_KEY_BIZ_CODE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        View view = inflater.inflate(R.layout.activity_coupon_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBusFactory.getBus().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = "";
        if(StringUtils.isEmpty(bizCode)){
            title =getString(R.string.t45);
        }else{
            title = GlobalParams.getInstance().getBizName(bizCode);
        }
        tvTitle.setText(title);
        tvSubtitle.setText(name);
        tvDate.setText(getString(R.string.t46)+valid);
        //imgBg.setImageResource(bgRecByBizCode(bizCode));
        initData();
    }

    //当跳转至 订单页面时关闭优惠券详细页面
    @Subscribe
    public void onStartOrderFinishThis(ResponseOpenOrderJson openOrderJson){
        if(openOrderJson!= null){
            this.dismiss();
        }
    }

    private void initData(){

        BizDataRequest.requestCouponcode(getActivity(), tag, id, new BizDataRequest.OnResponseMyCodeJson() {
            @Override
            public void onSuccess(ResponseMyCodeJson responseMyCodeJson) {
                if (responseMyCodeJson.errorCode == 0) {
                    if(mIvCode != null){
                        mIvCode.setImageBitmap(QRCodeUtil.encodeAsBitmap(getActivity(), responseMyCodeJson.data));
                    }
                    Event.CheckConfirmOrder checkConfirmOrder = new Event.CheckConfirmOrder();
                    checkConfirmOrder.timestamp = responseMyCodeJson.timestamp;
                    checkConfirmOrder.isStopCheckThread = false;
                    EventBusFactory.getBus().post(checkConfirmOrder);
                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        Event.CheckConfirmOrder checkConfirmOrder = new Event.CheckConfirmOrder();//关闭页面后继续请求一分钟
        checkConfirmOrder.isStopCheckThread = true;
        EventBusFactory.getBus().post(checkConfirmOrder);
        EventBusFactory.getBus().unregister(this);
        OkGo.getInstance().cancelTag(tag);
    }

    /*private int bgRecByBizCode(String bizCode) {
        GlobalParams gp = GlobalParams.getInstance();
        Map map = new HashMap();
        map.put(gp.getCoffeeCode(),R.drawable.bg_coupon_detail_orange);
        map.put(gp.getStudioCode(),R.drawable.bg_coupon_detail_darkgreen);
        map.put(gp.getFitnessCode(),R.drawable.bg_coupon_detail_darkgreen);
        map.put(gp.getGogreenCode(),R.drawable.bg_coupon_detail_green);
        map.put(gp.getKitchenCode(),R.drawable.bg_coupon_detail_yellow);
        map.put(gp.getWorkplaceCode(),R.drawable.bg_coupon_detail_blue);

        Object obj =  map.get(bizCode);
        if (obj != null) {
            return (int)obj;
        }
        return R.drawable.bg_coupon_detail_yellow;
    }*/
}
