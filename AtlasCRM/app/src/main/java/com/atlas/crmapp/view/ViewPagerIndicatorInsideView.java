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
import com.atlas.crmapp.adapter.CoffeeDetailImagesAdapter;
import com.atlas.crmapp.model.SKUJson;
import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.DrawableBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hoda_fang on 2017/5/1.
 * 指示器在内部
 */

public class ViewPagerIndicatorInsideView extends LinearLayout{
    private Context context;

    @BindView(R.id.vp_imager)
    ViewPager vpImager;
    @BindView(R.id.v_indicator)
    FixedIndicatorView vIndicator;

    public ViewPagerIndicatorInsideView(Context context) {
        super(context);
        initViews(context);
    }

    public ViewPagerIndicatorInsideView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public ViewPagerIndicatorInsideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ViewPagerIndicatorInsideView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_viewpager_indicator_in_side, this, true);
        ButterKnife.bind(this);
        vIndicator.setScrollBar(new DrawableBar(context, R.drawable.selector_indicator_yellow));

    }

    // 咖啡详情页
    public void updateViews(List<SKUJson.Media> actRecommendList){
        IndicatorViewPager idcAct = new IndicatorViewPager(vIndicator, vpImager);
        IndicatorViewPager.IndicatorPagerAdapter adapterAct = new CoffeeDetailImagesAdapter(context, actRecommendList);
        idcAct.setAdapter(adapterAct);
        if(actRecommendList.size()<=1){
            vIndicator.setVisibility(GONE);
        }
    }


}
