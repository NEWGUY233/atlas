package com.atlas.crmapp.fitness;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.CoachJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hoda on 2017/11/13.
 */

public class PersonalCoachDetailActivity extends BaseStatusActivity {

    @BindView(R.id.iv_coach_header)
    ImageView ivCoachHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_aptitude)
    TextView tvAptitude;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @OnClick(R.id.back)
    void onBack(){
        this.finish();
    }


    private CoachJson coachJson;
    private final static String KEY_COACH = "coach";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_coach_detail);

        if(getIntent() != null){
            coachJson = (CoachJson) getIntent().getSerializableExtra(KEY_COACH);
        }

        if(coachJson != null){
            updateActivityViews();
        }
    }

    @Override
    protected boolean translucentStatusBar() {
        return true;
    }

    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }

    @Override
    protected void updateActivityViews() {
        super.updateActivityViews();
        StatusBarUtil.setTranslucentForImageView(this, Constants.STATUS_BAR_ALPHA.BAR_ALPHA , null);
        tvName.setText(coachJson.name);
        tvAptitude.setText(coachJson.aptitude);
        tvDescription.setText(coachJson.description);
        Glide.with(this).load(LoadImageUtils.middleWebP(coachJson.thumbnail)).apply(new RequestOptions()
                .placeholder(R.drawable.bg_top_coach)).into(ivCoachHeader);
    }

    public static void newInstance(Context context, CoachJson coachJson){
        Intent intent = new Intent(context, PersonalCoachDetailActivity.class);
        intent.putExtra(KEY_COACH, coachJson);
        context.startActivity(intent);
    }

}
