package com.atlas.crmapp.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.util.SpUtil;

/**
 * Created by hoda on 2017/7/6.
 */

public class MainGuideDialog extends DialogFragment{

    private RelativeLayout rlStepOne, rlStepTwo;
    private TextView tvStepInfo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_main_guide, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rlStepOne = (RelativeLayout) view.findViewById(R.id.rl_step_one);
        rlStepTwo = (RelativeLayout) view.findViewById(R.id.rl_step_two);
        tvStepInfo = (TextView) view.findViewById(R.id.tv_step_info);
        view.findViewById(R.id.ll_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rlStepTwo.getVisibility() == View.VISIBLE){
                    SpUtil.putBoolean(getActivity(), Constants.SpKey.MAIN_GUIDE_LOOKED, true);
                    MainGuideDialog.this.dismiss();
                }
                rlStepOne.setVisibility(View.INVISIBLE);
                rlStepTwo.setVisibility(View.VISIBLE);
                tvStepInfo.setText(getActivity().getString(R.string.guide_step_two));
            }
        });
    }
}
