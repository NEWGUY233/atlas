package com.atlas.crmapp.activity.index.fragment.index.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.index.activity.PreviewPhotosActivity;
import com.atlas.crmapp.activity.index.fragment.index.activity.TagDetailActivity;
import com.atlas.crmapp.selector.entry.Image;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.view.SquareImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 2018/3/20.
 */

public class PostNewsAdapter extends RecyclerView.Adapter<PostNewsAdapter.Holder> {
    Context c;
    private List<String> list;
    public PostNewsAdapter(Context c){
        this.c = c;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        SquareImageView imageView = new SquareImageView(c);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int f = (int) c.getResources().getDisplayMetrics().density;
        lp.setMargins(5*f,5*f,5*f,5*f);
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new Holder(imageView);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        if (list == null || (position == list.size() && list.size() != 9)) {
            holder.imageView.setImageResource(R.mipmap.icon_addimage);
            holder.imageView.setClickable(true);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (click != null)
                        click.onClick();
                }
            });
        }else {
//            holder.imageView.setImageResource(R.color.theme_yellow);
//            GlideUtils.loadImageView(c,list.get(position),holder.imageView);
            Glide.with(c).load(list.get(position)).apply(new RequestOptions().dontAnimate()).into(holder.imageView);
            holder.imageView.setClickable(false);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity)c).startActivityForResult(new Intent(c, PreviewPhotosActivity.class)
                            .putExtra("position",position)
                            .putStringArrayListExtra("images", (ArrayList<String>) getList())
                            ,0x112);
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return list == null ? 1 : list.size()>= 9 ? 9 : list.size() + 1;
    }

    public List<String> getList() {
        if (this.list == null)
            return new ArrayList<>();
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setList(ArrayList<Image> list){
        if (this.list == null)
            this.list = new ArrayList<>();
        for (Image image : list)
            this.list.add(image.getPath());
        notifyDataSetChanged();
    }

    public void setClick(OnAddClick click) {
        this.click = click;
    }

    protected class Holder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public Holder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView;
        }
    }

    private OnAddClick click;
    public interface OnAddClick{
        void onClick();
    }
}
