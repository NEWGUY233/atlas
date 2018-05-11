package com.atlas.crmapp.dagger.component.index;

import com.atlas.crmapp.activity.index.fragment.index.activity.IndexDynamicDetailActivity;
import com.atlas.crmapp.activity.index.fragment.index.activity.TagDetailActivity;
import com.atlas.crmapp.dagger.module.index.IndexDynamicDetailActivityModule;
import com.atlas.crmapp.dagger.module.index.TagDetailActivityModule;
import com.atlas.crmapp.dagger.scope.PerActivity;

import dagger.Component;

/**
 * Created by Administrator on 2018/3/20.
 */

@PerActivity
@Component(modules = {IndexDynamicDetailActivityModule.class})
public interface IndexDynamicDetailActivityComponent {
    void inject(IndexDynamicDetailActivity fragment);
}
