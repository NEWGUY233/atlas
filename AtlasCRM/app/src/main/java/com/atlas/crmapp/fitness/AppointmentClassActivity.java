package com.atlas.crmapp.fitness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;
import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.NavFragmentsActivity;
import com.atlas.crmapp.adapter.navadapter.AppointmentClassNavAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppointmentClassActivity extends NavFragmentsActivity {
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.tvText)
    TextView mTvAll;

    @OnClick(R.id.tvText)
    void onClickAll(){
        startActivity(new Intent(AppointmentClassActivity.this, AllCourseActivity.class));
    }

    @OnClick(R.id.ibBack)
    void onBack() {
        this.finish();
    }

    @Override
    protected FragmentNavigatorAdapter getNavAdapter() {
        return new AppointmentClassNavAdapter();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.appointment_class_fl_container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        umengPageTitle = getString(R.string.umeng_lesson_list);
        setContentView(R.layout.activity_appointment_class);
        ButterKnife.bind(this);
        textViewTitle.setText(R.string.index_top_class);
        mTvAll.setText(R.string.t19);
        mTvAll.setVisibility(View.VISIBLE);
        initView();
    }

    private void initView() {
        mFragmentNavigator.setDefaultPosition(0);
        mFragmentNavigator.showFragment(0);
    }

}
