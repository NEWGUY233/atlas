package com.atlas.crmapp.coffee;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.ActionUriUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orhanobut.logger.Logger;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by hoda_fang on 2017/6/3.
 */

public class JcVideoPlayerAcitvity extends BaseActivity{
    private JCVideoPlayerStandard vVideo;
    private String videoUrl;
    private String imageUrl;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_jc_video_player);

        Intent intent = getIntent();
        if(intent!= null){
            videoUrl = intent.getStringExtra(ActionUriUtils.url);
            imageUrl = intent.getStringExtra(ActionUriUtils.imageUrl);
        }
        Logger.d("videoUrl----"+ videoUrl +"imageUrl---" + imageUrl);
        vVideo = (JCVideoPlayerStandard) findViewById(R.id.v_video);
        backButton = (ImageView) vVideo.findViewById(R.id.back);
        startPlayer();
    }

    private void startPlayer(){
        if(!TextUtils.isEmpty(imageUrl)){
            Glide.with(this).load(LoadImageUtils.loadMiddleImage(imageUrl)).apply(new RequestOptions().skipMemoryCache(true).centerCrop()).into(vVideo.thumbImageView);
        }
        if(!TextUtils.isEmpty(videoUrl)){
            vVideo.setUp(videoUrl, JCVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN, "");
            //vVideo.startVideo();
            vVideo.startButton.performClick();
        }
        if(backButton != null){
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JcVideoPlayerAcitvity.this.finish();
                }
            });
        }

    }


    //按返回键暂停
/*   @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        if(vVideo != null){
            vVideo.releaseAllVideos();
        }
    }


}
