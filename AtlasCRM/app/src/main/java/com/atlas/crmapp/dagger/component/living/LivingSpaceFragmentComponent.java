package com.atlas.crmapp.dagger.component.living;

import com.atlas.crmapp.activity.index.fragment.index.IndexFragment;
import com.atlas.crmapp.activity.index.fragment.livingspace.LivingSpaceFragment;
import com.atlas.crmapp.dagger.component.AppComponent;
import com.atlas.crmapp.dagger.module.index.IndexFragmentModule;
import com.atlas.crmapp.dagger.module.living.LivingSpaceFragmentModule;
import com.atlas.crmapp.dagger.scope.PerActivity;

import dagger.Component;

/**
 * Created by Administrator on 2018/3/20.
 */

@PerActivity
@Component(dependencies = AppComponent.class,modules = {LivingSpaceFragmentModule.class})
public interface LivingSpaceFragmentComponent {
    void inject(LivingSpaceFragment fragment);
}
