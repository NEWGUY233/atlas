package com.atlas.crmapp.dagger.component.index;

import com.atlas.crmapp.activity.index.fragment.index.IndexFragment;
import com.atlas.crmapp.activity.index.fragment.index.activity.fragment.AATagFragment;
import com.atlas.crmapp.dagger.component.AppComponent;
import com.atlas.crmapp.dagger.module.index.AATagFragmentModule;
import com.atlas.crmapp.dagger.module.index.IndexFragmentModule;
import com.atlas.crmapp.dagger.scope.PerActivity;

import dagger.Component;

/**
 * Created by Administrator on 2018/3/20.
 */

@PerActivity
@Component(modules = {AATagFragmentModule.class})
public interface AATagFragmentComponent {
    void inject(AATagFragment fragment);
}
