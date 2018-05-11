package com.atlas.crmapp.dagger.module.chat;

import com.atlas.crmapp.activity.index.fragment.communication.ChatDynamicActivity;
import com.atlas.crmapp.activity.index.fragment.index.activity.TagDetailActivity;
import com.atlas.crmapp.dagger.scope.PerActivity;
import com.atlas.crmapp.presenter.ChatDynamicActivityPresenter;
import com.atlas.crmapp.presenter.TagDetailActivityPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/3/15.
 */

@Module
public class ChatDynamicActivityModule {
    ChatDynamicActivity fragment;
    public ChatDynamicActivityModule(ChatDynamicActivity fragment){
        this.fragment = fragment;
    }

    @Provides
    @PerActivity
    public ChatDynamicActivityPresenter getPresenter(){
        return new ChatDynamicActivityPresenter(fragment);
    }

//    @Provides
//    @PerActivity
//    public IndexFragment getFragment(){
//        return this.fragment;
//    }
}

