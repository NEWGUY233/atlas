package com.atlas.crmapp.selector.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.selector.entry.Folder;
import com.atlas.crmapp.selector.entry.Image;
import com.atlas.crmapp.selector.views.ImageSelectorActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Leo on 2018/3/20.
 */

public class SelectorAdapter extends RecyclerView.Adapter<SelectorAdapter.Holder> {
    private ArrayList<Image> images;
    Context c;
    public SelectorAdapter(Context c){
        this.c = c;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(c).inflate(R.layout.selector_item_image,parent,false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Image image = images.get(position);
        Glide.with(c).load(image.getPath()).apply(new RequestOptions().centerCrop().dontAnimate()).into(holder.imageView);
        holder.click.setText("");
        holder.click.setBackgroundResource(R.mipmap.icon_choseimage_unsel);
    }

    @Override
    public int getItemCount() {
        return images == null ? 0 : images.size();
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    public void setClick(OnClick click) {
        this.click = click;
    }

    protected class Holder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ImageView imageView_;
        TextView click;
        public Holder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_img);
            imageView_ = (ImageView) itemView.findViewById(R.id.iv_img_);
            click = (TextView) itemView.findViewById(R.id.click);
        }
    }

    private OnClick click;
    public interface OnClick{
        void onClick();
    }
}
