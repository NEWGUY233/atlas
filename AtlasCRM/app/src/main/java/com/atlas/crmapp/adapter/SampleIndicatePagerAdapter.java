package com.atlas.crmapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atlas.crmapp.R;
import com.shizhefei.view.indicator.IndicatorViewPager;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/15
 *         Description :
 */

public class SampleIndicatePagerAdapter extends IndicatorViewPager.IndicatorViewPagerAdapter {

    private Context mContext;
    private int mLayoutId;

    public SampleIndicatePagerAdapter(Context context, int layoutId) {
        this.mContext = context;
        this.mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        return 3;
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
            convertView = LayoutInflater.from(mContext).inflate(mLayoutId, container, false);
        }
        return convertView;
    }
}
