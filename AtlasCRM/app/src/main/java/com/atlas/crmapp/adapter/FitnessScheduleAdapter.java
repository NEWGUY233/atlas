package com.atlas.crmapp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.fitness.AppointmentClassFragment;
import com.atlas.crmapp.model.DateModl;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Alex on 2017/4/27.
 */

public class FitnessScheduleAdapter extends BaseQuickAdapter<DateModl, BaseViewHolder> {
    private Context context;
    private List<DateModl> data;
    private int selectedPos = 0;

    public FitnessScheduleAdapter(Context context, List<DateModl> data) {
        super(R.layout.item_schedule_meetingroom_fragment, data);
        this.context = context;
        this.data = data;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final DateModl item) {
        helper.setText(R.id.tv_day, item.getDayydate())
                .setText(R.id.tv_week_day,item.getWeek());
        TextView tvDay =helper.getView(R.id.tv_day);
        tvDay.setSelected(helper.getAdapterPosition() == selectedPos);
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedPos == helper.getAdapterPosition()) {
                    return;
                }
                int oldSelect = selectedPos;
                selectedPos = helper.getAdapterPosition();
                notifyItemChanged(oldSelect);
                notifyItemChanged(selectedPos);
                AppointmentClassFragment.fragment.prepareActivityData(item.getYeardate());
            }
        });
    }

}