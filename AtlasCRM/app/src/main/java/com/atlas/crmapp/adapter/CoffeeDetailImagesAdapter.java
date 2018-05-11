package com.atlas.crmapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.SKUJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.GlideUtils;
import com.shizhefei.view.indicator.IndicatorViewPager;

import java.util.List;

/**
 * Created by Alex on 2017/4/19.
 */

public class CoffeeDetailImagesAdapter extends IndicatorViewPager.IndicatorViewPagerAdapter {

    private Context mContext;
    private List<SKUJson.Media> actRecommendList;

    public CoffeeDetailImagesAdapter(Context context, List<SKUJson.Media> actRecommendList) {
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
            final SKUJson.Media media = actRecommendList.get(position);
            GlideUtils.loadCustomImageView(mContext, R.drawable.product_thum, LoadImageUtils.loadMiddleImage(media.url), actImageView);
        }
        return convertView;
    }
}