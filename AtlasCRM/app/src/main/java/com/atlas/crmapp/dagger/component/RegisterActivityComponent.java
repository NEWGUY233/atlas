package com.atlas.crmapp.dagger.component;

import com.atlas.crmapp.activity.index.fragment.index.IndexFragment;
import com.atlas.crmapp.dagger.module.RegisterActivityModule;
import com.atlas.crmapp.dagger.module.index.IndexFragmentModule;
import com.atlas.crmapp.dagger.scope.PerActivity;
import com.atlas.crmapp.register.RegisterActivity;

import dagger.Component;

/**
 * Created by Administrator on 2018/3/20.
 */

@PerActivity
@Component(dependencies = AppComponent.class,modules = {RegisterActivityModule.class})
public interface RegisterActivityComponent {
    void inject(RegisterActivity fragment);
}
