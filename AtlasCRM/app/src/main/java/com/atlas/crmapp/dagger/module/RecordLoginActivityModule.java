package com.atlas.crmapp.dagger.module;

import com.atlas.crmapp.dagger.scope.PerActivity;
import com.atlas.crmapp.presenter.RecordLoginActivityPresenter;
import com.atlas.crmapp.presenter.RegisterActivityPresenter;
import com.atlas.crmapp.register.RecordLoginActivity;
import com.atlas.crmapp.register.RegisterActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/3/15.
 */

@Module
public class RecordLoginActivityModule {
    RecordLoginActivity fragment;
    public RecordLoginActivityModule(RecordLoginActivity fragment){
        this.fragment = fragment;
    }

    @Provides
    @PerActivity
    public RecordLoginActivityPresenter getRecordLoginActivityPresenter(){
        return new RecordLoginActivityPresenter(fragment);
    }

}

