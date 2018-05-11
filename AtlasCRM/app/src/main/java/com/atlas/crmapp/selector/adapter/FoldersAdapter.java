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
import com.atlas.crmapp.activity.index.fragment.index.activity.TagCentreActivity;
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

public class FoldersAdapter extends RecyclerView.Adapter<FoldersAdapter.Holder> {
    private ArrayList<Folder> mFolders;
    Context c;
    public FoldersAdapter(Context c){
        this.c = c;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(c).inflate(R.layout.selector_item_folders,parent,false));
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final Folder folder = mFolders.get(position);
        ArrayList<Image> images = folder.getImages();
        holder.name.setText(folder.getName());
        if (images != null && !images.isEmpty()) {
            holder.number.setText("（" + images.size() + "）");
            Glide.with(c).load(new File(images.get(0).getPath()))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).dontAnimate().centerCrop())
                    .into(holder.imageView);
        } else {
            holder.number.setText("（0）");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click != null)
                    click.onClick(folder,position,v);
//                c.startActivity(new Intent(c, ImageSelectorActivity.class).putExtra("folder",folder));
            }

        });

    }

    @Override
    public int getItemCount() {
        return mFolders == null ? 0 : mFolders.size();
    }

    public ArrayList<Folder> getFolders() {
        return mFolders;
    }

    public void setFolders(ArrayList<Folder> mFolders) {
        this.mFolders = mFolders;
        notifyDataSetChanged();
    }

    public void setClick(ItemClick click) {
        this.click = click;
    }


    protected class Holder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name;
        TextView number;
        public Holder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            number = (TextView) itemView.findViewById(R.id.number);
        }
    }

    private ItemClick click;
    public interface ItemClick{
        void onClick(final Folder folder,int position,View view);
    }

}
