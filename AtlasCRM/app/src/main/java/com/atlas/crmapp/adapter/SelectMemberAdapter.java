package com.atlas.crmapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;

import com.atlas.crmapp.model.bean.ItemSelectMember;

import java.util.List;

/**
 * Created by macbook on 2017/3/17.
 */

public class SelectMemberAdapter extends BaseAdapter {
    private  List<ItemSelectMember> itemSelectMembers;
    private Context context;
    public SelectMemberAdapter(Context context,List<ItemSelectMember> itemSelectMembers) {
       this.itemSelectMembers = itemSelectMembers;
        this.context = context;
    }

    @Override
    public int getCount() {
        return itemSelectMembers.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView =LayoutInflater.from(context).inflate(R.layout.item_select_member, null);
            holder.vLine = (View)convertView.findViewById(R.id.v_line);
            holder.v_LineShort = (View)convertView.findViewById(R.id.v_lineShort);
            holder.imgIcon = (ImageView)convertView.findViewById(R.id.iv_head);
            holder.txtName = (TextView)convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(position==0||position==itemSelectMembers.size()-1){
            holder.vLine.setVisibility(View.VISIBLE);
            holder.v_LineShort.setVisibility(View.GONE);
        }else {
            holder.vLine.setVisibility(View.GONE);
            holder.v_LineShort.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private class ViewHolder {
        View vLine,v_LineShort;
        ImageView imgIcon;
        TextView txtName;
    }
}
