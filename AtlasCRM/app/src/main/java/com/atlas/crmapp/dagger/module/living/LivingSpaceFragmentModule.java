package com.atlas.crmapp.dagger.module.living;

import com.atlas.crmapp.activity.index.fragment.index.IndexFragment;
import com.atlas.crmapp.activity.index.fragment.livingspace.LivingSpaceFragment;
import com.atlas.crmapp.dagger.scope.PerActivity;
import com.atlas.crmapp.presenter.IndexFragmentPresenter;
import com.atlas.crmapp.presenter.LivingSpaceFragmentPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/3/15.
 */

@Module
public class LivingSpaceFragmentModule {
    LivingSpaceFragment fragment;
    public LivingSpaceFragmentModule(LivingSpaceFragment fragment){
        this.fragment = fragment;
    }

    @Provides
    @PerActivity
    public LivingSpaceFragmentPresenter getIndexFragmentPresenter(){
        return new LivingSpaceFragmentPresenter(fragment);
    }

//    @Provides
//    @PerActivity
//    public IndexFragment getFragment(){
//        return this.fragment;
//    }
}

