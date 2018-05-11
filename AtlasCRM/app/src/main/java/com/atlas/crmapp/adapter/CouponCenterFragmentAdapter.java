package com.atlas.crmapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.eventbus.Event;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.model.CouponModel;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.widget.ArcProgress;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by A.Developer on 2017/3/18.
 */

public class CouponCenterFragmentAdapter extends BaseQuickAdapter<CouponModel, BaseViewHolder> {

    private class CouponItem {
        public int index;
        public long uid;
    }

    public CouponCenterFragmentAdapter(Context context, ArrayList<CouponModel> data) {
        super(R.layout.item_coupon_center, data);
        mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CouponModel item) {

        RelativeLayout rl = helper.getView(R.id.rl_coupon_progress);
        ArcProgress ap = helper.getView(R.id.coupon_center_arcprogress);
        FrameLayout layout = helper.getView(R.id.frameLayout);
        if (item.have) {
            rl.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        } else {
            rl.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
            Logger.d( "item.name"  +  item.name + "   item.take  " + item.take   + "    item.count  " + item.count);

            if(item.count > 0 ){
                BigDecimal  d =  new BigDecimal(item.take).divide(new BigDecimal(item.count), 4, BigDecimal.ROUND_UP);
                BigDecimal bg = new BigDecimal(d.doubleValue() * 100.0);
                double progress = bg.setScale(2, BigDecimal.ROUND_UP).doubleValue();
                ap.setProgress(progress);
            }else{
                ap.setProgress(100);
            }

        }
        FormatCouponInfo.formatCouponInfo(item.type, item.value1,
                item.value2, new FormatCouponInfo.OnFormatCouponInfoDone() {
                    @Override
                    public void onFormatCouponInfoDone(String price, String remark) {
                        helper.setText(R.id.coupon_center_price, price);
                        helper.setText(R.id.coupon_center_remark, remark +"（"+item.count+ ")张");

                    }
                });
        final ImageView picture = helper.getView(R.id.coupon_center_pictrue);
        Glide.with(mContext).load(LoadImageUtils.loadSmallImage(item.thumbnail)).apply(new RequestOptions().placeholder(R.drawable.ic_product_thum)).into(picture);
        helper.setText(R.id.coupon_center_title, item.name)
                .setText(R.id.valid_time,mContext.getString(R.string.text_74)+ FormatCouponInfo.formatVaildDate(item.startTime, item.endTime));
        Logger.d(item.validStart+"  " + item.validEnd + " "+  item.name);

        helper.addOnClickListener(R.id.take_coupon);
        this.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(final BaseQuickAdapter adapter, View view, int position) {
                final int index = position;
                BizDataRequest.requestTakeCoupon(mContext, mData.get(position).id, new BizDataRequest.OnRequestResult() {
                    @Override
                    public void onSuccess() {
                        new AlertDialog.Builder(mContext).setTitle(R.string.text_75).setMessage(mContext.getString(R.string.text_76))
                                .setPositiveButton(mContext.getString(R.string.done), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        mData.get(index).have = true;
                                        adapter.notifyDataSetChanged();
                                        EventBusFactory.getBus().post(new Event.EventObject(Constants.EventType.ORDER_COMPLETE, null));

                                    }
                                }).show();
                    }

                    @Override
                    public void onError(DcnException error) {

                    }
                });
            }
        });
    }
}
