package com.atlas.crmapp.dagger.component.chat;

import com.atlas.crmapp.activity.index.fragment.communication.ChatDynamicActivity;
import com.atlas.crmapp.activity.index.fragment.index.activity.TagDetailActivity;
import com.atlas.crmapp.dagger.module.chat.ChatDynamicActivityModule;
import com.atlas.crmapp.dagger.module.index.TagDetailActivityModule;
import com.atlas.crmapp.dagger.scope.PerActivity;

import dagger.Component;

/**
 * Created by Administrator on 2018/3/20.
 */

@PerActivity
@Component(modules = {ChatDynamicActivityModule.class})
public interface ChatDynamicActivityComponent {
    void inject(ChatDynamicActivity fragment);
}
