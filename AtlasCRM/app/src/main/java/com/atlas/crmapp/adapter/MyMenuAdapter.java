package com.atlas.crmapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.MyMenu;

/**
 * Created by Alex on 2017/3/10.
 */

public class MyMenuAdapter extends MyBaseAdapter<MyMenu> {

    public MyMenuAdapter(Context context) {
        super(context);
    }

    @Override
    public void notifyDataSetChanged() {
        String group = "";
        int size = mDataList.size();
        for(int i = 0; i < size; i++){
            MyMenu item = mDataList.get(i);
            if(item.getGroup().equals(group)){
                item.setShowGroup(false);
            }
            else {
                group = item.getGroup();
                item.setShowGroup(true);
            }
        }

        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_mymenu, null);
            holder.txtGroup = (TextView)convertView.findViewById(R.id.txtGroup);
            holder.imgIcon = (ImageView)convertView.findViewById(R.id.imgIcon);
            holder.txtName = (TextView)convertView.findViewById(R.id.txtName);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try{

            if(mDataList!=null){
                final MyMenu item = mDataList.get(position);

                holder.txtName.setText(item.getName());

                if(item.getIcon()!=-1){
                    holder.imgIcon.setImageResource(item.getIcon());
                    holder.imgIcon.setVisibility(View.VISIBLE);

                }else{
                    holder.imgIcon.setVisibility(View.GONE);
                }

                if(item.isShowGroup()){
                    holder.txtGroup.setVisibility(View.VISIBLE);
                }
                else{
                    holder.txtGroup.setVisibility(View.GONE);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private class ViewHolder {
        TextView txtGroup;
        ImageView imgIcon;
        TextView txtName;
    }
}
