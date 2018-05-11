package com.atlas.crmapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.fitness.CourseDetailActivity;
import com.atlas.crmapp.model.LessonJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.DateUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Alex on 2017/4/21.
 */

public class FitnessAppointmentAdapter extends BaseQuickAdapter<LessonJson, BaseViewHolder> {

    private List<LessonJson> data;
    private Context context;

    public FitnessAppointmentAdapter(Context context, List<LessonJson> data) {
        super(R.layout.item_fitness_appointment, data);
        this.context = context;
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, final LessonJson item) {
        helper.setText(R.id.tv_name, item.name)
                .setText(R.id.tv_major, item.major)
                .setText(R.id.tv_startTime ,context.getString(R.string.text_78) + DateUtil.times(item.startTime, "HH:mm") + "-" + DateUtil.times(item.endTime, "HH:mm"));
        Glide.with(context).load(LoadImageUtils.loadSmallImage(item.thumbnail)).apply(new RequestOptions().centerCrop()).into((ImageView) helper.getView(R.id.iv_thumbnail));
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CourseDetailActivity.class);
                intent.putExtra("id", item.id);
                context.startActivity(intent);
            }
        });
    }

}