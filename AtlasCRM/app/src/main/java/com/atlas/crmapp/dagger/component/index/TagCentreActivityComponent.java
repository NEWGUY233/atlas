package com.atlas.crmapp.dagger.component.index;

import com.atlas.crmapp.activity.index.fragment.index.activity.TagCentreActivity;
import com.atlas.crmapp.activity.index.fragment.index.activity.TagDetailActivity;
import com.atlas.crmapp.dagger.module.index.TagCentreActivityModule;
import com.atlas.crmapp.dagger.module.index.TagDetailActivityModule;
import com.atlas.crmapp.dagger.scope.PerActivity;

import dagger.Component;

/**
 * Created by Administrator on 2018/3/20.
 */

@PerActivity
@Component(modules = {TagCentreActivityModule.class})
public interface TagCentreActivityComponent {
    void inject(TagCentreActivity fragment);
}
