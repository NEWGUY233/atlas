package com.atlas.crmapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.atlas.crmapp.model.ClassJson;
import com.atlas.crmapp.view.CourseDetailOrderItem;

import java.util.ArrayList;

/**
 * Created by hoda_fang on 2017/5/31.
 */

public class FitnessCourseDetailAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ClassJson> data;
    private View.OnClickListener onClickBooking;
    private boolean isFitContractOrAllowance;

    public FitnessCourseDetailAdapter(Context context, ArrayList<ClassJson> data, View.OnClickListener onClickBooking ,boolean isFitContractOrAllowance){
        this.context = context;
        this.data = data;
        this.onClickBooking = onClickBooking;
        this.isFitContractOrAllowance = isFitContractOrAllowance;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CourseDetailOrderItem itemView = null;
        if(convertView == null){
            convertView = new CourseDetailOrderItem(parent.getContext());
        }
        itemView = (CourseDetailOrderItem) convertView;
        if(data.size()> position){
            itemView.updateViews(data.get(position), onClickBooking ,isFitContractOrAllowance, true);
        }
        return itemView;
    }
}