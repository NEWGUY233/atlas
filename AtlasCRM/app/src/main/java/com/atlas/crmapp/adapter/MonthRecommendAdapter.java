package com.atlas.crmapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.ActionUriUtils;
import com.atlas.crmapp.util.GlideUtils;
import com.bumptech.glide.Glide;
import com.shizhefei.view.indicator.IndicatorViewPager;

import java.util.List;

/**
 * Created by macbook on 2017/3/21.
 */

public class MonthRecommendAdapter extends IndicatorViewPager.IndicatorViewPagerAdapter {

    private Context mContext;
    private List<ResourceJson.ResourceMedia> monthRecommendList;
    private String mBizCode;
    private long mUnitId;
    //private MyClickListener mListener;

    public MonthRecommendAdapter(Context context, String bizCode, long unitId, List<ResourceJson.ResourceMedia> monthRecommendList) {
        this.mContext = context;
        this.mBizCode = bizCode;
        this.mUnitId = unitId;
        this.monthRecommendList = monthRecommendList;
    }

    @Override
    public int getCount() {
        return  (int)(Math.ceil((double)monthRecommendList.size()/2));
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_recomm_monthly_coffee_fragment, container, false);
            ImageView leftImageView = (ImageView) convertView.findViewById(R.id.leftImageView);
            ImageView rightImageView = (ImageView) convertView.findViewById(R.id.rightImageView);
            TextView leftTextView = (TextView) convertView.findViewById(R.id.leftTextView);
            TextView rightTextView = (TextView) convertView.findViewById(R.id.rightTextView);
            View rightCardView = convertView.findViewById(R.id.rightCardView);
            View leftCardView = convertView.findViewById(R.id.leftCardView);

            final int realPosition = position*2;
            final ResourceJson.ResourceMedia media = monthRecommendList.get(realPosition);
            leftTextView.setText(media.content);
            GlideUtils.loadCustomImageView(mContext, R.drawable.ic_product_thum, LoadImageUtils.loadMiddleImage(media.url), leftImageView);
            if(realPosition+1<monthRecommendList.size()) {
                final ResourceJson.ResourceMedia media2 = monthRecommendList.get(realPosition + 1);
                rightTextView.setText(media2.content);
                GlideUtils.loadCustomImageView(mContext, R.drawable.ic_product_thum, LoadImageUtils.loadMiddleImage(media2.url),rightImageView);
                rightCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = ActionUriUtils.getIntent(mContext, media2.actionUri, LoadImageUtils.loadMiddleImage(media2.url), media2.content);
                        if(intent!=null){
                            intent.putExtra("unitId", mUnitId);
                            intent.putExtra("bizCode", mBizCode);
                            mContext.startActivity(intent);
                        }

                    }
                });
            }else{
                rightCardView.setVisibility(View.INVISIBLE);
            }

            convertView.setTag(position);

            leftCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = ActionUriUtils.getIntent(mContext, media, Constants.LoadImageType.MJ);
                        if(intent!=null){
                            intent.putExtra("unitId", mUnitId);
                            intent.putExtra("bizCode", mBizCode);
                            mContext.startActivity(intent);
                        }
                    }
                });
        }
        return convertView;
    }

    public static abstract class MyClickListener implements View.OnClickListener {

         public void onClick(View v) {
             myOnClick((Integer) v.getTag(), v);
         }
         public abstract void myOnClick(int position, View v);
     }
}
