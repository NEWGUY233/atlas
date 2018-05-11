package com.atlas.crmapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.ActionUriUtils;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.util.StringUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by Alex on 2017/4/16.
 */

public class RecommendAdapter extends BaseAdapter {
    private List<ResourceJson.ResourceMedia> actRecommendList;
    private Context mContext;
    public RecommendAdapter(Context context, List<ResourceJson.ResourceMedia> itemSelectMembers) {
        this.actRecommendList = itemSelectMembers;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return actRecommendList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_recomm_act_coffee_fragment, container, false);
            ImageView actImageView = (ImageView) convertView.findViewById(R.id.actImageView);

            final ResourceJson.ResourceMedia media = actRecommendList.get(position);
            GlideUtils.loadCustomImageView(mContext, R.drawable.product, LoadImageUtils.loadMiddleImage(media.url), actImageView);
            if(StringUtils.isNotEmpty(media.actionUri)) {
                actImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = ActionUriUtils.getIntent(mContext, media, Constants.LoadImageType.MJ);
                        if(intent!= null){
                            mContext.startActivity(intent);
                        }
                    }
                });
            }
        }
        return convertView;
    }

}