package com.atlas.crmapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.MeetingRoomJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.workplace.MeetingRoomAppointmentActivity;
import com.atlas.crmapp.workplace.view.MeetingScheduleView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;

import java.util.List;


public class MeetingBookingListAdapter extends BaseQuickAdapter<MeetingRoomJson, BaseViewHolder> {
    private String date;
    public MeetingBookingListAdapter(Context context, List<MeetingRoomJson> data, String date) {
        super(R.layout.item_detail_meetingroom_fragment, data);
        this.date = date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    protected void convert(BaseViewHolder helper, final MeetingRoomJson item) {
        helper.setText(R.id.tv_name, item.facility.name)
                .setText(R.id.tv_capacity, item.capacity+ "")
        .setText(R.id.tv_mprice, FormatCouponInfo.formatUnitTimePrice(item.facility.price));

        final MeetingScheduleView scheduleView = helper.getView(R.id.schedule_view_bar);
        Logger.d("adapter    name    " + item.facility.name);
		scheduleView.showScheduleView(item.occupyTimes, item.openTime, item.closeTime);
		Glide.with(mContext).load(LoadImageUtils.loadSmallImage(item.facility.thumbnail)).apply(new RequestOptions().placeholder(R.drawable.ic_meeting_pruduct).dontAnimate().centerCrop()).into((ImageView) helper.getView(R.id.card_img));
        helper.setText(R.id.tv_equipments, item.descript == null ? "" :item.descript);
        helper.getView(R.id.rl_item_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, MeetingRoomAppointmentActivity.class);
                intent.putExtra("id", item.id + "");
                intent.putExtra("date", date);
                ((Activity)mContext).startActivityForResult(intent, 999);
               // ((Activity)mContext).startActivityForResult(new Intent(mContext, MeetingDetailActivity.class).putExtra("id", item.id+"").putExtra("date", date), 999);
            }
        });

    }

}