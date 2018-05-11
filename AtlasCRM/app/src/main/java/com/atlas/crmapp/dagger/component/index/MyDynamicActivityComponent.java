package com.atlas.crmapp.dagger.component.index;

import com.atlas.crmapp.activity.index.fragment.index.activity.TagDetailActivity;
import com.atlas.crmapp.dagger.module.index.MyDynamicActivityModule;
import com.atlas.crmapp.dagger.module.index.TagDetailActivityModule;
import com.atlas.crmapp.dagger.scope.PerActivity;
import com.atlas.crmapp.usercenter.MyDynamicActivity;

import dagger.Component;

/**
 * Created by Administrator on 2018/3/20.
 */

@PerActivity
@Component(modules = {MyDynamicActivityModule.class})
public interface MyDynamicActivityComponent {
    void inject(MyDynamicActivity fragment);
}
