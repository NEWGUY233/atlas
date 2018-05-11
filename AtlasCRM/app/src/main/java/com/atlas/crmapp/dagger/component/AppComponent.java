package com.atlas.crmapp.dagger.component;

import android.content.Context;

import com.atlas.crmapp.dagger.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2018/3/14.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    Context getContext();
}
