package com.atlas.crmapp.activity.index.fragment.livingspace;

import android.content.Intent;
import android.os.Bundle;

import com.atlas.crmapp.R;
import com.atlas.crmapp.coffee.fragment.CoffeeFragment;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.fitness.FitnessFragment;

/**
 * Created by Leo on 2018/4/3.
 */

public class LivingAllActivity extends BaseStatusActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living_all);
        initView();
    }

    private void initView(){
        initToolbar();
        initTitle();
    }

    private void initTitle(){
        Intent i = getIntent();
        String title = i.getStringExtra("title");
        setTitle(title);
        if (title.equals(getString(R.string.index_living_space_food_kitchen))){
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout,new CoffeeFragment()).commit();

        }else if (title.equals(getString(R.string.index_living_space_food_coffe))){

            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout,new CoffeeFragment()).commit();

        }else if (title.equals(getString(R.string.index_living_space_sport_fitness))){
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout,new FitnessFragment()).commit();

        }else if (title.equals(getString(R.string.index_living_space_sport_golf))){
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout,new CoffeeFragment()).commit();

        }else {

        }


    }
}
