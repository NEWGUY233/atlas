package com.atlas.crmapp.dagger.component.chat;

import com.atlas.crmapp.activity.index.fragment.communication.ChatDynamicActivity;
import com.atlas.crmapp.activity.index.fragment.communication.ChatNoticeActivity;
import com.atlas.crmapp.dagger.module.chat.ChatDynamicActivityModule;
import com.atlas.crmapp.dagger.module.chat.ChatNoticeActivityModule;
import com.atlas.crmapp.dagger.scope.PerActivity;

import dagger.Component;

/**
 * Created by Administrator on 2018/3/20.
 */

@PerActivity
@Component(modules = {ChatNoticeActivityModule.class})
public interface ChatNoticeActivityComponent {
    void inject(ChatNoticeActivity fragment);
}
