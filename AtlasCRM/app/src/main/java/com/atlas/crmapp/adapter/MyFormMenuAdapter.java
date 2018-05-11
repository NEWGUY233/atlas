package com.atlas.crmapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.MyFormMenuModel;
import com.atlas.crmapp.network.LoadImageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by Alex on 2017/3/20.
 */

public class MyFormMenuAdapter extends MyBaseAdapter<MyFormMenuModel> {

    public MyFormMenuAdapter(Context context) {
        super(context);
    }

    @Override
    public void notifyDataSetChanged() {
        String group = "";
        int size = mDataList.size();
        for(int i = 0; i < size; i++){
            MyFormMenuModel item = mDataList.get(i);
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
        MyFormMenuAdapter.ViewHolder holder = null;
        if (convertView == null) {
            holder = new MyFormMenuAdapter.ViewHolder();
            convertView = mInflater.inflate(R.layout.item_my_form_menu, null);
            holder.txtGroup = (TextView)convertView.findViewById(R.id.txtGroup);
            holder.txtName = (TextView)convertView.findViewById(R.id.txtName);
            holder.txtContent= (TextView)convertView.findViewById(R.id.txtContent);
            holder.imageViewLogo=(ImageView)convertView.findViewById(R.id.imageViewLogo);
            convertView.setTag(holder);

        } else {
            holder = (MyFormMenuAdapter.ViewHolder) convertView.getTag();
        }
        try{

            if(mDataList!=null){
                final MyFormMenuModel item = mDataList.get(position);
                if(item.getType()==1){
                    holder.imageViewLogo.setVisibility(View.GONE);
                    holder.txtContent.setVisibility(View.VISIBLE);
                }else{
                    holder.txtContent.setVisibility(View.GONE);
                    holder.imageViewLogo.setVisibility(View.VISIBLE);

                    String url;
                    if (item.getLogo() != null && item.getLogo().length()>0){
                        url = LoadImageUtils.loadSmallImage(item.getLogo());
                        Glide.with(mContext).load(LoadImageUtils.loadSmallImage(url)).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_user_default).error(R.drawable.ic_user_default)).into(holder.imageViewLogo);
                    }

                    //File file = new File(url);

                }
                holder.txtName.setText(item.getName());
                holder.txtContent.setText(item.getContent());
                if(item.isShowGroup()){
                    holder.txtGroup.setVisibility(View.VISIBLE);
                }
                else{
                    holder.txtGroup.setVisibility(View.GONE);
                }

                if(position==0){
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
        TextView txtName;
        TextView txtContent;
        ImageView imageViewLogo;
    }
}