package com.atlas.crmapp.dagger.module;

import com.atlas.crmapp.activity.index.fragment.index.IndexFragment;
import com.atlas.crmapp.dagger.scope.PerActivity;
import com.atlas.crmapp.presenter.IndexFragmentPresenter;
import com.atlas.crmapp.presenter.RegisterActivityPresenter;
import com.atlas.crmapp.register.RegisterActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/3/15.
 */

@Module
public class RegisterActivityModule {
    RegisterActivity fragment;
    public RegisterActivityModule(RegisterActivity fragment){
        this.fragment = fragment;
    }

    @Provides
    @PerActivity
    public RegisterActivityPresenter getIndexFragmentPresenter(){
        return new RegisterActivityPresenter(fragment);
    }

}

