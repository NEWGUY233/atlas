package com.atlas.crmapp.dagger.module;

import android.content.Context;

import com.atlas.crmapp.dagger.scope.PerActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/3/14.
 */

@Module
public class AppModule {
    private Context context;
    public AppModule(Context context){
        this.context = context;
    }

    @Provides
    @Singleton
    public Context getContext(){
        return this.context;
    }


}
