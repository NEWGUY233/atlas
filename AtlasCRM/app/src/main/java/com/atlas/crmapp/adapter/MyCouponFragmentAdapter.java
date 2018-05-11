package com.atlas.crmapp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.UseableCouponsModel;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by A.Developer on 2017/3/18.
 */

public class MyCouponFragmentAdapter extends BaseQuickAdapter<UseableCouponsModel, BaseViewHolder> {

    private Context mContext;
    private List<UseableCouponsModel> mData;
    private int fragmentIndex ;

    public MyCouponFragmentAdapter(Context context,List<UseableCouponsModel> data, int fragmentIndex) {
        super(R.layout.item_my_coupon, data);
        mData = data;
        mContext = context;
        this.fragmentIndex = fragmentIndex;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final UseableCouponsModel item) {
        String couponNum = "";
        if(fragmentIndex == 1){
            couponNum = item.bind.used + "";
        }else {
            couponNum = item.bind.quota + "";
        }
        helper.setText(R.id.tv_coupon_title, item.coupon.coupon.name)
                .setText(R.id.tv_coupon_count, "x" + couponNum)
                .addOnClickListener(R.id.img_coupon_bg);

        TextView tvSubTitle = (TextView) helper.itemView.findViewById(R.id.tv_coupon_subtitle);
        final TextView tvMoreDetail = (TextView) helper.itemView.findViewById(R.id.tv_coupon_moreDetail);
        Button btnExpand = (Button) helper.itemView.findViewById(R.id.btn_coupon_expand);
        ImageView imgBg = (ImageView) helper.itemView.findViewById(R.id.img_coupon_bg);
        ImageView imgIcon = (ImageView) helper.itemView.findViewById(R.id.img_coupon_middle_icon);
        View couponContent = helper.itemView.findViewById(R.id.coupon_content);
        couponContent.bringToFront();
        Logger.d("item.bind.validEnd---" + item.bind.validEnd +"   item.coupon.coupon.name " + item.coupon.coupon.name  + "item.coupon.coupon.tag "+ item.coupon.coupon.targetBizCode);

        String subTitle = null;
        if (fragmentIndex == 0){
            subTitle = mContext.getString(R.string.text_81) + DateUtil.getInFutureDayNum(item.bind.validEnd);
        }else if (fragmentIndex == 1){
            subTitle = mContext.getString(R.string.text_82);
        }else {
            subTitle = mContext.getString(R.string.text_83);
        }
        tvSubTitle.setText(subTitle);
        if(StringUtils.isNotEmpty(item.coupon.coupon.description)){
            tvMoreDetail.setText(item.coupon.coupon.description);
        }
/*        FormatCouponInfo.formatCouponInfo(item.coupon.coupon.type, item.coupon.coupon.value1,
                item.coupon.coupon.value2, new FormatCouponInfo.OnFormatCouponInfoDone() {
                    @Override
                    public void onFormatCouponInfoDone(String price, String remark) {
                        if(StringUtils.isNotEmpty(remark)){
                            remark = remark + "/n" + item.coupon.coupon.description;
                        }else{
                            remark = remark + item.coupon.coupon.description;
                        }
                    }
                });*/

        /*tvMoreDetail.setBackgroundColor(bgColorByBizCode(item.coupon.coupon.targetBizCode));
        int btnExpandVisibility = tvMoreDetail.getText().length() > 0 ? View.VISIBLE: View.INVISIBLE;
        btnExpand.setVisibility(btnExpandVisibility);
        btnExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.coupon.coupon.isShowMoreDetail = item.coupon.coupon.isShowMoreDetail == true ? false : true;
                if(item.coupon.coupon.isShowMoreDetail){
                    tvMoreDetail.setVisibility(View.VISIBLE);
                }else{
                    tvMoreDetail.setVisibility(View.GONE); //default is GONE;
                }
            }
        });
        if(item.coupon.coupon.isShowMoreDetail){
            tvMoreDetail.setVisibility(View.VISIBLE);
        }else{
            tvMoreDetail.setVisibility(View.GONE); //default is GONE;
        }
        imgBg.setImageResource(bgRecByBizCode(item.coupon.coupon.targetBizCode));
        imgIcon.setImageResource(iconRecByBizCode(item.coupon.coupon.targetBizCode));
        imgBg.setClickable(true);
*/
    }

 /*   private int bgRecByBizCode(String bizCode) {

        GlobalParams gp = GlobalParams.getInstance();
        Map map = new HashMap();
        map.put(gp.getCoffeeCode(),R.drawable.bg_coupon_orange);
        map.put(gp.getStudioCode(),R.drawable.bg_coupon_darkgreen);
        map.put(gp.getFitnessCode(),R.drawable.bg_coupon_darkgreen);
        map.put(gp.getGogreenCode(),R.drawable.bg_coupon_green);
        map.put(gp.getKitchenCode(),R.drawable.bg_coupon_yellow);
        map.put(gp.getWorkplaceCode(),R.drawable.bg_coupon_blue);

        Object obj =  map.get(bizCode);
        if (obj != null) {
            return (int)obj;
        }else{
            return  R.drawable.bg_coupon_yellow;
        }
    }

    private int iconRecByBizCode(String bizCode) {

        GlobalParams gp = GlobalParams.getInstance();
        Map map = new HashMap();
        map.put(gp.getCoffeeCode(),R.drawable.icon_coupon_bind_orange);
        map.put(gp.getStudioCode(),R.drawable.icon_coupon_bind_darkgreen);
        map.put(gp.getFitnessCode(),R.drawable.icon_coupon_bind_darkgreen);
        map.put(gp.getGogreenCode(),R.drawable.icon_coupon_bind_green);
        map.put(gp.getKitchenCode(),R.drawable.icon_coupon_bind_yellow);
        map.put(gp.getWorkplaceCode(),R.drawable.icon_coupon_bind_blue);

        Object obj =  map.get(bizCode);
        if (obj != null) {
            return (int)obj;
        }else{
            return R.drawable.icon_coupon_bind_yellow;
        }

    }


    private int bgColorByBizCode(String bizCode) {
        GlobalParams gp = GlobalParams.getInstance();
        Map map = new HashMap();
        map.put(gp.getCoffeeCode(), 0xFFf0deec);
        map.put(gp.getStudioCode(),0xFFf0deec);
        map.put(gp.getFitnessCode(),0xFFd5e3e2);
        map.put(gp.getGogreenCode(),0xFFd2e7d7);
        map.put(gp.getKitchenCode(),0xFFfef2d0);
        map.put(gp.getWorkplaceCode(),0xFFd4eef9);

        Object obj =  map.get(bizCode);
        if (obj != null) {
            return (int)obj;
        }else{

        }
        return 0xFFfef2d0;
    }*/

}
