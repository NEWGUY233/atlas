package com.atlas.crmapp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.MonthRecommendAdapter;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.ActionUriUtils;
import com.atlas.crmapp.util.GlideUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.DrawableBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hoda_fang on 2017/5/1.
 * 指示器再底部
 */

public class ViewPagerIndicatorOnBottomView_ extends LinearLayout implements ViewPager.OnPageChangeListener {
    private Context context;

    @BindView(R.id.vp_imager)
    ViewPager mViewpagerRecommMontyly;
    @BindView(R.id.banner_tv_title)
    TextView title;
    @BindView(R.id.v_indicator)
    FixedIndicatorView mIndicatorRecommMonthly;
    @BindView(R.id.iv_play)
    View iv_play;

    public ViewPagerIndicatorOnBottomView_(Context context) {
        super(context);
        initViews(context);
    }

    public ViewPagerIndicatorOnBottomView_(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public ViewPagerIndicatorOnBottomView_(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ViewPagerIndicatorOnBottomView_(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }



    private void initViews(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_viewpager_indicator_in_side_, this, true);
        ButterKnife.bind(this);
        mIndicatorRecommMonthly.setScrollBar(new DrawableBar(context, R.drawable.selector_indicator_yellow));
        mViewpagerRecommMontyly.addOnPageChangeListener(this);
    }


    List<String> rmList;
    public void updateViews(List<String> rmList){
        this.rmList = rmList;
        IndicatorViewPager idcMonthly = new IndicatorViewPager(mIndicatorRecommMonthly, mViewpagerRecommMontyly);
        IndicatorViewPager.IndicatorPagerAdapter adapterMonthly = new Adapter();
        idcMonthly.setAdapter(adapterMonthly);
    }

    List<ResourceJson.ResourceMedia> mList;
    public void updateViews(ArrayList<ResourceJson.ResourceMedia> mList){
        this.mList  = mList;
        rmList = new ArrayList<>();
        for (ResourceJson.ResourceMedia bean : mList)
            rmList.add(bean.url);
        IndicatorViewPager idcMonthly = new IndicatorViewPager(mIndicatorRecommMonthly, mViewpagerRecommMontyly);
        IndicatorViewPager.IndicatorPagerAdapter adapterMonthly = new Adapter();
        idcMonthly.setAdapter(adapterMonthly);
        if (mList != null && mList.size() > 0) {
            title.setVisibility(VISIBLE);
            title.setText(mList.get(0).content);

            if ("VIDEO".equals(mList.get(0).actionType)){
                iv_play.setVisibility(VISIBLE);
            }else {
                iv_play.setVisibility(GONE);
            }
        }

    }

    public ViewPager getViewPager(){
        return this.mViewpagerRecommMontyly;
    }

    public int getSize(){
        return rmList == null ? -1 : rmList.size();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mList != null && mList.size() > 0) {
            title.setVisibility(VISIBLE);
            title.setText(mList.get(position).content);

            if ("VIDEO".equals(mList.get(position).actionType)){
                iv_play.setVisibility(VISIBLE);
            }else {
                iv_play.setVisibility(GONE);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class Adapter extends IndicatorViewPager.IndicatorViewPagerAdapter{
        public Adapter(){

        }

        @Override
        public int getCount() {
            return rmList == null ? 0 : rmList.size();
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.tab_guide, container, false);
            }
            return convertView;
        }

        @Override
        public View getViewForPage(final int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = new ImageView(context);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                convertView.setLayoutParams(lp);
                ((ImageView)convertView).setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
//            GlideUtils.loadImageView(context,rmList.get(position), (ImageView) convertView);
            Glide.with(context).load(LoadImageUtils.loadMiddleImage(rmList.get(position))).apply(new RequestOptions().centerCrop().placeholder(R.drawable.product)).into((ImageView) convertView);
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ResourceJson.ResourceMedia bean = mList.get(position);
                    Intent intent = ActionUriUtils.getIntent(getContext(), bean.actionUri, LoadImageUtils.loadMiddleImage(bean.url), bean.content);
                    if(intent!=null){
                        intent.putExtra("unitId", GlobalParams.getInstance().getAtlasId());
                        intent.putExtra("bizCode", "banner");
                        getContext().startActivity(intent);
                    }
                }
            });
            return convertView;
        }
    }

    private CountDownTimer timer = new CountDownTimer(600 * 1000, 5000) {

        @Override
        public void onTick(long millisUntilFinished) {
            moveToNet();
        }

        @Override
        public void onFinish() {
            timer.start();
        }
    };

    private void moveToNet(){
        if (getSize() == -1 || getSize() == 0 || getSize() == 1) {
            timer.cancel();
            return;
        }
        int position = mViewpagerRecommMontyly.getCurrentItem() + 1;
        if (position >= getSize()){
            position = 0;
        }

        mViewpagerRecommMontyly.setCurrentItem(position);
        title.setText(mList.get(position).content);
    }

    public void cancel(){
        if (timer != null)
            timer.cancel();
    }

    public void startTimer(){
        if (getSize() == -1 || getSize() == 0 || getSize() == 1 || getViewPager().getAdapter() == null) {
            return;
        }
        timer.start();
    }
}
