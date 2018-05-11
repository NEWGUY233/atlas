package com.atlas.crmapp.adapter;

import android.view.View;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.VisitCentersBean;
import com.atlas.crmapp.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by hoda on 2017/12/21.
 */

public class SelectVisitCenterAdapter extends BaseQuickAdapter<VisitCentersBean, BaseViewHolder> {
    public SelectVisitCenterAdapter(List<VisitCentersBean> data) {
        super(R.layout.item_select_visit_center, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, VisitCentersBean item) {
        helper.setText(R.id.tv_center_name, item.getName() );
        if(StringUtils.isNotEmpty(item.getCtiyName())){
            TextView tvCity = helper.getView(R.id.tv_city);
            tvCity.setText(item.getCtiyName());
            helper.getView(R.id.tv_city).setVisibility(View.VISIBLE);
        }else{
            helper.getView(R.id.tv_city).setVisibility(View.GONE);
        }

        helper.addOnClickListener(R.id.tv_center_name);
        helper.getView(R.id.v_line).setVisibility( item.isShowLine() ? View.VISIBLE :View.INVISIBLE);
        helper.getView(R.id.iv_select).setVisibility(item.isSelected() ? View.VISIBLE :View.INVISIBLE);
    }
}
