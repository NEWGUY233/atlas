package com.atlas.crmapp.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.UseableCouponsModel;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.GlideUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Date;
import java.util.List;

/**
 * Created by Harry on 2017/5/26.
 */

public class CorporationCouponFragmentAdapter extends BaseQuickAdapter<UseableCouponsModel, BaseViewHolder> {

    private Context mContext;

    public CorporationCouponFragmentAdapter(Context context, List<UseableCouponsModel> data) {
        super(R.layout.item_corporation_coupon, data);
        mContext = context;
    }


    @Override
    protected void convert(final BaseViewHolder helper, final UseableCouponsModel item) {
        FormatCouponInfo.formatCouponInfo(item.coupon.coupon.type, item.coupon.coupon.value1,
                item.coupon.coupon.value2, new FormatCouponInfo.OnFormatCouponInfoDone() {
            @Override
            public void onFormatCouponInfoDone(String price, String remark) {
                helper.setText(R.id.my_coupon_price, price);
                helper.setText(R.id.my_coupon_remark, remark);

            }
        });
        if ((new Date()).getTime()+(7*24*60*60*1000) > item.bind.validEnd) {
            helper.setVisible(R.id.iv_past_due, true);
        } else {
            helper.setVisible(R.id.iv_past_due, false);
        }
        ImageView picture = helper.getView(R.id.coupon_pictrue);
        GlideUtils.loadCustomImageView(mContext, R.drawable.product_thum, LoadImageUtils.loadSmallImage(item.coupon.coupon.thumbnail), picture);
        helper.setText(R.id.coupon_title, item.coupon.coupon.name)
                .setText(R.id.valid_time, FormatCouponInfo.formatVaildDate(item.bind.validStart, item.bind.validEnd));
        helper.addOnClickListener(R.id.coupon_qr_code);
        helper.addOnClickListener(R.id.iv_company_qrcode);
        helper.addOnClickListener(R.id.couponView);
       /* this.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(final BaseQuickAdapter adapter, View view, int position) {*/
               /* final Intent intent = new Intent(mContext,CouponDetailActivity.class);
                intent.putExtra("id",item.bind.id);
                intent.putExtra("name",item.coupon.coupon.name);
                intent.putExtra("valid", FormatCouponInfo.formatVaildDate(item.bind.validStart, item.bind.validEnd));
                intent.putExtra("bizCode",item.coupon.coupon.targetBizCode);
                FormatCouponInfo.formatCouponInfo(item.coupon.coupon.type, item.coupon.coupon.value1,
                        item.coupon.coupon.value2, new FormatCouponInfo.OnFormatCouponInfoDone() {
                            @Override
                            public void onFormatCouponInfoDone(String price, String remark) {
                                intent.putExtra("desc",remark);

                            }
                        });
                    mContext.startActivity(intent);*/




       /*     }
        });*/

    }



}
