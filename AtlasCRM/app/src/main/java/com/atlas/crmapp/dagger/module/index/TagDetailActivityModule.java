package com.atlas.crmapp.dagger.module.index;

import com.atlas.crmapp.activity.index.fragment.index.activity.TagDetailActivity;
import com.atlas.crmapp.activity.index.fragment.index.activity.fragment.AATagFragment;
import com.atlas.crmapp.dagger.scope.PerActivity;
import com.atlas.crmapp.presenter.AATagFragmentPresenter;
import com.atlas.crmapp.presenter.TagDetailActivityPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/3/15.
 */

@Module
public class TagDetailActivityModule {
    TagDetailActivity fragment;
    public TagDetailActivityModule(TagDetailActivity fragment){
        this.fragment = fragment;
    }

    @Provides
    @PerActivity
    public TagDetailActivityPresenter getTagDetailActivityPresenter(){
        return new TagDetailActivityPresenter(fragment);
    }

//    @Provides
//    @PerActivity
//    public IndexFragment getFragment(){
//        return this.fragment;
//    }
}

