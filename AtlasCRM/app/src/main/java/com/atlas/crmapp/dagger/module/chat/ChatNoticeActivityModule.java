package com.atlas.crmapp.dagger.module.chat;

import com.atlas.crmapp.activity.index.fragment.communication.ChatDynamicActivity;
import com.atlas.crmapp.activity.index.fragment.communication.ChatNoticeActivity;
import com.atlas.crmapp.dagger.scope.PerActivity;
import com.atlas.crmapp.presenter.ChatDynamicActivityPresenter;
import com.atlas.crmapp.presenter.ChatNoticeActivityPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/3/15.
 */

@Module
public class ChatNoticeActivityModule {
    ChatNoticeActivity fragment;
    public ChatNoticeActivityModule(ChatNoticeActivity fragment){
        this.fragment = fragment;
    }

    @Provides
    @PerActivity
    public ChatNoticeActivityPresenter getPresenter(){
        return new ChatNoticeActivityPresenter(fragment);
    }

//    @Provides
//    @PerActivity
//    public IndexFragment getFragment(){
//        return this.fragment;
//    }
}

