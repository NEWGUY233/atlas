package com.atlas.crmapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.coffee.JcVideoPlayerAcitvity;
import com.atlas.crmapp.common.commonadapter.BaseRvAdapter;
import com.atlas.crmapp.common.commonadapter.BaseRvViewHolder;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.ActionUriUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/13
 *         Description :
 */

public class CoffeeBannerAdapter extends BaseRvAdapter<CoffeeBannerAdapter.ViewHolder>{
    private Context mContext;
    private List<ResourceJson.ResourceMedia> videoDataList;
    private String mBizCode;
    private long mUnitId;


    public CoffeeBannerAdapter(Context context, String bizCode, long unitId,  List<ResourceJson.ResourceMedia> videoDataList){
        this.mContext = context;
        this.mBizCode = bizCode;
        this.mUnitId = unitId;
        this.videoDataList = videoDataList;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_banner_coffee_fragment;
    }

    @Override
    protected ViewHolder getViewHolder(View root, int viewType) {
        return new ViewHolder(root);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final ResourceJson.ResourceMedia res = videoDataList.get(position%videoDataList.size());
        holder.tvTitle.setText(res.content);//
        if(res.actionUri!=null) {
            int index = res.actionUri.indexOf(":");
            final String type = res.actionUri.substring(0, index);
            final String url = res.actionUri.substring(index + 1, res.actionUri.length());


            if (type.equals("video")) {
                holder.ivBg.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(url)){
                    holder.ivPlay.setVisibility(View.VISIBLE);
                }else{
                    holder.ivPlay.setVisibility(View.GONE);
                }
                Glide.with(mContext).load(LoadImageUtils.loadMiddleImage(res.url)).apply(new RequestOptions().dontAnimate().placeholder(R.drawable.product_thum).skipMemoryCache(true).centerCrop()).into(holder.ivBg);
                holder.ivBg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(holder.ivPlay.getVisibility() ==View.VISIBLE){
                            Intent intent = new Intent(mContext , JcVideoPlayerAcitvity.class);
                            intent.putExtra(InParamter.imageUrl,res.url);
                            intent.putExtra(InParamter.videoUrl, url);
                            mContext.startActivity(intent);
                        }
                    }
                });
            } else {
                holder.ivPlay.setVisibility(View.GONE);
                holder.ivBg.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(LoadImageUtils.loadMiddleImage(res.url)).apply(new RequestOptions().skipMemoryCache(true).centerCrop()).into(holder.ivBg);
                holder.ivBg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(url)) {
                            Intent intent = ActionUriUtils.getIntent(mContext, res.actionUri, LoadImageUtils.loadMiddleImage(res.url), res.content);
                            if(intent!=null){
                                intent.putExtra("unitId", mUnitId);
                                intent.putExtra("bizCode", mBizCode);
                                mContext.startActivity(intent);
                            }
                        }
                    }
                });
            }
        }else{
            holder.ivPlay.setVisibility(View.GONE);
            holder.ivBg.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(LoadImageUtils.loadMiddleImage(res.url)).apply(new RequestOptions().skipMemoryCache(true).centerCrop()).into(holder.ivBg);
        }
    }



    @Override
    public int getItemCount() {

        return videoDataList.size()* 20;
    }

    public class ViewHolder extends BaseRvViewHolder {
        @BindView(R.id.banner_iv_bg)
        ImageView ivBg;
        @BindView(R.id.banner_tv_title)
        TextView tvTitle;

        @BindView(R.id.banner_iv_ic_play)
        ImageView ivPlay;

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }

    public interface InParamter{
        String videoUrl = ActionUriUtils.url;//type  String;
        String imageUrl = ActionUriUtils.imageUrl;//type  String;
    }
}
