package com.atlas.crmapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.data.ImageData;
import com.orhanobut.logger.Logger;

import java.util.List;

import github.hellocsl.cursorwheel.CursorWheelLayout;

/**
 * Created by Alex on 2017/3/9.
 */

public class WheelImageAdapter extends CursorWheelLayout.CycleWheelAdapter {
    private List<ImageData> mMenuItemDatas;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public WheelImageAdapter(Context context, List<ImageData> menuItemDatas) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mMenuItemDatas = menuItemDatas;
    }

    @Override
    public int getCount() {
        return mMenuItemDatas == null ? 0 : mMenuItemDatas.size();
    }

    @Override
    public View getView(View parent, int position) {
        ImageData item = getItem(position);
        View root = mLayoutInflater.inflate(R.layout.wheel_image_item, null, false);
        ImageView imgView = (ImageView) root.findViewById(R.id.wheel_menu_item_iv);
        imgView.setImageResource(item.mImageRes);//item.mImageRes
        Logger.d("position :"  + position);
        return root;
    }

    @Override
    public ImageData getItem(int position) {
        return mMenuItemDatas.get(position);
    }



}
