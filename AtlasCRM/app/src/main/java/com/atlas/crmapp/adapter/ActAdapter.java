package com.atlas.crmapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.ecosphere.ActivitiesDetailActivity;
import com.atlas.crmapp.model.ActivityJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.DateUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Alex on 2017/5/17.
 */

public class ActAdapter extends BaseQuickAdapter<ActivityJson, BaseViewHolder> {

    private boolean displayTitleBar;
    private Context context;
    private List<ActivityJson> data;

    public ActAdapter(Context context, List<ActivityJson> data, boolean displayTitleBar) {
        super(R.layout.item_activities, data);
        this.context = context;
        this.displayTitleBar = displayTitleBar;
    }

    @Override
    protected void convert(BaseViewHolder helper, final ActivityJson item) {
        if(item.medias != null && item.medias.size() > 0) {
            Glide.with(context).load(LoadImageUtils.loadMiddleImage(item.medias.get(0).url)).apply(new RequestOptions().dontAnimate().placeholder(R.drawable.product).centerCrop()).into((ImageView) helper.getView(R.id.iv_shape));
        }
        String time = null;
        if(DateUtil.times(item.startTime).equals(DateUtil.times(item.endTime))){
            time = DateUtil.timesMin(item.startTime) + context.getString(R.string.text_71) + DateUtil.timeMinute(item.endTime);
        }else{
            time = DateUtil.times(item.startTime) + context.getString(R.string.text_71) + DateUtil.times(item.endTime);
        }
        helper.setText(R.id.tv_title, item.name)
                .setText(R.id.tv_content, item.description)
                .setText(R.id.tv_location , item.city)
                .setText(R.id.tv_date, time);
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActivitiesDetailActivity.class);
                intent.putExtra("id", item.id);
                context.startActivity(intent);
            }
        });

    }
}