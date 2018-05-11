package com.atlas.crmapp.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.widget.RecycleViewListViewDivider;
import com.liaoinstan.springview.widget.SpringView;

/**
 * Created by hoda on 2017/7/25.
 */

public class GetCommonObjectUtils {

    /**
     * 获取 RecyclerView 背景分割线
     * @param context
     * @param recyclerView
     * @return
     */

    public static View getRvBgDivideItem(Context context, RecyclerView recyclerView){
        return  LayoutInflater.from(context).inflate(R.layout.view_top_bg, recyclerView ,false);
    }

    /**
     * 获取 recyclerView 分割线
     * @param context
     * @return
     */
    public static RecycleViewListViewDivider getRvItemDecoration(Context context){
        return  new RecycleViewListViewDivider(context, LinearLayout.HORIZONTAL, UiUtil.dipToPx(context,1 ), Color.parseColor("#ebebeb"));
    }


    /**
     *
     * 加载 完成时  推迟取消小时 上下拉 控件
     * @param springView
     */
    public static void onFinishFreshAndLoad(final SpringView springView){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(springView != null)
                    springView.onFinishFreshAndLoad();
            }
        }, Constants.ToRefreshTime.REFRESH_TIME);
    }

}
