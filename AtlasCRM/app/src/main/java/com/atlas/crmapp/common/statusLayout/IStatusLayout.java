package com.atlas.crmapp.common.statusLayout;

import android.view.View;

import com.atlas.crmapp.network.DcnException;

/**
 * Created by hoda on 2017/7/2.
 */

public interface IStatusLayout {

    View getStatusContenView();

    void showLoading();

    void showLoading(String msg);

    void showError(DcnException error);

    void showContent(int delayedTime);

    void showContent();

    void showEmpty();

    boolean showStatusLayout();


}
