package com.atlas.crmapp.dagger.module;

import com.atlas.crmapp.dagger.scope.PerActivity;
import com.atlas.crmapp.presenter.RecordLoginActivityPresenter;
import com.atlas.crmapp.presenter.RegInfoActivity_Presenter;
import com.atlas.crmapp.register.RecordLoginActivity;
import com.atlas.crmapp.register.RegInfoActivity_;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/3/15.
 */

@Module
public class RegInfoActivity_Module {
    RegInfoActivity_ fragment;
    public RegInfoActivity_Module(RegInfoActivity_ fragment){
        this.fragment = fragment;
    }

    @Provides
    @PerActivity
    public RegInfoActivity_Presenter getRegInfoActivity_Presenter(){
        return new RegInfoActivity_Presenter(fragment);
    }

}

