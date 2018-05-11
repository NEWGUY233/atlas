package com.atlas.crmapp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Alex on 2017/4/17.
 */

public class MyViewPagerAdapter extends PagerAdapter {
    private int[] allPicturePaths;
    private Context mContext;
    public MyViewPagerAdapter(Context context, int[] allPicturePaths) {
        mContext = context;
        this.allPicturePaths = allPicturePaths;
    }

    @Override
    public int getCount() {
        return allPicturePaths.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 必须要实现的方法
     * 每次滑动的时实例化一个页面,ViewPager同时加载3个页面,假如此时你正在第二个页面，向左滑动，
     * 将实例化第4个页面
     **/
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        // TODO Auto-generated method stub
        ImageView imageView = new ImageView(mContext);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView .setLayoutParams(layoutParams);
        ((ViewPager) container).addView(imageView);
        Glide.with(mContext)
                .load(allPicturePaths[position])
                .apply(new RequestOptions().centerCrop())
//                .crossFade()
                .into(imageView);
        return imageView;
    }

    /**
     * 必须要实现的方法
     * 滑动切换的时销毁一个页面，ViewPager同时加载3个页面,假如此时你正在第二个页面，向左滑动，
     * 将销毁第1个页面
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub;
        ImageView imageView = (PhotoView) object;
        if (imageView == null)
            return;
//        Glide.clear(imageView);     //核心，解决OOM
        ((ViewPager) container).removeView(imageView);
    }
}