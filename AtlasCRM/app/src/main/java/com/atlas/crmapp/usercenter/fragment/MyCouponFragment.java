package com.atlas.crmapp.usercenter.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.MyCouponFragmentNewAdapter;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.commonactivity.CouponDetailActivity;
import com.atlas.crmapp.db.hepler.PushMsgHepler;
import com.atlas.crmapp.eventbus.Event;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.UseableCouponsModel;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.push.ReadPushMsg;
import com.atlas.crmapp.usercenter.CouponCenterActivity;
import com.atlas.crmapp.util.GetCommonObjectUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by A.Developer on 2017/3/18.
 */

@SuppressLint("ValidFragment")
public class MyCouponFragment extends BaseFragment {

    @BindView(R.id.my_coupon_recyclerView)
    RecyclerView recyclerView;

    private MyCouponFragmentNewAdapter adapter;

    private List<UseableCouponsModel> models = new ArrayList<>();
    private int index;

    @BindView(R.id.ll_main)
    RelativeLayout vMain;

    public static MyCouponFragment newInstance(int index) {
        MyCouponFragment fragment = new MyCouponFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("models", (Serializable) models);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusFactory.getBus().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusFactory.getBus().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshCouponList(Event.EventObject orderComplete){
        if(Constants.EventType.ORDER_COMPLETE.equals(orderComplete.type)){
            prepareFragmentData();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_coupon;
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {
        adapter = new MyCouponFragmentNewAdapter(getHoldingActivity(), models, index);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        if(index == 0){
            View footView = LayoutInflater.from(getHoldingActivity()).inflate(R.layout.layout_coupon_footer, recyclerView, false);

            footView.findViewById(R.id.coupon_look).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getHoldingActivity(), CouponCenterActivity.class);
                    getActivity().startActivityForResult(intent, 1000);
                }
            });
            adapter.addFooterView(footView);

        }else{
            adapter.addFooterView(GetCommonObjectUtils.getRvBgDivideItem(context , recyclerView));
        }
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                UseableCouponsModel item = models.get(position);
                   /* long id = item.bind.id;
                    String name = item.coupon.coupon.name;
                    String valid = FormatCouponInfo.formatVaildDate(item.bind.validStart, item.bind.validEnd);
                    String bizCode = item.coupon.coupon.targetBizCode;
                    CouponDetailDialog.newInstance(id, name, valid, bizCode).show(MyCouponFragment.this.getFragmentManager(), "MyCouponFragment");*/
                CouponDetailActivity.newInstance(getActivity(), item ,index);
            }
        });
        recyclerView.setAdapter(adapter);
        prepareFragmentData();
    }

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {
        if(savedInstanceState!= null){
            models = (ArrayList<UseableCouponsModel>) savedInstanceState.get("models");
        }
        if (getArguments() != null) {
            index = getArguments().getInt("index");
        } else {
            index = 0;
        }
    }



    @Override
    protected void prepareFragmentData() {
        super.prepareFragmentData();
        BizDataRequest.requestMyCoupon(getHoldingActivity(), index, statusLayout, new BizDataRequest.OnMyCouponsRequestResult() {
            @Override
            public void onSuccess(List<UseableCouponsModel> useableCouponsList) {
                models.clear();
                models.addAll(useableCouponsList);

                if(models.size() == 0 && index != 0){
                    adapter.setEmptyView(R.layout.view_product_null, recyclerView);
                }
                adapter.notifyDataSetChanged();
                if(index == 0){
                    setPushCouponUnread();
                }
            }

            @Override
            public void onError(DcnException error) {
                models = new ArrayList<UseableCouponsModel>();
            }
        });
    }


    //标记推送的优惠券为已经读取
    private void setPushCouponUnread(){
        PushMsgHepler.updateUnReadMsgTypeToRead(Constants.PushMsgTpye.COUPON_BIND_MSG);
        EventBusFactory.getBus().post(new ReadPushMsg(Constants.PushMsgTpye.COUPON_BIND_MSG, 0, false));
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareFragmentData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000)
        {
            if (resultCode == RESULT_OK) {
                prepareFragmentData();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
