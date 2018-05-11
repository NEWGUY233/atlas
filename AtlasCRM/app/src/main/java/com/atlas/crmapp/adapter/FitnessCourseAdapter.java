package com.atlas.crmapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.commonadapter.BaseRvViewHolder;
import com.atlas.crmapp.model.DateModl;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Jason on 2017/4/21.
 */

public class FitnessCourseAdapter extends RecyclerView.Adapter<FitnessCourseAdapter.ViewHolder> {


    private int selectedPos = 0;
    ArrayList<DateModl> al_date;
    Context c;
    public FitnessCourseAdapter(Context c, ArrayList<DateModl> al_date) {
        this.c = c;
        this.al_date = al_date;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(c).inflate(R.layout.item_schedule_meetingroom_fragment, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DateModl map=   al_date.get(position);
        holder.tvDay.setText(map.getDayydate());
        holder.tvWeekDay.setText(map.getWeek());
        holder.tvDay.setSelected(position == selectedPos);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return al_date.size();
    }


    public class ViewHolder extends BaseRvViewHolder {

        @BindView(R.id.tv_day)
        TextView tvDay;

        @BindView(R.id.tv_week_day)
        TextView tvWeekDay;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}