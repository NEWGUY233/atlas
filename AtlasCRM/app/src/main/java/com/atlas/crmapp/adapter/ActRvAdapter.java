package com.atlas.crmapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.commonadapter.BaseRvAdapter;
import com.atlas.crmapp.common.commonadapter.BaseRvViewHolder;
import com.atlas.crmapp.ecosphere.ActivitiesDetailActivity;
import com.atlas.crmapp.model.ActivityJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.DateUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/20
 *         Description :
 */

public class ActRvAdapter extends BaseRvAdapter<BaseRvViewHolder> {
    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_activities;
    }

    private Context context;
    private ArrayList<ActivityJson> data;
    private ViewHolder viewHolder;

    public ActRvAdapter(Context context, ArrayList<ActivityJson> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    protected BaseRvViewHolder getViewHolder(View root, int viewType) {
        viewHolder = new ViewHolder(root);
        return new BaseRvViewHolder(root);
    }

    @Override
    public void onBindViewHolder(BaseRvViewHolder holder, int position) {
        final ActivityJson activityJson =  data.get(position);
        if(activityJson.medias != null && activityJson.medias.size() > 0) {
            Glide.with(context).load(LoadImageUtils.loadMiddleImage(activityJson.medias.get(0).url)).apply(new RequestOptions().skipMemoryCache(true).centerCrop()).into(viewHolder.mIvShape);
        }
        viewHolder.mTvTitle.setText(activityJson.name);
        viewHolder.mTvContent.setText(activityJson.description);
        viewHolder.mTvLocation.setText(activityJson.city);
        viewHolder.mTvDate.setText(DateUtil.times(activityJson.startTime) + " - " + DateUtil.times(activityJson.endTime));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivitiesDetailActivity.class);
                intent.putExtra("id", activityJson.id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends BaseRvViewHolder {

        @BindView(R.id.iv_shape)
        tangxiaolv.com.library.EffectiveShapeView mIvShape;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_content)
        TextView mTvContent;
        @BindView(R.id.tv_location)
        TextView mTvLocation;
        @BindView(R.id.tv_date)
        TextView mTvDate;

        public ViewHolder(View itemView) {  super(itemView); }
    }
}
