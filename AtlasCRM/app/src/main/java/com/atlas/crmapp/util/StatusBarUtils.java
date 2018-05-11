package com.atlas.crmapp.util;

import android.app.Activity;

import com.atlas.crmapp.common.Constants;
import com.jaeger.library.StatusBarUtil;

/**
 * Created by hoda on 2017/10/24.
 */

public class StatusBarUtils {

    //半透明
    private void setTranslucentForImageView(Activity activity){
        StatusBarUtil.setTranslucentForImageViewInFragment(activity, Constants.STATUS_BAR_ALPHA.BAR_ALPHA , null);
    }

    //透明
    private void setTransparentForImageView(Activity activity){
        StatusBarUtil.setTransparentForImageView(activity, null);
    }
}
