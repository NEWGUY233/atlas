package com.atlas.crmapp.activity.index.fragment.index.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.network.LoadImageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shizhefei.view.indicator.IndicatorViewPager;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Alex on 2017/4/28.
 */

public class PreviewPhotosAdapter extends IndicatorViewPager.IndicatorViewPagerAdapter {
    public static final String TAG = PreviewPhotosAdapter.class.getSimpleName();
    private List<String> imageUrls;
    private AppCompatActivity activity;

    public PreviewPhotosAdapter(List<String> imageUrls, AppCompatActivity activity) {
        this.imageUrls = imageUrls;
        this.activity = activity;
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        String url = imageUrls.get(position);
//        PhotoView photoView = new PhotoView(activity);
//        Glide.with(activity)
//                .load(url).apply(new RequestOptions().placeholder(R.drawable.product))
//                .into(photoView);
//        container.addView(photoView);
//        photoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: ");
//                activity.finish();
//            }
//        });
//        return photoView;
//    }

    @Override
    public int getCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }

    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.tab_guide, container, false);
        }
        return convertView;
    }

    @Override
    public View getViewForPage(int position, View convertView, ViewGroup container) {
        String url = imageUrls.get(position);
        if (convertView == null)
            convertView = new PhotoView(activity);
        Glide.with(activity)
                .load(url).apply(new RequestOptions().placeholder(R.drawable.product))
                .into((ImageView) convertView);
//        container.addView(convertView);
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: ");
//                activity.finish();
//            }
//        });
        return convertView;
    }

}
