package com.atlas.crmapp.dagger.module.index;

import com.atlas.crmapp.activity.index.fragment.index.IndexFragment;
import com.atlas.crmapp.dagger.scope.PerActivity;
import com.atlas.crmapp.presenter.IndexFragmentPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/3/15.
 */

@Module
public class IndexFragmentModule {
    IndexFragment fragment;
    public IndexFragmentModule(IndexFragment fragment){
        this.fragment = fragment;
    }

    @Provides
    @PerActivity
    public IndexFragmentPresenter getIndexFragmentPresenter(){
        return new IndexFragmentPresenter(fragment);
    }

//    @Provides
//    @PerActivity
//    public IndexFragment getFragment(){
//        return this.fragment;
//    }
}

