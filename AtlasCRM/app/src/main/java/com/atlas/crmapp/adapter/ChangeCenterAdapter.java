package com.atlas.crmapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.model.CityCenterJson;
import com.atlas.crmapp.util.UiUtil;
import com.atlas.crmapp.view.FlowLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by hoda on 2017/9/23.
 */

public class ChangeCenterAdapter extends BaseQuickAdapter<CityCenterJson, BaseViewHolder> {
    private MarginLayoutParams lp;
    private Context context;
    private List<CityCenterJson> data;
    private int textUnSelectColor;
    private int textPaddingTop , textPaddingLeft;
    private FlowLayout.OnLabelSelectedListener onLabelSelectedListener;
    private long currentUnit;


    public ChangeCenterAdapter(Context context,  FlowLayout.OnLabelSelectedListener onLabelSelectedListener , List<CityCenterJson> data) {
        super(R.layout.item_change_center, data);
        this.context = context;
        this.data = data;
        this.onLabelSelectedListener = onLabelSelectedListener;
        currentUnit = GlobalParams.getInstance().getAtlasId();
    }

    @Override
    protected void convert(BaseViewHolder helper, CityCenterJson item) {
        helper.setText(R.id.tv_city, item.getCity().getName());
        FlowLayout flCenter = helper.getView(R.id.fl_center);
        if(lp == null){
            lp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.leftMargin = UiUtil.dipToPx(context, 8);
            lp.rightMargin = lp.leftMargin;
            lp.topMargin = UiUtil.dipToPx(context, 6);
            lp.bottomMargin = lp.topMargin;

            textPaddingTop = UiUtil.dipToPx(context , 5);
            textPaddingLeft = lp.rightMargin * 2;

        }

        if(textUnSelectColor == 0){
            textUnSelectColor = ContextCompat.getColor(context, R.color.gray_simple);
        }

        List<CityCenterJson.CentersBean> centersBeanList = item.getCenters();
        int centersBeanSize = centersBeanList.size();
        for (int i = 0 ; i < centersBeanSize; i ++) {
            CityCenterJson.CentersBean  centersBean = centersBeanList.get(i);
            TextView textView = getTextView(centersBean.getName(), item.getCity().getId() + "," + centersBean.getId(), textUnSelectColor);
            flCenter.addView(textView, lp);
        }
        if(onLabelSelectedListener != null){
            flCenter.setOnLabelSelectedListener(onLabelSelectedListener);
        }
    }

    public TextView getTextView(String name, String tag, int textColor){
        TextView view = new TextView(context);
        view.setText(name);
        view.setTextColor(textColor);
        view.setTag(tag);
        view.setPadding(textPaddingLeft, textPaddingTop, textPaddingLeft, textPaddingTop);
        // view.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_bg));
        return view;
    }


}
