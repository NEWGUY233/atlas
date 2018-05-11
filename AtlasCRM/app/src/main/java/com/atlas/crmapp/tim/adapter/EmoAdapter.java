package com.atlas.crmapp.tim.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2018/5/2.
 */

public class EmoAdapter extends PagerAdapter {
    Context context;
    List<View> list;
    public EmoAdapter(Context context,List<View> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ?  0 : list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(list.get(position));
        return list.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return list.indexOf(object);
    }
}
