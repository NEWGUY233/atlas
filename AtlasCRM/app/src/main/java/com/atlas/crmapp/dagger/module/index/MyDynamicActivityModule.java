package com.atlas.crmapp.dagger.module.index;

import com.atlas.crmapp.activity.index.fragment.index.activity.TagDetailActivity;
import com.atlas.crmapp.dagger.scope.PerActivity;
import com.atlas.crmapp.presenter.MyDynamicActivityPresenter;
import com.atlas.crmapp.presenter.TagDetailActivityPresenter;
import com.atlas.crmapp.usercenter.MyDynamicActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/3/15.
 */

@Module
public class MyDynamicActivityModule {
    MyDynamicActivity fragment;
    public MyDynamicActivityModule(MyDynamicActivity fragment){
        this.fragment = fragment;
    }

    @Provides
    @PerActivity
    public MyDynamicActivityPresenter getPresenter(){
        return new MyDynamicActivityPresenter(fragment);
    }

//    @Provides
//    @PerActivity
//    public IndexFragment getFragment(){
//        return this.fragment;
//    }
}

