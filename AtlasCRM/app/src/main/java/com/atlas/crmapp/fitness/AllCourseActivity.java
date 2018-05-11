package com.atlas.crmapp.fitness;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.FitnessAllCourseAdapter;
import com.atlas.crmapp.annotation.StateVariable;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.LessonJson;
import com.atlas.crmapp.model.LessonsListJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.UiUtil;
import com.atlas.crmapp.widget.RecycleViewListViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AllCourseActivity extends BaseStatusActivity {



    @BindView(R.id.rv_fitness_lesson)
    RecyclerView mRvView;

    @StateVariable
    List<LessonJson> lessons = new ArrayList<>();
    FitnessAllCourseAdapter adapter;

    private int pageIndex = 0;
    private int pageSize = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_course);
    }

    @Override
    protected void initActivityViews() {
        super.initActivityViews();
        setTitle(getString(R.string.t19));
        mRvView.setLayoutManager(new LinearLayoutManager(this));
        mRvView.addItemDecoration(new RecycleViewListViewDivider(this, LinearLayout.HORIZONTAL, UiUtil.dipToPx(this, 1), Color.parseColor("#ebebeb")));
        lessons = new ArrayList<>();
        adapter = new FitnessAllCourseAdapter(this, lessons);
        mRvView.setAdapter(adapter);
        prepareActivityData();
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareActivityData();
    }

    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
        BizDataRequest.requestGetLessons(this, pageIndex, pageSize, getGlobalParams().getFitnessId(), 0, Long.MAX_VALUE, false, statusLayout, new BizDataRequest.OnLessonsRequestResult() {
            @Override
            public void onSuccess(LessonsListJson lessonsJsons) {
                lessons.addAll(lessonsJsons.rows);
                adapter.notifyDataSetChanged();
                if(lessons.size() == 0){
                    statusLayout.showEmpty();
                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

}


