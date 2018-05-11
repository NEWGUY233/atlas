package com.atlas.crmapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.commonadapter.BaseRvAdapter;
import com.atlas.crmapp.common.commonadapter.BaseRvViewHolder;
import com.atlas.crmapp.commonactivity.PhotoViewActivity;
import com.atlas.crmapp.model.DetailMediaJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import tangxiaolv.com.library.EffectiveShapeView;

/**
 * Created by Alex on 2017/4/18.
 */

public class ActImagesAdapter extends BaseRvAdapter<ActImagesAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<DetailMediaJson> allImages; //所有的图片
    private ArrayList<DetailMediaJson> galleryImages; //显示在画廊的图片

    public ActImagesAdapter(Context context,ArrayList<DetailMediaJson> dataList) {
        this.mContext = context;
        allImages = dataList;

        galleryImages = new ArrayList(dataList);
        galleryImages.remove(0);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_act_images;
    }

    @Override
    protected ActImagesAdapter.ViewHolder getViewHolder(View root, int viewType) {
        return new ActImagesAdapter.ViewHolder(root);
    }


    @Override
    public void onBindViewHolder(ActImagesAdapter.ViewHolder holder,final int position) {
        DetailMediaJson media = galleryImages.get(position);
        holder.ivLogo.setImageResource(R.drawable.ic_product_thum);

        if(!media.url.equals("")) {
            Glide.with(mContext).load(LoadImageUtils.loadSmallImage(media.url)).into(holder.ivLogo);
        }
        holder.ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PhotoViewActivity.class);
                intent.putExtra("currentPosition",position + 1);
                intent.putExtra("imagesList", (Serializable) allImages);
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return galleryImages.size();
    }

    public class ViewHolder extends BaseRvViewHolder {

        @BindView(R.id.iv_act_image)
        EffectiveShapeView ivLogo;

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }
}