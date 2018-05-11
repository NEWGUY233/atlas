package com.atlas.crmapp.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.model.VisitInviteRecordJson;
import com.atlas.crmapp.util.DateUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by hoda on 2017/12/22.
 */

public class VisitInviteRecordAdapter extends BaseQuickAdapter<VisitInviteRecordJson.RowsBean, BaseViewHolder> {
    private Context context;
    public VisitInviteRecordAdapter(Context context, List<VisitInviteRecordJson.RowsBean> data) {
        super(R.layout.item_visit_invite_record, data);
        this.context = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, VisitInviteRecordJson.RowsBean item) {
        helper.setText(R.id.tv_visit_date_colon, Html.fromHtml(context.getString(R.string.visit_date_colon, DateUtil.formatTime(item.getBookDate(), "yyyy-MM-dd"))))
                .setText(R.id.tv_visit_num_colon, Html.fromHtml(context.getString(R.string.visit_num_colon, item.getVisitorNum())))
                .setText(R.id.tv_visit_unit_colon, Html.fromHtml(context.getString(R.string.visit_unit_colon, item.getUnitName())))
                .setText(R.id.tv_visit_purpose_colon, item.getPurpose())
                .addOnClickListener(R.id.rl_bottom);
        TextView tvSendOrTime = helper.getView(R.id.tv_send_or_actual_time);
        if (Constants.VISIT_INVITE_RECORD.CANCEL.equals(item.getState())){
            helper.getView(R.id.rl_bottom).setVisibility(View.GONE);
        }else if(Constants.VISIT_INVITE_RECORD.NORMAL.equals(item.getState())){
            tvSendOrTime.setText(context.getString(R.string.send_invite));
            helper.getView(R.id.rl_bottom).setClickable(true);
            helper.getView(R.id.iv_arr).setVisibility(View.VISIBLE);
        }else if(Constants.VISIT_INVITE_RECORD.COMPLETE.equals(item.getState())){
            helper.getView(R.id.rl_bottom).setClickable(false);
            tvSendOrTime.setText(Html.fromHtml(context.getString(R.string.visit_actual_time, DateUtil.formatTime(item.getArrivedTime(), "yyyy-MM-dd mm:ss"))));
        }
    }
}
