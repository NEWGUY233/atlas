package com.atlas.crmapp.dagger.component;

import com.atlas.crmapp.dagger.module.RecordLoginActivityModule;
import com.atlas.crmapp.dagger.module.RegisterActivityModule;
import com.atlas.crmapp.dagger.scope.PerActivity;
import com.atlas.crmapp.register.RecordLoginActivity;
import com.atlas.crmapp.register.RegisterActivity;

import dagger.Component;

/**
 * Created by Administrator on 2018/3/20.
 */

@PerActivity
@Component(dependencies = AppComponent.class,modules = {RecordLoginActivityModule.class})
public interface RecordLoginActivityComponent {
    void inject(RecordLoginActivity fragment);
}
