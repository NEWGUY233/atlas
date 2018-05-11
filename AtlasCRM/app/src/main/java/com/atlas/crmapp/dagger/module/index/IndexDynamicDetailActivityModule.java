package com.atlas.crmapp.dagger.module.index;

import com.atlas.crmapp.activity.index.fragment.index.activity.IndexDynamicDetailActivity;
import com.atlas.crmapp.activity.index.fragment.index.activity.TagDetailActivity;
import com.atlas.crmapp.dagger.scope.PerActivity;
import com.atlas.crmapp.presenter.IndexDynamicDetailActivityPresenter;
import com.atlas.crmapp.presenter.TagDetailActivityPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/3/15.
 */

@Module
public class IndexDynamicDetailActivityModule {
    IndexDynamicDetailActivity fragment;
    public IndexDynamicDetailActivityModule(IndexDynamicDetailActivity fragment){
        this.fragment = fragment;
    }

    @Provides
    @PerActivity
    public IndexDynamicDetailActivityPresenter getPresenter(){
        return new IndexDynamicDetailActivityPresenter(fragment);
    }

//    @Provides
//    @PerActivity
//    public IndexFragment getFragment(){
//        return this.fragment;
//    }
}

