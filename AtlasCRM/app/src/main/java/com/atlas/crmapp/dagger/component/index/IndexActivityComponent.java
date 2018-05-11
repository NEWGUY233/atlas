package com.atlas.crmapp.dagger.component.index;

import com.atlas.crmapp.activity.index.IndexActivity;
import com.atlas.crmapp.dagger.component.AppComponent;
import com.atlas.crmapp.dagger.module.index.IndexActivityModule;
import com.atlas.crmapp.dagger.scope.PerActivity;

import dagger.Component;

/**
 * Created by Administrator on 2018/3/15.
 */

@PerActivity
@Component(dependencies = AppComponent.class,modules = {IndexActivityModule.class})
public interface IndexActivityComponent {
    void inject(IndexActivity activity);
}
