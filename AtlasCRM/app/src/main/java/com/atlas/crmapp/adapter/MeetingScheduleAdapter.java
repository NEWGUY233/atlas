package com.atlas.crmapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.commonadapter.BaseRvAdapter;
import com.atlas.crmapp.common.commonadapter.BaseRvViewHolder;
import com.atlas.crmapp.model.DateModl;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/15
 *         Description :
 */

public class MeetingScheduleAdapter extends BaseRvAdapter<MeetingScheduleAdapter.ViewHolder> {


    private int selectedPos = 0;
    ArrayList<DateModl> al_date;
    Context c;
    RecyclerView recyclerView;
    private OnItemClickListener itemClickListener;

    public MeetingScheduleAdapter(Context c, RecyclerView recyclerView, ArrayList<DateModl> al_date) {
        this.c = c;
        this.al_date = al_date;
        this.recyclerView = recyclerView;
    }

    //高亮对应日期的item（传入的日期格式: 2017-06-13）
    public void highlightDate(String dateLiteral) {
        int dateIndex = 0;

        for (int i =0; i < al_date.size(); i++) {
            DateModl dateModel = al_date.get(i);

            if( dateLiteral.contains(dateModel.getYeardate()) ) {
                dateIndex = i;
                break;
            }
        }

        highlightPosition(dateIndex);
    }

    private void highlightPosition(int position) {
        selectedPos = position;
        notifyDataSetChanged();
        scrollToCenter(selectedPos);
    }

    private void scrollToCenter(int position) {
        LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
        int visibleCount = lastVisiblePosition - firstVisiblePosition;
        //scrollToPosition只会让目标item显示在屏幕边缘，为了让它显示在中央，我们加上了偏移量
        int centerOffset = visibleCount / 2;
        boolean targetPositionOnLeft = Math.abs(firstVisiblePosition - position) < Math.abs(lastVisiblePosition - position);
        int expectPosition = 0;
        if(targetPositionOnLeft) {
            expectPosition = Math.max((position - centerOffset),0);
        } else {
            expectPosition = Math.min((position + centerOffset),getItemCount()-1);
        }
        recyclerView.scrollToPosition(expectPosition);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_schedule_meetingroom_fragment;
    }

    @Override
    protected ViewHolder getViewHolder(View root, int viewType) {
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DateModl map=   al_date.get(position);
        //holder.tvYm.setText(dateFormmat(map.getYeardate()));
        holder.tvDay.setText(map.getDayydate());
        holder.tvWeekDay.setText(weekFormmat(map.getWeek()));
        holder.vBottomTab.setSelected(position == selectedPos);
        if(position == selectedPos){
            holder.tvWeekDay.setTextColor(ContextCompat.getColor(c, R.color.text_dark));
            holder.tvDay.setTextColor(ContextCompat.getColor(c, R.color.text_dark));
        }else {
            holder.tvWeekDay.setTextColor(ContextCompat.getColor(c, R.color.gray_simple));
            holder.tvDay.setTextColor(ContextCompat.getColor(c, R.color.gray_simple));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedPos == holder.getAdapterPosition()) {
                    return;
                }
                int oldSelect = selectedPos;
                selectedPos = holder.getAdapterPosition();
                scrollToCenter(selectedPos);
                notifyItemChanged(oldSelect);
                notifyItemChanged(selectedPos);
                itemClickListener.onItemClick(map.getYeardate() , position,dateFormmat(map.getYeardate()));

            }
        });
    }

    //改变格式：将"2017-05-31"变成"2017/05"
    private String dateFormmat(String dateStr) {
        String result = dateStr;
        String[] spliteStr = dateStr.split("-");
        if (spliteStr.length > 1) {
            result = spliteStr[0] + "/" + spliteStr[1];
        }
        return result;
    }

    //改变格式：将"星期三"变成"周三"
    private String weekFormmat(String weakStr) {
        return weakStr.replace(c.getString(R.string.text_79),c.getString(R.string.text_80));
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

        @BindView(R.id.v_bottom_tab)
        View vBottomTab;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    //点击事件接口
    public interface OnItemClickListener{
        void onItemClick(String date, int position, String formatDate);
    }
    //设置点击事件的方法
    public void setItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
