package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.MonthRecommendAdapter;
import com.atlas.crmapp.model.ResourceJson;
import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.DrawableBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hoda_fang on 2017/5/1.
 * 指示器再底部
 */

public class ViewPagerIndicatorOnBottomView extends LinearLayout{
    private Context context;

    @BindView(R.id.viewpager_recomm_montyly)
    ViewPager mViewpagerRecommMontyly;
    @BindView(R.id.indicator_recomm_monthly)
    FixedIndicatorView mIndicatorRecommMonthly;

    public ViewPagerIndicatorOnBottomView(Context context) {
        super(context);
        initViews(context);
    }

    public ViewPagerIndicatorOnBottomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public ViewPagerIndicatorOnBottomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ViewPagerIndicatorOnBottomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }



    private void initViews(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_viewpager_indicator_on_bottom, this, true);
        ButterKnife.bind(this);
        mIndicatorRecommMonthly.setScrollBar(new DrawableBar(context, R.drawable.selector_indicator_yellow));

    }

    public void updateViews(List<ResourceJson.ResourceMedia> rmList, String bizCode, long unitId){
        IndicatorViewPager idcMonthly = new IndicatorViewPager(mIndicatorRecommMonthly, mViewpagerRecommMontyly);
        IndicatorViewPager.IndicatorPagerAdapter adapterMonthly = new MonthRecommendAdapter(context, bizCode, unitId, rmList);
        idcMonthly.setAdapter(adapterMonthly);
    }
}
