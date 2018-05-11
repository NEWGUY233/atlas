package com.atlas.crmapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.liaoinstan.springview.container.BaseHeader;

/**
 * Created by hoda on 2017/7/6.
 */

public class RefreshHeaderView extends BaseHeader{
    private ImageView ivLoadingHeader;
    private Context context;

    public RefreshHeaderView(Context context){
        this.context = context;

    }
    @Override
    public View getView(LayoutInflater inflater, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.view_refresh_header, viewGroup, true);
        /*ivLoadingHeader = (ImageView) view.findViewById(R.id.iv_loading_header);
        AnimationDrawable animationDrawable = (AnimationDrawable) ivLoadingHeader.getDrawable();
        animationDrawable.start();*/
        return view;
    }

    @Override
    public void onPreDrag(View rootView) {

    }

    @Override
    public void onDropAnim(View rootView, int dy) {

    }

    @Override
    public void onLimitDes(View rootView, boolean upORdown) {

    }

    @Override
    public void onStartAnim() {

    }

    @Override
    public void onFinishAnim() {

    }
}
