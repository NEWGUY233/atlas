package com.atlas.crmapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.DetailMediaJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
* @author: Hoda_fang
* @date: 2017/6/19 15:33
* @desc: 产品介绍
*/

public class ProductIntroduceView extends LinearLayout{
    private Context context;
    private TextView tvTitle;
    private TextView tvProdutInfo;
    private LinearLayout llImages;
    public ProductIntroduceView(Context context) {
        super(context);
        initViews(context);
    }

    public ProductIntroduceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public ProductIntroduceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProductIntroduceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        this.context =  context;
        LayoutInflater.from(context).inflate(R.layout.view_produt_introduce, this, true);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvProdutInfo = (TextView) findViewById(R.id.tv_coffer_detail);
        llImages = (LinearLayout) findViewById(R.id.ll_product);
    }

    public void updateVies(List<DetailMediaJson> mediaList, String title, String produtInfo ){
        for (DetailMediaJson medias : mediaList){
            ImageView view = new ImageView(context);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setAdjustViewBounds(true);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //params.setMargins(marginLeftAndRight,0,marginLeftAndRight,0);//4个参数按顺序分别是左上右下
            view.setLayoutParams(params);
            Glide.with(context).load(LoadImageUtils.loadMiddleImage(medias.url)).apply(new RequestOptions().placeholder(R.drawable.product_thum)).into(view);
            llImages.addView(view);
        }
        if(mediaList == null || mediaList.size() == 0){
            llImages.setVisibility(GONE);
        }
        setTitleAndProdutInfo(title, produtInfo);
    }

    private void setTitleAndProdutInfo(String title, String produtInfo){
        if(StringUtils.isNotEmpty(title)){
            tvTitle.setText(title);
        }
        if(StringUtils.isNotEmpty(produtInfo)){
            tvProdutInfo.setText(produtInfo);
        }else{
            tvProdutInfo.setVisibility(GONE);
        }
    }
}
