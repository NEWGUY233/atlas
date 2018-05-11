package com.atlas.crmapp.activity.index.fragment.index.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.activity.index.fragment.index.activity.PreviewPhotosActivity;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.GlideUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/16.
 */

public class IndexRecyclerPicAdapter extends RecyclerView.Adapter<IndexRecyclerPicAdapter.Holder>{
    Context c;
    List<String> list;
    public IndexRecyclerPicAdapter(Context c,List<String> list){
        this.c = c;
        this.list = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IndexRecyclerPicAdapter.Holder(LayoutInflater.from(c).inflate(R.layout.item_index_pic_item,parent,false));
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        if (list.size() != 1){
            int width = (int) ((c.getResources().getDisplayMetrics().widthPixels - c.getResources().getDisplayMetrics().density * 40) /3);
            holder.iv_pic.getLayoutParams().width = width;
            holder.iv_pic.getLayoutParams().height = width;
        }
//        GlideUtils.loadImageView(c,list.get(position),holder.iv_pic);
        if (list.get(position).startsWith("http"))
            Glide.with(c).load(LoadImageUtils.loadSmallImage(list.get(position))).apply(new RequestOptions().centerCrop()).into(holder.iv_pic);
        else
            Glide.with(c).load(list.get(position)).apply(new RequestOptions().centerCrop()).into(holder.iv_pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)c).startActivityForResult(new Intent(c, PreviewPhotosActivity.class)
                                .putExtra("position",position)
                                 .putExtra("type","just_show")
                                .putStringArrayListExtra("images", (ArrayList<String>) list)
                        ,0x112);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageView iv_pic;
        public Holder(View itemView) {
            super(itemView);
            iv_pic = (ImageView) itemView.findViewById(R.id.iv_pic);
        }
    }
}
