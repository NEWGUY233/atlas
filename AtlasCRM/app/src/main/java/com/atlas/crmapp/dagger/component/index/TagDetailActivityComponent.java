package com.atlas.crmapp.dagger.component.index;

import com.atlas.crmapp.activity.index.fragment.index.activity.TagDetailActivity;
import com.atlas.crmapp.activity.index.fragment.index.activity.fragment.AATagFragment;
import com.atlas.crmapp.dagger.module.index.AATagFragmentModule;
import com.atlas.crmapp.dagger.module.index.TagDetailActivityModule;
import com.atlas.crmapp.dagger.scope.PerActivity;

import dagger.Component;

/**
 * Created by Administrator on 2018/3/20.
 */

@PerActivity
@Component(modules = {TagDetailActivityModule.class})
public interface TagDetailActivityComponent {
    void inject(TagDetailActivity fragment);
}
