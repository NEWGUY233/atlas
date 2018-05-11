package com.atlas.crmapp.usercenter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.ChangeCenterAdapter;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.CenterUnitJson;
import com.atlas.crmapp.model.CityCenterJson;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.ClickUtil;
import com.atlas.crmapp.util.GetCommonObjectUtils;
import com.atlas.crmapp.util.KProgressHUDUtils;
import com.atlas.crmapp.util.RegisterCommonUtils;
import com.atlas.crmapp.util.SpUtil;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.view.FlowLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ChangeCityCenterActivity extends BaseStatusActivity {

    @BindView(R.id.rv_change_center)
    RecyclerView rvChangeCenter;

    private List<CityCenterJson> centerListJsons;
    private ChangeCenterAdapter centerAdapter ;
    private KProgressHUD dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_city_center);
        setTitle(getString(R.string.change_center));
        prepareActivityData();

    }



    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
        BizDataRequest.requestCityCenterList(this, statusLayout, new BizDataRequest.OnResponseGetCenterList() {
            @Override
            public void onSuccess(List<CityCenterJson> centerListJsons) {
                ChangeCityCenterActivity.this.centerListJsons = centerListJsons;
                updateActivityViews();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    @Override
    protected void updateActivityViews() {
        super.updateActivityViews();
        if(centerListJsons != null && centerListJsons.size() > 0){
            rvChangeCenter.setLayoutManager(new LinearLayoutManager(this));
            rvChangeCenter.addItemDecoration(GetCommonObjectUtils.getRvItemDecoration(this));
            if(centerAdapter == null){
                centerAdapter = new ChangeCenterAdapter(this, onLabelSelectedListener, centerListJsons);
            }

            rvChangeCenter.setAdapter(centerAdapter);
            View headerView =  LayoutInflater.from(this).inflate(R.layout.view_change_city_center_header, rvChangeCenter ,false);
            TextView tvCurrentCenter = (TextView) headerView.findViewById(R.id.tv_current_center);
            tvCurrentCenter.setText(GlobalParams.getInstance().getAtlasName());
            TextView tvCurrentCity = (TextView) headerView.findViewById(R.id.tv_current_city);
            String cityName = SpUtil.getGson(this, Constants.SpKey.KEY_CENTER_INFO, CenterUnitJson.class).cityName;
            if(StringUtils.isEmpty(cityName)){
                cityName = "广州";
            }
            tvCurrentCity.setText(cityName);
            centerAdapter.addHeaderView(headerView );

        }


    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);

        prepareActivityData();
    }

    FlowLayout.OnLabelSelectedListener onLabelSelectedListener = new FlowLayout.OnLabelSelectedListener() {
        @Override
        public void onSelected(int position, TextView selectTextView) {
            String tag = (String) selectTextView.getTag();
            Logger.d("select center info " + tag);
            if(StringUtils.isNotEmpty(tag)){
                CenterUnitJson selectCenterUnitJson = new CenterUnitJson();
                String tags[] = tag.split(",");

                int cityId = Integer.parseInt(tags[0]);
                int centerId = Integer.parseInt(tags[1]); ;
                for(CityCenterJson centerUnitJson : centerListJsons){
                    if(centerUnitJson.getCity().getId() == cityId){
                        List<CityCenterJson.CentersBean> centersBeens = centerUnitJson.getCenters();
                        for(CityCenterJson.CentersBean centersBeen : centersBeens){
                             if(centersBeen.getId() == centerId){
                                 selectCenterUnitJson.id = centersBeen.getId();
                                 selectCenterUnitJson.cityName = centerUnitJson.getCity().getName();
                                 selectCenterUnitJson.keyfree = centersBeen.isKeyfree();
                                 selectCenterUnitJson.name = centersBeen.getName();
                                 break;
                             }
                        }
                        break;
                    }
                }

                if(tags.length == 2){

                    if(selectCenterUnitJson.id == GlobalParams.getInstance().getAtlasId()){
                        ChangeCityCenterActivity.this.finish();
                    }
                    if(ClickUtil.isFastDoubleClick(3000)){
                        return;
                    }
                    dialog = KProgressHUDUtils.getKProgressHUD(ChangeCityCenterActivity.this);
                    dialog.setCancellable(false);
                    dialog.show();
                    RegisterCommonUtils.initUnits(ChangeCityCenterActivity.this, getGlobalParams(), selectCenterUnitJson, false, new RegisterCommonUtils.OnDownLoadResImageListener() {
                        @Override
                        public void needDownLoadImage(ArrayList<ResourceJson.ResourceMedia> resList) {
                            downloadResImage(resList);
                        }
                    }, null);
                }
            }
        }
    };


    private int needDownloadImage = 0;
    private void downloadResImage(List<ResourceJson.ResourceMedia> resList)  {
//        needDownloadImage += resList.size();
//        for (int i=0; i< resList.size(); i++) {
//            final ResourceJson.ResourceMedia res = resList.get(i);
//            glideLoadImage(res.url);
//        }
        finish();
    }

    //加载主页面 图片
    private void glideLoadImage(final String realUrl){
        Glide.with(getApplicationContext())
                .load(realUrl)
//                .asBitmap()
                .apply(new RequestOptions()
                .dontAnimate()
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(new SimpleTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(Drawable resource) {
//
//                    }

                    @Override
                    public void onLoadFailed( Drawable errorDrawable) {
                        super.onLoadFailed( errorDrawable);
                        glideLoadImage(realUrl);
                        String msg = "";
//                        if(e!= null){
//                            msg =e.getMessage();
//                        }
//                        Logger.e("needDownloadImage---fail---  "  + realUrl + " ,needDownloadImage:" +needDownloadImage +"  e.getMessage()  is "  + msg);
                    }

                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        needDownloadImage--;
                        if (needDownloadImage == 0) {
                            onImagesDownloadComplete();
                        }
                        Logger.d("needDownloadImage---success-- "  + realUrl+ " ,needDownloadImage:" +needDownloadImage);
                    }
                });
    }

    private void onImagesDownloadComplete(){
        this.finish();
    }
}
