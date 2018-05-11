

package com.atlas.crmapp.huanxin;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.atlas.crmapp.Atlas;
import com.atlas.crmapp.common.GlobalParams;
import com.hyphenate.easeui.ui.EaseBaseActivity;

@SuppressLint("Registered")
public class BaseActivity extends EaseBaseActivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }

    public GlobalParams getGlobalParams() {
        return ((Atlas)getApplication()).getGlobalParams();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // umeng
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // umeng
//        MobclickAgent.onPause(this);
    }

}
