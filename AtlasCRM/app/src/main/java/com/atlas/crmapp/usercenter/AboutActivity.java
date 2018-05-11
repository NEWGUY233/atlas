package com.atlas.crmapp.usercenter;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @OnClick(R.id.ibBack)
    void onBack() {
        this.finish();
    }

    @BindView(R.id.iv_about_atlas)
    ImageView ivAboutAtlas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ButterKnife.bind(this);
        textViewTitle.setText(R.string.t47);

    }
}
