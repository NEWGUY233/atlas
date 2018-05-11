package com.atlas.crmapp.dagger.module.index;

import com.atlas.crmapp.activity.index.fragment.index.activity.TagCentreActivity;
import com.atlas.crmapp.activity.index.fragment.index.activity.TagDetailActivity;
import com.atlas.crmapp.dagger.scope.PerActivity;
import com.atlas.crmapp.presenter.TagCentreActivityPresenter;
import com.atlas.crmapp.presenter.TagDetailActivityPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/3/15.
 */

@Module
public class TagCentreActivityModule {
    TagCentreActivity fragment;
    public TagCentreActivityModule(TagCentreActivity fragment){
        this.fragment = fragment;
    }

    @Provides
    @PerActivity
    public TagCentreActivityPresenter getTagCentreActivityPresenter(){
        return new TagCentreActivityPresenter(fragment);
    }

//    @Provides
//    @PerActivity
//    public IndexFragment getFragment(){
//        return this.fragment;
//    }
}

