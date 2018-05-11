package com.atlas.crmapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.liaoinstan.springview.container.BaseHeader;

/**
 * Created by hoda on 2017/7/6.
 */

public class RefreshFootView extends BaseHeader{
    private TextView tvRefreshFoot;
    private Context context;
    private View vSpin;

    public RefreshFootView(Context context){
        this.context = context;

    }
    @Override
    public View getView(LayoutInflater inflater, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.view_refresh_foot, viewGroup, true);
        tvRefreshFoot = (TextView) view.findViewById(R.id.tv_refresh_foot);
        vSpin = view.findViewById(R.id.v_spin);
        return view;
    }

    @Override
    public void onPreDrag(View rootView) {

    }

    @Override
    public void onDropAnim(View rootView, int dy) {

    }

    @Override
    public void onLimitDes(View rootView, boolean upORdown) {

    }

    @Override
    public void onStartAnim() {

    }

    @Override
    public void onFinishAnim() {

    }

    public void setLoadingFinish(){
        vSpin.setVisibility(View.GONE);
        tvRefreshFoot.setText(context.getString(R.string.not_more));
    }
}
