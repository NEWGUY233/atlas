package com.atlas.crmapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.bean.ItemCoffeeProduct;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.badgeview.BGABadgeFrameLayout;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/16
 *         Description :
 */

public class CoffeeCategoryListAdapter extends BaseAdapter {

    private List<ItemCoffeeProduct> mList;

    public CoffeeCategoryListAdapter() {
        mList = new ArrayList<>();
    }

    public void setCategory(List<ItemCoffeeProduct> data) {
        if (data == null) {
            return;
        }

        mList.clear();
        mList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        ItemCoffeeProduct item = mList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coffee_category, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTvName.setText(item.getCategoryName());

        if (item.getChildOrderCount() > 0) {
            holder.mBadgeView.showTextBadge(item.getChildOrderCount() + "");
        } else {
            holder.mBadgeView.hiddenBadge();
        }

        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.badge_view)
        BGABadgeFrameLayout mBadgeView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
