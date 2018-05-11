package com.atlas.crmapp.widget;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.adapter.FindDialogRvAdapter;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.db.hepler.PushMsgHepler;
import com.atlas.crmapp.db.model.PushMsg;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.model.CouponModel;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.push.ReadPushMsg;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/20
 *         Description :
 */

public class FindDialog extends DialogFragment {

    private Unbinder unbinder;
    private FindDialogRvAdapter adapter;
    private long timeInterval = 1000 * 60 * 60 *24 * 7;
    @BindView(R.id.rv_dialog_find)
    RecyclerView mRvDialogFind;

    private boolean isLogin;

    public static FindDialog newInstance(Activity activity) {
        FindDialog findDialog = new FindDialog();
        return findDialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.s89));
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.s89));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_dialog_find, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        PushMsgHepler.updateUnReadMsgToRead();
        EventBusFactory.getBus().post(new ReadPushMsg(Constants.PushMsgTpye.COUPON_BIND_MSG, 0, false));
        EventBusFactory.getBus().post(new ReadPushMsg(Constants.PushMsgTpye.NORMAl, 0, false));
        isLogin = GlobalParams.getInstance().isLogin();
        if (isLogin) {
            BizDataRequest.requestCouponsCenterWithToken(getActivity(), true, null, new BizDataRequest.OnCouponsCenterRequestResult() {
                @Override
                public void onSuccess(final List<CouponModel> couponModelsList) {
                    updateView(couponModelsList);
                }

                @Override
                public void onError(DcnException error) {

                }
            });
        } else {
            BizDataRequest.requestCouponsCenter(getActivity(), new BizDataRequest.OnCouponsCenterRequestResult() {
                @Override
                public void onSuccess(List<CouponModel> couponModelsList) {
                    updateView(couponModelsList);
                }

                @Override
                public void onError(DcnException error) {
                }
            });
        }
    }

    private void updateView(List<CouponModel> couponModelsList){
        if (mRvDialogFind != null) {
            PushMsgHepler.removeAllCoupon();
            PushMsgHepler.removeOldMsg(timeInterval);
            List<PushMsg> msgModels = new ArrayList<>();
            if(isLogin){
                msgModels.addAll(PushMsgHepler.getRealMsg());
            }
            msgModels.addAll(getCouponMsg(couponModelsList));


            if (mRvDialogFind != null) {
                //mRvDialogFind.setHasFixedSize(true);
                mRvDialogFind.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter = new FindDialogRvAdapter(getActivity(), msgModels, this);
                mRvDialogFind.setAdapter(adapter);
                if (msgModels.size() == 0) {
                    adapter.setEmptyView(R.layout.view_product_null, mRvDialogFind);
                }
            }
            onClickRightIamgeToNotifyData(msgModels);
        }
    }


    private List<PushMsg> getCouponMsg(List<CouponModel> couponModelsList){
        final List<PushMsg> couponMsgs = new ArrayList<>();
        for (int i = 0; i < couponModelsList.size(); i++) {
            final CouponModel couponModel = couponModelsList.get(i);
            if (!couponModel.have) {
                FormatCouponInfo.formatCouponInfo(couponModel.type, couponModel.value1,
                        couponModel.value2, new FormatCouponInfo.OnFormatCouponInfoDone() {
                            @Override
                            public void onFormatCouponInfoDone(String price, String remark) {
                                String content = FormatCouponInfo.formatVaildDate(Long.valueOf(couponModel.startTime), Long.valueOf(couponModel.endTime));
                                PushMsg pushMsg = new PushMsg();
                                pushMsg.setCouponId(couponModel.id);//CouponId
                                pushMsg.setTitle(couponModel.name);
                                pushMsg.setContent(content);
                                pushMsg.setDate(Long.valueOf(couponModel.endTime));
                                pushMsg.setThumbnail(couponModel.thumbnail);
                                pushMsg.setPrice(price);
                                pushMsg.setRemark(remark);
                                pushMsg.setType(Constants.PushMsgTpye.COUPON_BIND_MSG);
                                couponMsgs.add(pushMsg);
                               /* PushMsgHepler.insertCoupon( couponModel.id,
                                        couponModel.name,
                                        formatVaildDate(Long.valueOf(couponModel.startTime), Long.valueOf(couponModel.endTime)),
                                        Long.valueOf(couponModel.endTime),
                                        couponModel.thumbnail,
                                        price,
                                        remark);*/
                            }
                        });
            }
        }
        return  couponMsgs;
    }


    //点击录取优惠券时刷新
    private void  onClickRightIamgeToNotifyData(final List<PushMsg> msgModels){
        if(adapter != null){
            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
                    if (GlobalParams.getInstance().isLogin()) {
                        BizDataRequest.requestTakeCoupon(getActivity(), msgModels.get(position).couponId, new BizDataRequest.OnRequestResult() {
                            @Override
                            public void onSuccess() {
                                msgModels.get(position).have = true;
                                adapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onError(DcnException error) {

                            }
                        });
                    } else {
                        ((BaseActivity)getActivity()).showAskLoginDialog();
                    }
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
