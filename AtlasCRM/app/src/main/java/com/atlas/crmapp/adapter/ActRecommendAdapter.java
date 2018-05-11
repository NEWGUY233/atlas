package com.atlas.crmapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.bumptech.glide.Glide;
import com.shizhefei.view.indicator.IndicatorViewPager;

import java.util.List;

/**
 * Created by macbook on 2017/3/21.
 */

public class ActRecommendAdapter extends IndicatorViewPager.IndicatorViewPagerAdapter {

    private Context mContext;
    private List<ResourceJson.ResourceMedia> actRecommendList;

    public ActRecommendAdapter(Context context, List<ResourceJson.ResourceMedia> actRecommendList) {
        this.mContext = context;
        this.actRecommendList = actRecommendList;
    }

    @Override
    public int getCount() {
        return actRecommendList.size();
    }

    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.tab_guide, container, false);
        }
        return convertView;
    }

    @Override
    public View getViewForPage(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_recomm_act_coffee_fragment, container, false);
            ImageView actImageView = (ImageView) convertView.findViewById(R.id.actImageView);

            final ResourceJson.ResourceMedia media = actRecommendList.get(position);
            Glide.with(mContext).load(LoadImageUtils.loadSmallImage(media.url)).into(actImageView);
        }
        return convertView;
    }
}
