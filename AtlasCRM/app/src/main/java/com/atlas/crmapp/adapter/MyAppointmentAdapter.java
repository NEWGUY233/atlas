package com.atlas.crmapp.adapter;

import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.ResponseMyAppointmentJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.GlideUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by A.Developer on 2017/3/21.
 */

public class MyAppointmentAdapter extends BaseQuickAdapter<ResponseMyAppointmentJson.MyAppointment, BaseViewHolder> {

    public MyAppointmentAdapter( List<ResponseMyAppointmentJson.MyAppointment> data) {
        super(R.layout.item_my_appointment, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ResponseMyAppointmentJson.MyAppointment item) {
        String itemTitle = FormatCouponInfo.getAppointmentTitle(item.getType());
        String timeRange ="";

        String startTime = DateUtil.timesStampToDateTime(item.getStartTime());
        String endTime = DateUtil.timesStampToDateTime(item.getEndTime());

        if (startTime.substring(0, 10).equals(endTime.substring(0, 10))){
            timeRange = startTime + " - " + DateUtil.formatTime(item.getEndTime(),"HH:mm");
        }else{
            timeRange = startTime + " - \n" + endTime;
        }

        //.setText(R.id.appointment_status, getStrStatus((TextView) helper.getView(R.id.appointment_status), item.getState()))
        helper.setText(R.id.appointment_title, item.getRefName())
                .setText(R.id.appointment_address, itemTitle)
                .setText(R.id.appointment_label, timeRange);

        helper.addOnClickListener(R.id.rl_main);
        ImageView picture = helper.getView(R.id.appointment_shapeview);
        GlideUtils.loadCustomImageView(mContext,R.drawable.product_thum,LoadImageUtils.loadSmallImage(item.getThumbnail()),picture);
    }

}
