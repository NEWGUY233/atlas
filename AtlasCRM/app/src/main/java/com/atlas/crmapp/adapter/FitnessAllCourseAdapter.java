package com.atlas.crmapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.commonadapter.BaseRvAdapter;
import com.atlas.crmapp.common.commonadapter.BaseRvViewHolder;
import com.atlas.crmapp.fitness.CourseDetailActivity;
import com.atlas.crmapp.model.LessonJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.GlideUtils;

import java.util.List;

import butterknife.BindView;


/**
 * Created by Alex on 2017/4/21.
 */

public class FitnessAllCourseAdapter extends BaseRvAdapter<BaseRvViewHolder> {

    private Context context;
    private List<LessonJson> data;
    private FitnessAllCourseAdapter.ViewHolder viewHolder;

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_fitness_all_course;
    }

    public FitnessAllCourseAdapter(Context context, List<LessonJson> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    protected BaseRvViewHolder getViewHolder(View root, int viewType) {
        viewHolder = new FitnessAllCourseAdapter.ViewHolder(root);
        return new BaseRvViewHolder(root);
    }

    @Override
    public void onBindViewHolder(BaseRvViewHolder holder, int position) {
        final LessonJson lesson = data.get(position);
        GlideUtils.loadCustomImageView(context, R.drawable.ic_product_thum,LoadImageUtils.loadSmallImage(lesson.thumbnail),viewHolder.mIvThumbnail );
        viewHolder.mTvName.setText(lesson.name);
        viewHolder.mTvMajor.setText(lesson.major);
        viewHolder.mTvStartTime.setText(DateUtil.times(lesson.startTime, "yyyy.MM.dd HH:mm"));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CourseDetailActivity.class);
                intent.putExtra("id", lesson.id);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends BaseRvViewHolder {

        @BindView(R.id.iv_thumbnail)
        ImageView mIvThumbnail;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_major)
        TextView mTvMajor;
        @BindView(R.id.tv_startTime)
        TextView mTvStartTime;

        public ViewHolder(View itemView) {  super(itemView); }
    }
}
