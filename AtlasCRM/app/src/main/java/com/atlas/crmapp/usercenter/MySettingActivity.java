package com.atlas.crmapp.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.commonactivity.WebActivity;
import com.atlas.crmapp.util.AppUtil;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.RegisterCommonUtils;
import com.atlas.crmapp.util.StringUtils;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MySettingActivity extends BaseActivity {

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.btn_submit)
    Button btnLogout;
    @BindView(R.id.tv_language)
    TextView tvLanguage;
    @BindView(R.id.rl_language)
    RelativeLayout rlLanguage;

    @OnClick(R.id.ibBack)
    void onBack() {
        this.finish();
    }

    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_update_red_dot)
    View tvUpdateRedDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);

        ButterKnife.bind(this);
        textViewTitle.setText(R.string.t88);

        if (!getGlobalParams().isLogin()) {
            btnLogout.setVisibility(View.INVISIBLE);
        }
        tvVersion.setText(getString(R.string.t89) + AppUtil.getVersionName(this) + "." + AppUtil.getVersionCode(this));

        tvLanguage.setText(getString(R.string.language_text));
        rlLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MySettingActivity.this,SettingLanguageActivity.class),0x123);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String version = FormatCouponInfo.getOnBuglyUpdateVersion();
        if (StringUtils.isNotEmpty(version)) {
            tvUpdateRedDot.setVisibility(View.VISIBLE);
        } else {
            tvUpdateRedDot.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //退出登录
    public void logout(View v) {
        RegisterCommonUtils.logout(this);
    }


    public void updateApp(View view) {
        Beta.checkUpgrade();
        loadUpgradeInfo();
    }


    private void loadUpgradeInfo() {


        /***** 获取升级信息 *****/
        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();

        if (upgradeInfo == null) {
            return;
        }
        StringBuilder info = new StringBuilder();
        info.append(getString(R.string.info1)).append(upgradeInfo.id).append("\n");
        info.append(getString(R.string.info2)).append(upgradeInfo.title).append("\n");
        info.append(getString(R.string.info3)).append(upgradeInfo.newFeature).append("\n");
        info.append(getString(R.string.info4)).append(upgradeInfo.versionCode).append("\n");
        info.append(getString(R.string.info5)).append(upgradeInfo.versionName).append("\n");
        info.append(getString(R.string.info6)).append(upgradeInfo.publishTime).append("\n");
        info.append(getString(R.string.info7)).append(upgradeInfo.apkMd5).append("\n");
        info.append(getString(R.string.info8)).append(upgradeInfo.apkUrl).append("\n");
        info.append(getString(R.string.info9)).append(upgradeInfo.fileSize).append("\n");
        info.append(getString(R.string.info10)).append(upgradeInfo.popInterval).append("\n");
        info.append(getString(R.string.info11)).append(upgradeInfo.popTimes).append("\n");
        info.append(getString(R.string.info12)).append(upgradeInfo.publishType).append("\n");
        info.append(getString(R.string.info13)).append(upgradeInfo.upgradeType).append("\n");
        info.append(getString(R.string.info14)).append(upgradeInfo.imageUrl);
        Logger.d(info.toString());

    }


    //意见反馈
    public void feedback(View v) {
        Intent intent = new Intent(MySettingActivity.this, MyOpinionActivity.class);
        MySettingActivity.this.startActivity(intent);
    }

    //关于
    public void about(View v) {
        WebActivity.newInstance(this, Constants.CUSTOM_URL.ABOUT_ATLAS, getString(R.string.about_atlas), true);
        /*Intent intent = new Intent(MySettingActivity.this, AboutActivity.class);
        MySettingActivity.this.startActivity(intent);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x123 && resultCode == 0x123)
            finish();
    }
}
