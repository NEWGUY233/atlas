package com.atlas.crmapp.activity.index.fragment.livingspace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.util.StringUtils;

/**
 * Created by Administrator on 2018/4/27.
 */

public class LivingNoneActivity extends BaseStatusActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living_none);
        initView();
    }

    String title = "";
    private void initView(){
        initToolbar();
        title = getIntent().getStringExtra("title");
        setTitle(title);

        ((TextView)findView(R.id.tv_name)).setText(getIntent().getStringExtra("biz"));

        findView(R.id.tv_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(title))
                    return;

//                if (title.equals(getString(R.string.index_living_space_food_kitchen))){
//
//                }else  if (title.equals(getString(R.string.index_living_space_food_coffe))){
//
//                }else  if (title.equals(getString(R.string.index_living_space_sport_fitness))){
//
//                }else  if (title.equals(getString(R.string.index_living_space_sport_golf))){
//
//                }

                toLivingAll(title);

            }
        });
    }

    private void toLivingAll(String title){
        Intent i = new Intent(this,LivingAllActivity.class);
        i.putExtra("title",title);
        startActivity(i);
        finish();
    }
}
