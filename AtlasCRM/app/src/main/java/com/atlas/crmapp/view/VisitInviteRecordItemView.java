package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.model.VisitInviteRecordJson;
import com.atlas.crmapp.util.DateUtil;

import butterknife.BindView;

/**
 * Created by hoda on 2017/12/22.
 */

public class VisitInviteRecordItemView extends LinearLayout {

    @BindView(R.id.tv_visit_date_colon)
    TextView tvVisitDateColon;
    @BindView(R.id.tv_visit_num_colon)
    TextView tvVisitNumColon;
    @BindView(R.id.tv_visit_unit_colon)
    TextView tvVisitUnitColon;
    @BindView(R.id.tv_visit_purpose_colon)
    TextView tvVisitPurposeColon;
    @BindView(R.id.tv_send_or_actual_time)
    TextView tvSendOrActualTime;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    private Context context;

    public VisitInviteRecordItemView(Context context) {
        super(context);
        initViews(context);
    }

    public VisitInviteRecordItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public VisitInviteRecordItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VisitInviteRecordItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }


    private void initViews(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.item_visit_invite_record, this, true);

    }

    public void updateViews(VisitInviteRecordJson.RowsBean item){
        tvVisitDateColon.setText(Html.fromHtml(context.getString(R.string.visit_date_colon, DateUtil.formatTime(item.getBookDate(), "yyyy-MM-dd"))));
        tvVisitNumColon.setText(Html.fromHtml(context.getString(R.string.visit_num_colon, item.getVisitorNum())));
        tvVisitUnitColon.setText(Html.fromHtml(context.getString(R.string.visit_unit_colon, item.getUnitName())));
        tvVisitPurposeColon.setText(item.getPurpose());
        if (Constants.VISIT_INVITE_RECORD.CANCEL.equals(item.getState())){
            rlBottom.setVisibility(View.GONE);
        }else if(Constants.VISIT_INVITE_RECORD.NORMAL.equals(item.getState())){
            tvSendOrActualTime.setText(context.getString(R.string.send_invite));
            rlBottom.setClickable(true);
        }else if(Constants.VISIT_INVITE_RECORD.COMPLETE.equals(item.getState())){
            rlBottom.setClickable(false);
            tvSendOrActualTime.setText(Html.fromHtml(context.getString(R.string.visit_actual_time, DateUtil.formatTime(item.getArrivedTime(), "yyyy-MM-dd mm:ss"))));
        }
    }
}
