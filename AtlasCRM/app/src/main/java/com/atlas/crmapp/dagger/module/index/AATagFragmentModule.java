package com.atlas.crmapp.dagger.module.index;

import com.atlas.crmapp.activity.index.fragment.index.IndexFragment;
import com.atlas.crmapp.activity.index.fragment.index.activity.fragment.AATagFragment;
import com.atlas.crmapp.dagger.scope.PerActivity;
import com.atlas.crmapp.presenter.AATagFragmentPresenter;
import com.atlas.crmapp.presenter.IndexFragmentPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/3/15.
 */

@Module
public class AATagFragmentModule {
    AATagFragment fragment;
    public AATagFragmentModule(AATagFragment fragment){
        this.fragment = fragment;
    }

    @Provides
    @PerActivity
    public AATagFragmentPresenter getAATagFragmentPresenter(){
        return new AATagFragmentPresenter(fragment);
    }

//    @Provides
//    @PerActivity
//    public IndexFragment getFragment(){
//        return this.fragment;
//    }
}

