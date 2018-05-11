package com.atlas.crmapp.dagger.component;

import com.atlas.crmapp.dagger.module.RecordLoginActivityModule;
import com.atlas.crmapp.dagger.module.RegInfoActivity_Module;
import com.atlas.crmapp.dagger.scope.PerActivity;
import com.atlas.crmapp.register.RecordLoginActivity;
import com.atlas.crmapp.register.RegInfoActivity_;

import dagger.Component;

/**
 * Created by Administrator on 2018/3/20.
 */

@PerActivity
@Component(modules = {RegInfoActivity_Module.class})
public interface RegInfoActivity_Component {
    void inject(RegInfoActivity_ fragment);
}
