package com.atlas.crmapp.common;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.atlas.crmapp.common.statusLayout.OnRetryListener;
import com.atlas.crmapp.common.statusLayout.StatusLayoutImpl;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hoda on 2017/8/9.
 */

public abstract class BaseStatusDialogFragment extends DialogFragment {
    public static final int NO_INSERT_STATUS = -1;
    protected StatusLayoutImpl statusLayout;

    protected abstract int setCreateView();
    private Unbinder unbinder;
    private View contentView;



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(true);
        statusLayout = new StatusLayoutImpl(getActivity(), setCreateView(), -1, onRetryListener, false);
        if(setCreateView() != NO_INSERT_STATUS){
            contentView = statusLayout.getStatusContenView();
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            contentView.setLayoutParams(params);
        }else{
            contentView = inflater.inflate(setCreateView(), container, false);
        }
        unbinder = ButterKnife.bind(this, contentView);
        return contentView;
    }

    OnRetryListener onRetryListener = new OnRetryListener() {
        @Override
        public void onRetry(View view, int id) {
            BaseStatusDialogFragment.this.onRefresh(view, id);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder != null){
            unbinder.unbind();
        }
    }

    //刷新调用方法
    protected void onRefresh(View view ,int id){

    }
}
